package vista.vistaGrafica.events;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import vista.vistaGrafica.Arista;
import vista.vistaGrafica.AristaAP;
import vista.vistaGrafica.AristaGeneral;
import vista.vistaGrafica.AristaTuring;
import vista.vistaGrafica.AutomataCanvas;
import vista.vistaGrafica.Estado;
import accesoBD.Mensajero;

/**
 * Clase que se encarga de añadir y borrar los estados al dibujo si la infromación
 * que se le pide al usuario es correcta y borrar aristas del dibujo
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class CanvasMouseAdapter extends MouseAdapter {

	private final int borrar=2;
	private final int estado=3;
	private JTextField nomEst;
	private String nombreEstado;
	private boolean tieneEstadoInicial;
	private int cordX;
	private int cordY;
	private boolean esFinal;
	private boolean esInicial;
	private Mensajero mensajero;
	private JDialog dialog;
	private AutomataCanvas canvas;
	
	/**
	 * Constructor del mouseAdapter del panel de dibujo que recibe como parámetro
	 * @param c panel de dibujo de automatas
	 */
	public CanvasMouseAdapter(AutomataCanvas c){
		super();
		this.canvas=c;
		cordX=Integer.MIN_VALUE;
		cordX=Integer.MIN_VALUE;
		mensajero=Mensajero.getInstancia();
		esFinal=false;
		esInicial=false;
		tieneEstadoInicial=false;
	}
	
	/**
	 * Método accesor que indica si del estado selecionado es final
	 * @return true si es final, false si no
	 */
	public boolean isEsFinal() {
		return esFinal;
	}

	/**
	 * Método modificador de la porpiedad es final del estado selecionado 
	 * @param esFinal indica que a aprtir de ahora el estado es final o no
	 */
	public void setEsFinal(boolean esFinal) {
		this.esFinal = esFinal;
	}

	/**
	 * Método accesor que indica si del estado selecionado es inicial
	 * @return true si es inicial, false si no
	 */
	public boolean isEsInicial() {
		return esInicial;
	}

	/**
	 * Método modificador de la porpiedad es inicial del estado selecionado 
	 * @param esInicial indica que a aprtir de ahora el estado es inicial o no
	 */
	public void setEsInicial(boolean esInicial) {
		this.esInicial = esInicial;
	}

	/**
	 * Método que indica si el automata tiene estado inicial
	 * @return true si tiene estado inicial, false si no
	 */
	public boolean isTieneEstadoInicial() {
		return tieneEstadoInicial;
	}

	/**
	 * Método que establece la propiedad del automata de tener estado inicial
	 * o no
	 * @param tieneEstadoInicial indica si a aprtir de ahora es automata tiene 
	 * estado inicial o no
	 */
	public void setTieneEstadoInicial(boolean tieneEstadoInicial) {
		this.tieneEstadoInicial = tieneEstadoInicial;
	}

	/**
	 * Devuelve la coordenada x donde se ha pinchado
	 * @return coordenada x
	 */
	public int getCordX() {
		return cordX;
	}

	/**
	 * Devuelve la coordenada y donde se ha pinchado
	 * @return coordenada y
	 */
	public int getCordY() {
		return cordY;
	}

	/**
	 * Devuelve la ventana de diálogo del panel
	 * @return ventana de diálogo
	 */
	public JDialog getDialog() {
		return dialog;
	}

	/**
	 * Estableca una nueva ventna de diálogo
	 * @param dialog nueva entan de diálogo
	 */
	public void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * Devuevle el panel de dibujos
	 * @return panel de dibujos de automatas
	 */
	public AutomataCanvas getCanvas() {
		return canvas;
	}

	/**
	 * Establece el panel de dibujos nuevo
	 * @param canvas nuevo panel para dibujar automatas
	 */
	public void setCanvas(AutomataCanvas canvas) {
		this.canvas = canvas;
	}

	/**
	 * Método que detectya que se ha pulsado y soltado una tecla del ratón, si
	 * es la izquierda dependiando del boton de la barra de herramientas que esté
	 * seleccionado se añade un estado o una arista
	 * @param e evento de pulsación de tecla de ratón
	 */
	public void mousePressed(MouseEvent e){
		//buscar estado cercano y lanzarlo desde su corteza(en caso de que sea arista)
		cordX = e.getX();
		cordY = e.getY();
		if (e.getButton()==MouseEvent.BUTTON1 ){  
			switch (canvas.getEstadoB()){
				case estado:{
					dialog=createDialogEstado();
					dialog.setSize(new Dimension(300,150));
					dialog.setVisible(true);
					canvas.repaint();
					break;
				}
				case borrar:{
					Estado est=canvas.estadoEn(e.getPoint());
					if(est!=null){
						modificarRelacinesEstado(est);
						if(canvas.getListaFinales().contains(est.getEtiqueta()))
							canvas.getListaFinales().remove(est.getEtiqueta());
						if(canvas.getEstadoInicial().equals(est.getEtiqueta()))
							canvas.setEstadoInicial("");
						canvas.getListaEstados().remove(est);
						canvas.repaint();
					}
					AristaGeneral arist=canvas.aristaEn(e.getPoint());
					if(arist!=null){
						if (arist instanceof AristaAP){ 
							//System.out.println("YEAHYEAHYEAH");
							/***********************************************/
							//REVISAR DETERMINISMO
							/***********************************************/
							canvas.getListaAristasPila().remove((AristaAP)arist);
							modificarRelacionesArista((AristaAP)arist); 
						}
						if (arist instanceof Arista){
							canvas.getListaAristas().remove((Arista)arist);
							modificarRelacionesArista((Arista)arist);
						}
						if (arist instanceof AristaTuring){
							canvas.getListaAristasTuring().remove((AristaTuring)arist);
							modificarRelacionesArista((AristaTuring)arist);
						}
						canvas.repaint();
							
					}
					break;
				}
			}
		 }
	}

	private void modificarRelacionesArista(AristaTuring arist) {
		// TODO Auto-generated method stub
		canvas.setAlfabeto(canvas.minimizarAlfabeto());
		canvas.setAlfabetoPila(canvas.minimizarAlfabetoPila());
	}
	private void modificarRelacionesArista(AristaAP arist) {
		// TODO Auto-generated method stub
		canvas.setAlfabeto(canvas.minimizarAlfabeto());
		canvas.setAlfabetoPila(canvas.minimizarAlfabetoPila());
	}
	
	private void modificarRelacionesArista(Arista arist) {
		// TODO Auto-generated method stub
		canvas.setAlfabeto(canvas.minimizarAlfabeto());
	}


	private void modificarRelacinesEstado(Estado est) {
		// TODO Auto-generated method stub
		
		ArrayList<Arista> lArist = null;
		ArrayList<AristaAP> lAristAP = null;
		Iterator<Arista> iArist = null; 
		Iterator<AristaAP> iAristAP = null; 
		
		
		if (!canvas.getListaAristas().isEmpty()){
			lArist=new ArrayList<Arista>();
			iArist=canvas.getListaAristas().iterator(); 
		}
		
		if (!canvas.getListaAristasPila().isEmpty()){
			lAristAP=new ArrayList<AristaAP>();
			iAristAP=canvas.getListaAristasPila().iterator(); 			
		}
    
		if(iArist != null || iAristAP != null)	{
			String etiqueta = est.getEtiqueta();
			if (iArist != null){
				while(iArist.hasNext()){
			//Arista a = null; AristaAP ap = null;
					Arista obj = iArist.next(); 
					if(!(obj.getDestino().equals(etiqueta))&&
						!(obj.getOrigen().equals(etiqueta))){
								lArist.add(obj);
					}				
				}
			}
			if (iAristAP != null){
				while(iAristAP.hasNext()){
					AristaAP obj = iAristAP.next(); 					
						if(!(obj.getDestino().equals(etiqueta))&&
							!(obj.getOrigen().equals(etiqueta))){
								lAristAP.add(obj);
						}					
				}
			}	

			canvas.setAlfabeto(canvas.minimizarAlfabeto());
			if(!canvas.getListaAristasPila().isEmpty()){
				canvas.setListaAristasAP(lAristAP);
				canvas.setAlfabetoPila(canvas.minimizarAlfabetoPila());
			}
			else{canvas.setListaAristas(lArist);}
		}
	}
	
	private JDialog createDialogEstado(){
		Mensajero m=Mensajero.getInstancia();
		JOptionPane pane=new JOptionPane();
		JDialog d=pane.createDialog(null,m.devuelveMensaje("vista.est",2));
		JPanel panelD=new JPanel(new GridLayout(4,1));
		JPanel panelR=new JPanel();
		JRadioButton fin=new JRadioButton(m.devuelveMensaje("vista.final",2));
		fin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JRadioButton b=(JRadioButton)e.getSource();
				if(b.isSelected())esFinal=true;
				else esFinal=false;
			}
		});
		JRadioButton ini=new JRadioButton(m.devuelveMensaje("vista.inicial",2));
		ini.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JRadioButton b=(JRadioButton)e.getSource();
				if(b.isSelected())esInicial=true;
				else esInicial=false;
			}
		});
		panelR.add(fin);
		panelR.add(ini);
		JLabel etiqN=new JLabel(m.devuelveMensaje("vista.nestado",2));
		nomEst=new JTextField();
		nomEst.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					try{
						nombreEstado=nomEst.getText();
						if(yaEstaEstado(nombreEstado)){
							JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.estadorepetidoM",2),mensajero.devuelveMensaje("canvas.estadorepetidoT",2),JOptionPane.ERROR_MESSAGE);
						}else{
							if(esVacio(nombreEstado)){
								JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.estadovacioM",2),mensajero.devuelveMensaje("canvas.estadovacioT",2),JOptionPane.ERROR_MESSAGE);
							}else{
								if(!incorrecto(nombreEstado)){
									if(esFinal){
										canvas.getListaFinales().add(nombreEstado);
										if(!esInicial){
											dialog.setVisible(false);
											canvas.getListaEstados().add(new Estado(cordX,cordY,nombreEstado));
										}
									}
									if(esInicial){
										canvas.setEstadoInicial(nombreEstado);
										if(!tieneEstadoInicial)tieneEstadoInicial=true;
										canvas.getListaEstados().add(new Estado(cordX,cordY,nombreEstado));
										dialog.setVisible(false);
									}
									if(!esFinal&&!esInicial){
										canvas.getListaEstados().add(new Estado(cordX,cordY,nombreEstado));
										dialog.setVisible(false);
									}
									esInicial=false;
									esFinal=false;
								} else {
									JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.nombreIncorrectoM",2),mensajero.devuelveMensaje("canvas.nombreIncorrectoT",2),JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}catch(NullPointerException ex){
						JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.estadorepetidoM",2),mensajero.devuelveMensaje("canvas.estadorepetidoT",2),JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		JPanel panelB=  new JPanel();
		JButton aceptar=new JButton(m.devuelveMensaje("vista.aceptar",2));
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					nombreEstado=nomEst.getText();
					if(yaEstaEstado(nombreEstado)){
						JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.estadorepetidoM",2),mensajero.devuelveMensaje("canvas.estadorepetidoT",2),JOptionPane.ERROR_MESSAGE);
						}else{
						if(esVacio(nombreEstado)){
							JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.estadovacioM",2),mensajero.devuelveMensaje("canvas.estadovacioT",2),JOptionPane.ERROR_MESSAGE);
						}else{
							if(!incorrecto(nombreEstado)){
								if(esFinal){
									canvas.getListaFinales().add(nombreEstado);
									if(!esInicial){
										dialog.setVisible(false);
										canvas.getListaEstados().add(new Estado(cordX,cordY,nombreEstado));
									}
								}
								if(esInicial){
									canvas.setEstadoInicial(nombreEstado);
									if(!tieneEstadoInicial)tieneEstadoInicial=true;
									canvas.getListaEstados().add(new Estado(cordX,cordY,nombreEstado));
									dialog.setVisible(false);
								}
								if(!esFinal&&!esInicial){
									canvas.getListaEstados().add(new Estado(cordX,cordY,nombreEstado));
									dialog.setVisible(false);
								}
								esInicial=false;
								esFinal=false;
							} else {
								JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.nombreIncorrectoM",2),mensajero.devuelveMensaje("canvas.nombreIncorrectoT",2),JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}catch(NullPointerException ex){
					JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.estadovacioM",2),mensajero.devuelveMensaje("canvas.estadovacioT",2),JOptionPane.ERROR_MESSAGE);
				}
			}
		});
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
		d.setContentPane(panelD);
		d.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		return d;
	}
		
	/**
	 * Método que detecta si el estado cuyo nombre que recibe como parámetro
	 * está ya añadido en el panel de dibujos
	 * @param etiqueta nombre del estado abuscar
	 * @return true si ya hay un estado con ese nombre, false si no
	 */
	protected boolean yaEstaEstado(String etiqueta){
		Iterator<Estado> it=canvas.getListaEstados().iterator();
		while(it.hasNext()){
			Estado e=it.next();
			if(e.getEtiqueta().equals(etiqueta)) return true;
		}
		return false;
	}
	
	/**
	 * Detecta si el string que recibe está vacío
	 * @param s string que queremos comprobar que no es vacío
	 * @return true si es vacío, false si no
	 */
	protected boolean esVacio(String s){
		int i=0;
		while(i<s.length()){
			Character c=s.charAt(i);
			if(!c.equals(' ')) return false;
			i++;
		}
		return true;
	}
	
	/**
	 * Método que indica si un nombre de estado, que recibe como parámetero, es 
	 * correcto o no. No lo es si acaba en "_$"
	 * @param nombreEstado nombre del estado que se quiere comprobar
	 * @return true si es correcto, false si no
	 */
	protected boolean incorrecto(String nombreEstado) {
		// TODO Auto-generated method stub
		if(nombreEstado.length()>=2){
			Character c1=nombreEstado.charAt(nombreEstado.length()-1);
			Character c2=nombreEstado.charAt(nombreEstado.length()-2);
			return (c1.equals('$')&&c2.equals('_'));
		} else return false;
	}
	
}

