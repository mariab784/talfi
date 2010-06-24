package vista.vistaGrafica;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.*;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import accesoBD.Mensajero;
import accesoBD.ParserXML;

import vista.Vista;
import vista.vistaGrafica.events.CanvasMouseAdapter;
import vista.vistaGrafica.events.OyenteArista;
import vista.vistaGrafica.events.OyenteEditar;
import modelo.AutomatasException;
import modelo.automatas.Alfabeto;
import modelo.automatas.AlfabetoCinta;
import modelo.automatas.AlfabetoPila_imp;
import modelo.automatas.Alfabeto_Pila;
import modelo.automatas.Alfabeto_imp;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFNDLambda;
import modelo.automatas.AutomataPila;
import modelo.automatas.MaquinaTuring;

/**
 * Clase que almacena y trata toda la información de los automatas que salen en la interfaz
 * grafica y donde se realiza el pintado de automatas.
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class AutomataCanvas extends JScrollPane {

	
	private static final long serialVersionUID = 1L;

	private final int radio=20;
	private final int editar=1;
	private final int borrar=2;
	private final int estado=3;
	private final int arista=4;
	private static Stroke STROKE = new java.awt.BasicStroke(2.4f);
	
	private String tipoAutomatas;
	private int estadoB;
	private VistaGrafica vista;
	private ArrayList<Estado> listaEstados;
	private ArrayList<Arista> listaAristas;
	private ArrayList<AristaAP> listaAristasAP;
	private ArrayList<AristaTuring> listaAristasTuring;
	private String estadoInicial;
	private ArrayList<String> listaFinales;
	private Alfabeto alfabeto;
	private Alfabeto_Pila alfabetoPila;
	private AlfabetoCinta alfabetoCinta;
	private ArrayList<String> listaPalabras;
	private ArrayList<String> listaPalabrasNo;
	
	
	private boolean apd;
	
	private AutomataPila ap;
	
	private Mensajero m;
	
	
	/**
	 * Constructor por defecto de la clase
	 * @param v Vista, clase que corresponde a la interfaz grafica
	 * 
	 */
	public AutomataCanvas(VistaGrafica v){
		this.setAutoscrolls(true);
		estadoB=0;
		vista=v;
		listaEstados=new ArrayList<Estado>();
		listaAristas=new ArrayList<Arista>();
		listaAristasAP=new ArrayList<AristaAP>();
		listaAristasTuring = new ArrayList<AristaTuring>();
		listaFinales=new ArrayList<String>();
		estadoInicial=new String();
		apd = false;
		m = Mensajero.getInstancia();
		CanvasMouseAdapter c=new CanvasMouseAdapter(this);
		addMouseListener(c);
		OyenteEditar editar=new OyenteEditar(this);
		OyenteArista arista=new OyenteArista(this,c);
		

		addMouseListener(editar);
		addMouseListener(arista);
		addMouseMotionListener(editar);
		addMouseMotionListener(arista);
		
	}
		
	public void creaAP(){
		
		if (ap == null){ ap = new AutomataPila(); }
	}
	
	public void borraAP(){ap = null;}
	
	public AutomataPila getAP(){return ap;}
	
	public ArrayList<String> getNombreEstados(){
		
		ArrayList<String> s = new ArrayList<String>();
		int i = 0;
		while (i < listaEstados.size()){
			s.add(listaEstados.get(i).getEtiqueta());
			i++;
		}
		return s;
	}
	

	
	public  boolean getAPD(){return apd;}
	
	public void setPila(boolean b){ VistaGrafica.setPila(b);}

	public void setTuring(boolean b){ VistaGrafica.setTuring(b);}

	
	public boolean getPila(){ return VistaGrafica.getPila();}
	
	public boolean getTuring(){ return VistaGrafica.getTuring();}
	
	/**
	 * Devuelve el alfabeto del automata
	 * @return alfabeto del automata
	 */
	public Alfabeto getAlfabeto() {
		return alfabeto;
	}
	
	public Alfabeto_Pila getAlfabetoPila() {
		return alfabetoPila;
	}

	public AlfabetoCinta getAlfabetoCinta() {
		return alfabetoCinta;
	}
	
	/**
	 * Devuelve la lista de estados
	 * @return lista con los estdo del automata
	 */
	public ArrayList<Estado> getListaEstados() {
		return listaEstados;
	}

	/**
	 * Establece la lista de estados
	 * @param listaEstados nueva lsiata con los estados del automata
	 */
	public void setListaEstados(ArrayList<Estado> listaEstados) {
		this.listaEstados = listaEstados;
	}

	/**
	 * Devuelve la lista de aristas del automata
	 * @return lista con las aristas del automata
	 */
	public ArrayList<Arista> getListaAristas() {
		return listaAristas;
	}
	
	/**
	 * Devuelve la lista de aristas del automata de pila
	 * @return lista con las aristas del automata de pila
	 */
	public ArrayList<AristaAP> getListaAristasPila() {
		return listaAristasAP;
	}
	
	public ArrayList<AristaTuring> getListaAristasTuring() {
		return listaAristasTuring;
	}	
	/**
	 * Establece la lista de aristas
	 * @param listaAristas nueva lista de aristas el automata
	 */

	public void setListaAristas(ArrayList<Arista> lista) {

			this.listaAristas = lista;


	}
	
	public void setListaAristasAP(ArrayList<AristaAP> listaAP) {

		listaAristasAP = listaAP;
		
	}

	/**
	 * Devuelve la lista de finales
	 * @return lista de finales del automata
	 */
	public ArrayList<String> getListaFinales() {
		return listaFinales;
	}

	/**
	 * Establece la lista de finales
	 * @param listaFinales nueva lista con los estado finales
	 */
	public void setListaFinales(ArrayList<String> listaFinales) {
		this.listaFinales = listaFinales;
	}

	/**
	 * Devuelve la vista asociada(interfaz grafica)
	 * @return vista del automataCanvas
	 */
	public Vista getVista() {
		return vista;
	}

	/**
	 * Método accesor del boton de dibujo que está pulsado, depende del entero
	 * @return entero que representa le boton pulsado
	 */
	public int getEstadoB() {
		return this.estadoB;
	}
	
	/**
	 * Establece el alfabeto
	 * @param a nuevo alfabeto
	 */
	public void setAlfabeto(Alfabeto a){
		alfabeto=a;
	}
	
	public void setAlfabetoPila(Alfabeto_Pila a){
		alfabetoPila=a;
	}
	
	public void setAlfabetoCinta(AlfabetoCinta a){
		alfabetoCinta=a;
	}
	/**
	 * Devuelve el estado inicial
	 * @return estado inicial del automata
	 */
	public String getEstadoInicial() {
		return estadoInicial;
	}

	/**
	 * Establece el estado inicial
	 * @param estadoInicial nuevo estado incial
	 */
	public void setEstadoInicial(String estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	/**
	 * Devuelve el radio
	 * @return radio del dibujo de los estados
	 */
	public int getRadio() {
		return radio;
	}
	
	/**
	 * Establece el tipo de automata 
	 * @param automata tipo del automata del Canvas
	 */
	public void setTipoAutomata(String automata){
		tipoAutomatas=automata;
	}
	
	/**
	 * Devuelve el tipo de automata 
	 * @return tipo del automata del Canvas
	 */
	public String getTipoAutomata(){
		return tipoAutomatas;
	}
	


	public void creaListaPalabras(String l,int ind){
		
		System.out.println("DIMEKETIENE EL LISTAPALABRAS A RECONOCER: " + l);
		if(ind == 0)this.listaPalabras = new ArrayList<String>();
		else if(ind == 1)this.listaPalabrasNo = new ArrayList<String>();
		StringTokenizer st=new StringTokenizer(l,",");
		while(st.hasMoreTokens()){
			String ss=st.nextToken();
			if(ind == 0){
				if(!listaPalabras.contains(ss))listaPalabras.add(ss);
			}
			else if (ind == 1){
				if(!listaPalabrasNo.contains(ss))listaPalabras.add(ss);
			}
		}
		System.out.println("DIMEKE HAS GUARDADO EN LISTAPALABRAS: " + listaPalabras);
	}

	public ArrayList<String> getListaPalabras(){return listaPalabras;}
	
	public ArrayList<String> getListaPalabrasNo(){return listaPalabrasNo;}
	
	/**
	 * Pinta la arista de un AF en la interfaz grafica
	 * @param g clase graphics para pintar sobre el panel
	 */
	private void pintaArista(Graphics g){
		Iterator<Arista> itArist=listaAristas.iterator();
		while(itArist.hasNext()) {
			Arista a=itArist.next();
			java.awt.Graphics2D g2=(java.awt.Graphics2D)g; 
			Stroke s=g2.getStroke();
			g2.setStroke(STROKE);
			g2.setColor(Color.black);
			boolean noPintar=false;
			String etiqueta=a.getEtiqueta();
			String repetida=seRepite(listaAristas,a); //aqui compara con el resto por si van al mismo est
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
				curva.draw((Graphics2D) g);
				if (noPintar!=true) {
					curva.setLabel(etiqueta);
					curva.drawText(g);
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
				curva.draw((Graphics2D) g);
				//curva.setLabel(etiqueta);
				if (noPintar!=true) {
					curva.setLabel(etiqueta);
					curva.drawText(g);
				}
			}
			noPintar=false;
			g2.setStroke(s);
		}
		
	}
	
	/**
	 * Pinta la arista de un AP en la interfaz grafica
	 * @param g clase graphics para pintar sobre el panel
	 */
	
	private void pintaAristaPila(Graphics g){
		
		
		if (!listaAristasAP.isEmpty()){	
			int numAristas = listaAristasAP.size();
			int j = 0;
			int i = 0;
			String etiquetaAux = "";// String etiqueta = "";
			AristaAP aristaAP = listaAristasAP.get(i);
			while (i < numAristas){

				java.awt.Graphics2D g2=(java.awt.Graphics2D)g; 
				Stroke s=g2.getStroke();
				g2.setStroke(STROKE);
				g2.setColor(Color.black);
			
				if ( !aristaAP.getMarcada() ){ 
					//Mensajero m=Mensajero.getInstancia();	
					String separador = m.devuelveMensaje("simbolos.separador",4);
					if (etiquetaAux.equals("")){ 
						etiquetaAux = dameEtiqueta(aristaAP.getEntradaSimbolos(),0) + separador + aristaAP.getCimaPila() + separador + dameEtiqueta(aristaAP.getSalidaPila(),1);
						j = i+1;
					}
				
					if (j < numAristas){ // falta llave
				
						AristaAP aux = listaAristasAP.get(j);
						boolean origen,destinos;		
						destinos = aristaAP.getDestino().equals( aux.getDestino());
						origen = aristaAP.getOrigen().equals( aux.getOrigen());

						if (destinos && origen && !aux.getMarcada() ) {
							etiquetaAux += " , " + dameEtiqueta(aux.getEntradaSimbolos(),0) + separador + aux.getCimaPila() + separador + dameEtiqueta(aux.getSalidaPila(),1);
							aux.setMarcada(true);
						//j++;
						}
					
					}
							
					if	(j >= numAristas){
		
						if (aristaAP.getX()>aristaAP.getFx()) {
							CurvedArrow curva=null;
							if(aristaAP.getOrigen().equals(aristaAP.getDestino()))curva=new CurvedArrow(aristaAP.getX()+20,aristaAP.getY()+8,aristaAP.getFx()-20,aristaAP.getFy()+8, 3);
							else {
								int angulo=0;//2;
							//if (esUnicaAP(aristaAP)) angulo=0;
								if (aristaAP.getY()>aristaAP.getFy())
									curva=new CurvedArrow(aristaAP.getX()-15,aristaAP.getY()-15,aristaAP.getFx()+12,aristaAP.getFy()+19, angulo);
								else curva=new CurvedArrow(aristaAP.getX()-15,aristaAP.getY()+15,aristaAP.getFx()+12,aristaAP.getFy()-19, angulo);
							}
							curva.draw((Graphics2D) g);
							curva.setLabel(/*etiqueta*/ etiquetaAux);
							curva.drawText(g);
						}
						else {
							CurvedArrow curva=null;
							//System.out.println("ARISTA A PINTAR: " + aristaAP);
							if(aristaAP.getOrigen().equals(aristaAP.getDestino()))curva=new CurvedArrow(aristaAP.getX()+20,aristaAP.getY()+8,aristaAP.getFx()-20,aristaAP.getFy()+8, 3);
							else {
								int angulo=0;
								if (aristaAP.getY()>aristaAP.getFy()) 
									curva=new CurvedArrow(aristaAP.getX()+15,aristaAP.getY()-15,aristaAP.getFx()-12,aristaAP.getFy()+19, angulo);
								else curva=new CurvedArrow(aristaAP.getX()+15,aristaAP.getY()+15,aristaAP.getFx()-12,aristaAP.getFy()-19,angulo);
							}
							curva.draw((Graphics2D) g);
							curva.setLabel(/*etiqueta*/ etiquetaAux);
							curva.setLabel(/*etiqueta*/ etiquetaAux);
							curva.drawText(g);
						}
						g2.setStroke(s);
					}
				}
			
				if (j >= numAristas){ 
					i++; 
					j = i+1;  
					etiquetaAux = "";
					if (i < listaAristasAP.size()) 	aristaAP = listaAristasAP.get(i);
				}
				else j++; 
			}
		}//llave if ArrayList vacio

	}

	/*******************************************************************/
	
	/**
	 * Pinta la arista de una MT en la interfaz grafica
	 * @param g clase graphics para pintar sobre el panel
	 */
	
	private void pintaAristaTuring(Graphics g){

		if (!listaAristasTuring.isEmpty()){	
			
			int numAristas = listaAristasTuring.size();
			int j = 0;
			int i = 0;
			String etiquetaAux = "";// String etiqueta = "";
			AristaTuring aristaT = listaAristasTuring.get(i);
			while (i < numAristas){

				java.awt.Graphics2D g2=(java.awt.Graphics2D)g; 
				Stroke s=g2.getStroke();
				g2.setStroke(STROKE);
				g2.setColor(Color.black);
			
				if ( !aristaT.getMarcada() ){ 
					//Mensajero m=Mensajero.getInstancia();	
					String separador = m.devuelveMensaje("simbolos.separador",4);
					if (etiquetaAux.equals("")){ 
						etiquetaAux = dameEtiqueta(aristaT.getEntradaCinta(),0) + separador + aristaT.getSimboloCinta() + separador + /*dameEtiqueta(*/aristaT.getDireccion()/*)*/;
						j = i+1;
					}
				
					if (j < numAristas){ // falta llave
				
						AristaTuring aux = listaAristasTuring.get(j);
						boolean origen,destinos;		
						destinos = aristaT.getDestino().equals( aux.getDestino());
						origen = aristaT.getOrigen().equals( aux.getOrigen());

						if (destinos && origen && !aux.getMarcada() ) {
							etiquetaAux += " , " + dameEtiqueta(aux.getEntradaCinta(),0) + separador + aux.getSimboloCinta() + separador + /*dameEtiqueta(*/aux.getDireccion()/*SalidaPila())*/;
							aux.setMarcada(true);
						//j++;
						}
					
					}
							
					if	(j >= numAristas){
		
						if (aristaT.getX()>aristaT.getFx()) {
							CurvedArrow curva=null;
							if(aristaT.getOrigen().equals(aristaT.getDestino()))curva=new CurvedArrow(aristaT.getX()+20,aristaT.getY()+8,aristaT.getFx()-20,aristaT.getFy()+8, 3);
							else {
								int angulo=0;//2;
							//if (esUnicaAP(aristaAP)) angulo=0;
								if (aristaT.getY()>aristaT.getFy())
									curva=new CurvedArrow(aristaT.getX()-15,aristaT.getY()-15,aristaT.getFx()+12,aristaT.getFy()+19, angulo);
								else curva=new CurvedArrow(aristaT.getX()-15,aristaT.getY()+15,aristaT.getFx()+12,aristaT.getFy()-19, angulo);
							}
							curva.draw((Graphics2D) g);
							curva.setLabel(/*etiqueta*/ etiquetaAux);
							curva.drawText(g);
						}
						else {
							CurvedArrow curva=null;
							if(aristaT.getOrigen().equals(aristaT.getDestino()))curva=new CurvedArrow(aristaT.getX()+20,aristaT.getY()+8,aristaT.getFx()-20,aristaT.getFy()+8, 3);
							else {
								int angulo=0;
								if (aristaT.getY()>aristaT.getFy()) 
									curva=new CurvedArrow(aristaT.getX()+15,aristaT.getY()-15,aristaT.getFx()-12,aristaT.getFy()+19, angulo);
								else curva=new CurvedArrow(aristaT.getX()+15,aristaT.getY()+15,aristaT.getFx()-12,aristaT.getFy()-19,angulo);
							}
							curva.draw((Graphics2D) g);
							curva.setLabel(/*etiqueta*/ etiquetaAux);
							curva.setLabel(/*etiqueta*/ etiquetaAux);
							curva.drawText(g);
						}
						g2.setStroke(s);
					}
				}
			
				if (j >= numAristas){ 
					i++; 
					j = i+1;  
					etiquetaAux = "";
					if (i < listaAristasTuring.size()) 	aristaT = listaAristasTuring.get(i);
				}
				else j++; 
			}
		}//llave if ArrayList vacio
	}
	
	
	/**
	 * Pinta el automata en la interfaz grafica
	 * @param g clase graphics para pintar sobre el panel
	 */
	public void paint(Graphics g){
		//g.clearRect(0,0,2000,2000);
	//	desmarcarAristas(listaAristas);
		
		if (getTuring()) desmarcarAristas/*Turing*/(listaAristasTuring);
		else if(getPila())desmarcarAristas/*AP*/(listaAristasAP);
		else desmarcarAristas(listaAristas);
		
		//VALE LO DE ARRIBA ESTO ES PARA DEBUG 
		//desmarcarAristasAP(listaAristasAP);
		
		Iterator<Estado> itEst=listaEstados.iterator();
		while(itEst.hasNext()) {
			Estado state=itEst.next();
			Point point=new Point(state.getX(),state.getY());
			drawBackground(g, state, point, Color.yellow);
			g.setColor(Color.black);
			int dx = ((int) g.getFontMetrics().getStringBounds(state.getEtiqueta(), g).getWidth()) >> 1;
			int dy = ((int) g.getFontMetrics().getAscent()) >> 1;
			g.drawString(state.getEtiqueta(), point.x - dx, point.y + dy);
			g.drawOval(point.x - radio, point.y - radio,2 * radio, 2 * radio);
			if (listaFinales.contains(state.getEtiqueta()))
				g.drawOval(point.x - radio + 3, point.y - radio + 3,
						(radio - 3) << 1, (radio - 3) << 1);
			if ((estadoInicial != null) && (estadoInicial.equals(state.getEtiqueta()))) {
				int[] x = { point.x - radio, point.x - (radio << 1),point.x - (radio << 1) };
				int[] y = { point.y, point.y - radio, point.y + radio };
				g.setColor(Color.white);
				g.fillPolygon(x, y, 3);
				g.setColor(Color.black);
				g.drawPolygon(x, y, 3);
			}
		}
		
		boolean esPila = getPila();
		boolean esTuring = getTuring();
		
		if (esPila) pintaAristaPila(g);
		else if (esTuring) pintaAristaTuring(g);
		else pintaArista(g);
		
		

		vista.requestFocus();
	}

	
	
	
	
	/**
	 * Repinta el automata en la interfaz grafica
	 * @param g objeto grafico donde se pinta
	 */
	public void repaint(Graphics g){
		//if (getTuring())
		/*elseif(getPila())desmarcarAristasAP(listaAristasAP);
		else desmarcarAristas(listaAristas);*/
		
		if (getTuring()) desmarcarAristas/*Turing*/(listaAristasTuring);
		else if(getPila())desmarcarAristas/*AP*/(listaAristasAP);
		else desmarcarAristas(listaAristas);
		
		g.clearRect(0,0,2000,2000);

		Iterator<Estado> itEst=listaEstados.iterator();
		while(itEst.hasNext()) {
			Estado state=itEst.next();
			
			Point point=new Point(state.getX(),state.getY());
			drawBackground(g, state, point, Color.yellow);
			g.setColor(Color.black);
			int dx = ((int) g.getFontMetrics().getStringBounds(state.getEtiqueta(), g).getWidth()) >> 1;
			int dy = ((int) g.getFontMetrics().getAscent()) >> 1;
			g.drawString(state.getEtiqueta(), point.x - dx, point.y + dy);
			g.drawOval(point.x - radio, point.y - radio,2 * radio, 2 * radio);
			if (listaFinales.contains(state.getEtiqueta()))
				g.drawOval(point.x - radio + 3, point.y - radio + 3,
						(radio - 3) << 1, (radio - 3) << 1);
			if ( (estadoInicial != null) && (estadoInicial.equals(state.getEtiqueta()))) {
				int[] x = { point.x - radio, point.x - (radio << 1),point.x - (radio << 1) };
				int[] y = { point.y, point.y - radio, point.y + radio };
				g.setColor(Color.white);
				g.fillPolygon(x, y, 3);
				g.setColor(Color.black);
				g.drawPolygon(x, y, 3);
			}
		}

		
		
		boolean esPila = getPila();
		boolean esTuring = getTuring();
		
		if (esPila) pintaAristaPila(g);
		else if (esTuring) pintaAristaTuring(g);
		else pintaArista(g);
		
		
		
		vista.requestFocus();
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
	
	/**
	 * Crea un archivo xml con los datos del automata
	 * @return texto xml con el automata
	 * @throws AutomatasException lanza la excepción si el qutomata no tiene estado inicial
	 */
	public String traducirXML()throws AutomatasException {

		if(!this.estadoInicial.equals("")&&!(this.estadoInicial==null)){

			String fichero="<authomata>\n\t<type>\n\t\t<item>";
			fichero+=tipoAutomata()+"</item>\n\t</type>\n";
			Iterator<Arista> itArist = null; Iterator<AristaAP> itAristAP = null;
			Iterator<AristaTuring> itAristTuring = null;
			String lambda = m.devuelveMensaje("simbolos.lambda",4);
			String blanco = m.devuelveMensaje("simbolos.blanco",4);
			if (this.getPila()){
				itAristAP=listaAristasAP.iterator();
				while (itAristAP.hasNext()){
					AristaAP a = itAristAP.next();
					ArrayList<String> s = a.getEntradaSimbolos();
					String letra;
					for(int i = 0; i < s.size(); i++){
						letra = s.get(i);
						if(!alfabeto.dameListaLetras().contains(letra)&& !letra.equals(lambda)){
							alfabeto.aniadirLetra(letra);						
						}	
					}
					letra = a.getCimaPila();
					if(!alfabetoPila.dameListaLetras().contains(letra) && !letra.equals(lambda))
						alfabetoPila.aniadirLetra(letra);					
					
					s = a.getSalidaPila();
					for(int i = 0; i < s.size(); i++){
						letra = s.get(i);
						if(!alfabetoPila.dameListaLetras().contains(letra) && !letra.equals(lambda)){
							alfabetoPila.aniadirLetra(letra);							
						}	
					}
					
				}
				VistaGrafica.setGordaLatex(true);
				VistaGrafica.setEstaPalabra(true);
				VistaGrafica.setMaricaTuring(false);
			}
			else if (this.getTuring()){
				
				itAristTuring = listaAristasTuring.iterator();
				while (itAristTuring.hasNext()){
					AristaTuring a = itAristTuring.next();
					ArrayList<String> s = a.getEntradaCinta();
					String letra;
					for(int i = 0; i < s.size(); i++){
						letra = s.get(i);
						if(!alfabeto.dameListaLetras().contains(letra)&& !letra.equals(blanco)){
							alfabeto.aniadirLetra(letra);						
						}	
					}
					letra = a.getSimboloCinta();
					if(!alfabetoCinta.dameListaLetras().contains(letra) && !letra.equals(blanco))
						alfabetoCinta.aniadirLetra(letra);	
					
				}
				VistaGrafica.setGordaLatex(true);
				VistaGrafica.setEstaPalabra(false);
				VistaGrafica.setMaricaTuring(true);
			}

			else {
				itArist=listaAristas.iterator();
				while(itArist.hasNext()) {
					String letra=itArist.next().getEtiqueta();
					if (!alfabeto.dameListaLetras().contains(letra)) {
						alfabeto.aniadirLetra(letra);
					}
				}
				
				VistaGrafica.setGordaLatex(true);
				VistaGrafica.setEstaPalabra(false);
				VistaGrafica.setMaricaTuring(false);
			}
			if(alfabeto!=null){
				Iterator<String> itAlfabeto=alfabeto.dameListaLetras().iterator();
				fichero+="\t<alphabet>\n\t";
				while(itAlfabeto.hasNext()) {
					fichero+="\t<item>"+itAlfabeto.next()+"</item>\n\t";
				}
				fichero+="</alphabet>\n\t";
				
				if (this.getPila()){
					Iterator<String> itAlfabetoP=alfabetoPila.dameListaLetras().iterator();

					fichero+="\t<alphabetP>\n\t";
					while(itAlfabetoP.hasNext()) {
						String let = itAlfabetoP.next();
						if (!let.equals(lambda))
						fichero+="\t<item>"+let+"</item>\n\t";
					}
					fichero+="</alphabetP>\n\t";
		
				}
				
				else if (this.getTuring()){
					Iterator<String> itAlfabetoP=alfabetoCinta.dameListaLetras().iterator();

					fichero+="\t<alphabetP>\n\t";
					while(itAlfabetoP.hasNext()) {
						String let = itAlfabetoP.next();
						if (!let.equals(blanco))  //XXX REVISAR
						fichero+="\t<item>"+let+"</item>\n\t";
					}
					fichero+="</alphabetP>\n\t";
		
				}
				
				Iterator<Estado> itEst=listaEstados.iterator();
				fichero+="<states>\n\t";
				while(itEst.hasNext()) {
					fichero+="\t<state>"+itEst.next().getEtiqueta()+"</state>\n\t";
				}
				fichero+="</states>\n\t";
				//GENERACION DE LOS EsTADOS FINALES E INICIALES
				fichero+="<init>\n\t\t<state>"+estadoInicial+"</state>\n\t</init>\n\t";
				Iterator<String> itFin=listaFinales.iterator();
				fichero+="<finals>\n\t";
				while(itFin.hasNext()) {
					fichero+="\t<state>"+itFin.next()+"</state>\n\t";
				}
				fichero+="</finals>\n\t";
				//GENERACION DE LAS ARISTAS!
				
				
				fichero+="<arrows>\n\t";
				
				if (this.getPila()){

					Iterator<AristaAP> itAristaAP=listaAristasAP.iterator();
					while(itAristaAP.hasNext()) {
						AristaAP a=itAristaAP.next();
						fichero+="\t<arrow>\n\t";
						fichero+="\t<state>"+a.getOrigen()+"</state>\n\t";
						fichero+="\t<state>"+a.getDestino()+"</state>\n\t";
						fichero+="\t<item>";
						for(int i = 0; i < a.getEntradaSimbolos().size(); i++){
							
							fichero+=a.getEntradaSimbolos().get(i);
							if(i != a.getEntradaSimbolos().size()-1)
								fichero+=",";
							
						}
						fichero+="</item>\n\t";
						
						fichero+="\t<cima>"+a.getCimaPila()+"</cima>\n\t";
						fichero+="\t<trans>";
						for(int i = 0; i < a.getSalidaPila().size(); i++){
							
							fichero+=a.getSalidaPila().get(i);
							
						}
						fichero+="</trans>\n\t";
						fichero+="\t</arrow>\n\t";
					}
					
				}
				else if (this.getTuring()){
					
					Iterator<AristaTuring> itAristaTuring=listaAristasTuring.iterator();
					while(itAristaTuring.hasNext()) {
						AristaTuring a=itAristaTuring.next();
						fichero+="\t<arrow>\n\t";
						fichero+="\t<state>"+a.getOrigen()+"</state>\n\t";
						fichero+="\t<state>"+a.getDestino()+"</state>\n\t";
						fichero+="\t<item>";
						for(int i = 0; i < a.getEntradaCinta().size(); i++){
							
							fichero+=a.getEntradaCinta().get(i);
							if(i != a.getEntradaCinta().size()-1)
								fichero+=",";
							
						}
						fichero+="</item>\n\t";
						
						fichero+="\t<scinta>"+a.getSimboloCinta()+"</scinta>\n\t";
						fichero+="\t<direc>"+a.getDireccion()+"</direc>\n\t";
						fichero+="\t</arrow>\n\t";
					}
				}
				else{
				
					Iterator<Arista> itArista=listaAristas.iterator();
					while(itArista.hasNext()) {
						Arista a=itArista.next();
						fichero+="\t<arrow>\n\t";
						fichero+="\t<state>"+a.getOrigen()+"</state>\n\t";
						fichero+="\t<state>"+a.getDestino()+"</state>\n\t";
						fichero+="\t<item>"+a.getEtiqueta()+"</item>\n\t</arrow>\n\t";
					}
				}
				fichero+="</arrows>\n";
				
				fichero+="<coordenadas>\n";
				Iterator<Estado> itEstc=listaEstados.iterator();
				while(itEstc.hasNext()) {
					Estado estado=itEstc.next();
					fichero+="<estadoCoord><nombre>"+estado.getEtiqueta()+"</nombre>";
					fichero+="<x>"+estado.getX()+"</x>";
					fichero+="<y>"+estado.getY()+"</y>";
					fichero+="</estadoCoord>";
				}
				fichero+="</coordenadas>";

				fichero+="</authomata>";
				return fichero;
			} else {
				throw new AutomatasException(m.devuelveMensaje("canvas.noarrows",2));
			}
		} else {
			throw new AutomatasException(m.devuelveMensaje("canvas.noinicial",2));
		}
	}
	
	/**
	 * Carga un automata contenido en un xml
	 * @param rutaXml que contiene el automata a cargar en el panel
	 */
	public void cargarAutomata(String rutaXml/*,String pintar*/) {
		ParserXML parser=new ParserXML();		
	    try{
	    	cargarAutomataNuevo(parser.extraerAutomata(rutaXml)/*,pintar*/);
	    	vista.requestFocus();
	    } catch(AutomatasException e){
	    	JOptionPane.showMessageDialog(null,e.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	
	/**
	 * Establece el estado de la interfaz segun el boton que se ha pulsado
	 * @param b boton pulsado
	 */
	public void botonPulsado(JToggleButton b){
		String s=b.getName();
		if(s.equals("editar")) estadoB=editar;
		if(s.equals("borrar")) estadoB=borrar;
		if(s.equals("estado")) estadoB=estado;
		if(s.equals("arista")) estadoB=arista;
	}
	
	/**
	 * Traduce un automata a la interfaz grafica 
	 * Si el automata tiene informacion de las coordenadas de sus estados, establece esas,
	 * en caso contrario lo transforma en un poligono regular, numero de vertices=numero de estados.
	 * @param a Automata a cargar en el panel
	 */
	public void cargarAutomataNuevo(Automata a){
		//en un futuro se cargara desde un xml??estas cosas hay q decidirlas,
		//igual se puede dejar asi.... no habria problema creo yo, porque seria
		//el controlador el que lo pasa a la interfaz grafica, y ésta a esta clase: AutomataCanvas
		//IDEA: generar un poligono regular a partir de los vertices, el radio
		//del polígono deberia aumentar dependiendo del numero de vertives
		borrarPanel();
		this.alfabeto=a.getAlfabeto();
		//if (a instanceof AutomataPila){setPila(false);}
		if (a instanceof AutomataPila){
			setPila(true); setTuring(false); this.alfabetoPila = ((AutomataPila)a).getAlfabetoPila();
		}
		if (a instanceof MaquinaTuring){
			setTuring(true);setPila(false); this.alfabetoCinta = ((MaquinaTuring)a).getAlfabetoCinta();
		}

		
		Iterator<String> itEst=a.getEstados().iterator();
		
		double r=160.0;//deberia variar segun numero de estads??? facil de hacer
		//es el radio de la circunferencia donde inscribimos el poligono regular
		double incrA=2*Math.PI/a.getEstados().size();
		
		int i=0;
		//estado inicial
		estadoInicial=a.getEstadoInicial();
		//estados
		while(itEst.hasNext()) {
			String est=itEst.next();
			int x=0;
			int y=0;
			Estado estado=null;
			if (a.hayCoordenadas()) {
				x=a.getCoordenadas(est).getX();
				y=a.getCoordenadas(est).getY();
				estado=new Estado(x,y,est);
			}
			else  {
				x=(int) (r*Math.sin(i*incrA));
				y=(int) (r*Math.cos(i*incrA));
				estado=new Estado(x+460,y+250,est);
			}
			listaEstados.add(estado);
			i++;	
		}
		//estados finales
		Iterator<String> itFinales=a.getEstadosFinales().iterator();
		while(itFinales.hasNext()) {
			listaFinales.add(itFinales.next());
		}
		//aristas
		if((a instanceof AutomataPila)){
			
			vista.setEnabledMenuAlgAP();
			Iterator<AristaAP> iAP = ((AutomataPila) a).getAutomataPila().iterator();
			while (iAP.hasNext()){
				AristaAP arAP = iAP.next();
				
				Iterator<Estado> itEstNuevos=listaEstados.iterator();
				while(itEstNuevos.hasNext()) {
				
					Estado aux=itEstNuevos.next();
					
					if (aux.getEtiqueta().equals(arAP.getOrigen())) {
						arAP.setX(aux.getX());
						arAP.setY(aux.getY());
						
					}
					if (aux.getEtiqueta().equals(arAP.getDestino())) {
						arAP.setFx(aux.getX());
						arAP.setFy(aux.getY());
						
					}
			}
			this.listaAristasAP.add(arAP.clone());
			}
			
			
		}
		else if((a instanceof MaquinaTuring)){
			
			vista.setEnabledMenuAlgMT();
			Iterator<AristaTuring> iTuring = ((MaquinaTuring) a).getAristasTuring().iterator();
			while (iTuring.hasNext()){
				AristaTuring arAP = iTuring.next();
				
				Iterator<Estado> itEstNuevos=listaEstados.iterator();
				while(itEstNuevos.hasNext()) {
				
					Estado aux=itEstNuevos.next();
					
					if (aux.getEtiqueta().equals(arAP.getOrigen())) {
						arAP.setX(aux.getX());
						arAP.setY(aux.getY());
						
					}
					if (aux.getEtiqueta().equals(arAP.getDestino())) {
						arAP.setFx(aux.getX());
						arAP.setFy(aux.getY());
						
					}
			}
			this.listaAristasTuring.add(arAP.clone());
			}
		}
		/*if(!(a instanceof AutomataPila))*/else{
			
			
			vista.setEnabledMenuAlgAF();
		Iterator<String> itEst2=a.getEstados().iterator();
		while(itEst2.hasNext()) {
			String est=itEst2.next();
			ArrayList<String> letrasV=a.getAristasVertice(est);
			

			//System.out.println("LETRASV : " + letrasV); //XXX
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
					/*	if (a instanceof AutomataPila){
							AristaAP aristaAP=new AristaAP(x,y,fx,fy,est,estadoDestino);
							listaAristasAP.add(aristaAP);
							
						} */
						//else if (a instanceof MaquinaTuring)
					//	else{
							Arista arista=new Arista(x,y,fx,fy,letra,est,estadoDestino);
							listaAristas.add(arista);
					//	}
						
					}
				}
			}
		}
		}//llave si no AP
		String brr=new Character((char)92).toString();
		String ruta=System.getProperty("user.dir")+brr+"HTML"+brr+"imagen.jpg";
		generarImagenJPg(ruta);
		//if (!(a instanceof AutomataPila) || !(a instanceof MaquinaTuring))
			this.repaint();
		
		vista.requestFocus();
		
	}
	
	public void cargarAutomataNuevoPila(Automata a){}
	
	/**
	 * Borra el automata contenido en el panel
	 */
	
	/************************************************************/
	 
	public void borrarPanel() {
		// TODO Auto-generated method stub
		listaEstados=new ArrayList<Estado>();
    	listaAristas=new ArrayList<Arista>();
    	listaFinales=new ArrayList<String>();
    	listaAristasAP = new ArrayList<AristaAP>();
    	listaAristasTuring = new ArrayList<AristaTuring>();
    	alfabeto = null;
    	alfabetoPila = null;
    	alfabetoCinta = null;
    	estadoInicial="";
    	apd = false;
    	ap = null;
    	setPila(false);
    	setTuring(false);
    	this.repaint();
    	vista.requestFocus();
	}
	
	/**
	 * Genera una imagen jpg con la informacion del automata
	 * @return ruta de la imagen jpg
	 */ 
	public String generarImagenJPg(String ruta) {
		
		if (getTuring()) desmarcarAristas/*Turing*/(listaAristasTuring);
		else if(getPila())desmarcarAristas/*AP*/(listaAristasAP);
		else desmarcarAristas(listaAristas);
		//desmarcarAristas(listaAristas);cvc
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
			drawBackground(gimg, state, point, Color.yellow);
			gimg.setColor(Color.black);
			int dx = ((int) gimg.getFontMetrics().getStringBounds(state.getEtiqueta(), gimg).getWidth()) >> 1;
			int dy = ((int) gimg.getFontMetrics().getAscent()) >> 1;
			gimg.drawString(state.getEtiqueta(), point.x - dx, point.y + dy);
			gimg.drawOval(point.x - radio, point.y - radio,2 * radio, 2 * radio);
			if (listaFinales.contains(state.getEtiqueta()))
				gimg.drawOval(point.x - radio + 3, point.y - radio + 3,
						(radio - 3) << 1, (radio - 3) << 1);
			if ((estadoInicial != null ) && (estadoInicial.equals(state.getEtiqueta()))) {
				int[] x = { point.x - radio, point.x - (radio << 1),point.x - (radio << 1) };
				int[] y = { point.y, point.y - radio, point.y + radio };
				gimg.setColor(Color.white);
				gimg.fillPolygon(x, y, 3);
				gimg.setColor(Color.black);
				gimg.drawPolygon(x, y, 3);
			}
		}
        

		boolean esPila = getPila();
		boolean esTuring = getTuring();
		
		if (esPila) pintaAristaPilaJPG(imagen);
		else if (esTuring) pintaAristaTuringJPG(imagen);
		else pintaAristaJPG(imagen);

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

        // Se crea un flujo de datos, en este caso será FileOutPutStream, aunque
        // podés utilizar cualquier otro.

        FileOutputStream out;
        try {
            out = new FileOutputStream(new File(ruta));

            // Se decodifica la imagen y se envía al flujo.
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
        return ruta;
	}
	

	public void pintaAristaJPG(BufferedImage imagen){
		
		Iterator<Arista> itArist=listaAristas.iterator();
		Graphics gimg = imagen.getGraphics();
		
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

        // Se crea un flujo de datos, en este caso será FileOutPutStream, aunque
        // podés utilizar cualquier otro.

   //     FileOutputStream out;
		
	}
	
	public void pintaAristaTuringJPG(BufferedImage imagen){
		if (!listaAristasTuring.isEmpty()){	
			Graphics gimg = imagen.getGraphics();
			int numAristas = listaAristasTuring.size();
			int j = 0;
			int i = 0;
			String etiquetaAux = "";// String etiqueta = "";
			AristaTuring aristaT = listaAristasTuring.get(i);
			while (i < numAristas){

				java.awt.Graphics2D g2=(java.awt.Graphics2D)gimg; 
				Stroke s=g2.getStroke();
				g2.setStroke(STROKE);
				g2.setColor(Color.black);
			
				if ( !aristaT.getMarcada() ){ 
					//Mensajero m=Mensajero.getInstancia();	
					String separador = m.devuelveMensaje("simbolos.separador",4);
					if (etiquetaAux.equals("")){ 
						etiquetaAux = dameEtiqueta(aristaT.getEntradaCinta(),0) + separador + aristaT.getSimboloCinta() + separador + /*dameEtiqueta(*/aristaT.getDireccion()/*)*/;
						j = i+1;
					}
				
					if (j < numAristas){ // falta llave
				
						AristaTuring aux = listaAristasTuring.get(j);
						boolean origen,destinos;		
						destinos = aristaT.getDestino().equals( aux.getDestino());
						origen = aristaT.getOrigen().equals( aux.getOrigen());

						if (destinos && origen && !aux.getMarcada() ) {
							etiquetaAux += " , " + dameEtiqueta(aux.getEntradaCinta(),0) + separador + aux.getSimboloCinta() + separador + /*dameEtiqueta(*/aux.getDireccion()/*SalidaPila())*/;
							aux.setMarcada(true);
						//j++;
						}
					
					}
							
					if	(j >= numAristas){
		
						if (aristaT.getX()>aristaT.getFx()) {
							CurvedArrow curva=null;
							if(aristaT.getOrigen().equals(aristaT.getDestino()))curva=new CurvedArrow(aristaT.getX()+20,aristaT.getY()+8,aristaT.getFx()-20,aristaT.getFy()+8, 3);
							else {
								int angulo=0;//2;
							//if (esUnicaAP(aristaAP)) angulo=0;
								if (aristaT.getY()>aristaT.getFy())
									curva=new CurvedArrow(aristaT.getX()-15,aristaT.getY()-15,aristaT.getFx()+12,aristaT.getFy()+19, angulo);
								else curva=new CurvedArrow(aristaT.getX()-15,aristaT.getY()+15,aristaT.getFx()+12,aristaT.getFy()-19, angulo);
							}
							curva.draw((Graphics2D) gimg);
							curva.setLabel(/*etiqueta*/ etiquetaAux);
							curva.drawText(gimg);
						}
						else {
							CurvedArrow curva=null;
							if(aristaT.getOrigen().equals(aristaT.getDestino()))curva=new CurvedArrow(aristaT.getX()+20,aristaT.getY()+8,aristaT.getFx()-20,aristaT.getFy()+8, 3);
							else {
								int angulo=0;
								if (aristaT.getY()>aristaT.getFy()) 
									curva=new CurvedArrow(aristaT.getX()+15,aristaT.getY()-15,aristaT.getFx()-12,aristaT.getFy()+19, angulo);
								else curva=new CurvedArrow(aristaT.getX()+15,aristaT.getY()+15,aristaT.getFx()-12,aristaT.getFy()-19,angulo);
							}
							curva.draw((Graphics2D) gimg);
							curva.setLabel(/*etiqueta*/ etiquetaAux);
							curva.setLabel(/*etiqueta*/ etiquetaAux);
							curva.drawText(gimg);
						}
						g2.setStroke(s);
					}
				}
			
				if (j >= numAristas){ 
					i++; 
					j = i+1;  
					etiquetaAux = "";
					if (i < listaAristasTuring.size()) 	aristaT = listaAristasTuring.get(i);
				}
				else j++; 
			}
		}//llave if ArrayList vacio}
	}
	
	public void pintaAristaPilaJPG(BufferedImage imagen){
		

		
		if (!listaAristasAP.isEmpty()){	
			int numAristas = listaAristasAP.size();
			int j = 0;
			int i = 0;
			String etiquetaAux = "";// String etiqueta = "";
			AristaAP aristaAP = listaAristasAP.get(i);
			Graphics gimg = imagen.getGraphics();
			while (i < numAristas){

				java.awt.Graphics2D g2=(java.awt.Graphics2D)gimg; 
				Stroke s=g2.getStroke();
				g2.setStroke(STROKE);
				g2.setColor(Color.black);
			
				if ( !aristaAP.getMarcada() ){ 
					//Mensajero m=Mensajero.getInstancia();	
					String separador = m.devuelveMensaje("simbolos.separador",4);
					if (etiquetaAux.equals("")){ 
						etiquetaAux = dameEtiqueta(aristaAP.getEntradaSimbolos(),0) + separador + aristaAP.getCimaPila() + separador + dameEtiqueta(aristaAP.getSalidaPila(),1);
						j = i+1;
					}
				
					if (j < numAristas){ // falta llave
				
						AristaAP aux = listaAristasAP.get(j);
						boolean origen,destinos;		
						destinos = aristaAP.getDestino().equals( aux.getDestino());
						origen = aristaAP.getOrigen().equals( aux.getOrigen());

						if (destinos && origen && !aux.getMarcada() ) {
							etiquetaAux += " , " + dameEtiqueta(aux.getEntradaSimbolos(),0) + separador + aux.getCimaPila() + separador + dameEtiqueta(aux.getSalidaPila(),1);
							aux.setMarcada(true);
						//j++;
						}
					
					}
							
					if	(j >= numAristas){
		
						if (aristaAP.getX()>aristaAP.getFx()) {
							CurvedArrow curva=null;
							if(aristaAP.getOrigen().equals(aristaAP.getDestino()))curva=new CurvedArrow(aristaAP.getX()+20,aristaAP.getY()+8,aristaAP.getFx()-20,aristaAP.getFy()+8, 3);
							else {
								int angulo=0;//2;
							//if (esUnicaAP(aristaAP)) angulo=0;
								if (aristaAP.getY()>aristaAP.getFy())
									curva=new CurvedArrow(aristaAP.getX()-15,aristaAP.getY()-15,aristaAP.getFx()+12,aristaAP.getFy()+19, angulo);
								else curva=new CurvedArrow(aristaAP.getX()-15,aristaAP.getY()+15,aristaAP.getFx()+12,aristaAP.getFy()-19, angulo);
							}
							curva.draw((Graphics2D) gimg);
							curva.setLabel(/*etiqueta*/ etiquetaAux);
							curva.drawText(gimg);
						}
						else {
							CurvedArrow curva=null;
							//System.out.println("ARISTA A PINTAR: " + aristaAP);
							if(aristaAP.getOrigen().equals(aristaAP.getDestino()))curva=new CurvedArrow(aristaAP.getX()+20,aristaAP.getY()+8,aristaAP.getFx()-20,aristaAP.getFy()+8, 3);
							else {
								int angulo=0;
								if (aristaAP.getY()>aristaAP.getFy()) 
									curva=new CurvedArrow(aristaAP.getX()+15,aristaAP.getY()-15,aristaAP.getFx()-12,aristaAP.getFy()+19, angulo);
								else curva=new CurvedArrow(aristaAP.getX()+15,aristaAP.getY()+15,aristaAP.getFx()-12,aristaAP.getFy()-19,angulo);
							}
							curva.draw((Graphics2D) gimg);
							curva.setLabel(/*etiqueta*/ etiquetaAux);
							curva.setLabel(/*etiqueta*/ etiquetaAux);
							curva.drawText(gimg);
						}
						g2.setStroke(s);
					}
				}
			
				if (j >= numAristas){ 
					i++; 
					j = i+1;  
					etiquetaAux = "";
					if (i < listaAristasAP.size()) 	aristaAP = listaAristasAP.get(i);
				}
				else j++; 
			}
		}//llave if ArrayList vacio
		
	}
	
	/**
	 * Repinta el panel(si tiene automata) llama a repaint(graphics)
	 */
	public void repaint(){
		if (listaEstados==null) super.repaint();
		else repaint(getGraphics());
	}
	
	/**
	 * Decide si el punto pont esta en alguna de las aristas
	 * @param point punto en el que se busca na ariata
	 * @return Arista si hay alguna, sino null
	 */
	public AristaGeneral aristaEn(Point point) {
		// TODO Auto-generated method stub
		Iterator<?> iArist  = null; //Iterator<Arista> iArist
		if (!listaAristas.isEmpty())iArist = listaAristas.iterator();
		else if (!listaAristasAP.isEmpty()) iArist = listaAristasAP.iterator();
		else if (!listaAristasTuring.isEmpty()) iArist = listaAristasTuring.iterator();
		
		if (iArist != null)
		while(iArist.hasNext()){
			AristaGeneral a = (AristaGeneral)iArist.next();
			//Arista a = iArist.next();
			
			CurvedArrow curva=null;
			if (a.getX()>a.getFx()) {
				if(a.getOrigen().equals(a.getDestino()))curva=new CurvedArrow(a.getX()+20,a.getY()+8,a.getFx()-20,a.getFy()+8, 3);
				else {
					int angulo=2;
					if (a instanceof Arista) if (esUnica((Arista)a)) angulo=0;
					//if (a instanceof AristaAP) System.out.println("MUAHAHAHAAH");
					if (a.getY()>a.getFy())
						curva=new CurvedArrow(a.getX()-15,a.getY()-15,a.getFx()+15,a.getFy()+15, angulo);
					else curva=new CurvedArrow(a.getX()-15,a.getY()+15,a.getFx()+15,a.getFy()-15, angulo);
				}
			}
			else {
				if(a.getOrigen().equals(a.getDestino()))curva=new CurvedArrow(a.getX()+20,a.getY()+8,a.getFx()-20,a.getFy()+8, 3);
				else {
					int angulo=1;
					if (a instanceof Arista) if (esUnica((Arista)a)) angulo=0;
					if ((a instanceof AristaAP) || (a instanceof AristaTuring)){angulo=0; /*System.out.println("MUAHAHAHAAH");*/}
					if (a.getY()>a.getFy()) 
						curva=new CurvedArrow(a.getX()+15,a.getY()-15,a.getFx()-15,a.getFy()+15, angulo);
					else curva=new CurvedArrow(a.getX()+15,a.getY()+15,a.getFx()-15,a.getFy()-15,angulo);
				}
			}
		//	System.out.println("PUNTO RAYANTE: " + point);
			if(curva.isNear(point,2)){  
				
				return /*(Arista)*/ a; 
			}
		}	
		return null;
	}
	/**
	 * Decide si point esta contenido en algun estado
	 * @param point punto en el que se busca un estado
	 * @return Estado si hay estado, sino null
	 */
	public Estado estadoEn(Point point) {
		// TODO Auto-generated method stub
		Iterator<Estado> it=listaEstados.iterator();
		while(it.hasNext()){
			Estado e=it.next();
			if (point.distance(new Point(e.getX(),e.getY())) <= radio){
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Busca algun estado que se llame como etiqueta
	 * @param etiqueta etiqueta con el nombre del estado a buscar
	 * @return Estado que se busca si está, sino null
	 */
	public Estado buscaEstado(String etiqueta){
		Iterator<Estado> it=listaEstados.iterator();
		while(it.hasNext()){
			Estado e=it.next();
			if(e.getEtiqueta().equals(etiqueta)) return e;
		}
		return null;
	}
	
	
	private String seRepite(ArrayList<Arista> la, Arista a) {
		//Iterator<Arista> it=la.iterator();
		String repetida=a.getEtiqueta();
		int i=0;
		while(i<la.size()) {
			Arista aux=la.get(i);
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
	
	/*******************************************************************************************/
	

	
	
	// 0 para simbolos, 1 para transicion de pila
	private String dameEtiqueta(ArrayList<String> a, int tipo){
	
		Iterator<String> it = a.iterator();
		String r = it.next();
		while (it.hasNext()){
			if (tipo == 0)
				r+=",";
			//else r+=
			r += it.next();
		}
		return r;
	}
	
	
	
	
	
	/*******************************************************************************************/	
	
	private void desmarcarAristas(ArrayList<?>/*</*AristaObject>*/ la) {
		Iterator<?>/*<AristaObject>*/ it=la.iterator();
		while(it.hasNext()) {
			/*Arista*/Object aux=it.next();
			if (aux instanceof Arista)((Arista)aux).setMarcada(false);
			else if (aux instanceof AristaAP)((AristaAP)aux).setMarcada(false);
			if (aux instanceof AristaTuring)((AristaTuring)aux).setMarcada(false);
		}
	}

	/**
	 * Método que selecciona los estados que están dentro del rectángulo que recibe
	 * @param bounds rectángulo que va a mirar si contiene a los estados
	 * @return devuelve el número de estados seleccionados
	 */
	public int seleccionaEstadosEnElrectangulo(Rectangle bounds) {
		// TODO Auto-generated method stub
		int i=0;
		Iterator<Estado> iest=listaEstados.iterator();
		while(iest.hasNext()){
			Estado e=iest.next();
			e.setSelected(false);
			if(bounds.contains(new Point(e.getX(),e.getY()))){	
				e.setSelected(true);
				i++;
			}
		}
		return i;
	}

	/**
	 * Método que dibuja el rectángilo que recibe por parámetro
	 * @param bounds rectángulo a dibujar
	 */
	public void drawBounds(Rectangle bounds) {
		// TODO Auto-generated method stub
		Graphics g=getGraphics();
		g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
	}
	
	private void drawBackground(Graphics g, Estado state, Point point, Color color) {
		g.setColor(color);
		if(state.isSelected()) g.setColor(new Color(100, 200, 200));
			g.fillOval(point.x - radio, point.y - radio, 2 * radio,	2 * radio);
	}

	/**
	 * Método que deselecciona todos los estados del automata
	 */
	public void deseleccionaEstados() {
		// TODO Auto-generated method stub
		Iterator<Estado> ies=listaEstados.iterator();
		while(ies.hasNext()){
			ies.next().setSelected(false);
		}
	}

	private String tipoAutomata(){
		if(this.getPila()) return "AutomataPila";
		if(this.getTuring()) return "MaquinaTuring";
		//if(this.getTuring()) return "Turing";
		Automata auto=new AutomataFNDLambda();
		if(alfabeto.dameListaLetras().contains(OyenteArista.getLambda()))
			return "AutomataFNDLambda";
		auto.setAlfabeto(alfabeto);
		Iterator<Estado> iAut=listaEstados.iterator();
		while(iAut.hasNext()){
			Estado a=iAut.next();
			auto.insertaVertice(a.getEtiqueta());
		}
		Iterator<Arista> iArs=listaAristas.iterator();
		while(iArs.hasNext()){
			Arista a=iArs.next();
			auto.insertaArista(a.getOrigen(),a.getDestino(),a.getEtiqueta());
		}
		HashMap<String, HashMap<String,ArrayList<String>>> automata=auto.getAutomata();
		Iterator<String> itst=auto.getEstados().iterator();
		while(itst.hasNext()){
			String estado=itst.next();
			Iterator<String> iAlf=alfabeto.dameListaLetras().iterator();
			while(iAlf.hasNext()){
				String letra=iAlf.next();
				if (automata.get(estado)!=null) {
					if (automata.get(estado).get(letra)!=null)
						if(automata.get(estado).get(letra).size()>1) return "AutomataFND";
				}
			}
		}
		return "AutomataFD";
	}
	
	/**
	 * Borra del panel los estados seleccionados por el usuario
	 */
	public void borrarSeleccionados() {
		Iterator<Estado> itEst=listaEstados.iterator();
		ArrayList<Estado> listaN=new ArrayList<Estado>();
		while(itEst.hasNext()) {
			Estado e=itEst.next();
			if (!e.isSelected()) {
				listaN.add(e);
			} else {
				modificarRelacinesEstado(e);
			}
		}
		listaEstados=listaN;
	}
	

	private void modificarRelacinesEstado(Estado est) {
		// TODO Auto-generated method stub
	/*	ArrayList<Arista> lArist = null; ArrayList<AristaAP> lAristAP = null;
		Iterator<Arista> iArist = null; Iterator<AristaAP> iAristAP = null; 
		int tipo = -1;
		
		lArist=new ArrayList<Arista>();
		if (!getListaAristas().isEmpty()){
			lArist=new ArrayList<Arista>();
			iArist=getListaAristas().iterator(); 
			tipo = 0;
		}
		
		if (!getListaAristasPila().isEmpty()){
			lAristAP=new ArrayList<AristaAP>();
			iAristAP=getListaAristasPila().iterator(); 
			tipo = 1;
			
		}
    
		if(iArist != null || iAristAP != null)	{
			String etiqueta = est.getEtiqueta();
			if (iArist != null)
			while(iArist.hasNext()){
			//Arista a = null; AristaAP ap = null;
				Arista obj = iArist.next(); 
				
				
					if(!(obj.getDestino().equals(etiqueta))&&
							!(obj.getOrigen().equals(etiqueta))){
							lArist.add(obj);
					}
				
			}
			if (iAristAP != null){
				while(iAristAP.hasNext()){
				//Arista a = null; AristaAP ap = null;
					AristaAP obj = iAristAP.next(); 
					
					
						if(!(obj.getDestino().equals(etiqueta))&&
								!(obj.getOrigen().equals(etiqueta))){
								lAristAP.add(obj);
						}
					
				}
		}	
		Iterator<Estado> itEst = getListaEstados().iterator();
		ArrayList<Estado> nEstados = new ArrayList<Estado>();
		while (itEst.hasNext()){
			Estado e = itEst.next();
			if (!est.getEtiqueta().equals(e.getEtiqueta()))
				nEstados.add(e);
			
		}
		setListaEstados(nEstados);
		setListaAristas(lArist);

		setAlfabeto(minimizarAlfabeto());
		if(!getListaAristasPila().isEmpty()){
			setListaAristasAP(lAristAP);
			setAlfabetoPila(minimizarAlfabetoPila());
			getAP().setAPNuevo(lAristAP);
		}
		else{setListaAristas(lArist);}
	}*/
	}
	
	/**
	 * Método que calcula las letras del alfabto a partir de la lista
	 * de las aristas del automata
	 * @return el alfabeto del automata
	 */
	public Alfabeto minimizarAlfabeto(){
		Iterator<?> iA = null;
		if (m == null) m = Mensajero.getInstancia();
		String lambda = m.devuelveMensaje("simbolos.lambda",4);
		String blanco = m.devuelveMensaje("simbolos.blanco",4);
		if (!listaAristas.isEmpty()) iA=listaAristas.iterator();
		else if (!listaAristasAP.isEmpty()) iA=listaAristasAP.iterator();
		else if (!listaAristasTuring.isEmpty()) iA=listaAristasTuring.iterator();
		Alfabeto alf=new Alfabeto_imp();
		if (iA != null)
		while(iA.hasNext()){
			/*Arista*/Object a=iA.next();
			
			if (a instanceof Arista) {
				StringTokenizer st =null;
				st=new StringTokenizer(((Arista)a).getEtiqueta(),",");
				while(st.hasMoreTokens()){
					String ss=st.nextToken();
					if(!alf.estaLetra(ss)&&!lambda.equals(ss)){
						alf.aniadirLetra(ss);
					}
				}
			}
			if (a instanceof AristaAP) {
				
				for(int i = 0; i < ((AristaAP) a).getEntradaSimbolos().size(); i++){
					String sim = ((AristaAP) a).getEntradaSimbolos().get(i);
					if(!alf.estaLetra(sim)&&
							!lambda.equals(sim)){
						alf.aniadirLetra(sim);
					}					
					
				}

			}
			if (a instanceof AristaTuring) {
				
				for(int i = 0; i < ((AristaTuring) a).getEntradaCinta().size(); i++){
					String sim = ((AristaTuring) a).getEntradaCinta().get(i);
					if(!alf.estaLetra(sim)&&
							!blanco.equals(sim)){
						alf.aniadirLetra(sim);
					}					
					
				}

			}
		}
		return alf;
	}
	
	/**
	 * Método que calcula las letras del alfabto a partir de la lista
	 * de las aristas del automata
	 * @return el alfabeto del automata
	 */
	public Alfabeto_Pila minimizarAlfabetoPila(){
		Iterator<AristaAP> iA=listaAristasAP.iterator();
		Alfabeto_Pila alf = new AlfabetoPila_imp();
		if (m == null) m = Mensajero.getInstancia();
		String lambda = m.devuelveMensaje("simbolos.lambda",4);
		alf.aniadirLetra(m.devuelveMensaje("simbolos.cima",4));
		while(iA.hasNext()){
			AristaAP a=iA.next();
			String letra = a.getCimaPila();
			if (!alf.estaLetra(letra)&&!lambda.equals(letra)) alf.aniadirLetra(letra);
			int i = 0;
			ArrayList<String> s = a.getSalidaPila();
			while(i < s.size()){
				String ss= s.get(i);
				if(!alf.estaLetra(ss)&&!lambda.equals(ss)){
					alf.aniadirLetra(ss);
				} 
				i++;
			}
		}
		return alf;
	}
	/************************************************************/
	public AlfabetoCinta minimizarAlfabetoCinta(){
		Iterator<AristaTuring> iA=listaAristasTuring.iterator();
		AlfabetoCinta alf = new AlfabetoCinta();
		if (m == null) m = Mensajero.getInstancia();
		String blanco = m.devuelveMensaje("simbolos.blanco",4);
		alf.aniadirLetra(blanco);
		while(iA.hasNext()){
			AristaTuring a=iA.next();
			String letra = a.getSimboloCinta();
			if (!alf.estaLetra(letra)&&!blanco.equals(letra)) alf.aniadirLetra(letra);

		}
		return alf;
	}
	/************************************************************/
/*	private boolean contieneEntrada(AristaAP arista, AristaAP a){ 

		int tamA = a.getEntradaSimbolos().size(); 		
		int i = 0;
		while ( i < tamA){
			if (arista.getEntradaSimbolos().contains(a.getEntradaSimbolos().get(i))) return true;
			i++;
		}
		return false;
	}*/
	
	private boolean contieneEntrada(AristaTuring arista, AristaTuring a){ 

		int tamA = a.getEntradaCinta().size(); 		
		int i = 0;
		while ( i < tamA){
			if (arista.getEntradaCinta().contains(a.getEntradaCinta().get(i))) return true;
			i++;
		}
		return false;
	}
	
/*	private int existeTransicion(AristaAP a){
		
		
		if ( getListaAristasPila().isEmpty() ) return -1;
		else{
			int i = 0;
			
			while (i < getListaAristasPila().size() ){
				AristaAP aux = getListaAristasPila().get(i);
				boolean destinos = a.getDestino().equals( aux.getDestino());
				boolean origen = a.getOrigen().equals( aux.getOrigen());
				boolean cima = a.getCimaPila().equals( aux.getCimaPila());
				boolean salida = iguales(a.getSalidaPila(),aux.getSalidaPila());

				
				if ( destinos && origen && cima ){
					
					if (salida){ 
						combinarEntrada(aux, a.getEntradaSimbolos()); //XXX
						return i;
					}
					
					
					if (contieneEntrada(aux, a)) return -2;
					
				}	
				i++;
			}
			return -1;
			
		}
	}*/

	private int existeTransicion(AristaTuring a){
		
		
		if ( getListaAristasTuring().isEmpty() ) return -1;
		else{
			int i = 0;
			
			while (i < getListaAristasTuring().size() ){
				AristaTuring aux = getListaAristasTuring().get(i);
				boolean destinos = a.getDestino().equals( aux.getDestino());
				boolean origen = a.getOrigen().equals( aux.getOrigen());
				boolean simboloCinta = a.getSimboloCinta().equals( aux.getSimboloCinta());
				boolean direccion = a.getDireccion().equals(aux.getDireccion());
				
				if ( destinos && origen && simboloCinta ){
					
					if (direccion){ 
						combinarEntrada(aux, a.getEntradaCinta()); //XXX
						return i;
					}
					
					
					if (contieneEntrada(aux, a)) return -2;
					
				}	
				i++;
			}
			return -1;
			
		}
		
		

	}
	
/*	private void combinarEntrada(AristaAP a, ArrayList<String> e){
		
		Iterator<String> it = e.iterator();
		while (it.hasNext()){
			String s = it.next();
			if (!a.getEntradaSimbolos().contains(s))
				a.getEntradaSimbolos().add(s);
		}
		
	}*/
	
	private void combinarEntrada(AristaTuring a, ArrayList<String> e){
		
		Iterator<String> it = e.iterator();
		while (it.hasNext()){
			String s = it.next();
			if (!a.getEntradaCinta().contains(s))
				a.getEntradaCinta().add(s);
		}
		
	}
	
	
	/******************************************************************************/
	
/*	private boolean compruebaAPD(){
		
		int i = 0;

		boolean esta;
		while (i < listaAristasAP.size()){
			
			esta = listaAristasAP.get(i).getEntradaSimbolos().contains(OyenteArista.getLambda()); 
		
			if (esta && (listaAristasAP.get(i).getEntradaSimbolos().size() > 1) ) return false;
			else i++;
		}
		return true;
	}*/
	/******************************************************************************/
/*	private boolean iguales(ArrayList<String> a, ArrayList<String> b){
		
		if (a.size() != b.size()) return false;
		
		Iterator<String> itA = a.iterator();
		Iterator<String> itB = b.iterator();
		
		while (itA.hasNext()){

			if ( !itA.next().equals(itB.next()) )return false;
		}
		return true;
		
	}*/

	public void anadeAristaTuring(AristaTuring a){
		
		try{
			int i = existeTransicion(a);
			if ( i == -1 ) listaAristasTuring.add(a);
			else if (i == -2){
				Mensajero m=Mensajero.getInstancia();
				throw new AutomatasException(m.devuelveMensaje("canvas.notransvalida",2));
			}
			
		}								//cambiar tb lo de mensajes en ingles
		catch (AutomatasException ex){
			JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	public void anadeAristaAP(AristaAP a){
		
/*		try{
			int i = existeTransicion(a);
			if ( i == -1 ) listaAristasAP.add(a);
			else if (i == -2){
				Mensajero m=Mensajero.getInstancia();
				throw new AutomatasException(m.devuelveMensaje("canvas.notransvalida",2));
			}
			
			apd = compruebaAPD();*/
			ap.setAPNuevo(listaAristasAP);
			ap.anadeArista(a);
			listaAristasAP = ap.getAutomataPila();
		//	if(ap.getApd())System.out.println("DETERMINISTA!!!");
		//	else System.out.println("SH*T!!!");
			
		/*}								//cambiar tb lo de mensajes en ingles
		catch (AutomatasException ex){
			JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
		}*/
	}
	/********************************************************************************/
	/********************************************************************************/
	private int existeTransicion(Arista a){
		
		
		if ( getListaAristas().isEmpty() ) return -1;
		else{
			int i = 0;

			while (i < getListaAristas().size() ){
				Arista aux = getListaAristas().get(i);
				boolean destinos = a.getDestino().equals( aux.getDestino());
				boolean origen = a.getOrigen().equals( aux.getOrigen());
				boolean simbolos = a.getEtiqueta().equals(aux.getEtiqueta());

				
				if ( destinos && origen  ){
	
					if (simbolos) return -2;
					
				}	
				i++;
			}
			return -1;
			
		}
		
	}
		/********************************************************************************/
		/********************************************************************************/
	public void anadeArista(Arista a){
		

			int i = existeTransicion(a);
			if ( i == -1 ) listaAristas.add(a);

	}
	
	public void setListaAristasTuring(ArrayList<AristaTuring> lista){listaAristasTuring = lista;}
 
	public static void main(String[] args){
		
		//AutomataCanvas a = new AutomataCanvas(null);
		AutomataPila aut = new AutomataPila();
//		AutomataPila aut2 = new AutomataPila();
		//a.listaEstados.add(new Estado(0,0,"s1"));
		aut.getEstados().add("s1");
		aut.getEstados().add("s2");
		aut.getEstados().add("s3");
//		aut.getEstados().add("s4");
		aut.setEstadoInicial("s1");
		aut.setEstadoFinal("s3");
		aut.setEstadoFinal("s2");

/*		aut2.getEstados().add("s1");
		aut2.getEstados().add("s2");
		aut2.setEstadoInicial("s1");
		aut2.setEstadoFinal("s2");*/
		//System.out.println("ESTADOS: " + aut.getEstados());
		
		AristaAP arist;
		
		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("a");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		

		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("a");
		arist.setCimaPila("Z");
		arist.anadirPila("CZ");
		
		aut.anadeArista(arist);	
		
		arist = new AristaAP(0,0,0,0,"s1","s1");
		arist.anadirSimbolo("0");
		arist.setCimaPila("C");
		arist.anadirPila("CC");
		
		aut.anadeArista(arist);	
	
		arist = new AristaAP(0,0,0,0,"s2","s2");
		arist.anadirSimbolo("1");
		arist.setCimaPila("C");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);
		
		arist = new AristaAP(0,0,0,0,"s1","s3");
//		arist.anadirSimbolo("0");
		arist.anadirSimbolo("1");
		arist.setCimaPila("C");
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);		

//		AristaAP borrada = 	aut.getAutomataPila().remove(2);
//		System.out.println("ARISTA borrada: " + borrada );

/*		arist = new AristaAP(0,0,0,0,"s3","s2");
		//arist.anadirSimbolo("0");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		
		arist = new AristaAP(0,0,0,0,"s3","s4");
		arist.anadirSimbolo("0");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		
		arist = new AristaAP(0,0,0,0,"s3","s4");
		arist.anadirSimbolo("0");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist); 
		
//		aut2.anadeArista(arist);

		
		arist = new AristaAP(0,0,0,0,"s1","s2");
//		arist.anadirSimbolo("0");
		arist.anadirSimbolo("1");
		arist.setCimaPila("A");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);
		
/*		arist = new AristaAP(0,0,0,0,"s3","s2");
		arist.anadirSimbolo("\\");
		arist.setCimaPila("Z");
		arist.anadirPila("Z");

		
		aut.anadeArista(arist);
		

		
	/*	arist = new AristaAP(0,0,0,0,"s2","s2");
		arist.anadirSimbolo("b");
		arist.setCimaPila("Z"); 
		arist.anadirPila("\\");
		
		aut.anadeArista(arist);*/
		
/*		arist = new AristaAP(0,0,0,0,"s3","s4");
		arist.anadirSimbolo("\\");
		//System.out.println("LAMBDA: \\" );
		arist.setCimaPila("Z");
		arist.anadirPila("Z");
		
		aut.anadeArista(arist);*/
		 //una vez ordenado no puedes desordenar
		//System.out.println("ARISTAS: " + aut.getAutomataPila());

/*		ArrayList<String> lp = new  ArrayList<String>();
		lp.add("0");
		lp.add("000");
		lp.add("00");*/
	
//		AutomataPila.compruebaPalabras(aut, aut2, lp);
/*		Alfabeto_Pila alf = new AlfabetoPila_imp();
		alf.aniadirLetra("Z");
		alf.aniadirLetra("C");
		aut.setAlfabetoPila(alf);*/
		aut.reconocePalabra("0101"/*, true*/);
		
		//a.anadeAristaAP(arist);
		/*arist = new AristaAP(0,0,0,0,"S1","S1");
		arist.anadirSimbolo("b");
		arist.setCimaPila("X");
		arist.anadirPila("X");*/
		//a.anadeAristaAP(arist);
		
		//a.pintaAristaPila(a.getGraphics());
	}

}