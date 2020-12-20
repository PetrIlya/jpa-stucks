package com.github.petrilya.jpastucks.entity;

import lombok.Data;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class ImageCollection {
    @Id
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    private User author;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private List<User> editors;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> viewers;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Image> images;

    public void addImage(Image image) {
        this.images.add(image);
    }
}
