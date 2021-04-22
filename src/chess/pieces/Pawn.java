package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece{
	
	
	
	public Pawn(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() { //onde tiver a position da peça vai aparecer o P de Pawn (Peao).
		return "P";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);
		
		//white pawn
		if (getColor() == Color.WHITE) {
			p.setValues(position.getRow()-1, position.getColumn()); //movimento normal para frente, andando 1
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow()-2, position.getColumn());
			Position p2 = new Position(position.getRow()-1, position.getColumn()); //movimento incial q pode andar 2 casa, ai testa se tem algo na primeira ou na segunda.
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount()==0) {
				mat[p.getRow()][p.getColumn()] = true;						
			}
			p.setValues(position.getRow()-1, position.getColumn()-1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) { //para comer uma peça na diagonal
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow()-1, position.getColumn()+1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) { //para comer uma peça na diagonal
				mat[p.getRow()][p.getColumn()] = true;
			}
			
		}
		else { //black pawn
			p.setValues(position.getRow()+1, position.getColumn()); //movimento normal para frente, andando 1
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow()+2, position.getColumn());
			Position p2 = new Position(position.getRow()+1, position.getColumn()); //movimento incial q pode andar 2 casa, ai testa se tem algo na primeira ou na segunda.
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount()==0) {
				mat[p.getRow()][p.getColumn()] = true;						
			}
			p.setValues(position.getRow()+1, position.getColumn()-1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) { //para comer uma peça na diagonal
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow()+1, position.getColumn()+1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) { //para comer uma peça na diagonal
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		return mat;
	}
	
	
	

}
