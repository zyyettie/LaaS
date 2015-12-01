package org.g6.laas.server.database.service;

import org.g6.laas.server.database.entity.user.Users;
import org.g6.laas.server.database.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepo;

    public void saveUser(Users user) {
        userRepo.save(user);
    }

    public void deleteUser(Users user) {
        userRepo.delete(user);
    }
}
