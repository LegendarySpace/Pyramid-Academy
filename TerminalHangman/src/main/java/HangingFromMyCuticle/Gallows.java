package HangingFromMyCuticle;

public class Gallows {
    public int chancesRemaining;

    public Gallows() {
        chancesRemaining = 6;
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
    }

    public void displayHead() {
        boolean drawHead = chancesRemaining < 6;
        System.out.printf("       %s        |   %n", drawHead? "_":" ");
        System.out.printf("      %s %s       |   %n", drawHead? "/":" ", drawHead? "\\":" ");
        System.out.printf("      %s%s%s       |   %n", drawHead? "\\":" ", drawHead? "_":" ", drawHead? "/":" ");
    }

    public void displayBody() {
        boolean drawBody = chancesRemaining < 5;
        System.out.printf("    %s%s%s     |   %n", drawLeftArm()?"___":"   ", drawBody?"|":" ",drawRightArm()?"___":"   ");
        System.out.printf("       %s        |   %n", drawBody? "|":" ");
        System.out.printf("       %s        |   %n", drawBody? "|":" ");
    }

    public boolean drawLeftArm() {
        return chancesRemaining < 4;
    }

    public boolean drawRightArm() {
        return chancesRemaining < 3;
    }

    public void displayLegs() {
        System.out.printf("      %s %s       |   %n", drawLeftLeg()? "/":" ", drawRightLeg()? "\\":" ");
        System.out.printf("     %s   %s      |   %n", drawLeftLeg()? "/":" ", drawRightLeg()? "\\":" ");
    }

    public boolean drawLeftLeg() {
        return chancesRemaining < 2;
    }

    public boolean drawRightLeg() {
        return chancesRemaining < 1;
    }
}
