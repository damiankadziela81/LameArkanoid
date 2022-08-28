package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private final int GAME_WIDTH = 600;
    private final int GAME_HEIGHT = 500;
    private int paddleWidth = 100;
    private int paddleHeight = 10;
    private int xPaddle = (GAME_WIDTH - paddleWidth)/2;
    private int yPaddle = GAME_HEIGHT - paddleHeight;
    private int ballDiameter = 20;
    private int xBall = (GAME_WIDTH - ballDiameter)/2;
    private int yBall = (GAME_HEIGHT - ballDiameter)/2;
    private int dxBall = 2;
    private int dyBall = -2;
    private Timer timer;

    private Random brickGenerator = new Random();
    private int brickColumns = brickGenerator.nextInt(5) + 4;
    private int brickRows = brickGenerator.nextInt(5) + 4;
    private int brickWidth = 50;
    private int brickHeight = 10;
    private int brickSpace = 5;
    private int bricksLeftSpace = (GAME_WIDTH - (brickWidth*brickColumns + brickSpace*(brickColumns - 1))) / 2;
    private int bricksUpperSpace = 10;
    private int bricksX[][] = new int[brickColumns][brickRows];
    private int bricksY[][] = new int[brickColumns][brickRows];
    private boolean isBrickHit[][] = new boolean[brickColumns][brickRows];

    public GamePanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

        for(int i = 0; i < brickColumns; i++) {
            for(int j = 0; j < brickRows; j++) {
                bricksX[i][j] = i * (brickWidth + brickSpace) + bricksLeftSpace;
                bricksY[i][j] = j * (brickHeight + brickSpace) + bricksUpperSpace;
                isBrickHit[i][j] = false;
            }
        }
        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //this will paint the background
        drawPaddle(g);
        drawBall(g);
        drawBricks(g);
    }

    private void drawPaddle (Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(xPaddle, yPaddle, paddleWidth, paddleHeight);
        g.setColor(Color.WHITE);
        g.drawRect(xPaddle, yPaddle, paddleWidth, paddleHeight);
    }

    private void drawBall (Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(xBall, yBall, ballDiameter, ballDiameter);
    }

    private void ballMovement () {
        if(xBall <= 0 || xBall > GAME_WIDTH - ballDiameter) {
            dxBall = dxBall * -1;
        }
        if(yBall <= 0) {
            dyBall = dyBall * -1;
        }
        xBall += dxBall;
        yBall += dyBall;
    }

    private void drawBricks (Graphics g) {
        g.setColor(Color.GREEN);
        for(int i = 0; i < brickColumns; i++) {
            for (int j = 0; j < brickRows; j++) {
                if(!isBrickHit[i][j]) {
                    g.fillRect(bricksX[i][j], bricksY[i][j], brickWidth, brickHeight);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ballMovement();
        repaint();
    }
}
