import java.io.IOException;
import java.util.Scanner;

public class GameEngine extends Thread{
    private final char[][]  mapMe;
    private final char[][]  mapEnemy;
    private final Scanner   scan;
    private final Network   network;
    private final Printer   printer;
    private char            indexB;
    private boolean         pU1;
    private boolean         pU2;
    private char            pU3;

    public GameEngine() {
        this.mapMe      = new char[10][10];
        this.mapEnemy   = new char[10][10];
        this.printer    = new Printer(this);
        this.printer.setLanguage();
        this.network    = new Network(printer);
        scan            = new Scanner(System.in);
        indexB  = 1;
        pU1 = true;
        pU2 = true;
        pU3 = Printer.tries2;
        for (int outer = 0; outer != 10; outer++) {
            for (int inner = 0; inner != 10; inner++)
                mapMe[outer][inner] = 32;
            for (int inner = 0; inner != 10; inner++)
                mapEnemy[outer][inner] = 32;}
        try {network.buildConnection(); sleep(700);
        } catch (IOException | InterruptedException e) {throw new RuntimeException(e);}
    }

    //Getter
    public char[][] getMapMe()      {return mapMe;}
    public char[][] getMapEnemy()   {return mapEnemy;}
    public boolean  getPU1()        {return pU1;}
    public boolean  getPU2()        {return pU2;}
    public char     getPU3()        {return pU3;}

    //Game loop
    public void     gameBegin()             {
        System.out.println(printer.gameBeginMess1);
        printer.printMap();
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        System.out.println(printer.gameBeginMess2);
        System.out.println(printer.gameBeginMess3);
        placePieces("■ | ■ | ■ | ■ | ■", "■ | ■ | ■ | ■", "■ | ■ | ■", "■ | ■ | ■");
        System.out.println(printer.gameBeginMess4);
        try { sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        if (network.role != 1) {
            String order = Integer.toString(((int) (Math.random() * 10)) % 2);
            network.sendSignal(order);
            if ("0".equals(order)) {
                attacked();
                network.sendSignal(Printer.cont);}
        } else {
            if ("1".equals(network.receiveSignal())) {
                attacked();
                network.sendSignal(Printer.cont);}}
        while (true) {
            attack();
            if (gameOver(0)) break;
            attacked();
            if (gameOver(1)) break;}
        try { network.closeConnection(); } catch (IOException e) {throw new RuntimeException(e);}
    }
    private boolean gameOver(int qualifier) {
        if (qualifier == 1) {
            for (char[] outer : mapMe)
                for (char inner : outer)
                    if (inner != ' ' && inner != Printer.miss && inner != Printer.hit) {
                        network.sendSignal(Printer.cont);
                        return false;}
            network.sendSignal(Printer.end);
            System.out.println(printer.loseMess);
            return true;
        } else {
            if (network.receiveSignal().equals(Printer.end)) {
                System.out.println(printer.winMess);
                return true;}}
        return false;
    }

    //Helper method
    private boolean validateInput(String input) {
        return (input.length() == 2 && input.charAt(0) >= 97 && input.charAt(0) <= 106 && input.charAt(1) >= 48 && input.charAt(1) <= 57);
    }

    //Placing ships
    private void    placePieces(String boot1, String boot2, String boot3, String boot4) {
        int     id;
        String  inputStart;
        String  inputEnd;
        while (!boot1.equals(boot2) || !boot1.equals(boot3) || !boot1.equals(boot4)) {
            inputStart   = scan.next();
            inputEnd     = scan.next();
            if (validateInput(inputStart) && validateInput(inputEnd)) {
                id = Math.abs(((int) inputStart.charAt(0) - (int) inputEnd.charAt(0)) - ((int) inputStart.charAt(1) - (int) inputEnd.charAt(1)));
                if (id == 4 && !boot1.equals(Printer.dismiss)) {
                    if (placeDots(inputStart.charAt(0) - 97, inputStart.charAt(1) - 48, inputEnd.charAt(0) - 97, inputEnd.charAt(1) - 48))
                        boot1 = Printer.dismiss;
                } else if (id == 3 && !boot2.equals(Printer.dismiss)) {
                    if (placeDots(inputStart.charAt(0) - 97, inputStart.charAt(1) - 48, inputEnd.charAt(0) - 97, inputEnd.charAt(1) - 48))
                        boot2 = Printer.dismiss;
                } else if (id == 2 && !boot3.equals(Printer.dismiss)) {
                    if (placeDots(inputStart.charAt(0) - 97, inputStart.charAt(1) - 48, inputEnd.charAt(0) - 97, inputEnd.charAt(1) - 48))
                        boot3 = Printer.dismiss;
                } else if (id == 2 && !boot4.equals(Printer.dismiss)) {
                    if (placeDots(inputStart.charAt(0) - 97, inputStart.charAt(1) - 48, inputEnd.charAt(0) - 97, inputEnd.charAt(1) - 48))
                        boot4 = Printer.dismiss;
                } else {
                    System.out.println(printer.notValidMess);
                    placePieces(boot1, boot2, boot3, boot4);
                    break;}
            } else {
                System.out.println(printer.notValidRetryMess);
                placePieces(boot1, boot2, boot3, boot4);
                break;}
            if (!boot1.equals(boot2) || !boot1.equals(boot3) || !boot1.equals(boot4)) {
            System.out.println(printer.stillOpenMess + "\n 1.) " + boot1 + "\n 2.) " + boot2 + "\n 3.) " + boot3 + "\n 4.) " + boot4 + "\n");
            System.out.println(printer.placeNext);}}
    }
    private boolean placeDots(int start0, int start1, int end0, int end1)               {
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
                if (mapMe[runH1][runV1] != Printer.empty) {
                    System.out.println(printer.notValidMess);
                    return false;}
            runV1 = cpyV1;}
        runH1 = cpyH1;
        for (; runH1 <= runH2; runH1++) {
            for (; runV1 <= runV2; runV1++)
                mapMe[runH1][runV1] = indexB;
            runV1 = cpyV1;}
        indexB++;
        System.out.println(printer.shipPlacedMess);
        printer.printMap();
        return true;
    }

