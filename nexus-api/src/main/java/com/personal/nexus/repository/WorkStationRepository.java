package com.personal.nexus.repository;
import com.personal.nexus.entity.workstation.WorkStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkStationRepository extends JpaRepository<WorkStation, Long> {

}
