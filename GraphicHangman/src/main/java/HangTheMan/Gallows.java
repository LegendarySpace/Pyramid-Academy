package HangTheMan;

public class Gallows {
    public static final int initialChances = 6;
    public int chancesRemaining;

    public Gallows() {
        chancesRemaining = initialChances;
    }

    public void buildGallows() {
        System.out.println();
        System.out.println("       +--------+   ");
        displayHead();
        displayBody();
        displayLegs();
        System.out.println("                |   ");
        System.out.println("                |   ");
        System.out.println("  ==============+=  ");
        System.out.println();
        //
    }

    private void displayHead() {
        boolean drawHead = chancesRemaining < (initialChances);
        System.out.printf("       %s        |   %n", drawHead? "_":" ");
        System.out.printf("      %s %s       |   %n", drawHead? "/":" ", drawHead? "\\":" ");
        System.out.printf("      %s%s%s       |   %n", drawHead? "\\":" ", drawHead? "_":" ", drawHead? "/":" ");
    }

    private void displayBody() {
        boolean drawBody = chancesRemaining < (initialChances - 1);
        System.out.printf("    %s%s%s     |   %n", drawLeftArm()?"___":"   ", drawBody?"|":" ",drawRightArm()?"___":"   ");
        System.out.printf("       %s        |   %n", drawBody? "|":" ");
        System.out.printf("       %s        |   %n", drawBody? "|":" ");
    }

    private boolean drawLeftArm() {
        return chancesRemaining < (initialChances - 2);
    }

    private boolean drawRightArm() {
        return chancesRemaining < (initialChances - 3);
    }

    private void displayLegs() {
        System.out.printf("      %s %s       |   %n", drawLeftLeg()? "/":" ", drawRightLeg()? "\\":" ");
        System.out.printf("     %s   %s      |   %n", drawLeftLeg()? "/":" ", drawRightLeg()? "\\":" ");
    }

    private boolean drawLeftLeg() {
        return chancesRemaining < (initialChances - 4);
    }

    private boolean drawRightLeg() {
        return chancesRemaining < (initialChances - 5);
    }
}
