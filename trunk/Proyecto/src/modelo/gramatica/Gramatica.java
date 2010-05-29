/**
 * 
 */
package modelo.gramatica;
import java.util.*;

import accesoBD.Mensajero;

import vista.vistaGrafica.Estado;

import modelo.automatas.Alfabeto;
import modelo.automatas.Alfabeto_Pila;

/**
 * @author Rocio Barrigüete, Mario Huete, Luis San Juan
 *
 */
public abstract class Gramatica {
	
	private ArrayList<String> variables;
	private ArrayList<String> simbolos;
	private HashMap<String,ArrayList<Produccion>> producciones;
	private String variableInicial; //meter en xml como con Z y lambda? de momento pongo S a pincho
	private Mensajero mensajero;
	private String lambda;
	private HashMap<String,Integer> prodConLambdaUnit;
	private HashMap<String,Integer> prodConLambdaMulti;
	
	public Gramatica(){
		this.producciones = new HashMap<String,ArrayList<Produccion>>();
		this.variables = new ArrayList<String>();
		mensajero=Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		prodConLambdaUnit = new HashMap<String,Integer> ();
		prodConLambdaMulti = new HashMap<String,Integer> ();
	}
	
	@SuppressWarnings("unchecked")
	public Gramatica(ArrayList<String> v, ArrayList<String> s, 
			HashMap<String,ArrayList<Produccion>> p,String vInicial){
		
		variables = (ArrayList<String>) v.clone();
		simbolos = (ArrayList<String>) s.clone();
		producciones = clonar(p);//(HashMap<String, ArrayList<Produccion>>) p.clone();
		variableInicial = new String(vInicial);
		mensajero=Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		prodConLambdaUnit = new HashMap<String,Integer> ();
		prodConLambdaMulti = new HashMap<String,Integer> ();
	}
	
	public Gramatica(Alfabeto alf, ArrayList<Estado> est,Alfabeto_Pila alfPila){
		
		variables = new ArrayList<String>();
		simbolos = alf.getListaLetras();
		variableInicial = "S";
		creaVariables(est, alfPila);
		mensajero=Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		prodConLambdaUnit = new HashMap<String,Integer> ();
		prodConLambdaMulti = new HashMap<String,Integer> ();
	}
	
	public void anadeVariable(String v){
		
		if (!this.variables.contains(v)) variables.add(v);
	}
	
	private HashMap<String, ArrayList<Produccion>> clonar(HashMap<String, ArrayList<Produccion>> p){
		
		HashMap<String, ArrayList<Produccion>> nuevo = new HashMap<String, ArrayList<Produccion>>();
		
		Set<String> claves = p.keySet();
		Iterator<String> itClaves = claves.iterator();
		while (itClaves.hasNext()){
			String c = itClaves.next();
			ArrayList<Produccion> listaProd = p.get(c);
			ArrayList<Produccion> nListaProd = new ArrayList<Produccion>();
			Iterator<Produccion> itListaProd = listaProd.iterator();
			Produccion nProd = null;
			while(itListaProd.hasNext()){
				Produccion prod = itListaProd.next();
				nProd = new Produccion();
				ArrayList<String> nConcat = new ArrayList<String>();
				Iterator<String> itConcat = prod.getConcatenacion().iterator();
				while(itConcat.hasNext()){
					String s = itConcat.next();
					nConcat.add(s);
				}
				nProd.setConcatenacion(nConcat);
				nListaProd.add(nProd);
			}
			
			nuevo.put(c, nListaProd);
		}
		
		return nuevo;
	}
	
	private void creaVariables(ArrayList<Estado> est,Alfabeto_Pila alfPila){
		
		variables.add(variableInicial);
		
		
		int numEstados = est.size(); int tamAlfPila = alfPila.getListaLetras().size();
		String e1; String sp;
		
		for (int i = 0; i < numEstados; i++){
			e1 = est.get(i).getEtiqueta();
			for (int j = 0; j < tamAlfPila; j++){
				sp = alfPila.getListaLetras().get(j);
				for (int k = 0; k < numEstados; k++){
					variables.add("["+e1+sp+est.get(k).getEtiqueta()+"]");
					
				}
		
			}
		}
	}
	
