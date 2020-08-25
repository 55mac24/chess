package chess;

//import java.io.File;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static chess.ChessBoard.*;


public class ChessInput {
    static int isDraw = 2, validInput = 1, promotion = 0, invalid_input = -1;
    public static void main(String[] args) throws IOException {
        ChessBoard board = new ChessBoard();
        Scanner input = new Scanner(System.in);
        board.outputChessBoard();
        int turns = board.getMove();
        boolean valid = true, draw = false;
        File test_case = new File("/Users/msp194/JavaProjects/213/Chess73/src/chess/failed_case_2.txt");
        Scanner data = new Scanner(test_case);

        while (data.hasNextLine()) {

            System.out.println();

            if(turns % 2 == 0){
                System.out.print("White's move: ");
            }else{
                System.out.print("Black's move: ");
            }

            String move = data.nextLine(); //input.nextLine().toLowerCase();
            if ((draw && move.equals("draw")) || (move.equals("resign"))) {
                break;

            } else if (move.equals("draw?")) {
                draw = true;
                // continue;
            } else if (instruction_validity_check(move, board) > invalid_input) {

                if (instruction_validity_check(move, board) == 2) {
                    draw = true;
                }

                String[] move_piece_to = move.split("\\s+");
                String piece = (board.getPieceIndexFromChessLocation(move_piece_to[0])).toString();

                if (piece.equals("invalid")
                        || (piece.charAt(0) == black && turns % 2 == 0)
                        || (piece.charAt(0) == white && turns % 2 == 1)) {
                    System.out.println("Invalid move, try again");

                    continue;
                }


                int isSuccessful = invalidMove;
                if (move_piece_to.length == 2) {
                    System.out.println(move_piece_to[0] + " " + move_piece_to[1]);
                    isSuccessful = board.play_move(move_piece_to[0], move_piece_to[1], "");
                    board.update_moves();
                } else if (move_piece_to.length == 3) {
                    System.out.println(move_piece_to[0] + " " + move_piece_to[1] + " " +move_piece_to[2]);
                    isSuccessful = board.play_move(move_piece_to[0], move_piece_to[1], move_piece_to[2]);
                    board.update_moves();
                }
                System.out.println();
                valid = false;
                if (isSuccessful == invalidMove) {
                    System.out.println("Invalid move, try again");

                }else{
                    valid = true;

                    board.outputChessBoard();
                    turns = board.getMove();

                    if (isSuccessful == check) {
                        System.out.println();
                        System.out.println("Check");
                        valid = true;
                    } else if (isSuccessful == checkmate) {
                        System.out.println();
                        System.out.println("Checkmate");

                        break;
                    } else if (isSuccessful == stalemate) {
                        System.out.println();
                        System.out.println("Stalemate");

                        break;
                    }
                }
            } else {
                System.out.println("Invalid move, try again");

            }
        }
        System.out.println();
        if (board.getMove() % 2 == 1 && valid) {
            System.out.println("White wins");
        } else {
            System.out.println("Black wins");
        }
        //System.out.println();
       // System.out.println("Program broke on line: " + count + " and " + board.getMove() + " moves were played");
    }

    public static int[] convertChessLocationToBoardIndex(String instruction) {
        char alpha = instruction.charAt(0);
        char row_number = instruction.charAt(1);
        int i = 8 - ((int) (row_number - '0'));
        int j = (int) alpha - 97;
        return new int[]{i, j};
    }
    public static boolean isArgValidFormat(String[] checker) {

        if(!(checker.length < 4 && checker.length > 1)) {
            return false;
        }
        if (checker[0].length() == 2 || checker[1].length() == 2) {
            char pp1 = checker[0].charAt(0);
            char pc1 = checker[0].charAt(1);
            char pp2 = checker[1].charAt(0);
            char pc2 = checker[1].charAt(1);
            return (pp1 <= 'h' && pp1 >= 'a') && (pp2 <= 'h' && pp2 >= 'a') && (pc1 < '9' && pc1 > '0') && (pc2 < '9' && pc2 > '0');
        }
        return false;
    }
    public static int instruction_validity_check(String instruction, ChessBoard board) {
        String[] checker = instruction.split("\\s");
        boolean areArgsValid = isArgValidFormat(checker);
        if(areArgsValid){
            if(checker.length == 3) {
                if (checker[2].equals("draw?")) { // 3rd args is draw
                    return isDraw;//draw check
                }else if((checker[2].length() <= 1) && checker[1].charAt(1)=='8'){ //3rd arg may be pawn promote

                    int[] temp = convertChessLocationToBoardIndex(checker[0]);
                    String piece = board.chessBoard[temp[0]][temp[1]].toString();
                    System.out.println("Piece: " + piece);
                    if(piece.charAt(1)=='P'){
                       return promotion;
                    }else if(checker[2].length() == 1){ // not pawn but has 3 args is invalid
                        return invalid_input;
                    }else{
                        return validInput;
                    }
                }else {
                    return invalid_input;
                }
            }else{ // two args formatted correctly
                return validInput;
            }
        }else{
            return invalid_input;
        }
    }

}
