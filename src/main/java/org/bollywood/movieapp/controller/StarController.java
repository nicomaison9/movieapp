package org.bollywood.movieapp.controller;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stars")
public class StarController {

	@GetMapping
	public List<String>  stars() {
		return List.of("elijah wood, Cloris Leachman");
	}
	@GetMapping("/un")
	public String  star() {
		return "Daniel Craig";
	}
}
