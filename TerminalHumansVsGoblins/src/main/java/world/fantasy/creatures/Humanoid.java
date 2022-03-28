package world.fantasy.creatures;

import world.fantasy.items.consumable.Consumable;
import world.fantasy.items.equipment.*;
import world.fantasy.items.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static world.fantasy.world.World.*;

public abstract class Humanoid {
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

    public Humanoid() {
        inventory = new ArrayList<>();
        mainHand = null;
        offHand = null;
        armor = null;
        bIsNPC = true;
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
    protected int getInitialSkillPoints() { return 5; }
    public boolean isNPC() { return bIsNPC; }
    public void setIsNPC(boolean bIsNPC) { this.bIsNPC = bIsNPC; }
    private int statModifier(int stat) { return (stat / 2) - 5; }

    // Inventory management
    public ArrayList<Item> getInventory() { return inventory; }
    public String displayInventory() {
        List<String> list = inventory.stream().map(Item::toDetailedString).toList();
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
            sb.append(switch (slot) {
                case ARMOR -> (armor == null)? "none":armor.toDetailedString();
                case MAINHAND -> (mainHand == null)? "none":mainHand.toDetailedString();
                case OFFHAND -> (offHand == null)? "none":offHand.toDetailedString();
            });
        }
        return sb.toString();
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
        System.out.println(String.format("%s was unequipped from %s slot%n", e.toDetailedString(), GearSlot.MAINHAND));
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
        System.out.println(String.format("%s was unequipped from %s slot%n", e.toDetailedString(), GearSlot.OFFHAND));
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
        System.out.println(String.format("%s was unequipped from %s slot%n", a.toDetailedString(), GearSlot.ARMOR));
        return a;
    }
    public boolean wearEquipment(GearSlot slot, Equipment item) {
        if (item == null) return false;
        System.out.println(String.format("%s was equipped to %s slot\n", item.toDetailedString(), slot.name()));
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
    public boolean autoEquip(Equipment equipment) {
        for (var slot : getAllSlots()) if (wearEquipment(slot, equipment)) return true;
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
            case ARMOR -> { unequipArmor(); }
            case MAINHAND -> { unequipMainHand(); }
            case OFFHAND -> { unequipOffHand(); }
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
    public Item encounter(Object obj) {
        if (obj == null) return null;
        Item loot = null;
        if (obj instanceof Humanoid h) {
            System.out.printf("%s attacked %s for %d damage, %s has %d health remaining %n",
                    this, h, attack(h), h, h.getHealth());
            if (!h.hasDied()) return h.encounter(this);
            System.out.printf("%s has died%n", h);
            loot = h.lootDrop();
        }
        if (obj instanceof Item i) loot = i;

        if (loot != null) {
            String message = String.format("Do you want to pick up %s?", loot.toDetailedString());
            if (!isNPC() && !yesOrNo(message)) return loot;
            pickupItem(loot);
            if (loot instanceof Equipment e) {
                if (!isNPC() && !yesOrNo("Would you like to auto equip this?")) return null;
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
    public int attack(Humanoid humanoid) {
        int primary;
        int secondary;
        if (mainHand == null) { primary = (int) ((getStrength() * 2 / 3) * .9); }
        else if (mainHand instanceof Melee) { primary = (getStrength() * 2 / 3) + mainHand.getDamage(); }
        else if (mainHand instanceof Ranged) { primary = (getDexterity() * 2 / 3) + mainHand.getDamage(); }
        else if (mainHand instanceof Magic) { primary = (getIntelligence() * 2 / 3) + mainHand.getDamage(); }
        else if (mainHand instanceof Shield) { primary = (int) ((getStrength() * 2 / 3) * .6) + mainHand.getDamage(); }
        else { primary = getStrength(); }
        if (offHand == null) { secondary = (int) ((getStrength() / 3) * .9); }
        else if (offHand instanceof Melee) { secondary = (getStrength() / 3) + offHand.getDamage(); }
        else if (offHand instanceof Ranged) { secondary = (getDexterity() / 3) + offHand.getDamage(); }
        else if (offHand instanceof Magic) { secondary = (getIntelligence() / 3) + offHand.getDamage(); }
        else if (offHand instanceof Shield) { secondary = (int) ((getStrength() / 3) * .6) + offHand.getDamage(); }
        else { secondary = getStrength(); }

        // remove primary + secondary from humanoid
        return humanoid.applyDamage((int) ((primary+secondary) * ThreadLocalRandom.current().nextDouble()));
    }
    public int applyDamage(int damage) {
        int reducedDamage = damageReduction(damage);
        setHealth(getHealth()-reducedDamage);
        return reducedDamage;
    }
    private int damageReduction(int damage) {
        int primary;
        int secondary;
        if (mainHand == null) { primary = (strength / 4); }
        else if (mainHand instanceof Shield) { primary = (strength /4) + mainHand.getDamage() + statModifier(dexterity); }
        else primary = strength / 4;
        if (offHand == null) { secondary = (strength / 8); }
        else if (offHand instanceof  Shield) { secondary = (strength / 8) + offHand.getDamage() + (statModifier(dexterity) / 2); }
        else secondary = strength / 8;
        int incomingDamage = (armor == null)? (primary + secondary) : (primary + secondary + armor.getDefence());
        int reduction = (int) (incomingDamage * ThreadLocalRandom.current().nextDouble());
        return Math.max(3, damage - reduction);
    }
    public boolean hasDied() { return getHealth() <= 0; }

    // Leveling mechanics
    protected void applyStatPoint() {
        int selection = ThreadLocalRandom.current().nextInt(1,7);
        if (selection < 1 || selection > 6) applyStatPoint();
        else increaseStatByID(selection);
    }
    protected boolean validateStat(String stat) {
        if (stat == null || stat.isEmpty()) return false;
        if (stat.equals("1") || stat.equalsIgnoreCase("h") ||
                stat.equalsIgnoreCase("health")) return true;
        if (stat.equals("2") || stat.equalsIgnoreCase("m") ||
                stat.equalsIgnoreCase("Mana")) return true;
        if (stat.equals("3") || stat.equalsIgnoreCase("i") ||
                stat.equalsIgnoreCase("Intelligence")) return true;
        if (stat.equals("4") || stat.equalsIgnoreCase("s") ||
                stat.equalsIgnoreCase("Strength")) return true;
        if (stat.equals("5") || stat.equalsIgnoreCase("c") ||
                stat.equalsIgnoreCase("Constitution")) return true;
        return stat.equals("6") || stat.equalsIgnoreCase("d") ||
                stat.equalsIgnoreCase("Dexterity");
    }
    protected int statToID(String stat) {
        if (stat == null || stat.isEmpty()) return 0;
        if (stat.equals("1") || stat.equalsIgnoreCase("h") ||
                stat.equalsIgnoreCase("health")) return 1;
        if (stat.equals("2") || stat.equalsIgnoreCase("m") ||
                stat.equalsIgnoreCase("Mana")) return 2;
        if (stat.equals("3") || stat.equalsIgnoreCase("i") ||
                stat.equalsIgnoreCase("Intelligence")) return 3;
        if (stat.equals("4") || stat.equalsIgnoreCase("s") ||
                stat.equalsIgnoreCase("Strength")) return 4;
        if (stat.equals("5") || stat.equalsIgnoreCase("c") ||
                stat.equalsIgnoreCase("Constitution")) return 5;
        if (stat.equals("6") || stat.equalsIgnoreCase("d") ||
                stat.equalsIgnoreCase("Dexterity")) return 6;
        return 0;
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

    // Menu system
    public List<UnitOption> menuOptions() { return new ArrayList<>(List.of(UnitOption.MOVE)); }
    public UnitOption determineAction() { return UnitOption.MOVE; }

    @Override
    public String toString() {
        return Character.toString('\u2640');
    }
}
