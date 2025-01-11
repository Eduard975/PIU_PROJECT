package player;

import entities.Ally;
import entities.DeadEnemy;
import graphic.Texture;

public class ResurrectAbility extends AbilityBase {
    public ResurrectAbility(int cost, int cooldown, Player player) {
        super(cost, cooldown, player);
        this.icon = new Texture("src/main/resources/images/resurectSkill.png");
    }

    public static DeadEnemy enemyToResurrect;

    @Override
    public void useEffect() {
        System.out.println(enemyToResurrect.hp);
        player.allies.add(new Ally(enemyToResurrect.position, enemyToResurrect.hp, enemyToResurrect.attack, (float)(player.baseDamage * 0.6)));
    }

    public static void setEnemyToResurrect(DeadEnemy deadEnemy) {
        enemyToResurrect = deadEnemy;
    }

    public static DeadEnemy getEnemyToResurrect() {
        return enemyToResurrect;
    }
}
