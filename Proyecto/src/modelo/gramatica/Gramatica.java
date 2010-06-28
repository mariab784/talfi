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
 * @author Rocio Barrig�ete, Mario Huete, Luis San Juan
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
	private ArrayList<String> prodMulti;
	private ArrayList<String> prodConTerminales;
	private ArrayList<String> prodRecursivas;
	private ArrayList<String> variablesSinProd;
	private ArrayList<String> prodConProdUnit;
	private ArrayList<String> prodParaAnadirEnProdUnit;
	private ArrayList<String> alcanzables;
	private ArrayList<String> noAlcanzables;
	
	
	public Gramatica(){
		this.producciones = new HashMap<String,ArrayList<Produccion>>();
		this.variables = new ArrayList<String>();
		mensajero=Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		prodUnit = new ArrayList<String>();
		prodMulti = new ArrayList<String>();
		prodConTerminales = new ArrayList<String>();
		variablesSinProd = new ArrayList<String> ();
		prodRecursivas = new ArrayList<String> ();
		prodConProdUnit= new ArrayList<String> ();
		prodParaAnadirEnProdUnit =  new ArrayList<String> ();
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
		prodMulti = new ArrayList<String>();
		variablesSinProd = new ArrayList<String> ();
		prodRecursivas = new ArrayList<String> ();
		prodConProdUnit= new ArrayList<String> ();
		prodParaAnadirEnProdUnit =  new ArrayList<String> ();
	}
	
	public Gramatica(Alfabeto alf, ArrayList<Estado> est,Alfabeto_Pila alfPila){
		
		variables = new ArrayList<String>();
		simbolos = alf.getListaLetras();
		variableInicial = "S";
		creaVariables(est, alfPila);
		mensajero=Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		prodUnit = new ArrayList<String>();
		prodMulti = new ArrayList<String>();
		variablesSinProd = new ArrayList<String> ();
		prodRecursivas = new ArrayList<String> ();
		prodConProdUnit= new ArrayList<String> ();
		prodParaAnadirEnProdUnit =  new ArrayList<String> ();
	}
	public ArrayList<String> getAlcanzables(){return alcanzables;}
	public void anadeProdConTerminales(String v){prodConTerminales.add(v);}
	public ArrayList<String> getProdConTerminales(){return prodConTerminales;}
	@SuppressWarnings("unchecked")
	public void setProdConTerminales(ArrayList<String> p){prodConTerminales = (ArrayList<String>) p.clone();}
	@SuppressWarnings("unchecked")
	public void setVariablesSinProd(ArrayList<String> p){variablesSinProd = (ArrayList<String>) p.clone();}
	public ArrayList<String> getVariablesSinProd(){return variablesSinProd;}
	
	public ArrayList<String> dameNoAlcanzables(){
		
		noAlcanzables = new ArrayList<String>();
		Iterator<String> it = variables.iterator();
		while(it.hasNext()){
			
			String s = it.next();
			if(!alcanzables.contains(s)){
				if(noAlcanzables.contains(s)) noAlcanzables.add(s);
			}
		}
		
		System.out.println("alcanzables: " + alcanzables);
		System.out.println("no alcanzables: " + noAlcanzables);
		System.out.println("variables: " + variables);
		return noAlcanzables;
	}
	
	@SuppressWarnings("unchecked")
	public boolean calculaAlcanzables(){
		
		alcanzables = new ArrayList<String>();
		alcanzables.add(variableInicial);
		
		Iterator<Produccion> itProd = producciones.get(variableInicial).iterator();
		while(itProd.hasNext()){
			Produccion p = itProd.next();
			//ArrayList<String> as = p.getConcatenacion();
			//if(p.getConcatenacion().size() != 1){
				for(int i = 0; i < p.getConcatenacion().size(); i++){
					String s = p.getConcatenacion().get(i);
					if(variables.contains(s)){
						if(!alcanzables.contains(s))alcanzables.add(s);
					}
				}
			//}
			
		}
		
		
		ArrayList<String> copia = (ArrayList<String>) alcanzables.clone();
		Iterator<String> it = copia.iterator();
		boolean acabado = false;
		while(!acabado && it.hasNext()){
			String v = it.next();
			if(!v.equals(variableInicial)){
				if(producciones.get(v) != null){
				itProd = producciones.get(v).iterator();
				
				while(itProd.hasNext()){
					Produccion p = itProd.next();
					if(p.getConcatenacion().size() != 1){
						for(int i = 0; i < p.getConcatenacion().size(); i++){
							String s = p.getConcatenacion().get(i);
							if(variables.contains(s)){
								if(!alcanzables.contains(s)){
									alcanzables.add(s);
								}
							}
						}
					}
					
				}
			}//llave if != null
			}
			if(!it.hasNext()){
			if(copia.size() == alcanzables.size()){ acabado = true; }
			else{copia = (ArrayList<String>) alcanzables.clone(); it = copia.iterator();}
			}
		}
		
		System.out.println("alcanzables: " + alcanzables);
		System.out.println("no alcanzables: " + noAlcanzables);
		System.out.println("variables: " + variables);
		
		return (alcanzables.size() != variables.size());
	}
	
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
	 * Nuestro m�todo para combinar dentro de la tabla
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
//			System.out.println("+++A�ADIMOS PROD CON LAMBDAS+++");
//			System.out.println("VAR KE A�ADIMOS: " + var);
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
	
	public ArrayList<String> getProdMulti(){return prodMulti;}
	public void setProdMulti(ArrayList<String> pl){prodMulti=pl;}
	public ArrayList<String> getProdUnit(){return prodUnit;}
	public void setProdUnit(ArrayList<String> pl){prodUnit=pl;}
	
