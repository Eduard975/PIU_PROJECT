package player;

import entities.EnemyManager;
import main.CollisionManager;
import map.Level;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class AbilityManager {
    private final Player player;
    private final EnemyManager enemyManager;
    private final CollisionManager collisionManager;

    private final ProjectileAbility projectileAbility;
    private final ResurrectAbility resurrectAbility;
    private final CorpseExplosionAbility corpseExplosionAbility;

    long windowId = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();

    public AbilityManager(Player player, EnemyManager enemyManager, CollisionManager collisionManager) {
        this.player = player;
        this.enemyManager = enemyManager;
        this.collisionManager = collisionManager;

        projectileAbility = new ProjectileAbility(5, 2000, this.player);
        resurrectAbility = new ResurrectAbility(10, 2000, this.player);
        corpseExplosionAbility = new CorpseExplosionAbility(10, 2000, this.player);
    }

    public void update() {
        if (glfwGetKey(windowId, GLFW_KEY_S) == GLFW_PRESS) {
            player.position.y -= player.speed;
            if (player.position.y < -Level.yBounds) {
                player.position.y = -Level.yBounds;
            }
        }
        if (glfwGetKey(windowId, GLFW_KEY_W) == GLFW_PRESS) {
            player.position.y += player.speed;
            if (player.position.y > Level.yBounds) {
                player.position.y = Level.yBounds;
            }
        }

        if (glfwGetKey(windowId, GLFW_KEY_A) == GLFW_PRESS) {
            player.position.x -= player.speed;
            if (player.position.x < -Level.xBounds) {
                player.position.x = -Level.xBounds;
            }
        }

        if (glfwGetKey(windowId, GLFW_KEY_D) == GLFW_PRESS) {
            player.position.x += player.speed;
            if (player.position.x > Level.xBounds) {
                player.position.x = Level.xBounds;
            }
        }

        if (glfwGetKey(windowId, GLFW_KEY_E) == GLFW_PRESS) {
            if (projectileAbility.canUse(player.mp)) {
                projectileAbility.use(player.mp);
                player.mp -= projectileAbility.getCost();
            }
        }

        if (glfwGetKey(windowId, GLFW_KEY_F) == GLFW_PRESS) {
            if (resurrectAbility.canUse(player.mp)) {
                if (collisionManager.checkDeadEnemyMouseCollision(player.getMousePosition(), enemyManager.deadEnemies)) {
                    resurrectAbility.use(player.mp);
                    player.mp -= resurrectAbility.getCost();
                    enemyManager.deadEnemies.remove(ResurrectAbility.getEnemyToResurrect());
                }
            }
        }

        if (glfwGetKey(windowId, GLFW_KEY_R) == GLFW_PRESS) {
            if (corpseExplosionAbility.canUse(player.mp)) {
                if (collisionManager.checkDeadEnemyMouseCollision(player.getMousePosition(), enemyManager.deadEnemies)) {
                    corpseExplosionAbility.use(player.mp);
                    player.mp -= corpseExplosionAbility.getCost();
                    enemyManager.deadEnemies.remove(CorpseExplosionAbility.getEnemyToExplode());
                    collisionManager.checkAOEHit(player.explosion, enemyManager.enemies);
                }
            }
        }
    }


    public List<AbilityBase> getAbilities() {
        List<AbilityBase> abilitiesIcons = new ArrayList<>();
        abilitiesIcons.add(projectileAbility);
        abilitiesIcons.add(resurrectAbility);
        abilitiesIcons.add(corpseExplosionAbility);

        return abilitiesIcons;
    }
}
