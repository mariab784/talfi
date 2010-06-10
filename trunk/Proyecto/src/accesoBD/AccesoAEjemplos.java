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
 * ejercicios/ejemplos y a partir del cual se crearñ el ñrbol de 
 * ejemplos/ejericios de la interfaz grñfica
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class AccesoAEjemplos {
	
	private static AccesoAEjemplos instancia;
	
	/**
	 * Mñtodo Singleton que contiene la ñnica instancia de la clase
	 * para toda la aplicaciñn y que la devuelve de forma estñtica
	 * @return AccesoAEjemplos instancia de AccesoAEjemplos para la aplicaciñn
	 */
	public static AccesoAEjemplos getInstancia(){
		if(instancia==null) instancia=new AccesoAEjemplos();
		return instancia;
	}
	
	/**
	 * Mñtodo que lee del xml y devuelve la colecciñn con los ejemplos almacenados
	 * en el archivo xml
	 * @return colecciñn de ejemplos del xml
	 * @throws AutomatasException lanza la excepciñn si hay algñn problema al parsear
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
		 
		/*********************************************************/
		ArrayList<String> ejemplosAP = new ArrayList<String>();
		tipo=documento.getElementsByTagName("autompila");
		//System.out.println(tipo.item(0).getChildNodes().item(1).getTextContent());
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejemplosAP.add(var);
			 j++;
		}	
		
		arbol.put("APila",ejemplosAP);
		
		ArrayList<String> ejemplosMT = new ArrayList<String>();
		tipo=documento.getElementsByTagName("maqturing");
		//System.out.println(tipo.item(0).getChildNodes().item(1).getTextContent());
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejemplosMT.add(var);
			 j++;
		}	
		
		arbol.put("MTuring",ejemplosMT);
		/*********************************************************/
		return arbol;
	}
	
	/**
	 * Mñtodo que lee del xml y devuelve la colecciñn con los ejercicios almacenados
	 * en el archivo xml
	 * @return colecciñn de ejercicios del xml
	 * @throws AutomatasException lanza la excepciñn si hay algñn problema al parsear
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
		//AP
		ArrayList<String> ejerciciosAP=new ArrayList<String>();
		tipo=documento.getElementsByTagName("transformacionPila");
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejerciciosAP.add(var);
			 j++;
		}	
		arbol.put("TransformacionPila",ejerciciosAP);
		//MT
/*		ArrayList<String> ejerciciosTuring=new ArrayList<String>();
		tipo=documento.getElementsByTagName("maquinaTuring");
		
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			 String var = tipo.item(0).getChildNodes().item(j).getTextContent();
			 ejerciciosTuring.add(var);
			 j++;
		}	
		arbol.put("MaquinasDeTuring",ejerciciosTuring);*/
		
		
		return arbol;
	}

	/**
	 * Mñtodo que actualiza el archivo xml de construcciñn del ñrbol de ejemplos
	 * con un nuevo ejemplo del tipo que recibe por parñmetro
	 * @param texto contiene el ejemplo en formato texto
	 * @param tipo nuevo ejemplo a insertar en el arbol
	 * @throws AutomatasException lanza una excepciñn si no se puede crear el nuevo
	 * ejemplo o no se puede mofificar el archivo xml de generaciñn del ñrbol
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
			
			//fich+="\t\t</afnlambda>\n\t</ejemplos>\n\t<ejercicios>\n\t\t<lenguaje>\n";
			
			fich+="\t\t</afnlambda>\n\t\t<autompila>\n";
			
			/*********************************************************/
			array=xml.get("APila");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			i++;
			
			if(tipo.equals("AutomataPila")){
				String nombre="Ejemplo"+i;
				String rutaFichero="XML/ejemplos/AP/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</autompila>\n\t<maqturing>\n";
			
			array=xml.get("MTuring");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			i++;
			
			if(tipo.equals("MaquinaTuring")){
				String nombre="Ejemplo"+i;
				String rutaFichero="XML/ejemplos/MT/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</maqturing>\n\t</ejemplos>\n\t<ejercicios>\n\t\t<lenguaje>\n";
			/*********************************************************/
			
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
			
			fich+="\t\t</equivERs>\n\t\t<transformacionPila>\n";
	/////////////////AP
			array=xml.get("TransformacionPila");
			it=array.iterator();
			i=1;		
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</transformacionPila>\n\t</ejercicios>\n</jtree>";
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
			bw.append(fich);
			bw.close();
			
		} catch(IOException ex){
			JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("parser.entsalida",2),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Mñtodo que actualiza el archivo xml de construcciñn del ñrbol de ejercicios
	 * con un nuevo ejercicio del tipo que recibe por parñmetro
	 * @param texto contiene el ejercico en formato texto
	 * @param tipo nuevo ejercicio a insertar en el arbol
	 * @throws AutomatasException lanza una excepciñn si no se puede crear el nuevo
	 * ejercicio o no se puede mofificar el archivo xml de generaciñn del ñrbol
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
			System.out.println("XML EJEMPLOS: " + xml);
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
			
			fich+="\t\t</afnlambda>\n\t\t<autompila>\n";
			
			/*********************************************************/
			array=xml.get("APila");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</autompila>\n\t<maqturing>\n";
			
			array=xml.get("MTuring");
			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			
			fich+="\t\t</maqturing>\n\t</ejemplos>\n\t<ejercicios>\n\t\t<lenguaje>\n";
			/*********************************************************/
			
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
			
			fich+="\t\t</equivERs>\n\t\t<transformacionPila>\n";
			
			array=xml.get("TransformacionPila");

			it=array.iterator();
			i=1;
			
			while(it.hasNext()){
				String s=it.next();
				fich+="\t\t\t<ejemplo>"+s+"</ejemplo>\n";
				i++;
			}
			System.out.println("TIPO EJS: " + tipo);
			if(tipo.equals(/*"TransformacionPila"*/"AutomatasDePila")){
				String nombre="Ejercicio"+i;
				String rutaFichero="XML/ejercicios/Transformacion_Pila/"+nombre+".xml";
				BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero));
				bw.append(texto);
				bw.close();
				fich+="\t\t\t<ejemplo>"+nombre+"</ejemplo>\n";
			}
			
			fich+="\t\t</transformacionPila>\n\t</ejercicios>\n</jtree>";
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
			bw.append(fich);
			bw.close();
			
		} catch(IOException ex){
			JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("parser.entsalida",2),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}

}
