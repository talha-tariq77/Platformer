package com.gdx.plat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.physics.box2d.*;
import org.w3c.dom.Text;

import java.sql.Time;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class GdxGame extends ApplicationAdapter {
	public static World world;
	SpriteBatch batch;
	OrthographicCamera camera;

	OrthographicCamera debugging;
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

	BitmapFont font;

	int prevState;



	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();

		debugging = new OrthographicCamera();

		atlas = new TextureAtlas(Gdx.files.internal("animations/animations_packed.atlas"));

		//		System.out.println(animation.getKeyFrames().length);
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

		camera.setToOrtho(false, Globals.VIEWPORT_WIDTH, Globals.VIEWPORT_HEIGHT);
		debugging.setToOrtho(false, Globals.VIEWPORT_WIDTH, Globals.VIEWPORT_HEIGHT);

		world = new World(new Vector2(0, Globals.GRAVITY * Globals.GRAVITY_MULTIPLIER), true);
		Listener contactListener = new Listener();
		world.setContactListener(contactListener);
		ground = new Ground();

		camera.position.set(0, Globals.VIEWPORT_HEIGHT / 2, 0);
// 5 + 1.7f + 2f
		player = new Player(0f, 0f + Globals.PLAYER_HEIGHT, 0f);

		stateTime = 0f;


		ExtraAnimation<TextureRegion> attackAnimation = new ExtraAnimation<TextureRegion>(2f, atlas.findRegions("attack"), false);
		player.animations.put(player.getTotal(List.of(player.actionComponent.stateDict.get("ATTACKING"))), attackAnimation);


		player.animations.put(player.getTotal(List.of(player.actionComponent.stateDict.get("ATTACKING"), player.actionComponent.stateDict.get("AIRBORNE"))), new ExtraAnimation<TextureRegion>(2f, atlas.findRegions("attack"), false));


		player.animations.put(player.getTotal(List.of(player.actionComponent.stateDict.get("ATTACKING"), player.actionComponent.stateDict.get("MOVING"))), new ExtraAnimation<TextureRegion>(2f, atlas.findRegions("attack"), false));
		player.animations.put(player.getTotal(List.of(player.actionComponent.stateDict.get("AIRBORNE"),player.actionComponent.stateDict.get("MOVING"), player.actionComponent.stateDict.get("ATTACKING"))), new ExtraAnimation<TextureRegion>(2f, atlas.findRegions("attack"), false));


		player.animations.put(player.getTotal(List.of(player.actionComponent.stateDict.get("AIRBORNE"))), new ExtraAnimation<TextureRegion>(1/8f, atlas.findRegions("jumping"), false));


		player.animations.put(player.getTotal(List.of(player.actionComponent.stateDict.get("AIRBORNE"),player.actionComponent.stateDict.get("MOVING"))), new ExtraAnimation<TextureRegion>(1/8f, atlas.findRegions("jumping"), true));

		player.animations.put(player.getTotal(List.of(player.actionComponent.stateDict.get("MOVING"))), new ExtraAnimation<TextureRegion>(1/8f, atlas.findRegions("running"), true));


		player.animations.put(player.getTotal(List.of(player.actionComponent.stateDict.get("IDLE"))), new ExtraAnimation<TextureRegion>(1/8f, atlas.findRegions("idle"), true));


		player.animations.put(0, new ExtraAnimation<TextureRegion>(1/8f, atlas.findRegions("idle"), true));

		// airborne (e.g. knockback, and jumping will likely have different

		// make animations require one for each state ?

		player.constantOffsets.put(player.getTotal(List.of(player.actionComponent.stateDict.get("IDLE"))), 8f);

		player.changingOffsets.put(player.getTotal(List.of(player.actionComponent.stateDict.get("ATTACKING"))), new HashMap<>());
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("ATTACKING")))).put(new TimeRange(0,0), 15f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("ATTACKING")))).put(new TimeRange(1,1), 13f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("ATTACKING")))).put(new TimeRange(2,2), 2f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("ATTACKING")))).put(new TimeRange(3,3), 4f);

		player.changingOffsets.put(player.getTotal(List.of(player.actionComponent.stateDict.get("MOVING"))), new HashMap<>());

		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("MOVING")))).put(new TimeRange(0,0), 8f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("MOVING")))).put(new TimeRange(1,1), 11f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("MOVING")))).put(new TimeRange(2,2), 8f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("MOVING")))).put(new TimeRange(3,3), 11f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("MOVING")))).put(new TimeRange(4,4), 7f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("MOVING")))).put(new TimeRange(5,5), 9f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("MOVING")))).put(new TimeRange(6,6), 10f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("MOVING")))).put(new TimeRange(7,7), 11f);


		player.changingOffsets.put(player.getTotal(List.of(player.actionComponent.stateDict.get("MOVING"), player.actionComponent.stateDict.get("ATTACKING"))), new HashMap<>());
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("ATTACKING"), player.actionComponent.stateDict.get("MOVING")))).put(new TimeRange(0,0), 15f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("ATTACKING"), player.actionComponent.stateDict.get("MOVING")))).put(new TimeRange(1,1), 13f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("ATTACKING"), player.actionComponent.stateDict.get("MOVING")))).put(new TimeRange(2,2), 2f);
		player.changingOffsets.get(player.getTotal(List.of(player.actionComponent.stateDict.get("ATTACKING"), player.actionComponent.stateDict.get("MOVING")))).put(new TimeRange(3,3), 4f);
		prevState = 0;
		// by looping

		// use arrays for per frame
		// fastest
		// or a constant value

		// animation and action
		// offsets
		//



		// actions, offsets and animations

		debugRenderer = new Box2DDebugRenderer();
