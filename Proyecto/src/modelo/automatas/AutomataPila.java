/**
 * 
 */
package modelo.automatas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.sun.servicetag.SystemEnvironment;

import accesoBD.Mensajero;

import vista.vistaGrafica.AristaAP;



/**
 * Clase que implementa la funcionalidad de los automatas de pila
 *  @author Rocío Barrigüete, Mario Huete, Luis San Juan 
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
	private boolean determinista;
	
	private String lambda;
	private String fondoPila;
	
	private boolean porEstado;
	private boolean porPila;

	private ArrayList<Integer> aristasQueDesapilan; // esto tocarlo tb en la parte visual
	private ArrayList<Integer> aristasLambda;
	
	private static ArrayList<Boolean> result1;
	private static ArrayList<Boolean> result2;
	
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
		determinista = false; ///////REVISAR PARA MODIFICARLO
		Mensajero mensajero = Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		fondoPila = mensajero.devuelveMensaje("simbolos.cima",4);
		aristasQueDesapilan = new ArrayList<Integer>();
		aristasLambda = new ArrayList<Integer>();
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
		
		determinista = det;
		aristasQueDesapilan = new ArrayList<Integer>();
		
	}
	//-------------------------------------------------------------
	public void anadeArista(AristaAP a){
		
		this.automata.add(a);
		//System.out.println("ARISTA AÑADIDA: " + a);
		//int i = automata.size()-1;
		//System.out.println("COMPROBAMOS KE ES LA ULTIMA: " + automata.get(i));
		/*if (a.getSalidaPila().contains(lambda)){
			this.aristasQueDesapilan.add( automata.size()-1 );
			//System.out.println("EN LAS KE DESAPILAN ESTA: " + aristasQueDesapilan.get(0));
		}*/
		
	//sobre cuando decir si es determinista no estoy segura, lo mismo tambien actualizarlo en otro lugar mas
		
		if ( (a.getEntradaSimbolos().size() > 1 ) && a.getEntradaSimbolos().contains(lambda) )
			determinista = false;
		else determinista = true;
	}
	
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
	 * Método que devuelve el alfabeto del autómata de pila.
	 * @return alfabeto , el alfabeto de entrada.
	 */
	public Alfabeto getAlfabeto() {
		// TODO Auto-generated method stub
		return alfabeto;
	}
	

	//-------------------------------------------------------------
	/**
	 * Devuelve las aristas que salen de un estado en el autómata.
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
	/**
	 * Método que inserta una arista nueva en el autómata de pila.
	 * @param estado origen, estado destino, símbolos, cima y apilar/desapilar.
	 */
	public void insertaArista(String origen,String destino,ArrayList<String> simbolos,String cima,ArrayList<String> salida) {
		// TODO Auto-generated method stub
		int indice = automata.indexOf(origen);
		int x,y,fx,fy;
		x=0; y=0; fx=0; fy=0;

		
		if (!determinista){
			determinista = ( simbolos.size() >1 ) && simbolos.contains(lambda);
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

	private boolean iguales(String a1, String a2){
		
		return a1.equals(a2);
	}

	/**
	 * Método que guarda la lista de estados destino segun un origen,cima de pila, y simbolo.
	 * @param palabra.
	 * @param boolean estadoFinal, true si acepta por estado final, false si es por pila vacia
	 * @return bool.
	 */
	private ArrayList<Integer> buscaEstados(String estado, String cima, String simbolo, boolean conSimbolo){
		/*Esto es un poco idea feliz. No se si seria mejor usar hashmap, y decidir si esta solo el
		 * simbolo, solo lambda, o ambos*/
		ArrayList<Integer> s = null;
		int i = 0;
	/*	System.out.println("**datos entrada**");
		System.out.println("ESTADO PARAM ENTRADA: " + estado);
		System.out.println("CIMA PARAM ENTRADA: " + cima);
		System.out.println("SIMBOLO PARAM ENTRADA: " + simbolo);*/
		//if (simbolo == null) return null;
		if (cima == null) return s;
		
		while (i < automata.size()){
			
			AristaAP a = automata.get(i);
	//		System.out.println("ARISTA BUCLE: " + a);
			/*		System.out.println("ESTADO PARAM ENTRADA: " + estado);
			System.out.println("CIMA PARAM ENTRADA: " + cima);
			System.out.println("SIMBOLO PARAM ENTRADA: " + simbolo);*/
			if (a.getCimaPila() == null) System.out.println("NULLL");
		//	else System.out.println("CIMA ARISTA: " + a.getCimaPila());
			boolean contiene = false; 
			if (conSimbolo){
				contiene = a.getEntradaSimbolos().contains(lambda) || a.getEntradaSimbolos().contains(simbolo);
			}
			else {conSimbolo = true;}
	//		if (contiene) System.out.println("contiene cierto");
			boolean b1 = iguales(estado,a.getOrigen());
	//		if (b1) System.out.println("b1 cierto");
			boolean b2 = iguales(cima,a.getCimaPila());
	//		if (b2) System.out.println("b2 cierto");
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
	 * Método que actualiza la pila segun la transicion de la arista.
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
		System.out.println("pila actualizada: " + aux);
		/*		System.out.println("tamaño pila actualizada: " + aux.size());
		System.out.println("pila de entrada: " + pila);
		System.out.println("tamaño pila de entrada: " + pila.size());*/
		
		return aux;
		
	}

	
	
	//--------------------------------------------------------------------------------------------
	private boolean estaPorPila(ArrayList<Integer> listaEstados, ArrayList<String> pila, int iPila, String cimaPila,
			String palabra,	int iPalabra, String estado){
				
		if (listaEstados == null){
			
			if(pila == null || pila.isEmpty()){

				if (iPalabra > palabra.length() || iguales(palabra,lambda)){
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
				while ( it.hasNext() ){ 

					String simboloAux = null;
					Integer indice = it.next();
					AristaAP a = automata.get(indice);

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
					else{
		
						estadoAux = a.getDestino();
						if (iPalabra != palabra.length()){
							simboloAux = "" + palabra.charAt(iPalabra);						
						}

										
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
			if (iPalabra > palabra.length() || iguales(palabra,lambda)){
				if ( estadosFinales.contains(estado)){ System.out.println("BIEEEEEEEEEEEEEN"); return true;}
				else {System.out.println("OOOOOOOOH ACABASTE EN UN ESTADO QUE NO ACEPTA estado"); return false;}
			}
			else {System.out.println("TE PEINASSSSSSS NO TERMINAASTE DE PROCESAR LA CADENA"); return false;}
			
		}

		else{

			//creo ke esto nunca se cumple
			if (pila == null){ System.out.println("sniffFFFFFFFFFFFFFFFFFFFF"); return false;}
			
			

			ArrayList<Integer> listaAux = null;
			ArrayList<String> pilaAux;
			String estadoAux;
			int iPilaAux;
			String cimaPilaAux;
			Iterator<Integer> it = listaEstados.iterator();
			while ( it.hasNext() ){

				String simboloAux = null;
				Integer indice = it.next();
				System.out.println("INDICE BUCLE: " + indice.toString());
				AristaAP a = automata.get(indice.intValue());
				System.out.println("ARISTA BUCLE: " + a);

				if (a.getEntradaSimbolos().contains(lambda)){

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
				else{
					
					estadoAux = a.getDestino();
					if (iPalabra != palabra.length()){
						simboloAux = "" + palabra.charAt(iPalabra);						
					}

					pilaAux = actualizaPila( automata.get(indice).getSalidaPila(),
							pila,cimaPila,iPila  );
					
					if (pilaAux == null || pilaAux.isEmpty()){ iPilaAux = 0; cimaPilaAux = null;}
					else {iPilaAux = pilaAux.size()-1; cimaPilaAux = pilaAux.get(iPilaAux);}
					
					listaAux = buscaEstados(estadoAux, cimaPilaAux, simboloAux,true);
					

					if( estaPorEstado(listaAux, pilaAux , iPilaAux, cimaPilaAux ,palabra,
							iPalabra+1,estadoAux) ) return true; //REVISAR
					
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
	
			if (a.getEntradaSimbolos().contains(lambda)) this.aristasLambda.add(new Integer(i));
			
			
			i++;
		}
		
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
		
		//System.out.println("ARISTAS: " + this.automata);
		//System.out.println("ARISTAS ke desapilan: " + this.aristasQueDesapilan);
//		System.out.println("ARISTAS lambda: " + this.aristasLambda);
	}
	
	/**
	 * Método que verifica si una palabra es reconocida por el autómata de pila.
	 * @param palabra.
	 * @param boolean estadoFinal, true si acepta por estado final, false si es por pila vacia
	 * @return bool.
	 */
	//y si devolvemos un entero mejor??
	public int reconocePalabra(final String palabra){

//REVISAR ESTO
//		if (palabra.equals(lambda) && estadosFinales.contains(estadoInicial) )  return true;
		try{
		
		recogeAristasEspeciales();
		if (!( this.aristasLambda.isEmpty() ) )organizarAristas();
		
/*		System.out.println("ESTADOS: " + this.estados);
		
		System.out.println("ARISTAS: " + this.automata);
		System.out.println("ARISTAS ke desapilan: " + this.aristasQueDesapilan);
		System.out.println("ARISTAS lambda: " + this.aristasLambda);*/
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
		else porEstado = estaPorEstado(listaEstados,pila,0,fondoPila,palabra,1,estado);
		
		if (this.aristasQueDesapilan.isEmpty()) porPila = false;
		else porPila = 	estaPorPila(listaEstados,pila,0,fondoPila,palabra,1,estado);
		
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
	///////////////////////////////////////////////////////////////
	
	/*public ArrayList<String> listaPorEstado(){
		
		ArrayList<String> l = new ArrayList<String>();
		
		ArrayList<Integer> listaEstados = buscaEstados(this.estadoInicial,fondoPila,null,false); //simbolo = null
		int i = 0;
		while ( (i < listaEstados.size()) || (l.size() != 10) ){
			
			
		}
		
		return l;
	}*/
	
/*	public ArrayList<String> listaPorPila(){
		
		ArrayList<String> l = new ArrayList<String>();
		ArrayList<Integer> listaEstados = buscaEstados(this.estadoInicial,fondoPila,null,false);
		int i = 0;
		while ( (i < listaEstados.size()) || (l.size() != 10) ){
			
			
		}		
		return l;
	}*/
	
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
