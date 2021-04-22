package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch { // regras do jogo

	private Board board;
	private int turn;
	private Color currentPlayer;
	private boolean check; 		//por padrao uma variavel boolean ja comeca com FALSE, nao precisa inicializar ela no construtor.
	private boolean checkMate;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8); // cria o tabuleiro 8 por 8
		turn = 1;
		currentPlayer = Color.WHITE;
		
		initialSetup(); // chama a funcao q inicia a partida
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
	
	public boolean getCheckMate() {
		return checkMate;
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
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){ //para imprimir no Program as posições possiveis a partir de uma posicao de origem.
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition ) {
		//converter as 2 posicoes para posicoes na matriz
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		
		//conferir se nessa posicao havia 1 peça (validar a posicao de origem)
		validateSourcePosition(source);
		//validar se a posicao de destino eh valida em relação a posicao de origem
		validateTargetPosition(source, target);
		
		Piece capturedPiece = makeMove(source, target);
		
		//testar se o movimento colocou o proprio jogador em Check, e avisar q n pode fazer essa jogada e desfazer
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check!");
		}
		
		//testar agora se o OPONENTE ficou em check!
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
		nextTurn(); //para trocar o turno
		}
		
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source); //remove a peça P da posicao de origem
		p.increaseMoveCount();
				
		Piece capturedPiece = board.removePiece(target); //peça capturada eh removida
		board.placePiece(p, target); //coloca a peça P na posicao de destino
		
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		// #specialMove castling Menor
		if (p instanceof King && target.getColumn() == source.getColumn() +2) {  //identifica q o king andou 2 pra o lado>
			Position sourceT = new Position(source.getRow(), source.getColumn()+3); // e move a torre ROOK
			Position targetT = new Position(source.getRow(), source.getColumn()+1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		// #specialMove castling Maior
		if (p instanceof King && target.getColumn() == source.getColumn() -2) {
			Position sourceT = new Position(source.getRow(), source.getColumn()-4); 
			Position targetT = new Position(source.getRow(), source.getColumn()-1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) { //logica para o Check
		ChessPiece p = (ChessPiece)board.removePiece(target); //tira a peça q moveu antes
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
		// #specialMove castling Menor
		if (p instanceof King && target.getColumn() == source.getColumn() +2) {  //identifica q o king andou 2 pra o lado>
			Position sourceT = new Position(source.getRow(), source.getColumn()+3); // e move a torre ROOK
			Position targetT = new Position(source.getRow(), source.getColumn()+1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		// #specialMove castling Maior
		if (p instanceof King && target.getColumn() == source.getColumn() -2) {
			Position sourceT = new Position(source.getRow(), source.getColumn()-4); 
			Position targetT = new Position(source.getRow(), source.getColumn()-1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
	}
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) { //se nao existir 1 peça na posicao de origem
			throw new ChessException("There is no piece on source position");
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) { //testa a cor da peça, se for difernte do jogador atual.
																//significa q eh a peça do jogador adversario e assi nao posso move-la
			throw new ChessException("The chosen piece is not yours");
			
		}
		if (!board.piece(position).isThereAnyPossibleMove()) { //testar se existe movimentos 
															//possiveis para a peça, se nao tiver lanca a execão
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	private void validateTargetPosition(Position source,Position target) { //se eh um movimento possivel em relação a posicao de destino
		if(!board.piece(source).possibleMove(target)) { //se para a peça de origem a posicao de destino NAO eh um movimento possivel, significa q n posso mexer para la...
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
	
	private boolean testCheck(Color color) { // tem q percorrer todas as peças adversarias e ver se alguma pode se mover para pegar o REI
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves(); // matriz de movimentos possiveis de cada peça adversaria p
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) { //se tiver a posicao do rei na matriz da peça, vai retornar true
				return true;				
			}
		}
		return false; //se esgotar o FOR, entao nao estara em Check.
	}
	
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) { //se nao tiver em check, nao estara em check mate
			return false;
		}
		List<Piece> list =  piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) { //se alguma peça pode salvar do check mate
			boolean[][] mat = p.possibleMoves();
			for (int i =0; i<board.getRows();i++) {
				for(int j=0; j<board.getColumns();j++) {
					if(mat[i][j]) { //entra se for um movimento possivel (Faz o movimento para testar e desfaz)
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if(!testCheck) { //se nao ficou mais em check com o movimento simulado, entao nao esta em check Mate
							return false;
						}
					}
				}
			}
			
		}
		return true;
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	private void initialSetup() { // vai iniciar a partida de xadrez, colocando as peças no tabuleiro
		
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE));
		
		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
		
	}
}