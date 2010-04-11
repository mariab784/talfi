package modelo.gramatica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import accesoBD.Mensajero;

public class Greibach extends GramaticaIC{
	
	private final int numPalabras = 10;
	private ArrayList<String> listaPalabras;
	private ArrayList<Integer> palabrasCompletadas;
	private HashMap<String,ArrayList<Integer>> prodConTerminal;
	private Mensajero mensajero;
	private String lambda;
	
	public Greibach(ArrayList<String> v, ArrayList<String> s, 
			HashMap<String,ArrayList<Produccion>> p,String vInicial){
		
		super(v,s,p,vInicial);
		listaPalabras = new ArrayList<String>();
		prodConTerminal = new HashMap<String,ArrayList<Integer>>();
		Mensajero mensajero = Mensajero.getInstancia();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		palabrasCompletadas = new ArrayList<Integer>();
	}
	
	@SuppressWarnings("unchecked")
	public void creaListaPalabras(){
		
		
		String variableActual = this.getVariableInicial();
		ArrayList<Produccion> listaProd = this.getProducciones().get(variableActual);
		ArrayList<Produccion> listaProdPalabras = new ArrayList<Produccion>();
		ArrayList<String> pal;

		int i = 0; int tam = listaProd.size();
			//idea: coger las producciones de S. Coger la primera produccion, y sustituir la variable.
			//si tiene terminal, prioridad?
		while(i < tam){
			
			Produccion prod = listaProd.get(i);

			Produccion np = new Produccion();
			ArrayList<String> concat = prod.getConcatenacion();
			np.setConcatenacion((ArrayList<String>) concat.clone());
			listaProdPalabras.add(np);

			i++;
		}
		//aki tenemos las producciones de S que vamos a empezar a sustituir
		buscaDondeHayTerminales();
		//completamos la lista de palabras si es que no hemos llegado al maximo
		tam = listaProdPalabras.size(); i = 0;
		
		while (tam < numPalabras){
			
			Produccion prod = listaProdPalabras.get(/*i*/0);	
			listaProdPalabras.remove(/*i*/0);
			int j = 1; 
			ArrayList<String> concat = (ArrayList<String>) prod.getConcatenacion();
			int tamConcat = concat.size();
			if (tamConcat > 1){
				boolean enc = false; String s = null;
				while(j < tamConcat && !enc){
					s = concat.get(j);
					if (!this.getVariables().contains(s)){
						j++;						
					}
					else enc = true;
				}
				//sustituimos la variable encontrada en la lista de producciones
				/***************************************************************/
				//copiamos la subcadena que va desde la izq hasta antes de la variable
				
				ArrayList<String> principio = new ArrayList<String>();
				for(int k = 0; k <j; k++){
					String trozo = concat.get(k);
					principio.add(trozo);
				}
				
				System.out.println("PRINCIPIO: " + principio);
				ArrayList<Produccion> prodVar = null;
				if (s == null) System.out.println("ERROR!ERROR!");
				else{prodVar = this.getProducciones().get(s);}
				
				int taman = prodVar.size(); int k = 0; ArrayList<String> nueva2 = null;
				//aqui recorremos las producciones para la variable a sustituir
				if (enc)
					concat.remove(j);
				
				while( (k < taman) && (tam < numPalabras) ){
				

					if (enc){

						Produccion itProdVar = prodVar.get(k);

						Produccion nueva = new Produccion();					

						nueva2 = (ArrayList<String>) principio.clone();

						ArrayList<String> concatProd = itProdVar.getConcatenacion();

					
						int m = 0; concatProd = itProdVar.getConcatenacion();
						int tamConcatProd = concatProd.size();
						
						while(m < tamConcatProd){
							nueva2.add(concatProd.get(m));
							m++;
						}

						m = j;

						while(m < tamConcat-1){		
							String letrita = concat.get(m);
							System.out.println("letrita: " + letrita);
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
			tam = listaProdPalabras.size();
		}
		
		System.out.println("vamos bien al final?" + listaProdPalabras);
		/**************************hasta aki bien ya**************************/
		while (!listaProdPalabras.isEmpty()){
			
			ArrayList<String> palParaCompletar = listaProdPalabras.get(0).getConcatenacion();
			if(!tieneVariables(palParaCompletar)){
				listaPalabras.add(construyePalabra(palParaCompletar));
				listaProdPalabras.remove(0);
			}
			
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
				
				if (concat.size() == 1){
					
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
		
		String salida = null;
		Iterator<String> itS = as.iterator();
		while(itS.hasNext()){
			String s = itS.next();
			salida+=s;
			
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
