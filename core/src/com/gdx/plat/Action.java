package com.gdx.plat;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class Action {
    public Map<Range, FrameAction> frameActions;

    Action () {

        // instead of creating frameAction using Functional interface (SAM) and method name
        // just initialise it in the add

        frameActions.put(new Range(1f, 4f), this::test);

//        Consumer<Object> a = (hi) -> test(new ArrayList<>());


        // consumer does not produce the nullpointerexception warning
        // the ide is prob not detecting that FrameAction is a SAM
    }

    public void test(Object Actor) {

        System.out.println("started");
    }


}
