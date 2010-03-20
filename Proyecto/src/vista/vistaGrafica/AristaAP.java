package vista.vistaGrafica;

import java.awt.Point;
import java.util.ArrayList;


import accesoBD.Mensajero;



/**
 * La clase arista define los parámetros de las aristas para AP de la interfaz gráfica
 *  @author Rocío Barrigüete, Mario Huete, Luis San Juan
 *
 */
@SuppressWarnings("unchecked")
public class AristaAP extends AristaGeneral implements /*AristaInterface,*/ Comparable{
	
	protected java.awt.geom.Rectangle2D bounds = new java.awt.Rectangle(0, 0);
	
/*	private int x;
	private int y;
	private int fx;
	private int fy;
	String origen;
	String destino;*/
	ArrayList<String> entradaSimbolos;
	String cimaPila;
	ArrayList<String> salidaPila;
//	boolean marcada;

	
	/**
	 * Establece los datos iniciales de la arista al cargar un ejercicio
	 * @param x coordenada x del estado de inicio
	 * @param y coordenada y del estado de inicio
	 * @param fx coordenada x del estado destion
	 * @param fy coordenada y del estado destino
	 * @param origen nombre del estado de origen
	 * @param destino nombre del estado de destino
	 */
	public AristaAP(String origen,String destino) {
		
		super(origen,destino);
	}
	
	/**
	 * Establece los datos iniciales de la arista en la interfaz gráfica
	 * @param x coordenada x del estado de inicio
	 * @param y coordenada y del estado de inicio
	 * @param fx coordenada x del estado destion
	 * @param fy coordenada y del estado destino
	 * @param origen nombre del estado de origen
	 * @param destino nombre del estado de destino
	 */
	public AristaAP(int x,int y,int fx, int fy,String origen,String destino) {

		super(x,y,fx,fy,origen,destino);
		/*	this.x=x;
		this.y=y;
		this.fx=fx;
		this.fy=fy;*/
		this.entradaSimbolos = new ArrayList</*AristaGeneral*/String>();
		this.salidaPila = new ArrayList<String>();
	//	this.marcada = false;
	//	this.origen=origen;
	//	this.destino=destino;
	}

	/**
	 * Método accesor de la lista de simbolos de entrada marcados
	 * @return lista de simbolos de entrada entradaSimbolos
	 */
	
/*	public boolean getMarcada(){ return marcada;}
	
	/**
	 * Método modificador de una posicion de la lista de simbolos de entrada marcados
	 * @param b valor de marcado
	 */
	
/*	public void setMarcada(boolean b){ marcada = b;}
	
	/**
	 * Método accesor de la lista de simbolos de entrada
	 * @return lista de simbolos de entrada entradaSimbolos
	 */
	
	public ArrayList</*AristaGeneral*/String> getEntradaSimbolos(){ return entradaSimbolos;}
	
	
	/**
	 * Método que añade nuevo simbolo de entrada
	 * @param s simbolo de entrada
	 */
	
	public void anadirSimbolo(String s){ 
		
		//AristaGeneral a = new AristaGeneral(s);
		if (!entradaSimbolos.contains(/*a*/s))
			entradaSimbolos.add(/*a*/s);
	
	}

	
	/**
	 * Método que añade nuevo simbolo de pila
	 * @param s simbolo de pila
	 */
	
	public void anadirPila(String s){ 
	
		//if (!salidaPila.contains(s))
			int i = 0;
			String aux;
			while (i < s.length()){
				aux = "" + s.charAt(i);
				salidaPila.add(aux);
				i++;
			}
		
	}
	
	
	/**
	 * Método accesor de la cima de la pila
	 * @return cima de la pila cimaPila
	 */
	
	public ArrayList<String> getSalidaPila(){ return salidaPila;}
	
	/**
	 * Método accesor de la cima de la pila
	 * @return cima de la pila cimaPila
	 */
	
	public String getCimaPila(){ return cimaPila;}
	
	/**
	 * Método modificador de la cima de la pila
	 * @param s cima de la pila cimaPila
	 */
	

	public void setCimaPila(String s) {
		this.cimaPila = s;
	}
	
