package com.github.petrilya.jpastucks.view;

import com.github.petrilya.jpastucks.entity.Image;
import com.github.petrilya.jpastucks.entity.ImageCollection;
import com.github.petrilya.jpastucks.entity.User;
import com.github.petrilya.jpastucks.service.ImageCollectionService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class CreateCollection {
    private User author;
    private List<Image> images;
    @Autowired
    private ImageCollectionService service;
    private List<Image> chosen;

    public CreateCollection() {
    }

    public CreateCollection(Iterable<Image> images,
                            User user,
                            ImageCollectionService service) {
        this.images = new ArrayList<>();
        this.chosen = new ArrayList<>();
        images.forEach(this.images::add);
        this.author = user;
        this.service = service;
    }

    public void show() {
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(3, 1));
        addUI(frame);
        frame.pack();
        frame.setVisible(true);
    }

    private void addUI(JFrame frame) {
        JTextField name = new JTextField();
        JButton addImages = new JButton("Add iamges");
        addImages.addActionListener(e -> {
            ViewCollection view = new ViewCollection(this.images, this.author);
            JButton add = new JButton("Add image");
            add.addActionListener(event -> {
                Image image = view.getImages().get(view.getIdx());
                this.chosen.add(image);
            });
            view.show();
            view.getControls().add(add);
        });
        JButton create = new JButton("Create collection");
        create.addActionListener(e -> {
            ImageCollection collection = new ImageCollection();
            collection.setAuthor(author);
            collection.setName(name.getText());
            collection.setImages(chosen);
            this.service.save(collection);
            this.chosen.clear();
        });
        frame.add(name);
        frame.add(addImages);
        frame.add(create);
    }
}
