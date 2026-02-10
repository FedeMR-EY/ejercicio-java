package com.ey.ejercicio_java.service;

import com.ey.ejercicio_java.model.Phone;
import com.ey.ejercicio_java.repository.PhoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PhoneService implements DatabaseService<Phone> {

    private final PhoneRepository phoneRepository;

    public PhoneService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public Phone save(Phone entity) {
        return phoneRepository.save(entity);
    }

    @Override
    public List<Phone> getAll() {
        return phoneRepository.findAll();
    }

    @Override
    public Phone findById(UUID id) {
        return phoneRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(UUID id) {
        phoneRepository.deleteById(id);
    }
}
