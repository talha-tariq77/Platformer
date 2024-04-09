package com.gdx.plat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.physics.box2d.*;
import jdk.tools.jlink.internal.plugins.StripNativeCommandsPlugin;

public class GdxGame extends ApplicationAdapter {
	public static World world;
	SpriteBatch batch;
	OrthographicCamera camera;
	Box2DDebugRenderer debugRenderer;

	public static Ground ground;
	Ball ball1;

	Player player;
//	Ball ball2;
//	Ball ball3;


	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();

		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

		camera.setToOrtho(false, Globals.VIEWPORT_WIDTH, Globals.VIEWPORT_HEIGHT);

		world = new World(new Vector2(0, Globals.GRAVITY), true);
		Listener contactListener = new Listener();
		world.setContactListener(contactListener);
		ground = new Ground();

		camera.position.set(0, Globals.VIEWPORT_HEIGHT / 2, 0);

		player = new Player(2, 5 + 1.7f + 2f);

		// h h of world + player height


//		System.out.println(ground.groundBody.getWorldPoint(ground.groundBody.getLocalCenter()));

//		ball1 = new Ball(3, Globals.VIEWPORT_HEIGHT, 1F);

//		ball2 = new Ball(Globals.VIEWPORT_WIDTH/2f, Globals.VIEWPORT_HEIGHT + 10, 1F);
//
//		ball3 = new Ball(Globals.VIEWPORT_WIDTH/2f, Globals.VIEWPORT_HEIGHT + 20, 1F);

		// use set and get user data when collision detecting using contact listeners?

		debugRenderer = new Box2DDebugRenderer();

//		groundBody.setUserData(ground);
//		System.out.println(((Ground) groundBody.getUserData()).helloWorld());
	}

	@Override
	public void render () {
//		camera.position.set(ball1.ballBody.getPosition(), 0);
		handleInput();
		update();

		ScreenUtils.clear(0, 0, 0, 0);
		camera.update();
		debugRenderer.render(world, camera.combined);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.end();


		world.step(1/60f, 2, 6);
		debuggingInfo();
	}
// camera should be in center

	private void debuggingInfo() {
	}

	private void update() {
		player.update();
	}
	private void handleInput() {
//		System.out.println(ball1.ballBody.getLinearVelocity());
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			camera.zoom += 0.02;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.T)) {
			camera.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-1, 0);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(1, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, 1);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -1);
		}
//		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//			ball1.ballBody.applyForceToCenter( new Vector2(1, 0), true);
//		}

		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			player.moveX(1);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			player.moveX(-1);
		}
		else {
			player.playerBody.setLinearVelocity(0f, player.playerBody.getLinearVelocity().y);
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
