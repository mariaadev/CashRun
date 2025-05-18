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

    /** Rectangle per gestionar col·lisions */
    private Rectangle collisionRect;

    private Animation<TextureRegion> thiefAnimationLeft;
    private Animation<TextureRegion> thiefAnimationRight;

    private TextureRegion thiefPause;

    /** Velocitat de moviment */
    private float speed = 1.2f;

    private Vector2 targetPosition = null;
    /** tolerància per aturar-se prop del dest**/
    private static final float TOLERANCE = 1.0f;

    public Thief(float x, float y, int width, int height){
        this.width = width;
        this.height = height;

        this.thiefAnimationLeft = AssetManager.thiefAnimationLeft;
        this.thiefAnimationRight = AssetManager.thiefAnimationRight;
        this.thiefPause = AssetManager.thief;
        isPaused = true;
        collisionRect = new Rectangle();

        /** Defineix posició i mida de l'actor a l’escenari */
        setBounds(x, y, width, height);
        setTouchable(Touchable.enabled);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public float getSpeed() {
        return speed;
    }

    public float getWidth(){ return width; }
    public float getHeight(){ return  height; }

    public Rectangle getCollisionRect(){
        /** Defineix una hitbox més petit per col·lisions més realistes */
        float insetX = width * 0.6f;
        float insetY = height * 0.6f;
        collisionRect.set(getX() + insetX, getY() + insetY,
            width - 2 * insetX, height - 2 * insetY);

        return collisionRect;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch, parentAlpha);
        batch.draw(getCurrentFrame(Gdx.graphics.getDeltaTime()), getX(), getY(), getWidth(),getHeight());
    }

    public void moveTo(float x, float y) {
        targetPosition = new Vector2(x - width / 2f, y - height / 2f);
    }

    /**
     * dx y dy deben ser valores como -1, 0, 1 (o fracciones).
     * La velocidad real aplicada será dx*speed, dy*speed.
     */
    public void move(float dx, float dy){
        Gdx.app.log("THIEF_MOVE", "dx=" + dx + ", dy=" + dy + " (speed=" + speed + ")");
        velocity.set(dx * speed, dy * speed);
        isPaused = dx == 0 && dy == 0;

        if (dx > 0.1f) direction = THIEF_RIGHT;
        else if (dx < -0.1f) direction = THIEF_LEFT;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        /** Captura moviment amb teclat */
        float dx = 0, dy = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) dx = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) dx = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) dy = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) dy = -1;

        if (dx != 0 || dy != 0) {
            /** Cancel·la moviment automàtic si hi ha moviment manual */
            targetPosition = null;
            move(dx, dy);
        } else if (targetPosition != null) {
            /** Si hi ha destí fixat, mou cap a ell */
            Vector2 currentPosition = new Vector2(getX(), getY());
            Vector2 direction = targetPosition.cpy().sub(currentPosition);
            float distance = direction.len();

            if (distance < TOLERANCE) {
                targetPosition = null;
                move(0, 0);
            } else {
                direction.nor();
                move(direction.x, direction.y);

            }
        }  else {
            move(0, 0);
        }

        /** Aplica moviment */
        moveBy(velocity.x, velocity.y);
        collisionRect.set(getX(), getY(), getWidth(), getHeight());
    }


    /** Retorna el frame actual per mostrar segons moviment/pausa */
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
