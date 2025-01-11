package player;

import entities.EnemyBase;
import graphic.Texture;

public class StunAbility extends AbilityBase {
    public StunAbility(int cost, int cooldown, Player player) {
        super(cost, cooldown, player);
        this.icon = new Texture("src/main/resources/images/stun.png");
    }

    public static EnemyBase enemyToStun;
    public static int stunDuration = 200;

    @Override
    public void useEffect() {
        enemyToStun.stunDuration = stunDuration;
    }

    public static void setEnemyToStun(EnemyBase enemy) {
        enemyToStun = enemy;
    }

    public static EnemyBase getEnemyToStun() {
        return enemyToStun;
    }
}
