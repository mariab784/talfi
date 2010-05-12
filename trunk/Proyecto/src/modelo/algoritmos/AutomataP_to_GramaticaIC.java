/**
 * 
 */
package modelo.algoritmos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import vista.vistaGrafica.AristaAP;

import accesoBD.Mensajero;
import controlador.Controlador;
import controlador.Controlador_imp;
import modelo.Algoritmo;
import modelo.automatas.*;
//import modelo.expresion_regular.ExpresionRegular;
import modelo.gramatica.GramaticaIC;
import modelo.gramatica.Produccion;

/**
 * @author Rocio Barrigüete, Mario Huete, Luis San Juan
 *
 */
public class AutomataP_to_GramaticaIC implements Algoritmo {

	private static final char comienzo = 'A';
	private String xml;
	private AutomataPila automataOriginal;
	private AutomataPila automataEntrada;
	private GramaticaIC gic;
	private Controlador controlador;
	private Mensajero mensajero;
	private String Gramatica;
	private String lambda;
	private String fondoPila;
	
	private AutomataPila resultadosParciales;
	/**
	 * 
	 */
	public AutomataP_to_GramaticaIC(Automata a) {
		// TODO Auto-generated constructor stub
		automataOriginal = (AutomataPila) a;
		if (a.getEstadosFinales() == null || a.getEstadosFinales().isEmpty())
			automataEntrada=((AutomataPila) a);
		else automataEntrada=((AutomataPila) a).convertirPilaVacia();
		
/*		System.out.println("***********CONTROL**************");
		System.out.println(automataEntrada);
		System.out.println("***********fin CONTROL**************");*/
		
//		System.out.println("afinal: " + a);
		mensajero=Mensajero.getInstancia();
		xml=new String();
		controlador=Controlador_imp.getInstancia();
		Mensajero mensajero = Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		fondoPila = ((AutomataPila) a).getFondoPila();
	}
	

	/* (non-Javadoc)
	 * @see modelo.Algoritmo#ejecutar(boolean)
	 */
	@Override
	public Automata ejecutar(boolean muestraPasos) {
		// TODO Auto-generated method stub
		
		return null;
	}

