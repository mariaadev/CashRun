package com.helena.maria.m8.uf3.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.helena.maria.m8.uf3.CashRun;
import com.helena.maria.m8.uf3.actors.GameState;
import com.helena.maria.m8.uf3.actors.Money;
import com.helena.maria.m8.uf3.actors.MoneyType;
import com.helena.maria.m8.uf3.actors.Police;
import com.helena.maria.m8.uf3.actors.Thief;
import com.helena.maria.m8.uf3.helpers.AssetManager;
import com.helena.maria.m8.uf3.helpers.InputHandler;
import com.helena.maria.m8.uf3.map.ChessBoardMap;
import com.helena.maria.m8.uf3.utils.Settings;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Iterator;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;

    private OrthographicCamera camera;
    private StretchViewport    viewport;

    Boolean gameOver = false;
    private Thief thief;

    Game game;

    private Array<Money> moneyList;
    private boolean gameWon = false;
    private boolean reachedEnd = false;
    private int moneyCollected = 0;

    private BitmapFont font;

    private Texture lightTile, darkTile;

    private GameState gameState = GameState.RUNNING;

    public GameScreen(Game game){
        AssetManager.music.play();

        this.game = game;
        camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        camera.setToOrtho(false);
        viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);

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

        for (int i = 0; i < 5; i++) {
            Police police = new Police(Settings.POLICE_WIDTH, Settings.POLICE_HEIGHT, thief);
            stage.addActor(police);
        }



        stage.addActor(thief);

        thief.setName("thief");


        moneyList = new Array<>();

        MoneyType[] types = {MoneyType.COIN, MoneyType.MONEYBAG, MoneyType.GOLD};
        Vector2[] positions = {
            new Vector2(40, 72),
            new Vector2(81, 12),
            new Vector2(84, 48),
            new Vector2(150, 28),
            new Vector2(170, 1),
            new Vector2(40, 5),
            new Vector2(150, 72),
            new Vector2(192, 45),
            new Vector2(117, 81),
            new Vector2(29, 38)
        };

        for (int i = 0; i < positions.length; i++) {
            MoneyType type = types[i % types.length];
            Money money = new Money(positions[i].x, positions[i].y, 20, 20, type);
            moneyList.add(money);
            stage.addActor(money);
        }


        lightTile = ChessBoardMap.generateTile(Color.LIGHT_GRAY);
        darkTile = ChessBoardMap.generateTile(Color.DARK_GRAY);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixelifySans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 8;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        setupInput();
    }
    private void setupInput() {
        Gdx.app.log("M8INPUT", "setupInput llamado");
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new InputHandler(this));
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.app.log("M8INPUT", "setInputProcessor hecho");
    }

    @Override
    public void show() {
        setupInput();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        float cols = 22f;
        float rows = 10f;

        float screenWidth = viewport.getWorldWidth();
        float screenHeight = viewport.getWorldHeight();

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

        /*Lògica col·lisions*/
        if (gameState == GameState.RUNNING) {
            for (Actor actor : stage.getActors()) {
                if (actor instanceof Police) {
                    Police police = (Police) actor;
                    if (police.collides(thief)) {
                        AssetManager.gameOver.play();
                        thief.remove();
                        gameState = GameState.GAME_OVER;
                        game.setScreen(new FinalScreen(game, gameState, this));
                        return;
                    }
                }
            }
        }

        if (gameState == GameState.RUNNING) {
            Iterator<Money> iterator = moneyList.iterator();
            while (iterator.hasNext()) {
                Money money = iterator.next();
                if (money.collides(thief)) {
                    money.collect();
                    moneyCollected += money.getValue();
                    iterator.remove();
                }
            }

            if (moneyCollected > 0 && thief.getX() >= Settings.GAME_WIDTH - thief.getWidth()) {
                AssetManager.winner.play();
                gameState = GameState.WINNER;
                game.setScreen(new FinalScreen(game, gameState, this));
                return;
            }

            stage.act(delta);

    }

        stage.draw();
        batch.begin();

        /*RUNNING*/
        String scoreText = moneyCollected + " PTS";
        GlyphLayout layout = new GlyphLayout(font, scoreText);
        float x = Settings.GAME_WIDTH - layout.width - 5;
        float y = Settings.GAME_HEIGHT - 8;
        font.draw(batch, layout, x, y);


        batch.end();

    }





    @Override public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        lightTile.dispose();
        darkTile.dispose();
        font.dispose();

    }

    public Thief getThief(){ return thief; }
    public Stage getStage() { return stage; }

    public boolean isGameOver() {
        return gameOver;
    }

    public void restartGame() {
        game.setScreen(new GameScreen(game));

    }

    public Viewport getViewport() {
        return viewport;
    }

}
