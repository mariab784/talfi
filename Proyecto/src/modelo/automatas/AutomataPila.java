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
/******************************************************************/
/**
 * @author anicetobacter
 *
 */
/**
 * @author anicetobacter
 *
 */
public class AutomataPila extends AutomataFND{
	
	private final  static String nombreAux = "aux";
	private final  static String iniNombreAux = "iniaux";
	//Atributos////////////////////////////////////////////////////
	private String estadoInicial;
	private ArrayList<String> estadosFinales;
	private Alfabeto alfabeto;
	private Alfabeto_Pila alfabetoPila;
	//private Alfabeto alfabetoMenosL;
	private ArrayList<String> estados;
	private ArrayList<AristaAP> automata;
	private HashMap<String,Coordenadas> coordenadasGraficas;
//	protected HashMap<String, HashMap</*String*/ArrayList<String>,ArrayList<String>>> aut;
//	protected HashMap<String,HashMap<String,HashMap<String,HashMap<ArrayList<String>,ArrayList<String>>>>> aut;
	private boolean apd;
	private Mensajero mensajero;
	
	private String lambda;
	private String fondoPila;
	



	private ArrayList<Integer> aristasQueDesapilan; // esto tocarlo tb en la parte visual
//	private ArrayList<Integer> aristasLambda;
	
//	private boolean[] visitados;
	
	private ArrayList<AristaAP> aristasPilaVacia;
	private String estadoPilaVacia;
	private String estadoIniPilaVacia;
	
	//private  ArrayList<CiclosYSoluciones> listaCiclos;
	//como en la arte grafica se meten al final no hay problema (creo)
	///////////////////////////////////////////////////////////////
	
