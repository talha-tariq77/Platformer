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
        float deltaTime = Gdx.graphics.getDeltaTime();

        switch (keyCode) {
            case Input.Keys.SPACE:
//                if (player.updateState(Player.State.AIRBORNE)) {
//                    player.jump();
//                    player.updateAnimationCallTime(deltaTime);
//                }
                player.jump();
                player.updateState(deltaTime);
                break;
            case Input.Keys.A:
                player.moveX(-1);
                player.updateState(deltaTime);
//                if (!(Gdx.input.isKeyPressed(Input.Keys.D))) {
//                    if (player.updateState(Player.State.MOVING)) {
//                        player.updateAnimationCallTime(deltaTime);
//                    }
//                }
                break;
            case Input.Keys.D:
                player.moveX(1);
                player.updateState(deltaTime);
//                if (!(Gdx.input.isKeyPressed(Input.Keys.A))) {
//                    if (player.updateState(Player.State.MOVING)) {
//                        player.updateAnimationCallTime(deltaTime);
//                    }
//                }
                break;
            case Input.Keys.Q:
                player.attack();
                player.updateState(deltaTime);
//                if (player.updateState(Player.State.ATTACKING)) {
//                    player.updateAnimationCallTime(deltaTime);
//                }
                break;
        }
        return true;
    }
}

