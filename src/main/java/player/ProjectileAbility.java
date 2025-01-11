package player;

import graphic.Texture;

public class ProjectileAbility extends AbilityBase {
    public ProjectileAbility(int cost, int cooldown, Player player) {
        super(cost, cooldown, player);
        this.icon = new Texture("src/main/resources/images/mainSkill.png");
    }

    @Override
    public void useEffect() {
        player.projectiles.add(new Projectile(player.getPosition(), player.getAngle(), player.getProjectileDirection(), player.getBaseDamage()));
    }
}
