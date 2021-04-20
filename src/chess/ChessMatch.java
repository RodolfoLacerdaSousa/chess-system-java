package chess;

import boardgame.Board;

public class ChessMatch { //regras do jogo
	
	private Board board;

	
	public ChessMatch() {
		board = new Board(8, 8);
	}
	
	public ChessPiece[][] getPieces(){ //vai retornar uma matriz de peças de xadrez correpondentes a essa partida
		
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		
		// percorrer a matriz de peças do tabulero (Board) e para cada peça do tabuleiro, faz 1 Downcasting para ChessPiece
		for (int i =0; i<board.getRows(); i++) {
			for (int j = 0; j<board.getColumns();j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}

}
