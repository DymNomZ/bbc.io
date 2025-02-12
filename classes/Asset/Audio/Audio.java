package classes.Asset.Audio;

import javax.sound.sampled.*;

import static src.Utils.showError;

public class Audio {
    public static float DEFAULT_VOLUME = -15f;

    private Clip stream = null;
    private float volume = DEFAULT_VOLUME;
    private FloatControl controller;
    private boolean isRepeat;

    public Audio play() {
        if (stream == null) return this;
        stream.stop();
        stream.setMicrosecondPosition(0);
        stream.start();
        return this;
    }

    public Audio load(String filePath) {
        if (stream != null) {
            stream.close();
        }

        Clip stream;

        try {
            stream = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            showError("[AUDIO] Player cannot be initialized: \n" + e.getMessage());
            return this;
        }

        try {
            stream.open(AudioSystem.getAudioInputStream(Audio.class.getResourceAsStream("../../../assets/sounds/" + filePath)));
            controller = (FloatControl) stream.getControl(FloatControl.Type.MASTER_GAIN);
            controller.setValue(volume);

            Audio parent = this;

            stream.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (isRepeat && event.getType() == LineEvent.Type.STOP) parent.play();
                }
            });
        } catch (Exception e) {
            showError("[AUDIO LOADER] Error at opening audio file" + filePath + " with Error Message:\n" + e.getMessage());
            return this;
        }

        if (this.stream != null) {
            this.stream.close();
        }
        this.stream = stream;
        return this;
    }

    public Audio stop() {
        if (stream == null) return this;
        stream.stop();
        return this;
    }

    public float getVolume() {
        return volume;
    }

    public Audio setVolume(float decibels) {
        if (stream == null) return this;
        volume = decibels;
        controller.setValue(decibels);
        return this;
    }

    public Audio setRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
        return this;
    }

    public void close() {
        stream.close();
    }
}