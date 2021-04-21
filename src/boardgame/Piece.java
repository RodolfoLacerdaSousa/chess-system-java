package boardgame;

public abstract class Piece { //tem 1 metodo abstract entao a classe tem q ser tbm

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
	
	public abstract boolean[][] possibleMoves(); //vai dar verdadeiro onde tiver posicao possivel para a peça ir
	
	
	public boolean possibleMove(Position position) { //apesar desse metodo CONCRETO chamar uma funcao ABSTRACT, 
													//ele vai funcionar pq havera uma funcao concreta dessa abstrata nas peças.
		return possibleMoves()[position.getRow()][position.getColumn()];
	}
	
	
	public boolean isThereAnyPossibleMove() { //para ver se ele esta presa/sem movimentos
		boolean[][] mat = possibleMoves();
		for (int i = 0; i<mat.length; i++) {
			for (int j= 0; j<mat.length;j++) {
				if (mat[i][j]) { //se for verdadeira a posicao, vai retornar True
					return true;
				}
			}
		}
		return false;
	}
	
	
}
