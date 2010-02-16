package vista.vistaGrafica;

import java.awt.Point;
import java.util.ArrayList;

/**
 * La clase arista define los parñmetros de las aristas para AP de la interfaz grñfica
 *  @author Rocño Barrigñete, Mario Huete, Luis San Juan
 *
 */
public class AristaAP {
	
	protected java.awt.geom.Rectangle2D bounds = new java.awt.Rectangle(0, 0);
	
	private int x;
	private int y;
	private int fx;
	private int fy;
	String origen;
	String destino;
	ArrayList<String> entradaSimbolos;
	String cimaPila;
	ArrayList<String> salidaPila;
	boolean marcado;

	
	/**
	 * Establece los datos iniciales de la arista en la interfaz grñfica
	 * @param x coordenada x del estado de inicio
	 * @param y coordenada y del estado de inicio
	 * @param fx coordenada x del estado destion
	 * @param fy coordenada y del estado destino
	 * @param origen nombre del estado de origen
	 * @param destino nombre del estado de destino
	 */
	public AristaAP(int x,int y,int fx, int fy,String origen,String destino) {
		this.x=x;
		this.y=y;
		this.fx=fx;
		this.fy=fy;
		this.entradaSimbolos = new ArrayList</*AristaGeneral*/String>();
		this.salidaPila = new ArrayList<String>();
		this.marcado = false;
		this.origen=origen;
		this.destino=destino;
	}

	/**
	 * Mñtodo accesor de la lista de simbolos de entrada marcados
	 * @return lista de simbolos de entrada entradaSimbolos
	 */
	
	public boolean getMarcados(){ return marcado;}
	
	/**
	 * Mñtodo modificador de una posicion de la lista de simbolos de entrada marcados
	 * @param b valor de marcado
	 */
	
	public void setMarcados(boolean b){ marcado = b;}
	
	/**
	 * Mñtodo accesor de la lista de simbolos de entrada
	 * @return lista de simbolos de entrada entradaSimbolos
	 */
	
	public ArrayList</*AristaGeneral*/String> getEntradaSimbolos(){ return entradaSimbolos;}
	
	
	/**
	 * Mñtodo que añade nuevo simbolo de entrada
	 * @param s simbolo de entrada
	 */
	
	public void anadirSimbolo(String s){ 
		
		//AristaGeneral a = new AristaGeneral(s);
		if (!entradaSimbolos.contains(/*a*/s))
			entradaSimbolos.add(/*a*/s);
	
	}

	
	/**
	 * Mñtodo que añade nuevo simbolo de pila
	 * @param s simbolo de pila
	 */
	
	public void anadirPila(String s){ 
	
		if (!salidaPila.contains(s))
			salidaPila.add(s);
		
	}
	
	
	/**
	 * Mñtodo accesor de la cima de la pila
	 * @return cima de la pila cimaPila
	 */
	
	public ArrayList<String> getSalidaPila(){ return salidaPila;}
	
	/**
	 * Mñtodo accesor de la cima de la pila
	 * @return cima de la pila cimaPila
	 */
	
	public String getCimaPila(){ return cimaPila;}
	
	/**
	 * Mñtodo modificador de la cima de la pila
	 * @param s cima de la pila cimaPila
	 */
	

	public void setCimaPila(String s) {
		this.cimaPila = s;
	}
	
	/**
	 * Mñtodo modificador de la cima de la pila
	 * @param x nueva coordenada x del origen
	 */
	
	public int getX() {
		return x;
	}

	/**
	 * Mñtodo modificador de la coordenada x del estado origen
	 * @param x nueva coordenada x del origen
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Mñtodo accesor de la coordenada y del estado origen
	 * @return coordenada y del origen
	 */
	public int getY() {
		return y;
	}

	/**
	 * Mñtodo modificador de la coordenada y del estado origen
	 * @param y nueva coordenada y del origen
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Mñtodo accesor de la coordenada x del estado destino
	 * @return coordenada x del destino
	 */
	public int getFx() {
		return fx;
	}

	/**
	 * Mñtodo modificador de la coordenada x del estado destino
	 * @param fx nueva coordenada x del destino
	 */
	public void setFx(int fx) {
		this.fx = fx;
	}

	/**
	 * Mñtodo accesor de la coordenada x del estado destino
	 * @return coordenada x del destino
	 */
	public int getFy() {
		return fy;
	}

	/**
	 * Mñtodo modificador de la coordenada y del estado destino
	 * @param fy nueva coordenada y del destino
	 */
	public void setFy(int fy) {
		this.fy = fy;
	}

	/**
	 * Mñtodo accesor del nombre del estado origen
	 * @return nombre del estado origen
	 */
	public String getOrigen() {
		return origen;
	}
	
	/**
	 * Mñtodo modificador del nombre del estado origen
	 * @param origen nuevo nombre del origen
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
	}

	/**
	 * Mñtodo accesor del nombre del estado destino
	 * @return monbre del estado destino
	 */
	public String getDestino() {
		return destino;
	}

	/**
	 * Mñtodo modificador del nombre del estado destino
	 * @param destino nuevo nombre del destino
	 */
	public void setDestino(String destino) {
		this.destino = destino;
	}

	
	/**
	 * Mñtodo accesor de la etiqueta de la arista
	 * @return nombre de la arista(letra que realiza la transicion)
	 */
/*	public String getEtiqueta() {
		return etiqueta;
	}*/

	/**
	 * Mñtodo midificador de la etiqueta de la arista
	 * @param etiqueta nueva etiqueta de la atista
	 */
/*	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}*/
	
	/**
	 * Establece la cercania de la arista a un punto de la interfaz grñfica
	 * @param p punto del que se quiere saber la distancia a al arista
	 * @return true si estñ encima, false si no
	 */
	public boolean estaCerca(Point p){
		if(bounds.contains(p)){
			return true;
		}
		return false;
	}
	
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
	
	/**
	 * Mñtodo que devuelve la arista en formato texto con los nombre de los estados
	 * origen y destino, la etiqueta y si estñ maarcada o no
	 * @return cadena con la informaciñn de la arista
	 */
	public String toString() {
		return "d("+origen+","+entradaSimbolos+","+cimaPila+")=("+destino+","+salidaPila+")";//+"("+marcada+")";
	}
	
	public void setSimbolos(ArrayList<String> simbolos) {
		// TODO Auto-generated method stub
		this.entradaSimbolos = simbolos;
	}

	public void setSalida(ArrayList<String> salida) {
		// TODO Auto-generated method stub
		this.salidaPila = salida;
	}
	 public boolean contieneOrigen(String estado) {
		 if(this.origen.equals(estado))
			 return true;
		 else return false;
	 }
	 public boolean contieneDestino(String estado) {
		 if(this.destino.equals(estado))
			 return true;
		 else return false;
	 }
	
}
