/**
 * 
 */
package modelo.automatas;

import java.util.*;

import accesoBD.Mensajero;
import vista.vistaGrafica.AristaAP;
/**
 * Clase que implementa la funcionalidad de los automatas de pila
 *  @author Rocío Barrigüete, Mario Huete, Luis San Juan 
 *
 */

public class AutomataPila extends AutomataFND implements Automata{
	
	private final  static String nombreAux = "aux";
	private final  static String iniNombreAux = "iniaux";
	private String estadoInicial;
	private ArrayList<String> estadosFinales;
	private Alfabeto alfabeto;
	private Alfabeto_Pila alfabetoPila;
	private ArrayList<String> estados;
	private ArrayList<AristaAP> automata;
	private HashMap<String,Coordenadas> coordenadasGraficas;
	private boolean apd;
	private Mensajero mensajero;
	private boolean aceptaLambda;
	private String lambda;
	private String fondoPila;
	private ArrayList<String> listaPalabrasEj;
	private ArrayList<String> listaPalabrasEjNo;
	private ArrayList<Integer> aristasQueDesapilan;
	private ArrayList<AristaAP> aristasPilaVacia;
	private String estadoPilaVacia;
	private String estadoIniPilaVacia;
	
	
	public AutomataPila(){
		
		coordenadasGraficas=null;
		estados=new ArrayList<String>();
		estadosFinales=new ArrayList<String>();
		alfabeto=new Alfabeto_imp();
		automata=new ArrayList<AristaAP>();
		apd = true;
		mensajero = Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		fondoPila = mensajero.devuelveMensaje("simbolos.cima",4);
		alfabetoPila = new AlfabetoPila_imp();
		alfabetoPila.aniadirLetra(fondoPila);
		aristasQueDesapilan = new ArrayList<Integer>();
		aristasPilaVacia = null;
		estadoPilaVacia = null;
	}

	public AutomataPila(String estadoI,ArrayList<String> estadosF,Alfabeto alf, Alfabeto_Pila alfPila, 
			ArrayList<String> est, ArrayList<AristaAP> aut){		

		estadoInicial = estadoI;
		estadosFinales = estadosF;
		alfabeto = alf;
		alfabetoPila = alfPila;	
		estados = est;
		automata = convierte(aut);		
		apd = compruebaAPD();
		aristasQueDesapilan = new ArrayList<Integer>();
		aristasPilaVacia = new ArrayList<AristaAP>();		
		mensajero = Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		fondoPila = mensajero.devuelveMensaje("simbolos.cima",4);	
	}
	
	private ArrayList<AristaAP> convierte(ArrayList<AristaAP> aut){
		
		if(aut == null || aut.isEmpty()) return aut;
		
		ArrayList<AristaAP> naut = new ArrayList<AristaAP>();
		int tam = aut.size(); int i = 0;
		while(i < tam){
			AristaAP a = aut.get(i);
			AristaAP na = new AristaAP(a.getX(),a.getY(),a.getFx(),a.getY(),a.getOrigen(),a.getDestino());
			na.setSimbolos(a.getEntradaSimbolos());
			na.setCimaPila(a.getCimaPila().toUpperCase());
			na.setSalida(transforma(a.getSalidaPila()));
			naut.add(na);
			i++;
		}
		return naut;
	}
	
	public boolean getAceptaLambda(){return aceptaLambda;}
	
	public void setAceptaLambda(boolean b){aceptaLambda = b;}
	
	public void anadeArista(AristaAP a){
		int i = existeTransicion(a);
		if ( i == -1 ) this.automata.add(a);
		anadeSimbolosAlf(a.getEntradaSimbolos());
		anadeSimbolosAlfPila(a.getCimaPila().toUpperCase(),transforma(a.getSalidaPila()));
		apd = compruebaAPD();
	}
	
	private ArrayList<String> transforma(ArrayList<String> as){
	
		if(as.toString().equals(lambda)) return as;
		int i = 0;
		int tam = as.size();
		ArrayList<String> ns = new ArrayList<String>();
		while(i < tam){
			ns.add(as.get(i).toUpperCase());
			i++;
		}
		return ns;
	}

