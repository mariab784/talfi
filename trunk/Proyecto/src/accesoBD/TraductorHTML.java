/**
 * 
 * 
 */
package accesoBD;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.*;
import vista.vistaGrafica.Arista;
import vista.vistaGrafica.AristaAP;
import vista.vistaGrafica.CurvedArrow;
import vista.vistaGrafica.Estado;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.org.apache.xerces.internal.parsers.*;
import modelo.AutomatasException;
import modelo.algoritmos.AutomataP_to_GramaticaIC;
import modelo.algoritmos.GIC_to_FNChomsky;
import modelo.algoritmos.GIC_to_FNG;
import modelo.algoritmos.Registro;
import modelo.automatas.Alfabeto;
import modelo.automatas.AlfabetoPila_imp;
import modelo.automatas.Alfabeto_Pila;
import modelo.automatas.Alfabeto_imp;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFD;
import modelo.automatas.AutomataFND;
import modelo.automatas.AutomataFNDLambda;
import modelo.automatas.AutomataPila;
import modelo.ejercicios.Ejercicio;
import modelo.expresion_regular.ArbolER;
import modelo.expresion_regular.ExpresionRegular;
import modelo.expresion_regular.ExpresionRegularImpl;


/**
 * @author Rocio Barrig�ete, Mario Huete, Luis San Juan
 *
 */
public class TraductorHTML { 
	
	
private static TraductorHTML traductor;
//variables para generacion de imagenes jpg
private ArrayList<Estado> listaEstados;
private ArrayList<Arista> listaAristas;
//private ArrayList<Arista> listaAristasPila;
private ArrayList<String> listaFinales;
private String estadoInicial;
private static Stroke STROKE = new java.awt.BasicStroke(2.4f);
//fin variables para la generacion de imagenes jpg


	/**
	 * El traductorHTML es un singleton por tanto tiene una instancia de si mismo de forma estatica, lo convierte en unico
	 * @return TraductorHTML de la aplicaciñn
	 */
	public static TraductorHTML getInstancia(){
		if(traductor==null) traductor=new TraductorHTML();
		return traductor;
	}
	
	/**
	 * Traduce un automata de un fichero xml a una pagina html
	 * @param ruta xml donde se buscarñ el automata
	 * @return codigo html de la pñgina a crear
	 * @throws AutomatasException  lanza la excepciñn si hay algñn problema al
	 * abrir o encontrar el fichero xml o al parsear el automata del xml
	 */
	
	public String traducirAutomata(String ruta)throws AutomatasException  {
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
		String brr=new Character((char)92).toString();
		String rutaHTML=System.getProperty("user.dir")+brr+"HTML"+brr+"automata.html";
		//String rutaHTML="HTML/automata.html";
		File fichero = new File (rutaHTML);
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(fichero));
		
		
		bw.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
		bw.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
		bw.append("<head><meta http-equiv='content-type' content='text/html; charset=UTF-8'><link rel='stylesheet' type='text/css' href='style.css' media='screen'>");
		
		bw.append("<title>Automata</title></head><body>");
		
		NodeList tipo = documento.getElementsByTagName("type");
		
		String var = null;
		for (int i = 1; i <tipo.item(0).getChildNodes().getLength(); i++) {
			 var = tipo.item(0).getChildNodes().item(i).getTextContent();
			 i++;	 
		}
		Alfabeto_imp alf= new Alfabeto_imp();		
		ArrayList<String> estad= new ArrayList<String>();
		ArrayList<String> finalss= new ArrayList<String>();
		Automata automata=null;
		
		if (var.equals("AutomataFD")){
			
			automata = new AutomataFD();		
		}
		if (var.equals("AutomataFND")){
			
			automata = new AutomataFND();		
		}
		if (var.equals("AutomataFNDLambda")){
			
			automata = new AutomataFNDLambda();		
		}
		if (var.equals("AutomataPila")){
			
			automata = new AutomataPila();		
		}
		bw.append("<br><p>Type:"+var+"</p>");
		
		NodeList nodos = documento.getElementsByTagName("alphabet");
		
		bw.append("<br><p>"+mensajero.devuelveMensaje("automata.alfabeto",3)+"/p><ul>");
		for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
			alf.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
			 bw.append("<li>"+(nodos.item(0).getChildNodes().item(i).getTextContent()+"</li>"));
			 i++;
		}
		bw.append("</ul>");
						
		
		nodos = documento.getElementsByTagName("states");
		bw.append("<br><p>"+mensajero.devuelveMensaje("automata.estados",3)+"</p><ul>");
		for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
			estad.add(nodos.item(0).getChildNodes().item(i).getTextContent());
			bw.append("<li>"+(nodos.item(0).getChildNodes().item(i).getTextContent()+"</li>"));
			i++;
		}
		bw.append("</ul>");
		automata.setEstados(estad);
		
		nodos = documento.getElementsByTagName("init");
		bw.append("<br><p>"+mensajero.devuelveMensaje("automata.inicial",3)+nodos.item(0).getChildNodes().item(1).getTextContent()+"</p>");
		automata.setEstadoInicial(nodos.item(0).getChildNodes().item(1).getTextContent());
		
		
		
		nodos = documento.getElementsByTagName("finals");
		bw.append("<br><p>"+mensajero.devuelveMensaje("automata.finales",3)+"</p><ul>");
		for (int i = 1; i < nodos.item(0).getChildNodes().getLength(); i++) {
			bw.append("<li>"+(nodos.item(0).getChildNodes().item(i).getTextContent()+"</li>"));
			finalss.add(nodos.item(0).getChildNodes().item(i).getTextContent());
			i++;
		}
		bw.append("</ul>");
		automata.setEstadosFinales(finalss);
		bw.append("<br><h2>Automata</h2><table><tr><th>_</th>");
		nodos = documento.getElementsByTagName("arrow");
		for (int i = 0; i < nodos.getLength(); i++){
			for (int x = 1; x < nodos.item(i).getChildNodes().getLength(); x++) {
				automata.insertaArista(nodos.item(i).getChildNodes().item(x).getTextContent(), nodos.item(i).getChildNodes().item(x+2).getTextContent(), nodos.item(i).getChildNodes().item(x+4).getTextContent());
				x= x+6;
			}
		}
		
		Iterator<String> it1=alf.dameListaLetras().iterator();
		while(it1.hasNext()) {
			bw.append("<th>"+it1.next()+"</th>");
		}
		bw.append("</tr>");
		Iterator<String> it2=estad.iterator();
		while(it2.hasNext()) {
			String estado=it2.next();
			bw.append("<tr><td>"+estado+"</td>");
			it1=alf.dameListaLetras().iterator();
			while(it1.hasNext()) {
				String letra=it1.next();
				bw.append("<td>"+automata.deltaExtendida(estado, letra).toString()+"</td>");
			}
			bw.append("</tr>");
		}
		
		
		bw.append("</table></BODY></HTML>");
		bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rutaHTML;		
	}

	/**
	 * Traduce una expresion regular de un fichero xml a una pagina html
	 * @param ruta xml donde se buscarñ la expresiñn
	 * @return codigo html de la pñgina a crear
	 * @throws AutomatasException  lanza la excepciñn si hay algñn problema al
	 * abrir o encontrar el fichero xml o al parsear el automata del xml
	 */
	
	public String traducirER(String ruta)throws AutomatasException  {
		
		Mensajero mensajero=Mensajero.getInstancia();
		String expr = null;
		DOMParser parser = new DOMParser(); 
		String brr=new Character((char)92).toString();
		String rutaHTML=System.getProperty("user.dir")+brr+"HTML"+brr+"regExpr.html";
		//String rutaHTML="HTML/regExpr.html";
		File fichero = new File (rutaHTML);
		BufferedWriter bw;
		
		
		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
			Document documento = parser.getDocument();
			
			bw = new BufferedWriter(new FileWriter(fichero));
			
			
			bw.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
			bw.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
			bw.append("<head><meta http-equiv='content-type' content='text/html; charset=UTF-8'><link rel='stylesheet' type='text/css' href='style.css' media='screen'>");
			
			bw.append("<title>"+mensajero.devuelveMensaje("ERtoAFNDL.exp",3)+"</title></head><body>");
			
			
			NodeList nodos = documento.getElementsByTagName("RExpr");
			for (int i = 1; i < nodos.item(0).getChildNodes().getLength(); i++) {
				expr = nodos.item(0).getChildNodes().item(i).getTextContent();
				i++;
			}
			
			bw.append("<p>"+mensajero.devuelveMensaje("ERtoAFNDL.er",2)+expr+"</p>");
			bw.append("</table></BODY></HTML>");
			bw.close();
			
			//ExpresionRegular er=new ExpresionRegularImpl(expr);
			
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
		
		
		return rutaHTML;
		
	}
	
	/**
	 * Traduce a html los pasos y resultado del algoritmo de simplificacion de gics
	 * @param ruta xml con los pasos y  resultado del algortimo
	 * @return codigo html de la pñgina a crear
	 * @throws AutomatasException  lanza la excepciñn si hay algñn problema al
	 * abrir o encontrar el fichero xml o al parsear
	 */
	
	public String traducirPasosSimplificacion(String ruta)throws AutomatasException  {
		
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser(); 
		String brr=new Character((char)92).toString();
		String rutaHTML=System.getProperty("user.dir")+brr+"HTML"+brr+"saleSimplificacion.html";
		File fichero = new File (rutaHTML);
		BufferedWriter bw;
		try{
			
			parser.parse(new InputSource(new FileInputStream(ruta)));
			Document documento = parser.getDocument();
			
			bw = new BufferedWriter(new FileWriter(fichero));
			
			
			bw.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
			bw.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
			bw.append("<head><meta http-equiv='content-type' content='text/html; charset=UTF-8'><link rel='stylesheet' type='text/css' href='style.css' media='screen'>");
			
			bw.append("<title>"+mensajero.devuelveMensaje("simplificacion.title",3)+"</title></head><body>");
			bw.append("<div id='cabecera'><img src='logo3.gif'></div>");
			
			
			bw.append("<div id='resultado'>");
			NodeList tipo = documento.getElementsByTagName("type");
			
			String var = null;
			for (int i = 1; i <tipo.item(0).getChildNodes().getLength(); i++) {
				 var = tipo.item(0).getChildNodes().item(i).getTextContent();
				 i++;	 
			}
			Alfabeto_imp alf= new Alfabeto_imp();
			
			ArrayList<String> estad= new ArrayList<String>();
			ArrayList<String> finalss= new ArrayList<String>();
			Alfabeto_Pila alfabetoPila = new AlfabetoPila_imp();
			AutomataPila automata=new AutomataPila();
			

			bw.append("<br><p>Type:"+var+"</p>");
			
			NodeList nodos = documento.getElementsByTagName("alphabet");
			
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.alfabeto",3));
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				alf.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
				 i++;
			}
			bw.append(alf.getListaLetras().toString()+"</p>");
			
			nodos = documento.getElementsByTagName("alphabetP");
			
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.alfabeto",3));
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				alfabetoPila.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
				 i++;
			}
				
			bw.append(alfabetoPila.getListaLetras().toString()+"</p>");
			
			nodos = documento.getElementsByTagName("states");
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.estados",3));
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				estad.add(nodos.item(0).getChildNodes().item(i).getTextContent());
				i++;
			}
			bw.append(estad.toString()+"</p>");
			automata.setEstados(estad);
			
			nodos = documento.getElementsByTagName("init");
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.inicial",3)+nodos.item(0).getChildNodes().item(1).getTextContent()+"</p>");
			automata.setEstadoInicial(nodos.item(0).getChildNodes().item(1).getTextContent());
			
			nodos = documento.getElementsByTagName("finals");
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.finales",3));
			for (int i = 1; i < nodos.item(0).getChildNodes().getLength(); i++) {
				finalss.add(nodos.item(0).getChildNodes().item(i).getTextContent());
				i++;
			}
			bw.append(finalss.toString()+"</p>");
			automata.setEstadosFinales(finalss);
			bw.append("<br><h2>Aristas</h2><table><tr>"/*<th>"+new Character((char)95).toString()+"</th>"*/);

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
						
						
						
						//StringTokenizer st=new StringTokenizer(nomArs.getText(),",");
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
						//insertaArista(String origen,String destino,ArrayList<String> simbolos,String cima,ArrayList<String> salida)
						((AutomataPila)automata).insertaArista2(s1,s2,entrada,s4,salida);				
					}

					
					
				}
			
			
