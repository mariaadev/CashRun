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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class SplashScreen implements Screen {
    private final CashRun game;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;

    private final Texture backgroundOverlay;

    private final BitmapFont font;

    private GameScreen gameScreen;

    private final float iconSize = 400f;
    private final float thiefSize = iconSize * 2.2f;
    private final float policeSize = iconSize * 1.8f;
    private final float moneySize = iconSize * 1.5f;
    private final float titleSize = 2.5f;

    public SplashScreen(final CashRun game) {
        AssetManager.music.play();
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.5f);
        pixmap.fill();
        backgroundOverlay = new Texture(pixmap);
        pixmap.dispose();


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixelifySans.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 70;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        gameScreen = new GameScreen(game);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                Gdx.input.setInputProcessor(null);
                return true;
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0.3f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        /*fons transparent*/
        batch.setColor(0, 0, 0, 0.5f);
        batch.draw(backgroundOverlay, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(1, 1, 1, 1);

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        float iconY = centerY;
        float spaceBetweenIcons = -90f;

        float thiefX = centerX - thiefSize / 2;
        float policeX = thiefX - spaceBetweenIcons - policeSize;
        float moneyX = thiefX + thiefSize + spaceBetweenIcons;

        float thiefY = iconY + -155f;
        float policeY = iconY + -65f;
        float moneyY = iconY + -30;

        batch.draw(AssetManager.policeRight[0], policeX, policeY, policeSize, policeSize);
        batch.draw(AssetManager.thiefRight[0], thiefX, thiefY, thiefSize, thiefSize);
        batch.draw(AssetManager.coins, moneyX, moneyY, moneySize, moneySize);


        batch.draw(AssetManager.titleCashRun,
                centerX - AssetManager.titleCashRun.getWidth() * titleSize / 2,
                centerY - AssetManager.titleCashRun.getHeight() * titleSize / 2,
                AssetManager.titleCashRun.getWidth() * titleSize,
                AssetManager.titleCashRun.getHeight() * titleSize);

            font.setColor(Color.WHITE);
            font.draw(batch, "Pulsa enter o clica para empezar", centerX - 550, centerY - 250);

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