	public void anadeSimbolosAlf(ArrayList<String> a){
	
		Iterator<String> itA = a.iterator();
		while(itA.hasNext()){
			String s = itA.next();
			if (!this.alfabeto.getListaLetras().contains(s) && !s.equals(lambda))
				this.alfabeto.aniadirLetra(s);
		}
	
	}

	public void setListaPalabrasEj(ArrayList<String> l){ listaPalabrasEj = l;}

	public void setListaPalabrasEjNo(ArrayList<String> l){ listaPalabrasEjNo = l;}

	public ArrayList<String> getListaPalabrasEj(){ return listaPalabrasEj;}

	public ArrayList<String> getListaPalabrasEjNo(){ return listaPalabrasEjNo;}

	public void anadeSimbolosAlfPila(String c,ArrayList<String> a){
	
		if (!this.alfabetoPila.getListaLetras().contains(c) && !c.equals(lambda))
			this.alfabetoPila.aniadirLetra(c);
		
		Iterator<String> itA = a.iterator();
		while(itA.hasNext()){
		String s = itA.next();
		if (!this.alfabetoPila.getListaLetras().contains(s) && !s.equals(lambda))
			this.alfabetoPila.aniadirLetra(s);
		}
	}

	@SuppressWarnings("unchecked")
	private boolean compruebaAPD(){  

		if (this.getAutomataPila().isEmpty()) return true;
		int i, j;
		i = 1; j = 1;
		Collections.sort(getAutomataPila());
		AristaAP a = this.getAutomataPila().get(0);
		if ((a.getEntradaSimbolos().size() > 1) && (a.getEntradaSimbolos().contains(lambda))) return false;
	
		while (i < this.getAutomataPila().size()){
			AristaAP aux = this.getAutomataPila().get(i);
			if ((aux.getEntradaSimbolos().size() > 1) && (aux.getEntradaSimbolos().contains(lambda))) 
				return false;		
		
			if ( iguales(a.getOrigen(),aux.getOrigen()) ){
			
				if (!iguales(a.getCimaPila(),aux.getCimaPila())) i++;
				else{
					if (this.contieneEntrada(a, aux)) return false;
					if (a.getEntradaSimbolos().contains(lambda) || aux.getEntradaSimbolos().contains(lambda))
						return false;				
					i++;
				}			
			}
			else i++;
		
			if (i == this.getAutomataPila().size()){
				a = this.getAutomataPila().get(j); j++; i = j;
				if ((a.getEntradaSimbolos().size() > 1) && (a.getEntradaSimbolos().contains(lambda))) return false;

			} 
			
		}

		return true;
	}

	private int existeTransicion(AristaAP a){
	
		if ( this.automata.isEmpty() ) return -1;
		else{
			int i = 0;
		
			while (i < this.automata.size() ){
				AristaAP aux = this.automata.get(i);
				boolean destinos = a.getDestino().equals( aux.getDestino());
				boolean origen = a.getOrigen().equals( aux.getOrigen());
				boolean cima = a.getCimaPila().equals( aux.getCimaPila());
				boolean salida = iguales(a.getSalidaPila(),aux.getSalidaPila());
			
				if ( destinos && origen && cima ){
				
					if (salida){ 
						combinarEntrada(aux, a.getEntradaSimbolos()); //XXX
						return i;
					}			
				}	
				i++;
			}
			return -1;
		}
	}

	private void combinarEntrada(AristaAP a, ArrayList<String> e){
	
		Iterator<String> it = e.iterator();
		while (it.hasNext()){
			String s = it.next();
			if (!a.getEntradaSimbolos().contains(s))
				a.getEntradaSimbolos().add(s);
		}	
	}
	
	private boolean contieneEntrada(AristaAP arista, AristaAP a){ 

		int tamA = a.getEntradaSimbolos().size(); 		
		int i = 0;
		while ( i < tamA){
			if (arista.getEntradaSimbolos().contains(a.getEntradaSimbolos().get(i))) return true;
			i++;
		}
		return false;
	}

	private static boolean iguales(ArrayList<String> a, ArrayList<String> b){
	
		if (a.size() != b.size()) return false;
	
		Iterator<String> itA = a.iterator();
		Iterator<String> itB = b.iterator();
	
		while (itA.hasNext()){
			if ( !itA.next().equals(itB.next()) )return false;
		}
		return true;
	
	}