/*			Iterator<String> it1=alf.dameListaLetras().iterator();
			while(it1.hasNext()) {
				bw.append("<th>"+it1.next()+"</th>");
			}
			bw.append("</tr>");
	//		Iterator<String> it2=estad.iterator();
	/*		while(it2.hasNext()) {
				String estado=it2.next();
				bw.append("<tr><td>"+estado+"</td>");
				it1=alf.dameListaLetras().iterator();

				bw.append("</tr>");
			}*/
			//////////////
	//		Iterator<String> itP1=alfabetoPila.dameListaLetras().iterator();
	/*		while(itP1.hasNext()) {
				bw.append("<th>"+itP1.next()+"</th>");
			}*/
/*			bw.append("</tr>");
			Iterator<String> itP2=estad.iterator();
			while(itP2.hasNext()) {
				String estado=itP2.next();
				bw.append("<tr><td>"+estado+"</td>");
				itP1=alf.dameListaLetras().iterator();
				while(itP1.hasNext()) {
					String letra=itP1.next();
					ArrayList<String> delta = automata.getAristasLetra(estado,letra);
					if (delta != null)//automata.existeArista(estado,letra))//.deltaExtendida(estado, letra).contains(null))
						bw.append("<td>"+/*((AutomataPila)automata) .deltaExtendida(estado, letra)*//*delta.toString()+"</td>");
/*					else bw.append("<td></td>");
				}
				bw.append("</tr>");
			}*/
			Iterator<AristaAP> itArsAP = automata.getAutomataPila().iterator();
	//		bw.append("<tr>");
			while(itArsAP.hasNext()){
				
				AristaAP aris = itArsAP.next();
				bw.append("<tr><td>" + aris.toString() + "</td></tr>");
			}
			//bw.append("</tr>");
			
			bw.append("</table></div>");
			bw.append("<div id='authomata'>");
			
			//GENERACIñN DE LOS PASOS DE SIMPLIFICACION
			
			bw.append("<p>"+mensajero.devuelveMensaje("minimizacion.input",3)+"</p><img src='imagenEntrada.jpg' alt='Input'></p>");
//			bw.append("</table><p>"+mensajero.devuelveMensaje("minimizacion.title",3)+"</p><p><img src='imagen.jpg' alt='Output'></p></div>");
	
			//trataMensaje(mensajero.devuelveMensaje("minimizacion.iniciar",2));
			
			AutomataP_to_GramaticaIC agic = new AutomataP_to_GramaticaIC(automata);
			//agic.AP_Gramatica();
			System.out.println("AGIC getgic: " + agic.getGic());
			GIC_to_FNG gictofnc = new GIC_to_FNG(agic.getGic(),true);
			
		//	gictofnc.registraControlador(this);
			gictofnc.simplifica(true,false);
			

			System.out.println("dame html: " + gictofnc.getHTML());
			System.out.println("dame latex:" + gictofnc.getLatex());
			bw.append(gictofnc.getHTML());
			bw.append("</body></html>");
			bw.close();

			
		
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
	
	
	return rutaHTML;
		
	}
	
	
	public String traducirPasosSimplificacionFNC(String ruta)throws AutomatasException  {
		
		System.out.println("RUTA PASOSFNC: " + ruta);
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser(); 
		String brr=new Character((char)92).toString();
		String rutaHTML=System.getProperty("user.dir")+brr+"HTML"+brr+"saleSimplificacionFNC.html";
		File fichero = new File (rutaHTML);
		BufferedWriter bw;
		try{
			
			parser.parse(new InputSource(new FileInputStream(ruta)));
			Document documento = parser.getDocument();
			
			bw = new BufferedWriter(new FileWriter(fichero));
			
			
			bw.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
			bw.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
			bw.append("<head><meta http-equiv='content-type' content='text/html; charset=UTF-8'><link rel='stylesheet' type='text/css' href='style.css' media='screen'>");
			
			bw.append("<title>"+mensajero.devuelveMensaje("simplificacion.title",3)+"</title></head><body>");
			bw.append("<div id='cabecera'><img src='logo3.gif'></div>");
			
			
			bw.append("<div id='resultado'>");
			NodeList tipo = documento.getElementsByTagName("type");
			
			String var = null;
			for (int i = 1; i <tipo.item(0).getChildNodes().getLength(); i++) {
				 var = tipo.item(0).getChildNodes().item(i).getTextContent();
				 i++;	 
			}
			Alfabeto_imp alf= new Alfabeto_imp();
			
			ArrayList<String> estad= new ArrayList<String>();
			ArrayList<String> finalss= new ArrayList<String>();
			Alfabeto_Pila alfabetoPila = new AlfabetoPila_imp();
			AutomataPila automata=new AutomataPila();
			

			bw.append("<br><p>Type:"+var+"</p>");
			
			NodeList nodos = documento.getElementsByTagName("alphabet");
			
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.alfabeto",3));
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				alf.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
				 i++;
			}
			bw.append(alf.getListaLetras().toString()+"</p>");
			
			nodos = documento.getElementsByTagName("alphabetP");
			
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.alfabeto",3));
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				alfabetoPila.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
				 i++;
			}
				
			bw.append(alfabetoPila.getListaLetras().toString()+"</p>");
			
			nodos = documento.getElementsByTagName("states");
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.estados",3));
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				estad.add(nodos.item(0).getChildNodes().item(i).getTextContent());
				i++;
			}
			bw.append(estad.toString()+"</p>");
			automata.setEstados(estad);
			
			nodos = documento.getElementsByTagName("init");
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.inicial",3)+nodos.item(0).getChildNodes().item(1).getTextContent()+"</p>");
			automata.setEstadoInicial(nodos.item(0).getChildNodes().item(1).getTextContent());
			
			nodos = documento.getElementsByTagName("finals");
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.finales",3));
			for (int i = 1; i < nodos.item(0).getChildNodes().getLength(); i++) {
				finalss.add(nodos.item(0).getChildNodes().item(i).getTextContent());
				i++;
			}
			bw.append(finalss.toString()+"</p>");
			automata.setEstadosFinales(finalss);
			bw.append("<br><h2>Aristas</h2><table><tr>"/*<th>"+new Character((char)95).toString()+"</th>"*/);

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
						
						
						
						//StringTokenizer st=new StringTokenizer(nomArs.getText(),",");
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
						//insertaArista(String origen,String destino,ArrayList<String> simbolos,String cima,ArrayList<String> salida)
						((AutomataPila)automata).insertaArista2(s1,s2,entrada,s4,salida);				
					}

					
					
				}
			
			
