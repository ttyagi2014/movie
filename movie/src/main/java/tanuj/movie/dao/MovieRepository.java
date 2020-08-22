package tanuj.movie.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tanuj.movie.model.Movie;
@Repository
public interface MovieRepository extends CrudRepository<Movie,Integer> {

}
