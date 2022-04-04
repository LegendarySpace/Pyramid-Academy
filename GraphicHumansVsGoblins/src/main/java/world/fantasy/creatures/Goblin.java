package world.fantasy.creatures;

import world.fantasy.world.World;

import static world.fantasy.world.World.d6;

public class Goblin extends Creature {
    // Base stats randomly generated from 2 d6
    public Goblin(World world) {
        this(world, (int) ((d6()+ d6()) * 1.5), d6() + d6(), d6() + d6(),
                d6() + d6(), d6() + d6(), d6() + d6());
        for (int i = 0; i < getInitialSkillPoints(); i++) applyStatPoint();
    }

    public Goblin(World world, int health, int mana, int intelligence, int strength, int constitution, int dexterity) {
        super(world);
        setHealth(health);
        setMana(mana);
        setIntelligence(intelligence);
        setStrength(strength);
        setConstitution(constitution);
        setDexterity(dexterity);
        setIsNPC(true);
    }

}
