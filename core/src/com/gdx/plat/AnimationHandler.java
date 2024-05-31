package com.gdx.plat;

import java.util.HashMap;
import java.util.Map;

public class AnimationHandler {
    HashMap<Float, ActionGroup> actionGroups;

    ActionGroup attackGroup;
    HashMap<Float, Policy> attackGroupExtPolicies;


    ActionGroup movementGroup;
    HashMap<Float, Policy> movementGroupExtPolicies;


    ActionGroup idle;
    HashMap<Float, Policy> idleExtPolicies;

    ActionGroup airborne;

    HashMap<Float, Policy> airborneExtPolicies;


    AnimationHandler() {
        // put where cna interrupt
        // lower one puts if combinable
        idleExtPolicies = new HashMap<>();

        idleExtPolicies.put(2f, Policy.INTERRUPT);

        movementGroupExtPolicies = new HashMap<>();

        movementGroupExtPolicies.put(1f, Policy.INTERRUPT);
        movementGroupExtPolicies.put(3f, Policy.COMBINE);
        movementGroupExtPolicies.put(4f, Policy.COMBINE);

        airborneExtPolicies = new HashMap<>();

        airborneExtPolicies.put(1f, Policy.INTERRUPT);
        airborneExtPolicies.put(2f, Policy.COMBINE);
        airborneExtPolicies.put(4f, Policy.COMBINE);


        attackGroupExtPolicies = new HashMap<>();
        attackGroupExtPolicies.put(1f, Policy.INTERRUPT);

        idle = new ActionGroup(Policy.NONE, idleExtPolicies);

        movementGroup = new ActionGroup(Policy.NONE, movementGroupExtPolicies);

        airborne = new ActionGroup(Policy.NONE, airborneExtPolicies);
        // update none once >1 in groups

        attackGroup = new ActionGroup(Policy.NONE, attackGroupExtPolicies);


        actionGroups = new HashMap<>();
        actionGroups.put(1f, idle);
        actionGroups.put(2f, movementGroup);
        actionGroups.put(3f, airborne);
        actionGroups.put(4f, attackGroup);
    }
}

