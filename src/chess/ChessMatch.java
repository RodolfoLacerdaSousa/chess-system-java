package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch { // regras do jogo

	private Board board;
	private int turn;
	private Color currentPlayer;
	private boolean check; 		//por padrao uma variavel boolean ja comeca com FALSE, nao precisa inicializar ela no construtor.
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8); // cria o tabuleiro 8 por 8
		turn = 1;
		currentPlayer = Color.WHITE;
		
		inicialSetup(); // chama a funcao q inicia a partida
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}

	public ChessPiece[][] getPieces() { // vai retornar uma matriz de pe�as de xadrez correpondentes a essa partida

		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];

		// percorrer a matriz de pe�as do tabulero (Board) e para cada pe�a do
		// tabuleiro, faz 1 Downcasting para ChessPiece
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){ //para imprimir no Program as posi��es possiveis a partir de uma posicao de origem.
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition ) {
		//converter as 2 posicoes para posicoes na matriz
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		
		//conferir se nessa posicao havia 1 pe�a (validar a posicao de origem)
		validateSourcePosition(source);
		//validar se a posicao de destino eh valida em rela��o a posicao de origem
		validateTargetPosition(source, target);
		
		Piece capturedPiece = makeMove(source, target);
		
		//testar se o movimento colocou o proprio jogador em Check, e avisar q n pode fazer essa jogada e desfazer
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check!");
		}
		
		//testar agora se o OPONENTE ficou em check!
		check = (testCheck(opponent(currentPlayer))) ? true : false;
				
		nextTurn(); //para trocar o turno
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source); //remove a pe�a P da posicao de origem
		Piece capturedPiece = board.removePiece(target); //pe�a capturada eh removida
		board.placePiece(p, target); //coloca a pe�a P na posicao de destino
		
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) { //logica para o Check
		Piece p = board.removePiece(target); //tira a pe�a q moveu antes
		board.placePiece(p, source);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) { //se nao existir 1 pe�a na posicao de origem
			throw new ChessException("There is no piece on source position");
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) { //testa a cor da pe�a, se for difernte do jogador atual.
																//significa q eh a pe�a do jogador adversario e assi nao posso move-la
			throw new ChessException("The chosen piece is not yours");
			
		}
		if (!board.piece(position).isThereAnyPossibleMove()) { //testar se existe movimentos 
															//possiveis para a pe�a, se nao tiver lanca a exec�o
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	private void validateTargetPosition(Position source,Position target) { //se eh um movimento possivel em rela��o a posicao de destino
		if(!board.piece(source).possibleMove(target)) { //se para a pe�a de origem a posicao de destino NAO eh um movimento possivel, significa q n posso mexer para la...
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE; //troca de turno
	}
	
	private Color opponent(Color color) { 
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) { //percorre a List para achar o Rei
			if (p instanceof King) { //siginifica q encontrou o Rei
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color+  "King on the board"); //isso NAO eh pra acontecer!
	}
	
	private boolean testCheck(Color color) { // tem q percorrer todas as pe�as adversarias e ver se alguma pode se mover para pegar o REI
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves(); // matriz de movimentos possiveis de cada pe�a adversaria p
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) { //se tiver a posicao do rei na matriz da pe�a, vai retornar true
				return true;				
			}
		}
		return false; //se esgotar o FOR, entao nao estara em Check.
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	private void inicialSetup() { // vai iniciar a partida de xadrez, colocando as pe�as no tabuleiro
		
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
