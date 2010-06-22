package modelo.algoritmos;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import accesoBD.Mensajero;

import controlador.Controlador;
import controlador.Controlador_imp;


import vista.vistaGrafica.AristaAP;


import modelo.automatas.AutomataPila;
import modelo.gramatica.Gramatica;
import modelo.gramatica.GramaticaIC;
import modelo.gramatica.Greibach;
import modelo.gramatica.Produccion;
/**
 * Clase que ejecuta los procedimientos para llevar a cabo la 
 * transformaci�n de una gram�tica cualquiera a una gram�tica 
 * en forma normal de Greinbach.
 *  @author Rocio Barrig�ete, Mario Huete, Luis San Juan
 */
public class GIC_to_FNG {
	//ATRIBUTOS:**************************************************
	private GramaticaIC gramaticaEntrada;
	private Greibach gramaticaSalida;
	private String xml;
	private String html;
	private boolean tabla[][];
	private int lon;
	private HashMap<Integer,ArrayList<Integer>> columna;
	private int clave;
	private boolean tablaTieneMarcas;
	@SuppressWarnings("unused")
	private Controlador controlador;
	private Mensajero mensajero;
	private String lambda;
	private String lat;
	//************************************************************
	public GIC_to_FNG(GramaticaIC g,boolean b){
		if (mensajero == null) mensajero=Mensajero.getInstancia();
		controlador=Controlador_imp.getInstancia();
		gramaticaEntrada = g; 
		gramaticaSalida = new Greibach(g.getVariables(),g.getSimbolos(),g.getProducciones(),g.getVariableInicial());
		xml= "<exit><steps>\n";
		html="";
		lat="";
		lat+="\\documentclass[a4paper,11pt]{article}\n" + 
		     "\\usepackage[latin1]{inputenc}\n" +
		     "\\usepackage{ulem}\n" +
		     "\\usepackage{a4wide}\n" + 
		     "\\usepackage[dvipsnames,svgnames]{xcolor}\n" +
		     "\\usepackage[pdftex]{graphicx}\n" + 
		     "\\usepackage{hyperref}\n" +
             "\n" +
		     "\\newcommand{\\MYp}[1]{ {\\color[rgb]{0.392,0.392,0.392}#1} }\n" +
		     "\\newcommand{\\MYunder}[1]{ {\\color[rgb]{0.2,0.209,0.3}\\underline{#1}} }\n" +
			 "\n" +
			 "\\begin{document}\n" +
			 "\n" +
		     "\\MYp{\n"+    
		     "\\includegraphics{fdi.jpg}}\n"+
		     "\n" +
		     "\\begin{center}\n" +
		     "\\MYp{\\Large Entrada}\n" +
		     "\\includegraphics{}}\n" +
		     "\\newline\n" +
		     "\\newline\n" +
		     "\\newline\n" +
		     "\\newline\n" +
		     "\n" +
		 //aut�mata a pila dibujo
		 //String rutaimagen="HTML/imagen"+i+".jpg";
		 //generarImagenJPG(rutaimagen, automata);
		     "\\MYp{\\Huge Gram\\'{a}tica}\n" +
		     "\\newline\n" +
		     "\\newline\n" +
		     "\n" +
		     gramaticaSalida.toLat() + "\n";

		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		transforma_FNG(b);

	}
	//M�TODOS:****************************************************
	//------------------------------------------------------------
	public void registraControlador(Controlador controlador) {
		// TODO Auto-generated method stub
		this.controlador=controlador;
	}
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
	public String getXML() {
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
	
	private void limpia(){

//		System.out.println("LIMPIA AL PRINCIPIO GRAMATICASALIDA ES: " + gramaticaSalida);
		Greibach gramsal = this.gramaticaSalida;
		ArrayList<String> vargram = gramsal.getVariables();
		HashMap<String,ArrayList<Produccion>> gramsalprod = gramsal.getProducciones();
		HashMap<String,ArrayList<Produccion>> ngramsalprod = new HashMap<String,ArrayList<Produccion>>();
		int i = 0; int tam = vargram.size();
		while(i < tam){
			String s = vargram.get(i);
			//System.out.println("SE KE PETA " + s);
			//System.out.println("vars " + gramsal.getVariables());
			ArrayList<Produccion> aprod = gramsalprod.get(s);
			ArrayList<Produccion> naprod = new ArrayList<Produccion>();
			int j = 0; int tamProd = aprod.size();
			while(j < tamProd){
				Produccion pr = aprod.get(j);
				ArrayList<String> concat = pr.getConcatenacion();
				Produccion npr = new Produccion();
				ArrayList<String> nconcat = arreglaConcatenacion(concat);
				if(!nconcat.isEmpty())npr.setConcatenacion(nconcat);
				
				j++;
				if(!nconcat.isEmpty() && !esta(npr,naprod)) naprod.add(npr);
			}
			ngramsalprod.put(s, naprod);
			i++;
		}
		this.gramaticaSalida.setProducciones(ngramsalprod);
//		System.out.println("GRAMATICASALIDA LIMPITA ES: " + gramaticaSalida);
		
	}
	//------------------------------------------------------------
	public void transforma_FNG(boolean mostrarPasos){

		//vamos a limpiar a ver si consigo ke funcione de una puta vez
		limpia();
//		this.gramaticaSalida.dimeSiHayProdRecursivas();
		//this.gramaticaSalida.quitaProdRecursivas();

		ArrayList<String> variables = gramaticaSalida.getVariables();
		HashMap<String, ArrayList<Produccion>> producciones = gramaticaSalida.getProducciones();
		lon = variables.size(); 
		tabla = new boolean[lon][lon]; 
		inicializarTabla();
		tablaTieneMarcas=false;

		html+= "<br><h2>Gramatica</h2>" + gramaticaSalida.toHTML() + "<br>";
		
		
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
								String primerSimbolo = aux.getConcatenacion().get(0)/*.toString();*//*.charAt(0) + ""*/;
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
		
		if (mostrarPasos){

			pintaTabla();
		}
		
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
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX//
	public void pintaTabla(){
		
		lat += "\\begin{tabular}{||";
		for(int i = 0; i <lon; i++){
			lat += "c||";
		}
		lat += "}\n" +
			"\\hline\n" + 
		       "\\hline\n" +
		       "-";
		for(int i = 1; i <lon; i++){
				lat += " & "+this.getGramaticaSalida().getVariables().get(i);
		}
		lat += " \\\\"+
			   "\n"+
			   "\\hline\n" + 
	           "\\hline\n";
		for(int i=0; i<lon; i++){
			for(int j=0; j<lon-1; j++){
				if (j == 0){
					lat += this.getGramaticaSalida().getVariables().get(i);
				}
				if (tabla[i][j]) {
					lat += " & X";
				}
				else {
					lat += " & -";
				}
			}
			lat += " \\\\"+
			   "\n"+
			   "\\hline\n" + 
	           "\\hline\n";
		}
		lat += "\\end{tabular}\n" + 
		       "\\\\" +
		       "\n";

		html +="<table>";
		//xml+="<exit>";
		//xml+="<steps>\n";
		xml+="<step><table>";
		xml+="<column>";
		html +="<tr>";
		for(int i = 0; i <lon; i++){
			xml+="<fila>"; //html +="<td>";
			if (i == 0) {xml+="  "; html +="<td> - </td>"; System.out.print("  ");}
			//xml+="<step>";
			xml+=this.getGramaticaSalida().getVariables().get(i) + " ";
			html +="<td>" + this.getGramaticaSalida().getVariables().get(i) + " </td>";
			//xml+="</step>";
			xml+="</fila>"; //html +="</td>";
			System.out.print(this.getGramaticaSalida().getVariables().get(i) + " ");
		}
		xml+="</column>";
		html +="</tr><br>";
		System.out.println();
		for(int i=0; i<lon; i++){
			html+="<tr>";
			for(int j=0; j<lon; j++){
			//	String s;
	//			xml+="<step>";
				if (j == 0){
					xml+=(this.getGramaticaSalida().getVariables().get(i) + " ");
					html+="<td>"+this.getGramaticaSalida().getVariables().get(i) + " " + "</td>";
					System.out.print(this.getGramaticaSalida().getVariables().get(i) + " ");
				}
				if (tabla[i][j]) {
					xml+="X ";System.out.print("X ");
					html+="<td> X </td>";
				}
				else {
					xml+="- ";System.out.print("- ");
					html+="<td> - </td>";
				
				}
				//xml+="</step>";
				//System.out.println("I: " + i + " J: " + j + "VALOR: " + tabla[i][j]);
			}
			System.out.println();html+="</tr>";
		}
		xml+="</table></step>\n";
		//xml+="</exit>";
		html +="</table>";
		//System.out.println("XML PINTATABLA: " + xml);
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
		limpia();
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
				String p = new String(ayay.get(0));
				String pp = p/*"" +  p.charAt(0)*/;
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
						nueva.setConcatenacion(/*arreglaConcatenacion(*/nueva2/*)*/); //XXX CAMBIADO CMBIADOOOOOOOOOO
//						System.out.println("nueva: "+ nueva);
						
						if (!esta(nueva,pnueva))pnueva.add(nueva);
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
		
		//A�ADIDOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
		limpia();
		
		System.out.println("GRAMATICA eres tu?: " + gramaticaSalida.getProducciones());
	//	System.out.println("GRAMATICA ENTRADA: " + gramaticaEntrada.getProducciones());

	}
	//************************************************************
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
	
	private String dameEntreCorchetes(String ultVar){
		
		String s = "";
		int i = 0; int tam = ultVar.length();
		while(i < tam){
			String aux = ultVar.charAt(i)+"";
			if(!aux.equals("[") && !aux.equals("]"))
				s +=aux;
			i++;
		}
		System.out.println("ke devuelve entrecorchetes?" + s);
		return new String(s);
	}
	
	@SuppressWarnings("unchecked")
	public void sustituirDiagonal(){
		
		limpia();
		HashMap<String, ArrayList<Produccion>> producciones = gramaticaSalida.getProducciones();
		ArrayList<Produccion> produccionesDiagonal;
		produccionesDiagonal = producciones.get(gramaticaSalida.getVariables().get(clave));

		String var = gramaticaSalida.getVariables().get(clave);

		String nVar = null;
		int tam = gramaticaSalida.getVariables().size()-1;
		String ultVar = gramaticaSalida.getVariables().get(tam);
		
		char nVarAux = new Character ((char)(ultVar.charAt(0)+1) ) ;

		nVar =  nVarAux + "";

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
				
				ArrayList<String> copiaConcat = (ArrayList<String>) paux.getConcatenacion().clone();
				Produccion ppaux = new Produccion();
				ppaux.setConcatenacion(copiaConcat);
				p.add(/*paux*/ppaux);
				concat = (ArrayList<String>)a.clone();
				concat.add(nVar);
				pod = new Produccion();

				pod.setConcatenacion(/*arreglaConcatenacion(*/concat/*)*/); //XXX CAMBIADOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
		//		System.out.println("ke es pod?1" + pod);
				p.add(pod);
		//		System.out.println("ke es p?1" + p);
//				System.out.println("p " + p);
			}
			else{
				concat = (ArrayList<String>)a.clone();
				concat.remove(0);
				pod = new Produccion();
				pod.setConcatenacion(/*arreglaConcatenacion(*/concat/*)*/); //XXX CAMBIADOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
				//				System.out.println("ke es pod? 1" + pod);
				if (!concat.isEmpty())produccionesNuevas.add(pod);
//				System.out.println("pod1 " + pod);
				//pod = new Produccion();
				pod = new Produccion();
				concat = (ArrayList<String>)a.clone();
				concat.remove(0);
				pod.setConcatenacion(/*arreglaConcatenacion(*/concat/*)*/); //XXX CAMBIADOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
				pod.anadeCadena(nVar);
//				System.out.println("pod2 " + pod);
				//				System.out.println("ke es pod? 2" + pod);
				produccionesNuevas.add(pod);
				//				System.out.println("produccionesNuevas " + produccionesNuevas);
			}
		}
		System.out.println("produccionesNuevas ke es? " + produccionesNuevas);
		if(produccionesNuevas.size() == 1 || produccionesNuevas.isEmpty()){
						System.out.println("produccionesNuevas ke es? " + produccionesNuevas);
			//			System.out.println("NO ME A�ADAS!!");
			gramaticaSalida.getProducciones().remove(var); //CAMBIADO
			ArrayList<Produccion> pajus = quitaProdRecursiva(produccionesDiagonal,var);
			gramaticaSalida.getProducciones().put(var, pajus);		
		}
		else{
			gramaticaSalida.getProducciones().remove(var);
			
			gramaticaSalida.getProducciones().put(var, p);
			gramaticaSalida.getProducciones().put(nVar, produccionesNuevas);
			gramaticaSalida.getVariables().add(nVar);
		}
		


		
		System.out.println("GRAMATICA salida diagonal: " + gramaticaSalida.getProducciones());
	}
	
