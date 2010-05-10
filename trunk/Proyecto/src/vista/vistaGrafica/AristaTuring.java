package vista.vistaGrafica;

import java.awt.Point;
import java.util.ArrayList;

import vista.AristaInterface;

/**
 * La clase arista define los parámetros de las aristas de Turing de la interfaz gráfica
 *  @author Rocío Barrigüete, Mario Huete, Luis San Juan
 *
 */ 
public class AristaTuring extends AristaGeneral implements AristaInterface{

//	protected java.awt.geom.Rectangle2D bounds = new java.awt.Rectangle(0, 0);
	
/*	private int x;
	private int y;
	private int fx;
	private int fy;
	String origen;
	String destino;*/
	ArrayList<String> entradaCinta;
	String simboloCinta;
	String direccion;
//	boolean marcada;
	
	public AristaTuring(String origen,String destino){
		
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
	public AristaTuring(int x,int y,int fx, int fy,String origen,String destino) {
		super(x,y,fx,fy,origen,destino);
	/*	this.x=x;
		this.y=y;
		this.fx=fx;
		this.fy=fy;*/
		this.entradaCinta = new ArrayList</*AristaGeneral*/String>();
		//this.marcada = false;
	//	this.origen=origen;
	//	this.destino=destino;
	}
	
	public String getDireccion(){ return direccion;}
	
	public void setDireccion(String s){ direccion= s;}
	/**
	 * Método accesro de la característica de la arista
	 * @return true si la arista esta seleccionada
	 */
	
	public boolean getMarcada(){ return marcada;}
	
	/**
	 * Marca (o desmarca) la arista
	 * @param marcada si true marca la arista, si false la desmarca
	 */
	
	public void setMarcada(boolean b){ marcada = b;}
	
	/**
	 * Método accesor de la lista de simbolos de entrada
	 * @return lista de simbolos de entrada entradaSimbolos
	 */
	
	public ArrayList</*AristaGeneral*/String> getEntradaCinta(){ return entradaCinta;}
	
	
	/**
	 * Método que añade nuevo simbolo de entrada
	 * @param s simbolo de entrada
	 */
	
	public void anadirSimboloCintaEntrada(String s){ 
		
		//AristaGeneral a = new AristaGeneral(s);
		if (!entradaCinta.contains(/*a*/s))
			entradaCinta.add(/*a*/s);
	
	}

	
	/**
	 * Método que añade nuevo simbolo de pila
	 * @param s simbolo de pila
	 */
	
	public void anadirSimboloCinta(String s){ simboloCinta = s;	}
	

	
	/**
	 * Método accesor de la cima de la pila
	 * @return cima de la pila cimaPila
	 */
	
	public String getSimboloCinta(){ return simboloCinta;}
	
	
	/**
	 * Método modificador de los simbolos de la arista
	 * @param s ArrayList<String> simbolos de entrada
	 */
	

	public void setSimbolos(ArrayList<String> s) {
		this.entradaCinta = s;
	}
	
	
	/**
	 * Método modificador de la cima de la pila
	 * @param s cima de la pila cimaPila
	 */
	

	public void setSimboloCinta(String s) {
		this.simboloCinta = s;
	}
	
	/**
	 * Método modificador de la cima de la pila
	 * @param x nueva coordenada x del origen
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
		return "d("+origen+","+entradaCinta+","+")=("+destino+","+simboloCinta+","+direccion+")";//+"("+marcada+")";
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
	/**
	 * Método que devuelve los simbolos de entrada que se consumen en la arista en formato texto
	 *  @return cadena con los simbolos consumidos en la arista
	 */
	public String toStringSimbolos() {
		String s = "";
		int tam = this.getEntradaCinta().size();
		for(int i = 0; i < tam; i++){
			s += this.getEntradaCinta().get(i);
			if (i != tam-1)	 s+= ",";
			
		}
		return s;
	}
	
	public AristaTuring clone(){
		AristaTuring  a = new AristaTuring (getX(),getY(),getFx(),getFy(), getOrigen(),getDestino());
		a.setSimbolos(this.getEntradaCinta());
		a.setSimboloCinta(this.getSimboloCinta() );
		a.setDireccion(this.getDireccion());
		return a;
	}
}
