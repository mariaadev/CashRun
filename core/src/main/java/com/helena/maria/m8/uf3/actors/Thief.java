package com.helena.maria.m8.uf3.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.helena.maria.m8.uf3.helpers.AssetManager;
import com.helena.maria.m8.uf3.utils.Settings;


public class Thief extends Actor {
    public static final int THIEF_LEFT = 0;
    public static final int THIEF_RIGHT = 1;

    private int width, height;
    private int direction = THIEF_RIGHT;
    private boolean isPaused = false;

    private Vector2 velocity = new Vector2();
    private float stateTime = 0;

    private Rectangle collisionRect;

    private Animation<TextureRegion> thiefAnimationLeft;
    private Animation<TextureRegion> thiefAnimationRight;

    private TextureRegion thiefPause;

    public Thief(float x, float y, int width, int height){
        this.width = width;
        this.height = height;

        this.thiefAnimationLeft = AssetManager.thiefAnimationLeft;
        this.thiefAnimationRight = AssetManager.thiefAnimationRight;
        this.thiefPause = AssetManager.thief;

        collisionRect = new Rectangle();

        setBounds(x, y, width, height);
        setTouchable(Touchable.enabled);
    }


    public float getWidth(){ return width; }
    public float getHeight(){ return  height; }

    public Rectangle getCollisionRect(){
        collisionRect.set(getX(), getY(), width, height);
        return collisionRect;
    }


    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch, parentAlpha);
        batch.draw(getCurrentFrame(Gdx.graphics.getDeltaTime()), getX(), getY(), getWidth(),getHeight());
    }

    public void move(float dx, float dy){
        Gdx.app.log("THIEF_MOVE", "dx=" + dx + ", dy=" + dy);
        velocity.set(dx, dy);
        isPaused = dx == 0 && dy == 0;

        if(dx > 0) direction = THIEF_RIGHT;
        else if(dx < 0) direction = THIEF_LEFT;

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx = -2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx = 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            dy = 2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            dy = -2;
        }

        if (dx != 0 || dy != 0) {
            isPaused = false;
            velocity.set(dx, dy);

            if (dx > 0) direction = THIEF_RIGHT;
            else if (dx < 0) direction = THIEF_LEFT;

            float newX = getX() + velocity.x;
            float newY = getY() + velocity.y;

            newX = Math.max(0, Math.min(newX, Settings.GAME_WIDTH - getWidth()));
            newY = Math.max(0, Math.min(newY, Settings.GAME_HEIGHT - getHeight()));

            setPosition(newX, newY);
        } else {
            isPaused = true;
        }

        collisionRect.set(getX(), getY(), getWidth(), getHeight());
    }


    public TextureRegion getCurrentFrame(float delta){
        stateTime += delta;

        if(isPaused){
            return  thiefPause;
        }

        return direction == THIEF_RIGHT
            ? thiefAnimationRight.getKeyFrame(stateTime, true)
            : thiefAnimationLeft.getKeyFrame(stateTime, true);


    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

}
