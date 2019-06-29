package com.melardev.snake;

import com.melardev.snake.entities.Food;
import com.melardev.snake.entities.Snake;
import com.melardev.snake.enums.Orientation;
import com.melardev.snake.input.KeyBoard;
import com.melardev.snake.input.Mouse;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SnakePane extends JComponent implements Runnable {


    private final SnakeGame game;
    private final KeyBoard keyBoard;
    private int sleepsSkipped;
    private boolean running;
    private int numberOfTimesGameUpdated;
    private int numOfReports;
    private int framesSkipped;
    private Thread threadGame;
    private long startTime;
    private BufferedImage gameImage;

    private Snake snake;
    private Food food;
    private int score;

    public SnakePane(SnakeGame game) {
        this.game = game;
        setSize(new Dimension(SnakeGame.GAME_WIDTH, SnakeGame.GAME_HEIGHT));

        sleepsSkipped = 0;
        setFocusable(true);
        running = false;
        numberOfTimesGameUpdated = 0;
        numOfReports = 0;
        framesSkipped = 0;

        snake = new Snake();
        food = new Food();

        gameImage = new BufferedImage(SnakeGame.GAME_WIDTH, SnakeGame.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Mouse mouseHandler = new Mouse();
        keyBoard = new KeyBoard(this);
        addKeyListener(keyBoard);
        addMouseListener(mouseHandler);
        requestFocus();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                running = false;
                StringBuilder stats = new StringBuilder();
                long secondsPassed = ((System.nanoTime() - startTime) / 1_000_000_000L);
                if (secondsPassed > 0) {
                    stats.append("updates per second : " + numberOfTimesGameUpdated / secondsPassed);
                    stats.append("\nnumber of updates : " + numberOfTimesGameUpdated);
                    stats.append("\ntime in seconds passed " + secondsPassed);
                    stats.append("frames skipped" + framesSkipped);
                    System.out.println(stats.toString());
                }
            }
        });
    }


    @Override
    public void addNotify() {
        super.addNotify();
        if (!running || threadGame == null) {
            threadGame = new Thread(this, "Game Thread");
            threadGame.start();
            System.out.println("threadGame thread started");
        }
    }

    @Override
    public void run() {
        long now, lastTime, delta;
        long timeToSleepInMsec, framesToSkip;
        lastTime = System.nanoTime();
        startTime = lastTime;

        running = true;

        while (running) {
            now = System.nanoTime();
            delta = now - lastTime;
            lastTime = now;
            framesToSkip = delta / SnakeGame.NANO_SECS_TIME_PER_FRAME;

            int currentFramesSkipped = 0;
            while (framesToSkip > 1 && currentFramesSkipped < SnakeGame.MAX_SKIPS) {
                gameUpdate();
                System.out.println("Inside slow");
                currentFramesSkipped++;
                framesToSkip -= 1;
            }

            framesSkipped += currentFramesSkipped;

            gameUpdate();
            gameRender();

            timeToSleepInMsec = (SnakeGame.NANO_SECS_TIME_PER_FRAME - ((System.nanoTime() - lastTime))) / 1_000_000L;
            try {
                if (timeToSleepInMsec > 0) {
                    // We are in the good road, let's sleep for a while
                    // System.out.println("Go to sleep " + timeToSleep);
                    Thread.sleep(timeToSleepInMsec);
                } else {
                    // We are running out of time, no sleeping, unless we did not sleep for MAX_SLEEP_SKIPPED times
                    sleepsSkipped++;
                    if (sleepsSkipped >= SnakeGame.MAX_SLEEP_SKIPPED) {
                        sleepsSkipped = 0;
                        Thread.yield();
                        System.out.println("[MAX_SLEEP_SKIPPED] Sleeping because we had no chance to sleep before.");
                        // Should I add 50ms to LastTime ? Because now i have 50
                        // mils of delay ...
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long diffTimeSinceStartGame = (System.nanoTime() - startTime) / 1_000_000L;
            // Each ~2 sec set title
            if (diffTimeSinceStartGame > (2000L * (numOfReports + 1))) {
                numOfReports++;
                // Number Of Times updated / seconds passed since the start of the game
                this.game.setTitle("Updates per second : " + numberOfTimesGameUpdated / (diffTimeSinceStartGame / 1000L));
            }
        }
        System.out.println("exiting...");
        System.exit(0);
    }

    private void gameUpdate() {
        numberOfTimesGameUpdated++;

        snake.update();
        food.update();

        if (snake.ateFood(food)) {
            score += 20;
            boolean newPositionFound = false;
            while (!newPositionFound) {
                food.resetState();
                Rectangle bounds = food.getBounds();
                newPositionFound = !snake.contains(bounds);
            }
        } else if (snake.died()) {
            running = false;
        }

    }

    private void gameRender() {
        Graphics g = getGraphics();
        // Get a canvas to draw on
        Graphics graphics = gameImage.createGraphics(); // We must get graphics and use it
        // , We can not get graphics
        // inline each time because it
        // returns
        // one different graphics instance each time

        drawBase(graphics);
        drawScore(graphics);
        snake.draw(graphics);
        food.draw(graphics);

        // Display the image
        g.drawImage(gameImage, 0, 0, null);
    }

    private void drawBase(Graphics graphics) {
        // Paint with base color
        graphics.setColor(SnakeGame.GAME_COLOR);
        graphics.fillRect(0, 0, gameImage.getWidth(), gameImage.getHeight());
    }

    private void drawScore(Graphics graphics) {
        // Drawing the Memory Cards
        graphics.setColor(SnakeGame.COLOR_TEXT);
        graphics.setFont(new Font("Verdan", 0, 30));
        graphics.drawString(String.format("Score: %d", score), 20, 40);
    }

    @Override
    public Dimension getPreferredSize() {
        super.getPreferredSize();
        return new Dimension(SnakeGame.GAME_WIDTH, SnakeGame.GAME_HEIGHT);
    }

    @Override
    public Dimension getSize(Dimension dimension) {
        super.getSize(dimension);
        return new Dimension(SnakeGame.GAME_WIDTH, SnakeGame.GAME_HEIGHT);
    }

    public void onKeyExit() {
        running = false;
    }

    public void onKeyUpPressed() {
        snake.setOrientation(Orientation.Up);
    }

    public void onKeyRightPressed() {
        snake.setOrientation(Orientation.Right);
    }

    public void onKeyDownPressed() {
        snake.setOrientation(Orientation.Down);
    }

    public void onKeyLeftPressed() {
        snake.setOrientation(Orientation.Left);
    }
}
