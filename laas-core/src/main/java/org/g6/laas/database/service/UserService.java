package org.g6.laas.database.service;

import org.g6.laas.database.entity.User;
import org.g6.laas.database.repository.IUserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private IUserRepository userRepo;

    public void saveUser(User user) {
        userRepo.save(user);
    }

    public void deleteUser(User user) {
        userRepo.delete(user);
    }
}
