package main;

import entities.EnemyBase;
import math.Vector3f;
import player.Player;
import player.Projectile;

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

    public void checkPlayerEnemyCollision(Player player, ArrayList<EnemyBase> enemies){
        Vector3f playerMin = player.getMinBounds();
        Vector3f playerMax = player.getMaxBounds();

        for(EnemyBase enemy: enemies){
            Vector3f enemyMin = enemy.getMinBounds();
            Vector3f enemyMax = enemy.getMaxBounds();

            if (playerMin.x > enemyMin.x && playerMin.x < enemyMax.x &&
                    playerMax.y > enemyMin.y && playerMin.y < enemyMax.y) {
                player.hp -=  enemy.attackDamage;
            }
        }
    }
}