	private ArrayList<Produccion> quitaProdRecursiva(ArrayList<Produccion> p,String var){
		
		ArrayList<Produccion> pp = new ArrayList<Produccion>();
		int i = 0; int tam = p.size();
		while(i < tam){
			Produccion ap = p.get(i);
			ArrayList<String> con= this.arreglaConcatenacion(ap.getConcatenacion());
			if(con.size() == 1 && con.get(0).equals(var)){}
			else	if(!esta(ap,pp)) pp.add(ap);
			
			i++;
			
		}
		System.out.println("KE DEVUELVE QUITAPRODREC?"+ pp);
		return pp;
	}
	
	public void simplifica(boolean b,boolean pasarXml){
		
		while(getTablaTieneMarcas()){
			
			if (!diagonalMarcada()){ System.out.println("DIAGONAL NO "); sustituir();}
			else{ System.out.println("DIAGONAL SI "); sustituirDiagonal();}
			transforma_FNG(b);
			
		}
		//quitamos las lambdas que no esten en S
		boolean bol = gramaticaSalida.dimeSiHayProdMulti();
		int i = 0;
		while(bol){
			gramaticaSalida.quitaProdMulti();
			bol = gramaticaSalida.dimeSiHayProdMulti();
			i++;
			System.out.println("VUELTAS kita lambda: " + i);
			this.limpia();
		}
		
		
		html+="<br><h2>Gramatica final simplificada</h2>" + gramaticaSalida.toHTML();
		lat += "\\MYp{\\Huge Gram\\'{a}tica final simplificada}\n"+
	       gramaticaSalida.toLat();
		
		if (pasarXml){
			
			String rutaxmlconaut="XML/pruebas/ficheroG.xml";
			File archivo = null;
			FileReader fr = null;
			BufferedReader br = null;
			String cinta = "";
			try {
				// Apertura del fichero y creacion de BufferedReader para poder
				// hacer una lectura comoda (disponer del metodo readLine()).
				archivo = new File(rutaxmlconaut);
				fr = new FileReader (archivo);
				br = new BufferedReader(fr);
	        // Lectura del fichero
				String linea;
				while((linea=br.readLine())!=null)
					cinta += linea+"\n";
	        
				br.close();
				fr.close();
				xml+="\n<result>"+cinta+"</result></steps></exit>";
			}
			//xml+="</exit>";
			catch(FileNotFoundException e){
				JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("vista.nocinta", 2),mensajero.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
				
				
			}
	    	catch(Exception e){
	    		System.out.println("TROCOTRO");
	    		e.printStackTrace();
	    	}
	    }
		

	}
	
