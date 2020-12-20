package com.github.petrilya.jpastucks.entity;

import lombok.Data;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Data
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private byte[] content;
    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    public static byte[] toByteArray(BufferedImage biImage) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(biImage, "jpg", bos );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bos.toByteArray();
    }

    public BufferedImage fromByteArray() {
        ByteArrayInputStream bais = new ByteArrayInputStream(content);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