	/* (non-Javadoc)
	 * @see modelo.Algoritmo#getXML()
	 */
	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return null;
	}

	public AutomataPila getAutomataEntrada(){return automataEntrada;}
	
	public AutomataPila getAutomataOriginal(){return automataOriginal;} 
	
	/* (non-Javadoc)
	 * @see modelo.Algoritmo#registraControlador(controlador.Controlador)
	 */
	@Override
	public void registraControlador(Controlador controlador) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return Gramatica resultado de aplicar el algoritmo
	 * para hallar una gramatica indepenediente de contexto
	 * a partir de un automata de pila dado.
	 */
	@SuppressWarnings("unchecked")
	public GramaticaIC AP_Gramatica(){
		
//		System.out.println("*****AP_Gramatica antes de nada****");
//		System.out.println("AUTOMATA DE ENTRADA!\n" + automataEntrada);
		
		/*GramaticaIC*/ gic = new GramaticaIC();
		gic.setSimbolos(automataEntrada.getAlfabeto().getListaLetras());//añadido
		gic.setVariableInicial("S");
		ArrayList<String> estados = (ArrayList<String>) this.automataEntrada.getEstados().clone();
		Iterator<String> it = estados.iterator();
		gic.getVariables().add("S");//añadido
		while (it.hasNext()){
			
//			System.out.println("automataEntrada.getEstadoInicial() " + automataEntrada.getEstadoInicial());
//			System.out.println("fondoPila " + fondoPila);
			
			
			Produccion p = new Produccion();
			p.anadeCadena("["+this.automataEntrada.getEstadoInicial()+fondoPila+it.next()+"]");
			System.out.println("P INICIAL: " + p);
			gic.anadeProduccion("S", p);
		}
		/* Aquí hemos Iniciado la gramatica, con simbolo inicial S 
		 * y añadiendo todos los estados con simbolo de pila)*/
		
		System.out.println("GIC ANTES : " + gic);
		
		//declaraciones de ArrayList Auxiliares y de iterators que comentamos despues
		ArrayList<String> estados2 = (ArrayList<String>) this.automataEntrada.getEstados().clone();
		ArrayList<String> estados3 = (ArrayList<String>) this.automataEntrada.getEstados().clone();
		ArrayList<String> estados4 = (ArrayList<String>) this.automataEntrada.getEstados().clone();
		Iterator<String> it2 = estados2.iterator();
		String estado = null;
		
		//Recorremos los estados "que ponemos despues del primer corchete"
		
		while (it2.hasNext()){
			estado = it2.next();
			ArrayList<String> alfabetoPila = this.automataEntrada.dameCimasEstado(estado);
			if(alfabetoPila!=null){
				Iterator<String> letra = alfabetoPila.iterator();
				String estaLetra = null;
				
				//recorremos las cimas posibles para ese estado en las transiciones
				
				
				//Para crear producciones solo en caso de que haya transiciones con esa cima de pila
				while (letra.hasNext()){
					Iterator<String> estadoF = estados4.iterator();
					estaLetra = letra.next();
					String actual;
					ArrayList<String> posibles = this.automataEntrada.dameLetraEstadoCima(estado, estaLetra); 
					//Los estados que pueden convertirse en finales
					while (estadoF.hasNext()){
						String estadoFF = estadoF.next();
						if (this.automataEntrada.dameEstados(estado).contains(estadoFF)){
						//genero el simbolo que será la clave de una nueva serie de producciones
						String simbolos = "["+estado+estaLetra+estadoFF+"]";
						if(posibles != null ) {
							Iterator<String> itp = posibles.iterator();
							//Las posibles letras que llevan a transicion
									while (itp.hasNext()){
										actual = itp.next();
											Iterator<String> it3 = estados3.iterator();								
											//Para generar todas las combinaciones posibles
											while (it3.hasNext()){
												String estado2 = it3.next();
												//if (this.automataEntrada.dameFinPilaEstadoLetra(estado, estaLetra, actual) != null){
													ArrayList<ArrayList<String>> listaApila = this.automataEntrada.dameFinPilaEstadoLetra(estado, estaLetra, actual);
													Iterator<ArrayList<String>> itApilas = listaApila.iterator();
													Produccion p = null;
													//Recorro los simbolos que apareceran en la cima de pila
													while (itApilas.hasNext()){
														p = new Produccion();
														ArrayList<String> futuras = itApilas.next();
														if(this.automataEntrada.dameEstadoLetraEstado(estado,actual)!=null){
															p.anadeCadena(actual);
															if (futuras.size() == 1){
																if (!futuras.get(0).equals(lambda)){
																	p.anadeCadena("["+estado+futuras.get(0)+estado2+"]");
																}
															}
															else if (futuras.size()>0){
																p.anadeCadena("["+estado+futuras.get(0)+estado2+"]");
																p.anadeCadena("["+estado2+futuras.get(1)+estadoFF+"]");
															
															}
															boolean pasa = true;
															if(gic.getProducciones().get(simbolos) != null){
															if (p.getConcatenacion().size() == 1 && !this.automataEntrada.dameEstadoLetraEstado(estado,actual).contains(estadoFF)) pasa = false;
															ArrayList<Produccion> quita = gic.getProducciones().get(simbolos);
															Iterator<Produccion> it19 = quita.iterator();
															while (it19.hasNext()){
																Produccion aux = it19.next();
																if (aux.getConcatenacion().toString().contains(p.getConcatenacion().toString()))
																	pasa = false;
															}
														}
														if(gic.getProducciones().get(simbolos) == null || pasa)	{
														gic.anadeProduccion(simbolos, p);
														//añadido
														if(!estaEnVariables(simbolos))
															gic.getVariables().add(simbolos); //añadido
														}
												//	}
													}	
												}
											}
										}
									}
								}
							}
						}
					}
				}

		System.out.println("GIC ANTES TRADUCE VARIABLES: " + gic);
		traduceVariables();	
		return gic;
		
	}
	
	private boolean esta(Produccion pnueva, ArrayList<Produccion >lprod){
		ArrayList<String> concat = pnueva.getConcatenacion();
		Iterator<Produccion> itProd = lprod.iterator();
		while(itProd.hasNext()){
			Produccion p = itProd.next();
			ArrayList<String> pConcat = p.getConcatenacion(); 
			if (iguales(concat,pConcat)) return true;
		}
		return false;
	}
	
	private boolean iguales(ArrayList<String> a1, ArrayList<String> a2){
		
		if (a1.size() != a2.size()) return false;
		int i = 0; int tam = a1.size();
		while(i < tam){
			String ss1 = a1.get(i); String ss2 = a2.get(i);
			if (!ss1.equals(ss2)) return false;
			
			i++;
		}
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	private void traduceVariables(){
		
		HashMap<String,ArrayList<Produccion>> nProduc = null;
		int tamano = 0;// = produc.size();
		boolean terminado = false;
		//int i = 0;
		ArrayList<String> variables = gic.getVariables();
		HashMap<String,ArrayList<Produccion>> produc = gic.getProducciones();
		ArrayList<String> nuVariables = null;
		while (!terminado){
			
			nuVariables = new ArrayList<String>();
			nProduc = new HashMap<String,ArrayList<Produccion>>();
			
			tamano = produc.size();
			for(int i = 0; i < tamano; i++){ //recorremos todas las producciones ke hay con las variables
				String var = variables.get(i);
				ArrayList<Produccion> nListaProd = new  ArrayList<Produccion>();
				ArrayList<Produccion> listaProd = produc.get(var);
				int j = 0;
				int tamListaProd = listaProd.size();
				Produccion nProd = null; 
				while(j < tamListaProd){
					nProd = new Produccion();
					Produccion prod = listaProd.get(j);
					ArrayList<String> nConcat= compruebaContatenacion(prod);

					if (nConcat != null && !nConcat.isEmpty()){ 
						nProd.setConcatenacion(nConcat); 
						nListaProd.add(nProd);
					}

					j++;				
				}
				if (!nListaProd.isEmpty()){nProduc.put(var, nListaProd); nuVariables.add(var);}
				
			}
			if (iguales(produc,nProduc)) terminado = true;
			produc = (HashMap<String, ArrayList<Produccion>>) nProduc.clone();
			variables = (ArrayList<String>) nuVariables.clone();
		}
		
		System.out.println("*********************traduceVariables****************************");
		System.out.println("producciones : " + produc);
		System.out.println("Variables: " + variables);
		System.out.println("producciones nProduc: " + nProduc);
		System.out.println("nuVariables: " + nuVariables);
		this.gic.setProducciones(nProduc);
		this.gic.setVariables(nuVariables);

//**********************************hasta aki OK***************************************************//
		
		

		ArrayList<String> nVariables = new ArrayList<String>();
		nVariables.add(gic.getVariableInicial());
		int tam = gic.getVariables().size();
		char ultima = comienzo; char nVar = comienzo;
		for(int i = 1; i <tam; i++){
			
			nVar = new Character (ultima) ;
			ultima = (char)(nVar + 1);
			if (nVar != 'S'){
				String s = nVar + "";
				nVariables.add(s);
			}
		}

		
		nProduc = new HashMap<String,ArrayList<Produccion>>();
		String vIni = gic.getVariableInicial();
		tamano = nVariables.size();
		variables = gic.getVariables();
		ArrayList<String> noSirven = new ArrayList<String>();
		for(int i = 0; i < tamano; i++){ //recorremos todas las producciones ke hay con las variables
			String var = variables.get(i);
			String nnVar = nVariables.get(i);
			ArrayList<Produccion> nListaProd = new  ArrayList<Produccion>();
			ArrayList<Produccion> listaProd = produc.get(var);
			int j = 0;
			int tamListaProd = listaProd.size();
			Produccion nProd = null; 
			while(j < tamListaProd){
				nProd = new Produccion();
				Produccion prod = listaProd.get(j);
				ArrayList<String> nConcat= nuevaContatenacion(prod,variables,nVariables);

					nProd.setConcatenacion(nConcat); 
					if (!esta(nProd,nListaProd)) nListaProd.add(nProd);

				j++;				
			}
			if ((!inservible(nListaProd)) || (nnVar.equals(vIni))/*nListaProd.isEmpty()*/){nProduc.put(nnVar, nListaProd); }
			else {noSirven.add(nnVar);}
			
		}
		
		if (!noSirven.isEmpty()){
			int tama = noSirven.size();
			for(int i = 0; i < tama; i++){
				String s = noSirven.get(i);
				nVariables.remove(s);
			}
		}
		
		this.gic.setProducciones(nProduc);
		this.gic.setVariables(nVariables);

	}
	/*****************************************************************************************************/
	private boolean inservible(ArrayList<Produccion> nListaProd){
			
		return (nListaProd.size() == 1) && (nListaProd.get(0).getConcatenacion().size() == 1);
	}
	/*****************************************************************************************************/
	private boolean iguales(HashMap<String,ArrayList<Produccion>> ant,
			HashMap<String,ArrayList<Produccion>> nuevo){
		
		Set<String> clavesAnt = ant.keySet();
		Set<String> clavesNuevas = nuevo.keySet();
		
		if (clavesAnt.size() != clavesNuevas.size()) return false;
		
		Iterator<String> itAnt = clavesAnt.iterator();
		while (itAnt.hasNext()){
			
			String s = itAnt.next();
			if (!clavesNuevas.contains(s)) return false;
		}
		return true;
	}
	

	private ArrayList<String> nuevaContatenacion(Produccion prod,ArrayList<String> variables,
			ArrayList<String> nVariables ){
		
		ArrayList<String> salida = new ArrayList<String>();
		ArrayList<String> concat = prod.getConcatenacion();
		int tam = concat.size(); int i = 0;
		String s;
		while(i<tam){
			s = concat.get(i);
			/*		if (gic.getSimbolos().contains(cadena) || lambda.equals(cadena))
			return 0;
		if (gic.getVariables().contains(cadena))
			return 1;//System.out.println("variable!!");
		else
			return 2;//System.out.println("SHIT");*/ //tomamos como imposible el 2
			if (/*comprueba*/dimeTipo(s) == 0){
				salida.add(s);				
			}
			else if(dimeTipo(s) == 1){
				int ind = variables.indexOf(s);
				//System.out.println("IND: " + ind);
				String ns = null;//nVariables.get(ind);
				if (inservible(gic.getProducciones().get(s)) )
					ns = gic.getProducciones().get(s).get(0).getConcatenacion().get(0);
				else ns = nVariables.get(ind);
				
				if (!ns.equals(lambda)){
					salida.add(ns); //XXX
				}
			}
			else{
				System.out.println("S EXPLOTA: " + s); 
				System.out.println("E X P L O S I O N !!!!!"); //return null;
			
			}
			i++;
		}		
		return salida;
	}
	
	
	
	
	private ArrayList<String> compruebaContatenacion(Produccion prod){
		
		ArrayList<String> salida = new ArrayList<String>();
		ArrayList<String> concat = prod.getConcatenacion();
		int tam = concat.size(); int i = 0;
		String s;
		while(i<tam){

			
			s = concat.get(i);
			if (compruebaElemento(s)){
				salida.add(s);				
			}
			else return null;
			i++;
	
		}		
		return salida;
	}
	
	private boolean compruebaElemento(String s){
		
		if(dimeTipo(s) == 2)	return false;
		else	return true;

	}
	
	private int dimeTipo(String cadena){
		

		if (gic.getSimbolos().contains(cadena) || lambda.equals(cadena))
			return 0;
		if (gic.getVariables().contains(cadena))
			return 1;//System.out.println("variable!!");
		else
			return 2;//System.out.println("SHIT");
		
		
	}
	
	private boolean estaEnVariables(String s){
		
		return gic.getVariables().contains(s);
	}

	/**
	 * @param args
	 */
	public GramaticaIC getGic(){
		if(gic == null){
//			gic = new GramaticaIC();
//			gic.setProducciones(producciones);
		System.out.println("GIC null");	
			
		}

		return gic;
	}
	
	public static void main(String[] args) {
		AutomataPila aut = new AutomataPila();
//		AutomataPila aut2 = new AutomataPila();
		//a.listaEstados.add(new Estado(0,0,"s1"));
aut.getEstados().add("s0");//		aut.getEstados().add("s1");
aut.getEstados().add("s1");//		aut.getEstados().add("s2");
//aut.getEstados().add("s2");//		aut.getEstados().add("s3");

		//		aut.getEstados().add("s4");

aut.setEstadoInicial("s0");//		aut.setEstadoInicial("s1");
//aut.setEstadoFinal("s1");//		aut.setEstadoFinal("s3");
		//aut.setEstadoFinal("s2");

/*		aut2.getEstados().add("s1");
		aut2.getEstados().add("s2");
		aut2.setEstadoInicial("s1");
		aut2.setEstadoFinal("s2");*/
		//System.out.println("ESTADOS: " + aut.getEstados());
		
		AristaAP arist;
		
		arist = new AristaAP(0,0,0,0,"s0","s0");//		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("1");
		arist.setCimaPila("Z");
		arist.anadirPila("1Z");//		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		
		/*******/

		
		arist = new AristaAP(0,0,0,0,"s0","s0");//		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("1");
		arist.setCimaPila("1");
		arist.anadirPila("11");//		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
	/******/	
		
		
		arist = new AristaAP(0,0,0,0,"s0","s1");		//arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("0");
		arist.setCimaPila("1");//		arist.setCimaPila("Z");
		arist.anadirPila("\\");//		arist.anadirPila("CZ");
		
		aut.anadeArista(arist);	
		
		//añadido//
/*		arist = new AristaAP(0,0,0,0,"s0","s0");		//arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("b");
		arist.setCimaPila("Z");//		arist.setCimaPila("Z");
		arist.anadirPila("\\");//		arist.anadirPila("CZ");
		
		aut.anadeArista(arist);	*/
		
		arist = new AristaAP(0,0,0,0,"s1","s1");//		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("0");//		arist.anadirSimbolo("0");
		arist.setCimaPila("1");//		arist.setCimaPila("C");
		arist.anadirPila("\\");//		arist.anadirPila("CC");
		
		aut.anadeArista(arist);	
	
		arist = new AristaAP(0,0,0,0,"s1","s1");//		arist = new AristaAP(0,0,0,0,"s2","s2");
		arist.anadirSimbolo("\\");		//arist.anadirSimbolo("1");
		arist.setCimaPila("Z");		//arist.setCimaPila("C");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);
		
		//arist = new AristaAP(0,0,0,0,"s1","s2");//		arist = new AristaAP(0,0,0,0,"s1","s3");
//		arist.anadirSimbolo("0");
		//arist.anadirSimbolo("b");		//arist.anadirSimbolo("1");
		//arist.setCimaPila("Z");		//arist.setCimaPila("C");
		//arist.anadirPila("\\");
		
		//aut.anadeArista(arist);		

//		AristaAP borrada = 	aut.getAutomataPila().remove(2);
//		System.out.println("ARISTA borrada: " + borrada );

/*		arist = new AristaAP(0,0,0,0,"s3","s2");
		//arist.anadirSimbolo("0");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		
		arist = new AristaAP(0,0,0,0,"s3","s4");
		arist.anadirSimbolo("0");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		
		arist = new AristaAP(0,0,0,0,"s3","s4");
		arist.anadirSimbolo("0");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist); 
		
//		aut2.anadeArista(arist);

		
		arist = new AristaAP(0,0,0,0,"s1","s2");
//		arist.anadirSimbolo("0");
		arist.anadirSimbolo("1");
		arist.setCimaPila("A");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		
/*		arist = new AristaAP(0,0,0,0,"s3","s2");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");

		
		aut.anadeArista(arist);
		

		
	/*	arist = new AristaAP(0,0,0,0,"s2","s2");
		arist.anadirSimbolo("b");
		arist.setCimaPila("Z"); 
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);*/
		
/*		arist = new AristaAP(0,0,0,0,"s3","s4");
		arist.anadirSimbolo("\\");
		//System.out.println("LAMBDA: \\" );
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);*/
		 //una vez ordenado no puedes desordenar
		//System.out.println("ARISTAS: " + aut.getAutomataPila());

/*		ArrayList<String> lp = new  ArrayList<String>();
		lp.add("0");
		lp.add("000");
		lp.add("00");*/
	
//		AutomataPila.compruebaPalabras(aut, aut2, lp);
/*		Alfabeto_Pila alf = new AlfabetoPila_imp();
		alf.aniadirLetra("Z");
		alf.aniadirLetra("A");//		alf.aniadirLetra("C");
		aut.setAlfabetoPila(alf);
		Alfabeto al = new Alfabeto_imp();
		al.aniadirLetra("a");
		al.aniadirLetra("b");
		aut.setAlfabeto(al);*/

/*		System.out.println("AUT:\n" + aut);
		AutomataP_to_GramaticaIC a = new AutomataP_to_GramaticaIC(aut);
		System.out.println(a.AP_Gramatica().getProducciones().toString());*/
		
		System.out.println("AUT:\n" + aut);
		AutomataP_to_GramaticaIC a = new AutomataP_to_GramaticaIC(aut);
		

		System.out.println(a.AP_Gramatica()/*.getProducciones().toString()*/);
		
		
		
		GIC_to_FNC piticli = new GIC_to_FNC(a.getGic());
		while(piticli.getTablaTieneMarcas()){
			
			if (!piticli.diagonalMarcada()){ System.out.println("DIAGONAL NO "); piticli.sustituir();}
			else{ System.out.println("DIAGONAL SI "); piticli.sustituirDiagonal();}
			piticli.transforma_FNG();
		}
		System.out.println("ENTRADA:\n" + piticli.getGramaticaEntrada().getProducciones());
		System.out.println("SALIDA:\n" + piticli.getGramaticaSalida().getProducciones());

		piticli.getGramaticaSalida().creaListaPalabras();
		
		
		// TODO Auto-generated method subs

	}

}
