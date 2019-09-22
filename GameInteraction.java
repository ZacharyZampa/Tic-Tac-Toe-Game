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

	private static void playLAN(Scanner input, Game games, Board gameBoard) {
		 System.out.println("Enter \"Host\" to host the game and \"Join\" to join one");
		 String choice = "";
		 // loop until choice == "Host" or "Join"
		 do {
			choice = input.nextLine();  // take player input
		 } while (!(choice.equalsIgnoreCase("Host") || choice.equalsIgnoreCase("Join")));

		try {
			if (choice.equalsIgnoreCase("Host")) {
				// Player is hosting
				hostGame(input, games, gameBoard);
			} else {
				// Player is joining
				joinGame(input, games, gameBoard);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
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
				turnChoose(turnCount, games, gameBoard, input, -2);
				// see if win / tie
				int check = Game.isWin(gameBoard);

				// Check end conditions
				checkEndConditions(input, gameBoard, check);

				turnCount++;  // add to turn count to next player
			}
		}
		
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
				turnChoose(turnCount, games, gameBoard, input, -1);
				// see if win / tie
				int check = Game.isWin(gameBoard);
	
				// Enter Final Game Conditions 
				checkEndConditions(input, gameBoard, check);
	
				turnCount++;  // add to turn count to next player
			}
		}
	}

	/**
	 * Allow user to host a LAN game
	 * @param input
	 * @param games
	 * @param gameBoard
	 * @throws Exception
	 */
	private static void hostGame(Scanner input, Game games, Board gameBoard) throws Exception {
		// Game setup
		System.out.println("Please enter the Port Number");
		while (!input.hasNextInt()) {
			String ent = input.next();
			System.out.println(ent + " is not a valid number");
		}
		int portNumber = input.nextInt();
		Online lan = new Online(portNumber);
		System.out.println("Waiting for other player to join...");
		lan.hostServer();
		System.out.println("Connected");
		
		gameLoopLAN(input, games, gameBoard, lan, 0);  // run the actual game loop
	}

	/**
	 * The main loop when hosting a LAN game
	 * @param input
	 * @param games
	 * @param gameBoard
	 * @param lan
	 * @throws Exception
	 */
	private static void gameLoopLAN(Scanner input, Game games, Board gameBoard, Online lan, int side) throws Exception {
		// loop while the game is in session
		while (true) {
			int turnCount = side;  // keep track of whose turn it is; evens player goes
			while (true) {
				// loop until game exits with win or tie
				int move = -3;
				if (turnCount % 2 != 0) {
					// other player's turn
					move = lan.processLANResponse();
					move = turnChoose(turnCount, games, gameBoard, input, move);
				} else {
					move = turnChoose(turnCount, games, gameBoard, input, move);
					lan.sendLANResponse(move);
				}
				
				// see if win / tie
				int check = Game.isWin(gameBoard);
				
	
				// Enter Final Game Conditions 
				checkEndConditions(input, gameBoard, check);
	
				turnCount++;  // add to turn count to next player
			}
		}
	}

	private static void joinGame(Scanner input, Game games, Board gameBoard) {
		// Game setup		
		System.out.println("Please enter the IP Address of the Host");
		String ip = input.nextLine();
		System.out.println("Please enter the Port Number");
		while (!input.hasNextInt()) {
			String ent = input.next();
			System.out.println(ent + " is not a valid number");
		}
		int portNumber = input.nextInt();
		
		Online lan = new Online(portNumber, ip);
				
		try {
			lan.joinServer();  // connect to the host
			System.out.println("Connected");
			
			gameLoopLAN(input, games, gameBoard, lan, 1);  // run the actual game loop
		} catch (Exception ex) {
			System.out.println("Connection to host failed");
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
	 * Figure out who plays next
	 * @param turnCount
	 * @param games
	 * @param gameBoard
	 * @param input 
	 * @param type ; -2 means local human, -1 means AI, anything else is LAN player and type is used as move
	 * @return move position
	 */
	public static int turnChoose(int turnCount, Game games, Board gameBoard, Scanner input, int type) {
		int move = -3;
		if (turnCount % 2 == 0) {
			move = playerMove(gameBoard, input, Game.HPLAY);
		} else if (type == -2) {
			// other player is a human too
			move = playerMove(gameBoard, input, Game.AIPLAY);
		} else if (type == -1) {
			// other player is AI
			move = Game.setPlayerMove(Game.AIPLAY, games.game.getValue(gameBoard), gameBoard);
		} else {
			// other player is over LAN
			move = Game.lanPlayerMove(gameBoard, type, Game.AIPLAY);
		}
		
		return move;
	}
	

	/**
	 * Allows the player to place their move
	 * 
	 * @param gameBoard
	 * @param input
	 * @param player
	 * @return move position
	 */
	private static int playerMove(Board gameBoard, Scanner input, char player) {
		boardPrint(gameBoard.getBoard());  // print board for user to see
		System.out.println("0, 1, 2 \n3, 4, 5 \n6, 7, 8");
		System.out.println("Choose where to play (enter position number 0-8");
		int move;
		int status;

		do {
			while (!input.hasNextInt()) {
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
		
		return move;
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

	private static void checkEndConditions(Scanner input, Board gameBoard, int check) {
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
	}


}
