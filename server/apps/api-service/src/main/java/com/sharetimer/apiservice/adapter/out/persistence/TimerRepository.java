package com.sharetimer.apiservice.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimerRepository extends JpaRepository<TimerJpaEntity, UUID> {

  @EntityGraph(attributePaths = "timestamps")
  Optional<TimerJpaEntity> findWithTimestampsById(UUID id);
}
