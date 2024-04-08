package com.gdx.plat;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Listener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        System.out.println(contact.getFixtureA().getFilterData().categoryBits);
        System.out.println(contact.getFixtureB().getFilterData().categoryBits);
        if (contact.getFixtureA().getFilterData().categoryBits == 1 && contact.getFixtureB().getFilterData().categoryBits == 2) {
            System.out.println("Player hit the ground");
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
