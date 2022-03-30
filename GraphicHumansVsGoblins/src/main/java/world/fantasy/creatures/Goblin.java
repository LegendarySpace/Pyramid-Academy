package world.fantasy.creatures;

import static world.fantasy.world.World.d6;

public class Goblin extends Creature {
    // Base stats randomly generated from 2 d6
    public Goblin() {
        // AI
        super();
        setHealth((int) ((d6()+ d6()) * 1.5));
        setMana(d6() + d6());
        setIntelligence(d6() + d6());
        setStrength(d6() + d6());
        setConstitution(d6() + d6());
        setDexterity(d6() + d6());
        for (int i = 0; i < getInitialSkillPoints(); i++) applyStatPoint();
    }

    public Goblin(int health, int mana, int intelligence, int strength, int constitution, int dexterity) {
        super();
        setHealth(health);
        setMana(mana);
        setIntelligence(intelligence);
        setStrength(strength);
        setConstitution(constitution);
        setDexterity(dexterity);
        setIsNPC(true);
    }

    @Override
    public String toString() {
        return Character.toString('\u263f');
    }
}
