package modelo.algoritmos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import vista.vistaGrafica.AristaAP;

import modelo.automatas.AlfabetoPila_imp;
import modelo.automatas.Alfabeto_Pila;
import modelo.automatas.AutomataPila;
import modelo.gramatica.Gramatica;
import modelo.gramatica.GramaticaIC;
import modelo.gramatica.Greibach;
import modelo.gramatica.Produccion;
/**
 * Clase que ejecuta los procedimientos para llevar a cabo la 
 * transformación de una gramática cualquiera a una gramática 
 * en forma normal de Greinbach.
 *  @author Rocio Barrigüete, Mario Huete, Luis San Juan
 */
public class GIC_to_FNC {
	//ATRIBUTOS:**************************************************
	private GramaticaIC gramaticaEntrada;
	private Greibach gramaticaSalida;
	private String xml;
	private boolean tabla[][];
	private int lon;
	private HashMap<Integer,ArrayList<Integer>> columna; // columna, filas
	private int clave;
	private boolean tablaTieneMarcas;
	//************************************************************
	public GIC_to_FNC(GramaticaIC g){
		gramaticaEntrada = g;
		gramaticaSalida = new Greibach(g.getVariables(),g.getSimbolos(),g.getProducciones(),g.getVariableInicial());
		transforma_FNG();
	}
	//MÉTODOS:****************************************************
	//------------------------------------------------------------
	public void setGramaticaEntrada(GramaticaIC gramaticaEntrada) {
		this.gramaticaEntrada = gramaticaEntrada;
	}
	//------------------------------------------------------------
	public Gramatica getGramaticaEntrada() {
		return gramaticaEntrada;
	}
	//------------------------------------------------------------
	public void setGramaticaSalida(Greibach gramaticaSalida) {
		this.gramaticaSalida = gramaticaSalida;
	}
	//------------------------------------------------------------
	public Greibach getGramaticaSalida() {
		return gramaticaSalida;
	}
	//------------------------------------------------------------
	public void setXml(String xml) {
		this.xml = xml;
	}
	//------------------------------------------------------------
	public String getXml() {
		return xml;
	}
	//------------------------------------------------------------
	public boolean getTablaTieneMarcas() {
		return tablaTieneMarcas;
	}
	//------------------------------------------------------------
	public void setTablaTieneMarcas(boolean b) {
		tablaTieneMarcas = b;
	}
	//------------------------------------------------------------
	public void/*Gramatica*/ transforma_FNG(){

		ArrayList<String> variables = gramaticaSalida.getVariables();
		HashMap<String, ArrayList<Produccion>> producciones = gramaticaSalida.getProducciones();
		lon = variables.size(); 
		tabla = new boolean[lon][lon]; 
		inicializarTabla();
		tablaTieneMarcas=false;

		for(int i=0; i<lon; i++){ //recorre variables fila
			String simboloFila = variables.get(i);
			String simboloColumna = null;
			ArrayList<Produccion> produccionesDeFila = producciones.get(simboloFila);
		//	System.out.println("PROD FILA: " + produccionesDeFila);
			for(int j=0; j<lon; j++) { //recorre variables columna
				simboloColumna = variables.get(j);
				
			//	System.out.println("simboloColumna: " + simboloColumna);
			//	System.out.println("PROD FILA: " + produccionesDeFila);
				Iterator<Produccion> it = produccionesDeFila.iterator();
				boolean enc = false;
				
				while (it.hasNext() && !enc){
					Produccion aux = it.next();
					int k = 0;

							//	System.out.println("aux.getConcatenacion(): " + aux.getConcatenacion());
								String primerSimbolo = aux.getConcatenacion().get(0).charAt(0) + "";
							//	System.out.println("simboloColumna: " + simboloColumna);
							//	System.out.println("primerSimbolo: " + primerSimbolo);
								if (simboloColumna.equals(primerSimbolo)){
									tabla[i][j] = true; enc = true;
									tablaTieneMarcas=true;
									//System.out.println("**I J MARCADAS**: " + i + "," + j);
									//if (!hayMarcadas){
								}
								k++;
				}
			}
		}
		pintaTabla();
		calculaColumna();

	}
	//------------------------------------------------------------
	private void calculaColumna(){
		columna = null;
		ArrayList<Integer> f = new ArrayList<Integer>();
		int i = 0;
		boolean enc = false;
		while(i < lon && !enc){
			for(int j = 0; j <lon; j++){
				if (tabla[j][i]){
					if(columna == null){
						columna = new HashMap<Integer,ArrayList<Integer>>();
						clave = i;
						enc = true;
					}
					f.add(j);					
				}	
			}
			i++;
		}
		if (columna != null )columna.put(clave, f);
	}
	
