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
    private final HashMap<GearSlot, Equipment> gear; // Gear currently equipped
    private boolean bIsNPC;     // Is NPC or Player
    private Actor target;
    public Creature(World world) {
        super(world);
        name = "Creature of the Night";
        inventory = new ArrayList<>();
        gear = new HashMap<>();
        initializeGear();
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


    // Define initial gear slots
    private void initializeGear() {
        for (var slot : getAllSlots()) gear.put(slot, null);
    }
    public List<GearSlot> getAllSlots() {
        ArrayList<GearSlot> slots = new ArrayList<>(getArmorSlots());
        slots.addAll(getWeaponSlots());
        return slots;
    }
    public List<GearSlot> getWeaponSlots() { return List.of(GearSlot.MAINHAND, GearSlot.OFFHAND); }
    public List<GearSlot> getArmorSlots() { return List.of(GearSlot.ARMOR); }

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
        return gear.get(slot);
    }
    public boolean equipGear(GearSlot slot, Equipment e){
        if ((e instanceof Armor && getArmorSlots().contains(slot)) ||
            (e instanceof Weapon && getWeaponSlots().contains(slot))) {
            unequipSlot(slot);
            gear.put(slot, e);
            inventory.remove(e);
            return true;
        }
        return false;
    }
    public void unequipGear(Equipment e) {
        gear.keySet().stream().filter(s -> gear.get(s) == e).forEach(s -> {
            inventory.add(gear.get(s));
            gear.put(s, null);
            System.out.printf("%s was unequipped from %s slot%n", e.toString(), s.name());
        });
    }
    public Equipment unequipSlot(GearSlot slot) {
        var e = gear.get(slot);
        if (e == null) return null;
        gear.put(slot, null);
        inventory.add(e);
        System.out.printf("%s was unequipped from %s slot%n", e.toString(), slot.name());
        return e;
    }
    public boolean wearEquipment(GearSlot slot, Equipment item) {
        if (item == null) return false;
        var b = equipGear(slot, item);
        if (b) System.out.printf("%s was equipped to %s slot%n", item.toString(), slot.name());
        return b;
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
        if (item == null || !gear.containsValue(item)) return false;
        unequipGear(item);
        return true;
    }
    public boolean removeEquipment(GearSlot slot) {
        if (slot == null || gear.get(slot) == null) return false;
        unequipSlot(slot);
        return true;
    }
    public void removeAll() {
        for (GearSlot slot : gear.keySet()) removeEquipment(slot);
    }
    public GearSlot getSlotFromItem(Equipment item) {
        if (!gear.containsValue(item)) return null;
        return gear.keySet().stream().filter(s -> gear.get(s) == item).findFirst().orElse(null);
    }

    // Encounter mechanics
    @Override
    public Actor encounter(Actor obj) {
        if (obj == null) return null;
        Item loot = null;
        getUnitPane().updateText();
        obj.getUnitPane().updateText();
        if (obj instanceof Creature opponent) {
            // TODO: this will prompt a display of damage
            System.out.printf("%s attacked %s for %d damage, %s has %d health remaining %n",
                    this, opponent, attack(opponent), opponent, opponent.getHealth());
            if (!opponent.hasDied()) return opponent.encounter(this);
            System.out.printf("%s has died%n", opponent);
            getUnitPane().updateText();
            opponent.getUnitPane().updateText("\u2620");
            loot = opponent.lootDrop();
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
            if (dropTable.isEmpty()) return null;
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
        if (!getWeaponSlots().contains(slot)) return 0;
        int dmg = calculateWeaponDamage((Weapon) getGearFromSlot(slot));
        if (slot == GearSlot.OFFHAND) dmg *= .65;
        return dmg;
    }
    protected int calculateWeaponDamage(Weapon weapon) {
        if (weapon == null) { return (int) ((getStrength() * 2 / 3) * .9); }
        if (weapon instanceof Melee) { return (getStrength() * 2 / 3) + weapon.getDamage(); }
        if (weapon instanceof Ranged) { return (getDexterity() * 2 / 3) + weapon.getDamage(); }
        if (weapon instanceof Magic) { return (getIntelligence() * 2 / 3) + weapon.getDamage(); }
        if (weapon instanceof Shield) { return (int) ((getStrength() * 2 / 3) * .6) + weapon.getDamage(); }
        return getStrength();
    }
    public int applyDamage(int damage) {
        return applyDamage(damage, false);
    }
    public int applyDamage(int damage, boolean trueDamage) {
        int finalDamage = trueDamage?damage:damageReduction(damage);
        setHealth(getHealth()-finalDamage);
        getUnitPane().updateText(Integer.toString(finalDamage));
        if (hasDied()) setPosition(null);
        return finalDamage;
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
        if (!getWeaponSlots().contains(slot)) return 0;
        int block = calculateWeaponDefence((Weapon) getGearFromSlot(slot));
        if (slot == GearSlot.OFFHAND) block *= .65;
        return block;
    }
    protected int calculateWeaponDefence(Weapon weapon) {
        if (weapon == null) return (strength / 5);
        if (weapon instanceof Shield) return (strength /4) + weapon.getDamage() + statModifier(dexterity);
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