    //Attacking
    private void    powerUpInput(String in)     {
        String input;
        for (int x = in.charAt(0) - 97, y = in.charAt(1) - 48; in.length() > 3; in = in.substring(3), x = in.charAt(0) - 97, y = in.charAt(1) - 48) {
            input = network.receiveSignal();
            if (x >= 0 && y >= 0 && x <= 9 && y <= 9) {
                if (input.equals(Printer.missSignal))
                    mapEnemy[x][y] = Printer.miss;
                else
                    mapEnemy[x][y] = Printer.hit;}}
    }
    private char    powerUpRand(char input)     {
        int num = ((int)(Math.random() * 10)) % 3;
        if (((int)(Math.random() * 10)) % 2 == 0) {
            return (char) ((input - num));
        } else {
            return (char) ((input + num));}
    }
    private boolean powerUp1()                  {
        System.out.println(printer.powerUp1Mess);
        String input = scan.next();
        String output = "";
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        if (input.charAt(0) < 97 || input.charAt(0) > 106) {
            for (int counter = 0; counter < 11; counter++)
                output = output + (char) (counter + 97) + input.charAt(0) + Printer.empty;
        } else if (input.charAt(0) < 48 || input.charAt(0) > 57) {
            for (int counter = 0; counter < 11; counter++)
                output = output + input.charAt(0) + (char) (counter + 48) + Printer.empty;
        } else {
            System.out.println(printer.notValidMess);
            return false;}
        network.sendSignal(Printer.pU1);
        network.sendSignal(output);
        powerUpInput(output);
        pU1 = false;
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        printer.printMap();
        return true;
    }
    private boolean powerUp2()                  {
        System.out.println(printer.powerUp2Mess);
        String input = scan.next();
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        String output = input + Printer.empty;
        String strive = "";
        if (!validateInput(input)) {
            System.out.println(printer.notValidMess);
            return false;}
        for (int counter = 0; counter < 11;) {
            strive = strive + powerUpRand(input.charAt(0)) + powerUpRand(input.charAt(1));
            if (!output.contains(strive) && !strive.equals(input)) {
                output = output + strive + Printer.empty;
                counter++;}
            strive = "";}
        network.sendSignal(Printer.pU2);
        network.sendSignal(output);
        powerUpInput(output);
        pU2 = false;
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        printer.printMap();
        return true;
    }
    private boolean powerUp3()                  {
        System.out.println(printer.powerUp3Mess);
        String input = scan.next();
        String output;
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        if (!validateInput(input)) {
            System.out.println(printer.notValidMess);
            return false;}
        network.sendSignal(Printer.pU3);
        network.sendSignal(input);
        output = network.receiveSignal();
        if (output.equals(Printer.missSignal)) {
            System.out.println(printer.missedMess);
        } else {
            System.out.println(printer.hitMess);
            mapEnemy[output.charAt(0) - 97][output.charAt(1) - 48] = Printer.hit;}
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        printer.printMap();
        if (pU3 == Printer.tries2)
            pU3 = Printer.tries1;
        else pU3 = Printer.empty;
        return true;
    }
    private boolean attackPowerUp(String input) {
        if (input.equals("power")) {
            printer.printPowerUp();
            input = scan.next();
            if (input.equals("1") && pU1)                                                       return powerUp1();
            else if (input.equals("2") && pU2)                                                  return powerUp2();
            else if (input.equals("3") && (pU3 == Printer.tries2 || pU3 == Printer.tries1))     return powerUp3();
            else if (input.equals("4")) {attack();                                              return true;}}
        return false;
    }
    private void    attack()                    {
        System.out.println(printer.attackMess);
        String input = scan.next();
        if ((!pU1 && !pU2 && pU3 == Printer.empty || !attackPowerUp(input))) {
            if (!validateInput(input))
                attack();
            else {
                int x = input.charAt(0) - 97;
                int y = input.charAt(1) - 48;

                if (mapEnemy[x][y] == Printer.hit || mapEnemy[x][y] == Printer.miss) {
                    System.out.println(printer.notValidMess);
                    attack();
                } else {
                    network.sendSignal(input);
                    input = network.receiveSignal();
                    try {
                        if (input.equals(Printer.missSignal)) {
                            mapEnemy[x][y] = Printer.miss;
                            System.out.println(printer.missedMess);
                            sleep(700);
                        } else if (input.equals(Printer.hitSignal)) {
                            mapEnemy[x][y] = Printer.hit;
                            System.out.println(printer.hitMess);
                            sleep(700);
                        } else {
                            mapEnemy[x][y] = Printer.hit;
                            System.out.println(printer.shipDestroyedMess);
                            sleep(700);}
                    } catch (InterruptedException e) {throw new RuntimeException(e);}}
                printer.printMap();
            }}
    }

