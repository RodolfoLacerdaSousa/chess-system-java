package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {

	public Rook(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() { //onde tiver a position da peça vai aparecer o R de Rook.
		return "R";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		//aboce (acima)
		p.setValues(position.getRow() - 1, position.getColumn()); //position eh a posicao da propria peça q esta na classe Piece
		
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { //enquanto a posicao existir E nao tiver nenhuma peça
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow()-1); //a linha anda 1
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//left
		p.setValues(position.getRow(), position.getColumn() - 1); 
		
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn()-1); // a coluna anda
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//right
		p.setValues(position.getRow(), position.getColumn() + 1); 
		
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn()+1); 
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//below
		p.setValues(position.getRow() + 1, position.getColumn()); 
		
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow()+1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		return mat;
	}
}
