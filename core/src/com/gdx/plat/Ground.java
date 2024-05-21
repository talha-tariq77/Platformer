package com.gdx.plat;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Ground {
    BodyDef groundBodyDef = new BodyDef();
    Body groundBody;
    PolygonShape groundShape = new PolygonShape();
    public static Fixture groundFixture;

    Filter groundFilter;

    int groundLength = 10000;
    float groundHeight = 0.5f;
//    FixtureDef groundFixtureDef;

    Ground() {
        groundBodyDef.position.set(groundLength/2f,groundHeight/2f);
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBody = GdxGame.world.createBody(groundBodyDef);

        groundShape.setAsBox(groundLength, groundHeight);
        groundBody.createFixture(groundShape, 0f);
        groundFilter = new Filter();
        groundFilter.categoryBits = Globals.GROUND_BIT;
//        groundFixtureDef = new FixtureDef();
//        groundFixtureDef.shape = groundShape;
        groundFixture = groundBody.createFixture(groundShape, 1f);
        groundFixture.setUserData(this);
        groundFixture.setFilterData(groundFilter);
        groundFixture.setFriction(1);

        System.out.println(groundBody.getWorldPoint(new Vector2(0f, 0f)));

    }
}
// make objs with bodies inside of em,
// managed by other classes