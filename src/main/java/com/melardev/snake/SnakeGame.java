package com.melardev.snake;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class SnakeGame extends JFrame {

    public static final int MAX_SLEEP_SKIPPED = 50;
    public static final int MAX_SKIPS = 5;
    public static final int FPS = 20;
    public static final long NANO_SECS_TIME_PER_FRAME = 1_000_000_000L / FPS;

    public static final Color GAME_COLOR = Color.BLACK;
    public static final Color COLOR_TEXT = Color.GREEN;

    public static final int X_TILE_COUNT = 50;
    public static final int TILE_WIDTH = 25;
    public static final Color COLOR_SNAKE_HEAD = new Color(0x343212);
    public static final Color COLOR_SNAKE_TAIL = new Color(0x123561);

    private static final int Y_TILE_COUNT = 25;
    public static final int TILE_HEIGHT = 25;

    public final static int GAME_WIDTH = X_TILE_COUNT * TILE_WIDTH;
    public final static int GAME_HEIGHT = Y_TILE_COUNT * TILE_HEIGHT;


    public SnakeGame() {
        Container mainContentPane = getContentPane();
        mainContentPane.setLayout(new BorderLayout());
        SnakePane snakePane = new SnakePane(this);
        mainContentPane.add(snakePane, BorderLayout.CENTER);
        setUndecorated(false); //no effect when false

        // Ignore automatic paints, we are gonna paint what we need manually ourselves
        setIgnoreRepaint(true);

        pack(); // Pack so we get the insets populated
        // now we have the insets, let's adjust the size
        setSize(GAME_WIDTH + getInsets().left + getInsets().right,
                GAME_HEIGHT + getInsets().top + getInsets().bottom);


        setLocation(200, 200);
        setResizable(false);
        setVisible(true);
    }

    public static Point getMiddleRandomPoint() {
        int minX = SnakeGame.GAME_WIDTH / 4;
        int maxX = SnakeGame.GAME_WIDTH * 3 / 4;

        int minY = SnakeGame.GAME_HEIGHT / 4;
        int maxY = SnakeGame.GAME_HEIGHT * 3 / 4;

        int xPos = ThreadLocalRandom.current().nextInt(minX, maxX);
        int yPos = ThreadLocalRandom.current().nextInt(minY, maxY);
        return new Point(xPos, yPos);
    }

    public static void main(String[] args) {
        SnakeGame game = new SnakeGame();
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


}
