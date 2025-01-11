package main;

import entities.DeadEnemy;
import entities.EnemyBase;
import math.Vector3f;
import player.*;

import java.util.ArrayList;
import java.util.Iterator;

public class CollisionManager {
    public void checkProjectilesCollision(ArrayList<Projectile> projectiles, ArrayList<EnemyBase> enemies) {
        Iterator<Projectile> projectileIterator = projectiles.iterator();
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();
            Vector3f projectileMin = projectile.getMinBounds();
            Vector3f projectileMax = projectile.getMaxBounds();

            for (EnemyBase enemy : enemies) {
                Vector3f enemyMin = enemy.getMinBounds();
                Vector3f enemyMax = enemy.getMaxBounds();

                if (projectileMin.x > enemyMin.x && projectileMin.x < enemyMax.x &&
                        projectileMax.y > enemyMin.y && projectileMin.y < enemyMax.y) {
                    enemy.hp -= (int) projectile.damage;
                    projectileIterator.remove();
                    break;
                }
            }
        }
    }

    public void checkPlayerEnemyCollision(Player player, ArrayList<EnemyBase> enemies) {
        Vector3f playerMin = player.getMinBounds();
        Vector3f playerMax = player.getMaxBounds();

        for (EnemyBase enemy : enemies) {
            Vector3f enemyMin = enemy.getMinBounds();
            Vector3f enemyMax = enemy.getMaxBounds();

            if (playerMin.x > enemyMin.x && playerMin.x < enemyMax.x &&
                    playerMax.y > enemyMin.y && playerMin.y < enemyMax.y) {
                if (player.hp <= 0) {
                    player.hp = 0;
                } else
                    player.hp -= enemy.attackDamage;
            }
        }
    }

    public boolean checkDeadEnemyMouseCollision(Vector3f MousePosition, ArrayList<DeadEnemy> deadEnemies) {
        float mouseMinX = MousePosition.x - 5;
        float mouseMaxX = MousePosition.x + 5;
        float mouseMinY = MousePosition.y - 5;
        float mouseMaxY = MousePosition.y + 5;

        for (DeadEnemy enemy : deadEnemies) {
            Vector3f enemyMin = enemy.getMinBounds();
            Vector3f enemyMax = enemy.getMaxBounds();
            if (mouseMinX > enemyMin.x && mouseMinX < enemyMax.x &&
                    mouseMaxY > enemyMin.y && mouseMinY < enemyMax.y) {
                ResurrectAbility.setEnemyToResurrect(enemy);
                CorpseExplosionAbility.setEnemyToExplode(enemy);
                return true;
            }
        }
        return false;
    }

    public void checkAOEHit(Explosion explosion, ArrayList<EnemyBase> enemies){
        for (EnemyBase enemy : enemies) {
            float dx = enemy.position.x - explosion.position.x;
            float dy = enemy.position.y - explosion.position.y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance <= explosion.radius) {
                enemy.hp -= explosion.damage;
            }
        }
    }
}
