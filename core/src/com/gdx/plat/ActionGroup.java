package com.gdx.plat;

import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

import java.util.function.Consumer;

public class ActionGroup {

    ArrayList<Action> actions;

    ActionComponent actionComponent;

    Consumer<ActionComponent> completeAction;

    ActionGroup(ActionComponent actionComponent) {
        // add all actions based on time ranges
        // pass this into super
        this.actions = new ArrayList<>();
        this.actionComponent = actionComponent;

        // when completed = true, then the frame-time based ones can do one last action
    }

    public boolean hasAction(int keyFrameIndex) {
        for (Action action:
            actions) {
            if (action.range.rangeStart <= keyFrameIndex && action.range.rangeEnd >= keyFrameIndex) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Consumer<ActionComponent>> getCurrentAction(int keyFrameIndex) {
        ArrayList<Consumer<ActionComponent>> currActions = new ArrayList<>();
        for (Action action:
             actions) {
            if (action.range.rangeStart <= keyFrameIndex && action.range.rangeEnd >= keyFrameIndex) {
                currActions.add(action.consumer);
            }
        }
        return currActions;
    }

    // add physics bools


    // continuous, add actions to


}
