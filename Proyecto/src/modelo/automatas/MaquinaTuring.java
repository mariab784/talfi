/**
 * 
 */
package modelo.automatas;
import java.io.*;  //necesario para añadir el .txt
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import accesoBD.Mensajero;
import vista.vistaGrafica.AristaTuring;
/**
 * Clase que implementa la funcionalidad de las Máquina de Turing.
 *  @author Rocio Barrigüete, Mario Huete, Luis San Juan 
 *
 */
public class MaquinaTuring extends AutomataFNDLambda{
//ATRIBUTOS:******************************************************	
	private String estadoIni;
	private ArrayList<String> estadosFin;
	private Alfabeto alfabet;
	private ArrayList<String> estados;
	private Alfabeto alfabetoCinta;    //XXX Un .txt
	private ArrayList<AristaTuring> maquina;
	private boolean termina;
	private String blanco;
//****************************************************************	
//MÉTODOS*********************************************************	
	/**
	 * Constructora por defecto de la Máquina de Turing.
	 */
	public MaquinaTuring(){
		super();
		setEstadoInicial(null);
		setAlfabetoCinta(null);   //XXX Un .txt
		maquina = new ArrayList<AristaTuring>();
        Mensajero mensajero = Mensajero.getInstancia();
        blanco = mensajero.devuelveMensaje("simbolos.blanco",4);
		setTermina(false);
	}
//----------------------------------------------------------------
	/**
	 * Constructora de la Máquina de Turing.
	 * @param estado, estadosF, alf, est, maq, cinta.
	 */
	public MaquinaTuring(String estado,ArrayList<String> estadosF,Alfabeto alf,
			ArrayList<String> est, ArrayList<AristaTuring> maq//,
			/*Alfabeto cinta*/){
		
		setEstadoIni(estado);
		setEstadosFin(estadosF);
		setAlfabetoCinta(alf);
		setEstados(est);
		setMaquina(maq);
//		setAlfabetoCinta(cinta);
		setTermina(false);
	}
//----------------------------------------------------------------	
	/**
	 * Método que modifica el alfabeto de cinta de la máquina de
	 * Turing.
	 * @param alfabetoCinta.
	 */
	public void setAlfabetoCinta(Alfabeto alfabetoCinta) {
		this.alfabetoCinta = alfabetoCinta;
	}
//----------------------------------------------------------------
	/**
	 * Método que devuelve el alfabeto de cinta de la máquina de 
	 * Turing.
	 * @return alfabetoCinta.
	 */
	public Alfabeto getAlfabetoCinta() {
		return alfabetoCinta;
	}
//----------------------------------------------------------------
	/**
	 * Método que modifica el parámetro de terminación de la
	 * máquina de Turing.
	 * @param termina.
	 */
	public void setTermina(boolean termina) {
		this.termina = termina;
	}
//----------------------------------------------------------------
	/**
	 * Método que devuelve el parámetro de terminación de la
	 * máquina de Turing.
	 * @return termina.
	 */
	public boolean isTermina() {
		return termina;
	}
//----------------------------------------------------------------
	/**
	 * Método que modifica máquina de Turing.
	 * @param maquina.
	 */
	public void setMaquina(ArrayList<AristaTuring> maquina) {
		this.maquina = maquina;
	}
//----------------------------------------------------------------
	/**
	 * Método que devuelve la propia Máquina de Turing. 
	 * @return maquina.
	 */
	public ArrayList<AristaTuring> getMaquina() {
		return maquina;
	}
//----------------------------------------------------------------
	/**
	 * Método que modifica el estado inicial.
	 * @param estadoIni.
	 */
	public void setEstadoIni(String estadoIni) {
		this.estadoIni = estadoIni;
	}
//----------------------------------------------------------------
	/**
	 * Método que devuelve estado inicial. 
	 * @return estadosIni.
	 */
	public String getEstadoIni() {
		return estadoIni;
	}
//----------------------------------------------------------------
	/**
	 * Método que modifica los estados finales
	 * @param estadosFin.
	 */
	public void setEstadosFin(ArrayList<String> estadosFin) {
		this.estadosFin = estadosFin;
	}
//----------------------------------------------------------------
	/**
	 * Método que devuelve la lista de estados finales 
	 * @return estados finales.
	 */
	public ArrayList<String> getEstadosFin() {
		return estadosFin;
	}
//----------------------------------------------------------------
	/**
	 * Método que modifica el alfabeto de los símbolos de la 
	 * cadena de entrada.
	 * @param alfabet.
	 */
	public void setAlfabet(Alfabeto alfabet) {
		this.alfabet = alfabet;
	}
//----------------------------------------------------------------
	/**
	 * Método que devuelve el alfabeto de los símbolos de la 
	 * cadena de entrada.
	 * @return alfabet.
	 */
	public Alfabeto getAlfabet() {
		return alfabet;
	}
//----------------------------------------------------------------
	/**
	 * Método que devuelve las coordenadas de un estado.
	 * @param estado.
	 * @return coordenadas.
	 */
	public Coordenadas getCoordenadas(String estado) {
		// TODO Auto-generated method stub
		return coordenadasGraficas.get(estado);
	}
//----------------------------------------------------------------
	/**
	 * Método que inserta nuevas coordenadas a un estado de la
	 * máquina de Turing.
	 * @param estado, cord.
	 */
	public void setCoordenadas(String estado, Coordenadas cord) {
		// TODO Auto-generated method stub 
		if (coordenadasGraficas==null) coordenadasGraficas=new HashMap<String,Coordenadas>();
		coordenadasGraficas.put(estado,cord);
	}
//----------------------------------------------------------------
	/**
	 * Método que verifica si existen coordenadas.
	 * @return bool.
	 */
	public boolean hayCoordenadas() {
		// TODO Auto-generated method stub
		return (coordenadasGraficas!=null);
	}
//----------------------------------------------------------------
	/**
	 * Método que devuelve la lista de estados a la que se llega 
	 * desde el estado que se pasa con el símbolo de entrada que 
	 * se le pasa.
	 * @param estado , estado incial desde el que se buscan los 
	 * destinos.
	 * @param letra , símbolo de entrada de las transiciones que 
	 * se devuelven.
	 * @return estados , la lista de estados a los que se llega 
	 * desde el estado con el símbolo de entrada.
	 */
	public ArrayList<String> getDestinosSimbolo(String estado,String letra) {
        // TODO Auto-generated method stub
		int i = 0;
		ArrayList<String> estados = new ArrayList<String>(); 
		AristaTuring arista = maquina.get(i);
		while(maquina.indexOf(arista) != -1){
			if(maquina.get(i).contieneOrigen(estado)){
				estados.add(estado);
			}
			i++;
			arista = maquina.get(i);
		}
		return estados;
    }
//----------------------------------------------------------------
	/**
	 * Método que devuelve una lista con los símbolos de entrada 
	 * de las transiciones entre dos estados que se pasan como 
	 * parámetros.
	 * @param origen , estado de inicio para buscar las 
	 * tansiciones.
	 * @param destino , estado de finalización para buscar las 
	 * transiciones.
	 * @return simbolos de entrada, ArrayList con las letras de 
	 * la transición.
	 */
	public ArrayList<String> getEntradasTransicion(String origen, String destino) {
		// TODO Auto-generated method stub
		int i = 0;
		AristaTuring arista;		
		int tamano = maquina.size();
		while(i < tamano){
			arista = maquina.get(i);
			if((maquina.get(i).contieneOrigen(origen))&&(maquina.get(i).contieneDestino(destino))){
				return arista.getEntradaCinta();
			}
			i++;
		}
		return null;
	}
	
//----------------------------------------------------------------
	/**
	 * Método que devuelve el símbolo de cinta de la transición 
	 * entre dos estados que se pasan como parámetros.
	 * @param origen , estado de inicio para buscar el símbolo de
	 * cinta.
	 * @param destino , estado de finalización para buscar el
	 * símbolo de cinta.
	 * @return simbolo de cinta.
	 */
	public String getSimboloCintaTransicion(String origen, String destino) {
		// TODO Auto-generated method stub
		int i = 0;
		AristaTuring arista;		
		int tamano = maquina.size();
		while(i < tamano){
			arista = maquina.get(i);
			if((maquina.get(i).contieneOrigen(origen))&&(maquina.get(i).contieneDestino(destino))){
				return arista.getSimboloCinta();
			}
			i++;
		}
		return null;
	}
//----------------------------------------------------------------
	/**
	 * Método que devuelve la dirección de la transición entre dos
	 * estados que se pasan como parámetros.
	 * @param origen , estado de inicio para buscar la dirección.
	 * @param destino , estado de finalización para buscar la
	 * dirección.
	 * @return direccion.
	 */
	public String getDireccionTransicion(String origen, String destino) {
		// TODO Auto-generated method stub
		int i = 0;
		AristaTuring arista;		
		int tamano = maquina.size();
		while(i < tamano){
			arista = maquina.get(i);
			if((maquina.get(i).contieneOrigen(origen))&&(maquina.get(i).contieneDestino(destino))){
				return arista.getDireccion();
			}
			i++;
		}
		return null;
	}
//----------------------------------------------------------------
	/**
	 * Método que verifica si dos Strings son iguales.
	 * @param Strings 'a' y 'b'.
	 * @return bool.
	 */
	private static boolean iguales(final String a, final String b){	
		return a.equals(b);
	}
//----------------------------------------------------------------
	/**
	 * Método que añade una nueva arista a la máquina de Turing .
	 * @param arista.
	 */
	public void anadeArista(AristaTuring a){
		int i = existeTransicion(a);
		if ( i == -1 ){ 
			this.maquina.add(a); 
			ArrayList<String> simbolos = a.getEntradaCinta();
			int j = 0;
			while (j < simbolos.size()){
				String s = simbolos.get(j);
				if (!s.equals(blanco) && !this.getAlfabet().getListaLetras().contains(s)){
					this.getAlfabet().getListaLetras().add(s);
				}
				
			}
		}
		
		
}
//----------------------------------------------------------------
	/**
	 * Método que añade cada una de las componentes de una arista.
	 * @param origen, destino, simbolos, cinta, direccion.
	 */
	public void insertaArista(int x1, int y1, int x2, int y2,
			String origen,String destino,ArrayList<String> simb,
			String cint,String dir) {
		AristaTuring arist = new AristaTuring(x1,y1,x2,y2,origen,destino);
		//arist.setEntradaCinta(simb);
		arist.setSimboloCinta(cint);
		arist.setDireccion(dir);
		anadeArista(arist);		
	}
//----------------------------------------------------------------
	/**
	 * Método que devuelve la posición de una arista dentro de la
	 * máquina de Turing, -1 si dicha arista no existe.
	 * @param arista.
	 * @return int.
	 */
	private int existeTransicion(AristaTuring a){	
		if (this.maquina.isEmpty()) 
			return -1;
		else{
			int i = 0;
			while (i < this.maquina.size() ){
				AristaTuring aux = this.maquina.get(i);
				boolean destinos = a.getDestino().equals(aux.getDestino());
				boolean origen = a.getOrigen().equals( aux.getOrigen());
				//boolean entrcinta = a.getEntradaCinta().equals(aux.getEntradaCinta());
				boolean simbcinta = a.getSimboloCinta().equals(aux.getSimboloCinta());
				boolean dir = iguales(a.getDireccion(),aux.getDireccion());
				if (destinos && origen && simbcinta && dir){	
					combinarEntrada(aux,a.getEntradaCinta()); 
					return i;			
				}
				i++;
			}
			return -1;
		}
	}
//----------------------------------------------------------------
	/**
	 * Método que inserta un nuevo estado en la máquina de Turing,
	 * si éste no estaba contenido previamente.
	 * @param estado.
	 */
	public void insertaEstado(String estado) {
		// TODO Auto-generated method stub
		if(!estados.contains(estado)){
			estados.add(estado);
		}
	}	
//----------------------------------------------------------------
	private void combinarEntrada(AristaTuring a,ArrayList<String> e){
		Iterator<String> it = e.iterator();
		while (it.hasNext()){
			String s = it.next();
			if (!a.getEntradaCinta().contains(s))
				a.getEntradaCinta().add(s);
		}	
	}
//----------------------------------------------------------------
public ArrayList<AristaTuring> getAristasTuring(){
	
	return maquina;
}
	


	public String toString(){
		
		String s = "";
		s += "Estados: " + this.getEstados() + "\n";
		s += "Estado Inicial: " + this.getEstadoIni() + "\n";
		s += "Estados Finales: " + this.getEstadosFin() + "\n";
		s += "Alfabeto: " + this.getAlfabetoCinta() + "\n";
		s += "Aristas: " + this.getAristasTuring() + "\n";
		
		return s;
	}
	
	/**
	 * Método que verifica si una palabra es reconocida por la 
	 * máquina de Turing.
	 * @param palabra.
	 * @return bool.
	 */
	public boolean aceptaInput(final String palabra) {
		return true;
	}
//----------------------------------------------------------------
//****************************************************************
}
