package com.sharetimer.apiservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sharetimer.apiservice.domain.Timer;

public interface TimerRepository extends JpaRepository<Timer, UUID> {
}