/*	private boolean esta(String c, ArrayList<String>lc){
		
		Iterator<String> itLC = lc.iterator();
		while(itLC.hasNext()){
			String p = itLC.next();
			if (c.equals(p)) return true;
		}
		return false;
	}
	
/*	private boolean esLambda(ArrayList<String> c){
		
		return (c.size() == 1 && c.get(0).equals(lambda))/*esta(lambda,c)*//*;
	}*/
	
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
	
	public boolean compruebaSiEsta(Produccion pnueva, ArrayList<Produccion >lprod){
		return esta(pnueva,lprod);
	}

	
	public ArrayList<Produccion> actualizaProducciones(String v){
		
		ArrayList<Produccion> lp = this.getProducciones().get(v);

		int i = 0;
		int tam = lp.size();
		System.out.println("v cuanto vale?" + v);

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
				@SuppressWarnings("unused")
				Produccion pp = nuevaProduccion(lp.get(0),v);

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
		//solo sera null si tiene tama�o 1 y coincide con v
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
	//				System.out.println("ke co�o SOY? " + as);
					if(!this.getVariables().contains(as)){
						if (this.getSimbolos().contains(as)){pp.anadeCadena(as);}
						else{ System.out.println("NO ESTA!!"); }
					}
					else {
						pp.anadeCadena(as);
		//			System.out.println("nuevo pp!!" + as +"a�adida!"); 
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
				int tam = this.getProducciones().get(s).get(0).getConcatenacion().size();
				//String ss = this.getProducciones().get(s).get(0).getConcatenacion().get(0);
				for(int k = 0; k < tam; k++){
					String ss = this.getProducciones().get(s).get(0).getConcatenacion().get(k);
					as.add(new String(ss));
				}
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

		ArrayList<String> vars = this.getVariables();
		int i = 0; int tam = vars.size();
		HashMap<String,ArrayList<Produccion>> np = new HashMap<String,ArrayList<Produccion>>();
		while(i < tam){
			ArrayList<Produccion> nap = null;
			String v = new String(this.getVariables().get(i));
			if(!this.prodUnit.contains(v)){
				
				ArrayList<Produccion> ap = this.getProducciones().get(v);
				nap = new ArrayList<Produccion>();
				
				int tamAP = ap.size(); int j = 0;
				while(j < tamAP){
					Produccion p = ap.get(j);
					Produccion nnp = null;
					ArrayList<Integer> indices = this.contieneVarConProdUnit(p);
					if(indices != null){
						nnp = creaNuevaProduccion(p,indices);
					}
					else{nnp=p;}
					
					if(!this.esta(nnp, nap)) nap.add(nnp);
					
					j++;
				}
				
			}
			if(nap != null && !nap.isEmpty()){np.put(v, nap);} 
			i++;
			
		}
		this.setProducciones(np);
	}
	
	
	public void quitaProdMulti(){

		//Primero: Quitar el lambda de las prod que lo tienen.
		Iterator<String> itMulti = prodMulti.iterator();
		System.out.println("producciones antes de kitar lambdas: " + producciones);
		while(itMulti.hasNext()){
			String s = itMulti.next();
			ArrayList<Produccion> np = new ArrayList<Produccion>();
			Iterator<Produccion> itantp = producciones.get(s).iterator();
			while(itantp.hasNext()){
				Produccion prod = itantp.next();
				ArrayList<String> concat = prod.getConcatenacion();
				if(!(s.equals(this.getVariableInicial())) && (concat.size() == 1) 
						&& concat.get(0).equals(lambda)){
					System.out.println("veamos si la prod es lambda?: " + concat);
				}
				else{
					System.out.println("veamos si la prod NO es lambda?estara bien?: " + concat);
					np.add(prod);
				}
			}
			//borramos lo que habia, y las metemos sin el lambda
			producciones.remove(s);
			producciones.put(s, np);
		}
		//hasta aki en principio bien
		System.out.println("producciones despues de kitar lambdas: " + producciones);
		//vamos ahora a sustituir por lambdas todas las apariciones de la variable
		Iterator<String> itvariables = this.getVariables().iterator();
		while(itvariables.hasNext()){
			String v = itvariables.next();
			ArrayList<Produccion> lp = producciones.get(v);
			ArrayList<Produccion> nlp = null;
			Iterator<Produccion> itlp = lp.iterator();
			while(itlp.hasNext()){
				Produccion p = itlp.next();
				Produccion nprod = comprueba(p); //XXX
				//si es null es ke no la hemos cambiado
				if(nprod != null){
					//es ke hemos sustituido algun lambda
					System.out.println("ANTES!:" + lp);
					if (nlp == null){ nlp = new ArrayList<Produccion>();}
					if(!esta(nprod,nlp)){System.out.println("SIIII!SISISSI"); nlp.add(nprod);}
				//	System.out.println("despues!:" + lp);
				//	System.out.println("despues 2.0!:" + producciones);
				}
			}
			if(nlp != null){
				
				for(int j = 0; j < nlp.size(); j++){
					Produccion pp = nlp.get(j);
					if(!esta(pp,lp)){System.out.println("SIIII!SISISSI"); lp.add(pp);}
				}
				
			}
		}
		System.out.println("lo hemos hecho bien?: " + producciones);
	}
	
	@SuppressWarnings("unchecked")
	public void quitaProdConProdUnitarias(){

		Iterator<String> itpcp = this.prodConProdUnit.iterator();

		while(itpcp.hasNext()){
			String s = itpcp.next();
			ArrayList<Produccion> np = new ArrayList<Produccion>();
			Iterator<Produccion> itantp = producciones.get(s).iterator();
			while(itantp.hasNext()){
				Produccion prod = itantp.next();
				ArrayList<String> concat = prod.getConcatenacion();
				if(!(s.equals(this.getVariableInicial())) && (concat.size() == 1) 
						&& !(concat.get(0).equals(s)) && (this.getVariables().contains(concat.get(0))) ){
					System.out.println("veamos si la prod es var?: " + concat);
				}
				else{
					System.out.println("veamos si la prod NO es var?estara bien?: " + concat);
					np.add(prod);
				}
			}
			//borramos lo que habia, y las metemos sin el lambda
			producciones.remove(s);
			producciones.put(s, np);
		}
		
		
		Iterator<String> itvar = this.prodConProdUnit.iterator();
		Iterator<String> itanadir = this.prodParaAnadirEnProdUnit.iterator();
		while(itvar.hasNext()){
			String v = itvar.next();
			String av = itanadir.next();
			ArrayList<Produccion> lp = producciones.get(v);
			ArrayList<Produccion> nlp = producciones.get(av);
			Iterator<Produccion> itnlp = nlp.iterator();
			while(itnlp.hasNext()){
				Produccion nprod = new Produccion();
				nprod.setConcatenacion((ArrayList<String>) itnlp.next().getConcatenacion().clone()); //XXX
				if(!esta(nprod,lp)) lp.add(nprod);
				
			}

		}
		System.out.println("lo hemos hecho bien?: " + producciones);
	}
	
	
	@SuppressWarnings("unchecked")
	private Produccion comprueba(Produccion p){
		
		Produccion np = null;
		ArrayList<String> concat = (ArrayList<String>) p.getConcatenacion().clone();
		int i = 0; int tam = concat.size();
		boolean cambiado = false;
		while(i < tam){
			String s = concat.get(i);
			if(this.getProdMulti().contains(s)){
				concat.set(i, lambda);
				if(!cambiado)cambiado = true;
			}
			i++;
		}
		if(cambiado){
			np = new Produccion();
			np.setConcatenacion(concat);
			System.out.println("CAMBIADO NUEVA PRODUCCION: " + np);
		}
		return np;
	}
	public void quitaProdRecursivas(){

		Iterator<String> its = this.getVariables().iterator();
		HashMap<String,ArrayList<Produccion>> np = new HashMap<String,ArrayList<Produccion>>();
		while(its.hasNext()){
			
			String v = its.next();
			ArrayList<Produccion> lp = this.getProducciones().get(v);
			if(this.prodRecursivas.contains(v)){
				Iterator<Produccion> itlp = lp.iterator();
				Produccion centinela = new Produccion();
				ArrayList<Produccion> nlp = new ArrayList<Produccion>();
				centinela.anadeCadena(v);
				while(itlp.hasNext()){
					Produccion p = itlp.next();
					if(!this.iguales(centinela.getConcatenacion(),p.getConcatenacion() )){
						nlp.add(p);
					}
				}
				np.put(v, nlp);
			}
			else{np.put(v, lp);}
		}
		
		
	}

	public boolean dimeSiHayProdMulti(){

		this.prodMulti = new ArrayList<String>();
		int tamProducciones = variables.size();
		int i = 0;
		while (i < tamProducciones){
			String v = variables.get(i);
			ArrayList<Produccion> prodParaV = producciones.get(v);
			int tamListaProducciones = prodParaV.size();
			if (tamListaProducciones != 1){				
				int k = 0; boolean enc = false;
				while(k < tamListaProducciones && !enc){
					Produccion p = prodParaV.get(k);
					ArrayList<String> concat = p.getConcatenacion();				
					if(concat.size() == 1){					
						if(concat.get(0).equals(lambda)){
							if (!v.equals(this.getVariableInicial())){
								enc = true;
							}
						}						
						//else{enc = true;}
					}
					
					k++;
				}
				if(enc){
					prodMulti.add(new String(v));
				}
			}			
			i++;
		}
		System.out.println("KE TIENE PRODMULTI?" + prodMulti);
		return (!prodMulti.isEmpty());
	}

	
	public boolean dimeSiHayProdUnitarias(){

		prodUnit = new ArrayList<String>();
		int tamProducciones = variables.size();
		int i = 0;
		while (i < tamProducciones){
			String v = variables.get(i);
			ArrayList<Produccion> prodParaV = producciones.get(v);
			int tamListaProducciones = prodParaV.size();
			if (tamListaProducciones == 1){
				//if(producciones.get(v).get(0).getConcatenacion().size() == 1){
					//if(producciones.get(v).get(0).getConcatenacion().get(0).equals(lambda)){
					
						if (!v.equals(this.getVariableInicial()))
							//no keremos tocar nada de la variable inicial, 
							//porke no aparece en ningun lado a sust
							prodUnit.add(new String(v));	
					//}
				//}
			}
			
			i++;
		}
		return (!prodUnit.isEmpty());
	}
	
	public boolean dimeSiHayVariablesQueNoTienenProd(){
		//metemos variables de mas en var como dice el algoritmo.
		//1 -> Recorrer las variables. Si alguna no tiene produccion -> a�adir para borrar ? o borrar ya?
		//2 -> Recorrer las producciones en buskeda de variables sin producciones
		
		int i = 0; 
		ArrayList<String> vars = this.getVariables();
		//	System.out.println("variables: " + var);
		variablesSinProd = new ArrayList<String>();
		@SuppressWarnings("unused")
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
							//	System.out.println(" a�adida! variablesSinProd es " +  variablesSinProd);
						}
					}
					k++;
				}				
				j++;
			}
			}
			i++;
		}