	public void pintaTabla(){
		
		for(int i = 0; i <lon; i++){
			if (i == 0) System.out.print("  ");
			System.out.print(this.getGramaticaSalida().getVariables().get(i) + " ");
		}
		System.out.println();
		for(int i=0; i<lon; i++){
			for(int j=0; j<lon; j++){
				String s;
				if (j == 0)
					System.out.print(this.getGramaticaSalida().getVariables().get(i) + " ");
				if (tabla[i][j]) System.out.print("X ");
				else System.out.print("- ");
				//System.out.println("I: " + i + " J: " + j + "VALOR: " + tabla[i][j]);
			}
			System.out.println();
		}
	}
	
	public void inicializarTabla() {
		for(int i=0; i<lon; i++)
			for(int j=0; j<lon; j++) 
				tabla[i][j] = false;
	}
	//************************************************************
	public boolean diagonalMarcada(){
		
		System.out.println("COLUMNA: " + columna);
		ArrayList<Integer> aa = columna.get(clave);
		return aa.contains(clave);
	}
	//************************************************************	
	@SuppressWarnings("unchecked")
	public void sustituir(){
		 
		HashMap<String, ArrayList<Produccion>> producciones = gramaticaSalida.getProducciones();		
		ArrayList<Produccion> produccionesDeColumna = producciones.get(gramaticaSalida.getVariables().get(clave));

		ArrayList<Integer> filas = columna.get(clave);
		Iterator<Integer> itFilas = filas.iterator();

		ArrayList<Produccion> pnueva = null;
		while (itFilas.hasNext()){
			pnueva = new ArrayList<Produccion>();
			Integer indice = itFilas.next();
			String var = gramaticaSalida.getVariables().get(indice);
			ArrayList<Produccion> produccionesDeFilas = producciones.get(gramaticaSalida.getVariables().get(indice.intValue()));
//			System.out.println("produccionesDeFilas " + produccionesDeFilas);
			int i = 0;int tam = produccionesDeFilas.size();
			
			while( i < tam){ //recorre las producciones filas marcadas
				Produccion aux2 = produccionesDeFilas.get(i);
			
				ArrayList<String> ayay = ((ArrayList<String>)aux2.getConcatenacion().clone());
				String p = ayay.get(0);
				String pp = "" +  p.charAt(0);
//				System.out.println("p " + p);
				Produccion nueva = null;
				if (pp.equals(gramaticaSalida.getVariables().get(clave))){
//					System.out.println("produccionesDeFilasDespues " + produccionesDeFilas);
//					System.out.println("produccionesDeColumnaNocambian?" + produccionesDeColumna);
					Iterator<Produccion> itProdCol = produccionesDeColumna/*.get(0).getConcatenacion()*/.iterator();
					aux2.getConcatenacion().remove(0);
					while (itProdCol.hasNext()){ //recorre las producciones de las columnas
						nueva = new Produccion();
						Produccion aux3 = itProdCol.next();
						ArrayList<String> nueva2 = (ArrayList<String>)aux3.getConcatenacion().clone();
//						System.out.println("nueva2: "+ nueva2);
//						System.out.println("aux3: "+ aux3);
						
//						System.out.println("aux2: "+ aux2);
						for(int k = 0; k< aux2.getConcatenacion().size();k++){
							nueva2.add(aux2.getConcatenacion().get(k));
							
						}
						nueva.setConcatenacion(nueva2);
//						System.out.println("nueva: "+ nueva);

						pnueva.add(nueva);
						//System.out.println("produccionesDeFilasfinal: "+ produccionesDeFilas);
						//System.out.println("GRAMATICA: " + gramaticaSalida.getProducciones());
					}					
				}
				else{
					pnueva.add(aux2);
//					System.out.println("temporal: " + pnueva);					
				}
				i++;
			}
			gramaticaSalida.getProducciones().remove(var);
			gramaticaSalida.getProducciones().put(var, pnueva);
		}
		System.out.println("GRAMATICA: " + gramaticaSalida.getProducciones());
	}
	//************************************************************
	
