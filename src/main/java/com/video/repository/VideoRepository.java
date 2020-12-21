package com.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.video.model.VideoFile;

public interface VideoRepository extends JpaRepository<VideoFile, String>{

	

}
