package modelo.automatas;

import java.util.ArrayList;

public class AlfabetoPila_imp implements Alfabeto_Pila {

	private ArrayList<String> alfabeto;
	
	public AlfabetoPila_imp(){
		
		alfabeto=new ArrayList<String>();
	}
	
	public AlfabetoPila_imp(ArrayList<String> lista) {
		alfabeto=lista;
	}
	
	
	
	
	public ArrayList<String> dameListaLetras(){
		
		return alfabeto;
	}
	

	public void aniadirLetra(String l){
		
		if(!alfabeto.contains(l)){
			alfabeto.add(l);
		}
	}
	

	public boolean estaLetra(String l){
		
		return alfabeto.contains(l);
	}
	

	public boolean equals(Alfabeto_Pila a){
		
		if (alfabeto.containsAll(a.getListaLetras())) {
			return true;
		}
		return false;
	}
	

	public ArrayList<String> getListaLetras(){
		return alfabeto;
	}

	public String toString() {
		
		String s = "";
		int i = 0;
		while (i < alfabeto.size()){
			s += alfabeto.get(i);
			i++;
			
		}
		return s;
	}
}
