package com.github.petrilya.jpastucks.service;

import com.github.petrilya.jpastucks.entity.Image;
import com.github.petrilya.jpastucks.entity.ImageCollection;
import com.github.petrilya.jpastucks.repository.ImageCollectionRepository;
import com.github.petrilya.jpastucks.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImageService {
    @Autowired
    @Qualifier("imageRepository")
    private ImageRepository repository;
    @Autowired
    private ImageCollectionRepository collectionRepository;

    public void saveImage(Image image) {
        repository.save(image);
    }
    public Iterable<Image> findAll() {
        return repository.findAll();
    }
    @Transactional
    public void delete(Image image) {
        collectionRepository.findAll().forEach(collection -> {
            collection.getImages().removeIf(st -> st.getId().equals(image.getId()));
            collectionRepository.save(collection);
        });
        this.repository.delete(image);
    }
}
