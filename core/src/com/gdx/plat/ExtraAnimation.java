package com.gdx.plat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ArrayReflection;

/**
 * <p>
 * An Animation stores a list of objects representing an animated sequence, e.g. for running or jumping. Each object in the
 * Animation is called a key frame, and multiple key frames make up the animation.
 * <p>
 * The animation's type is the class representing a frame of animation. For example, a typical 2D animation could be made up of
 * {@link com.badlogic.gdx.graphics.g2d.TextureRegion TextureRegions} and would be specified as:
 * <p>
 * <code>Animation&lt;TextureRegion&gt; myAnimation = new Animation&lt;TextureRegion&gt;(...);</code>
 *
 * @author mzechner */
public class ExtraAnimation<T> {

    public enum PlayMode {
        NORMAL, REVERSED, LOOP, LOOP_REVERSED, LOOP_PINGPONG, LOOP_RANDOM,
    }

    /** Length must not be modified without updating {@link #animationDuration}. See {@link #setKeyFrames(T[])}. */
    T[] keyFrames;
    private float frameDuration;
    private float animationDuration;
    public int lastFrameNumber;
    private float lastStateTime;

    public boolean completed;

    public boolean looping;

    public boolean frameNumberChanged;
    private PlayMode playMode = PlayMode.NORMAL;

    /** Constructor, storing the frame duration and key frames.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames the objects representing the frames. If this Array is type-aware, {@link #getKeyFrames()} can return the
     *           correct type of array. Otherwise, it returns an Object[]. */
    public ExtraAnimation (float frameDuration, Array<? extends T> keyFrames, boolean looping) {
        this.frameDuration = frameDuration;
        Class arrayType = keyFrames.items.getClass().getComponentType();
        T[] frames = (T[])ArrayReflection.newInstance(arrayType, keyFrames.size);
        for (int i = 0, n = keyFrames.size; i < n; i++) {
            frames[i] = keyFrames.get(i);
        }
        setKeyFrames(frames);

        completed = false;
        frameNumberChanged = false;
        this.looping = looping;
    }

    /** Constructor, storing the frame duration and key frames.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames the objects representing the frames. If this Array is type-aware, {@link #getKeyFrames()} can return the
     *           correct type of array. Otherwise, it returns an Object[]. */
    public ExtraAnimation (float frameDuration, Array<? extends T> keyFrames, PlayMode playMode, boolean looping) {
        this(frameDuration, keyFrames, looping);
        setPlayMode(playMode);
        completed = false;
        frameNumberChanged = false;
    }

    /** Constructor, storing the frame duration and key frames.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames the objects representing the frames. */
    public ExtraAnimation (float frameDuration, boolean looping, T... keyFrames) {
        this.frameDuration = frameDuration;
        setKeyFrames(keyFrames);
        this.looping = looping;
        completed = false;
        frameNumberChanged = false;
    }

    /** Returns a frame based on the so called state time. This is the amount of seconds an object has spent in the state this
     * Animation instance represents, e.g. running, jumping and so on. The mode specifies whether the animation is looping or not.
     *
     * @param stateTime the time spent in the state represented by this animation.
     * @return the frame of animation for the given state time. */
    public T getKeyFrame (float stateTime) {
        // we set the play mode by overriding the previous mode based on looping
        // parameter value
        PlayMode oldPlayMode = playMode;
        if (looping && (playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)) {
            if (playMode == PlayMode.NORMAL)
                playMode = PlayMode.LOOP;
            else
                playMode = PlayMode.LOOP_REVERSED;
        } else if (!looping && !(playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)) {
            if (playMode == PlayMode.LOOP_REVERSED)
                playMode = PlayMode.REVERSED;
            else
                playMode = PlayMode.LOOP;
        }

        T frame = getKeyFrame2(stateTime);
        playMode = oldPlayMode;
        return frame;
    }

