package com.github.petrilya.jpastucks.view;

import com.github.petrilya.jpastucks.entity.Image;
import com.github.petrilya.jpastucks.entity.User;
import com.github.petrilya.jpastucks.service.ImageService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ViewCollection {
    private final List<Image> images;
    private final User user;
    private JFrame frame;
    private JPanel panel;
    private JLabel image;
    private JPanel controls;
    private int idx = 0;
    private ImageService service;

    public ViewCollection(Iterable<Image> images, User user) {
        this.images = new ArrayList<>();
        this.user = user;
        images.forEach(this.images::add);
    }

    public void show() {
        this.frame = new JFrame();
        frame.setPreferredSize(new Dimension(200, 200));
        addImageForm(frame, images.get(idx));
        frame.pack();
        frame.setVisible(true);
    }

    private void addImageForm(JFrame frame, Image image) {
        this.panel = getPanel(image);
        frame.add(panel);
    }

    private JPanel getPanel(Image image) {
        JPanel panel = new JPanel(new BorderLayout());
        GridBagConstraints c = new GridBagConstraints();
        addImageName(panel, image);
        addControls(panel, image);
        addImage(panel, image);
        return panel;
    }

    public void updatePanel() {
        this.frame.setVisible(false);
        ImageIcon icon = new ImageIcon();
        if (idx < 0 || images.size() == 0) {
            image.setText("Nothing to see here");
        } else {
            icon.setImage(getScaledImage(images.get(idx), 200, 200));
        }
        this.image.setIcon(icon);
        this.frame.setVisible(true);
    }

    private void addImageName(JPanel panel, Image image) {
        JLabel label = new JLabel(image.getName());
        panel.add(label, BorderLayout.NORTH);
    }

    private void addImage(JPanel panel, Image image) {
        JPanel paneImage = new JPanel(new GridLayout(1, 1));
        JLabel label = new JLabel();
        ImageIcon icon = new ImageIcon();
        icon.setImage(getScaledImage(image, 200, 200));
        label.setIcon(icon);
        label.setSize(200, 200);
        paneImage.add(label);
        panel.add(label, BorderLayout.CENTER);
        this.image = label;
    }

    @Transactional
    void addControls(JPanel panel, Image image) {
        controls = new JPanel(new GridLayout(1, 5));
        JButton prev = new JButton("<<");
        prev.addActionListener(e -> {
            if (idx - 1 >= 0) {
                idx--;
                updatePanel();
            }
        });
        JButton next = new JButton(">>");
        next.addActionListener(e -> {
            if (idx + 1 < images.size()) {
                idx++;
                updatePanel();
            }
        });
        JButton up = new JButton("up");
        JButton down = new JButton("down");
        JButton remove = new JButton("remove");
        remove.addActionListener(e -> {
            if (images.size() <= 0) {
                return;
            }
            Image tdI = this.images.get(idx);
            this.images.remove(tdI);
            idx--;
            this.service.delete(tdI);
            updatePanel();
        });
        controls.add(prev);
        controls.add(next);
        controls.add(up);
        controls.add(down);
        if (image.getAuthor().getName().equals(user.getName())) {
            controls.add(remove);
        }
        panel.add(controls, BorderLayout.SOUTH);
    }

    private BufferedImage getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg.fromByteArray(), 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
}
