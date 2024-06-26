package com.gdx.plat;

public class Globals {
    public static int WINDOW_WIDTH = 1280;
    public static int WINDOW_HEIGHT = 800;

    static int VIEWPORT_WIDTH = 10;

    static float VIEWPORT_HEIGHT = 10;

    static float WIDTH_FACTOR = (float) VIEWPORT_WIDTH / WINDOW_WIDTH;
    // pixels per unit


    // in game objects have been set to a different size/scale
    // since i have set them arbitrarily according to the in-game size i wanted
    // my player sprite to appear (keeping proportion)

    static float PLAYER_PIXELS_WIDTH = 20;

    public static final float PLAYER_HEIGHT = 1.75f;
    public static final float PLAYER_WIDTH = 0.73f;

    static float PIXELS_PER_UNIT = (float) PLAYER_PIXELS_WIDTH / PLAYER_WIDTH;

    static float PLAYER_PIXELS_HEIGHT = PLAYER_HEIGHT * PIXELS_PER_UNIT;


    static float UNITS_PER_PIXEL = (float) PLAYER_WIDTH / PLAYER_PIXELS_WIDTH;

    static float HEIGHT_FACTOR = (float) VIEWPORT_HEIGHT / WINDOW_HEIGHT;

    static int GRAVITY = -10;

    static int GRAVITY_MULTIPLIER = 8;

    public static final short PLAYER_BIT = 1;
    public static final short GROUND_BIT = 2;

    public static final float SKIN_WIDTH = (2f/WINDOW_WIDTH) * VIEWPORT_WIDTH;
    public static final float SKIN_HEIGHT = (2f/WINDOW_HEIGHT) * VIEWPORT_HEIGHT;

    public static final float PLAYER_WIDTH_CONVERT = PLAYER_WIDTH / 20;
    // width without sword = 20

//    public static final float PLAYER_HEIGHT_CONVERT = PLAYER_HEIGHT /
}
