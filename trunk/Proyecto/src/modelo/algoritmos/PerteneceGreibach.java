/**
 * 
 */
package modelo.algoritmos;

import accesoBD.Mensajero;
import controlador.Controlador;

import modelo.Algoritmo;
import modelo.automatas.Automata;
import java.util.*;

import modelo.gramatica.*;

/**
 * @author 
 *
 */
public class PerteneceGreibach implements Algoritmo {

	private Greibach gramatica;
	private ArrayList<String> palabra;
	private String lambda;
	private Mensajero mensajero;
	/**
	 * 
	 */
	public PerteneceGreibach(Greibach g, ArrayList<String> s) {
		// TODO Auto-generated constructor stub
		if (mensajero == null) mensajero=Mensajero.getInstancia();

		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		gramatica = g;
		palabra = s;
	}

	@Override
	public Automata ejecutar(boolean muestraPasos) {
		// TODO Auto-generated method stub
		String palabras;
		Iterator<String> it = palabra.iterator();
		while(it.hasNext()){
			palabras = it.next();
			char[] pals = palabras.toCharArray();
			pertGreibach(gramatica, gramatica.getProducciones().get(gramatica.getVariableInicial()), pals.length, pals, 0);
		}
		return null;
	}

	private boolean pertGreibach(Greibach g,
			ArrayList<Produccion> p, int l, char[] pal, int s) {
		// TODO Auto-generated method stub
		boolean este = false;
		if (s == l){
			if (p.contains(lambda)) return true;
			else return false;
		}
		else if (s == l-1){
			Iterator<Produccion> it = p.iterator();
			while (it.hasNext()){
				Produccion aux = it.next();
				if (aux.getConcatenacion().get(0).toCharArray()[0] == pal[s]){
					if (aux.getConcatenacion().get(1) == null) return true;
					else este = este || pertGreibach(g,g.getProducciones().get(aux.getConcatenacion().get(1)),l,pal,s+1);
				}
				return este;
			}
		}
		else {
			Iterator<Produccion> it = p.iterator();
			while (it.hasNext()){
				Produccion aux = it.next();
				if (aux.getConcatenacion().get(0).toCharArray()[0] == pal[s]){
					if (aux.getConcatenacion().get(1) != null)
						este = este || pertGreibach(g,g.getProducciones().get(aux.getConcatenacion().get(1)),l,pal,s+1);
				}
			}
		}
		
		
		return este;
	}

	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registraControlador(Controlador controlador) {
		// TODO Auto-generated method stub
		
	}

}
