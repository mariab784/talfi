/**
 * 
 */
package modelo.automatas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
/**
 * Clase que implementa la funcionalidad de los automata finitos deterministas
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class AutomataFD implements Automata{
	
	protected String estadoInicial;
	protected ArrayList<String> estadosFinales;
	protected Alfabeto alfabeto;
	protected Alfabeto alfabetoMenosL;
	protected ArrayList<String> estados;
	protected HashMap<String, HashMap<String,ArrayList<String>>> automata;
	protected HashMap<String,Coordenadas> coordenadasGraficas;
	
	/**
	 * Constructor que crea un automata finito determinista vacño
	 */
	public AutomataFD() {
		coordenadasGraficas=null;
		estados=new ArrayList<String>();
		estadosFinales=new ArrayList<String>();
		alfabeto=new Alfabeto_imp();
		alfabetoMenosL=new Alfabeto_imp();
		automata=new HashMap<String, HashMap<String,ArrayList<String>>>();
	}
	
	/**
	 * 
	 * @param a
	 */
	public void setAutomataNuevo(HashMap<String, HashMap<String,ArrayList<String>>> a) {
		this.automata=a;
	}
	
	public String funcion_delta(String estado, String letra) {
		// TODO Auto-generated method stub
		try {
			automata.get(estado).get(letra).get(0);
		}catch(Exception e) {
			return null;
			}
		return automata.get(estado).get(letra).get(0);
	}
	
	public Alfabeto getAlfabeto() {
		// TODO Auto-generated method stub
		return alfabeto;
	}
	public Alfabeto getAlfabetoMenosL() {
		// TODO Auto-generated method stub
		return alfabetoMenosL;
	}
	
	public ArrayList<String> getAristasVertice(String vertice) {
		// TODO Auto-generated method stub
		System.out.println(" AUF: " + automata);
		if (automata.get(vertice)==null) return new ArrayList<String>();
		Iterator<String> it=automata.get(vertice).keySet().iterator();
		ArrayList<String> aristas=new ArrayList<String>();
		while(it.hasNext()) aristas.add(it.next());
		return aristas;
	}
	
	public String getEstadoInicial() {
		// TODO Auto-generated method stub
		return estadoInicial;
	}
	
	
	public ArrayList<String> getEstados() {
		// TODO Auto-generated method stub
		return estados;
	}
	
	public ArrayList<String> getEstadosFinales() {
		// TODO Auto-generated method stub
		return estadosFinales;
	}
	
	public void insertaArista(String verticeV, String verticeF, String letra) {
		// TODO Auto-generated method stub
		if (automata.get(verticeV)==null) {
			HashMap<String,ArrayList<String>> hs=new HashMap<String,ArrayList<String>>();
			ArrayList<String> al=new ArrayList<String>();
			al.add(verticeF);
			hs.put(letra, al);
			automata.put(verticeV, hs);
			
		}
		ArrayList<String> aux=automata.get(verticeV).get(letra);
		if (aux!=null) {
			if (!aux.contains(verticeF))automata.get(verticeV).get(letra).add(verticeF);
		}
		else {
			aux=new ArrayList<String>();
			aux.add(verticeF);
			automata.get(verticeV).put(letra, aux);
		}
		
		
	}
	
	public void insertaVertice(String vertice) {
		// TODO Auto-generated method stub
		if(!estados.contains(vertice)){
			estados.add(vertice);
		}
	}
	
	public void setAlfabeto(Alfabeto alfabeto) {
		// TODO Auto-generated method stub
		this.alfabeto=alfabeto;
	}
	
	public void setEstadoInicial(String estado) {
		// TODO Auto-generated method stub
		estadoInicial=estado;
		
	}
	
	public void setEstados(ArrayList<String> estados) {
		// TODO Auto-generated method stub
		this.estados=estados;
		
	}
	
	public void setEstadosFinales(ArrayList<String> estados) {
		// TODO Auto-generated method stub
		this.estadosFinales=estados;
		
	}
	
	/**
	 * Establece lista estados de aceptaciñn del automata uno a uno
	 * recorriendo la lista que se le pasa y añadiendo cada estado
	 * @param estado nueva lista de estados de aceptaciñn
	 */
	public void setEstadosFinales2(ArrayList<String> estado) {
		// TODO Auto-generated method stub
		for (int i=0;i<estado.size();i++){
			this.estadosFinales.add(estado.get(i));
		
		}
	}	
	
	public void setEstadoFinal(String estado) {
		// TODO Auto-generated method stub
		this.estadosFinales.add(estado);
		
	}
	
	public void insertaEstadoFinal(String estado) {
		this.estadosFinales.add(estado);
	}
	
	
	public ArrayList<String> deltaExtendida(String estado, String letra) {
		ArrayList<String> l=new ArrayList<String>();
		l.add(funcion_delta(estado,letra));
		return l;
	}

	
	public Coordenadas getCoordenadas(String estado) {
		// TODO Auto-generated method stub
		return coordenadasGraficas.get(estado);
	}

	
	public void setCoordenadas(String estado, Coordenadas cord) {
		// TODO Auto-generated method stub 
		if (coordenadasGraficas==null) coordenadasGraficas=new HashMap<String,Coordenadas>();
		coordenadasGraficas.put(estado,cord);
	}

	
	public boolean hayCoordenadas() {
		// TODO Auto-generated method stub
		return (coordenadasGraficas!=null);
	}
	
	
	public ArrayList<String> getAristasLetra(String v1,String letra) {
        // TODO Auto-generated method stub
        HashMap<String,ArrayList<String>> hs=automata.get(v1);
        if (hs==null) return null;
        return hs.get(letra);
    }
	
	public String toString() {
		return "Estados:"+estados.toString()+"\n"+"Estados Finales: "+estadosFinales.toString()+"" +
				"\n"+automata.toString();
	}
	
	public void eliminaVertice(String v){
		automata.remove(v);
		Iterator<String> iEst=estados.iterator();
		while(iEst.hasNext()){
			String e=iEst.next();
			Iterator<String> iAlf=this.getAristasVertice(e).iterator();
			while(iAlf.hasNext()){
				String l=iAlf.next();
				ArrayList<String> a=automata.get(e).get(l);
				a.remove(v);
				if (a.size()==0) {
					automata.get(e).remove(l);
				}
			}
		}
		if(!estadosFinales.contains(v)) estados.remove(v);
	}
	
	public void insertaAristaSobreescribe(String v1, String v2,String letra){
		ArrayList<String> letraA=getLetraTransicion(v1,v2);
		Iterator<String> iLet=letraA.iterator();
		if(letraA.size()>0) {
			while(iLet.hasNext()){
				String let=iLet.next();
				ArrayList<String> listaLetras=automata.get(v1).get(let);
				if (listaLetras.size()==1)
					automata.get(v1).remove(let);
				else 
					automata.get(v1).get(let).remove(v2);
				if (let.length()>1&&let.contains("+")) {
					let="("+let+")";
				}
				insertaArista(v1,v2,let+"+"+letra);
				alfabeto.aniadirLetra(let+"+"+letra);
			}
		}
		else {
			insertaArista(v1,v2,letra);
			alfabeto.aniadirLetra(letra);
		}
	}

	
	public ArrayList<String> getLetraTransicion(String estado1, String estado2) {
		// TODO Auto-generated method stub
		ArrayList<String> l=new ArrayList<String>();
		if (automata.get(estado1)!=null) {
		Iterator<String> itaut=automata.get(estado1).keySet().iterator();
		while(itaut.hasNext()) {
			String letra=itaut.next();
			if (automata.get(estado1).get(letra).get(0).equals(estado2)) {
				l.add(letra);
			}
		}		
		}
		return l;
	}

	
	public ArrayList<String> getDestinos(String estado) {
		// TODO Auto-generated method stub
		Iterator<String> itLetras=this.getAristasVertice(estado).iterator();
		ArrayList<String> destinos=new ArrayList<String>();
		while(itLetras.hasNext()) {
			String letra=itLetras.next();
			destinos.add(this.funcion_delta(estado, letra));
		}
		return destinos;
	}
	
	
	@SuppressWarnings("unchecked")
	public Object clone() throws CloneNotSupportedException {
		
		ArrayList<String> nuevosEstados=(ArrayList<String>) this.estados.clone();
		ArrayList<String> nuevosFinales=(ArrayList<String>) this.estadosFinales.clone();
		ArrayList<String> nuevoAlfabeto=(ArrayList<String>) alfabeto.dameListaLetras().clone();
		Alfabeto nuevoAlf=new Alfabeto_imp(nuevoAlfabeto);
		//HashMap<String, HashMap<String,ArrayList<String>>> nuevoAutomata=(HashMap<String, HashMap<String, ArrayList<String>>>) automata.clone();
		HashMap<String, HashMap<String,ArrayList<String>>> nuevoAutomata=new HashMap<String, HashMap<String,ArrayList<String>>>();
		Iterator<String> itclaves=automata.keySet().iterator();
		while(itclaves.hasNext()) {
			String clave=itclaves.next();
			HashMap<String,ArrayList<String>> transEstado=automata.get(clave);
			Iterator<String> itclaves2=transEstado.keySet().iterator();
			HashMap<String,ArrayList<String>> transEstadoNuevo=new HashMap<String,ArrayList<String>> ();
			while(itclaves2.hasNext()) {
				String clave2=itclaves2.next();
				transEstadoNuevo.put(clave2,(ArrayList<String>) transEstado.get(clave2).clone());
			}
			nuevoAutomata.put(clave, transEstadoNuevo);
		}
		
		AutomataFD nuevo=new AutomataFD();
		nuevo.setAlfabeto(nuevoAlf);
		nuevo.setEstados(nuevosEstados);
		nuevo.setEstadosFinales(nuevosFinales);
		nuevo.setEstadoInicial(estadoInicial);
		nuevo.setAutomataNuevo(nuevoAutomata);
		return nuevo;
	     
	}

	
	public HashMap<String, HashMap<String, ArrayList<String>>> getAutomata() {
		// TODO Auto-generated method stub
		return automata;
	}
	public ArrayList<String> getDestinos2(String estado) {
		// TODO Auto-generated method stub
		Iterator<String> itLetras=this.getAristasVertice(estado).iterator();
		ArrayList<String> destinos=new ArrayList<String>();
		while(itLetras.hasNext()) {
			String letra=itLetras.next();
			if ((!estado.equals(this.funcion_delta(estado, letra)) || (this.getEstadoInicial().equals(estado)))){
				destinos.add(this.funcion_delta(estado, letra));
			}
		}
		return destinos;
	}
	
	public void insertaEstadoNoFinal(String estado) {
		this.estados.add(estado);
	}

}

