package vista.vistaGrafica;

import java.awt.Point;

import vista.AristaInterface;

public abstract class AristaGeneral implements AristaInterface{

	protected java.awt.geom.Rectangle2D bounds = new java.awt.Rectangle(0, 0);
	
	protected int x;
	protected int y;
	protected int fx;
	protected int fy;
	protected String origen;
	protected String destino;
	
	protected boolean marcada;
	
	public AristaGeneral(String origen,String destino){
		this.origen=origen;
		this.destino=destino;
	}
	
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
	
	/**
	 * M�todo accesro de la caracter�stica de la arista
	 * @return true si la arista esta seleccionada
	 */
	public boolean getMarcada() {
		return marcada;
	}
	
	/**
	 * Marca (o desmarca) la arista
	 * @param marcada si true marca la arista, si false la desmarca
	 */
	public void setMarcada(boolean marcada) {
		this.marcada=marcada;
	}
	
	
	
	/**
	 * M�todo accesor de la coordenada x del estado origen
	 * @return coordenada x del origen
	 */
	public int getX() {
		return x;
	}

	/**
	 * M�todo modificador de la coordenada x del estado origen
	 * @param x nueva coordenada x del origen
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * M�todo accesor de la coordenada y del estado origen
	 * @return coordenada y del origen
	 */
	public int getY() {
		return y;
	}

	/**
	 * M�todo modificador de la coordenada y del estado origen
	 * @param y nueva coordenada y del origen
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * M�todo accesor de la coordenada x del estado destino
	 * @return coordenada x del destino
	 */
	public int getFx() {
		return fx;
	}

	/**
	 * M�todo modificador de la coordenada x del estado destino
	 * @param fx nueva coordenada x del destino
	 */
	public void setFx(int fx) {
		this.fx = fx;
	}

	/**
	 * M�todo accesor de la coordenada x del estado destino
	 * @return coordenada x del destino
	 */
	public int getFy() {
		return fy;
	}

	/**
	 * M�todo modificador de la coordenada y del estado destino
	 * @param fy nueva coordenada y del destino
	 */
	public void setFy(int fy) {
		this.fy = fy;
	}

	/**
	 * M�todo accesor del nombre del estado origen
	 * @return nombre del estado origen
	 */
	public String getOrigen() {
		return origen;
	}
	
	/**
	 * M�todo modificador del nombre del estado origen
	 * @param origen nuevo nombre del origen
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
	}

	/**
	 * M�todo accesor del nombre del estado destino
	 * @return monbre del estado destino
	 */
	public String getDestino() {
		return destino;
	}

	/**
	 * M�todo modificador del nombre del estado destino
	 * @param destino nuevo nombre del destino
	 */
	public void setDestino(String destino) {
		this.destino = destino;
	}


	
	/**
	 * Establece la cercania de la arista a un punto de la interfaz gr�fica
	 * @param p punto del que se quiere saber la distancia a al arista
	 * @return true si est� encima, false si no
	 */
	public boolean estaCerca(Point p){
		if(bounds.contains(p)){
			return true;
		}
		return false;
	}
	
	/**
	 * M�todo accesro de la caracter�stica de la arista
	 * @return true si la arista esta seleccionada
	 */
/*	public boolean getMarcada() {
		return marcada;
	}
	
	/**
	 * Marca (o desmarca) la arista
	 * @param marcada si true marca la arista, si false la desmarca
	 */
/*	public void setMarcada(boolean marcada) {
		this.marcada=marcada;
	}
	
	/**
	 * M�todo que devuelve la arista en formato texto con los nombre de los estados
	 * origen y destino, la etiqueta y si est� maarcada o no
	 * @return cadena con la informaci�n de la arista
	 */
	public String toString() {
		return "d("+origen+")="+destino+"("+marcada+")";
	}
	

	
}
