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
		Gramatica gramatica = null;
		String variableInicial = gramaticaSalida.getVariableInicial();
		ArrayList<String> variables = gramaticaSalida.getVariables();
		HashMap<String, ArrayList<Produccion>> producciones = gramaticaSalida.getProducciones();
		lon = variables.size(); 
		tabla = new boolean[lon][lon]; 
		inicializarTabla();
		tablaTieneMarcas=false;
		columna = null;
		
		//boolean hayMarcadas = false;
		for(int i=0; i<lon; i++){
			String simboloFila = variables.get(i);
			String simboloColumna = null;
			ArrayList<Produccion> produccionesDeFila = producciones.get(simboloFila);
//			System.out.println("PROD FILA: " + produccionesDeFila);
			for(int j=0; j<lon; j++) {
				simboloColumna = variables.get(j);
				
				//System.out.println("simboloColumna: " + simboloColumna);
				//System.out.println("PROD FILA: " + produccionesDeFila);
				Iterator<Produccion> it = produccionesDeFila.iterator();
				boolean enc = false;
				
				while (it.hasNext() && !enc){
					Produccion aux = it.next();
					int k = 0;
					int tam = aux.getConcatenacion().size();
						while (k < tam){
					//			System.out.println("aux.getConcatenacion(): " + aux.getConcatenacion());
								String primerSimbolo = aux.getConcatenacion().get(k).charAt(0) + "";
					//			System.out.println("simboloColumna: " + simboloColumna);
					//			System.out.println("primerSimbolo: " + primerSimbolo);
								if (simboloColumna.equals(primerSimbolo)){
									tabla[i][j] = true; enc = true;
									tablaTieneMarcas=true;
								//	System.out.println("**I J MARCADAS**: " + i + "," + j);
									//if (!hayMarcadas){
										if (columna == null) 
											columna = new HashMap<Integer,ArrayList<Integer>>();
										
										if (columna.isEmpty()){
											ArrayList<Integer> a = new ArrayList<Integer>();
											a.add(new Integer(i));
											columna.put(new Integer(j), a);	
											clave = j;
										}
										else{
											if (columna.containsKey(j)){
											
												ArrayList<Integer> a = columna.get(j);
												a.add(new Integer(i));
												columna.put(new Integer(j), a);
											}
										}
										
								//	}
								}
								k++;
						}
						//if (columna != null) hayMarcadas = true;
				}
			}
		}