//		debugRenderer.setDrawAABBs(true);
//		debugRenderer.setDrawBodies(true);
//		debugRenderer.setDrawContacts(true);
//		debugRenderer.setDrawJoints(true);
//		debugRenderer.setDrawVelocities(true);
//		debugRenderer.setDrawInactiveBodies(true);

		cameraFollow = false;
		// build a custom animations class

		inputHandler = new InputHandler(player);
		Gdx.input.setInputProcessor(inputHandler);

		font = new BitmapFont(Gdx.files.internal("default.fnt"));
		font.getData().setScale(0.1f);
	}

	public void updateCamera(OrthographicCamera orthographicCamera) {
		if (player.actionComponent.body.isAwake()) {
//			float extreme_x = player.playerBody.getWorldPoint(new Vector2(Globals.PLAYER_WIDTH/2f, 0f)).x;
			if (player.actionComponent.body.getWorldCenter().x > orthographicCamera.position.x && cameraFollow) {
				orthographicCamera.position.set(player.actionComponent.body.getWorldCenter().x, orthographicCamera.position.y, orthographicCamera.position.z);
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
		updateCamera(camera);



		// Animation.isAnimationFinished() just checks if 1 singular animation has finished

		// could do a combinatorial


//		if (stateTime - player.startTime >= player.animations.get(player.currState).getAnimationDuration() && player.currState == Player.State.ATTACKING) {
//			player.attacking = false;
//		}



		if (!player.animations.containsKey(player.getTotal(player.currOneTime) + player.getTotal(player.currState))) {
			System.out.println("no");
		}


		// same one-time durations, while

		// implement as queueu

		// when non-looping state is over
		// returns to a default, or some other state

		// can call player.default

		// calls player's state manager's defaulting state
		// state manager
		// default
		// bool -> state
		// state list to bools

//		List<String> test1 = new List<>();
//		List<Float> test2 = new List<Float>() {
//		};
//
//		test1.add(3.14f);
//		test2.add(3.14f);
//
//		if (test1 == test2) {
//			System.out.println("SAME");
//		}
//		else {
//			System.out.println("DIFFERENT");
//		}


		frame = player.animations.get(player.getTotal(player.currState) + player.getTotal(player.currOneTime))
				.getKeyFrame(player.currStateTime);

		if (player.actionGroupMapping.containsKey(player.getTotal(player.currOneTime))) {

			// changed state
			// set true/false
			if (player.animations.get(player.getTotal(player.currOneTime) + player.getTotal(player.currState)).frameNumberChanged
			&& prevState == player.getTotal(player.currState) + player.getTotal(player.currOneTime)) {
				// prevents double fixtures when change dir

				// both
				// and animation not changed
				// action w frame?
				int keyFrameIndex = player.animations.get(player.getTotal(player.currOneTime) + player.getTotal(player.currState))
						.justGetKeyFrameIndex(player.currStateTime);

				for (Consumer<ActionComponent> action: player.actionGroupMapping.get(player.getTotal(player.currOneTime))
						.getCurrentAction(keyFrameIndex)) {
					action.accept(player.actionComponent);
				}

			}
			else {
				if (player.animations.get(player.getTotal(player.currOneTime) + player.getTotal(player.currState)).completed
				&& player.actionGroupMapping.get(player.getTotal(player.currOneTime))
						.completeAction != null) {
					player.actionGroupMapping.get(player.getTotal(player.currOneTime))
							.completeAction.accept(player.actionComponent);
				}
			}
		}


		// have actions seperated
		// it may skip the final frame, skip completion action
		//



		player.currStateTime += Gdx.graphics.getDeltaTime();

		if (!player.currOneTime.isEmpty() && player.animations.get(player.getTotal(player.currOneTime) + player.getTotal(player.currState)).completed) {
			player.animations.get(player.getTotal(player.currState) + player.getTotal(player.currOneTime)).completed = false;
			player.actionComponent.stateBools.put(player.currOneTime.get(player.currOneTime.size() - 1), false);

			// one times complete then get removed, dont need to reset time
			// ongoings need to reset when added to/ combined

//			int i = 0;
////			while (i < player.currOneTime.size()) {
//////				if (player.animations.get(player.getTotal(List.of(player.currOneTime.get(i)))).completed) {
//////					player.stateBools.put(player.currOneTime.get(i), false);
//////				}
//////				i++;
////			}
			player.currOneTime.remove(player.currOneTime.size() - 1);
		}


//		System.out.printf("%d %b %b%n", player.curr_direction, player.FLIP, frame.isFlipX());



		System.out.println(player.currState);
		System.out.println(player.currOneTime);
		System.out.printf("%s %d%n", (((TextureAtlas.AtlasRegion) frame).name), ((TextureAtlas.AtlasRegion) frame).index);


		// getKeyFrame returns keyframe
		// based on 0-animation duration, in seconds
		// mapped to each frame's length

//		if (player.animations.get(player.currState).isAnimationFinished(stateTime) && player.currState == Player.State.ATTACKING) {
//			player.currState = Player.State.IDLE;
//			player.attacking = false;
//		}

		prevState = player.getTotal(player.currOneTime) + player.getTotal(player.currState);


		ScreenUtils.clear(0, 0, 0, 0);
		camera.update();
		debugging.update();
		debugRenderer.render(world, camera.combined);
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		float width_scale = Globals.PLAYER_HEIGHT / frame.getRegionHeight();

//		System.out.println(String.format("%s %d", frame));
		float x = player.actionComponent.body.getWorldCenter().x + (Globals.PLAYER_WIDTH/2f + Globals.SKIN_WIDTH/2f +
				player.getOffset() * Globals.PLAYER_WIDTH_CONVERT) * -player.actionComponent.curr_direction;
		float y = player.actionComponent.body.getWorldCenter().y - Globals.PLAYER_HEIGHT/2f - Globals.SKIN_HEIGHT/2f;
		batch.draw(frame, x,
				y,
				frame.getRegionWidth() * width_scale * player.actionComponent.curr_direction,
				Globals.PLAYER_HEIGHT);

		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), camera.position.x - camera.viewportWidth/2f, camera.viewportHeight/2f);
		batch.end();

		// am stretching to fit

		// look at original size


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
//		if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
//			if ((player.currState.bits & Player.State.MOVING.bits) == player.currState.bits) {
//				player.moveX(1);
//			}
//		}
//		else if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
//			if ((player.currState.bits & Player.State.MOVING.bits) == player.currState.bits) {
//				player.moveX(-1);
//			}
//		}

//		else if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
//			player.moveX(-1);
//		}
		if (Gdx.input.isKeyPressed(Input.Keys.A) && player.xMove) {
			player.moveX(-1);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.D) && player.xMove) {
			player.moveX(1);
		}


		if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
			player.xStationary();
			player.updateState();
		}
		player.updateState();
		// call after update, ground

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
