package vista.vistaGrafica;

public abstract class AristaGeneral {

	protected java.awt.geom.Rectangle2D bounds = new java.awt.Rectangle(0, 0);
	
	protected int x;
	protected int y;
	protected int fx;
	protected int fy;
	protected String origen;
	protected String destino;
	
	protected boolean marcada;
	
	public AristaGeneral(int x,int y,int fx, int fy,String origen,String destino){
		
		this.x=x;
		this.y=y;
		this.fx=fx;
		this.fy=fy;
		this.origen=origen;
		this.destino=destino;
		//this.etiqueta=etiqueta;
		marcada=false;
	}
	
/*	public String getEtiqueta() {
		return etiqueta;
	}*/
	
	/**
	 * Mñtodo accesro de la caracterñstica de la arista
	 * @return true si la arista esta seleccionada
	 */
/*	public boolean getMarcada() {
		return marcada;
	}*/
	
	/**
	 * Marca (o desmarca) la arista
	 * @param marcada si true marca la arista, si false la desmarca
	 */
/*	public void setMarcada(boolean marcada) {
		this.marcada=marcada;
	}*/
}
