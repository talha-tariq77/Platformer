package com.gdx.plat;
import com.badlogic.gdx.physics.box2d.*;

public class Ball {

    BodyDef ballBodyDef;
    Body ballBody;

    float restitution = 0f;
    float density = 1f;

    int posX;
    int posY;

    Ball (float posX, float posY, float radius) {
        ballBodyDef = new BodyDef();
        ballBodyDef.type = BodyDef.BodyType.DynamicBody;
        ballBodyDef.position.set(posX, posY);
        ballBody = GdxGame.world.createBody(ballBodyDef);

        CircleShape ballShape = new CircleShape();
        ballShape.setRadius(radius);

        Fixture ballFixture = ballBody.createFixture(ballShape, density);
        ballFixture.setRestitution(restitution);
    }
}
