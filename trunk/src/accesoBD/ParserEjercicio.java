/**
 * 
 */
package accesoBD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import modelo.AutomatasException;
import modelo.automatas.Alfabeto;
import modelo.automatas.Alfabeto_imp;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFD;
import modelo.automatas.AutomataFND;
import modelo.automatas.AutomataFNDLambda;
import modelo.automatas.Coordenadas;
import modelo.ejercicios.Ejercicio;
import modelo.ejercicios.Ejercicio_imp;

/**
 * Clase que se encarga de la extracion de los ejercicios de los archivos xml
 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class ParserEjercicio {
	
	/**
	 * Extrae un ejercicio desde un xml
	 * @param ruta ruta xml donde se realizará la extracción 
	 * @return Ejercicio resultado de la extracción
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
	 */
	public Ejercicio extraerEjercicio(String ruta) throws AutomatasException {
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		

		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
			
			String tipo=null;
			Document documento = parser.getDocument();
			
			NodeList tipoEnun=documento.getElementsByTagName("tipo");
			tipo=tipoEnun.item(0).getTextContent();
			
			if(tipo.equals("Lenguaje")){
				return extraerEjercicioLenguaje(ruta);
			}
			if(tipo.equals("Minimizacion")) {
				return extraerEjercicioAutomatas(ruta);
			}
			if(tipo.equals("AFNTOAFD")) {
				return extraerEjercicioAFNAFD(ruta);
			}
			if(tipo.equals("AFDTOER")){
				return extraerEjercicioAFDTOER(ruta);
			}
			if(tipo.equals("AFNLTOAFN")){
				return extraerEjercicioAFNLAFN(ruta);
			}
			if(tipo.equals("EquivAutos")){
				return extraerEjercicioEquivAutos(ruta);
			}
			if(tipo.equals("EquivAutoER")){
				return extraerEjercicioEquivAutoER(ruta);
			}
			if(tipo.equals("EquivERAuto")){
				return extraerEjercicioEquivERAuto(ruta);
			}
			if(tipo.equals("EquivERs")){
				return extraerEjercicioEquivERs(ruta);
			}
			
			return null;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.noarchivo",2));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.sax",2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		} catch(AutomatasException e){
			throw e;
		}
	}
	
	/**
	 * Extrae un ejercicio de tipo Automata-Expresión Regular
	 * @param ruta ruta xml donde se realizará la extracción 
	 * @return Ejercicio resultado de la extracción, de tipo AFDTOER
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
	 */
	public Ejercicio extraerEjercicioAFDTOER(String ruta)  throws AutomatasException {
		
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		String output=null;//lo que habrá que comparar en un futuro
		Automata input=null;
		Alfabeto alf=null;
		
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
			e.printStackTrace();
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		
		Document documento = parser.getDocument();
		
		NodeList tipoEnun=documento.getElementsByTagName("enunciado");
		enunciado=tipoEnun.item(0).getTextContent();
		
		NodeList tipo = documento.getElementsByTagName("input");
		String var=null;
		
		int fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("type");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i).getChildNodes().item(j).getTextContent();
				 j++;	 
			}	
				
			if (var.equals("AutomataFD")){		
				input = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){		
				input = new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){			
				input = new AutomataFNDLambda();		
			}	
			
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setAlfabeto(alf);				
				
			ArrayList<String> estad=new ArrayList<String>();
			tipo = documento.getElementsByTagName("states");		
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				estad.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setEstados(estad);
				
			tipo = documento.getElementsByTagName("init");
			input.setEstadoInicial(tipo.item(i).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			tipo = documento.getElementsByTagName("finals");
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++) {
				finalss.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setEstadosFinales(finalss);		

			tipo = documento.getElementsByTagName("arrows");
			ArrayList<String> arrow=null;
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++){
				for(int k=1; k < tipo.item(i).getChildNodes().item(j).getChildNodes().getLength();k++){
					if(arrow==null)arrow=new ArrayList<String>();
					arrow.add(tipo.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
					k++;
				}
				if(arrow!=null)input.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
				arrow=null;
				j++;
			}
		}
		
		tipo = documento.getElementsByTagName("output");
		
		fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("RExpr");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i).getChildNodes().item(j).getTextContent();
				 output=var;
				 j++;	 
			}
		}
		return new Ejercicio_imp(enunciado,input,output,alf,"AFDTOER",ruta);

	}


	/**
	 * Extrae un ejercicio de tipo Lenguaje-Expresion regular
	 * @param ruta ruta xml donde se realizará la extracción 
	 * @return Ejercicio resultado de la extracción, de tipo lenguaje
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
	 */
	public Ejercicio extraerEjercicioLenguaje(String ruta) throws AutomatasException {
		
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		String output=null;//lo que habrá que comparar en un futuro
		Alfabeto alf=null;
		

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
			e.printStackTrace();
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		
		Document documento = parser.getDocument();
		
		NodeList tipoEnun=documento.getElementsByTagName("enunciado");
		enunciado=tipoEnun.item(0).getTextContent();
		
		NodeList tipo = documento.getElementsByTagName("input");
		String var=null;
		
		int fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
		}
		tipo = documento.getElementsByTagName("output");
		
		fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("RExpr");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i).getChildNodes().item(j).getTextContent();
				 output=var;
				 j++;	 
			}
		}
		return new Ejercicio_imp(enunciado,null,output,alf,"Lenguaje",ruta);
	}
	
    /**
     * Extrae un ejercicio de tipo Minimizacion de automatas
      * @param ruta ruta xml donde se realizará la extracción 
	 * @return Ejercicio resultado de la extracción, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioAutomatas(String ruta) throws AutomatasException {
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		Automata input=null;
		Automata output=null;//lo que habrá que comparar en un futuro
		Alfabeto alf=null;
		

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
		
		NodeList tipoEnun=documento.getElementsByTagName("enunciado");
		enunciado=tipoEnun.item(0).getTextContent();
		
		NodeList tipo = documento.getElementsByTagName("input");
		String var=null;
		
		int fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("type");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i).getChildNodes().item(j).getTextContent();
				 j++;	 
			}	
				
			if (var.equals("AutomataFD")){		
				input = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){		
				input = new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){			
				input = new AutomataFNDLambda();		
			}	
			
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setAlfabeto(alf);				
				
			ArrayList<String> estad=new ArrayList<String>();
			tipo = documento.getElementsByTagName("states");		
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				estad.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setEstados(estad);
				
			tipo = documento.getElementsByTagName("init");
			input.setEstadoInicial(tipo.item(i).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			tipo = documento.getElementsByTagName("finals");
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++) {
				finalss.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setEstadosFinales(finalss);		

			tipo = documento.getElementsByTagName("arrows");
			ArrayList<String> arrow=null;
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++){
				for(int k=1; k < tipo.item(i).getChildNodes().item(j).getChildNodes().getLength();k++){
					if(arrow==null)arrow=new ArrayList<String>();
					arrow.add(tipo.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
					k++;
				}
				if(arrow!=null)input.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
				arrow=null;
				j++;
			}
		}
////////////////////////////////////////////////////////////////////////////////
		
		tipo=documento.getElementsByTagName("estadoCoord");
		if (tipo.getLength()!=0&&tipo!=null) {
			for(int i=0;i< tipo.getLength();i++) {
			Coordenadas coord=new Coordenadas((Integer.parseInt(tipo.item(i).getChildNodes().item(1).getTextContent())),
					(Integer.parseInt(tipo.item(i).getChildNodes().item(2).getTextContent())));
			input.setCoordenadas(tipo.item(i).getChildNodes().item(0).getTextContent(), coord);
			}
		}
		////////////////////////////////////////////////////////////////////////////////
		
		tipo = documento.getElementsByTagName("output");
		
		fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("type");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i+1).getChildNodes().item(j).getTextContent();
				 j++;	 
			}	
				
			if (var.equals("AutomataFD")){		
				output = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){		
				output= new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){			
				output = new AutomataFNDLambda();		
			}	
			
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i+1).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setAlfabeto(alf);				
				
			ArrayList<String> estad=new ArrayList<String>();
			tipo = documento.getElementsByTagName("states");		
			for (int j = 1; j <tipo.item(i+1).getChildNodes().getLength(); j++) {
				estad.add(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setEstados(estad);
				
			tipo = documento.getElementsByTagName("init");
			output.setEstadoInicial(tipo.item(i+1).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			tipo = documento.getElementsByTagName("finals");
			for (int j = 1; j < tipo.item(i+1).getChildNodes().getLength(); j++) {
				finalss.add(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setEstadosFinales(finalss);		

			tipo = documento.getElementsByTagName("arrows");
			ArrayList<String> arrow=null;
			for (int j = 1; j < tipo.item(i+1).getChildNodes().getLength(); j++){
				for(int k=1; k < tipo.item(i+1).getChildNodes().item(j).getChildNodes().getLength();k++){
					if(arrow==null)arrow=new ArrayList<String>();
					arrow.add(tipo.item(i+1).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
					k++;
				}
				if(arrow!=null)output.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
				arrow=null;
				j++;
			}
		}
		
		return new Ejercicio_imp(enunciado, input, output, alf,"Minimizacion",ruta);
	}
	
	/**
     * Extrae un ejercicio de tipo transformación de automatas no deterministas en 
     *  deterministas
     * @param ruta ruta xml donde se realizará la extracción 
	 * @return Ejercicio resultado de la extracción, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioAFNAFD(String ruta) throws AutomatasException  {
		// TODO Auto-generated method stub
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		Automata input=null;
		Automata output=null;//lo que habrá que comparar en un futuro
		Alfabeto alf=null;
		

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
		
		NodeList tipoEnun=documento.getElementsByTagName("enunciado");
		enunciado=tipoEnun.item(0).getTextContent();
		
		NodeList tipo = documento.getElementsByTagName("input");
		String var=null;
		
		int fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("type");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i).getChildNodes().item(j).getTextContent();
				 j++;	 
			}	
				
			if (var.equals("AutomataFD")){		
				input = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){		
				input = new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){			
				input = new AutomataFNDLambda();		
			}	
			
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setAlfabeto(alf);				
				
			ArrayList<String> estad=new ArrayList<String>();
			tipo = documento.getElementsByTagName("states");		
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				estad.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setEstados(estad);
				
			tipo = documento.getElementsByTagName("init");
			input.setEstadoInicial(tipo.item(i).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			tipo = documento.getElementsByTagName("finals");
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++) {
				finalss.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setEstadosFinales(finalss);		

			tipo = documento.getElementsByTagName("arrows");
			ArrayList<String> arrow=null;
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++){
				for(int k=1; k < tipo.item(i).getChildNodes().item(j).getChildNodes().getLength();k++){
					if(arrow==null)arrow=new ArrayList<String>();
					arrow.add(tipo.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
					k++;
				}
				if(arrow!=null)input.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
				arrow=null;
				j++;
			}
		}
		////////////////////////////////////////////////////////////////////////////////
		
		tipo = documento.getElementsByTagName("output");
		
		fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("type");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i+1).getChildNodes().item(j).getTextContent();
				 j++;	 
			}	
				
			if (var.equals("AutomataFD")){		
				output = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){		
				output= new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){			
				output = new AutomataFNDLambda();		
			}	
			
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i+1).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setAlfabeto(alf);				
				
			ArrayList<String> estad=new ArrayList<String>();
			tipo = documento.getElementsByTagName("states");		
			for (int j = 1; j <tipo.item(i+1).getChildNodes().getLength(); j++) {
				estad.add(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setEstados(estad);
				
			tipo = documento.getElementsByTagName("init");
			output.setEstadoInicial(tipo.item(i+1).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			tipo = documento.getElementsByTagName("finals");
			for (int j = 1; j < tipo.item(i+1).getChildNodes().getLength(); j++) {
				finalss.add(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setEstadosFinales(finalss);		

			tipo = documento.getElementsByTagName("arrows");
			ArrayList<String> arrow=null;
			for (int j = 1; j < tipo.item(i+1).getChildNodes().getLength(); j++){
				for(int k=1; k < tipo.item(i+1).getChildNodes().item(j).getChildNodes().getLength();k++){
					if(arrow==null)arrow=new ArrayList<String>();
					arrow.add(tipo.item(i+1).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
					k++;
				}
				if(arrow!=null)output.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
				arrow=null;
				j++;
			}
		}
		
		return new Ejercicio_imp(enunciado, input, output, alf,"AFNTOAFD",ruta);
	}

	/**
     * Extrae un ejercicio de tipo transformación de automatas labmda a automatas sin lambda
      * @param ruta ruta xml donde se realizará la extracción 
	 * @return Ejercicio resultado de la extracción, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioAFNLAFN(String ruta) throws AutomatasException{
		// TODO Auto-generated method stub
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		Automata input=null;
		Automata output=null;//lo que habrá que comparar en un futuro
		Alfabeto alf=null;
		

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
		
		NodeList tipoEnun=documento.getElementsByTagName("enunciado");
		enunciado=tipoEnun.item(0).getTextContent();
		
		NodeList tipo = documento.getElementsByTagName("input");
		String var=null;
		
		int fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("type");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i).getChildNodes().item(j).getTextContent();
				 j++;	 
			}	
				
			if (var.equals("AutomataFD")){		
				input = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){		
				input = new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){			
				input = new AutomataFNDLambda();		
			}	
			
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setAlfabeto(alf);				
				
			ArrayList<String> estad=new ArrayList<String>();
			tipo = documento.getElementsByTagName("states");		
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				estad.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setEstados(estad);
				
			tipo = documento.getElementsByTagName("init");
			input.setEstadoInicial(tipo.item(i).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			tipo = documento.getElementsByTagName("finals");
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++) {
				finalss.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setEstadosFinales(finalss);		

			tipo = documento.getElementsByTagName("arrows");
			ArrayList<String> arrow=null;
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++){
				for(int k=1; k < tipo.item(i).getChildNodes().item(j).getChildNodes().getLength();k++){
					if(arrow==null)arrow=new ArrayList<String>();
					arrow.add(tipo.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
					k++;
				}
				if(arrow!=null)input.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
				arrow=null;
				j++;
			}
		}
		////////////////////////////////////////////////////////////////////////////////
		
		tipo = documento.getElementsByTagName("output");
		
		fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("type");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i+1).getChildNodes().item(j).getTextContent();
				 j++;	 
			}	
				
			if (var.equals("AutomataFD")){		
				output = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){		
				output= new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){			
				output = new AutomataFNDLambda();		
			}	
			
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i+1).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setAlfabeto(alf);				
				
			ArrayList<String> estad=new ArrayList<String>();
			tipo = documento.getElementsByTagName("states");		
			for (int j = 1; j <tipo.item(i+1).getChildNodes().getLength(); j++) {
				estad.add(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setEstados(estad);
				
			tipo = documento.getElementsByTagName("init");
			output.setEstadoInicial(tipo.item(i+1).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			tipo = documento.getElementsByTagName("finals");
			for (int j = 1; j < tipo.item(i+1).getChildNodes().getLength(); j++) {
				finalss.add(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setEstadosFinales(finalss);		

			tipo = documento.getElementsByTagName("arrows");
			ArrayList<String> arrow=null;
			for (int j = 1; j < tipo.item(i+1).getChildNodes().getLength(); j++){
				for(int k=1; k < tipo.item(i+1).getChildNodes().item(j).getChildNodes().getLength();k++){
					if(arrow==null)arrow=new ArrayList<String>();
					arrow.add(tipo.item(i+1).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
					k++;
				}
				if(arrow!=null)output.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
				arrow=null;
				j++;
			}
		}
		
		return new Ejercicio_imp(enunciado, input, output, alf,"AFNLAFN",ruta);
	}
	
	/**
     * Extrae un ejercicio de tipo transformación de automatas labmda a automatas sin lambda
      * @param ruta ruta xml donde se realizará la extracción 
	 * @return Ejercicio resultado de la extracción, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioEquivAutos(String ruta) throws AutomatasException{
		// TODO Auto-generated method stub
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		Automata input=null;
		Automata output=null;//lo que habrá que comparar en un futuro
		Alfabeto alf=null;
		

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
		
		NodeList tipoEnun=documento.getElementsByTagName("enunciado");
		enunciado=tipoEnun.item(0).getTextContent();
		
		NodeList tipo = documento.getElementsByTagName("input");
		String var=null;
		
		int fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("type");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i).getChildNodes().item(j).getTextContent();
				 j++;	 
			}	
				
			if (var.equals("AutomataFD")){		
				input = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){		
				input = new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){			
				input = new AutomataFNDLambda();		
			}	
			
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setAlfabeto(alf);				
				
			ArrayList<String> estad=new ArrayList<String>();
			tipo = documento.getElementsByTagName("states");		
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				estad.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setEstados(estad);
				
			tipo = documento.getElementsByTagName("init");
			input.setEstadoInicial(tipo.item(i).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			tipo = documento.getElementsByTagName("finals");
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++) {
				finalss.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setEstadosFinales(finalss);		

			tipo = documento.getElementsByTagName("arrows");
			ArrayList<String> arrow=null;
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++){
				for(int k=1; k < tipo.item(i).getChildNodes().item(j).getChildNodes().getLength();k++){
					if(arrow==null)arrow=new ArrayList<String>();
					arrow.add(tipo.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
					k++;
				}
				if(arrow!=null)input.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
				arrow=null;
				j++;
			}
		}
		////////////////////////////////////////////////////////////////////////////////
		
		tipo = documento.getElementsByTagName("output");
		
		fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("type");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i+1).getChildNodes().item(j).getTextContent();
				 j++;	 
			}	
				
			if (var.equals("AutomataFD")){		
				output = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){		
				output= new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){			
				output = new AutomataFNDLambda();		
			}	
			
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i+1).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setAlfabeto(alf);				
				
			ArrayList<String> estad=new ArrayList<String>();
			tipo = documento.getElementsByTagName("states");		
			for (int j = 1; j <tipo.item(i+1).getChildNodes().getLength(); j++) {
				estad.add(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setEstados(estad);
				
			tipo = documento.getElementsByTagName("init");
			output.setEstadoInicial(tipo.item(i+1).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			tipo = documento.getElementsByTagName("finals");
			for (int j = 1; j < tipo.item(i+1).getChildNodes().getLength(); j++) {
				finalss.add(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setEstadosFinales(finalss);		

			tipo = documento.getElementsByTagName("arrows");
			ArrayList<String> arrow=null;
			for (int j = 1; j < tipo.item(i+1).getChildNodes().getLength(); j++){
				for(int k=1; k < tipo.item(i+1).getChildNodes().item(j).getChildNodes().getLength();k++){
					if(arrow==null)arrow=new ArrayList<String>();
					arrow.add(tipo.item(i+1).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
					k++;
				}
				if(arrow!=null)output.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
				arrow=null;
				j++;
			}
		}
		
		return new Ejercicio_imp(enunciado, input, output, alf,"EquivAutos",ruta);
	}
	
	/**
     * Extrae un ejercicio de tipo transformación de automatas labmda a automatas sin lambda
      * @param ruta ruta xml donde se realizará la extracción 
	 * @return Ejercicio resultado de la extracción, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioEquivAutoER(String ruta) throws AutomatasException{
		// TODO Auto-generated method stub
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		Automata input=null;
		Automata output=null;//lo que habrá que comparar en un futuro
		Alfabeto alf=null;
		

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
		
		NodeList tipoEnun=documento.getElementsByTagName("enunciado");
		enunciado=tipoEnun.item(0).getTextContent();
		
		NodeList tipo = documento.getElementsByTagName("input");
		String var=null;
		
		int fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("type");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i).getChildNodes().item(j).getTextContent();
				 j++;	 
			}	
				
			if (var.equals("AutomataFD")){		
				input = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){		
				input = new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){			
				input = new AutomataFNDLambda();		
			}	
			
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setAlfabeto(alf);				
				
			ArrayList<String> estad=new ArrayList<String>();
			tipo = documento.getElementsByTagName("states");		
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				estad.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setEstados(estad);
				
			tipo = documento.getElementsByTagName("init");
			input.setEstadoInicial(tipo.item(i).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			tipo = documento.getElementsByTagName("finals");
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++) {
				finalss.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			input.setEstadosFinales(finalss);		

			tipo = documento.getElementsByTagName("arrows");
			ArrayList<String> arrow=null;
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++){
				for(int k=1; k < tipo.item(i).getChildNodes().item(j).getChildNodes().getLength();k++){
					if(arrow==null)arrow=new ArrayList<String>();
					arrow.add(tipo.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
					k++;
				}
				if(arrow!=null)input.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
				arrow=null;
				j++;
			}
		}
		////////////////////////////////////////////////////////////////////////////////
		
		tipo = documento.getElementsByTagName("output");
		
		fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("type");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i+1).getChildNodes().item(j).getTextContent();
				 j++;	 
			}	
				
			if (var.equals("AutomataFD")){		
				output = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){		
				output= new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){			
				output = new AutomataFNDLambda();		
			}	
			
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i+1).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setAlfabeto(alf);				
				
			ArrayList<String> estad=new ArrayList<String>();
			tipo = documento.getElementsByTagName("states");		
			for (int j = 1; j <tipo.item(i+1).getChildNodes().getLength(); j++) {
				estad.add(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setEstados(estad);
				
			tipo = documento.getElementsByTagName("init");
			output.setEstadoInicial(tipo.item(i+1).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			tipo = documento.getElementsByTagName("finals");
			for (int j = 1; j < tipo.item(i+1).getChildNodes().getLength(); j++) {
				finalss.add(tipo.item(i+1).getChildNodes().item(j).getTextContent());
				j++;
			}
			output.setEstadosFinales(finalss);		

			tipo = documento.getElementsByTagName("arrows");
			ArrayList<String> arrow=null;
			for (int j = 1; j < tipo.item(i+1).getChildNodes().getLength(); j++){
				for(int k=1; k < tipo.item(i+1).getChildNodes().item(j).getChildNodes().getLength();k++){
					if(arrow==null)arrow=new ArrayList<String>();
					arrow.add(tipo.item(i+1).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
					k++;
				}
				if(arrow!=null)output.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
				arrow=null;
				j++;
			}
		}
		
		return new Ejercicio_imp(enunciado, input, output, alf,"EquivAutoER",ruta);
	}
	
	/**
     * Extrae un ejercicio de tipo transformación de automatas labmda a automatas sin lambda
      * @param ruta ruta xml donde se realizará la extracción 
	 * @return Ejercicio resultado de la extracción, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioEquivERAuto(String ruta) throws AutomatasException{
		// TODO Auto-generated method stub
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		String output=null;//lo que habrá que comparar en un futuro
		Alfabeto alf=null;
		

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
			e.printStackTrace();
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		
		Document documento = parser.getDocument();
		
		NodeList tipoEnun=documento.getElementsByTagName("enunciado");
		enunciado=tipoEnun.item(0).getTextContent();
		
		NodeList tipo = documento.getElementsByTagName("input");
		String var=null;
		
		int fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
		}
		tipo = documento.getElementsByTagName("output");
		
		fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("RExpr");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i).getChildNodes().item(j).getTextContent();
				 output=var;
				 j++;	 
			}
		}
		
		return new Ejercicio_imp(enunciado, null, output, alf,"EquivERAuto",ruta);
	}
	
	/**
     * Extrae un ejercicio de tipo transformación de automatas labmda a automatas sin lambda
      * @param ruta ruta xml donde se realizará la extracción 
	 * @return Ejercicio resultado de la extracción, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioEquivERs(String ruta) throws AutomatasException{
		// TODO Auto-generated method stub
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		String output=null;//lo que habrá que comparar en un futuro
		Alfabeto alf=null;
		

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
			e.printStackTrace();
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		
		Document documento = parser.getDocument();
		
		NodeList tipoEnun=documento.getElementsByTagName("enunciado");
		enunciado=tipoEnun.item(0).getTextContent();
		
		NodeList tipo = documento.getElementsByTagName("input");
		String var=null;
		
		int fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
		}
		tipo = documento.getElementsByTagName("output");
		
		fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			 
			tipo = documento.getElementsByTagName("RExpr");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i).getChildNodes().item(j).getTextContent();
				 output=var;
				 j++;	 
			}
		}
		
		return new Ejercicio_imp(enunciado, null, output, alf,"EquivERs",ruta);
	}
	
}