	/**
	 * Método que introduce nuevo autómata de pila.
	 * @param a , el nuevo autómata de pila.
	 */
	public void setAPNuevo(ArrayList<AristaAP> a) {

		this.automata = a;
	}

	/**
	 * Método que devuelve el booleano que indica si el ap es determinista
	 * @return apd , booleano que indica determinismo.
	 */
	public boolean getApd() {
		// TODO Auto-generated method stub
		return apd;
	}

	/**
	 * Método que devuelve el alfabeto del autómata de pila.
	 * @return alfabeto , el alfabeto de entrada.
	 */
	public Alfabeto getAlfabeto() {
		// TODO Auto-generated method stub
		return alfabeto;
	}
	
	/**
	 * Método que devuelve el estado inicial del autómata de pila.
	 * @return estadoInicial.
	 */
	public String getEstadoInicial() {
		// TODO Auto-generated method stub
		return estadoInicial;
	}

	/**
	 * Método que devuelve todos los estados del autómata de pila.
	 * @return estados.
	 */
	public ArrayList<String> getEstados() {
		// TODO Auto-generated method stub
		return estados;
	}

	/**
	 * Método que devuelve los estados finales del autómata de pila.
	 * @return estadosFinales.
	 */
	public ArrayList<String> getEstadosFinales() {
		// TODO Auto-generated method stub
		return estadosFinales;
	}

	public void insertaArista2(String origen,String destino,ArrayList<String> simbolos,String cima,ArrayList<String> salida) {

		AristaAP arist = new AristaAP(origen,destino);
		arist.setCimaPila(cima.toUpperCase());
		arist.setSalida(transforma(salida));
		arist.setSimbolos(simbolos);
		anadeArista(arist);
		apd = compruebaAPD();		
	}

	/**
	 * Método que inserta un estado nuevo en autómata de pila
	 * @param estado.
	 */
	public void insertaEstado(String estado) {
		// TODO Auto-generated method stub
		if(!estados.contains(estado)){
			estados.add(estado);
		}
	}

	/**
	 * Método que inserta un alfabeto de entrada en un autómata de pila.
	 * @param alfabeto.
	 */
	public void setAlfabeto(Alfabeto alfabeto) {
		// TODO Auto-generated method stub
		this.alfabeto = alfabeto;
	}
	
	/**
	 * Método que inserta el estado inicial de un autómata de pila.
	 * @param estado.
	 */
	public void setEstadoInicial(String estado) {
		// TODO Auto-generated method stub
		estadoInicial = estado;
	}

	/**
	 * Método que inserta una lista de estados en un autómata de pila.
	 * @param estados.
	 */
	public void setEstados(ArrayList<String> estados) {
		// TODO Auto-generated method stub
		this.estados = estados;	
	}

	/**
	 * Método que inserta una lista de estados finales en un autómata de pila.
	 * @param estados.
	 */
	public void setEstadosFinales(ArrayList<String> estados) {
		// TODO Auto-generated method stub
		this.estadosFinales = estados;	
	}

	/**
	 * Método que inserta un estado final de un autómata de pila.
	 * @param estado.
	 */
	public void setEstadoFinal(String estado) {
		// TODO Auto-generated method stub
		this.estadosFinales.add(estado);	
	}

	/**
	 * Método que inserta un estado (no final) en un autómata de pila.
	 * @param estado.
	 */
	public void setEstadoNoFinal(String estado) {
		// TODO Auto-generated method stub
		this.estados.add(estado);	
	}

	/**
	 * Método que devuelve las coordenadas de un estado.
	 * @param estado.
	 * @return coordenadas.
	 */
	public Coordenadas getCoordenadas(String estado) {
		// TODO Auto-generated method stub
		return coordenadasGraficas.get(estado);
	}

	/**
	 * Método que inserta nuevas coordenadas a un estado del autómata de pila.
	 * @param estado , cord.
	 */
	public void setCoordenadas(String estado, Coordenadas cord) {
		// TODO Auto-generated method stub 
		if (coordenadasGraficas==null) coordenadasGraficas=new HashMap<String,Coordenadas>();
		coordenadasGraficas.put(estado,cord);
	}

