/**
 * 
 */
package modelo.automatas;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interfaz que define la funcionalidad de los automatas, cuyas aristas son
 * dirigidas, con un nodo inicial y uno de llegada
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public interface Automata {
	
	/**
	 * Inserta un vertice en el automta
	 * @param vertice el vértice a insertar
	 */
	public void insertaVertice(String vertice);
	
	/**
	 * Devuelve las aristas que salen de un vertice en el automata
	 * @param vertice vértice del que se quieren calcular las aristas salientes
	 * @return lista de todas las aristas del vértice
	 */
	public ArrayList<String> getAristasVertice(String vertice);
	
	/**
	 * Devuelve la lista de estados del automata
	 * @return lista con todos los estados 
	 */
	public ArrayList<String> getEstados();
	
	/**
	 * Inserta una arista en el automata
	 * @param verticeV vertice del que sale la arista
	 * @param verticeF vertice al que entra la arista
	 * @param letra letra de la arista
	 */
	public void insertaArista(String verticeV, String verticeF, String letra);
	
	/**
	 * Devuelve el estado inicial del automata
	 * @return nombre del estado inicial del automata
	 */
	public String getEstadoInicial();
	
	/**
	 * Establece el estado inicial
	 * @param estado nuevo estado que será inicial
	 */
	public void setEstadoInicial(String estado);
	
	/**
	 * Devuelve los estados de aceptación del automata
	 * @return lista con los estados de aceptación
	 */
	public ArrayList<String> getEstadosFinales();
	
	/**
	 * Devuelve el alfabeto del automata
	 * @return alfabeto del automata
	 */
	public Alfabeto getAlfabeto();
	
	/**
	 * Devuelve alfabeto menos lambda el automata 
	 * @return alfabeto sin la letra lambda
	 */
	public Alfabeto getAlfabetoMenosL();
	
	/**
	 * Establece lista estados de aceptación del automata
	 * @param estados nueva lista de estados de aceptación
	 */
	public void setEstadosFinales(ArrayList<String> estados);
	
	/**
	 * Establece lista estados de aceptación del automata
	 * @param estados nueva lista de estados de aceptación
	 */
	public void setEstadosFinales2(ArrayList<String> estados);
	
	/**
	 * Establece el estado final
	 * @param estado  nuevo estado final
	 */
	public void setEstadoFinal(String estado);
	
	/**
	 * Establece la lista de estados
	 * @param estados nueva lista de estados
	 */
	public void setEstados(ArrayList<String> estados);
	
	/**
	 * Establece el alfabeto
	 * @param alfabeto nuevo alfabeto del automata
	 */
	public void setAlfabeto(Alfabeto alfabeto);
	
	/**
	 * Realiza la funcion delta: funcion de transicion de estados
	 * @param estado estado del que se pregunta la función delta
	 * @param letra letra por la que se pregunta la función delta
	 * @return la lista de estados a la que se va desde el estado con la letra
	 */
	public String funcion_delta(String estado,String letra);
	
	/**
	 * Inserta un estado que es de aceptación
	 * @param estado de aceptación a insertar
	 */
	public void insertaEstadoFinal(String estado);
	
	/**
	 * Realiza la funcion delta extendida: funcion de transicion de estados incluyendo transiciones lambda
	 * @param estado estado del que se pregunta la función delta extendida
	 * @param letra letra por la que se pregunta la función delta extendida
	 * @return la lista de estados a la que se va desde el estado con la letra
	 */
	public ArrayList<String> deltaExtendida(String estado, String letra);
	
	
	/**
	 * Devuelve las coordenadas en la interfaz de un estado
	 * @param estado estado del que se quieren conocer las coordenadas
	 * @return coordenadas coordenadas del estado
	 */
	public Coordenadas getCoordenadas(String estado);
	
	/**
	 * Establece las coordenadas de un estado
	 * @param estado estado del que se quieren establecer las coordenadas
	 * @param cord nuevas coordenadas del estado
	 */
	public void setCoordenadas(String estado,Coordenadas cord);
	
	/**
	 * Decide si hay coordenadas guardadas en los estados o no
	 * @return true si hay coordenadas, false si no
	 */
	public boolean hayCoordenadas();
	
	/**
	 * Método que devuelve la lista de estados a la que se llega desde el estado que
	 * se pasa con la letra que se le pasa
	 * @param v1 estado incial desde el que se buscan los destinos
	 * @param letra letra que etiqueta las transiciones que se devuelven
	 * @return la lsta de estados a los que se llega desde el estado con la letra
	 */
	public ArrayList<String> getAristasLetra(String v1,String letra);
	
	/**
	 * Método que devuelve una lista con las letras de las transiciones entre dos
	 * estados que se pasan como parámetros
	 * @param estado1 estado de inicio para buscar las tansiciones
	 * @param estado2 estado de finalización para buscar las transiciones
	 * @return ArrayList con las letras que tiene la transicion
	 */
	public ArrayList<String> getLetraTransicion(String estado1, String estado2);
	
	/**
	 * Método que devuelve los destinos de un estado dado
	 * @param estado estado del que se quieren calcualr los destinos
	 * @return ArrayList con la lista de destinos del estado
	 */
	public ArrayList<String> getDestinos(String estado);
	
	/**
	 * Método que devuelve el estado en un String
	 * @return String con el automata
	 */
	public String toString();
	
	/**
	 * Método que elimina un vértice que no es final, si el vértice es final
	 * se elimina de la tabla pero no de la lsita de vértices.
	 * @param v vértice no final a eliminar
	 */
	public void eliminaVertice(String v);
	
	/**
	 * Método que inserta una arista y si entre esos dos estados ya había una
	 * inserta la arista con la letra + la antigua letra
	 * @param v1 estado de inicio
	 * @param v2 estado final 
	 * @param letra nueva latra a insertar
	 */
	public void insertaAristaSobreescribe(String v1, String v2,String letra);
	
	/**
	 * Clona el automata completo en un nuevo objeto
	 * @return objeto copi exacta del automata
	 * @throws CloneNotSupportedException lanza la excepción si algún parámetro no se 
	 * puede clonar 
	 */
	public Object clone() throws CloneNotSupportedException;
	
	/**
	 * Establece una nueva tabla de automata
	 * @param a nueva tabla que será el nuevo automata
	 */public void setAutomataNuevo(HashMap<String, HashMap<String,ArrayList<String>>> a);
	
	 /**
	  * Metodo que devuelve el automata en forma de tabla
	  * @return automata en forma de tabla de la clase
	  */
	 public HashMap<String, HashMap<String,ArrayList<String>>> getAutomata();
}
