package com.desafio.challenge.service;

import com.desafio.challenge.model.User;
import com.desafio.challenge.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User criarUsuario(User user) {


        if (!validarCPF(user.getDocumento())) {
            throw new IllegalArgumentException("CPF inválido");
        }

        if (userRepository.existsByLogin(user.getLogin())) {
            throw new IllegalArgumentException("Login já existe");
        }

        if (userRepository.existsByDocumento(user.getDocumento())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        user.setSenha(passwordEncoder.encode(user.getSenha()));
        return userRepository.save(user);
    }

    public Optional<User> buscarPorLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> buscarPorId(Long id) {
        return userRepository.findById(id);
    }

    private boolean validarCPF(String cpf) {
        if (StringUtils.isBlank(cpf)) return false;

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) return false;


        if (cpf.matches("(\\d)\\1{10}")) return false;


        try {
            int[] digits = cpf.chars().map(Character::getNumericValue).toArray();


            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += digits[i] * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            if (firstDigit >= 10) firstDigit = 0;

            if (digits[9] != firstDigit) return false;

            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += digits[i] * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            if (secondDigit >= 10) secondDigit = 0;

            return digits[10] == secondDigit;
        } catch (Exception e) {
            return false;
        }
    }
}