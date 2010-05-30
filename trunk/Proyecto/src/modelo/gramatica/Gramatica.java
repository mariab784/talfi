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
	private ArrayList<String> prodConTerminales;
	
	public Gramatica(){
		this.producciones = new HashMap<String,ArrayList<Produccion>>();
		this.variables = new ArrayList<String>();
		mensajero=Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		prodConLambdaUnit = new HashMap<String,Integer> ();
		prodConLambdaMulti = new HashMap<String,Integer> ();
		prodConTerminales = new ArrayList<String>();
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
	
	public void anadeProdConTerminales(String v){prodConTerminales.add(v);}
	public ArrayList<String> getProdConTerminales(){return prodConTerminales;}
	@SuppressWarnings("unchecked")
	public void setProdConTerminales(ArrayList<String> p){prodConTerminales = (ArrayList<String>) p.clone();}
	
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
		//	System.out.println("prodConLambdaMulti!" + prodConLambdaMulti);
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
		System.out.println("KEREMOS VER SI ESTA: " + pnueva);
		System.out.println("EN: " + lprod);
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
		
		ArrayList<Produccion> lp = this.getProducciones().get(v);

		int i = 0;
		int tam = lp.size();
	//	System.out.println("v cuanto vale?" + v);
	//	System.out.println("tam cuanto vale?" + tam);
		ArrayList<Produccion> p = null;
		//si solo tiene lambda,o a si misma, no la ponemos				
		if(tam == 1){
			ArrayList<String> s = lp.get(0).getConcatenacion();
			if(s.size() == 1){
				String ss = s.get(0);
				if(ss.equals(v)){						
					return null;
				}
				
				if(this.getProdConLambdaUnit().containsKey(v)) return null;
				
				p = new ArrayList<Produccion>();
				Produccion pp = /*new Produccion();*/nuevaProduccion(lp.get(0),v);
				//pp.anadeCadena(new String(ss));
				return p;
			}
			else{
				//para las concatenaciones de las producciones de long > 1
				Produccion pp = lp.get(i);
				Produccion np = nuevaProduccion(pp,v);
			//	System.out.println("pp ke eres? " + pp);
			//	System.out.println("np ke eres? " + np);
				if (( np != null) && (!np.getConcatenacion().isEmpty())){
					p = new ArrayList<Produccion>();
					p.add(np);
				
				return p;
				}
			}
			
		} //llave size =1
		p = new ArrayList<Produccion>();
		while(i < tam){

			
			Produccion pp = lp.get(i);
			Produccion np = nuevaProduccion(pp,v);
	//		System.out.println("np ke eres? " + np);
			if (( np != null) && (!np.getConcatenacion().isEmpty()))
				p.add(np);
	//			System.out.println("p ke eres? " + p);
			i++;	
		}
		
	//	System.out.println("p ke devuelves? " + p);
		return p;
		
	}
	
	private Produccion nuevaProduccion(Produccion p,String v){
		//solo sera null si tiene tamaño 1 y coincide con v
	//	System.out.println("***NUEVA PRODUCCION***");
		ArrayList<String> s = p.getConcatenacion();
	//	System.out.println("s ke es?" + s);
		Produccion pp = null;
		
		if(s.size() == 1){
	//		System.out.println("***AKI NO ENTRAS, VERDAD?***");
			String ss = s.get(0);
	//		System.out.println("ss ?" + ss);
	//		System.out.println("v ?" + v);
			if(ss.equals(v)) return null;
				
			pp = new Produccion();
			pp.anadeCadena(new String(ss));
	//		System.out.println("***pp ke eres?***" + pp);
			return pp;
		}
		
		//para longitudes >1
		pp = new Produccion();
//		Iterator<String> itS = s.iterator();
		//recorremos el arraylist para ver ke tiene
		int i = 0; int tam = s.size();
		while(i < tam){
			String as = s.get(i);
	//		System.out.println("as ke eres?" + as); 
			boolean b = prodConLambdaUnit.containsKey(as); //variable no tiene lambda
			boolean c = (s.size() != 1) && !as.equals(lambda);
			
			if (!as.equals(lambda)){
				if(!b){//*!as.equals(lambda)*/){
	//				System.out.println("SIII!!"); 
	//				System.out.println("ke coño SOY? " + as);
					if(!this.getVariables().contains(as)){
						if (this.getSimbolos().contains(as)){pp.anadeCadena(as);}
						else{ System.out.println("NO ESTA!!"); }
					}
					else {
						pp.anadeCadena(as);
		//			System.out.println("nuevo pp!!" + as +"añadida!"); 
		//			System.out.println("ke es pp!!" + pp); 
					}
				}
				else{
					//System.out.println("NO!!");
				}
			}	
			i++;
		}
	//	System.out.println("pp ke devuelves? " + pp);
	//	System.out.println("***FIN NUEVA PRODUCCION***");
		return pp;
	}
	
	private Produccion nuevaProduccionMulti(Produccion p,String v,ArrayList<Produccion> lps){//NO SE USA ESTE METODO
		//solo sera null si tiene tamaño 1 y coincide con v
		//buscamos encontrar una var ke pertenezca a multi, y crear 
	//	System.out.println("***NUEVA PRODUCCION***");
		ArrayList<String> s = p.getConcatenacion(); 
	//	System.out.println("s ke es?" + s);
		Produccion pp = null;
		
		if(s.size() == 1){
	//		System.out.println("***AKI NO ENTRAS, VERDAD?***");
			String ss = s.get(0);
	//		System.out.println("ss ?" + ss);
	//		System.out.println("v ?" + v);
			if(ss.equals(v)) return null;
				
			pp = new Produccion();
			pp.anadeCadena(new String(ss));
	//		System.out.println("***pp ke eres?***" + pp);
			return pp;
		}
		
		//para longitudes >1 XXX
		pp = new Produccion();
//		Iterator<String> itS = s.iterator();
		//recorremos el arraylist para ver ke tiene
		int i = 0; int tam = s.size();
		while(i < tam){
//Produccion p,String v,ArrayList<Produccion> lps
			
			String as = s.get(i);
	//		System.out.println("as ke eres?" + as); REVISAR
			boolean b = prodConLambdaMulti.containsKey(as); //variable no tiene lambda
//			boolean c = (s.size() != 1) && !as.equals(lambda);
			
	//		f
			
	//		i++;
		}
	//	System.out.println("pp ke devuelves? " + pp);
	//	System.out.println("***FIN NUEVA PRODUCCION***");
		return pp;
	}
	
	public Produccion mezcla(ArrayList<String> nconcatParaP, ArrayList<String>concatDeProdMulti, 
			ArrayList<String> concatPp,int i){
		System.out.println("***ENTRAMOS EN MEZCLA***");
		System.out.println("***1 nconcatParaP***" + nconcatParaP);
		System.out.println("***2 concatDeProdMulti***" + concatDeProdMulti);
		System.out.println("***3 concatPp***" + concatPp);
		System.out.println("***I SIG**" + i);
		//SI HAY ALGO EN NCONCATPARAP OCONCATPP NO PONER LAMBDAS!
		
		
		Produccion p = new Produccion();
		if (nconcatParaP != null){
			for(int j = 0; j < nconcatParaP.size(); j++){
				String ns = new String(nconcatParaP.get(j));
				if (!ns.equals(lambda)){
					if(!this.getVariables().contains(ns)){
						
						System.out.println("INSERVIBLE"); return null;
					} 
					p.anadeCadena(ns);
				
				
				}			
			}
		}
		System.out.println("***p despues de concatenar nconcatParaP**" + p);
		
//		int tamc = -1;
		
	//	if (concatPp != null )tamc = concatPp.size();
		
		System.out.println("kien eres concatDeProdMulti?" + concatDeProdMulti);
		for(int j = 0; j < concatDeProdMulti.size(); j++){
			System.out.println("ENTRA!");
			//System.out.println("tamc! " + tamc); System.out.println("i!" + i);
		//	if (tamc !=i){ //y esto xk es?
			//	System.out.println("aki tb ENTRA!");
				String ss = new String(concatDeProdMulti.get(j));
				//if(!ss.equals(lambda))
				if(!this.getVariables().contains(ss)){
					
					System.out.println("INSERVIBLE"); return null;
				} 
				p.anadeCadena(ss);
		//	}
		}
		
		System.out.println("***p despues de concatenar concatDeProdMulti**" + p);
		
		if(concatPp != null){
	//		if (tamc !=i){
				for(int j = i; j < concatPp.size(); j++){
					String ns = new String(concatPp.get(j));//new String(nconcatParaP.get(j));
					if (!ns.equals(lambda)){
						if(!this.getVariables().contains(ns)){
							
							System.out.println("INSERVIBLE"); return null;
						} 
						p.anadeCadena(new String(concatPp.get(j)));
					}//p.anadeCadena(ns);
					
					/*if (tamc !=i)*///p.anadeCadena(new String(concatPp.get(j)));			
				}
			
			//}
		}
		System.out.println("ke me devuelves mezcla?" + p);
		return p;
		
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Produccion> compruebaMulti(ArrayList<Produccion> lps,String v){
		//var actual, lps arraylist<produccion>de esa var
		//objetivo: coger produccion. Si unitaria -> Si lambda, dejarla. Si = var, no añadir
		//
		System.out.println("ke es lps?" + lps);

		int tam = lps.size();
		
		ArrayList<Produccion> p = null;
		//si solo tiene lambda,o a si misma, no la ponemos				
		if(tam == 1){
			//esta produccion no tiene xk tener longitud 1!
			System.out.println("hola soy " + v +"y solo tengo una produccion!");
			ArrayList<String> s = lps.get(0).getConcatenacion();
			
			System.out.println("hola soy  su produccion y soy " + s);
			System.out.println("Y MI TAMAÑO ES: " +s.size());
			if(s.size() == 1){
				String ss = s.get(0);
				if(ss.equals(v)){						
					return null;
				}
				//si contiene una variable ke tiene multiLambda
				else if(this.getProdConLambdaMulti().containsKey(ss)){
					p = new ArrayList<Produccion>();
					System.out.println("hola me llamo " + s + "soy multi !uhh");
					
					ArrayList<Produccion> lprodMulti = this.getProducciones().get(ss);
					System.out.println("me gustaria saber ke voy a intercalar: " +  lprodMulti);
					
					int tamLProdMulti = lprodMulti.size();
					int k = 0;
					while(k <tamLProdMulti){
						ArrayList<String> concatDeProdMulti = lprodMulti.get(k).getConcatenacion();
						System.out.println("quiero saber:!" + "var: " + v + "lps mia: " + lps);
						System.out.println("concatDeProdMulti" + concatDeProdMulti);
						//no hay nada antes ni despues ke concatenar.
						Produccion nnp = mezcla(null,concatDeProdMulti,null,-2);
						System.out.println("***MEZCLA NOS HA DEVUELTO: ***" + nnp);
						if (nnp!= null && !esta(nnp,p)) {p.add(nnp);}
		//				System.out.println("nnp ke es?mezcla(null,concatDeProdMulti,null,-2); " + nnp);
		//				System.out.println("p ke es? p.add(nnp);" + p);
						k++;
					}
					System.out.println("aviso! devolvere p: " + p);
					return p;
				}
				//si no es recursivo ni contiene una variable con lambdamulti
				else{
					System.out.println("soy " + ss +". me contiene lambda multi?");
					System.out.println("lambda multi: " + this.getProdConLambdaMulti());
					System.out.println("se supone ke no!");
				p = new ArrayList<Produccion>();
				Produccion pp = new Produccion();
				pp.anadeCadena(new String(ss));
				return p;
				}
			}
			else{
				//System.out.println("NO ME TRATAS DONDE DEBES! soy" +v);
				//puede ser ke contenga a si mismo y nunca lleguemos a ningun lado!
				////////////////////////////////////////////////////////////////
				//tam de la concat de la prod >1
				//recorremos la concatenacion, para buscar alguna variable de Multi
				//en s esta el la concat = arrayList<String>
				if (s.contains(v)){System.out.println("NO VOY A LLEGAR A NINGUN PUTO LADO");
				
				return null;
				}
				int j = 0;
				p = new ArrayList<Produccion>();
				Produccion npp = new Produccion();
				//recorremos la concatenacion de la produccion
				int tamConcatPp = s.size();
				ArrayList<String> nconcatParaP = new ArrayList<String>();
				while(j < tamConcatPp){
					
					String sss = /*concatPp*/s.get(j);
				//	ArrayList<String> nconcatParaP = new ArrayList<String>();
					
					if (!this.getProdConLambdaMulti().containsKey(s)){
						//no pertenece, dejarlo tal cual
						nconcatParaP.add(new String(sss));
						npp.setConcatenacion(nconcatParaP);
						if (j == tamConcatPp-1){
							System.out.println("nnnp ke es? " + npp);
							if (npp!= null && !esta(npp,p)) p.add(npp);
							//System.out.println("p ke es? " + p); //OJO
						}
					}
					else{ 
						//S ESTA EN LAMBDAMULTI
						// tenemos que: 1. lo ke habia hasta el momento en concar guardarlo, y crear producciones
						//a partir de el
						//cogemos las producciones asociadas a la variable contenida en Multi
						ArrayList<Produccion> lprodMulti = this.getProducciones().get(s);
						int tamLProdMulti = lprodMulti.size();
						int k = 0;
						while(k <tamLProdMulti){
							ArrayList<String> concatDeProdMulti = lprodMulti.get(k).getConcatenacion();
							System.out.println("npp.getConcatenacion()" + npp.getConcatenacion());
							System.out.println("concatDeProdMulti" + concatDeProdMulti);
							System.out.println("concatPp" + /*concatPp*/s); 
							Produccion nnnp = mezcla(/*nconcatParaP*/npp.getConcatenacion(),concatDeProdMulti,/*concatPp*/s,/*i*/j+1);
							System.out.println("nnnp ke es? mezcla con arralist<prod> > 1 y tam concat > 1" + nnnp);
							if (nnnp!= null && !esta(nnnp,p)) p.add(nnnp);
							System.out.println("p ke es? " + p);
							k++;
						}
						
					}
					
					j++;
					
				}//llave while de recorrer la concat
			}
			
		} //llave size =1
		else{
			System.out.println("TAM MAYOR KE UNO, TIENE MAS DE UNA PROD");
		}
		
		//para longitudes de arrayList produccion >1
		p = new ArrayList<Produccion>();

		//recorremos el arraylist para ver ke tiene
		int i = 0; 
		//recorremos el arrayList. tam >1
		while(i < tam){
			
			Produccion pp = lps.get(i);
			ArrayList<String> concatPp = pp.getConcatenacion();
			int tamConcatPp = concatPp.size();
			//Si la concatenacion de pp tiene longitud 1, importa si es recursiva
			//depende si tiene terminales o no.
			if(tamConcatPp == 1){
				String s = concatPp.get(0);
				System.out.println("s ke es? " + s);
				System.out.println("y v? " + v);
					if(!s.equals(v)){ // no estoy segura de ke tenga ke filtrar tanto
									//nada asegura ke si hay varias y una sea recursiva tenga ke kitarlo,no?
									//no se pone xk seria autorecursivo y añadiria lo ke ya hay
						//no recursivo
						if(this.getProdConLambdaMulti().containsKey(s)){
							System.out.println("hola me llamo " + s + "soy multi");
							ArrayList<Produccion> lprodMulti = this.getProducciones().get(s);
							int tamLProdMulti = lprodMulti.size();
							int k = 0;
							while(k <tamLProdMulti){
								ArrayList<String> concatDeProdMulti = lprodMulti.get(k).getConcatenacion();
								Produccion nnp = mezcla(null,concatDeProdMulti,null,-1);
								if (!esta(nnp,p)) p.add(nnp);
								System.out.println("nnp ke es?mezcla(null,concatDeProdMulti,null,-1); " + nnp);
								System.out.println("p ke es? p.add(nnp);" + p);
								k++;
							}
							
						} // si s no esta lambdamulti copiar y meter
						else{
							Produccion npp = new Produccion();
							npp.anadeCadena(new String(s));
							if (!esta(npp,p)) p.add(npp);
							System.out.println("p ke es? " + p);
						}
					}
					//s == v fuera
					//si es recursivo lo kitamos?? no deberiamos, no?

			}
			//la concatenacion de la prod tiene long > 1
			else{
			////////////////////////////////////////////////////////////////
			//tam de la concat de la prod >1
			//recorremos la concatenacion, para buscar alguna variable de Multi
			int j = 0;
			
			Produccion npp = new Produccion();
			//recorremos la concatenacion de la produccion
			ArrayList<String> nconcatParaP = new ArrayList<String>();
			while(j < tamConcatPp){
				
				String s = concatPp.get(j);
			//	ArrayList<String> nconcatParaP = new ArrayList<String>();
				
				if (!this.getProdConLambdaMulti().containsKey(s)){
					//no pertenece, dejarlo tal cual
					nconcatParaP.add(new String(s));
					npp.setConcatenacion(nconcatParaP);
					if (j == tamConcatPp-1){
						System.out.println("nnnp ke es? " + npp);
						if (!esta(npp,p)) p.add(npp);
						//System.out.println("p ke es? " + p); //OJO
					}
				}
				else{ 
					//S ESTA EN LAMBDAMULTI
					// tenemos que: 1. lo ke habia hasta el momento en concar guardarlo, y crear producciones
					//a partir de el
					//cogemos las producciones asociadas a la variable contenida en Multi
					ArrayList<Produccion> lprodMulti = this.getProducciones().get(s);
					int tamLProdMulti = lprodMulti.size();
					int k = 0;
					while(k <tamLProdMulti){
						ArrayList<String> concatDeProdMulti = lprodMulti.get(k).getConcatenacion();
						System.out.println("npp.getConcatenacion()" + npp.getConcatenacion());
						System.out.println("concatDeProdMulti" + concatDeProdMulti);
						System.out.println("concatPp" + concatPp); 
						Produccion nnnp = mezcla(/*nconcatParaP*/npp.getConcatenacion(),concatDeProdMulti,concatPp,/*i*/j+1);
						System.out.println("nnnp ke es? mezcla con arralist<prod> > 1 y tam concat > 1" + nnnp);
						if (nnnp!= null &&!esta(nnnp,p)) p.add(nnnp);
						System.out.println("p ke es? " + p);
						k++;
					}
					
				}
				
				j++;
				
			}//llave while de recorrer la concat
		}//llave else de longconcat > 1
		i++;	

	}
		System.out.println("p ke devuelves? " + p);
		System.out.println("***FIN NUEVA PRODUCCION***");
		return p;
		
		
		
		/*		Iterator<Produccion> itP = lps.iterator();
		while(itP.hasNext()){
			Produccion prod = itP.next();
	//		System.out.println("ke es prod?" + prod);
			Produccion nProd = new Produccion();
			ArrayList<String> concat = (ArrayList<String>) prod.getConcatenacion().clone();
			int tamConcat = concat.size();
			int i = 0;
			while(i < tamConcat){
				String s = concat.get(i);
		//		System.out.println("ke es s?" + s);
		//		System.out.println("i!" + i);
				if(this.getProdConLambdaMulti().containsKey(s)){
					
			//		System.out.println("s es MULTI!!");
					int limite = this.getProdConLambdaMulti().get(s);
					
			//		System.out.println("ke es limite?" + limite);
					ArrayList<Produccion> pMulti = this.getProducciones().get(s);
					for(int j = 0; j < limite; j++){
						Produccion nMp = new Produccion();
						ArrayList<String> concatename = (ArrayList<String>) nProd.getConcatenacion().clone();
			//			System.out.println("ke es concatename?" + concatename);
						nMp.setConcatenacion(concatename);
						Produccion prodDeMulti = pMulti.get(j);
						ArrayList<String> concatDeMulti = prodDeMulti.getConcatenacion();
		//				System.out.println("ke es concatDeMulti?" + concatDeMulti);
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
		//				System.out.println("ke es nMp?" + nMp);
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
		System.out.println("tamano p?" + p.size());*/
		//return p;
	}
	
	public boolean dimeSiHayProdUnitarias(){
		
		prodConLambdaUnit = new HashMap<String,Integer>();
		int tamProducciones = variables.size();
		int i = 0;
		while (i < tamProducciones){
			String v = variables.get(i);
			int tamListaProducciones = producciones.get(v).size();
			if (tamListaProducciones == 1){
				if(producciones.get(v).get(0).getConcatenacion().size() == 1){
					if(producciones.get(v).get(0).getConcatenacion().get(0).equals(lambda)){
					
						if (!v.equals(this.getVariableInicial()))
							prodConLambdaUnit.put(new String(v), 0);	
					}
				}
			}
			
			i++;
		}
//		System.out.println("dame prodLambdaUnit DIMESIHAYPROD: " + prodConLambdaUnit);
//		System.out.println("dame prodLambdaUnit DIMESIHAYPROD ke devuelves?: " + (!prodConLambdaUnit.isEmpty()));
		return (!prodConLambdaUnit.isEmpty());
	}

	public boolean dimeSiHayProdMulti(){
		
		prodConLambdaMulti = new HashMap<String,Integer>();
		int tamProducciones = variables.size();
		int i = 0;
		while (i < tamProducciones){
			String v = variables.get(i);
			int tamListaProducciones = producciones.get(v).size();
			if (tamListaProducciones > 1){
				int j = 0; boolean enc = false;
				while(!enc && (j < tamListaProducciones)){
					
					Produccion p = producciones.get(v).get(j);
					if(p.getConcatenacion().size() == 1){
						
						if(p.getConcatenacion().get(0).equals(lambda)){
						
							if (!v.equals(this.getVariableInicial())){
								prodConLambdaMulti.put(new String(v), j);
								enc = true;
							}
						}
					}
					j++;
				}

			}
			
			i++;
		}
		System.out.println("dame prodLambdaMulti DIMESIHAYPROD: " + prodConLambdaMulti);
		System.out.println("dame prodLambdaMulti DIMESIHAYPROD ke devuelves?: " + (!prodConLambdaMulti.isEmpty()));
		return (!prodConLambdaMulti.isEmpty());
	}
}
