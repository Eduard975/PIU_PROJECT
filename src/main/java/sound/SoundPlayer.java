package sound;

public class SoundPlayer {
    public final Sound SHOOT;
    public final Sound GAME_OVER;
    public final Sound EXPLOSION;

    public final Sound BACKGROUND_MUSIC;

    public SoundPlayer() {
        SHOOT = new Sound("src/main/resources/sounds/fireball.wav");
        GAME_OVER = new Sound("src/main/resources/sounds/game_over.wav");
        EXPLOSION = new Sound("src/main/resources/sounds/explosion.wav");

        BACKGROUND_MUSIC = new Sound("src/main/resources/sounds/background.wav");
    }

    public void setAllSoundToVolume(float volume) {
        if (volume < 0.0f || volume > 1.0f) {
            throw new Error("Volume vale should be between 0 and 1");
        }

        SHOOT.setVolume(volume, false);
        GAME_OVER.setVolume(volume, false);
        EXPLOSION.setVolume(volume, false);


        BACKGROUND_MUSIC.setVolume(volume, true);
    }
}
