package modelo.gramatica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class Chomsky extends GramaticaIC{
	
	private HashMap<String,ArrayList<String>> relacionados;

	@SuppressWarnings("unchecked")
	public Chomsky(ArrayList<String> v, ArrayList<String> s,String vInicial){
	
		super((ArrayList<String>) v.clone(),(ArrayList<String>) s.clone(),
				new HashMap<String,ArrayList<Produccion>>(),vInicial);
	}

	public Chomsky(ArrayList<String> v, ArrayList<String> s,
			HashMap<String,ArrayList<Produccion>> producciones,String vInicial){
		
		this(v,s,vInicial);
		HashMap<String,ArrayList<Produccion>> aux = this.clonarProducciones(producciones);
		this.setProducciones(aux);

	}
	
	private HashMap<String,ArrayList<Produccion>> 
	clonarProducciones(HashMap<String,ArrayList<Produccion>> original){
		
		HashMap<String,ArrayList<Produccion>> np = new HashMap<String,ArrayList<Produccion>>();
		
		Set<String> claves = original.keySet();
		Iterator<String> itClaves = claves.iterator();
		while(itClaves.hasNext()){
			String c = new String(itClaves.next());
			ArrayList<Produccion> nproduc = clonarArrayProduc(original.get(c));
			np.put(c, nproduc);
		}
		 
		return np;
	}
	
	public void setRelacionados(HashMap<String,ArrayList<String>> r){relacionados = r;}

	public HashMap<String,ArrayList<String>> getRelacionados(){return relacionados;}
	
	private ArrayList<Produccion> clonarArrayProduc(ArrayList<Produccion> original){
		
		ArrayList<Produccion> np = new ArrayList<Produccion>();
		Iterator<Produccion> itNp = original.iterator();
		while(itNp.hasNext()){
			Produccion actualP = itNp.next();
			np.add(clonar(actualP));
		}		
		return np;
	}

	@SuppressWarnings("unchecked")
	private Produccion clonar(Produccion original){
	
		Produccion p = new Produccion();
		ArrayList<String> temporal = (ArrayList<String>) original.getConcatenacion().clone();
		p.setConcatenacion(temporal);
	
		return p;
	}

}
