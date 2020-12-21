package com.video.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.video.repository.VideoRepository;


@Controller
public class FileController {

	@Autowired
	private VideoRepository vp;

	@GetMapping("/")
	public String getWelcomePage() {
		return "welcome";
	}

	@GetMapping("/videoDisplay/{id}")
	public String Display(Model model, @PathVariable("id") String id)
	{
		model.addAttribute("video", "/video/"+id);
		return "display";
	}
	@GetMapping("/listVideos") 
	public ModelAndView getListVideos() {

		ModelAndView mv = new ModelAndView("video");
		mv.addObject("videos",vp.findAll());
		return mv; }

}
