package boardgame;

public class Board {
	
	private int rows; //linhas
	private int columns; //colunas
	
	//matriz de pe�as
	private Piece[][] pieces;

	public Board(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns]; //a matriz sera do tamanho de linhas e colunas informadas.
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	

}