	/**
	 * @return the variables
	 */
	public ArrayList<String> getVariables() {
		return variables;
	}
	/**
	 * @param variables the variables to set
	 */
	public void setVariables(ArrayList<String> variables) {
		this.variables = variables;
	}
	/**
	 * @return the simbolos
	 */
	public ArrayList<String> getSimbolos() {
		return simbolos;
	}
	/**
	 * @param simbolos the simbolos to set
	 */
	public void setSimbolos(ArrayList<String> simbolos) {
		this.simbolos = simbolos;
	}
	/**
	 * @return the producciones
	 */
	public HashMap<String, ArrayList<Produccion>> getProducciones() {
		return producciones;
	}
	/**
	 * @param producciones the producciones to set
	 */
	public void setProducciones(HashMap<String, ArrayList<Produccion>> producciones) {
		this.producciones = producciones;
	}
	/**
	 * @return the variableInicial
	 */
	public String getVariableInicial() {
		return variableInicial;
	}
	/**
	 * @param variableInicial the variableInicial to set
	 */
	public void setVariableInicial(String variableInicial) {
		this.variableInicial = variableInicial;
	}
	
	/**
	 * 
	 * @param clave
	 * @param p
	 * Nuestro método para combinar dentro de la tabla
	 */
	public void anadeProduccion (String clave, Produccion p){
		if (producciones.containsKey(clave)){
			producciones.get(clave).add(p);
		}
		else {
			ArrayList<Produccion> nuevoP = new ArrayList<Produccion>();
			nuevoP.add(p);
			producciones.put(clave, nuevoP);
		}
	}
	
	
	public String toString(){
		
		String s ="";
		
		s+="Inicial: " + variableInicial + "\n";
		s+="Variables " + variables  + "\n";
		s+="Terminales: " + simbolos + "\n";
		s+="Producciones: " + producciones + "\n";
		
		return s;
		
		
	}
	
	public ArrayList<Produccion> creaProducciones(String var){
		
		ArrayList<Produccion> np = new ArrayList<Produccion>();
		ArrayList<Produccion> p = this.getProducciones().get(var);
		int i = 0;
		int tam = p.size();
	//	int cuantasProdHay = 0;
		boolean hayLambda=false;
		while(i < tam){
			
			Produccion prod = p.get(i);
			Produccion nProd = creaProduccion(prod);
			ArrayList<String> concatProd = prod.getConcatenacion();
			if(!concatProd.isEmpty() && !esta(nProd, np)){
				if (esLambda(concatProd)){ //solo lambda
					//	cuantasProdHay++;		
					hayLambda=true;
				}
				np.add(nProd);
			}
			
			i++;
		}	
		if((np.size() == 1) && esLambda(np.get(0).getConcatenacion())){ 
//			System.out.println("+++AÑADIMOS PROD CON LAMBDAS+++");
//			System.out.println("VAR KE AÑADIMOS: " + var);
//			System.out.println("np ke VAR KE tiene: " + np);
	//		System.out.println("UNIT!");
	//		System.out.println("VAR!" + var);
	//		System.out.println("TAM!" + np.size());
			prodConLambdaUnit.put(var,np.size()); }
		else if (hayLambda){
		//	System.out.println("MULTI!");
		//	System.out.println("VAR!" + var);
		//	System.out.println("TAM!" + np.size());
			prodConLambdaMulti.put(var,np.size()); 
			System.out.println("prodConLambdaMulti!" + prodConLambdaMulti);
		}
		return np;
	}
	
	public HashMap<String,Integer> getProdConLambdaMulti(){return prodConLambdaMulti;}
	public void setProdConLambdaMulti(HashMap<String,Integer> pl){prodConLambdaMulti=pl;}
	public HashMap<String,Integer> getProdConLambdaUnit(){return prodConLambdaUnit;}
	public void setProdConLambdaUnit(HashMap<String,Integer> pl){prodConLambdaUnit=pl;}
	
	private boolean esta(String c, ArrayList<String>lc){
		
		Iterator<String> itLC = lc.iterator();
		while(itLC.hasNext()){
			String p = itLC.next();
			if (c.equals(p)) return true;
		}
		return false;
	}
	