/*			Iterator<String> it1=alf.dameListaLetras().iterator();
			while(it1.hasNext()) {
				bw.append("<th>"+it1.next()+"</th>");
			}
			bw.append("</tr>");
	//		Iterator<String> it2=estad.iterator();
	/*		while(it2.hasNext()) {
				String estado=it2.next();
				bw.append("<tr><td>"+estado+"</td>");
				it1=alf.dameListaLetras().iterator();

				bw.append("</tr>");
			}*/
			//////////////
	//		Iterator<String> itP1=alfabetoPila.dameListaLetras().iterator();
	/*		while(itP1.hasNext()) {
				bw.append("<th>"+itP1.next()+"</th>");
			}*/
/*			bw.append("</tr>");
			Iterator<String> itP2=estad.iterator();
			while(itP2.hasNext()) {
				String estado=itP2.next();
				bw.append("<tr><td>"+estado+"</td>");
				itP1=alf.dameListaLetras().iterator();
				while(itP1.hasNext()) {
					String letra=itP1.next();
					ArrayList<String> delta = automata.getAristasLetra(estado,letra);
					if (delta != null)//automata.existeArista(estado,letra))//.deltaExtendida(estado, letra).contains(null))
						bw.append("<td>"+/*((AutomataPila)automata) .deltaExtendida(estado, letra)*//*delta.toString()+"</td>");
/*					else bw.append("<td></td>");
				}
				bw.append("</tr>");
			}*/
			Iterator<AristaAP> itArsAP = automata.getAutomataPila().iterator();
	//		bw.append("<tr>");
			while(itArsAP.hasNext()){
				
				AristaAP aris = itArsAP.next();
				bw.append("<tr><td>" + aris.toString() + "</td></tr>");
			}
			//bw.append("</tr>");
			
			bw.append("</table></div>");
			bw.append("<div id='authomata'>");
			
			//GENERACIñN DE LOS PASOS DE SIMPLIFICACION
			
			bw.append("<p>"+mensajero.devuelveMensaje("minimizacion.input",3)+"</p><img src='imagenEntrada.jpg' alt='Input'></p>");
//			bw.append("</table><p>"+mensajero.devuelveMensaje("minimizacion.title",3)+"</p><p><img src='imagen.jpg' alt='Output'></p></div>");
	
			//trataMensaje(mensajero.devuelveMensaje("minimizacion.iniciar",2));
			
			AutomataP_to_GramaticaIC agic = new AutomataP_to_GramaticaIC(automata);
			//agic.AP_Gramatica();
			System.out.println("AGIC getgic: " + agic.getGic());
			GIC_to_FNChomsky gictofnc = new GIC_to_FNChomsky(agic.getGic(),true);
			
		//	gictofnc.registraControlador(this);
		//	gictofnc.simplifica(true,false);
			

			System.out.println("dame html: " + gictofnc.getHTML());
			//System.out.println("dame latex:" + gictofnc.getLatex());
			bw.append(gictofnc.getHTML());
			bw.append("</body></html>");
			bw.close();

			
		
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
	
	
	return rutaHTML;
		
	}
/**
 * Traduce a html los pasos y resultado del algoritmo de minimizacion
 * @param ruta xml con los pasos y  resultado del algortimo
 * @return codigo html de la pñgina a crear
 * @throws AutomatasException  lanza la excepciñn si hay algñn problema al
 * abrir o encontrar el fichero xml o al parsear
 */
	
@SuppressWarnings("unchecked")
public String traducirPasosMinimizacion(String ruta)throws AutomatasException  {
		
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser(); 
		String brr=new Character((char)92).toString();
		String rutaHTML=System.getProperty("user.dir")+brr+"HTML"+brr+"saleMinimizacion.html";
		File fichero = new File (rutaHTML);
		BufferedWriter bw;
		
		
		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
			Document documento = parser.getDocument();
			
			bw = new BufferedWriter(new FileWriter(fichero));
			
			
			bw.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
			bw.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
			bw.append("<head><meta http-equiv='content-type' content='text/html; charset=UTF-8'><link rel='stylesheet' type='text/css' href='style.css' media='screen'>");
			
			bw.append("<title>"+mensajero.devuelveMensaje("minimizacion.title",3)+"</title></head><body>");
			bw.append("<div id='cabecera'><img src='logo3.gif'></div>");
			
		
		//RESULTADO DEL ALGORITMO	(AUTOMATA DE SALIDA!!)
			bw.append("<div id='resultado'>");
			NodeList tipo = documento.getElementsByTagName("type");
			
			String var = null;
			for (int i = 1; i <tipo.item(0).getChildNodes().getLength(); i++) {
				 var = tipo.item(0).getChildNodes().item(i).getTextContent();
				 i++;	 
			}
			Alfabeto_imp alf= new Alfabeto_imp();		
			ArrayList<String> estad= new ArrayList<String>();
			ArrayList<String> finalss= new ArrayList<String>();
			Automata automata=null;
			
			if (var.equals("AutomataFD")){
				
				automata = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){
				
				automata = new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){
				
				automata = new AutomataFNDLambda();		
			}
			bw.append("<br><p>Type:"+var+"</p>");
			
			NodeList nodos = documento.getElementsByTagName("alphabet");
			
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.alfabeto",3));
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				alf.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
				 i++;
			}
			bw.append(alf.getListaLetras().toString()+"</p>");
							
			
			nodos = documento.getElementsByTagName("states");
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.estados",3));
			for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
				estad.add(nodos.item(0).getChildNodes().item(i).getTextContent());
				i++;
			}
			bw.append(estad.toString()+"</p>");
			automata.setEstados(estad);
			
			nodos = documento.getElementsByTagName("init");
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.inicial",3)+nodos.item(0).getChildNodes().item(1).getTextContent()+"</p>");
			automata.setEstadoInicial(nodos.item(0).getChildNodes().item(1).getTextContent());
			
			nodos = documento.getElementsByTagName("finals");
			bw.append("<br><p>"+mensajero.devuelveMensaje("automata.finales",3));
			for (int i = 1; i < nodos.item(0).getChildNodes().getLength(); i++) {
				finalss.add(nodos.item(0).getChildNodes().item(i).getTextContent());
				i++;
			}
			bw.append(finalss.toString()+"</p>");
			automata.setEstadosFinales(finalss);
			bw.append("<br><h2>Automata</h2><table><tr><th>"+new Character((char)95).toString()+"</th>");
			nodos = documento.getElementsByTagName("arrow");
			for (int i = 0; i < nodos.getLength(); i++){
				for (int x = 1; x < nodos.item(i).getChildNodes().getLength(); x++) {
					automata.insertaArista(nodos.item(i).getChildNodes().item(x).getTextContent(), nodos.item(i).getChildNodes().item(x+2).getTextContent(), nodos.item(i).getChildNodes().item(x+4).getTextContent());
					x= x+6;
				}
			}
			
			Iterator<String> it1=alf.dameListaLetras().iterator();
			while(it1.hasNext()) {
				bw.append("<th>"+it1.next()+"</th>");
			}
			bw.append("</tr>");
			Iterator<String> it2=estad.iterator();
			while(it2.hasNext()) {
				String estado=it2.next();
				bw.append("<tr><td>"+estado+"</td>");
				it1=alf.dameListaLetras().iterator();
				while(it1.hasNext()) {
					String letra=it1.next();
					if (!automata.deltaExtendida(estado, letra).contains(null))
						bw.append("<td>"+automata.deltaExtendida(estado, letra).toString()+"</td>");
					else bw.append("<td></td>");
				}
				bw.append("</tr>");
			}
			
			bw.append("</table></div>");
			bw.append("<div id='authomata'>");
			
			//GENERACIñN DE LOS PASOS DE MINIMIZACION
			bw.append("<p>"+mensajero.devuelveMensaje("minimizacion.input",3)+"</p><img src='imagenEntrada.jpg' alt='Input'></p>");
			HashMap<String,HashMap<String,Registro>> tabla=new HashMap<String,HashMap<String,Registro>>();
			
			NodeList nodos1 = documento.getElementsByTagName("table");
			bw.append("<p>"+mensajero.devuelveMensaje("minimizacion.steps",3)+"</p><table>");
			for (int i = 0; i < nodos1.item(0).getChildNodes().getLength(); i++) {
				NodeList nodos2=nodos1.item(0).getChildNodes().item(i).getChildNodes();
				HashMap<String,Registro> hs=new HashMap<String,Registro>();
				for(int j=0;j<nodos2.getLength();j++) {
					if (nodos2.item(j).getChildNodes().item(1)!=null) {
						Registro r = new Registro("0",true,"ninguno");
						if (!(nodos2.item(j).getChildNodes().item(1).getTextContent().equals("null"))) {
							if (nodos2.item(j).getChildNodes().item(1).getTextContent().equals("false")) {
								r.setMarcado(false);
								r.setEstados(nodos2.item(j).getChildNodes().item(3).getTextContent());
								r.setPaso(nodos2.item(j).getChildNodes().item(2).getTextContent());
								hs.put(nodos2.item(j).getChildNodes().item(0).getTextContent(),r);
							}
							else {
								hs.put(nodos2.item(j).getChildNodes().item(0).getTextContent(),r);
							}
						}
					}
				}
				tabla.put(nodos2.item(0).getTextContent(), hs);
				}
			//ordenar lista estados hashmap!!!!!!  
			ArrayList<String> listaOrdenada=new ArrayList<String>();
			Iterator<String> itKeys=tabla.keySet().iterator();
			if (itKeys.hasNext()) {
				String primera=itKeys.next();
				listaOrdenada.add(primera);
			}
			while(itKeys.hasNext()) {
				String clave=itKeys.next();
				int j=0;
				boolean listo=false;
				ArrayList<String> listaOrdenadaAux=(ArrayList<String>) listaOrdenada.clone();
				while(j<listaOrdenada.size()&&(!listo)) {
					String actual=listaOrdenada.get(j);
					int actualVal=tabla.get(actual).keySet().size();
					int claveVal=tabla.get(clave).keySet().size();
					if (claveVal>actualVal) {
						listaOrdenadaAux.add(j, clave);
						listo=true;
					}
					j++;
				}
				listaOrdenada=listaOrdenadaAux;
				if (!listo) listaOrdenada.add(clave);
			}
			
			
			Iterator<String> itEst=listaOrdenada.iterator();
			ArrayList<String> lEstadosAux=(ArrayList<String>) listaOrdenada.clone();
			if (itEst.hasNext()) itEst.next();
			while(itEst.hasNext()) {
				String estado=itEst.next();
				bw.append("<tr><th>"+estado.subSequence(0, estado.length()-1)+"</th>");
				Iterator<String> itEst2=lEstadosAux.iterator();
				while(itEst2.hasNext()) {
					String val=itEst2.next();
					HashMap<String,Registro> ths=tabla.get(val);
					if (ths.get(estado)!=null) {
						if (ths.get(estado).getMarcado()) bw.append("<td> </td>");
						else bw.append("<td>"+ths.get(estado).getPaso() +" "+
								ths.get(estado).getEstados() + "</td>");
					}
				}
				bw.append("</tr>");
			}
			itEst=listaOrdenada.iterator();
			bw.append("<tr><td></td>");
			while(itEst.hasNext()) {
				String estado=itEst.next();
				if (itEst.hasNext())
					bw.append("<td>"+estado.subSequence(0, estado.length()-1)+"</td>");	
			}
			bw.append("</tr>");
			//sacar salida html
			bw.append("</table><p>"+mensajero.devuelveMensaje("minimizacion.title",3)+"</p><p><img src='imagen.jpg' alt='Output'></p></div>");
			bw.append("</body></html>");
			bw.close();
			
			//ExpresionRegular er=new ExpresionRegularImpl(expr);
			
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
		
		
		return rutaHTML;
		
	}


