package com.helena.maria.m8.uf3.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.helena.maria.m8.uf3.helpers.AssetManager;

public class Police extends Actor {

    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;

    private Vector2 position;
    private Vector2 startPoint;
    private Vector2 endPoint;
    private Vector2 velocity;

    private int width, height;
    private int direction;

    private Rectangle collisionRect;

    private Animation<TextureRegion> animationLeft;
    private Animation<TextureRegion> animationRight;
    private float stateTime = 0f;

    public Police(Vector2 start, Vector2 end, int width, int height) {
        this.startPoint = start;
        this.endPoint = end;
        this.position = new Vector2(start);
        this.width = width;
        this.height = height;

        this.collisionRect = new Rectangle();
        setBounds(position.x, position.y, width, height);

        velocity = new Vector2(end).sub(start).nor().scl(1.5f); // Ajusta velocidad aquí
        direction = velocity.x > 0 ? DIRECTION_RIGHT : DIRECTION_LEFT;

        animationLeft = AssetManager.policeAnimationLeft;
        animationRight = AssetManager.policeAnimationRight;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        position.add(velocity.x, velocity.y);

        if (reachedEnd()) {
            Vector2 temp = startPoint;
            startPoint = endPoint;
            endPoint = temp;

            velocity.set(endPoint).sub(startPoint).nor().scl(1.5f);
            direction = velocity.x > 0 ? DIRECTION_RIGHT : DIRECTION_LEFT;
        }

        setBounds(position.x, position.y, width, height);
        collisionRect.set(position.x, position.y, width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion currentFrame = getCurrentFrame();
        batch.draw(currentFrame, position.x, position.y, width, height);
    }

    private TextureRegion getCurrentFrame() {
        if (direction == DIRECTION_LEFT) {
            return animationLeft.getKeyFrame(stateTime, true);
        } else {
            return animationRight.getKeyFrame(stateTime, true);
        }
    }

    private boolean reachedEnd() {
        return position.dst(endPoint) < 2f;
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    public boolean collides(Thief thief) {
        return Intersector.overlaps(this.collisionRect, thief.getCollisionRect());
    }

}
