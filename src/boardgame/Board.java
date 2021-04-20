package boardgame;

public class Board {
	
	private int rows; //linhas
	private int columns; //colunas
	
	//matriz de peças
	private Piece[][] pieces;

	public Board(int rows, int columns) {
		if(rows < 1 || columns<1 ) { //PROGRAMACAO DEFENSIVA - nao pode criar 1 tabuleiro menor q 1/1
			throw new BoardExcepition("Error creating board: there must be at least 1 row and 1 column");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns]; //a matriz sera do tamanho de linhas e colunas informadas.
	}

	public int getRows() {
		return rows;
	}

//retirou setRows e setColumns para nao deixar alterar a quantidade de linhas e colunas
 
	public int getColumns() {
		return columns;
	}


	
	public Piece piece(int row, int column) {
		if (!positionExists(row, column)) {//PROGRAMACAO DEFENSIVA - se essa posicao NAO EXISTIR
			throw new BoardExcepition("Position not on the board!");
		}
		return pieces[row][column];
		
	}
	
	public Piece piece(Position position) {
		if (!positionExists(position)) {//PROGRAMACAO DEFENSIVA - se essa posicao NAO EXISTIR
			throw new BoardExcepition("Position not on the board!");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
			//PROGRAMACAO DEFENSIVA - antes de colocar a peça na posicao tem q testar se ja existe uma peça nessa posicao
		if (thereIsAPiece(position)) {
			throw new BoardExcepition("There is already a piece on position " + position);
		}
		
		pieces[position.getRow()][position.getColumn()] = piece; //colocar a Peça na posicao do tabuleiro
		piece.position = position; //a posicao da peça agora eh position.
					//lembrando q Position eh PROTECTED e por isso consegue acessar por aqui assim.
	}
	
	public Piece removePiece(Position position) {
		if (!positionExists(position)) { //se nao existir a posicao
			throw new BoardExcepition("Position not on the board!");
		}
		if (piece(position) == null) {
			return null;
		}
		Piece aux = piece(position);
		aux.position = null;
		pieces[position.getRow()][position.getColumn()] = null;
		return aux;
	}
	
	private boolean positionExists(int row, int column) { //condicao para saber se existe a posicao recebendo linha e coluna
		return row >= 0 && row < rows && column >=0 && column < columns;
	}
	
	public boolean positionExists(Position position) {//condicao para saber se existe a posicao recebendo o Position
		return positionExists(position.getRow(), position.getColumn());
	}
	
	public boolean thereIsAPiece(Position position) {
		if (!positionExists(position)) {	//PROGRAMACAO DEFENSIVA - Antes de testar o thereIsAPiece, ele testa se a Posicao existe
			throw new BoardExcepition("Position not on the board!");
		}
		
		return piece(position) != null; //se for difrente de nulo siginifica q tem 1 peça nessa posicao
	}
	
	

}
