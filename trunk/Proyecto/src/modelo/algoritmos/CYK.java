/**
 * 
 */
package modelo.algoritmos;

import controlador.Controlador;
import modelo.Algoritmo;
import modelo.automatas.Automata;
import modelo.gramatica.*;

import java.util.*;

/**
 * @author sanjual
 *
 */
public class CYK implements Algoritmo {
	private Chomsky g;
	ArrayList<String> palabras;
	
	/**
	 * 
	 */
	public CYK() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see modelo.Algoritmo#ejecutar(boolean)
	 */
	@Override
	public Automata ejecutar(boolean muestraPasos) {
		// TODO Auto-generated method stub
		Iterator<String> pals = palabras.iterator();
		while (pals.hasNext()){
			char[] palabra = pals.next().toCharArray();
			cYK(g, palabra);
		}
		return null;
	}

	private boolean cYK(Chomsky g2, char[] palabra) {
		// TODO Auto-generated method stub
		Set<String> prod = g2.getProducciones().keySet();
		HashMap<String,Integer> aux1 = new HashMap<String, Integer>();
		boolean[][][] acepta = new boolean[palabra.length][palabra.length][g2.getProducciones().keySet().size()];
		//palabras de longitud 1 para ir marcando
		for (int i = 0; i < palabra.length; i++){
			int s = 0;
			Character ch = palabra[i];
			Iterator<String> it = prod.iterator();
			while (it.hasNext()){
				String este = it.next();
				aux1.put(este, s);
				if (g2.getProducciones().get(este).contains(ch.toString()))
					acepta[i][1][s] = true;
				else acepta[i][1][s] = false;
				s++;
			}
		}
		// palabras de más longitud mirando si podemos marcar
		for (int i = 1; i < palabra.length; i++){
			for (int j = 0; j<(palabra.length-i+1); j++){
				for (int k = 0; k< i-1 ; k++){
					Iterator<String> it = prod.iterator();
					int s = 0;
					while (it.hasNext()){
						String este = it.next();
						ArrayList<Produccion> miramos = g2.getProducciones().get(este);
						Iterator<Produccion> it2 = miramos.iterator();
						while (it2.hasNext()){
							Produccion p = it2.next();
							if (p.getConcatenacion().get(1)!= null){
								String a1 = p.getConcatenacion().get(0);
								String a2 = p.getConcatenacion().get(1);
								//Las posiciones en el keySet las añado a una tabla hash por el coste constante de conseguir las cosas
								//aunque sea lineal crearla, pero solo la creo una vez y añado y accedo bastantes
								if (acepta[j][k][aux1.get(a1)] && acepta[j+k][i-k][aux1.get(a2)]) acepta[j][i][s] = true;
							}
						}
						s++;
						
					}
				}
			}
		}
		for (int i = 0; i<g2.getProducciones().keySet().size(); i++)
			if (acepta[0][palabra.length-1][i]) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see modelo.Algoritmo#getXML()
	 */
	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see modelo.Algoritmo#registraControlador(controlador.Controlador)
	 */
	@Override
	public void registraControlador(Controlador controlador) {
		// TODO Auto-generated method stub

	}

}
