package modelo.gramatica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Greibach extends GramaticaIC{
	
	private final int numPalabras = 10;
	private ArrayList<String> listaPalabras;
	
	public Greibach(ArrayList<String> v, ArrayList<String> s, 
			HashMap<String,ArrayList<Produccion>> p,String vInicial){
		
		super(v,s,p,vInicial);
		listaPalabras = new ArrayList<String>();
	}
	
	@SuppressWarnings("unchecked")
	public void creaListaPalabras(){
		
		int tam = listaPalabras.size();
		String variableActual = this.getVariableInicial();
		ArrayList<Produccion> listaProd = this.getProducciones().get(variableActual);
		ArrayList<Produccion> listaProdPalabras = new ArrayList<Produccion>();
		ArrayList<String> pal;
//		while(tam < numPalabras){
	//para empezar		
			Iterator<Produccion> itListaProd = listaProd.iterator();
			while(itListaProd.hasNext()){
				
				Produccion prod = itListaProd.next();
/*				if (!tieneVariables(prod)) listaPalabras.add(construyePalabra(prod.getConcatenacion()));
				else {
					pal = prod.getConcatenacion();
					
				}*/
				Produccion np = new Produccion();
				np.setConcatenacion((ArrayList<String>) prod.getConcatenacion().clone());
				listaProdPalabras.add(np);
				
//			}
		}
		//construyeListaPalabras();
		
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
	
	private boolean tieneVariables(Produccion prod){
		
		ArrayList<String> s = prod.getConcatenacion();
		Iterator<String> itS = s.iterator();
		ArrayList<String> vars = this.getVariables();
		while(itS.hasNext()){
			String ss = itS.next();
			if(vars.contains(ss)) return true;
			
		}
		
		return false;
	}

}
