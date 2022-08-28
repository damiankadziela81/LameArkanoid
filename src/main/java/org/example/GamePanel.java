package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private final int GAME_WIDTH = 600;
    private final int GAME_HEIGHT = 500;
    private int paddleWidth = 100;
    private int paddleHeight = 10;
    private int xPaddle = (GAME_WIDTH - paddleWidth)/2;
    private int yPaddle = GAME_HEIGHT - paddleHeight;
    private int ballDiameter = 10;
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
    private int gapBetweenBricks = 5;
    private int leftEdgeToBricksGap = (GAME_WIDTH - (brickWidth*brickColumns + gapBetweenBricks *(brickColumns - 1))) / 2;
    private int topEdgeToBricksGap = 10;
    private int bricksX[][] = new int[brickColumns][brickRows];
    private int bricksY[][] = new int[brickColumns][brickRows];
    private boolean isBrickHit[][] = new boolean[brickColumns][brickRows];
    private String gameState = "runs";
    private boolean enableCheat = false;

    public GamePanel() {
        this.setBackground(Color.BLACK);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

        spawnBricks();
        timer = new Timer(10, this);
        timer.start();
        addKeyListener(new PaddleControl());
        setFocusable(true);
    }

    public class PaddleControl extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_LEFT && xPaddle > 0) {
                xPaddle = xPaddle - 10;
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT && xPaddle < (GAME_WIDTH - paddleWidth)) {
                xPaddle = xPaddle + 10;
            }
            if(!gameState.equals("runs") && e.getKeyCode() == KeyEvent.VK_SPACE) {
                resetGame();
            }
            if(gameState.equals("runs") && e.getKeyCode() == KeyEvent.VK_A) {
               enableCheat = true;
                System.out.println("CHEAT ENABLED!");
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //this will paint the background
        if(gameState.equals("runs")) {
            drawPaddle(g);
            drawBall(g);
            drawBricks(g);
        } else {
            gameOver(g);
        }
    }

    private void spawnBricks() {
        for(int i = 0; i < brickColumns; i++) {
            for(int j = 0; j < brickRows; j++) {
                bricksX[i][j] = i * (brickWidth + gapBetweenBricks) + leftEdgeToBricksGap;
                bricksY[i][j] = j * (brickHeight + gapBetweenBricks) + topEdgeToBricksGap;
                isBrickHit[i][j] = false;
            }
        }
    }

    private void drawPaddle (Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(xPaddle, yPaddle, paddleWidth, paddleHeight);
    }

    private void drawBall (Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(xBall, yBall, ballDiameter, ballDiameter);
    }

    private void ballMovement () {
        if(xBall <= 0 || xBall > GAME_WIDTH - ballDiameter) {
            dxBall = -dxBall;
        }
        if(yBall <= 0 || (yBall + ballDiameter) >= yPaddle && xBall > xPaddle && xBall < (xPaddle + paddleWidth)) {
            dyBall = -dyBall;
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

    private void collisionWithBricks () {
        for(int i = 0; i < brickColumns; i++) {
            for (int j = 0; j < brickRows; j++) {
                if(!isBrickHit[i][j]) {
                    if(xBall > bricksX[i][j]
                            && xBall < bricksX[i][j] + brickWidth
                            && yBall + ballDiameter > bricksY[i][j]
                            && yBall < bricksY[i][j] + brickHeight) {
                        dyBall = -dyBall;
                        isBrickHit[i][j] = true;
                    } else if(yBall > bricksY[i][j]
                            && yBall < bricksY[i][j] + brickHeight
                            && xBall + ballDiameter > bricksX[i][j]
                            && xBall < bricksX[i][j] + brickWidth) {
                        dxBall = -dxBall;
                        isBrickHit[i][j] = true;
                    }
                }
            }
        }
    }

    private void checkGameState () {
        if(yBall > GAME_HEIGHT) gameState = "YOU LOST";
        int bricksLeft = 0;
        for(int i = 0; i < brickColumns; i++) {
            for (int j = 0; j < brickRows; j++) {
                if(!isBrickHit[i][j]) bricksLeft++;
            }
        }
        if(bricksLeft == 0) gameState = "YOU WON";
    }

    private void gameOver(Graphics g) {
        Font font = new Font(null, Font.BOLD, 40);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(gameState, (GAME_WIDTH - metrics.stringWidth(gameState)) / 2, GAME_HEIGHT / 2);
    }

    private void resetGame() {
        spawnBricks();
        xPaddle = (GAME_WIDTH - paddleWidth)/2;
        yPaddle = GAME_HEIGHT - paddleHeight;
        xBall = (GAME_WIDTH - ballDiameter)/2;
        yBall = (GAME_HEIGHT - ballDiameter)/2;
        dxBall = 2;
        dyBall = -2;
        enableCheat = false;
        gameState = "runs";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(enableCheat) {
            xPaddle = xBall - paddleWidth / 2;
        }
        checkGameState();
        collisionWithBricks();
        ballMovement();
        repaint();
    }
}
