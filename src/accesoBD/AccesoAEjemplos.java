/**
 * 
 */
package accesoBD;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JOptionPane;
import modelo.AutomatasException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 * Clase que se encarga de la lectura y escriturade un xml que contiene los 
 * ejercicios/ejemplos y a partir del cual se creará el árbol de 
 * ejemplos/ejericios de la interfaz gráfica
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class AccesoAEjemplos {
	
	private static AccesoAEjemplos instancia;
	
	/**
	 * Método Singleton que contiene la única instancia de la clase
	 * para toda la aplicación y que la devuelve de forma estática
	 * @return AccesoAEjemplos instancia de AccesoAEjemplos para la aplicación
	 */
	public static AccesoAEjemplos getInstancia(){
		if(instancia==null) instancia=new AccesoAEjemplos();
		return instancia;
	}
	
	/**
	 * Método que lee del xml y devuelve la colección con los ejemplos almacenados
	 * en el archivo xml
	 * @return colección de ejemplos del xml
	 * @throws AutomatasException lanza la excepción si hay algún problema al parsear
	 * el archivo xml
	 */
	public HashMap<String,ArrayList<String>> devolverListadoEjemplos() throws AutomatasException {
		
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String ruta="XML/arbolEjemplos/arbol.xml";
		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.noarchivo",2));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.sax",2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		
		Document documento = parser.getDocument();
		
		HashMap<String,ArrayList<String>> arbol=new HashMap<String,ArrayList<String>>(); 
		
		//primero los afd
		ArrayList<String> ejemplosAfd=new ArrayList<String>();
		NodeList tipo=documento.getElementsByTagName("afd");
		//System.out.println(tipo.item(0).getChildNodes().item(1).getTextContent());
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejemplosAfd.add(var);
			 j++;
		}	

		arbol.put("AFD",ejemplosAfd);
		///////////////////////////////////////////
		//los afn
		ArrayList<String> ejemplosAfn=new ArrayList<String>();
		tipo=documento.getElementsByTagName("afn");
		//System.out.println(tipo.item(0).getChildNodes().item(1).getTextContent());
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejemplosAfn.add(var);
			 j++;
		}	
		
		arbol.put("AFN",ejemplosAfn);
		
		//los afnlambda
		ArrayList<String> ejemplosAfnlambda=new ArrayList<String>();
		tipo=documento.getElementsByTagName("afnlambda");
		//System.out.println(tipo.item(0).getChildNodes().item(1).getTextContent());
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejemplosAfnlambda.add(var);
			 j++;
		}	
	
		arbol.put("AFNLambda",ejemplosAfnlambda);
		
		return arbol;
	}
	
	/**
	 * Método que lee del xml y devuelve la colección con los ejercicios almacenados
	 * en el archivo xml
	 * @return colección de ejercicios del xml
	 * @throws AutomatasException lanza la excepción si hay algún problema al parsear
	 * el archivo xml
	 */
	public HashMap<String,ArrayList<String>> devolverListadoEjercicios() throws AutomatasException {
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String ruta="XML/arbolEjemplos/arbol.xml";
		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.noarchivo",2));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.sax",2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		
		Document documento = parser.getDocument();
		
		HashMap<String,ArrayList<String>> arbol=new HashMap<String,ArrayList<String>>(); 
		
		//primero los afd
		ArrayList<String> ejerciciosLenguaje=new ArrayList<String>();
		NodeList tipo=documento.getElementsByTagName("lenguaje");
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejerciciosLenguaje.add(var);
			 j++;
		}	
		arbol.put("Lenguaje",ejerciciosLenguaje);
		///////////////////////////////////////////
		//los afn
		ArrayList<String> ejerciciosMinimizacion=new ArrayList<String>();
		tipo=documento.getElementsByTagName("minimizacion");
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejerciciosMinimizacion.add(var);
			 j++;
		}	
		arbol.put("minimizacion",ejerciciosMinimizacion);
		
		//los afnlambda
		ArrayList<String> ejerciciosAFDER=new ArrayList<String>();
		tipo=documento.getElementsByTagName("transformacionafder");
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejerciciosAFDER.add(var);
			 j++;
		}	
		arbol.put("AFD_TO_ER",ejerciciosAFDER);
		
		//los afnlambda
		ArrayList<String> ejerciciosAFNAFD=new ArrayList<String>();
		tipo=documento.getElementsByTagName("transformacionAFNAFD");
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejerciciosAFNAFD.add(var);
			 j++;
		}	
		arbol.put("AFD_TO_AFN",ejerciciosAFNAFD);
		
		//los afnlambda
		ArrayList<String> ejerciciosAFNLAMBDA=new ArrayList<String>();
		tipo=documento.getElementsByTagName("transformacionAFNLambda");
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejerciciosAFNLAMBDA.add(var);
			 j++;
		}	
		arbol.put("AFNLambda_TO_AFN",ejerciciosAFNLAMBDA);
		
		ArrayList<String> ejerciciosEquivAutos=new ArrayList<String>();
		tipo=documento.getElementsByTagName("equivAutos");
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejerciciosEquivAutos.add(var);
			 j++;
		}	
		arbol.put("EquivAutos",ejerciciosEquivAutos);
		
		ArrayList<String> ejerciciosEquivAutoER=new ArrayList<String>();
		tipo=documento.getElementsByTagName("equivAutoER");
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejerciciosEquivAutoER.add(var);
			 j++;
		}	
		arbol.put("EquivAutoER",ejerciciosEquivAutoER);
		
		ArrayList<String> ejerciciosEquivERAuto=new ArrayList<String>();
		tipo=documento.getElementsByTagName("equivERAuto");
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejerciciosEquivERAuto.add(var);
			 j++;
		}	
		arbol.put("EquivERAuto",ejerciciosEquivERAuto);
		
		ArrayList<String> ejerciciosEquivERs=new ArrayList<String>();
		tipo=documento.getElementsByTagName("equivERs");
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejerciciosEquivERs.add(var);
			 j++;
		}	
		arbol.put("EquivERs",ejerciciosEquivERs);
		
		return arbol;
	}

	/**
	 * Método que actualiza el archivo xml de construcción del árbol de ejemplos
	 * con un nuevo ejemplo del tipo que recibe por parámetro
	 * @param texto contiene el ejemplo en formato texto
	 * @param tipo nuevo ejemplo a insertar en el arbol
	 * @throws AutomatasException lanza una excepción si no se puede crear el nuevo
	 * ejemplo o no se puede mofificar el archivo xml de generación del árbol
	 */
	public void actualizaXMLEjemplos(String texto, String tipo) throws AutomatasException{
		// TODO Auto-generated method stub
		
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		String fich=new String();
		
		String ruta="XML/arbolEjemplos/arbol.xml";
		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.noarchivo",2));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.sax",2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		
		try {
		
			HashMap<String,ArrayList<String>> xml=devolverListadoEjemplos();
			ArrayList<String> array=xml.get("AFD");
			Iterator<String> it=array.iterator();
			int i=1;
			fich+="<jtree>\n\t<ejemplos>\n\t\t<afd>\n";
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			i++;
			
			if(tipo.equals("AutomataFD")){
				String nombre="Ejemplo"+i;
				String rutaFichero="XML/ejemplos/AFD/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</afd>\n\t\t<afn>\n";
			
			array=xml.get("AFN");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			i++;
			
			if(tipo.equals("AutomataFND")){
				String nombre="Ejemplo"+i;
				String rutaFichero="XML/ejemplos/AFND/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</afn>\n\t\t<afnlambda>\n";
			
			array=xml.get("AFNLambda");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			i++;
			
			if(tipo.equals("AutomataFNDLambda")){
				String nombre="Ejemplo"+i;
				String rutaFichero="XML/ejemplos/AFND_lambda/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</afnlambda>\n\t</ejemplos>\n\t<ejercicios>\n\t\t<lenguaje>\n";
			
			xml=devolverListadoEjercicios();
			array=xml.get("Lenguaje");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</lenguaje>\n\t\t<minimizacion>\n";
			
			array=xml.get("minimizacion");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</minimizacion>\n\t\t<transformacionafder>\n";
			
			array=xml.get("AFD_TO_ER");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</transformacionafder>\n\t\t<transformacionAFNAFD>\n";
			
			array=xml.get("AFD_TO_AFN");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</transformacionAFNAFD>\n\t\t<transformacionAFNLambda>\n";

			array=xml.get("AFNLambda_TO_AFN");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</transformacionAFNLambda>\n\t\t<equivAutos>\n";
			
			array=xml.get("EquivAutos");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</equivAutos>\n\t\t<equivAutoER>\n";
			
			array=xml.get("EquivAutoER");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</equivAutoER>\n\t\t<equivERAuto>\n";
			
			array=xml.get("EquivERAuto");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</equivERAuto>\n\t\t<equivERs>\n";
			
			array=xml.get("EquivERs");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</equivERss>\n\t</ejercicios>\n</jtree>";
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
			bw.append(fich);
			bw.close();
			
		} catch(IOException ex){
			JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("parser.entsalida",2),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Método que actualiza el archivo xml de construcción del árbol de ejercicios
	 * con un nuevo ejercicio del tipo que recibe por parámetro
	 * @param texto contiene el ejercico en formato texto
	 * @param tipo nuevo ejercicio a insertar en el arbol
	 * @throws AutomatasException lanza una excepción si no se puede crear el nuevo
	 * ejercicio o no se puede mofificar el archivo xml de generación del árbol
	 */
	public void actualizaXMLEjercicios(String texto, String tipo) throws AutomatasException {
		// TODO Auto-generated method stub
	
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		String fich=new String();
		
		String ruta="XML/arbolEjemplos/arbol.xml";
		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.noarchivo",2));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.sax",2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		
		try {
		
			HashMap<String,ArrayList<String>> xml=devolverListadoEjemplos();
			ArrayList<String> array=xml.get("AFD");
			Iterator<String> it=array.iterator();
			int i=1;
			fich+="<jtree>\n\t<ejemplos>\n\t\t<afd>\n";
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
	
			fich+="\t\t</afd>\n\t\t<afn>\n";
			
			array=xml.get("AFN");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</afn>\n\t\t<afnlambda>\n";
			
			array=xml.get("AFNLambda");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</afnlambda>\n\t</ejemplos>\n\t<ejercicios>\n\t\t<lenguaje>\n";
			
			xml=devolverListadoEjercicios();
			array=xml.get("Lenguaje");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
			}
			i++;
			
			if(tipo.equals("Lenguaje")){
				String nombre="Ejercicio"+i;
				String rutaFichero="XML/ejercicios/Lenguaje/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</lenguaje>\n\t\t<minimizacion>\n";
			
			array=xml.get("minimizacion");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			i++;
			
			if(tipo.equals("Minimizacion")){
				String nombre="Ejercicio"+i;
				String rutaFichero="XML/ejercicios/Minimizacion/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</minimizacion>\n\t\t<transformacionafder>\n";
			
			array=xml.get("AFD_TO_ER");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			i++;
			
			if(tipo.equals("RE")){
				String nombre="Ejercicio"+i;
				String rutaFichero="XML/ejercicios/TransformacionAFD_to_ER/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</transformacionafder>\n\t\t<transformacionAFNAFD>\n";
			
			array=xml.get("AFD_TO_AFN");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			i++;
			
			if(tipo.equals("AFNTOAFD")){
				String nombre="Ejercicio"+i;
				String rutaFichero="XML/ejercicios/TransformacionAFNAFD/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</transformacionAFNAFD>\n\t\t<transformacionAFNLambda>\n";

			array=xml.get("AFNLambda_TO_AFN");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			i++;
			
			if(tipo.equals("AFNLTOAFN")){
				String nombre="Ejercicio"+i;
				String rutaFichero="XML/ejercicios/TransformacionAFNLambda_AFN/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</transformacionAFNLambda>\n\t\t<equivAutos>\n";
			
			array=xml.get("EquivAutos");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			if(tipo.equals("EquivAutos")){
				String nombre="Ejercicio"+i;
				String rutaFichero="XML/ejercicios/Equivalencia_Automatas/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</equivAutos>\n\t\t<equivAutoER>\n";
			
			array=xml.get("EquivAutoER");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			if(tipo.equals("EquivAutoER")){
				String nombre="Ejercicio"+i;
				String rutaFichero="XML/ejercicios/Equivalencia_Automata_Expresion/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</equivAutoER>\n\t\t<equivERAuto>\n";
			
			array=xml.get("EquivERAuto");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			if(tipo.equals("EquivERAuto")){
				String nombre="Ejercicio"+i;
				String rutaFichero="XML/ejercicios/Equivalencia_Expresion_Automata/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</equivERAuto>\n\t\t<equivERs>\n";
			
			array=xml.get("EquivERs");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			if(tipo.equals("EquivERs")){
				String nombre="Ejercicio"+i;
				String rutaFichero="XML/ejercicios/Equivalencia_Expresiones/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</equivERs>\n\t</ejercicios>\n</jtree>";
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
			bw.append(fich);
			bw.close();
			
		} catch(IOException ex){
			JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("parser.entsalida",2),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}

}
