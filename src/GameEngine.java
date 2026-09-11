import java.io.IOException;
import java.util.Scanner;

public class GameEngine extends Thread{
    private final char[][] mapMe;
    private final char[][] mapEnemy;
    private final Scanner scan;
    private final Network network;
    private char indexB;

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String MAGENTA = "\u001B[35m";

    public GameEngine() {
        this.mapMe = new char[10][10];
        this.mapEnemy = new char[10][10];
        this.network = new Network();
        scan = new Scanner(System.in);
        for (int outer = 0; outer != 10; outer++) {
            for (int inner = 0; inner != 10; inner++)
                mapMe[outer][inner] = 32;
            for (int inner = 0; inner != 10; inner++)
                mapEnemy[outer][inner] = 32;
        }
        indexB = 1;
        try {
            network.buildConnection();
        } catch (IOException e) {
            System.out.println("Verbindung konnte nicht aufgebaut werden :(");
            throw new RuntimeException(e);
        }
    }

    private boolean validateInput(String input) {
        if (input.length() >= 2 && input.charAt(0) >= 97 || input.charAt(0) <= 106 && input.charAt(1) >= 48 || input.charAt(1) <= 57)
            return true;
        System.out.println("Bitte gib ein valides Feld ein!");
        return false;
    }

    public boolean gameOver(int qual) {
        if (qual == 1) {
            for (char[] outer : mapMe)
                for (char inner : outer)
                    if (inner != ' ' && inner != '≈' && inner != '⊗') {
                        network.sendSignal("C");
                        return false;
                    }
            network.sendSignal("L");
            System.out.println("Du hast verloren...");
            try { sleep(1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
            return true;
        } else {
            if (network.receiveSignal().equals("L")) {
                System.out.println("Du hast gewonnen!");
                try { sleep(1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
                return true;
            }
        }
        return false;
    }

    public void gameBegin() {
        System.out.println("Lasst die Schlacht beginnen! \nHier einmal das Schlachtfeld!\n");
        printMap();
        System.out.println("\nBitte Platziere nun deine Schiffe. Zur Auswahl stehen:\n 1.) ■ | ■ | ■ | ■ | ■\n 2.) ■ | ■ | ■ | ■\n 3.) ■ | ■ | ■\n 4.) ■ | ■ | ■");
        System.out.println("\nDie schiffe kannst du platzieren, indem du die Nummer, sowie den Start- und Endpunkt angibst.");
        placePieces("*****", "****", "***", "***");
        System.out.println("Der Kampf beginnt!\n");

        try { sleep(1000);} catch (InterruptedException e) {throw new RuntimeException(e);}

        if (network.role != 1) {
            String order = Integer.toString(((int) (Math.random() * 10)) % 2);
            network.sendSignal(order);
            if ("0".equals(order)) {
                attacked();
                network.sendSignal("C");
            }
        } else {
            if ("1".equals(network.receiveSignal())) {
                attacked();
                network.sendSignal("C");
            }
        }
        while (true) {
            attack();
            if (gameOver(0)) break;
            attacked();
            if (gameOver(1)) break;
        }
        try { network.close(); } catch (IOException e) {throw new RuntimeException(e);}
    }

    private void placePieces(String boot1, String boot2, String boot3, String boot4) {
        int nextID;
        String nextStart;
        String nextEnd;

        while (!boot1.equals(boot2) || !boot1.equals(boot3) || !boot1.equals(boot4)) {
            nextStart = scan.next();
            nextEnd = scan.next();
            if (validateInput(nextStart) && validateInput(nextEnd)) {
                nextID = Math.abs(((int) nextStart.charAt(0) - (int) nextEnd.charAt(0)) - ((int) nextStart.charAt(1) - (int) nextEnd.charAt(1)));
                if (nextID == 4 && !boot1.equals("-")) {
                    if (placeDots(nextStart.charAt(0) - 97, nextStart.charAt(1) - 48, nextEnd.charAt(0) - 97, nextEnd.charAt(1) - 48))
                        boot1 = "-";
                } else if (nextID == 3 && !boot2.equals("-")) {
                    if (placeDots(nextStart.charAt(0) - 97, nextStart.charAt(1) - 48, nextEnd.charAt(0) - 97, nextEnd.charAt(1) - 48))
                        boot2 = "-";
                } else if (nextID == 2 && !boot3.equals("-")) {
                    if (placeDots(nextStart.charAt(0) - 97, nextStart.charAt(1) - 48, nextEnd.charAt(0) - 97, nextEnd.charAt(1) - 48))
                        boot3 = "-";
                } else if (nextID == 2 && !boot4.equals("-")) {
                    if (placeDots(nextStart.charAt(0) - 97, nextStart.charAt(1) - 48, nextEnd.charAt(0) - 97, nextEnd.charAt(1) - 48))
                        boot4 = "-";
                } else {
                    System.out.println("Dieses Boot ist nicht mehr verfügbar. Bitte geben Sie es erneut ein");
                    placePieces(boot1, boot2, boot3, boot4);
                    break;
                }
            } else {
                System.out.println("Keine gültige Eingabe, bitte geben Sie es erneut ein");
                placePieces(boot1, boot2, boot3, boot4);
                break;
            }
            System.out.println("Noch übrig:\n 1.) " + boot1 + "\n 2.) " + boot2 + "\n 3.) " + boot3 + "\n 4.) " + boot4 + "\n");
        }
    }

    private boolean placeDots(int start0, int start1, int end0, int end1) {
        int runV1 = start1;
        int runV2 = end1;
        int runH1 = start0;
        int runH2 = end0;

        if (start0 > end0) {
            runH1 = end0;
            runH2 = start0;
        } else if (start1 > end1) {
            runV1 = end1;
            runV2 = start1;}
        int cpyV1 = runV1;
        int cpyH1 = runH1;

        for (; runH1 <= runH2; runH1++) {
            for (; runV1 <= runV2; runV1++)
                if (mapMe[runH1][runV1] != ' ') {
                    System.out.println("Die Schiffe dürfen nicht gestapelt werden!");
                    return false;}
            runV1 = cpyV1; }
        runH1 = cpyH1;
        for (; runH1 <= runH2; runH1++) {
            for (; runV1 <= runV2; runV1++)
                mapMe[runH1][runV1] = indexB;
            runV1 = cpyV1;}
        indexB++;
        System.out.println("Das Schiff wurde aufgestellt!");
        printMap();
        return true;
    }

    private boolean checkDestroyed(int x, int y) {
        for (int outer = 0; outer < 10; outer++)
            for (int inner = 0; inner < 10; inner++)
                if (mapMe[outer][inner] == mapMe[x][y] && outer != x && inner != y)
                    return false;
        return true;
    }
    
    public void attack() {
        System.out.println("Jetzt darfst du angreifen! Welches Feld?");
        String input = scan.next();
        if (!validateInput(input))
            attack();
        int x = input.charAt(0)-97;
        int y = input.charAt(1)-48;

        if (mapEnemy[x][y] == '⊗' || mapEnemy[x][y] == '≈') {
            System.out.println("Bitte greife kein bereits angegriffenes Feld an!\n");
            attack();
        } else {
            network.sendSignal(input);
            input = network.receiveSignal();
            try {
                if (input.equals("O")) {
                    mapEnemy[x][y] = '≈';
                    System.out.println("Leider daneben. Nächstes mal!\n");
                    sleep(1000);
                } else if (input.equals("X")) {
                    mapEnemy[x][y] = '⊗';
                    System.out.println("Getroffen!\n");
                    sleep(1000);
                } else {
                    mapEnemy[x][y] = '⊗';
                    System.out.println("Schiff zerstört!\n");
                    sleep(1000);
                }
            } catch (InterruptedException e) {throw new RuntimeException(e);}
        }
        printMap();
    }

    public void attacked() {
        System.out.println("Dein gegner greift an...");
        String input = network.receiveSignal();
        if (validateInput(input)) {
            int x = input.charAt(0) - 97;
            int y = input.charAt(1) - 48;

            try {
                if (mapMe[x][y] == ' ') {
                    mapMe[x][y] = '≈';
                    System.out.println("Verfehlt\n");
                    sleep(1000);
                    network.sendSignal("O");
                } else {
                    if (!checkDestroyed(x, y)) {
                        System.out.println("Dein Schiff wurde zerstört!\n");
                        sleep(1000);
                        network.sendSignal("XX");
                    } else {
                        System.out.println("Du wurdest getroffen!\n");
                        sleep(1000);
                        network.sendSignal("X");
                    }
                    mapMe[x][y] = '⊗';
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            printMap();
        } else attacked();
    }

    private void printBoat(char field) {
        switch (field) {
            case '⊗': System.out.print(RED + field + RESET);    break;
            case '≈': System.out.print(BLUE + field + RESET);   break;
            case 1: System.out.print(YELLOW + '■' + RESET);     break;
            case 2: System.out.print(CYAN + '■' + RESET);       break;
            case 3: System.out.print(MAGENTA + '■' + RESET);    break;
            case 4: System.out.print(GREEN + '■' + RESET);      break;
            default: System.out.print(' ');
        }
    }
    
    private void printMap() {
        System.out.println("     Dein Schlachtfeld:                                              Gegnerisches Schlachtfeld:");
        System.out.println("     0   1   2   3   4   5   6   7   8   9                           0   1   2   3   4   5   6   7   8   9  ");
        System.out.println("   -----------------------------------------                       ----------------------------------------- ");
        for (int outer = 0; outer != mapMe.length; outer++) {
            System.out.print(((char) (outer + 97)) + " |");
            for (char inner : mapMe[outer]) {
                System.out.print("| ");
                printBoat(inner);
                System.out.print(" ");}
            System.out.print("|                    " + ((char) (outer + 97)) + " |");
            for (char inner : mapEnemy[outer]) {
                System.out.print("| ");
                printBoat(inner);
                System.out.print(" ");}
            System.out.println("|\n   -----------------------------------------                       ----------------------------------------- ");
        }
    }
}
