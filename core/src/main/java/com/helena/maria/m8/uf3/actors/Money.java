package com.helena.maria.m8.uf3.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.helena.maria.m8.uf3.helpers.AssetManager;

public class Money extends Actor {

    private Rectangle bounds;
    private boolean collected = false;
    private MoneyType type;
    private TextureRegion texture;
    private int value;

    public Money(float x, float y, float width, float height, MoneyType type) {
        this.type = type;

        switch (type) {
            case COIN:
                texture = AssetManager.coins;
                value = 1000;
                break;
            case MONEYBAG:
                texture = AssetManager.moneyBack;
                value = 100;
                /*ajustar mida perquè l'imatge és més petita*/
                width *= 1.4f;
                height *= 1.4f;
                break;
            case GOLD:
                texture = AssetManager.gold;
                value = 5000;
                break;
        }
        setBounds(x, y, width, height);
        bounds = new Rectangle(x, y, width, height);
        setName("money");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!collected) {
            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        }
    }

    public boolean collides(Thief thief) {
        return !collected && bounds.overlaps(thief.getBounds());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        bounds.set(getX(), getY(), getWidth(), getHeight());
    }

    public void collect() {
        collected = true;
        setVisible(false);
        AssetManager.pickMoney.play();
    }

    public boolean isCollected() {
        return collected;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getValue() {
        return value;
    }

    public MoneyType getType() {
        return type;
    }
}
