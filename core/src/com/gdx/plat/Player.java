package com.gdx.plat;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Filter;


public class Player {
    BodyDef playerBodyDef;
    Body playerBody;
    PolygonShape hitBox;
    FixtureDef playerFixtureDef;
    Fixture playerFixture;

    float maxSpeed = 10f;
    boolean NO_X_MOVE = false;
    boolean STUN = false;
    boolean xMove = false;
    Filter testFilter;
    float airborneMaxSpeed = 0.75f * maxSpeed;

    boolean moving;

    boolean airborne;

    Player(float posX, float posY) {
        playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(posX, posY);

        playerBody = GdxGame.world.createBody(playerBodyDef);
        playerBody.setFixedRotation(true);
//        playerBody.setLinearDamping(100f);

        hitBox = new PolygonShape();
        hitBox.setAsBox(Globals.PLAYER_WIDTH, Globals.PLAYER_HEIGHT);
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
    }
    public void xStationary() {
        playerBody.setLinearVelocity(0f, playerBody.getLinearVelocity().y);
    }

    public void jump() {
        if (!airborne) {
            playerBody.applyLinearImpulse(new Vector2(0, 210f*3f), playerBody.getWorldCenter(), true);
            airborne = true;
        }
    }

    public void test() {
        System.out.println(5);
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
