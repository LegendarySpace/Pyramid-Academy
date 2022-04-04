package world.fantasy.creatures;

import world.fantasy.world.World;

import java.util.List;

import static world.fantasy.world.World.*;

public class Human extends Creature {
    // base stats are randomly generated from 2 random d6
    // player can increase these stats using 10 initial points, CPU randomly decides which stat to allocate each point to

    public Human(World world) {
        this(world, true);
    }

    public Human(World world, Boolean NPC) {
        // Player
        this(world, (d6()+ d6()) * 2, d6() + d6(), d6() + d6(),
                d6() + d6(), d6() + d6(), d6() + d6(), NPC);
        if (NPC) for (int i = 0; i < getInitialSkillPoints(); i++) applyStatPoint();
        else remainingSkillPoints = getInitialSkillPoints();
    }

    public Human(World world, int health, int mana, int intelligence, int strength, int constitution, int dexterity) {
        this(world, health, mana, intelligence, strength, constitution, dexterity, false);
    }

    public Human(World world, int health, int mana, int intelligence, int strength, int constitution, int dexterity, boolean npc) {
        super(world);
        setHealth(health);
        setMana(mana);
        setIntelligence(intelligence);
        setStrength(strength);
        setConstitution(constitution);
        setDexterity(dexterity);
        setIsNPC(npc);
    }

    // TODO: Deprecate this
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
    protected int getInitialSkillPoints() {
        return 10;
    }

    @Override
    public List<UnitOption> menuOptions() {
        var list = super.menuOptions();
        list.addAll(List.of(UnitOption.INVENTORY, UnitOption.GEAR, /*UnitOption.SPELLS,*/ UnitOption.QUIT));
        return list;
    }

}
