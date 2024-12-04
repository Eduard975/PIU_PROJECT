package entities;

import map.Level;
import math.Vector3f;

import java.util.ArrayList;
import java.util.Random;

public class EnemyManager {
    public ArrayList<EnemyBase> enemies;
    public ArrayList<DeadEnemy> deadEnemies;

    private int spawnCooldown = 5000;

    private long lastSpawnTime = 0;

    public EnemyManager(){
        enemies = new ArrayList<>();
        deadEnemies = new ArrayList<>();
    }

    public void addEnemy(Vector3f pos){
        enemies.add(new Slime(pos));
    }

    public void removeEnemy(EnemyBase e){
        enemies.remove(e);
        deadEnemies.add(new DeadEnemy(e.position));
    }

    public void render(){
        for( EnemyBase enemy : enemies)
            enemy.render();
        for( DeadEnemy deadEnemy : deadEnemies)
            deadEnemy.render();
    }

    public void spawnRandomEnemy(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastSpawnTime >= spawnCooldown){
            Random rand = new Random();
            float enemyX = -Level.xBounds + (rand.nextFloat() * Level.xBounds);
            float enemyY = -Level.yBounds + (rand.nextFloat() * Level.yBounds);
            addEnemy(new Vector3f(enemyX,enemyY,0.2f));
            lastSpawnTime = currentTime;
            System.out.println("Spawning enemies");
        }
    }

    public void update(){

        spawnRandomEnemy();

        ArrayList<EnemyBase> enemiesToRemove = new ArrayList<>();

        for (EnemyBase e : enemies) {
            e.update();
            if (e.hp < 0) {
                enemiesToRemove.add(e);
            }
        }

        for(EnemyBase enemy:enemiesToRemove){
            removeEnemy(enemy);
        }
    }
}