	@SuppressWarnings("unchecked")
	public void sustituirDiagonal(){
		
		HashMap<String, ArrayList<Produccion>> producciones = gramaticaSalida.getProducciones();
		ArrayList<Produccion> produccionesDiagonal;
		produccionesDiagonal = producciones.get(gramaticaSalida.getVariables().get(clave));
//		System.out.println("produccionesDiagonal" + produccionesDiagonal);
		String var = gramaticaSalida.getVariables().get(clave);
		String nVar = null;
		int tam = gramaticaSalida.getVariables().size()-1;
		String ultVar = gramaticaSalida.getVariables().get(tam);
		char nVarAux = new Character ((char)(ultVar.charAt(0)+1) ) ;
		nVar =  nVarAux + "";
		//System.out.println("nVar.charValue()" + nVar);
				gramaticaSalida.getVariables().add(nVar);
		/**************************************************************************************/
		/**nueva variable creada,ahora hay ke crear las producciones de la nueva variable**/
		ArrayList<Produccion> p = new ArrayList<Produccion>();		
		ArrayList<Produccion> produccionesNuevas = new ArrayList<Produccion>();
		Produccion pod;
		ArrayList<String> concat;
		for(int j = 0; j < produccionesDiagonal.size(); j++){
			Produccion paux = produccionesDiagonal.get(j);

			ArrayList<String> a = paux.getConcatenacion();
			if(!var.equals(a.get(0))){
				
				p.add(paux);
				concat = (ArrayList<String>)a.clone();
				concat.add(nVar);
				pod = new Produccion();
				pod.setConcatenacion(concat);
				p.add(pod);
//				System.out.println("p " + p);
			}
			else{
				concat = (ArrayList<String>)a.clone();
				concat.remove(0);
				pod = new Produccion();
				pod.setConcatenacion(concat);
				produccionesNuevas.add(pod);
//				System.out.println("pod1 " + pod);
				//pod = new Produccion();
				pod = new Produccion();
				concat = (ArrayList<String>)a.clone();
				concat.remove(0);
				pod.setConcatenacion(concat);
				pod.anadeCadena(nVar);
//				System.out.println("pod2 " + pod);
				produccionesNuevas.add(pod);
//				System.out.println("produccionesNuevas " + produccionesNuevas);
			}
		}
		

		gramaticaSalida.getProducciones().remove(var);
		gramaticaSalida.getProducciones().put(var, p);
		gramaticaSalida.getProducciones().put(nVar, produccionesNuevas);
		
		System.out.println("GRAMATICA salida: " + gramaticaSalida.getProducciones());
	}
	
