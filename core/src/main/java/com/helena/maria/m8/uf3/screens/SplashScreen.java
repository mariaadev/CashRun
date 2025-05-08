package com.helena.maria.m8.uf3.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.helena.maria.m8.uf3.CashRun;
import com.helena.maria.m8.uf3.helpers.AssetManager;

public class SplashScreen implements Screen {
    private final CashRun game;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;

    private final Texture backgroundOverlay;

    private final BitmapFont font;

    private GameScreen gameScreen;

    private final float iconSize = 600f;
    private final float titleSize = 2.5f;

    public SplashScreen(final CashRun game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 1);
        pixmap.fill();
        backgroundOverlay = new Texture(pixmap);
        pixmap.dispose();


        font = new BitmapFont();
        gameScreen = new GameScreen();
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                game.setScreen(new GameScreen());
                return true;
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

            /*fons transparent*/
            batch.setColor(0, 0, 0, 0.7f);
            batch.draw(backgroundOverlay, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.setColor(1, 1, 1, 1);

            float centerX = Gdx.graphics.getWidth() / 2f;
            float centerY = Gdx.graphics.getHeight() / 2f;

            float iconY = centerY ;
            float iconSpacing = iconSize + 50f;

            batch.draw(AssetManager.policeRight[0], centerX - iconSpacing - iconSize / 2, iconY, iconSize, iconSize);
            batch.draw(AssetManager.thiefRight[0], centerX - iconSize / 2, iconY, iconSize, iconSize);
            batch.draw(AssetManager.coins, centerX + iconSpacing - iconSize / 2, iconY, iconSize, iconSize);

            batch.draw(AssetManager.titleCashRun,
                centerX - AssetManager.titleCashRun.getWidth() * titleSize / 2,
                centerY - AssetManager.titleCashRun.getHeight() * titleSize / 2,
                AssetManager.titleCashRun.getWidth() * titleSize,
                AssetManager.titleCashRun.getHeight() * titleSize);

        font.setColor(Color.WHITE);
            font.getData().setScale(2f);
            font.draw(batch, "Pulsa enter o clica para empezar", centerX - 150, centerY - 100);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundOverlay.dispose();
        font.dispose();
    }
}


