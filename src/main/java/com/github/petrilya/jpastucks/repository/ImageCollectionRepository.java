package com.github.petrilya.jpastucks.repository;

import com.github.petrilya.jpastucks.entity.ImageCollection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("collectionRepository")
public interface ImageCollectionRepository extends CrudRepository<ImageCollection, String> {
    Optional<ImageCollection> findByName(String name);
}
