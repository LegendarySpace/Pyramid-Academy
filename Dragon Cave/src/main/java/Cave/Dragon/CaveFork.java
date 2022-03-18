package Cave.Dragon;

public class CaveFork extends Cave{
    Cave left;
    Cave right;
    public CaveFork(int dist) {
        if (dist++ < 3) {
            left = switch (d10()) {
                case 1 -> new CaveFork(dist);
                case 2,3,4,5 -> new CavePath(dist);
                default -> new CaveDen(dist);
            };
            right = switch (d10()) {
                case 1 -> new CaveFork(dist);
                case 2,3,4,5 -> new CavePath(dist);
                default -> new CaveDen(dist);
            };
        } else {
            left = new CaveDen(dist);
            right = new CaveDen(dist);
        }
    }

    @Override
    public void explore() {
        System.out.println();
        System.out.println("As you make your way through the cave it suddenly widens and you encounter a forking path");

        if (chooseLeft()) left.explore();
        else right.explore();
    }

    public boolean chooseLeft() {
        String message = "Which direction do you go? (Left, Right)";
        String error = "Error getting user choice!!";
        String direction = ensureInput(message, error);
        if (!leftOrRight(direction)) return chooseLeft();
        if (direction.equalsIgnoreCase("left")) return true;
        return false;
    }

    public boolean leftOrRight(String direction) {
        if (direction.equalsIgnoreCase("left") || direction.equalsIgnoreCase("right")) return true;
        System.out.println("You did not choose Left or Right");
        return false;
    }
}
