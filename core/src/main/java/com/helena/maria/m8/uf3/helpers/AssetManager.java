package com.helena.maria.m8.uf3.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

public class AssetManager {

    public static Texture sheetThiefLeft;
    public static Texture sheetThiefRight;
    public static Texture sheetPoliceLeft;
    public static Texture sheetPoliceRight;

    public static TextureRegion[] thiefLeft;
    public static TextureRegion[] thiefRight;
    public static TextureRegion[] policeLeft;
    public static TextureRegion[] policeRight;

    public static Animation thiefAnimationLeft;
    public static Animation thiefAnimationRight;
    public static Animation policeAnimationLeft;
    public static Animation policeAnimationRight;

    public static Texture sheetMoney;

    public static TextureRegion coins;
    public static TextureRegion moneyBack;
    public static TextureRegion gold;

    public static Texture thiefTexture;
    public static TextureRegion thief;

    public static Sound pickMoney;
    public static Music gameOver;
    public static Music music;
    public static Music winner;


    public static void load(){
        sheetThiefLeft = new Texture(Gdx.files.internal("images/thief/thiefLeft.png"));
        sheetThiefLeft.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        sheetThiefRight = new Texture(Gdx.files.internal("images/thief/thiefRight.png"));
        sheetThiefRight.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        sheetPoliceLeft = new Texture(Gdx.files.internal("images/police/policeLeft.png"));
        sheetPoliceLeft.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        sheetPoliceRight = new Texture(Gdx.files.internal("images/police/policeRight.png"));
        sheetPoliceRight.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        sheetMoney = new Texture(Gdx.files.internal("images/money.png"));
        sheetMoney.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        coins = new TextureRegion(sheetMoney, 0, 0, 100, 100);
        gold = new TextureRegion(sheetMoney, 100, 0, 100, 100);
        moneyBack = new TextureRegion(sheetMoney, 200, 0, 100, 100);

        thiefTexture = new Texture(Gdx.files.internal("images/thief/pjstanding.png"));
        thiefTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        thief = new TextureRegion(thiefTexture);

        /* Aqui separem els sprites i fem les animacions */
        thiefLeft = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            thiefLeft[i] = new TextureRegion(sheetThiefLeft, i * 100, 0, 100, 100);
        }

        thiefAnimationLeft = new Animation<TextureRegion>(0.1f, thiefLeft);
        thiefAnimationLeft.setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        thiefRight = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            thiefRight[i] = new TextureRegion(sheetThiefRight, i * 100, 0, 100, 100);
        }

        thiefAnimationRight = new Animation<TextureRegion>(0.1f, thiefRight);
        thiefAnimationRight.setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        policeLeft = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            policeLeft[i] = new TextureRegion(sheetPoliceLeft, i * 100, 0, 100, 100);
        }

        policeAnimationLeft = new Animation<TextureRegion>(0.1f, policeLeft);
        policeAnimationLeft.setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        policeRight = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            policeRight[i] = new TextureRegion(sheetPoliceRight, i * 100, 0, 100, 100);
        }

        policeAnimationRight = new Animation<TextureRegion>(0.1f, policeRight);
        policeAnimationRight.setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        pickMoney = Gdx.audio.newSound(Gdx.files.internal("sounds/moneypick.mp3"));
        gameOver = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameover.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        winner = Gdx.audio.newMusic(Gdx.files.internal("sounds/winner.mp3"));

        music.setVolume(0.2f);
        gameOver.setVolume(0.2f);
        winner.setVolume(0.2f);
        music.setLooping(true);
        gameOver.setLooping(true);
        winner.setLooping(true);
    }

    public static void dispose(){
        sheetMoney.dispose();
        sheetPoliceRight.dispose();
        sheetPoliceLeft.dispose();
        sheetThiefLeft.dispose();
        sheetThiefRight.dispose();
        pickMoney.dispose();
        gameOver.dispose();
        music.dispose();
        winner.dispose();
        thiefTexture.dispose();
    }
}
