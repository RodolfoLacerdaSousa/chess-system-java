package chess;

import boardgame.Position;

public class ChessPosition {
	
	private char column; //a,b,c...
	private int row;
	
	public ChessPosition(char column, int row) {
		if (column < 'a'|| column > 'h' || row <1 || row > 8) {
			throw new ChessException("Error instantiating ChessPosition . Valid values are from a1 to h8");
		}
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	//protected tem # no diagrama la
	protected Position toPosition() {
		return new Position(8-row, column - 'a'); //linha pega 8 menos a linha da matriz do tabuleiro
													// coluna pega a coluna do tauleiro - 'a'. 
													// Ex> 'a' - 'a' = 0  / Ex> 'c' - 'a' = 2
	}
	//static fica sublinado no diagrama
	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char)('a' + position.getColumn()), 8 - position.getRow());
	}
	
	@Override
	public String toString() {
		return "" + column + row; // esse "" vazio eh para forçar o compilador a aceitar que eh uma compilacao de strings (a1,b3...)
	}
	
}
