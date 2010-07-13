package vista.vistaGrafica.events;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import accesoBD.Mensajero;
import modelo.AutomatasException;
import modelo.automatas.AlfabetoCinta;
import modelo.automatas.AlfabetoPila_imp;
import modelo.automatas.Alfabeto_imp;
import vista.AristaInterface;
import vista.vistaGrafica.Arista;
import vista.vistaGrafica.AristaAP;
import vista.vistaGrafica.AristaTuring;
import vista.vistaGrafica.AutomataCanvas;
import vista.vistaGrafica.CurvedArrow;
import vista.vistaGrafica.Estado;
import java.io.*;

/**
 * Clase que se encarga de a�adir las aristas al dibujo si la infromaci�n
 * que se le pide al usuario es correcta 
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class OyenteArista extends MouseAdapter {

	private final int arista=4;
	private static Stroke STROKE = new java.awt.BasicStroke(2.4f);
	private static java.awt.Color COLOR = new java.awt.Color(.5f, .5f, .5f, .5f);
	
	private CanvasMouseAdapter mouse;
	private AutomataCanvas canvas;
	private Estado origen;
	private Estado destino;
	private Point punto;
	private String estadoOrigen;
	private String estadoDestino;
	private String nombreEstado;
	private String nombreArista;
	private JTextField nomArs;
	private JTextField nomCima;
	private JTextField nomTrans;
	private JTextField nomEst;
	private JComboBox desde;
	private JComboBox hasta;
	private JDialog dialog;
	private Mensajero mensajero;
	
	private AristaAP aristaAP;
	private AristaTuring aristaTuring;
	private static String lambda;
	private static String fondoPila;
	private static String blanco;

	
	/**
	 * Clase que crea el oyente de dibujar aristas y a�adirlas al dibujo
	 * @param c panel de dibujos sobre el que se pinta
	 * @param m oyente de a�adir y borrar estados y borrasr aristas, padre de este
	 */
	public OyenteArista(AutomataCanvas c,CanvasMouseAdapter m){
		super();
		canvas=c;
		mouse=m;
		mensajero = Mensajero.getInstancia();
	}
	
	/**
	 * Devuelve la ventana de di�logo del panel
	 * @return ventana de di�logo
	 */
	public JDialog getDialog(){
		return dialog;
	}
	
	/**
	 * Devuevle el panel de dibujos
	 * @return panel de dibujos de automatas
	 */
	public AutomataCanvas getCanvas() {
		return canvas;
	}
	
	/**
	 * M�todo que devuelve el componente gr�fico que contiene una lista con todos
	 * los estado como posibles inicios de la arista
	 * @return menu con los posibles estados de inicio que contiene todos lo estado
	 */
	public JComboBox getDesde() {
		return desde;
	}

	/**
	 * M�todo que devuelve el componente gr�fico que contiene una lista con todos
	 * los estado como posibles finales de la arista
	 * @return menu con los posibles estados de finalizaci�n que contiene todos lo estado
	 */
	public JComboBox getHasta() {
		return hasta;
	}

	/**
	 * M�todo que devuelve el nombre del estado origen de la arista
	 * @return nombre del estado origen
	 */
	public String getEstadoOrigen() {
		return estadoOrigen;
	}

	/**
	 * M�todo que establece el nombre del estado origen de la arista
	 * @param estadoOrigen nombre nuevo del estado origen
	 */
	public void setEstadoOrigen(String estadoOrigen) {
		this.estadoOrigen = estadoOrigen;
	}

	/**
	 * M�todo que devuelve el nombre del estado destino de la arista
	 * @return nombre del estado destino
	 */
	public String getEstadoDestino() {
		return estadoDestino;
	}

	/**
	 * M�todo que establece el nombre del estado destino de la arista
	 * @param estadoDestino nombre nuevo del estado destino
	 */
	public void setEstadoDestino(String estadoDestino) {
		this.estadoDestino = estadoDestino;
	}
	
	/**
	 * M�todo que devuelve el nombre del estado
	 * @return nombre del estado
	 */
	public String getNombreEstado() {
		return nombreEstado;
	}

	/**
	 * M�todo modificador del nombre del estado
	 * @param nombreEstado nuevo nombre del estado
	 */
	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	/**
	 * M�todo que devuelve el nombre de la arista, las letras de la mismo
	 * @return nombre de la arista
	 */
	public String getNombreArista() {
		return nombreArista;
	}

	/**
	 * M�todo modificador del nombre de la arista
	 * @param nombreArista nuevo nombre de la arista, las letras de la misma
	 */
	public void setNombreArista(String nombreArista) {
		this.nombreArista = nombreArista;
	}

	/**
	 * M�todo accesor del campo de texto donde se escribe el nombre de la arista, las
	 * letras de la misma
	 * @return el campo de texto del nombre de la arista
	 */
	public JTextField getNomArs() {
		return nomArs;
	}
	
	/**
	 * M�todo accesor del campo de texto donde se escribe el nombre de la arista, la
	 * cima de la misma
	 * @return el campo de texto del nombre de la arista
	 */
	public JTextField getNomCima() {
		return nomCima;
	}
	/**
	 * M�todo accesor del campo de texto donde se escribe el nombre de la arista, la
	 * transicion de la misma
	 * @return el campo de texto del nombre de la arista
	 */
	public JTextField getNomTrans() {
		return nomTrans;
	}
	
	/**
	 * M�todo modificador del campo de texto donde se escribe el nombre de la arista, las
	 * letras de la misma
	 * @param nomArs nuevo campo de texto del nombre de la arista
	 */
	public void setNomArs(JTextField nomArs) {
		this.nomArs = nomArs;
	}
	
	/**
	 * M�todo accesor del campo de texto donde se escribe el nombre del estado
	 * @return el campo de texto del nombre del estado
	 */
	public JTextField getNomEst() {
		return nomEst;
	}

	/**
	 * M�todo modificador del campo de texto donde se escribe el nombre del estado
	 * @param nomEst nuevo campo de texto del nombre del estado
	 */
	public void setNomEst(JTextField nomEst) {
		this.nomEst = nomEst;
	}
	
	public static String getLambda(){return lambda;}
	
	public static String getFondoPila(){return fondoPila;}
	
	public static String getBlanco(){return blanco;}
	/**
	 * M�todo que detecta la pulsaci�n sobre el panel, si debajo hay un estado
	 * y se ha pulsado el boton derecho se inicia la creaci�n de la arista
	 * @param e evento de pulsaci�n de tecla del rat�n
	 */
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(canvas.getEstadoB()!=arista) return;
		origen=canvas.estadoEn(e.getPoint());
		if(origen==null){
			return;
		}
		punto=new Point(origen.getX(),origen.getY());
	}
	
	/**
	 * M�todo que detecta que se ha soltado una tecla sobre el panel,
	 * si es la tecla izquierda y hab�a un estado origen al pulsarla y 
	 * se est� dibujando una arista el dibujo se completa pidiendo las
	 * letras que llevar� la arista
	 * @param e evento de soltado de tecla 
	 */

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		

		
		
		canvas.repaint();
		if(canvas.getEstadoB()!=arista) return;
		if(e.getButton()==MouseEvent.BUTTON1){
			if(origen==null)return;
			destino=canvas.estadoEn(e.getPoint());
			if(destino!=null){
					if(canvas.getPila()){dialog=createDialogAristaAP();}
					else if(canvas.getTuring() ){dialog=createDialogAristaTuring();}
					else {dialog=createDialogArista();}
				dialog.setSize(new Dimension(400,150));
				dialog.setVisible(true);
				canvas.repaint();
			}
			origen=null;
		}
	}

	/**
	 * M�todoq ue detecta que se est� arrastrnado el rat�n sobre el panel con uan tecla
	 * pulsada, si es la izquierda y hab�a un estado debajo al pulsarla se va dibujando
	 * la arista al arrastrar para a�adirse al soltar si es sobre otro estado.
	 */
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if(canvas.getEstadoB()!=arista) return;
		if (origen == null)
			return;
		punto = e.getPoint();
		canvas.repaint();
		draw(canvas.getGraphics());
	}
	
	/**
	 * M�todo que detecta que se ha pulsado y soltado una tecla del rat�n sobre el panel
	 * si se ha hecho con el boon detrecho y debajo hab�a un estado o una arista se 
	 * muestra l popUp menu correspondiente, sino se muestra un menui con las opciones 
	 * de copiar y pegar para automatas.
	 * @param e evento de pulsaci�n de tecla de rat�n
	 */
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//mensajero=Mensajero.getInstancia();
		if (e.getButton()==MouseEvent.BUTTON3){
			 Estado est=canvas.estadoEn(e.getPoint());
			 JPopupMenu menu;
			 if(est!=null){
				menu=new JPopupMenu();
				JMenuItem itemEst=new JMenuItem(mensajero.devuelveMensaje("popup.estado",1));
				itemEst.setToolTipText(mensajero.devuelveMensaje("tooltip.popUpest",1));
				menu.add(itemEst);
				itemEst.addActionListener(new OyenteItemPopupEstado(est,this));
				menu.show(canvas,e.getPoint().x,e.getPoint().y);
			}
			AristaInterface/*General*/ arist=canvas.aristaEn(e.getPoint()); /**************** OJO AKI *********************/
			if(arist!=null){
				menu=new JPopupMenu();
				JMenuItem itemArist=new JMenuItem(mensajero.devuelveMensaje("popup.arista",1));
				itemArist.setToolTipText(mensajero.devuelveMensaje("tooltip.popUpar",1));
				menu.add(itemArist);
				if (arist instanceof Arista)itemArist.addActionListener(new OyenteItemPopupArista((Arista)arist,this));
				else if (arist instanceof AristaAP){ 
					itemArist.addActionListener(new OyenteItemPopupAristaAP((AristaAP)arist,this));
					
				}
				else if (arist instanceof AristaTuring){ 
					itemArist.addActionListener(new OyenteItemPopupAristaTuring((AristaTuring)arist,this));
					
				}
				menu.show(canvas,e.getPoint().x,e.getPoint().y);
			}
			if(arist==null&&est==null&&canvas.getVista().dibujar()){
				menu=new JPopupMenu();
				JMenuItem copiar=new JMenuItem(mensajero.devuelveMensaje("vista.copiar",2));
				copiar.setToolTipText(mensajero.devuelveMensaje("tooltip.copiar",1));
				JMenuItem pegar=new JMenuItem(mensajero.devuelveMensaje("vista.pegar",2));
				pegar.setToolTipText(mensajero.devuelveMensaje("tooltip.pegar",1));
				JMenuItem cortar=new JMenuItem(mensajero.devuelveMensaje("vista.cortar",2));
				cortar.setToolTipText(mensajero.devuelveMensaje("tooltip.cortar",1));
				CopiarPegar cp=CopiarPegar.getInstancia();
				pegar.setEnabled(cp.sePuedePegar());
				menu.add(copiar);
				menu.add(cortar);
				menu.add(pegar);
				pegar.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Mensajero m=Mensajero.getInstancia();
						try {
							if(!canvas.getVista().dibujar()) return;
							if(canvas.getListaEstados().size()==0){
								CopiarPegar cp=CopiarPegar.getInstancia();
								canvas.cargarAutomata(cp.getAutomata());
							} else {
								JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.yaauto", 2),"Error",JOptionPane.ERROR_MESSAGE);
							}
						} catch(AutomatasException ex){
							JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				copiar.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Mensajero m=Mensajero.getInstancia();
						try {
							CopiarPegar cp=CopiarPegar.getInstancia();
							cp.setAutomata(canvas.traducirXML());
						} catch(AutomatasException ex){
							JOptionPane.showMessageDialog(null,ex.getMensaje()+"\n"+m.devuelveMensaje("vista.ercopiar", 2),"Error",JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				cortar.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Mensajero m=Mensajero.getInstancia();
						try {
							CopiarPegar cp=CopiarPegar.getInstancia();
							cp.setAutomata(canvas.traducirXML());
							canvas.borrarPanel();
						} catch(AutomatasException ex){
							JOptionPane.showMessageDialog(null,ex.getMensaje()+"\n"+m.devuelveMensaje("vista.ercopy", 2),"Error",JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				menu.show(canvas,e.getPoint().x,e.getPoint().y);
			}
		 }
	}
	
	private JDialog createDialogArista(){
		//Mensajero m=Mensajero.getInstancia();
		JOptionPane pane=new JOptionPane();
		//mensajero=Mensajero.getInstancia();
		JDialog d=pane.createDialog(null,mensajero.devuelveMensaje("vista.arist",2));
		JPanel panelD=new JPanel(new GridLayout(4,1));
		JPanel panelC=new JPanel(new GridLayout(1,4));
		JLabel etiqN=new JLabel(mensajero.devuelveMensaje("vista.sym",2));
		nomArs=new JTextField();
		nomArs.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					try{
						nombreArista=nomArs.getText();
						StringTokenizer st=new StringTokenizer(nomArs.getText(),",");
						while(st.hasMoreTokens()){
							String ss=st.nextToken();
							if(canvas.getAlfabeto()==null) 
								canvas.setAlfabeto(new Alfabeto_imp());
							if(!canvas.getAlfabeto().estaLetra(ss)){
								canvas.getAlfabeto().aniadirLetra(ss);
							} 
							canvas.anadeArista(new Arista(origen.getX(),origen.getY(),destino.getX(),destino.getY(),ss,origen.getEtiqueta(),destino.getEtiqueta()));	
						}
						dialog.setVisible(false);
					} catch(NullPointerException ex){
						JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		JPanel panelB=  new JPanel();
		JButton aceptar=new JButton(mensajero.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					nombreArista=nomArs.getText();
					StringTokenizer st=new StringTokenizer(nomArs.getText(),",");
					while(st.hasMoreTokens()){
						String ss=st.nextToken();
						if(canvas.getAlfabeto()==null) canvas.setAlfabeto(new Alfabeto_imp());
						if(!canvas.getAlfabeto().estaLetra(ss)){
							canvas.getAlfabeto().aniadirLetra(ss);
						}
						canvas.getListaAristas().add(new Arista(origen.getX(),origen.getY(),destino.getX(),destino.getY(),ss,origen.getEtiqueta(),destino.getEtiqueta()));
					}
					dialog.setVisible(false);
				} catch(NullPointerException ex){
					JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JButton cancelar=new JButton(mensajero.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
			}
		});
		panelB.add(aceptar);
		panelB.add(cancelar);
		panelD.add(etiqN);
		panelD.add(nomArs);
		panelD.add(panelC);
		panelD.add(panelB);
		d.setContentPane(panelD);
		d.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		return d;
	}
	/**
	 * Clase que se encarga de preparar la transicion de automatas de pila
	 * @param a arista de la que se extrae la infromaci�n que se muestra
	 * @author Roc�o Barrig�ete, Mario Huete, Luis San Juan
	 *
	 */
	
	private JDialog createDialogAristaTuring(){
		//Mensajero m=Mensajero.getInstancia();
		JOptionPane pane=new JOptionPane();
		//mensajero=Mensajero.getInstancia();
		blanco = mensajero.devuelveMensaje("simbolos.blanco",4);
		JDialog d=pane.createDialog(null,mensajero.devuelveMensaje("vista.arist",2));
		JPanel panelD=new JPanel(new GridLayout(4,1));
		JPanel panelC=new JPanel(new GridLayout(1,4));
		JLabel etiqN=new JLabel(mensajero.devuelveMensaje("vista.sym3",2));
		nomArs=new JTextField();
		nomArs.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					actionAristaTuring();
				}
			}
		});
		JPanel panelB=  new JPanel();
		JButton aceptar=new JButton(mensajero.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionAristaTuring();
			}
		});
		JButton cancelar=new JButton(mensajero.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
			}
		});
		panelB.add(aceptar);
		panelB.add(cancelar);
		panelD.add(etiqN);
		panelD.add(nomArs);
		panelD.add(panelC);
		panelD.add(panelB);
		d.setContentPane(panelD);
		d.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		return d;
	}
	
	
	/**
	 * Extraer la informacion de el simbolo de cinta consumido en una transicion de maquina de turing
	 * @author Roc�o Barrig�ete, Mario Huete, Luis San Juan
	 *
	 */
	private void actionAristaTuring(){ ///////////*********************************/////////////////
		
		try{
			aristaTuring = new AristaTuring(origen.getX(),origen.getY(),destino.getX(),destino.getY(),origen.getEtiqueta(),destino.getEtiqueta());
			
			nombreArista=nomArs.getText();
			if(nombreArista.isEmpty()) throw new NullPointerException();
			StringTokenizer st=new StringTokenizer(nomArs.getText(),",");
			//aristaAP = new AristaAP(origen.getX(),origen.getY(),destino.getX(),destino.getY(),origen.getEtiqueta(),destino.getEtiqueta());
			while(st.hasMoreTokens()){
				String ss=st.nextToken().trim();


				aristaTuring.anadirSimboloCintaEntrada(ss);
			}
			
			//meteSimbolosAPTuring(1);
			dialog.setVisible(false);
			
			
			//dialog.setVisible(false);
			dialogCintaTuring();
		} catch(NullPointerException ex){
			JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Extraer la informacion de las simbolos de entrada consumidos en una transicion automata de pila
	 * @author Roc�o Barrig�ete, Mario Huete, Luis San Juan
	 *
	 */
	
	private void actionAristaAP(){

		try{
			nombreArista=nomArs.getText();
			if(nombreArista.isEmpty()) throw new NullPointerException();
			StringTokenizer st=new StringTokenizer(nomArs.getText(),",");
			aristaAP = new AristaAP(origen.getX(),origen.getY(),destino.getX(),destino.getY(),origen.getEtiqueta(),destino.getEtiqueta());
			while(st.hasMoreTokens()){
				String ss=st.nextToken().trim();

				aristaAP.anadirSimbolo(ss);
			}
			dialog.setVisible(false);
			dialogCimaPila();
		} catch(NullPointerException ex){
			JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/**
	 * Extraer la informacion de la cima de la pila en una transicion automata de pila
	 * @author Roc�o Barrig�ete, Mario Huete, Luis San Juan
	 *
	 */
	
	private void actionDireccionTuring(){ 
		
		try{
			nombreArista=nomArs.getText().trim();
			if(nombreArista.isEmpty()) throw new NullPointerException();
								aristaTuring.setDireccion(nombreArista);
				canvas.anadeAristaTuring(aristaTuring); //REVISAR
				
				Iterator<String> its = aristaTuring.getEntradaCinta().iterator();
				while(its.hasNext()){
					String ss = its.next();
				if(canvas.getAlfabeto()==null) canvas.setAlfabeto(new Alfabeto_imp()); //alfabeto maquina (sin #)
				if(!canvas.getAlfabeto().estaLetra(ss) && !ss.equals(blanco)){
					canvas.getAlfabeto().aniadirLetra(ss);
				} 
				}
				if(canvas.getAlfabetoCinta()==null) 
					canvas.setAlfabetoCinta(new AlfabetoCinta());
				
				if(!canvas.getAlfabetoCinta().estaLetra(aristaTuring.getSimboloCinta())){
					canvas.getAlfabetoCinta().aniadirLetra(aristaTuring.getSimboloCinta());
				}
				
				dialog.setVisible(false);
				
				
			//dialogTransicionPila();
		} catch(NullPointerException ex){ //METER ERROR NUEVO
			JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void actionCimaPila(){
		
		try{
			nombreArista=nomArs.getText();
			if(nombreArista.isEmpty()) throw new NullPointerException();
				aristaAP.setCimaPila(nombreArista.toUpperCase());
			dialog.setVisible(false);
			dialogTransicionPila();
		} catch(NullPointerException ex){ //METER ERROR NUEVO
			JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
		}
	}
	/****************************************/
	
	private JDialog createDialogDireccionTuring(){ //METER DIRECCION XML
		//Mensajero m=Mensajero.getInstancia();
		JOptionPane pane=new JOptionPane();
		//mensajero=Mensajero.getInstancia();
		JDialog d=pane.createDialog(null,mensajero.devuelveMensaje("vista.arist",2));
		JPanel panelD=new JPanel(new GridLayout(4,1));
		JPanel panelC=new JPanel(new GridLayout(1,4));
		JLabel etiqN=new JLabel(mensajero.devuelveMensaje("vista.sym5",2));
		nomArs=new JTextField();
		nomArs.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){ 		
					actionDireccionTuring();
				}
			}
		});
		JPanel panelB=  new JPanel();
		JButton aceptar=new JButton(mensajero.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionDireccionTuring();
			}
		});
		JButton cancelar=new JButton(mensajero.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
			}
		});
		panelB.add(aceptar);
		panelB.add(cancelar);
		panelD.add(etiqN);
		panelD.add(nomArs);
		panelD.add(panelC);
		panelD.add(panelB);
		d.setContentPane(panelD);
		d.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		return d;
		
	}
	
	private void dialogDireccionTuring(){
		
		dialog=createDialogDireccionTuring();
		dialog.setSize(new Dimension(400,150));
		dialog.setVisible(true);
		canvas.repaint();
	}
	
	
	private void actionCintaTuring(){
	
		try{
			nombreArista=nomArs.getText();
			if(nombreArista.isEmpty()) throw new NullPointerException();
 				aristaTuring.setSimboloCinta(nombreArista);
			dialog.setVisible(false);
			dialogDireccionTuring();
		} catch(NullPointerException ex){ //METER ERROR NUEVO
			JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
		}
		
	}
	/**
	 * Configura el dialogo para extraer la cima de la pila en una transicion automata de pila
	 * @author Roc�o Barrig�ete, Mario Huete, Luis San Juan
	 *
	 */
	
	private JDialog createDialogCimaPila(){
		//Mensajero m=Mensajero.getInstancia();
		JOptionPane pane=new JOptionPane();
		//mensajero=Mensajero.getInstancia();
		JDialog d=pane.createDialog(null,mensajero.devuelveMensaje("vista.arist",2));
		JPanel panelD=new JPanel(new GridLayout(4,1));
		JPanel panelC=new JPanel(new GridLayout(1,4));
		JLabel etiqN=new JLabel(mensajero.devuelveMensaje("vista.ap2",2));
		nomArs=new JTextField();
		nomArs.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){ 
					actionCimaPila();
				}
			}
		});
		JPanel panelB=  new JPanel();
		JButton aceptar=new JButton(mensajero.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionCimaPila();
			}
		});
		JButton cancelar=new JButton(mensajero.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
			}
		});
		panelB.add(aceptar);
		panelB.add(cancelar);
		panelD.add(etiqN);
		panelD.add(nomArs);
		panelD.add(panelC);
		panelD.add(panelB);
		d.setContentPane(panelD);
		d.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		return d;
		
	}
	
	/**
	 * Extraer la informacion de las transiciones de la pila en una transicion automata de pila
	 * @author Roc�o Barrig�ete, Mario Huete, Luis San Juan
	 *
	 */
	
	private void actionTransicionPila(){
		
		  
		try{
			nombreArista=nomArs.getText();
			if(nombreArista.isEmpty()) throw new NullPointerException();
			String s = nomArs.getText().trim();
			int i = 0;
			while(i < s.length()){
				String ss= "" + s.charAt(i);
				if(canvas.getAlfabetoPila()==null) canvas.setAlfabetoPila(new AlfabetoPila_imp());
				if(!canvas.getAlfabetoPila().estaLetra(ss.toUpperCase())&& !ss.equals(lambda)){
					canvas.getAlfabetoPila().aniadirLetra(ss.toUpperCase());
				}

				aristaAP.anadirPila(ss.toUpperCase());
				i++;
			}
			dialog.setVisible(false);
			
			
			if(canvas.getAlfabeto()==null) canvas.setAlfabeto(new Alfabeto_imp());
			Iterator<String> its = aristaAP.getEntradaSimbolos().iterator();
			while(its.hasNext()){
				String ss = its.next();
				if(!canvas.getAlfabeto().estaLetra(ss) && !ss.equals(lambda)){
					canvas.getAlfabeto().aniadirLetra(ss);
				} 
			}
			
			if(!canvas.getAlfabetoPila().estaLetra(aristaAP.getCimaPila().toUpperCase()) 
					&& !nombreArista.equals(lambda)){
				canvas.getAlfabetoPila().aniadirLetra(aristaAP.getCimaPila().toUpperCase());
			}
			

			canvas.anadeAristaAP(aristaAP);


		} catch(NullPointerException ex){
			JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Configura el dialogo para extraer los movimientos en la pila en una transicion automata de pila
	 * @author Roc�o Barrig�ete, Mario Huete, Luis San Juan
	 *
	 */
	
	private JDialog createDialogTransicionPila(){
		
		JOptionPane pane=new JOptionPane();
		JDialog d=pane.createDialog(null,mensajero.devuelveMensaje("vista.arist",2));
		JPanel panelD=new JPanel(new GridLayout(4,1));
		JPanel panelC=new JPanel(new GridLayout(1,4));
		JLabel etiqN=new JLabel(mensajero.devuelveMensaje("vista.ap3",2));
		nomArs=new JTextField();
		nomArs.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){ 		
					actionTransicionPila();
				}
			}
		});
		JPanel panelB=  new JPanel();
		JButton aceptar=new JButton(mensajero.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionTransicionPila();
			}
		});
		JButton cancelar=new JButton(mensajero.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
			}
		});
		panelB.add(aceptar);
		panelB.add(cancelar);
		panelD.add(etiqN);
		panelD.add(nomArs);
		panelD.add(panelC);
		panelD.add(panelB);
		d.setContentPane(panelD);
		d.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		return d;
		
	}
	
	/**
	 * Crea el dialogo para extraer los movimientos en la pila en una transicion automata de pila
	 * @author Roc�o Barrig�ete, Mario Huete, Luis San Juan
	 *
	 */
	private void dialogTransicionPila(){
		
		dialog=createDialogTransicionPila();
		dialog.setSize(new Dimension(400,150));
		dialog.setVisible(true);
		canvas.repaint();
	}
	
	/**
	 * Crea el dialogo para extraer la cima de la pila en una transicion automata de pila
	 * @author Roc�o Barrig�ete, Mario Huete, Luis San Juan
	 *
	 */
	
	private void dialogCimaPila(){
		
		dialog=createDialogCimaPila();
		dialog.setSize(new Dimension(400,150));
		dialog.setVisible(true);
		canvas.repaint();
	}
	
	/*************************************/

	private JDialog createDialogCintaTuring(){
		//Mensajero m=Mensajero.getInstancia();
		JOptionPane pane=new JOptionPane();
		//mensajero=Mensajero.getInstancia();
		JDialog d=pane.createDialog(null,mensajero.devuelveMensaje("vista.arist",2));
		JPanel panelD=new JPanel(new GridLayout(4,1));
		JPanel panelC=new JPanel(new GridLayout(1,4));
		JLabel etiqN=new JLabel(mensajero.devuelveMensaje("vista.sym4",2));
		nomArs=new JTextField();
		nomArs.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){ 
					actionCintaTuring();
				}
			}
		});
		JPanel panelB=  new JPanel();
		JButton aceptar=new JButton(mensajero.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionCintaTuring();
			}
		});
		JButton cancelar=new JButton(mensajero.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
			}
		});
		panelB.add(aceptar);
		panelB.add(cancelar);
		panelD.add(etiqN);
		panelD.add(nomArs);
		panelD.add(panelC);
		panelD.add(panelB);
		d.setContentPane(panelD);
		d.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		return d;
		
	}
	
	
	private void dialogCintaTuring(){
		
		dialog=createDialogCintaTuring();
		dialog.setSize(new Dimension(400,150));
		dialog.setVisible(true);
		canvas.repaint();
	}
	
	
	
	
	/**
	 * Configura el dialogo para extraer los simbolos consumidos en una transicion automata de pila
	 * @author Roc�o Barrig�ete, Mario Huete, Luis San Juan
	 *
	 */
	
	private JDialog createDialogAristaAP(){

		fondoPila = mensajero.devuelveMensaje("simbolos.cima",4);
		lambda = mensajero.devuelveMensaje("simbolos.lambda",4);
		canvas.creaAP(); 
		/************************************************/
		//NO SEGURO CREARLO AKI
		/************************************************/
		JOptionPane pane=new JOptionPane();
		JDialog d=pane.createDialog(null,mensajero.devuelveMensaje("vista.arist",2));
		JPanel panelD=new JPanel(new GridLayout(4,1));
		JPanel panelC=new JPanel(new GridLayout(1,4));
		JLabel etiqN=new JLabel(mensajero.devuelveMensaje("vista.ap1",2));
		nomArs=new JTextField();
		nomArs.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					
					actionAristaAP();
				}
			}
		});
		JPanel panelB=  new JPanel();
		JButton aceptar=new JButton(mensajero.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				actionAristaAP();
			}
		});
		JButton cancelar=new JButton(mensajero.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
			}
		});
		panelB.add(aceptar);
		panelB.add(cancelar);
		panelD.add(etiqN);
		panelD.add(nomArs);
		panelD.add(panelC);
		panelD.add(panelB);
		d.setContentPane(panelD);
		d.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		return d;
	}
	
	/**
	 * Crea el di�logo con la infromaci�n de la arista que recibe como par�metro
	 * @param a arista de la que se extrae la infromaci�n que se muestra
	 */
	protected void createDialogArista(Arista a){
		Mensajero m=Mensajero.getInstancia();
		JOptionPane pane=new JOptionPane();
		//mensajero=Mensajero.getInstancia();
		dialog=pane.createDialog(null,m.devuelveMensaje("vista.arist",2));
		JPanel panelD=new JPanel(new GridLayout(4,1));
		JLabel etiqN=new JLabel(m.devuelveMensaje("vista.sym",2));
		nomArs=new JTextField(a.getEtiqueta());
		nomArs.addKeyListener(new OyenteModificaAristaKeyAdapter(a,this));
		Vector<String> v=new Vector<String>();
		Iterator<Estado> iAr=canvas.getListaEstados().iterator();
		while(iAr.hasNext()){
			Estado es=iAr.next();
			v.add(es.getEtiqueta());
		}
		JPanel panelCombo=new JPanel();
		JLabel e1=new JLabel(m.devuelveMensaje("vista.desde",2));
		desde=new JComboBox(v);
		desde.setSelectedItem(a.getOrigen());
		JLabel e2=new JLabel(m.devuelveMensaje("vista.hasta",2));
		hasta=new JComboBox(v);
		hasta.setSelectedItem(a.getDestino());
		panelCombo.add(e1);
		panelCombo.add(desde);
		panelCombo.add(e2);
		panelCombo.add(hasta);
		JPanel panelB=  new JPanel();
		JButton aceptar=new JButton(m.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new OyenteModificaAristaActionListener(a,this));
		JButton cancelar=new JButton(m.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
			}
		});
		panelB.add(aceptar);
		panelB.add(cancelar);
		panelD.add(etiqN);
		panelD.add(nomArs);
		panelD.add(panelCombo);
		panelD.add(panelB);
		dialog.setContentPane(panelD);
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setSize(new Dimension(300,150));
		dialog.setVisible(true);
	}
	
	/**
	 * Crea el di�logo con la infromaci�n de la arista del automata de pila que recibe como par�metro
	 * @param a arista de la que se extrae la infromaci�n que se muestra
	 */
	protected void createDialogAristaAP(AristaAP a){
		Mensajero m=Mensajero.getInstancia();
		JOptionPane pane=new JOptionPane();
		//mensajero=Mensajero.getInstancia();
		dialog=pane.createDialog(null,m.devuelveMensaje("vista.arist",2));
		JPanel panelD=new JPanel(new GridLayout(4,1));
		//JPanel panelE=new JPanel(new GridLayout(4,1));
		//JPanel panelF=new JPanel(new GridLayout(4,1));
		JLabel etiqN=new JLabel(m.devuelveMensaje("vista.sym",2));
		nomArs=new JTextField(a.toStringSimbolos()); //REVISAR

		JLabel etiqC=new JLabel(m.devuelveMensaje("vista.ap2",2));
		nomCima=new JTextField(a.getCimaPila());
		JLabel etiqT=new JLabel(m.devuelveMensaje("vista.ap3",2));
		nomTrans=new JTextField(a.toStringTransicion());
		nomArs.addKeyListener(new OyenteModificaAristaAPKeyAdapter(a,this));
		nomCima.addKeyListener(new OyenteModificaAristaAPKeyAdapter(a,this));
		nomTrans.addKeyListener(new OyenteModificaAristaAPKeyAdapter(a,this));//REVISARRRRRRRR
		Vector<String> v=new Vector<String>();
		Iterator<Estado> iAr=canvas.getListaEstados().iterator();
		while(iAr.hasNext()){
			Estado es=iAr.next();
			v.add(es.getEtiqueta());
		}
		JPanel panelCombo=new JPanel();
		JLabel e1=new JLabel(m.devuelveMensaje("vista.desde",2));
		desde=new JComboBox(v);
		desde.setSelectedItem(a.getOrigen());
		JLabel e2=new JLabel(m.devuelveMensaje("vista.hasta",2));
		hasta=new JComboBox(v);
		hasta.setSelectedItem(a.getDestino());
		panelCombo.add(e1);
		panelCombo.add(desde); 
		panelCombo.add(e2);
		panelCombo.add(hasta);
		JPanel panelB=  new JPanel(); 
		JButton aceptar=new JButton(m.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new OyenteModificaAristaAPActionListener(a,this)); //REVISARRRRRRRR
		JButton cancelar=new JButton(m.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
			}
		});
		panelB.add(aceptar);
		panelB.add(cancelar);
		panelD.add(etiqN);
		panelD.add(nomArs);
		panelD.add(etiqC);
		panelD.add(nomCima);
		panelD.add(etiqT);
		panelD.add(nomTrans);
		panelD.add(panelCombo);
		panelD.add(panelB);