//		System.out.println("**PRIMERA COLUMNA**: " + columna);
//		pintaTabla();
		//return gramatica;
	}
	//------------------------------------------------------------
	public void pintaTabla(){
		for(int i=0; i<lon; i++)
			for(int j=0; j<lon; j++)
		
				System.out.println("I: " + i + " J: " + j + "VALOR: " + tabla[i][j]);
		

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
	public void sustituir(){
		
		HashMap<String, ArrayList<Produccion>> producciones = gramaticaSalida.getProducciones();
//		System.out.println("producciones" + producciones);
		ArrayList<Produccion> produccionesDeColumna = producciones.get(gramaticaSalida.getVariables().get(clave));
		
//		System.out.println("produccionesDeColumna" + produccionesDeColumna);

		ArrayList<Integer> filas = columna.get(clave);
		Iterator<Integer> itFilas = filas.iterator();

		while (itFilas.hasNext()){
			Integer indice = itFilas.next();
			ArrayList<Produccion> produccionesDeFilas = producciones.get(gramaticaSalida.getVariables().get(indice.intValue()));
//			System.out.println("produccionesDeFilas " + produccionesDeFilas);
			int i = 0;int tam = produccionesDeFilas.get(0).getConcatenacion().size();
			while( i < tam){
				String p = produccionesDeFilas.get(0).getConcatenacion().get(i);
				String pp = "" +  p.charAt(0);
//				System.out.println("p " + p);
				
				if (pp.equals(gramaticaSalida.getVariables().get(clave))){
					String aux2 = null;
					
					Iterator<String> itProdCol = produccionesDeColumna.get(0).getConcatenacion().iterator();
					while (itProdCol.hasNext()){
						String aux3 = itProdCol.next();
						if (p.length() > 1){ 
							aux2 = aux3.concat(p.substring(1));
						}
						else aux2 = aux3;
						
//						System.out.println("produccionesDeFilas.get(0).getConcatenacion() " + produccionesDeFilas.get(0).getConcatenacion());
//						System.out.println("produccionesDeFilas.get(0).getConcatenacion().get(i) " + produccionesDeFilas.get(0).getConcatenacion().get(i));
						
						produccionesDeFilas.get(0).anadeCadena(aux2);
					}
					produccionesDeFilas.get(0).getConcatenacion().remove(i);
				}
				
				i++;
			}


		}
		

	}
	//************************************************************
	public void sustituirDiagonal(){
		
		HashMap<String, ArrayList<Produccion>> producciones = gramaticaSalida.getProducciones();
		ArrayList<Produccion> produccionesDiagonal = producciones.get(gramaticaSalida.getVariables().get(clave));
		System.out.println("produccionesDiagonal" + produccionesDiagonal);
		int i = 0;
		boolean enc = false;
		String var = gramaticaSalida.getVariables().get(clave);
		String nVar = null;
		while (i < produccionesDiagonal.size() && !enc){
			
			String aux = produccionesDiagonal.get(0).getConcatenacion().get(i);
			System.out.println("aux: " + aux);
			String s = aux.charAt(0) + "";
			if (s.equals(var)){
				String aux2 = null;
				if (aux.length() > 1) 
					aux2 = aux.substring(1);
				
				enc = true;
				char nVarAux = new Character ((char)(var.charAt(0)+1) ) ;
				nVar =  nVarAux + "";
				//System.out.println("nVar.charValue()" + nVar);
				gramaticaSalida.getVariables().add(/*var.concat(var)*/nVar);
				ArrayList<Produccion> p = new ArrayList<Produccion>();
				Produccion pod = new Produccion();
				pod.anadeCadena(/*var*/aux2);
				System.out.println("nVar " + nVar);
				pod.anadeCadena(aux2.concat(nVar));
				p.add(pod);
				System.out.println("p nueva: " + p);
				gramaticaSalida.getProducciones().put(/*var.concat(var)*/nVar, p);

				
				produccionesDiagonal.get(0).getConcatenacion(). remove(i);
				

				
			}
			i++;
			
		}
		Produccion produccionesNuevas = new Produccion();
		produccionesNuevas.setConcatenacion(produccionesDiagonal.get(0).getConcatenacion());
		int tam = produccionesDiagonal.get(0).getConcatenacion().size();
		for(int j = 0; j < tam; j++){
			String adds = produccionesDiagonal.get(0).getConcatenacion().get(j);
			System.out.println("adds" + adds);
			produccionesNuevas.getConcatenacion().add(adds.concat(nVar));	
		}
		ArrayList<Produccion> pfinal = new ArrayList<Produccion>();
		pfinal.add(produccionesNuevas);
		gramaticaSalida.getProducciones().remove(clave);
		gramaticaSalida.getProducciones().put(var, pfinal);
	}
	
	//************************************************************
	public static void main(String[] args){
		
/*		Gramatica g = new GramaticaIC();
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
		pod.anadeCadena("\\"); pod.anadeCadena("a"); pod.anadeCadena("UTT"); pod.anadeCadena("abT"); p.add(pod); 
		lala.put("S",p);


		p = new ArrayList<Produccion>();
		pod = new Produccion();
		pod.anadeCadena("UT"); pod.anadeCadena("ab"); p.add(pod); 
		lala.put("T",p);
		
		p = new ArrayList<Produccion>();
		pod = new Produccion();
		pod.anadeCadena("UTT"); pod.anadeCadena("abT"); pod.anadeCadena("bbb"); p.add(pod); 
		lala.put("U",p);
		



		
		g.setProducciones(lala);
		

		

		
		System.out.println("GRAMATICA: " + g);
		GIC_to_FNC piticli = new GIC_to_FNC(g);
		while(piticli.getTablaTieneMarcas()){
			
			if (!piticli.diagonalMarcada()){ System.out.println("DIAGONAL NO "); piticli.sustituir();}
			else{ System.out.println("DIAGONAL SI "); piticli.sustituirDiagonal();}
			piticli.transforma_FNG();
		}
		System.out.println("gram.getProducciones() " + piticli.getGramaticaEntrada().getProducciones());
		
		
	}*/
		AutomataPila aut = new AutomataPila();
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
		
		AristaAP arist;
		
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
		System.out.println("gram.getProducciones() " + piticli.getGramaticaEntrada().getProducciones());

	}
}