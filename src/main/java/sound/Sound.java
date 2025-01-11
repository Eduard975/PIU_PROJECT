package sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Sound {
    private Clip clip;
    private Path soundPath;
    private FloatControl volumeControl;
    private long clipTimePosition;

    public Sound(String path) {
        try {
            soundPath = Paths.get(path);
            AudioInputStream audioIN = AudioSystem.getAudioInputStream(soundPath.toFile());
            clip = AudioSystem.getClip();
            clip.open(audioIN);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
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
    }

    public void setVolume(float volume) {
        if (volume < 0.0f || volume > 1.0f) {
            throw new Error("Volume vale should be between 0 and 1");
        }

        float min = -40.0f;
        float max = 0;

        System.out.println(max);
        float value = min + (max - min) * volume;

        volumeControl.setValue(value);
    }
}
