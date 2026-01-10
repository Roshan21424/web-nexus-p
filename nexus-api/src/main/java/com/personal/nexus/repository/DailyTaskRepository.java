package com.personal.nexus.repository;

import com.personal.nexus.entity.workstation.DailyTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyTaskRepository extends JpaRepository<DailyTask,Long> {
    List<DailyTask> findByWorkStationId(Long workStationId);

}