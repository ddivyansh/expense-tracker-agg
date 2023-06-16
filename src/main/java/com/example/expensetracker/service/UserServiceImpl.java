package com.example.expensetracker.service;

import com.example.expensetracker.Constants.Constants;
import com.example.expensetracker.domain.User;
import com.example.expensetracker.entity.UserEntity;
import com.example.expensetracker.exceptions.EtAuthException;
import com.example.expensetracker.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    @Override
    public User validateUser(String email, String password) throws EtAuthException {
        if (email != null) {
            email = email.toLowerCase();
        }
        User user = findByEmailAndPassword(email, password);
        if (user == null) {
            System.out.println("User not found !! ");
        }
        return user;
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if (email != null) {
            email = email.toLowerCase();
        }
        if (!pattern.matcher(email).matches()) {
            throw new EtAuthException("Invalid email format");
        }
        if (getCountByEmail(email) == 1) {
            throw new EtAuthException("Email already in use.");
        }
        Integer userId = create(firstName, lastName, email, password);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            return mapper.convertValue(userEntity, User.class);
        }
        return null;
    }

    @Override
    public Integer create(String firstName, String lastName, String email, String password) throws EtAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setFirstName(firstName);
            userEntity.setLastName(lastName);
            userEntity.setPassword(hashedPassword);
            userEntity.setEmail(email);
            UserEntity returnedUserEntity = userRepository.save(userEntity);
            return returnedUserEntity.getUserId();
        } catch (Exception e) {
            throw new EtAuthException("Invalid details. Failed to create account");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws EtAuthException {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        User user;
        if (userEntity.isPresent()) {
            user = mapper.convertValue(userEntity.get(), User.class);
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new EtAuthException("Invalid email/password");
            }
            return user;
        }
        return null;
    }

    @Override
    public Integer getCountByEmail(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            //email is in use
            return Constants.ONE;
        }
        return Constants.ZERO;
    }
}