//		System.out.println("VARIABLES SIN PRODUCCIONES: " + variablesSinProd);
//		System.out.println("vamos a comprobarlo " + producciones);
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
			//nap esta creado, si no se a�ade nada estara vacio
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
	
	public boolean dimeSiHayProdRecursivas(){
			
			prodRecursivas = new ArrayList<String>();
			ArrayList<String> vargram = getVariables();
			HashMap<String,ArrayList<Produccion>> gramsalprod = getProducciones();

			int i = 0; int tam = vargram.size();
			while(i < tam){
				String s = vargram.get(i);
				ArrayList<Produccion> aprod = gramsalprod.get(s);
				Produccion centinela = new Produccion();
				centinela.anadeCadena(s);
				
				if(esta(centinela,aprod)) this.prodRecursivas.add(s);

				i++;
			}
			System.out.println("ke producciones son recursivas?");
			return !this.prodRecursivas.isEmpty();
		}

	public boolean dimeSiHayProdConProdUnitarias(){
		
		prodConProdUnit = new ArrayList<String>();
		prodParaAnadirEnProdUnit = new ArrayList<String>();
		ArrayList<String> vargram = getVariables();
		HashMap<String,ArrayList<Produccion>> gramsalprod = getProducciones();

		int i = 0; int tam = vargram.size();
		while(i < tam){
			String s = vargram.get(i);
			ArrayList<Produccion> aprod = gramsalprod.get(s);
			Iterator<Produccion> itprod = aprod.iterator();
			while(itprod.hasNext()){
				Produccion p = itprod.next();
				if((p.getConcatenacion().size() == 1)&& !s.equals(this.getVariableInicial())){
					if(this.getVariables().contains(p.getConcatenacion().get(0)) &&
							!p.getConcatenacion().get(0).equals(s)	){
						prodConProdUnit.add(s);
						prodParaAnadirEnProdUnit.add(p.getConcatenacion().get(0));
					}
				}
			}
			

			i++;
		}

		return !this.prodConProdUnit.isEmpty();
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
	
	public String toHTML(){
		//0" cellspacing="10"
		String s = "";
		s +="<table border=\"0\" cellspacing=\"10\" align=\"left\">";
		s += "<tr><td border=\"0\">Variables: " + getVariables().toString() + "</td></tr>";
		s += "<tr><td border=\"0\">Variable Inicial: " + getVariableInicial().toString() + "</td></tr>";
		s += "<tr><td border=\"0\">Simbolos Terminales: " + getSimbolos().toString() + "</td></tr>";
		s += "<tr><td border=\"0\">Producciones: " + getProducciones() +  "</td></tr></table>";

		
		
		return s;
	}
}
