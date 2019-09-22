package zacharyzampa;
import java.util.Arrays;
import java.util.Random;

public class Game {

	public HashedDictionary<Board, Integer> game; 
	public static final int BOARD_SIZE = 9;
	public static final char HPLAY = 'x';
	public static final char AIPLAY = 'o';
	public static final char EMPTY = '-';
	public static final String EMPTY_BOARD = "";


	/**
	 * Check if winner exists
	 * @param b Board in use
	 * @return 0 if no winner yet, 1 if tie, 2 if Human Win, 3 if AI Win
	 */
	public static int isWin(Board b) {
		// check rows
		for (int i = 0; i <= 6; i += 3) {
			if (b.getBoardPos(i) == HPLAY && b.getBoardPos(i + 1) == HPLAY && b.getBoardPos(i + 2) == HPLAY) {
				// human win
				return 2;
			} else if (b.getBoardPos(i) == AIPLAY && b.getBoardPos(i + 1) == AIPLAY && b.getBoardPos(i + 2) == AIPLAY) {
				// AI Win
				return 3;
			}
		}

		// check columns
		for (int i = 0; i <= 2; i++) {
			if (b.getBoardPos(i) == HPLAY && b.getBoardPos(i + 3) == HPLAY && b.getBoardPos(i + 6) == HPLAY) {
				// human win
				return 2;
			} else if (b.getBoardPos(i) == AIPLAY && b.getBoardPos(i + 3) == AIPLAY && b.getBoardPos(i + 6) == AIPLAY) {
				// AI Win
				return 3;
			}
		}

		// check diagonal
		if ((b.getBoardPos(0) == HPLAY && b.getBoardPos(4) == HPLAY && b.getBoardPos(8) == HPLAY)
				|| (b.getBoardPos(2) == HPLAY && b.getBoardPos(4) == HPLAY && b.getBoardPos(6) == HPLAY)) {
			// human win
			return 2;
		} else if ((b.getBoardPos(0) == AIPLAY && b.getBoardPos(4) == AIPLAY && b.getBoardPos(8) == AIPLAY)
				|| (b.getBoardPos(2) == AIPLAY && b.getBoardPos(4) == AIPLAY && b.getBoardPos(6) == AIPLAY)) {
			// AI win
			return 3;
		}

		// check if tie exist
		for (int i = 0; i < BOARD_SIZE; i++) {
			if (b.getBoardPos(i) == EMPTY) {
				// empty spots still exist
				return 0;
			}
		}

		// else, all spots are taken, and no winner, this is tie
		return 1;
	}

	/**
	 * Possible way to call getBestMove; converts to other
	 * @param board String of board
	 * @return best move position
	 */
	public int getBestMove(String board) {
		Board b = new Board("|||||||||");
		for (int i = 0; i < 9; i++) {
			b.setBoard(Character.toLowerCase(board.charAt(i)), i);
		}

		return getBestMove(b);
	}

	/**
	 * Steps to Win for AI
	 * 1. Can I win
	 * 2. Can they win
	 * 3. If no to 1 or 2 then go Corner -> Middle -> Side
	 * 
	 * @param board board object
	 * @return best move position
	 */
	public int getBestMove(Board b) {
		// if possible win the game -- if not, return -1
		int move = getWinMove(b);

		// block player from winning
		if (move == -1) {
			move = getBlockMove(b);
		}
		
		// check if player played in middle on first move; if so go to corner
		if (move == -1) {
			if (b.equals(new Board(new char[] {'-', '-', '-', '-', 'x', '-', '-', '-', '-'}))) {
				move = getCornerMove(b);
			}
		}
		
		// check if player played in corner on first move, if so go to middle
		if (move == -1) {
			if (b.equals(new Board(new char[] {'x', '-', '-', '-', '-', '-', '-', '-', '-'})) ||
					b.equals(new Board(new char[] {'-', '-', 'x', '-', '-', '-', '-', '-', '-'})) ||
					b.equals(new Board(new char[] {'-', '-', '-', '-', '-', '-', '-', '-', 'x'})) ||
					b.equals(new Board(new char[] {'-', '-', '-', '-', '-', '-', 'x', '-', '-'}))) {
				move = getCenterMove(b);
			}
		}
		
		// choose somewhere to go
		if (move == -1) {
			move = getRandMove(b);
		}
		// return the spot it can place at
		return move;
	}

	/**
	 * AI attempt to win
	 * @param b Board in use
	 * @return move position or -1 if not found
	 */
	public int getWinMove(Board b) {
		int move = -1;  // if no move can be done

		for (int i = 0; i < BOARD_SIZE; i++) {
			if (b.getBoardPos(i) == EMPTY) {
				// this spot is not taken
				b.setBoard(AIPLAY, i);  // AI plays here

				int stat = isWin(b);
				if (stat == 3) {
					// check if winner; if one does, game exits
					move = i;
				} else {
					// revoke move; this is not winning, move somewhere else
				}
				b.setBoard(EMPTY, i);

			}
		}

		// return the spot it can place at
		return move; 
	}

