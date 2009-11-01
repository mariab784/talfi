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
import modelo.automatas.Alfabeto_imp;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFNDLambda;

/**
 * Clase que almacena y trata toda la informaci�n de los automatas que salen en la interfaz
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
	private Vista vista;
	private ArrayList<Estado> listaEstados;
	private ArrayList<Arista> listaAristas;
	private String estadoInicial;
	private ArrayList<String> listaFinales;
	private Alfabeto alfabeto;
	
	/**
	 * Constructor por defecto de la clase
	 * @param v Vista, clase que corresponde a la interfaz grafica
	 * 
	 */
	public AutomataCanvas(Vista v){
		this.setAutoscrolls(true);
		estadoB=0;
		vista=v;
		listaEstados=new ArrayList<Estado>();
		listaAristas=new ArrayList<Arista>();
		listaFinales=new ArrayList<String>();
		estadoInicial=new String();
		CanvasMouseAdapter c=new CanvasMouseAdapter(this);
		addMouseListener(c);
		OyenteEditar editar=new OyenteEditar(this);
		OyenteArista arista=new OyenteArista(this,c);
		addMouseListener(editar);
		addMouseListener(arista);
		addMouseMotionListener(editar);
		addMouseMotionListener(arista);
	}
	
	/**
	 * Devuelve el alfabeto del automata
	 * @return alfabeto del automata
	 */
	public Alfabeto getAlfabeto() {
		return alfabeto;
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
	 * Establece la lista de aristas
	 * @param listaAristas nueva lista de aristas el automata
	 */
	public void setListaAristas(ArrayList<Arista> listaAristas) {
		this.listaAristas = listaAristas;
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
	 * M�todo accesor del boton de dibujo que est� pulsado, depende del entero
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

	/**
	 * Pinta el automata en la interfaz grafica
	 * @param g clase graphics para pintar sobre el panel
	 */
	public void paint(Graphics g){
		//g.clearRect(0,0,2000,2000);
		desmarcarAristas(listaAristas);
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
			if (estadoInicial.equals(state.getEtiqueta())) {
				int[] x = { point.x - radio, point.x - (radio << 1),point.x - (radio << 1) };
				int[] y = { point.y, point.y - radio, point.y + radio };
				g.setColor(Color.white);
				g.fillPolygon(x, y, 3);
				g.setColor(Color.black);
				g.drawPolygon(x, y, 3);
			}
		}
		Iterator<Arista> itArist=listaAristas.iterator();
		while(itArist.hasNext()) {
			Arista a=itArist.next();
			java.awt.Graphics2D g2=(java.awt.Graphics2D)g; 
			Stroke s=g2.getStroke();
			g2.setStroke(STROKE);
			g2.setColor(Color.black);
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
				curva.setLabel(etiqueta);
				if (noPintar!=true) {
					curva.setLabel(etiqueta);
					curva.drawText(g);
				}
			}
			noPintar=false;
			g2.setStroke(s);
		}
		vista.requestFocus();
	}

	/**
	 * Repinta el automata en la interfaz grafica
	 * @param g objeto grafico donde se pinta
	 */
	public void repaint(Graphics g){
		desmarcarAristas(listaAristas);
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
			if (estadoInicial.equals(state.getEtiqueta())) {
				int[] x = { point.x - radio, point.x - (radio << 1),point.x - (radio << 1) };
				int[] y = { point.y, point.y - radio, point.y + radio };
				g.setColor(Color.white);
				g.fillPolygon(x, y, 3);
				g.setColor(Color.black);
				g.drawPolygon(x, y, 3);
			}
		}
		Iterator<Arista> itArist=listaAristas.iterator();
		while(itArist.hasNext()) {
			Arista a=itArist.next();
			java.awt.Graphics2D g2=(java.awt.Graphics2D)g; 
			Stroke s=g2.getStroke();
			g2.setStroke(STROKE);
			g2.setColor(Color.black);
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
				curva.setLabel(etiqueta);
				if (noPintar!=true) {
					curva.setLabel(etiqueta);
					curva.drawText(g);
				}
			}
			noPintar=false;
			g2.setStroke(s);
		}
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
	 * @throws AutomatasException lanza la excepci�n si el qutomata no tiene estado inicial
	 */
	public String traducirXML()throws AutomatasException {
		Mensajero m=Mensajero.getInstancia();
		if(!this.estadoInicial.equals("")&&!(this.estadoInicial==null)){
			String fichero="<authomata>\n\t<type>\n\t\t<item>";
			fichero+=tipoAutomata()+"</item>\n\t</type>\n";
			Iterator<Arista> itArist=listaAristas.iterator();
			//se genera el alfabeto para el xml
			while(itArist.hasNext()) {
				String letra=itArist.next().getEtiqueta();
				if (!alfabeto.dameListaLetras().contains(letra)) {
					alfabeto.aniadirLetra(letra);
				}
			}
			if(alfabeto!=null){
				Iterator<String> itAlfabeto=alfabeto.dameListaLetras().iterator();
				fichero+="\t<alphabet>\n\t";
				while(itAlfabeto.hasNext()) {
					fichero+="\t<item>"+itAlfabeto.next()+"</item>\n\t";
				}
				fichero+="</alphabet>\n\t";
				//se genera la lista de estados y se guarda en el xml
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
				Iterator<Arista> itArista=listaAristas.iterator();
				while(itArista.hasNext()) {
					Arista a=itArista.next();
					fichero+="\t<arrow>\n\t";
					fichero+="\t<state>"+a.getOrigen()+"</state>\n\t";
					fichero+="\t<state>"+a.getDestino()+"</state>\n\t";
					fichero+="\t<item>"+a.getEtiqueta()+"</item>\n\t</arrow>\n\t";
				}
				fichero+="</arrows>\n";
				////////////////////////////////////////////////////////////////////
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
				////////////////////////////////////////////////////////////////////
				fichero+="</authomata>";
				//fuera hay que decidir donde se guarda el fichero
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
	public void cargarAutomata(String rutaXml) {
		ParserXML parser=new ParserXML();		
	    try{
	    	cargarAutomataNuevo(parser.extraerAutomata(rutaXml));
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
		//el controlador el que lo pasa a la interfaz grafica, y �sta a esta clase: AutomataCanvas
		//IDEA: generar un poligono regular a partir de los vertices, el radio
		//del pol�gono deberia aumentar dependiendo del numero de vertives
		borrarPanel();
		this.alfabeto=a.getAlfabeto();
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
		String brr=new Character((char)92).toString();
		String ruta=System.getProperty("user.dir")+brr+"HTML"+brr+"imagen.jpg";
		generarImagenJPg(ruta);
		this.repaint();
		vista.requestFocus();
	}
	/**
	 * Borra el automata contenido en el panel
	 */
	public void borrarPanel() {
		// TODO Auto-generated method stub
		listaEstados=new ArrayList<Estado>();
    	listaAristas=new ArrayList<Arista>();
    	listaFinales=new ArrayList<String>();
    	estadoInicial="";
    	this.repaint();
    	vista.requestFocus();
	}
	
	/**
	 * Genera una imagen jpg con la informacion del automata
	 * @return ruta de la imagen jpg
	 */ 
	public String generarImagenJPg(String ruta) {
		
		desmarcarAristas(listaAristas);
        // Le asignamos un tama�o a la imagen
        int width = 768, height = 512;

        // Creamos una imagen con ese tama�o y con su correspondiente formato de
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

        // Se crea un flujo de datos, en este caso ser� FileOutPutStream, aunque
        // pod�s utilizar cualquier otro.

        FileOutputStream out;
        try {
            out = new FileOutputStream(new File(ruta));

            // Se decodifica la imagen y se env�a al flujo.
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
	public Arista aristaEn(Point point) {
		// TODO Auto-generated method stub
		Iterator<Arista> iArist=listaAristas.iterator();
		while(iArist.hasNext()){
			Arista a=iArist.next();
			CurvedArrow curva=null;
			if (a.getX()>a.getFx()) {
				if(a.getOrigen().equals(a.getDestino()))curva=new CurvedArrow(a.getX()+20,a.getY()+8,a.getFx()-20,a.getFy()+8, 3);
				else {
					int angulo=2;
					if (esUnica(a)) angulo=0;
					if (a.getY()>a.getFy())
						curva=new CurvedArrow(a.getX()-15,a.getY()-15,a.getFx()+15,a.getFy()+15, angulo);
					else curva=new CurvedArrow(a.getX()-15,a.getY()+15,a.getFx()+15,a.getFy()-15, angulo);
				}
			}
			else {
				if(a.getOrigen().equals(a.getDestino()))curva=new CurvedArrow(a.getX()+20,a.getY()+8,a.getFx()-20,a.getFy()+8, 3);
				else {
					int angulo=1;
					if (esUnica(a)) angulo=0;
					if (a.getY()>a.getFy()) 
						curva=new CurvedArrow(a.getX()+15,a.getY()-15,a.getFx()-15,a.getFy()+15, angulo);
					else curva=new CurvedArrow(a.getX()+15,a.getY()+15,a.getFx()-15,a.getFy()-15,angulo);
				}
			}
			if(curva.isNear(point,2)){
				return a;
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
	 * @return Estado que se busca si est�, sino null
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
	
	private void desmarcarAristas(ArrayList<Arista> la) {
		Iterator<Arista> it=la.iterator();
		while(it.hasNext()) {
			Arista aux=it.next();
			aux.setMarcada(false);
		}
	}

	/**
	 * M�todo que selecciona los estados que est�n dentro del rect�ngulo que recibe
	 * @param bounds rect�ngulo que va a mirar si contiene a los estados
	 * @return devuelve el n�mero de estados seleccionados
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
	 * M�todo que dibuja el rect�ngilo que recibe por par�metro
	 * @param bounds rect�ngulo a dibujar
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
	 * M�todo que deselecciona todos los estados del automata
	 */
	public void deseleccionaEstados() {
		// TODO Auto-generated method stub
		Iterator<Estado> ies=listaEstados.iterator();
		while(ies.hasNext()){
			ies.next().setSelected(false);
		}
	}

	private String tipoAutomata(){
		Automata auto=new AutomataFNDLambda();
		if(alfabeto.dameListaLetras().contains("/"))
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
		ArrayList<Arista> lArist=new ArrayList<Arista>();
		Iterator<Arista> iArist=listaAristas.iterator();
		while(iArist.hasNext()){
			Arista a=iArist.next();
			if(!(a.getDestino().equals(est.getEtiqueta()))&&!(a.getOrigen().equals(est.getEtiqueta()))){
				lArist.add(a);
			}		
		}
		setListaAristas(lArist);
		setAlfabeto(minimizarAlfabeto());
	}
	
	/**
	 * M�todo que calcula las letras del alfabto a partir de la lista
	 * de las aristas del automata
	 * @return el alfabeto del automata
	 */
	public Alfabeto minimizarAlfabeto(){
		Iterator<Arista> iA=listaAristas.iterator();
		Alfabeto alf=new Alfabeto_imp();
		while(iA.hasNext()){
			Arista a=iA.next();
			StringTokenizer st=new StringTokenizer(a.getEtiqueta(),",");
			while(st.hasMoreTokens()){
				String ss=st.nextToken();
				if(!alf.estaLetra(ss)){
					alf.aniadirLetra(ss);
				}
			}
		}
		return alf;
	}

}