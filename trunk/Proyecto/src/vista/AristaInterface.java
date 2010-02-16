package vista;

import java.awt.Point;

public interface AristaInterface {

	/**
	 * Mñtodo accesor de la coordenada x del estado origen
	 * @return coordenada x del origen
	 */
	public int getX();

	/**
	 * Mñtodo modificador de la coordenada x del estado origen
	 * @param x nueva coordenada x del origen
	 */
	public void setX(int x);

	/**
	 * Mñtodo accesor de la coordenada y del estado origen
	 * @return coordenada y del origen
	 */
	public int getY();

	/**
	 * Mñtodo modificador de la coordenada y del estado origen
	 * @param y nueva coordenada y del origen
	 */
	public void setY(int y);

	/**
	 * Mñtodo accesor de la coordenada x del estado destino
	 * @return coordenada x del destino
	 */
	public int getFx();

	/**
	 * Mñtodo modificador de la coordenada x del estado destino
	 * @param fx nueva coordenada x del destino
	 */
	public void setFx(int fx);

	/**
	 * Mñtodo accesor de la coordenada x del estado destino
	 * @return coordenada x del destino
	 */
	public int getFy();

	/**
	 * Mñtodo modificador de la coordenada y del estado destino
	 * @param fy nueva coordenada y del destino
	 */
	public void setFy(int fy);

	/**
	 * Mñtodo accesor del nombre del estado origen
	 * @return nombre del estado origen
	 */
	public String getOrigen();
	
	/**
	 * Mñtodo modificador del nombre del estado origen
	 * @param origen nuevo nombre del origen
	 */
	public void setOrigen(String origen);

	/**
	 * Mñtodo accesor del nombre del estado destino
	 * @return monbre del estado destino
	 */
	public String getDestino();

	/**
	 * Mñtodo modificador del nombre del estado destino
	 * @param destino nuevo nombre del destino
	 */
	public void setDestino(String destino);
	
	/**
	 * Establece la cercania de la arista a un punto de la interfaz grñfica
	 * @param p punto del que se quiere saber la distancia a al arista
	 * @return true si estñ encima, false si no
	 */
	public boolean estaCerca(Point p);
	
	/**
	 * Mñtodo accesro de la caracterñstica de la arista
	 * @return true si la arista esta seleccionada
	 */
	public boolean getMarcada();
	
	/**
	 * Marca (o desmarca) la arista
	 * @param marcada si true marca la arista, si false la desmarca
	 */
	public void setMarcada(boolean marcada);
	
	/**
	 * Mñtodo que devuelve la arista en formato texto con los nombre de los estados
	 * origen y destino, la etiqueta y si estñ maarcada o no
	 * @return cadena con la informaciñn de la arista
	 */
	public String toString();

}