	//Métodos//////////////////////////////////////////////////////
	//-------------------------------------------------------------
	public AutomataPila(){
		coordenadasGraficas=null;
		estados=new ArrayList<String>();
		estadosFinales=new ArrayList<String>();
		alfabeto=new Alfabeto_imp();
		
		//alfabetoMenosL=new Alfabeto_imp();
		automata=new ArrayList<AristaAP>();
		apd = true; ///////REVISAR PARA MODIFICARLO
		mensajero = Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		fondoPila = mensajero.devuelveMensaje("simbolos.cima",4);
		alfabetoPila = new AlfabetoPila_imp();
		alfabetoPila.aniadirLetra(fondoPila);
		aristasQueDesapilan = new ArrayList<Integer>();
//		aristasLambda = new ArrayList<Integer>();
		aristasPilaVacia = null;
		estadoPilaVacia = null;
//		aut=new HashMap<String, HashMap<String,HashMap<String,HashMap<ArrayList<String>,ArrayList<String>>>>>();	
	}
	//-------------------------------------------------------------
	public AutomataPila(String estadoI,ArrayList<String> estadosF,Alfabeto alf, Alfabeto_Pila alfPila, 
			ArrayList<String> est, ArrayList<AristaAP> aut){
		

		estadoInicial = estadoI;
		estadosFinales = estadosF;
		alfabeto = alf;
		alfabetoPila = alfPila;
		
		estados = est;
		automata = aut;
		
		apd = compruebaAPD();
		aristasQueDesapilan = new ArrayList<Integer>();
		aristasPilaVacia = new ArrayList<AristaAP>();
		
		mensajero = Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		fondoPila = mensajero.devuelveMensaje("simbolos.cima",4);
		
	}
	//-------------------------------------------------------------
public void anadeArista(AristaAP a){
		

			int i = existeTransicion(a);
			if ( i == -1 ) this.automata.add(a);
			anadeSimbolosAlf(a.getEntradaSimbolos());
			anadeSimbolosAlfPila(a.getCimaPila(),a.getSalidaPila());
	}
	
/******************************************************************************/
public void anadeSimbolosAlf(ArrayList<String> a){
	
	Iterator<String> itA = a.iterator();
	while(itA.hasNext()){
		String s = itA.next();
		if (!this.alfabeto.getListaLetras().contains(s) && !s.equals(lambda))
			this.alfabeto.aniadirLetra(s);
		
	}
	
}
/******************************************************************************/
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
/******************************************************************************/

@SuppressWarnings("unchecked")
private boolean compruebaAPD(){  
	//libro dice:
	/*
	 * d(q,a,X) tiene como maximo un elemento para cualkier q en Q, a en E o a = lambda, y X en T.
	 * Si d(q,a,X) no esta vacio para algun a en E, d(q,e,X) debe estar vacio
	 * */
	if (this.getAutomataPila().isEmpty()) return true;
	
	int i, j;
	i = 1; j = 1;
	
	Collections.sort(getAutomataPila());

	AristaAP a = this.getAutomataPila().get(0); //AristaAP aux = this.getAutomataPila().get(1);
	
	if ((a.getEntradaSimbolos().size() > 1) && (a.getEntradaSimbolos().contains(lambda))) return false;
	
	while (i < this.getAutomataPila().size()){
		
		AristaAP aux = this.getAutomataPila().get(i);

		if ((aux.getEntradaSimbolos().size() > 1) && (aux.getEntradaSimbolos().contains(lambda))) return false;
		
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
		
		//comparar a y aux

	return true;
}

/******************************************************************************/
//-------------------------------------------------------------
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

/*****************************************************/
private void combinarEntrada(AristaAP a, ArrayList<String> e){
	
	Iterator<String> it = e.iterator();
	while (it.hasNext()){
		String s = it.next();
		if (!a.getEntradaSimbolos().contains(s))
			a.getEntradaSimbolos().add(s);
	}
	
}
/*****************************************************/
private boolean contieneEntrada(AristaAP arista, AristaAP a){ 

	int tamA = a.getEntradaSimbolos().size(); 		
	int i = 0;
	while ( i < tamA){
		if (arista.getEntradaSimbolos().contains(a.getEntradaSimbolos().get(i))) return true;
		i++;
	}
	return false;
}
/*****************************************************/
private static boolean iguales(ArrayList<String> a, ArrayList<String> b){
	
	if (a.size() != b.size()) return false;
	
	Iterator<String> itA = a.iterator();
	Iterator<String> itB = b.iterator();
	
	while (itA.hasNext()){

		if ( !itA.next().equals(itB.next()) )return false;
	}
	return true;
	
}
/*****************************************************/
	//-------------------------------------------------------------
	/**
	 * Método que introduce nuevo autómata de pila.
	 * @param a , el nuevo autómata de pila.
	 */
	public void setAPNuevo(ArrayList<AristaAP> a) {

		this.automata = a;
	}
	//-------------------------------------------------------------
	/**
	 * Método que devuelve el booleano que indica si el ap es determinista
	 * @return apd , booleano que indica determinismo.
	 */
	public boolean getApd() {
		// TODO Auto-generated method stub
		return apd;
	}
	

	//-------------------------------------------------------------

	/**
	 * Método que devuelve el alfabeto del autómata de pila.
	 * @return alfabeto , el alfabeto de entrada.
	 */
	public Alfabeto getAlfabeto() {
		// TODO Auto-generated method stub
		return alfabeto;
	}
	

	//-------------------------------------------------------------
	/**
	 * Método que devuelve el estado inicial del autómata de pila.
	 * @return estadoInicial.
	 */
	public String getEstadoInicial() {
		// TODO Auto-generated method stub
		return estadoInicial;
	}
	//-------------------------------------------------------------
	/**
	 * Método que devuelve todos los estados del autómata de pila.
	 * @return estados.
	 */
	public ArrayList<String> getEstados() {
		// TODO Auto-generated method stub
		return estados;
	}
	//-------------------------------------------------------------
	/**
	 * Método que devuelve los estados finales del autómata de pila.
	 * @return estadosFinales.
	 */
	public ArrayList<String> getEstadosFinales() {
		// TODO Auto-generated method stub
		return estadosFinales;
	}
	//-------------------------------------------------------------
	public void insertaArista2(String origen,String destino,ArrayList<String> simbolos,String cima,ArrayList<String> salida) {

		AristaAP arist = new AristaAP(origen,destino);
		arist.setCimaPila(cima);
		arist.setSalida(salida);
		arist.setSimbolos(simbolos);
		anadeArista(arist);
		
	}
	//-------------------------------------------------------------
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
	//-------------------------------------------------------------
	/**
	 * Método que inserta un alfabeto de entrada en un autómata de pila.
	 * @param alfabeto.
	 */
	public void setAlfabeto(Alfabeto alfabeto) {
		// TODO Auto-generated method stub
		this.alfabeto = alfabeto;
	}
	
	
	//-------------------------------------------------------------
	/**
	 * Método que inserta el estado inicial de un autómata de pila.
	 * @param estado.
	 */
	public void setEstadoInicial(String estado) {
		// TODO Auto-generated method stub
		estadoInicial = estado;
	}
	//-------------------------------------------------------------
	/**
	 * Método que inserta una lista de estados en un autómata de pila.
	 * @param estados.
	 */
	public void setEstados(ArrayList<String> estados) {
		// TODO Auto-generated method stub
		this.estados = estados;	
	}
	//-------------------------------------------------------------
	/**
	 * Método que inserta una lista de estados finales en un autómata de pila.
	 * @param estados.
	 */
	public void setEstadosFinales(ArrayList<String> estados) {
		// TODO Auto-generated method stub
		this.estadosFinales = estados;	
	}
	//-------------------------------------------------------------
	/**
	 * Método que inserta un estado final de un autómata de pila.
	 * @param estado.
	 */
	public void setEstadoFinal(String estado) {
		// TODO Auto-generated method stub
		this.estadosFinales.add(estado);	
	}
	//-------------------------------------------------------------
	/**
	 * Método que inserta un estado (no final) en un autómata de pila.
	 * @param estado.
	 */
	public void setEstadoNoFinal(String estado) {
		// TODO Auto-generated method stub
		this.estados.add(estado);	
	}
	//-------------------------------------------------------------
	/**
	 * Método que devuelve las coordenadas de un estado.
	 * @param estado.
	 * @return coordenadas.
	 */
	public Coordenadas getCoordenadas(String estado) {
		// TODO Auto-generated method stub
		return coordenadasGraficas.get(estado);
	}
	//-------------------------------------------------------------
	/**
	 * Método que inserta nuevas coordenadas a un estado del autómata de pila.
	 * @param estado , cord.
	 */
	public void setCoordenadas(String estado, Coordenadas cord) {
		// TODO Auto-generated method stub 
		if (coordenadasGraficas==null) coordenadasGraficas=new HashMap<String,Coordenadas>();
		coordenadasGraficas.put(estado,cord);
	}
	//-------------------------------------------------------------
	/**
	 * Método que verifica si existem coordenadas.
	 * @return bool.
	 */
	public boolean hayCoordenadas() {
		// TODO Auto-generated method stub
		return (coordenadasGraficas!=null);
	}
	//-------------------------------------------------------------
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
			
			if(automata.get(i).contieneOrigen(estado)){
				estados.add(estado);
			}
			arista = automata.get(i);
			i++;
		}
		return estados;
    }
	//-------------------------------------------------------------
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
	//-------------------------------------------------------------
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
	//-------------------------------------------------------------
	/**
	 * Método que devuelve el autómata de pila en cuestión.
	 * @return automata.
	 */
	public ArrayList<AristaAP> getAutomataPila() {
		// TODO Auto-generated method stub
		return automata;
	}
	//-------------------------------------------------------------
	
