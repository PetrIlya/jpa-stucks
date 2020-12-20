package com.github.petrilya.jpastucks.service;

import com.github.petrilya.jpastucks.entity.ImageCollection;
import com.github.petrilya.jpastucks.repository.ImageCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageCollectionService {
    @Autowired
    @Qualifier("collectionRepository")
    private ImageCollectionRepository repository;

    public void save(ImageCollection collection) {
        this.repository.save(collection);
    }
    public void delete(ImageCollection collection) {
        this.repository.delete(collection);
    }
    public Optional<ImageCollection> findCollectionByName(String name) {
        return repository.findByName(name);
    }
}
