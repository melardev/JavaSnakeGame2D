package com.melardev.snake.entities;


import com.melardev.snake.SnakeGame;
import com.melardev.snake.enums.Orientation;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Snake extends AbstractEntity {

    private final Random random;
    private List<Rectangle> snakeParts;
    Map<Orientation, Point> orientationMap;
    private Orientation currentOrientation;

    public Snake() {
        snakeParts = new ArrayList<>();
        orientationMap = new HashMap<>();

        currentOrientation = Orientation.Left;
        orientationMap.put(Orientation.Up, new Point(0, -1));
        orientationMap.put(Orientation.Right, new Point(1, 0));
        orientationMap.put(Orientation.Down, new Point(0, 1));
        orientationMap.put(Orientation.Left, new Point(-1, 0));

        resetState();

        random = new Random();
    }

    private void resetState() {

        Point randomPosition = SnakeGame.getMiddleRandomPoint();

        // For a different way of achieving the same goal(retrieving the nearest multiple of TILE_WIDTH and TILE_HEIGHT)
        // Look at Food.java
        int xPos = (randomPosition.x / SnakeGame.TILE_WIDTH) * SnakeGame.TILE_WIDTH;
        int yPos = (randomPosition.y / SnakeGame.TILE_HEIGHT) * SnakeGame.TILE_HEIGHT;

        // xPos and yPos must be a multiple of TILE_WIDTH and TILE_HEIGHT correspondingly

        if (snakeParts.size() > 6)
            snakeParts.subList(6, snakeParts.size() - 1).clear();

        for (int i = 0; i <= 5; i++) {
            if (snakeParts.size() < (i + 1))
                snakeParts.add(new Rectangle(xPos, yPos, SnakeGame.TILE_WIDTH, SnakeGame.TILE_HEIGHT));
        }


    }

    @Override
    public void draw(Graphics graphics) {
        for (int i = 0; i < snakeParts.size(); i++) {

            if (i == 0) {
                graphics.setColor(SnakeGame.COLOR_SNAKE_HEAD);
            } else {
                graphics.setColor(SnakeGame.COLOR_SNAKE_TAIL);
            }

            Rectangle currentPart = snakeParts.get(i);
            graphics.fillRect(currentPart.x, currentPart.y, currentPart.width, currentPart.height);
        }
    }

    @Override
    public void update() {
        for (int i = snakeParts.size() - 1; i > 0; i--) {
            Rectangle currentPart = snakeParts.get(i);
            Rectangle previousPart = snakeParts.get(i - 1);
            currentPart.setLocation(previousPart.x, previousPart.y);
        }

        // Move the head
        Rectangle head = snakeParts.get(0);
        Point pos = head.getLocation();
        head.setLocation(pos.x + orientationMap.get(currentOrientation).x * SnakeGame.TILE_WIDTH,
                pos.y + orientationMap.get(currentOrientation).y * SnakeGame.TILE_HEIGHT);
    }

    public boolean ateFood(Food food) {
        Rectangle head = snakeParts.get(0);
        boolean result = head.contains(food.getBounds());
        if (result)
            snakeParts.add(new Rectangle(snakeParts.get(snakeParts.size() - 1)));
        return result;
    }

    public boolean died() {

        Rectangle head = snakeParts.get(0);

        // Head out of bounds check
        if (head.x < 0 || head.x > SnakeGame.GAME_WIDTH || head.y < 0 || head.y > SnakeGame.GAME_HEIGHT)
            return true;

        // Self collision check
        for (int i = 1; i < snakeParts.size() - 1; i++) {
            if (head.contains(snakeParts.get(i)))
                return true;
        }

        return false;
    }

    public void setOrientation(Orientation orientation) {
        if ((currentOrientation == Orientation.Up || currentOrientation == Orientation.Down)
                &&
                (orientation == Orientation.Up || orientation == Orientation.Down))
            return;

        if ((currentOrientation == Orientation.Left || currentOrientation == Orientation.Right)
                &&
                (orientation == Orientation.Left || orientation == Orientation.Right))
            return;

        currentOrientation = orientation;

    }

    public boolean contains(Rectangle bounds) {
        for (Rectangle part : snakeParts) {
            if (part.contains(bounds)) return true;
        }

        return false;
    }
}