/**
 * Traduce el resultado y los pasos del algoritmo de equivalencia
 * @param ruta xml con los pasos y  resultado del algortimo
 * @return codigo html de la pñgina a crear
 * @throws AutomatasException  lanza la excepciñn si hay algñn problema al
 * abrir o encontrar el fichero xml o al parsear
 */
@SuppressWarnings("unchecked")
public String traducirPasosEquivalencia(String ruta)throws AutomatasException  {
	
	Mensajero mensajero=Mensajero.getInstancia();
	DOMParser parser = new DOMParser(); 
	String brr=new Character((char)92).toString();
	String rutaHTML=System.getProperty("user.dir")+brr+"HTML"+brr+"saleEquivalencia.html";
	//String rutaHTML="HTML/saleEquivalencia.html";
	File fichero = new File (rutaHTML);
	BufferedWriter bw;
	
	try {
		parser.parse(new InputSource(new FileInputStream(ruta)));
		Document documento = parser.getDocument();
		
		bw = new BufferedWriter(new FileWriter(fichero));
		
		
		bw.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
		bw.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
		bw.append("<head><meta http-equiv='content-type' content='text/html; charset=UTF-8'><link rel='stylesheet' type='text/css' href='style.css' media='screen'>");
		
		bw.append("<title>"+mensajero.devuelveMensaje("equivalencia.title",3)+"</title><body>");
		bw.append("<div id='cabecera'><img src='logo3.gif'></div>");
		
		bw.append("<div id='resultado'><p>Output:</p>");
		NodeList tipo = documento.getElementsByTagName("result");
		if (tipo.item(0).getTextContent().equals("no")) {
			bw.append("<h2>"+mensajero.devuelveMensaje("equivalencia.no",3)+"</h2>");
		}
		else {
			bw.append("<h2>"+mensajero.devuelveMensaje("equivalencia.si",3)+"</h2>");
		}
		bw.append("</div>");
		bw.append("<div id='authomata'>");
		
		//GENERACIñN DE LOS PASOS DE MINIMIZACION
		
		HashMap<String,HashMap<String,Registro>> tabla=new HashMap<String,HashMap<String,Registro>>();
		
		NodeList nodos1 = documento.getElementsByTagName("table");
		bw.append("<p>"+mensajero.devuelveMensaje("equivalencia.steps",3)+"</p><table>");
		for (int i = 0; i < nodos1.item(0).getChildNodes().getLength(); i++) {
			NodeList nodos2=nodos1.item(0).getChildNodes().item(i).getChildNodes();
			HashMap<String,Registro> hs=new HashMap<String,Registro>();
			for(int j=0;j<nodos2.getLength();j++) {
				if (nodos2.item(j).getChildNodes().item(1)!=null) {
					Registro r = new Registro("0",true,"ninguno");
					if (!(nodos2.item(j).getChildNodes().item(1).getTextContent().equals("null"))) {
						if (nodos2.item(j).getChildNodes().item(1).getTextContent().equals("false")) {
							r.setEstados(nodos2.item(j).getChildNodes().item(1).getTextContent());
							r.setMarcado(false);
							r.setPaso(nodos2.item(j).getChildNodes().item(2).getTextContent());
							hs.put(nodos2.item(j).getChildNodes().item(0).getTextContent(),r);
						}
						else {
							hs.put(nodos2.item(j).getChildNodes().item(0).getTextContent(),r);
						}
					}
				}
			}
			tabla.put(nodos2.item(0).getTextContent(), hs);
			}
		
		//ordenar lista estados hashmap!!!!!!
		ArrayList<String> listaOrdenada=new ArrayList<String>();
		Iterator<String> itKeys=tabla.keySet().iterator();
		if (itKeys.hasNext()) {
			String primera=itKeys.next();
			listaOrdenada.add(primera);
		}
		while(itKeys.hasNext()) {
			String clave=itKeys.next();
			int j=0;
			boolean listo=false;
			ArrayList<String> listaOrdenadaAux=(ArrayList<String>)listaOrdenada.clone();
			while(j<listaOrdenada.size()&&(!listo)) {
				String actual=listaOrdenada.get(j);
				int actualVal=tabla.get(actual).keySet().size();
				int claveVal=tabla.get(clave).keySet().size();
				if (claveVal>actualVal) {
					listaOrdenadaAux.add(j, clave);
					listo=true;
				}
				j++;
			}
			listaOrdenada=listaOrdenadaAux;
			if (!listo) listaOrdenada.add(clave);
		}

		Iterator<String> itEst=listaOrdenada.iterator();
		ArrayList<String> lEstadosAux=(ArrayList<String>)listaOrdenada.clone();
		if (itEst.hasNext()) itEst.next();
		while(itEst.hasNext()) {
			String estado=itEst.next();
			bw.append("<tr><th>"+estado.replace("_$", "")+"</th>");
			Iterator<String> itEst2=lEstadosAux.iterator();
			while(itEst2.hasNext()) {
				String val=itEst2.next();
				HashMap<String,Registro> ths=tabla.get(val);
				if (ths.get(estado)!=null) {
					if (ths.get(estado).getMarcado()) bw.append("<td>  </td>");
					else bw.append("<td>"+ths.get(estado).getPaso()+" "+ths.get(estado).getEstados()+"</td>");
				}
			}
			bw.append("</tr>");
		}
		itEst=listaOrdenada.iterator();
		bw.append("<tr><td></td>");
		while(itEst.hasNext()) {
			String estado=itEst.next();
			if (itEst.hasNext())
				bw.append("<td>"+estado.replace("_$", "")+"</td>");	
		}
		bw.append("</tr>");
		//sacar salida html
		bw.append("</table><p>");
		bw.append(mensajero.devuelveMensaje("equivalencia.input",3)+"</p><p><img src='imagenEQV1.jpg' alt='Input'></p><p>");
		bw.append(mensajero.devuelveMensaje("equivalencia.output",3)+"</p><p><img src='imagen.jpg' alt='Output'></p></div>");
		bw.append("</div></body></html>");
		bw.close();
		
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
	
	
	return rutaHTML;
	
}

/**
 * Traduce los pasos de ejecucion y resultado del algoritmo AFN->AFD
 * @param ruta xml con los pasos y  resultado del algortimo
 * @return codigo html de la pñgina a crear
 * @throws AutomatasException  lanza la excepciñn si hay algñn problema al
 * abrir o encontrar el fichero xml o al parsear
 */
public String traducirPasosAFND_AFD(String ruta)throws AutomatasException  {
	
	Mensajero mensajero=Mensajero.getInstancia();
	DOMParser parser = new DOMParser(); 
	String brr=new Character((char)92).toString();
	String rutaHTML=System.getProperty("user.dir")+brr+"HTML"+brr+"saleAFNDAFD.html";
	//String rutaHTML="HTML/saleAFNDAFD.html";
	File fichero = new File (rutaHTML);
	BufferedWriter bw;
	
	try {
		parser.parse(new InputSource(new FileInputStream(ruta)));
		Document documento = parser.getDocument();
		
		bw = new BufferedWriter(new FileWriter(fichero));
		
		
		bw.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
		bw.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
		bw.append("<head><meta http-equiv='content-type' content='text/html; charset=UTF-8'><link rel='stylesheet' type='text/css' href='style.css' media='screen'>");
		
		bw.append("<title>"+mensajero.devuelveMensaje("AFNTOAFD.title",3)+"</title><body>");
		bw.append("<div id='cabecera'><img src='logo3.gif'></div>");
		
		
		
		//RESULTADO DEL ALGORITMO	(AUTOMATA DE SALIDA!!)
		bw.append("<div id='resultado'>");
		NodeList tipo = documento.getElementsByTagName("type");
		
		String var = null;
		for (int i = 1; i <tipo.item(0).getChildNodes().getLength(); i++) {
			 var = tipo.item(0).getChildNodes().item(i).getTextContent();
			 i++;	 
		}
		Alfabeto_imp alf= new Alfabeto_imp();		
		ArrayList<String> estad= new ArrayList<String>();
		ArrayList<String> finalss= new ArrayList<String>();
		Automata automata=null;
		
		if (var.equals("AutomataFD")){
			
			automata = new AutomataFD();		
		}
		if (var.equals("AutomataFND")){
			
			automata = new AutomataFND();		
		}
		if (var.equals("AutomataFNDLambda")){
			
			automata = new AutomataFNDLambda();		
		}
		
		bw.append("<br><p>"+mensajero.devuelveMensaje("automata.tipo",3)+var+"</p>");
		
		NodeList nodos = documento.getElementsByTagName("alphabet");
		
		bw.append("<br><p>"+mensajero.devuelveMensaje("automata.alfabeto",3));
		for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
			alf.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
			 i++;
		}
		bw.append(alf.getListaLetras().toString()+"</p>");
						
		automata.setAlfabeto(alf);
		
		nodos = documento.getElementsByTagName("states");
		bw.append("<br><p>"+mensajero.devuelveMensaje("automata.estados",3));
		for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
			estad.add(nodos.item(0).getChildNodes().item(i).getTextContent());
			i++;
		}
		bw.append(estad.toString()+"</p>");
		automata.setEstados(estad);
		
		//automata.setEstados(estad);
		
		nodos = documento.getElementsByTagName("init");
		bw.append("<br><p>"+mensajero.devuelveMensaje("automata.inicial",3)+nodos.item(0).getChildNodes().item(1).getTextContent()+"</p>");
		automata.setEstadoInicial(nodos.item(0).getChildNodes().item(1).getTextContent());
		
		nodos = documento.getElementsByTagName("finals");
		bw.append("<br><p>"+mensajero.devuelveMensaje("automata.finales",3));
		for (int i = 1; i < nodos.item(0).getChildNodes().getLength(); i++) {
			finalss.add(nodos.item(0).getChildNodes().item(i).getTextContent());
			i++;
		}
		bw.append(finalss.toString()+"</p>");
		automata.setEstadosFinales(finalss);
		bw.append("<br><h2>Automata</h2><table><tr><th>"+new Character((char)95).toString()+"</th>");
		nodos = documento.getElementsByTagName("arrow");
		for (int i = 0; i < nodos.getLength(); i++){
			for (int x = 1; x < nodos.item(i).getChildNodes().getLength(); x++) {
				automata.insertaArista(nodos.item(i).getChildNodes().item(x).getTextContent(), nodos.item(i).getChildNodes().item(x+2).getTextContent(), nodos.item(i).getChildNodes().item(x+4).getTextContent());
				x= x+6;
			}
		}
		
		Iterator<String> it1=alf.dameListaLetras().iterator();
		while(it1.hasNext()) {
			bw.append("<th>"+it1.next()+"</th>");
		}
		bw.append("</tr>");
		Iterator<String> it2=estad.iterator();
		while(it2.hasNext()) {
			String estado=it2.next();
			bw.append("<tr><td>"+estado+"</td>");
			it1=alf.dameListaLetras().iterator();
			while(it1.hasNext()) {
				String letra=it1.next();
				if (!automata.deltaExtendida(estado, letra).contains(null))
					bw.append("<td>"+automata.deltaExtendida(estado, letra).toString()+"</td>");
				else bw.append("<td></td>");
			}
			bw.append("</tr>");
		}
		
		bw.append("</table></div>");
		
	
		//GENERACION DE LA TABLA DE TRADUCCION
	
		NodeList nodos1 = documento.getElementsByTagName("flecha");
		bw.append("<div id='authomata'>");
		bw.append("<p>"+mensajero.devuelveMensaje("automata.entrada",3)+"</p><img src='imagenEntrada.jpg' alt='Input'></p>");
		bw.append("<p>"+mensajero.devuelveMensaje("automata.pasos",3)+"</p><table>");
		Iterator<String> itAlf=alf.dameListaLetras().iterator();
		bw.append("<tr><th></th>");
		while(itAlf.hasNext()) {
			String letra=itAlf.next();
			bw.append("<td>"+letra+"</td>");
		}
		bw.append("</tr>");
		for(int i=0;i<nodos1.getLength();i++) {
			bw.append("<tr><th>"+nodos1.item(i).getChildNodes().item(0).getTextContent()+"</th>");
			for(int j=0;j<nodos1.item(i).getChildNodes().getLength();j+=2) {
				if (j>0) {
					if (nodos1.item(i).getChildNodes().item(j).getTextContent().equals("null"))
						bw.append("<td>[ ]</td>");
					else
						bw.append("<td>"+nodos1.item(i).getChildNodes().item(j).getTextContent()+"</td>");
				}
			}
			bw.append("</tr>");
		}
		bw.append("</table>");
		
		bw.append("<p>"+mensajero.devuelveMensaje("automata.salida",3)+"</p><p><img src='imagen.jpg' alt='Output'></p>");
		bw.append("</div></body></html>");
		bw.close();
		
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
	
	return rutaHTML;
	
}

