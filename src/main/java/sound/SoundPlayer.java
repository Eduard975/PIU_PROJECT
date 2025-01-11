package sound;

public class SoundPlayer {
    public final Sound SHOOT;
    public final Sound PLAYER_DEATH;

    public SoundPlayer() {
        SHOOT = new Sound("src/main/resources/sounds/fireball.wav");
        PLAYER_DEATH = new Sound("src/main/resources/sounds/game_over.wav");
    }

    public void setAllSoundToVolume(float volume) {
        if (volume < 0.0f || volume > 1.0f) {
            throw new Error("Volume vale should be between 0 and 1");
        }

        SHOOT.setVolume(volume);
    }
}
