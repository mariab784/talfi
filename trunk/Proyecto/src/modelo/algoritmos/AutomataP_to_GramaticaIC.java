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
			String cima = new String(aristaActual.getCimaPila());

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
		
		System.out.println("GIC ANTES TRADUCE VARIABLES:\n " + gic);
		System.out.println("*************************************************************************");
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
						

					p.anadeCadena(cadProd);
				
				}

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
			lista.add(estados.get(i));
			listaSalida.add(lista);
		}
		
		if (tam == 1)return listaSalida;
		
	//	int tamListaFinal = (int) Math.pow(tamEstados, tam);
		
		if(tam == 2){
			for(int i = 0; i < tamEstados; i++){ //Para recorrer las listas ya creadas
				for(int j = 0; j < tamEstados; j++){
					ArrayList<String> nCombinacion= (ArrayList<String>) listaSalida.get(0).clone();
					nCombinacion.add(estados.get(j));
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
						nCombinacion.add(estados.get(j));
						listaSalida.add(nCombinacion);
					}
					listaSalida.remove(0);
				}	
			}	
		}

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
				gic.anadeProdConTerminales(nVar);
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
	

	

	
	@SuppressWarnings("unchecked")
	private void traduceVariables(){
			

		//Antes de nada, copiar la gramatica ke hemos obtenido.
//		GramaticaIC gicAux = new GramaticaIC(gic.getVariables(),gic.getSimbolos(),gic.getProducciones(),gic.getVariableInicial());
		GramaticaIC gicSalida = new GramaticaIC();
		gicSalida.setVariableInicial(new String (gic.getVariableInicial()));
		gicSalida.setSimbolos((ArrayList<String>) gic.getSimbolos().clone());
		gicSalida.setVariables((ArrayList<String>) gic.getVariables().clone());

		//ya tenemos todo
		
		HashMap<String,ArrayList<Produccion>> listaProducciones = gic.getProducciones();
		ArrayList<String> listaVariables = gic.getVariables();
		
//		int tamLP = listaProducciones.size();
//		int i = 0;
		//1: eliminar las variables que aparezcan en producciones,y no tengan producciones.
/*		while(i < tamLP){
			
			String var = listaVariables.get(i);
			
			ArrayList<Produccion> nuevaProduccion = gic.creaProducciones(var);
			if (!nuevaProduccion.isEmpty()) gicSalida.getProducciones().put(var, nuevaProduccion);
			i++;
		}*/
	//	System.out.println("GIC SALIDA tienes multi?: " + gicSalida.getProdConLambdaMulti());
	//	System.out.println("GIC aux tienes multi?: " + gicAux.getProdConLambdaMulti());
	//	System.out.println("GIC this tienes multi?: " + gic.getProdConLambdaMulti());
//		gicSalida.setProdConLambdaUnit(gic.getProdConLambdaUnit());
		//		gicSalida.setProdConLambdaMulti(gic.getProdConLambdaMulti());
		//		gicSalida.setProdConTerminales(gic.getProdConTerminales());
	//	gicAux = null;
		//		System.out.println("GIC SALIDA: TRADUCE VARIABLES" + gicSalida);
//		System.out.println("prodConLambda" + gicSalida.getProdConLambdaUnit());
		//		this.setGIC(gicSalida);
		System.gc();
	/***********************hasta aki OK******************************************/
		//quitamos variables que no existen//
		boolean daVueltas = true;
		boolean b = this.gic.quitaVariablesQueNoExisten();
		while(daVueltas){
		
		System.out.println("QUITAMOS VARIABLES QUE NO EXISTEN");
		System.out.println("antes: \n" +gic);
		/*boolean*/ //b = this.gic.quitaVariablesQueNoExisten();
		//si es verdad, es ke hemos kitado
		while(b){
			System.out.println("SEGUIMOS QUITANDO VARIABLES QUE NO EXISTEN");
			System.out.println("dentro bucle: \n" +gic);
			b = this.gic.quitaVariablesQueNoExisten();
		}
		System.out.println("despues: \n" +gic);
		System.out.println("YA HEMOS TERMINADO DE QUITAR VARIABLES QUE NO EXISTEN");
		
		boolean hayLambdaUnitarias = this.gic.dimeSiHayProdUnitarias();
		System.out.println("ANTES BUCLE");
		if (hayLambdaUnitarias){ 
			quitaLambdasUnitarias();
			hayLambdaUnitarias = this.gic.dimeSiHayProdUnitarias();
		}
		b = this.gic.quitaVariablesQueNoExisten();
		daVueltas = b || hayLambdaUnitarias;
			
/*		boolean hayLambdaUnitarias = this.gic.dimeSiHayProdUnitarias();
		System.out.println("ANTES BUCLE");
		System.out.println("this.gic.dimeSiHayProdUnitarias(): " + this.gic.getProdConLambdaUnit());
		System.out.println("hayLambdaUnitarias: " + hayLambdaUnitarias);
		//IDEA:JUNTAR ESTOS BUCLES
		while(hayLambdaUnitarias){
		//	System.out.println("*************BUCLE*******************");
			quitaLambdasUnitarias();
			hayLambdaUnitarias = this.gic.dimeSiHayProdUnitarias();
			System.gc();
		//	System.out.println("this.gic.dimeSiHayProdUnitarias() bucle: " + this.gic.getProdConLambdaUnit());
		//	System.out.println("*************FIN BUCLE*******************");
		}
		System.out.println("YA NO HAY UNITARIAS.VEAMOS MULTI");
		
		System.out.println("dimeGic, no? " + gic);
		boolean hayLambdaMulti = this.gic.dimeSiHayProdMulti();
/*		while(hayLambdaMulti){
			System.out.println("*************BUCLE MULTI*******************");
			quitaLambdasNoUnitarias();
			hayLambdaMulti = this.gic.dimeSiHayProdMulti();
			System.out.println("this.gic.dimeSiHayProdUnitarias() bucle: " + this.gic.getProdConLambdaMulti());
			System.out.println("*************FIN BUCLE MULTI*******************");
			System.gc();
		}*/
//		quitaLambdasNoUnitarias();
		
		}//llaveDaVueltas
		System.out.println("DESCANSO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//		quitaLambdasNoUnitarias();
		System.out.println("YA NO HAY nah de NA");
		
	//	quitaLambdasNoUnitarias();
		cambiaNombreVariables();

	}
	
	@SuppressWarnings("unchecked")
	private void quitaLambdasUnitarias(){
		
		GramaticaIC gicSalida = new GramaticaIC();
		gicSalida.setProducciones(clonarProducciones(gic.getProducciones()));
		gicSalida.setVariableInicial(new String (gic.getVariableInicial()));
		gicSalida.setSimbolos((ArrayList<String>) gic.getSimbolos());
		gicSalida.setVariables((ArrayList<String>) gic.getVariables().clone());
		gicSalida.setProdConTerminales(gic.getProdConTerminales());
		gicSalida.setProdConLambdaUnit((HashMap<String,Integer>) gic.getProdConLambdaUnit().clone());
		gicSalida.setProdConLambdaMulti((HashMap<String,Integer>) gic.getProdConLambdaMulti().clone());
		
		//ya tenemos todo
		System.out.println("GIC AUX:\n" + gic);
		System.out.println("GIC SALIDA:principio quitaLambdasUnitarias\n" + gicSalida);

		
		HashMap<String,ArrayList<Produccion>> listaProducciones = gic.getProducciones();
		ArrayList<String> listaVariables = gic.getVariables();
		
		int tamLP = listaProducciones.size();
		int i = 0;

		//1: eliminar las variables que aparezcan en producciones,y no tengan producciones.
		while(i < tamLP){
			String var = listaVariables.get(i);
				
			ArrayList<Produccion> nuevaProduccion = gicSalida.actualizaProducciones(var);
		//	System.out.println("ke es var?" + var);
		//	System.out.println("ke es nuevaProduccion?: " + nuevaProduccion);
		//	System.out.println("ke tenia la prod para var?" + gicSalida.getProducciones().get(var));
				//si es null es ke tiene tamaño 1 y tiene o lambda o a si mismo
				if ((nuevaProduccion != null) && (!nuevaProduccion.isEmpty())){ 
					//if (gicSalida.getProducciones().containsKey(var)) gicSalida.getProducciones().remove(var);
					System.out.println("METEMOS!VAR " + var + "nuevaProduccion" + nuevaProduccion);
					
					gicSalida.getProducciones().put(var, nuevaProduccion);
	//				System.out.println("ke tiene produccion? DENTRO IF: " + gic.getProducciones());
				}
			i++;
		}
		

		Set<String> setClaves = gic.getProdConLambdaUnit().keySet();
	//	System.out.println("gicAux.getProdConLambda():\n" + gic.getProdConLambdaUnit());
		Iterator<String> itClaves = setClaves.iterator();
		while(itClaves.hasNext()){
			String s = itClaves.next();

		//		System.out.println("entra!");
				//gicSalida.getProdConLambda().remove((String)s);
				gicSalida.getProducciones().remove((String)s);
				gicSalida.getVariables().remove((String)s);			
		}
		
	//	gicSalida.setProdConLambda(gicAux.getProdConLambda());
//		this.setGIC(gicSalida);
		System.out.println("GIC SALIDA final\n" + gicSalida);

		gicSalida.setProdConLambdaMulti(gic.getProdConLambdaMulti());
		
		gicSalida.setProdConLambdaUnit(null);
		this.setGIC(gicSalida);
		System.out.println("GIC de quitaLambdasUnitarias : " + gic);
//		System.out.println("lambdaMulti?: " + gic.getProdConLambdaMulti());
//		System.out.println("FINAL GIC solo lambdas: " + gic.getProdConLambdaUnit());
		
		//faltaria kitar las prod ke tienen lamba pero tienen mas de 2 prod.
	//	gic.setProdConLambdaMulti(gicSalida.getProdConLambdaMulti());
//		System.gc();
//		quitaLambdasNoUnitarias();
//		cambiaNombreVariables();
		System.gc();
//		cambiaNombreVariables();
	}
	
	@SuppressWarnings("unchecked")
	public void quitaLambdasNoeeeeUnitarias(){
		System.out.println("principio gic(this)" + gic);
				GramaticaIC gicSalida = new GramaticaIC();
		gicSalida.setProducciones(clonarProducciones(gic.getProducciones()));
		gicSalida.setVariableInicial(new String (gic.getVariableInicial()));
		gicSalida.setSimbolos((ArrayList<String>) gic.getSimbolos().clone());
		gicSalida.setVariables((ArrayList<String>) gic.getVariables().clone());
		gicSalida.setProdConTerminales((ArrayList<String>) gic.getProdConTerminales().clone());
//		gicSalida.setProdConLambdaUnit((HashMap<String,Integer>) gic.getProdConLambdaUnit().clone());
		gicSalida.setProdConLambdaMulti((HashMap<String,Integer>) gic.getProdConLambdaMulti().clone());
		
		
		GramaticaIC gicAux = new GramaticaIC(gic.getVariables(),gic.getSimbolos(),gic.getProducciones(),gic.getVariableInicial());
//		gicAux.setProdConLambdaUnit(gic.getProdConLambdaUnit());
		
		/*		GramaticaIC gicSalida = new GramaticaIC();
		gicSalida.setProducciones(clonarProducciones(gicAux.getProducciones()));
		gicSalida.setVariableInicial(new String (gicAux.getVariableInicial()));
		gicSalida.setSimbolos((ArrayList<String>) gicAux.getSimbolos().clone());
		gicSalida.setVariables((ArrayList<String>) gicAux.getVariables().clone());
		gicSalida.setProdConLambdaMulti(gic.getProdConLambdaMulti());*/
		HashMap<String,ArrayList<Produccion>> listaProducciones = gicAux.getProducciones();
		System.out.println("ke es multi?" + gicSalida.getProdConLambdaMulti());
		int tamLP = listaProducciones.size();
		int i = 0;
		ArrayList<String> variables = gic.getVariables();
		while(i < tamLP){
			String v = variables.get(i);
			System.out.println("ke es v?" + v);
			ArrayList<Produccion> lps = gicAux.getProducciones().get(v);
			System.out.println("ke es lps?gicAux.getProducciones().get(v)" + lps);
			ArrayList<Produccion> nlps = gicSalida.compruebaMulti(lps,v);
			//nlps es null si el arrayList solo tiene una produccion y a su vez la long concat de esa prod
			//es 1, y es recursivo a si mismo.
			System.out.println("ke es nlps?gicSalida.compruebaMulti(lps,v)" + nlps);
			if (nlps == null){
				System.out.println("no sirvo");
				gicSalida.getProducciones().remove(v);
				gicSalida.getVariables().remove(v);
			}
			else if (!nlps.isEmpty()){ 
				/*if (!gicSalida.getProducciones().containsKey(v)){ 
					gicSalida.getProducciones().remove(v);
				}*/
				
				System.out.println("AÑADIR!");
				System.out.println("ke es v?" + v);
				System.out.println("ke es nlps?" + nlps);
				gicSalida.getProducciones().remove(v);
				gicSalida.getProducciones().put(v, nlps);
				System.out.println("AÑADIDO!");
				System.out.println("si no lo veo no lo creo!" + gicSalida.getProducciones());
			}
			else{
				System.out.println("esto ke significa!!!!");
				System.out.println("veamos si ha metido algo en prod" + gicSalida.getProducciones());
				
			}
			i++;
		}


		
		Set<String> setClaves = gic.getProdConLambdaMulti().keySet();
		System.out.println("gicAux.getProdConLambda()HOLA?:\n" + gic.getProdConLambdaMulti());
		Iterator<String> itClaves = setClaves.iterator();
		while(itClaves.hasNext()){
			String s = new String(itClaves.next());

		//		System.out.println("entra!");
				//gicSalida.getProdConLambda().remove((String)s);
				gicSalida.getProducciones().remove(s);
				gicSalida.getVariables().remove((s));			
		}
		
		System.out.println("MULTI LAMBDA");
		System.out.println("GIC SALIDA despues de limpiar:\n" + gicSalida);
		System.out.println("GIC SALIDA: prod con lambda\n" + gicSalida.getProdConLambdaMulti());
	//	gicSalida.setProdConLambdaMulti(null);
		this.setGIC(gicSalida);
		System.out.println("FINAL GIC MULTI: " + gic);
//		System.out.println("FINAL GIC solo lambdas: " + gic.getProdConLambdaUnit());
		
		System.gc();

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

		System.out.println("tam antiwas: " + variables);
		System.out.println("tam nuevas: "+nVariables);
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
			System.out.println("var: " + var);
			System.out.println("nnVar: "+nnVar);
			ArrayList<Produccion> nListaProd = new  ArrayList<Produccion>();
			ArrayList<Produccion> listaProd = produc.get(var);
			int j = 0;
			int tamListaProd = listaProd.size();
			Produccion nProd = null; 
			while(j < tamListaProd){
				nProd = new Produccion();
				Produccion prod = listaProd.get(j);
				ArrayList<String> nConcat= nuevaContatenacion(prod,variables,nVariables);
				System.out.println("algo falla en nConcat ?: " + nConcat /*+ "\n j :" + j*/);
				
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
		
		System.out.println("ke es variables: "+ variables);
		System.out.println("ke es nVariables: "+ nVariables);
		ArrayList<String> salida = new ArrayList<String>();
		ArrayList<String> concat = prod.getConcatenacion();
		int tam = concat.size(); int i = 0;
		String s;
		while(i<tam){
			s = new String(concat.get(i));
			System.out.println("ke es s: "+ s);
		//	System.out.println("nVAR: "+ nVariables);
			/*		if (gic.getSimbolos().contains(cadena) || lambda.equals(cadena))
			return 0;
		if (gic.getVariables().contains(cadena))
			return 1;//System.out.println("variable!!");
		else
			return 2;//System.out.println("SHIT");*/ //tomamos como imposible el 2
			int dt = dimeTipo(s,variables);
			if (/*comprueba*/ dt == 0){
				System.out.println("ke es s1: "+ s);
				salida.add(s);				
			}
			else if(dt == 1){
				int ind = variables.indexOf(s);
				System.out.println("ke es s2: "+ s);
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
		System.out.println("ke devuelves en salida?: " + salida);
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
		arist.anadirPila("\\");//		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
	/******/	
	
		AutomataP_to_GramaticaIC a = new AutomataP_to_GramaticaIC(aut);
		

		a.AP_Gramatica();
//		System.out.println("main apgramatica: " + a.getGic());
		
		
		
		GIC_to_FNC piticli = new GIC_to_FNC(a.getGic(),true); 
		piticli.simplifica(true,false);
		
		
		System.out.println("ENTRADA:\n" + piticli.getGramaticaEntrada());
		System.out.println("SALIDA:\n" + piticli.getGramaticaSalida());

	piticli.getGramaticaSalida().creaListaPalabras();
		
		
		// TODO Auto-generated method subs

	}

}
