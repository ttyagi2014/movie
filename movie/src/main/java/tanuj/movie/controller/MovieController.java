package tanuj.movie.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import tanuj.movie.dao.MovieRepository;
import tanuj.movie.exception.ResourceNotFoundException;
import tanuj.movie.model.Movie;

@RestController
public class MovieController {
	@Autowired
	private MovieRepository mr;
	@GetMapping("/movie")
	public Map<String,String> getWelcomePage() {
		Map<String,String> map=new HashMap<String,String>();
		map.put("HOME PAGE","localhost:8080/movie ");
		map.put("findAll", "localhost:8080/movie/find");
		map.put("findById", "localhost:8080/movie/find/id");
		map.put("create", "localhost:8080/movie/create");
		map.put("update", "localhost:8080/movie/update/id");
		map.put("partial update(PATCH REQUEST)","localhost:8080/movie/changes/id");
		map.put("delete","localhost:8080/movie/delete/id");
		
		return map;
	
		
	}
	
	@GetMapping("/movie/find")
	public List<Movie> getMovieAll(){
		return (List<Movie>) mr.findAll();
	}
	
	@GetMapping("/movie/find/{id}")
	public ResponseEntity<Movie> getMovieById(@PathVariable(value = "id") Integer movieId) throws ResourceNotFoundException{
		
		Movie mv= mr.findById(movieId).orElseThrow(()-> new RuntimeException("Movie Not found with Id:"+movieId));
		
		return ResponseEntity.ok().body(mv);
		
	}
	
    @PostMapping("/movie/create")
    public Movie setMovie(@Valid @RequestBody Movie mv) {
    	     return mr.save(mv);
        
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    	
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    
    @DeleteMapping("/movie/delete/{id}")
    public ResponseEntity<Map<String,Boolean>> getMovieDelete(@PathVariable(value = "id") Integer movieId){
    	
    	 mr.findById(movieId).orElseThrow(()-> new RuntimeException("Movie Not found with Id:"+movieId));
         mr.deleteById(movieId);
    	 Map<String, Boolean> response = new HashMap<>();
         response.put("deleted", Boolean.TRUE);
         return ResponseEntity.ok().body(response);
    	
    	
    }
    @PutMapping("/movie/update/{id}")
    public ResponseEntity<Movie> updateUser(
    @PathVariable(value = "id") Integer movieId,
    @Valid @RequestBody Movie movie) throws ResourceNotFoundException {
    	Movie mv= mr.findById(movieId).orElseThrow(()-> new RuntimeException("Movie Not found with Id:"+movieId));
  
        mv.setCategory(movie.getCategory());
        mv.setCreatedBy(movie.getCreatedBy());
        mv.setTitle(movie.getTitle());
        mv.setRatings(movie.getRatings());
        Movie updatedMovie = mr.save(mv);
        return ResponseEntity.ok(updatedMovie);
   }
    @PatchMapping(value="/movie/changes/{id}")
    public ResponseEntity<Movie> getMovieChanges(@PathVariable(value = "id") Integer movieId,@RequestBody JsonPatch patch) throws JsonMappingException, JsonProcessingException, JsonPatchException{
    
        Movie movie= mr.findById(movieId).orElseThrow(()-> new RuntimeException("Movie Not found with Id:"+movieId));
        Movie moviePatched=applyPatchToMovie(patch, movie);
        movie=  mr.save(moviePatched);
       
          return ResponseEntity.ok().body(movie);
    }
    
   
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handleRunTimeExceptions(RuntimeException msg
    	      ) {
    	    	
    	        Map<String, String> errors = new HashMap<>();
    	        errors.put("RESOURCENOTFOUND",msg.getMessage() );
    	        return errors;
    	    }
    
    private Movie applyPatchToMovie(
    		   JsonPatch patch, Movie targetMovie) throws JsonPatchException, JsonProcessingException {
    			ObjectMapper objectMapper=new ObjectMapper();
    		    JsonNode patched = patch.apply(objectMapper.convertValue(targetMovie, JsonNode.class));
    		    return objectMapper.treeToValue(patched, Movie.class);
    		}
}
