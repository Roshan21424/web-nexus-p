package com.personal.nexus.service;

import com.personal.nexus.entity.workstation.DailyRemainder;
import com.personal.nexus.repository.DailyRemainderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyRemainderService {

    @Autowired
    private DailyRemainderRepository dailyRemainderRepository;
    public List<DailyRemainder> getDailyRemaindersByWorkStationId(Long workStationId){
        return dailyRemainderRepository.findByWorkStationId(workStationId);
    }

}
