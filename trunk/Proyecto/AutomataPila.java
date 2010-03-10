/**
 * 
 */
package modelo.automatas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import modelo.AutomatasException;

import com.sun.servicetag.SystemEnvironment;

import accesoBD.Mensajero;

import vista.vistaGrafica.AristaAP;
import vista.vistaGrafica.events.OyenteArista;



/**
 * Clase que implementa la funcionalidad de los automatas de pila
 *  @author Roc�o Barrig�ete, Mario Huete, Luis San Juan 
 *
 */
/******************************************************************/
public class AutomataPila extends AutomataFND{
	
	//Atributos////////////////////////////////////////////////////
	private String estadoInicial;
	private ArrayList<String> estadosFinales;
	private Alfabeto alfabeto;
	private Alfabeto_Pila alfabetoPila;
	//private Alfabeto alfabetoMenosL;
	private ArrayList<String> estados;
	private ArrayList<AristaAP> automata;
	private HashMap<String,Coordenadas> coordenadasGraficas;
	private boolean apd;
	
	private String lambda;
	private String fondoPila;
	
	private boolean porEstado;
	private boolean porPila;

	private ArrayList<Integer> aristasQueDesapilan; // esto tocarlo tb en la parte visual
	private ArrayList<Integer> aristasLambda;
	
//	private boolean[] visitados;
	
	private static ArrayList<Boolean> result1;
	private static ArrayList<Boolean> result2;
	
	//private  ArrayList<CiclosYSoluciones> listaCiclos;
	//como en la arte grafica se meten al final no hay problema (creo)
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
		apd = true; ///////REVISAR PARA MODIFICARLO
		Mensajero mensajero = Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		fondoPila = mensajero.devuelveMensaje("simbolos.cima",4);
		aristasQueDesapilan = new ArrayList<Integer>();
		aristasLambda = new ArrayList<Integer>();
		
//		visitados = null;
	}
	//-------------------------------------------------------------
	public AutomataPila(String estadoI,ArrayList<String> estadosF,Alfabeto alf, Alfabeto_Pila alfPila, 
			ArrayList<String> est, ArrayList<AristaAP> aut, boolean det){
		

		estadoInicial = estadoI;
		estadosFinales = estadosF;
		alfabeto = alf;
		alfabetoPila = alfPila;
		
		estados = est;
		automata = aut;
		
		apd = det;
		aristasQueDesapilan = new ArrayList<Integer>();
		
	}
	//-------------------------------------------------------------
public void anadeArista(AristaAP a){
		
	//	try{
			int i = existeTransicion(a);
			if ( i == -1 ) this.automata.add(a);
		/*	else if (i == -2){
				Mensajero m=Mensajero.getInstancia();
				throw new AutomatasException(m.devuelveMensaje("canvas.notransvalida",2));
			}*/
			//Collections.sort(getAutomataPila());
	//		if (apd) apd = compruebaAPD(a); //REVISAR AQUI Y EN AUTOMATA CANVAS KE ES LO MISMO
			
	//	}								//cambiar tb lo de mensajes en ingles
	/*	catch (AutomatasException ex){
			JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
		}*/
	}
	
