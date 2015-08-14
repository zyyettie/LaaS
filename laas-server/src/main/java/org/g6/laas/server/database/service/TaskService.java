package org.g6.laas.server.database.service;

import org.g6.laas.server.database.repository.ITaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    private ITaskRepository taskRepo;
}