	/**
	 * Método modificador de la cima de la pila
	 * @param x nueva coordenada x del origen
	 */
	
/*	public int getX() {
		return x;
	}

	/**
	 * Método modificador de la coordenada x del estado origen
	 * @param x nueva coordenada x del origen
	 */
/*	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Método accesor de la coordenada y del estado origen
	 * @return coordenada y del origen
	 */
/*	public int getY() {
		return y;
	}

	/**
	 * Método modificador de la coordenada y del estado origen
	 * @param y nueva coordenada y del origen
	 */
/*	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Método accesor de la coordenada x del estado destino
	 * @return coordenada x del destino
	 */
/*	public int getFx() {
		return fx;
	}

	/**
	 * Método modificador de la coordenada x del estado destino
	 * @param fx nueva coordenada x del destino
	 */
/*	public void setFx(int fx) {
		this.fx = fx;
	}

	/**
	 * Método accesor de la coordenada x del estado destino
	 * @return coordenada x del destino
	 */
/*	public int getFy() {
		return fy;
	}

	/**
	 * Método modificador de la coordenada y del estado destino
	 * @param fy nueva coordenada y del destino
	 */
/*	public void setFy(int fy) {
		this.fy = fy;
	}

	/**
	 * Método accesor del nombre del estado origen
	 * @return nombre del estado origen
	 */
/*	public String getOrigen() {
		return origen;
	}
	
	/**
	 * Método modificador del nombre del estado origen
	 * @param origen nuevo nombre del origen
	 */
/*	public void setOrigen(String origen) {
		this.origen = origen;
	}

	/**
	 * Método accesor del nombre del estado destino
	 * @return monbre del estado destino
	 */
/*	public String getDestino() {
		return destino;
	}

	/**
	 * Método modificador del nombre del estado destino
	 * @param destino nuevo nombre del destino
	 */
/*	public void setDestino(String destino) {
		this.destino = destino;
	}

	
	/**
	 * Método accesor de la etiqueta de la arista
	 * @return nombre de la arista(letra que realiza la transicion)
	 */
/*	public String getEtiqueta() {
		return etiqueta;
	}*/

	/**
	 * Método midificador de la etiqueta de la arista
	 * @param etiqueta nueva etiqueta de la atista
	 */
/*	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}*/
	
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
	 * Método que devuelve la arista en formato texto con los nombre de los estados
	 * origen y destino, la etiqueta y si está maarcada o no
	 * @return cadena con la información de la arista
	 */
	public String toString() {
		return "d("+origen+","+entradaSimbolos+","+cimaPila+")=("+destino+","+salidaPila+")";//+"("+marcada+")";
	}
	/**
	 * Método que devuelve los simbolos de entrada que se consumen en la arista en formato texto
	 *  @return cadena con los simbolos consumidos en la arista
	 */
	public String toStringSimbolos() {
		String s = "";
		int tam = this.getEntradaSimbolos().size();
		for(int i = 0; i < tam; i++){
			s += this.getEntradaSimbolos().get(i);
			if (i != tam-1)	 s+= ",";
			
		}
		return s;
	}
	/**
	 * Método que devuelve la transicion de pila de la arista en formato texto
	 * @return cadena con la información de la transicion de pila de la arista
	 */
	public String toStringTransicion() {
		String s = "";
		int tam = this.getSalidaPila().size();
		for(int i = 0; i < tam; i++){
			s += this.getSalidaPila().get(i);
		}
		return s;	}
	
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

	public int compareTo(Object arg0) {
		AristaAP a2 = (AristaAP)arg0;
		Mensajero mensajero = Mensajero.getInstancia();
		String lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		/*if (cadena1.compareTo(cadena2) == 0)
   System.out.println("cadena1 y cadena2 son iguales");
else
if (cadena1.compareTo(cadena2) < 0)
  System.out.println ("cadena1 va antes que cadena2");
else
if (cadena1.compareTo(cadena2) > 0)
  System.out.println("cadena2 va después que cadena1");
*/
		// TODO Auto-generated method stub
		/*if ( this.getOrigen().compareTo(a2.getOrigen() ) != 0 &&
				!this.getEntradaSimbolos().contains(lambda)) return 1;
		else return -1;*/
		int origenes = this.getOrigen().compareTo(a2.getOrigen() );
		
		if ( origenes > 0 ) return 1;
		
		if ( origenes == 0 ){
			
			if (!this.getEntradaSimbolos().contains(lambda)) return -1;
			else return 1;
		}
		else return -1;
		
		/*!= 0 &&
		!this.getEntradaSimbolos().contains(lambda)*/
		
	}
	
	public AristaAP clone(){
		AristaAP a = new AristaAP(getX(),getY(),getFx(),getFy(),getOrigen(),getDestino());
		a.setSimbolos(this.getEntradaSimbolos());
		a.setCimaPila(this.getCimaPila());
		a.setSalida(this.getSalidaPila());
		return a;
	}
}
