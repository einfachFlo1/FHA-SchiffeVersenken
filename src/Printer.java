import java.util.Scanner;

public class Printer extends Thread{
    private final GameEngine    engine;

    //Colors
    private static final String RESET       = "\u001B[0m";
    private static final String RED         = "\u001B[31m";
    private static final String GREEN       = "\u001B[32m";
    private static final String YELLOW      = "\u001B[33m";
    private static final String BLUE        = "\u001B[34m";
    private static final String MAGENTA     = "\u001B[35m";
    private static final String CYAN        = "\u001B[36m";

    //Game Pieces (Interchangeable)
    static final char hit                   = '⊗';
    static final char miss                  = '≈';
    static final char pieces                = '■';

    //Constants
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

    //Print strings
    String attackMess           = "Jetzt darfst du angreifen! ";
    String backToAttackMess     = "\n(4) zurück zum normalen Angriff.";
    String battlefieldTitleMess = "\n     Dein Schlachtfeld:                                              Gegnerisches Schlachtfeld:";
    String boatsExplMess        = "Bitte geben Sie die Länge der Boote ein.\nSie müssen 4 Boote eingeben. Diese können zwischen 2 und 6 Felder lang sein. Bei 0 fällt eines weg.\nInsgesamt müssen diese eine Länge von 15 ergeben.";
    String boatsToUseMess       = "Wollen Sie die standard-Boote nutzen (S) oder selbst die Boote festlegen (beliebige Eingabe)?";
    String connectedMess        = "Verbunden!";
    String connectingMess       = "Verbinde...";
    String displayIP            = "Ihre IP: ";
    String displayIPMess        = "Geben Sie sie weiter, damit sich Clients verbinden können.";
    String enemyAttackMess      = "Dein Gegner greift an.";
    String enterIPMess          = "Bitte gib die IP ein: ";
    String errorMess            = "Fehler.";
    String gameBeginMess1       = "\nLasst die Schlacht beginnen! \nHier einmal das Schlachtfeld!";
    String gameBeginMess2       = "Bitte Platziere nun deine Schiffe. Zur Auswahl stehen:\n";
    String gameBeginMess3       = "\nDie Schiffe kannst du platzieren, indem du den Start- und Endpunkt angibst.";
    String gameBeginMess4       = "Der Kampf beginnt!";
    String hitMess              = "Getroffen!";
    String hostOrClientMess     = "Host(H) oder Client(beliebige Eingabe): ";
    String loseMess             = "Du hast verloren...";
    String missedMess           = "Verfehlt!";
    String notValidMess         = "Keine valide Eingabe.";
    String notValidRetryMess    = "Keine gültige Eingabe, bitte geben Sie es erneut ein.";
    String placeNext            = "Platziere dein nächstes Schiff!";
    String powerUp1ExplMess     = "Bombardiere eine Reihe/Zeile!";
    String powerUp2ExplMess     = "Wirft eine Splitterbombe, welche zufällige Felder in einem Umkreis von 2 trifft.";
    String powerUp3ExplMess     = "Starte eine Suchrakete, welche Boote in der Nähe findet.";
    String powerUp1Mess         = "Gib eine Reihe oder Spalte an, die bombardiert werden soll!";
    String powerUp2Mess         = "Gib die Position an, wo der die Bombe einschlagen soll!";
    String powerUp3Mess         = "Gib die Position an, an der die Rakete suchen soll!";
    String powerUpActiveMess    = "Powerup aktiv, wähle eines und gib den Index ein:";
    String powerUpMess          = "Wähle ein Feld oder schreibe \"power\", um ein Powerup zu nutzen.";
    String powerUpUsedMess      = "Powerup wurde genutzt.";
    String shipDestroyedMess    = "Ein Schiff wurde zerstört!";
    String shipPlacedMess       = "Das Schiff wurde aufgestellt!";
    String stillOpenMess        = "Noch übrig:";
    String waitClientMess       = "Warte auf Client...";
    String winMess              = "Du hast gewonnen!";

    public Printer(GameEngine engine) {
        this.engine = engine;
    }

