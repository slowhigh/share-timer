package com.slowhigh.chronos.timer.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.slowhigh.chronos.timer.domain.Timer;

public interface TimerRepository extends JpaRepository<Timer, UUID> {
}
