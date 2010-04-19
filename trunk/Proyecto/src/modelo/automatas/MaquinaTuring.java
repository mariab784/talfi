/**
 * 
 */
package modelo.automatas;
import java.io.*;  //necesario para a�adir el .txt
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import accesoBD.Mensajero;
import vista.vistaGrafica.AristaTuring;
/**
 * Clase que implementa la funcionalidad de las M�quina de Turing.
 *  @author Rocio Barrig�ete, Mario Huete, Luis San Juan 
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
//M�TODOS*********************************************************	
	/**
	 * Constructora por defecto de la M�quina de Turing.
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
	 * Constructora de la M�quina de Turing.
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
	 * M�todo que modifica el alfabeto de cinta de la m�quina de
	 * Turing.
	 * @param alfabetoCinta.
	 */
	public void setAlfabetoCinta(Alfabeto alfabetoCinta) {
		this.alfabetoCinta = alfabetoCinta;
	}
//----------------------------------------------------------------
	/**
	 * M�todo que devuelve el alfabeto de cinta de la m�quina de 
	 * Turing.
	 * @return alfabetoCinta.
	 */
	public Alfabeto getAlfabetoCinta() {
		return alfabetoCinta;
	}
//----------------------------------------------------------------
	/**
	 * M�todo que modifica el par�metro de terminaci�n de la
	 * m�quina de Turing.
	 * @param termina.
	 */
	public void setTermina(boolean termina) {
		this.termina = termina;
	}
//----------------------------------------------------------------
	/**
	 * M�todo que devuelve el par�metro de terminaci�n de la
	 * m�quina de Turing.
	 * @return termina.
	 */
	public boolean isTermina() {
		return termina;
	}
//----------------------------------------------------------------
	/**
	 * M�todo que modifica m�quina de Turing.
	 * @param maquina.
	 */
	public void setMaquina(ArrayList<AristaTuring> maquina) {
		this.maquina = maquina;
	}
//----------------------------------------------------------------
	/**
	 * M�todo que devuelve la propia M�quina de Turing. 
	 * @return maquina.
	 */
	public ArrayList<AristaTuring> getMaquina() {
		return maquina;
	}
//----------------------------------------------------------------
	/**
	 * M�todo que modifica el estado inicial.
	 * @param estadoIni.
	 */
	public void setEstadoIni(String estadoIni) {
		this.estadoIni = estadoIni;
	}
//----------------------------------------------------------------
	/**
	 * M�todo que devuelve estado inicial. 
	 * @return estadosIni.
	 */
	public String getEstadoIni() {
		return estadoIni;
	}
//----------------------------------------------------------------
	/**
	 * M�todo que modifica los estados finales
	 * @param estadosFin.
	 */
	public void setEstadosFin(ArrayList<String> estadosFin) {
		this.estadosFin = estadosFin;
	}
//----------------------------------------------------------------
	/**
	 * M�todo que devuelve la lista de estados finales 
	 * @return estados finales.
	 */
	public ArrayList<String> getEstadosFin() {
		return estadosFin;
	}
//----------------------------------------------------------------
	/**
	 * M�todo que modifica el alfabeto de los s�mbolos de la 
	 * cadena de entrada.
	 * @param alfabet.
	 */
	public void setAlfabet(Alfabeto alfabet) {
		this.alfabet = alfabet;
	}
//----------------------------------------------------------------
	/**
	 * M�todo que devuelve el alfabeto de los s�mbolos de la 
	 * cadena de entrada.
	 * @return alfabet.
	 */
	public Alfabeto getAlfabet() {
		return alfabet;
	}
//----------------------------------------------------------------
	/**
	 * M�todo que devuelve las coordenadas de un estado.
	 * @param estado.
	 * @return coordenadas.
	 */
	public Coordenadas getCoordenadas(String estado) {
		// TODO Auto-generated method stub
		return coordenadasGraficas.get(estado);
	}
//----------------------------------------------------------------
	/**
	 * M�todo que inserta nuevas coordenadas a un estado de la
	 * m�quina de Turing.
	 * @param estado, cord.
	 */
	public void setCoordenadas(String estado, Coordenadas cord) {
		// TODO Auto-generated method stub 
		if (coordenadasGraficas==null) coordenadasGraficas=new HashMap<String,Coordenadas>();
		coordenadasGraficas.put(estado,cord);
	}
//----------------------------------------------------------------
	/**
	 * M�todo que verifica si existen coordenadas.
	 * @return bool.
	 */
	public boolean hayCoordenadas() {
		// TODO Auto-generated method stub
		return (coordenadasGraficas!=null);
	}
//----------------------------------------------------------------
	/**
	 * M�todo que devuelve la lista de estados a la que se llega 
	 * desde el estado que se pasa con el s�mbolo de entrada que 
	 * se le pasa.
	 * @param estado , estado incial desde el que se buscan los 
	 * destinos.
	 * @param letra , s�mbolo de entrada de las transiciones que 
	 * se devuelven.
	 * @return estados , la lista de estados a los que se llega 
	 * desde el estado con el s�mbolo de entrada.
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
	 * M�todo que devuelve una lista con los s�mbolos de entrada 
	 * de las transiciones entre dos estados que se pasan como 
	 * par�metros.
	 * @param origen , estado de inicio para buscar las 
	 * tansiciones.
	 * @param destino , estado de finalizaci�n para buscar las 
	 * transiciones.
	 * @return simbolos de entrada, ArrayList con las letras de 
	 * la transici�n.
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
	 * M�todo que devuelve el s�mbolo de cinta de la transici�n 
	 * entre dos estados que se pasan como par�metros.
	 * @param origen , estado de inicio para buscar el s�mbolo de
	 * cinta.
	 * @param destino , estado de finalizaci�n para buscar el
	 * s�mbolo de cinta.
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
	 * M�todo que devuelve la direcci�n de la transici�n entre dos
	 * estados que se pasan como par�metros.
	 * @param origen , estado de inicio para buscar la direcci�n.
	 * @param destino , estado de finalizaci�n para buscar la
	 * direcci�n.
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
	 * M�todo que verifica si dos Strings son iguales.
	 * @param Strings 'a' y 'b'.
	 * @return bool.
	 */
	private static boolean iguales(final String a, final String b){	
		return a.equals(b);
	}
//----------------------------------------------------------------
	/**
	 * M�todo que a�ade una nueva arista a la m�quina de Turing .
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
	 * M�todo que a�ade cada una de las componentes de una arista.
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
	 * M�todo que devuelve la posici�n de una arista dentro de la
	 * m�quina de Turing, -1 si dicha arista no existe.
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
	 * M�todo que inserta un nuevo estado en la m�quina de Turing,
	 * si �ste no estaba contenido previamente.
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
	 * M�todo que verifica si una palabra es reconocida por la 
	 * m�quina de Turing.
	 * @param palabra.
	 * @return bool.
	 */
	public boolean aceptaInput(final String palabra) {
		return true;
	}
//----------------------------------------------------------------
//****************************************************************
}
