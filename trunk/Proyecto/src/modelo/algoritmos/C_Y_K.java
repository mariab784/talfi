
package modelo.algoritmos;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import modelo.gramatica.Chomsky;
import modelo.gramatica.Gramatica;
import modelo.gramatica.GramaticaIC;
import modelo.gramatica.Produccion;

public class C_Y_K {
	
	private Chomsky g;
	private ArrayList<String> listaPalabras;
	private ArrayList<Boolean> listaResultados;
	private HashMap<String,Produccion[][]> tablas;
	
	
	@SuppressWarnings("unchecked")
	public C_Y_K(ArrayList<String> lista, Chomsky gc){
		g = gc;
		listaPalabras= (ArrayList<String>) lista.clone();
		for(int i = 0; i < listaPalabras.size();i++){
			System.out.println(construyeTabla(listaPalabras.get(i)));
		}
	}
	
	public boolean construyeTabla(String palabra){
		
		int tam = palabra.length();
		Produccion[][] tabla = new Produccion[tam][tam];


		 for(int i =1;i<=tam;i++){
		 	Produccion p = new Produccion();
		 	String s = "["+palabra.charAt(i-1)+""+"]";
		// 	System.out.println("estas? " + s);
		// 	System.out.println("en esto: " + g.getRelacionados());
		 		if(!g.getRelacionados().containsKey(s)){return false;}
		 		else{
		 			p.setConcatenacion(g.getRelacionados().get(s));
		 			tabla[i-1][0] = p;		 			
		 		}
		 }
		 
		 for(int j=2;j<=tam;j++){
			 for(int i=1;i<=tam-j+1;i++){
				 Produccion p = new Produccion();
				 tabla[i-1][j-1] = p;
				 for(int k = 1; k<= j-1; k++){
					 ArrayList<String> vik = tabla[i-1][k-1].getConcatenacion();
					 System.out.println("V"+i+k+": " + vik);
					 ArrayList<String> vimaskjmenosk = tabla[i+k-1][j-k-1].getConcatenacion();
					 System.out.println("V"+(i+k)+","+(j-k)+": " + vimaskjmenosk);
					 for(int m = 0; m < vimaskjmenosk.size(); m++){
						 for(int s = 0; s < vik.size(); s++){
							 ArrayList<String> acentinel = new ArrayList<String>();
							 acentinel.add(vik.get(s)); acentinel.add(vimaskjmenosk.get(m));
							 System.out.println("centinel: " + acentinel.toString());
							 System.out.println("relacionados: " + g.getRelacionados());
							 ArrayList<String> existe = g.getRelacionados().get(acentinel.toString());
							 if(existe == null){
								 
							 }
							 else{
								 System.out.println("EXISTE!: ");
								 for(int mm = 0; mm < existe.size(); mm++){
									 p.anadeCadena(existe.toString());
								 }
								 
							 }
							 
						 }
						 
					 }
					 
				 }
				 
				 tabla[i-1][j-1] = p;
			 }	 
			 
		 }
		 pintaTabla(tam,tabla);
		 return (tabla[0][tam-1].getConcatenacion().contains(g.getVariableInicial()));
	}
	
	
	public void pintaTabla(int lon,Produccion[][] tabla){
		


/*		for(int i = 0; i <lon; i++){

			if (i == 0) {System.out.print("  ");}
			//xml+="<step>";

			//xml+="</step>";
			
			System.out.print(this.g.getVariables().get(i) + " ");
		}*/

		System.out.println();
		for(int i=0; i<lon; i++){
			System.out.print("i=" + i);
			for(int j=0; j<lon; j++){
				System.out.print(" j=" + j +" ");

				System.out.print(tabla[j][i]);


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
		ArrayList<String> cad = new ArrayList();
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
		GIC_to_FNChomsky c = new GIC_to_FNChomsky(g,true);
		c.calculaRelacionadosCYK();
		System.out.println("RELACIONADOS EN ESTE Ej!: " + c.getGramaticaSalida().getRelacionados());
		C_Y_K cyk = new C_Y_K(listaPalabras,c.getGramaticaSalida());
		
		
	}
}
