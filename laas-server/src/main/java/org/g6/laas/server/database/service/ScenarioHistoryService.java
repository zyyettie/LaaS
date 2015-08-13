package org.g6.laas.server.database.service;

import org.g6.laas.server.database.repository.IScenarioHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScenarioHistoryService {
    @Autowired
    private IScenarioHistoryRepository scenarioHistoryRepo;
}
