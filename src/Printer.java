import java.util.Scanner;

public class Printer extends Thread{
    private final GameEngine    engine;
    private static final String RESET       = "\u001B[0m";
    private static final String RED         = "\u001B[31m";
    private static final String GREEN       = "\u001B[32m";
    private static final String YELLOW      = "\u001B[33m";
    private static final String BLUE        = "\u001B[34m";
    private static final String MAGENTA     = "\u001B[35m";
    private static final String CYAN        = "\u001B[36m";

    static final char hit                   = '⊗';
    static final char miss                  = '≈';
    static final char empty                 = ' ';
    static final char tries2                = '2';
    static final char tries1                = '1';
    static final String dismiss             = "-";
    static final String hitSignal           = "X";
    static final String destroyedSignal     = "XX";
    static final String missSignal          = "O";
    static final String cont                = "C";
    static final String end                 = "L";
    static final String pU1                 = "pU1";
    static final String pU2                 = "pU2";
    static final String pU3                 = "pU3";

    String hostOrClientMess     = "Host(H) oder Client(beliebige Eingabe)?";
    String waitClientMess       = "Warte auf Client...";
    String enterIPMess          = "Bitte gib die IP ein...";
    String connectingMess       = "Verbinde...";
    String connectedMess        = "Verbunden!";
    String errorMess            = "Fehler";
    String gameBeginMess1       = "\nLasst die Schlacht beginnen! \nHier einmal das Schlachtfeld!\n";
    String gameBeginMess2       = "Bitte Platziere nun deine Schiffe. Zur Auswahl stehen:\n 1.) ■ | ■ | ■ | ■ | ■\n 2.) ■ | ■ | ■ | ■\n 3.) ■ | ■ | ■\n 4.) ■ | ■ | ■";
    String gameBeginMess3       = "\nDie Schiffe kannst du platzieren, indem du den Start- und Endpunkt angibst.";
    String gameBeginMess4       = "Der Kampf beginnt!";
    String loseMess             = "Du hast verloren...";
    String winMess              = "Du hast gewonnen!";
    String notValidRetryMess    = "Keine gültige Eingabe, bitte geben Sie es erneut ein";
    String notValidMess         = "Keine valide Eingabe";
    String shipPlacedMess       = "Das Schiff wurde aufgestellt!";
    String powerUp1Mess         = "Gib eine Reihe oder Spalte an, die bombardiert werden soll";
    String powerUp2Mess         = "Gib die Position an, wo der die Bombe einschlagen soll";
    String powerUp3Mess         = "Gib die Position an, an der die Rakete suchen soll";
    String attackMess           = "Jetzt darfst du angreifen! Wähle ein Feld oder schreibe \"power\", um ein Powerup zu nutzen.";
    String enemyAttackMess      = "Dein Gegner greift an";
    String hitMess              = "Getroffen";
    String missedMess           = "Verfehlt";
    String shipDestroyedMess    = "Ein Schiff wurde zerstört!";
    String powerUpUsedMess      = "Powerup wurde genutzt";
    String stillOpenMess        = "Noch übrig:";
    String powerUpActiveMess    = "Powerup aktiv, wähle eines und gib den Index ein:";
    String powerUp1ExplMess     = "Bombardiere eine Reihe/Zeile";
    String powerUp2ExplMess     = "Wirf eine Splitterbombe, welche zufällige Felder in einem Umkreis von 2 trifft";
    String powerUp3ExplMess     = "Starte eine Suchrakete, welche Boote in der Nähe findet";
    String backToAttackMess     = "\n(4) zurück zum normalen Angriff";
    String battlefieldTitleMess = "\n     Dein Schlachtfeld:                                              Gegnerisches Schlachtfeld:";
    String placeNext            = "Platziere dein nächstes Schiff";

    public Printer(GameEngine engine) {
        this.engine = engine;
    }

