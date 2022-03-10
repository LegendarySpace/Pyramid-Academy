package Cave.Dragon;

public class CaveDen extends Cave{
    // NOTE: 1 gold = $100
    private int type;
    public CaveDen() {
        type = switch(d20()) {
            case 2,3,4,5,6,7,8,9,10 -> 1;
            case 11,12,13,14,15,16,17,18,19 -> 2;
            default -> 3;
        };
    }

    @Override
    public void explore() {
        System.out.println("As you wander through the cave you approach a large opening.");
        System.out.println("It appears to be a dragon's den.");
        switch (type) {
            case 1: kind();
            case 2: vicious();
            case 3: empty();
        }
    }

    public void empty() {
        System.out.println("You search around cautiously but nobody appears to be home, in fact it looks abandoned.");
        System.out.println("You notice a small pile of gold glinting in the corner from the faint light seeping through cracks in the ceiling.");
        System.out.println("You found about 100 pieces of gold and decide not to push your luck.");
        System.out.println("You return to town considering today a success, revelling in your new found wealth.");
    }

    public void kind() {
        System.out.println("You stealthily approach. As you inch closer you feel a small rumble periodically, it must be the dragon.");
        System.out.println("You prepare yourself for battle, readying your weapons and potions.");
        System.out.println("You wait silently as the dragon approaches. As soon as it's near you jump out ready to attack. As you do the dragon roars...");
        System.out.println("\"Hello my friend. Don't be alarmed, I won't hurt you.\" He appears to be a kind dragon. You put away your weapons");
        System.out.println("In a low, rumbling voice he continues, \"You know you're not that good at hiding. I could smell you as soon as you entered my path.\"");
        System.out.println("You sit with the dragon recounting tales of your adventures. The dragon hangs on every word excited to hear a human's perspective");
        System.out.println("Eventually you decide it's time to head home. Having made a new friend the dragon decides to share a portion of it's treasure with you.");
        System.out.println("As you prepare to leave the dragon pushes aside a pile containing more gold than you've ever seen and tells you to enjoy it.");
        System.out.println("You return to town with 10,000 gold, satisfied with a new friend and the wealth of a noble.");
    }

    public void vicious() {
        System.out.println("You stealthily approach. It's eerily quiet. You ready your weapons, preparing for an encounter. You make your way deeper.");
        System.out.println("You can see a massive mound of gold, jewels and treasures galore, but no sign of the dragon");
        System.out.println("You feel a breeze from the tunnel behind you then CHOMP!!!!! Your legs fall to the ground, disconnected from your body.");
        System.out.println("The dragon had snuck up behind you and devoured you before you knew it was there. A tragic end");
    }
}
