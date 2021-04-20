package chess;

import boardgame.Board;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch { //regras do jogo
	
	private Board board;

	
	public ChessMatch() {
		board = new Board(8, 8); //cria o tabuleiro 8 por 8
		inicialSetup(); // chama a funcao q inicia a partida
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
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	
	private void inicialSetup() { //vai iniciar a partida de xadrez, colocando as peças no tabuleiro
		placeNewPiece('b', 6, new Rook(board, Color.WHITE));
		placeNewPiece('e', 8, new King(board, Color.BLACK));
		placeNewPiece('e', 1, new King(board, Color.WHITE));
	}

}
