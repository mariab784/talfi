/**
 * 
 */
package vista.vistaGrafica;

import vista.Vista;
import vista.vistaGrafica.events.BarraHerramientas;
import vista.vistaGrafica.events.CopiarPegar;
import vista.vistaGrafica.events.OyenteCopiarPegar;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreePath;
import controlador.Controlador;
import controlador.Controlador_imp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;

import accesoBD.AccesoAEjemplos;
import accesoBD.Mensajero;
import accesoBD.ParserEjercicio;
import accesoBD.ParserXML;
import accesoBD.TraductorHTML;
import accesoBD.usuariosBD.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.CardLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import java.util.Vector;

import latexCode.LatexCodeConverter;
import modelo.AutomatasException;
import modelo.algoritmos.AceptaTuring;
import modelo.algoritmos.AutomataP_to_GramaticaIC;
import modelo.algoritmos.GIC_to_FNG;
import modelo.automatas.Alfabeto;

//import modelo.automatas.Alfabeto_Pila;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFD;
import modelo.automatas.AutomataFND;
import modelo.automatas.AutomataFNDLambda;
import modelo.automatas.AutomataPila;
import modelo.automatas.MaquinaTuring;
import modelo.ejercicios.Ejercicio;

/**
 *  En esta clase esta toda la gestion y ejecucion de la interfaz grafica swing de la aplicacion
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class VistaGrafica extends JFrame implements Vista{

	
	private static final long serialVersionUID = 1L;
	private final int MINIMIZACION=10;
	private final int ERTOLAMBDA=11;
	private final int AFNDTOAFD=12;
	private final int EQUIVALENCIA=13;
	private final int AFNDLAMBDATOAFND=14;
	private final int AFDTOER=15;
	private final int GRAMATICA=16;
	private final int FNC=17;
	
	private JTextField expresion;
	private String rutaxml;
	private String rutaxml2;
	private boolean preparadoEquivalencia;
	private JDialog dialog;
	/****/
//	private JDialog d;
	/****/
	private JMenuBar menu;
	private PanelCentral panelCentral;
	private BarraHerramientas tool;
	private JToolBar tool2;
	private JToggleButton estado;
	private JToggleButton arista;
	/****/
	private static JToggleButton estaPalabra;
	private static JToggleButton gordaLatex;
	private static JToggleButton maricaTuring;
	/****/
	private JToggleButton borrar;
	private JToggleButton editar;
	private JToolBar nuevos;
	private JButton nuevoAutomata;
	private JButton nuevaER;
	private JButton ejercicio;
	private JTextArea consola;
	private JTextArea lineaComandos;
	private JPanel panelNorte;
	private Controlador controlador;
	private JTabbedPane sur;
	private JPanel panelIzquierda;
	private JTree arbolDir;
	private JPanel panelUsuario;
	private JComboBox combo;
	private JPanel panel;
	private Usuario usr;
	private boolean admin;
	private String rutaVista;
	private String rutaPredef;
	private JButton delete;
	private boolean dibujar;
	private JButton copiar;
	private JButton pegar;
	private JButton cortar;
	

	private Mensajero mensajero;
	private static boolean pila;
	private static boolean turing;