	//************************************************************
	public static void main(String[] args){
		
		GramaticaIC g = new GramaticaIC();
		g.setVariableInicial("S");
		ArrayList<String> s = new ArrayList<String>();
		s.add("S");s.add("T");s.add("U");
		g.setVariables(s);
		s = new ArrayList<String>();
		s.add("\\");s.add("a");s.add("b");
		g.setSimbolos(s);

		ArrayList<Produccion> p = new ArrayList<Produccion>();
		Produccion pod = new Produccion();
		HashMap<String,ArrayList<Produccion>> lala = new  HashMap<String,ArrayList<Produccion>>();

		p = new ArrayList<Produccion>();
		pod = new Produccion();
		pod.anadeCadena("\\"); 
		p.add(pod);
		pod = new Produccion();
		pod.anadeCadena("a");
		p.add(pod);
		pod = new Produccion();
		pod.anadeCadena("T"); pod.anadeCadena("T"); 
		p.add(pod); 
		lala.put("S",p);
		
//		System.out.println("GRAMATICA: " + lala);

		
		p = new ArrayList<Produccion>();
		pod = new Produccion();
		pod.anadeCadena("U"); pod.anadeCadena("T");
		p.add(pod); 
		pod = new Produccion();
		pod.anadeCadena("a"); pod.anadeCadena("b");
		p.add(pod); 
		lala.put("T",p);

//		System.out.println("GRAMATICA: " + lala);


		

		
		p = new ArrayList<Produccion>();
		pod = new Produccion();
		pod.anadeCadena("T"); pod.anadeCadena("T");
		p.add(pod);
		pod = new Produccion();
		pod.anadeCadena("b"); pod.anadeCadena("b"); pod.anadeCadena("b"); 
		p.add(pod); 
		lala.put("U",p);
		
//		System.out.println("GRAMATICA: " + lala);
		

		
		g.setProducciones(lala);
		
/*		HashMap<Integer,ArrayList<ArrayList<Integer>>> prueba = new HashMap<Integer,ArrayList<ArrayList<Integer>>>();
		ArrayList<Integer> gr = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> gr2 = new ArrayList<ArrayList<Integer>>();
		gr.add(new Integer(2));
		gr2.add(gr); 
		prueba.put(2, gr2); gr2.add(gr); prueba.put(3, gr2); prueba.put(4, gr2);

		System.out.println("veamos: " + prueba);*/
		
		System.out.println("GRAMATICA: " + g);
		GIC_to_FNC piticli = new GIC_to_FNC(g);
		while(piticli.getTablaTieneMarcas()){
			
			if (!piticli.diagonalMarcada()){ System.out.println("DIAGONAL NO "); piticli.sustituir();}
			else{ System.out.println("DIAGONAL SI "); piticli.sustituirDiagonal();}
			piticli.transforma_FNG();
			
		}
		System.out.println("gram.getProducciones() " + piticli.getGramaticaEntrada().getProducciones());
		
		
	
/*		AutomataPila aut = new AutomataPila();
//		AutomataPila aut2 = new AutomataPila();
		//a.listaEstados.add(new Estado(0,0,"s1"));
		aut.getEstados().add("s1");
		aut.getEstados().add("s2");
		aut.getEstados().add("s3");
//		aut.getEstados().add("s4");
		aut.setEstadoInicial("s1");
		aut.setEstadoFinal("s3");
		aut.setEstadoFinal("s2");

/*		aut2.getEstados().add("s1");
		aut2.getEstados().add("s2");
		aut2.setEstadoInicial("s1");
		aut2.setEstadoFinal("s2");*/
		//System.out.println("ESTADOS: " + aut.getEstados());
		
//AKI XXX
/*		AristaAP arist;
		
		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("a");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		

		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("a");
		arist.setCimaPila("Z");
		arist.anadirPila("CZ");
		
		aut.anadeArista(arist);	
		
		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("0");
		arist.setCimaPila("C");
		arist.anadirPila("CC");
		
		aut.anadeArista(arist);	
	
		arist = new AristaAP(0,0,0,0,"s2","s2");
		arist.anadirSimbolo("1");
		arist.setCimaPila("C");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);
		
		arist = new AristaAP(0,0,0,0,"s1","s3");
//		arist.anadirSimbolo("0");
		arist.anadirSimbolo("1");
		arist.setCimaPila("C");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);
		
		Alfabeto_Pila alf = new AlfabetoPila_imp();
		alf.aniadirLetra("Z");
		alf.aniadirLetra("C");
		aut.setAlfabetoPila(alf);
		AutomataP_to_GramaticaIC a = new AutomataP_to_GramaticaIC(aut);
		System.out.println(a.AP_Gramatica().getProducciones().toString());
		
		
		
		GIC_to_FNC piticli = new GIC_to_FNC(a.getGic());
		while(piticli.getTablaTieneMarcas()){
			
			if (!piticli.diagonalMarcada()){ System.out.println("DIAGONAL NO "); piticli.sustituir();}
			else{ System.out.println("DIAGONAL SI "); piticli.sustituirDiagonal();}
			piticli.transforma_FNG();
		}
		System.out.println("gram.getProducciones() " + piticli.getGramaticaEntrada().getProducciones());*/

	}
}