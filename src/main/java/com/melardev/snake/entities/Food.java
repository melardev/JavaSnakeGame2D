package com.melardev.snake.entities;

import com.melardev.snake.SnakeGame;

import java.awt.*;

public class Food extends AbstractEntity {
    private Rectangle foodRectangle;

    public Food() {
        resetState();
    }

    public void resetState() {
        Point location = SnakeGame.getMiddleRandomPoint();
        int xPos = location.x - (location.x % SnakeGame.TILE_WIDTH);
        int yPos = location.y - (location.y % SnakeGame.TILE_HEIGHT);

        // xPos and yPos must be a multiple of TILE_WIDTH and TILE_HEIGHT correspondingly

        foodRectangle = new Rectangle(xPos, yPos, SnakeGame.TILE_WIDTH, SnakeGame.TILE_HEIGHT);
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillRect(foodRectangle.x, foodRectangle.y, foodRectangle.width, foodRectangle.height);
    }

    @Override
    public void update() {

    }

    public Rectangle getBounds() {
        return foodRectangle;
    }
}
