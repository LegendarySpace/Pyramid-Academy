package world.fantasy.creatures;

import world.fantasy.world.World;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static world.fantasy.world.World.*;

public class Human extends Creature {
    // base stats are randomly generated from 2 random d6
    // player can increase these stats using 10 initial points, CPU randomly decides which stat to allocate each point to

    public Human(World world) {
        this(world, true);
    }

    public Human(World world, Boolean NPC) {
        // Player
        this(world, "", "", (d6()+ d6()) * 2, d6() + d6(), d6() + d6(),
                d6() + d6(), d6() + d6(), d6() + d6(), NPC);
        setName(chooseName());
        setImagePath(chooseImage());

        if (NPC) for (int i = 0; i < getInitialSkillPoints(); i++) applyStatPoint();
        else remainingSkillPoints = getInitialSkillPoints();
    }

    public Human(World world, String name, String image, int health, int mana, int intelligence, int strength, int constitution, int dexterity) {
        this(world, name, image, health, mana, intelligence, strength, constitution, dexterity, false);
    }

    public Human(World world, String name, String image, int health, int mana, int intelligence, int strength, int constitution, int dexterity, boolean npc) {
        super(world);
        setName(name);
        setImagePath(image);
        setHealth(health);
        setMana(mana);
        setIntelligence(intelligence);
        setStrength(strength);
        setConstitution(constitution);
        setDexterity(dexterity);
        setIsNPC(npc);
    }



    @Override
    protected int getInitialSkillPoints() {
        return 5;
    }

    @Override
    public List<UnitOption> menuOptions() {
        var list = super.menuOptions();
        list.addAll(List.of(UnitOption.INVENTORY, UnitOption.GEAR, /*UnitOption.SPELLS,*/ UnitOption.QUIT));
        return list;
    }

    @Override
    protected String chooseName() {
        try {
            var names = Files.readAllLines(Paths.get("src/main/resources/world/fantasy/humannames.txt"));
            Collections.shuffle(names);
            return names.get(0);
        } catch (Exception e) {
            return "Human";
        }
    }

    @Override
    protected String chooseImage() {
        return switch (ThreadLocalRandom.current().nextInt(1, 5)) {
            case 1 -> "/human1.png";
            case 2 -> "/human2.png";
            case 3 -> "/human3.png";
            case 4 -> "/human4.png";
            default -> "/human.png";
        };
    }
}
