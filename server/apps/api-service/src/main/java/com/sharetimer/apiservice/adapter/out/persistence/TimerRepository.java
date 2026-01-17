package com.sharetimer.apiservice.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sharetimer.apiservice.domain.model.Timer;

public interface TimerRepository extends JpaRepository<Timer, UUID> {

  @EntityGraph(attributePaths = "timestamps")
  Optional<Timer> findWithTimestampsById(UUID id);
}
