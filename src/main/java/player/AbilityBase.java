package player;

public abstract class AbilityBase {
    protected int cost;
    protected long cooldown;
    protected long lastUsedTime;
    protected final Player player;

    public AbilityBase(int cost, int cooldown, Player player) {
        this.cost = cost;
        this.cooldown = cooldown;
        this.lastUsedTime = 0;
        this.player = player;
    }

    public boolean canUse(int mp) {
        return mp >= cost && (System.currentTimeMillis() - lastUsedTime) >= cooldown;
    }

    public void use(int mp) {
        if (!canUse(mp)) {
            return;
        }
        lastUsedTime = System.currentTimeMillis();
        useEffect();
    }

    public abstract void useEffect();

    public int getCost() {
        return cost;
    }

    public void updateCost(int cost) {
        this.cost = cost;
    }

    public void setCooldown(long cooldown){
        this.cooldown = cooldown;
    }

    public long getCooldown() {
        return cooldown;
    }
}
