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
	
	private final String auxtraduccion;
	private final char comienzo;
	private final String ultimatraduccion;
	private AutomataPila automataOriginal;
	private AutomataPila automataEntrada;
	private GramaticaIC gic;
	private Mensajero mensajero;
	private String lambda;
	private String fondoPila;
	private String xml;
	private String xmllatex;

	private String html;
	private String lat;
	@SuppressWarnings("unused")
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
		auxtraduccion = mensajero.devuelveMensaje("simbolos.auxtraduccion",4);
		ultimatraduccion = mensajero.devuelveMensaje("simbolos.ultimatraduccion",4);
		fondoPila = ((AutomataPila) automataEntrada).getFondoPila();
		comienzo = mensajero.devuelveMensaje("simbolos.comienzo",4).charAt(0);
		xml="";
		xml+="<pasos>\n";
		controlador=Controlador_imp.getInstancia();
		lat="";
		lat+= mensajero.devuelveMensaje("formatos.cabecera",5);
			
		xmllatex="<steps>\n<cabecera>"+lat+"</cabecera>\n";

		AP_Gramatica();
	}
	
	
	public String getHTML(){return html;}
	
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
		return xml;
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
	private void AP_Gramatica(){
			
		gic = new GramaticaIC();
		gic.setSimbolos((ArrayList<String>)automataEntrada.getAlfabeto().getListaLetras().clone());
		
		gic.setVariableInicial("S");
		
		ArrayList<String> estados = (ArrayList<String>) this.automataEntrada.getEstados().clone();
		Iterator<String> it = estados.iterator();
		
		gic.anadeVariable("S");
		while (it.hasNext()){
						
			Produccion p = new Produccion();
			String ini = this.automataEntrada.getEstadoInicial();
			String cadena = "["+ini+fondoPila+it.next()+"]";
			p.anadeCadena(cadena);
			gic.anadeProduccion("S", p);
			gic.anadeVariable(cadena);
		}

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
		
		html="";
		html+="<br><h2>"+mensajero.devuelveMensaje("simplificacionGICs.resulap",2)+
		"</h2><center><table>" + gic.toHTML() + "</table></center><br>";
		xml+="<paso><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.resulap",2)+"</titulo>"
		+gic.toXML()+"</paso>\n";
		xmllatex+="<ocultar>\n<oculto><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.resulaplatex",2)+
		"</titulo>"+gic.toLat()+"</oculto>\n";
		
		traduceVariables();	
	}
	
	
	private void anadeProduccionConUnaLista(String origen,String destino,String cima, ArrayList<String> lista,
			ArrayList<String> simbolos, ArrayList<String>salidaPila){

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
			for(int i = 0; i < tamEstados; i++){
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
				for(int i = 0; i < tamEstados; i++){
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
	
	private void limpiaAConciencia(boolean simp){
		
		boolean b = this.getGic().dimeSiHayProdUnitarias();
		boolean c = this.getGic().dimeSiHayProdRecursivas();	
		boolean d;
		if(simp)d= this.getGic().dimeSiHayVariablesQueNoTienenProd();
		else{ d = this.getGic().calculaAlcanzables();

			if(d) this.getGic().setVariablesSinProd(this.getGic().dameNoAlcanzables());
		}
		boolean e = this.getGic().dimeSiHayProdConProdUnitarias();
		
		boolean a = b || c || d || e;
		if(simp && a){ 
			html+="<center>"+mensajero.devuelveMensaje("simplificacionGICs.simp",2)+"</center>";
			xml+="<paso><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.simp",2)+"</titulo></paso>\n";
			xmllatex+="<oculto><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.simplatex",2)+
			"</titulo>"+gic.toLat() +"</oculto>\n";
		}
		
		boolean hecho = false;
		while(a){
			
			boolean escribe = d; 
			if(escribe && simp){
				html+="<br><h2>"+mensajero.devuelveMensaje("simplificacionGICs.quitvsimprod",2)+"</h2><h3>"+
			this.getGic().getVariablesSinProd().toString()+"</h3>";
				xml+="<paso><titulo>"+
				mensajero.devuelveMensaje("simplificacionGICs.quitvsimprod",2)+
				this.getGic().getVariablesSinProd().toString()+"</titulo>";
				xmllatex+="<oculto><titulo>"+
				mensajero.devuelveMensaje("simplificacionGICs.quitvsimprodlatex",2)+
				this.getGic().getVariablesSinProd().toString()+"</titulo>";
				
				/*System.out.println(mensajero.devuelveMensaje("simplificacionGICs.quitvsimprod",2)+
						this.getGic().getVariablesSinProd().toString());*/

			}
			if(escribe && !simp){

				html+="<br><h2>Quitamos variables no alcanzables: </h2><h3>" +
			this.getGic().dameNoAlcanzables().toString()+"</h3>";

				xml+="<paso><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.noalc",2) +
				this.getGic().dameNoAlcanzables().toString()+"</titulo>";
				
				xmllatex+="<oculto><titulo>"+
				mensajero.devuelveMensaje("simplificacionGICs.noalclatex",2)+
				this.getGic().dameNoAlcanzables().toString()+"</titulo>";
				/*System.out.println(mensajero.devuelveMensaje("simplificacionGICs.noalc",2) +
						this.getGic().dameNoAlcanzables().toString());*/
			}

			while(d){
				this.getGic().quitaVariablesQueNoExisten();
				d = this.getGic().dimeSiHayVariablesQueNoTienenProd();
				if(!hecho)hecho = true;
			}
			if(escribe){
				html+="<br><center><table>" + gic.toHTML() + "</table></center><br>";
				xml+= gic.toXML()+"</paso>\n";
				xmllatex+=gic.toLat()+"</oculto>\n";
				//System.out.println(gic);
				
			}

			if(!hecho){
				if(b){//unicaprod
				html+="<br><h2>"+ mensajero.devuelveMensaje("simplificacionGICs.unicaprod",2)+ "</h2><h3>"+
				this.getGic().getProdUnit().toString()+"</h3>";
				xml+="<paso><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.unicaprod",2)+
				this.getGic().getProdUnit().toString()+"</titulo>";
				xmllatex+="<oculto><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.unicaprodlatex",2)+
				this.getGic().getProdUnit().toString()+"</titulo>";
				/*System.out.println(mensajero.devuelveMensaje("simplificacionGICs.unicaprod",2)+
						this.getGic().getProdUnit().toString());*/
				
				this.getGic().quitaProdUnitarias();
				html+="<br><center><table>" + gic.toHTML() + "</table></center><br>";
				xml+=gic.toXML()+"</paso>\n";
				xmllatex+=gic.toLat()+"</oculto>\n";
				if(!hecho)hecho = true;
				}
				else if(!hecho && c) {//prodrec
				html+="<br><h2>"+mensajero.devuelveMensaje("simplificacionGICs.prodrec",2)+"</h2><h3>"+
				this.getGic().getProdRecursivas().toString()+"</h3>";
				xml+="<paso><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.prodrec",2)+
				"</titulo>";
				xmllatex+="<oculto><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.prodreclatex",2)+
				"</titulo>";
				
				this.getGic().quitaProdRecursivas();
				html+="<br><center><table>" + gic.toHTML() + "</table></center><br>";
				xml+=gic.toXML()+"</paso>\n";
				xmllatex+=gic.toLat()+"</oculto>\n";
				if(!hecho)hecho = true;
				}
				else if (!hecho && e){ 
				if(!this.getGic().getProdConProdUnit().isEmpty()){
				html+="<br><h2>"+mensajero.devuelveMensaje("simplificacionGICs.varconpunit",2)+"</h2><h3>"+
				this.getGic().getProdConProdUnit()+"</h3>";
				xml+="<paso><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.varconpunit",2)+
				"</titulo>";
				xmllatex+="<oculto><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.varconpunitlatex",2)+
				"</titulo>";
				this.getGic().quitaProdConProdUnitarias();
				html+="<br><center><table>" + gic.toHTML() + "</table></center><br>";
				xml+=gic.toXML()+"</paso>\n";
				xmllatex+=gic.toLat()+"</oculto>\n";
				if(!hecho)hecho = true;
				}
				}
			}

			//d = this.getGic().dimeSiHayVariablesQueNoTienenProd();
			if(simp)d= this.getGic().dimeSiHayVariablesQueNoTienenProd();
			else{ d = this.getGic().calculaAlcanzables();}
			if(!d){
				b = this.getGic().dimeSiHayProdUnitarias();
				c = this.getGic().dimeSiHayProdRecursivas();	

				e = this.getGic().dimeSiHayProdConProdUnitarias();
			}
				a = b || c || d || e;
				
				hecho = false;

		}
	}
	
	private void limpia(){

		limpiaAConciencia(true);
		limpiaAConciencia(false);

		
	}

	private void traduceVariables(){
			
		limpia();
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
		int ii = 1;
		boolean completo = false;
		String s = null;
		int jj = 0;
		while(ii < tam){
			
			if(!completo){
				nVar = new Character (ultima);
				ultima = (char)(nVar + 1);
				if (nVar != 'S'){
					s = nVar+"";
					nVariables.add(s);
					ii++;
					
				}
				if (s.equals(ultimatraduccion)){
					completo = true;
				}
			}
			else{
				s = "[" + auxtraduccion + jj + "]";
				nVariables.add(s);
				jj++;
				ii++;
			}
			
		}

		HashMap<String,ArrayList<Produccion>> nProduc = new HashMap<String,ArrayList<Produccion>>();

		variables = (ArrayList<String>) variables.clone();
		int tamano = nVariables.size();

		for(int i = 0; i < tamano; i++){
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
				if (nConcat != null)nProd.setConcatenacion(nConcat);
				if (!esta(nProd,nListaProd)&& ((nConcat != null))) nListaProd.add(nProd);
				j++;				
			}
				nProduc.put(nnVar, nListaProd); 			
		}
		
		this.gic.setProducciones(nProduc);
		this.gic.setVariables(nVariables);

		html+="<br><h2>"+mensajero.devuelveMensaje("simplificacionGICs.genttotalsimp", 2)+"</h2>";
		html+="<br><center><table>" + gic.toHTML() + "</table></center><br>";
