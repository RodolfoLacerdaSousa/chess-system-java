package boardgame;

public class Piece {

	protected Position position; //protected pq nao quer q seja visto no program
	private Board board;
	
	
	public Piece(Board board) {
		this.board = board;
		position = null; //o construtor a position da peça inicial eh Null
	}


	protected Board getBoard() { //o get esta Protected para so ser acessado por classes do mesmo pacote ou sub classes
		return board;
	}

	//setBoard foi tirado pq nao quer permitir que o tabuleiro seja alterado.
	
	
	
	
}
