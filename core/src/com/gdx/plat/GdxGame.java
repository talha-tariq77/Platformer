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

	public TextureAtlas atlas;

	float stateTime;

	boolean cameraFollow;
	TextureRegion frame;
//	Ball ball2;
//	Ball ball3;

	InputHandler inputHandler;


	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();

		atlas = new TextureAtlas(Gdx.files.internal("animations/animations_packed.atlas"));

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
		player = new Player(0f, 0f + Globals.PLAYER_HEIGHT, 0f);

		stateTime = 0f;

		player.animations.put(Player.State.ATTACKING , new ExtraAnimation<TextureRegion>(4/30f, atlas.findRegions("attack")));
		player.looping.put(Player.State.ATTACKING, false);
		player.callTime.put(Player.State.ATTACKING, null);

		player.animations.put(Player.State.AIRBORNE_AND_ATTACKING , new ExtraAnimation<TextureRegion>(4/30f, atlas.findRegions("attack")));
		player.looping.put(Player.State.AIRBORNE_AND_ATTACKING, false);
		player.callTime.put(Player.State.AIRBORNE_AND_ATTACKING, null);


		player.animations.put(Player.State.MOVING_AND_ATTACKING , new ExtraAnimation<TextureRegion>(4/30f, atlas.findRegions("attack")));
		player.looping.put(Player.State.MOVING_AND_ATTACKING, false);
		player.callTime.put(Player.State.MOVING_AND_ATTACKING, null);


		player.animations.put(Player.State.AIRBORNE, new ExtraAnimation<TextureRegion>(1/8f, atlas.findRegions("jumping")));
		player.looping.put(Player.State.AIRBORNE, true);
		player.callTime.put(Player.State.AIRBORNE, null);

		player.animations.put(Player.State.MOVING_AND_AIRBORNE, new ExtraAnimation<TextureRegion>(1/8f, atlas.findRegions("jumping")));
		player.looping.put(Player.State.MOVING_AND_AIRBORNE, true);
		player.callTime.put(Player.State.AIRBORNE_AND_ATTACKING, null);


		player.animations.put(Player.State.MOVING, new ExtraAnimation<TextureRegion>(1/8f, atlas.findRegions("running")));
		player.looping.put(Player.State.MOVING, true);

		player.animations.put(Player.State.IDLE, new ExtraAnimation<TextureRegion>(1/8f, atlas.findRegions("idle")));
		player.looping.put(Player.State.IDLE, true);
		player.callTime.put(Player.State.IDLE, stateTime);

		player.animations.put(Player.State.NO_STATE, new ExtraAnimation<TextureRegion>(1/8f, atlas.findRegions("idle")));
		player.looping.put(Player.State.NO_STATE, true);



		// airborne (e.g. knockback, and jumping will likely have different

		// make animations require one for each state ?

		debugRenderer = new Box2DDebugRenderer();
		cameraFollow = false;
		// build a custom animations class

		inputHandler = new InputHandler(player);
		Gdx.input.setInputProcessor(inputHandler);
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
//		System.out.println(player.currState);
//		System.out.println(player.animations.get(player.currState));
//		System.out.println(player.playerBody.getLinearVelocity());
	}

	@Override
	public void render () {
		stateTime += Gdx.graphics.getDeltaTime();
		handleInput(stateTime);
		update();
		updateCamera();


		// Animation.isAnimationFinished() just checks if 1 singular animation has finished

		// could do a combinatorial

		player.currStateTime += Gdx.graphics.getDeltaTime();


//		if (stateTime - player.startTime >= player.animations.get(player.currState).getAnimationDuration() && player.currState == Player.State.ATTACKING) {
//			player.attacking = false;
//		}



		// when non-looping state is over
		// returns to a default, or some other state

		// can call player.default

		// calls player's state manager's defaulting state
		// state manager
		// default
		// bool -> state
		// state list to bools

		frame = player.getFrame();

//		if (player.looping.get(player.currState)) {
//			frame = player.animations.get(player.currState).getKeyFrame(player.currStateTime,
//					player.looping.get(player.currState));
//		}
//		else {
//			frame = player.animations.get(player.currState).getKeyFrame(stateTime - player.startTime, player.callTime.get(player.currState),
//					player.looping.get(player.currState));
//		}

		System.out.println(player.currState);
		System.out.printf("%s %d%n", (((TextureAtlas.AtlasRegion) frame).name), ((TextureAtlas.AtlasRegion) frame).index);


		// getKeyFrame returns keyframe
		// based on 0-animation duration, in seconds
		// mapped to each frame's length

//		if (player.animations.get(player.currState).isAnimationFinished(stateTime) && player.currState == Player.State.ATTACKING) {
//			player.currState = Player.State.IDLE;
//			player.attacking = false;
//		}

		ScreenUtils.clear(0, 0, 0, 0);
		camera.update();
		debugRenderer.render(world, camera.combined);
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		float width_scale = Globals.PLAYER_HEIGHT / frame.getRegionHeight();

//		System.out.println(String.format("%s %d", frame));
		batch.draw(frame, player.playerBody.getWorldCenter().x - Globals.PLAYER_WIDTH/2f - Globals.SKIN_WIDTH/2f - 8 * width_scale,
				player.playerBody.getWorldCenter().y - Globals.PLAYER_HEIGHT/2f - Globals.SKIN_HEIGHT/2f,
				frame.getRegionWidth() * width_scale, Globals.PLAYER_HEIGHT);
		batch.end();

		// am stretching to fit

		// look at original size

		// sprite batch
		// has limit on amount of sprites that can be sent to GPU in one call
		// this can be set
		// maxSpritesInBatch
		// is this for sprites?


		world.step(1/60f, 2, 6);
		debuggingInfo();
	}
// camera should be in center



	private void update() {
		player.update();
	}
	private void handleInput(float deltaTime) {
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

//		if (Gdx.input.isKeyJustPressed(Input.Keys.D) && !Gdx.input.isKeyJustPressed(Input.Keys.A)) {
//
//		}
//		else if (Gdx.input.isKeyJustPressed(Input.Keys.A) && !Gdx.input.isKeyJustPressed(Input.Keys.D)) {
//			player.updateState(Player.State.MOVING);
//			player.updateAnimationCallTime(deltaTime);
//			System.out.println("still called");
//		}
		if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
			if ((player.currState.bits & Player.State.MOVING.bits) == Player.State.MOVING.bits) {
				player.moveX(1);
				System.out.println("MOVING D");
			}
		}

		else if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
			if ((player.currState.bits & Player.State.MOVING.bits) == Player.State.MOVING.bits) {
				player.moveX(-1);
			}
		}

//		else if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
//			player.moveX(-1);
//		}
		if ((!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) || (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D))) {
			// subtraction of moving -> no_state
			if (player.updateState(Player.State.IDLE)) {
				player.resetCallTime();
			}
			if (!player.checkStateContains(Player.State.MOVING)) {
				player.xStationary();
			}
		}
//		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
//			player.updateState(Player.State.ATTACKING);
//			player.updateAnimationCallTime(deltaTime);
////			player.attack();
//		}

//		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
//			if (player.updateState(Player.State.AIRBORNE)) {
//				player.jump();
//				player.updateAnimationCallTime(deltaTime);
//			}
//		}

		if (Gdx.input.isKeyPressed(Input.Keys.L)){
			System.out.println("stopped");
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
