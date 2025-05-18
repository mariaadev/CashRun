package com.helena.maria.m8.uf3.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.helena.maria.m8.uf3.actors.Thief;
import com.helena.maria.m8.uf3.screens.GameScreen;

public class InputHandler implements InputProcessor {

    private Thief thief;
    private GameScreen screen;
    private Stage stage;

    private Vector2 lastDragPos = null;
    private boolean isTouchMoving = false;

    private Vector2 touchStart;

    // Ajusta aquí la velocidad base
    private static final float SPEED = 1.2f;

    public InputHandler(GameScreen screen){
        this.screen = screen;
        this.thief = screen.getThief();
        this.stage = screen.getStage();
        // Puedes ajustar la velocidad global desde aquí si quieres
        thief.setSpeed(SPEED);
    }

    @Override
    public boolean keyDown(int keycode) { return false; }
    @Override
    public boolean keyUp(int keycode) { return false; }
    @Override
    public boolean keyTyped(char character) { return false; }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("M8INPUT", "touchDown: screenX=" + screenX + ", screenY=" + screenY);
        lastDragPos = new Vector2(screenX, screenY);
        touchStart = new Vector2(screenX, screenY);
        isTouchMoving = true;

        thief.move(0, 0);

        if(screen.isGameOver()){
            screen.restartGame();
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        thief.move(0, 0);

        // Swipe (opcional, si quieres mantener swipe, puedes dejar este bloque)
        if (touchStart != null) {
            Vector2 touchEnd = new Vector2(screenX, screenY);
            Vector2 swipe = touchEnd.cpy().sub(touchStart);

            if (swipe.len() > 50) {
                float dx = 0;
                float dy = 0;

                if (Math.abs(swipe.x) > Math.abs(swipe.y)) {
                    dx = swipe.x > 0 ? 1 : -1;
                } else {
                    dy = swipe.y > 0 ? -1 : 1;
                }

                thief.move(dx, dy);

                // Detenerse luego de un pequeño tiempo si es swipe (movimiento único)
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        thief.move(0, 0);
                    }
                }, 0.2f);
            }
        }
        touchStart = null;
        isTouchMoving = false;
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        isTouchMoving = false;
        thief.move(0, 0);
        lastDragPos = null;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Gdx.app.log("M8INPUT", "touchDragged: screenX=" + screenX + ", screenY=" + screenY);

        float dx = screenX - lastDragPos.x;
        float dy = screenY - lastDragPos.y;

        // Ajusta sensibilidad y dirección
        float threshold = 4f;
        float moveX = 0, moveY = 0;
        if (Math.abs(dx) > threshold) moveX = (dx > 0) ? 1 : -1;
        if (Math.abs(dy) > threshold) moveY = (dy < 0) ? 1 : -1; // Y invertido

        Gdx.app.log("TOUCH_DRAGGED", "dx=" + dx + " dy=" + dy + " -> moveX=" + moveX + " moveY=" + moveY);

        thief.move(moveX, moveY);
        lastDragPos.set(screenX, screenY);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
