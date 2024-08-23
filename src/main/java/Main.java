import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        boolean playAgain = true;

        while (playAgain) {
            String cs = "UTF-8";
            File scores = new File("src/scores.txt");
            String importedScores = FileUtils.readFileToString(scores, cs);
            String[] importedScoresArray = importedScores.split(" & ");
            int sPlayer1 = 0;
            int sPlayer2 = 0;
            if (importedScoresArray.length == 2 && importedScoresArray[0].contains("Player1")
                    && importedScoresArray[1].contains("Player2")) {
                sPlayer1 = Integer.parseInt(importedScoresArray[0].split(": ")[1]);
                sPlayer2 = Integer.parseInt(importedScoresArray[1].split(": ")[1]);
            }
            System.out.println("ACTUAL SCORE:\nPlayer1: " + sPlayer1 + "\nPlayer2: " + sPlayer2 + System.lineSeparator());
            String[][] table = new String[][]{new String[]{"-", "-", "-"}, new String[]{"-", "-", "-"}, new String[]{"-", "-", "-"}};
            for (int i = 0; i < table.length; i++) {
                for (int j = 0; j < table[i].length; j++) {
                    System.out.print(table[i][j] + "\t");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("Choice numbers:");
            int[][] numbersTable = new int[][]{new int[]{1, 2, 3}, new int[]{4, 5, 6}, new int[]{7, 8, 9}};
            for (int i = 0; i < numbersTable.length; i++) {
                for (int j = 0; j < numbersTable[i].length; j++) {
                    System.out.print(numbersTable[i][j] + "\t");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("LET'S START! Player1 has 'X' and Player2 has 'O'");
            boolean winCheck = false;
            boolean error;
            int count = 0;
            while (!winCheck) {
                String player1Badge = "X";
                String player2Badge = "O";
                error = true;
                while (error) {
                    error = false;
                    System.out.println("Player1 choice:");
                    try {
                        int choice1 = Integer.parseInt(in.nextLine());
                        choiceCheck(choice1);
                        try {
                            occupiedCellCheck(table, choice1);
                            choice(table, choice1, player1Badge);
                            count++;
                            for (int i = 0; i < table.length; i++) {
                                for (int j = 0; j < table[i].length; j++) {
                                    System.out.print(table[i][j] + "\t");
                                }
                                System.out.println();
                            }
                            winCheck = winCheck(table, winCheck, "Player1");
                            if (winCheck) {
                                sPlayer1 += 3;
                            }
                        } catch (OccupiedCellException e) {
                            error = true;
                            System.out.println(e.getMessage());
                        }
                    } catch (NumberFormatException e) {
                        error = true;
                        System.out.println("You must insert a number!");
                    } catch (ChoiceException e) {
                        error = true;
                        System.out.println(e.getMessage());
                    }
                }
                if (count == 7 && !winCheck) {
                    if (drawCheck(table, player1Badge)) {
                        System.out.println("Draw!");
                        sPlayer1++;
                        sPlayer2++;
                        winCheck = true;
                    }
                }
                if (!winCheck) {
                    error = true;
                    while (error) {
                        error = false;
                        System.out.println("Player2 choice:");
                        try {
                            int choice2 = Integer.parseInt(in.nextLine());
                            choiceCheck(choice2);
                            try {
                                occupiedCellCheck(table, choice2);
                                choice(table, choice2, player2Badge);
                                count++;
                                for (int i = 0; i < table.length; i++) {
                                    for (int j = 0; j < table[i].length; j++) {
                                        System.out.print(table[i][j] + "\t");
                                    }
                                    System.out.println();
                                }
                                winCheck = winCheck(table, winCheck, "Player2");
                                if (winCheck) {
                                    sPlayer2 += 3;
                                }
                            } catch (OccupiedCellException e) {
                                error = true;
                                System.out.println(e.getMessage());
                            }
                        } catch (NumberFormatException e) {
                            error = true;
                            System.out.println("You must insert a number!");
                        } catch (ChoiceException e) {
                            error = true;
                            System.out.println(e.getMessage());
                        }
                    }
                }
                if (count == 8 && !winCheck) {
                    if (drawCheck(table, player2Badge)) {
                        System.out.println("Draw!");
                        sPlayer1++;
                        sPlayer2++;
                        winCheck = true;
                    }
                }

            }
            System.out.println("Player1: " + sPlayer1 + System.lineSeparator() + "Player2: " + sPlayer2);
            FileUtils.writeStringToFile(scores, "Player1: " + sPlayer1 + " & Player2: " + sPlayer2, cs);
            boolean check = true;
            while (check) {
                System.out.println("Do you want to play again? (Y/N)");
                try {
                    String s = in.nextLine();
                    yesOrNot(s);
                    if (s.equals("n")) {
                        playAgain = false;
                    }
                    check = false;
                } catch (YesOrNotException e) {
                    check = true;
                    System.out.println(e.getMessage());
                }
            }
        }
        in.close();
    }

    public static void yesOrNot(String string) {
        String s = string.toLowerCase();
        if (!s.equals("y") && !s.equals("n")) {
            throw new YesOrNotException("You must insert 'Y' or 'N'!");
        }
    }

    public static void choiceCheck(int x) {
        if (x < 1 || x > 9) {
            throw new ChoiceException("You must insert a number in range [1,9]!");
        }
    }

    public static void occupiedCellCheck(String[][] table, int choiceNumber) {
        switch (choiceNumber) {
            case 1:
                if (!table[0][0].equals("-")) {
                    throw new OccupiedCellException("Cell is already occupied!");
                }
                break;
            case 2:
                if (!table[0][1].equals("-")) {
                    throw new OccupiedCellException("Cell is already occupied!");
                }
                break;
            case 3:
                if (!table[0][2].equals("-")) {
                    throw new OccupiedCellException("Cell is already occupied!");
                }
                break;
            case 4:
                if (!table[1][0].equals("-")) {
                    throw new OccupiedCellException("Cell is already occupied!");
                }
                break;
            case 5:
                if (!table[1][1].equals("-")) {
                    throw new OccupiedCellException("Cell is already occupied!");
                }
                break;
            case 6:
                if (!table[1][2].equals("-")) {
                    throw new OccupiedCellException("Cell is already occupied!");
                }
                break;
            case 7:
                if (!table[2][0].equals("-")) {
                    throw new OccupiedCellException("Cell is already occupied!");
                }
                break;
            case 8:
                if (!table[2][1].equals("-")) {
                    throw new OccupiedCellException("Cell is already occupied!");
                }
                break;
            case 9:
                if (!table[2][2].equals("-")) {
                    throw new OccupiedCellException("Cell is already occupied!");
                }
                break;
        }
    }

    public static void choice(String[][] table, int choiceNumber, String badge) {
        switch (choiceNumber) {
            case 1:
                table[0][0] = badge;
                break;
            case 2:
                table[0][1] = badge;
                break;
            case 3:
                table[0][2] = badge;
                break;
            case 4:
                table[1][0] = badge;
                break;
            case 5:
                table[1][1] = badge;
                break;
            case 6:
                table[1][2] = badge;
                break;
            case 7:
                table[2][0] = badge;
                break;
            case 8:
                table[2][1] = badge;
                break;
            case 9:
                table[2][2] = badge;
                break;
        }
    }

    public static boolean winCheck(String[][] table, boolean check, String player) {
        for (int i = 0; i <= 2; i++)
            if (table[i][0].equals(table[i][1]) && table[i][0].equals(table[i][2]) && !table[i][0].equals("-")) {
                check = true;
            }
        for (int j = 0; j <= 2; j++)
            if (table[0][j].equals(table[1][j]) && table[0][j].equals(table[2][j]) && !table[0][j].equals("-")) {
                check = true;
            }
        if (table[0][0].equals(table[1][1]) && table[0][0].equals(table[2][2]) && !table[0][0].equals("-")) {
            check = true;
        }
        if (table[0][2].equals(table[1][1]) && table[0][2].equals(table[2][0]) && !table[0][2].equals("-")) {
            check = true;
        }
        if (check) {
            System.out.println(player + " won!");
        }
        return check;
    }

    public static boolean drawCheck(String[][] table, String badge) {
        int positions = 0;
        for (int i = 0; i < 3; i++) {
            if (table[i][0].equals(badge) || table[i][1].equals(badge) || table[i][2].equals(badge)) {
                positions++;
            }
        }
        for (int j = 0; j < 3; j++) {
            if (table[0][j].equals(badge) || table[1][j].equals(badge) || table[2][j].equals(badge)) {
                positions++;
            }
        }
        if (table[0][0].equals(badge) || table[1][1].equals(badge) || table[2][2].equals(badge)) {
            positions++;
        }
        if (table[0][2].equals(badge) || table[1][1].equals(badge) || table[2][0].equals(badge)) {
            positions++;
        }
        if (positions == 8) {
            return true;
        } else return false;
    }
    
}
