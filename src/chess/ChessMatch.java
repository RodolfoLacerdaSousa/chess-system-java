package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch { // regras do jogo

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8); // cria o tabuleiro 8 por 8
		inicialSetup(); // chama a funcao q inicia a partida
	}

	public ChessPiece[][] getPieces() { // vai retornar uma matriz de peças de xadrez correpondentes a essa partida

		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];

		// percorrer a matriz de peças do tabulero (Board) e para cada peça do
		// tabuleiro, faz 1 Downcasting para ChessPiece
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition ) {
		//converter as 2 posicoes para posicoes na matriz
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		
		//conferir se nessa posicao havia 1 peça (validar a posicao de origem)
		validateSourcePosition(source);
		
		Piece capturedPiece = makeMove(source, target);
		
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source); //remove a peça P da posicao de origem
		Piece captredPiece = board.removePiece(target); //peça capturada eh removida
		
		board.placePiece(p, target); //coloca a peça P na posicao de destino
		return captredPiece;
	}
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) { //se nao existir 1 peça na posicao de origem
			throw new ChessException("There is no piece on source position");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) //testar se existe movimentos 
															//possiveis para a peça, se nao tiver lanca a execão
			throw new ChessException("There is no possible moves for the chosen piece");
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}

	private void inicialSetup() { // vai iniciar a partida de xadrez, colocando as peças no tabuleiro
		
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));

		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
	}

}
