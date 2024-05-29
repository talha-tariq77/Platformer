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
                if (player.updateState(Player.State.AIRBORNE)) {
                    player.jump();
                    player.updateAnimationCallTime(deltaTime);
                }
                break;
            case Input.Keys.A:
                if (!(Gdx.input.isKeyPressed(Input.Keys.D))) {
                    if (player.updateState(Player.State.MOVING)) {
                        player.updateAnimationCallTime(deltaTime);
                    }
                }
                break;
            case Input.Keys.D:
                if (!(Gdx.input.isKeyPressed(Input.Keys.A))) {
                    if (player.updateState(Player.State.MOVING)) {
                        player.updateAnimationCallTime(deltaTime);
                    }
                }
                break;
            case Input.Keys.Q:
                if (player.updateState(Player.State.ATTACKING)) {
                    player.updateAnimationCallTime(deltaTime);
                }
                break;
        }
        return true;
    }
}