/*	public HashMap<String, HashMap<String,HashMap<String,HashMap<ArrayList<String>,ArrayList<String>>>>>
 getAut(){
		
		return aut;
	}*/
	
	/**
	 * Método que devuelve el alfabeto de pila.
	 * @return alfabetoPila.
	 */
	public Alfabeto_Pila getAlfabetoPila() {
		return alfabetoPila;
	}
	//-------------------------------------------------------------
	/**
	 * Método que inserta el alfabeto de pila a un autómata de pila.
	 * @param alf.
	 */
	public void setAlfabetoPila(Alfabeto_Pila alf) {
		this.alfabetoPila = alf;
	}
	//-------------------------------------------------------------	

	private static boolean iguales(String a1, String a2){
		
		return a1.equals(a2);
	}

	/**
	 * Método que guarda la lista de estados destino segun un origen,cima de pila, y simbolo.
	 * @param palabra.
	 * @param boolean estadoFinal, true si acepta por estado final, false si es por pila vacia
	 * @return bool.
	 */
/*	private ArrayList<Integer> buscaEstados(String estado, String cima, String simbolo, boolean conSimbolo){
		/*Esto es un poco idea feliz. No se si seria mejor usar hashmap, y decidir si esta solo el
		 * simbolo, solo lambda, o ambos*/
/*		ArrayList<Integer> s = null;
		int i = 0;
/*		System.out.println("**datos entrada**");
		System.out.println("ESTADO PARAM ENTRADA: " + estado);
		System.out.println("CIMA PARAM ENTRADA: " + cima);
		System.out.println("SIMBOLO PARAM ENTRADA: " + simbolo);*/
	//	if (simbolo == null) return null; //CAMBIADO
