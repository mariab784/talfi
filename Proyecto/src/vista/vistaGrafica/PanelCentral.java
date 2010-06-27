package vista.vistaGrafica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import vista.Vista;
import controlador.Controlador;
import accesoBD.Mensajero;
import accesoBD.ParserEjercicio;
import accesoBD.ParserXML;
import accesoBD.TraductorHTML;
import accesoBD.usuariosBD.AccesBDUsuarios;
import accesoBD.usuariosBD.Usuario;
import modelo.AutomatasException;
import modelo.automatas.Alfabeto;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFD;
import modelo.automatas.AutomataFND;
import modelo.automatas.AutomataFNDLambda;
import modelo.automatas.AutomataPila;
import modelo.automatas.MaquinaTuring;
import modelo.ejercicios.Ejercicio;

/**
 * Panel central que almacena automatas y ejercicios
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class PanelCentral extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private AutomataCanvas panel;
	private VistaGrafica vista;
	private JTextArea enunciado;
	private JButton corregir;
	private JButton mandar;
	private Ejercicio ejercicio;
	private JPanel aux;
	private JPanel center;
	private JPanel panelB;
	private JDialog dialog;
	private String expresion;
	private JTextField tex;
	private JTextField alf;
	private JButton guardar;
	private JPanel p;
	private JButton boton;
	private JTextField cajitaPalabrasSi;
	private JTextField cajitaPalabrasNo;
	private JTextField cajitaPalabrasBucle;
	private JTextField cajitaCintaPalabrasSi;
	private JTextField cajitaCintaPalabrasNo;
	/**
	 * Constructor del panel que recibe la vista sobre la que se añade
	 * @param v vista grñfica que contiene el panel
	 */
	public PanelCentral(VistaGrafica v){
		super(new BorderLayout());
		vista=v;
		Mensajero m=Mensajero.getInstancia();
		aux=new JPanel(new BorderLayout());
		aux.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		center=new JPanel();
		panelB=new JPanel();
		enunciado=new JTextArea(5,90);
		enunciado.setText("");
		enunciado.setEditable(false);
		

		
		
		
		corregir=new JButton(m.devuelveMensaje("vista.solucion",2));
		corregir.setEnabled(false);
		corregir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane pane=new JOptionPane();
				Mensajero m=Mensajero.getInstancia();
				System.out.println("tipo para corregirte: " + ejercicio.getTipo());
				if(ejercicio.getTipo().equals("TURING")){
					panel.borrarPanel();
					VistaGrafica.setPila(false);
					VistaGrafica.setTuring(true);					
					
					boton=new JButton(m.devuelveMensaje("ejercicio.html",2));
					boton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							try{
								String ruta=TraductorHTML.getInstancia().traducirEjercicio(ejercicio.getRuta());
								vista.muestraHtml(true, ruta);
							} catch(AutomatasException ex)   {
								JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
							}
						}
					});
					p=new JPanel();
					p.add(boton);
					panelB.add(p, BorderLayout.SOUTH);
					panelB.setVisible(false);
					panelB.setVisible(true);
					mandar.setEnabled(true);
					corregir.setEnabled(false);
					vista.activaToogleButtons();
					
				}
				if(ejercicio.getTipo().equals("TRANSFORMACIONPILA")){

					panel.borrarPanel();
					VistaGrafica.setPila(true);
					VistaGrafica.setTuring(false);
					boton=new JButton(m.devuelveMensaje("ejercicio.html",2));
					boton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							try{
								String ruta=TraductorHTML.getInstancia().traducirEjercicio(ejercicio.getRuta());
								vista.muestraHtml(true, ruta);
							} catch(AutomatasException ex)   {
								JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
							}
						}
					});
					p=new JPanel();
					p.add(boton);
					panelB.add(p, BorderLayout.SOUTH);
					panelB.setVisible(false);
					panelB.setVisible(true);
					mandar.setEnabled(true);
					corregir.setEnabled(false);
					vista.activaToogleButtons();
					
					
				}
				
				if(ejercicio.getTipo().equals("Lenguaje")||ejercicio.getTipo().equals("EquivERs")||
						ejercicio.getTipo().equals("EquivAutoER")){
					dialog=pane.createDialog(m.devuelveMensaje("vista.expresion",2));
					PanelExpresionRegular p=new PanelExpresionRegular(dialog);
					dialog.setContentPane(p);
					dialog.setSize(new Dimension(300,150));
					dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
					dialog.setVisible(true);
					String query=null;
					String ruta=null;
					Alfabeto alfabeto=ejercicio.getAlfabeto();
					try {
						switch(p.getOpcion()){
							case 0:{
								//nada
								break;
							}
							case 1:{
								String exp=p.getExp();
								expresion=exp;
								ruta=vista.traducirXML(exp,alfabeto);
								if(p.isPasos()){
									query="TALF -r-p "+ruta;
									vista.getControlador().ejecutaQuery(query);
									TraductorHTML trhtml=TraductorHTML.getInstancia();
									String xmlSalida=vista.getControlador().salidaXML();
									String rutahtml=trhtml.traducirPasosER_AFNDL(xmlSalida);
									vista.muestraHtml(p.isPasos(),rutahtml);
								}
								else{
									query="TALF -r "+ruta;
									vista.getControlador().ejecutaQuery(query);
								}
								Automata a=(AutomataFNDLambda)vista.getControlador().getSalida();
								panel.cargarAutomataNuevo(a);
								panel.setTipoAutomata("AutomataFNDLambda");
								vista.setExpresion(exp);
								mandar.setEnabled(true);
								corregir.setEnabled(false);
								break;
							}
							case 2:{
								String exp="%";
								expresion=exp;
								ruta=vista.traducirXML(exp,alfabeto);
								if(p.isPasos()){
									query="TALF -r-p "+ruta;
									TraductorHTML trhtml=TraductorHTML.getInstancia();
									String xmlSalida=vista.getControlador().salidaXML();
									String rutahtml=trhtml.traducirPasosER_AFNDL(xmlSalida);
									vista.muestraHtml(p.isPasos(),rutahtml);
								}
								else {
									query="TALF -r "+ruta;
									vista.getControlador().ejecutaQuery(query);
								}
								Automata a=(AutomataFNDLambda)vista.getControlador().getSalida();
								panel.cargarAutomataNuevo(a);
								vista.setExpresion(exp);
								mandar.setEnabled(true);
								corregir.setEnabled(false);
								VistaGrafica.setPila(false);
								VistaGrafica.setTuring(false);
								break;
							}
						}
					} catch(AutomatasException ex){
						JOptionPane.showMessageDialog(null,ex.getMensaje(),m.devuelveMensaje("ejercicio.error",2),JOptionPane.ERROR_MESSAGE);
						panel.borrarPanel();
					}
				} 
				if(ejercicio.getTipo().equals("AFDTOER")){
					dialog=pane.createDialog(m.devuelveMensaje("ejercicio.er",2));
					PanelExpresionRegular p=new PanelExpresionRegular(dialog);
					dialog.setContentPane(p);
					dialog.setSize(new Dimension(300,150));
					dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
					dialog.setVisible(true);
					String query=null;
					String ruta=null;
					Alfabeto alfabeto=ejercicio.getAlfabeto();
					try {
						switch(p.getOpcion()){
							case 0:{
								//nada
								break;
							}
							case 1:{
								String exp=p.getExp();
								expresion=exp;
								ruta=vista.traducirXML(exp,alfabeto);
								if(p.isPasos()){
									query="TALF -r-p "+ruta;
									vista.getControlador().ejecutaQuery(query);
									TraductorHTML trhtml=TraductorHTML.getInstancia();
									String xmlSalida=vista.getControlador().salidaXML();
									String rutahtml=trhtml.traducirPasosER_AFNDL(xmlSalida);
									vista.muestraHtml(p.isPasos(),rutahtml);
								}
								else{
									query="TALF -r "+ruta;
									vista.getControlador().ejecutaQuery(query);
								}
								Automata a=(AutomataFNDLambda)vista.getControlador().getSalida();
								panel.cargarAutomataNuevo(a);
								panel.setTipoAutomata("AutomataFNDLambda");
								vista.setExpresion(exp);
								mandar.setEnabled(true);
								corregir.setEnabled(false);
								break;
							}
							case 2:{
								String exp="%";
								expresion=exp;
								ruta=vista.traducirXML(exp,alfabeto);
								if(p.isPasos()){
									query="TALF -r-p "+ruta;
									TraductorHTML trhtml=TraductorHTML.getInstancia();
									String xmlSalida=vista.getControlador().salidaXML();
									String rutahtml=trhtml.traducirPasosER_AFNDL(xmlSalida);
									vista.muestraHtml(p.isPasos(),rutahtml);
								}
								else {
									query="TALF -r "+ruta;
									vista.getControlador().ejecutaQuery(query);
								}
								Automata a=(AutomataFNDLambda)vista.getControlador().getSalida();
								panel.cargarAutomataNuevo(a);
								vista.setExpresion(exp);
								mandar.setEnabled(true);
								corregir.setEnabled(false);
								break;
							}
						}
					} catch(AutomatasException ex){
						JOptionPane.showMessageDialog(null,ex.getMensaje(),m.devuelveMensaje("ejercicio.error",2),JOptionPane.ERROR_MESSAGE);
						panel.borrarPanel();
					}
					VistaGrafica.setPila(false);
					VistaGrafica.setTuring(false);
				}
				if(ejercicio.getTipo().equals("AFNLTOAFN")||ejercicio.getTipo().equals("AFNTOAFD")||
						ejercicio.getTipo().equals("Minimizacion")||ejercicio.getTipo().equals("EquivAutos")){
					panel.borrarPanel();
					boton=new JButton(m.devuelveMensaje("ejercicio.html",2));
					boton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							try{
								String ruta=TraductorHTML.getInstancia().traducirEjercicio(ejercicio.getRuta());
								vista.muestraHtml(true, ruta);
							} catch(AutomatasException ex)   {
								JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
							}
						}
					});
					p=new JPanel();
					p.add(boton);
					panelB.add(p, BorderLayout.SOUTH);
					panelB.setVisible(false);
					panelB.setVisible(true);
					mandar.setEnabled(true);
					corregir.setEnabled(false);
					vista.activaToogleButtons();
					VistaGrafica.setPila(false);
					VistaGrafica.setTuring(false);
				}
				if(ejercicio.getTipo().equals("EquivERAuto")){
					panel.borrarPanel();
					mandar.setEnabled(true);
					corregir.setEnabled(false);
					vista.activaToogleButtons();
					VistaGrafica.setPila(false);
					VistaGrafica.setTuring(false);
				}
			}
		});
		mandar=new JButton(m.devuelveMensaje("ejercicio.mandar",2));
		mandar.setEnabled(false);
		mandar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					boolean res=false;
					if(ejercicio.getTipo().equals("Lenguaje")||ejercicio.getTipo().equals("AFDTOER")||
							ejercicio.getTipo().equals("EquivERs")||ejercicio.getTipo().equals("EquivAutoER"))
						res=ejercicio.corregir(expresion);
					else {
						String brr=new Character((char)92).toString();
						String rutaxml=System.getProperty("user.dir")+brr+"XML"+brr+"pruebas"+brr+"fichero.xml";
						File fichero = new File (rutaxml);
						BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
						bw.append(panel.traducirXML());
						bw.close();
						
						res=ejercicio.corregir(ParserXML.getInstancia().extraerAutomata(rutaxml));
					}
					if (res) {
						Mensajero m=Mensajero.getInstancia();
						JOptionPane.showMessageDialog(null,m.devuelveMensaje("ejercicio.bien",2),m.devuelveMensaje("ejercicio.resultado",2),JOptionPane.INFORMATION_MESSAGE);
						Usuario usr=Usuario.getInstancia();
						ArrayList<String> ejr=usr.getEjerciciosBien();
						if(!ejr.contains(ejercicio.getRuta())){
							ejr.add(ejercicio.getRuta());
							Usuario u=new Usuario(usr.getNombre(),usr.getDni(),usr.getPassword(),usr.getEjerciciosConsultados(),ejr,usr.getEjerciciosMal());
							AccesBDUsuarios.getInstancia().actualizarUsuario(usr,u);
							vista.reconstruirPanelUsuario();
						}
					}
					else {
						Mensajero m=Mensajero.getInstancia();
						JOptionPane.showMessageDialog(null,m.devuelveMensaje("ejercicio.mal",2),m.devuelveMensaje("ejercicio.resultado",2),JOptionPane.INFORMATION_MESSAGE);
						Usuario usr=Usuario.getInstancia();
						ArrayList<String> ejr=usr.getEjerciciosMal();
						if(!ejr.contains(ejercicio.getRuta())){
							ejr.add(ejercicio.getRuta());
							Usuario u=new Usuario(usr.getNombre(),usr.getDni(),usr.getPassword(),usr.getEjerciciosConsultados(),usr.getEjerciciosBien(),ejr);
							AccesBDUsuarios.getInstancia().actualizarUsuario(usr,u);
							vista.reconstruirPanelUsuario();
						}
					}
					
				} catch(IOException ex){
					Mensajero m=Mensajero.getInstancia();
					JOptionPane.showMessageDialog(null,m.devuelveMensaje("ejercicio.error",2),"Error",JOptionPane.ERROR_MESSAGE);
				} catch(AutomatasException exc){
					JOptionPane.showMessageDialog(null,exc.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panelB.add(corregir);
		panelB.add(mandar);
//		panelB.add(expr);//XXX
		
		center.add(new JScrollPane(enunciado));
		aux.add(center,BorderLayout./*CENTER*/NORTH);
		System.out.println("buscame por si lo revientas");
		aux.add(panelB,BorderLayout.SOUTH);
		add(aux,BorderLayout.NORTH);
		panel=new AutomataCanvas(vista);
		add(panel,BorderLayout.CENTER);
		enunciado.setVisible(false);
		corregir.setVisible(false);
		mandar.setVisible(false);
	}
	
	/**
	 * Carga un ejercicio en el panel
	 * @param rutaXml del archivo que contiene el ejercicio
	 */
	@SuppressWarnings("static-access")
	public void cargarEjercicio(String rutaXml) {
		this.remove(panel);
		this.removeAll();
		panelB.removeAll();
		if(p!=null) {
			p.removeAll();
			panelB.remove(p);
		}
		panelB.add(corregir);
		panelB.add(mandar);
		aux.removeAll(); //XXX
		aux.add(center,BorderLayout.CENTER);
		aux.add(panelB,BorderLayout.SOUTH);
		add(aux,BorderLayout.NORTH);
		add(panel,BorderLayout.CENTER);
		enunciado.setVisible(true);
		enunciado.setEditable(false);
		corregir.setVisible(true);
		mandar.setVisible(true);
		panel.borrarPanel();
		ParserEjercicio parser=new ParserEjercicio();
		boolean expresionR=rutaXml.contains("Lenguaje")||rutaXml.contains("Equivalencia_Expresiones");
	    try{
	    	Mensajero m=Mensajero.getInstancia();
	    	if (expresionR) {
    			this.vista.getPanelCentral().getCanvas().setPila(false);
    			this.vista.getPanelCentral().getCanvas().setTuring(false);
	    		Ejercicio ej=null;
	    		if(rutaXml.contains("Lenguaje"))
	    			ej=parser.extraerEjercicioLenguaje(rutaXml);
	    		if(rutaXml.contains("EquivERs"))
	    			ej=parser.extraerEjercicioEquivERs(rutaXml);
	    		if(ej!=null) {
	    			setEjercicio(ej);
	    			corregir.setText(m.devuelveMensaje("ejercicio.escribir",2));
	    			vista.desactivaToogleButtons();
	    			vista.requestFocus();
	    		} else 
	    			JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.noejer",2),"Error",JOptionPane.ERROR_MESSAGE);
	    	}
	    	else {
	    		if (rutaXml.contains("Minimizacion")) {
	    			setEjercicio(parser.extraerEjercicioAutomatas(rutaXml));
	    			panel.cargarAutomataNuevo((Automata)ejercicio.getEntrada());
	    			corregir.setText(m.devuelveMensaje("ejercicio.dibujar",2));
	    			this.vista.getPanelCentral().getCanvas().setPila(false);
	    			this.vista.getPanelCentral().getCanvas().setTuring(false);
	    			vista.activaToogleButtons();
	    			vista.requestFocus();
	    		}
	    		if (rutaXml.contains("TransformacionAFD_to_ER")) {
	    			setEjercicio(parser.extraerEjercicioAFDTOER(rutaXml));
	    			panel.cargarAutomataNuevo((Automata)ejercicio.getEntrada());
	    			corregir.setText(m.devuelveMensaje("ejercicio.escribir",2));
	    			vista.desactivaToogleButtons();
	    			vista.requestFocus();
	    			this.vista.getPanelCentral().getCanvas().setPila(false);
	    			this.vista.getPanelCentral().getCanvas().setTuring(false);
	    		}
	    		if (rutaXml.contains("TransformacionAFNAFD")) {
	    			setEjercicio(parser.extraerEjercicioAFNAFD(rutaXml));
	    			panel.cargarAutomataNuevo((Automata)ejercicio.getEntrada());
	    			corregir.setText(m.devuelveMensaje("ejercicio.dibujar",2));
	    			vista.activaToogleButtons();
	    			vista.requestFocus();
	    			this.vista.getPanelCentral().getCanvas().setPila(false);
	    			this.vista.getPanelCentral().getCanvas().setTuring(false);
	    		}
	    		if (rutaXml.contains("TransformacionAFNLambda_AFN")) {
	    			setEjercicio(parser.extraerEjercicioAFNLAFN(rutaXml));
	    			panel.cargarAutomataNuevo((Automata)ejercicio.getEntrada());
	    			corregir.setText(m.devuelveMensaje("ejercicio.dibujar",2));
	    			vista.activaToogleButtons();
	    			vista.requestFocus();
	    			this.vista.getPanelCentral().getCanvas().setPila(false);
	    			this.vista.getPanelCentral().getCanvas().setTuring(false);
	    		}
	    		if (rutaXml.contains("Equivalencia_Automatas")) {
	    			setEjercicio(parser.extraerEjercicioEquivAutos(rutaXml));
	    			panel.cargarAutomataNuevo((Automata)ejercicio.getEntrada());
	    			corregir.setText(m.devuelveMensaje("ejercicio.dibujar",2));
	    			vista.desactivaToogleButtons();
	    			vista.requestFocus();
	    			this.vista.getPanelCentral().getCanvas().setPila(false);
	    			this.vista.getPanelCentral().getCanvas().setTuring(false);
	    			System.out.println("EKIVALENCIA");
	    		}
	    		if (rutaXml.contains("Equivalencia_Automata_Expresion")) {
	    			setEjercicio(parser.extraerEjercicioEquivAutoER(rutaXml));
	    			panel.cargarAutomataNuevo((Automata)ejercicio.getEntrada());
	    			corregir.setText(m.devuelveMensaje("ejercicio.escribir",2));
	    			vista.desactivaToogleButtons();
	    			vista.requestFocus();
	    			this.vista.getPanelCentral().getCanvas().setPila(false);
	    			this.vista.getPanelCentral().getCanvas().setTuring(false);
	    		}
	    		if (rutaXml.contains("Equivalencia_Expresion_Automata")) {
	    			setEjercicio(parser.extraerEjercicioEquivERAuto(rutaXml));
	    			corregir.setText(m.devuelveMensaje("ejercicio.dibujar",2));
	    			vista.desactivaToogleButtons();
	    			vista.requestFocus();
	    			this.vista.getPanelCentral().getCanvas().setPila(false);
	    			this.vista.getPanelCentral().getCanvas().setTuring(false);
	    		}
	    		if (rutaXml.contains("Transformacion_Pila")) {
	    			setEjercicio(parser.extraerEjercicioPila(rutaXml));
	 //   			System.out.println("getentradaess: " + ejercicio.getEntrada());
	    			if (ejercicio.getEntrada() != null)
	    				panel.cargarAutomataNuevo((AutomataPila)ejercicio.getEntrada());
	    			corregir.setText(m.devuelveMensaje("ejercicio.dibujar",2));
	    			vista.desactivaToogleButtons();
	    			vista.requestFocus();
	    			System.out.println("cambiate!!");
	    			this.vista.setPila(true);
	    			this.vista.setTuring(false);
	    		}
	    		if (rutaXml.contains("Maquina_Turing")) {

	    			setEjercicio(parser.extraerEjercicioTuring(rutaXml));
	    			System.out.println("getentradaess: " + ejercicio.getEntrada());
	    			if (ejercicio.getEntrada() != null)
	    				panel.cargarAutomataNuevo((MaquinaTuring)ejercicio.getEntrada());
	    			corregir.setText(m.devuelveMensaje("ejercicio.dibujar",2));
	    			vista.desactivaToogleButtons();
	    			vista.requestFocus();
	    			this.vista.getPanelCentral().getCanvas().setPila(false);
	    			this.vista.getPanelCentral().getCanvas().setTuring(true);
	    		}
	    	} 

	    	enunciado.setText(ejercicio.getEnunciado());
	    	corregir.setEnabled(true);
	    	Usuario usr=Usuario.getInstancia();
			ArrayList<String> ejr=usr.getEjerciciosConsultados();
			if(!ejr.contains(ejercicio.getRuta())){
				ejr.add(ejercicio.getRuta());
				Usuario u=new Usuario(usr.getNombre(),usr.getDni(),usr.getPassword(),ejr,usr.getEjerciciosBien(),usr.getEjerciciosMal());
				AccesBDUsuarios.getInstancia().actualizarUsuario(usr,u);
				vista.reconstruirPanelUsuario();
				vista.requestFocus();
			}
		} catch(AutomatasException exc){
			JOptionPane.showMessageDialog(null,exc.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
			vista.requestFocus();
		}
	}
	
	/**
	 * Devuelve el ejercicio que tiene almacenado el panel
	 * @return ejercicio almacenado
	 */
	public Ejercicio getEjercicio() {
		return ejercicio;
	}

	/**
	 * Establece el ejercicio del panel
	 * @param ejercicio nuevo ejercicio del panel
	 */
	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}

	/**
	 * Devuelve el panel de automata contenido
	 * @return panel de dibujos de automatas
	 */
	public AutomataCanvas getPanel() {
		return panel;
	}

	/**
	 * Establece el panel de automata
	 * @param panel nuevo panel de dibujos
	 */
	public void setPanel(AutomataCanvas panel) {
		this.panel = panel;
	}
	
	/**
	 * Devuelve la expresion del ejercicio contenido
	 * @return expresion del ejercicio
	 */
	public String getExpresionEjercicio(){
		return tex.getText();
	}

	/**
	 * Desactiva el modo de ejercicios para cargar un automata
	 */
	public void desactivarEjercicio() {
		enunciado.setVisible(false);
		corregir.setVisible(false);
		mandar.setVisible(false);
		this.remove(panel);
		this.removeAll();
		this.add(panel,BorderLayout.CENTER);
		panel.repaint();	
		setEjercicio(null);
	}
	
	/**
	 * Carga el ejercicio almacenado en el panel 
	 */
	public void cargarEjercicio() {
		this.remove(panel);
		this.removeAll();
		panelB.removeAll();
		panelB.add(corregir);
		panelB.add(mandar);
		aux.add(center,BorderLayout.CENTER);
		aux.add(panelB,BorderLayout.SOUTH);
		add(aux,BorderLayout.NORTH);
		add(panel,BorderLayout.CENTER);
		enunciado.setVisible(true);
		enunciado.setEditable(true);
		corregir.setVisible(true);
		corregir.setEnabled(false);
		mandar.setVisible(true);
		mandar.setEnabled(false);
		panel.borrarPanel();
		Mensajero m=Mensajero.getInstancia();
	   	if (ejercicio.getTipo().equals("Lenguaje")||ejercicio.getTipo().equals("EquivERs")) {
	   		corregir.setText(m.devuelveMensaje("ejercicio.escribir",2));
	   		vista.desactivaToogleButtons();
	   	}
	   	if(ejercicio.getTipo().equals("AFNLTOAFN")||ejercicio.getTipo().equals("AFNTOAFD")||ejercicio.getTipo().equals("Minimizacion")
	   			||ejercicio.getTipo().equals("EquivAutos")){
	   		panel.cargarAutomataNuevo((Automata)ejercicio.getEntrada());
	   		corregir.setText(m.devuelveMensaje("ejercicio.dibujar",2));
	   		vista.activaToogleButtons();
	    }
	    if(ejercicio.getTipo().equals("AFDTOER")){
	   		panel.cargarAutomataNuevo((Automata)ejercicio.getEntrada());
	   		corregir.setText(m.devuelveMensaje("ejercicio.escribir",2));
	   		vista.desactivaToogleButtons();
	   	}
	    if(ejercicio.getTipo().equals("EquivAutoER")){
	   		panel.cargarAutomataNuevo((Automata)ejercicio.getEntrada());
	   		corregir.setText(m.devuelveMensaje("ejercicio.escribir",2));
	   		vista.activaToogleButtons();
	   	}
	    if(ejercicio.getTipo().equals("EquivERAuto")){
	   		corregir.setText(m.devuelveMensaje("ejercicio.dibujar",2));
	   		vista.activaToogleButtons();
	   	}
	   	enunciado.setText(ejercicio.getEnunciado());
	   	corregir.setEnabled(true);
	}

	/**
	 * Permite crear un ejercicio y almacenarlo en la bd de ejercicios
	 * @param tipo tipo de ejercicio que se quiere crear
	 */
	public void crearEjercicio(String tipo) {
		// TODO Auto-generated method stub
		this.remove(panel);
		this.removeAll();
		aux.removeAll();
		if(p!=null){
			setVisible(false);
			p.removeAll();
			setVisible(true);
		}
		panelB.removeAll();
		JPanel pbotones = new JPanel();
		pbotones.add(corregir);
		pbotones.add(mandar);
		//panelB.add(corregir);
		//panelB.add(mandar);
		panelB.add(pbotones,BorderLayout.SOUTH);
		aux.add(center,BorderLayout./*CENTER*/NORTH);
		aux.add(panelB,BorderLayout.SOUTH);
		add(aux,BorderLayout.NORTH);
		add(panel,BorderLayout.CENTER);
		enunciado.setVisible(true);
		enunciado.setEditable(true);
		enunciado.setText("");
		Mensajero m=Mensajero.getInstancia();
		corregir.setText(m.devuelveMensaje("ejercicio.escribirc",2));
		corregir.setVisible(true);
		corregir.setEnabled(false);
		panel.borrarPanel();
		mandar.setVisible(false);
		guardar=new JButton(m.devuelveMensaje("ejercicio.guardar",2));
		
		if(tipo.equals("AutomatasDePila") || tipo.equals("MaquinaTuring")){
			
			if(tipo.equals("MaquinaTuring")){
				corregir.setText(m.devuelveMensaje("ejercicio.escribirAut",2));
				guardar.addActionListener(new OyenteGuardarEjercicio("MaquinaTuring",vista));
			}
			if(tipo.equals("AutomatasDePila")){
				corregir.setText(m.devuelveMensaje("ejercicio.escribirAut",2));
				guardar.addActionListener(new OyenteGuardarEjercicio("AutomatasDePila",vista));
			}
			//p=new JPanel();			
			/*p*//*panelB*/pbotones.add(guardar);
						
			/*JToolBar*/JPanel expr=new JPanel();//JToolBar();
			JPanel panelPalabras=new JPanel();JLabel e;JLabel e1;
			if (tipo.equals("AutomatasDePila")){
			e =new JLabel("<"+"html"+">"+m.devuelveMensaje("ejercicio.palabra",2)+
					"<"+"br"+">"+m.devuelveMensaje("ejercicio.rec",2)+"<!--"+"html"+"-->");
			e1 =new JLabel("<"+"html"+">"+m.devuelveMensaje("ejercicio.palabra",2)+
					"<"+"br"+">"+m.devuelveMensaje("ejercicio.nrec",2)+"<!--"+"html"+"-->");
			cajitaPalabrasSi= new JTextField(20);
//			cajitaPalabrasSi.setEditable(true);
//			cajitaPalabrasSi.setEnabled(true);
//			cajitaPalabrasSi.setVisible(true);
			cajitaPalabrasNo= new JTextField(20);
//			cajitaPalabrasNo.setEditable(true);
//			cajitaPalabrasNo.setEnabled(true);
//			cajitaPalabrasNo.setVisible(true);
			panelPalabras.add(e);
			panelPalabras.add(cajitaPalabrasSi);
			
			panelPalabras.add(e1);
			panelPalabras.add(cajitaPalabrasNo);
			}
			if(tipo.equals("MaquinaTuring")){
				e =new JLabel("<"+"html"+">"+m.devuelveMensaje("ejercicio.palabra",2)+
						"<"+"br"+">"+m.devuelveMensaje("ejercicio.rec",2)+"<!--"+"html"+"-->");
				
				e1 = new JLabel("<"+"html"+">"+m.devuelveMensaje("ejercicio.cinta1",2)+
						"<"+"br"+">"+m.devuelveMensaje("ejercicio.cinta2",2)+"<!--"+"html"+"-->");
				
				JLabel e2 = new JLabel("<"+"html"+">"+m.devuelveMensaje("ejercicio.palabra",2)+
						"<"+"br"+">"+m.devuelveMensaje("ejercicio.nrec",2)+"<!--"+"html"+"-->");
				
				JLabel e3 =new JLabel("<"+"html"+">"+m.devuelveMensaje("ejercicio.cinta1",2)+
						"<"+"br"+">"+m.devuelveMensaje("ejercicio.cinta2",2)+"<!--"+"html"+"-->");

				JLabel e4 =new JLabel("<"+"html"+">"+m.devuelveMensaje("ejercicio.palabra",2)+
						"<"+"br"+">"+m.devuelveMensaje("ejercicio.ciclo1",2)+"<"+"br"+">"+
						m.devuelveMensaje("ejercicio.ciclo2",2)+"<!--"+"html"+"-->");
				
				cajitaPalabrasSi= new JTextField(12);
				cajitaCintaPalabrasSi= new JTextField(12);
				cajitaPalabrasNo= new JTextField(12);
				cajitaCintaPalabrasNo= new JTextField(12);
				cajitaPalabrasBucle= new JTextField(12);

				panelPalabras.add(e);
				panelPalabras.add(cajitaPalabrasSi);
				
				panelPalabras.add(e1);
				panelPalabras.add(cajitaCintaPalabrasSi);
				
				panelPalabras.add(e2);
				panelPalabras.add(cajitaPalabrasNo);

				panelPalabras.add(e3);
				panelPalabras.add(cajitaCintaPalabrasNo);
				
				panelPalabras.add(e4);
				panelPalabras.add(cajitaPalabrasBucle);
				
			}
			JPanel panelPalabrasTotal=new JPanel();
			panelPalabrasTotal.add(panelPalabras);

			//expr.add(panelPalabrasTotal,BorderLayout/*.SOUTH*/.WEST);
		//	panelB.add(p,BorderLayout.SOUTH); //p es var de clase
			aux.add(panelPalabrasTotal,BorderLayout.CENTER);
			panelB/*aux*/.add(expr,BorderLayout.CENTER);
			
			
			vista.activaToogleButtons();
			
			if(tipo.equals("AutomatasDePila")){
				VistaGrafica.setPila(true); 
				VistaGrafica.setTuring(false);
				this.getCanvas().setTipoAutomata("AutomataPila");
			}
			
			if(tipo.equals("MaquinaTuring")){
				VistaGrafica.setPila(false);
				VistaGrafica.setTuring(true);
				this.getCanvas().setTipoAutomata("MaquinaTuring");
			}
		}
		if(tipo.equals("Lenguaje")){
			corregir.setText(m.devuelveMensaje("ejercicio.escribirER",2));
			tex= new JTextField(25);
			p=new JPanel();
			tex.setText(m.devuelveMensaje("ejercicio.er",2));
			p.add(tex);
			alf=new JTextField(10);
			alf.setText(m.devuelveMensaje("ejercicio.alf",2));
			p.add(alf);
			p.add(guardar);
			guardar.addActionListener(new OyenteGuardarEjercicio("Lenguaje",vista));
			panelB.add(p,BorderLayout.SOUTH);
			vista.desactivaToogleButtons();
			VistaGrafica.setPila(false); VistaGrafica.setTuring(false);
			this.getCanvas().setTipoAutomata("Lenguaje");
		}
		if (tipo.equals("Minimizacion")){
			corregir.setText(m.devuelveMensaje("ejercicio.escribirAut",2));
			p=new JPanel();
			guardar.addActionListener(new OyenteGuardarEjercicio("Minimizacion",vista));
			p.add(guardar);
			panelB.add(p,BorderLayout.SOUTH);
			vista.activaToogleButtons();
			VistaGrafica.setPila(false); VistaGrafica.setTuring(false);
			this.getCanvas().setTipoAutomata("Minimizacion");
		}
		if (tipo.equals("AFNTOAFD")){
			corregir.setText(m.devuelveMensaje("ejercicio.escribirAut",2));
			p=new JPanel();
			guardar.addActionListener(new OyenteGuardarEjercicio(tipo,vista));
			p.add(guardar);
			panelB.add(p,BorderLayout.SOUTH);
			vista.activaToogleButtons();
			VistaGrafica.setPila(false); VistaGrafica.setTuring(false);
			this.getCanvas().setTipoAutomata("AFNTOAFD");
		}
		if (tipo.equals("AFNLTOAFN")){
			corregir.setText(m.devuelveMensaje("ejercicio.escribirAut",2));
			p=new JPanel();
			guardar.addActionListener(new OyenteGuardarEjercicio(tipo,vista));
			p.add(guardar);
			panelB.add(p,BorderLayout.SOUTH);
			vista.activaToogleButtons();
			VistaGrafica.setPila(false); VistaGrafica.setTuring(false);
			this.getCanvas().setTipoAutomata("AFNLTOAFN");
		}
		if(tipo.equals("RE")){
			corregir.setText(m.devuelveMensaje("ejercicio.escribirER",2));
			tex= new JTextField(25);
			p=new JPanel();
			tex.setText(m.devuelveMensaje("ejercicio.er",2));
			p.add(tex);
			p.add(guardar);
			guardar.addActionListener(new OyenteGuardarEjercicio("RE",vista));
			panelB.add(p,BorderLayout.SOUTH);
			vista.activaToogleButtons();
			VistaGrafica.setPila(false); VistaGrafica.setTuring(false);
			this.getCanvas().setTipoAutomata("RE");
		}
		if(tipo.equals("EquivAutos")){
			corregir.setText(m.devuelveMensaje("ejercicio.escribirAut",2));
			p=new JPanel();
			guardar.addActionListener(new OyenteGuardarEjercicio("EquivAutos",vista));
			p.add(guardar);
			panelB.add(p,BorderLayout.SOUTH);
			vista.activaToogleButtons();
			VistaGrafica.setPila(false); VistaGrafica.setTuring(false);
			this.getCanvas().setTipoAutomata("EquivAutos");
		}
		if(tipo.equals("EquivAutoER")){
			corregir.setText(m.devuelveMensaje("ejercicio.escribirAut",2));
			p=new JPanel();
			p.add(guardar);
			guardar.addActionListener(new OyenteGuardarEjercicio("EquivAutoER",vista));
			panelB.add(p,BorderLayout.SOUTH);
			vista.activaToogleButtons();
			VistaGrafica.setPila(false); VistaGrafica.setTuring(false);
			this.getCanvas().setTipoAutomata("EquivAutoER");
		}
		if(tipo.equals("EquivERAuto")){
			corregir.setText(m.devuelveMensaje("ejercicio.escribirAut",2));
			tex= new JTextField(25);
			p=new JPanel();
			tex.setText(m.devuelveMensaje("ejercicio.er",2));
			p.add(tex);
			alf=new JTextField(10);
			alf.setText(m.devuelveMensaje("ejercicio.alf",2));
			p.add(alf);
			p.add(guardar);
			guardar.addActionListener(new OyenteGuardarEjercicio("EquivERAuto",vista));
			panelB.add(p,BorderLayout.SOUTH);
			vista.desactivaToogleButtons();
			VistaGrafica.setPila(false); VistaGrafica.setTuring(false);
			this.getCanvas().setTipoAutomata("EquivERAuto");
		}
		if(tipo.equals("EquivERs")){
			corregir.setText(m.devuelveMensaje("ejercicio.escribirAut",2));
			tex= new JTextField(25);
			p=new JPanel();
			tex.setText(m.devuelveMensaje("ejercicio.er",2));
			p.add(tex);
			alf=new JTextField(10);
			alf.setText(m.devuelveMensaje("ejercicio.alf",2));
			p.add(alf);
			p.add(guardar);
			guardar.addActionListener(new OyenteGuardarEjercicio("EquivERs",vista));
			panelB.add(p,BorderLayout.SOUTH);
			vista.desactivaToogleButtons();
			VistaGrafica.setPila(false); VistaGrafica.setTuring(false);
			this.getCanvas().setTipoAutomata("EquivERs");
		}
		vista.requestFocus();
	}
	
	/**
	 * Traduce el ejercicio a un fichero xml
	 * @param tipo de ejercicio que se va a traducir a cadena
	 * @return cadena de caracteres enformato xml que contendrñ al ejercicio
	 * @throws AutomatasException se lanza si hay algñn problema al parsear el ejercicio
	 * @throws IOException se lanza si no se puede abrir un fichero de pruebas
	 */
	private String traducirXMLEjercicio(String tipo)throws AutomatasException, IOException {
		// TODO Auto-generated method stub
		Mensajero m=Mensajero.getInstancia();
		String fich="<ejercicio><tipo>";
		if(tipo.equals("Lenguaje")){
			fich+="Lenguaje</tipo><enunciado>"+enunciado.getText()+"</enunciado>\n\t<input>";
			fich+="\n\t\t<alphabet>";
			String s=alf.getText();
			StringTokenizer st=new StringTokenizer(s,", ");
			while(st.hasMoreTokens()){
				String ss=st.nextToken();
				if(ss.equals("/")){
					throw new AutomatasException(m.devuelveMensaje("vista.lambda",2));
				}
				else{
					if(ss.contains("/")){
						throw new AutomatasException(m.devuelveMensaje("vista.lambda",2));
					} else
						fich+="\n\t\t\t<item>"+ss+"</item>";
				}
			}
			fich+="\n\t\t</alphabet>\n\t</input>\n\t\t<output>\n\t\t\t<RExpr>\n\t\t\t\t<item>"+tex.getText()+"</item>\n\t\t\t</RExpr>\n\t\t";
		}
		if(tipo.equals("MaquinaTuring")){
			fich+="EjMT</tipo><enunciado>"+enunciado.getText()+"</enunciado><output>";
			String input=panel.traducirXML();
			
			String lp = "<listaPalabras>";
			panel.creaListaPalabras(cajitaPalabrasSi.getText(),0);
			if(!panel.getListaPalabras().isEmpty()){
				Iterator<String> itLp = panel.getListaPalabras().iterator();
				while(itLp.hasNext()){
					lp+="\n\t\t\t<item>"+ itLp.next()+"</item>";
				}
			}
			lp += "</listaPalabras>\n";
			
			lp += "<listaCintaPalabras>";
			if(panel.getListaFinales().isEmpty()){
				panel.creaListaPalabras(cajitaCintaPalabrasSi.getText(),2);
				
				if(!panel.getCintaListaPalabras().isEmpty()){
					Iterator<String> itLp = panel.getCintaListaPalabras().iterator();
					while(itLp.hasNext()){
						lp+="\n\t\t\t<item>"+ itLp.next()+"</item>";
					}
				}
			}
			lp += "</listaCintaPalabras>\n";
			
			panel.creaListaPalabras(cajitaPalabrasNo.getText(),1);
			lp += "<listaPalabrasNo>";
			if(!panel.getListaPalabrasNo().isEmpty()){
				Iterator<String> itLp = panel.getListaPalabrasNo().iterator();
				while(itLp.hasNext()){
					lp+="\n\t\t\t<item>"+ itLp.next()+"</item>";
				}
			}
			lp += "</listaPalabrasNo>\n";
			
			lp += "<listaCintaPalabrasNo>\n";
			if(panel.getListaFinales().isEmpty()){
				panel.creaListaPalabras(cajitaCintaPalabrasNo.getText(),3);
				
				if(!panel.getCintaListaPalabrasNo().isEmpty()){
					Iterator<String> itLp = panel.getCintaListaPalabrasNo().iterator();
					while(itLp.hasNext()){
						lp+="\n\t\t\t<item>"+ itLp.next()+"</item>";
					}
				}
			}
			lp += "</listaCintaPalabrasNo>\n";
			
	
			lp += "<listaPalabrasBucle>\n";

				panel.creaListaPalabras(cajitaPalabrasBucle.getText(),4);
				
				if(!panel.getListaPalabrasBucle().isEmpty()){
					Iterator<String> itLp = panel.getListaPalabrasBucle().iterator();
					while(itLp.hasNext()){
						lp+="\n\t\t\t<item>"+ itLp.next()+"</item>";
					}
				}

			lp += "</listaPalabrasBucle>\n";
			
			

			fich+=input+"\n"+lp+"</output>";
			fich+="\n</ejercicio>";
			String rutaxml="XML"+"/pruebas/ejercicios"+"/ficheroEj.xml";
			File fichero = new File (rutaxml);
			BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
			bw.append(fich);
			bw.close();
			Controlador controlador=vista.getControlador();
			controlador.ejecutaQuery("TALF -ejmt "+rutaxml);	
			Automata a=(MaquinaTuring)controlador.getSalida();
			System.out.println("AUTOMATA GUARDA EJ:" + (MaquinaTuring)a);
			panel.cargarAutomataNuevo(a);
			panel.setTipoAutomata("MaquinaTuring");
			return fich;
		}
		
		if(tipo.equals("AutomatasDePila")){
			
			fich+="TransformacionAPs</tipo><enunciado>"+enunciado.getText()+"</enunciado><output>";
			panel.creaListaPalabras(cajitaPalabrasSi.getText(),0);
			String input=panel.traducirXML();
			String lp = "<listaPalabras>";
			if(!panel.getListaPalabras().isEmpty()){
				Iterator<String> itLp = panel.getListaPalabras().iterator();
				while(itLp.hasNext()){
					lp+="\n\t\t\t<item>"+ itLp.next()+"</item>";
				}
			}
			lp += "</listaPalabras>\n";
			
			panel.creaListaPalabras(cajitaPalabrasNo.getText(),1);
			lp += "<listaPalabrasNo>";
			System.out.println();
			if(!panel.getListaPalabrasNo().isEmpty()){
				Iterator<String> itLp = panel.getListaPalabrasNo().iterator();
				while(itLp.hasNext()){
					lp+="\n\t\t\t<item>"+ itLp.next()+"</item>";
				}
			}
			lp += "</listaPalabrasNo>\n";
			
			
			fich+=input+"\n"+lp+"</output>";
			fich+="\n"+"</ejercicio>"; 
			String rutaxml="XML"+"/pruebas/ejercicios"+"/ficheroEj.xml";
			File fichero = new File (rutaxml);
			BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
			bw.append(fich);
			bw.close();
			Controlador controlador=vista.getControlador();
			controlador.ejecutaQuery("TALF -ejap "+rutaxml);	
			Automata a=(AutomataPila)controlador.getSalida();
			System.out.println("AUTOMATA GUARDA EJ:" + (AutomataPila)a);
			panel.cargarAutomataNuevo(a);
			panel.setTipoAutomata("AutomataPila");
			return fich;
		}
		if(tipo.equals("Minimizacion")){
			fich+="Minimizacion</tipo><enunciado>"+enunciado.getText()+"</enunciado><input>";
			String input=panel.traducirXML();
			String brr=new Character((char)92).toString();
			fich+=input+"</input><output>";
			String rutaxml=System.getProperty("user.dir")+brr+"XML"+brr+"pruebas"+brr+"fichero.xml";
			File fichero = new File (rutaxml);
			BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
			bw.append(input);
			bw.close();
			Controlador controlador=vista.getControlador();
			controlador.ejecutaQuery("TALF -m-p "+rutaxml);	
			Automata a=(AutomataFD)controlador.getSalida();
			panel.cargarAutomataNuevo(a);
			panel.setTipoAutomata("AutomataFD");
			fich+=panel.traducirXML();
		}
		if(tipo.equals("RE")){
			fich+="RE</tipo><enunciado>"+enunciado.getText()+"</enunciado><input>";
			String input=panel.traducirXML();
			fich+=input+"</input>";
			fich+="<output>\n\t\t\t<RExpr>\n\t\t\t\t<item>"+tex.getText()+"</item>\n\t\t\t</RExpr>";		
		}
		if(tipo.equals("AFNTOAFD")){
			fich+="AFNTOAFD</tipo><enunciado>"+enunciado.getText()+"</enunciado><input>";
			String input=panel.traducirXML();
			String brr=new Character((char)92).toString();
			fich+=input+"</input><output>";
			String rutaxml=System.getProperty("user.dir")+brr+"XML"+brr+"pruebas"+brr+"fichero.xml";
			File fichero = new File (rutaxml);
			BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
			bw.append(input);
			bw.close();
			Controlador controlador=vista.getControlador();
			controlador.ejecutaQuery("TALF -t2-p "+rutaxml);	
			Automata a=(AutomataFD)controlador.getSalida();
			panel.cargarAutomataNuevo(a);
			panel.setTipoAutomata("AutomataFD");
			fich+=panel.traducirXML();
		}
		if(tipo.equals("AFNLTOAFN")){
			fich+="AFNLTOAFN</tipo><enunciado>"+enunciado.getText()+"</enunciado><input>";
			String input=panel.traducirXML();
			String brr=new Character((char)92).toString();
			fich+=input+"</input><output>";
			String rutaxml=System.getProperty("user.dir")+brr+"XML"+brr+"pruebas"+brr+"fichero.xml";
			File fichero = new File (rutaxml);
			BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
			bw.append(input);
			bw.close();
			Controlador controlador=vista.getControlador();
			controlador.ejecutaQuery("TALF -t1-p "+rutaxml);	
			Automata a=(AutomataFND)controlador.getSalida();
			panel.cargarAutomataNuevo(a);
			panel.setTipoAutomata("AutomataFND");
			fich+=panel.traducirXML();
		}
		if(tipo.equals("EquivAutos")){
			fich+="EquivAutos</tipo><enunciado>"+enunciado.getText()+"</enunciado><input>";
			String input=panel.traducirXML();
			fich+=input+"</input><output>";
			String output=panel.traducirXML();
			fich+=output;
		}
		if(tipo.equals("EquivAutoER")){
			fich+="EquivAutoER</tipo><enunciado>"+enunciado.getText()+"</enunciado><input>";
			String input=panel.traducirXML();
			fich+=input+"</input><output>";
			String output=panel.traducirXML();
			fich+=output;
		}
		if(tipo.equals("EquivERAuto")){
			fich+="EquivERAuto</tipo><enunciado>"+enunciado.getText()+"</enunciado>\n\t<input>";
			fich+="\n\t\t<alphabet>";
			String s=alf.getText();
			StringTokenizer st=new StringTokenizer(s,", ");
			while(st.hasMoreTokens()){
				String ss=st.nextToken();
				if(ss.equals("/")){
					throw new AutomatasException(m.devuelveMensaje("vista.lambda",2));
				}
				else{
					if(ss.contains("/")){
						throw new AutomatasException(m.devuelveMensaje("vista.lambda",2));
					} else
						fich+="\n\t\t\t<item>"+ss+"</item>";
				}
			}
			fich+="\n\t\t</alphabet>\n\t</input>\n\t\t<output>\n\t\t\t<RExpr>\n\t\t\t\t<item>"+tex.getText()+"</item>\n\t\t\t</RExpr>\n\t\t";
		}
		if(tipo.equals("EquivERs")){
			fich+="EquivERs</tipo><enunciado>"+enunciado.getText()+"</enunciado>\n\t<input>";
			fich+="\n\t\t<alphabet>";
			String s=alf.getText();
			StringTokenizer st=new StringTokenizer(s,", ");
			while(st.hasMoreTokens()){
				String ss=st.nextToken();
				if(ss.equals("/")){
					throw new AutomatasException(m.devuelveMensaje("vista.lambda",2));
				}
				else{
					if(ss.contains("/")){
						throw new AutomatasException(m.devuelveMensaje("vista.lambda",2));
					} else
						fich+="\n\t\t\t<item>"+ss+"</item>";
				}
			}
			fich+="\n\t\t</alphabet>\n\t</input>\n\t\t<output>\n\t\t\t<RExpr>\n\t\t\t\t<item>"+tex.getText()+"</item>\n\t\t\t</RExpr>\n\t\t";
		}
		fich+="</output>\n</ejercicio>";
		return fich;
	}
	
	/**
	 * Oyente para guardar un ejercicio
	 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteGuardarEjercicio implements ActionListener{
		 
	    private String tipo;
	    private Vista vista;
		 
	    /**
	     * Constructor que establece el tipo de ejrecicio a guardar
	     * @param tipo identifica el tipo de ejercicio
	     * @param v vista para actualizar el arbol con el ejercicio
	     */
		public OyenteGuardarEjercicio(String tipo,Vista v){
		 this.tipo=tipo;
		 vista=v;
		}
		 
		private int numPalabras(String ss){
			int i = 0;
			StringTokenizer st=new StringTokenizer(ss,",");
			while(st.hasMoreTokens()){
				@SuppressWarnings("unused")
				String s=st.nextToken();
				i++;
			}
			return i;
		}
		/**
		 * Guarda el ejercicio al pulsar el boton de guardar
		 * @param e evento de pulsaciñn de raton sobre el boton
		 */
		public void actionPerformed(ActionEvent e){
			Mensajero m=Mensajero.getInstancia();
			try {
				
				if(tipo.equals("MaquinaTuring")){
					System.out.println("palabrasSi: " +  cajitaPalabrasSi.getText());
					System.out.println("palabrasCintaSi: " +  cajitaCintaPalabrasSi.getText());
					System.out.println("palabrasNo: " +  cajitaPalabrasNo.getText());
					System.out.println("palabrasCintaNo: " +  cajitaCintaPalabrasNo.getText());
					System.out.println("palabrasBucle: " +  cajitaPalabrasBucle.getText());

					if(panel.getListaFinales().isEmpty()){
						
						if(numPalabras(cajitaPalabrasSi.getText())/*.length()*/ != 
							numPalabras(cajitaCintaPalabrasSi.getText())/*.length()*/){
							throw new AutomatasException(m.devuelveMensaje("excep.palabrasSi",2));
						}
						else if(numPalabras(cajitaPalabrasNo.getText())/*.length()*/ != 
							numPalabras(cajitaCintaPalabrasNo.getText())/*.length()*/
							){
							throw new AutomatasException(m.devuelveMensaje("excep.palabrasNo",2));
						}
						else{

							System.out.println("oooooook");
						}
					}

				}
				
				String texto=traducirXMLEjercicio(tipo);
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Archivos XML", "xml");
			    chooser.setFileFilter(filter);
			    if(vista.getRutaPredef()!=null){
			    	File dir= new File(vista.getRutaPredef());
			    	chooser.setCurrentDirectory(dir);
			    }
			    int returnVal = chooser.showSaveDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	String brr=new Character((char)92).toString();
			    	vista.setRutaPredef(chooser.getCurrentDirectory().getAbsolutePath());
			    	String ruta=chooser.getCurrentDirectory().getAbsolutePath()+brr+chooser.getSelectedFile().getName();
			    	if(!(ruta.contains(".xml")))ruta+=".xml";
					File fichero = new File (ruta);
					BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
					bw.append(texto);
					bw.close();
					JOptionPane pane=new JOptionPane();
					dialog=pane.createDialog(m.devuelveMensaje("vista.BD",2));
					PanelAniadirEjerBD p=new PanelAniadirEjerBD(dialog,vista,texto,tipo);
					dialog.setContentPane(p);
					dialog.setSize(new Dimension(500,120));
					dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
					dialog.setVisible(true);
			    }
			} catch(IOException ex){
				JOptionPane.showMessageDialog(null,m.devuelveMensaje("parser.entsalida",2),"Error",JOptionPane.ERROR_MESSAGE);
			} catch(AutomatasException exc){
				JOptionPane.showMessageDialog(null,exc.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Limpia el panel quitando el panel de enunciado y borrando el de dibujo
	 */
	public void limpiar() {
		// TODO Auto-generated method stub
		enunciado.setVisible(false);
		corregir.setVisible(false);
		mandar.setVisible(false);
		remove(aux);
		setEjercicio(null);
		panel.borrarPanel();
	}
	
	public AutomataCanvas getCanvas() {
		return panel;
	}

	
}
