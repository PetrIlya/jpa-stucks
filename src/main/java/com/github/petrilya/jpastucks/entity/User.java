package com.github.petrilya.jpastucks.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity (name = "user_entity")
@Data
public class User {
    @Id
    private String name;
    private String password;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Image> images;
    @OneToMany(fetch = FetchType.EAGER)
    private List<ImageCollection> imageCollections;
}
