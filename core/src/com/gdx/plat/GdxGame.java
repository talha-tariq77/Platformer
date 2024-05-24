package com.gdx.plat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

	public static TextureAtlas atlas;
	Animation<TextureRegion> animation;
	float stateTime;

	boolean cameraFollow;
//	Ball ball2;
//	Ball ball3;


	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();

		atlas = new TextureAtlas(Gdx.files.internal("animations/animations_packed.atlas"));

		animation = new Animation<TextureRegion>(1/8f, atlas.findRegions("idle"), Animation.PlayMode.NORMAL);
//		System.out.println(animation.getKeyFrames().length);
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

		camera.setToOrtho(false, Globals.VIEWPORT_WIDTH, Globals.VIEWPORT_HEIGHT);

		world = new World(new Vector2(0, Globals.GRAVITY * Globals.GRAVITY_MULTIPLIER), true);
		Listener contactListener = new Listener();
		world.setContactListener(contactListener);
		ground = new Ground();

		camera.position.set(0, Globals.VIEWPORT_HEIGHT / 2, 0);
// 5 + 1.7f + 2f
		player = new Player(0f, 0f + Globals.PLAYER_HEIGHT);


		debugRenderer = new Box2DDebugRenderer();
		cameraFollow = false;
		stateTime = 0f;
	}

	public void updateCamera() {
		if (player.playerBody.isAwake()) {
//			float extreme_x = player.playerBody.getWorldPoint(new Vector2(Globals.PLAYER_WIDTH/2f, 0f)).x;
			if (player.playerBody.getWorldCenter().x > camera.position.x && cameraFollow) {
				camera.position.set(player.playerBody.getWorldCenter().x, camera.position.y, camera.position.z);
			}
		}
	}

	private void debuggingInfo() {
		System.out.println(player.playerBody.getLinearVelocity());
	}

	@Override
	public void render () {
		handleInput();
		update();
		updateCamera();
		stateTime += Gdx.graphics.getDeltaTime();

		TextureRegion frame = player.currAnimation.getKeyFrame(stateTime, true);


		ScreenUtils.clear(0, 0, 0, 0);
		camera.update();
		debugRenderer.render(world, camera.combined);
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(frame, player.playerBody.getWorldCenter().x - Globals.PLAYER_WIDTH/2f - Globals.SKIN_WIDTH/2f, player.playerBody.getWorldCenter().y - Globals.PLAYER_HEIGHT/2f - Globals.SKIN_HEIGHT/2f,
				Globals.PLAYER_WIDTH + Globals.SKIN_WIDTH, Globals.PLAYER_HEIGHT + Globals.SKIN_HEIGHT);
		batch.end();


		world.step(1/60f, 2, 6);
		debuggingInfo();
	}
// camera should be in center



	private void update() {
		player.update();
	}
	private void handleInput() {
		float move_diff = 0.05f;
		if (Gdx.input.isKeyPressed(Input.Keys.O)) {
			move_diff += 0.05f;
//			System.out.println("move diff");
//			System.out.println((float) move_diff);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			move_diff -= 0.05f;
//			System.out.println("move diff");
//			System.out.println((float) move_diff);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.F))
			cameraFollow = !cameraFollow;

		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			camera.zoom += 0.02;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.T)) {
			camera.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-move_diff, 0);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(move_diff, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, move_diff);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -move_diff);
		}
//		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//			ball1.ballBody.applyForceToCenter( new Vector2(1, 0), true);
//		}


		if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
			player.moveX(1);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
			player.moveX(-1);
		}
		if ((!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) || (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D))) {
			player.xStationary();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			player.jump();
		}

//		if (player.playerBody.getLinearVelocity().y == 0) {
//			player.airborne = false;
//		}
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
