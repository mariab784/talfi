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
	private final String constantVar;
	public final String constantVar2;
	private int contador;
	private GramaticaIC gramaticaEntrada;
	private Chomsky gramaticaSalida;
	private String xml2;
	private String xmllatex;
	@SuppressWarnings("unused")
	private Controlador controlador;
	private Mensajero mensajero;
	private String lambda;
	private HashMap<String,String> relacionados;
	private HashMap<String,ArrayList<String>> relacionadosCYK;
	//************************************************************
	public GIC_to_FNChomsky(GramaticaIC g,boolean b){
		if (mensajero == null) mensajero=Mensajero.getInstancia();
		controlador=Controlador_imp.getInstancia();
		gramaticaEntrada = g; 
		xml2= "<exit>";
		xml2+=g.getXML();
		xml2+="<steps>\n";
		xmllatex=g.getXMLLatex();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		constantVar = mensajero.devuelveMensaje("simbolos.constantVar",4);
		constantVar2 = mensajero.devuelveMensaje("simbolos.constantVar2",4);
		relacionados = new HashMap<String,String>();
		contador = 0;
		transforma_FNChomsky(b);

	}
	
	public GramaticaIC getGramaticaEntrada(){return gramaticaEntrada;}
	public Chomsky getGramaticaSalida(){return gramaticaSalida;}

	public String getXML(){
		
		return xml2;
	}
	public String getXMLLatex(){
		
		return xmllatex;
	}
	public void registraControlador(Controlador controlador) {
		// TODO Auto-generated method stub
		this.controlador=controlador;
	}
	@SuppressWarnings("unchecked")
	public void transforma_FNChomsky(boolean mostrarPasos){
		
		Chomsky g1 = new Chomsky(gramaticaEntrada.getVariables(),gramaticaEntrada.getSimbolos(),
				gramaticaEntrada.getVariableInicial());
		

		xml2+="<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.fase1", 2)+"</titulo></step>\n";
		xmllatex+="<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.fase1lat",2)+"</titulo></step>\n";
		
		Iterator<String> itvar = gramaticaEntrada.getVariables().iterator();
		while(itvar.hasNext()){
			
			String v = itvar.next();
			Iterator<Produccion> itprod = gramaticaEntrada.getProducciones().get(v).iterator();
			ArrayList<Produccion> nprod = new ArrayList<Produccion>();
			Produccion np = null;
			while(itprod.hasNext()){
				Produccion p = itprod.next();
				if(p.getConcatenacion().size() > 1){
					np = comprueba(p,g1);
					
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
		xml2+="<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.gramatica",2)+"</titulo>"+
		g1.toXML()+"</step>\n";

		xml2+="<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.fase2", 2) +"</titulo>"+
		"</step>\n";

		xmllatex+="<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.gramaticalat",2)+
		"</titulo>"+g1.toLat()+"</step>\n";
		xmllatex+="<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.fase2lat",2)+"</titulo></step>";
		
		boolean acabado = false;
		Chomsky g2 = null;
		while(!acabado){
			acabado = true;
			gramaticaSalida = new Chomsky(g1.getVariables(),g1.getSimbolos(),g1.getProducciones(),
					g1.getVariableInicial());
			 g2 = gramaticaSalida;

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
			xml2+="<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.gramatica",2)+"</titulo>"+
			g2.toXML()+"</step>\n";
			xmllatex+="<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.gramaticalat",2)+
			"</titulo>"+g2.toLat()+"</step>\n";
		}
		
		
		gramaticaSalida = new Chomsky(g2.getVariables(),g2.getSimbolos(),g2.getProducciones(),
				g2.getVariableInicial());

		boolean bol = gramaticaSalida.dimeSiHayProdMulti();
		int i = 0;
		this.limpia();
		while(bol){
			gramaticaSalida.quitaProdMulti();
			bol = gramaticaSalida.dimeSiHayProdMulti();
			i++;
			this.limpia();
		}
		
		xml2+="<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.gsalidasimsimp", 2)+
		"</titulo>"+gramaticaSalida.toXML()+"</step>\n"+"</steps>\n";

		xmllatex+="<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.gsalidasimsimplatex", 2)+
		"</titulo>"+gramaticaSalida.toLat()+"</step>\n</steps>\n";
		
		gramaticaSalida.calculaAlcanzables();
		calculaRelacionadosCYK();
		gramaticaSalida.setRelacionados(relacionadosCYK);
			
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
				xml2+="\n<result>"+cinta+"</result>\n</exit>\n";
			}
			catch(FileNotFoundException e){
				JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("vista.nocinta", 2),mensajero.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
			}
	    	catch(Exception e){
	    		
	    		e.printStackTrace();
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

		Chomsky gramsal = this.gramaticaSalida;
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
				if(!nconcat.isEmpty() && !esta(npr,naprod)){
					if (npr.getConcatenacion().size()==1){
						String cc= npr.getConcatenacion().get(0);
						if(gramsal.getVariables().contains(cc)){

							ArrayList<Produccion> recambio = gramsal.getProducciones().get(cc);

							for(int k = 0; k < recambio.size();k++){
								Produccion nproduccion = new Produccion();
								nproduccion.setConcatenacion(arreglaConcatenacion(
										(ArrayList<String>) recambio.get(k).getConcatenacion().clone()));
								if(!esta(nproduccion,naprod)){
									naprod.add(nproduccion);
								}
																
							}
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
	
	private Produccion creaProducciones(Chomsky g2,Produccion p){
		Produccion np = new Produccion();
		ArrayList<String> nconcat = new ArrayList<String>();
		nconcat.add(new String(p.getConcatenacion().get(0)));

		String nvar = null;

		ArrayList<String> ss = copiaSubconcat(arreglaConcatenacion(p.getConcatenacion()));
		String s = ss.toString();

		if(relacionados.containsKey(s)){

			nvar = relacionados.get(s);
			
		}
		else{

			nvar = "["+constantVar2+contador+"]";
			contador++;
			relacionados.put(s, nvar);
			g2.anadeVariable(nvar);
		}		
		ArrayList<Produccion> nnp = convertir(ss);

		g2.getProducciones().put(nvar,nnp );

		nconcat.add(nvar);
		np.setConcatenacion(nconcat);

		return np;
	}
	
	private ArrayList<Produccion> convertir(ArrayList<String> s){
		ArrayList<Produccion> lp = new ArrayList<Produccion>();
		Produccion p = new Produccion();
		p.setConcatenacion(s);
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
	private Produccion comprueba(Produccion p, Chomsky g){
		
		Produccion np = new Produccion();
		ArrayList<String> nconcat = (ArrayList<String>) p.getConcatenacion().clone();
		np.setConcatenacion(nconcat);
		int i = 0; int tam = nconcat.size();
		while(i < tam){
			String s = nconcat.get(i);
			if(g.getSimbolos().contains(s)){
				String var = "["+constantVar+s+"]";
				nconcat.set(i,var);
				if(!gramaticaEntrada.getVariables().contains(var)){
					g.anadeVariable(var);
					Produccion pp = new Produccion();
					pp.anadeCadena(s);
					g.anadeProduccion(var, pp);
					
				}
			}
			i++;			
		}
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
		
		arist = new AristaAP(0,0,0,0,"s0","s1");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");		
		aut.anadeArista(arist);
		/******/	
	
		AutomataP_to_GramaticaIC a = new AutomataP_to_GramaticaIC(aut);
		GIC_to_FNChomsky piticli = new GIC_to_FNChomsky(a.getGic(),true); 
		System.out.println("ENTRADA:\n" + piticli.getGramaticaEntrada());
		System.out.println("SALIDA:\n" + piticli.getGramaticaSalida());
	}
}