	/**
	 * Método que verifica si existem coordenadas.
	 * @return bool.
	 */
	public boolean hayCoordenadas() {
		// TODO Auto-generated method stub
		return (coordenadasGraficas!=null);
	}

	/**
	 * Método que devuelve la lista de estados a la que se llega desde el estado que
	 * se pasa con la letra que se le pasa.
	 * @param estado , estado incial desde el que se buscan los destinos.
	 * @param letra , letra que etiqueta las transiciones que se devuelven.
	 * @return estados , la lista de estados a los que se llega desde el estado con la letra.
	 */
	public ArrayList<String> getAristasLetra(String estado,String letra) {
        // TODO Auto-generated method stub
		int i = 0;
		ArrayList<String> estados = new ArrayList<String>(); 
		@SuppressWarnings("unused")
		AristaAP arista = automata.get(i);
		while(i<automata.size()){
			
			if(automata.get(i).contieneOrigen(estado) && !estados.contains(estado)){
				estados.add(estado);
			}
			arista = automata.get(i);
			i++;
		}
		return estados;
    }

	/**
	 * Método que devuelve una lista con las letras de las transiciones entre dos
	 * estados que se pasan como parámetros.
	 * @param origen , estado de inicio para buscar las tansiciones.
	 * @param destino , estado de finalización para buscar las transiciones.
	 * @return simbolos , ArrayList con las letras que tiene la transición.
	 */
	public ArrayList<String> getLetraTransicion(String origen, String destino) {
		// TODO Auto-generated method stub
		int i = 0;
		AristaAP arista;		
		
		int tamano = automata.size();
		while(i < tamano){
			
			arista = automata.get(i);
			if((automata.get(i).contieneOrigen(origen))&&(automata.get(i).contieneDestino(destino))){
				return arista.getEntradaSimbolos();
			}
			i++;
		}
		return null;
	}

	/**
	 * Método que devuelve una lista con la salida de la transición entre dos
	 * estados que se pasan como parámetros.
	 * @param origen , estado de inicio para buscar la salida.
	 * @param destino , estado de finalización para buscar la salida.
	 * @return estados , ArrayList con la salida que tiene la transición.
	 */
	public ArrayList<String> getSalidaTransicion(String origen, String destino) {
		// TODO Auto-generated method stub
		int i = 0;
		AristaAP arista;		
		
		int tamano = automata.size();
		while(i < tamano){
			
			arista = automata.get(i);
			if((automata.get(i).contieneOrigen(origen))&&(automata.get(i).contieneDestino(destino))){
				return arista.getSalidaPila();
			}
			i++;
		}
		return null;
	}

	/**
	 * Método que devuelve el autómata de pila en cuestión.
	 * @return automata.
	 */
	public ArrayList<AristaAP> getAutomataPila() {
		// TODO Auto-generated method stub
		return automata;
	}

	/**
	 * Método que devuelve el alfabeto de pila.
	 * @return alfabetoPila.
	 */
	public Alfabeto_Pila getAlfabetoPila() {
		return alfabetoPila;
	}

	/**
	 * Método que inserta el alfabeto de pila a un autómata de pila.
	 * @param alf.
	 */
	public void setAlfabetoPila(Alfabeto_Pila alf) {
		this.alfabetoPila = alf;
	}	

	private static boolean iguales(String a1, String a2){
		
		return a1.equals(a2);
	}
	
	public String getFondoPila(){return fondoPila;}

	public void setEstadoPilaVacia(String s){estadoPilaVacia = s;}

	public void setEstadoIniPilaVacia(String s){estadoIniPilaVacia = s;}

	public void setFondoPila(String s){fondoPila = s;}
	
	public String getEstadoPilaVacia(){return estadoPilaVacia;}

	public String getEstadoIniPilaVacia(){return estadoIniPilaVacia;}
	
