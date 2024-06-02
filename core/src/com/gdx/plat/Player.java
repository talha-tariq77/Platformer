package com.gdx.plat;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Filter;

import java.sql.Array;
import java.util.*;


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

    ArrayList<Boolean> actionBools;

    HashMap<Integer, ExtraAnimation<TextureRegion>> animations;

    public enum State {
        NO_STATE (1),
        IDLE (2),
        MOVING (4),
        AIRBORNE (8),
        ATTACKING (16);

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
    HashMap<ArrayList<State>, Boolean> looping;


    float currStateTime;

    EnumMap<State, Boolean> stateBools;

    ArrayList<State> currState;

    ArrayList<State> currOneTime;


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


        currStateTime = 0f;

        currState = new ArrayList<State>();
        currState.add(State.IDLE);

        stateBools = new EnumMap<State, Boolean>(State.class);

        stateBools.put(State.NO_STATE, false);
        stateBools.put(State.IDLE, true);
        stateBools.put(State.ATTACKING, false);
        stateBools.put(State.AIRBORNE, false);
        stateBools.put(State.MOVING, false);

        animations = new HashMap<>();

        looping = new HashMap<ArrayList<State>, Boolean>();

        currOneTime = new ArrayList<State>();
        // those dependent on time-alone for ending



        // can wrap looping and lastCalled in correctly into each other
        // using a seperate class
        // component animation

    }


    public int getTotal(List<State> states) {
        int total = 0;
        for (State state :
                states) {
            total += state.bits;
        }
        return total;
    }
    public void moveX(int DirectionX) {
        // reimplement as the base method of the base class no-item affected, handled by the sys

//        System.out.println("Called");
//        playerBody.applyForceToCenter(10f * DirectionX, 0f, true);

        if ((playerBody.getLinearVelocity().x < 0 && DirectionX > 0) || (playerBody.getLinearVelocity().x > 0 && DirectionX < 0)) {
            playerBody.setLinearVelocity(0f, playerBody.getLinearVelocity().y);
        }
        float speedCap;
        if (stateBools.get(State.AIRBORNE)) {
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

        stateBools.put(State.MOVING, true);

    }
    public void xStationary() {
        stateBools.put(State.MOVING, false);
        playerBody.setLinearVelocity(0f, playerBody.getLinearVelocity().y);


        // update state separate into the different state setters
    }

    public void jump() {
        if (!stateBools.get(State.AIRBORNE)) {
            playerBody.applyLinearImpulse(new Vector2(0, 125f), playerBody.getWorldCenter(), true);
            stateBools.put(State.AIRBORNE, true);
        }
    }
    // TODO
    // check available
    // able to do right and left

    // FIXME
    // attack + movement causes attack to reset/repeat

    // input handler calls relevant methods in the animation/state
    // and the physics handler + bool (?)
    // can seperate updateState to attack, move, airborne
    // each one performs combinatorial checks on the bools

    public boolean updateState() {
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

        // check availability beforehnad, don't need all this
        int prevCurrentState = getTotal(currState);
        int prevOneTime = getTotal(currOneTime);

        currState.clear();
        currOneTime.clear();

        if (stateBools.get(State.ATTACKING)) {
            currOneTime.add(State.ATTACKING);

            if (stateBools.get(State.MOVING)) {
                currState.add(State.MOVING);
            }
            if (stateBools.get(State.AIRBORNE)) {
                currState.add(State.AIRBORNE);
            }
        }
        else if (stateBools.get(State.AIRBORNE)) {
            currState.add(State.AIRBORNE);
            if (stateBools.get(State.MOVING)) {
                currState.add(State.MOVING);
            }

            // replace

        }
        else if (stateBools.get(State.MOVING))
            currState.add(State.MOVING);

        else {
            currState.add(State.IDLE);
        }

        int newCurrState = getTotal(currState);
        int newOneTime = getTotal(currOneTime);

        return newOneTime != prevOneTime;
        // may need or constant has changed (?) but it is combinational, so currOneTime size > 0

//        currState.sort(Enum::compareTo);
        // can keep contiguous animation by passing on the last called from one animation to next
    }

    public void translateBools() {

    }

    public void resetAnimationCallTime() {
        currStateTime = 0f;
    }

//    public void updateAnimationCall(float deltaTime) {
//        Player.State oldState = currState;
//        updateState();
//        if (currState != oldState) {
//            lastCalled.put(currState, deltaTime);
//        }
//    }


    public void attack() {
        if (!stateBools.get(State.ATTACKING)) {
            stateBools.put(State.ATTACKING, true);
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