//	private JTextField nomArs;
	/********************************/
	private JMenuItem grama;
	//private JMenuItem gramaC;
	private JMenuItem AFNDLambda_to_AFND;
	private JMenuItem AFN_to_AFD;
	private JMenuItem MinimizacionAFD;
	private JMenuItem equivalentes;
	private JMenuItem afd_to_er;	
	private JMenu algoritmos;
	/*********************************/
	private LatexCodeConverter codLatex;
	/*********************************/
	/**
	 * Constructor de la ventana de la aplicaciñn, vista grñfica
	 */
	public VistaGrafica(){
		super("TALFi");	
		 
		
		pila = false;
		turing = false;
		
		
		admin=false;
		rutaVista=null;
		rutaPredef=null;
		dibujar=true;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		JOptionPane pane=new JOptionPane();
		JDialog loginD=pane.createDialog("Alta usuario");
		LoginUsuarios login=new LoginUsuarios(loginD);
		loginD.setContentPane(login);
		loginD.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		loginD.setSize(new Dimension(500,120));
		loginD.setVisible(true);
		setVisible(false);
		panelIzquierda=creaPanelIzquierda();
		/***/
		creaPanelNorte2();
		panelCentral=new PanelCentral(this);
		panelNorte=creaPanelNorte();
		/***/
		//panelCentral=new PanelCentral(this);
		controlador=Controlador_imp.getInstancia();
		menu=creaMenu();
		preparadoEquivalencia=false;
		panel=new JPanel(new BorderLayout());
		this.setJMenuBar(menu);
		panel.add(new JScrollPane(panelCentral),BorderLayout.CENTER);
		//panelNorte=creaPanelNorte();
		panel.add(panelNorte,BorderLayout.NORTH);
		panel.add(panelIzquierda,BorderLayout.WEST);
		this.setContentPane(panel);
		panelCentral.desactivarEjercicio();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		Dimension d=panelIzquierda.getSize();
		panelIzquierda.setPreferredSize(d);
		panelIzquierda.setMaximumSize(d);
		requestFocus();
		addKeyListener(new OyenteCopiarPegar(panelCentral.getCanvas()));
		
		
		/*****/
		mensajero=Mensajero.getInstancia();
		/****/
	}
	/****/
	public JToolBar getTool2(){return tool2;}
	/****/
	public static boolean getPila(){return pila;}
	public static boolean getTuring(){return turing;}
	public static void setPila(boolean valor){pila = valor;}
	public static void setTuring(boolean valor){turing = valor;}


	/**
	 * Metodo que permite que el panel pueda tener eventos de teclado
	 */	
	
	public boolean isFocusable(){
		return true;
	}
	
	public void setEnabledgramatica(boolean b){grama.setEnabled(b);}
	public void setEnabledAFNDLambda_to_AFND(boolean b){AFNDLambda_to_AFND.setEnabled(b);}
	public void setEnabledAFN_to_AFD(boolean b){AFN_to_AFD.setEnabled(b);}
	public void setEnabledMinimizacionAFD(boolean b){MinimizacionAFD.setEnabled(b);}
	public void setEnabledEquivalentes(boolean b){equivalentes.setEnabled(b);}
	public void setEnabledAfd_to_er(boolean b){afd_to_er.setEnabled(b);}
	public PanelCentral getPanelCentral(){return panelCentral;}
	

	/**
	 * Reconstruye el panel grafico
	 */
	public void reconstruir() {
		panelCentral=new PanelCentral(this);
		controlador=Controlador_imp.getInstancia();
		menu=creaMenu();
		preparadoEquivalencia=false;
		panel=new JPanel(new BorderLayout());
		this.setJMenuBar(menu);
		JPanel norte=creaPanelNorte();
		panel.add(norte,BorderLayout.NORTH);
		sur=creaPanelSur();
		panel.add(new JScrollPane(panelCentral),BorderLayout.CENTER);
		panelNorte=creaPanelNorte();
		panelIzquierda=creaPanelIzquierda();
		panel.add(panelNorte,BorderLayout.NORTH);
		panel.add(panelIzquierda,BorderLayout.WEST);
		this.setContentPane(panel);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		panelCentral.desactivarEjercicio();
		Dimension d=panelIzquierda.getSize();
		panelIzquierda.setPreferredSize(d);
		panelIzquierda.setMaximumSize(d);
		requestFocus();
	}
	
	private JMenuBar creaMenu(){
		Mensajero mensajero=Mensajero.getInstancia();
		JMenuBar m= new JMenuBar();
		//----------------------------------------------------------------------
		JMenu archivo= new JMenu(mensajero.devuelveMensaje("archivo.texto",1));
		archivo.setToolTipText(mensajero.devuelveMensaje("tooltip.archivo",1));
		archivo.setMnemonic(javax.swing.event.MenuKeyEvent.VK_A);
		JMenuItem nuevo=new JMenuItem(mensajero.devuelveMensaje("archivo.nuevo",1));
		nuevo.setToolTipText(mensajero.devuelveMensaje("tooltip.nuevoAuto",1));
		nuevo.setMnemonic(javax.swing.event.MenuKeyEvent.VK_N);
		nuevo.addActionListener(new OyenteAutomata());
		
		JMenuItem nuevo2=new JMenuItem(mensajero.devuelveMensaje("archivo.nuevo2",1));
		nuevo2.setToolTipText(mensajero.devuelveMensaje("tooltip.nuevo2",1));
		nuevo2.setMnemonic(javax.swing.event.MenuKeyEvent.VK_P);
		nuevo2.addActionListener(new OyenteAutomataPila()); //XXX
		 
		JMenuItem nuevo3=new JMenuItem(mensajero.devuelveMensaje("archivo.nuevo3",1));
		nuevo3.setToolTipText(mensajero.devuelveMensaje("tooltip.nuevo3",1));
		nuevo3.setMnemonic(javax.swing.event.MenuKeyEvent.VK_U);
		nuevo3.addActionListener(new OyenteAutomataTuring()); 
		
		
		
		JMenuItem nuevaER=new JMenuItem(mensajero.devuelveMensaje("archivo.nueva",1));
		nuevaER.setToolTipText(mensajero.devuelveMensaje("tooltip.nuevaER",1));
		nuevaER.setMnemonic(javax.swing.event.MenuKeyEvent.VK_R);
		nuevaER.addActionListener(new OyenteExpr());
		
		JMenuItem abrir=new JMenuItem(mensajero.devuelveMensaje("archivo.abrir",1));
		abrir.setToolTipText(mensajero.devuelveMensaje("tooltip.abrir",1));
		abrir.setMnemonic(javax.swing.event.MenuKeyEvent.VK_O);
		abrir.addActionListener(new OyenteAbrir());
		JMenuItem guardar=new JMenuItem(mensajero.devuelveMensaje("archivo.guardar",1));
		guardar.setToolTipText(mensajero.devuelveMensaje("tooltip.guardar",1));
		guardar.setMnemonic(javax.swing.event.MenuKeyEvent.VK_S);
		guardar.addActionListener(new OyenteGuardar(this));
		JMenuItem guardarc=new JMenuItem(mensajero.devuelveMensaje("archivo.guardarc",1));
		guardarc.setToolTipText(mensajero.devuelveMensaje("tooltip.guardarComo",1));
		guardarc.setMnemonic(javax.swing.event.MenuKeyEvent.VK_C);
		guardarc.addActionListener(new OyenteGuardarC(this));
		JMenuItem abrire=new JMenuItem(mensajero.devuelveMensaje("archivo.guardare",1));
		abrire.setToolTipText(mensajero.devuelveMensaje("tooltip.guardarEj",1));
		abrire.setMnemonic(javax.swing.event.MenuKeyEvent.VK_T);
		abrire.addActionListener(new OyenteAbrirEjer());
		JMenuItem salir=new JMenuItem(mensajero.devuelveMensaje("archivo.salir",1));
		salir.setToolTipText(mensajero.devuelveMensaje("tooltip.salir",1));
		salir.setMnemonic(javax.swing.event.MenuKeyEvent.VK_X);
		salir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		JMenuItem ejer=new JMenuItem(mensajero.devuelveMensaje("archivo.ejercicio",1));
		ejer.setToolTipText(mensajero.devuelveMensaje("tooltip.ejercicio",1));
		ejer.setMnemonic(javax.swing.event.MenuKeyEvent.VK_J);
		ejer.addActionListener(new OyenteEjercicio());
		archivo.add(nuevo);
		archivo.add(nuevo2);
		archivo.add(nuevo3);
		archivo.add(nuevaER);
		if(admin)archivo.add(ejer);
		archivo.add(new JSeparator());
		archivo.add(abrir);
		archivo.add(abrire);
		archivo.add(new JSeparator());
		archivo.add(guardar);
		archivo.add(guardarc);
		archivo.add(new JSeparator());
		archivo.add(salir);
		
		//--------------------------
		JMenu opciones= new JMenu(mensajero.devuelveMensaje("opciones.texto",1));
		opciones.setToolTipText(mensajero.devuelveMensaje("tooltip.opciones",1));
		opciones.setMnemonic(javax.swing.event.MenuKeyEvent.VK_C);
		JMenu idioma=new JMenu(mensajero.devuelveMensaje("opciones.idioma",1));
		idioma.setToolTipText(mensajero.devuelveMensaje("tooltip.idiomas",1));
		
		JMenuItem espanyol=new JMenuItem(mensajero.devuelveMensaje("opciones.Spanish",1));
		espanyol.setToolTipText(mensajero.devuelveMensaje("tooltip.espanyol",1));
		espanyol.setMnemonic(javax.swing.event.MenuKeyEvent.VK_L);
		espanyol.addActionListener(new ActionListener() { 
				             public void actionPerformed( ActionEvent evento )
				             {
				                Mensajero.getInstancia().setIdioma(false);
				                setVisible(false);
				                JMenuBar menuN=creaMenu();
				                menu=menuN;
				                setJMenuBar(menuN);
				                setVisible(true);
				                reconstruir();
				             }}
		); 
		
		JMenuItem ingles=new JMenuItem(mensajero.devuelveMensaje("opciones.English",1));
		ingles.setToolTipText(mensajero.devuelveMensaje("tooltip.ingles",1));
		ingles.setMnemonic(javax.swing.event.MenuKeyEvent.VK_E);
		ingles.addActionListener(new ActionListener() { 
            public void actionPerformed( ActionEvent evento )
            {
               Mensajero.getInstancia().setIdioma(true);
               setVisible(false);
               JMenuBar menuN=creaMenu();
               menu=menuN;
               setJMenuBar(menuN);
               setVisible(true);
               reconstruir();
            }}
		);
		
		idioma.add(espanyol);
		idioma.add(ingles);
		
		opciones.add(idioma);
		//-----------------------------
		JMenu ayuda= new JMenu(mensajero.devuelveMensaje("ayuda.texto",1));
		ayuda.setToolTipText(mensajero.devuelveMensaje("tooltip.ayuda",1));
		ayuda.setMnemonic(javax.swing.event.MenuKeyEvent.VK_H);
		JMenuItem manual=new JMenuItem(mensajero.devuelveMensaje("ayuda.manual",1));
		manual.setToolTipText(mensajero.devuelveMensaje("tooltip.manual",1));
		manual.setMnemonic(javax.swing.event.MenuKeyEvent.VK_M);
		manual.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String brr=new Character((char)92).toString();
				muestraHtml(true,System.getProperty("user.dir")+brr+"HTML"+brr+"Ayuda"+brr+"ayuda.pdf");
				requestFocus();
			}
		});
		ayuda.add(manual);
		//---------------------menu algoritmos
		/*JMenu*/ algoritmos= new JMenu(mensajero.devuelveMensaje("algoritmos.texto",1));
		algoritmos.setToolTipText(mensajero.devuelveMensaje("tooltip.algoritmos",1));
		algoritmos.setMnemonic(javax.swing.event.MenuKeyEvent.VK_L);
		
		/*JMenuItem*/ AFNDLambda_to_AFND=new JMenuItem(mensajero.devuelveMensaje("algoritmos.AFNDLambda_to_AFND",1));
		AFNDLambda_to_AFND.setToolTipText(mensajero.devuelveMensaje("tooltip.AFNDLtoAFND",1));
		AFNDLambda_to_AFND.setMnemonic(javax.swing.event.MenuKeyEvent.VK_N);
		AFNDLambda_to_AFND.addActionListener(new OyenteMenuItem(AFNDLAMBDATOAFND));
		
		/*JMenuItem*/ AFN_to_AFD=new JMenuItem(mensajero.devuelveMensaje("algoritmos.AFN_to_AFD",1));
		AFN_to_AFD.setToolTipText(mensajero.devuelveMensaje("tooltip.AFNDtoAFD",1));
		AFN_to_AFD.setMnemonic(javax.swing.event.MenuKeyEvent.VK_F);
		AFN_to_AFD.addActionListener(new OyenteMenuItem(AFNDTOAFD));
		
		/*JMenuItem*/ MinimizacionAFD=new JMenuItem(mensajero.devuelveMensaje("algoritmos.MinimizacionAFD",1));
		MinimizacionAFD.setToolTipText(mensajero.devuelveMensaje("tooltip.minimizacion",1));
		MinimizacionAFD.setMnemonic(javax.swing.event.MenuKeyEvent.VK_M);
		MinimizacionAFD.addActionListener(new OyenteMenuItem(MINIMIZACION));
		
		/*JMenuItem*/ equivalentes=new JMenuItem(mensajero.devuelveMensaje("algoritmos.equivalentes",1));
		equivalentes.setToolTipText(mensajero.devuelveMensaje("tooltip.equivalencia",1));
		equivalentes.setMnemonic(javax.swing.event.MenuKeyEvent.VK_E);
		equivalentes.addActionListener(new OyenteMenuItem(EQUIVALENCIA));
		
		/*JMenuItem*/ afd_to_er=new JMenuItem(mensajero.devuelveMensaje("algoritmos.afd_to_er",1));
		afd_to_er.setToolTipText(mensajero.devuelveMensaje("tooltip.equivalencia",1));
		afd_to_er.setMnemonic(javax.swing.event.MenuKeyEvent.VK_T);
		afd_to_er.addActionListener(new OyenteMenuItem(AFDTOER));
		
		//XXX
		/*JMenuItem*/ grama=new JMenuItem(mensajero.devuelveMensaje("algoritmos.gramatica",1)); 
		grama.setToolTipText(mensajero.devuelveMensaje("tooltip.gramatica",1));
		grama.setMnemonic(javax.swing.event.MenuKeyEvent.VK_G);
		grama.addActionListener(new OyenteMenuItem(GRAMATICA));
	

		//XXX
		
		algoritmos.add(AFNDLambda_to_AFND);
		algoritmos.add(AFN_to_AFD);
		algoritmos.add(MinimizacionAFD);
		algoritmos.add(equivalentes);
		algoritmos.add(afd_to_er);
		//XXX
		algoritmos.add(grama);
		//algoritmos.add(gramaC);
		
		setEnabledMenuAlgMT();
		//XXX
		
		JMenu usuarios=new JMenu(mensajero.devuelveMensaje("usuarios.texto",1));
		usuarios.setToolTipText(mensajero.devuelveMensaje("tooltip.usu",1));
		usuarios.setMnemonic(javax.swing.event.MenuKeyEvent.VK_U);
		
		JMenuItem consultarInfo=new JMenuItem(mensajero.devuelveMensaje("usuarios.consultar",1));
		consultarInfo.setToolTipText(mensajero.devuelveMensaje("tooltip.info",1));
		consultarInfo.setMnemonic(javax.swing.event.MenuKeyEvent.VK_F);
		consultarInfo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane pane=new JOptionPane();
				dialog=pane.createDialog("Datos usuario");
				DatosUsuario p= new DatosUsuario(dialog,usr);
				dialog.setContentPane(p);
				dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				dialog.setSize(new Dimension(600,350));
				dialog.setVisible(true);
				requestFocus();
			}
		});
		
		JMenuItem actualizar=new JMenuItem(mensajero.devuelveMensaje("usuarios.actualizar",1));
		actualizar.setToolTipText(mensajero.devuelveMensaje("tooltip.actualizar",1));
		actualizar.setMnemonic(javax.swing.event.MenuKeyEvent.VK_D);
		actualizar.addActionListener(new OyenteActualizar(this));
			
		
		JMenuItem buscarAlumno=new JMenuItem(mensajero.devuelveMensaje("usuarios.buscar",1));
		buscarAlumno.setToolTipText(mensajero.devuelveMensaje("tooltip.buscar",1));
		buscarAlumno.setMnemonic(javax.swing.event.MenuKeyEvent.VK_F);
		buscarAlumno.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane pane=new JOptionPane();
				dialog=pane.createDialog("Busca usuario");
				BuscaUsuario p= new BuscaUsuario(dialog);
				dialog.setContentPane(p);
				dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				dialog.setSize(new Dimension(500,120));
				dialog.setVisible(true);
				requestFocus();
			}
		});
		
		usuarios.add(consultarInfo);
		usuarios.add(actualizar);
		if(admin) usuarios.add(buscarAlumno);
		//------------------------------------
		m.add(archivo);
		m.add(opciones);
		m.add(algoritmos);
		m.add(ayuda);
		m.add(usuarios);
		return m;
	}
	/*********************/
	

	
	private ArrayList<String> construir(boolean b){
		
		try{

			AutomataCanvas c = panelCentral.getCanvas();
			
			AutomataPila ap = new AutomataPila(c.getEstadoInicial(),c.getListaFinales(),c.getAlfabeto(),
					c.getAlfabetoPila(),c.getNombreEstados(),c.getListaAristasPila());

			System.out.println("**********************************************************");
			System.out.println("AP CONSTRUIDO BOTON::: \n" + ap);
			System.out.println("**********************************************************");
			AutomataP_to_GramaticaIC g = new AutomataP_to_GramaticaIC(ap);
			//System.out.println("producciones: " + g.getGic());
			//g.AP_Gramatica();
			
//		System.out.println("gramatica boton: \n" + g.getGic());
			
			
			GIC_to_FNG piticli = new GIC_to_FNG(g.getGic(),b);

							piticli.simplifica(true,false);
			
			System.out.println("ENTRADA:\n" + piticli.getGramaticaEntrada());
			System.out.println("SALIDA:\n" + piticli.getGramaticaSalida());

			piticli.getGramaticaSalida().creaListaPalabras(); 

			ArrayList<String> pg = null;
			pg = piticli.getGramaticaSalida().dameListaPalabras();
			return pg;
	 	
		}
		catch(Exception ex){
			if ( panelCentral.getCanvas().getListaEstados().isEmpty() ) //XXX
				JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
		return null; //para ke no pete
		
	}
	
	private void creaPanelNorte2(){
		
		
		tool2= new JToolBar();
		
		estaPalabra=new JToggleButton(new ImageIcon("imagenes/estaPalabra.jpg"));

		estaPalabra.setMargin(new java.awt.Insets(0,0,0,0));
		estaPalabra.setName("estaPalabra");
//		estaPalabra.setToolTipText("ASDJAKDFJSAHJ"); //XXX
		
		gordaLatex=new JToggleButton(new ImageIcon("imagenes/botonltx.jpg"));
		gordaLatex.setMargin(new java.awt.Insets(0,0,0,0));
		gordaLatex.setName("gordaLatex");
//		estaPalabra.setToolTipText("LTX"); //XXX		
		
		maricaTuring=new JToggleButton(new ImageIcon("imagenes/MT.jpg"));
		maricaTuring.setMargin(new java.awt.Insets(0,0,0,0));
		maricaTuring.setName("gordaLatex");
//		estaPalabra.setToolTipText("LTX"); //XXX	

		tool2.add(estaPalabra); //aniadirBotton
		tool2.add(gordaLatex); //aniadirBotton
		tool2.add(maricaTuring); //aniadirBotton
		
		maricaTuring.addActionListener(new ActionListener(){
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				actionPerformedOpenDialog(e);

			}
			
			
		});
		
		gordaLatex.addActionListener(new ActionListener(){
            
            
			public void actionPerformed(ActionEvent e) {
				
				codLatex = LatexCodeConverter.getInstancia(panelCentral);
				codLatex.convertir();
            }


         
         
        });
		
		estaPalabra.addActionListener(new ActionListener(){
			
			
			public void actionPerformed(ActionEvent e) {
				
				
				JOptionPane pane=new JOptionPane();
				
				dialog=pane.createDialog(null);
				JPanel panelD=new JPanel(new GridLayout(4,1));
				JPanel panelC=new JPanel(new GridLayout(1,4));
				//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX XXX XXX
				JLabel etiqN=new JLabel(mensajero.devuelveMensaje("vista.listaPalabras",2) + construir(true).toString()); //XXX
				
				//	nomArs=new JTextField();
				
				/*nomArs.addKeyListener(new KeyAdapter(){
					public void keyPressed(KeyEvent e){
						if(e.getKeyCode()==KeyEvent.VK_ENTER){
							actionAceptar();
						
						}
					}
				});*/
				
				JPanel panelB=  new JPanel();
				JButton aceptar=new JButton(mensajero.devuelveMensaje("vista.aceptar",2));
				aceptar.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						//actionAceptar();
			//			construir(/*true*/false);
						dialog.setVisible(false);
					}
				});
				//JButton cancelar=new JButton(mensajero.devuelveMensaje("vista.cancelar",2));
				
		/*		cancelar.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						construir(false);
						dialog.setVisible(false);
					}
				});*/
				panelB.add(aceptar);
				//panelB.add(cancelar);
				panelD.add(etiqN);
				//panelD.add(nomArs);
				panelD.add(panelC);
				panelD.add(panelB);
				dialog.setContentPane(panelD);
				dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				dialog.setSize(new Dimension(400,150));
				dialog.setVisible(true);
				

			}
			
			
		});
		
	}
	
	private JPanel creaPanelNorte(){
		Mensajero mensajero=Mensajero.getInstancia();
		

		
		JPanel n= new JPanel();
		n.setBackground(Color.BLUE);
		nuevos= new JToolBar();
		nuevoAutomata=new JButton(new ImageIcon("imagenes/automata.jpg"));
		nuevoAutomata.setMargin(new java.awt.Insets(0,0,0,0));
		nuevoAutomata.setToolTipText(mensajero.devuelveMensaje("tooltip.nuevoAuto",1));
		nuevoAutomata.addActionListener(new OyenteAutomata());
		nuevaER=new JButton(new ImageIcon("imagenes/er.jpeg"));
		nuevaER.setMargin(new java.awt.Insets(0,0,0,0));
		nuevaER.setToolTipText(mensajero.devuelveMensaje("tooltip.nuevaER",1));
		nuevaER.addActionListener(new OyenteExpr());
		ejercicio=new JButton(new ImageIcon("imagenes/ejercicio.jpg"));
		ejercicio.setMargin(new java.awt.Insets(0,0,0,0));
		ejercicio.setToolTipText(mensajero.devuelveMensaje("tooltip.ejercicio",1));
		ejercicio.addActionListener(new OyenteEjercicio());
		nuevos.add(nuevoAutomata);
		nuevos.add(nuevaER);
		if(admin)nuevos.add(ejercicio);
		tool=new BarraHerramientas(panelCentral.getPanel());
		
		estado=new JToggleButton(new ImageIcon("imagenes/estado.jpg"));
		estado.setMargin(new java.awt.Insets(0,0,0,0));
		estado.setToolTipText(mensajero.devuelveMensaje("tooltip.estado",1));
		estado.setName("estado");
		arista=new JToggleButton(new ImageIcon("imagenes/linea.jpg"));
		arista.setMargin(new java.awt.Insets(0,0,0,0));
		arista.setName("arista");
		arista.setToolTipText(mensajero.devuelveMensaje("tooltip.arista",1)); //XXX

		borrar=new JToggleButton(new ImageIcon("imagenes/borrar.jpg"));
		borrar.setMargin(new java.awt.Insets(0,0,0,0));
		borrar.setToolTipText(mensajero.devuelveMensaje("tooltip.borrar",1));
		borrar.setName("borrar");
		editar=new JToggleButton(new ImageIcon("imagenes/editar.jpg"));
		editar.setMargin(new java.awt.Insets(0,0,0,0));
		editar.setToolTipText(mensajero.devuelveMensaje("tooltip.editar",1));
		editar.setName("editar");
		JToolBar del= new JToolBar();
		delete=new JButton(new ImageIcon("imagenes/borrar.jpeg"));
		delete.setToolTipText(mensajero.devuelveMensaje("tooltip.borraP",1));
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				panelCentral.limpiar();
				repaint();
				rutaVista=null;
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		copiar=new JButton(new ImageIcon("imagenes/copiar.jpg"));
		copiar.setToolTipText(mensajero.devuelveMensaje("tooltip.copiar",1));
		copiar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Mensajero m=Mensajero.getInstancia();
				try {
					CopiarPegar cp=CopiarPegar.getInstancia();
					cp.setAutomata(panelCentral.getPanel().traducirXML());
					pegar.setEnabled(cp.sePuedePegar());
				} catch(AutomatasException ex){
					JOptionPane.showMessageDialog(null,ex.getMensaje()+"\n"+m.devuelveMensaje("vista.ercopy", 2),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		pegar=new JButton(new ImageIcon("imagenes/pegar.jpg"));
		pegar.setToolTipText(mensajero.devuelveMensaje("tooltip.pegar",1));
		pegar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Mensajero m=Mensajero.getInstancia();
				try {
					if(!dibujar) return;
					if(panelCentral.getPanel().getListaEstados().size()==0){
						CopiarPegar cp=CopiarPegar.getInstancia();
						panelCentral.getPanel().cargarAutomata(cp.getAutomata());
					} else {
						JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.yaauto", 2),"Error",JOptionPane.ERROR_MESSAGE);
					}
				} catch(AutomatasException ex){
					JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		CopiarPegar cp=CopiarPegar.getInstancia();
		pegar.setEnabled(cp.sePuedePegar());
		cortar=new JButton(new ImageIcon("imagenes/cortar.jpg"));
		cortar.setToolTipText(mensajero.devuelveMensaje("tooltip.cortar",1));
		cortar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Mensajero m=Mensajero.getInstancia();
				try {
					CopiarPegar cp=CopiarPegar.getInstancia();
					cp.setAutomata(panelCentral.getPanel().traducirXML());
					panelCentral.getPanel().borrarPanel();
					pegar.setEnabled(cp.sePuedePegar());
				} catch(AutomatasException ex){
					JOptionPane.showMessageDialog(null,ex.getMensaje()+"\n"+m.devuelveMensaje("vista.ercut", 2),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		del.add(delete);
		del.add(copiar);
		del.add(cortar);
		del.add(pegar);
		desactivaToogleButtons();
		n.add(nuevos);
		n.add(del);
		n.add(tool);
		n.add(tool2);
		tool.aniadirBotton(editar);
		tool.aniadirBotton(estado);
		tool.aniadirBotton(arista);
		tool.aniadirBotton(borrar);


		setGordaLatex(false);
		setEstaPalabra(false);
		setMaricaTuring(false);
		
		JToolBar expr=new JToolBar();
		JPanel panelEr=new JPanel();
		JLabel e =new JLabel(mensajero.devuelveMensaje("algoritmos.er",1));
		expresion=new JTextField(20);
		expresion.setEditable(false);
		expresion.setEnabled(false);
		panelEr.add(e);
		panelEr.add(expresion);
		expr.add(panelEr);
		n.add(expr);
		return n;
	}
	
	private JTabbedPane creaPanelSur(){
		Mensajero m=Mensajero.getInstancia();
		JTabbedPane s=new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);
		JPanel pc=new JPanel(new CardLayout());
		consola=new JTextArea();
		consola.setEditable(false);
		consola.setRows(8);
		consola.getDocument().addDocumentListener(new DocumentListener(){
			public void insertUpdate(DocumentEvent e){
				// TODO Auto-generated method stub
				sur.setSelectedIndex(1);
			}

			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				sur.setSelectedIndex(1);
			}

			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				sur.setSelectedIndex(1);
			}
		});
		JPanel pl=new JPanel(new CardLayout());
		lineaComandos=new JTextArea();
		lineaComandos.setEditable(true);
		lineaComandos.setRows(8);
		lineaComandos.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER){
					try{
						controlador.ejecutaQuery(lineaComandos.getText());
					}catch (AutomatasException e1) {
                        // TODO Auto-generated catch block
						Mensajero m=Mensajero.getInstancia();
						JOptionPane.showMessageDialog(null,e1.getMensaje(),m.devuelveMensaje("vista.titulo",2),JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		String mconsola=m.devuelveMensaje("log.consola",1);
		String mlcomandos=m.devuelveMensaje("log.comandos",1);
		s.addTab(mlcomandos,new ImageIcon("imagenes/comandos.gif"),pl);
		pl.add(new JScrollPane(lineaComandos),"linea de comandos");
		s.addTab(mconsola,new ImageIcon("imagenes/consola.gif"),pc);
		pc.add(new JScrollPane(consola),"consola");
		return s;
	}
	
	private JPanel creaPanelIzquierda(){
		Mensajero m=Mensajero.getInstancia();
		JPanel izquierda=new JPanel(new BorderLayout());
		AccesoAEjemplos ae=AccesoAEjemplos.getInstancia();
		HashMap<String,ArrayList<String>> datosEjemplos=null;
		HashMap<String,ArrayList<String>> datosEjercicios=null;
		try {
			datosEjemplos=ae.devolverListadoEjemplos();
			datosEjercicios=ae.devolverListadoEjercicios();
		} catch (AutomatasException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Vector<String> cabecera=new Vector<String>();
		cabecera.add("Ejemplos de automatas");
		cabecera.add("Ejercicios");
		Vector<String> ejemplosAFD=new Vector<String>();
		ejemplosAFD.addAll(datosEjemplos.get("AFD"));
		
		Vector<String> ejemplosAFND=new Vector<String>();
		ejemplosAFND.addAll(datosEjemplos.get("AFN"));
		
		Vector<String> ejemplosAFNDLambda=new Vector<String>();
		ejemplosAFNDLambda.addAll(datosEjemplos.get("AFNLambda"));
	
		/*********************************************************/
		Vector<String> ejemplosAP=new Vector<String>();
		ejemplosAP.addAll(datosEjemplos.get("APila"));
		
		Vector<String> ejemplosMT=new Vector<String>();
		ejemplosMT.addAll(datosEjemplos.get("MTuring"));
		/*********************************************************/
		Hashtable<String,Vector<String>> hsEjemplos=new Hashtable<String,Vector<String>>();
		hsEjemplos.put(m.devuelveMensaje("jtree.eafd",1), ejemplosAFD);
		hsEjemplos.put(m.devuelveMensaje("jtree.eafn",1), ejemplosAFND);
		hsEjemplos.put(m.devuelveMensaje("jtree.eafnl",1),ejemplosAFNDLambda);
		/*********************************************************/
		hsEjemplos.put(m.devuelveMensaje("jtree.eap",1),ejemplosAP);
		hsEjemplos.put(m.devuelveMensaje("jtree.emt",1),ejemplosMT);
		/*********************************************************/
		
		////////////////////////////////////////////////
		Hashtable<String,Vector<String>> hsEjercicios=new Hashtable<String,Vector<String>>();
		Vector<String> ejerciciosLenguaje=new Vector<String>();
		ejerciciosLenguaje.addAll(datosEjercicios.get("Lenguaje"));
		
		Vector<String> ejerciciosMinimizacion=new Vector<String>();
		ejerciciosMinimizacion.addAll(datosEjercicios.get("minimizacion"));
		
		Vector<String> ejerciciosAFNAFD=new Vector<String>();
		ejerciciosAFNAFD.addAll(datosEjercicios.get("AFD_TO_AFN"));
		
		
		Vector<String> ejerciciosAFNLAMBDA=new Vector<String>();
		ejerciciosAFNLAMBDA.addAll(datosEjercicios.get("AFNLambda_TO_AFN"));
		
		Vector<String> ejerciciosAFDTOER=new Vector<String>();
		ejerciciosAFDTOER.addAll(datosEjercicios.get("AFD_TO_ER"));
		
		Vector<String> ejerciciosEquivAutos=new Vector<String>();
		ejerciciosEquivAutos.addAll(datosEjercicios.get("EquivAutos"));
		
		Vector<String> ejerciciosEquivAutoER=new Vector<String>();
		ejerciciosEquivAutoER.addAll(datosEjercicios.get("EquivAutoER"));
		
		Vector<String> ejerciciosEquivERAuto=new Vector<String>();
		ejerciciosEquivERAuto.addAll(datosEjercicios.get("EquivERAuto"));
		
		Vector<String> ejerciciosEquivERs=new Vector<String>();
		ejerciciosEquivERs.addAll(datosEjercicios.get("EquivERs"));
		
		Vector<String> ejsTransformacionPila=new Vector<String>();
		ejsTransformacionPila.addAll(datosEjercicios.get("TransformacionPila"));
		
		Vector<String> ejsMaquinaTuring=new Vector<String>();
		ejsMaquinaTuring.addAll(datosEjercicios.get("MaquinasDeTuring"));
		
		
		hsEjercicios.put(m.devuelveMensaje("jtree.lenguaje",1), ejerciciosLenguaje);
		hsEjercicios.put(m.devuelveMensaje("jtree.minim",1), ejerciciosMinimizacion);
		hsEjercicios.put(m.devuelveMensaje("jtree.afn",1),ejerciciosAFNAFD);
		hsEjercicios.put(m.devuelveMensaje("jtree.afnl",1), ejerciciosAFNLAMBDA);
		hsEjercicios.put(m.devuelveMensaje("jtree.afdtoer",1), ejerciciosAFDTOER);
		hsEjercicios.put(m.devuelveMensaje("jtree.eautos",1), ejerciciosEquivAutos);
		hsEjercicios.put(m.devuelveMensaje("jtree.eautoer",1), ejerciciosEquivAutoER);
		hsEjercicios.put(m.devuelveMensaje("jtree.eerauto",1), ejerciciosEquivERAuto);
		hsEjercicios.put(m.devuelveMensaje("jtree.eers",1), ejerciciosEquivERs);
		hsEjercicios.put(m.devuelveMensaje("jtree.taps",1), ejsTransformacionPila);
		hsEjercicios.put(m.devuelveMensaje("jtree.mturings",1), ejsMaquinaTuring);
		
		Hashtable<String,Hashtable<String,Vector<String>>> hs=new Hashtable<String,Hashtable<String,Vector<String>>>();
		hs.put(m.devuelveMensaje("jtree.ejemplos",1), hsEjemplos);
		hs.put(m.devuelveMensaje("jtree.ejercicios",1), hsEjercicios);
		arbolDir=new JTree(hs);
		MouseListener ml = new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		         int selRow = arbolDir.getRowForLocation(e.getX(), e.getY());
		         TreePath selPath = arbolDir.getPathForLocation(e.getX(), e.getY());
		         if(selRow != -1) {
		             if(e.getClickCount() == 1) {
		                 un_solo_click(selRow, selPath);
		             }
		             else if(e.getClickCount() == 2) {
		                 dobleClick(selRow, selPath);
		             }
		         }
		     }
		 };
		arbolDir.addMouseListener(ml);
		JPanel panelAux=new JPanel();
		panelAux.setBackground(Color.white);
		panelAux.add(arbolDir);
		izquierda.add(new JScrollPane(panelAux),BorderLayout.CENTER);
		panelUsuario=creaPanelUsuario();
		izquierda.add(panelUsuario,BorderLayout.SOUTH);
		return izquierda;
	}
	
	private JPanel creaPanelUsuario(){
		Mensajero m=Mensajero.getInstancia();
		JPanel panelUsuario=new JPanel(new GridLayout(5,1));
		JPanel titulo=new JPanel();
		titulo.setBackground(Color.BLUE);
		JLabel t=new JLabel(m.devuelveMensaje("usuarios.titulo",1));
		t.setForeground(Color.white);
		titulo.add(t);
		usr=Usuario.getInstancia();
		JLabel nusuario=new JLabel();
		nusuario.setForeground(Color.white);
		JLabel ejersCons=new JLabel();
		ejersCons.setForeground(Color.white);
		JLabel ejersNoCons=new JLabel();
		ejersNoCons.setForeground(Color.white);
		JLabel idioma=new JLabel();
		idioma.setForeground(Color.white);
		if (usr.getNombre()!=null) {
			if(usr.getNombre().equals("administrador")) admin=true;
			nusuario=new JLabel(m.devuelveMensaje("usuarios.nombre",1)+usr.getNombre());
			nusuario.setForeground(Color.white);
			ejersCons=new JLabel(m.devuelveMensaje("usuarios.cons",1)+usr.getEjerciciosConsultados().size());
			ejersCons.setForeground(Color.white);
			ejersNoCons=new JLabel(m.devuelveMensaje("usuarios.bien",1)+usr.getEjerciciosBien().size());
			ejersNoCons.setForeground(Color.white);
			idioma=new JLabel(m.devuelveMensaje("usuarios.mal",1)+usr.getEjerciciosMal().size());
			idioma.setForeground(Color.white);
		}
		panelUsuario.add(titulo);
		panelUsuario.add(nusuario);
		panelUsuario.add(ejersCons);
		panelUsuario.add(ejersNoCons);
		panelUsuario.add(idioma);
		panelUsuario.setBackground(Color.DARK_GRAY);
		return panelUsuario;
	}
	
	private void un_solo_click(int selRow,TreePath selPath) {
		//mostrar algo con la descripciñn del ejemplo???
	}
	
	private void dobleClick(int selRow, TreePath selPath) {
		//llamada al AutomataCanvas con el xml contenido en la entrada.
		int cantidad=selPath.getPathCount();
		Object tipoEjemplo=selPath.getPathComponent(cantidad-2);
		Object nombreEjemplo=selPath.getLastPathComponent();
		
		Mensajero m=Mensajero.getInstancia();
		if(tipoEjemplo.toString().equals(m.devuelveMensaje("jtree.eafd",1))) {
			panelCentral.getPanel().cargarAutomata("XML/ejemplos/AFD/"+nombreEjemplo.toString()+".xml");
			panelCentral.getPanel().setTipoAutomata("AutomataFD");
			deleteExpresion();
			activaToogleButtons();
			panelCentral.desactivarEjercicio();
			this.repaint();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			VistaGrafica.setOpcionesAF();
		}
		if(tipoEjemplo.toString().equals(m.devuelveMensaje("jtree.eafn",1))) {
			panelCentral.getPanel().cargarAutomata("XML/ejemplos/AFND/"+nombreEjemplo.toString()+".xml");
			panelCentral.getPanel().setTipoAutomata("AutomataFND");
			deleteExpresion();
			activaToogleButtons();
			panelCentral.desactivarEjercicio();
			this.repaint();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			VistaGrafica.setOpcionesAF();
		}
		if(tipoEjemplo.toString().equals(m.devuelveMensaje("jtree.eafnl",1))) {
			panelCentral.getPanel().cargarAutomata("XML/ejemplos/AFND_lambda/"+nombreEjemplo.toString()+".xml");
			panelCentral.getPanel().setTipoAutomata("AutomataFNDLambda");
			deleteExpresion();
			activaToogleButtons();
			panelCentral.desactivarEjercicio();
			this.repaint();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			VistaGrafica.setOpcionesAF();
		}
		
		/*********************************************************/
		if(tipoEjemplo.toString().equals(m.devuelveMensaje("jtree.eap",1))) {
			panelCentral.getPanel().cargarAutomata("XML/ejemplos/AP/"+nombreEjemplo.toString()+".xml");
			panelCentral.getPanel().setTipoAutomata("AutomataPila");
			deleteExpresion();
			activaToogleButtons();
			panelCentral.desactivarEjercicio();
			this.repaint(); 
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			VistaGrafica.setEstaPalabra(true);
			VistaGrafica.setGordaLatex(true);
			VistaGrafica.setMaricaTuring(false);
		}
		if(tipoEjemplo.toString().equals(m.devuelveMensaje("jtree.emt",1))) {
			panelCentral.getPanel().cargarAutomata("XML/ejemplos/MT/"+nombreEjemplo.toString()+".xml");
			panelCentral.getPanel().setTipoAutomata("MaquinaTuring");
			deleteExpresion();
			activaToogleButtons();
			panelCentral.desactivarEjercicio();
			this.repaint();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			VistaGrafica.setEstaPalabra(false);
			VistaGrafica.setGordaLatex(true);
			VistaGrafica.setMaricaTuring(true);
		}
		/*********************************************************/
		
		if(tipoEjemplo.toString().equals(m.devuelveMensaje("jtree.lenguaje",1))) {
			panelCentral.cargarEjercicio("XML/ejercicios/Lenguaje/"+nombreEjemplo.toString()+".xml");
			deleteExpresion();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			VistaGrafica.setOpcionesAF();
		}
		
		if(tipoEjemplo.toString().equals(m.devuelveMensaje("jtree.minim",1))) {
			panelCentral.cargarEjercicio("XML/ejercicios/Minimizacion/"+nombreEjemplo.toString()+".xml");
			deleteExpresion();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			VistaGrafica.setOpcionesAF();
		}
		
		if(tipoEjemplo.toString().equals(m.devuelveMensaje("jtree.afn",1))) {
			panelCentral.cargarEjercicio("XML/ejercicios/TransformacionAFNAFD/"+nombreEjemplo.toString()+".xml");
			deleteExpresion();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			VistaGrafica.setOpcionesAF();
		}
		
		if(tipoEjemplo.toString().equals(m.devuelveMensaje("jtree.afnl",1))) {
			panelCentral.cargarEjercicio("XML/ejercicios/TransformacionAFNLambda_AFN/"+nombreEjemplo.toString()+".xml");
			activaToogleButtons();
			deleteExpresion();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			VistaGrafica.setOpcionesAF();
		}
		
		if(tipoEjemplo.toString().equals(m.devuelveMensaje("jtree.afdtoer",1))) {
			panelCentral.cargarEjercicio("XML/ejercicios/TransformacionAFD_to_ER/"+nombreEjemplo.toString()+".xml");
			deleteExpresion();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			VistaGrafica.setOpcionesAF();
		}

		if(tipoEjemplo.toString().equals(m.devuelveMensaje("jtree.taps",1))) {
			panelCentral.cargarEjercicio("XML/ejercicios/Transformacion_Pila/"+nombreEjemplo.toString()+".xml");
			deleteExpresion();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			VistaGrafica.setOpcionesAP();
		}
		
		requestFocus();
		
		if(tipoEjemplo.toString().equals(m.devuelveMensaje("jtree.mturings",1))) {
			panelCentral.cargarEjercicio("XML/ejercicios/Maquina_Turing/"+nombreEjemplo.toString()+".xml");
			deleteExpresion();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			VistaGrafica.setOpcionesMT();
		}
		
		requestFocus();
	}
	
	
	private void ejecuta(int algoritmo,boolean pasos){
		String rutahtml=new String();
		Mensajero m=Mensajero.getInstancia();
		try{
			if (algoritmo==GRAMATICA){
				rutaxml="XML/pruebas/ficheroG.xml";
				File fichero = new File (rutaxml);
				BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
				bw.append(panelCentral.getPanel().traducirXML());
				bw.close();
				
			}
			else if (algoritmo==FNC){
				rutaxml="XML/pruebas/ficheroC.xml";
				File fichero = new File (rutaxml);
				BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
				bw.append(panelCentral.getPanel().traducirXML());
				bw.close();
				System.out.println("xmlfnc: " + panelCentral.getPanel().traducirXML());
				
			}
			else if(preparadoEquivalencia || algoritmo!=EQUIVALENCIA){
				rutaxml="XML/pruebas/fichero.xml";
				File fichero = new File (rutaxml);
				BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
				bw.append(panelCentral.getPanel().traducirXML());
				bw.close();
			}	
			else {
				rutaxml2="XML/pruebas/fichero2.xml";
				File fichero2 = new File (rutaxml2);
				BufferedWriter bw = new BufferedWriter(new FileWriter(fichero2));
				bw.append(panelCentral.getPanel().traducirXML());
				bw.close();
				String img1="HTML/imagenEQV1.jpg";
				TraductorHTML.getInstancia().generarImagenJPG(img1,ParserXML.getInstancia().extraerAutomata(rutaxml2));
			}
			switch(algoritmo){
			case GRAMATICA:{
				//preparadoEquivalencia=false;
				if (pasos){
					String ruta="HTML/imagenEntrada.jpg";
					panelCentral.getPanel().generarImagenJPg(ruta);
					controlador.ejecutaQuery("TALF -gr-p "+rutaxml);						
					TraductorHTML trhtml=TraductorHTML.getInstancia();
					String xmlSalida=controlador.salidaXML();
					rutahtml=trhtml.traducirPasosSimplificacion(xmlSalida);
				}
				else controlador.ejecutaQuery("TALF -gr "+rutaxml);
				
				Automata a=(AutomataPila)controlador.getSalida();
				setTuring(false);
				setPila(true);
				panelCentral.getPanel().cargarAutomataNuevo(a);
				panelCentral.getPanel().setTipoAutomata("AutomataPila");
				activaToogleButtons();
				deleteExpresion();
				rutaVista=null;
				break;
			}
			case FNC:{
				//preparadoEquivalencia=false;
				if (pasos){
					String ruta="HTML/imagenEntrada.jpg";
					System.out.println("rutaxmlfnc" + rutaxml);
					panelCentral.getPanel().generarImagenJPg(ruta);
					controlador.ejecutaQuery("TALF -grfnc-p "+rutaxml);						
					TraductorHTML trhtml=TraductorHTML.getInstancia();
					String xmlSalida=controlador.salidaXML();
					System.out.println("xmlsalidaes: " + xmlSalida);
					rutahtml=trhtml.traducirPasosSimplificacionFNC(xmlSalida);
				}
				else controlador.ejecutaQuery("TALF -grfnc "+rutaxml);
				
				Automata a=(AutomataPila)controlador.getSalida();
				setTuring(false);
				setPila(true);
				panelCentral.getPanel().cargarAutomataNuevo(a);
				panelCentral.getPanel().setTipoAutomata("AutomataPila");
				activaToogleButtons();
				deleteExpresion();
				rutaVista=null;
				break;
			}
				case MINIMIZACION:{
					preparadoEquivalencia=false;
					if (pasos){
						String ruta="HTML/imagenEntrada.jpg";
						panelCentral.getPanel().generarImagenJPg(ruta);
						controlador.ejecutaQuery("TALF -m-p "+rutaxml);						
						TraductorHTML trhtml=TraductorHTML.getInstancia();
						String xmlSalida=controlador.salidaXML();
						rutahtml=trhtml.traducirPasosMinimizacion(xmlSalida);
					}
					else controlador.ejecutaQuery("TALF -m "+rutaxml);
					Automata a=(AutomataFD)controlador.getSalida();

					setTuring(false);
					setPila(false);
					panelCentral.getPanel().cargarAutomataNuevo(a);
					panelCentral.getPanel().setTipoAutomata("AutomataFD");
					activaToogleButtons();
					deleteExpresion();
					rutaVista=null;
					break;
				}
				case EQUIVALENCIA:{
					if(preparadoEquivalencia){
						if (pasos) {
							controlador.ejecutaQuery("TALF -e-p "+ rutaxml +" "+rutaxml2);
							TraductorHTML trhtml=TraductorHTML.getInstancia();
							String xmlSalida=controlador.salidaXML();
							rutahtml=trhtml.traducirPasosEquivalencia(xmlSalida);
							rutaVista=null;
						}
						else controlador.ejecutaQuery("TALF -e "+rutaxml+" "+rutaxml2);
						setTuring(false);
						setPila(false);
						boolean a=(Boolean)controlador.getSalida();
						if(a)
							JOptionPane.showMessageDialog(this,m.devuelveMensaje("equivalenciajo.si", 2),m.devuelveMensaje("equivalenciajo.equiv",2),JOptionPane.INFORMATION_MESSAGE);
						else 	
							JOptionPane.showMessageDialog(this,m.devuelveMensaje("equivalenciajo.no", 2),m.devuelveMensaje("equivalenciajo.noequiv",2),JOptionPane.INFORMATION_MESSAGE);
						preparadoEquivalencia=false;
					} else {
						JOptionPane.showMessageDialog(this,m.devuelveMensaje("equivalenciajo.segundo", 2),m.devuelveMensaje("equivalenciajo.second",2), JOptionPane.INFORMATION_MESSAGE);
						panelCentral.getPanel().borrarPanel();
						preparadoEquivalencia=true;
					}
					deleteExpresion();
					break;
				}
				case ERTOLAMBDA:{
					preparadoEquivalencia=false;
					if (pasos){
						controlador.ejecutaQuery("TALF -r-p "+rutaxml);
						TraductorHTML trhtml=TraductorHTML.getInstancia();
						String xmlSalida=controlador.salidaXML();
						rutahtml=trhtml.traducirPasosER_AFNDL(xmlSalida);
					}
					else controlador.ejecutaQuery("TALF -r "+rutaxml);
					
					setTuring(false);
					setPila(false);
					Automata a=(AutomataFNDLambda)controlador.getSalida();
					panelCentral.getPanel().cargarAutomataNuevo(a);
					panelCentral.getPanel().setTipoAutomata("AutomataFNDLambda");
					activaToogleButtons();
					deleteExpresion();
					rutaVista=null;
					break;
				}
				case AFNDTOAFD:{
					preparadoEquivalencia=false;
					if (pasos){
						String ruta="HTML/imagenEntrada.jpg";
						panelCentral.getPanel().generarImagenJPg(ruta);
						controlador.ejecutaQuery("TALF -t2-p "+rutaxml);
						TraductorHTML trhtml=TraductorHTML.getInstancia();
						String xmlSalida=controlador.salidaXML();
						rutahtml=trhtml.traducirPasosAFND_AFD(xmlSalida);
					}
					else controlador.ejecutaQuery("TALF -t2 "+rutaxml);
					
					setTuring(false);
					setPila(false);
					Automata a=(AutomataFD)controlador.getSalida();
					panelCentral.getPanel().cargarAutomataNuevo(a);
					panelCentral.getPanel().setTipoAutomata("AutomataFD");
					activaToogleButtons();
					deleteExpresion();
					rutaVista=null;
					break;
				}
				case AFNDLAMBDATOAFND:{
					preparadoEquivalencia=false;
					if (pasos) {
						String ruta="HTML/imagenEntrada.jpg";
						panelCentral.getPanel().generarImagenJPg(ruta);
						controlador.ejecutaQuery("TALF -t1-p "+rutaxml);
						TraductorHTML trhtml=TraductorHTML.getInstancia();
						String xmlSalida=controlador.salidaXML();
						rutahtml=trhtml.traducirPasosAFNDL_AFND(xmlSalida);
					}
					else controlador.ejecutaQuery("TALF -t1 "+rutaxml);
					
					setTuring(false);
					setPila(false);
					Automata a=(AutomataFND)controlador.getSalida();
					panelCentral.getPanel().cargarAutomataNuevo(a);
					panelCentral.getPanel().setTipoAutomata("AutomataFND");
					activaToogleButtons();
					deleteExpresion();
					rutaVista=null;
					break;
				}
				case AFDTOER:{
					preparadoEquivalencia=false;
					if (pasos) {
						String ruta="HTML/imagenEntrada.jpg";
						panelCentral.getPanel().generarImagenJPg(ruta);
						controlador.ejecutaQuery("TALF -t3-p "+rutaxml);
						TraductorHTML trhtml=TraductorHTML.getInstancia();
						String xmlSalida=controlador.salidaXML();
						rutahtml=trhtml.traducirPasosAFDTOER(xmlSalida);
					}
					else controlador.ejecutaQuery("TALF -t3 "+rutaxml);
					
					setTuring(false);
					setPila(false);
					String a=(String)controlador.getSalida();
					setExpresion(a);
					JOptionPane.showMessageDialog(this,a,"AFD to ER",JOptionPane.INFORMATION_MESSAGE);
					rutaVista=null;
					break;
				}
			}
		} catch(AutomatasException e){
			JOptionPane.showMessageDialog(this,e.getMensaje(),m.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
		}catch(IOException e){
			JOptionPane.showMessageDialog(this,m.devuelveMensaje("parser.entsalida", 2),m.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
			panelCentral.getPanel().borrarPanel();
		}
		muestraHtml(pasos,rutahtml);
	}
	
	
	/**
	 * Muestra una pagina web html
	 */
	public void muestraHtml(boolean pasos,String rutahtml){
		if (pasos) {
			Mensajero m=Mensajero.getInstancia();
			String url=rutahtml;
			String osName = System.getProperty("os.name");
		      try {
		         if (osName.startsWith("Mac OS")) {
		            Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
		            Method openURL = fileMgr.getDeclaredMethod("openURL",
		               new Class[] {String.class});
		            openURL.invoke(null, new Object[] {url});
		            }
		         else if (osName.startsWith("Windows"))
		            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
		         else { //assume Unix or Linux
		            String[] browsers = {
		               "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
		            String browser = null;
		            for (int count = 0; count < browsers.length && browser == null; count++)
		               if (Runtime.getRuntime().exec(
		                     new String[] {"which", browsers[count]}).waitFor() == 0)
		                  browser = browsers[count];
		            if (browser == null) {
		            	JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.navegador", 2),"Error",JOptionPane.ERROR_MESSAGE);
		            } else {
		               Runtime.getRuntime().exec(new String[] {browser, url});
		            }
		         }
		      }
		      catch (Exception e) {
		    	  JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.ejecucion", 2),"Error",JOptionPane.ERROR_MESSAGE);
		      }
		}
	}

	
	public void trataMensaje(String mensaje) {
		// TODO Auto-generated method stub
		//consola.append(mensaje);
	}
	
	
	/**
	 * Devuelve el controlador
	 */
	public Controlador getControlador(){
		return controlador;
	}
	
	
	public void setAutomata(Automata a) {
		panelCentral.getPanel().cargarAutomataNuevo(a);
		//consola.append(a.toString()); //XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX
		System.out.println("SET AUTOMATA: " + a);
	}

	/**
	 * Clase oyente del boton de menu pare ejecutar algortimos que se distinguen segñn
	 * el entro que se le pasa al oyente
	 * @author Miguel Ballesteros, Josñ Antnio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteMenuItem implements ActionListener {
		
		private int algoritmo;
		private boolean pasos;
		
		/**
		 * Constructor de la casle que recibe el tipo de algoritmo
		 * @param algoritmo identiica el algortimo
		 */
		public OyenteMenuItem(int algoritmo){
			this.algoritmo=algoritmo;
			pasos=false;
		}
		
		
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
			if  (algoritmo == GRAMATICA){
				JOptionPane pane=new JOptionPane();
				Mensajero m=Mensajero.getInstancia();
				dialog=pane.createDialog(null,m.devuelveMensaje("pasos.preguntat",2));
				JPanel panel=new JPanel(new GridLayout(2,1));
				JPanel botones=new JPanel();
				JLabel pasosE=new JLabel(m.devuelveMensaje("pasos.tipos",2));
				JButton aceptar=new JButton(m.devuelveMensaje("pasos.fng",2));
				aceptar.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						pasos=true;
						dialog.setVisible(false);
						ejecuta(algoritmo,pasos); //XXX
						rutaVista=null;
					}
				});
				JButton cancelar=new JButton(m.devuelveMensaje("pasos.fnc",2));
				cancelar.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						pasos=true;
						dialog.setVisible(false);
						ejecuta(FNC,pasos);
						rutaVista=null;
					}
				});
				panel.add(pasosE);
				botones.add(aceptar);
				botones.add(cancelar);
				panel.add(botones);
				dialog.setContentPane(panel);
				dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				dialog.setSize(new Dimension(300,150));
				dialog.setVisible(true);
				
				
			}
			else if(preparadoEquivalencia || algoritmo!=EQUIVALENCIA){
				JOptionPane pane=new JOptionPane();
				Mensajero m=Mensajero.getInstancia();
				dialog=pane.createDialog(null,m.devuelveMensaje("pasos.pregunta",2));
				JPanel panel=new JPanel(new GridLayout(2,1));
				JPanel botones=new JPanel();
				JLabel pasosE=new JLabel(m.devuelveMensaje("pasos.preguntaG",2));
				JButton aceptar=new JButton(m.devuelveMensaje("pasos.con",2));
				aceptar.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						pasos=true;
						dialog.setVisible(false);
						ejecuta(algoritmo,pasos);
						rutaVista=null;
					}
				});
				JButton cancelar=new JButton(m.devuelveMensaje("pasos.sin",2));
				cancelar.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						pasos=false;
						dialog.setVisible(false);
						ejecuta(algoritmo,pasos);
						rutaVista=null;
					}
				});
				panel.add(pasosE);
				botones.add(aceptar);
				botones.add(cancelar);
				panel.add(botones);
				dialog.setContentPane(panel);
				dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				dialog.setSize(new Dimension(300,150));
				dialog.setVisible(true);
				
			} else {
				
				ejecuta(algoritmo,pasos);
			}
		}
	}
	
	
	public void activaToogleButtons(){
		editar.setEnabled(true);
		borrar.setEnabled(true);
		arista.setEnabled(true);
		estado.setEnabled(true);
		

		dibujar=true;
		panelCentral.getPanel().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}
	
	
	public void desactivaToogleButtons(){
		editar.setEnabled(false);
		borrar.setEnabled(false);
		arista.setEnabled(false);
		estado.setEnabled(false);
		estaPalabra.setEnabled(false);
		maricaTuring.setEnabled(false);
		gordaLatex.setEnabled(false);
		dibujar=false;
		panelCentral.getPanel().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	
	public void setExpresion(String expr){
		expresion.setText(expr);
		expresion.setEnabled(true);
	}
	
	
	public void deleteExpresion(){
		expresion.setText("");
		expresion.setEnabled(false);
	}

	
	public String traducirXML(String exp,Alfabeto alfabeto) throws AutomatasException {
		try {
			String rutaxml="XML/pruebas/fichero.xml";
			File fichero = new File (rutaxml);
			BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
			bw.append(introduceER(exp,alfabeto));
			bw.close();
			return rutaxml;
		} catch (IOException e){
			Mensajero m=Mensajero.getInstancia();
			throw new AutomatasException(m.devuelveMensaje("parser.entsalida", 2));
		}
	}
	
	private String introduceER(String expresion,Alfabeto alfabeto){
		String fichero="<authomata>\n\t<RExpr>\n\t\t<item>"+expresion+"</item>\n\t</RExpr>\n\t<alphabet>\n";
		Iterator<String> ialf=alfabeto.dameListaLetras().iterator();
		while(ialf.hasNext()){
			fichero+="\t\t<item>"+ialf.next()+"</item>\n";
		}
		fichero+="\t</alphabet>\n</authomata>";
		return fichero;
	}
	
	
	public void ejecuta() {
		// TODO Auto-generated method stub
	}

	/**
	 * Clase que se encarga de preparar el panel de dibujos
	 * @author Rocño Barrigñete, Mario Huete, Luis San Juan
	 *
	 */
	public class OyenteAutomataGeneral implements ActionListener{
		
		
		public void actionPerformed(ActionEvent e){

			panelCentral.getPanel().borrarPanel();
			activaToogleButtons();
			rutaVista=null;
			
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			requestFocus();
		}
	}
	
	/**
	 * Clase que se encarga de preparar el panel de dibujos para cear automatas
	 * @author Miguel Ballesteros, Josñ Antnio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteAutomata extends OyenteAutomataGeneral implements ActionListener{
		
		public void actionPerformed(ActionEvent e){

			super.actionPerformed(e);
			VistaGrafica.setPila(false);
			VistaGrafica.setTuring(false);
			
			
			VistaGrafica.setOpcionesAF();
			setEnabledMenuAlgAF();
		}
	}
	
	/**
	 * Clase que se encarga de preparar el panel de dibujos para cear automatas de pila
	 * @author Rocño Barrigñete, Mario Huete, Luis San Juan
	 *
	 */
	public class OyenteAutomataPila extends OyenteAutomataGeneral implements ActionListener{
		
		
		public void actionPerformed(ActionEvent e){

	/*		panelCentral.getPanel().borrarPanel();
			activaToogleButtons();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			requestFocus();*/
			super.actionPerformed(e);
			VistaGrafica.setPila(true);
			VistaGrafica.setTuring(false);
			getPanelCentral().getPanel().setTipoAutomata("AutomataPila");

			VistaGrafica.setOpcionesAP();
			setEnabledMenuAlgAP();
		}
	}
	
	/**
	 * Clase que se encarga de preparar el panel de dibujos para cear automatas de Turing
	 * @author Rocño Barrigñete, Mario Huete, Luis San Juan
	 *
	 */
	public class OyenteAutomataTuring extends OyenteAutomataGeneral implements ActionListener{
		
		
		public void actionPerformed(ActionEvent e){

		/*	panelCentral.getPanel().borrarPanel();
			activaToogleButtons();
			rutaVista=null;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			requestFocus();*/
			super.actionPerformed(e);
			VistaGrafica.setPila(false);
			VistaGrafica.setTuring(true);
			getPanelCentral().getPanel().setTipoAutomata("MaquinaTuring");

			VistaGrafica.setOpcionesMT();
			setEnabledMenuAlgMT();
		}
	}
	
	/**
	 * Clase que se encarga de mostrar las opciones para creaciñn de automats a partir
	 * de una expresiñn regular introducida
	 * @author Miguel Ballesteros, Josñ Antnio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteExpr implements ActionListener{
		
		
		public void actionPerformed(ActionEvent e){
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			Mensajero m=Mensajero.getInstancia();
			panelCentral.desactivarEjercicio();
			JOptionPane pane=new JOptionPane();
			dialog=pane.createDialog(m.devuelveMensaje("vista.alfabeto", 2));
			PanelAlfabeto alf=new PanelAlfabeto(dialog);
			dialog.setContentPane(alf);
			dialog.setSize(new Dimension(500,120));
			dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			dialog.setVisible(true);
			if(alf.getAceptar()){
				dialog=pane.createDialog(m.devuelveMensaje("vista.expresion", 2));
				PanelExpresionRegular p=new PanelExpresionRegular(dialog);
				dialog.setContentPane(p);
				dialog.setSize(new Dimension(300,150));
				dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				dialog.setVisible(true);
				String query=null;
				String ruta=null;
				Alfabeto alfabeto=alf.getAlfabeto();
				try {
					switch(p.getOpcion()){
						case 0:{
							requestFocus();
							break;
						}
						case 1:{
							String exp=p.getExp();
							ruta=traducirXML(exp,alfabeto);
							if(p.isPasos()){
								query="TALF -r-p "+ruta;
								controlador.ejecutaQuery(query);
								TraductorHTML trhtml=TraductorHTML.getInstancia();
								String xmlSalida=controlador.salidaXML();
								String rutahtml=trhtml.traducirPasosER_AFNDL(xmlSalida);
								muestraHtml(p.isPasos(),rutahtml);
							}
							else{
								query="TALF -r "+ruta;
								controlador.ejecutaQuery(query);
							}
							Automata a=(AutomataFNDLambda)controlador.getSalida();
							panelCentral.getPanel().cargarAutomataNuevo(a);
							panelCentral.getPanel().setTipoAutomata("AutomataFNDLambda");
							activaToogleButtons();
							setExpresion(exp);
							rutaVista=null;
							requestFocus();
							break;
						}
						case 2:{
							String exp="%";
							ruta=traducirXML(exp,alfabeto);
							if(p.isPasos()){
								query="TALF -r-p "+ruta;
								TraductorHTML trhtml=TraductorHTML.getInstancia();
								String xmlSalida=controlador.salidaXML();
								String rutahtml=trhtml.traducirPasosER_AFNDL(xmlSalida);
								muestraHtml(p.isPasos(),rutahtml);
							}
							else {
								query="TALF -r "+ruta;
								controlador.ejecutaQuery(query);
							}
							Automata a=(AutomataFNDLambda)controlador.getSalida();
							panelCentral.getPanel().cargarAutomataNuevo(a);
							activaToogleButtons();
							setExpresion(exp);
							rutaVista=null;
							requestFocus();
							break;
						}
					}
					VistaGrafica.setOpcionesAF();
					setEnabledMenuAlgAF();
				} catch(AutomatasException ex){
					JOptionPane.showMessageDialog(null,ex.getMensaje(),m.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
					panelCentral.getPanel().borrarPanel();
					requestFocus();
				}
			}
		}
	}
	
	
	








	
	
	
	
	
	/**
	 * Clase que se encarga de las gestiones del usuario para gusrdar automtas en
	 * archivos xml
	 * @author Miguel Ballesteros, Josñ Antnio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteAbrir implements ActionListener{
		 
		
		public void actionPerformed(ActionEvent e){
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Archivos XML", "xml");
		    chooser.setFileFilter(filter);
		    if(rutaPredef!=null){
		    	File dir= new File(rutaPredef);
		    	chooser.setCurrentDirectory(dir);
		    }
		    int returnVal = chooser.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	try{
		    		panelCentral.desactivarEjercicio();
		    		String brr=new Character((char)92).toString();
		    		rutaPredef=chooser.getCurrentDirectory().getAbsolutePath();
		    		String ruta=chooser.getCurrentDirectory().getAbsolutePath()+brr+chooser.getSelectedFile().getName();
		    		Automata a=ParserXML.getInstancia().extraerAutomata(ruta);
		            setAutomata(a);
      
		            switch(a.dameTipo()){ 
		             
		            case 0: panelCentral.getPanel().setTipoAutomata("AutomataFD");
		            setOpcionesAF();
		            setPila(false);
		            setTuring(false);
		            break;
		            	
		            case 1: panelCentral.getPanel().setTipoAutomata("AutomataFND"); 
		            setOpcionesAF();
		            setPila(false);
		            setTuring(false);
		            break;
		            	
		            case 2: panelCentral.getPanel().setTipoAutomata("AutomataFNDLambda");
		            setOpcionesAF();
		            setPila(false);
		            setTuring(false);
		            break;
		            	
		            case 3:	panelCentral.getPanel().setTipoAutomata("AutomataPila"); 
					setGordaLatex(true);
					setEstaPalabra(true);
					setMaricaTuring(false);
		            setPila(true);
		            setTuring(false);
		            break;
		            	
		            case 4: panelCentral.getPanel().setTipoAutomata("MaquinaTuring"); 
					setGordaLatex(true);
					setEstaPalabra(false);
					setMaricaTuring(true);
					setPila(false);
		            setTuring(true);
		            break;
		            
		            
		            }
		            
/*		            if(a instanceof AutomataFNDLambda) {
		            	panelCentral.getPanel().setTipoAutomata("AutomataFNDLambda");}
		            else if(a instanceof AutomataFND) {
		            	panelCentral.getPanel().setTipoAutomata("AutomataFND");}
		            else if(a instanceof AutomataFD) {
		            	panelCentral.getPanel().setTipoAutomata("AutomataFD");}
		            /*********************************************************/	            		
/*		            if(a instanceof AutomataPila){
		            	panelCentral.getPanel().setTipoAutomata("AutomataPila");}
		            else if(a instanceof MaquinaTuring){
		            	panelCentral.getPanel().setTipoAutomata("MaquinaTuring");}  
		            /*********************************************************/
		            
		            
		            activaToogleButtons();
		            rutaVista=ruta;
		            requestFocus();
		    	} catch(AutomatasException ex) {
		    		JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
		    		requestFocus();
		    	}
		    }
		}
	}
	
	public static void setOpcionesAF(){
		setGordaLatex(true);
		setEstaPalabra(false);
		setMaricaTuring(false);
	}
	
	public static void setOpcionesAP(){
		setGordaLatex(true);
		setEstaPalabra(true);
		setMaricaTuring(false);
	}
	
	public static void setOpcionesMT(){
		setGordaLatex(true);
		setEstaPalabra(false);
		setMaricaTuring(true);
	}
	
	public void setEnabledMenuAlgAF(){
		this.setEnabledAfd_to_er(true);
		this.setEnabledAFN_to_AFD(true);
		this.setEnabledAFNDLambda_to_AFND(true);
		setEnabledMinimizacionAFD(true);
		this.setEnabledEquivalentes(true);
		this.setEnabledgramatica(false);
	}
	public void setEnabledMenuAlgAP(){		
	this.setEnabledAfd_to_er(false);
	this.setEnabledAFN_to_AFD(false);
	this.setEnabledAFNDLambda_to_AFND(false);
	setEnabledMinimizacionAFD(false);
	this.setEnabledEquivalentes(false);
	this.setEnabledgramatica(true);
	}
	public void setEnabledMenuAlgMT(){
		this.setEnabledAfd_to_er(false);
		this.setEnabledAFN_to_AFD(false);
		this.setEnabledAFNDLambda_to_AFND(false);
		setEnabledMinimizacionAFD(false);
		this.setEnabledEquivalentes(false);
		this.setEnabledgramatica(false);
	}
	
	/**
	 * Clase que se encarga de realizar las estiones con el usuario de guardar los
	 * automatas en archivos xml
	 * @author Miguel Ballesteros, Josñ Antnio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteGuardarC implements ActionListener{
		
		private Vista vista;
		
		/**
		 * Crea el oyente con la vista que se le pasa
		 * @param v vita grafica para actualizar el arbol con el ejemplo
		 */
		public OyenteGuardarC(Vista v){
			vista=v;
		}
		
		
		public void actionPerformed(ActionEvent e){
			Mensajero m= Mensajero.getInstancia();
			try {
				if(panelCentral.getEjercicio()==null){
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "Archivos XML", "xml");
				    chooser.setFileFilter(filter);
				    if(rutaPredef!=null){
				    	File dir= new File(rutaPredef);
				    	chooser.setCurrentDirectory(dir);
				    }
				    int returnVal = chooser.showSaveDialog(null);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    	String texto=panelCentral.getPanel().traducirXML();
				    	String brr=new Character((char)92).toString();
						String ruta=chooser.getCurrentDirectory().getAbsolutePath()+brr+chooser.getSelectedFile().getName();
						rutaPredef=chooser.getCurrentDirectory().getAbsolutePath();
						if(!(ruta.contains(".xml")))ruta+=".xml";
						File fichero = new File (ruta);
						try {
							BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
							bw.append(texto);
							bw.close();
							JOptionPane pane=new JOptionPane();
							dialog=pane.createDialog(m.devuelveMensaje("vista.BD", 2));
							PanelAniadirEjemploBD p=new PanelAniadirEjemploBD(dialog,vista,texto,panelCentral.getPanel().getTipoAutomata());
							dialog.setContentPane(p);
							dialog.setSize(new Dimension(500,120));
							dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
							dialog.setVisible(true);
							rutaVista=ruta;
							requestFocus();
						} catch(IOException ex){
							JOptionPane.showMessageDialog(null,m.devuelveMensaje("parser.entsalida", 2),"error",JOptionPane.ERROR_MESSAGE);
							requestFocus();
						}
				    }
				} else {
					JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.guardarE", 2),m.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
					requestFocus();
				}
		    } catch(AutomatasException ex){
		    	JOptionPane.showMessageDialog(null,ex.getMensaje(),m.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
		    	requestFocus();
		    }
		}
	}
	
	/**
	 * Clase que se encarga de realizar las estiones con el usuario de guardar los
	 * automatas en archivos xml, si ya estaba guardado en un ruta lo guarda en la
	 * misma sobreescribiñndolo, sino muestra un dialogo de guardado
	 * @author Miguel Ballesteros, Josñ Antnio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteGuardar implements ActionListener{
		
		private Vista vista;
		
		/**
		 * Crea el oyente con la vista que se le pasa
		 * @param v vita grafica para actualizar el arbol con el ejemplo
		 */
		public OyenteGuardar(Vista v){
			vista=v;
		}
		
		
		public void actionPerformed(ActionEvent e){
			Mensajero m=Mensajero.getInstancia();
			try {
				if(panelCentral.getEjercicio()==null){
					if(rutaVista!=null){
						try {
							String texto=panelCentral.getPanel().traducirXML();
							File fichero = new File (rutaVista);
							BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
							bw.append(texto);
							bw.close();
							JOptionPane pane=new JOptionPane();
							dialog=pane.createDialog(m.devuelveMensaje("vista.BD", 2));
							PanelAniadirEjemploBD p=new PanelAniadirEjemploBD(dialog,vista,texto,panelCentral.getPanel().getTipoAutomata());
							dialog.setContentPane(p);
							dialog.setSize(new Dimension(500,120));
							dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
							dialog.setVisible(true);
							requestFocus();
						} catch(IOException ex){
							JOptionPane.showMessageDialog(null,m.devuelveMensaje("parser.entsalida", 2),"error",JOptionPane.ERROR_MESSAGE);
							requestFocus();
						}
					} else {
						JFileChooser chooser = new JFileChooser();
						FileNameExtensionFilter filter = new FileNameExtensionFilter(
					        "Archivos XML", "xml");
					    chooser.setFileFilter(filter);
					    if(rutaPredef!=null){
					    	File dir= new File(rutaPredef);
					    	chooser.setCurrentDirectory(dir);
					    }
					    int returnVal = chooser.showSaveDialog(null);
					    if(returnVal == JFileChooser.APPROVE_OPTION) {
					    	String texto=panelCentral.getPanel().traducirXML();
					    	String brr=new Character((char)92).toString();
					    	rutaPredef=chooser.getCurrentDirectory().getAbsolutePath();
							String ruta=chooser.getCurrentDirectory().getAbsolutePath()+brr+chooser.getSelectedFile().getName()+".xml";
							File fichero = new File (ruta);
							try {
								BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
								bw.append(texto);
								bw.close();
								JOptionPane pane=new JOptionPane();
								dialog=pane.createDialog(m.devuelveMensaje("vista.BD", 2));
								PanelAniadirEjemploBD p=new PanelAniadirEjemploBD(dialog,vista,texto,panelCentral.getPanel().getTipoAutomata());
								dialog.setContentPane(p);
								dialog.setSize(new Dimension(500,120));
								dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
								dialog.setVisible(true);
								rutaVista=ruta;
								requestFocus();
							} catch(IOException ex){
								JOptionPane.showMessageDialog(null,m.devuelveMensaje("parser.entsalida", 2),"Error",JOptionPane.ERROR_MESSAGE);
								requestFocus();
							}
				    	}
					}
				} else {
					JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.guardarE", 2),m.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
					requestFocus();
				}
		    } catch(AutomatasException ex){
		    	JOptionPane.showMessageDialog(null,ex.getMensaje(),m.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
		    	requestFocus();
		    }
		}
	}

	/**
	 * Calse que se encarga de la implementaciñn de guardar ejericios segñn
	 * su tipo, y de la creaciñn del panel correspondiente
	 * @author Miguel Ballesteros, Josñ Antnio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteEjercicio implements ActionListener{
	
		
		public void actionPerformed(ActionEvent e){
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			Mensajero m=Mensajero.getInstancia();
			JOptionPane pane=new JOptionPane();
			dialog=pane.createDialog(m.devuelveMensaje("vista.tipoE", 2));
			JPanel content=new JPanel();
			JLabel lab=new JLabel(m.devuelveMensaje("vista.tipoEjer", 2));
			Vector<String> v=new Vector<String>();
			v.add("Minimizacion");
			v.add("Lenguaje");
			v.add("RE");
			v.add("AFNTOAFD");
			v.add("AFNLTOAFN");
			v.add("EquivAutos");
			v.add("EquivAutoER");
			v.add("EquivERAuto");
			v.add("EquivERs");
			v.add("AutomatasDePila");
			v.add("MaquinaTuring");
			combo=new JComboBox(v);
			JPanel panelB= new JPanel();
			JButton aceptar= new JButton(m.devuelveMensaje("vista.aceptar",2));
			aceptar.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ev){
					panelCentral.crearEjercicio(combo.getSelectedItem().toString());
					dialog.setVisible(false);
				}
			});
			JButton cancelar=new JButton(m.devuelveMensaje("vista.cancelar",2));
			cancelar.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ev){
					dialog.setVisible(false);
				}
			});
			panelB.add(aceptar);
			panelB.add(cancelar);
			content.add(lab);
			content.add(combo);
			content.add(panelB);
			dialog.setContentPane(content);
			dialog.setSize(new Dimension(500,120));
			dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			dialog.setVisible(true);
			requestFocus();
		}
	}

	/**
	 * Clase que se encarga de gestionar la forma de guardar los ejercicios en archivos xml
	 * @author Miguel Ballesteros, Josñ Antnio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteAbrirEjer implements ActionListener{
		
		
		public void actionPerformed(ActionEvent e){
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			Mensajero m=Mensajero.getInstancia();
			try {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Archivos XML", "xml");
			    chooser.setFileFilter(filter);
			    if(rutaPredef!=null){
			    	File dir= new File(rutaPredef);
			    	chooser.setCurrentDirectory(dir);
			    }
			    int returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	ParserEjercicio parser= new ParserEjercicio();
			    	String brr=new Character((char)92).toString();
			    	rutaPredef=chooser.getCurrentDirectory().getAbsolutePath();
		    		String ruta=chooser.getCurrentDirectory().getAbsolutePath()+brr+chooser.getSelectedFile().getName();
		    		Ejercicio ej=parser.extraerEjercicio(ruta);
		    		if(ej!=null){
		    			panelCentral.setEjercicio(ej);
		    			panelCentral.cargarEjercicio();
		    			desactivaToogleButtons();
		    			requestFocus();
		    		} else {
		    			JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.noejer", 2),m.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
		    			requestFocus();
		    		}
			    }
		    } catch(AutomatasException ex){
		    	JOptionPane.showMessageDialog(null,ex.getMensaje(),m.devuelveMensaje("vista.ejecucion", 2),JOptionPane.ERROR_MESSAGE);
		    	requestFocus();
		    }
		}
	}

	/**
	 * Clase que se encarga de gestionar la actualizaciñn de suarios de la base de datos
	 * @author Miguel Ballesteros, Josñ Antnio Blanes, Samer Nabhan
	 *
	 */
	public class OyenteActualizar implements ActionListener{
		
		private Vista vista;
		
		/**
		 * Creaciñn del oyente que recibe la vista sobre la que se actualzia el usuario
		 * @param v vista de la que se gestiona el usuario a actualziar
		 */
		public OyenteActualizar(Vista v){
			vista=v;
		}
		
		
		public void actionPerformed(ActionEvent e){
			Mensajero m=Mensajero.getInstancia();
			JOptionPane pane=new JOptionPane();
			dialog=pane.createDialog(m.devuelveMensaje("vista.update", 2));
			ActualizaUsuario p= new ActualizaUsuario(dialog,usr,vista);
			dialog.setContentPane(p);
			dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			dialog.setSize(new Dimension(500,160));
			dialog.setVisible(true);
			requestFocus();
		}
	}

	
	public void reconstruirPanelUsuario() {
		// TODO Auto-generated method stub
		panelIzquierda.setVisible(false);
		panelIzquierda.remove(panelUsuario);
		panelUsuario=creaPanelUsuario();
		panelIzquierda.add(panelUsuario,BorderLayout.SOUTH);
		panelIzquierda.setVisible(true);
		requestFocus();
	}
	
	
	public void reconstruirPanelJTree() {
		// TODO Auto-generated method stub
		panel.setVisible(false);
		panel.remove(panelIzquierda);
		panelIzquierda=creaPanelIzquierda();
		panel.add(panelIzquierda,BorderLayout.WEST);
		panel.setVisible(true);
		this.setVisible(true);
		Dimension d=panelIzquierda.getSize();
		panelIzquierda.setPreferredSize(d);
		panelIzquierda.setMaximumSize(d);
		requestFocus();
	}
	
	
	public boolean dibujar(){
		return dibujar;
	}

	
	public String getRutaPredef() {
		return rutaPredef;
	}

	
	public void setRutaPredef(String rutaPredef) {
		this.rutaPredef = rutaPredef;
	}


	
	public void setPegar() {
		// TODO Auto-generated method stub
		pegar.setEnabled(true);
	}
	public void actionPerformedOpenDialog (ActionEvent ae) {
		JFileChooser _fileChooser = new JFileChooser();
		int retval = _fileChooser.showOpenDialog(null); 
        if (retval == JFileChooser.APPROVE_OPTION) { 
        	File archivo = _fileChooser.getSelectedFile (); 
        	archivo = _fileChooser.getSelectedFile ();
        	String ruta = archivo.getAbsolutePath();
        	System.out.println(ruta);
        	//try catch XXX
			AutomataCanvas c = panelCentral.getCanvas();
			MaquinaTuring m = new MaquinaTuring(c.getEstadoInicial(),c.getListaFinales(),c.getAlfabeto(),
					c.getAlfabetoCinta(),c.getNombreEstados(),c.getListaAristasTuring());
			
			System.out.println("ACTIONPERFORMED: " + m);
			
			AceptaTuring aceptacion = new AceptaTuring(ruta,m);
			aceptacion.acepta();
        }
	}
	
	public static void setGordaLatex(boolean b){gordaLatex.setEnabled(b);}
	public static void setEstaPalabra(boolean b){estaPalabra.setEnabled(b);}
	public static void setMaricaTuring(boolean b){maricaTuring.setEnabled(b);}
}
