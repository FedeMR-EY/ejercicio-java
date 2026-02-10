package com.ey.ejercicio_java;

import com.ey.ejercicio_java.model.Phone;
import com.ey.ejercicio_java.model.User;
import com.ey.ejercicio_java.repository.PhoneRepository;
import com.ey.ejercicio_java.repository.UserRepository;
import com.ey.ejercicio_java.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(UserService.class)
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("save - debe guardar un usuario y asignarle un UUID")
    void save_shouldPersistUserAndGenerateId() {
        User user = new User();
        user.setName("Juan Perez");
        user.setEmail("juan@example.com");
        user.setPassword("password123");

        User savedUser = userService.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Juan Perez");
        assertThat(savedUser.getEmail()).isEqualTo("juan@example.com");
    }

    @Test
    @DisplayName("getAll - debe retornar todos los usuarios")
    void getAll_shouldReturnAllUsers() {
        User user1 = new User();
        user1.setName("Usuario 1");
        user1.setEmail("user1@example.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("Usuario 2");
        user2.setEmail("user2@example.com");
        userRepository.save(user2);

        List<User> users = userService.getAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getName)
                .containsExactlyInAnyOrder("Usuario 1", "Usuario 2");
    }

    @Test
    @DisplayName("getAll - debe retornar lista vacia cuando no hay usuarios")
    void getAll_shouldReturnEmptyListWhenNoUsers() {
        List<User> users = userService.getAll();

        assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("findById - debe encontrar usuario existente")
    void findById_shouldReturnUserWhenExists() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        User savedUser = userRepository.save(user);

        User foundUser = userService.findById(savedUser.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.getName()).isEqualTo("Test User");
    }

    @Test
    @DisplayName("findById - debe retornar null cuando usuario no existe")
    void findById_shouldReturnNullWhenNotExists() {
        User foundUser = userService.findById(java.util.UUID.randomUUID());

        assertThat(foundUser).isNull();
    }

    @Test
    @DisplayName("deleteById - debe eliminar usuario existente")
    void deleteById_shouldRemoveUser() {
        User user = new User();
        user.setName("To Delete");
        user.setEmail("delete@example.com");
        User savedUser = userRepository.save(user);

        userService.deleteById(savedUser.getId());

        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }

    @Test
    @DisplayName("save - debe actualizar usuario existente")
    void save_shouldUpdateExistingUser() {
        User user = new User();
        user.setName("Original Name");
        user.setEmail("original@example.com");
        User savedUser = userRepository.save(user);

        savedUser.setName("Updated Name");
        savedUser.setEmail("updated@example.com");
        User updatedUser = userService.save(savedUser);

        assertThat(updatedUser.getId()).isEqualTo(savedUser.getId());
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    @DisplayName("save - debe guardar usuario con telefonos (cascade persist)")
    void save_shouldPersistUserWithPhones() {
        User user = new User();
        user.setName("Usuario con Telefonos");
        user.setEmail("phones@example.com");

        Phone phone1 = new Phone();
        phone1.setNumber("1234567890");
        phone1.setCityCode(11);
        phone1.setCountryCode(54);
        phone1.setUser(user);

        Phone phone2 = new Phone();
        phone2.setNumber("0987654321");
        phone2.setCityCode(221);
        phone2.setCountryCode(54);
        phone2.setUser(user);

        user.setPhones(new ArrayList<>(List.of(phone1, phone2)));

        User savedUser = userService.save(user);

        assertThat(savedUser.getPhones()).hasSize(2);
        assertThat(phoneRepository.findAll()).hasSize(2);
        assertThat(savedUser.getPhones()).extracting(Phone::getNumber)
                .containsExactlyInAnyOrder("1234567890", "0987654321");
    }

    @Test
    @DisplayName("save - debe eliminar telefono huerfano (orphanRemoval)")
    void save_shouldRemoveOrphanPhone() {
        User user = new User();
        user.setName("Usuario Orphan Test");
        user.setEmail("orphan@example.com");

        Phone phone1 = new Phone();
        phone1.setNumber("111111");
        phone1.setCityCode(11);
        phone1.setCountryCode(54);
        phone1.setUser(user);

        Phone phone2 = new Phone();
        phone2.setNumber("222222");
        phone2.setCityCode(11);
        phone2.setCountryCode(54);
        phone2.setUser(user);

        user.setPhones(new ArrayList<>(List.of(phone1, phone2)));
        User savedUser = userRepository.saveAndFlush(user);

        savedUser.getPhones().removeIf(p -> p.getNumber().equals("111111"));
        userRepository.saveAndFlush(savedUser);

        assertThat(phoneRepository.findAll()).hasSize(1);
        assertThat(phoneRepository.findAll().get(0).getNumber()).isEqualTo("222222");
    }

    @Test
    @DisplayName("deleteById - debe eliminar usuario y sus telefonos (cascade delete)")
    void deleteById_shouldRemoveUserAndPhones() {
        User user = new User();
        user.setName("Usuario Cascade Delete");
        user.setEmail("cascade@example.com");

        Phone phone = new Phone();
        phone.setNumber("333333");
        phone.setCityCode(11);
        phone.setCountryCode(54);
        phone.setUser(user);

        user.setPhones(new ArrayList<>(List.of(phone)));
        User savedUser = userService.save(user);

        userService.deleteById(savedUser.getId());

        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
        assertThat(phoneRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("save - debe agregar telefono a usuario existente")
    void save_shouldAddPhoneToExistingUser() {
        User user = new User();
        user.setName("Usuario Add Phone");
        user.setEmail("addphone@example.com");
        user.setPhones(new ArrayList<>());
        User savedUser = userService.save(user);

        Phone newPhone = new Phone();
        newPhone.setNumber("444444");
        newPhone.setCityCode(11);
        newPhone.setCountryCode(54);
        newPhone.setUser(savedUser);
        savedUser.getPhones().add(newPhone);
        userService.save(savedUser);

        assertThat(phoneRepository.findAll()).hasSize(1);
        assertThat(phoneRepository.findAll().get(0).getNumber()).isEqualTo("444444");
    }
}
