package modelo.algoritmos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import modelo.automatas.MaquinaTuring;


public class AceptaTuring {
//ATRIBUTOS:******************************************************	
	private String ruta;
	private MaquinaTuring maquina;
//****************************************************************	
//MÉTODOS*********************************************************	
	public void acepta(String ruta) {
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
	        System.out.println("cinta: "+cinta);
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
	/*
	class LeeFichero {
	   public static void main(String [] arg) {
	      File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;

	      try {
	         // Apertura del fichero y creacion de BufferedReader para poder
	         // hacer una lectura comoda (disponer del metodo readLine()).
	         archivo = new File ("C:\\archivo.txt");
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);

	         // Lectura del fichero
	         String linea;
	         while((linea=br.readLine())!=null)
	            System.out.println(linea);
	      }
	      catch(Exception e){
	         e.printStackTrace();
	      }finally{
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
	}
	*/
		
		
		


/*	public class EscribeFichero
	{
	    public static void main(String[] args)
	    {
	        FileWriter fichero = null;
	        PrintWriter pw = null;
	        try
	        {
	            fichero = new FileWriter("c:/prueba.txt");
	            pw = new PrintWriter(fichero);

	            for (int i = 0; i < 10; i++)
	                pw.println("Linea " + i);

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
	*/
//****************************************************************	
}
