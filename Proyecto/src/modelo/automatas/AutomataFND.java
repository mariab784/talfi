package modelo.automatas;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase que define las funciones de un automata finito no determinista
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class AutomataFND extends AutomataFD {
	
	/**
	* Construye un automata finito determinista, posteriormente al irsele añadiendo
	* aristas se convertirñ en indeterminista
	*/
	public AutomataFND() {
		super();
	}
	
	
	@Override
	public ArrayList<String> deltaExtendida(String estado,String letra){
		
		HashMap<String,ArrayList<String>> hs=automata.get(estado);
		if (hs==null) return null;
		return hs.get(letra);

	}
	
	


}
