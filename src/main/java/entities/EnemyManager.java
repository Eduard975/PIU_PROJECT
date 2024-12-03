package entities;

import java.util.ArrayList;

public class EnemyManager {
    public ArrayList<EnemyBase> enemies;
    public ArrayList<DeadEnemy> deadEnemies;

    public EnemyManager(){
        enemies = new ArrayList<>();
        deadEnemies = new ArrayList<>();
    }

    public void addEnemy(){
        enemies.add(new Slime());
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

    public void update(){



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
