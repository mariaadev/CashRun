package com.helena.maria.m8.uf3.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.helena.maria.m8.uf3.actors.GameState;
import com.helena.maria.m8.uf3.helpers.AssetManager;

public class FinalScreen implements Screen {
    private final Game game;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private final Texture backgroundOverlay;

    private final GameState gameState;

    private final float centerX;
    private final float centerY;

    private final float iconSize = 400f;
    private final float thiefSize = iconSize * 2.2f;
    private final float policeSize = iconSize * 1.8f;
    private final float moneySize = iconSize * 1.5f;
    private final float titleSize = 2.5f;

    public FinalScreen(Game game, GameState gameState) {
        this.game = game;
        this.gameState = gameState;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Fondo overlay transparente negro (como en SplashScreen)
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.8f);
        pixmap.fill();
        backgroundOverlay = new Texture(pixmap);
        pixmap.dispose();

        // Fuente para mensaje final
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixelifySans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        centerX = Gdx.graphics.getWidth() / 2f;
        centerY = Gdx.graphics.getHeight() / 2f;

        // Procesar input para reiniciar juego (Enter o clic)
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
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(backgroundOverlay, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Según estado dibuja imagen y texto
        if (gameState == GameState.GAME_OVER) {
            batch.draw(AssetManager.titleGameover,
                centerX - AssetManager.titleGameover.getWidth() * titleSize / 2,
                centerY - AssetManager.titleGameover.getHeight() * titleSize / 2,
                AssetManager.titleGameover.getWidth() * titleSize,
                AssetManager.titleGameover.getHeight() * titleSize);

        } else if (gameState == GameState.WINNER) {
            batch.draw(AssetManager.titleWinner,
                centerX - AssetManager.titleWinner.getWidth() * titleSize / 2,
                centerY - AssetManager.titleWinner.getHeight() * titleSize / 2,
                AssetManager.titleWinner.getWidth() * titleSize,
                AssetManager.titleWinner.getHeight() * titleSize);

        }
        font.setColor(Color.WHITE);
        font.draw(batch, "Pulsa enter o clica para volver a jugar", centerX - 550, centerY - 250);

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
