package com.gdx.plat;

import java.util.ArrayList;

public class EventHandler {
    public static ArrayList<Event> events;


    public void handle(Event event) {
//        AnimationHandler.currState = Player.State.NO_STATE;
    }

    public void run() {
        for (Event event: events) {
            handle(event);
        }
    }
}