/*		if (cima == null) return s;
		
		while (i < automata.size()){
			
			AristaAP a = automata.get(i);
			System.out.println("ARISTA BUCLE: " + a);
					System.out.println("ESTADO PARAM ENTRADA: " + estado);
			System.out.println("CIMA PARAM ENTRADA: " + cima);
			System.out.println("SIMBOLO PARAM ENTRADA: " + simbolo);
			if (a.getCimaPila() == null) System.out.println("NULLL");
			//else System.out.println("CIMA ARISTA: " + a.getCimaPila());
			boolean contiene = false; 
			if (conSimbolo){
				contiene = a.getEntradaSimbolos().contains(lambda) || a.getEntradaSimbolos().contains(simbolo);
			}
			else {conSimbolo = true;}
			if (contiene) System.out.println("contiene cierto");
			boolean b1 = iguales(estado,a.getOrigen());
			if (b1) System.out.println("b1 cierto");
			boolean b2 = iguales(cima,a.getCimaPila());
			if (b2) System.out.println("b2 cierto");
			if ( b1 && b2 && contiene){
				if (s == null) s = new ArrayList<Integer>();
				
				s.add(new Integer(i));
			}
			i++;
		}
		System.out.println("LISTAESTADOS DEVUELVE: " + s);
		return s;
	}*/

	//-------------------------------------------------------------------------------------------
	
	//suponemos que tiene al menos un simbolo
	
	/**
	 * Método que actualiza la pila segun la transicion de la arista.
	 * @param ArrayList<String> salidaPila, como se cambia la pila en la transicion
	 * @param ArrayList<String> pila, pila en el momento antes de cambiar
	 * @param String cima, cima de la pila antes de actualizarla
	 * @param int iPila, indice auxiliar que nos indica la posicion de la cima de la pila antes de actualizarla
	 * @return ArrayList<String> devuelve la pila ya actualizada para esta transicion concreta
	 */
/*	private ArrayList<String> actualizaPila( ArrayList<String> salidaPila, ArrayList<String> pila,
			String cima, int iPila){
		//por definicion simpre se desapila
		ArrayList<String> aux;
		aux = (ArrayList<String>)pila.clone();
		
		
/*		System.out.println("**datos entrada**");
		System.out.println("SALIDAPILA ENTRADA: " + salidaPila);
		System.out.println("PILA ENTRADA: " + pila);
		System.out.println("CIMA ENTRADA: " + cima);
		System.out.println("IPILA ENTRADA: " + iPila); */
		
/*		if (aux.isEmpty()) return null;
		
		aux.remove(iPila);
		
		//if (borrado.equals(fondoPila)) return null;
		//if (salidaPila.contains(lambda)) return aux;
/*		if ( (salidaPila.size() == 1) && salidaPila.contains(lambda)){
			if ((aux.size() == 0))return null;
			else return aux;
		}*/
	/*	if ( (salidaPila.size() == 1) && salidaPila.contains(lambda) ){ 
			aux.remove(iPila); 
			return aux;
		} */
		//if (salidaPila.contains(fondoPila)) return aux;
			
/*		int i = salidaPila.size()-1;
		String sim;
		if (!salidaPila.contains(lambda)){
			while (i >= 0){
				sim = salidaPila.get(i);
				aux.add(sim);
				i--;
			}
		}
//		System.out.println("pila actualizada: " + aux);
		/*		System.out.println("tamaño pila actualizada: " + aux.size());
		System.out.println("pila de entrada: " + pila);
		System.out.println("tamaño pila de entrada: " + pila.size());*/
		
/*		return aux;
		
	}*/

	
	
	//--------------------------------------------------------------------------------------------
