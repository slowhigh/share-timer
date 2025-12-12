package com.sharetimer.apiservice.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sharetimer.apiservice.domain.model.Timer;

public interface TimerRepository extends JpaRepository<Timer, UUID> {
}