	//************************************************************
	public String getLatex(){
		lat+="\\end{center}\n"+
        "\n" +
        "\\end{document}";
		lat.replace("[\\,","[/,");
		/*lat.replaceAll("[ \\,","[/,");
		lat.replaceAll("[\\ ,","[/,");
		lat.replaceAll("[ \\ ,","[/,");
		lat.replaceAll(",\\,",",/,");
		lat.replaceAll(", \\,",",/,");
		lat.replaceAll(",\\ ,",",/,");
		lat.replaceAll(", \\ ,",",/,");
		lat.replaceAll(",\\]",",/]");
		lat.replaceAll(", \\]",",/]");
		lat.replaceAll(",\\ ]",",/]");
		lat.replaceAll(", \\ ]",",/]");*/
		return lat;
	}
	
	public String getHTML(){
		
		return html;
	}
	
	private ArrayList<String> arreglaConcatenacion(ArrayList<String> nconcat){
		//nconcat no sera ni null ni vacio nunca

		if (todosLambda(nconcat)){ //tambien lambda solo incluido
			ArrayList<String> ss = new ArrayList<String>();
			ss.add(lambda);
			return ss;
		}
		
		ArrayList<String> s = new ArrayList<String>();
		int tam = nconcat.size();
		int i = 0;
		while(i < tam){
			String ss = nconcat.get(i);
			if(!ss.equals(lambda) && 
					(this.gramaticaSalida.getVariables().contains(ss)
							|| this.gramaticaSalida.getSimbolos().contains(ss))){s.add(
					new String(ss));
			}
			i++;
		}
		return s;
	}
	
