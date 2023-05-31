package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exception.UsernameAlreadyExistException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.AbstractCrudService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service("userService")
public class UserServiceImpl extends AbstractCrudService<User, UserRepository> {

    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder)
    {
        super(repository);
        this.encoder = encoder;
    }

    @Override
    public User insert(User entity) {
        Optional<User> existingAccount = repository.findByUsername(entity.getUsername());
        if (existingAccount.isPresent()) {
            throw new UsernameAlreadyExistException(entity.getUsername());
        }
        entity.setPassword(this.encoder.encode(entity.getPassword()));
        return this.repository.save(entity);
    }

    @Override
    public void update(Integer id, User entity) {
        User updatedEntity = getById(id);
        if(!usernameIsNotUsedAndBlank(entity, updatedEntity)){
            throw new RuntimeException("An user can not be updated with an existing username");
        }
        entity.setPassword(this.encoder.encode(entity.getPassword()));
        updatedEntity.update(entity);
        repository.save(updatedEntity);
    }

    private boolean usernameIsNotUsedAndBlank(User entity, User updatedEntity) {
        boolean isNotUsed;
        boolean isNotBlank = entity.getUsername() != null
                && !entity.getUsername().isBlank();
        //IF the username will be updated, check that it is not used ELSE the username remain the same but can be use
        if (!updatedEntity.getUsername().equals(entity.getUsername())) {
            isNotUsed = repository.findByUsername(entity.getUsername()).isEmpty();
        } else {
            isNotUsed = true;
        }
        return isNotBlank && isNotUsed;
    }
}
