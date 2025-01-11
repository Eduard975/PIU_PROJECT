package player;

import entities.Ally;
import entities.DeadEnemy;

public class CorpseExplosionAbility extends AbilityBase{
    public CorpseExplosionAbility(int cost, int cooldown, Player player) {
        super(cost, cooldown, player);
    }

    public static DeadEnemy enemyToExplode;
    public static float radius = 200;
    @Override
    public void useEffect() {
        player.explosion = new Explosion(player.getMousePosition(), radius);

    }

    public static void setEnemyToExplode(DeadEnemy deadEnemy){
        enemyToExplode = deadEnemy;
    }

    public static DeadEnemy getEnemyToExplode(){
        return enemyToExplode;
    }

    public static void setRadius(float newRadius){
        radius = newRadius;
    }

    public static float getRadius(){
        return radius;
    }
}
