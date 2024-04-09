package com.gdx.plat;

import com.badlogic.gdx.physics.box2d.*;

public class Listener implements ContactListener {

    public Object helper(Fixture fixtureA, Fixture fixtureB, int category) {
        if (fixtureA.getFilterData().categoryBits == category) {
            return fixtureA.getUserData();
        }
        else {
            return fixtureB.getUserData();
        }
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        short catA = fixtureA.getFilterData().categoryBits;
        short catB = fixtureB.getFilterData().categoryBits;

        int categoryOr = catA | catB;

        switch (categoryOr) {
            case (1 | 2) :
                if (catA == 1) {
                    ((Player) fixtureA.getUserData()).STUN = false;
                    System.out.println("check");
                }
                else {
                    ((Player) fixtureB.getUserData()).STUN = false;
                    System.out.println("check");
                }
                break;
            case (1 | 4):
                System.out.println("hi");
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

        int categoryOr = catA | catB;

        switch (categoryOr) {
            case (1 | 2) :
                if (catA == 1) {
                    ((Player) fixtureA.getUserData()).STUN = true;
                    System.out.println("Contact ended");
                }
                break;
            case (1 | 4):
                System.out.println("bye");
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
