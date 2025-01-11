package player;

import entities.Ally;
import entities.DeadEnemy;

public class ResurrectAbility extends AbilityBase {
    public ResurrectAbility(int cost, int cooldown, Player player) {
        super(cost, cooldown, player);
    }
    public static DeadEnemy enemyToResurrect;
    @Override
    public void useEffect() {
        player.allies.add(new Ally(enemyToResurrect.position));
    }

    public static void setEnemyToResurrect(DeadEnemy deadEnemy){
        enemyToResurrect = deadEnemy;
    }

    public static DeadEnemy getEnemyToResurrect(){
        return enemyToResurrect;
    }
}
