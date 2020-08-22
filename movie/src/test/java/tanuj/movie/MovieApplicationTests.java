package tanuj.movie;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import tanuj.movie.model.Movie;

@SpringBootTest(classes =MovieApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
    private int port;
	
	private String getRootUrl() {
        return "http://localhost:" + port;
    }
	
	@Test
	void contextLoads() {
	}
	
	@Test
	public void testGetAllMovie() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/movie/find",HttpMethod.GET,entity,String.class);
       System.out.println(response.getBody());
 
        assertNotNull(response.getBody());
   }
	
	@Test
    public void testGetMovieById() {
        Movie  movie = restTemplate.getForObject(getRootUrl() + "/movie/find/113", Movie.class);
        System.out.println(movie.getId()+" "+movie.getCreatedBy()+" "+movie.getCreatedAt()+" "+movie.getTitle()+" "+movie.getRatings());
        assertNotNull(movie);
    }
	
	 @Test
	    public void testCreateMovie() {
	        Movie movie = new Movie();
	        movie.setTitle("LALISHQ");
	        movie.setCategory("THRILLER");
	        movie.setCreatedBy("TANUJ TYAGI");
	        movie.setRatings(3);
	        
	        ResponseEntity<Movie> postResponse = restTemplate.postForEntity(getRootUrl() + "/movie/create", movie, Movie.class);
	        assertNotNull(postResponse);
	        assertNotNull(postResponse.getBody());
	    }
	
	 @Test
	    public void testDeleteMovie() {
	          int id = 113;
	           Movie movie=restTemplate.getForObject(getRootUrl() + "/movie/delete/" + id, Movie.class);
	           assertNotNull(movie);
	           movie = restTemplate.getForObject(getRootUrl() + "/movie/delete/" + id, Movie.class);
	           restTemplate.delete(getRootUrl()+"/movie/delete/"+id,Movie.class);
	           
	    
	        
	  }
	 
	  @Test
	    public void testUpdateMovie() {
	         int id = 86;
	         Movie movie = restTemplate.getForObject(getRootUrl() + "/movie/find/" + id, Movie.class);
	         movie.setCreatedBy("TANUJ TYAGI");
	         movie.setTitle("THE MULE");
	         movie.setCategory("DRAMA");

	         restTemplate.put(getRootUrl() + "/movie/update/" + id, movie);

	         Movie updatedMovie = restTemplate.getForObject(getRootUrl() + "/movie/find/" + id, Movie.class);
	         assertNotNull(updatedMovie);
	    }
	  
	   
	  
}
