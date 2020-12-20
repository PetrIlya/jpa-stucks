package com.github.petrilya.jpastucks;

import com.github.petrilya.jpastucks.view.UserInterfaceAdapter;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

@SpringBootApplication
public class SwingApp extends JFrame {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {

        var ctx = new
                SpringApplicationBuilder(SwingApp.class)
                .headless(false).run(args);
        EventQueue.invokeLater(() -> {
            var adapter = ctx.getBean("Adapter", UserInterfaceAdapter.class);
            adapter.signIn();
        });
    }
}