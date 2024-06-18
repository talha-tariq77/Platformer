package com.gdx.plat;

import com.badlogic.gdx.physics.box2d.Body;

import java.sql.Time;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Action {

    TimeRange range;

    Consumer<ActionComponent> consumer;
    // bi consumer with bools
    // bools static val

    Action(TimeRange range, Consumer<ActionComponent> consumer) {
        this.range = range;
        this.consumer = consumer;
    }
}
