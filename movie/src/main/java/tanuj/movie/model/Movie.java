package tanuj.movie.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="movie")
@EntityListeners(AuditingEntityListener.class)
public class Movie {
	
	@Id
	@Column(name="movieId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="movie_title",nullable = false)
	@NotBlank(message = "title is mandatory")
	private String title;
	
	@Column(nullable = false)
	@NotBlank(message = "Category is mandatory")
	private String category;
	
	@Column(nullable=false)
	@Min(value=1,message = "ratings are mandatory abobe 0")
	private int ratings;
	
	@Column(nullable=false)
	@NotBlank(message = "createdBy  is mandatorSSy")
	private String createdBy;
	
	@Column(nullable=false)
	@CreatedDate
	private Date createdAt;
	
	@Column(nullable = false)
	@LastModifiedDate
	private Date updateAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String Category) {
		this.category = Category;
	}

	public int getRatings() {
		return ratings;
	}

	public void setRatings(int ratings) {
		this.ratings = ratings;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
	
	

}