    //Being attacked
    private String  searchBoat(String input, int distance)  {
        String output = Printer.missSignal;
        for (int x = input.charAt(0) - 97 - distance; x <= input.charAt(0) - 97 + distance; x++)
            for (int y = input.charAt(1) - 48 - distance; y <= input.charAt(1) - 48 + distance; y++) {
                if (x >= 0 && y >= 0 && x <= 9 && y <= 9)
                    if (mapMe[x][y] >= 1 && mapMe[x][y] <= 4) {
                        mapMe[x][y] = Printer.hit;
                        System.out.println(printer.hitMess);
                        return ("" + ((char) (x + 97)) + ((char) (y + 48)));}}
        if (distance < 2)
            return searchBoat(input, distance + 1);
        System.out.println(printer.missedMess);
        return output;
    }
    private boolean checkDestroyed(int x, int y)            {
        for (int outer = 0; outer < 10; outer++)
            for (int inner = 0; inner < 10; inner++)
                if ((int) mapMe[outer][inner] == (int) mapMe[x][y] && (outer != x || inner != y))
                    return true;
        return false;
    }
    private boolean attackedPowerUp(String input)           {
        String inputSignal;
        if (input.equals(Printer.pU1) || input.equals(Printer.pU2)) {
            System.out.println(printer.powerUpUsedMess);
            inputSignal = network.receiveSignal();
            for (int x = inputSignal.charAt(0) - 97, y = inputSignal.charAt(1) - 48; inputSignal.length() > 3; inputSignal = inputSignal.substring(3), x = inputSignal.charAt(0) - 97, y = inputSignal.charAt(1) - 48) {
                if (x >= 0 && y >= 0 && x <= 9 && y <= 9) {
                    if (mapMe[x][y] == Printer.empty || mapMe[x][y] == Printer.miss) {
                        mapMe[x][y] = Printer.miss;
                        network.sendSignal(Printer.missSignal);
                    } else {
                        mapMe[x][y] = Printer.hit;
                        network.sendSignal(Printer.hitSignal);}
                } else network.sendSignal(Printer.dismiss);}
            return true;
        } else if (input.equals(Printer.pU3)) {
            network.sendSignal(searchBoat(network.receiveSignal(), 0));
            return true;}
        return false;
    }
    private void    attacked()                              {
        Thread printDots = new Printer(this);
        System.out.println(printer.enemyAttackMess);
        printDots.start();
        String input = network.receiveSignal();
        printDots.interrupt();
        System.out.println();
        if (!attackedPowerUp(input)) {
            int x = input.charAt(0) - 97;
            int y = input.charAt(1) - 48;
            try {
                if (mapMe[x][y] == Printer.empty) {
                    mapMe[x][y] = Printer.miss;
                    System.out.println(printer.missedMess);
                    network.sendSignal(Printer.missSignal);
                    sleep(700);
                } else {
                    if (!checkDestroyed(x, y)) {
                        System.out.println(printer.shipDestroyedMess);
                        network.sendSignal(Printer.destroyedSignal);
                        sleep(700);
                    } else {
                        System.out.println(printer.hitMess);
                        network.sendSignal(Printer.hitSignal);}
                        sleep(700);
                    mapMe[x][y] = Printer.hit;}
            } catch (InterruptedException e) {throw new RuntimeException(e);}}
        printer.printMap();
    }
}