	/**
	 * AI attempts to block player
	 * @param b board in use
	 * @return position to move to or -1 if none found
	 */
	public int getBlockMove(Board b) {
		int move = -1;  // if no move can be done

		for (int i = 0; i < BOARD_SIZE; i++) {
			if (b.getBoardPos(i) == EMPTY) {
				// empty spot
				b.setBoard(HPLAY, i);  // temp set to human
				if (isWin(b) == 2) {
					b.setBoard(AIPLAY, i);
					move = i;
					b.setBoard(EMPTY, i);
					break;
				}
				else {
					// set back to empty
					b.setBoard(EMPTY, i);
				}
			}
		}


		// return the spot it can place at
		return move;
	}

	/**
	 * AI just plays a random move since no ideal move found
	 * @param b board in use
	 * @return position to move to
	 */
	public int getRandMove(Board b) {
		int move;

		// search for a spot, randomly until it reaches one that it can place at
		Random rand = new Random();
		int breaker = 0;  // emergency break if full board
		do {
			move = rand.nextInt(BOARD_SIZE);
			if (breaker++ >= 9) break;
		} while (b.getBoardPos(move) == AIPLAY || b.getBoardPos(move) == HPLAY);

		// return the spot it can place at
		return move;
	}

	/**
	 * AI attempts to place a piece at the center of the board
	 * @param b board in use
	 * @return position to move to
	 */
	public int getCenterMove(Board b) {
		int move = -1;  // returns -1 if cannot move here
		
		if (b.getBoardPos(4) == '-') {
			// center is empty; move to pos 4 (center)
			move = 4;
		}
		
		return move;
	}
	
	/**
	 * AI attempts to place a piece at a corner of the board
	 * @param b board in use
	 * @return position to move to
	 */
	public int getCornerMove(Board b) {
		int move = -1;  // returns -1 if cannot move here
		
		if (b.getBoardPos(0) == '-') {
			// center is empty; move to pos 0 (top left)
			move = 0;
		} else if (b.getBoardPos(2) == '-') {
			// center is empty; move to pos 2 (top right)
			move = 2;
		} else if (b.getBoardPos(6) == '-') {
			// center is empty; move to pos 6 (bottom left)
			move = 6;
		} else if (b.getBoardPos(8) == '-') {
			// center is empty; move to pos 8 (bottom right)
			move = 8;
		}
		
		return move;
	}
	

	/**
	 * Set player's move
	 * @param player
	 * @param move - spot to enter
	 * @param b Game board
	 * @return int; 0 if placed; 1 if occupied spot; -1 if outside range
	 */
	public static int setPlayerMove(char player, int move, Board b) {
		if (!(move >= 0 && move <= 8)) {
			// outside range
			return -1;
		} else if (b.getBoardPos(move) != EMPTY) {
			// filled spot
			return 1;
		}
		b.setBoard(player, move);
		return 0;
	}


	/**
	 * Game constructor Creates the boards for tic tac toe
	 */
	public Game() {
		game = new HashedDictionary<Board, Integer>();
		boardGenerator();
	}

	/**
	 * Generate the board
	 */
	public void boardGenerator() {
		String str = "|||||||||";
		// construct board
		char[] bd = new char[9];
		for (int i = 0; i < str.length(); i++) {
			bd[i] = str.charAt(i);
		}
		boardGenerator(bd);
	}

	private void boardGenerator(char[] brd) {
		int left = -1;  // how many spots are left in the board untouched

		// cycle through the board looking at everything
		for (int i = 0; i < brd.length; i++) {
			if (brd[i] == '|') {
				left = i;
				// this is an empty spot break loop and modify
				break;
			}
		}
		// base case -- a full board
		if (left == -1) {
			// no more spots left to modify
			if (isValid(brd)) {
				// board is a valid board (i.e. not all x's or o's)
				Board b = new Board(brd);  // turn finished board to Board object
				game.add(b, getBestMove(b));  // add to dictionary
			}
		} else {
			boardGenerator(copyBoard(left, brd, 'x'));  // add x's in the left spots
			boardGenerator(copyBoard(left, brd, 'o'));  // add o's in the left spots
			boardGenerator(copyBoard(left, brd, '-'));  // add emptys in the left spots
		}
	}


	/**
	 * Copy the board and add to the spot in question
	 * @param left spot to add to 
	 * @param brd board in use
	 * @param c character to add
	 * @return the modified and copied board
	 */
	private char[] copyBoard(int left, char[] brd, char c) {
		char[] newBrd = Arrays.copyOf(brd, 9);
		newBrd[left] = c;
		return newBrd;
	}

	/**
	 * Helper method to check if the board is valid
	 * @param brd boad in question
	 * @return true if valid board
	 */
	private boolean isValid(char[] brd) {
		String str = "";
		for (int i = 0; i < brd.length; i++) {
			str += brd[i];
		}

		return isValid(str);		
	}

	/**
	 * Check if the created board is valid
	 * @param str board being made
	 * @return true if there is one more x than o (x always goes first so must always be one more)
	 */
	public boolean isValid(String str) {
		int x = 0;
		int o = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == 'x') {
				// x is in the position add to count
				x++;
			} else if (str.charAt(i) == 'o') {
				// o is in the position add to the count
				o++;
			}
		}

		return x == (o + 1) || x == o;
	}

	/**
	 * Enter where the lan player played; Trust that already validated on LAN End
	 * @param gameBoard
	 * @param move
	 * @param playerPiece
	 * @return move position
	 */
	public static int lanPlayerMove(Board b, int move, char playerPiece) {
		b.setBoard(playerPiece, move);
		return move;
	}

}
