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

    private String cifrarPasword(String password) {
        String encryptedpassword = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");

            m.update(password.getBytes());

            byte[] bytes = m.digest();

            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            /* Complete hashed password in hexadecimal format */
            encryptedpassword = s.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return encryptedpassword;
    }

    public void save(UserModel userModel)  {
        userModel.setPassword(cifrarPasword(userModel.getPassword()));
        userRepository.save(userModel);
    }


    public List<UserModel> findAll()  {
        return (List<UserModel>) userRepository.findAll();
    }

    public Optional<UserResponse> findByLogin(LoginUser login, PasswordUser password)  {
        Optional<UserModel> userModel = userRepository.findById(login.getValue());

        return userModel.isPresent() && userModel.get().getPassword().equals(cifrarPasword(password.getValue()))
                ? Optional.of(UserResponse.fromAgragate(userModel.get()))
                : Optional.empty();
    }
}