	@SuppressWarnings("unchecked")
	public AutomataPila convertirPilaVacia(){

		if(mensajero == null) mensajero = Mensajero.getInstancia();
		String fondoPilaAux = mensajero.devuelveMensaje("simbolos.cimaVacia",4);
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		fondoPila = mensajero.devuelveMensaje("simbolos.cima",4);

		AlfabetoPila_imp nalfPila = new AlfabetoPila_imp();
		ArrayList<String> letrasAlfPila = 
			(ArrayList<String>) transforma(this.getAlfabetoPila().getListaLetras()).clone();
		letrasAlfPila.add(fondoPilaAux.toUpperCase());
		nalfPila.setLetras(letrasAlfPila);
		
		String autEstIni = this.getEstadoInicial();
		Alfabeto autAlfIni = this.getAlfabeto();
		ArrayList<String> autEstados = (ArrayList<String>) this.getEstados().clone();
		ArrayList<AristaAP> autAristas = (ArrayList<AristaAP>) this.getAutomataPila().clone();
		AutomataPila aut = new AutomataPila(autEstIni,new ArrayList<String>(),autAlfIni,
				nalfPila,autEstados,autAristas);
		int i = 0; int j = 0;
		
		aut.setEstadoPilaVacia(nombreAux);
		aut.setEstadoIniPilaVacia(iniNombreAux);
		aut.getEstados().add(aut.getEstadoIniPilaVacia());
		
		String antiguoIni = aut.getEstadoInicial();
		AristaAP aristaIni = new AristaAP(0,0,0,0,aut.getEstadoIniPilaVacia(),antiguoIni);
		
		ArrayList<String> nSimb = new ArrayList<String>();
		nSimb.add(lambda);
		aristaIni.setSimbolos(nSimb);
		aristaIni.setCimaPila(fondoPilaAux);
		nSimb = new ArrayList<String>();
		nSimb.add(fondoPila);
		nSimb.add(fondoPilaAux);
				
		aristaIni.setSalida(nSimb);
		
		aut.getAutomataPila().add(aristaIni);
		
		aut.setFondoPila(fondoPilaAux);
		
		aut.setEstadoInicial(aut.getEstadoIniPilaVacia());
		
		aut.getEstados().add(aut.getEstadoPilaVacia());
		aristasPilaVacia = new ArrayList<AristaAP>();
		String simboloPila;
		
		if (!this.getEstadosFinales().isEmpty()){
			String est = this.getEstadosFinales().get(i);
			int tamAlfpila = /*this*/aut.alfabetoPila.getListaLetras().size();
			int tamEstFin = this.getEstadosFinales().size();
			if (mensajero == null)
				mensajero = Mensajero.getInstancia();
		
			while ( (i < tamEstFin) && (j < tamAlfpila) ){

				AristaAP a = new AristaAP(0,0,0,0,est,aut.getEstadoPilaVacia());
				simboloPila = aut.alfabetoPila.getListaLetras().get(j);
				a.anadirSimbolo(lambda);
				a.setCimaPila(simboloPila);
				ArrayList<String> trans = new ArrayList<String>();
				trans.add(lambda);
				a.setSalida(trans);
				aut.getAutomataPila().add(a);
				aut.aristasPilaVacia.add(a);
				a = new AristaAP(0,0,0,0,aut.getEstadoPilaVacia(),aut.getEstadoPilaVacia());
				a.setOrigen(aut.getEstadoPilaVacia());
				a.anadirSimbolo(lambda);
				a.setCimaPila(simboloPila);
				a.setSalida(trans);
				aut.getAutomataPila().add(a);
			
				if (j == (tamAlfpila -1)) {								 
					if (i != (tamEstFin -1)){
						i++;
						est = this.getEstadosFinales().get(i);
						j = 0;
					}
					else {j++;}
				}
				else {j++;}
			}	
		}
		
		boolean b = aut.compruebaAPD();
		aut.setDeterminista(b);
		return aut;
	}
	
	public void setDeterminista(boolean b){apd = b;}
	
	/**
	 * Método que verifica si una palabra es reconocida por el autómata de pila.
	 * @param palabra.
	 * @param boolean estadoFinal, true si acepta por estado final, false si es por pila vacia
	 * @return bool.
	 */
	public void reconocePalabra(final String palabra){

		try{		
			apd = compruebaAPD();
			if (this.apd)System.out.println("DETERMINISTA!!!");
			else System.out.println("SH*T!!!");
		
			if (!this.estadosFinales.isEmpty()) convertirPilaVacia();
		
			if (this.aristasQueDesapilan.isEmpty() && this.estadosFinales.isEmpty()) 
				System.out.println("LENGUAJE VACIO");
		}
		catch (StackOverflowError e){System.out.println("FALLO grrrrrrrr");}
	}
		
