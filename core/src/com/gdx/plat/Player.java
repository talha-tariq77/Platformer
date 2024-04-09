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
    float maxSpeed = 15f;
    boolean NO_X_MOVE = false;
    boolean STUN = true;
    Filter testFilter;

    boolean moving;

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
//        playerFixture.setDensity(1f);
//        playerFixtureDef.friction = 10f;

        playerFixture = playerBody.createFixture(playerFixtureDef);

        playerFixture.setUserData(this);

        testFilter = new Filter();
        testFilter.categoryBits = 1;
        playerFixture.setFilterData(testFilter);

        moving = false;
    }

    public void moveX(int DirectionX) {
        System.out.println("Called");
//        playerBody.applyForceToCenter(10f * DirectionX, 0f, true);
        if (!NO_X_MOVE && !STUN) {
            playerBody.applyLinearImpulse(new Vector2(maxSpeed * DirectionX, 0f), playerBody.getWorldCenter(),true);
        }
    }

    public void test() {
        System.out.println(5);
    }


    public void update() {
        if (playerBody.getLinearVelocity().x > maxSpeed) {
            playerBody.setLinearVelocity(maxSpeed, playerBody.getLinearVelocity().y);
        }
        else if (playerBody.getLinearVelocity().x < -maxSpeed) {
            playerBody.setLinearVelocity(-maxSpeed, playerBody.getLinearVelocity().y);
        }

    }

}
