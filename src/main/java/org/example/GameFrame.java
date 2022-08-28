package org.example;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    GameFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new GamePanel());
        pack();
        setResizable(false);
        setVisible(true);
        setTitle("Lame Arkanoid");
    }
}
