package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {

	public Rook(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() { //onde tiver a position da pe�a vai aparecer o R de Rook.
		return "R";
	}
	
	
	
}