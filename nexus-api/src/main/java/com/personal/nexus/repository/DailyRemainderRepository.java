package com.personal.nexus.repository;

import com.personal.nexus.entity.workstation.DailyRemainder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyRemainderRepository extends JpaRepository<DailyRemainder, Long> {

    List<DailyRemainder> findByWorkStationId(Long workStationId);

}
