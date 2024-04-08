package com.gdx.plat;
import com.badlogic.gdx.physics.box2d.*;

public class Ground {
    BodyDef groundBodyDef = new BodyDef();
    Body groundBody;
    PolygonShape groundShape = new PolygonShape();
    public static Fixture groundFixture;

    int groundLength = 10000;
    int groundHeight = 5;
//    FixtureDef groundFixtureDef;

    Ground() {
        groundBodyDef.position.set(0,0);
        groundBody = GdxGame.world.createBody(groundBodyDef);

        groundShape.setAsBox(groundLength, groundHeight);
        groundBody.createFixture(groundShape, 0f);
//        groundFixtureDef = new FixtureDef();
//        groundFixtureDef.shape = groundShape;
        groundFixture = groundBody.createFixture(groundShape, 1f);
        groundFixture.setFriction(1);

    }
}
// make objs with bodies inside of em,
// managed by other classes