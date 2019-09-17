package zacharyzampa;
import java.util.Arrays;

public class Board {

	// base board, all empty
	public static final char[] BASEBOARD = {'-', '-', '-', '-', '-', '-', '-', '-', '-'}; 
	public static String emptyBoard = "";
	private char[] board;
	
	/**
	 * Default Board Constructor - sends in empty string
	 */
	public Board() {
		this(emptyBoard);
	}

	/**
	 * Workhorse Board Constructor
	 * @param String b of board  -- converts string to char array to store
	 */
	public Board(String b) {
		char[] barr = b.toCharArray();
		board = barr;
	}
	
	/**
	 * Alternate board constructor
	 * @param arr Array to be used in board
	 */
	public Board(char[] arr) {
		board = arr;
	}
	
	
	
	/* 
	 * Generate Hashcode for a Board Object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 0;
		for (int i = 0; i < board.length; i++) {
		    hash = hash * prime + board[i];
		}
		
		return hash;
	}
	
	/**
	 * See if one board is equal to another board
	 * @param other object to compare to
	 */
	public boolean equals(Object other) {
	      if ((other == null) || (getClass() != other.getClass())) {
	         return false;
	      } else {
	         Board otherB = (Board)other;
	         return Arrays.toString(board).equals(Arrays.toString(otherB.board));
	      }

	   } // end equals
	
	/**
	 * Clear the board in use
	 */
	public void clear() {
		board = Arrays.copyOf(BASEBOARD, 9);
	}
	
	/**
	 * Get the board
	 * @return the board array
	 */
	public char[] getBoard() {
		return board;
	}
	
	/**
	 * Return a position on the board
	 * @param pos position desired
	 * @return character
	 */
	public char getBoardPos(int pos) {
		return board[pos];
	}
	
	/**
	 * Set a position on a board
	 * @param addition what char to add
	 * @param pos where to add
	 */
	public void setBoard(char addition, int pos) {
		board[pos] = addition;
	}
	
	
}
