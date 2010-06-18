package modelo.gramatica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import accesoBD.Mensajero;

public class Greibach extends GramaticaIC{
	
	private final int numPalabras = 5;
	private ArrayList<String> listaPalabras;
	private HashMap<String,ArrayList<Integer>> prodConTerminal;
	private String lambda;
	private ArrayList<Produccion> listaProdPalabras;
	
	public Greibach(ArrayList<String> v, ArrayList<String> s, 
			HashMap<String,ArrayList<Produccion>> p,String vInicial){
		
		super(v,s,p,vInicial);
		listaPalabras = new ArrayList<String>();
		prodConTerminal = new HashMap<String,ArrayList<Integer>>();
		Mensajero mensajero = Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
//		palabrasCompletadas = new ArrayList<Integer>();
		listaProdPalabras = new ArrayList<Produccion>();
	}
	
	private boolean todosTerminales(ArrayList<Produccion> prod){ //XXX
		
		for(int i = 0; i < prod.size(); i++){
			
			ArrayList<String> concat = prod.get(i).getConcatenacion();
			if(concat.size() == 1){
				if(tieneVariables(concat)) return false;
			}
			else return false;
		}
		
		
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	public void creaListaPalabras(){
		
		
		String variableActual = this.getVariableInicial();
		ArrayList<Produccion> listaProd = this.getProducciones().get(variableActual);
//		ArrayList<Produccion> listaProdPalabras = new ArrayList<Produccion>();
//		ArrayList<String> pal;

		int i = 0; int tam = listaProd.size();
			//idea: coger las producciones de S. Coger la primera produccion, y sustituir la variable.
			//si tiene terminal, prioridad?
		buscaDondeHayTerminales();
		
		if (this.getProdConTerminal().isEmpty()){
			System.out.println("NO HAY TERMINALES = NO PODEMOS CREAR CADENAS");
		}
		else{
		while(i < tam){
			
			Produccion prod = listaProd.get(i);

			Produccion np = new Produccion();
			ArrayList<String> concat = prod.getConcatenacion();
			np.setConcatenacion((ArrayList<String>) concat.clone());
			listaProdPalabras.add(np);

			i++;
		}
		
//		System.out.println("LISTAprodPALABRAS: " + listaProdPalabras);
		//aki tenemos las producciones de S que vamos a empezar a sustituir
		
		
		//completamos la lista de palabras si es que no hemos llegado al maximo
		if (  todosTerminales(listaProd) ){
			
			System.out.println("NO HAY PRODUCCIONES CON VARIABLES EN S. SOLO TERMINALES");
			 
			for(int m = 0; m < tam; m++){
				listaPalabras.add(this.construyePalabra(listaProd.get(m).getConcatenacion()));
			}
			System.out.println("LISTAPALABRAS: " + listaPalabras);

		}
		else{
			tam = listaProdPalabras.size(); i = 0;		
			while (tam < numPalabras){
		//		System.out.println("LISTAPRODPALABRAS: " + listaProdPalabras);
				Produccion prod = listaProdPalabras.get(0);	
				listaProdPalabras.remove(0);
				int j = 1; //CAMBIADO!! XXX XXX
				ArrayList<String> concat = (ArrayList<String>) prod.getConcatenacion();
				int tamConcat = concat.size();
		//		if (tamConcat > 1){			CUIDADO!!!!!!!!
					boolean enc = false; String s = null;
					while(j < tamConcat && !enc){
						s = concat.get(j);
						if (!this.getVariables().contains(s)){
							j++;						
						}
						else enc = true;
					}
				//sustituimos la variable encontrada en la lista de producciones
				//-------------------------------------------------------------------/
				//copiamos la subcadena que va desde la izq hasta antes de la variable
				
					ArrayList<String> principio = new ArrayList<String>();
					for(int k = 0; k <j; k++){
						String trozo = new String(concat.get(k));
						principio.add(trozo);
					}
				
//				System.out.println("PRINCIPIO: " + principio);
					ArrayList<Produccion> prodVar = null;
					if (s == null) System.out.println("ERROR!ERROR!");
					else{
						prodVar = this.getProducciones().get(s);
			//			System.out.println("NO HAY ERROR!");	
			//			System.out.println("prodVar : " + prodVar);
			//			System.out.println("s : " + s);
					}
				//si prodvar == null cadena terminada
					
					if (prodVar == null){
						
						//this.listaPalabras.add(prod.getConcatenacion().toString());
						System.out.println("prod? : " + prod);
						this.listaProdPalabras.add(prod);
					}
					else{
						
					
					int taman = prodVar.size(); 
					int k = 0; ArrayList<String> 
					nueva2 = null;
				//aqui recorremos las producciones para la variable a sustituir
					if (enc)
						concat.remove(j);
				
					while( (k < taman) && (tam < numPalabras) && (s != null)){
				

						if (enc){

							Produccion itProdVar = prodVar.get(k);
							Produccion nueva = new Produccion();					
							nueva2 = (ArrayList<String>) principio.clone();
							ArrayList<String> concatProd = itProdVar.getConcatenacion();

							int m = 0; concatProd = itProdVar.getConcatenacion();
							int tamConcatProd = concatProd.size();
						
							while(m < tamConcatProd){
								String anadir = concatProd.get(m);
								if (!anadir.equals(lambda))
									nueva2.add(anadir);
								m++;
							}

							m = j;

							while(m < tamConcat-1){		
								String letrita = concat.get(m);
						//	System.out.println("letrita: " + letrita);
								nueva2.add(letrita);
								m++;
							}							
							nueva.setConcatenacion(nueva2);
							if (!esta(nueva,listaProdPalabras)){
						//		System.out.println("nueva : " + nueva);
								listaProdPalabras.add(nueva);
							}					
						}	
						k++;
						tam = listaProdPalabras.size();
					}
				}
					
		//		} //llave else creo	
				tam = listaProdPalabras.size();
	//			System.out.println("listaProdPalabras FINAL BUCLE ANTES DE SUBIR : " + listaProdPalabras);
			}
		
//			System.out.println("vamos bien al final?" + listaProdPalabras);
		//----------------------------hasta aki bien ya------------------------------/
			while ((listaPalabras.size() < numPalabras)){
	//			System.out.println("Lista en cada vuelta!: " + listaPalabras);
	//			System.out.println("Listaprodpalabras en cada vuelta!: " + listaProdPalabras);
				
				boolean enc = false; 
				ArrayList<String> concat = (ArrayList<String>) listaProdPalabras.get(0).getConcatenacion().clone();
				int tamConcat = concat.size();
				if(!tieneVariables(concat)){
					String definitiva = construyePalabra(concat);
					if (!listaPalabras.contains(definitiva))listaPalabras.add(definitiva);
					listaProdPalabras.remove(0);
				}
				else{
					//a�adido
					listaProdPalabras.remove(0);
					
					int j = 0;String s = null;
		//			if (tamConcat > 1){
					//boolean enc = false; 
						while(j < tamConcat && !enc){
							
							s = concat.get(j);
				//			System.out.println("S del bucle!!!!!!" + s);
							if (!this.getVariables().contains(s)){
								j++;						
							}
							else enc = true;
						}
		//			}
				
				//		System.out.println("S FUERA del bucle!!!!!!" + s);
					ArrayList<String> principio = new ArrayList<String>();
					for(int k = 0; k <j; k++){
						String trozo = new String(concat.get(k));
							principio.add(trozo);
					}

						construyePosibles(s,enc,concat,j,tam,principio,tamConcat);					
				}
			}
			System.out.println("Lista: " + listaPalabras);
		}
		
		}
	}
	
	public ArrayList<String> dameListaPalabras(){return listaPalabras;}
	
	@SuppressWarnings("unchecked")
	private void construyePosibles(String s,boolean enc,ArrayList<String> concat,int j,int tam, 
			ArrayList<String> principio,int tamConcat){
		
	//	ArrayList<String> concat= (ArrayList<String>) concat2.clone();
		//System.out.println("PRINCIPIO: " + principio);
		Produccion repetir = new Produccion(); //A�ADIDO
		repetir.setConcatenacion((ArrayList<String>) concat.clone());
		listaProdPalabras.add(repetir);
		
		ArrayList<Produccion> prodVar = null;
		if (s == null) System.out.println("ERROR!ERROR!");
		else{prodVar = this.getProducciones().get(s);}
		
		int taman = prodVar.size(); int k = 0; ArrayList<String> nueva2 = null;
		//aqui recorremos las producciones para la variable a sustituir
		if (enc)
			concat.remove(j); //AKI PETA
		
		while( (k < taman) && (s != null)){
		

			if (enc){

				Produccion itProdVar = prodVar.get(k);

				Produccion nueva = new Produccion();					

				nueva2 = (ArrayList<String>) principio.clone();

				ArrayList<String> concatProd = (ArrayList<String>) itProdVar.getConcatenacion().clone();

			
				int m = 0; //concatProd = itProdVar.getConcatenacion();
				int tamConcatProd = concatProd.size();
				
				while(m < tamConcatProd){
					String saux = new String(concatProd.get(m));
					

					//System.out.println("saux:" + lambda+"fin");
					//System.out.println("saux indexof: " + saux.indexOf(lambda));
					if (!saux.equals(lambda)){
						//System.out.println("saux:" + saux);
					//	System.out.println("lambda:" + lambda+"fin");
					//	System.out.println("distintas!");
						nueva2.add(saux);
					}
					//else{ System.out.println("iguales!!"); }
					m++;
				}

				m = j;

				while(m < tamConcat-1){		
					String letrita = new String(concat.get(m));
					nueva2.add(letrita);
					m++;
				}							
				nueva.setConcatenacion(nueva2);
				if (!esta(nueva,listaProdPalabras))listaProdPalabras.add(nueva);					
			}	
			k++;
			tam = listaProdPalabras.size();
		}
	}
//	tam = listaProdPalabras.size();
		
		
//	}

	
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
	
	public HashMap<String,ArrayList<Integer>> getProdConTerminal(){
		
		return prodConTerminal;
	}
	
	public ArrayList<String> getListaPalabras(){
		
		return listaPalabras;
	}
	
	
	private void buscaDondeHayTerminales(){
//		System.out.println("Ke hay en variables?: " + this.getVariables());
//		System.out.println("Ke hay en terminales?: " + this.getSimbolos());
		Iterator<String> itLetras = this.getVariables().iterator();

		while (itLetras.hasNext()){
			
			String variableActual = itLetras.next();
		//	System.out.println("Ke hay en variableActual?: " + variableActual);
			ArrayList<Produccion> prod = this.getProducciones().get(variableActual);
		//	System.out.println("Ke hay en rrayList<Produccion> prod?: " + prod);
			int i = 0; int tam = prod.size();
			while(i < tam){
				Produccion produc = prod.get(i);
		//		System.out.println("Ke hay en produc?: " + produc);
				ArrayList<String> concat = produc.getConcatenacion();
		//		System.out.println("Ke hay en concat?: " + produc);
				if (concat.size() == 1 && !this.getVariables().contains(concat.get(0))){
		//			System.out.println("TERMINAL!");
					if (!prodConTerminal.containsKey(variableActual)){
		//				System.out.println("NO ESTOY!");
						ArrayList<Integer> aInt = new ArrayList<Integer>();
						aInt.add(i);
						prodConTerminal.put(variableActual, aInt);							
					}
					else{
		//				System.out.println("YA ESTOY!");
						ArrayList<Integer> aInt = prodConTerminal.get(variableActual);
						prodConTerminal.remove(variableActual);
						aInt.add(i);
						prodConTerminal.put(variableActual, aInt);							
					}
				}
				i++;
			}
		}
//		System.out.println("KE HAY EN prodConTerminal?" + prodConTerminal);
//		System.out.println("FIN BUSCAR TERMINALES");
	}
	
	
	private String construyePalabra(ArrayList<String> as) {
		
		String salida = "";
		Iterator<String> itS = as.iterator();
		String s;
		if(as.size() == 1) salida+=itS.next();
		else{
		
			while(itS.hasNext()){
				s = itS.next();
			
				if (!s.equals(lambda) )
					salida+=s;
			
			}
		}
		return salida;
		
		
	}
	
	private boolean tieneVariables(ArrayList<String> prod){
		
		Iterator<String> itS = prod.iterator();
		ArrayList<String> vars = this.getVariables();
		while(itS.hasNext()){
			String ss = itS.next();
			if(vars.contains(ss)) return true;
			
		}
		
		return false;
	}
	
	public String toHTML(){
		//0" cellspacing="10"
		String s = "";
		s +="<table border=\"0\" cellspacing=\"10\" align=\"left\">";
		s += "<tr><td border=\"0\">Variables: " + super.getVariables().toString() + "</td></tr>";
		s += "<tr><td border=\"0\">Variable Inicial: " + super.getVariableInicial().toString() + "</td></tr>";
		s += "<tr><td border=\"0\">Simbolos Terminales: " + super.getSimbolos().toString() + "</td></tr>";
		s += "<tr><td border=\"0\">Producciones: " + super.getProducciones() +  "</td></tr></table>";

		
		
		return s;
	}

	//**********************
	/*XXX XXX*/
	public String toLat(){
		String s = "";//a�adir lo que sale en las lineas horizontales
		HashMap<String, ArrayList<Produccion>> proc = super.getProducciones();
		String p = proc.toString();
		String fin = "";
		int lon = p.length();
		for(int i=0; i< lon; i++){
			if(p.charAt(i) == '\\'){
				fin+="/";
			}	
			else{
				fin+=p.charAt(i);
			}
		}
		s+="\\begin{tabular}{||c||}\n"+
           "\\hline\n" +
           "\\hline\n" +
           "Variables: " + super.getVariables().toString() + " \\\\" +
           "\n" +
           "\\hline\n" +
           "\\hline\n" +
           "\\end{tabular}\n" +
           "\\\\" +
           "\n" +
           "\\begin{tabular}{||c||}\n"+
           "\\hline\n" +
           "\\hline\n" +
           "Variable Inicial: " + super.getVariableInicial().toString() + " \\\\" +
           "\n" +
           "\\hline\n" +
           "\\hline\n" +
           "\\end{tabular}\n" +
           "\\\\" +
           "\n" +
           "\\begin{tabular}{||c||}\n"+
           "\\hline\n" +
           "\\hline\n" +
           "S\\'{i}mbolos Terminales: " + super.getSimbolos().toString() + " \\\\" +
           "\n" +
           "\\hline\n" +
           "\\hline\n" +
           "\\end{tabular}\n" +
           "\\\\" +
           "\n" +
           "\\begin{tabular}{||c||}\n"+
           "\\hline\n" +
           "\\hline\n" +
           "Producciones: " + /*super.getProducciones() +*/fin + " \\\\" +
           "\n" +
           "\\hline\n" +
           "\\hline\n" +
           "\\end{tabular}\n" +
           "\\\\" +
           "\n";
		return s;
	}
	//**********************
	
/*	public boolean dimeSiHayProdMulti(){

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
					
					if(concat.get(0).equals(lambda) || this.getSimbolos().contains(concat.get(0))){
						if (v.equals(this.getVariableInicial())){
							enc = true;
						}
					}
					
					else{enc = true;}
				}
				
				k++;
			}
			if(!enc){
				prodMulti.add(new String(v));
			}

		}
		
		i++;
	}
	return (!prodMulti.isEmpty());
}*/
}