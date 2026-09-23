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

    public          GameEngine()                                                        {
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

    //Game loop
    public void     gameBegin()                                                         {
        System.out.println(printer.gameBeginMess1);
        printer.printMap();
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        chooseBoatSize();
        System.out.println(printer.gameBeginMess4);
        try { sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        if (network.role != 1) {                                                                                        //Condition deciding, who starts
            String order = Integer.toString(((int) (Math.random() * 10)) % 2);
            network.sendSignal(order);
            if ("0".equals(order)) {
                attacked();
                network.sendSignal(Printer.cont);}
        } else {
            if ("1".equals(network.receiveSignal())) {
                attacked();
                network.sendSignal(Printer.cont);}}
        while (true) {                                                                                                  //Main game loop
            attack();
            if (gameOver(0)) break;
            attacked();
            if (gameOver(1)) break;}
        try { network.closeConnection(); } catch (IOException e) {throw new RuntimeException(e);}
    }
    private boolean gameOver(int qualifier)                                             {
        if (qualifier == 1) {                                                                                           //Checks, if i lost
            for (char[] outer : mapMe)
                for (char inner : outer)
                    if (inner != ' ' && inner != Printer.miss && inner != Printer.hit) {
                        network.sendSignal(Printer.cont);
                        return false;}
            network.sendSignal(Printer.end);
            System.out.println(printer.loseMess);
            return true;
        } else {                                                                                                        //Checks, if enemy lost
            if (network.receiveSignal().equals(Printer.end)) {
                System.out.println(printer.winMess);
                return true;}}
        return false;
    }

    //Helper method
    private boolean validateInput(String input)                                         {                                                                       //Checks, if input is valid or not
        return (input.length() == 2 && ((input.charAt(0) >= 97 && input.charAt(0) <= 106) || (input.charAt(0) >= 65 && input.charAt(0) <= 74)) && input.charAt(1) >= 48 && input.charAt(1) <= 57);
    }
    private int     transformSign(char sign)                                            {
        if (sign >= 48 && sign <= 57)
            return (sign - 48);
        else if (sign >= 97 && sign <= 106)
            return (sign - 97);
        else if (sign >= 65 && sign <= 74)
            return (sign - 65);
        return 0;
    }

    //Placing ships
    private int     getID(String boat)                                                  {
        String piece = Printer.pieces + "";
        if (boat.equals(Printer.dismiss))
            return 0;
        return boat.length() - boat.replace(piece, "").length();
    }
    private String  buildBoat(int size)                                                 {
        String boat = Printer.pieces  + " | " +  Printer.pieces;
        for (int counter = 2; counter < size; counter++)
            boat = boat + " | "  + Printer.pieces;
        if (size == 0)
            return Printer.dismiss;
        return boat;
    }
    private void    chooseBoatSize()                                                    {
        int[] boats = new int[4];
        System.out.println(printer.boatsToUseMess);
        if (scan.nextLine().charAt(0) == 'S')
            placePieces(buildBoat(5), buildBoat(4), buildBoat(3), buildBoat(3));
        else {
            System.out.println(printer.boatsExplMess);
            for (int counter = 0; counter < 4; counter++)
                for (boats[counter] = scan.nextLine().charAt(0) - 48; !(boats[counter] <= 6 && boats[counter] >= 2) && !(boats[counter] == 0); boats[counter] = scan.nextLine().charAt(0) - 48) System.out.println(printer.notValidMess);
            if (boats[0] + boats[1] + boats[2] + boats[3] != 15) {
                System.out.println(printer.notValidMess);
                chooseBoatSize();
            } else
                placePieces(buildBoat(boats[0]), buildBoat(boats[1]), buildBoat(boats[2]), buildBoat(boats[3]));
        }
    }
    private void    placePieces(String boat1, String boat2, String boat3, String boat4) {
        int     id;
        String  inputStart;
        String  inputEnd;
        System.out.println(printer.gameBeginMess2 + "1.) " + boat1 + "\n2.) " + boat2 + "\n3.) " + boat3 + "\n4.) " + boat4);
        System.out.println(printer.gameBeginMess3);
        while (!boat1.equals(Printer.dismiss) || !boat2.equals(Printer.dismiss) || !boat3.equals(Printer.dismiss) || !boat4.equals(Printer.dismiss)) {                                  //loops, while not all boats are placed
            inputStart   = scan.next();
            inputEnd     = scan.next();
            if (validateInput(inputStart) && validateInput(inputEnd)) {                                                 //Checks distance between start and end, to identify the boat
                id = 1 + Math.abs((transformSign(inputStart.charAt(0)) - transformSign(inputEnd.charAt(0))) - (transformSign(inputStart.charAt(1)) - transformSign(inputEnd.charAt(1))));
                if (id == getID(boat1) && !boat1.equals(Printer.dismiss)) {
                    if (placeDots(transformSign(inputStart.charAt(0)), transformSign(inputStart.charAt(1)), transformSign(inputEnd.charAt(0)), transformSign(inputEnd.charAt(1))))
                        boat1 = Printer.dismiss;
                } else if (id == getID(boat2) && !boat2.equals(Printer.dismiss)) {
                    if (placeDots(transformSign(inputStart.charAt(0)), transformSign(inputStart.charAt(1)), transformSign(inputEnd.charAt(0)), transformSign(inputEnd.charAt(1))))
                        boat2 = Printer.dismiss;
                } else if (id == getID(boat3) && !boat3.equals(Printer.dismiss)) {
                    if (placeDots(transformSign(inputStart.charAt(0)), transformSign(inputStart.charAt(1)), transformSign(inputEnd.charAt(0)), transformSign(inputEnd.charAt(1))))
                        boat3 = Printer.dismiss;
                } else if (id == getID(boat4) && !boat4.equals(Printer.dismiss)) {
                    if (placeDots(transformSign(inputStart.charAt(0)), transformSign(inputStart.charAt(1)), transformSign(inputEnd.charAt(0)), transformSign(inputEnd.charAt(1))))
                        boat4 = Printer.dismiss;
                } else {
                    System.out.println(printer.notValidMess);
                    placePieces(boat1, boat2, boat3, boat4);
                    break;}
            } else {
                System.out.println(printer.notValidRetryMess);
                placePieces(boat1, boat2, boat3, boat4);
                break;}
            if (!boat1.equals(boat2) || !boat1.equals(boat3) || !boat1.equals(boat4)) {
            System.out.println(printer.stillOpenMess + "\n 1.) " + boat1 + "\n 2.) " + boat2 + "\n 3.) " + boat3 + "\n 4.) " + boat4 + "\n");
            System.out.println(printer.placeNext);}}
    }
    private boolean placeDots(int start0, int start1, int end0, int end1)               {
        System.out.println(" s0:" + start0 + " s1:" + start1 + " e0:" + end0 + " e1:" + end1);
        int runV1 = start1;
        int runV2 = end1;
        int runH1 = start0;
        int runH2 = end0;
        if (start0 > end0) {                                                                                            //Potentially swaps coordinates, if start > end
            runH1 = end0;
            runH2 = start0;
        } else if (start1 > end1) {
            runV1 = end1;
            runV2 = start1;}
        int cpyV1 = runV1;
        int cpyH1 = runH1;
        for (; runH1 <= runH2; runH1++) {                                                                               //Checks, if boats are placeable
            for (; runV1 <= runV2; runV1++)
                if (mapMe[runH1][runV1] != Printer.empty) {
                    System.out.println(printer.notValidMess);
                    return false;}
            runV1 = cpyV1;}
        runH1 = cpyH1;
        for (; runH1 <= runH2; runH1++) {                                                                               //Places boats
            for (; runV1 <= runV2; runV1++)
                mapMe[runH1][runV1] = indexB;
            runV1 = cpyV1;}
        indexB++;
        System.out.println(printer.shipPlacedMess);
        printer.printMap();
        return true;
    }

    //Attacking
    private void    powerUpEvaluate(String hitList)                                     {
        String input;                                                                                                   //evaluates output list while receiving hit/miss signals from defender
        for (int x = transformSign(hitList.charAt(0)), y = transformSign(hitList.charAt(1)); hitList.length() > 3; hitList = hitList.substring(3), x = transformSign(hitList.charAt(0)), y = transformSign(hitList.charAt(1))) {
            input = network.receiveSignal();
            if (x >= 0 && y >= 0 && x <= 9 && y <= 9) {
                if (input.equals(Printer.missSignal))
                    mapEnemy[x][y] = Printer.miss;
                else
                    mapEnemy[x][y] = Printer.hit;}}
    }
    private boolean powerUp1()                                                          {
        System.out.println(printer.powerUp1Mess);                                                                       //makes attack list
        String input = scan.next();
        String output = "";
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        if (input.charAt(0) < 97 || input.charAt(0) > 106) {                                                            //based on column
            for (int counter = 0; counter < 11; counter++)
                output = output + (char) (counter + 97) + input.charAt(0) + Printer.empty;
        } else if (input.charAt(0) < 48 || input.charAt(0) > 57) {                                                      //based on row
            for (int counter = 0; counter < 11; counter++)
                output = output + input.charAt(0) + (char) (counter + 48) + Printer.empty;
        } else {
            System.out.println(printer.notValidMess);                                                                   //input error
            return false;}
        network.sendSignal(Printer.pU1);
        network.sendSignal(output);
        powerUpEvaluate(output);
        pU1 = false;                                                                                                    //power up used
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        printer.printMap();
        return true;
    }
    private char    powerUp2Rand(char input)                                            {                                                                   //Gives back position 0-2 steps from input position
        int num = ((int)(Math.random() * 10)) % 3;
        if (((int)(Math.random() * 10)) % 2 == 0) {
            return (char) ((input - num));
        } else {
            return (char) ((input + num));}
    }
    private boolean powerUp2()                                                          {
        System.out.println(printer.powerUp2Mess);
        String input = scan.next();
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        String output = input + Printer.empty;
        String strive = "";
        if (!validateInput(input)) {
            System.out.println(printer.notValidMess);
            return false;}
        for (int counter = 0; counter < 11;) {
            strive = strive + powerUp2Rand(input.charAt(0)) + powerUp2Rand(input.charAt(1));
            if (!output.contains(strive) && !strive.equals(input)) {
                output = output + strive + Printer.empty;
                counter++;}
            strive = "";}
        network.sendSignal(Printer.pU2);
        network.sendSignal(output);
        powerUpEvaluate(output);
        pU2 = false;
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        printer.printMap();
        return true;
    }
    private boolean powerUp3()                                                          {
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
            mapEnemy[transformSign(output.charAt(0))][transformSign(output.charAt(1))] = Printer.hit;}
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        printer.printMap();
        if (pU3 == Printer.tries2)
            pU3 = Printer.tries1;
        else pU3 = Printer.empty;
        return true;
    }
    private boolean attackPowerUp(String input)                                         {
        if (input.equals("power")) {
            printer.printPowerUp();
            input = scan.next();
            if (input.equals("1") && pU1)                                                       return powerUp1();
            else if (input.equals("2") && pU2)                                                  return powerUp2();
            else if (input.equals("3") && (pU3 == Printer.tries2 || pU3 == Printer.tries1))     return powerUp3();
            else if (input.equals("4")) {attack();                                              return true;}}
        return false;
    }
    private void    attack()                                                            {
        System.out.print(printer.attackMess);
        System.out.println(printer.powerUpMess);
        String input = scan.next();
        if ((!pU1 && !pU2 && pU3 == Printer.empty || !attackPowerUp(input))) {
            if (!validateInput(input))
                attack();
            else {
                int x = transformSign(input.charAt(0));
                int y = transformSign(input.charAt(1));

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
    private String  powerUp3Search(String input, int distance)                          {
        String output = Printer.missSignal;
        for (int x = transformSign(input.charAt(0)) - distance; x <= transformSign(input.charAt(0)) + distance; x++)
            for (int y = transformSign(input.charAt(1)) - distance; y <= transformSign(input.charAt(1)) + distance; y++) {
                if (x >= 0 && y >= 0 && x <= 9 && y <= 9)
                    if (mapMe[x][y] >= 1 && mapMe[x][y] <= 4) {
                        mapMe[x][y] = Printer.hit;
                        System.out.println(printer.hitMess);
                        return ("" + ((char) (x + 97)) + ((char) (y + 48)));}}
        if (distance < 2)
            return powerUp3Search(input, distance + 1);
        System.out.println(printer.missedMess);
        return output;
    }
    private boolean checkDestroyed(int x, int y)                                        {
        for (int outer = 0; outer < 10; outer++)
            for (int inner = 0; inner < 10; inner++)
                if ((int) mapMe[outer][inner] == (int) mapMe[x][y] && (outer != x || inner != y))
                    return true;
        return false;
    }
    private boolean attackedPowerUp(String input)                                       {
        String inputSignal;
        if (input.equals(Printer.pU1) || input.equals(Printer.pU2)) {
            System.out.println(printer.powerUpUsedMess);
            inputSignal = network.receiveSignal();
            for (int x = transformSign(inputSignal.charAt(0)), y = transformSign(inputSignal.charAt(1)); inputSignal.length() > 3; inputSignal = inputSignal.substring(3), x = transformSign(inputSignal.charAt(0)), y = transformSign(inputSignal.charAt(1))) {
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
            network.sendSignal(powerUp3Search(network.receiveSignal(), 0));
            return true;}
        return false;
    }
    private void    attacked()                                                          {
        Thread printDots = new Printer(this);
        System.out.println(printer.enemyAttackMess);
        printDots.start();
        String input = network.receiveSignal();
        printDots.interrupt();
        System.out.println();
        if (!attackedPowerUp(input)) {
            int x = transformSign(input.charAt(0));
            int y = transformSign(input.charAt(1));
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

    //Getter
    public char[][] getMapMe()      {return mapMe;}
    public char[][] getMapEnemy()   {return mapEnemy;}
    public boolean  getPU1()        {return pU1;}
    public boolean  getPU2()        {return pU2;}
    public char     getPU3()        {return pU3;}
}