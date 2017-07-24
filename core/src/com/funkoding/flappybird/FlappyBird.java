package com.funkoding.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;

    Texture background;
    Texture[] birds;
    Texture aboveTube;
    Texture belowTube;
    Texture gameOver;

    Circle birdCircle;

    int score = 0;
    int scoringTube = 0;

    BitmapFont font;


    float birdX = 0;
    float birdY = 0;

    int birdState = 0;
    int gameState = 0;

    float velocity;

    float maxTubeOffset;

    Random randomGenerator;

    float tubeVelocity = 4;

    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];

    float distanceBetweenTubes;

    Rectangle[] topRectangle;
    Rectangle[] belowRectangle;


    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png");

        birds = new Texture[2];
        birds[0] = new Texture("bird_state1.png");
        birds[1] = new Texture("bird_state2.png");
        gameOver = new Texture("gameOver.png");

        birdCircle = new Circle();


        aboveTube = new Texture("above_tube.png");
        belowTube = new Texture("below_tube.png");

        maxTubeOffset = Gdx.graphics.getHeight() / 2 - 200 - 100;

        randomGenerator = new Random();

        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

        topRectangle = new Rectangle[numberOfTubes];
        belowRectangle = new Rectangle[numberOfTubes];

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        startGame();
    }


    private void startGame() {
        birdX = Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2;
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200 - 200);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - aboveTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            topRectangle[i] = new Rectangle();
            belowRectangle[i] = new Rectangle();
        }

    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
                score++;

                if (scoringTube < numberOfTubes - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                velocity -= 25;

            }

            for (int i = 0; i < numberOfTubes; i++) {
                if (tubeX[i] < -aboveTube.getWidth()) {
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200 - 200);
                } else {
                    tubeX[i] -= tubeVelocity;

                }

                batch.draw(aboveTube, tubeX[i], Gdx.graphics.getHeight() / 2 + 200 + tubeOffset[i]);
                batch.draw(belowTube, tubeX[i], Gdx.graphics.getHeight() / 2 - belowTube.getHeight() - 200 + tubeOffset[i]);

                topRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + 200 + tubeOffset[i], aboveTube.getWidth(), aboveTube.getHeight());
                belowRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - 200 - belowTube.getHeight() + tubeOffset[i], belowTube.getWidth(), belowTube.getHeight());
            }
            if (birdY > 0) {
                velocity++;
                birdY -= velocity;
            } else {
                gameState = 2;
            }
        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 2) {
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
            if (Gdx.input.justTouched()) {
                gameState = 1;

                startGame();
                scoringTube = 0;
                score = 0;
                velocity = 0;
            }
        }
        if (birdState == 0) {
            birdState = 1;
        } else {
            birdState = 0;
        }

        batch.draw(birds[birdState], birdX, birdY);


        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[birdState].getHeight() / 2, birds[birdState].getWidth() / 2);

        font.draw(batch, String.valueOf(score), 100, 200);

        for (int i = 0; i < numberOfTubes; i++) {
            if (Intersector.overlaps(birdCircle, topRectangle[i]) || Intersector.overlaps(birdCircle, belowRectangle[i])) {
                gameState = 2;
            }
        }
        batch.end();
    }

}
