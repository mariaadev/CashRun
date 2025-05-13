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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.helena.maria.m8.uf3.CashRun;
import com.helena.maria.m8.uf3.actors.Money;
import com.helena.maria.m8.uf3.actors.Police;
import com.helena.maria.m8.uf3.actors.Thief;
import com.helena.maria.m8.uf3.helpers.AssetManager;
import com.helena.maria.m8.uf3.map.ChessBoardMap;
import com.helena.maria.m8.uf3.utils.Settings;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;

    Boolean gameOver = false;
    private Thief thief;

    Game game;

    private Array<Money> moneyList;
    private boolean gameWon = false;
    private boolean reachedEnd = false;
    private int moneyCollected = 0;


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

        Vector2[][] patrolPoints = {
            {new Vector2(0, 10), new Vector2(200, 10)},
            {new Vector2(220, 50), new Vector2(20, 50)},
            {new Vector2(100, 0), new Vector2(100, 120)},
            {new Vector2(150, 30), new Vector2(150, 90)},
            {new Vector2(50, 20), new Vector2(200, 20)},
            {new Vector2(0, 100), new Vector2(240, 100)},
            {new Vector2(30, 60), new Vector2(180, 60)}
        };

        for (int i = 0; i < patrolPoints.length; i++) {
            Vector2 start = patrolPoints[i][0];
            Vector2 end = patrolPoints[i][1];
            Police police = new Police(start, end, 100, 100);
            stage.addActor(police);
        }

        stage.addActor(thief);

        thief.setName("thief");


        moneyList = new Array<>();

        // Posiciones de dinero, ajusta según el mapa
        Vector2[] moneyPositions = {
            new Vector2(50, 50), new Vector2(150, 70), new Vector2(200, 100),
            new Vector2(100, 30), new Vector2(220, 10), new Vector2(20, 90), new Vector2(180, 60)
        };

        for (Vector2 pos : moneyPositions) {
            Money money = new Money(pos.x, pos.y, 40, 40);
            moneyList.add(money);
            stage.addActor(money);
        }


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

        // Dibuja fondo de tablero
        batch.begin();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                boolean isLight = (row + col) % 2 == 0;
                Texture tile = isLight ? lightTile : darkTile;
                batch.draw(tile, col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
        batch.end(); // <- FIN antes de que stage lo use

        // Lógica de movimiento y colisión
        if (!gameOver) {
            for (Actor actor : stage.getActors()) {
                if (actor instanceof Police) {
                    Police police = (Police) actor;
                    if (police.collides(thief)) {
                        AssetManager.gameOver.play();
                        thief.remove();
                        gameOver = true;
                        break;
                    }
                }
            }
        }

        if (!gameOver && !gameWon) {
            for (Money money : moneyList) {
                if (money.collides(thief)) {
                    money.collect();
                    moneyCollected++;
                }
            }

            // Comprobamos si el ladrón ha llegado al final del mapa
            if (moneyCollected > 0 && thief.getX() >= Settings.GAME_WIDTH - thief.getWidth()) {
                AssetManager.winner.play();
                gameWon = true;
            }
        }

        // Dibuja actores
        stage.act(delta);
        stage.draw();

        if (gameWon) {
            batch.begin();
            // Puedes usar BitmapFont aquí
            // font.draw(batch, "¡Victoria!", 100, 100);
            batch.end();
        }

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

    public Thief getThief(){ return thief; }
    public Stage getStage() { return stage; }

    public boolean isGameOver() {
        return gameOver;
    }

    public void restartGame() {
        game.setScreen(new GameScreen(game));  // Cambia la pantalla a ReadyScreen
    }
}
