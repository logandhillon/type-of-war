package com.logandhillon.typeofwar.resource;

import javafx.scene.media.AudioClip;

import java.util.Objects;

/**
 * @author Logan Dhillon
 */
public class Audios {
    public static final AudioClip COUNTDOWN = new AudioClip(
            Objects.requireNonNull(Audios.class.getResource("/sound/countdown.wav")).toExternalForm());
    public static final AudioClip COUNTDOWN_END = new AudioClip(
            Objects.requireNonNull(Audios.class.getResource("/sound/countdown1.wav")).toExternalForm());
}