	private boolean todosLambda(ArrayList<String> nconcat){
		
		int i = 0; int tam = nconcat.size();
		while(i < tam){
			String s = nconcat.get(i);
			if (!s.equals(lambda)) return false;
			i++;
		}
		return true;
	}
	
	//************************************************************
	public static void main(String[] args){
		
		GramaticaIC g = new GramaticaIC();
		g.setVariableInicial("S");
		ArrayList<String> s = new ArrayList<String>();
		s.add("S");s.add("T");s.add("U");
		g.setVariables(s);
		s = new ArrayList<String>();
		/*s.add("\\");*/s.add("a");s.add("b");
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
		GIC_to_FNG piticli = new GIC_to_FNG(g,true);
		piticli.simplifica(true,false);
//		System.out.println("XML MAIN: \n" + piticli.getXML());
		System.out.println("ENTRADA!" + piticli.gramaticaEntrada);
		System.out.println("salida!" + piticli.gramaticaSalida);
		
		piticli.getGramaticaSalida().creaListaPalabras();
		
/*		while(piticli.getTablaTieneMarcas()){
			
			if (!piticli.diagonalMarcada()){ System.out.println("DIAGONAL NO "); piticli.sustituir();}
			else{ System.out.println("DIAGONAL SI "); piticli.sustituirDiagonal();}
			piticli.transforma_FNG(false);
			
		}*/
	//	System.out.println("gram.getProducciones() " + piticli.getGramaticaEntrada().getProducciones());
		
		
	
		AutomataPila aut = new AutomataPila();
//		AutomataPila aut2 = new AutomataPila();
		//a.listaEstados.add(new Estado(0,0,"s1"));
aut.getEstados().add("s0");//		aut.getEstados().add("s1");
aut.getEstados().add("s1");//		aut.getEstados().add("s2");
aut.getEstados().add("s2");//		aut.getEstados().add("s3");

		//		aut.getEstados().add("s4");

aut.setEstadoInicial("s0");//		aut.setEstadoInicial("s1");
//aut.setEstadoFinal("s1");//		aut.setEstadoFinal("s3");
		aut.setEstadoFinal("s2");

/*		aut2.getEstados().add("s1");
		aut2.getEstados().add("s2");
		aut2.setEstadoInicial("s1");
		aut2.setEstadoFinal("s2");*/
		//System.out.println("ESTADOS: " + aut.getEstados());
		
		AristaAP arist;
		
		
		/*******/

		
/*		arist = new AristaAP(0,0,0,0,"s0","s0");//		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("\\");//		arist.anadirPila("Z");
		
		aut.anadeArista(arist);*/
	/******/
		
		arist = new AristaAP(0,0,0,0,"s0","s0");//		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("a");
		arist.setCimaPila("Z");

		arist.anadirPila("AZ");//		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		

		arist = new AristaAP(0,0,0,0,"s0","s0");		//arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("a");
		arist.setCimaPila("A");//		arist.setCimaPila("Z");
		arist.anadirPila("AA");//		arist.anadirPila("CZ");
		
		aut.anadeArista(arist);	
		
		//a�adido//
/*		arist = new AristaAP(0,0,0,0,"s0","s0");		//arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("b");
		arist.setCimaPila("Z");//		arist.setCimaPila("Z");
		arist.anadirPila("\\");//		arist.anadirPila("CZ");
		
		aut.anadeArista(arist);	*/
		
		arist = new AristaAP(0,0,0,0,"s0","s1");//		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("b");//		arist.anadirSimbolo("0");
		arist.setCimaPila("A");//		arist.setCimaPila("C");
		arist.anadirPila("\\");//		arist.anadirPila("CC");
		
		aut.anadeArista(arist);	
	
		arist = new AristaAP(0,0,0,0,"s1","s1");//		arist = new AristaAP(0,0,0,0,"s2","s2");
		arist.anadirSimbolo("b");		//arist.anadirSimbolo("1");
		arist.setCimaPila("A");		//arist.setCimaPila("C");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);
		
		arist = new AristaAP(0,0,0,0,"s1","s2");//		arist = new AristaAP(0,0,0,0,"s1","s3");
//		arist.anadirSimbolo("0");
		arist.anadirSimbolo("b");		//arist.anadirSimbolo("1");
		arist.setCimaPila("Z");		//arist.setCimaPila("C");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);		


//		AutomataP_to_GramaticaIC a = new AutomataP_to_GramaticaIC(aut);
		
//		System.out.println("aut :\n" + aut);
//		System.out.println(a.AP_Gramatica()/*.getProducciones().toString()*/);
		


//		piticli.getGramaticaSalida().creaListaPalabras();


	}
}