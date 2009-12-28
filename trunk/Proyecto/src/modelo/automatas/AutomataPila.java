/**
 * 
 */
package modelo.automatas;

import java.util.ArrayList;
import java.util.HashMap;

import vista.vistaGrafica.AristaAP;


/**
 * Clase que implementa la funcionalidad de los automatas de pila
 *  @author Roc�o Barrig�ete, Mario Huete, Luis San Juan 
 *
 */
/******************************************************************/
public class AutomataPila{
	//Atributos////////////////////////////////////////////////////
	private String estadoInicial;
	private ArrayList<String> estadosFinales;
	private Alfabeto alfabeto;
	private Alfabeto alfabetoPila;
	//private Alfabeto alfabetoMenosL;
	private ArrayList<String> estados;
	private ArrayList<AristaAP> automata;
	private HashMap<String,Coordenadas> coordenadasGraficas;
	///////////////////////////////////////////////////////////////
	
	//M�todos//////////////////////////////////////////////////////
	//-------------------------------------------------------------
	public AutomataPila(){
		coordenadasGraficas=null;
		estados=new ArrayList<String>();
		estadosFinales=new ArrayList<String>();
		alfabeto=new Alfabeto_imp();
		//alfabetoMenosL=new Alfabeto_imp();
		automata=new ArrayList<AristaAP>();
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que introduce nuevo aut�mata de pila.
	 * @param a , el nuevo aut�mata de pila.
	 */
	public void setAPNuevo(ArrayList<AristaAP> a) {
		this.automata = a;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que devuelve el alfabeto del aut�mata de pila.
	 * @return alfabeto , el alfabeto de entrada.
	 */
	public Alfabeto getAlfabeto() {
		// TODO Auto-generated method stub
		return alfabeto;
	}
	//-------------------------------------------------------------
	/**
	 * Devuelve las aristas que salen de un estado en el aut�mata.
	 * @param estado , estado del que se quieren calcular las aristas salientes.
	 * @return lista de todas las aristas del estado.
	 */
	public ArrayList<String> getAristasVertice(String estado) {
		// TODO Auto-generated method stub
		int indice = automata.indexOf(estado);
		ArrayList<String> simbolos = automata.get(indice).getEntradaSimbolos();
		return simbolos;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que devuelve el estado inicial del aut�mata de pila.
	 * @return estadoInicial.
	 */
	public String getEstadoInicial() {
		// TODO Auto-generated method stub
		return estadoInicial;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que devuelve todos los estados del aut�mata de pila.
	 * @return estados.
	 */
	public ArrayList<String> getEstados() {
		// TODO Auto-generated method stub
		return estados;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que devuelve los estados finales del aut�mata de pila.
	 * @return estadosFinales.
	 */
	public ArrayList<String> getEstadosFinales() {
		// TODO Auto-generated method stub
		return estadosFinales;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que inserta una arista nueva en el aut�mata de pila.
	 * @param estado origen, estado destino, s�mbolos, cima y apilar/desapilar.
	 */
	public void insertaArista(String origen,String destino,ArrayList<String> simbolos,String cima,ArrayList<String> salida) {
		// TODO Auto-generated method stub
		int indice = automata.indexOf(origen);
		int x,y,fx,fy;
		x=0; y=0; fx=0; fy=0;
		AristaAP arista = new AristaAP(x,y,fx,fy,origen,destino);
		arista.setCimaPila(cima);
		arista.setSimbolos(simbolos);
		arista.setSalida(salida);
		if(indice == -1)
			automata.add(arista);
		else if(!automata.get(indice).equals(arista))
			automata.add(arista);	
		else return;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que inserta un estado nuevo en aut�mata de pila
	 * @param estado.
	 */
	public void insertaEstado(String estado) {
		// TODO Auto-generated method stub
		if(!estados.contains(estado)){
			estados.add(estado);
		}
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que inserta un alfabeto de entrada en un aut�mata de pila.
	 * @param alfabeto.
	 */
	public void setAlfabeto(Alfabeto alfabeto) {
		// TODO Auto-generated method stub
		this.alfabeto = alfabeto;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que inserta el estado inicial de un aut�mata de pila.
	 * @param estado.
	 */
	public void setEstadoInicial(String estado) {
		// TODO Auto-generated method stub
		estadoInicial = estado;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que inserta una lista de estados en un aut�mata de pila.
	 * @param estados.
	 */
	public void setEstados(ArrayList<String> estados) {
		// TODO Auto-generated method stub
		this.estados = estados;	
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que inserta una lista de estados finales en un aut�mata de pila.
	 * @param estados.
	 */
	public void setEstadosFinales(ArrayList<String> estados) {
		// TODO Auto-generated method stub
		this.estadosFinales = estados;	
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que inserta un estado final de un aut�mata de pila.
	 * @param estado.
	 */
	public void setEstadoFinal(String estado) {
		// TODO Auto-generated method stub
		this.estadosFinales.add(estado);	
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que inserta un estado (no final) en un aut�mata de pila.
	 * @param estado.
	 */
	public void setEstadoNoFinal(String estado) {
		// TODO Auto-generated method stub
		this.estados.add(estado);	
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que devuelve las coordenadas de un estado.
	 * @param estado.
	 * @return coordenadas.
	 */
	public Coordenadas getCoordenadas(String estado) {
		// TODO Auto-generated method stub
		return coordenadasGraficas.get(estado);
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que inserta nuevas coordenadas a un estado del aut�mata de pila.
	 * @param estado , cord.
	 */
	public void setCoordenadas(String estado, Coordenadas cord) {
		// TODO Auto-generated method stub 
		if (coordenadasGraficas==null) coordenadasGraficas=new HashMap<String,Coordenadas>();
		coordenadasGraficas.put(estado,cord);
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que verifica si existem coordenadas.
	 * @return bool.
	 */
	public boolean hayCoordenadas() {
		// TODO Auto-generated method stub
		return (coordenadasGraficas!=null);
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que devuelve la lista de estados a la que se llega desde el estado que
	 * se pasa con la letra que se le pasa.
	 * @param estado , estado incial desde el que se buscan los destinos.
	 * @param letra , letra que etiqueta las transiciones que se devuelven.
	 * @return estados , la lista de estados a los que se llega desde el estado con la letra.
	 */
	public ArrayList<String> getAristasLetra(String estado,String letra) {
        // TODO Auto-generated method stub
		int i = 0;
		ArrayList<String> estados = new ArrayList<String>(); 
		AristaAP arista = automata.get(i);
		while(automata.indexOf(arista) != -1){
			if(automata.get(i).contieneOrigen(estado)){
				estados.add(estado);
			}
			i++;
			arista = automata.get(i);
		}
		return estados;
    }
	//-------------------------------------------------------------
	/**
	 * M�todo que devuelve una lista con las letras de las transiciones entre dos
	 * estados que se pasan como par�metros.
	 * @param origen , estado de inicio para buscar las tansiciones.
	 * @param destino , estado de finalizaci�n para buscar las transiciones.
	 * @return simbolos , ArrayList con las letras que tiene la transici�n.
	 */
	public ArrayList<String> getLetraTransicion(String origen, String destino) {
		// TODO Auto-generated method stub
		int i = 0;
		ArrayList<String> simbolos = new ArrayList<String>(); 
		AristaAP arista = automata.get(i);
		while(automata.indexOf(arista) != -1){
			if((automata.get(i).contieneOrigen(origen))&&(automata.get(i).contieneDestino(destino))){
				//simbolos.add(estado);
				//XXX me falta algo,que ser�?,pues meter 
				//XXX entradasimbolos en simbolos
			}
			i++;
			arista = automata.get(i);
		}
		return simbolos;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que devuelve una lista con la salida de la transici�n entre dos
	 * estados que se pasan como par�metros.
	 * @param origen , estado de inicio para buscar la salida.
	 * @param destino , estado de finalizaci�n para buscar la salida.
	 * @return estados , ArrayList con la salida que tiene la transici�n.
	 */
	public ArrayList<String> getSalidaTransicion(String origen, String destino) {
		// TODO Auto-generated method stub
		int i = 0;
		ArrayList<String> simbolos = new ArrayList<String>(); 
		AristaAP arista = automata.get(i);
		while(automata.indexOf(arista) != -1){
			if((automata.get(i).contieneOrigen(origen))&&(automata.get(i).contieneDestino(destino))){
				//simbolos.add(estado);
				//XXX me falta algo,que ser�?,pues meter 
				//XXX salidaPila en simbolos
			}
			i++;
			arista = automata.get(i);
		}
		return simbolos;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que devuelve el aut�mata de pila en cuesti�n.
	 * @return automata.
	 */
	public ArrayList<AristaAP> getAutomata() {
		// TODO Auto-generated method stub
		return automata;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que devuelve el alfabeto de pila.
	 * @return alfabetoPila.
	 */
	@SuppressWarnings("unused")
	private Alfabeto getAlfabetoPila() {
		return alfabetoPila;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que inserta el alfabeto de pila a un aut�mata de pila.
	 * @param alf.
	 */
	@SuppressWarnings("unused")
	private void setAlfabetoPila(final Alfabeto alf) {
		this.alfabetoPila = alf;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que verifica si una palabra es reconocida por el aut�mata de pila.
	 * @param palabra.
	 * @return bool.
	 */
	private boolean reconocePalabra(final String palabra){
		//**//
		return true;
	}
	//-------------------------------------------------------------
	///////////////////////////////////////////////////////////////
}
/******************************************************************/