    /** Returns a frame based on the so called state time. This is the amount of seconds an object has spent in the state this
     * Animation instance represents, e.g. running, jumping and so on using the mode specified by {@link #setPlayMode(PlayMode)}
     * method.
     *
     * @param stateTime
     * @return the frame of animation for the given state time. */
    public T getKeyFrame2 (float stateTime) {
        int frameNumber = getKeyFrameIndex(stateTime, true);
        return keyFrames[frameNumber];
    }

    public int justGetKeyFrameIndex(float stateTime) {
        PlayMode oldPlayMode = playMode;
        if (looping && (playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)) {
            if (playMode == PlayMode.NORMAL)
                playMode = PlayMode.LOOP;
            else
                playMode = PlayMode.LOOP_REVERSED;
        } else if (!looping && !(playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)) {
            if (playMode == PlayMode.LOOP_REVERSED)
                playMode = PlayMode.REVERSED;
            else
                playMode = PlayMode.LOOP;
        }
        int i = getKeyFrameIndex(stateTime, false);
        playMode = oldPlayMode;
        return i;
    }

    /** Returns the current frame number.
     * @param stateTime
     * @return current frame number */
    public int getKeyFrameIndex (float stateTime, boolean update) {
        if (keyFrames.length == 1) return 0;

        int frameNumber = (int)(stateTime / frameDuration);
        switch (playMode) {
            case NORMAL:
                if (frameNumber > keyFrames.length - 1) {
                    frameNumber = keyFrames.length - 1;
                    if (update) {
                        completed = true;
                    }
                }
                break;
            case LOOP:
                frameNumber = frameNumber % keyFrames.length;
                break;
            case LOOP_PINGPONG:
                frameNumber = frameNumber % ((keyFrames.length * 2) - 2);
                if (frameNumber >= keyFrames.length) frameNumber = keyFrames.length - 2 - (frameNumber - keyFrames.length);
                break;
            case LOOP_RANDOM:
                int lastFrameNumber = (int)((lastStateTime) / frameDuration);
                if (lastFrameNumber != frameNumber) {
                    frameNumber = MathUtils.random(keyFrames.length - 1);
                } else {
                    frameNumber = this.lastFrameNumber;
                }
                break;
            case REVERSED:
                frameNumber = Math.max(keyFrames.length - frameNumber - 1, 0);
                break;
            case LOOP_REVERSED:
                frameNumber = frameNumber % keyFrames.length;
                frameNumber = keyFrames.length - frameNumber - 1;
                break;
        }

        if (update) {
            frameNumberChanged = frameNumber != lastFrameNumber;
            lastFrameNumber = frameNumber;
            lastStateTime = stateTime;
        }


        return frameNumber;
    }

    /** Returns the keyframes[] array where all the frames of the animation are stored.
     * @return The keyframes[] field. This array is an Object[] if the animation was instantiated with an Array that was not
     *         type-aware. */
    public T[] getKeyFrames () {
        return keyFrames;
    }

    protected void setKeyFrames (T... keyFrames) {
        this.keyFrames = keyFrames;
        this.animationDuration = keyFrames.length * frameDuration;
    }

    /** Returns the animation play mode. */
    public PlayMode getPlayMode () {
        return playMode;
    }

    /** Sets the animation play mode.
     *
     * @param playMode The animation {@link PlayMode} to use. */
    public void setPlayMode (PlayMode playMode) {
        this.playMode = playMode;
    }

    /** Whether the animation would be finished if played without looping (PlayMode#NORMAL), given the state time.
     * @param stateTime
     * @return whether the animation is finished. */
    public boolean isAnimationFinished (float stateTime) {
        int frameNumber = (int)(stateTime / frameDuration);
        return keyFrames.length - 1 < frameNumber;
    }

    /** Sets duration a frame will be displayed.
     * @param frameDuration in seconds */
    public void setFrameDuration (float frameDuration) {
        this.frameDuration = frameDuration;
        this.animationDuration = keyFrames.length * frameDuration;
    }

    /** @return the duration of a frame in seconds */
    public float getFrameDuration () {
        return frameDuration;
    }

    /** @return the duration of the entire animation, number of frames times frame duration, in seconds */
    public float getAnimationDuration () {
        return animationDuration;
    }
}
