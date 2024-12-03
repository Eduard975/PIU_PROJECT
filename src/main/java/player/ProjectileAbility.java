package player;

import math.Vector3f;

public class ProjectileAbility extends AbilityBase{
    public ProjectileAbility(int cost, int cooldown, Player player) {
        super(cost, cooldown, player);
    }

    @Override
    public void useEffect() {
        Vector3f position = player.getPosition();
        float angle = player.getAngle();
        player.projectiles.add(new Projectile(position, angle));
    }
}
