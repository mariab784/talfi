
package modelo.algoritmos;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import accesoBD.Mensajero;

import modelo.gramatica.Chomsky;
import modelo.gramatica.GramaticaIC;
import modelo.gramatica.Produccion;

public class C_Y_K {
	
	private Chomsky g;
	private ArrayList<String> listaPalabrasSi;
	private ArrayList<String> listaPalabrasNo;
	private boolean aceptadas;
	private boolean noAceptadas;
	private ArrayList<Boolean> listaResultadosSi;
	private ArrayList<Boolean> listaResultadosNo;
	private HashMap<String,Produccion[][]> tablas;
	private Mensajero mensajero;
	private String lambda;
	
	
	@SuppressWarnings("unchecked")
	public C_Y_K(ArrayList<String> lista,ArrayList<String> listaNo, Chomsky gc){
		mensajero = Mensajero.getInstancia();
		tablas = new HashMap<String,Produccion[][]>();
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		g = gc;
		aceptadas = true;
		noAceptadas = true;
		listaResultadosSi = new ArrayList<Boolean>();
		listaResultadosNo = new ArrayList<Boolean>();
		listaPalabrasSi= (ArrayList<String>) lista.clone();
		listaPalabrasNo= (ArrayList<String>) listaNo.clone();
		String pal = null;
		boolean b;
		for(int i = 0; i < listaPalabrasSi.size();i++){
			pal = listaPalabrasSi.get(i);
			if(pal.equals(lambda)){
				
				b = compruebaLambda();
			}
			else{
				b = construyeTabla(pal);
				//System.out.println(b);
			}
			listaResultadosSi.add(b);
			aceptadas =  b && aceptadas ;
			
		}
		for(int i = 0; i < listaPalabrasNo.size();i++){
			
			pal = listaPalabrasNo.get(i);
			if(pal.equals(lambda)){
				
				b = compruebaLambda();
			}
			else{
				b = construyeTabla(pal);
				//System.out.println(b);
			}
			
			
			//b = construyeTabla(listaPalabrasNo.get(i));
			//System.out.println(b);
			listaResultadosNo.add(!b);
			noAceptadas = (!b) && noAceptadas;

		}
		
		System.out.println("LISTAACEPTADAS: " + listaResultadosSi);
		System.out.println("LISTAACEPTADASNO: " + listaResultadosNo);
	}

	private boolean compruebaLambda(){
		Produccion p = new Produccion();
		p.anadeCadena(lambda);
		return( g.compruebaSiEsta(p, g.getProducciones().get(g.getVariableInicial())) );
		
	}
	
	public boolean getAceptadas(){return aceptadas;}
	public boolean getNoAceptadas(){return noAceptadas;}
	
	private String dameCadena(ArrayList<String> c){
		
		String s = "";
		Iterator<String> itc = c.iterator();
		while(itc.hasNext()){
			s+=itc.next();
		}
		return s;
	}
	
