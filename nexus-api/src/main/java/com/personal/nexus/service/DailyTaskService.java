package com.personal.nexus.service;

import com.personal.nexus.entity.workstation.DailyTask;
import com.personal.nexus.repository.DailyTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyTaskService {
    @Autowired
    private DailyTaskRepository dailyTaskRepository;

    public List<DailyTask> getDailyTaskRepositoryByWorkStationId(Long workStationId){
        return dailyTaskRepository.findByWorkStationId(workStationId);
    }
}
