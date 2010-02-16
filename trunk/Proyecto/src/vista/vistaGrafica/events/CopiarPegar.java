/**
 * 
 */
package vista.vistaGrafica.events;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import accesoBD.Mensajero;
import modelo.AutomatasException;

/**
 * Clase que guarda el ñltimo automata que se ha copiado o cortado y que se puede 
 * pegar a continuaciñn 
 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class CopiarPegar {
	
	private String automata;
	private static CopiarPegar instancia;
	
	/**
	 * Constructor de la clase que inicializa el auotmta para pegar a null
	 */
	public CopiarPegar(){
		automata=null;
	}
	
	/**
	 * Metodo singleton de la clase que asegura que solo habra una instancia del objeto
	 * en la aplicaciñn.
	 * @return instancia del objeto CopiarPegar
	 */
	public static CopiarPegar getInstancia(){
		if(instancia==null) instancia=new CopiarPegar();
		return instancia;
	}

	/**
	 * Metodo que crea un archivo xml con el automata que se ha copiardo o cortado
	 * y que devuelve la ruta del archivo
	 * @return ruta del archivo con el automata
	 * @throws AutomatasException lanza la excepcion si no se puede guardar el archivo
	 */
	public String getAutomata()throws AutomatasException {
		try {
			File fichero = new File ("XML/copia.xml");
			BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
			bw.append(automata);
			bw.close();
		} catch(Exception s){
			Mensajero m=Mensajero.getInstancia();
			throw new AutomatasException(m.devuelveMensaje("vista.erpaste",2));
		}
		return "XML/copia.xml";
	}

	/**
	 * Metodo que guarda el automata que se ha copiado/cortado
	 * @param automata automata cadena que identifica al automata que se ha copiado/cortado
	 */
	public void setAutomata(String automata) {
		this.automata = automata;
	}
	
	/**
	 * Metodo que indica si hay un automata guardado para poder pegar 
	 * @return true si hay automata para pegar, false si no lo hay
	 */
	public boolean sePuedePegar(){
		return automata!=null;
	}

}
