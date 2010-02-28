/**
 * 
 */
package controlador;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import accesoBD.Mensajero;
import accesoBD.ParserXML;
import modelo.algoritmos.AFD_to_ER;
import modelo.algoritmos.AFNDLambda_to_AFND;
import modelo.algoritmos.AFN_to_AFD;
import modelo.algoritmos.Automatas_equivalentes;
import modelo.algoritmos.MinimizacionAFD;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFD;
import modelo.automatas.AutomataFND;
import modelo.automatas.AutomataFNDLambda;
import modelo.expresion_regular.ExpresionRegular;
import vista.Vista;
import modelo.algoritmos.ERToAFNDLambda;
import modelo.AutomatasException;
/**
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class Controlador_imp implements Controlador{
	private static Controlador instancia;
	private ArrayList<String> query;
	private Object salida;
	private String xmlSalida;
	private boolean pasos;
	private ArrayList<Vista> vistas;
	private Mensajero mensajero;
	
	/**
	 * Constructor del Controlador vacño
	 */
	public Controlador_imp(){
		query=new ArrayList<String>();
		pasos=false;
		vistas=new ArrayList<Vista>();
		mensajero=Mensajero.getInstancia();
	}
	
	/**
	 * Mñtodo singleton que obtiene la instancia del Controlador si 
	 * estñ creada y sino crea una nueva
	 * @return el controlador de la aplicaciñn
	 */
	public static Controlador getInstancia() {
		if (instancia==null) {
			instancia=new Controlador_imp();
		}
		return instancia;
	}
	
	private int analizaQuery(String query) {
		pasos=false;
		this.query=new ArrayList<String>();
		StringTokenizer t=new StringTokenizer(query," ");
		while(t.hasMoreElements()){
			this.query.add(t.nextElement().toString());
		}
		if((this.query.get(0).toUpperCase()).equals(new String("TALF"))){
			StringTokenizer t2=new StringTokenizer(this.query.get(1),"-");
			ArrayList<String> opciones= new ArrayList<String>();
			while(t2.hasMoreElements()){
				String s=t2.nextElement().toString();
				opciones.add(s);
			}
			int args=this.query.size();
			if(opciones.contains("p")){
				pasos=true;
			} 
			for (int i=0;i<opciones.size();i++){
				String opcion=opciones.get(i);
				if(opciones.contains("h")){
					if ((args==2) && (opciones.size()==1)){
						return 6;
					}
				}
				if(opcion.equals("t1"))
						if (args==3)
							return 1;
				if (opcion.equals("t2"))
						if(args==3)
							return 2;
				if(opcion.equals("m"))
						if (args==3) 
							return 3;
				if(opcion.equals("r"))
					if (args==3) 
						return 4;
				if(opcion.equals("e"))
					if (args==4) 
						return 5;
				if (opcion.equals("t3"))
					if(args==3)
						return 7;
				if (!opcion.equals("p")&&(!opcion.equals("h")))
						return 0;
		}
	}
	return 0;
}
	
	private Automata obtenerAutomata()throws AutomatasException {
		// TODO Auto-generated method stub
		return ParserXML.getInstancia().extraerAutomata(query.get(2));
	}
	
	private Automata obtenerAutomata2()throws AutomatasException {
		// TODO Auto-generated method stub
		return ParserXML.getInstancia().extraerAutomata(query.get(3));
	}
	
	private ExpresionRegular obtenerEr() throws AutomatasException {
		// TODO Auto-generated method stub
		return ParserXML.getInstancia().extraerER(query.get(2));
	}

	
	public void registraVista(Vista v){
		vistas.add(v);
	}
	
	
	public void ejecutaQuery(String query) throws AutomatasException{
		int caso=analizaQuery(query);
		switch(caso) {
			case 0: {
				mensajero=Mensajero.getInstancia();
				throw new AutomatasException(mensajero.devuelveMensaje("controlador.param",2));
			}
			case 1: {
				//obtener automata en xml de la query tambien.
				Automata a= obtenerAutomata();
				//lanzamiento de algoritmo de traduccion de AFND-lambda a AFND
				TRANSFORMA_AFNDLambda_AFND(a,pasos);
				break;
			}
			case 2: {
				//obtener automata en xml de la query tambien.
				Automata a=obtenerAutomata();
				//lanzamiento de algoritmo de traduccion de AFND a AFD
				Transforma_AFND_AFD(a,pasos);
				break;
			}
			case 3: {
				//obtener automata en xml de la query tambien.
				Automata a=obtenerAutomata();
				if (a instanceof AutomataFNDLambda){
					TRANSFORMA_AFNDLambda_AFND(a,pasos);
					a = (AutomataFND) salida;
					}
				if (a instanceof AutomataFND){
					Transforma_AFND_AFD(a,pasos);
					a = (AutomataFD)salida;
					
				}
				//lanzamiento de algoritmo de minimizacion de automatas
				minimizacion(a,pasos);
				//a = (AutomataFD)salida;
				//minimizacion(a,pasos);
				break;
			}
			case 4: {
				//obtener ER en xml de la query tambien.
				ExpresionRegular er=obtenerEr();
				TRANSFORMA_ER_AFNDLambda(er,pasos);
				break;
			}
			case 5: {
				//EQUIVALENCIA
				Automata a=obtenerAutomata();
				Automata b=obtenerAutomata2();
				EQUIVALENCIA(a,b,pasos);
				break;
			}
			case 6: {
				//ayuda
				ayuda();
				break;
			}
			case 7: {
				//obtener automata en xml de la query tambien.
				Automata a=obtenerAutomata();
				//lanzamiento de algoritmo de minimizacion de automatas
				afdtoer(a,pasos);
				break;
			}
		}
	}

	
	private void minimizacion(Automata a,boolean pasos) throws AutomatasException {
		//OJO A LOS PASOS
		Automata aux2=null;
		aux2=a;
		if (aux2 instanceof AutomataFD && !(aux2 instanceof AutomataFND)) {
			trataMensaje(mensajero.devuelveMensaje("minimizacion.iniciar",2));
			MinimizacionAFD algMinimizacion = null;
			try {
				algMinimizacion = new MinimizacionAFD(aux2);
			} catch (CloneNotSupportedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			algMinimizacion.registraControlador(this);
			salida=algMinimizacion.ejecutar(pasos);
		
			/*
			 * Obtencion del xml de salida del algoritmo
			 */
			String xml=algMinimizacion.getXML();
			String brr=new Character((char)92).toString();
			String rutaxml=System.getProperty("user.dir")+brr+"XML"+brr+"ejecucionMinimizacion"+brr+"prueba.xml";
			File fichero = new File (rutaxml);
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
				bw.append(xml);
				bw.close();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			xmlSalida=rutaxml;
			salida.toString();
			//se recargarña y se llmarña a la clase de pintado en la interfaz grafica con el objeto salida
			//eso fuera de ese metodo, serña despues del ejecuta query con la salida obtenida
		}
			else {
				mensajero=Mensajero.getInstancia();
				throw new AutomatasException(mensajero.devuelveMensaje("controlador.auto",2));
			}
	}
	
	
	private void Transforma_AFND_AFD(Automata a,boolean pasos) throws AutomatasException {
		trataMensaje(mensajero.devuelveMensaje("AFNtoAFD.iniciar",2));
		if (a instanceof AutomataFND && !(a instanceof AutomataFNDLambda)) {
			AFN_to_AFD algAFNAFD=new AFN_to_AFD(a);
			algAFNAFD.registraControlador(this);
			salida=algAFNAFD.ejecutar(pasos);
			String xml=algAFNAFD.getXML();
			String brr=new Character((char)92).toString();
			String rutaxml=System.getProperty("user.dir")+brr+"XML"+brr+"ejecucionAFNAFD"+brr+"prueba.xml";
			File fichero = new File (rutaxml);
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
				bw.append(xml);
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			xmlSalida=rutaxml;
			salida.toString();
		}
		else {
			mensajero=Mensajero.getInstancia();
			throw new AutomatasException(mensajero.devuelveMensaje("controlador.auto",2));
		}
		
	}
	
	
	private void TRANSFORMA_AFNDLambda_AFND(Automata a,boolean pasos) {
		trataMensaje(mensajero.devuelveMensaje("AFNDLambdatoAFND.iniciar",2));
		if (a instanceof AutomataFNDLambda) {
			AFNDLambda_to_AFND algLambda=new AFNDLambda_to_AFND(a);
			algLambda.registraControlador(this);
			salida=algLambda.ejecutar(pasos);
			/*Obtencion del xml de salida del algoritmo */
			String xml=algLambda.getXML();
			String brr=new Character((char)92).toString();
			String rutaxml=System.getProperty("user.dir")+brr+"XML"+brr+"ejecucionAFNDLambda"+brr+"prueba.xml";
			File fichero = new File (rutaxml);
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
				bw.append(xml);
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			xmlSalida=rutaxml;
			salida.toString();
		}
	}
	private void afdtoer(Automata a,boolean pasos)throws AutomatasException {
		//trataMensaje(mensajero.devuelveMensaje("AFNDLambdatoAFND.iniciar",2));
		if (a instanceof AutomataFD) {
			AFD_to_ER algoritmo=new AFD_to_ER(a);
			algoritmo.registraControlador(this);
			algoritmo.ejecutar(pasos);
			salida=algoritmo.getSalidaER();
		
			//Obtencion del xml de salida del algoritmo 
			String xml=algoritmo.getXML();
			String brr=new Character((char)92).toString();
			String rutaxml=System.getProperty("user.dir")+brr+"XML"+brr+"ejecucionAFDTOER"+brr+"prueba.xml";
			File fichero = new File (rutaxml);
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
				bw.append(xml);
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			xmlSalida=rutaxml;
		}else {
			mensajero=Mensajero.getInstancia();
			throw new AutomatasException(mensajero.devuelveMensaje("controlador.auto",2));
		}
		
	}
	
	
	private void TRANSFORMA_ER_AFNDLambda(ExpresionRegular a,boolean pasos) {
		ERToAFNDLambda algLambda=new ERToAFNDLambda(a.getArbolER(),a.getAlfabeto());
		salida=algLambda.ejecutar(pasos);
		String xml=algLambda.getXML();
		String brr=new Character((char)92).toString();
		String rutaxml=System.getProperty("user.dir")+brr+"XML"+brr+"ejecucionERaAFNDLambda"+brr+"prueba.xml";
		File fichero = new File (rutaxml);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
			bw.append(xml);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmlSalida=rutaxml;
	}
	
	private void EQUIVALENCIA(Automata a,Automata b,boolean pasos){
		Automatas_equivalentes algEquiv=new Automatas_equivalentes();
		algEquiv.registraAutomata1(a);
		algEquiv.registraAutomata2(b);
		algEquiv.ejecutar(pasos);
		salida=algEquiv.getResultado();
		String xml=algEquiv.getXML();
		String brr=new Character((char)92).toString();
		String rutaxml=System.getProperty("user.dir")+brr+"XML"+brr+"ejecucionEquivalencia"+brr+"prueba.xml";
		File fichero = new File (rutaxml);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
			bw.append(xml);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmlSalida=rutaxml;
	}
	
	private void ayuda() {
	}

	
	public void trataMensaje(String mensaje) {
		// TODO Auto-generated method stub
		Iterator<Vista> it=vistas.iterator();
		while(it.hasNext()) {
			it.next().trataMensaje(mensaje);
		}
		
	}
	
	
	public void setIdioma(boolean idioma) {
		Mensajero.getInstancia().setIdioma(idioma);
	}
	
	
	public Object getSalida(){
		return salida;
	}
	
	
	public String salidaXML() {
		return xmlSalida;
	}
	

}







