package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exception.UsernameAlreadyExistException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.AbstractCrudService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service("userService")
public class UserServiceImpl extends AbstractCrudService<User, UserRepository> {

    public UserServiceImpl(UserRepository repository) { super(repository); }
    @Override
    public User insert(User entity) {
        Optional<User> existingAccount = repository.findByUsername(entity.getUsername());
        if (existingAccount.isPresent()) {
            throw new UsernameAlreadyExistException(entity.getUsername());
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        entity.setPassword(encoder.encode(entity.getPassword()));
        return this.repository.save(entity);
    }
}
