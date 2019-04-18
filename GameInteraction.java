import java.util.Arrays;
import java.util.Scanner;

public class GameInteraction {
	
	public static void main(String[] args) {
		System.out.println("---Loading Please Wait---");
		
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		Game games = new Game();  // generate the boards
		Board gameBoard = new Board("---------");  // create the session game board
		
		System.out.println("---Welcome to Zachary Zampa's Tic Tac Toe Game---");
						

		while (true) {
			int turnCount = 0;  // keep track of whose turn it is; evens player goes
			while (true) {
				// loop until game exits with win or tie
				turnChoose(turnCount, games, gameBoard);
				// see if win / tie
				int check = Game.isWin(gameBoard);
				
				switch (check) {
				case 1: System.out.println("Tie! No winner");
				boardPrint(gameBoard.getBoard());  // print board for user to see
				System.out.println("Would you like to play again y/n");
				if (input.nextLine().equals("n")) {
					System.exit(0);
				} else {
					gameBoard.clear();
				}
				break;
				case 2: System.out.println("Human Wins!");
				boardPrint(gameBoard.getBoard());  // print board for user to see
				System.out.println("Would you like to play again y/n");
				if (input.nextLine().equals("n")) {
					System.exit(0);
				} else {
					gameBoard.clear();
				}
				break;
				case 3: System.out.println("AI Wins!");
				boardPrint(gameBoard.getBoard());  // print board for user to see
				System.out.println("Would you like to play again y/n");
				if (input.nextLine().equals("n")) {
					System.exit(0);
				} else {
					gameBoard.clear();
				}
				break;
				}

				turnCount++;  // add to turn count to next player
			}
		}
	}
	
	/**
	 * Evens - player goes, Odds - AI goes
	 * @param turnCount turn number
	 */
	public static void turnChoose(int turnCount, Game games, Board gameBoard) {
		if (turnCount % 2 == 0) {
			@SuppressWarnings("resource")
			Scanner keyboard = new Scanner(System.in);
			boardPrint(gameBoard.getBoard());  // print board for user to see
			System.out.println("0, 1, 2 \n3, 4, 5 \n6, 7, 8");
			System.out.println("Choose where to play (enter position number 0-8");
			int move;
			int status;
			
			do {
				move = keyboard.nextInt();
				status = Game.setPlayerMove(Game.HPLAY, move, gameBoard);
				if (status == -1) {
					System.out.println("No such position exists");
				} else if (status == 1) {
					System.out.println("Position already occupied");
				}
			} while(status != 0);
			
		} else {
			Game.setPlayerMove(Game.AIPLAY, games.game.getValue(gameBoard), gameBoard);
		}
	}
	
	
	/**
	 * Print the board in session
	 * @param b the Board in use
	 */
	public static void boardPrint(char[] b) {
		String str = Arrays.toString(b);
		System.out.println(str.substring(1, 8) + "\n" + str.substring(10, 17) 
			+ "\n" + str.substring(19, 26));
	}
	
	
}