/*		lat="\\MYp{\\Huge " + mensajero.devuelveMensaje("simplificacionGICs.gentradasimlatex", 2) +

		

		"\\newline\n"+ "\\newline\n"+"}\n"+
	       gic.toLat() + "\\newpage";*/
		xmllatex+="</ocultar>";
		xmllatex+="\n<step><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.gentradasimlatex", 2)
		+"</titulo>"+gic.toLat()+"</step>\n";
		
		xml+="<paso><titulo>"+mensajero.devuelveMensaje("simplificacionGICs.genttotalsimp", 2)+
		"</titulo>"+gic.toXML()+"</paso>\n</pasos>\n";
		gic.setXML(xml);
		
/*		xmllatex+="\n<item>"+"<titulo>"+mensajero.devuelveMensaje("simplificacionGICs.genttotalsimp", 2)+
		"</titulo>"+"<tabla>"+gic.toLat()+"</tabla>"+"</item>\n</ocultar>\n"*/;
		System.out.println("XML LATEX APTOGRAMAT:\n" + xmllatex);
		
		gic.setXMLLatex(xmllatex);//XXX
	}
	
	public String getLatex(){return lat;}
	
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
	
	private ArrayList<String> nuevaContatenacion(Produccion prod,ArrayList<String> variables,
			ArrayList<String> nVariables ){
		
		ArrayList<String> salida = new ArrayList<String>();
		ArrayList<String> concat = prod.getConcatenacion();
		int tam = concat.size(); int i = 0;
		String s;
		while(i<tam){
			s = new String(concat.get(i));
			int dt = dimeTipo(s,variables);
			if (dt == 0){
				salida.add(s);				
			}
			else if(dt == 1){
				int ind = variables.indexOf(s);
				String ns = null;
				ns = new String (nVariables.get(ind));
				salida.add(ns);
			}
			else{
				return null;			
			}
			i++;
		}
		return salida;
	}
	
	private int dimeTipo(String cadena,ArrayList<String> v){
		
		if (gic.getSimbolos().contains(cadena) || lambda.equals(cadena))
			return 0;
		if (v.contains(cadena))
			return 1;
		else
			return 2;		
	}
	

	/**
	 * @param args
	 */
	public GramaticaIC getGic(){
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
		
		arist = new AristaAP(0,0,0,0,"s0","s1");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		/******/	
	
		AutomataP_to_GramaticaIC a = new AutomataP_to_GramaticaIC(aut);
		a.AP_Gramatica();
	}
}