    //Output map
    public void     setLanguage()                           {
        Scanner scan = new Scanner(System.in);
        System.out.println("Which language? (D/E)");
        if ("E".equals(scan.next())) {
            hostOrClientMess    = "Host(H) or client(any input)?";
            waitClientMess      = "Waiting for client...";
            enterIPMess         = "Please enter the IP...";
            connectingMess      = "Connecting...";
            connectedMess       = "Connected!";
            errorMess           = "Error";
            gameBeginMess1      = "\nLet the battle begin! \nLet's have a look at the battlefield!\n";
            gameBeginMess2      = "Its time, to place your ships. You can chose from:\n 1.) ■ | ■ | ■ | ■ | ■\n 2.) ■ | ■ | ■ | ■\n 3.) ■ | ■ | ■\n 4.) ■ | ■ | ■";
            gameBeginMess3      = "\nYou can place the ships by entering the start- and endposition.";
            gameBeginMess4      = "The battle begins!";
            loseMess            = "You lost...";
            winMess             = "You won!";
            notValidRetryMess   = "No valid input, please try again";
            notValidMess        = "No valid input";
            shipPlacedMess      = "The ship has been placed!";
            powerUp1Mess        = "Enter a row or column to bomb";
            powerUp2Mess        = "Enter the position, where you want to place the bomb";
            powerUp3Mess        = "Enter the position for the searching rocket";
            attackMess          = "Its time to attack! Chose a field or write \"power\", to use a powerup.";
            enemyAttackMess     = "Your enemy attacks now";
            hitMess             = "Hit";
            missedMess          = "Missed";
            shipDestroyedMess   = "A ship has been destroyed!";
            powerUpUsedMess     = "Powerup was used";
            stillOpenMess       = "Still open:";
            powerUpActiveMess   = "Powerup active, chose one by entering it's index:";
            powerUp1ExplMess    = "Bomb a row/column";
            powerUp2ExplMess    = "Place a fragmentation bomb, which hits random fields in a radius of 2";
            powerUp3ExplMess    = "Place a searching roket, which finds ships close to it";
            backToAttackMess    = "\n(4) Back to normal attacking";
            battlefieldTitleMess = "\n     Your battlefield:                                               Enemy battlefield:";
            placeNext           = "Place your next ship";
        }
    }
    public void     run()                                   {
        try {
            printDots();
        } catch (InterruptedException _) {}
    }
    public void     printDots() throws InterruptedException {
        while (true) {
            System.out.print(".");
            sleep(700);}
    }
    public void     printPowerUp()                          {
        System.out.print(powerUpActiveMess);
        System.out.print("\n(1) ");
        if (engine.getPU1()) System.out.print(powerUp1ExplMess);
        else System.out.print(dismiss);
        System.out.print("\n(2) ");
        if (engine.getPU2()) System.out.print(powerUp2ExplMess);
        else System.out.print(dismiss);
        System.out.print("\n(3) ");
        if (engine.getPU3() == tries2 || engine.getPU3() == tries1) System.out.print(powerUp3ExplMess);
        else System.out.print(dismiss);
        if (engine.getPU3() == tries2) System.out.print(" (x2)");
        System.out.println(backToAttackMess);
    }
    public void     printMap()                              {
        System.out.println(battlefieldTitleMess);
        System.out.println("     0   1   2   3   4   5   6   7   8   9                           0   1   2   3   4   5   6   7   8   9  ");
        System.out.println("   -----------------------------------------                       ----------------------------------------- ");
        for (int outer = 0; outer != engine.getMapMe().length; outer++) {
            System.out.print(((char) (outer + 97)) + " |");
            for (char inner : engine.getMapMe()[outer]) {
                System.out.print("| ");
                printBoat(inner);
                System.out.print(" ");}
            System.out.print("|                    " + ((char) (outer + 97)) + " |");
            for (char inner : engine.getMapEnemy()[outer]) {
                System.out.print("| ");
                printBoat(inner);
                System.out.print(" ");}
            System.out.println("|\n   -----------------------------------------                       ----------------------------------------- ");}}
    private void    printBoat(char field)                   {
        switch (field) {
            case '⊗': System.out.print(RED + field + RESET);    break;
            case '≈': System.out.print(BLUE + field + RESET);   break;
            case 1: System.out.print(YELLOW + '■' + RESET);     break;
            case 2: System.out.print(CYAN + '■' + RESET);       break;
            case 3: System.out.print(MAGENTA + '■' + RESET);    break;
            case 4: System.out.print(GREEN + '■' + RESET);      break;
            default: System.out.print(Printer.empty);}
    }
}
