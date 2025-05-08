package com.helena.maria.m8.uf3.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.helena.maria.m8.uf3.helpers.AssetManager;


public class Thief extends Actor {
    public static final int THIEF_LEFT = 0;
    public static final int THIEF_RIGHT = 1;

    private Vector2 position;
    private int width, height;
    private int direction = THIEF_RIGHT;
    private boolean isPaused = true;

    private Vector2 velocity = new Vector2();
    private float stateTime = 0;

    private Rectangle collisionRect;

    private Animation<TextureRegion> thiefAnimationLeft;
    private Animation<TextureRegion> thiefAnimationRight;

    private TextureRegion thiefPause;

    public Thief(float x, float y, int width, int height){
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        this.thiefAnimationLeft = AssetManager.thiefAnimationLeft;
        this.thiefAnimationRight = AssetManager.thiefAnimationRight;
        this.thiefPause = AssetManager.thief;

        collisionRect = new Rectangle();

        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);
    }

    public float getX() { return position.x; }
    public float getY() { return position.y; }
    public float getWidth(){ return width; }
    public float getHeight(){ return  height; }

    public Rectangle getCollisionRect(){
        collisionRect.set(position.x, position.y, width, height);
        return collisionRect;
    }


    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch, parentAlpha);
        batch.draw(getCurrentFrame(Gdx.graphics.getDeltaTime()), position.x, position.y, width,height);
    }

    public void move(float dx, float dy){
        velocity.set(dx, dy);
        isPaused = dx == 0 && dy == 0;

        if(dx > 0) direction = THIEF_RIGHT;
        else if(dx < 0) direction = THIEF_LEFT;

        position.add(dx, dy);
        setPosition(position.x, position.y);
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
}
