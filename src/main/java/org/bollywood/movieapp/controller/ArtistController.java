package org.bollywood.movieapp.controller;
import java.util.List;

import org.bollywood.movieapp.entity.Artist;
import org.bollywood.movieapp.persistence.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping("/api/artists")
public class ArtistController {
	
	@Autowired
	ArtistRepository ArtistRepository;

//	@GetMapping
//	public List<String>  artists() {
//		return List.of("elijah wood, Cloris Leachman");
//	}
//	@GetMapping("/un")
//	public String  artist() {
//		return "Daniel Craig";
//	}
	
	@PostMapping
	public Artist addArtist(@RequestBody Artist artist) {
		return ArtistRepository.save(artist);
		
	}
}