/*	private boolean estaPorPila(ArrayList<Integer> listaEstados, ArrayList<String> pila, int iPila, String cimaPila,
			String palabra,	int iPalabra, String estado){
				
		if (listaEstados == null){
			
			if(pila == null || pila.isEmpty()){

				if (iPalabra /*>*//*== palabra.length() || iguales(palabra,lambda)){//cambiado > por ==
					System.out.println("BIEEEEEEEEEEEEEN"); return true;
				}
				else {System.out.println("TE PEINASSSSSSS NO TERMINAASTE DE PROCESAR LA CADENA"); return false;}
			}
			else{ 
				System.out.println("OOOOOOOOH LA PILA NO ESTA VACIA");
				return false;
			}
		}

				
		else{
					
			if(pila == null || pila.isEmpty()){
				if (iPalabra > palabra.length()){System.out.println("BIEEEEEEEEEEEEEN AKI SI"); return true;}
				else {System.out.println("TE PEINASSSSSSS NO TERMINAASTE DE PROCESAR LA CADENA"); return false;}
			}

			if (pila == null){ System.out.println("sniffFFFFFFFFFFFFFFFFFFFF"); return false;}

				ArrayList<Integer> listaAux = null;
				ArrayList<String> pilaAux;
				String estadoAux;
				int iPilaAux;
				String cimaPilaAux;
				Iterator<Integer> it = listaEstados.iterator();
				
				/*System.out.println("***INFORMACION DE ENTRADA***");
				System.out.println("LISTAESTADOS: " + listaEstados);
				System.out.println("PILA: " + pila);
				System.out.println("CIMAPILA: " + cimaPila);
				System.out.println("IPALABRA: " + iPalabra);
				System.out.println("ESTADO: " + estado);
				System.out.println("***	FIN INFORMACION DE ENTRADA***");*/
				
	/*			while ( it.hasNext() ){ 

					String simboloAux = null;
					Integer indice = it.next();
					//AristaAP a = automata.get(indice);
			//		System.out.println("IPALABRA bucle: " + iPalabra);
					
					/*System.out.println("***INFORMACION DE BUCLE	***");
					System.out.println("LISTAESTADOS: " + listaEstados);
					System.out.println("PILA: " + pila);
					System.out.println("CIMAPILA: " + cimaPila);
					System.out.println("IPALABRA: " + iPalabra);
					System.out.println("ESTADO: " + estado);
					System.out.println("***	FIN INFORMACION DE BUCLE***");*/


		//			System.out.println("INDICE BUCLE: " + indice.toString());
	/*				AristaAP a = automata.get(indice.intValue());
		//			System.out.println("ARISTA BUCLE: " + a);
					
					
					if (a.getEntradaSimbolos().contains(lambda)){
							
						System.out.println("ENTRA de pila");
						estadoAux = a.getDestino();
						if (iPalabra < palabra.length()){
							System.out.println("IPALABRA LAMBDA VALE: " + iPalabra);
							simboloAux = "" + palabra.charAt(iPalabra);						
						}
						else simboloAux = null;
														
						pilaAux = actualizaPila( automata.get(indice).getSalidaPila(),pila,cimaPila,iPila  );
										
						if (pilaAux == null || pilaAux.isEmpty()){ iPilaAux = 0; cimaPilaAux = null;}
						else {iPilaAux = pilaAux.size()-1; cimaPilaAux = pilaAux.get(iPilaAux);}
						
						listaAux = buscaEstados(estadoAux, cimaPilaAux, simboloAux,true);
						
						if (pilaAux == null){
							System.out.println("PILAVACIA!!!!"); 
										
							if (iPalabra == palabra.length() ){ 
								System.out.println("PILA VACIA!!!!");
																	}
							else {
								System.out.println("TE PEINASSSSSSS NO TERMINAASTE DE PROCESAR LA CADENA"); 
								return false;
							}

										
						}
									
						if (iguales(palabra,lambda)){
							System.out.println("YYAYAYYA SON IGUALES");
							if ( pilaAux.isEmpty()){
								System.out.println("BIEEEEEEEEEEEEEN"); return true;
							}
							else {										
								//System.out.println("LO MISMO ES KE HAY UN CICLO Y NUNCA ACABARAS");
								if (aristasQueDesapilan.isEmpty()){
									System.out.println("NO HAY ARISTAS KE DESAPILEN");
									return false;
								}
								
								//buscamos con un arrayList de enteros auxiliar las aristas ke pueden
								//formar ciclo desde el estado en el ke estamos, y devolvemos las posibles
								//salidas??
							}  //XXX
						}
										
						if (iPalabra > palabra.length() && estadosFinales.contains(estado)){
							System.out.println("BIEEEEEEEEEEEEEN 2.0"); 
							return true; 
							
						}
										
						if( estaPorPila(listaAux, pilaAux , iPilaAux, cimaPilaAux ,palabra,
												iPalabra,estadoAux) ) return true; //REVISAR
					}
					/*else*//*if (!(a.getEntradaSimbolos().contains(lambda)) || 
							((a.getEntradaSimbolos().contains(lambda)) &&	(a.getEntradaSimbolos().size() != 1) ) ){
		
						estadoAux = a.getDestino();
						if (iPalabra != palabra.length() -1){ // CAMBIADO, ANTES SIN -1
							simboloAux = "" + palabra.charAt(iPalabra+1);	//cambiado antes iPalabra					
						}
						else simboloAux = null;
										
						pilaAux = actualizaPila( automata.get(indice).getSalidaPila(),pila,cimaPila,iPila  );
							
						if (pilaAux == null || pilaAux.isEmpty()){ iPilaAux = 0; cimaPilaAux = null;}
						else {iPilaAux = pilaAux.size()-1; cimaPilaAux = pilaAux.get(iPilaAux);}

						listaAux = buscaEstados(estadoAux, cimaPilaAux, simboloAux,true);
							

						if (pilaAux == null){
							System.out.println("PILAVACIA!!!!"); 
							
							if (iPalabra == palabra.length() ){ 
								System.out.println("PILA VACIA!!!!");
							}
							else {
								System.out.println("TE PEINASSSSSSS NO TERMINAASTE DE PROCESAR LA CADENA"); 
								return false;
							}							
						}
							
						/*System.out.println("***INFORMACION DE REC***");
						System.out.println("LISTAESTADOS: " + listaAux);
						System.out.println("PILA: " + pilaAux);
						System.out.println("CIMAPILA: " + cimaPilaAux);
						System.out.println("IPALABRA: " + (iPalabra+1));
						System.out.println("ESTADO: " + estadoAux);
						System.out.println("***	FIN INFORMACION DE REC***");*/
						
						
