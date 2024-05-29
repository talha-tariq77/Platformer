package com.gdx.plat;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ArrayReflection;


// just redo entire class
// cpy paste and edit whole class

// cant have relationship, since private variables in parent/instantiation, which cannot access
// since they are private


// extraAnimation and extraAnimation with duration array

// e.g. common setters/getters, names and return types
// extract common functionality to abstract class

public class ExtraAnimation<T> {
//    Array<Float> frameDurations;
// player animation (component) stores animation information

        // super just refers to parent

        // no loop callback
        // different frame durations

        // state manager, or above manager
        // class + animation state + frame -> fixtures (+fixtures using some other component)

    // can put animation vals all in one class

    public enum PlayMode {
        NORMAL, REVERSED, LOOP, LOOP_REVERSED, LOOP_PINGPONG, LOOP_RANDOM,
    }

    /** Length must not be modified without updating {@link #animationDuration}. See {@link #setKeyFrames(T[])}. */
    T[] keyFrames;
    float frameDuration;
    float animationDuration;
    int lastFrameNumber;
    float lastStateTime;

    private Animation.PlayMode playMode = Animation.PlayMode.NORMAL;

    /** Constructor, storing the frame duration and key frames.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames the objects representing the frames. If this Array is type-aware, {@link #getKeyFrames()} can return the
     *           correct type of array. Otherwise, it returns an Object[]. */
    public ExtraAnimation (float frameDuration, Array<? extends T> keyFrames) {
        this.frameDuration = frameDuration;
        Class arrayType = keyFrames.items.getClass().getComponentType();
        T[] frames = (T[]) ArrayReflection.newInstance(arrayType, keyFrames.size);
        for (int i = 0, n = keyFrames.size; i < n; i++) {
            frames[i] = keyFrames.get(i);
        }
        setKeyFrames(frames);
    }

    /** Constructor, storing the frame duration and key frames.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames the objects representing the frames. If this Array is type-aware, {@link #getKeyFrames()} can return the
     *           correct type of array. Otherwise, it returns an Object[]. */
    public ExtraAnimation (float frameDuration, Array<? extends T> keyFrames, Animation.PlayMode playMode) {
        this(frameDuration, keyFrames);
        setPlayMode(playMode);
    }

    /** Constructor, storing the frame duration and key frames.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames the objects representing the frames. */
    public ExtraAnimation (float frameDuration, T... keyFrames) {
        this.frameDuration = frameDuration;
        setKeyFrames(keyFrames);
    }

    /** Returns a frame based on the so called state time. This is the amount of seconds an object has spent in the state this
     * Animation instance represents, e.g. running, jumping and so on. The mode specifies whether the animation is looping or not.
     *
     * @param stateTime the time spent in the state represented by this animation.
     * @param looping whether the animation is looping or not.
     * @return the frame of animation for the given state time. */
    public T getKeyFrame (float stateTime, float lastCallTime, boolean looping) {
        // we set the play mode by overriding the previous mode based on looping
        // parameter value
        Animation.PlayMode oldPlayMode = playMode;
        if (looping && (playMode == Animation.PlayMode.NORMAL || playMode == Animation.PlayMode.REVERSED)) {
            if (playMode == Animation.PlayMode.NORMAL)
                playMode = Animation.PlayMode.LOOP;
            else
                playMode = Animation.PlayMode.LOOP_REVERSED;
        } else if (!looping && !(playMode == Animation.PlayMode.NORMAL || playMode == Animation.PlayMode.REVERSED)) {
            if (playMode == Animation.PlayMode.LOOP_REVERSED)
                playMode = Animation.PlayMode.REVERSED;
            else
                playMode = Animation.PlayMode.LOOP;
        }

        T frame = getKeyFrame(stateTime, lastCallTime);
        playMode = oldPlayMode;
        return frame;
    }

    /** Returns a frame based on the so called state time. This is the amount of seconds an object has spent in the state this
     * Animation instance represents, e.g. running, jumping and so on using the mode specified by {@link #setPlayMode(Animation.PlayMode)}
     * method.
     *
     * @param stateTime
     * @return the frame of animation for the given state time. */
    public T getKeyFrame (float stateTime, float lastCallTime) {
        int frameNumber = getKeyFrameIndex(stateTime, lastCallTime);
        return keyFrames[frameNumber];
    }

    /** Returns the current frame number.
     * @param stateTime
     * @return current frame number */
    public int getKeyFrameIndex (float stateTime, float lastCallTime) {
        if (keyFrames.length == 1) return 0;

        int frameNumber = (int)((stateTime - lastCallTime) / frameDuration);
        switch (playMode) {
            case NORMAL:
                frameNumber = Math.min(keyFrames.length - 1, frameNumber);
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

        lastFrameNumber = frameNumber;
        lastStateTime = stateTime;

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
    public Animation.PlayMode getPlayMode () {
        return playMode;
    }

    /** Sets the animation play mode.
     *
     * @param playMode The animation {@link Animation.PlayMode} to use. */
    public void setPlayMode (Animation.PlayMode playMode) {
        this.playMode = playMode;
    }
    // if not looping, animation

    /** Whether the animation would be finished if played without looping (PlayMode#NORMAL), given the state time.
     * @param stateTime
     * @return whether the animation is finished. */
    public boolean isAnimationFinished (float stateTime, float lastCallTime) {
        int frameNumber = (int)((stateTime - lastCallTime) / frameDuration);
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




    // for each animation
    // animation duration for each frame can be different
    // get keyframe, draw fixture based on keyframe
    // do true non-looping animation, by getting time since called
    // check if looping actually always starts at frame=0
    // looping does'nt start at frame=0
    // just carries on as before


    // need to store the lastCalledTime for each state


    // each animation stores animation duration for each frame, last called time
}
