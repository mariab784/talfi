package vista.vistaGrafica.events;

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import vista.vistaGrafica.AutomataCanvas;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class BarraHerramientas extends JToolBar implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AutomataCanvas canvas;
	private ButtonGroup grupo;
	private JToggleButton seleccionado;
	
	/**
	 * Creación de la barra de herramientas que contiene los botones de dibujo
	 * de automatas, recibe el panel de dibujo
	 * @param canvas panel de dibujo de automatas
	 */
	public BarraHerramientas(AutomataCanvas canvas){
		super(javax.swing.SwingConstants.HORIZONTAL);
		this.canvas=canvas;
		grupo= new ButtonGroup();
		seleccionado=null;
	}
	
	/**
	 * Añade un boton en la barra de herramientas
	 * @param boton nuevo boton a añadir
	 */
	public void aniadirBotton(JToggleButton boton){
		this.grupo.add(boton);
		this.add(boton);
		boton.addActionListener(this);
	}
	
	
	
	/**
	 * Oyente para la pulsación de un boton
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		seleccionado=(JToggleButton)e.getSource();
		canvas.botonPulsado(seleccionado);
	}

}
