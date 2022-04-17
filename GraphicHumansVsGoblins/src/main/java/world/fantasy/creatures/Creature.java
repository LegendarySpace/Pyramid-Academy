package world.fantasy.creatures;

import world.fantasy.Actor;
import world.fantasy.items.consumable.Consumable;
import world.fantasy.items.equipment.*;
import world.fantasy.items.*;
import world.fantasy.world.Direction;
import world.fantasy.world.World;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static world.fantasy.world.World.*;

public abstract class Creature extends Actor {
    private int Health;         // Total health
    private int Mana;           // Used for casting magic
    private int intelligence;   // Affects magic and magic based weapons
    private int strength;       // Affects physical based weapons
    private int constitution;   // Affects hit points and damage resistance
    private int dexterity;      // Affects range based weapons
    protected int remainingSkillPoints;
    private final ArrayList<Item> inventory;  // Everything a humanoid is carrying
    private Weapon mainHand;      // Item held in humanoid main hand
    private Weapon offHand;       // Item held in humanoid offhand
    private Armor armor;        // Armor currently being worn by humanoid
    private boolean bIsNPC;     // Is NPC or Player
    private Actor target;
    public Creature(World world) {
        super(world);
        name = "Creature of the Night";
        inventory = new ArrayList<>();
        mainHand = null;
        offHand = null;
        armor = null;
        bIsNPC = true;
        target = null;
    }



