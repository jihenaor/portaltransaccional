package com.serviciudad.service;

import com.serviciudad.entity.*;
import com.serviciudad.model.*;
import com.serviciudad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public final class UserService {
    @Autowired
    UserRepository userRepository;

    public static String cifrarPasword(String password) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());
            byte[] bytes = m.digest();

            StringBuilder s = new StringBuilder();
            for (byte aByte : bytes) {
                s.append(String.format("%02x", aByte));
            }

            return s.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // O manejar de otra manera
        }
    }

    public Optional<UserResponse> findByLogin(LoginUser login, PasswordUser password)  {
        Optional<UserModel> userModel = userRepository.findById(login.getValue());
        String passwordCifrado = cifrarPasword(password.getValue());

        return userModel.isPresent() && userModel.get().getPassword().equals(passwordCifrado)
                ? Optional.of(UserResponse.fromAgragate(userModel.get()))
                : Optional.empty();
    }
}
