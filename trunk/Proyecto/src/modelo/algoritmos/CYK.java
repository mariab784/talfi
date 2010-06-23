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
	private ArrayList<String> palabras;
	private Controlador controlador;
	private String xml;
	
	/**
	 * 
	 */
	public CYK(Chomsky gramatica, ArrayList<String> pals) {
		// TODO Auto-generated constructor stub
		g = gramatica;
		palabras = pals;
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
			System.out.println(cYK(g, palabra));
			
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
				ArrayList<Produccion> d = g2.getProducciones().get(este);
				Iterator<Produccion> j = d.iterator();
				boolean verdad = false;
				while (j.hasNext() && !verdad){
					if (j.next().contains(ch.toString()))
						verdad = true;
				}
				acepta[i][0][s] = verdad;
				s++;
			}
		}
		// palabras de más longitud mirando si podemos marcar
		for (int i = 1; i < palabra.length; i++){
			for (int j = 0; j<(palabra.length-i); j++){
				for (int k = 0; k< i ; k++){
					Iterator<String> it = prod.iterator();
					int s = 0;
					while (it.hasNext()){
						String este = it.next();
						ArrayList<Produccion> miramos = g2.getProducciones().get(este);
						Iterator<Produccion> it2 = miramos.iterator();
						while (it2.hasNext()){
							Produccion p = it2.next();
							if (p.getConcatenacion().size()!= 1){
								String a1 = p.getConcatenacion().get(0);
								String a2 = p.getConcatenacion().get(1);
								int n1 = aux1.get(a1);
								int n2 = aux1.get(a2);
								//Las posiciones en el keySet las añado a una tabla hash por el coste constante de conseguir las cosas
								//aunque sea lineal crearla, pero solo la creo una vez y añado y accedo bastantes
								if (acepta[j][k][n1] && acepta[j+k+1][i-(k+1)][n2]){
									acepta[j][i][s] = true;
								}
							}
						}
						s++;
						
					}
				}
			}
		}
		//for (int i = 0; i<g2.getProducciones().keySet().size(); i++)
			if (acepta[0][palabra.length-1][aux1.get(g2.getVariableInicial())]) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see modelo.Algoritmo#getXML()
	 */
	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return this.xml;
	}

	/* (non-Javadoc)
	 * @see modelo.Algoritmo#registraControlador(controlador.Controlador)
	 */
	@Override
	public void registraControlador(Controlador controlador) {
		// TODO Auto-generated method stub
		this.controlador=controlador;

	}
	public static void main(String[] args) {
		ArrayList<String> simbolos = new ArrayList<String>();
		ArrayList<String> variables = new ArrayList<String>();
		HashMap<String,ArrayList<Produccion>> eso = new HashMap<String,ArrayList<Produccion>>(20);
		Produccion p; Produccion p1; Produccion p2;
		ArrayList<Produccion> pon;
		simbolos.add("a");
		simbolos.add("b");
		variables.add("S"); variables.add("D"); variables.add("B"); variables.add("A");
		variables.add("E"); variables.add("G"); variables.add("F");
		//---------------------------------------------------
		p = new Produccion ();
		p1 = new Produccion ();
		p2 = new Produccion ();
		p.anadeCadena("F"); p.anadeCadena("A");
		p1.anadeCadena("G"); p1.anadeCadena("B");
		pon = new ArrayList<Produccion>();
		pon.add(p); pon.add(p1);
		eso.put("S", pon);
		//---------------------------------------------------
		p = new Produccion ();
		p1 = new Produccion ();
		p2 = new Produccion();
		p.anadeCadena("F"); p.anadeCadena("D");
		p1.anadeCadena("G"); p1.anadeCadena("S");
		p2.anadeCadena("a");
		pon = new ArrayList<Produccion>();
		pon.add(p); pon.add(p1); pon.add(p2);
		eso.put("A", pon);
		//---------------------------------------------------
		p = new Produccion ();
		p1 = new Produccion ();
		p2 = new Produccion();
		p.anadeCadena("G"); p.anadeCadena("E");
		p1.anadeCadena("F"); p1.anadeCadena("S");
		p2.anadeCadena("b");
		pon = new ArrayList<Produccion>();
		pon.add(p); pon.add(p1); pon.add(p2);
		eso.put("B", pon);
		//---------------------------------------------------
		p = new Produccion ();

		p.anadeCadena("A"); p.anadeCadena("A");

		pon = new ArrayList<Produccion>();
		pon.add(p); 
		eso.put("D", pon);
		//---------------------------------------------------
		p = new Produccion ();

		p.anadeCadena("B"); p.anadeCadena("B");

		pon = new ArrayList<Produccion>();
		pon.add(p); 
		eso.put("E", pon);
		//---------------------------------------------------
		p = new Produccion ();

		p.anadeCadena("a");

		pon = new ArrayList<Produccion>();
		pon.add(p); 
		eso.put("G", pon);
		//---------------------------------------------------
		p = new Produccion ();

		p.anadeCadena("b");

		pon = new ArrayList<Produccion>();
		pon.add(p); 
		eso.put("F", pon);
		ArrayList<String> pasa;
		
		Chomsky s = new Chomsky(variables, simbolos, eso, "S");
		String palabras = new String ("aabbaa");
		pasa = new ArrayList<String>();
		pasa.add(palabras);
		palabras = new String ("ab");
		pasa.add(palabras);
		palabras = new String ("bbbbbbb");
		pasa.add(palabras);
		palabras = new String ("baba");
		pasa.add(palabras);
		palabras = new String ("bbbba");
		pasa.add(palabras);
		palabras = new String ("aaaaabbbbbababab");
		pasa.add(palabras);
		palabras = new String ("abba");
		pasa.add(palabras);
		palabras = new String ("bababababababababababababababa");
		pasa.add(palabras);
		
		CYK prueba = new CYK(s,pasa);
		prueba.ejecutar(false);
		
	}

}