/******************************************************************************/
//cambiar a privado luego!!!!!!!!!!
@SuppressWarnings("unchecked")
public boolean compruebaAPD(){  //usar contieneEntrada
	//buscaEstados(String estado, String cima, String simbolo, boolean conSimbolo)
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
//		System.out.println("ARISTA A: " + a);
//		System.out.println("ARISTA AUX: " + aux);
		if ((aux.getEntradaSimbolos().size() > 1) && (aux.getEntradaSimbolos().contains(lambda))) return false;
		
		if ( iguales(a.getOrigen(),aux.getOrigen()) ){
			
			if (!iguales(a.getCimaPila(),aux.getCimaPila())) i++;
			// aqui estariamos con dos aristas ke tienen el mismo origen y la misma cima
			// nos keda mirar si hay mas de una posibilidad para moverse
			// puede ser ke ambos tengan un mismo simbolo, o que haya lambdas, no?
			else{
				if (this.contieneEntrada(a, aux)) return false;
				//if (iguales(a.getSalidaPila(),aux.getSalidaPila())) return false;
			//contieneEntrada compara si algunas de las entradas de aux esta en las entradas de a
			//si es false es ke no comparten simbolos, pero y si una o ambas contiene lambda?
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
				
				
			//	if (contieneEntrada(aux, a)) return -2;
				
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
	 * M�todo que introduce nuevo aut�mata de pila.
	 * @param a , el nuevo aut�mata de pila.
	 */
	public void setAPNuevo(ArrayList<AristaAP> a) {

		this.automata = a;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que devuelve el booleano que indica si el ap es determinista
	 * @return apd , booleano que indica determinismo.
	 */
	public boolean getApd() {
		// TODO Auto-generated method stub
		return apd;
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
		ArrayList<String> simbolos = null;
		if (indice != -1){
			simbolos = automata.get(indice).getEntradaSimbolos();
		}
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

		
		if (!apd){
			apd = ( simbolos.size() >1 ) && simbolos.contains(lambda);
		}
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
	 * M�todo que devuelve una lista con la salida de la transici�n entre dos
	 * estados que se pasan como par�metros.
	 * @param origen , estado de inicio para buscar la salida.
	 * @param destino , estado de finalizaci�n para buscar la salida.
	 * @return estados , ArrayList con la salida que tiene la transici�n.
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
	 * M�todo que devuelve el aut�mata de pila en cuesti�n.
	 * @return automata.
	 */
	public ArrayList<AristaAP> getAutomataPila() {
		// TODO Auto-generated method stub
		return automata;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que devuelve el alfabeto de pila.
	 * @return alfabetoPila.
	 */
	public Alfabeto_Pila getAlfabetoPila() {
		return alfabetoPila;
	}
	//-------------------------------------------------------------
	/**
	 * M�todo que inserta el alfabeto de pila a un aut�mata de pila.
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
	 * M�todo que guarda la lista de estados destino segun un origen,cima de pila, y simbolo.
	 * @param palabra.
	 * @param boolean estadoFinal, true si acepta por estado final, false si es por pila vacia
	 * @return bool.
	 */
	private ArrayList<Integer> buscaEstados(String estado, String cima, String simbolo, boolean conSimbolo){
		/*Esto es un poco idea feliz. No se si seria mejor usar hashmap, y decidir si esta solo el
		 * simbolo, solo lambda, o ambos*/
		ArrayList<Integer> s = null;
		int i = 0;
/*		System.out.println("**datos entrada**");
		System.out.println("ESTADO PARAM ENTRADA: " + estado);
		System.out.println("CIMA PARAM ENTRADA: " + cima);
		System.out.println("SIMBOLO PARAM ENTRADA: " + simbolo);*/
	//	if (simbolo == null) return null; //CAMBIADO
		if (cima == null) return s;
		
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
	}

	//-------------------------------------------------------------------------------------------
	
	//suponemos que tiene al menos un simbolo
	
	/**
	 * M�todo que actualiza la pila segun la transicion de la arista.
	 * @param ArrayList<String> salidaPila, como se cambia la pila en la transicion
	 * @param ArrayList<String> pila, pila en el momento antes de cambiar
	 * @param String cima, cima de la pila antes de actualizarla
	 * @param int iPila, indice auxiliar que nos indica la posicion de la cima de la pila antes de actualizarla
	 * @return ArrayList<String> devuelve la pila ya actualizada para esta transicion concreta
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<String> actualizaPila( ArrayList<String> salidaPila, ArrayList<String> pila,
			String cima, int iPila){
		//por definicion simpre se desapila
		ArrayList<String> aux;
		aux = (ArrayList<String>)pila.clone();
		
		
/*		System.out.println("**datos entrada**");
		System.out.println("SALIDAPILA ENTRADA: " + salidaPila);
		System.out.println("PILA ENTRADA: " + pila);
		System.out.println("CIMA ENTRADA: " + cima);
		System.out.println("IPILA ENTRADA: " + iPila); */
		
		if (aux.isEmpty()) return null;
		
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
			
		int i = salidaPila.size()-1;
		String sim;
		if (!salidaPila.contains(lambda)){
			while (i >= 0){
				sim = salidaPila.get(i);
				aux.add(sim);
				i--;
			}
		}
//		System.out.println("pila actualizada: " + aux);
		/*		System.out.println("tama�o pila actualizada: " + aux.size());
		System.out.println("pila de entrada: " + pila);
		System.out.println("tama�o pila de entrada: " + pila.size());*/
		
		return aux;
		
	}

	
	
	//--------------------------------------------------------------------------------------------
	private boolean estaPorPila(ArrayList<Integer> listaEstados, ArrayList<String> pila, int iPila, String cimaPila,
			String palabra,	int iPalabra, String estado){
				
		if (listaEstados == null){
			
			if(pila == null || pila.isEmpty()){

				if (iPalabra /*>*/== palabra.length() || iguales(palabra,lambda)){//cambiado > por ==
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
				
				while ( it.hasNext() ){ 

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
					AristaAP a = automata.get(indice.intValue());
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
					/*else*/if (!(a.getEntradaSimbolos().contains(lambda)) || 
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
						
						
						if( estaPorPila(listaAux, pilaAux , iPilaAux, cimaPilaAux ,palabra,
									iPalabra+1,estadoAux) ) return true; //REVISAR			
					}
				}//llave while
		}
				
				
		return false;//para ke no pete
	}
	
	
	
	private boolean estaPorEstado(ArrayList<Integer> listaEstados, ArrayList<String> pila, int iPila, String cimaPila,
	String palabra,	int iPalabra, String estado){
		
		if (listaEstados == null){
			if (iguales(palabra,lambda) ) System.out.println("IGUALEEES");
			System.out.println("IPALABRA: " + iPalabra);
			if (iPalabra /*>*/== palabra.length() || iguales(palabra,lambda)){ //cambiado > por ==
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
				
				//A�ADIDO
			/*	if (visitados != null && (visitados[this.estados.indexOf(estado)]))
					if(it.hasNext())
						indice = it.next();*/
				
				
			//	System.out.println("INDICE BUCLE: " + indice.toString());
				AristaAP a = automata.get(indice.intValue());
			//	System.out.println("ARISTA BUCLE: " + a);
															
				if (a.getEntradaSimbolos().contains(lambda) ){

					
					//A�ADIDO
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
								

					//A�ADIDO
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
				
				
				/*else*/if (!(a.getEntradaSimbolos().contains(lambda)) || 
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
	}
	

	
	
	public void recogeAristasEspeciales(){
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
	}
	
	/**
	 * Recoloca primero las aristas sin \, y luego las ke tienen \
	 * (Metodo en pruebas, tambien en un futuro deberia romper los ciclos)
	 */
	private void organizarAristas(){
		
		//boolean conLambda = false;
//		System.out.println("ARISTAS: " + this.automata);
		//System.out.println("ARISTAS ke desapilan: " + this.aristasQueDesapilan);
//		System.out.println("ARISTAS lambda: " + this.aristasLambda);
		
		//System.out.println("ARISTAS iniciales: " + this.automata);
		Iterator<Integer> it = aristasLambda.iterator(); int i = 0;
		AristaAP aristAux = null; Integer indice = null; //ArrayList<AristaAP> autAux = new ArrayList<AristaAP>();
		int centinel = 0;
		while (it.hasNext()){
			
			indice = it.next();
			i = indice.intValue() - centinel;
			
				
			aristAux = automata.get(i);
			
			automata.remove(i);
			//System.out.println("ARISTAS quitado: " + this.automata);
			automata.add(aristAux);
			//System.out.println("ARISTAS puesto: " + this.automata);
			centinel++;
		}
		
		//System.out.println("ARISTAS final: " + this.automata);
		//System.out.println("ARISTAS ke desapilan: " + this.aristasQueDesapilan);
//		System.out.println("ARISTAS lambda: " + this.aristasLambda);
	}
	
	/**
	 * M�todo que verifica si una palabra es reconocida por el aut�mata de pila.
	 * @param palabra.
	 * @param boolean estadoFinal, true si acepta por estado final, false si es por pila vacia
	 * @return bool.
	 */
	//y si devolvemos un entero mejor??
	public int reconocePalabra(final String palabra){

//REVISAR ESTO
//		if (palabra.equals(lambda) && estadosFinales.contains(estadoInicial) )  return true;
		try{
			apd = compruebaAPD();
		if (this.apd)System.out.println("DETERMINISTA!!!");
		else System.out.println("SH*T!!!");
		recogeAristasEspeciales();
//		if (!( this.aristasLambda.isEmpty() ) )organizarAristas();
		
/*		System.out.println("ESTADOS: " + this.estados);
		
		System.out.println("ARISTAS: " + this.automata);*/
		System.out.println("ESTADOS: " + getEstados());
		System.out.println("ARISTAS : " + getAutomataPila());
		System.out.println("ARISTAS ke desapilan: " + this.aristasQueDesapilan);
		System.out.println("ARISTAS lambda: " + this.aristasLambda);
		ArrayList<String> pila = new ArrayList<String>();
		pila.add(fondoPila);
		String estado = estadoInicial;
		ArrayList<Integer> listaEstados;
		int iPalabra = 0;
		String simbolo = "" + palabra.charAt(iPalabra);
		listaEstados = buscaEstados(estado,fondoPila,simbolo,true);
		//System.out.println("LISTA ESTADOS INICIAL: " + listaEstados);
		//mirar porque si hubiera lambda no estoy segura,a parte no se si va
		//esta puesto como si no hubiera, de ahi el segundo 1 
		
		
		
		if (this.estadosFinales.isEmpty()) porEstado = false;
		else porEstado = estaPorEstado(listaEstados,pila,0,fondoPila,palabra,/*1*/0,estado);
		
		if (this.aristasQueDesapilan.isEmpty()) porPila = false;
		else porPila = 	estaPorPila(listaEstados,pila,0,fondoPila,palabra,/*1*/0,estado);
		
		if (porEstado && porPila){System.out.println("ESTADO Y PILA"); return 3;}
		else if (porEstado && !porPila){System.out.println("ESTADO"); return 2;}
		else if (!porEstado && porPila){System.out.println("PILA"); return 1;}
		else/*if (!porEstado && !porPila)*/{System.out.println("NANAI"); return 0;}
		
		//return false;
		
			 //XXX PARA KE NO PETE!!!! PENSANDO EN DEVOLVER UN NUMERO MEJOR KE BOOLEAN
		}
		catch (StackOverflowError e){System.out.println("FALLO POR CICLOS");return -1;}
	}
	//-------------------------------------------------------------

	
	/*
	 * Metodo que determinara el comportamiendo de dos automatas.
	 * Metodo que dada una lista de palabras que el usuario cree que se reconocen, comprueba si pertenecen o no a 
	 * los dos automatas pasados por parametro.
	 * */
	public static void compruebaPalabras(AutomataPila aut1, AutomataPila aut2, ArrayList<String> listaPalabras){
		
		Iterator<String> it = listaPalabras.iterator();
		result1 = new ArrayList<Boolean>();
		result2 = new ArrayList<Boolean>();
		String pal = null; int r1; int r2;
		aut1.recogeAristasEspeciales(); aut2.recogeAristasEspeciales();
		while (it.hasNext()){
			
			pal = it.next();
			
			r1 = aut1.reconocePalabra(pal);
			if (r1 != 0)/*acepta*/ result1.add(new Boolean(true));
			else result1.add(new Boolean(false));
	
			
			
			r2 = aut2.reconocePalabra(pal);
			if (r2 != 0)/*acepta*/ result2.add(new Boolean(true));
			else result2.add(new Boolean(false));

		}
			System.out.println("Lista pal: " + listaPalabras);
			System.out.println("Lista aut1: " + result1);
			System.out.println("Lista aut2: " + result2);
		
	}
}
/******************************************************************/
