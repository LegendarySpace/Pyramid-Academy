package world.fantasy.items.consumable;

public class HealthPotion extends Consumable{
    @Override
    public void use() {
        getOwner().setHealth(getOwner().getHealth() + 10);
    }

    @Override
    public String toDetailedString() {
        return "a potion used to increase health by 10";
    }
}