/*						if( estaPorPila(listaAux, pilaAux , iPilaAux, cimaPilaAux ,palabra,
									iPalabra+1,estadoAux) ) return true; //REVISAR			
					}
				}//llave while
		}
				
				
		return false;//para ke no pete
	}*/
	
	
	
/*	private boolean estaPorEstado(ArrayList<Integer> listaEstados, ArrayList<String> pila, int iPila, String cimaPila,
	String palabra,	int iPalabra, String estado){
		
		if (listaEstados == null){
			if (iguales(palabra,lambda) ) System.out.println("IGUALEEES");
			System.out.println("IPALABRA: " + iPalabra);
			if (iPalabra /*>*//*== palabra.length() || iguales(palabra,lambda)){ //cambiado > por ==
				if ( estadosFinales.contains(estado)){ System.out.println("BIEEEEEEEEEEEEEN"); return true;}
				else {System.out.println("OOOOOOOOH ACABASTE EN UN ESTADO QUE NO ACEPTA estado"); return false;}
			}
			else {System.out.println("TE PEINASSSSSSS NO TERMINAASTE DE PROCESAR LA CADENA"); return false;}
			
		}

		else{

			//creo ke esto nunca se cumple
			if (pila == null){ System.out.println("sniffFFFFFFFFFFFFFFFFFFFF"); return false;}
			
			System.out.println("***INFORMACION DE ENTRADA***");
			System.out.println("LISTAESTADOS: " + listaEstados);
			System.out.println("PILA: " + pila);
			System.out.println("CIMAPILA: " + cimaPila);
			System.out.println("IPALABRA: " + iPalabra);
			System.out.println("ESTADO: " + estado);
			System.out.println("***	FIN INFORMACION DE ENTRADA***");
			
			ArrayList<Integer> listaAux = null;
			ArrayList<String> pilaAux;
			String estadoAux;
			int iPilaAux;
			String cimaPilaAux;
			Iterator<Integer> it = listaEstados.iterator();
			while ( it.hasNext() ){

				String simboloAux = null;
				Integer indice = it.next();
				
				System.out.println("IPALABRA bucle: " + iPalabra);
				
				System.out.println("***INFORMACION DE BUCLE	***");
				System.out.println("LISTAESTADOS: " + listaEstados);
				System.out.println("PILA: " + pila);
				System.out.println("CIMAPILA: " + cimaPila);
				System.out.println("IPALABRA: " + iPalabra);
				System.out.println("ESTADO: " + estado);
				System.out.println("***	FIN INFORMACION DE BUCLE***");
				
				//AÑADIDO
			/*	if (visitados != null && (visitados[this.estados.indexOf(estado)]))
					if(it.hasNext())
						indice = it.next();*/
				
				
			//	System.out.println("INDICE BUCLE: " + indice.toString());
