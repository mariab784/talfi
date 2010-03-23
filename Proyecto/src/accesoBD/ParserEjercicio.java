/**
 * 
 */
package accesoBD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import modelo.AutomatasException;
import modelo.automatas.Alfabeto;
import modelo.automatas.AlfabetoPila_imp;
import modelo.automatas.Alfabeto_imp;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFD;
import modelo.automatas.AutomataFND;
import modelo.automatas.AutomataFNDLambda;
import modelo.automatas.AutomataPila;
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
	 * @param ruta ruta xml donde se realizarñ la extracciñn 
	 * @return Ejercicio resultado de la extracciñn
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
	 */
	public Ejercicio extraerEjercicio(String ruta) throws AutomatasException {
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		

		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
			
//			String tipo=null;
			Document documento = parser.getDocument();
			
/*			NodeList tipoEnun=documento.getElementsByTagName("type");
			tipo= tipoEnun.item(1).getChildNodes().item(0).getTextContent();*///tipoEnun.item(0).getTextContent();
//			NodeList tipo = documento.getElementsByTagName("type");

			NodeList tipoE = documento.getElementsByTagName("type");
			 
			String tipo = null;
			for (int i = 1; i <tipoE.item(0).getChildNodes().getLength(); i++) {
				 tipo = tipoE.item(0).getChildNodes().item(i).getTextContent();
				 i++;
				 
			}			
			
			System.out.println("TIPO: " + tipo);
			
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
			if(tipo.equals("TransformacionPila")){
				return extraerEjercicioPila(ruta);
			}
/*			if(tipo.equals("Turing")){
				return extraerEjercicioTuring(ruta);
			}*/
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
	 * Extrae un ejercicio de tipo Automata Pila
	 * @param ruta ruta xml donde se realizarñ la extracciñn 
	 * @return Ejercicio resultado de la extracciñn, de tipo AutomataPila
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
	 */
	public Ejercicio extraerEjercicioPila(String ruta) throws AutomatasException {
		
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		String output=null;//lo que habrñ que comparar en un futuro
		AutomataPila input=null;
		Alfabeto alf=null;
		AlfabetoPila_imp alfPila=null;
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
		
		NodeList nodos = documento.getElementsByTagName("type");
		String var = null;
		for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
			 var = nodos.item(0).getChildNodes().item(i).getTextContent();
			 i++;
			 
		}

			if (var.equals("AutomataPila")){
				
				input = new AutomataPila();	
			}	
			
			alf=new Alfabeto_imp();
			nodos = documento.getElementsByTagName("alphabet");
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				if(!nodos.item(0).getChildNodes().item(i).getTextContent().equals("\\"))
					alf.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
				i++;
			}
			input.setAlfabeto(alf);			
			
			
			alfPila=new AlfabetoPila_imp();
			nodos = documento.getElementsByTagName("alphabetP");
				
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				 alfPila.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
				 i++;
			}
			
			input.setAlfabetoPila(alfPila);
			
			ArrayList<String> estad=new ArrayList<String>();
			nodos = documento.getElementsByTagName("states");		
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				estad.add(nodos.item(0).getChildNodes().item(i).getTextContent());
				i++;
		}
			input.setEstados(estad);
				
			nodos = documento.getElementsByTagName("init");
			input.setEstadoInicial(nodos.item(0).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			nodos = documento.getElementsByTagName("finals");
			for (int i = 1; i < nodos.item(0).getChildNodes().getLength(); i++) {
				finalss.add(nodos.item(0).getChildNodes().item(i).getTextContent());
				i++;
			}
			input.setEstadosFinales(finalss);		

			
			nodos = documento.getElementsByTagName("arrow");
			for (int i = 0; i < nodos.getLength(); i++){
				for (int x = 1; x < nodos.item(i).getChildNodes().getLength(); x++) {
					
					
					String s1 = nodos.item(i).getChildNodes().item(x).getTextContent();
					String s2 = nodos.item(i).getChildNodes().item(x+2).getTextContent();
					
					
					ArrayList<String> entrada = new ArrayList<String>();
					String s3 = nodos.item(i).getChildNodes().item(x+4).getTextContent();
					StringTokenizer st=new StringTokenizer(s3,",");
					while(st.hasMoreTokens()){

						entrada.add(st.nextToken());
					}
					
					
					String s4 = nodos.item(i).getChildNodes().item(x+6).getTextContent();
					String s5 = nodos.item(i).getChildNodes().item(x+8).getTextContent();
		
					int indice = 0;
					ArrayList<String> salida = new ArrayList<String>();
					if (s5.compareTo("\\") != 0){
						while(/*st.hasMoreTokens()*/indice < s5.length()){
							String ss= "" + s5.charAt(indice);//st.nextToken();
							salida.add(ss);
							indice++;
											}
					}
					else { salida.add("\\");}
					
			
					x = x+ 10;
					((AutomataPila)input).insertaArista2(s1,s2,entrada,s4,salida);
					
				}
			}	
			

			nodos=documento.getElementsByTagName("estadoCoord");

			//if (nodos.getLength()==0) return automata;
			//if (nodos==null) return automata;
			for(int i=0;i< nodos.getLength();i++) {
				Coordenadas coord=new Coordenadas((Integer.parseInt(nodos.item(i).getChildNodes().item(1).getTextContent())),
						(Integer.parseInt(nodos.item(i).getChildNodes().item(2).getTextContent())));
				input.setCoordenadas(nodos.item(i).getChildNodes().item(0).getTextContent(), coord);
			}
		
		return new Ejercicio_imp(enunciado,input,output,alf,alfPila,"TRANSFORMACIONPILA",ruta);

		
	}
	
	
	
	
	
	/**
	 * Extrae un ejercicio de tipo Automata-Expresiñn Regular
	 * @param ruta ruta xml donde se realizarñ la extracciñn 
	 * @return Ejercicio resultado de la extracciñn, de tipo AFDTOER
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
	 */
	public Ejercicio extraerEjercicioAFDTOER(String ruta)  throws AutomatasException {
		
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		String output=null;//lo que habrñ que comparar en un futuro
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
	 * @param ruta ruta xml donde se realizarñ la extracciñn 
	 * @return Ejercicio resultado de la extracciñn, de tipo lenguaje
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
	 */
	public Ejercicio extraerEjercicioLenguaje(String ruta) throws AutomatasException {
		
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		String output=null;//lo que habrñ que comparar en un futuro
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
      * @param ruta ruta xml donde se realizarñ la extracciñn 
	 * @return Ejercicio resultado de la extracciñn, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioAutomatas(String ruta) throws AutomatasException {
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		Automata input=null;
		Automata output=null;//lo que habrñ que comparar en un futuro
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
     * Extrae un ejercicio de tipo transformaciñn de automatas no deterministas en 
     *  deterministas
     * @param ruta ruta xml donde se realizarñ la extracciñn 
	 * @return Ejercicio resultado de la extracciñn, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioAFNAFD(String ruta) throws AutomatasException  {
		// TODO Auto-generated method stub
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		Automata input=null;
		Automata output=null;//lo que habrñ que comparar en un futuro
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
     * Extrae un ejercicio de tipo transformaciñn de automatas labmda a automatas sin lambda
      * @param ruta ruta xml donde se realizarñ la extracciñn 
	 * @return Ejercicio resultado de la extracciñn, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioAFNLAFN(String ruta) throws AutomatasException{
		// TODO Auto-generated method stub
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		Automata input=null;
		Automata output=null;//lo que habrñ que comparar en un futuro
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
     * Extrae un ejercicio de tipo transformaciñn de automatas labmda a automatas sin lambda
      * @param ruta ruta xml donde se realizarñ la extracciñn 
	 * @return Ejercicio resultado de la extracciñn, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioEquivAutos(String ruta) throws AutomatasException{
		// TODO Auto-generated method stub
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		Automata input=null;
		Automata output=null;//lo que habrñ que comparar en un futuro
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
     * Extrae un ejercicio de tipo transformaciñn de automatas labmda a automatas sin lambda
      * @param ruta ruta xml donde se realizarñ la extracciñn 
	 * @return Ejercicio resultado de la extracciñn, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioEquivAutoER(String ruta) throws AutomatasException{
		// TODO Auto-generated method stub
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		Automata input=null;
		Automata output=null;//lo que habrñ que comparar en un futuro
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
     * Extrae un ejercicio de tipo transformaciñn de automatas labmda a automatas sin lambda
      * @param ruta ruta xml donde se realizarñ la extracciñn 
	 * @return Ejercicio resultado de la extracciñn, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioEquivERAuto(String ruta) throws AutomatasException{
		// TODO Auto-generated method stub
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		String output=null;//lo que habrñ que comparar en un futuro
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
     * Extrae un ejercicio de tipo transformaciñn de automatas labmda a automatas sin lambda
      * @param ruta ruta xml donde se realizarñ la extracciñn 
	 * @return Ejercicio resultado de la extracciñn, de tipo automatas
	 * @throws AutomatasException se lanza si hay error al abrir o encontrar el archivo
     */
	public Ejercicio extraerEjercicioEquivERs(String ruta) throws AutomatasException{
		// TODO Auto-generated method stub
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();
		
		String enunciado=null;
		String output=null;//lo que habrñ que comparar en un futuro
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
