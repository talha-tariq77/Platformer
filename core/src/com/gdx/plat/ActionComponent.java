package com.gdx.plat;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class ActionComponent {

    Body body;

    HashMap<Integer, Boolean> stateBools;

    int curr_direction;

    HashMap<String, Integer> stateDict;

    ArrayList<Action> actions;

    ActionComponent(Body body, HashMap<Integer, Boolean> stateBools, HashMap<String, Integer> stateDict,
                    int curr_direction) {
        this.body = body;
        this.stateBools = stateBools;
        this.stateDict = stateDict;
        this.curr_direction = curr_direction;
    }
}
