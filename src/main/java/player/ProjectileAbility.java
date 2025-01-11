package player;

import math.Vector3f;

public class ProjectileAbility extends AbilityBase{
    public ProjectileAbility(int cost, int cooldown, Player player) {
        super(cost, cooldown, player);
    }

    @Override
    public void useEffect() {
        player.projectiles.add(new Projectile(player.getPosition(), player.getAngle(), player.getProjectileDirection()));
    }
}
