package zacharyzampa;
import java.util.Arrays;
import java.util.Scanner;

public class GameInteraction {

	public static void main(String[] args) {
		System.out.println("---Loading Please Wait---");
		Game games = new Game();  // generate the boards
		Board gameBoard = new Board("---------");  // create the session game board
		Scanner input = new Scanner(System.in);
		System.out.println("---Welcome to Zachary Zampa's Tic Tac Toe Game---");
		
		short mode = getGameMode(input);
		if (mode == 0) {
			// AI Game
			playAI(input, games, gameBoard);
		} else if (mode == 1) {
			// play against other player on Local Machine
			playLocal(input, games, gameBoard);
		} else {
			// play against other player through LAN
			playLAN(input, games, gameBoard);
		}


	}

	/**
	 * Player plays against another player on the same machine
	 * @param input
	 * @param games
	 * @param gameBoard
	 */
	private static void playLocal(Scanner input, Game games, Board gameBoard) {
		while (true) {
			int turnCount = 0;  // keep track of whose turn it is; evens player goes
			while (true) {
				// loop until game exits with win or tie
				turnChoose(turnCount, games, gameBoard, input, true);
				// see if win / tie
				int check = Game.isWin(gameBoard);

				// Check end conditions
				switch (check) {
				case 1: input.nextLine();  // clear extraneous input
				System.out.println("Tie! No winner");
				boardPrint(gameBoard.getBoard());  // print board for user to see
				System.out.println("Would you like to play again y/n");
				if (input.nextLine().equals("n")) {
					System.exit(0);
				} else {
					gameBoard.clear();
				}
				break;
				case 2: input.nextLine();  // clear extraneous input
				System.out.println("X Wins!");
				boardPrint(gameBoard.getBoard());  // print board for user to see
				System.out.println("Would you like to play again y/n");
				if (input.nextLine().equals("n")) {
					System.exit(0);
				} else {
					gameBoard.clear();
				}
				break;
				case 3: input.nextLine();  // clear extraneous input
				System.out.println("O Wins!");
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

	
	
	private static void playLAN(Scanner input, Game games, Board gameBoard) {
		System.out.println("Under Construction");
	}


	/**
	 * Player plays against an AI player
	 * @param input
	 * @param games
	 * @param gameBoard
	 */
	private static void playAI(Scanner input, Game games, Board gameBoard) {
		
		while (true) {
			int turnCount = 0;  // keep track of whose turn it is; evens player goes
			while (true) {
				// loop until game exits with win or tie
				turnChoose(turnCount, games, gameBoard, input, false);
				// see if win / tie
				int check = Game.isWin(gameBoard);

				// Enter Final Game Conditions 
				switch (check) {
				case 1: input.nextLine();  // clear extraneous input
				System.out.println("Tie! No winner");
				boardPrint(gameBoard.getBoard());  // print board for user to see
				System.out.println("Would you like to play again y/n");
				if (input.nextLine().equals("n")) {
					System.exit(0);
				} else {
					gameBoard.clear();
				}
				break;
				case 2: input.nextLine();  // clear extraneous input
				System.out.println("Human Wins!");
				boardPrint(gameBoard.getBoard());  // print board for user to see
				System.out.println("Would you like to play again y/n");
				if (input.nextLine().equals("n")) {
					System.exit(0);
				} else {
					gameBoard.clear();
				}
				break;
				case 3: input.nextLine();  // clear extraneous input
				System.out.println("AI Wins!");
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
	 * Determine if game will be AI, local or LAN multiplayer
	 * @return 0 if AI, 1 if local, 2 if LAN
	 */
	private static short getGameMode(Scanner keyboard) {
		short resp;
		do {
			System.out.println("Please enter what gamemode you would like\n"
					+ "0 = AI, 1 = Local Multiplayer, 2 = LAN Multiplayer");
			while (!keyboard.hasNextShort()) {
				String ent = keyboard.next();
				System.out.println(ent + " is not a valid number");
			}
			resp = keyboard.nextShort();
		} while (resp < 0 || resp > 2);
		
		keyboard.nextLine();  // flush out extra input
		return resp;
	}

	/**
	 * Evens - player goes, Odds - AI goes
	 * @param turnCount turn number
	 */
	public static void turnChoose(int turnCount, Game games, Board gameBoard, Scanner input, boolean human) {
		if (turnCount % 2 == 0) {
			playerMove(gameBoard, input, Game.HPLAY);
		} else if (human) {
			// other player is a human too
			playerMove(gameBoard, input, Game.AIPLAY);
		} else {
			// other player is AI
			Game.setPlayerMove(Game.AIPLAY, games.game.getValue(gameBoard), gameBoard);
		}
	}

	private static void playerMove(Board gameBoard, Scanner input, char player) {
		boardPrint(gameBoard.getBoard());  // print board for user to see
		System.out.println("0, 1, 2 \n3, 4, 5 \n6, 7, 8");
		System.out.println("Choose where to play (enter position number 0-8");
		int move;
		int status;

		do {
			while (!input.hasNextShort()) {
				String ent = input.next();
				System.out.println(ent + " is not a valid number");
			}
			move = input.nextInt();
			status = Game.setPlayerMove(player, move, gameBoard);
			if (status == -1) {
				System.out.println("No such position exists");
			} else if (status == 1) {
				System.out.println("Position already occupied");
			}
		} while(status != 0);
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
