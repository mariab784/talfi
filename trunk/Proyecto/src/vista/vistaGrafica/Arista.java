package vista.vistaGrafica;

import java.awt.Point;

/**
 * La clase arista define los parámetros de las aristas de la interfaz gráfica
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class Arista extends AristaGeneral {
	
	protected java.awt.geom.Rectangle2D bounds = new java.awt.Rectangle(0, 0);
	
	private int x;
	private int y;
	private int fx;
	private int fy;
	String origen;
	String destino;
//	String etiqueta;
//	boolean marcada;
	
	/**
	 * Establece los datos iniciales de la arista en la interfaz gráfica
	 * @param x coordenada x del estado de inicio
	 * @param y coordenada y del estado de inicio
	 * @param fx coordenada x del estado destion
	 * @param fy coordenada y del estado destino
	 * @param etiqueta etiqueta de la arista
	 * @param origen nombre del estado de origen
	 * @param destino nombre del estado de destino
	 */
	public Arista(int x,int y,int fx, int fy,String etiqueta,String origen,String destino) {
		super(etiqueta);
		this.x=x;
		this.y=y;
		this.fx=fx;
		this.fy=fy;
//		this.etiqueta=etiqueta;
		this.origen=origen;
		this.destino=destino;
//		marcada=false;
	}

	/**
	 * Método accesor de la coordenada x del estado origen
	 * @return coordenada x del origen
	 */
	public int getX() {
		return x;
	}

	/**
	 * Método modificador de la coordenada x del estado origen
	 * @param x nueva coordenada x del origen
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Método accesor de la coordenada y del estado origen
	 * @return coordenada y del origen
	 */
	public int getY() {
		return y;
	}

	/**
	 * Método modificador de la coordenada y del estado origen
	 * @param y nueva coordenada y del origen
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Método accesor de la coordenada x del estado destino
	 * @return coordenada x del destino
	 */
	public int getFx() {
		return fx;
	}

	/**
	 * Método modificador de la coordenada x del estado destino
	 * @param fx nueva coordenada x del destino
	 */
	public void setFx(int fx) {
		this.fx = fx;
	}

	/**
	 * Método accesor de la coordenada x del estado destino
	 * @return coordenada x del destino
	 */
	public int getFy() {
		return fy;
	}

	/**
	 * Método modificador de la coordenada y del estado destino
	 * @param fy nueva coordenada y del destino
	 */
	public void setFy(int fy) {
		this.fy = fy;
	}

	/**
	 * Método accesor del nombre del estado origen
	 * @return nombre del estado origen
	 */
	public String getOrigen() {
		return origen;
	}
	
	/**
	 * Método modificador del nombre del estado origen
	 * @param origen nuevo nombre del origen
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
	}

	/**
	 * Método accesor del nombre del estado destino
	 * @return monbre del estado destino
	 */
	public String getDestino() {
		return destino;
	}

	/**
	 * Método modificador del nombre del estado destino
	 * @param destino nuevo nombre del destino
	 */
	public void setDestino(String destino) {
		this.destino = destino;
	}

	
	/**
	 * Método accesor de la etiqueta de la arista
	 * @return nombre de la arista(letra que realiza la transicion)
	 */
	public String getEtiqueta() {
		return etiqueta;
	}

	/**
	 * Método midificador de la etiqueta de la arista
	 * @param etiqueta nueva etiqueta de la atista
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	/**
	 * Establece la cercania de la arista a un punto de la interfaz gráfica
	 * @param p punto del que se quiere saber la distancia a al arista
	 * @return true si está encima, false si no
	 */
	public boolean estaCerca(Point p){
		if(bounds.contains(p)){
			return true;
		}
		return false;
	}
	
	/**
	 * Método accesro de la característica de la arista
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
	 * Método que devuelve la arista en formato texto con los nombre de los estados
	 * origen y destino, la etiqueta y si está maarcada o no
	 * @return cadena con la información de la arista
	 */
	public String toString() {
		return "d("+origen+","+etiqueta+")="+destino+"("+marcada+")";
	}
	
}