	/**
	 * @param estado
	 * @return lista de las cimas posibles para un estado
	 * Lo utilizaremos en varios en algoritmos
	 */
	public ArrayList<String> dameCimasEstado (String estado){
		Iterator<AristaAP> it = this.automata.iterator();
		ArrayList<String> devuelve = null;
		while (it.hasNext()){
			AristaAP aux = it.next();
			if (aux.getOrigen().equals(estado)){
				if (devuelve == null) devuelve = new ArrayList<String>();
				if (!devuelve.contains(aux.getCimaPila())) devuelve.add(aux.getCimaPila());
			}
			
		}
		return devuelve;
	}
	
	public ArrayList<String> dameEstados (String estado){
		Iterator<AristaAP> it = this.automata.iterator();
		ArrayList<String> devuelve = null;
		while (it.hasNext()){
			AristaAP aux = it.next();
			if (aux.getOrigen().equals(estado)){
				if (devuelve == null) devuelve = new ArrayList<String>();
				if (!devuelve.contains(aux.getDestino())) devuelve.add(aux.getDestino());
			}
			
		}
		return devuelve;
	}
	
	/**
	 * @param estado
	 * @param cima
	 * @return Lista de Letras que llevan de ese estado con esa cima a algun lugar
	 */
	public ArrayList<String> dameLetraEstadoCima(String estado, String cima){
		Iterator<AristaAP> it = this.automata.iterator();
		ArrayList<String> devuelve = null;
		devuelve = new ArrayList<String>();
		while (it.hasNext()){
			AristaAP aux = it.next();
			if (aux.getOrigen().equals(estado) && aux.getCimaPila().equals(cima)){
				devuelve.addAll(aux.getEntradaSimbolos());
			}
			
		}
		return devuelve;
	}
	
	public ArrayList<String> dameEstadoLetraEstado(String estado, String letra){
		Iterator<AristaAP> it = this.automata.iterator();
		ArrayList<String> devuelve = null;
		devuelve = new ArrayList<String>();
		while (it.hasNext()){
			AristaAP aux = it.next();
			if (aux.getOrigen().equals(estado) && aux.getEntradaSimbolos().contains(letra) && !devuelve.contains(aux.getDestino()))
				devuelve.add(aux.getDestino());
		}
			
		return devuelve;
	}
	
	public ArrayList<ArrayList<String>> dameFinPilaEstadoLetra(String estado, String cima, String letra){
		Iterator<AristaAP> it = this.automata.iterator();
		ArrayList<ArrayList<String>> devuelve = null;
		while (it.hasNext()){
			AristaAP aux = it.next();
			if (aux.getOrigen().equals(estado) && aux.getCimaPila().equals(cima) && aux.getEntradaSimbolos().contains(letra) ){
				if (devuelve == null) devuelve = new ArrayList<ArrayList<String>>();
				devuelve.add(aux.getSalidaPila());
			}
			
		}
		return devuelve;
	}
	
	public ArrayList<String> generaPalabras(int maximo){
		ArrayList<String> palabras = new ArrayList<String>(1);
		int i = 0;
		while (i < maximo){
			String añade = new String("");
			for (int j = 0; j < Math.pow(i,2) && j < 10; j++){
				for (int s = 0; s < i; s++){
					Random a = new Random();
					int z = a.nextInt(this.alfabetoPila.getListaLetras().size());
					añade+=this.alfabetoPila.getListaLetras().get(z);
				}
			}
			if (!palabras.contains(añade))
				palabras.add(añade);
		}
		
		return palabras;
	}

	public String toString() {
		return "Estados:"+estados.toString()+"\n"+"Estados Finales: "+estadosFinales.toString()+"\n"+
		"Estados Inicial: "+estadoInicial.toString() + "\n"+"Alfabeto: "+alfabeto.toString()+
		"\n"+"Alfabeto Pila: "+alfabetoPila.toString()+"\n"+"Determinista: "+apd+"\n"+automata.toString()
		+"\n"+"FondoPila: "+fondoPila+"\n";
	}

	public int dameTipo(){return 3;}
}