package com.github.petrilya.jpastucks.repository;

import com.github.petrilya.jpastucks.entity.Image;
import com.github.petrilya.jpastucks.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("imageRepository")
public interface ImageRepository extends CrudRepository<Image, Long> {
    Optional<Image> getAllByAuthor(User author);
}
