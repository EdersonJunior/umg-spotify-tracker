package com.app.umg.repository;

import com.app.umg.entity.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<TrackEntity, String> {
}