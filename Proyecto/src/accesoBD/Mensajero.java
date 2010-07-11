/**
 * 
 */
package accesoBD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 * Clase que se encarga de la gestiñn de los mansajes segñn el idioma que se
 * detecte en la versiñn de java instalada, puede cambiarse dinñmicamente
 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class Mensajero {
	
	private static Mensajero mensajero;
	private boolean language;
	
	/**
	 * El mensajero es un Singleton por tanto contiene una instancia de si mismo
	 * y aqui la devuelve de forma estatica
	 * @return Mensajero instancia de Mensajero para la aplicaciñn
	 */
	public static Mensajero getInstancia(){ 
		if(mensajero==null) mensajero=new Mensajero();
		return mensajero;
	}
	
	/**
	 * Constructor que cera el mensajero con el lengueje que contenga la
	 * version de java instalada.
	 */
	public Mensajero(){
		String lang=System.getProperty("user.language");
		//String lang="es";
		if (!lang.equals("es")) language=true;
	}
	
	
	/**
	 * Devuelve el mensaje segun el idioma, el campo y el sector
	 * @param campo campo del xml donde se encuentra el mensaje
	 * @param sector selecciñn del archivo xml donde buscar
	 * @return Mensaje cadana con el mensaje en el idioma especñfico
	 */
	public String devuelveMensaje(String campo, int sector) {
		int idioma=0;
		if (language) idioma=1;
		else idioma=0;
		String xml="";
		switch(sector) { 
			case 1: {
				if (idioma==0) xml="XML/menu/menu_spa.xml";
				else xml="XML/menu/menu_eng.xml";
				break;
			}
			case 2: {
				if (idioma==0) xml="XML/mensajes/mensajes.xml";
				else xml="XML/mensajes/messages.xml";
				break;
			}
			case 3: {//sector traductor html
				if (idioma==0) xml="XML/mensajes/thtml_esp.xml";
				else xml="XML/mensajes/thtml_eng.xml";
				break;
			}
			case 4: {
				xml="XML/simbolos/simbolos.xml";
				break;				
				
			}
			case 5: {
				xml="XML/formatoLatex/formatolatex.xml";
				break;				
				
			}
		}
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new FileInputStream(xml)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		Document documento = parser.getDocument();
		StringTokenizer st=new StringTokenizer(campo,".");
		String seccion=st.nextToken();
		String busco=st.nextToken();

		NodeList nodos = documento.getElementsByTagName(seccion);
		for (int i = 0; i <nodos.item(0).getChildNodes().getLength(); i++) {
			String aux=nodos.item(0).getChildNodes().item(i).toString();
			aux=aux.replace("[","");
			aux=aux.replace("]","");
			StringTokenizer tk=new StringTokenizer(aux,":");
			String compara=tk.nextToken();
			if (compara.equals(busco)) return nodos.item(0).getChildNodes().item(i).getTextContent();	
		}
		return null;
	}
	/**
	 * Actualizacion del idioma
	 * @param idioma false=ingles, true=español
	 */
	public void setIdioma(boolean idioma) {
		this.language=idioma;
	}
}
