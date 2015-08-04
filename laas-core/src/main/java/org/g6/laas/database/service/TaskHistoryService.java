package org.g6.laas.database.service;

import org.g6.laas.database.repository.ITaskHistoryRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TaskHistoryService {
    @Resource
    private ITaskHistoryRepository taskRepo;


}
