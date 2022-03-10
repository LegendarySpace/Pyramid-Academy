package Cave.Dragon;

public class CavePath extends Cave{
    Cave connection;
    public CavePath() {
        if (dist < 3) {
            switch (d10()) {
                case 1, 2, 3, 4 -> connection = new CaveFork();     // 40% chance to spawn fork
                default -> connection = new CaveDen();
            }
        } else {
            connection = new CaveDen();
        }
    }

    @Override
    public void explore() {
        dist++;
        System.out.println("You continue down a long dark corridor. You can barely see and must move slowly.");
        System.out.println("As you make your way down the twisting path there's an occasional rumble");
        if (d20() == 1) encounter();        // 5% chance auto game over
        else connection.explore();
    }

    public void encounter() {
        System.out.println("You encounter a wandering dragon and it devours you before you can react. GAME OVER!!!!");
    }
}
