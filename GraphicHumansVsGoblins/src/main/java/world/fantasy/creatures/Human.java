package world.fantasy.creatures;

import java.util.List;

import static world.fantasy.world.World.*;

public class Human extends Creature {
    // base stats are randomly generated from 2 random d6
    // player can increase these stats using 10 initial points, CPU randomly decides which stat to allocate each point to

    public Human() {
        // AI
        super();
        setHealth((d6()+ d6()) * 2);
        setMana(d6() + d6());
        setIntelligence(d6() + d6());
        setStrength(d6() + d6());
        setConstitution(d6() + d6());
        setDexterity(d6() + d6());
        for (int i = 0; i < getInitialSkillPoints(); i++) applyStatPoint();
    }

    @Override
    protected int getInitialSkillPoints() {
        return 1;
    }

    @Override
    public List<UnitOption> menuOptions() {
        var list = super.menuOptions();
        list.addAll(List.of(UnitOption.INVENTORY, UnitOption.GEAR, /*UnitOption.SPELLS,*/ UnitOption.QUIT));
        return list;
    }

    public Human(Boolean NPC) {
        // Player
        super();
        setHealth((d6()+ d6()) * 2);
        setMana(d6() + d6());
        setIntelligence(d6() + d6());
        setStrength(d6() + d6());
        setConstitution(d6() + d6());
        setDexterity(d6() + d6());
        setIsNPC(NPC);
        if (NPC) for (int i = 0; i < getInitialSkillPoints(); i++) applyStatPoint();
        else {
            remainingSkillPoints = getInitialSkillPoints();
            for (int i = 0; i < getInitialSkillPoints(); i++) chooseStatPoint();
        }
    }

    public Human(int health, int mana, int intelligence, int strength, int constitution, int dexterity) {
        super();
        setHealth(health);
        setMana(mana);
        setIntelligence(intelligence);
        setStrength(strength);
        setConstitution(constitution);
        setDexterity(dexterity);
    }

    public Human(int health, int mana, int intelligence, int strength, int constitution, int dexterity, boolean npc) {
        super();
        setHealth(health);
        setMana(mana);
        setIntelligence(intelligence);
        setStrength(strength);
        setConstitution(constitution);
        setDexterity(dexterity);
        setIsNPC(npc);
    }

    public void chooseStatPoint() {
        System.out.printf("You have %s Skill Points remaining, Currents Stats:\n", remainingSkillPoints);
        System.out.printf("Health: %s    Intelligence: %s    Constitution: %s\n", getHealth(), getIntelligence(), getConstitution());
        System.out.printf("Mana: %s    Strength: %s    Dexterity: %s\n", getMana(), getStrength(), getDexterity());
        System.out.println("Please choose a stat");
        String selection = ensureInput("1: Health\n2: Mana\n3: Intelligence\n4: Strength\n5: Constitution\n6: Dexterity\n","That was not valid input");
        if (validateStat(selection)) {
            increaseStatByID(statToID(selection));
            remainingSkillPoints -= 1;
        }
        else {
            System.out.println("That was not a valid stat!!");
            chooseStatPoint();
        }
    }

    @Override
    public String toString() {
        return Character.toString('\u2625');
    }
}