/*				AristaAP a = automata.get(indice.intValue());
			//	System.out.println("ARISTA BUCLE: " + a);
															
				if (a.getEntradaSimbolos().contains(lambda) ){

					
					//AÑADIDO
					//if (visitados == null) visitados = new boolean[estados.size()];
					
					
					estadoAux = a.getDestino();

					
					if (iPalabra < palabra.length()){
						System.out.println("IPALABRA LAMBDA VALE: " + iPalabra);
						simboloAux = "" + palabra.charAt(iPalabra);						
					}
					else simboloAux = null;

					pilaAux = actualizaPila( automata.get(indice).getSalidaPila(),pila,cimaPila,iPila  );
								
					if (pilaAux == null || pilaAux.isEmpty()){ iPilaAux = 0; cimaPilaAux = null;}
					else {iPilaAux = pilaAux.size()-1; cimaPilaAux = pilaAux.get(iPilaAux);}
								
					listaAux = buscaEstados(estadoAux, cimaPilaAux, simboloAux,true);
								

					//AÑADIDO
					//	visitados[this.estados.indexOf(estadoAux)] = true;
					
					
					
					if (iguales(palabra,lambda)){
						if ( estadosFinales.contains(estado)){ 
							System.out.println("BIEEEEEEEEEEEEEN"); 
							return true; 
						}
									
					}
					
					if (iPalabra > palabra.length() && estadosFinales.contains(estado)){
						System.out.println("BIEEEEEEEEEEEEEN 2.0"); 
						return true; 
						
					}

					
					
					

					
					if( estaPorEstado(listaAux, pilaAux , iPilaAux, cimaPilaAux ,palabra,
										iPalabra,estadoAux) ) return true; //REVISAR
				}
				
				
				/*else*//*if (!(a.getEntradaSimbolos().contains(lambda)) || 
					((a.getEntradaSimbolos().contains(lambda)) &&	(a.getEntradaSimbolos().size() != 1) ) ){
					
					
					
					estadoAux = a.getDestino();
					if (iPalabra != palabra.length() -1){ // CAMBIADO, ANTES SIN -1
						simboloAux = "" + palabra.charAt(iPalabra+1); 	//iPalabra cambiado					
					}
					else simboloAux = null;

					pilaAux = actualizaPila( automata.get(indice).getSalidaPila(),
							pila,cimaPila,iPila  );
					
					if (pilaAux == null || pilaAux.isEmpty()){ iPilaAux = 0; cimaPilaAux = null;}
					else {iPilaAux = pilaAux.size()-1; cimaPilaAux = pilaAux.get(iPilaAux);}
					
					
					listaAux = buscaEstados(estadoAux, cimaPilaAux, simboloAux,true);
					
					System.out.println("***INFORMACION DE REC***");
					System.out.println("LISTAESTADOS: " + listaAux);
					System.out.println("PILA: " + pilaAux);
					System.out.println("CIMAPILA: " + cimaPilaAux);
					System.out.println("IPALABRA: " + (iPalabra+1));
					System.out.println("ESTADO: " + estadoAux);
					System.out.println("***	FIN INFORMACION DE REC***");
					
					if( estaPorEstado(listaAux, pilaAux , iPilaAux, cimaPilaAux ,palabra,
							iPalabra+1,estadoAux) ) return true; //REVISAR
					//iPalabra cambiado
				}
			
				
			
			}

		}
		
		
		return false;//para ke no pete
	}*/
	

	
	
/*	public void recogeAristasEspeciales(){
		//metodo para ver si tenemos aristas que desapilar alguna vez
		// y para guardar aristas con transicion lamdba, para comprobar ciclos
		
		int i = 0;
		while (i < this.automata.size() ){
			
			AristaAP a = automata.get(i);
			if (a.getSalidaPila().contains(lambda)) this.aristasQueDesapilan.add(new Integer(i));
	
			if (a.getEntradaSimbolos().contains(lambda) && (a.getEntradaSimbolos().size() == 1)) this.aristasLambda.add(new Integer(i));
			
			
			i++;
		}
//		System.out.println("ARISTAS lambda: " + this.aristasLambda);
	}*/
	
	public String getFondoPila(){return fondoPila;}
	
	/**
	 * Método que convierte un AP que acepta por estado final en un AP que acepte por pila vacia.
	 */

	public void setEstadoPilaVacia(String s){estadoPilaVacia = s;}
	public void setEstadoIniPilaVacia(String s){estadoIniPilaVacia = s;}
	public void setFondoPila(String s){fondoPila = s;}
	
	public String getEstadoPilaVacia(){return estadoPilaVacia;}
	public String getEstadoIniPilaVacia(){return estadoIniPilaVacia;}
