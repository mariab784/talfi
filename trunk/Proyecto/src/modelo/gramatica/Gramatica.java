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
	private ArrayList<String> prodUnit;
	private ArrayList<String> prodConLambdaMulti;
	private ArrayList<String> prodConTerminales;
	private ArrayList<String> variablesSinProd;
	
	public Gramatica(){
		this.producciones = new HashMap<String,ArrayList<Produccion>>();
		this.variables = new ArrayList<String>();
		mensajero=Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		prodUnit = new ArrayList<String>();
		prodConLambdaMulti = new ArrayList<String>();
		prodConTerminales = new ArrayList<String>();
		variablesSinProd = new ArrayList<String> ();
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
		prodUnit = new ArrayList<String>();
		prodConLambdaMulti = new ArrayList<String>();
		variablesSinProd = new ArrayList<String> ();
	}
	
	public Gramatica(Alfabeto alf, ArrayList<Estado> est,Alfabeto_Pila alfPila){
		
		variables = new ArrayList<String>();
		simbolos = alf.getListaLetras();
		variableInicial = "S";
		creaVariables(est, alfPila);
		mensajero=Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		prodUnit = new ArrayList<String>();
		prodConLambdaMulti = new ArrayList<String>();
		variablesSinProd = new ArrayList<String> ();
	}
	
	public void anadeProdConTerminales(String v){prodConTerminales.add(v);}
	public ArrayList<String> getProdConTerminales(){return prodConTerminales;}
	@SuppressWarnings("unchecked")
	public void setProdConTerminales(ArrayList<String> p){prodConTerminales = (ArrayList<String>) p.clone();}
	public void setVariablesSinProd(ArrayList<String> p){variablesSinProd = (ArrayList<String>) p.clone();}
	public ArrayList<String> getVariablesSinProd(){return variablesSinProd;}
	
	
	public void anadeVariable(String v){
		
		if (!this.variables.contains(v)) variables.add(new String(v));
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
		
		/*variables.add*/this.anadeVariable(variableInicial);
		
		
		int numEstados = est.size(); int tamAlfPila = alfPila.getListaLetras().size();
		String e1; String sp;
		
		for (int i = 0; i < numEstados; i++){
			e1 = est.get(i).getEtiqueta();
			for (int j = 0; j < tamAlfPila; j++){
				sp = alfPila.getListaLetras().get(j);
				for (int k = 0; k < numEstados; k++){
					this.anadeVariable("["+e1+sp+est.get(k).getEtiqueta()+"]");
					
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
			if(!this.esta(p, producciones.get(clave)))producciones.get(clave).add(p);
		}
		else {
			ArrayList<Produccion> nuevoP = new ArrayList<Produccion>();
			nuevoP.add(p);
			producciones.put(clave, nuevoP);
			this.anadeVariable(clave);
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
	
/*	public ArrayList<Produccion> creaProducciones(String var){
		
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
			/*prodUnit.add(var);}// ./*put*//*add(var);/*,np.size()); }
		else if (hayLambda){
		//	System.out.println("MULTI!");
		//	System.out.println("VAR!" + var);
		//	System.out.println("TAM!" + np.size());
			/*prodConLambdaMulti./*put*//*add(var/*,np.size()*///); 
		//	System.out.println("prodConLambdaMulti!" + prodConLambdaMulti);
		/*}
		return np;
	}*/
	
	public ArrayList<String> getProdConLambdaMulti(){return prodConLambdaMulti;}
	public void setProdConLambdaMulti(ArrayList<String> pl){prodConLambdaMulti=pl;}
	public ArrayList<String> getProdUnit(){return prodUnit;}
	public void setProdUnit(ArrayList<String> pl){prodUnit=pl;}
	
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
	//	System.out.println("KEREMOS VER SI ESTA: " + pnueva);
	//	System.out.println("EN: " + lprod);
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
		System.out.println("v cuanto vale?" + v);
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
				
				if(this.getProdUnit().contains/*Key*/(v)) return null;
				
				p = new ArrayList<Produccion>();
				Produccion pp = /*new Produccion();*/nuevaProduccion(lp.get(0),v);
				//pp.anadeCadena(new String(ss));
				return p;
			}
			else{
				//para las concatenaciones de las producciones de long > 1
				Produccion pp = lp.get(i);
				Produccion np = nuevaProduccion(pp,v);
				System.out.println("v ke eres? " + v);
				System.out.println("pp ke eres? " + pp);
				System.out.println("np ke eres? " + np);
				if (( np != null) && (!np.getConcatenacion().isEmpty())){
					p = new ArrayList<Produccion>();
					p.add(np);
				
				
				}
				else{ return null;} //XXX //np no tiene nada!!!	
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
			if(ss.equals(v) || !this.getVariables().contains(v)) return null;
				
			pp = new Produccion();
			pp.anadeCadena(new String(ss));
	//		System.out.println("***pp ke eres?***" + pp);
			return pp;
		}
		
			//LA LONGITUD DE LA CONCAT ES MAYOR KE UNO PERO PUEDE SER RECURSIVA IGUALMENTE!!!
			
		if(s.contains(v)){ 
			//this.getVariables().remove(v);
			System.out.println("ESTO ES RECURSIVO!!"); return null;
			}
		
		//para longitudes >1
		pp = new Produccion();
//		Iterator<String> itS = s.iterator();
		//recorremos el arraylist para ver ke tiene
		int i = 0; int tam = s.size();
		while(i < tam){
			String as = s.get(i);
	//		System.out.println("as ke eres?" + as); 
			boolean b = prodUnit.contains/*Key*/(as); //variable no tiene lambda
	//		boolean c = (s.size() != 1) && !as.equals(lambda);
			
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
	
	private ArrayList<Integer> contieneVarConProdUnit(Produccion p){
		
		ArrayList<Integer> ai = null;
		//new ArrayList<Integer>();
		ArrayList<String> concat = p.getConcatenacion();
		int tamConcat = concat.size(); int i = 0;
		while(i < tamConcat){
			String s = concat.get(i);
			if(this.getProdUnit().contains(s)){
				if(ai == null) ai = new ArrayList<Integer>();
				ai.add(i);
			}
			i++;
		}
		
		return ai;
	}
	
	private Produccion creaNuevaProduccion(Produccion p,ArrayList <Integer> indices){
		
		ArrayList<String> as = new ArrayList<String>();
		ArrayList<String> c = p.getConcatenacion();
		for(int i = 0; i < c.size();i++){
			String s = c.get(i);
			if(indices.contains(i)){
				String ss = this.getProducciones().get(s).get(0).getConcatenacion().get(0);
				as.add(new String(ss));
			}
			else{
				//String s = c.get(i);
				as.add(new String(s));				
			}
		}
		
		Produccion np = new Produccion();
		np.setConcatenacion(this.arreglaConcatenacion(as));
		return np;
	}
	
	public void quitaProdUnitarias(){
		//reemplaza las producciones unitarias ke pueda haber, ya sean lambda o una variable o un terminal
		System.out.println("DIME PROD: " + this.getProducciones());
		//boolean b = false;
		ArrayList<String> vars = this.getVariables();
//		ArrayList<String> varsParaBorrar = new ArrayList<String>();
		int i = 0; int tam = vars.size();
		HashMap<String,ArrayList<Produccion>> np = new HashMap<String,ArrayList<Produccion>>();
		//recorremos las producciones
		while(i < tam){
			String v = new String(this.getVariables().get(i));
			System.out.println("v" + v);
			ArrayList<Produccion> ap = this.getProducciones().get(v);
			System.out.println("ap" + ap);
			ArrayList<Produccion> nap = new ArrayList<Produccion>();
			
			//ha petado ap porke sale null = v no tiene producciones
//			if (ap == null){varsParaBorrar.add(v);}
//			else{
			int tamAProd = ap.size();
			//recorremos el arrayList de producciones
			//si el arrayList es 1, es ke es unitario! XXX
			if((tamAProd == 1) && (ap.get(0).getConcatenacion().size() == 1) && !v.equals(this.getVariableInicial())){
				//unitaria
				System.out.println("UNITARIA PERO NO KITAR S!");
			}
			else{
				int j = 0;
				while(j < tamAProd){ //recorremos las producciones del arrayList
					Produccion p = ap.get(j); //Produccion nprod = null;
					Produccion nnp = null;
				//	System.out.println("p.getConcatenacion()" + p.getConcatenacion());
					
					if(p.getConcatenacion().size() != 1){
					//para no coger las ke ya son unitarias
					//si es null es ke ningun string esta en prodUnit
						ArrayList<Integer> indices = contieneVarConProdUnit(p);
						if (indices != null){
						//tenemos ke cambiar la produccion = cambiar la concatenacion
							nnp = creaNuevaProduccion(p,indices);
					//	b = true;
						}
						else{System.out.println("indices NULL"); nnp = p;}//no hacemos nada
					}//si la concatenacion tiene tamaño > 1 tb keremos ke este!
					//LA CONCATENACION VALE 1, PUEDE SER VARIABLE CONTENIDA EN PRODUNIT O NO
					else{
						//if(this.getProdUnit().contains(p.getConcatenacion().get(0))){
							ArrayList<Integer> indices = contieneVarConProdUnit(p);
							if (indices != null){
								nnp = creaNuevaProduccion(p,indices);
							}
							else{System.out.println("indices NULL"); nnp = p;}
						
					}
				
					if (nnp != null){
						System.out.println("nnp" + nnp);
						nap.add(nnp);
						System.out.println("nap" + nap);
					}
					j++;
				}
			}

			//this.getProducciones().remove(v);
			
			/*this.getProducciones()*/
			System.out.println("np.put(v, nap)" + v +"," + nap);
			if(!nap.isEmpty())np.put(v, nap);
			i++;
		
		}
		this.setProducciones(np);

/*		for(int j = 0; j < varsParaBorrar.size();j++){
			this.getVariables().remove(varsParaBorrar.get(j));
		}*/
	//	return b;
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
				else if(this.getProdConLambdaMulti().contains/*Key*/(ss)){
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
					
					if (!this.getProdConLambdaMulti().contains/*Key*/(s)){
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
				return p;
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
						if(this.getProdConLambdaMulti().contains/*Key*/(s)){
							System.out.println("hola me llamo " + s + "soy multi");
							ArrayList<Produccion> lprodMulti = this.getProducciones().get(s);
							int tamLProdMulti = lprodMulti.size();
							int k = 0;
							while(k <tamLProdMulti){
								ArrayList<String> concatDeProdMulti = lprodMulti.get(k).getConcatenacion();
								Produccion nnp = mezcla(null,concatDeProdMulti,null,-1);
								if ( !esta(nnp,p)) p.add(nnp); 
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
				
				if (!this.getProdConLambdaMulti().contains/*Key*/(s)){
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
		
		

	}
	
	public boolean dimeSiHayProdConLambdaMulti(){

		prodConLambdaMulti = new ArrayList<String>();
		int tamProducciones = variables.size();
	//	System.out.println("variables:" + variables);
		int i = 0;
		while (i < tamProducciones){
			String v = variables.get(i);
		//	System.out.println("V KE PETA:" + v);
			ArrayList<Produccion> prodParaV = producciones.get(v);
	//		System.out.println("prodParaVV KE PETA:" + prodParaV);
	//		System.out.println("prods en cada vuelta:" + producciones);
			int tamListaProducciones = prodParaV.size();
			if (tamListaProducciones > 1){
				for(int k = 0; k < tamListaProducciones; k++){
					if(producciones.get(v).get(k).getConcatenacion().size() == 1
						 && producciones.get(v).get(k).getConcatenacion().get(0).equals(lambda) ){
					//if(producciones.get(v).get(0).getConcatenacion().get(0).equals(lambda)){
					
						if (!v.equals(this.getVariableInicial()))
							//no keremos tocar nada de la variable inicial, 
							//porke no aparece en ningun lado a sust
							prodConLambdaMulti.add(new String(v));
					}
					//}
				}
			}
			
			i++;
		}
		return (!prodConLambdaMulti.isEmpty());
	}
	
	
	public boolean dimeSiHayProdUnitarias(){

		prodUnit = new ArrayList<String>();
		int tamProducciones = variables.size();
	//	System.out.println("variables:" + variables);
		int i = 0;
		while (i < tamProducciones){
			String v = variables.get(i);
		//	System.out.println("V KE PETA:" + v);
			ArrayList<Produccion> prodParaV = producciones.get(v);
	//		System.out.println("prodParaVV KE PETA:" + prodParaV);
	//		System.out.println("prods en cada vuelta:" + producciones);
//			if(prodParaVV == null)
			int tamListaProducciones = prodParaV.size();
			if (tamListaProducciones == 1){
				if(producciones.get(v).get(0).getConcatenacion().size() == 1){
					//if(producciones.get(v).get(0).getConcatenacion().get(0).equals(lambda)){
					
						if (!v.equals(this.getVariableInicial()))
							//no keremos tocar nada de la variable inicial, 
							//porke no aparece en ningun lado a sust
							prodUnit.add(new String(v));	
					//}
				}
			}
			
			i++;
		}
		return (!prodUnit.isEmpty());
	}

	public boolean dimeSiHayProdMulti(){
		
		prodConLambdaMulti = new ArrayList<String>();
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
								prodConLambdaMulti./*put*/add(new String(v))/*, j)*/;
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
	
	public boolean dimeSiHayVariablesQueNoTienenProd(){
		//metemos variables de mas en var como dice el algoritmo.
		//1 -> Recorrer las variables. Si alguna no tiene produccion -> añadir para borrar ? o borrar ya?
		//2 -> Recorrer las producciones en buskeda de variables sin producciones
		
		int i = 0; 
		ArrayList<String> vars = this.getVariables();
		//	System.out.println("variables: " + var);
		variablesSinProd = new ArrayList<String>();
		int tam = vars.size();
		//Set<String> setProducs = proDucs.keySet();
		Iterator<String> itVars = /*setP*/vars.iterator();
		while(/*i < tam*/itVars.hasNext()){
			String v = itVars.next(); //.get(i);
			//		System.out.println("v: " + v);
			ArrayList<Produccion> prod = this.getProducciones().get(v);
			//		System.out.println(" prod: " +  prod);
			if (prod == null){ //variable ke no tiene producciones
				if(!variablesSinProd.contains(v)) variablesSinProd.add(v);
				
			}
			else{
			int tamProd = prod.size(); int j = 0;
			while(j < tamProd){
				Produccion p = prod.get(j);
				ArrayList<String> concat = p.getConcatenacion();
				//		System.out.println(" concat: " +  concat);
				int tamConcat = concat.size(); int k = 0;
				while(k < tamConcat){
					String s = concat.get(k); //REVISAR
					boolean esTerminal = this.getSimbolos().contains(s);
					boolean esLambda = s.equals(lambda);
					
					//	System.out.println(" esTerminal: " +  esTerminal);
					//	System.out.println(" esLambda: " +  esLambda);
					
					if(!esTerminal && !esLambda){ //tiene ke ser variable
						ArrayList<Produccion> hayConcat = this.getProducciones().get(s);
						//System.out.println(" hayConcat: " +  hayConcat);
						if (hayConcat == null){ //no hay ArrayList<produccion> asociado a ella
							if(!variablesSinProd.contains(s))variablesSinProd.add(s);
							//	System.out.println(" añadida! variablesSinProd es " +  variablesSinProd);
						}
					}
					k++;
				}				
				j++;
			}
			}
			i++;
		}
		System.out.println("VARIABLES SIN PRODUCCIONES: " + variablesSinProd);
		System.out.println("vamos a comprobarlo " + producciones);
		return !variablesSinProd.isEmpty();
	}
	
	public void quitaVariablesQueNoExisten(){

	//	System.out.println("variablesSinProd!!!!!!!!!!!!!!!!!; " + variablesSinProd);
		ArrayList<String> vars = this.getVariables();
	//	System.out.println("vars; " + vars);
		int i = 0; int tam = vars.size();
		HashMap<String,ArrayList<Produccion>> np = new HashMap<String,ArrayList<Produccion>>();
		while(i < tam){
			String v = new String(this.getVariables().get(i));
	//		System.out.println("v; " + v);
			ArrayList<Produccion> ap = this.getProducciones().get(v);
	//		System.out.println("ap; " + ap);
			ArrayList<Produccion> nap = new ArrayList<Produccion>();

			if(ap == null){/*System.out.println("AP NULL CON " + v);*/}
			else{
			int tamAProd = ap.size(); int j = 0;
			while(j < tamAProd){
				Produccion p = ap.get(j); Produccion nprod = null;
			//	System.out.println("p; " + p);
				ArrayList<String> concat = p.getConcatenacion();
		//		System.out.println("concat; " + concat);
				int tamConcat = concat.size(); ArrayList<String> nconcat = null;
				int k = 0;
				
				while(k < tamConcat){
					String s = concat.get(k);
		//			System.out.println("s; " + s);
					if(variablesSinProd.contains(s)){ 
		//				System.out.println("variablesSinProd.contains(s)!!! NO VALE LA PRODUCCION" );
						k = tamConcat;
						nprod = null;
						nconcat = null;
						//hemos encontrado unavariable sin prod -> NO VALE LA PRODUCCION
						// no esta en variables ni es terminal si es lambda = Variable sin produccion
						//this.getVariables().remove(s);
						//varsParaBorrar.add(s);
				/*		if(nconcat == null){
							nconcat = new ArrayList<String>();
						}
						String ss = this.getProducciones().get(s).get(0).getConcatenacion().get(0);
						System.out.println("this.getProducciones().get(s) " + this.getProducciones().get(s) );
						System.out.println("ss " + ss );
						nconcat.add(new String(ss));
						System.out.println("nconcat " + nconcat );*/
					}
					else{
						if(nconcat == null){
							nconcat = new ArrayList<String>();
						}
						nconcat.add(new String(s));
				//		System.out.println("nconcat " + nconcat );
			//			System.out.println("s " + s );
					}
					k++;
				} //nconcat puede ser null! = no hay concatenacion!
				if(nconcat != null){
					if (nprod == null){
							nprod = new Produccion();
						
						nprod.setConcatenacion(arreglaConcatenacion(nconcat));
		//				System.out.println("nprod " + nprod );
					}
					
				}
				//nprod puede ser null = no hay prod = no hay concatenacion!
				if(nprod != null){
					if(!esta(nprod,nap)){
				//		System.out.println(nprod + " no esta en " + nap);
						nap.add(nprod);
				//		System.out.println("nap" + nap);
					}
					
				}
				else{ //no hacemosnada por si acaso, tendremoske volver a limpiar
					//if(!variablesSinProd.contains(s))  variablesSinProd.add(s);
				}
				j++;
			}
			//nap esta creado, si no se añade nada estara vacio
			if(!nap.isEmpty()){
				np.put(v, nap);
	//			System.out.println("np es?" + np);
			}
			} //llave else
			i++;
		
		}
//		System.out.println("variablesSinProd " + variablesSinProd);
//		System.out.println("this.getVariables() antes " + this.getVariables());
		this.setProducciones(np);
		for(int m = 0; m < this.variablesSinProd.size(); m++){
			this.getVariables().remove(variablesSinProd.get(m));
			
		}

//		System.out.println("variablesSinProd despues" + variablesSinProd);
//		System.out.println("this.getVariables() despues" + this.getVariables());
	//	return b;
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
			if(!ss.equals(lambda)){s.add(
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
}
