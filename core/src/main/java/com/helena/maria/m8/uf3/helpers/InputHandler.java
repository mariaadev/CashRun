package com.helena.maria.m8.uf3.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.helena.maria.m8.uf3.actors.Thief;
import com.helena.maria.m8.uf3.screens.GameScreen;

public class InputHandler implements InputProcessor {

    private Thief thief;
    private GameScreen screen;
    private Stage stage;

    private Vector2 stageCoord;
    private int previousX = 0;
    private int previousY = 0;

    public InputHandler(GameScreen screen){
        this.screen = screen;
        this.thief = screen.getThief();
        this.stage = screen.getStage();
    }
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.W:
                thief.move(0, 2);
                break;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                thief.move(0, -2);
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                thief.move(-2, 0);
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                thief.move(2, 0);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        thief.move(0, 0);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        previousX = screenX;
        previousY = screenY;

        stageCoord = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
        Actor actorHit = stage.hit(stageCoord.x, stageCoord.y, true);
        if(actorHit != null){
            Gdx.app.log("HIT", actorHit.getName());
        }

        if(screen.isGameOver()){
            screen.restartGame();
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        thief.move(0, 0);
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        thief.move(0, 0);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        int deltaX = screenX - previousX;
        int deltaY = screenY - previousY;

        float dx = 0;
        float dy = 0;

        if (Math.abs(deltaX) > 2) {
            dx = (deltaX > 0) ? 2 : -2;
            previousX = screenX;
        }

        if (Math.abs(deltaY) > 2) {
            dy = (deltaY > 0) ? -2 : 2;
            previousY = screenY;
        }

        if (dx != 0 || dy != 0) {
            thief.move(dx, dy);
        }

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
