package com.funkoding.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;

	Texture background;
    Texture[] birds;
    Texture aboveTube;
    Texture belowTube;

    float birdX=0;
    float birdY=0;

    int birdState = 0;
    int gameState = 0;

    float velocity ;


	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");

        birds = new Texture[2];
        birds[0] = new Texture("bird_state1.png");
        birds[1] = new Texture("bird_state2.png");
        birdX = Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2;
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        aboveTube = new Texture("above_tube.png");
        belowTube = new Texture("below_tube.png");

	}

	@Override
	public void render () {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState!=0) {

            batch.draw(aboveTube, Gdx.graphics.getWidth()/2 - aboveTube.getWidth()/2, Gdx.graphics.getHeight()/2 + 200 );
            batch.draw(belowTube, Gdx.graphics.getWidth()/2 - belowTube.getWidth()/2, Gdx.graphics.getHeight()/2 - belowTube.getHeight() - 200 );

            if (Gdx.input.justTouched()) {
                velocity -= 25;
            }

            if (birdY > 0 || velocity < 0) {
                velocity++;
                birdY -= velocity;
            }
        }else{
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }
        if (birdState == 0) {
            birdState = 1;
        } else {
            birdState = 0;
        }

        batch.draw(birds[birdState], birdX, birdY);
        batch.end();
	}

}
