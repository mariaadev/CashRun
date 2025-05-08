package com.helena.maria.m8.uf3.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.helena.maria.m8.uf3.CashRun;
import com.helena.maria.m8.uf3.actors.Thief;
import com.helena.maria.m8.uf3.helpers.AssetManager;
import com.helena.maria.m8.uf3.map.ChessBoardMap;
import com.helena.maria.m8.uf3.utils.Settings;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;

    Boolean gameOver = false;
    private Thief thief;

    Game game;

    private Texture lightTile, darkTile;

    public GameScreen(Game game){
        AssetManager.music.play();

        this.game = game;

        OrthographicCamera camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        camera.setToOrtho(true);

        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);

        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);

        thief = new Thief(Settings.THIEF_STARTX, Settings.THIEF_STARTY,
            Settings.THIEF_WIDTH, Settings.THIEF_HEIGHT);

        stage.addActor(thief);

        thief.setName("thief");


        lightTile = ChessBoardMap.generateTile(Color.LIGHT_GRAY);
        darkTile = ChessBoardMap.generateTile(Color.DARK_GRAY);

    }

    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float cols = 22f;
        float rows = 10f;

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float tileSizeX = screenWidth / cols;
        float tileSizeY = screenHeight / rows;

        float tileSize = Math.min(tileSizeX, tileSizeY);

        float scaleFactor = 1.05f;

        tileSize *= scaleFactor;

        batch.begin();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                boolean isLight = (row + col) % 2 == 0;
                Texture tile = isLight ? lightTile : darkTile;
                batch.draw(tile, col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }


        batch.end();

        stage.act(delta);
        stage.draw();
    }





    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        lightTile.dispose();
        darkTile.dispose();
    }
}