    // Stat management
    public int getHealth() { return Health; }
    public void setHealth(int health) { Health = health; }
    public int getMana() { return Mana; }
    public void setMana(int mana) { Mana = mana; }
    public int getIntelligence() { return intelligence; }
    public void setIntelligence(int intelligence) { this.intelligence = intelligence; }
    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }
    public int getConstitution() { return constitution; }
    public void setConstitution(int constitution) { this.constitution = constitution; }
    public int getDexterity() { return dexterity; }
    public void setDexterity(int dexterity) { this.dexterity = dexterity; }
    public Actor getTarget() { return target; }
    public void setTarget(Actor actor) { this.target = actor; }
    protected int getInitialSkillPoints() { return 5; }
    public int getRemainingSkillPoints() {
        return remainingSkillPoints;
    }
    public boolean isNPC() { return bIsNPC; }
    public void setIsNPC(boolean bIsNPC) { this.bIsNPC = bIsNPC; }
    private int statModifier(int stat) { return (stat / 2) - 5; }

    // Inventory management
    public ArrayList<Item> getInventory() { return inventory; }
    public String displayInventory() {
        List<String> list = inventory.stream().map(Item::toString).toList();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(String.format("\n%d: %s", i, list.get(i)));
        }
        return sb.toString();
    }
    public Item getItem(int index) { return inventory.get(index); }
    public void clearInventory() {
        for (var i : inventory) i.setOwner(null);
        inventory.clear();
    }
    public void pickupItem(Item item) {
        inventory.add(item);
        item.setOwner(this);
        item.setPosition(null);
    }
    public boolean dropItem(Item item) {
        if (item != null) item.setOwner(null);
        return inventory.remove(item);
    }
    public void useItem(Item item) {
        if (item == null) return;
        if (item instanceof Consumable c) {
            c.use();
            dropItem(item);
        }
    }

    // Equipment management
    public String displayEquipment() {
        StringBuilder sb = new StringBuilder();
        var slots = getAllSlots();
        for (var slot : slots) {
            sb.append("\n").append(slot.name()).append(": ");
            sb.append(getGearFromSlot(slot).toString());
        }
        return sb.toString();
    }
    public Equipment getGearFromSlot(GearSlot slot) {
        return switch (slot) {
            case ARMOR -> armor;
            case MAINHAND -> mainHand;
            case OFFHAND -> offHand;
        };
    }
    public Weapon getMainHand() { return mainHand; }
    public void equipMainHand(Weapon item) {
        if (mainHand != null) unequipMainHand();
        mainHand = item;

        dropItem(item);
    }
    public Weapon unequipMainHand() {
        if (mainHand == null) return null;
        Weapon e = mainHand;
        inventory.add(mainHand);
        mainHand = null;
        System.out.printf("%s was unequipped from %s slot%n", e.toString(), GearSlot.MAINHAND);
        return e;
    }
    public Weapon getOffHand() { return offHand; }
    public void equipOffHand(Weapon item) {
        if (offHand != null) unequipOffHand();
        offHand = item;
        dropItem(item);
    }
    public Weapon unequipOffHand() {
        if (offHand == null) return null;
        Weapon e = offHand;
        inventory.add(offHand);
        offHand = null;
        System.out.printf("%s was unequipped from %s slot%n", e.toString(), GearSlot.OFFHAND);
        return e;
    }
    public Armor getArmor() { return armor; }
    public void equipArmor(Armor item) {
        if (armor != null) unequipArmor();
        armor = item;
        dropItem(item);
    }
    public Armor unequipArmor() {
        if (armor == null) return null;
        Armor a = armor;
        inventory.add(armor);
        armor = null;
        System.out.printf("%s was unequipped from %s slot%n", a.toString(), GearSlot.ARMOR);
        return a;
    }
    public boolean wearEquipment(GearSlot slot, Equipment item) {
        if (item == null) return false;
        System.out.printf("%s was equipped to %s slot%n", item.toString(), slot.name());
        switch (slot) {
            case ARMOR:
                if (item instanceof Armor a) {
                    equipArmor(a);
                    return true;
                }
            case MAINHAND:
                if (item instanceof Weapon e) {
                    equipMainHand(e);
                    return true;
                }
            case OFFHAND:
                if (item instanceof Weapon e) {
                    equipOffHand(e);
                    return true;
                }
        }
        return false;
    }
    public void autoEquip() {
        removeAll();
        for (var slot : getArmorSlots()) {
            wearEquipment(slot, getInventory().stream().filter(item -> item instanceof Armor)
                    .map(i -> (Armor) i).max(Comparator.comparingInt(Equipment::getTotal)).orElse(null));
        }
        for (var slot : getWeaponSlots()) {
            wearEquipment(slot, getInventory().stream().filter(item -> item instanceof Weapon)
                    .map(i -> (Weapon) i).max(Comparator.comparingInt(Equipment::getTotal)).orElse(null));
        }
    }
    public boolean autoEquip(Equipment equipment) {
        for (var slot : getAllSlots()) {
            if (getGearFromSlot(slot) == null || getGearFromSlot(slot).getTotal() < equipment.getTotal()) {
                if (wearEquipment(slot, equipment)) return true;
            }
        }
        return false;
    }
    public boolean removeEquipment(Equipment item) {
        if (item == null) return false;
        switch (getSlotFromItem(item)) {
            case ARMOR -> { if (item instanceof Armor a) if (a == getArmor()) unequipArmor(); }
            case MAINHAND -> { if (item instanceof Weapon e) if (e == getMainHand()) unequipMainHand(); }
            case OFFHAND -> { if (item instanceof Weapon e) if (e == getOffHand()) unequipOffHand(); }
            default -> { return false; }
        }
        return true;
    }
    public boolean removeEquipment(GearSlot slot) {
        if (slot == null) return false;
        switch (slot) {
            case ARMOR -> unequipArmor();
            case MAINHAND -> unequipMainHand();
            case OFFHAND -> unequipOffHand();
            default -> { return false; }
        }
        return true;
    }
    public void removeAll() {
        for (GearSlot slot : GearSlot.values()) removeEquipment(slot);
    }
    public GearSlot getSlotFromItem(Equipment item) {
        if (item == getArmor()) return GearSlot.ARMOR;
        if (item == getMainHand()) return GearSlot.MAINHAND;
        if (item == getOffHand()) return GearSlot.OFFHAND;
        return null;
    }
    public List<GearSlot> getAllSlots() {
        ArrayList<GearSlot> slots = new ArrayList<>(getArmorSlots());
        slots.addAll(getWeaponSlots());
        return slots;
    }
    public List<GearSlot> getWeaponSlots() { return List.of(GearSlot.MAINHAND, GearSlot.OFFHAND); }
    public List<GearSlot> getArmorSlots() { return List.of(GearSlot.ARMOR); }

    // Encounter mechanics
    public Actor encounter(Actor obj) {
        if (obj == null) return null;
        Item loot = null;
        if (obj instanceof Creature h) {
            System.out.printf("%s attacked %s for %d damage, %s has %d health remaining %n",
                    this, h, attack(h), h, h.getHealth());
            if (!h.hasDied()) return h.encounter(this);
            System.out.printf("%s has died%n", h);
            loot = h.lootDrop();
        }
        if (obj instanceof Item i) loot = i;

        if (loot != null) {
            pickupItem(loot);
            if (loot instanceof Equipment e) {
                autoEquip(e);
            }
        }
        return null;
    }
    private Item lootDrop() {
        int dropChance = d6();
        if (dropChance == 1 || dropChance == 2) {
            removeAll();
            var dropTable = getInventory();
            if (dropTable.size() < 1) return null;
            int pos = ThreadLocalRandom.current().nextInt(0, dropTable.size());
            Item dropped = dropTable.get(pos);
            clearInventory();
            return dropped;
        }
        return null;
    }
    public int attack(Creature creature) {
        int output = getWeaponSlots().stream().mapToInt(this::damageAdjustedBySlot).sum();

        return creature.applyDamage((int) (output * ThreadLocalRandom.current().nextDouble()));
    }
    protected int damageAdjustedBySlot(GearSlot slot) {
        if (slot == GearSlot.OFFHAND) return (int) (calculateWeaponDamage(offHand) * .65);
        return calculateWeaponDamage((Weapon) getGearFromSlot(slot));
    }
    protected int calculateWeaponDamage(Weapon weapon) {
        if (mainHand == null) { return (int) ((getStrength() * 2 / 3) * .9); }
        if (mainHand instanceof Melee) { return (getStrength() * 2 / 3) + mainHand.getDamage(); }
        if (mainHand instanceof Ranged) { return (getDexterity() * 2 / 3) + mainHand.getDamage(); }
        if (mainHand instanceof Magic) { return (getIntelligence() * 2 / 3) + mainHand.getDamage(); }
        if (mainHand instanceof Shield) { return (int) ((getStrength() * 2 / 3) * .6) + mainHand.getDamage(); }
        return getStrength();
    }
    public int applyDamage(int damage) {
        int reducedDamage = damageReduction(damage);
        setHealth(getHealth()-reducedDamage);
        // TODO: Call to GUI to display damage
        if (getHealth() <= 0) setPosition(null);
        return reducedDamage;
    }
    private int damageReduction(int damage) {
        int incomingDamage = calculateTotalArmor() + calculateTotalBlock();
        int reduction = (int) (incomingDamage * ThreadLocalRandom.current().nextDouble());
        return Math.max(3, damage - reduction);
    }
    protected int calculateTotalBlock() {
        return getWeaponSlots().stream().mapToInt(this::blockAdjustedBySlot).sum();
    }
    protected int blockAdjustedBySlot(GearSlot slot) {
        if (slot == GearSlot.OFFHAND) return (int) (calculateWeaponDefence(offHand) *.6);
        return calculateWeaponDefence((Weapon) getGearFromSlot(slot));
    }
    protected int calculateWeaponDefence(Weapon weapon) {
        if (mainHand == null) return (strength / 5);
        if (mainHand instanceof Shield) return (strength /4) + mainHand.getDamage() + statModifier(dexterity);
        return strength / 4;
    }
    protected int calculateTotalArmor() {
        return getArmorSlots().stream().map(this::getGearFromSlot).filter(Objects::nonNull)
                .map(e -> (Armor)e).mapToInt(Armor::getDefence).sum();
    }
    public boolean hasDied() { return getHealth() <= 0; }

    // Leveling mechanics
    protected void applyStatPoint() {
        int selection = ThreadLocalRandom.current().nextInt(1,7);
        if (selection < 1 || selection > 6) applyStatPoint();
        else increaseStatByID(selection);
    }
    public void statUp(int id) {
        if (getRemainingSkillPoints() < 1) return;
        increaseStatByID(id);
        remainingSkillPoints -= 1;
    }
    protected void increaseStatByID(int i) {
        if (i < 1 || i > 6) return;
        switch (i) {
            case 1 -> setHealth(getHealth() + 2);
            case 2 -> setMana(getMana() + 1);
            case 3 -> setIntelligence(getIntelligence() + 1);
            case 4 -> setStrength(getStrength() + 1);
            case 5 -> setConstitution(getConstitution() + 1);
            case 6 -> setDexterity(getDexterity() + 1);
        }
    }
    // Movement mechanics
    public List<Direction> validMovements() {
        return Arrays.stream(Direction.values()).filter(dir -> world.landExists(getPosition().getDirection(dir))).toList();
    }

    // Menu system
    public List<UnitOption> menuOptions() { return new ArrayList<>(List.of(UnitOption.MOVE)); }
    public UnitOption determineAction() { return UnitOption.MOVE; }

    // Setup
    protected abstract String chooseName();
    protected abstract String chooseImage();
}