//	public String getFondoPila(){return fondoPila;}
	
	@SuppressWarnings("unchecked")
	public AutomataPila convertirPilaVacia(){
		//String estadoI,ArrayList<String> estadosF,Alfabeto alf, Alfabeto_Pila alfPila, 
		//ArrayList<String> est, ArrayList<AristaAP> aut
//		System.out.println("this: " + this);
		if(mensajero == null) mensajero = Mensajero.getInstancia();
		String fondoPilaAux = mensajero.devuelveMensaje("simbolos.cimaVacia",4);
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		fondoPila = mensajero.devuelveMensaje("simbolos.cima",4);

		AlfabetoPila_imp nalfPila = new AlfabetoPila_imp();
		ArrayList<String> letrasAlfPila = (ArrayList<String>) this.getAlfabetoPila().getListaLetras().clone(); 
		letrasAlfPila.add(fondoPilaAux);
		nalfPila.setLetras(letrasAlfPila);
		
		AutomataPila aut = new AutomataPila(this.getEstadoInicial(),this.getEstadosFinales(),this.getAlfabeto(),
				nalfPila,(ArrayList<String>) this.getEstados().clone(),(ArrayList<AristaAP>) this.getAutomataPila().clone());
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
		
		aut.getEstados().add(aut.getEstadoPilaVacia());//añadido
		aristasPilaVacia = new ArrayList<AristaAP>();
		String simboloPila;// = this.alfabetoPila.getListaLetras().get(j);
		
		if (!this.getEstadosFinales().isEmpty()){
		String est = this.getEstadosFinales().get(i);
//		System.out.println("est final: " + est);
		int tamAlfpila = this.alfabetoPila.getListaLetras().size();
//		System.out.println("tamAlfpila : " + tamAlfpila);
		int tamEstFin = this.getEstadosFinales().size();
//		System.out.println("tamEstFin : " + tamEstFin);
		if (mensajero == null)
			mensajero = Mensajero.getInstancia();
//		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		
		
		
		while ( (i < tamEstFin) && (j < tamAlfpila) ){
			
			//(int x,int y,int fx, int fy,String origen,String destino)
			AristaAP a = new AristaAP(0,0,0,0,est,aut.getEstadoPilaVacia());
			simboloPila = this.alfabetoPila.getListaLetras().get(j);
			a.anadirSimbolo(lambda);
			a.setCimaPila(simboloPila);
			ArrayList<String> trans = new ArrayList<String>();
			trans.add(lambda);
			a.setSalida(trans);
			aut.getAutomataPila().add(a);
			aut.aristasPilaVacia.add(a);
			//this.aristasPilaVacia.add(a);
			a = new AristaAP(0,0,0,0,aut.getEstadoPilaVacia(),aut.getEstadoPilaVacia());
			a.setOrigen(aut.getEstadoPilaVacia());
			a.anadirSimbolo(lambda);
			a.setCimaPila(simboloPila);
			a.setSalida(trans);
			//this.aristasPilaVacia.add(a);
			
			//aut.aristasPilaVacia.add(a); quitado
			
			aut.getAutomataPila().add(a);
			
			
			if (j == (tamAlfpila -1)) {
				
				 
				if (i != (tamEstFin -1)){
					i++;
					est = this.getEstadosFinales().get(i);
					j = 0;
				}
				else {j++; }
			}
			else {j++; }
//			System.out.println("I: " + i + " J : " + j);
			
		}
		
		}
//		System.out.println("ARISTAS AUXILIARES: " + this.aristasPilaVacia);
		System.out.println("AUTOMATA ORIGINAL: \n" + this);
		System.out.println("AUTOMATA NUEVO: \n" + aut);
		return aut;
	}
	
	
	/**
	 * Método que verifica si una palabra es reconocida por el autómata de pila.
	 * @param palabra.
	 * @param boolean estadoFinal, true si acepta por estado final, false si es por pila vacia
	 * @return bool.
	 */
	//y si devolvemos un entero mejor??
	public /*int*/void reconocePalabra(final String palabra){

//REVISAR ESTO
//		if (palabra.equals(lambda) && estadosFinales.contains(estadoInicial) )  return true;
		try{

	//dentro de compruebaAPD hace un sort() a las aristas		
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

	//-------------------------------------------------------------

	
	/*
	 * Metodo que determinara el comportamiendo de dos automatas.
	 * Metodo que dada una lista de palabras que el usuario cree que se reconocen, comprueba si pertenecen o no a 
	 * los dos automatas pasados por parametro.
	 * */
	public static void compruebaPalabras(AutomataPila aut1, AutomataPila aut2, ArrayList<String> listaPalabras){
		

	}
	public String toString() {
		return "Estados:"+estados.toString()+"\n"+"Estados Finales: "+estadosFinales.toString()+"\n"+
		"Estados Inicial: "+estadoInicial.toString() + "\n"+"Alfabeto: "+alfabeto.toString()+
		"\n"+"Alfabeto Pila: "+alfabetoPila.toString()+"\n"+"Determinista: "+apd+"\n"+automata.toString()
		+"\n"+"FondoPila: "+fondoPila+"\n";
	}
}
/******************************************************************/
