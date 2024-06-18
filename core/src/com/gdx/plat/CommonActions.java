package com.gdx.plat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class CommonActions {

    // need common component with curr_direction
    // bools can be static (?) but time-based get automatically updated, no need implement for them
    // Body
    // other important information

    

    public static void createPlayerRelativeContactFixture(float playerRelativeWidth, float playerRelativeHeight
            , float playerEdgeRelativeX, float playerTopRelativeY, ActionComponent actionComponent) {
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
        // x, y relative to playerBody.getCenter()
        fixtureDef.shape = sensorShape;
        // y = up
        ;
        // when not relative player, create bullet (?)

        Fixture newFixture = actionComponent.body.createFixture(fixtureDef);
        System.out.println("in common actions");
        newFixture.setUserData(actionComponent);
        Filter newFilter = new Filter();
        newFilter.categoryBits = 4;
        newFixture.setFilterData(newFilter);

//        fixtureDef.shape = Shape();
        // queue to handle this, shapes?
    }

    public static void deleteLastExtraFixture(ActionComponent actionComponent) {
        if (actionComponent.body.getFixtureList().size > 0) {
            actionComponent.body.destroyFixture(
                    actionComponent.body.getFixtureList().get(actionComponent.body.getFixtureList().size - 1));
        }
    }


}
