package com.gdx.plat;

public class Globals {
    public static int WINDOW_WIDTH = 1280;
    public static int WINDOW_HEIGHT = 800;

    static int VIEWPORT_WIDTH = 10;

    static float VIEWPORT_HEIGHT = 10;

    static float WIDTH_FACTOR = (float) VIEWPORT_WIDTH / WINDOW_WIDTH;

    static float HEIGHT_FACTOR = (float) VIEWPORT_HEIGHT / WINDOW_HEIGHT;

    static int GRAVITY = -10;

    static int GRAVITY_MULTIPLIER = 8;
    public static final float PLAYER_HEIGHT = 1.75f;
    public static final float PLAYER_WIDTH = 0.72916f;

    public static final short PLAYER_BIT = 1;
    public static final short GROUND_BIT = 2;

    public static final float SKIN_WIDTH = (2f/WINDOW_WIDTH) * VIEWPORT_WIDTH;
    public static final float SKIN_HEIGHT = (2f/WINDOW_HEIGHT) * VIEWPORT_HEIGHT;
}
