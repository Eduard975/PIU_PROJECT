package sound;

import javax.sound.sampled.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Sound {
    private Clip clip;
    private Path soundPath;
    private FloatControl volumeControl;
    private long clipTimePosition;

    public boolean hasFinished = true;

    public Sound(String path) {
        try {
            soundPath = Paths.get(path);
            AudioInputStream audioIN = AudioSystem.getAudioInputStream(soundPath.toFile());
            clip = AudioSystem.getClip();
            clip.open(audioIN);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP && clip.getMicrosecondPosition() >= clip.getMicrosecondLength()) {
                    hasFinished = true;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        hasFinished = false;
        clip.setMicrosecondPosition(0);
        clip.start();
    }

    public void pause() {
        clipTimePosition = clip.getMicrosecondPosition();
        clip.stop();
    }

    public void resume() {
        clip.setMicrosecondPosition(clipTimePosition);
        clip.start();
    }

    public void reset() {
        clip.stop();
        clip.setMicrosecondPosition(0);
        hasFinished = false;
    }

    public void setVolume(float volume, boolean isBackgroundMusic) {
        if (volume < 0.0f || volume > 1.0f) {
            throw new IllegalArgumentException("Volume value should be between 0 and 1");
        }

        float min = -40f;
        float max = volumeControl.getMaximum();
        float value = min + (max - min) * volume;

        if (isBackgroundMusic) {
            value -= 10f;
        }

        volumeControl.setValue(value);
    }
}
