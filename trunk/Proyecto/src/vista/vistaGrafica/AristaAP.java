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

	ArrayList<String> entradaSimbolos;
	String cimaPila;
	ArrayList<String> salidaPila;

	
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
		this.entradaSimbolos = new ArrayList</*AristaGeneral*/String>();
		this.salidaPila = new ArrayList<String>();
	}

	
	public ArrayList<String> getEntradaSimbolos(){ return entradaSimbolos;}
	
	
	/**
	 * Método que añade nuevo simbolo de entrada
	 * @param s simbolo de entrada
	 */
	
	public void anadirSimbolo(String s){ 
		
		if (!entradaSimbolos.contains(s))
			entradaSimbolos.add(s);	
	}
	
	/**
	 * Método que añade nuevo simbolo de pila
	 * @param s simbolo de pila
	 */
	
	public void anadirPila(String s){ 

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
	 * Método que devuelve la arista en formato texto con los nombre de los estados
	 * origen y destino, la etiqueta y si está maarcada o no
	 * @return cadena con la información de la arista
	 */
	public String toString() {
		return "d("+origen+","+entradaSimbolos.toString()+","+cimaPila+")={("+destino+","+salidaPila.toString()+")}";//+"("+marcada+")";
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

		// TODO Auto-generated method stub

		int origenes = this.getOrigen().compareTo(a2.getOrigen() );
		
		if ( origenes > 0 ) return 1;
		
		if ( origenes == 0 ){
			
			if (!this.getEntradaSimbolos().contains(lambda)) return -1;
			else return 1;
		}
		else return -1;

		
	}
	
	public AristaAP clone(){
		AristaAP a = new AristaAP(getX(),getY(),getFx(),getFy(),getOrigen(),getDestino());
		a.setSimbolos(this.getEntradaSimbolos());
		a.setCimaPila(this.getCimaPila());
		a.setSalida(this.getSalidaPila());
		return a;
	}
}
