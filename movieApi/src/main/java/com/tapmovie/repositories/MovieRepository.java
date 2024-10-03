package com.tapmovie.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tapmovie.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

}
