package modelo.algoritmos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import accesoBD.Mensajero;

import vista.vistaGrafica.AristaTuring;

import modelo.AutomatasException;
import modelo.automatas.MaquinaTuring;
 

public class AceptaTuring {
//ATRIBUTOS:******************************************************	
	private String ruta;
	private MaquinaTuring maquina;
	private int cotaMax;
	private Mensajero mensajero;
//****************************************************************
	
	public AceptaTuring(MaquinaTuring m){
		maquina = m; mensajero=Mensajero.getInstancia();
		
	}
	public AceptaTuring(String r, MaquinaTuring m){
		this(m);
		ruta = r; 
	}
	
	private void cargaArchivo(){
		
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
	        ejecuta(cinta,null,false);
	    }
	    catch(FileNotFoundException e){
	    	JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("vista.nocinta", 2),mensajero.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
	    }
	    catch(Exception e){

	       	e.printStackTrace();
	    }
	}
	
	public int simulaEjecucion(String c,String cf){
		
		return ejecuta(c,cf,true);
	}
	
	private int ejecuta(String c,String cf, boolean corregir){
	
		String cinta = c;

		try {
		ArrayList<AristaTuring> aristas = maquina.getAristasTuring();
        int k = 0;
        int j = 0;
        String est = maquina.getEstadoIni();//estado de cada pasada
        char sim = cinta.charAt(/*k*/j);//simbolo de cada pasada
        int numAristas = aristas.size();
        
        maquina.creaAlfEntrada(cinta);
        
        int numVueltas = 0;
        cotaMax = 500;//(int) (Math.pow( (float)cinta.length(), (float)2 )  * aristas.size());

        if ( (maquina.getEstadoIni() == null) || 
        		(maquina.getEstadoIni() == "") )
        	throw new AutomatasException(mensajero.devuelveMensaje("canvas.noinicial",2));
        else{
        	        
        	while((k < numAristas) && (numVueltas < cotaMax)){
        	
        		AristaTuring arist = aristas.get(k);
    
        		boolean b1 = arist.contieneOrigen(est);
        		ArrayList<String> entradaCinta = arist.getEntradaCinta();
     
        		String sims = sim+"";
        		boolean b2 = entradaCinta.contains(/*(sim+"")*/sims);

        		if(b1 && b2 )   {
        			String primero = "";	        		
        			if(j >= 0) primero = cinta.substring(0, j);
        			else primero = primero.concat(maquina.getBlancoChar()+"");
      
        			String resto = "";
        			if(j < cinta.length()){ 
        				if (j < 0 ) resto = new String(cinta);
        				else resto = cinta.substring(j+1); 
        			}
        			else resto = maquina.getBlancoChar()+"";
        		
        			cinta = primero.concat(arist.getSimboloCinta()).concat(resto);
        		
        			if((arist.getDireccion().equals("I"))||(arist.getDireccion().equals("L"))) {
        				j--;
        				if (j < 0 ) sim = maquina.getBlancoChar();
        				else sim = cinta.charAt(j);
        			}
        			else if((aristas.get(k).getDireccion().equals("D"))||
        				(aristas.get(k).getDireccion().equals("R"))){
        				j++;
        				if (j < cinta.length())sim = cinta.charAt(j);
        				else sim = maquina.getBlancoChar();
        			}	
        			est = aristas.get(k).getDestino();
        			k = 0;
        			numVueltas++;
        		}
        		else 
        			k++;

        		//numVueltas++;
        	}
        }

        if (numVueltas == cotaMax){	        	
       		if(!corregir)JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("vista.ciclos", 2),mensajero.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
       		return -1;
        }
    	else{      		
    		if (todoBlancos(cinta,j)) {
    			if(corregir)cinta = mensajero.devuelveMensaje("simbolos.blanco",4);
    			else cinta = " ";
    			}
    		else cinta = salidaCinta(cinta,j);
    		
    		ArrayList<String> fin = this.maquina.getEstadosFinales();
    		if(fin.isEmpty() || fin == null){
    			FileWriter fichero = new FileWriter(ruta+"output.txt");
        		PrintWriter pw = new PrintWriter(fichero);
            	pw.println(cinta);
            	pw.close();
            	
            	if(cf != null){
            		if(cf.equals(cinta)){/*System.out.println("devuelvo 1 sin finales");*/ return 1;}
            		else{/*System.out.println("devuelvo 2 sin finales");*/ return 2;}
            	}
            	else{
            		//System.out.println("devuelvo 0 sin finales");
            		return 0;
            	}
            }
    		else{
    			if(!maquina.getEstadosFinales().contains(est)){
    				//System.out.println("La cinta de entrada no es reconocida por la Maquina de Turing.");
    				if(!corregir) JOptionPane.showMessageDialog(
                    		null,mensajero.devuelveMensaje("vista.turingno", 2),mensajero.devuelveMensaje("vista.ejecucion", 2)
                    		,JOptionPane.PLAIN_MESSAGE);
    			//	System.out.println("devuelvo 2 con finales");
    				return 2;
    			}
    			else{
    			//	System.out.println("La cinta de entrada es reconocida por la Maquina de Turing.");
    				if(!corregir) JOptionPane.showMessageDialog(
                    		null,mensajero.devuelveMensaje("vista.turingsi", 2),mensajero.devuelveMensaje("vista.ejecucion", 2)
                    		,JOptionPane.PLAIN_MESSAGE);
    	//			System.out.println("devuelvo 1 con finales");
    				return 1;
    			}
    			
    		}
    		
 
   		}	        	
		}
	    catch(FileNotFoundException e){
	    	JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("vista.nocinta", 2),mensajero.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
	    }
	    catch(AutomatasException e){
	    	JOptionPane.showMessageDialog(null,/*mensajero.devuelveMensaje("canvas.noinicial", 2)*/e.getMensaje(),mensajero.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
	    }
	    catch (IOException e) {
			// TODO Auto-generated catch block
	    	JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("parser.noarchivo", 2),mensajero.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
		}

	    return-2;//para que no pete
	}
	
//MÉTODOS*********************************************************
	
	public void acepta() {
		
		cargaArchivo();
		

    
	} 

//****************************************************************	

public String salidaCinta(String cinta,int j){
//	System.out.println("J SALIDACINTA: "+ j);
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
