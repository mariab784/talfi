package vista.vistaGrafica;

import java.awt.Point;

/**
 * Clase que almacena los datos de los estados de los automatas de la interfaz grafica
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class Estado {
	private int x;
	private int y;
	private String etiqueta;
	private boolean seleccionado;
	
	/**
	 * Constructor que establece los datos principales
	 * @param x coordenada x del estado
	 * @param y coordenada y del estado
	 * @param etiqueta nombre del estado
	 */
	public Estado(int x,int y,String etiqueta) {
		this.x=x;
		this.y=y;
		this.etiqueta=etiqueta;
		this.seleccionado=false;
	}
	/**
	 * Método accesro de la coordenada x del estado
	 * @return coordenada x del estado
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Método accesro de la coordenada y del estado
	 * @return coordenada y del estado
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Establece el punto donde esta el estado en la interfaz
	 * @param p punto nuevo donde se situará el estado
	 */
	public void setPoint(Point p){
		this.x=p.x;
		this.y=p.y;
	}
	
	/**
	 * Devuelve el nombre del estado
	 * @return etiqueta nombre del estado
	 */
	public String getEtiqueta(){
		return etiqueta;
	}
	
	/**
	 * Selecciona o deselecciona el estado
	 * @param slect nuevo estado de selecion para el estado
	 */
	public void setSelected(boolean slect){
		seleccionado=slect;
	}
	
	/**
	 * Devuelve si un estado esta seleccionado o no
	 * @return true si está seleccionado, false si no
	 */
	public boolean isSelected(){
		return seleccionado;
	}
}
