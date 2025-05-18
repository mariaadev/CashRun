package com.helena.maria.m8.uf3.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.helena.maria.m8.uf3.helpers.AssetManager;
import com.helena.maria.m8.uf3.utils.Settings;

public class Police extends Actor {
    private Thief thief;

    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;

    private Vector2 position;
    private Vector2 velocity;

    private final Vector2[] corners;
    private int currentCornerIndex;

    private int width, height;
    private int direction;

    private Rectangle collisionRect;

    private Animation<TextureRegion> animationLeft;
    private Animation<TextureRegion> animationRight;
    private float stateTime = 0f;

    private Vector2 target;

    /** Calcula nova velocitat cap a l’objectiu */
    private void updateVelocity() {
        velocity.set(target).sub(position).nor().scl(1.5f);
        direction = velocity.x > 0 ? DIRECTION_RIGHT : DIRECTION_LEFT;
    }

    private boolean patrolCorners;

    public Police(int width, int height, Thief thief) {
        this(width, height, thief, false, 0);
    }
    public Police( int width, int height, Thief thief,boolean patrolCorners, int startCornerIndex) {
        this.width = width;
        this.height = height;
        this.thief = thief;
        this.patrolCorners = patrolCorners;

        corners = new Vector2[] {
            new Vector2(30, 0), /* desplaçat en X per no coincidir amb el lladre */
            new Vector2(Settings.GAME_WIDTH - width, 0),
            new Vector2(Settings.GAME_WIDTH - width, Settings.GAME_HEIGHT - height),
            new Vector2(0, Settings.GAME_HEIGHT - height)
        };

        if (patrolCorners) {
            currentCornerIndex = startCornerIndex % corners.length;
            this.position = new Vector2(corners[currentCornerIndex]);
            target = corners[(currentCornerIndex + 1) % corners.length];
        } else {
            this.position = getRandomPoint();
            target = getRandomPoint();
        }

        this.velocity = new Vector2();
        this.collisionRect = new Rectangle();
        setBounds(position.x, position.y, width, height);

        animationLeft = AssetManager.policeAnimationLeft;
        animationRight = AssetManager.policeAnimationRight;

        target = corners[(currentCornerIndex + 1) % corners.length];
        updateVelocity(); // calcula direcció inicial
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        if (position.dst(target) < 2f) {
            if (patrolCorners) {
                currentCornerIndex = (currentCornerIndex + 1) % corners.length;
                target = corners[(currentCornerIndex + 1) % corners.length];
            } else {
                target = getRandomPoint();
            }
            updateVelocity();
        }

        position.add(velocity.x, velocity.y);

        position.x = MathUtils.clamp(position.x, 0, Settings.GAME_WIDTH - width);
        position.y = MathUtils.clamp(position.y, 0, Settings.GAME_HEIGHT - height);

        setBounds(position.x, position.y, width, height);
        collisionRect.set(position.x, position.y, width, height);


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion currentFrame = getCurrentFrame();
        batch.draw(currentFrame, position.x, position.y, width, height);
    }

    /** Retorna el frame animat segons direcció */
    private TextureRegion getCurrentFrame() {
        if (direction == DIRECTION_LEFT) {
            return animationLeft.getKeyFrame(stateTime, true);
        } else {
            return animationRight.getKeyFrame(stateTime, true);
        }
    }

    /** Escull un punt aleatori lluny del lladre */
    private Vector2 getRandomPoint() {
        Vector2 point;
        Rectangle thiefRect = new Rectangle(thief.getX(), thief.getY(), thief.getWidth(), thief.getHeight());

        Rectangle safeZone = new Rectangle(thiefRect);
        safeZone.setX(safeZone.getX() - 10);
        safeZone.setY(safeZone.getY() - 10);
        safeZone.setWidth(safeZone.getWidth() );
        safeZone.setHeight(safeZone.getHeight());

        Rectangle testRect = new Rectangle();
        int attempts = 0;

        do {
            float x = MathUtils.random(0, Settings.GAME_WIDTH - width);
            float y = MathUtils.random(0, Settings.GAME_HEIGHT - height);
            point = new Vector2(x, y);
            testRect.set(x, y, width, height);
            attempts++;
        } while (Intersector.overlaps(safeZone, testRect) && attempts < 100);

        return point;
    }



    public Rectangle getCollisionRect() {
        float insetX = width * 0.5f;
        float insetY = height * 0.5f;
        collisionRect.set(position.x + insetX, position.y + insetY,
            width - 2 * insetX, height - 2 * insetY);
        return collisionRect;
    }

    /** Detecta col·lisió amb el lladre */
    public boolean collides(Thief thief) {
        return Intersector.overlaps(this.collisionRect, thief.getCollisionRect());
    }

}
