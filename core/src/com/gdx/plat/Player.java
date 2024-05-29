package com.gdx.plat;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;
import java.util.HashMap;


public class Player {
    BodyDef playerBodyDef;
    Body playerBody;
    PolygonShape hitBox;
    FixtureDef playerFixtureDef;
    Fixture playerFixture;

    // even if not using hashmap, will still have to check animation based on state 1-1


    float maxSpeed = 5f;
    boolean NO_X_MOVE = false;
    boolean STUN = false;
    boolean xMove = false;
    Filter testFilter;
    float airborneMaxSpeed = 0.75f * maxSpeed;

    boolean moving;

    boolean airborne;
    boolean attacking;

    EnumMap<State, ExtraAnimation<TextureRegion>> animations;

    public enum State {
        NO_STATE (0),
        IDLE ( 1),
        MOVING (2),
        AIRBORNE (4),
        ATTACKING (8),
        MOVING_AND_ATTACKING (10),
        AIRBORNE_AND_ATTACKING (12),
        MOVING_AND_AIRBORNE (6);

        final int bits;

        State(int bits) {
            this.bits = bits;
        }


        // state needs combination checks every time we check state
        // during action we check state
        // we update state

        // during listener calls
        // we update state

        // and every time we update the state

        // bool flags need combination checks
        // every time we get the current Animation<>

        // may be figure out some combination where getting and setting is easier
        // and direct
        // on the combinations
        // e.g. getting if Airborne automatically easily checks if AIRBORNE_AND
        // e.g. setting new state automatically adds to existing if combination is present

        // 2 enums 1 combinatorial which takes 2 values, other ideas
        // bitwise, but need full replacement of bits when non-combinatorial used

        // using combinatorial means, can only do 2 things at once
        // limitation
        // and not doing combinatorial, seperates game logic from animation state logic
    }

//    public enum CombinatorialState {
//        NEW (State.ATTACKING, State.AIRBORNE),
//        YES;
//
//        final Array<Player.State> state1;
//        final Player.State state2;
//        public CombinatorialState(Player.State A, Player.State B) {
//            this.state1 = A;
//            this.state2 = B;
//        }
//    }
    State currState;
    EnumMap<State, Boolean> looping;

    EnumMap<State, Float> callTime;

    float startTime;

    EnumMap<State, EnumMap<State, State>> stateInterrupts;



    Player(float posX, float posY, float deltaTime) {
        playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(posX, posY);

        playerBody = GdxGame.world.createBody(playerBodyDef);
        playerBody.setFixedRotation(true);
//        playerBody.setLinearDamping(100f);

        hitBox = new PolygonShape();
        hitBox.setAsBox(Globals.PLAYER_WIDTH/2f, Globals.PLAYER_HEIGHT/2f);
        playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = hitBox;
        playerFixtureDef.density = 6f;
        playerFixtureDef.restitution = 0f;
        playerFixtureDef.friction = 0f;
//        playerFixture.setDensity(1f);
//        playerFixtureDef.friction = 10f;

        playerFixture = playerBody.createFixture(playerFixtureDef);
        playerFixture.setUserData(this);

        testFilter = new Filter();
        testFilter.categoryBits = Globals.PLAYER_BIT;
        playerFixture.setFilterData(testFilter);

        moving = false;
        airborne = false;
        attacking = false;

        currState = State.IDLE;

        animations = new EnumMap<State, ExtraAnimation<TextureRegion>>(State.class);

        looping = new EnumMap<State, Boolean>(State.class);

        callTime = new EnumMap<State, Float>(State.class);

        stateInterrupts = new EnumMap<State, EnumMap<State, State>>(State.class);
        stateInterrupts.put(State.ATTACKING, new EnumMap<State, State>(State.class));
        stateInterrupts.put(State.MOVING, new EnumMap<State, State>(State.class));
        stateInterrupts.put(State.AIRBORNE, new EnumMap<State, State>(State.class));
        stateInterrupts.put(State.IDLE, new EnumMap<State, State>(State.class));
        stateInterrupts.put(State.NO_STATE, new EnumMap<State, State>(State.class));
        stateInterrupts.put(State.MOVING_AND_AIRBORNE, new EnumMap<State, State>(State.class));



        stateInterrupts.get(State.ATTACKING).put(State.AIRBORNE, State.AIRBORNE);
        stateInterrupts.get(State.ATTACKING).put(State.MOVING, State.MOVING_AND_ATTACKING);


        stateInterrupts.get(State.MOVING).put(State.ATTACKING, State.MOVING_AND_ATTACKING);
        stateInterrupts.get(State.MOVING).put(State.AIRBORNE, State.AIRBORNE);
        stateInterrupts.get(State.MOVING).put(State.IDLE, State.IDLE);

        stateInterrupts.get(State.AIRBORNE).put(State.MOVING, State.MOVING_AND_AIRBORNE);
        stateInterrupts.get(State.AIRBORNE).put(State.NO_STATE, State.NO_STATE);

        stateInterrupts.get(State.MOVING_AND_AIRBORNE).put(State.NO_STATE, State.NO_STATE);



        stateInterrupts.get(State.IDLE).put(State.ATTACKING, State.ATTACKING);
        stateInterrupts.get(State.IDLE).put(State.MOVING, State.MOVING);
        stateInterrupts.get(State.IDLE).put(State.AIRBORNE, State.AIRBORNE);

        stateInterrupts.get(State.IDLE).put(State.MOVING_AND_AIRBORNE, State.MOVING_AND_AIRBORNE);
        stateInterrupts.get(State.IDLE).put(State.MOVING_AND_ATTACKING, State.MOVING_AND_ATTACKING);

        stateInterrupts.get(State.IDLE).put(State.AIRBORNE_AND_ATTACKING, State.AIRBORNE_AND_ATTACKING);

        stateInterrupts.get(State.NO_STATE).put(State.ATTACKING, State.ATTACKING);
        stateInterrupts.get(State.NO_STATE).put(State.MOVING, State.MOVING);
        stateInterrupts.get(State.NO_STATE).put(State.AIRBORNE, State.AIRBORNE);
        stateInterrupts.get(State.NO_STATE).put(State.MOVING_AND_AIRBORNE, State.MOVING_AND_AIRBORNE);
        stateInterrupts.get(State.NO_STATE).put(State.MOVING_AND_ATTACKING, State.MOVING_AND_ATTACKING);
        stateInterrupts.get(State.NO_STATE).put(State.AIRBORNE_AND_ATTACKING, State.AIRBORNE_AND_ATTACKING);
        stateInterrupts.get(State.NO_STATE).put(State.IDLE, State.IDLE);




        // can wrap looping and lastCalled in correctly into each other
        // using a seperate class
        // component animation

    }

