package com.video.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.video.model.VideoFile;
import com.video.repository.VideoRepository;
import com.video.service.VideoStreamService;


@RestController
public class VideoController {

	@Autowired
	private VideoRepository vr;
	
	@Autowired
	private VideoStreamService videoStreamService;
	
	@PostMapping(value = "/upload")
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody String uploadVideo(@RequestParam(value="name") String name, @RequestParam("file") MultipartFile file , @RequestParam("description") String description) {

		try
		{
			
			VideoFile v = new VideoFile(name, file.getSize(), file.getOriginalFilename(), file.getContentType(), file.getOriginalFilename(),file.getBytes(), description);
			vr.save(v);

			return "File uploaded Successfully";
		}
		catch(Exception e) {
			return "Error in Saving the File" + e;
		}
	}
	@GetMapping("/video/{id}")
	public ResponseEntity<byte[]> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList, @PathVariable("id") String id){
	
		try {	
			return videoStreamService.prepareContent(id, httpRangeList);
			
		}
		catch(Exception e) {
			return videoStreamService.prepareContent(id, httpRangeList);
		}
		}
}
