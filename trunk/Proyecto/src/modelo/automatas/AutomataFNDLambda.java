package modelo.automatas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Clase que define las funciones de un automata finito no determinista
 * que ademñs pueda incluir transiciones lambda
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class AutomataFNDLambda extends AutomataFND {
	
	/**
	* Construye un automata finito determinista, posteriormente al irsele añadiendo
	* aristas y transiciones lambda se convertirñ en indeterminista lambda
	*/
	public AutomataFNDLambda() {
		super();
	}
	
	@Override
	/**
	 * Dado un estado y una letra devuelve los estados a los que va con ella
	 * @param estado del que se quiere sacar la informaciñn
	 * @param letra que usamos para ver a quñ estado llegamos
	 * @return  arraylist con los estados que visita el estado con la letra
	 */
	public ArrayList<String> deltaExtendida(String estado,String letra){
		HashMap<String,ArrayList<String>> hs=automata.get(estado);
		if (hs==null) return null;
		return hs.get(letra);

	}
	/**
	 * Dado una lista de estados y una letra devuelve los estados a los que van con ella
	 * @param estados de los que se quiere sacar la informaciñn
	 * @param letra que usamos para ver a quñ estados llegamos
	 * @return arraylist con los estados que visitan los estados con la letra
	 */
	public ArrayList<String> deltaExtendida2(ArrayList<String> estados,String letra){
		ArrayList<String> aux = new ArrayList<String>();
		ArrayList<String> resultado = new ArrayList<String>();
		for (int i=0;i<estados.size();i++){
			aux = deltaExtendida(estados.get(i),letra);
			if (aux != null){
				for (int c=0;c<aux.size();c++){
					if (!resultado.contains(aux.get(c)))
						resultado.add(aux.get(c));					
				}
			}
		}
		return resultado;
	}
	
	/**
	 * Calcula el cierre lambda de un estado
	 * @param estado estado del que queremos calcular su cierre lambda
	 * @return arraylist con los estados que forman el cierre lambda buscado
	 */
	public ArrayList<String> cierreLambda(String estado){
		HashMap<String,ArrayList<String>> hs=automata.get(estado);

		ArrayList<String> retorno= new ArrayList<String>();
		ArrayList<String> retornoaux= new ArrayList<String>();
		
		//Si estado no tiene ningñn enlace, o no tiene ninguno lambda, devolvemos estado (siempre podemos ir al mismo estado usando lambda)
		if ((hs==null)||(hs.get("/")==null)){
			retorno.add(estado);
			return retorno;
		}
		
		ArrayList<String> lambda=hs.get("/");
		Iterator<String> it=lambda.iterator();
		
		while(it.hasNext()){
			String s=it.next();
			if(!retorno.contains(s)) 
				retorno.add(s);
			    retornoaux.add(s);
		}
		
		boolean nuevos=true;
		int i = 0;
		
		while(nuevos){
			nuevos=false;
			Iterator<String> aux=retorno.iterator();	
			
			while(aux.hasNext()){
				if (i<retornoaux.size())
					hs=automata.get(retornoaux.get(i));
				
				if(hs==null){i++;break;}
				if (hs.get("/")==null){
					if(i != retornoaux.size()-1){
						nuevos = true;
						}
					else {
						nuevos = false;
						}
					i++;
					break;					
					}
				Iterator<String> ite=hs.get("/").iterator();
				while(ite.hasNext()){
					String ss=ite.next();
					if(!retorno.contains(ss)){
						retorno.add(ss);
						retornoaux.add(ss);
						nuevos=true;
					}
				}
				i++;
			}
		}
		//Siempre devolvemos el estado inicial, ya que un estado siempre va a si mismo con lambda
		if(!retorno.contains(estado))
			retorno.add(estado);
		
		return retorno;
		
	}
	
	/**
	 * Devuelve el alfabeto del automata sin la palabra / (lambda)
	 */
	public Alfabeto getAlfabetoMenosL() {
		for (int i=0;i<alfabeto.cuentaLetras();i++){
			if (alfabeto.dameLetraPos(i).equals("/")){
				alfabeto.eliminaPos(i);
			}
		}
		return alfabeto;
	}

}
