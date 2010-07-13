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
		listaProdPalabras = new ArrayList<Produccion>();
	}
	
	private boolean todosTerminales(ArrayList<Produccion> prod){
		
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
		int i = 0; int tam = listaProd.size();
			//idea: coger las producciones de S. Coger la primera produccion, y sustituir la variable.
			//si tiene terminal, prioridad?
		buscaDondeHayTerminales();
		
		if (this.getProdConTerminal().isEmpty()){
			//System.out.println("NO HAY TERMINALES = NO PODEMOS CREAR CADENAS");
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
				Produccion prod = listaProdPalabras.get(0);	
				listaProdPalabras.remove(0);
				int j = 1;
				ArrayList<String> concat = (ArrayList<String>) prod.getConcatenacion();
				int tamConcat = concat.size();
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
				

					ArrayList<Produccion> prodVar = null;
					if (s == null) {}
					else{
						prodVar = this.getProducciones().get(s);
					}
				//si prodvar == null cadena terminada
					
					if (prodVar == null){
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
								nueva2.add(letrita);
								m++;
							}							
							nueva.setConcatenacion(nueva2);
							if (!esta(nueva,listaProdPalabras)){
								listaProdPalabras.add(nueva);
							}					
						}	
						k++;
						tam = listaProdPalabras.size();
					}
				}

				tam = listaProdPalabras.size();
			}

			while ((listaPalabras.size() < numPalabras)){
				
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
						while(j < tamConcat && !enc){							
							s = concat.get(j);
							if (!this.getVariables().contains(s)){
								j++;						
							}
							else enc = true;
						}
						
					ArrayList<String> principio = new ArrayList<String>();
					for(int k = 0; k <j; k++){
						String trozo = new String(concat.get(k));
							principio.add(trozo);
					}

						construyePosibles(s,enc,concat,j,tam,principio,tamConcat);					
				}
			}
		}
		
		}
	}
	
	public ArrayList<String> dameListaPalabras(){return listaPalabras;}
	
	@SuppressWarnings("unchecked")
	private void construyePosibles(String s,boolean enc,ArrayList<String> concat,int j,int tam, 
			ArrayList<String> principio,int tamConcat){

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

			
				int m = 0;
				int tamConcatProd = concatProd.size();
				
				while(m < tamConcatProd){
					String saux = new String(concatProd.get(m));

					if (!saux.equals(lambda)){
						nueva2.add(saux);
					}
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
		Iterator<String> itLetras = this.getVariables().iterator();

		while (itLetras.hasNext()){
			
			String variableActual = itLetras.next();
			ArrayList<Produccion> prod = this.getProducciones().get(variableActual);
			int i = 0; int tam = prod.size();
			while(i < tam){
				Produccion produc = prod.get(i);
				ArrayList<String> concat = produc.getConcatenacion();
				if (concat.size() == 1 && !this.getVariables().contains(concat.get(0))){
					if (!prodConTerminal.containsKey(variableActual)){
						ArrayList<Integer> aInt = new ArrayList<Integer>();
						aInt.add(i);
						prodConTerminal.put(variableActual, aInt);							
					}
					else{
						ArrayList<Integer> aInt = prodConTerminal.get(variableActual);
						prodConTerminal.remove(variableActual);
						aInt.add(i);
						prodConTerminal.put(variableActual, aInt);							
					}
				}
				i++;
			}
		}
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
	



}
