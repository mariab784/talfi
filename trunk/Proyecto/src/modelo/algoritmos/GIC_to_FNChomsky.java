package modelo.algoritmos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import vista.vistaGrafica.AristaAP;

import modelo.automatas.AutomataPila;
import modelo.gramatica.Chomsky;
import modelo.gramatica.GramaticaIC;

import modelo.gramatica.Produccion;
import accesoBD.Mensajero;
import controlador.Controlador;
import controlador.Controlador_imp;

public class GIC_to_FNChomsky {

	//ATRIBUTOS:**************************************************
	public static String constantVar = "C";
	public static String constantVar2 = "D";
	private int contador;
	private GramaticaIC gramaticaEntrada;
	private Chomsky gramaticaSalida;
	private String xml;
	private String html;
	private int lon;
	@SuppressWarnings("unused")
	private Controlador controlador;
	private Mensajero mensajero;
	private String lambda;
	private String lat;
	private HashMap<String,String> relacionados;
	private HashMap<String,ArrayList<String>> relacionadosCYK;
	//************************************************************
	public GIC_to_FNChomsky(GramaticaIC g,boolean b){
		if (mensajero == null) mensajero=Mensajero.getInstancia();
		controlador=Controlador_imp.getInstancia();
		gramaticaEntrada = g; 
		//gramaticaSalida = new Chomsky(g.getVariables(),g.getSimbolos(),g.getVariableInicial());
		xml= "<exit><steps>\n";
		html="";
/*		lat="";
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
		 //autómata a pila dibujo
		 //String rutaimagen="HTML/imagen"+i+".jpg";
		 //generarImagenJPG(rutaimagen, automata);
		     "\\MYp{\\Huge Gram\\'{a}tica}\n" +
		     "\\newline\n" +
		     "\\newline\n" +
		     "\n" +
		     gramaticaSalida.toLat() + "\n";*/

		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		relacionados = new HashMap<String,String>();
		contador = 0;
		html+="<br><h2>Gramatica</h2><center><table>" + gramaticaEntrada.toHTML() + "</table></center><br>";
		transforma_FNChomsky(b);


	}
	
