package com.gdx.plat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputHandler extends InputAdapter {
    Player player;
    InputHandler(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keyCode) {

        switch (keyCode) {
            case Input.Keys.SPACE:
                if (player.updateState(Player.State.AIRBORNE)) {
                    player.jump();
                    player.resetCallTime();
                }
                break;
            case Input.Keys.A:
                if (player.updateState(Player.State.MOVING)) {
                    player.resetCallTime();

                }
                break;
            case Input.Keys.D:
                System.out.println("detected keydown D");
                if (player.updateState(Player.State.MOVING)) {
                    player.resetCallTime();
                }

                break;
            case Input.Keys.Q:
                if (player.updateState(Player.State.ATTACKING)) {
                    player.resetCallTime();
                }
                break;
        }
        return true;
    }
}

