package com.gdx.plat;

public class Attack1ActionGroup extends ActionGroup{

    Attack1ActionGroup(ActionComponent actionComponent){
        super(actionComponent);

        actions.add(new Action(new TimeRange(2,2),
                (actionComponent1)-> CommonActions.createPlayerRelativeContactFixture(20,
                        16, 0, 32,
                        actionComponent1)));

        actions.add(new Action(new TimeRange(3,3),
                CommonActions::deleteLastExtraFixture));

        actions.add(new Action(new TimeRange(3, 3),
                (actionComponent1 -> CommonActions.createPlayerRelativeContactFixture(11,
                        24, 0, 24,
                        actionComponent1))));

        completeAction = CommonActions::deleteLastExtraFixture;
    }
}

// state
// actions, action groups
//
