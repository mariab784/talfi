/**
 * 
 */
package modelo.automatas;

import java.util.ArrayList;

/**
 * Clase que implementa las funciones de alfabeto
 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class Alfabeto_imp implements Alfabeto{
	
	private ArrayList<String> alfabeto;
	
	/**
	 * Constructor que crea un afabeto vacño, sin letras, sñlo lambda
	 */
	public Alfabeto_imp(){
		alfabeto=new ArrayList<String>();
	}
	
	/**
	 * Constructor que crea un afabeto a apartir de una lista de letras ademñas de lmbda
	 * que se añade siempre
	 * @param lista lista con las letras que contendrñ el alfabeto
	 */
	public Alfabeto_imp(ArrayList<String> lista) {
		alfabeto=lista;
	}
	
	
	public ArrayList<String> dameListaLetras() {
		return alfabeto;
	}

	
	public ArrayList<String> dameListaLetrasmenosL() {
		ArrayList<String> aux = new ArrayList<String>();
		for (int i=0;i<alfabeto.size();i++){
			if (alfabeto.get(i).equals("/")){}
			else
				aux.add(alfabeto.get(i));
		}
		
		return aux;
	}
	
	
	public void aniadirLetra(String l) {
		if(!alfabeto.contains(l)){
			alfabeto.add(l);
		}
	}
	
	
	public void dameAlfabetoMenosL() {
		for (int i=0;i<alfabeto.size();i++){
			if (alfabeto.get(i).equals("/")){
				alfabeto.remove(i);
			}
		}
	}
	
	
	public boolean estaLetra(String l) {
		return alfabeto.contains(l);
	}
	
	
	public void eliminaPos(int pos) {
		 alfabeto.remove(pos);
	}

	
	public boolean equals(Alfabeto a) {
		if (alfabeto.containsAll(a.getListaLetras())) {
			return true;
		}
		return false;
	}
	
	
	public ArrayList<String> getListaLetras() {
		return alfabeto;
	}

	
	public int cuentaLetras() {
		return alfabeto.size();
	}
	
	
	public String dameLetraPos(int pos) {
		return alfabeto.get(pos);
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