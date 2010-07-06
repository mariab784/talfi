package vista.vistaGrafica.events;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import modelo.AutomatasException;
import accesoBD.Mensajero;

import vista.vistaGrafica.AutomataCanvas;

/**
 * Clase oyentye que recibe los eventos de teclado de la ventana principal de la aplicaciñn.
 * Recibe los eventos control+c, control+v, control+x y supr para copiar, pegar, cortar y
 * eliminar estados selecionados respectivamente.
 * @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class OyenteCopiarPegar implements KeyListener {
	
	private AutomataCanvas canvas;
	private boolean control;
	
	/**
	 * Constructor del oyente que incializa la varibel de deteccciñn de pulsaciñn de la
	 * tecla control a false y establece el canvas de dibujo con e que recibe
	 * @param a
	 */
	public OyenteCopiarPegar(AutomataCanvas a) {
		this.canvas=a;
		control=false;
	}

	
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getKeyCode()==KeyEvent.VK_CONTROL) {
			control=true;
		}
		
		if (arg0.getKeyCode()==KeyEvent.VK_DELETE) {
			canvas.borrarSeleccionados(); 
			canvas.repaint();
		}

	}

	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode()==KeyEvent.VK_C&&control) {
			Mensajero m=Mensajero.getInstancia();
			try {
				CopiarPegar cp=CopiarPegar.getInstancia();
				cp.setAutomata(canvas.traducirXML());
				canvas.getVista().setPegar();
			} catch(AutomatasException ex){
				JOptionPane.showMessageDialog(null,ex.getMensaje()+"\n"+m.devuelveMensaje("vista.ercopy", 2),"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getKeyCode()==KeyEvent.VK_V&&control) {
			Mensajero m=Mensajero.getInstancia();
			try {
				CopiarPegar cp=CopiarPegar.getInstancia();
				if(!cp.sePuedePegar()){System.out.println("canvas.getTipoAutomata()" + canvas.getTipoAutomata()); return;}
				if(!canvas.getVista().dibujar()){System.out.println("canvas.getTipoAutomata()" + canvas.getTipoAutomata()); return;}
				if(canvas.getListaEstados().size()==0){
					System.out.println("canvas.getTipoAutomata()" + canvas.getTipoAutomata());
					canvas.cargarAutomata(cp.getAutomata());
				} else {
					JOptionPane.showMessageDialog(null,m.devuelveMensaje("vista.yaauto", 2),"Error",JOptionPane.ERROR_MESSAGE);
				}
			} catch(AutomatasException ex){
				JOptionPane.showMessageDialog(null,ex.getMensaje(),"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getKeyCode()==KeyEvent.VK_X&&control) {
			Mensajero m=Mensajero.getInstancia();
			try {
				CopiarPegar cp=CopiarPegar.getInstancia();
				cp.setAutomata(canvas.traducirXML());
				canvas.borrarPanel();
				canvas.getVista().setPegar();
			} catch(AutomatasException ex){
				JOptionPane.showMessageDialog(null,ex.getMensaje()+"\n"+m.devuelveMensaje("vista.ercut", 2),"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getKeyCode()==KeyEvent.VK_CONTROL) {
			control=false;
		}
		
	}

	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
		
	}


}
