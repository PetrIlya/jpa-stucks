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

@Component
@Getter
@Setter
public class PrivateCollectionView {
    @Autowired
    private ImageCollectionService service;
    private ImageCollection collection;
    private User user;
    private int status = 3;

    public PrivateCollectionView() {
    }

    public PrivateCollectionView(ImageCollection collection, User user) {
        this.collection = collection;
        this.user = user;
    }

    public void show() {
        var view = new ViewCollection(collection.getImages(), user);
        JButton button = new JButton("Alter");
        button.addActionListener(e -> {
            if (collection.getImages().
                    size() == 0 ||
                    view.getIdx() >= collection.getImages().size()) {
                return;
            }
            collection.getImages().remove(view.getImages().get(view.getIdx()));
            view.setIdx(view.getIdx() - 1);
            service.save(collection);
            view.updatePanel();
        });
        view.show();
        if (status > 1) {
            view.getControls().add(button);
        }
    }
}
