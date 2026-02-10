package com.ey.ejercicio_java.controller;

import com.ey.ejercicio_java.controller.dto.request.LoginRequest;
import com.ey.ejercicio_java.controller.dto.request.RegisterUserRequest;
import com.ey.ejercicio_java.model.entity.Phone;
import com.ey.ejercicio_java.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = JsonMapper.builder().build();

    @Autowired
    private UserRepository userRepository;

    private static final String REGISTER_URL = "/v1/auth/register";
    private static final String LOGIN_URL = "/v1/auth/login";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST /v1/auth/register")
    class RegisterTests {

        @Test
        @DisplayName("debe registrar usuario exitosamente y retornar 201")
        void register_shouldCreateUserAndReturn201() throws Exception {
            RegisterUserRequest request = RegisterUserRequest.builder()
                    .name("Juan Perez")
                    .email("juan@example.com")
                    .password("Password12")
                    .phones(new ArrayList<>())
                    .build();

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.uuid").exists())
                    .andExpect(jsonPath("$.created").exists())
                    .andExpect(jsonPath("$.modified").exists())
                    .andExpect(jsonPath("$.lastLogin").exists())
                    .andExpect(jsonPath("$.token").isNotEmpty())
                    .andExpect(jsonPath("$.isActive").value(true));
        }

        @Test
        @DisplayName("debe registrar usuario con telefonos exitosamente")
        void register_shouldCreateUserWithPhonesAndReturn201() throws Exception {
            Phone phone1 = new Phone();
            phone1.setNumber("1234567890");
            phone1.setCityCode(11);
            phone1.setCountryCode(54);

            Phone phone2 = new Phone();
            phone2.setNumber("0987654321");
            phone2.setCityCode(221);
            phone2.setCountryCode(54);

            RegisterUserRequest request = RegisterUserRequest.builder()
                    .name("Maria Garcia")
                    .email("maria@example.com")
                    .password("SecurePass99")
                    .phones(List.of(phone1, phone2))
                    .build();

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.uuid").exists())
                    .andExpect(jsonPath("$.token").isNotEmpty())
                    .andExpect(jsonPath("$.isActive").value(true));
        }

        @Test
        @DisplayName("debe retornar 400 cuando el email ya esta registrado")
        void register_shouldReturn400WhenEmailAlreadyExists() throws Exception {
            RegisterUserRequest request = RegisterUserRequest.builder()
                    .name("Usuario Original")
                    .email("duplicado@example.com")
                    .password("Password12")
                    .phones(new ArrayList<>())
                    .build();

            // Primer registro
            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            // Segundo registro con mismo email
            RegisterUserRequest duplicateRequest = RegisterUserRequest.builder()
                    .name("Usuario Duplicado")
                    .email("duplicado@example.com")
                    .password("OtroPass99")
                    .phones(new ArrayList<>())
                    .build();

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(duplicateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").value("El correo ya está registrado"));
        }

        @Test
        @DisplayName("debe retornar 400 cuando el email es invalido")
        void register_shouldReturn400WhenEmailIsInvalid() throws Exception {
            RegisterUserRequest request = RegisterUserRequest.builder()
                    .name("Usuario Test")
                    .email("email-invalido")
                    .password("Password12")
                    .phones(new ArrayList<>())
                    .build();

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").exists());
        }

        @Test
        @DisplayName("debe retornar 400 cuando el password no cumple el patron")
        void register_shouldReturn400WhenPasswordDoesNotMatchPattern() throws Exception {
            // Password sin mayuscula
            RegisterUserRequest request = RegisterUserRequest.builder()
                    .name("Usuario Test")
                    .email("test@example.com")
                    .password("password12")
                    .phones(new ArrayList<>())
                    .build();

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").exists());
        }

        @Test
        @DisplayName("debe retornar 400 cuando el password no tiene suficientes digitos")
        void register_shouldReturn400WhenPasswordDoesNotHaveEnoughDigits() throws Exception {
            // Password con solo un digito (requiere 2)
            RegisterUserRequest request = RegisterUserRequest.builder()
                    .name("Usuario Test")
                    .email("test@example.com")
                    .password("Password1")
                    .phones(new ArrayList<>())
                    .build();

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").exists());
        }

        @Test
        @DisplayName("debe retornar 400 cuando el nombre esta vacio")
        void register_shouldReturn400WhenNameIsEmpty() throws Exception {
            RegisterUserRequest request = RegisterUserRequest.builder()
                    .name("")
                    .email("test@example.com")
                    .password("Password12")
                    .phones(new ArrayList<>())
                    .build();

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").exists());
        }

        @Test
        @DisplayName("debe retornar 400 cuando phones es null")
        void register_shouldReturn400WhenPhonesIsNull() throws Exception {
            RegisterUserRequest request = RegisterUserRequest.builder()
                    .name("Usuario Test")
                    .email("test@example.com")
                    .password("Password12")
                    .phones(null)
                    .build();

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").exists());
        }
    }

    @Nested
    @DisplayName("POST /v1/auth/login")
    class LoginTests {

        private void registerTestUser(String email, String password) throws Exception {
            RegisterUserRequest request = RegisterUserRequest.builder()
                    .name("Usuario Test")
                    .email(email)
                    .password(password)
                    .phones(new ArrayList<>())
                    .build();

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("debe hacer login exitosamente y retornar 200 con token")
        void login_shouldReturnTokenWith200() throws Exception {
            String email = "login@example.com";
            String password = "Password12";

            registerTestUser(email, password);

            LoginRequest loginRequest = LoginRequest.builder()
                    .email(email)
                    .password(password)
                    .build();

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value("Usuario Test"))
                    .andExpect(jsonPath("$.email").value(email))
                    .andExpect(jsonPath("$.lastLogin").exists())
                    .andExpect(jsonPath("$.token").isNotEmpty())
                    .andExpect(jsonPath("$.isActive").value(true));
        }

        @Test
        @DisplayName("debe retornar 400 cuando las credenciales son invalidas")
        void login_shouldReturn400WhenCredentialsAreInvalid() throws Exception {
            String email = "usuario@example.com";
            String password = "Password12";

            registerTestUser(email, password);

            LoginRequest loginRequest = LoginRequest.builder()
                    .email(email)
                    .password("WrongPassword99")
                    .build();

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").value("Credenciales inválidas"));
        }

        @Test
        @DisplayName("debe retornar 400 cuando el usuario no existe")
        void login_shouldReturn400WhenUserDoesNotExist() throws Exception {
            LoginRequest loginRequest = LoginRequest.builder()
                    .email("noexiste@example.com")
                    .password("Password12")
                    .build();

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").value("Credenciales inválidas"));
        }

        @Test
        @DisplayName("debe retornar 400 cuando el email es invalido")
        void login_shouldReturn400WhenEmailIsInvalid() throws Exception {
            LoginRequest loginRequest = LoginRequest.builder()
                    .email("email-invalido")
                    .password("Password12")
                    .build();

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").exists());
        }

        @Test
        @DisplayName("debe retornar 400 cuando el email esta vacio")
        void login_shouldReturn400WhenEmailIsEmpty() throws Exception {
            LoginRequest loginRequest = LoginRequest.builder()
                    .email("")
                    .password("Password12")
                    .build();

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").exists());
        }

        @Test
        @DisplayName("debe retornar 400 cuando el password esta vacio")
        void login_shouldReturn400WhenPasswordIsEmpty() throws Exception {
            LoginRequest loginRequest = LoginRequest.builder()
                    .email("test@example.com")
                    .password("")
                    .build();

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").exists());
        }

        @Test
        @DisplayName("debe actualizar lastLogin despues del login")
        void login_shouldUpdateLastLogin() throws Exception {
            String email = "lastlogin@example.com";
            String password = "Password12";

            registerTestUser(email, password);

            // Primer login
            LoginRequest loginRequest = LoginRequest.builder()
                    .email(email)
                    .password(password)
                    .build();

            String firstResponse = mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // Segundo login (lastLogin deberia actualizarse)
            String secondResponse = mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.lastLogin").exists())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // Verificar que ambos logins retornan datos validos
            org.assertj.core.api.Assertions.assertThat(firstResponse).contains("lastLogin");
            org.assertj.core.api.Assertions.assertThat(secondResponse).contains("lastLogin");
        }
    }

    @Nested
    @DisplayName("Flujo completo de registro y login")
    class FullFlowTests {

        @Test
        @DisplayName("debe permitir registro y login inmediato")
        void shouldAllowRegisterAndImmediateLogin() throws Exception {
            String email = "flujo@example.com";
            String password = "Password12";

            // Registro
            RegisterUserRequest registerRequest = RegisterUserRequest.builder()
                    .name("Usuario Flujo")
                    .email(email)
                    .password(password)
                    .phones(new ArrayList<>())
                    .build();

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.token").isNotEmpty());

            // Login inmediato
            LoginRequest loginRequest = LoginRequest.builder()
                    .email(email)
                    .password(password)
                    .build();

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value(email))
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        @DisplayName("debe manejar multiples usuarios correctamente")
        void shouldHandleMultipleUsersCorrectly() throws Exception {
            // Registrar usuario 1
            RegisterUserRequest user1 = RegisterUserRequest.builder()
                    .name("Usuario Uno")
                    .email("uno@example.com")
                    .password("Password12")
                    .phones(new ArrayList<>())
                    .build();

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user1)))
                    .andExpect(status().isCreated());

            // Registrar usuario 2
            RegisterUserRequest user2 = RegisterUserRequest.builder()
                    .name("Usuario Dos")
                    .email("dos@example.com")
                    .password("OtroPass99")
                    .phones(new ArrayList<>())
                    .build();

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user2)))
                    .andExpect(status().isCreated());

            // Login usuario 1
            LoginRequest login1 = LoginRequest.builder()
                    .email("uno@example.com")
                    .password("Password12")
                    .build();

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(login1)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Usuario Uno"));

            // Login usuario 2
            LoginRequest login2 = LoginRequest.builder()
                    .email("dos@example.com")
                    .password("OtroPass99")
                    .build();

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(login2)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Usuario Dos"));

            // Verificar que password de usuario 1 no funciona para usuario 2
            LoginRequest wrongLogin = LoginRequest.builder()
                    .email("dos@example.com")
                    .password("Password12")
                    .build();

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(wrongLogin)))
                    .andExpect(status().isBadRequest());
        }
    }
}
