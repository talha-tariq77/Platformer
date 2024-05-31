package com.gdx.plat;

import java.util.HashMap;

public class ActionGroup {
    // find the action group using index via search tree
    Policy internalPolicy;

    HashMap<Float, Policy> externalPolicies;

    ActionGroup(Policy internalPolicy, HashMap<Float, Policy> externalPolicies) {
        this.internalPolicy = internalPolicy;
        this.externalPolicies = externalPolicies;
    }
}
