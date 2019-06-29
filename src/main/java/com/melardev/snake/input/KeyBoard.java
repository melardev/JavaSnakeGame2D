package com.melardev.snake.input;

import com.melardev.snake.SnakePane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyBoard extends KeyAdapter {


    private final SnakePane controller;


    public KeyBoard(SnakePane snakePane) {
        this.controller = snakePane;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_END ||
                keyCode == KeyEvent.VK_C && e.isControlDown())
            controller.onKeyExit();

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_END ||
                keyCode == KeyEvent.VK_C && e.isControlDown())
            controller.onKeyExit();
        else if(keyCode == KeyEvent.VK_UP ||keyCode == KeyEvent.VK_W)
            controller.onKeyUpPressed();
        else if(keyCode == KeyEvent.VK_RIGHT ||keyCode == KeyEvent.VK_D)
            controller.onKeyRightPressed();
        else if(keyCode == KeyEvent.VK_DOWN ||keyCode == KeyEvent.VK_S)
            controller.onKeyDownPressed();
        else if(keyCode == KeyEvent.VK_LEFT ||keyCode == KeyEvent.VK_A)
            controller.onKeyLeftPressed();
    }

}
