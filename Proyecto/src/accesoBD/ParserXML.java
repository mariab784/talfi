/**
 * 
 */
package accesoBD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.xml.sax.SAXException;
import org.w3c.dom.*;
import org.xml.sax.*;

import vista.vistaGrafica.VistaGrafica;


import com.sun.org.apache.xerces.internal.parsers.*;
import modelo.AutomatasException;
import modelo.automatas.AlfabetoCinta;
import modelo.automatas.AlfabetoPila_imp;
import modelo.automatas.Alfabeto_imp;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFD;
import modelo.automatas.AutomataFND;
import modelo.automatas.AutomataFNDLambda;
import modelo.automatas.AutomataPila;
import modelo.automatas.Coordenadas;
import modelo.automatas.MaquinaTuring;
import modelo.expresion_regular.ExpresionRegular;
import modelo.expresion_regular.ExpresionRegularImpl;

/**
 * Clase que se encarga de extraer los datos de los ficheros de tipo xml
 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class ParserXML implements Parser{ 
	
	private static ParserXML parser;
	
	/**
	 * Mñtodo singleton que permite obtener la instancia ñnica
	 * del parser en la aplicaciñn
	 * @return el parser xml 
	 */
	public static ParserXML getInstancia(){
		if(parser==null) parser=new ParserXML();
		return parser;
	}
	
	