/**
 * Traduce los pasos y resultado del algoritmo AFNDL->AFND
 * @param ruta xml con los pasos y  resultado del algortimo
 * @return codigo html de la pñgina a crear
 * @throws AutomatasException  lanza la excepciñn si hay algñn problema al
 * abrir o encontrar el fichero xml o al parsear
 */
public String traducirPasosAFNDL_AFND(String ruta)throws AutomatasException  {
	Mensajero mensajero=Mensajero.getInstancia();
	DOMParser parser = new DOMParser(); 
	String brr=new Character((char)92).toString();
	String rutaHTML=System.getProperty("user.dir")+brr+"HTML"+brr+"saleAFNDLtoAFND.html";
	//String rutaHTML="HTML/saleAFNDLtoAFND.html";
	File fichero = new File (rutaHTML);
	BufferedWriter bw;

	try {
		parser.parse(new InputSource(new FileInputStream(ruta)));
		Document documento = parser.getDocument();
		
		bw = new BufferedWriter(new FileWriter(fichero));
		
		
		bw.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
		bw.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
		bw.append("<head><meta http-equiv='content-type' content='text/html; charset=UTF-8'><link rel='stylesheet' type='text/css' href='style.css' media='screen'>");
		
		bw.append("<title>"+mensajero.devuelveMensaje("AFNLTOAFN.title", 3)+"</title><body>");
		bw.append("<div id='cabecera'><img src='logo3.gif'></div>");
		
		
		
		//RESULTADO DEL ALGORITMO	(AUTOMATA DE SALIDA!!)
		bw.append("<div id='resultado'>");
		NodeList tipo = documento.getElementsByTagName("type");
		
		String var = null;
		for (int i = 1; i <tipo.item(0).getChildNodes().getLength(); i++) {
			 var = tipo.item(0).getChildNodes().item(i).getTextContent();
			 i++;	 
		}
		Alfabeto_imp alf= new Alfabeto_imp();		
		ArrayList<String> estad= new ArrayList<String>();
		ArrayList<String> finalss= new ArrayList<String>();
		Automata automata=null;
		
		if (var.equals("AutomataFD")){
			
			automata = new AutomataFD();		
		}
		if (var.equals("AutomataFND")){
			
			automata = new AutomataFND();		
		}
		if (var.equals("AutomataFNDLambda")){
			
			automata = new AutomataFNDLambda();		
		}
		
		bw.append("<br><p>Type:"+var+"</p>");
		
		NodeList nodos = documento.getElementsByTagName("alphabet");
		
		bw.append("<br><p>Alphabet: ");
		for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
			alf.aniadirLetra(nodos.item(0).getChildNodes().item(i).getTextContent());
			i++;
		}
		bw.append(alf.getListaLetras().toString()+"</p>");
						
		automata.setAlfabeto(alf);
		
		nodos = documento.getElementsByTagName("states");
		bw.append("<br><p>States: ");
		for (int i = 1; i <nodos.item(0).getChildNodes().getLength(); i++) {
			estad.add(nodos.item(0).getChildNodes().item(i).getTextContent());
			i++;
		}
		bw.append(estad.toString()+"</p>");
		automata.setEstados(estad);
		
		//automata.setEstados(estad);
		
		nodos = documento.getElementsByTagName("init");
		bw.append("<br><p>Initial State: "+nodos.item(0).getChildNodes().item(1).getTextContent()+"</p>");
		automata.setEstadoInicial(nodos.item(0).getChildNodes().item(1).getTextContent());
		
		nodos = documento.getElementsByTagName("finals");
		bw.append("<br><p>Final States: ");
		for (int i = 1; i < nodos.item(0).getChildNodes().getLength(); i++) {
			finalss.add(nodos.item(0).getChildNodes().item(i).getTextContent());
			i++;
		}
		bw.append(finalss.toString()+"</p>");
		automata.setEstadosFinales(finalss);
		bw.append("<br><h2>Automata</h2><table><tr><th>"+new Character((char)95).toString()+"</th>");
		nodos = documento.getElementsByTagName("arrow");
		for (int i = 0; i < nodos.getLength(); i++){
			for (int x = 1; x < nodos.item(i).getChildNodes().getLength(); x++) {
				automata.insertaArista(nodos.item(i).getChildNodes().item(x).getTextContent(), nodos.item(i).getChildNodes().item(x+2).getTextContent(), nodos.item(i).getChildNodes().item(x+4).getTextContent());
				x= x+6;
			}
		}
		
		Iterator<String> it1=alf.dameListaLetras().iterator();
		while(it1.hasNext()) {
			bw.append("<th>"+it1.next()+"</th>");
		}
		bw.append("</tr>");
		Iterator<String> it2=estad.iterator();
		while(it2.hasNext()) {
			String estado=it2.next();
			bw.append("<tr><td>"+estado+"</td>");
			it1=alf.dameListaLetras().iterator();
			while(it1.hasNext()) {
				String letra=it1.next();
				if (automata.deltaExtendida(estado, letra) == null)
					bw.append("<td></td>");
				
				else bw.append("<td>"+automata.deltaExtendida(estado, letra).toString()+"</td>");
			}
			bw.append("</tr>");
		}
		
		bw.append("</table></div>");
	
		//GENERACION DE LA TABLA DE TRADUCCION
		
		NodeList nodos1 = documento.getElementsByTagName("flecha");
		bw.append("<div id='authomata'>");
		bw.append("<p>"+mensajero.devuelveMensaje("automata.entrada",3)+"</p><img src='imagenEntrada.jpg' alt='Input'></p>");
		bw.append("<p>"+mensajero.devuelveMensaje("automata.pasos",3)+"</p><table>");
		Iterator<String> itAlf=alf.dameListaLetras().iterator();
		bw.append("<tr><th></th>");
		while(itAlf.hasNext()) {
			String letra=itAlf.next();
			bw.append("<td>"+letra+"</td>");
		}
		bw.append("</tr>");
		for(int i=0;i<nodos1.getLength();i++) {
			
			bw.append("<tr><th>"+nodos1.item(i).getChildNodes().item(0).getTextContent()+"</th>");
			for(int j=0;j<nodos1.item(i).getChildNodes().getLength();j+=2) {
				if (j>0) {
					if (nodos1.item(i).getChildNodes().item(j).getTextContent().equals("null"))
						bw.append("<td>[ ]</td>");
					else
						bw.append("<td>"+nodos1.item(i).getChildNodes().item(j).getTextContent()+"</td>");
				}
			}
			
			bw.append("</tr>");
		}
		bw.append("</table>");
		
		bw.append("<p>"+mensajero.devuelveMensaje("automata.salida",3)+"</p><p><img src='imagen.jpg' alt='Output'></p>");
		bw.append("</div></body></html>");
		bw.close();
		
		
		//ExpresionRegular er=new ExpresionRegularImpl(expr);
		
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
	
	return rutaHTML;
	
}

