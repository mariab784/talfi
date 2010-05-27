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
	private AutomataPila automataOriginal;
	private AutomataPila automataEntrada;
	private GramaticaIC gic;
	private Mensajero mensajero;
	private String Gramatica;
	private String lambda;
	private String fondoPila;
	private ArrayList<String> variablesEnProducciones;
	private AutomataPila resultadosParciales;
	private Controlador controlador;
	/**
	 * 
	 */
	public AutomataP_to_GramaticaIC(Automata a) {
		// TODO Auto-generated constructor stub
		
		automataOriginal = (AutomataPila) a;
		if (a.getEstadosFinales() == null || a.getEstadosFinales().isEmpty())
			automataEntrada=((AutomataPila) a);
		else automataEntrada=((AutomataPila) a).convertirPilaVacia();

		mensajero=Mensajero.getInstancia();
		Mensajero mensajero = Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		fondoPila = ((AutomataPila) automataEntrada).getFondoPila();
		variablesEnProducciones = new ArrayList<String>();
		controlador=Controlador_imp.getInstancia();
	}
	

	/* (non-Javadoc)
	 * @see modelo.Algoritmo#ejecutar(boolean)
	 */
	@Override
	public Automata ejecutar(boolean muestraPasos) {
		// TODO Auto-generated method stub
		
		return automataOriginal;
	}

	/* (non-Javadoc)
	 * @see modelo.Algoritmo#getXML()
	 */
	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return "donde esta el xml matarilerilerile";
	}

	public AutomataPila getAutomataEntrada(){return automataEntrada;}
	
	public AutomataPila getAutomataOriginal(){return automataOriginal;} 
	
	/* (non-Javadoc)
	 * @see modelo.Algoritmo#registraControlador(controlador.Controlador)
	 */
	@Override
	public void registraControlador(Controlador controlador) {
		// TODO Auto-generated method stub
		this.controlador=controlador;
	}

	/**
	 * @return Gramatica resultado de aplicar el algoritmo
	 * para hallar una gramatica indepenediente de contexto
	 * a partir de un automata de pila dado.
	 */
	@SuppressWarnings("unchecked")
	public GramaticaIC AP_Gramatica(){
		
		System.out.println("********************************************");
		System.out.println("AUTOMATA DE ENTRADA!\n" + automataEntrada);
		System.out.println("AUTOMATA DE ORIGINAL!\n" + automataOriginal);
		System.out.println("********************************************");
		
		gic = new GramaticaIC();
		gic.setSimbolos((ArrayList<String>)automataEntrada.getAlfabeto().getListaLetras().clone());
		
		gic.setVariableInicial("S");
		
		ArrayList<String> estados = (ArrayList<String>) this.automataEntrada.getEstados().clone();
		Iterator<String> it = estados.iterator();
		
		gic.getVariables().add("S");//añadido
		while (it.hasNext()){
						
			Produccion p = new Produccion();
			p.anadeCadena("["+this.automataEntrada.getEstadoInicial()+fondoPila+it.next()+"]");
		//	System.out.println("P INICIAL: " + p);
			gic.anadeProduccion("S", p);
		}
		/* Aquí hemos Iniciado la gramatica, con simbolo inicial S 
		 * y añadiendo todos los estados con simbolo de pila)*/
		
		
//		ArrayList<String> estados = (ArrayList<String>) this.automataEntrada.getEstados().clone();
		ArrayList<AristaAP> arista = this.automataEntrada.getAutomataPila();
		Iterator<AristaAP> itArista = arista.iterator();
		
		while(itArista.hasNext()){
			
			AristaAP aristaActual = itArista.next();
			ArrayList<String> salidaPila = (ArrayList<String>) aristaActual.getSalidaPila().clone();
			String destino = new String(aristaActual.getDestino());
			String origen = new String(aristaActual.getOrigen());
			ArrayList<String> simbolos = (ArrayList<String>) aristaActual.getEntradaSimbolos().clone();
			String cima = aristaActual.getCimaPila();
			//creo ke seria con todos los estados, si no funciona sera solo con el destino
			if(desapila(salidaPila)){
				anadeProduccionesTerminales(origen,cima,estados,simbolos);				
			}
			else{
				int tamLista = salidaPila.size();
				ArrayList<ArrayList<String>> listaParesEstados = 
					construyeListasEstados((ArrayList<String>)estados.clone(),tamLista);
					anadeProduccionesConLista(origen,destino,cima,listaParesEstados,simbolos,salidaPila);
			}
			
			
			
			
			
			
			
			
			
		}
		

		


		System.out.println("GIC ANTES TRADUCE VARIABLES: " + gic);
		traduceVariables();	
		return gic;
		
	}
	private void anadeProduccionConUnaLista(String origen,String destino,String cima, ArrayList<String> lista,
			ArrayList<String> simbolos, ArrayList<String>salidaPila){
			
		//System.out.println("lista " + lista);
			int tamLista = lista.size();
			int tamListaConcreta = salidaPila.size();
			int tamSimbolos = simbolos.size();
			int nuevoTam = tamLista-1;
			String rk = lista.get(nuevoTam) +"";
			String nVar = "["+origen+cima+rk+"]";
			gic.anadeVariable(nVar);
			Produccion p = new Produccion();


			
			for(int j = 0; j < tamSimbolos; j++){
				String sim = simbolos.get(j);
			/*	if (!sim.equals(lambda))*/p.anadeCadena(sim); //por si acaso no diferenciamos al principio
			
			//for (int k = 0; k < tamListaConcreta; k++){
			//String cadProd = "";
				for(int i = 0; i < tamListaConcreta; i++){
					String cadProd = "[";
				//	System.out.println("lista.get(i)" + lista.get(i));
					if (i == 0) cadProd += destino;
					else cadProd += lista.get(i);
					String sigEstado = null;
					if(i+1 == tamListaConcreta){
						
						sigEstado = rk;
					}
					else{
						 sigEstado = lista.get(i+1);
					}	
					
					cadProd += salidaPila.get(i)+sigEstado+"]";
						
						
					
				//	System.out.println("nuevoTam: " + nuevoTam );
			//		System.out.println("cadprod: " + cadProd + "\n i: " + i);
					p.anadeCadena(cadProd);
				
				}
//				String simboloSalidaPilaSubK = salidaPila.get(nuevoTam);
//				String cadFinal = "["+rSubK+simboloSalidaPilaSubK+rk+"]";
//				p.anadeCadena(cadFinal);
	//			System.out.println("creando producciones: " + p);
				gic.anadeProduccion(nVar, p);
			}
		//	System.out.println("GIC creando producciones: " + gic.getProducciones());
			//}
			
	}
	
	private void anadeProduccionesConLista(String origen,String destino,String cima, ArrayList<ArrayList<String>> listaParesEstados,
			ArrayList<String> simbolos, ArrayList<String>salidaPila){
		
		int tamLista = listaParesEstados.size();
		for(int i = 0; i < tamLista; i++){
			
			anadeProduccionConUnaLista(origen,destino,cima, listaParesEstados.get(i),
					simbolos,salidaPila);
			
		}


			
					/*	String est = itEst.next();
			Iterator<String> itSimbolos = simbolos.iterator();
			while(itSimbolos.hasNext()){
				String sim = itSimbolos.next();
				Produccion p = new Produccion();
				p.anadeCadena(sim);
				String nVar = "[" +origen+cima+est+ "]";
				gic.anadeVariable(nVar);
				gic.anadeProduccion(nVar, p);
			}*/
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<ArrayList<String>> 
	construyeListasEstados(ArrayList<String>estados,int tam){
		
		int tamEstados = estados.size();
		ArrayList<ArrayList<String>> listaSalida = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < tamEstados; i++){
			ArrayList<String> lista = new ArrayList<String>();
			lista.add(estados.get(i));
			listaSalida.add(lista);
		}
		
//		System.out.println("listaSalida : " + listaSalida);
		if (tam == 1)return listaSalida;
		
		
//		int tamLista = listaSalida.size();
		int tamListaFinal = (int) Math.pow(tamEstados, tam);
//		System.out.println("TAMLISTAFINAL: " + tamListaFinal);
		
		if(tam == 2){
			for(int i = 0; i < tamEstados; i++){ //Para recorrer las listas ya creadas
				for(int j = 0; j < tamEstados; j++){
					ArrayList<String> nCombinacion= (ArrayList<String>) listaSalida.get(0).clone();
					nCombinacion.add(estados.get(j));
					listaSalida.add(nCombinacion);
/*			ArrayList<String> lista = new ArrayList<String>();
			String e = estados.get(i);
			for(int j = 0; i < tam; j++){
				lista.add(e);				
			}
			listaSalida.add(lista);*/
				}
				listaSalida.remove(0);
			}
		
		}

		System.out.println("LISTA SALIDA: " + listaSalida);
		System.out.println("COINDICE?\n + TAMLISTAFINAL: " 
				+ tamListaFinal + " || TAMLISTALISTAS: " + listaSalida.size());
		
		
		return listaSalida;
	}
	
	private void anadeProduccionesTerminales(String origen, String cima,ArrayList<String> estados,
			ArrayList<String> simbolos){
		
		Iterator<String> itEst = estados.iterator();
		while(itEst.hasNext()){
					
			String est = itEst.next();
			Iterator<String> itSimbolos = simbolos.iterator();
			while(itSimbolos.hasNext()){
				String sim = itSimbolos.next();
				Produccion p = new Produccion();
				p.anadeCadena(sim);
				String nVar = "[" +origen+cima+est+ "]";
				gic.anadeVariable(nVar);
				gic.anadeProduccion(nVar, p);
			}
		}
				
/*		Produccion p = new Produccion();
		p.anadeCadena("["+this.automataEntrada.getEstadoInicial()+fondoPila+it.next()+"]");
		System.out.println("P INICIAL: " + p);
		gic.anadeProduccion("S", p);*/
	}
	
	
	private boolean desapila(ArrayList<String> lista){
		
		return ((lista.size() == 1) && lista.get(0).equals(lambda));
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
		
//creo ke esto no hace falta
/*		ArrayList<String> vars = this.getGic().getVariables();
		ArrayList<String> nVars = new ArrayList<String>();
		System.out.println("VARIABLES: " + vars);
		for(int i = 0; i < vars.size(); i++){
			
			String var = vars.get(i);
			System.out.println("VAR: " + var);
			if (this.getGic().getProducciones().containsKey(var)){
				nVars.add(var);			
			}
			else{System.out.println(var +" no esta la borramos de V!!");}
			
		}*/
		
		
		HashMap<String,ArrayList<Produccion>> nProduc = null;
		//XXX
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
		
/*		System.out.println("*********************traduceVariables****************************");
		System.out.println("producciones : " + produc);
		System.out.println("Variables: " + variables);
		System.out.println("producciones nProduc: " + nProduc);
		System.out.println("nuVariables: " + nuVariables);*/
		this.gic.setProducciones(nProduc); 	
		this.gic.setVariables(nuVariables);

//**********************************hasta aki OK***************************************************//
		
		

/*		ArrayList<String> nVariables = new ArrayList<String>();
		nVariables.add(gic.getVariableInicial());
		int tam = gic.getVariables().size();
		char ultima = comienzo; char nVar = comienzo;
		int ii = 1;
		while(ii < tam){
			
			nVar = new Character (ultima) ;
			ultima = (char)(nVar + 1);
			System.out.println("nVar :" + nVar );
			if (nVar != 'S'){
				String s = nVar + "";
				nVariables.add(s);
				ii++;
			}
		}*/

		nProduc = new HashMap<String,ArrayList<Produccion>>();
		String vIni = gic.getVariableInicial();
		variables = (ArrayList<String>) gic.getVariables().clone();
		tamano = /*nVariables*/variables.size();
		
		ArrayList<String> noSirven = new ArrayList<String>();
		for(int i = 0; i < tamano; i++){ //recorremos todas las producciones ke hay con las variables
//			System.out.println("algo falla en nProduc : " + nProduc + "\n i :" + i);
			String var = variables.get(i);
			String nnVar = /*nV*/variables.get(i);
			ArrayList<Produccion> nListaProd = new  ArrayList<Produccion>();
			ArrayList<Produccion> listaProd = produc.get(var);
			int j = 0;
			int tamListaProd = listaProd.size();
			Produccion nProd = null; 
			while(j < tamListaProd){
				nProd = new Produccion();
				Produccion prod = listaProd.get(j);
				ArrayList<String> nConcat= nuevaContatenacion(prod,variables,variables/*,nVariables*/);
			//	System.out.println("algo falla en nConcat : " + nConcat + "\n j :" + j);
					if (nConcat != null )nProd.setConcatenacion(nConcat); //CUIDADO CON ESTO KIZA EXPLOTE SOMEWHERE
					if (!esta(nProd,nListaProd) && (nConcat != null)) nListaProd.add(nProd);

				j++;				
			}
			if ((!inservible(nListaProd)) || (nnVar.equals(vIni))){
				nProduc.put(nnVar, nListaProd); 
				
			}
			else {noSirven.add(nnVar);}
			
		}
		
		if (!noSirven.isEmpty()){
			int tama = noSirven.size();
			for(int i = 0; i < tama; i++){
				String s = new String(noSirven.get(i));
				/*nV*/variables.remove(s);
			}
		}
		
		System.out.println("*****SIMPLIFICACION*****");
		
		System.out.println("*****FIN SIMPLIFICACION*****");
		
		ArrayList<String> nVariables = new ArrayList<String>();
		nVariables.add(gic.getVariableInicial());
		int tam = /*gic.getVariables()*/variables.size();
		char ultima = comienzo; char nVar = comienzo;
		int ii = 1;
		/*for(int i = 1; i <tam; i++)*/while(ii < tam){
			
			nVar = new Character (ultima) ;
			ultima = (char)(nVar + 1);
	//		System.out.println("nVar :" + nVar );
			if (nVar != 'S'){
				String s = nVar + "";
				nVariables.add(s);
				ii++;
			}
		}
		
		nProduc = new HashMap<String,ArrayList<Produccion>>();
		/*String*/ vIni = gic.getVariableInicial();
		variables = (ArrayList<String>) /*gic.getV*/variables/*()*/.clone();
		tamano = nVariables.size();
		
		/*ArrayList<String>*/ noSirven = new ArrayList<String>();
		for(int i = 0; i < tamano; i++){ //recorremos todas las producciones ke hay con las variables
//			System.out.println("algo falla en nProduc : " + nProduc + "\n i :" + i);
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
			//	System.out.println("algo falla en nConcat : " + nConcat + "\n j :" + j);
					if (nConcat != null )nProd.setConcatenacion(nConcat); //CUIDADO CON ESTO KIZA EXPLOTE SOMEWHERE
					if (!esta(nProd,nListaProd) && (nConcat != null)) nListaProd.add(nProd);

				j++;				
			}
			if ((!inservible(nListaProd)) || (nnVar.equals(vIni))){
				nProduc.put(nnVar, nListaProd); 
				
			}
			else {noSirven.add(nnVar);}
			
		}
		
		if (!noSirven.isEmpty()){
			int tama = noSirven.size();
			for(int i = 0; i < tama; i++){
				String s = new String(noSirven.get(i));
				nVariables.remove(s);
			}
		}
		
	//	System.out.println();
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
			s = new String(concat.get(i));
		//	System.out.println("VAR: "+ variables);
		//	System.out.println("nVAR: "+ nVariables);
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
					ns = new String (gic.getProducciones().get(s).get(0).getConcatenacion().get(0));
				else {
			//		System.out.println("IND: " + ind);
			//		System.out.println("TAM VAR; " + variables.size());
			//		System.out.println("TAM nVAR; " + nVariables.size());
					ns = new String (nVariables.get(ind));}
				
				//if (!ns.equals(lambda)){
					salida.add(ns); //XXX
				//}
			}
			else{
				System.out.println("S EXPLOTA: " + s); 
				System.out.println("E X P L O S I O N !!!!!"); return null;
			
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
				salida.add(new String(s));
			}
			else{ 
				
				return null;
			}
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
		
		
		
		GIC_to_FNC piticli = new GIC_to_FNC(a.getGic(),false); //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXxx
		while(piticli.getTablaTieneMarcas()){
			
			if (!piticli.diagonalMarcada()){ System.out.println("DIAGONAL NO "); piticli.sustituir();}
			else{ System.out.println("DIAGONAL SI "); piticli.sustituirDiagonal();}
			piticli.transforma_FNG(false);
		}
		System.out.println("ENTRADA:\n" + piticli.getGramaticaEntrada().getProducciones());
		System.out.println("SALIDA:\n" + piticli.getGramaticaSalida().getProducciones());

		piticli.getGramaticaSalida().creaListaPalabras();
		
		
		// TODO Auto-generated method subs

	}

}
