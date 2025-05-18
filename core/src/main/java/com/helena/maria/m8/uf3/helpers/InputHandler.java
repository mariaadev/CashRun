package com.helena.maria.m8.uf3.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
        Vector3 worldCoordinates = screen.getViewport().unproject(new Vector3(screenX, screenY, 0));
        thief.moveTo(worldCoordinates.x, worldCoordinates.y);
        return true;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }


    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        isTouchMoving = false;
        thief.move(0, 0);
        lastDragPos = null;
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
