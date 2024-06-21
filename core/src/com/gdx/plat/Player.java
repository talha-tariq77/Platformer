package com.gdx.plat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Filter;

import java.sql.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class Player {
    BodyDef playerBodyDef;
    PolygonShape hitBox;
    FixtureDef playerFixtureDef;
    Fixture playerFixture;

    // even if not using hashmap, will still have to check animation based on state 1-1


    float maxSpeed = 5f;
    boolean NO_X_MOVE = false;
    boolean STUN = false;
    boolean xMove = true;
    Filter testFilter;
    float airborneMaxSpeed = 0.75f * maxSpeed;

    ArrayList<Boolean> actionBools;

    HashMap<Integer, ExtraAnimation<TextureRegion>> animations;


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

    public static float hello;

    HashMap<ArrayList<Integer>, Boolean> looping;


    float currStateTime;

    ArrayList<Integer> currState;

    ArrayList<Integer> currOneTime;

    HashMap<Integer, ArrayList<Supplier<Void>>> actions;

    HashMap<Integer, HashMap<TimeRange, Float>> changingOffsets;

    HashMap<Integer, Float> constantOffsets;

    int fullState;
    boolean FLIP;
    ActionComponent actionComponent;
    // make all in actionComponent
    // else 2 instances

    HashMap<Integer, ActionGroup> oneTimeStateActions;

    HashMap<Integer, ActionGroup> actionGroupMapping;



    Player(float posX, float posY, float deltaTime) {
        playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(posX, posY);

        Body playerBody = GdxGame.world.createBody(playerBodyDef);
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

        testFilter = new Filter();
        testFilter.categoryBits = Globals.PLAYER_BIT;
        playerFixture.setFilterData(testFilter);


        currStateTime = 0f;

        currState = new ArrayList<Integer>();

        HashMap<Integer, Boolean> stateBools = new HashMap<Integer, Boolean>();

//        NO_STATE (1),
//        IDLE (2),
//        MOVING (4),
//        AIRBORNE (8),
//        ATTACKING (16);

        HashMap<String, Integer> stateDict = new HashMap<String, Integer>();
        stateDict.put("NO_STATE", 1);
        stateDict.put("IDLE", 2);
        stateDict.put("MOVING", 4);
        stateDict.put("AIRBORNE", 8);
        stateDict.put("ATTACKING", 16);

        currState.add(stateDict.get("IDLE"));


        stateBools.put(stateDict.get("NO_STATE"), false);
        stateBools.put(stateDict.get("IDLE"), true);
        stateBools.put(stateDict.get("MOVING"), false);
        stateBools.put(stateDict.get("AIRBORNE"), false);
        stateBools.put(stateDict.get("MOVING"), false);
        stateBools.put(stateDict.get("ATTACKING"), false);

        animations = new HashMap<>();

        looping = new HashMap<ArrayList<Integer>, Boolean>();

        currOneTime = new ArrayList<Integer>();

        FLIP = false;

        int curr_direction = 1;

        actions = new HashMap<Integer, ArrayList<Supplier<Void>>>();

        changingOffsets = new HashMap<Integer, HashMap<TimeRange, Float>>();

        constantOffsets = new HashMap<Integer, Float>();

        fullState = calculateState();


        actionComponent = new ActionComponent(playerBody, stateBools, stateDict, curr_direction);
        playerFixture.setUserData(actionComponent);

        oneTimeStateActions = new HashMap<>();
        oneTimeStateActions.put(stateDict.get("ATTACKING"), new Attack1ActionGroup(actionComponent));

        actionGroupMapping = new HashMap<>();

        actionGroupMapping.put(stateDict.get("ATTACKING"), new Attack1ActionGroup(actionComponent));
//        actionGroupMapping.put(getTotal(List.of(stateDict.get("ATTACKING"), stateDict.get("MOVING"))),
//                                new Attack1ActionGroup(actionComponent));



        // those dependent on time-alone for ending



        // can wrap looping and lastCalled in correctly into each other
        // using a seperate class
        // component animation



    }


    public int getTotal(List<Integer> states) {
        int total = 0;
        for (int state :
                states) {
            total += state;
        }
        return total;
    }
    public void moveX(int DirectionX) {
        // reimplement as the base method of the base class no-item affected, handled by the sys

//        System.out.println("Called");
//        actionComponent.body.applyForceToCenter(10f * DirectionX, 0f, true);
        System.out.printf("moving %d\n", DirectionX);

        if ((actionComponent.body.getLinearVelocity().x < 0 && DirectionX > 0) || (actionComponent.body.getLinearVelocity().x > 0 && DirectionX < 0)) {
            actionComponent.body.setLinearVelocity(0f, actionComponent.body.getLinearVelocity().y);
        }
        float speedCap;
        if (actionComponent.stateBools.get(actionComponent.stateDict.get("AIRBORNE"))) {
            speedCap = airborneMaxSpeed;
        }
        else {
            speedCap = maxSpeed;
        }

        actionComponent.body.applyLinearImpulse(new Vector2(speedCap * DirectionX * playerFixture.getDensity()
                * Globals.PLAYER_WIDTH * Globals.PLAYER_HEIGHT, 0f), actionComponent.body.getWorldCenter(),true);

        if (actionComponent.body.getLinearVelocity().x > speedCap) {
            actionComponent.body.setLinearVelocity(speedCap, actionComponent.body.getLinearVelocity().y);
        }
        else if (actionComponent.body.getLinearVelocity().x < -speedCap) {
            actionComponent.body.setLinearVelocity(-speedCap, actionComponent.body.getLinearVelocity().y);
        }

        actionComponent.stateBools.put(actionComponent.stateDict.get("MOVING"), true);


        FLIP = DirectionX == -1;

        actionComponent.curr_direction = DirectionX;
    }

    public float getOffset() {
        if (constantOffsets.containsKey(fullState)) {
            return constantOffsets.get(fullState);
        }
        else if (changingOffsets.containsKey(fullState)) {
            for (TimeRange offset: changingOffsets.get(fullState).keySet()) {
                int currIndex = animations.get(fullState).getKeyFrameIndex(currStateTime, false);

                if (offset.rangeStart <= currIndex && offset.rangeEnd >= currIndex) {
                    return changingOffsets.get(fullState).get(offset);
                }
            }
            return 0;
        }
        else {
            return 0;
        }
    }

    public void xStationary() {
        float speedCap;
        if (actionComponent.stateBools.get(actionComponent.stateDict.get("AIRBORNE"))) {
            speedCap = airborneMaxSpeed;
        }
        else {
            speedCap = maxSpeed;
        }

        actionComponent.stateBools.put(actionComponent.stateDict.get("MOVING"), false);
        actionComponent.body.applyLinearImpulse(new Vector2(speedCap * actionComponent.curr_direction * -1
        * playerFixture.getDensity() * Globals.PLAYER_WIDTH * Globals.PLAYER_HEIGHT, 0f),
                actionComponent.body.getWorldCenter(), true);

        actionComponent.body.setLinearVelocity(0f, actionComponent.body.getLinearVelocity().y);
        // update state separate into the different state setters
    }

    public void jump() {
        if (!actionComponent.stateBools.get(actionComponent.stateDict.get("AIRBORNE"))) {
            actionComponent.body.applyLinearImpulse(new Vector2(0, 125f), actionComponent.body.getWorldCenter(), true);
            actionComponent.stateBools.put(actionComponent.stateDict.get("AIRBORNE"), true);
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


    public int calculateState() {
        return getTotal(currState) + getTotal(currOneTime);
    }

    public void createContactFixture(float playerRelativeWidth, float playerRelativeHeight
            , float playerEdgeRelativeX, float playerTopRelativeY) {
        // x relative to right/left edge of player
        // y relative to top of player

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        PolygonShape sensorShape = new PolygonShape();
        float halfWidth = playerRelativeWidth * Globals.UNITS_PER_PIXEL; // 20
        // 15 + 8 (offset)
        // need to store offsets with animation frames
        float halfHeight = playerRelativeHeight * Globals.UNITS_PER_PIXEL; //16
        sensorShape.setAsBox(halfWidth, halfHeight,
                new Vector2((Globals.PLAYER_WIDTH/2f + halfWidth + playerEdgeRelativeX) * actionComponent.curr_direction,
                Globals.PLAYER_HEIGHT/2f - playerTopRelativeY * Globals.UNITS_PER_PIXEL), 0);//32
        // x, y relative to actionComponent.body.getCenter()
        fixtureDef.shape = sensorShape;
        // y = up
        ;
        // when not relative player, create bullet (?)

        Fixture newFixture = actionComponent.body.createFixture(fixtureDef);
        newFixture.setUserData(this);

//        fixtureDef.shape = Shape();
                // queue to handle this, shapes?

        // boolean
        // index
        // actions

    }

    public void addFixture2() {
        // fixture generator/projectile non-bullet
        // use ranges (?)

        // new state:
        // add boolean
        // add State, for animation
        // add animation
        // add function for what to do
        // if non-single use
        // add to gdx.ispressed
        // if single use
        // add to keydown
        // else/and if frame-based
        // run action from frame

        // or
        // just set to true
        // physics will set to false if single-use
        // if constant, can do, if both empty, set idle =true

    }

    public boolean updateState() {
        // use priority numbers
        // to check if new state should be added or should interrupt the current ones
        // works similarly to current updateState
        // if one time state changed then restart animation

        // then can use these to do the relevant actions (?)
        // no longer need bools
        // keep actions list in action Component


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

        // only initial

        if (actionComponent.stateBools.get(actionComponent.stateDict.get("ATTACKING"))) {
            currOneTime.add(actionComponent.stateDict.get("ATTACKING"));

            if (actionComponent.stateBools.get(actionComponent.stateDict.get("MOVING"))) {
                currState.add(actionComponent.stateDict.get("MOVING"));
                // dont need to add, since not doing moving animation
            }
            if (actionComponent.stateBools.get(actionComponent.stateDict.get("AIRBORNE"))) {
                currState.add(actionComponent.stateDict.get("AIRBORNE"));
            }
        }
        else if (actionComponent.stateBools.get(actionComponent.stateDict.get("AIRBORNE"))) {
            currState.add(actionComponent.stateDict.get("AIRBORNE"));
            if (actionComponent.stateBools.get(actionComponent.stateDict.get("MOVING"))) {
                currState.add(actionComponent.stateDict.get("MOVING"));
            }

            // replace

        }
        else if (actionComponent.stateBools.get(actionComponent.stateDict.get("MOVING")))
            currState.add(actionComponent.stateDict.get("MOVING"));

        else {
            currState.add(actionComponent.stateDict.get("IDLE"));
        }

        int newCurrState = getTotal(currState);
        int newOneTime = getTotal(currOneTime);

//        if (newCurrState != prevCurrentState || newOneTime != prevOneTime)
//            fullState = calculateState();

        fullState = calculateState();
        return newOneTime != prevOneTime;
//         constants can always be carried on ?

        // may need or constant has changed (?) but it is combinational, so currOneTime size > 0

//        currState.sort(Enum::compareTo);
        // can keep contiguous animation by passing on the last called from one animation to next
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

    // oneTime, only turn statebool true if not already true


    public void attack() {
        if (!actionComponent.stateBools.get(actionComponent.stateDict.get("ATTACKING"))) {
            actionComponent.stateBools.put(actionComponent.stateDict.get("ATTACKING"), true);
            createContactFixture(20f, 16f,
                    0f, 32f);
            System.out.println("done");
        }
    }


    public void update() {
//        if (actionComponent.body.getLinearVelocity().x > maxSpeed) {
//            actionComponent.body.setLinearVelocity(maxSpeed, actionComponent.body.getLinearVelocity().y);
//        }
//        else if (actionComponent.body.getLinearVelocity().x < -maxSpeed) {
//            actionComponent.body.setLinearVelocity(-maxSpeed, actionComponent.body.getLinearVelocity().y);
//        }

    }

}
