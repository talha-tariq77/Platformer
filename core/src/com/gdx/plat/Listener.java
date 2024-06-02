package com.gdx.plat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class Listener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        short catA = fixtureA.getFilterData().categoryBits;
        short catB = fixtureB.getFilterData().categoryBits;
//        short tmp;
        if (catB > catA) {
            fixtureA = fixtureB;
            fixtureB = contact.getFixtureA();
        }
        // fixtureA always has the higher category bits value

        // category bits are a short - 16 bits
        // so you would set one bit to 1 per category
        // allows for easy checking of categories

        // this means categoryOr returns
        // a bitstring of 1's per category:
        // so if catA = 1, catB=2
        // bitstring last 2 values = 11
        // then you check which category is which, and do the function on that basis.

        int categoryOr = catA | catB;


        switch (categoryOr) {
            // fixtureA always has the higher category bits value
            case (Globals.GROUND_BIT | Globals.PLAYER_BIT) :
                ((Player) fixtureB.getUserData()).stateBools.put(Player.State.AIRBORNE, false);
                ((Player) fixtureB.getUserData()).updateState();
                System.out.println("landed");
                break;
//            case (1 | 4):
//                System.out.println("hi");
        }


//
//        if (fixtureA.getFilterData().categoryBits == 1) {
//
//        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        short catA = fixtureA.getFilterData().categoryBits;
        short catB = fixtureB.getFilterData().categoryBits;
//        short tmp;
        if (catB > catA) {
            fixtureA = fixtureB;
            fixtureB = contact.getFixtureA();
        }

        int categoryOr = catA | catB;
        // fixtureA always has the higher category bits value


        switch (categoryOr) {
            // fixtureA always has the higher category bits value
            case (Globals.GROUND_BIT | Globals.PLAYER_BIT) :
                ((Player) fixtureB.getUserData()).stateBools.put(Player.State.AIRBORNE, true);
                ((Player) fixtureB.getUserData()).updateState();

                break;
            case (1 | 4):
                System.out.println("bye");
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
//        Fixture fixtureA = contact.getFixtureA();
//        Fixture fixtureB = contact.getFixtureB();
//        short catA = fixtureA.getFilterData().categoryBits;
//        short catB = fixtureB.getFilterData().categoryBits;
////        short tmp;
//        if (catB > catA) {
//            fixtureA = fixtureB;
//            fixtureB = contact.getFixtureA();
//        }

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // use to handle collision impulse results

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        short catA = fixtureA.getFilterData().categoryBits;
        short catB = fixtureB.getFilterData().categoryBits;
//        short tmp;
        if (catB > catA) {
            fixtureA = fixtureB;
            fixtureB = contact.getFixtureA();
        }


        int categoryOr = catB | catA;

        switch(categoryOr) {
            case (Globals.GROUND_BIT | Globals.PLAYER_BIT):
//                if (fixtureB.getBody().getLinearVelocity().x > 0) {
//                    ((Player) fixtureB.getUserData()).moveX(1);
//                }
//                else if (fixtureB.getBody().getLinearVelocity().x < 0) {
//                    ((Player) fixtureB.getUserData()).moveX(-1);
//                }
        }

    }
}
