package Cave.Dragon;

public class CavePath extends Cave{
    Cave connection;
    public CavePath(int dist) {
        if (dist++ < 3) {
            switch (d10()) {
                case 1, 2, 3, 4 -> connection = new CaveFork(dist);     // 40% chance to spawn fork
                default -> connection = new CaveDen(dist);
            }
        } else {
            connection = new CaveDen(dist);
        }
    }

    @Override
    public void explore() {
        System.out.println();
        System.out.println("Ahead is a long dark corridor. A sense of unease washes over you. If you leave now you can still make it home.");
        if (!continueJourney()) return;

        System.out.println("You continue down a long dark corridor. You can barely see and must move slowly.");
        System.out.println("As you make your way down the twisting path there's an occasional rumble");
        if (d20() == 1) encounter();        // 5% chance auto game over
        else connection.explore();
    }

    private void encounter() {
        System.out.println("You encounter a wandering dragon and it devours you before you can react. GAME OVER!!!!");
    }

    protected boolean continueJourney() {
        String message = "Do you want to continue your journey? (y or n)";
        String error = "Error getting user data!!";
        String decision = ensureInput(message, error);
        if (yesOrNo(decision)) return decision.equalsIgnoreCase("y") || decision.equalsIgnoreCase("yes");
        return continueJourney();
    }

    protected boolean yesOrNo(String answer) {
        if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes") ||
                answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) return true;
        System.out.println("You did not choose Yes or No");
        return false;
    }
}