    //Output map
    public void     setLanguage()                           {
        Scanner scan = new Scanner(System.in);
        System.out.print("Which language? (D/E): ");
        if ("E".equals(scan.next())) {
            attackMess              = "Its time to attack! ";
            backToAttackMess        = "\n(4) Back to normal attacking.";
            battlefieldTitleMess    = "\n     Your battlefield:                                               Enemy battlefield:";
            boatsExplMess           = "Please enter the lengths of your boats.\nYou have to enter 4 boats. These can vary between 2 to 6 fields. By using 0 you delete one boat.\nOverall your boats have to be equivalent to 15 fieds.";
            boatsToUseMess          = "Use standard boats(S) or decide yourself? (any input)?";
            connectedMess           = "Connected!";
            connectingMess          = "Connecting...";
            displayIP               = "Your IP: ";
            displayIPMess           = "Share your IP, so clients can connect.";
            enemyAttackMess         = "Your enemy attacks now.";
            enterIPMess             = "Please enter the IP...";
            errorMess               = "Error.";
            gameBeginMess1          = "\nLet the battle begin! \nLet's have a look at the battlefield!";
            gameBeginMess2          = "Its time, to place your ships. You can chose from:\n";
            gameBeginMess3          = "\nYou can place the ships by entering the start- and endposition.";
            gameBeginMess4          = "The battle begins!";
            hitMess                 = "Hit!";
            hostOrClientMess        = "Host(H) or client(any input): ";
            loseMess                = "You lost...";
            missedMess              = "Missed!";
            notValidMess            = "No valid input.";
            notValidRetryMess       = "No valid input, please try again.";
            placeNext               = "Place your next ship!";
            powerUp1ExplMess        = "Bomb a row/column!";
            powerUp2ExplMess        = "Places a fragmentation bomb, which hits random fields in a radius of 2.";
            powerUp3ExplMess        = "Places a searching rocket, which finds ships close to it.";
            powerUp1Mess            = "Enter a row or column to bomb!";
            powerUp2Mess            = "Enter the position, where you want to place the bomb!";
            powerUp3Mess            = "Enter the position for the searching rocket!";
            powerUpActiveMess       = "Powerup active, chose one by entering it's index:";
            powerUpMess             = "Chose a field or write \"power\", to use a powerup.";
            powerUpUsedMess         = "Powerup was used.";
            shipDestroyedMess       = "A ship has been destroyed!";
            shipPlacedMess          = "The ship has been placed!";
            stillOpenMess           = "Still open:";
            waitClientMess          = "Waiting for client...";
            winMess                 = "You won!";
        }
    }
    public void     printIP(String address)                 {
        System.out.println(displayIP + GREEN + address + RESET + "\n" + displayIPMess);
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
        System.out.println("    ╥ 0 ╥ 1 ╥ 2 ╥ 3 ╥ 4 ╥ 5 ╥ 6 ╥ 7 ╥ 8 ╥ 9 ╥                       ╥ 0 ╥ 1 ╥ 2 ╥ 3 ╥ 4 ╥ 5 ╥ 6 ╥ 7 ╥ 8 ╥ 9 ╥");
        System.out.println("   ╭╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣                      ╭╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣ ");
        for (int outer = 0; outer != engine.getMapMe().length; outer++) {
            System.out.print(((char) (outer + 65)) + "  │");
            for (char inner : engine.getMapMe()[outer]) {
                System.out.print("║ ");
                printBoat(inner);
                System.out.print(" ");}
            System.out.print("║                   " + ((char) (outer + 65)) + "  │");
            for (char inner : engine.getMapEnemy()[outer]) {
                System.out.print("║ ");
                printBoat(inner);
                System.out.print(" ");}
            if (outer < engine.getMapMe().length - 1) System.out.println("║\n╞══╪╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣                   ╞══╪╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣ ");
            else System.out.println("║\n   ╰╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝                      ╰╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝ ");}}
    private void    printBoat(char field)                   {
        switch (field) {
            case hit: System.out.print(RED + field + RESET);        break;
            case miss: System.out.print(BLUE + field + RESET);      break;
            case 1: System.out.print(YELLOW + pieces + RESET);      break;
            case 2: System.out.print(CYAN + pieces + RESET);        break;
            case 3: System.out.print(MAGENTA + pieces + RESET);     break;
            case 4: System.out.print(GREEN + pieces + RESET);       break;
            default: System.out.print(empty);}
    }
}