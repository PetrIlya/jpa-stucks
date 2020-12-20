package com.github.petrilya.jpastucks.view;

import com.github.petrilya.jpastucks.entity.ImageCollection;
import com.github.petrilya.jpastucks.service.ImageCollectionService;
import com.github.petrilya.jpastucks.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.awt.*;

@Component
@Getter
@Setter
public class EditCollection {
    @Autowired
    private ImageCollectionService service;
    @Autowired
    private UserService userService;
    private ImageCollection collection;

    public EditCollection() {
    }

    public void show() {
        generateUI().setVisible(true);
    }

    private JFrame generateUI() {
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(8, 1));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        generateControls(frame);
        frame.pack();
        return frame;
    }

    @Transactional
    void generateControls(JFrame frame) {
        JTextField name = new JTextField(collection.getName());
        JButton setName = new JButton("Set new name");
        setName.addActionListener(e -> {
            this.collection.setName(name.getText());
            this.service.save(collection);
        });
        JTextField editors = new JTextField("New editor name");
        JButton addEditor = new JButton("Add editor");
        JButton removeEditor = new JButton("Remove editor");
        addEditor.addActionListener(e -> {
            userService.findById(editors.getText()).ifPresent(user -> {
                this.collection.getEditors().add(user);
                this.service.save(collection);
            });
        });
        removeEditor.addActionListener(e -> {
            userService.findById(editors.getText()).ifPresent(user -> {
                System.out.println("Was here");
                System.out.println(this.collection.getEditors().size());
                this.collection.
                        getEditors().
                        removeIf(user1 -> user1.getName().equals(user.getName()));
                System.out.println(this.collection.getEditors().size());
                this.service.save(collection);
            });
        });
        JTextField viewers = new JTextField("New viewer name");
        JButton addViewer = new JButton("Add viewer");
        JButton removeViewer = new JButton("Remove viewer");
        addViewer.addActionListener(e -> {
            userService.findById(viewers.getText()).ifPresent(user -> {
                this.collection.getViewers().add(user);
                this.service.save(collection);
            });
        });
        removeViewer.addActionListener(e -> {
            userService.findById(viewers.getText()).ifPresent(user -> {
                this.collection.getViewers().remove(user);
                this.service.save(collection);
            });
        });

        frame.add(name);
        frame.add(setName);
        frame.add(editors);
        frame.add(addEditor);
        frame.add(removeEditor);
        frame.add(viewers);
        frame.add(addViewer);
        frame.add(removeViewer);
    }
}
