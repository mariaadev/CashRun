package com.helena.maria.m8.uf3.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.helena.maria.m8.uf3.actors.enums.GameState;
import com.helena.maria.m8.uf3.helpers.AssetManager;

public class FinalScreen implements Screen {
    private final Game game;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private final Texture backgroundOverlay;

    private final GameState gameState;
    private final GameScreen gameScreen;
    private final float iconSize = 350f;
    private final float thiefSize = iconSize * 2.2f;
    private final float policeSize = iconSize * 1.8f;
    private final float moneySize = iconSize * 1.5f;
    private final float titleSize = 2.5f;

    public FinalScreen(Game game, GameState gameState, GameScreen gameScreen) {
        this.game = game;
        this.gameState = gameState;
        this.gameScreen = gameScreen;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.8f);
        pixmap.fill();
        backgroundOverlay = new Texture(pixmap);
        pixmap.dispose();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixelifySans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    game.setScreen(new GameScreen(game));
                    return true;
                }
                return false;
            }
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                return true;
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        gameScreen.render(delta);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(backgroundOverlay, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f ;

        float iconY = centerY;
        float spaceBetweenIcons = -400f;

        float thiefX = centerX - thiefSize / 2 + 200;
        float policeX = thiefX - spaceBetweenIcons - policeSize + 50;
        float moneyX = thiefX + thiefSize + spaceBetweenIcons;

        float thiefY = iconY + -100f;
        float policeY = iconY + -30f;
        float moneyY = iconY + -30;

        if (gameState == GameState.GAME_OVER) {
            batch.draw(AssetManager.policeRight[1], policeX, policeY, policeSize, policeSize);
            batch.draw(AssetManager.thief, thiefX, thiefY, thiefSize, thiefSize);

            batch.draw(AssetManager.titleGameover,
                centerX - AssetManager.titleGameover.getWidth() * titleSize / 2,
                centerY - AssetManager.titleGameover.getHeight() * titleSize / 2,
                AssetManager.titleGameover.getWidth() * titleSize,
                AssetManager.titleGameover.getHeight() * titleSize);

        } else if (gameState == GameState.WINNER) {
            float spacing = 0f;
            float totalWidth = iconSize * 5 + spacing * 4;
            float startX = centerX - totalWidth / 2f;
            batch.draw(AssetManager.coins, startX, iconY, moneySize, moneySize);
            batch.draw(AssetManager.moneyBack, startX + 350f, moneyY, moneySize, moneySize);

            batch.draw(AssetManager.thiefRight[0], startX + 550f, thiefY , thiefSize, thiefSize);

            batch.draw(AssetManager.coins, startX + + 1000f, moneyY, moneySize, moneySize);
            batch.draw(AssetManager.moneyBack, startX + 1350f, moneyY, moneySize, moneySize);


            batch.draw(AssetManager.titleWinner,
                centerX - AssetManager.titleWinner.getWidth() * 3f / 2,
                centerY - AssetManager.titleWinner.getHeight() * 3f / 2,
                AssetManager.titleWinner.getWidth() * 3f,
                AssetManager.titleWinner.getHeight() * 3f);

        }
        font.setColor(Color.WHITE);
        font.draw(batch, "Pulsa enter o clica para volver a jugar", centerX - 600, centerY - 250);

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
