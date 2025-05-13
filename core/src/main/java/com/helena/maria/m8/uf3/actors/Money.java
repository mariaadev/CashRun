package com.helena.maria.m8.uf3.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.helena.maria.m8.uf3.helpers.AssetManager;

public class Money extends Actor {

    private Rectangle bounds;
    private boolean collected = false;

    public Money(float x, float y, float width, float height) {
        setBounds(x, y, width, height);
        bounds = new Rectangle(x, y, width, height);
        setName("money");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!collected) {
            batch.draw(AssetManager.coins, getX(), getY(), getWidth(), getHeight());
        }
    }

    public boolean collides(Thief thief) {
        return !collected && bounds.overlaps(thief.getBounds());
    }

    public void collect() {
        collected = true;
        setVisible(false);
        AssetManager.pickMoney.play();
    }

    public boolean isCollected() {
        return collected;
    }
}