/**
 * Traduce los pasos y resultado del algoritmo ER->AFNDL
 * @param ruta xml con los pasos y  resultado del algortimo
 * @return codigo html de la pñgina a crear
 * @throws AutomatasException  lanza la excepciñn si hay algñn problema al
 * abrir o encontrar el fichero xml o al parsear
 */
public String traducirPasosER_AFNDL(String ruta)throws AutomatasException  {
	
	Mensajero mensajero=Mensajero.getInstancia();
	DOMParser parser = new DOMParser(); 
	String brr=new Character((char)92).toString();
	String rutaHTML=System.getProperty("user.dir")+brr+"HTML"+brr+"saleERtoAFNDL.html";
	//String rutaHTML="HTML/saleERtoAFNDL.html";
	
	File fichero = new File (rutaHTML);
	BufferedWriter bw;
	
	try {
		parser.parse(new InputSource(new FileInputStream(ruta)));
		Document documento = parser.getDocument();
		
		bw = new BufferedWriter(new FileWriter(fichero));
		
		
		bw.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
		bw.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
		bw.append("<head><meta http-equiv='content-type' content='text/html; charset=UTF-8'><link rel='stylesheet' type='text/css' href='style.css' media='screen'>");
		
		bw.append("<title>"+mensajero.devuelveMensaje("ERtoAFNDL.title",3)+"</title><body>");
		bw.append("<div id='cabecera'><img src='logo3.gif'></div>");
		
		///////expresion regular
		bw.append("<div id='expresion'>");
		NodeList tipo = documento.getElementsByTagName("RExpr");
		ExpresionRegular er=null;
		String expresion=null;
		String var = null;
		for (int i = 1; i <tipo.item(0).getChildNodes().getLength(); i++) {
			var = tipo.item(0).getChildNodes().item(i).getTextContent(); 
			expresion=var;
			bw.append("<p>"+mensajero.devuelveMensaje("ERtoAFNDL.er",3)+var+"</p>");
			i++;
			 
		}	
		bw.append("</div>");
		
		////// arbol sintactico
		
		Alfabeto alf=new Alfabeto_imp();
		tipo = documento.getElementsByTagName("alphabet");
		for (int j = 1; j <tipo.item(0).getChildNodes().getLength(); j++) {
			alf.aniadirLetra(tipo.item(0).getChildNodes().item(j).getTextContent());
			j++;
		}
		
		bw.append("<div id='arbol'>");
		
		//String rutaArb=System.getProperty("user.dir")+brr+"HTML"+brr+"imagenArbol.jpg";
		String rutaArb="HTML/imagenArbol.jpg";
		er= new ExpresionRegularImpl(alf,expresion);
		generarImagenArbolER(er.getArbolER(), rutaArb);
		bw.append("<p><img src='imagenArbol.jpg' alt='"+mensajero.devuelveMensaje("ERtoAFNDL.arbol",3)+"'></p>");
		
		bw.append("</div><div id='cuerpo'>");
		///// pasos
		tipo = documento.getElementsByTagName("step");
		
		int fin=tipo.getLength();
		for(int i=0;i<fin;i++){
			
			Automata automata = null;
			
			tipo = documento.getElementsByTagName("comment");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				var=tipo.item(i).getChildNodes().item(j).getTextContent();
				bw.append("<h4>"+var+"</h4>");
				if(i==fin-1) bw.append("<h2>"+mensajero.devuelveMensaje("ERtoAFNDL.final",3)+"</h2>");
				j++;
			}
			 
			tipo = documento.getElementsByTagName("type");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				 var = tipo.item(i).getChildNodes().item(j).getTextContent();
				 j++;	 
			}	
				
			if (var.equals("AutomataFD")){		
				automata = new AutomataFD();		
			}
			if (var.equals("AutomataFND")){		
				automata = new AutomataFND();		
			}
			if (var.equals("AutomataFNDLambda")){			
				automata = new AutomataFNDLambda();		
			}	
			
			alf=new Alfabeto_imp();
			tipo = documento.getElementsByTagName("alphabet");
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				alf.aniadirLetra(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			
			automata.setAlfabeto(alf);				
				
			ArrayList<String> estad=new ArrayList<String>();
			tipo = documento.getElementsByTagName("states");		
			for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
				estad.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			automata.setEstados(estad);
				
			tipo = documento.getElementsByTagName("init");
			automata.setEstadoInicial(tipo.item(i).getChildNodes().item(1).getTextContent());
			
			ArrayList<String> finalss=new ArrayList<String>();
			tipo = documento.getElementsByTagName("finals");
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++) {
				finalss.add(tipo.item(i).getChildNodes().item(j).getTextContent());
				j++;
			}
			automata.setEstadosFinales(finalss);		

			tipo = documento.getElementsByTagName("arrows");
			ArrayList<String> arrow=null;
			for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++){
				for(int k=1; k < tipo.item(i).getChildNodes().item(j).getChildNodes().getLength();k++){
					if(arrow==null)arrow=new ArrayList<String>();
					arrow.add(tipo.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
					k++;
				}
				if(arrow!=null)automata.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
				arrow=null;
				j++;
			}
			//String rutaimagen=System.getProperty("user.dir")+brr+"HTML"+brr+"imagen"+i+".jpg";
			String rutaimagen="HTML/imagen"+i+".jpg";
			generarImagenJPG(rutaimagen, automata);
			bw.append("<p><img src='imagen"+i+".jpg' alt='"+mensajero.devuelveMensaje("ERtoAFNDL.paso",3)+"'></p>");
		}
	
		bw.append("</div></body></html>");
		bw.close();
		
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
	
	
	return rutaHTML;
	
}




	/////////////////////////////////////////////////////////////////////////////////////
	//METODOS PARA FORMAR IMAGENES ER TO AFND-LAMBDA)///////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
