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

		System.out.println("AUT ENTRADA:\n" + automataEntrada);
		System.out.println("AUT ORIGINAL:\n" + automataOriginal);
		
		mensajero=Mensajero.getInstancia();
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
	public GramaticaIC/*void*/ AP_Gramatica(){
		
/*		System.out.println("********************************************");
		System.out.println("AUTOMATA DE ENTRADA!\n" + automataEntrada);
		System.out.println("AUTOMATA DE ORIGINAL!\n" + automataOriginal);
		System.out.println("********************************************");*/
		
		gic = new GramaticaIC();
		gic.setSimbolos((ArrayList<String>)automataEntrada.getAlfabeto().getListaLetras().clone());
		
		gic.setVariableInicial("S");
		
		ArrayList<String> estados = (ArrayList<String>) this.automataEntrada.getEstados().clone();
		Iterator<String> it = estados.iterator();
		
		gic.anadeVariable("S");//añadido
		while (it.hasNext()){
						
			Produccion p = new Produccion();
			String ini = this.automataEntrada.getEstadoInicial();
			String cadena = "["+ini+fondoPila+it.next()+"]";
			p.anadeCadena(cadena);
		//	System.out.println("P INICIAL: " + p);
			gic.anadeProduccion("S", p);
			gic.anadeVariable(cadena);
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
			String cima = new String(aristaActual.getCimaPila());

			if(desapila(salidaPila)){
				anadeProduccionesTerminales(origen,cima,estados,simbolos,destino);				
			}
			else{
				int tamLista = salidaPila.size();
				ArrayList<ArrayList<String>> listaParesEstados = 
					construyeListasEstados((ArrayList<String>)estados.clone(),tamLista);
					anadeProduccionesConLista(origen,destino,cima,listaParesEstados,simbolos,salidaPila);
					
			}
		}
		
		System.out.println("GIC ANTES TRADUCE VARIABLES:\n " + gic);
		System.out.println("*************************************************************************");
		traduceVariables();	
		return gic;
		
	}
	private void anadeProduccionConUnaLista(String origen,String destino,String cima, ArrayList<String> lista,
			ArrayList<String> simbolos, ArrayList<String>salidaPila){
			
	/*	System.out.println("ORIGEN: " + origen);
		System.out.println("DESTINO: " + destino);
		System.out.println("CIMA: " + cima);
		System.out.println("LISTA: " + lista);
		System.out.println("SIMBOLOS: " + simbolos);
		System.out.println("SALIDAPILA: " + salidaPila);*/

			int tamLista = lista.size();
			int tamListaConcreta = salidaPila.size();
			int tamSimbolos = simbolos.size();
			int nuevoTam = tamLista-1;
			String rk = lista.get(nuevoTam) +"";
			String nVar = "["+origen+cima+rk+"]";
			gic.anadeVariable(new String(nVar));
			Produccion p = new Produccion();

			
			for(int j = 0; j < tamSimbolos; j++){
				String sim = simbolos.get(j);
				p.anadeCadena(sim); //por si acaso no diferenciamos al principio

				for(int i = 0; i < tamListaConcreta; i++){
					String cadProd = "[";
				//	System.out.println("lista.get(i)" + lista.get(i));
					if (i == 0) cadProd += destino;
					else cadProd += lista.get(i-1);
					String sigEstado = null;
					if(i+1 == tamListaConcreta){
						
						sigEstado = rk;
					}
					else{
						 sigEstado = lista.get(i/*+1*/);
					}	
					
					cadProd += salidaPila.get(i)+sigEstado+"]";
						

					p.anadeCadena(new String(cadProd));
				}
				//System.out.println("gic.anadeProduccion(nVar, p);: " + nVar +"," + p);
				gic.anadeProduccion(nVar, p);
			}

			
	}
	
	
	private void anadeProduccionesConLista(String origen,String destino,String cima, ArrayList<ArrayList<String>> listaParesEstados,
			ArrayList<String> simbolos, ArrayList<String>salidaPila){
		
		int tamLista = listaParesEstados.size();
		for(int i = 0; i < tamLista; i++){
			
			anadeProduccionConUnaLista(origen,destino,cima, listaParesEstados.get(i),
					simbolos,salidaPila);
			
		}
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<ArrayList<String>> 
	construyeListasEstados(ArrayList<String>estados,int tam){

		int tamEstados = estados.size();
		ArrayList<ArrayList<String>> listaSalida = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < tamEstados; i++){
			ArrayList<String> lista = new ArrayList<String>();
			lista.add(new String(estados.get(i)));
			listaSalida.add(lista);
		}
		
		if (tam == 1)return listaSalida;
		
		if(tam == 2){
			for(int i = 0; i < tamEstados; i++){ //Para recorrer las listas ya creadas
				for(int j = 0; j < tamEstados; j++){
					ArrayList<String> nCombinacion= (ArrayList<String>) listaSalida.get(0).clone();
					nCombinacion.add(new String(estados.get(j)));
					listaSalida.add(nCombinacion);
				}
				listaSalida.remove(0);
			}	
		}
		
		if(tam == 3){
			for(int k = 0; k < tamEstados; k++){
				for(int i = 0; i < tamEstados; i++){ //Para recorrer las listas ya creadas
					for(int j = 0; j < tamEstados; j++){
						ArrayList<String> nCombinacion= (ArrayList<String>) listaSalida.get(0).clone();
						nCombinacion.add(new String(estados.get(j)));
						listaSalida.add(nCombinacion);
					}
					listaSalida.remove(0);
				}	
			}	
		}

		return listaSalida;
	}
	
	private void anadeProduccionesTerminales(String origen,String cima,ArrayList<String> estados,
			ArrayList<String> simbolos,String destino){
		

			System.out.println("ORIGEN: " + origen);
		System.out.println("DESTINO: " + destino);
		System.out.println("CIMA: " + cima);
		//System.out.println("LISTA: " + lista);
		System.out.println("SIMBOLOS: " + simbolos);
		//System.out.println("SALIDAPILA: " + salidaPila);
		
//		Iterator<String> itEst = estados.iterator();
//		while(itEst.hasNext()){
					
			String est = new String(/*itEst.next()*/destino);

			Iterator<String> itSimbolos = simbolos.iterator();
			while(itSimbolos.hasNext()){
				String sim = new String(itSimbolos.next());

					Produccion p = new Produccion();
					p.anadeCadena(sim);
					String nVar = "[" +origen+cima+est+ "]";
					gic.anadeVariable(nVar);
					gic.anadeProdConTerminales(nVar);
					gic.anadeProduccion(nVar, p);

				
			}
//		}
	}
	
	
	private boolean desapila(ArrayList<String> lista){
		
		return ((lista.size() == 1) && lista.get(0).equals(lambda));
	}
	
	@SuppressWarnings("unchecked")
	private Produccion clonar(Produccion original){
		
		Produccion p = new Produccion();
		ArrayList<String> temporal = (ArrayList<String>) original.getConcatenacion().clone();
		p.setConcatenacion(temporal);
		
		return p;
	}
	
	private ArrayList<Produccion> clonarArrayProduc(ArrayList<Produccion> original){
		
		ArrayList<Produccion> np = new ArrayList<Produccion>();
		Iterator<Produccion> itNp = original.iterator();
		while(itNp.hasNext()){
			Produccion actualP = itNp.next();
			np.add(clonar(actualP));
		}		
		return np;
	}
	
	private HashMap<String,ArrayList<Produccion>> 
	clonarProducciones(HashMap<String,ArrayList<Produccion>> original){
		
		HashMap<String,ArrayList<Produccion>> np = new HashMap<String,ArrayList<Produccion>>();
		
		Set<String> claves = original.keySet();
		Iterator<String> itClaves = claves.iterator();
		while(itClaves.hasNext()){
			String c = new String(itClaves.next());
			ArrayList<Produccion> nproduc = clonarArrayProduc(original.get(c));
			np.put(c, nproduc);
		}
		
		return np;
	}
	

	
	private void limpia(){
		boolean b = this.getGic().dimeSiHayVariablesQueNoTienenProd();
		while(b){
			if(b) System.out.println("dime ke variables no tienen prod pobres!: " + gic.getVariablesSinProd());
			this.getGic().quitaVariablesQueNoExisten();
			b = this.getGic().dimeSiHayVariablesQueNoTienenProd();
			if(b) System.out.println("dime ke variables no tienen prod pobres2!: " + gic.getVariablesSinProd());

		}
		
	}

	private void traduceVariables(){
			
		limpia();
		
		boolean c = this.getGic().dimeSiHayProdUnitarias();
		if(c) System.out.println("dime ke prod son unit: " + gic.getProdUnit());
		while(c){
			this.getGic().quitaProdUnitarias();
			limpia();
			c = this.getGic().dimeSiHayProdUnitarias();
			limpia();
		}
		
/*		c = this.getGic().dimeSiHayProdMulti();
		if(c) System.out.println("dime ke prod son multi: " + gic.getProdMulti());
		while(c){
			this.getGic().quitaProdMulti();
			limpia();
			c = this.getGic().dimeSiHayProdMulti();
		}*/
		
		
		System.out.println("DESCANSO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("YA NO HAY nah de NA");

		cambiaNombreVariables();

	}
	
	

	
	@SuppressWarnings("unchecked")
	public void cambiaNombreVariables(){
		ArrayList<String> variables = (ArrayList<String>) gic.getVariables().clone();
		HashMap<String,ArrayList<Produccion>> produc = clonarProducciones(gic.getProducciones());
		

		ArrayList<String> nVariables = new ArrayList<String>();
		nVariables.add(gic.getVariableInicial());
		int tam = gic.getVariables().size();
		char ultima = comienzo; char nVar = comienzo;
	//	int ultima = 0; int nVar = 0;
		int ii = 1;
		while(ii < tam){
			
			nVar = new Character (ultima);
			ultima = (char)(nVar + 1);
//			System.out.println("nVar :" + nVar );
			if (nVar != 'S'){
				String s = /*"["+*/nVar+""/*+"]"*/;
				nVariables.add(s);
				ii++;
			}
		}

//		System.out.println("tam antiwas: " + variables);
//		System.out.println("tam nuevas: "+nVariables);
//DESPUES DE AKI HAY UNA EXPLOSION 
		
		HashMap<String,ArrayList<Produccion>> nProduc = new HashMap<String,ArrayList<Produccion>>();
		String vIni = gic.getVariableInicial();
		/*ArrayList<String>*/ variables = (ArrayList<String>) /*gic.getV*/variables/*()*/.clone();
		int tamano = nVariables.size();
		
		ArrayList<String> noSirven = new ArrayList<String>();
		for(int i = 0; i < tamano; i++){ //recorremos todas las producciones ke hay con las variables
//			System.out.println("algo falla en nProduc : " + nProduc + "\n i :" + i);
			String var = variables.get(i);
			String nnVar = nVariables.get(i);
			//		System.out.println("var: " + var);
			//		System.out.println("nnVar: "+nnVar);
			ArrayList<Produccion> nListaProd = new  ArrayList<Produccion>();
			ArrayList<Produccion> listaProd = produc.get(var);
			int j = 0;
			int tamListaProd = listaProd.size();
			Produccion nProd = null; 
			while(j < tamListaProd){
				nProd = new Produccion();
				Produccion prod = listaProd.get(j);
				ArrayList<String> nConcat= nuevaContatenacion(prod,variables,nVariables);
				//			System.out.println("algo falla en nConcat ?: " + nConcat /*+ "\n j :" + j*/);
				
					if (nConcat != null/* && !RecInservible(nConcat,nnVar)*/)nProd.setConcatenacion(nConcat); //CUIDADO CON ESTO KIZA EXPLOTE SOMEWHERE
					if (!esta(nProd,nListaProd)&& ((nConcat != null))) nListaProd.add(nProd);

				j++;				
			}
	/*		if ((!inservible(nListaProd)) || (nnVar.equals(vIni))){*/
				nProduc.put(nnVar, nListaProd); 
				
/*			}
			else {noSirven.add(nnVar);}*/
			
		}
		
/*		if (!noSirven.isEmpty()){
			int tama = noSirven.size();
			for(int i = 0; i < tama; i++){
				String s = new String(noSirven.get(i));
				nVariables.remove(s);
			}
		}*/
		
	//	System.out.println();
		this.gic.setProducciones(nProduc);
		this.gic.setVariables(nVariables);
		System.out.println("GIC SIMPLIFICADA: " + gic);
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
	
	public void setGIC(GramaticaIC g){gic = g;}
	
	
/*	private boolean RecInservible(ArrayList<String> nConcat,String nnVar){
		
		if ((nConcat.size()==1) && (nnVar.equals(nConcat.get(0))) ){
			System.out.println("FUERA RECURSIVAS TONTAS!!!");
			return true;
		}
		else return false;
	}*/
	
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
		
//		System.out.println("ke es variables: "+ variables);
//		System.out.println("ke es nVariables: "+ nVariables);
		ArrayList<String> salida = new ArrayList<String>();
		ArrayList<String> concat = prod.getConcatenacion();
		int tam = concat.size(); int i = 0;
		String s;
		while(i<tam){
			s = new String(concat.get(i));
	//		System.out.println("ke es s: "+ s);
		//	System.out.println("nVAR: "+ nVariables);
			/*		if (gic.getSimbolos().contains(cadena) || lambda.equals(cadena))
			return 0;
		if (gic.getVariables().contains(cadena))
			return 1;//System.out.println("variable!!");
		else
			return 2;//System.out.println("SHIT");*/ //tomamos como imposible el 2
			int dt = dimeTipo(s,variables);
			if (/*comprueba*/ dt == 0){
	//			System.out.println("ke es s1: "+ s);
				salida.add(s);				
			}
			else if(dt == 1){
				int ind = variables.indexOf(s);
	//			System.out.println("ke es s2: "+ s);
				//System.out.println("IND: " + ind);
				String ns = null;//nVariables.get(ind);
				/*if (inservible(gic.getProducciones().get(s)) ){
					ns = new String (gic.getProducciones().get(s).get(0).getConcatenacion().get(0));
					System.out.println("ke es s3: "+ s);
					System.out.println("ke es ns: "+ ns);
				}else {*/
			//		System.out.println("IND: " + ind);
			//		System.out.println("TAM VAR; " + variables.size());
			//		System.out.println("TAM nVAR; " + nVariables.size());
					ns = new String (nVariables.get(ind));
			//	}
				
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
//		System.out.println("ke devuelves en salida?: " + salida);
		return salida;
	}
	
	
	
	
	private ArrayList<String> compruebaContatenacion(Produccion prod,ArrayList<String> v){
		
		ArrayList<String> salida = new ArrayList<String>();
		ArrayList<String> concat = prod.getConcatenacion();
		int tam = concat.size(); int i = 0;
		String s;
		while(i<tam){

			
			s = concat.get(i);
			if (compruebaElemento(s,v)){
				salida.add(new String(s));
			}
			else{ 
				
				return null;
			}
			i++;
	
		}		
		return salida;
	}
	
	private boolean compruebaElemento(String s,ArrayList<String> v){
		
		if(dimeTipo(s,v) == 2)	return false;
		else	return true;

	}
	
	private int dimeTipo(String cadena,ArrayList<String> v){
		

		if (gic.getSimbolos().contains(cadena) || lambda.equals(cadena))
			return 0;
		if (v.contains(cadena))
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

aut.getEstados().add("s0");
aut.getEstados().add("s1");



aut.setEstadoInicial("s0");
aut.setEstadoFinal("s1");

		AristaAP arist;
		
		arist = new AristaAP(0,0,0,0,"s0","s0");
		arist.anadirSimbolo("a");
		arist.setCimaPila("Z");
		arist.anadirPila("AZ");
		
		aut.anadeArista(arist);
		
		/*******/

		arist = new AristaAP(0,0,0,0,"s0","s0");
		arist.anadirSimbolo("b");
		arist.setCimaPila("Z");
		arist.anadirPila("BZ");
		
		aut.anadeArista(arist);
		
		/*******/
		
		arist = new AristaAP(0,0,0,0,"s0","s0");
		arist.anadirSimbolo("a");
		arist.setCimaPila("A");
		arist.anadirPila("AA");
		
		aut.anadeArista(arist);
		
		/*******/		
		
		arist = new AristaAP(0,0,0,0,"s0","s0");
		arist.anadirSimbolo("b");
		arist.setCimaPila("B");
		arist.anadirPila("BB");
		
		aut.anadeArista(arist);
		
		/*******/	
		
		arist = new AristaAP(0,0,0,0,"s0","s0");
		arist.anadirSimbolo("a");
		arist.setCimaPila("B");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);
		
		/*******/	
		
		arist = new AristaAP(0,0,0,0,"s0","s0");
		arist.anadirSimbolo("b");
		arist.setCimaPila("A");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);
		
		/*******/	
		
		
		
		
		arist = new AristaAP(0,0,0,0,"s0","s1");//		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");//		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
	/******/	
	
		AutomataP_to_GramaticaIC a = new AutomataP_to_GramaticaIC(aut);
		

		a.AP_Gramatica();

		
/*			GIC_to_FNC piticli = new GIC_to_FNC(a.getGic(),true); 
		piticli.simplifica(true,false);
		
		
		System.out.println("ENTRADA:\n" + piticli.getGramaticaEntrada());
		System.out.println("SALIDA:\n" + piticli.getGramaticaSalida());

	piticli.getGramaticaSalida().creaListaPalabras();*/
		
		
		// TODO Auto-generated method subs

	}

}