public Automata extraerAutomata(String ruta)throws AutomatasException  {
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser();

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
		Alfabeto_imp alf= new Alfabeto_imp();
		AlfabetoPila_imp alfP= new AlfabetoPila_imp();
		AlfabetoCinta alfCinta= new AlfabetoCinta();
		ArrayList<String> estad= new ArrayList<String>();
		ArrayList<String> finalss= new ArrayList<String>();

		//a�adido
		boolean esTuring = false;
		
		NodeList tipo = documento.getElementsByTagName("type");
		
		String var = null;
		for (int i = 1; i <tipo.item(0).getChildNodes().getLength(); i++) {
			 var = tipo.item(0).getChildNodes().item(i).getTextContent();
			 i++;
			 
		}	
		
		Automata automata=null;
		if (var.equals("AutomataFNDLambda")){
			
			automata = new AutomataFNDLambda();
			
			VistaGrafica.setOpcionesAF();

		}
		if (var.equals("AutomataFD")){
			
			automata = new AutomataFD();
			VistaGrafica.setOpcionesAF();

		}
		if (var.equals("AutomataFND")){
			
			automata = new AutomataFND();		
			VistaGrafica.setOpcionesAF();

		}

		/*********************************************************/
		if (var.equals("AutomataPila")){
			
			automata = new AutomataPila();	
			VistaGrafica.setOpcionesAP();

		}
		/*********************************************************/
		if (var.equals("MaquinaTuring")){
			
			automata = new MaquinaTuring();		
			esTuring = true;
			
			VistaGrafica.setOpcionesMT();

		}
		/*********************************************************/
		NodeList nodos = documento.getElementsByTagName("alphabet");
		
		for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
			if(!nodos.item(0).getChildNodes().item(i).getTextContent().equals("\\"))
				alf.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
			i++;
		}
		//if (automata instanceof AutomataPila) ((AutomataPila)automata).setAlfabeto(alf);
		//if (automata instanceof Turing) ((Turing)automata).setAlfabeto(alf);
		/*else*/ automata.setAlfabeto(alf);
		
		nodos = documento.getElementsByTagName("alphabetP");
		if (nodos.item(0) != null){
			
			
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				if (!esTuring)alfP.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
				if (esTuring)alfCinta.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
				 i++;
			}
			if (!esTuring)((AutomataPila)automata).setAlfabetoPila(alfP);
			if(esTuring)((MaquinaTuring)automata).setAlfabetoCinta(alfCinta);
		}
		
		nodos = documento.getElementsByTagName("states");		
		for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				NodeList chivato = nodos.item(0).getChildNodes();
				Node chivato2 = nodos.item(0).getChildNodes().item(1);
				estad.add(nodos.item(0).getChildNodes().item(i).getTextContent());
				i++;
		}
		
		automata.setEstados(estad);
		
		
		nodos = documento.getElementsByTagName("init");

		if (nodos.item(0).getChildNodes().item(1) != null)
			automata.setEstadoInicial(nodos.item(0).getChildNodes().item(1).getTextContent());
		
		
		nodos = documento.getElementsByTagName("finals");
		for (int i = 1; i < nodos.item(0).getChildNodes().getLength(); i++) {
			finalss.add(nodos.item(0).getChildNodes().item(i).getTextContent());
			i++;
		}
		if (!esTuring) automata.setEstadosFinales(finalss);	
		if (esTuring) ((MaquinaTuring)automata).setEstadosFin(finalss);	

		nodos = documento.getElementsByTagName("arrow");
		for (int i = 0; i < nodos.getLength(); i++){
			for (int x = 1; x < nodos.item(i).getChildNodes().getLength(); x++) {
				if (automata instanceof AutomataPila){

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
					
					
					
					//StringTokenizer st=new StringTokenizer(nomArs.getText(),",");
					int indice = 0;
					ArrayList<String> salida = new ArrayList<String>();
					if (s5.compareTo("\\") != 0){
						while(/*st.hasMoreTokens()*/indice < s5.length()){
							String ss= "" + s5.charAt(indice);//st.nextToken();
							salida.add(ss);
							indice++;
					//	System.out.println("TRANS: "+ss);
						//canvas.getListaAristas().add(new Arista(origen.getX(),origen.getY(),destino.getX(),destino.getY(),ss,origen.getEtiqueta(),destino.getEtiqueta()));
						}
					}
					else { salida.add("\\");}
					
					/*System.out.println("S1: " + s1);
					System.out.println("S2: " + s2);
					System.out.println("S3: " + s3);
					System.out.println("S4: " + s4);
					System.out.println("S5: " + s5);*/
					

					
					
					x = x+ 10;
					//insertaArista(String origen,String destino,ArrayList<String> simbolos,String cima,ArrayList<String> salida)
					((AutomataPila)automata).insertaArista2(s1,s2,entrada,s4,salida);				
				}
					

				
				
				//lalalallaal
				
				else if (automata instanceof MaquinaTuring){

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
					
					
					
					//StringTokenizer st=new StringTokenizer(nomArs.getText(),",");
				/*	int indice = 0;
					ArrayList<String> salida = new ArrayList<String>();
					if (s5.compareTo("#") != 0){
						while(/*st.hasMoreTokens()*//*indice < s5.length()){
					/*		String ss= "" + s5.charAt(indice);//st.nextToken();
							salida.add(ss);
							indice++; */
					//	System.out.println("TRANS: "+ss);
						//canvas.getListaAristas().add(new Arista(origen.getX(),origen.getY(),destino.getX(),destino.getY(),ss,origen.getEtiqueta(),destino.getEtiqueta()));
				/*		}
					}
					else { salida.add("#");} */
					
					/*System.out.println("S1: " + s1);
					System.out.println("S2: " + s2);
					System.out.println("S3: " + s3);
					System.out.println("S4: " + s4);
					System.out.println("S5: " + s5);*/
					
					x = x+ 10;
					//insertaArista(String origen,String destino,ArrayList<String> simbolos,String cima,ArrayList<String> salida)
					((MaquinaTuring)automata).insertaArista2(s1,s2,entrada,s4,s5);				
				}
					
				else	{automata.insertaArista(nodos.item(i).getChildNodes().item(x).getTextContent(), nodos.item(i).getChildNodes().item(x+2).getTextContent(), nodos.item(i).getChildNodes().item(x+4).getTextContent());
				x= x+6;}
				
			}
		}
		
		////////////////////////////////////////////////////////////////////////////////
		nodos=documento.getElementsByTagName("estadoCoord");

		if (nodos.getLength()==0) return automata;
		if (nodos==null) return automata;
		for(int i=0;i< nodos.getLength();i++) {
			Coordenadas coord=new Coordenadas((Integer.parseInt(nodos.item(i).getChildNodes().item(1).getTextContent())),
					(Integer.parseInt(nodos.item(i).getChildNodes().item(2).getTextContent())));
			automata.setCoordenadas(nodos.item(i).getChildNodes().item(0).getTextContent(), coord);
		}
//		System.out.println("AUTOMATA PARSER XML: " + automata);
		
		
		nodos = documento.getElementsByTagName("listaPalabras");
		if(nodos.item(0) != null){
			ArrayList<String> listaPalabras= new ArrayList<String>();
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				listaPalabras.add(nodos.item(0).getChildNodes().item(i).getTextContent());
			 i++;
			}
			((AutomataPila)automata).setListaPalabrasEj(listaPalabras);
		}
		
		
		
		return automata;		
	}

	
	public ExpresionRegular extraerER(String ruta)throws AutomatasException  {
		
		Mensajero mensajero=Mensajero.getInstancia();
		String expr = null;
		DOMParser parser = new DOMParser(); 
		
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
		Alfabeto_imp alf= new Alfabeto_imp();
		
		NodeList nodos = documento.getElementsByTagName("RExpr");
		for (int i = 1; i < nodos.item(0).getChildNodes().getLength(); i++) {
			expr = nodos.item(0).getChildNodes().item(i).getTextContent();
			i++;
		}
		
		nodos = documento.getElementsByTagName("alphabet");
		
		for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
			 alf.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
			 i++;
		}	
		
		ExpresionRegular er=new ExpresionRegularImpl(alf,expr);
		return er;
		
	}



}