	public boolean construyeTabla(String palabra){
		
		int tam = palabra.length();
		Produccion[][] tabla = new Produccion[tam+1][tam+1];


		 for(int i =1;i<=tam;i++){
		 	Produccion p = new Produccion();
		 	String s = /*"["+*/palabra.charAt(i-1)/*+""+"]"*/+"";
		 	//System.out.println("estas? " + s);
		 	//System.out.println("en esto: " + g.getRelacionados());
		 		if(!g.getRelacionados().containsKey(s)){return false;}
		 		else{
		 			p.setConcatenacion(g.getRelacionados().get(s));
		 			tabla[i][1] = p;		 			
		 		}
		 }
		 
		 for(int j=2;j<=tam;j++){
			 
			 for(int i=1;i<=tam-j+1;i++){
				 Produccion p = new Produccion();
				 tabla[i][j] = p;
				 for(int k = 1; k<=j-1; k++){
					 ArrayList<String> vik = tabla[i][k].getConcatenacion();
					 //System.out.println("V"+i+k+": " + vik);
					 ArrayList<String> vimaskjmenosk = tabla[i+k][j-k].getConcatenacion();
					 //System.out.println("V"+(i+k)+","+(j-k)+": " + vimaskjmenosk);
					 for(int m = 0; m < vimaskjmenosk.size(); m++){
						 for(int s = 0; s < vik.size(); s++){
							 ArrayList<String> acentinel = new ArrayList<String>();
							 acentinel.add(vik.get(s)); acentinel.add(vimaskjmenosk.get(m));
							 String centinel = dameCadena(acentinel);
							// System.out.println("centinel: " + centinel/*acentinel.toString()*/);
							// System.out.println("relacionados: " + g.getRelacionados());
							 ArrayList<String> existe = g.getRelacionados().get(centinel/*acentinel.toString()*/);
							// System.out.println("existe: " + existe);
							 if(existe == null){
								 
							 }
							 else{
							//	 System.out.println("EXISTE!: ");
								 for(int mm = 0; mm < existe.size(); mm++){
									 if(!p.getConcatenacion().contains(existe.get(mm)/*existe.toString()*/))
										 p.anadeCadena(/*existe.toString()*/existe.get(mm));
								 }
								 
							 }
							 
						 }
						 
					 }
					 //System.out.println("tabla " + i + "," + j +": " + p);
					 tabla[i][j] = p;
				 }

			 }	 
			 
		 }
		 pintaTabla(tam,tabla);
		 tablas.put(palabra, tabla);
		 return (tabla[1][tam].getConcatenacion().contains(g.getVariableInicial()));
	}
	
	
	public void pintaTabla(int lon,Produccion[][] tabla){
		

		System.out.println();
		for(int j=1; j<=lon; j++){
			for(int i=1; i<=lon; i++){		
			
				System.out.print(" j=" + j +" ");
				System.out.print("i=" + i);
				
				System.out.print("   " +tabla[i][j]);


				//xml+="</step>";
				//System.out.println("I: " + i + " J: " + j + "VALOR: " + tabla[i][j]);
			}
			System.out.println();
		}

		//System.out.println("XML PINTATABLA: " + xml);
	}
	
	
	public static void main(String[] args){

		ArrayList<String> variables = new ArrayList<String>();
		variables.add("S");variables.add("A");variables.add("B");variables.add("C");
		ArrayList<String> terminales = new ArrayList<String>();
		terminales.add("a"); terminales.add("b");
		GramaticaIC g = new GramaticaIC();
		HashMap<String,ArrayList<Produccion>> prod = new HashMap<String,ArrayList<Produccion>>();
		ArrayList<Produccion> lp = new ArrayList<Produccion>();
		Produccion p = new Produccion();
		p.anadeCadena("A"); p.anadeCadena("B"); lp.add(p);
		p = new Produccion();
		p.anadeCadena("B"); p.anadeCadena("C"); lp.add(p);
		prod.put("S", lp);
		lp = new ArrayList<Produccion>();
		p = new Produccion();
		p.anadeCadena("B"); p.anadeCadena("A"); lp.add(p);
		p = new Produccion();
		p.anadeCadena("a"); lp.add(p);
		prod.put("A", lp);
		lp = new ArrayList<Produccion>();
		p = new Produccion();
		p.anadeCadena("C"); p.anadeCadena("C"); lp.add(p);
		p = new Produccion();
		p.anadeCadena("b"); lp.add(p);
		prod.put("B", lp);
		lp = new ArrayList<Produccion>();
		p = new Produccion();
		p.anadeCadena("A"); p.anadeCadena("B"); lp.add(p);
		p = new Produccion();
		p.anadeCadena("a"); lp.add(p);
		prod.put("C", lp);
		g.setVariableInicial("S");
		g.setVariables(variables);
		g.setSimbolos(terminales);
		g.setProducciones(prod);
		
		System.out.println("G: " + g);
		
		ArrayList<String> listaPalabras=new ArrayList<String>();
		listaPalabras.add("baaba");
		ArrayList<String> listaPalabrasNo=new ArrayList<String>();
		listaPalabrasNo.add("aa");
		GIC_to_FNChomsky c = new GIC_to_FNChomsky(g,true);
		c.calculaRelacionadosCYK();
		System.out.println("RELACIONADOS EN ESTE Ej!: " + c.getGramaticaSalida().getRelacionados());
		C_Y_K cyk = new C_Y_K(listaPalabras,listaPalabrasNo,c.getGramaticaSalida());
		
		System.out.println("RESULTADO DE COMPROBAR LASPALABRAS: " + (cyk.getAceptadas()&& cyk.getNoAceptadas()));
		
		
	}
}