//		panelD.add(panelE);
//		panelD.add(panelF);
		dialog.setContentPane(panelD);
		
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setSize(new Dimension(/*300*/400,/*150*/350));
		dialog.setVisible(true);
	}
	
	
	/**
	 * Crea el di�logo con la infromaci�n de la arista del automata de pila que recibe como par�metro
	 * @param a arista de la que se extrae la infromaci�n que se muestra
	 */
	protected void createDialogAristaTuring(AristaTuring a){
		Mensajero m=Mensajero.getInstancia();
		JOptionPane pane=new JOptionPane();
		//mensajero=Mensajero.getInstancia();
		dialog=pane.createDialog(null,m.devuelveMensaje("vista.arist",2));
		JPanel panelD=new JPanel(new GridLayout(4,1));
		//JPanel panelE=new JPanel(new GridLayout(4,1));
		//JPanel panelF=new JPanel(new GridLayout(4,1));
		JLabel etiqN=new JLabel(m.devuelveMensaje("vista.sym",2));
		nomArs=new JTextField(a.toStringSimbolos()); //REVISAR

		JLabel etiqC=new JLabel(m.devuelveMensaje("vista.sym4",2));
		nomCima=new JTextField(a.getSimboloCinta());
		JLabel etiqT=new JLabel(m.devuelveMensaje("vista.sym5",2));
		nomTrans=new JTextField(a.getDireccion());
		nomArs.addKeyListener(new OyenteModificaAristaTuringKeyAdapter(a,this));
		nomCima.addKeyListener(new OyenteModificaAristaTuringKeyAdapter(a,this));
		nomTrans.addKeyListener(new OyenteModificaAristaTuringKeyAdapter(a,this));//REVISARRRRRRRR
		Vector<String> v=new Vector<String>();
		Iterator<Estado> iAr=canvas.getListaEstados().iterator();
		while(iAr.hasNext()){
			Estado es=iAr.next();
			v.add(es.getEtiqueta());
		}
		JPanel panelCombo=new JPanel();
		JLabel e1=new JLabel(m.devuelveMensaje("vista.desde",2));
		desde=new JComboBox(v);
		desde.setSelectedItem(a.getOrigen());
		JLabel e2=new JLabel(m.devuelveMensaje("vista.hasta",2));
		hasta=new JComboBox(v);
		hasta.setSelectedItem(a.getDestino());
		panelCombo.add(e1);
		panelCombo.add(desde); 
		panelCombo.add(e2);
		panelCombo.add(hasta);
		JPanel panelB=  new JPanel(); 
		JButton aceptar=new JButton(m.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new OyenteModificaAristaTuringActionListener(a,this)); //REVISARRRRRRRR
		JButton cancelar=new JButton(m.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
			}
		});
		panelB.add(aceptar);
		panelB.add(cancelar);
		panelD.add(etiqN);
		panelD.add(nomArs);
		panelD.add(etiqC);
		panelD.add(nomCima);
		panelD.add(etiqT);
		panelD.add(nomTrans);
		panelD.add(panelCombo);
		panelD.add(panelB);
