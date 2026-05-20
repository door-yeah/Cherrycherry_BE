package com.example.cherry_be.domain.log.repository;

import com.example.cherry_be.domain.log.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
