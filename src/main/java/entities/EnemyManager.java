package entities;

import java.util.ArrayList;

public class EnemyManager {
    public ArrayList<EnemyBase> enemies;

    public EnemyManager(){
        enemies = new ArrayList<>();
    }

    public void addEnemy(){
        enemies.add(new Slime());
    }

    public void removeEnemy(EnemyBase e){
        enemies.remove(e);
    }

    public void render(){
        for( EnemyBase obj : enemies)
            obj.render();
    }

    public void update(){
        enemies.forEach(e -> {
            e.update();
            if(e.hp < 0){
                removeEnemy(e);
            }
        });
    }
}
