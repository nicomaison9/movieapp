package movieapp.service.impl;

import java.util.Optional;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import movieapp.dto.ArtistSimple;
import movieapp.entity.Artist;
import movieapp.persistence.ArtistRepository;
import movieapp.service.IArtistService;

@Service
@Transactional
public class ArtistServiceJpa implements IArtistService{

	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Optional<ArtistSimple> getById(int id) {
		return artistRepository.findById(id) // fetch opt entity artist
			.map(artistEntity -> modelMapper.map(artistEntity, ArtistSimple.class)); // convert entity->dto
	}

	@Override
	public ArtistSimple add(ArtistSimple artist) {
		Artist artistEntityFromRepo = artistRepository.save(
				modelMapper.map(artist, Artist.class));	// convert dto param to entity
		return modelMapper.map(artistEntityFromRepo, ArtistSimple.class); // convert entity to dto result
	}

	@Override
	public Stream<ArtistSimple> getByName(String name) {
		return artistRepository.findByName(name)
			.map(ArtistEntity -> modelMapper.map(Stream.of(ArtistEntity), ArtistSimple.class));
		
	}

}