    public void moveX(int DirectionX) {
        // reimplement as the base method of the base class no-item affected, handled by the sys

//        System.out.println("Called");
//        playerBody.applyForceToCenter(10f * DirectionX, 0f, true);

        if ((playerBody.getLinearVelocity().x < 0 && DirectionX > 0) || (playerBody.getLinearVelocity().x > 0 && DirectionX < 0)) {
            playerBody.setLinearVelocity(0f, playerBody.getLinearVelocity().y);
        }
        float speedCap;
        if (airborne) {
            speedCap = airborneMaxSpeed;
        }
        else {
            speedCap = maxSpeed;
        }

        playerBody.applyLinearImpulse(new Vector2(speedCap * DirectionX * playerFixture.getDensity(), 0f), playerBody.getWorldCenter(),true);

        if (playerBody.getLinearVelocity().x > speedCap) {
            playerBody.setLinearVelocity(speedCap, playerBody.getLinearVelocity().y);
        }
        else if (playerBody.getLinearVelocity().x < -speedCap) {
            playerBody.setLinearVelocity(-speedCap, playerBody.getLinearVelocity().y);
        }
        moving = true;


    }
    public void xStationary() {
        moving = false;
        playerBody.setLinearVelocity(0f, playerBody.getLinearVelocity().y);

        // update state separate into the different state setters
    }

    public void jump() {
        if (!airborne) {
            playerBody.applyLinearImpulse(new Vector2(0, 125f), playerBody.getWorldCenter(), true);
            airborne = true;
        }
    }
    // input handler calls relevant methods in the animation/state
    // and the physics handler + bool (?)
    // can seperate updateState to attack, move, airborne
    // each one performs combinatorial checks on the bools

    public void updateState(float deltaTime) {
//        switch (currState) {
//            case ATTACKING:
//                switch (newState) {
//
//                }
//            case AIRBORNE
//        }
//        if (stateInterrupts.containsKey(currState)) {
//            if (stateInterrupts.get(currState).containsKey(newState)) {
//                currState = stateInterrupts.get(currState).get(newState);
//                return true;
//            }
//        }
//        return false;



//        if (currState.bits == (currState.bits & State.AIRBORNE.bits)) {
//            switch (newState) {
//                case
//            }
//        }
        Player.State newState;
        if (attacking) {
            if (moving) {
                newState = State.ATTACKING;
            } else if (airborne) {
                newState = State.ATTACKING;
            } else {
                newState = State.ATTACKING;
            }
        }
        else if (airborne) {
            if (moving)
                newState = State.AIRBORNE;
            // replace
            else
                newState = State.AIRBORNE;
        }
        else if (moving)
            newState = State.MOVING;

        else {
            newState = State.IDLE;
        }


        if (newState != currState) {
            currState = newState;
            updateAnimationCallTime(deltaTime);
        }

        // can keep contiguous animation by passing on the last called from one animation to next
    }

    public void updateAnimationCallTime(float deltaTime) {
        callTime.put(currState, deltaTime);
    }

//    public void updateAnimationCall(float deltaTime) {
//        Player.State oldState = currState;
//        updateState();
//        if (currState != oldState) {
//            lastCalled.put(currState, deltaTime);
//        }
//    }


    public void attack() {
        if (!attacking) {
            attacking = true;
        }
    }


    public void update() {
//        if (playerBody.getLinearVelocity().x > maxSpeed) {
//            playerBody.setLinearVelocity(maxSpeed, playerBody.getLinearVelocity().y);
//        }
//        else if (playerBody.getLinearVelocity().x < -maxSpeed) {
//            playerBody.setLinearVelocity(-maxSpeed, playerBody.getLinearVelocity().y);
//        }

    }

}
