package com.github.petrilya.jpastucks.view;

import com.github.petrilya.jpastucks.entity.Image;
import com.github.petrilya.jpastucks.entity.User;
import com.github.petrilya.jpastucks.service.ImageCollectionService;
import com.github.petrilya.jpastucks.service.ImageService;
import com.github.petrilya.jpastucks.service.UserService;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller("Adapter")
public class UserInterfaceAdapter {
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageCollectionService collectionService;
    @Autowired
    private CreateCollection createCollection;
    @Autowired
    private EditCollection editCollection;
    @Autowired
    private PrivateCollectionView privateCollectionView;
    private ViewCollection viewCollection;

    private User currentUser;
    private JFrame frame;

    public void signIn() {
        frame = getDefaultFrame();
        JTextField name = new JTextField();
        name.setSize(100, 10);
        JTextField password = new JTextField();
        password.setSize(100, 10);
        JButton button = new JButton();
        button.setSize(40, 40);
        button.addActionListener(e -> {
            userService.findById(name.getText()).ifPresent(user -> {
                currentUser = user;
                display();
            });
        });
        addAllToFrame(frame, name, password, button);
        frame.setVisible(true);
    }

    public void display() {
        frame.setVisible(false);
        frame.setLayout(new GridLayout(8, 0));
        frame.getContentPane().removeAll();
        JButton button = new JButton("Upload image");
        button.addActionListener(e -> {
            uploadImage();
        });
        JButton viewPublic = new JButton("View public");
        viewPublic.addActionListener(e -> {
            ViewCollection collection =
                    new ViewCollection(
                            imageService.findAll(),
                            this.currentUser);
            collection.setService(imageService);
            collection.show();
        });
        JButton createCollection = new JButton("Create collection");
        createCollection.addActionListener(e -> {
            this.createCollection.setAuthor(this.currentUser);
            List<Image> imageList = new ArrayList<>();
            this.imageService.findAll().forEach(imageList::add);
            this.createCollection.setImages(imageList);
            this.createCollection.show();
        });
        JTextField collectionName = new JTextField();
        collectionName.setMaximumSize(new Dimension(100, 30));
        JButton viewPrivate = new JButton("View private");
        viewPrivate.addActionListener(e -> {
            if (collectionName.getText().equals("")) {
                return;
            }
            collectionService.
                    findCollectionByName(collectionName.getText()).
                    ifPresent(collection -> {
                        if (collection.getViewers().contains(currentUser) ||
                        collection.getEditors().contains(currentUser) ||
                        collection.getAuthor().getName().equals(currentUser.getName())) {
                            this.privateCollectionView.setCollection(collection);
                            this.privateCollectionView.setUser(this.currentUser);
                            this.privateCollectionView.show();
                        }
                    });
        });
        JButton editCollection = new JButton("Edit collection");
        editCollection.addActionListener(e -> {
            this.collectionService.
                    findCollectionByName(collectionName.getText()).
                    ifPresent(collection -> {
                        this.editCollection.setCollection(collection);
                        this.editCollection.show();
                    });
        });
        JButton removeCollection = new JButton("Remove collection");
        removeCollection.addActionListener(e -> {
            this.collectionService.
                    findCollectionByName(collectionName.getText()).
                    ifPresent(collection -> {
                        collection.getEditors().clear();
                        collection.getViewers().clear();
                        collection.setAuthor(null);
                        collection.getImages().clear();
                        currentUser.
                                getImageCollections().
                                removeIf(collection1 -> collection.getName().equals(collection1.getName()));
                        this.collectionService.delete(collection);
                        this.userService.save(currentUser);
                    });
        });
        addAllToFrame(frame,
                button,
                viewPublic,
                createCollection,
                collectionName,
                viewPrivate,
                editCollection,
                removeCollection);
        frame.setVisible(true);
    }


    public static void addAllToFrame(final JFrame frame, Component... components) {
        for (Component component : components) {
            frame.add(component);
        }
    }

    public static JFrame getDefaultFrame() {
        JFrame frame = new JFrame();
        frame.setMinimumSize(new Dimension(250, 250));
        frame.setLayout(new GridLayout(3, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    public void uploadImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                int idx = f.getName().indexOf(".jpg");
                return idx != -1;
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        int rc = chooser.showOpenDialog(null);
        if (rc == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(file);
                byte[] data = Image.toByteArray(image);
                Image eImage = new Image();
                currentUser.getImages().add(eImage);
                eImage.setAuthor(this.currentUser);
                eImage.setContent(data);
                eImage.setName(file.getName());
                imageService.saveImage(eImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
