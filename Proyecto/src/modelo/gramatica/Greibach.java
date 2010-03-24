package modelo.gramatica;

import java.util.ArrayList;
import java.util.HashMap;

public class Greibach extends GramaticaIC{
	
	public Greibach(ArrayList<String> v, ArrayList<String> s, 
			HashMap<String,ArrayList<Produccion>> p,String vInicial){
		
		super(v,s,p,vInicial);
	}
	


}
