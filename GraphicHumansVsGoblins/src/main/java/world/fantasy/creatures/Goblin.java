package world.fantasy.creatures;

import world.fantasy.world.World;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

import static world.fantasy.world.World.d6;

public class Goblin extends Creature {
    // Base stats randomly generated from 2 d6
    public Goblin(World world) {
        this(world, null, null, (int) ((d6()+ d6()) * 1.5), d6() + d6(), d6() + d6(),
                d6() + d6(), d6() + d6(), d6() + d6());
        setName(chooseName());
        setImagePath(chooseImage());
        for (int i = 0; i < getInitialSkillPoints(); i++) applyStatPoint();
    }

    public Goblin(World world, String name, String image, int health, int mana, int intelligence, int strength, int constitution, int dexterity) {
        super(world);
        setName(name);
        setImagePath(image);
        setHealth(health);
        setMana(mana);
        setIntelligence(intelligence);
        setStrength(strength);
        setConstitution(constitution);
        setDexterity(dexterity);
        setIsNPC(true);
    }

    @Override
    public String chooseName() {
        try {
            var names = Files.readAllLines(Paths.get("src/main/resources/world/fantasy/femalegoblinnames.txt"));
            names.addAll(Files.readAllLines(Paths.get("src/main/resources/world/fantasy/malegoblinnames.txt")));
            Collections.shuffle(names);
            return names.get(0);
        } catch (Exception e) {
            return "Goblin";
        }
    }
    public String chooseImage() {
        return "/goblin.png";
    }

}
