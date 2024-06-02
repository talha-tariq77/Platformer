package com.gdx.plat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;

public class InputHandler extends InputAdapter {
    Player player;
    InputHandler(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keyCode) {

        switch (keyCode) {
            case Input.Keys.SPACE:
//                if (player.updateState(Player.State.AIRBORNE)) {
//                    player.jump();
//                    player.updateAnimationCallTime(deltaTime);
//                }
                player.jump();
                if (player.updateState()) {
                    player.resetAnimationCallTime();
                }
                break;
            case Input.Keys.A:
                player.moveX(-1);
                if (player.updateState()) {
                    player.resetAnimationCallTime();
                }
//                if (!(Gdx.input.isKeyPressed(Input.Keys.D))) {
//                    if (player.updateState(Player.State.MOVING)) {
//                        player.updateAnimationCallTime(deltaTime);
//                    }
//                }
                break;
            case Input.Keys.D:
                player.moveX(1);
                if (player.updateState()) {
                    player.resetAnimationCallTime();
                }
//                if (!(Gdx.input.isKeyPressed(Input.Keys.A))) {
//                    if (player.updateState(Player.State.MOVING)) {
//                        player.updateAnimationCallTime(deltaTime);
//                    }
//                }
                break;
            case Input.Keys.Q:
                player.attack();
                if (player.updateState()) {
                    player.resetAnimationCallTime();
                }

                // reset for one-time is diff
                // one time - time-based
                // constant - non-time based (e.g. jump, contact)

//                if (player.updateState(Player.State.ATTACKING)) {
//                    player.updateAnimationCallTime(deltaTime);
//                }
                break;
        }
        return true;
    }
}

