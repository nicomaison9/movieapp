package movieapp.service;

import java.util.Optional;
import java.util.stream.Stream;

import movieapp.dto.ArtistSimple;

public interface IArtistService {
	Optional<ArtistSimple> getById(int id);
	ArtistSimple add(ArtistSimple artist);
	Stream<ArtistSimple> getByName(String name);
}
