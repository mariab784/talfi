package modelo.algoritmos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import vista.vistaGrafica.AristaTuring;

import modelo.automatas.MaquinaTuring;
 

public class AceptaTuring {
//ATRIBUTOS:******************************************************	
	private String ruta;
	private MaquinaTuring maquina;
//****************************************************************	
	public AceptaTuring(String r, MaquinaTuring m){
		
		ruta = r; maquina = m;
	}
	
//MÉTODOS*********************************************************	
	public void acepta() {
		File archivo = null;
	    FileReader fr = null;
	    BufferedReader br = null;
	    String cinta = "";
	    try {
	    	// Apertura del fichero y creacion de BufferedReader para poder
	        // hacer una lectura comoda (disponer del metodo readLine()).
	        archivo = new File(ruta);
	        fr = new FileReader (archivo);
	        br = new BufferedReader(fr);
	        // Lectura del fichero
	        String linea;
	        while((linea=br.readLine())!=null)
	        	cinta += linea;
	        System.out.println("Hola cinta: " + cinta);//cinta inicial
	        ArrayList<AristaTuring> aristas = maquina.getAristasTuring();
	        int k = 0;
	        int j = 0;
	        String est = maquina.getEstadoIni();//estado de cada pasada
	        char sim = cinta.charAt(/*k*/j);//simbolo de cada pasada
	        int numAristas = aristas.size();
	        
	        maquina.creaAlfEntrada(cinta);
	        
	        if (maquina.getEstadosFinales().isEmpty()){
	        	System.out.println("NO TIENE FINALES NO ACEPTA");
	        }
	        else{
	        
	        
	        while((k < numAristas)/* && (!maquina.getEstadosFinales().contains(est))*/){
	        	
	        	AristaTuring arist = aristas.get(k);
	        	System.out.println("ARIST: " + arist);
	        	System.out.println("EST: " + est);
	        	boolean b1 = arist.contieneOrigen(est);
	        	if(b1) System.out.println("B1 CIERTO");
	        	else System.out.println("B1 falso");
	        	
	        	ArrayList<String> entradaCinta = arist.getEntradaCinta();
	        	System.out.println("getEntradaCinta(): " + entradaCinta);
	        	System.out.println("sim: " + sim);
	        	
	        	boolean b2 = entradaCinta.contains(/*cinta.charAt(j)*/(sim+""));
	        	
	        	if(b2) System.out.println("B2 CIERTO");
	        	else System.out.println("B2 falso");
	        	
	        	System.out.println("j: " + j);
	        	
	        	if( /*(arist.contieneOrigen(est))*/ b1 && 
	        			/*(arist.getEntradaCinta().contains(cinta.charAt(j)))*/ b2 )   {
	        		//cinta.replaceFirst(toString(cinta.charAt(j)),arist.getSimboloCinta());
	        		String primero = "";
	        		
	        		if(j >= 0) primero = cinta.substring(0, j);
	        		else primero = primero.concat(maquina.getBlancoChar()+"");
	        		System.out.println("primero: " + primero);
	        		
	        		System.out.println("recambio: " + arist.getSimboloCinta());
	        		
	        		String resto = "";
	        		System.out.println("j resto: " + j);
	        		if(j < cinta.length()){ 
	        			if (j < 0 ) resto = new String(cinta);
	        			else resto = cinta.substring(j+1); }
	        		else resto = maquina.getBlancoChar()+"";
	        		System.out.println("resto: " + resto);
	        		
	        		cinta = primero.concat(arist.getSimboloCinta()).concat(resto);
	        		System.out.println("CINTA REPLACE: " + cinta);
	        		
	        		if((arist.getDireccion().equals("I"))||(arist.getDireccion().equals("L"))) {
	        			j--;
	        			if (j < 0 ) sim = maquina.getBlancoChar();
	        			else sim = cinta.charAt(/*k*/j);
	        		}
	        		else if((aristas.get(k).getDireccion().equals("D"))||(aristas.get(k).getDireccion().equals("R"))){
	        			j++;
	        			if (j < cinta.length())sim = cinta.charAt(/*k*/j);
	        			else sim = maquina.getBlancoChar();
	        		}	
	        		est = aristas.get(k).getDestino();   //actualizo estado
	        		k = 0;  //empiezo a buscar arista
	        	}
	        	else 
	        		k++;  //busco siguiente arista
	        	
	        	
	        	System.out.println("EST: " + est);
	        }
	        
	        
	        }
	        
	        System.out.println("FIN");
	        if (todoBlancos(cinta,j)) cinta = "";
	        else cinta = salidaCinta(cinta,j);
	        FileWriter fichero = null;
	        PrintWriter pw = null;
	        if(/*k == numAristas*/!maquina.getEstadosFinales().contains(est)) {
	        	try
		        {
		            fichero = new FileWriter(ruta+"output.txt");
		            pw = new PrintWriter(fichero);
		            //for (int i = 0; i < 10; i++)
		                pw.println("La cinta de entrada no es reconocida por la Maquina de Turing.");
		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		           try {
		           // Nuevamente aprovechamos el finally para 
		           // asegurarnos que se cierra el fichero.
		           if (null != fichero)
		              fichero.close();
		           } catch (Exception e2) {
		              e2.printStackTrace();
		           }
		        }
	        }
	        else {
		        try
		        {
		            fichero = new FileWriter(ruta+"output.txt");
		            
		            pw = new PrintWriter(fichero/*fichero+"  -> Cinta resultante, la Máquina de Turing reconoce la entrada."*/);
		            		//fichero+"  -> Cinta resultante, la Máquina de Turing reconoce la entrada.");
		            //for (int i = 0; i < 10; i++)
		                pw.println(cinta);

		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		           try {
		           // Nuevamente aprovechamos el finally para 
		           // asegurarnos que se cierra el fichero.
		           if (null != fichero)
		              fichero.close();
		           } catch (Exception e2) {
		              e2.printStackTrace();
		           }
		        }
	        }
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    finally{
	        // En el finally cerramos el fichero, para asegurarnos
	        // que se cierra tanto si todo va bien como si salta 
	        // una excepcion.
	        try{                    
	        	if( null != fr ){   
	        		fr.close();     
	            }                  
	        }catch (Exception e2){ 
	            e2.printStackTrace();
	        }
	    }
	} 
	
/*private String toString(char charAt) {
	// TODO Auto-generated method stub
	String s = "";
	s += charAt;
	return s;
}*/
//****************************************************************	

public String salidaCinta(String cinta,int j){
	System.out.println("J SALIDACINTA: "+ j);
	if (j == -1) return new String(cinta);
	
	else return new String(cinta.substring(j));
	
}

public boolean todoBlancos(String cinta, int pos){
	
	int tam = cinta.length(); String blanco = maquina.getBlancoChar()+"";
	if(pos < 0){
		String principio = "";
		int i = pos;
		while (i < 0){
			principio = principio.concat(blanco+"");
			i++;
		}
		
		cinta = principio.concat(cinta);
		
		//cinta = principio;
		pos = 0;
		tam = cinta.length();
	}
	
	
	
	while(pos < tam){
		
		String car =cinta.charAt(pos)+"";
		if(!blanco.equals(car))return false;
		pos++;
	}
	System.out.println("TODO BLANCOS!!");
	return true;
	
}


public static void main (String[] args){
	
	MaquinaTuring mt = new MaquinaTuring();

	mt.anadeEstado("s0");
	mt.anadeEstado("s1");	
	mt.anadeEstado("s2");	
	mt.anadeEstado("s3");	
	mt.anadeEstado("s4");
	mt.anadeEstadoFinal("s4");
	mt.setEstadoIni("s0");
	
	AristaTuring a;
	
	a = new AristaTuring(0,0,0,0,"s0","s1");
	a.anadirSimboloCintaEntrada("0");
	a.anadirSimboloCinta("C");
	a.setDireccion("D");
	
	mt.anadeArista(a);
	
	a = new AristaTuring(0,0,0,0,"s0","s3");
	a.anadirSimboloCintaEntrada("U");
	a.anadirSimboloCinta("U");
	a.setDireccion("D");
	
	mt.anadeArista(a);
	
	a = new AristaTuring(0,0,0,0,"s1","s1");
	a.anadirSimboloCintaEntrada("0");
	a.anadirSimboloCinta("0");
	a.setDireccion("D");
	
	mt.anadeArista(a);
	
	a = new AristaTuring(0,0,0,0,"s1","s2");
	a.anadirSimboloCintaEntrada("1");
	a.anadirSimboloCinta("U");
	a.setDireccion("I");
	
	mt.anadeArista(a);
	
	a = new AristaTuring(0,0,0,0,"s1","s1");
	a.anadirSimboloCintaEntrada("U");
	a.anadirSimboloCinta("U");
	a.setDireccion("D");
	
	mt.anadeArista(a);
	
	a = new AristaTuring(0,0,0,0,"s2","s2");
	a.anadirSimboloCintaEntrada("0");
	a.anadirSimboloCinta("0");
	a.setDireccion("I");
	
	mt.anadeArista(a);
	
	a = new AristaTuring(0,0,0,0,"s2","s0");
	a.anadirSimboloCintaEntrada("C");
	a.anadirSimboloCinta("C");
	a.setDireccion("D");
	
	mt.anadeArista(a);
	
	a = new AristaTuring(0,0,0,0,"s2","s2");
	a.anadirSimboloCintaEntrada("U");
	a.anadirSimboloCinta("U");
	a.setDireccion("I");
	
	mt.anadeArista(a);
	
	a = new AristaTuring(0,0,0,0,"s3","s3");
	a.anadirSimboloCintaEntrada("U");
	a.anadirSimboloCinta("U");
	a.setDireccion("D");
	
	mt.anadeArista(a);
	
	a = new AristaTuring(0,0,0,0,"s3","s4");
	a.anadirSimboloCintaEntrada("#");
	a.anadirSimboloCinta("#");
	a.setDireccion("N");
	
	mt.anadeArista(a);
	
	String ruta = "C:\\Documents and Settings\\Ro\\Mis documentos\\turing.txt";
	AceptaTuring acepta = new AceptaTuring(ruta,mt);
	System.out.println("MT: \n" + mt);
	acepta.acepta();
}

}