	public GramaticaIC getGramaticaEntrada(){return gramaticaEntrada;}
	public Chomsky getGramaticaSalida(){return gramaticaSalida;}
	public String getHTML(){return html;}
	public String getXML(){return xml;}
	public void registraControlador(Controlador controlador) {
		// TODO Auto-generated method stub
		this.controlador=controlador;
	}
	@SuppressWarnings("unchecked")
	public void transforma_FNChomsky(boolean mostrarPasos){
		
		Chomsky g1 = new Chomsky(gramaticaEntrada.getVariables(),gramaticaEntrada.getSimbolos(),
				gramaticaEntrada.getVariableInicial());
		
		System.out.println("****************************FASE 1****************************");
		//xml+="<step><br>****************************FASE 1****************************<br>";
		html+="<center>**************FASE 1**************</center>";
		for(int i = 0; i < g1.getSimbolos().size();i++){
			
			String simb = g1.getSimbolos().get(i);
			String nvar = "["+constantVar+simb+"]";
			Produccion np = new Produccion();
			np.anadeCadena(simb);
			g1.anadeProduccion(nvar, np);
			relacionados.put(simb, nvar);
		}
		
		Iterator<String> itvar = gramaticaEntrada.getVariables().iterator();
		while(itvar.hasNext()){
			
			String v = itvar.next();
			Iterator<Produccion> itprod = gramaticaEntrada.getProducciones().get(v).iterator();
			ArrayList<Produccion> nprod = new ArrayList<Produccion>();
			Produccion np = null;
			while(itprod.hasNext()){
				Produccion p = itprod.next();
				if(p.getConcatenacion().size() > 1){
					np = comprueba(p);
					
				}
				else{
					np = new Produccion();
					np.setConcatenacion((ArrayList<String>) p.getConcatenacion().clone());		
				}
				
				if(!esta(np,nprod)) nprod.add(np);
			}
			g1.getProducciones().remove(v);
			g1.getProducciones().put(v, nprod);
		}

		html+="<br><h2>Gramatica</h2>" + g1.toHTML() + "<br>";
		System.out.println("g1:\n" + g1);
		System.out.println("****************************FASE 2****************************");
		html+="<center>**************FASE 2**************</center>";
		
		boolean acabado = false;
		Chomsky g2 = null;
		while(!acabado){
			acabado = true;
			gramaticaSalida = new Chomsky(g1.getVariables(),g1.getSimbolos(),g1.getProducciones(),
					g1.getVariableInicial());
			 g2 = gramaticaSalida;
				System.out.println("g2:\n" + g2);
			 relacionados = new HashMap<String,String>();
			 
			Iterator<String> its = g1.getVariables().iterator();
			while(its.hasNext()){
				ArrayList<Produccion> nlp = new ArrayList<Produccion>();
				String s = its.next();
				Iterator<Produccion> itp = g1.getProducciones().get(s).iterator();
				while(itp.hasNext()){
					Produccion p = itp.next();
					Produccion np = null;
					if(p.getConcatenacion().size() >= 3){
						
						np = creaProducciones(g2,p);
						if(!acabado) acabado = true;
					}
					else{
						np = p;
					}
					if(!esta(np,nlp)) nlp.add(np);
				}
				g2.getProducciones().remove(s);
				g2.getProducciones().put(s, nlp);
			}
			
			g1 = new Chomsky(g2.getVariables(),g2.getSimbolos(),g2.getProducciones(),
					g2.getVariableInicial());
			html+="<br><h2>Gramatica</h2>" + g2.toHTML() + "<br>";
		}
		
		
		gramaticaSalida = new Chomsky(g2.getVariables(),g2.getSimbolos(),g2.getProducciones(),
				g2.getVariableInicial());
		System.out.println("ANTES DE SIMPLIFICAR GSALIDA ES: " + gramaticaSalida);
		//quitamos las lambdas que no esten en S
		boolean bol = gramaticaSalida.dimeSiHayProdMulti();
		int i = 0;
		this.limpia();
		while(bol){
			gramaticaSalida.quitaProdMulti();
			bol = gramaticaSalida.dimeSiHayProdMulti();
			i++;
			System.out.println("VUELTAS kita lambda: " + i);
			System.out.println("GRAMATICA SALIDA EN VUELTAAS: " + gramaticaSalida);
			this.limpia();
		}
		System.out.println("DESPUES DE SIMPLIFICAR GSALIDA ES: " + gramaticaSalida);
		html+="<br><h2>Gramatica final simplificada</h2>" + gramaticaSalida.toHTML();
		
		

		calculaRelacionadosCYK();
		gramaticaSalida.setRelacionados(relacionadosCYK);
		System.out.println("RELACIONADOS: " + gramaticaSalida.getRelacionados());
		
		if (mostrarPasos){
			
			String rutaxmlconaut="XML/pruebas/ficheroC.xml";
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
	
	private String dameCadena(ArrayList<String> c){
		
		String s = "";
		Iterator<String> itc = c.iterator();
		while(itc.hasNext()){
			s+=itc.next();
		}
		return s;
	}
	
	
	public void calculaRelacionadosCYK(){
		
		relacionadosCYK = new HashMap<String,ArrayList<String>>();
		Iterator<String> variables = this.gramaticaSalida.getVariables().iterator();
		while(variables.hasNext()){
			
			String var = new String(variables.next());
			Iterator<Produccion> itp = this.gramaticaSalida.getProducciones().get(var).iterator();
			while(itp.hasNext()){
				ArrayList<String> concat = itp.next().getConcatenacion();
				if(concat.size() == 1 && concat.get(0).equals(lambda)){}
				else{
					String ss = new String(dameCadena(concat)); //XXX
					if (!relacionadosCYK.containsKey(ss)){
						ArrayList<String> nv = new ArrayList<String>();
						nv.add(var);
						relacionadosCYK.put(ss, nv);						
					}
					else{
						ArrayList<String> nv = relacionadosCYK.get(ss);
						nv.add(var);
						relacionadosCYK.remove(ss);
						relacionadosCYK.put(ss, nv);						
					}
					
				}
				
			}
			
		}
	}

	
	@SuppressWarnings("unchecked")
	private void limpia(){

		System.out.println("LIMPIA AL PRINCIPIO GRAMATICASALIDA ES: " + gramaticaSalida);
		/*Greibach*/Chomsky gramsal = this.gramaticaSalida;
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
			System.out.println("var: " + s +"\ny sus producciones: " + aprod);
			int j = 0; int tamProd = aprod.size();
			while(j < tamProd){
				Produccion pr = aprod.get(j);
				ArrayList<String> concat = pr.getConcatenacion();
				Produccion npr = new Produccion();
				ArrayList<String> nconcat = arreglaConcatenacion(concat);
				if(!nconcat.isEmpty())npr.setConcatenacion(nconcat);
				
				j++;
				if(!nconcat.isEmpty() && !esta(npr,naprod)){
					System.out.println("LIMPIANDO\n");
					System.out.println("nconcat?:\n" + nconcat);
					if (npr.getConcatenacion().size()==1){
						System.out.println("Long1!!\n");
						String cc= npr.getConcatenacion().get(0);
						if(gramsal.getVariables().contains(cc)){
							System.out.println("GOTCHA!!\n");
							ArrayList<Produccion> recambio = gramsal.getProducciones().get(cc);
							System.out.println("RECAMBIO!!: " + recambio);
							for(int k = 0; k < recambio.size();k++){
								Produccion nproduccion = new Produccion();
								nproduccion.setConcatenacion(arreglaConcatenacion(
										(ArrayList<String>) recambio.get(k).getConcatenacion().clone()));
								if(!esta(nproduccion,naprod)){
									naprod.add(nproduccion);
								}
																
							}
							System.out.println("naprod RECAMBIO!!: " + naprod);
						}
						else{naprod.add(npr);}
					}
					else{
						naprod.add(npr);
					}
				}
			}
			ngramsalprod.put(s, naprod);
			i++;
		}
		this.gramaticaSalida.setProducciones(ngramsalprod);
//		System.out.println("GRAMATICASALIDA LIMPITA ES: " + gramaticaSalida);
		
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
			//System.out.println("ss?" + ss);
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
	
	private Produccion creaProducciones(Chomsky g2,Produccion p){
		Produccion np = new Produccion();
		ArrayList<String> nconcat = new ArrayList<String>();
		nconcat.add(new String(p.getConcatenacion().get(0)));

		String nvar = null;
		System.out.println("p: " + p);
		ArrayList<String> ss = copiaSubconcat(arreglaConcatenacion(p.getConcatenacion()));
		String s = ss.toString();
		System.out.println("relacionados: " + relacionados);
		System.out.println("s: " + s);
		if(relacionados.containsKey(s)){
			System.out.println("AHA");
			nvar = relacionados.get(s);
			
		}
		else{
			System.out.println("nuuuuuuuuuuuuu");
			nvar = "["+constantVar2+contador+"]";
			contador++;
			relacionados.put(s, nvar);
			g2.anadeVariable(nvar);
		}		
		ArrayList<Produccion> nnp = convertir(ss);

		g2.getProducciones().put(nvar,nnp );
		System.out.println("g2.getProducciones().put(nvar,nnp ) " + g2.getProducciones());
		nconcat.add(nvar);
		np.setConcatenacion(nconcat);
		System.out.println("np???: " + np);
		return np;
	}
	
	private ArrayList<Produccion> convertir(ArrayList<String> s){
		ArrayList<Produccion> lp = new ArrayList<Produccion>();
		Produccion p = new Produccion();
		p.setConcatenacion(/*copiaSubconcat(s)*/s);
		lp.add(p);
		return lp;
	}
	
	private ArrayList<String> copiaSubconcat(ArrayList<String> as){
		ArrayList<String> nas = new ArrayList<String>();
		int i = 1; int tam = as.size();
		while(i < tam){
			nas.add(as.get(i));
			i++;
		}
		return nas;
	}
	@SuppressWarnings("unchecked")
	private Produccion comprueba(Produccion p){
		
		Produccion np = new Produccion();
		ArrayList<String> nconcat = (ArrayList<String>) p.getConcatenacion().clone();
		np.setConcatenacion(nconcat);
		int i = 0; int tam = nconcat.size();
		while(i < tam){
			String s = nconcat.get(i);
			if(/*gramaticaEntrada.getSimbolos().contains(s)*/relacionados.containsKey(s)){
				nconcat.set(i,/*"["+constantVar+s+"]"*/relacionados.get(s));
			}
			i++;			
		}
//		System.out.println("funcionas jodio?" + np);
		return np;
		
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

		
			GIC_to_FNChomsky piticli = new GIC_to_FNChomsky(a.getGic(),true); 
		//piticli.simplifica(true,false);
		
		
		System.out.println("ENTRADA:\n" + piticli.getGramaticaEntrada());
		System.out.println("SALIDA:\n" + piticli.getGramaticaSalida());

//	piticli.getGramaticaSalida().creaListaPalabras();
		
		
		// TODO Auto-generated method subs

	}
}