//		panelD.add(panelE);
//		panelD.add(panelF);
		dialog.setContentPane(panelD);
		
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setSize(new Dimension(/*300*/400,/*150*/350));
		dialog.setVisible(true);
	}
	
	
	
	
	
	/**
	 * Crea el di�logo con la infromaci�n del estado que recibe como par�metro
	 * @param e estado del que se extrae la infromaci�n que se muestra
	 */
	protected void createDialogEstado(Estado e){
		Mensajero m=Mensajero.getInstancia();
		JOptionPane pane=new JOptionPane();
		//mensajero=Mensajero.getInstancia();
		dialog=pane.createDialog(null,m.devuelveMensaje("vista.est",2));
		JPanel panelD=new JPanel(new GridLayout(4,1));
		JPanel panelR=new JPanel();
		JRadioButton fin=new JRadioButton(m.devuelveMensaje("vista.final",2));
		if(canvas.getListaFinales().contains(e.getEtiqueta())) {
			fin.setSelected(true);
			mouse.setEsFinal(true);
		} else mouse.setEsFinal(false);
		fin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JRadioButton b=(JRadioButton)e.getSource();
				if(b.isSelected())mouse.setEsFinal(true);
				else mouse.setEsFinal(false);
			}
		});
		JRadioButton ini=new JRadioButton(m.devuelveMensaje("vista.inicial",2));
		if(canvas.getEstadoInicial().equals(e.getEtiqueta())){
			ini.setSelected(true);
			mouse.setEsInicial(true);
		} else mouse.setEsInicial(false);
		ini.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JRadioButton b=(JRadioButton)e.getSource();
				if(b.isSelected())mouse.setEsInicial(true);
				else mouse.setEsInicial(false);
			}
		});
		panelR.add(fin);
		panelR.add(ini);
		JLabel etiqN=new JLabel(m.devuelveMensaje("vista.nestado",2));
		nomEst=new JTextField(e.getEtiqueta());
		nomEst.addKeyListener(new OyenteModificaEstadoKeyAdapter(e,this));
		JPanel panelB=  new JPanel();
		JButton aceptar=new JButton(m.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new OyenteModificaEstadoActionListener(e,this));
		JButton cancelar=new JButton(m.devuelveMensaje("vista.cancelar",2));
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
			}
		});
		panelB.add(aceptar);
		panelB.add(cancelar);
		panelD.add(etiqN);
		panelD.add(nomEst);
		panelD.add(panelR);
		panelD.add(panelB);
		dialog.setContentPane(panelD);
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setSize(new Dimension(300,150));
		dialog.setVisible(true);
	}
	
	/**
	 * Devuelve el oyente padre padre
	 * @return oyente padre de la clase
	 */
	public CanvasMouseAdapter getMouse(){
		return mouse;
	}
	
	/**
	 * Dibuja la arista desde el estao de inicio al puntero del raton
	 * @param g clase para dibujar sobre el panel
	 */
	public void draw(Graphics g) {
		if (origen == null)
			return;
		java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
		Stroke s = g2.getStroke();
		g2.setStroke(STROKE);
		g2.setColor(COLOR);		
		/*if (origen.getX()>punto.x) {
			CurvedArrow a=new CurvedArrow(origen.getX(), origen.getY(), punto.x, punto.y,0);
			a.draw((Graphics2D) g2);
		}
		else {
			//g2.drawLine(origen.getX(), origen.getY(), punto.x, punto.y);
			CurvedArrow a=new CurvedArrow(origen.getX(), origen.getY(), punto.x, punto.y,0);
			a.draw((Graphics2D) g2);
			}*/
		if (origen.getX()>punto.x) {
			CurvedArrow curva=null;
			/*if(a.getOrigen().equals(a.getDestino()))curva=new CurvedArrow(a.getX()+20,a.getY()+8,a.getFx()-20,a.getFy()+8, 3);
			else {*/
				if (origen.getY()>punto.y)
					curva=new CurvedArrow(origen.getX()-15,origen.getY()-15,punto.x,punto.y, 0);
				else curva=new CurvedArrow(origen.getX()-15,origen.getY()+15,punto.x,punto.y, 0);
				curva.draw((Graphics2D) g2);
			}
		//}
		else {
			CurvedArrow curva=null;
			if(estanCerca(origen,punto))curva=new CurvedArrow(origen.getX()+20,origen.getY()+8,origen.getX()-20,origen.getY()+8, 3);
			else {
				if (origen.getY()>punto.y) 
					curva=new CurvedArrow(origen.getX()+15,origen.getY()-15,punto.x,punto.y, 0);
				else curva=new CurvedArrow(origen.getX()+15,origen.getY()+15,punto.x,punto.y,0);
			}
			curva.draw((Graphics2D) g2);
		}
		g2.setStroke(s);
	}

	private boolean estanCerca(Estado e, Point point) {
		int radio=20;
		if (point.distance(new Point(e.getX(),e.getY())) <= radio)
					return true;
		return false;
	}

	public void actionPerformed(ActionEvent ae) {
		String textButton = ae.getActionCommand();
		String dialogTitle = "Abrir un fichero";
		if (textButton.equals("Guardar"))
			dialogTitle = "Guardar un fichero";

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(dialogTitle);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		JFrame frame = new JFrame();
		int sel = chooser.showOpenDialog(frame);
		if (sel == JFileChooser.APPROVE_OPTION){
			File selectedFile = chooser.getSelectedFile();
			JOptionPane.showMessageDialog(frame,selectedFile.getAbsolutePath());
		}
		else
		return;
		
	}
		
}

