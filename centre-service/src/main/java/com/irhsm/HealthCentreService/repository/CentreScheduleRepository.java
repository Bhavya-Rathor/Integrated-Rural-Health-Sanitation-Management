package com.irhsm.HealthCentreService.repository;

import com.irhsm.HealthCentreService.entity.CentreSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.DayOfWeek;

public interface CentreScheduleRepository extends JpaRepository<CentreSchedule, Long> {
    List<CentreSchedule> findByDayOfWeek(DayOfWeek dayOfWeek);
    List<CentreSchedule> findByCentreId(Long centreId);
    List<CentreSchedule> findByWorkerIdAndDayOfWeek(Long workerId, DayOfWeek dayOfWeek);
}