//////////////GENERACION DE IMAGEN DE UN ARBOL BINARIO//////////////////////
/**
 * Genera una imagen jpg de un arbol binario de expresion regular
 * @param arbol arbol sintñctico de la expresiñn que queremos dibujar
 * @param ruta lugar donde se guardarñ la imagen generada
 */
public void generarImagenArbolER(ArbolER arbol,String ruta) {
	
    // Le asignamos un tamaño a la imagen
    int width = 1024, height =512;

    // Creamos una imagen con ese tamaño y con su correspondiente formato de
    // color
    BufferedImage imagen = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_RGB);

    // obtenemos el objeto en el que dibujar
    Graphics gimg = imagen.getGraphics();

    // rellenamos el fondo
    gimg.setColor(Color.white);
    gimg.fillRect(0, 0, width, height);
    gimg.setColor(Color.black);
    //iniciamos la cuenta
    int x=500;
    int y=40;
    generarImgArbolAux(x,y,arbol,gimg,0);
    gimg.dispose();
    
    FileOutputStream out;
    try {
        out = new FileOutputStream(new File(ruta));

        // Se decodifica la imagen y se envña al flujo.
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(imagen);

    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (ImageFormatException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
private void generarImgArbolAux(int x,int y,ArbolER arbol,Graphics gimg,int nivel) {
	
	int radio=40;
	gimg.drawOval(x,y,radio,radio);
	gimg.drawString(arbol.getRaiz(), x+15, y+25);
	//arista hijo izquierdo
	if (arbol.getHijoIZ()!=null) {
		int x_iz=x-220;
		if (nivel>0) x_iz=x-(180/nivel);
		int y_iz=y+60;
		gimg.drawLine(x+4, y+30, x_iz+39, y_iz+15);
		nivel++;
		generarImgArbolAux(x_iz,y_iz,arbol.getHijoIZ(),gimg,nivel);
		nivel--;
	}

	if (arbol.getHijoDR()!=null) {
		int x_dr=x+220;
		if (nivel>0) x_dr=x+(180/nivel);
		int y_dr=y+60;
		gimg.drawLine(x+40, y+20, x_dr, y_dr+15);
		nivel++;
		generarImgArbolAux(x_dr,y_dr,arbol.getHijoDR(),gimg,nivel);
		nivel--;
	}
	
}
//////////////FIN GENERACION IMAGEN ARBOL BINARIO/////////////////////////////

	/**
	 * Genera una imagen jpg de un automata
	 * @param ruta lugar donde se guardarñ la imagen generada
	 * @param automata automata que queremos dibujar
	 */
	public void generarImagenJPG(String ruta, Automata automata) {
	
		int radio=20;

		cargarAutomataNuevo(automata);
	
        // Le asignamos un tamaño a la imagen
        int width = 768, height = 512;

        // Creamos una imagen con ese tamaño y con su correspondiente formato de
        // color
        BufferedImage imagen = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        // obtenemos el objeto en el que dibujar
        Graphics gimg = imagen.getGraphics();

        // rellenamos el fondo
        gimg.setColor(Color.white);
        gimg.fillRect(0, 0, width, height);        
        Iterator<Estado> itEst=listaEstados.iterator();
        while(itEst.hasNext()) {
			Estado state=itEst.next();
			Point point=new Point(state.getX(),state.getY());
			drawBackground(gimg, state, point, Color.yellow,radio);
			gimg.setColor(Color.black);
			int dx = ((int) gimg.getFontMetrics().getStringBounds(state.getEtiqueta(), gimg).getWidth()) >> 1;
			int dy = ((int) gimg.getFontMetrics().getAscent()) >> 1;
			gimg.drawString(state.getEtiqueta(), point.x - dx, point.y + dy);
			gimg.drawOval(point.x - radio, point.y - radio,2 * radio, 2 * radio);
			if (listaFinales.contains(state.getEtiqueta()))
				gimg.drawOval(point.x - radio + 3, point.y - radio + 3,
						(radio - 3) << 1, (radio - 3) << 1);
			if (estadoInicial.equals(state.getEtiqueta())) {
				int[] x = { point.x - radio, point.x - (radio << 1),point.x - (radio << 1) };
				int[] y = { point.y, point.y - radio, point.y + radio };
				gimg.setColor(Color.white);
				gimg.fillPolygon(x, y, 3);
				gimg.setColor(Color.black);
				gimg.drawPolygon(x, y, 3);
			}
		}
		Iterator<Arista> itArist=listaAristas.iterator();
		while(itArist.hasNext()) {
			Arista a=itArist.next();
			java.awt.Graphics2D gimg2=(java.awt.Graphics2D)gimg; 
			Stroke s=gimg2.getStroke();
			gimg2.setStroke(STROKE);
			gimg2.setColor(Color.black);
			boolean noPintar=false;
			String etiqueta=a.getEtiqueta();
			String repetida=seRepite(listaAristas,a);
			if (repetida!=null) {
				if (repetida.equals("yaesta")) {
					noPintar=true;
				}
				etiqueta=repetida;
			}
			if (a.getX()>a.getFx()) {
				CurvedArrow curva=null;
				if(a.getOrigen().equals(a.getDestino()))curva=new CurvedArrow(a.getX()+20,a.getY()+8,a.getFx()-20,a.getFy()+8, 3);
				else {
					int angulo=2;
					if (esUnica(a)) angulo=0;
					if (a.getY()>a.getFy())
						curva=new CurvedArrow(a.getX()-15,a.getY()-15,a.getFx()+12,a.getFy()+19, angulo);
					else curva=new CurvedArrow(a.getX()-15,a.getY()+15,a.getFx()+12,a.getFy()-19, angulo);
				}
				curva.draw((Graphics2D) gimg2);
				if (noPintar!=true) {
					curva.setLabel(etiqueta);
					curva.drawText(gimg2);
				}
			}
			else {
				CurvedArrow curva=null;
				if(a.getOrigen().equals(a.getDestino()))curva=new CurvedArrow(a.getX()+20,a.getY()+8,a.getFx()-20,a.getFy()+8, 3);
				else {
					int angulo=1;
					if (esUnica(a)) angulo=0;
					if (a.getY()>a.getFy()) 
						curva=new CurvedArrow(a.getX()+15,a.getY()-15,a.getFx()-12,a.getFy()+19, angulo);
					else curva=new CurvedArrow(a.getX()+15,a.getY()+15,a.getFx()-12,a.getFy()-19,angulo);
				}
				curva.draw((Graphics2D) gimg2);
				curva.setLabel(etiqueta);
				if (noPintar!=true) {
					curva.setLabel(etiqueta);
					curva.drawText(gimg2);
				}
			}
			noPintar=false;
			gimg2.setStroke(s);
		}

        // Ejecutamos el metodo Dispose para finalizar
        gimg.dispose();

        // Se crea un flujo de datos, en este caso serñ FileOutPutStream, aunque
        // podñs utilizar cualquier otro.

        FileOutputStream out;
        try {
            out = new FileOutputStream(new File(ruta));

            // Se decodifica la imagen y se envña al flujo.
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(imagen);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ImageFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

	}

	
	
	
	private boolean esUnica(Arista a) {
		// TODO Auto-generated method stub
		Iterator<Arista> itArist=listaAristas.iterator();
		while(itArist.hasNext()) {
			Arista b=itArist.next();
			if (!a.equals(b)) {
				if (a.getX()==b.getFx()&&(a.getY()==b.getFy())&&(a.getFx()==b.getX())&&(a.getFy()==b.getY())) return false;
			}
		}
		return true;
	}

	private void cargarAutomataNuevo(Automata a){
		listaEstados=new ArrayList<Estado>();
		listaFinales=new ArrayList<String>();
		listaAristas=new ArrayList<Arista>();
		Iterator<String> itEst=a.getEstados().iterator();
		
		double r=160.0;//deberia variar segun numero de estads??? facil de hacer
		//es el radio de la circunferencia donde inscribimos el poligono regular
		double incrA=2*Math.PI/a.getEstados().size();
		
		int i=0;
		//estado inicial
		estadoInicial=a.getEstadoInicial();
		//estados
		while(itEst.hasNext()) {
			int x=(int) (r*Math.sin(i*incrA));
			int y=(int) (r*Math.cos(i*incrA));
			Estado estado=new Estado(x+460,y+250,itEst.next());
			listaEstados.add(estado);
			i++;	
		}
		//estados finales
		Iterator<String> itFinales=a.getEstadosFinales().iterator();
		while(itFinales.hasNext()) {
			listaFinales.add(itFinales.next());
		}
		//aristas
		Iterator<String> itEst2=a.getEstados().iterator();
		while(itEst2.hasNext()) {
			String est=itEst2.next();
			ArrayList<String> letrasV=a.getAristasVertice(est);
			if(letrasV!=null){
				Iterator<String> itLetras=letrasV.iterator();
				while(itLetras.hasNext()) {
					String letra=itLetras.next();
					ArrayList<String> destinos=a.deltaExtendida(est, letra);
					Iterator<String> itDest=destinos.iterator();
					while(itDest.hasNext()) {
						String estadoDestino=itDest.next();
						int x=0;
						int y=0;
						int fx=0;
						int fy=0;
						//busqueda de coordenadas de origen y destino
						Iterator<Estado> itEstNuevos=listaEstados.iterator();
						while(itEstNuevos.hasNext()) {
							Estado aux=itEstNuevos.next();
							if (aux.getEtiqueta().equals(est)) {
								x=aux.getX();
								y=aux.getY();
							}
							if (aux.getEtiqueta().equals(estadoDestino)) {
								fx=aux.getX();
								fy=aux.getY();
							}
						}
						Arista arista=new Arista(x,y,fx,fy,letra,est,estadoDestino);
						listaAristas.add(arista);
					}
				}
			}
		}
	}
	
	

	public String seRepite(ArrayList<Arista> la, Arista a) {
		//Iterator<Arista> it=la.iterator();
		String repetida=a.getEtiqueta();
		int i=0;
		while(i<la.size()) {
		//while(it.hasNext()) {
			Arista aux=la.get(i);
			//if (aux.getMarcada()) return null;
			if ((a.getDestino().equals(aux.getDestino()))&&(a.getOrigen().equals(aux.getOrigen()))&&(!a.getEtiqueta().equals(aux.getEtiqueta()))) {
				if (aux.getMarcada()) return "yaesta";
				repetida+=","+aux.getEtiqueta();
			}
			i++;
		}
		if (repetida.contains(",")) {
			a.setMarcada(true);
			return repetida;
		}
		a.setMarcada(false);
		return null;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	//FIN METODOS PARA TRADUCTOR ER TO AFND-LAMBDA///////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Traduce los pasos y resultado del algoritmo ER->AFNDL
	 * @param ruta xml con los pasos y  resultado del algortimo
	 * @return codigo html de la pñgina a crear
	 * @throws AutomatasException  lanza la excepciñn si hay algñn problema al
	 * abrir o encontrar el fichero xml o al parsear
	 */
	public String traducirPasosAFDTOER(String ruta)throws AutomatasException  {
		
		Mensajero mensajero=Mensajero.getInstancia();
		DOMParser parser = new DOMParser(); 
		String brr=new Character((char)92).toString();
		String rutaHTML=System.getProperty("user.dir")+brr+"HTML"+brr+"saleAFDTOER.html";
		Alfabeto alf;
		//String rutaHTML="HTML/saleERtoAFNDL.html";
		
		File fichero = new File (rutaHTML);
		BufferedWriter bw;
		
		try {
			parser.parse(new InputSource(new FileInputStream(ruta)));
			Document documento = parser.getDocument();
			
			bw = new BufferedWriter(new FileWriter(fichero));
			
			
			bw.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
			bw.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
			bw.append("<head><meta http-equiv='content-type' content='text/html; charset=UTF-8'><link rel='stylesheet' type='text/css' href='style.css' media='screen'>");
			
			bw.append("<title>"+mensajero.devuelveMensaje("AFDTOER.title",3)+"</title><body>");
			bw.append("<div id='cabecera'><img src='logo3.gif'></div>");
			
			///////expresion regular
			bw.append("<div id='expresion'>");
			NodeList tipo = documento.getElementsByTagName("RExpr");
			String var = null;
			for (int i = 0; i <tipo.item(0).getChildNodes().getLength(); i++) {
				var = tipo.item(0).getChildNodes().item(i).getTextContent(); 
				bw.append("<p>"+mensajero.devuelveMensaje("ERtoAFNDL.er",3)+var+"</p>");
				i++;
				 
			}	
			bw.append("</div>");
			
			bw.append("</div><div id='cuerpoER'>");
			///// pasos
			tipo = documento.getElementsByTagName("step");
			
			int fin=tipo.getLength();
			bw.append("<h4>"+mensajero.devuelveMensaje("AFDTOER.entrada",3)+"</h4><img src='imagenEntrada.jpg' alt='"+mensajero.devuelveMensaje("automata.entrada",3)+"'>");
			for(int i=0;i<fin;i++){
				
				Automata automata = null;
				
				tipo = documento.getElementsByTagName("comment");
				for (int j = 0; j <tipo.item(i).getChildNodes().getLength(); j++) {
					var=tipo.item(i).getChildNodes().item(j).getTextContent();
					bw.append("<h4>"+var+"</h4>");
					//if(i==fin-1) bw.append("<h2>"+mensajero.devuelveMensaje("ERtoAFNDL.final",3)+"</h2>");
					j++;
				}
				 
				tipo = documento.getElementsByTagName("type");
				for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
					 var = tipo.item(i).getChildNodes().item(j).getTextContent();
					 j++;	 
				}	
					
				if (var.equals("AutomataFD")){		
					automata = new AutomataFD();		
				}
				if (var.equals("AutomataFND")){		
					automata = new AutomataFND();		
				}
				if (var.equals("AutomataFNDLambda")){			
					automata = new AutomataFNDLambda();		
				}	
				
				alf=new Alfabeto_imp();
				tipo = documento.getElementsByTagName("alphabet");
				for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
					alf.aniadirLetra(tipo.item(i).getChildNodes().item(j).getTextContent());
					j++;
				}
				
				automata.setAlfabeto(alf);				
					
				ArrayList<String> estad=new ArrayList<String>();
				tipo = documento.getElementsByTagName("states");		
				for (int j = 1; j <tipo.item(i).getChildNodes().getLength(); j++) {
					estad.add(tipo.item(i).getChildNodes().item(j).getTextContent());
					j++;
				}
				automata.setEstados(estad);
					
				tipo = documento.getElementsByTagName("init");
				automata.setEstadoInicial(tipo.item(i).getChildNodes().item(1).getTextContent());
				
				ArrayList<String> finalss=new ArrayList<String>();
				tipo = documento.getElementsByTagName("finals");
				for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++) {
					finalss.add(tipo.item(i).getChildNodes().item(j).getTextContent());
					j++;
				}
				automata.setEstadosFinales(finalss);		

				tipo = documento.getElementsByTagName("arrows");
				ArrayList<String> arrow=null;
				for (int j = 1; j < tipo.item(i).getChildNodes().getLength(); j++){
					for(int k=1; k < tipo.item(i).getChildNodes().item(j).getChildNodes().getLength();k++){
						if(arrow==null)arrow=new ArrayList<String>();
						arrow.add(tipo.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent());
						k++;
					}
					if(arrow!=null)automata.insertaArista(arrow.get(0),arrow.get(1),arrow.get(2));
					arrow=null;
					j++;
				}
				//String rutaimagen=System.getProperty("user.dir")+brr+"HTML"+brr+"imagen"+i+".jpg";
				String rutaimagen="HTML/imagen"+i+".jpg";
				generarImagenJPG(rutaimagen, automata);
				bw.append("<p><img src='imagen"+i+".jpg' alt='"+mensajero.devuelveMensaje("ERtoAFNDL.paso",3)+"'></p>");
			}
		
			bw.append("</div></body></html>");
			bw.close();
			
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
		
		
		return rutaHTML;
		
	}
	
	/**
	 * Traduce un ejrcicio a una pñgina html con el enunciado entexto y una foto del automata
	 * @param ruta ruta xml del fichero que contiene el ejrcicio
	 * @return ruta de la pñgina html
	 * @throws AutomatasException lanza una excepciñn si hay algñn problema con el
	 * fichero xml o al parsearlo.
	 */
	public String traducirEjercicio(String ruta)throws AutomatasException  {
		Mensajero mensajero=Mensajero.getInstancia();
		String brr=new Character((char)92).toString();
		String rutaHTML=System.getProperty("user.dir")+brr+"HTML"+brr+"ejercicio.html";
		
		File fichero = new File (rutaHTML);
		BufferedWriter bw;
		
		try {
			
			bw = new BufferedWriter(new FileWriter(fichero));
			
			
			bw.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
			bw.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>");
			bw.append("<head><meta http-equiv='content-type' content='text/html; charset=UTF-8'><link rel='stylesheet' type='text/css' href='style.css' media='screen'>");
			
			bw.append("<title>"+mensajero.devuelveMensaje("ejercicio.title",3)+"</title><body>");
			bw.append("<div id='cabecera'><img src='logo3.gif'></div>");
			
			bw.append("<div id='enunciado'>");
			
			bw.append("<h4>"+mensajero.devuelveMensaje("ejercicio.enunciado",3)+"</h4>");
			
			ParserEjercicio parEjr=new ParserEjercicio();
			Ejercicio e=parEjr.extraerEjercicio(ruta);
			
			bw.append("<p>"+e.getEnunciado()+"</p>");
			
			bw.append("</div><div id='fotoAutomata'>");
			Automata aut = (Automata)e.getEntrada();
			if(aut != null) generarImagenJPG("HTML/imgEjercicio.jpg", aut);
			
			if (aut != null) bw.append("<h4>"+mensajero.devuelveMensaje("ejercicio.entrada",3)+"'</h4>");
			if (aut != null) bw.append("<p><img src='imgEjercicio.jpg' alt='"+mensajero.devuelveMensaje("ejercicio.entrada",3)+"'></p>");
			bw.append("</div></body></html>");
			bw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.noarchivo",2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new AutomatasException(mensajero.devuelveMensaje("parser.entsalida",2));
		}
		
		return rutaHTML;
	}
	
	private void drawBackground(Graphics g, Estado state, Point point, Color color,int radio) {
		g.setColor(color);
		if(state.isSelected()) g.setColor(new Color(100, 200, 200));
			g.fillOval(point.x - radio, point.y - radio, 2 * radio,	2 * radio);
	}
	
}
