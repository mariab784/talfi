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
 * transformación de una gramática cualquiera a una gramática 
 * en forma normal de Greinbach.
 *  @author Rocio Barrigüete, Mario Huete, Luis San Juan
 */
public class GIC_to_FNG {
	//ATRIBUTOS:**************************************************
	private final String auxvariable;
	private int contadoraux;
	private GramaticaIC gramaticaEntrada;
	private Greibach gramaticaSalida;
	private String xml2;
	private String xmllatex;
	private boolean tabla[][];
	private int lon;
	private HashMap<Integer,ArrayList<Integer>> columna;
	private int clave;
	private boolean tablaTieneMarcas;
	@SuppressWarnings("unused")
	private Controlador controlador;
	private Mensajero mensajero;
	private String lambda;


	//************************************************************
	public GIC_to_FNG(GramaticaIC g,boolean b){
		if (mensajero == null) mensajero=Mensajero.getInstancia();
		controlador=Controlador_imp.getInstancia();
		gramaticaEntrada = g; 
		gramaticaSalida = new Greibach(g.getVariables(),g.getSimbolos(),g.getProducciones(),g.getVariableInicial());
		contadoraux = 0;
		xml2= "<exit>";
		xml2+=g.getXML();
		xml2+="<steps>\n";
		xmllatex=g.getXMLLatex();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		auxvariable = mensajero.devuelveMensaje("simbolos.auxvariable",4);
		transforma_FNG(b);
		

	}
	//MÉTODOS:****************************************************
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
		this.xml2 = xml;
	}
	//------------------------------------------------------------
	public String getXML() {
		return xml2;
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


		Greibach gramsal = this.gramaticaSalida;
		ArrayList<String> vargram = gramsal.getVariables();
		HashMap<String,ArrayList<Produccion>> gramsalprod = gramsal.getProducciones();
		HashMap<String,ArrayList<Produccion>> ngramsalprod = new HashMap<String,ArrayList<Produccion>>();
		int i = 0; int tam = vargram.size();
		while(i < tam){
			String s = vargram.get(i);
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
		
	}
	//------------------------------------------------------------
	private/*public*/ void transforma_FNG(boolean mostrarPasos){

		limpia();

		ArrayList<String> variables = gramaticaSalida.getVariables();
		HashMap<String, ArrayList<Produccion>> producciones = gramaticaSalida.getProducciones();
		lon = variables.size(); 
		tabla = new boolean[lon][lon]; 
		inicializarTabla();
		tablaTieneMarcas=false;

		xml2+="<step>"+"<titulo>" + mensajero.devuelveMensaje("simplificacionGICs.gramatica",2) + 
		"</titulo>"+gramaticaSalida.toXML()+"</step>\n";
		xmllatex+="<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.gramaticalat",2)+"</titulo>"+
		gramaticaSalida.toLat()+"</step>\n";
		
		for(int i=0; i<lon; i++){ //recorre variables fila
			String simboloFila = variables.get(i);
			String simboloColumna = null;
			ArrayList<Produccion> produccionesDeFila = producciones.get(simboloFila);

			for(int j=0; j<lon; j++) { //recorre variables columna
				simboloColumna = variables.get(j);
				
				Iterator<Produccion> it = produccionesDeFila.iterator();
				boolean enc = false;
				
				while (it.hasNext() && !enc){
					Produccion aux = it.next();
					int k = 0;

								String primerSimbolo = aux.getConcatenacion().get(0);
								if (simboloColumna.equals(primerSimbolo)){
									tabla[i][j] = true; enc = true;
									tablaTieneMarcas=true;
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
	private/*public*/ void pintaTabla(){
		
		xml2+="<step><table>";
		xml2+="<fila>";
		xmllatex+="<step><table>";
		xmllatex+="<fila>";
		
		for(int i = 0; i <lon; i++){
			
			

			if (i == 0) {
				xml2+="<item>"+" - "+"</item>";
				xmllatex+="<item>"+" - "+"</item>";

			}

			xml2+="<item>"+ this.getGramaticaSalida().getVariables().get(i) +"</item>";
			xmllatex+="<item>"+ this.getGramaticaSalida().getVariables().get(i) +"</item>";
		}
		xml2+="</fila>";
		xmllatex+="</fila>";

		for(int i=0; i<lon; i++){

			xml2+="<fila>";
			xmllatex+="<fila>";
			for(int j=0; j<lon; j++){

				if (j == 0){


					xml2+="<item>"+this.getGramaticaSalida().getVariables().get(i) + " "+"</item>";
					xmllatex+="<item>"+this.getGramaticaSalida().getVariables().get(i) + " "+"</item>";
				}
				if (tabla[i][j]) {

					xml2+="<item> X </item>";
					xmllatex+="<item> X </item>";
				}
				else {

					xml2+="<item> - </item>";
					xmllatex+="<item> - </item>";
				
				}
			}

			xml2+="</fila>";
			xmllatex+="</fila>";
		}

		xml2+="</table></step>\n";
		xmllatex+="</table></step>\n";

	}
	
	private/*public*/ void inicializarTabla() {
		for(int i=0; i<lon; i++)
			for(int j=0; j<lon; j++) 
				tabla[i][j] = false;
	}
	//************************************************************
	private/*public*/ boolean diagonalMarcada(){
		
		ArrayList<Integer> aa = columna.get(clave);
		return aa.contains(clave);
	}
	//************************************************************	

	
	
	@SuppressWarnings("unchecked")
	private/*public*/ void sustituir(){
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
			int i = 0;int tam = produccionesDeFilas.size();
			
			while( i < tam){ //recorre las producciones filas marcadas
				Produccion aux2 = produccionesDeFilas.get(i);
			
				ArrayList<String> ayay = ((ArrayList<String>)aux2.getConcatenacion().clone());
				String p = new String(ayay.get(0));
				String pp = p/*"" +  p.charAt(0)*/;

				Produccion nueva = null;
				if (pp.equals(gramaticaSalida.getVariables().get(clave))){
					Iterator<Produccion> itProdCol = produccionesDeColumna/*.get(0).getConcatenacion()*/.iterator();
					aux2.getConcatenacion().remove(0);
					while (itProdCol.hasNext()){ //recorre las producciones de las columnas
						nueva = new Produccion();
						Produccion aux3 = itProdCol.next();
						
						ArrayList<String> nueva2 = (ArrayList<String>)aux3.getConcatenacion().clone();

						for(int k = 0; k< aux2.getConcatenacion().size();k++){
							nueva2.add(aux2.getConcatenacion().get(k));
							
						}
						nueva.setConcatenacion(/*arreglaConcatenacion(*/nueva2/*)*/); //XXX CAMBIADO CMBIADOOOOOOOOOO
						
						if (!esta(nueva,pnueva))pnueva.add(nueva);

					}					
				}
				else{
					pnueva.add(aux2);					
				}
				i++;
			}
			gramaticaSalida.getProducciones().remove(var);
			gramaticaSalida.getProducciones().put(var, pnueva);
		}
		
		limpia();

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

	
	@SuppressWarnings("unchecked")
	private/*public*/ void sustituirDiagonal(){
		
		limpia();
		HashMap<String, ArrayList<Produccion>> producciones = gramaticaSalida.getProducciones();
		ArrayList<Produccion> produccionesDiagonal;
		produccionesDiagonal = producciones.get(gramaticaSalida.getVariables().get(clave));

		String var = gramaticaSalida.getVariables().get(clave);

		String nVar = null;
		int tam = gramaticaSalida.getVariables().size()-1;
		String ultVar = gramaticaSalida.getVariables().get(tam);
		System.out.println("tam ultvar: " + ultVar.length());
		if(ultVar.length() > 1){
			nVar = "[" + auxvariable + contadoraux + "]";
			contadoraux++;
		}
		else{
			char nVarAux = new Character ((char)(ultVar.charAt(0)+1) ) ;
			nVar =  nVarAux + "";
		}
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
				p.add(pod);
			}
			else{
				concat = (ArrayList<String>)a.clone();
				concat.remove(0);
				pod = new Produccion();
				pod.setConcatenacion(/*arreglaConcatenacion(*/concat/*)*/); //XXX CAMBIADOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
				if (!concat.isEmpty())produccionesNuevas.add(pod);
				pod = new Produccion();
				concat = (ArrayList<String>)a.clone();
				concat.remove(0);
				pod.setConcatenacion(/*arreglaConcatenacion(*/concat/*)*/); //XXX CAMBIADOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
				pod.anadeCadena(nVar);
				produccionesNuevas.add(pod);
			}
		}
		if(produccionesNuevas.size() == 1 || produccionesNuevas.isEmpty()){
						System.out.println("produccionesNuevas ke es? " + produccionesNuevas);
			//			System.out.println("NO ME AÑADAS!!");
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

		return pp;
	}
	
	public void ejecuta(boolean b,boolean pasarXml){
		simplifica(b,pasarXml);
	}
	
	private/*public*/ void simplifica(boolean b,boolean pasarXml){
		
		while(getTablaTieneMarcas()){
			
			if (!diagonalMarcada()){ /*System.out.println("DIAGONAL NO ");*/ sustituir();}
			else{ /*System.out.println("DIAGONAL SI ");*/ sustituirDiagonal();}
			transforma_FNG(b);
			
		}
		//quitamos las lambdas que no esten en S
		boolean bol = gramaticaSalida.dimeSiHayProdMulti();

		while(bol){
			gramaticaSalida.quitaProdMulti();
			bol = gramaticaSalida.dimeSiHayProdMulti();
			this.limpia();
		}
		
		xmllatex+="<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.gsalidasimsimplatex", 2)+
		"</titulo>"+gramaticaSalida.toLat()+"</step>\n</steps>\n";
		xml2+="\n<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.gsalidasimsimp", 2)+
		"</titulo>"+gramaticaSalida.toXML()+"</step>\n";
		
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
				xml2+="\n</steps>\n<result>"+cinta+"</result></exit>";
				
			}

			catch(FileNotFoundException e){
				JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("vista.nocinta", 2),mensajero.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
				
				
			}
	    	catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
		

	}
	
	//************************************************************
	public String getXMLLatex(){

		return xmllatex;
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
	
		p = new ArrayList<Produccion>();
		pod = new Produccion();
		pod.anadeCadena("T"); pod.anadeCadena("T");
		p.add(pod);
		pod = new Produccion();
		pod.anadeCadena("b"); pod.anadeCadena("b"); pod.anadeCadena("b"); 
		p.add(pod); 
		lala.put("U",p);
				
		g.setProducciones(lala);
		
		

		GIC_to_FNG piticli = new GIC_to_FNG(g,true);
		piticli.simplifica(true,false);

		System.out.println("ENTRADA!" + piticli.gramaticaEntrada);
		System.out.println("salida!" + piticli.gramaticaSalida);
		
		piticli.getGramaticaSalida().creaListaPalabras();
		
	
		AutomataPila aut = new AutomataPila();

		aut.getEstados().add("s0");
		aut.getEstados().add("s1");
		aut.getEstados().add("s2");

		aut.setEstadoInicial("s0");
		aut.setEstadoFinal("s2");

		AristaAP arist;
		

		
		arist = new AristaAP(0,0,0,0,"s0","s0");
		arist.anadirSimbolo("a");
		arist.setCimaPila("Z");
		arist.anadirPila("AZ");
		
		aut.anadeArista(arist);
		
		arist = new AristaAP(0,0,0,0,"s0","s0");		
		arist.anadirSimbolo("a");
		arist.setCimaPila("A");
		arist.anadirPila("AA");
		
		aut.anadeArista(arist);	
		
		
		arist = new AristaAP(0,0,0,0,"s0","s1");
		arist.anadirSimbolo("b");
		arist.setCimaPila("A");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);	
	
		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("b");
		arist.setCimaPila("A");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);
		
		arist = new AristaAP(0,0,0,0,"s1","s2");
		arist.anadirSimbolo("b");
		arist.setCimaPila("Z");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);		

	}
}