	private boolean esLambda(ArrayList<String> c){
		
		return (c.size() == 1 && c.get(0).equals(lambda))/*esta(lambda,c)*/;
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

	private Produccion creaProduccion(Produccion prod){
		
		Produccion p = new Produccion();
		ArrayList<String> concat = prod.getConcatenacion();
		int i = 0;
		int tam = concat.size();
		while(i < tam){
			String s = concat.get(i);
			if (this.getVariables().contains(s) || this.getSimbolos().contains(s) || s.equals(lambda))
				p.anadeCadena(s);
			
			i++;
		}
		
		return p;
	}
	
	public ArrayList<Produccion> actualizaProducciones(String v){
		
		ArrayList<Produccion> p = new ArrayList<Produccion>();
		ArrayList<Produccion> lp = this.getProducciones().get(v);

		int i = 0;
		int tam = lp.size();
	//	System.out.println("v ke eres? " + v);
	//	System.out.println("LP ke eres? " + lp);
		while(i < tam){

			Produccion pp = lp.get(i);
	//		System.out.println("pp ke eres? " + pp);
				
			Produccion np = nuevaProduccion(pp);
	//		System.out.println("np ke eres? " + np);
			if (!np.getConcatenacion().isEmpty())p.add(np);
			i++;	
		}
		
	//	System.out.println("p ke devuelves? " + p);
		return p;
		
	}
	
	private Produccion nuevaProduccion(Produccion p){
		
		
	//	System.out.println("***NUEVA PRODUCCION***");
		Produccion pp = new Produccion();
		ArrayList<String> s = p.getConcatenacion();
		if ((s.size() == 1)){
			
			pp.anadeCadena( s.get(0));
			return pp;
		}
		
		
		Iterator<String> itS = s.iterator();
		int i = 0;
		while(itS.hasNext()){
			String as = new String(itS.next());
		//	System.out.println("produccion p ke eres? " + p);
		//	System.out.println("as ke eres? " + as);
			/*if(this.getSimbolos().contains(as)){ 
				System.out.println("as CONTENIDO en var! ");
				System.out.println("SOY UN SIMBOLO Y SOY: " + as);
				pp.anadeCadena(as);
			}*/
		//	System.out.println(as + "estoy enPROD CON LAMBDA? ");
		//	System.out.println("PROD CON LAMBDA: " + prodConLambda);
			boolean b = prodConLambdaUnit.containsKey(as);
			boolean c = (s.size() != 1) && !as.equals(lambda);
			
			if (!as.equals(lambda)){
				if(!b){//*!as.equals(lambda)*/){
					//System.out.println("SIII!!"); 
					//System.out.println("ke coño SOY? " + as);
					pp.anadeCadena(as);
				}
				else{
				//	System.out.println("NO!!");
				}
			}	
			i++;
		}
	//	System.out.println("pp ke devuelves? " + pp);
	//	System.out.println("***FIN NUEVA PRODUCCION***");
		return pp;
	}
	
	public ArrayList<Produccion> compruebaMulti(ArrayList<Produccion> lps){
		
//		System.out.println("MULTI: " + this.getProdConLambdaMulti());
		System.out.println("ke es lps?" + lps);
		
		ArrayList<Produccion> p = new ArrayList<Produccion>();

		Iterator<Produccion> itP = lps.iterator();
		while(itP.hasNext()){
			Produccion prod = itP.next();
			System.out.println("ke es prod?" + prod);
			Produccion nProd = new Produccion();
			ArrayList<String> concat = (ArrayList<String>) prod.getConcatenacion().clone();
			int tamConcat = concat.size();
			int i = 0;
			while(i < tamConcat){
				String s = concat.get(i);
				System.out.println("ke es s?" + s);
		//		System.out.println("i!" + i);
				if(this.getProdConLambdaMulti().containsKey(s)){
					
					System.out.println("s es MULTI!!");
					int limite = this.getProdConLambdaMulti().get(s);
					
					System.out.println("ke es limite?" + limite);
					ArrayList<Produccion> pMulti = this.getProducciones().get(s);
					for(int j = 0; j < limite; j++){
						Produccion nMp = new Produccion();
						ArrayList<String> concatename = (ArrayList<String>) nProd.getConcatenacion().clone();
						System.out.println("ke es concatename?" + concatename);
						nMp./*anade*/setConcatenacion(concatename);
						Produccion prodDeMulti = pMulti.get(j);
						ArrayList<String> concatDeMulti = prodDeMulti.getConcatenacion();
						System.out.println("ke es concatDeMulti?" + concatDeMulti);
						int tamConcatdeMulti = concatDeMulti.size();
						for(int k = 0; k < tamConcatdeMulti; k++){
							String sdm = concatDeMulti.get(k);
							if(!sdm.equals(lambda))
								nMp.anadeCadena(sdm);
						}
						//miramos si hay mas simbolos despues
						for(int h = i+1; h <tamConcat;h++){
							String sig = concat.get(h);
							nMp.anadeCadena(sig);
						}
						System.out.println("ke es nMp?" + nMp);
						if (!nMp.getConcatenacion().isEmpty()){
							System.out.println("NO ESTA VACIA nMp!" + nMp);
							p.add(nMp);
						}
						//p.add(nMp);
						//p.add(concatena(,pMulti.get(j).getConcatenacion()));
					}
					
				}
				else {nProd.anadeCadena(s);}
				i++;
			}
			
			if (!nProd.getConcatenacion().isEmpty())p.add(nProd);
		}
		
		System.out.println("ha cambiado lps?" + lps);
		System.out.println("ke devuelve p?" + p);
		System.out.println("tamano p?" + p.size());
		return p;
	}

}
