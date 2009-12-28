package vista.vistaGrafica.events;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import vista.vistaGrafica.Estado;

import accesoBD.Mensajero;

/**
 * Clase que  modifica la información del estado que el usuario haya
 * decidido, se lanza por pulsación tecla sobre un campo de texto
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class OyenteModificaEstadoKeyAdapter extends KeyAdapter {

	private Estado estado;
	private OyenteArista mouse;
	private Mensajero mensajero;
	
	/**
	 * Constructor de la clase modificadora de la infromación del estado
	 * @param e estado a midificar
	 * @param m oyente que muestra de la información del estado de donde se consiguen
	 * los nuevos datos introducidos por el usuario
	 */
	public OyenteModificaEstadoKeyAdapter(Estado e,OyenteArista m){
		this.estado=e;
		this.mouse=m;
		this.mensajero=Mensajero.getInstancia();
	}
	
	/**
	 * Método que obtiene los datos nuevos del estado e inicia la actualización
	 * del panel de dibujos con ellos, sólo si se ha pulsado al tecla intro
	 * @param e evento de pulsación de tecla
	 */
	public void keyPressed(KeyEvent e){
		this.mensajero=Mensajero.getInstancia();
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			try{
				mouse.setNombreEstado(mouse.getNomEst().getText());
				if(mouse.getMouse().esVacio(mouse.getNombreEstado())){
					JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.estadovacioM",2),mensajero.devuelveMensaje("canvas.estadovacioT",2),JOptionPane.ERROR_MESSAGE);
				}else{
					if(!mouse.getMouse().incorrecto(mouse.getNombreEstado())){
						if(mouse.getMouse().isEsInicial()){
							if(mouse.getCanvas().getEstadoInicial()==estado.getEtiqueta()){
								mouse.getCanvas().setEstadoInicial("");
							}
							mouse.getCanvas().setEstadoInicial(mouse.getNombreEstado());
							if(!mouse.getMouse().isTieneEstadoInicial())mouse.getMouse().setTieneEstadoInicial(true);
							if(mouse.getCanvas().getListaFinales().contains(estado.getEtiqueta())&&!mouse.getMouse().isEsFinal()
									 && !(estado.getEtiqueta().equals(mouse.getNombreEstado()))){
								mouse.getCanvas().getListaFinales().remove(estado.getEtiqueta());
							}
							mouse.getCanvas().getListaEstados().remove(estado);
							mouse.getCanvas().getListaEstados().add(new Estado(estado.getX(),estado.getY(),mouse.getNombreEstado()));
							mouse.getDialog().setVisible(false);
						}
						if(mouse.getMouse().isEsFinal()){
							mouse.getCanvas().getListaFinales().add(mouse.getNombreEstado());
							if(!mouse.getMouse().isEsInicial()){
								if(mouse.getCanvas().getEstadoInicial().equals(estado.getEtiqueta())&&!mouse.getMouse().isEsInicial()
										 && !(estado.getEtiqueta().equals(mouse.getNombreEstado()))){
									mouse.getCanvas().setEstadoInicial("");
								}
								mouse.getDialog().setVisible(false);
								if(mouse.getCanvas().getListaFinales().contains(estado.getEtiqueta())&& (estado.getEtiqueta()!=mouse.getNombreEstado())){
									mouse.getCanvas().getListaFinales().remove(estado.getEtiqueta());
								}
								mouse.getCanvas().getListaEstados().remove(estado);
								mouse.getCanvas().getListaEstados().add(new Estado(estado.getX(),estado.getY(),mouse.getNombreEstado()));
							}
						}
						if(!mouse.getMouse().isEsFinal()&&!mouse.getMouse().isEsInicial()){
							if(mouse.getCanvas().getEstadoInicial()==estado.getEtiqueta()){
								mouse.getCanvas().setEstadoInicial(null);
							}
							if(mouse.getCanvas().getListaFinales().contains(estado.getEtiqueta())){
								mouse.getCanvas().getListaFinales().remove(estado.getEtiqueta());
							}
							mouse.getCanvas().getListaEstados().remove(estado);
							mouse.getCanvas().getListaEstados().add(new Estado(estado.getX(),estado.getY(),mouse.getNombreEstado()));
							mouse.getDialog().setVisible(false);
						}
						mouse.getMouse().setEsInicial(false);
						mouse.getMouse().setEsFinal(false);
					} else {
						JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.nombreIncorrectoM",2),mensajero.devuelveMensaje("canvas.nombreIncorrectoT",2),JOptionPane.ERROR_MESSAGE);
					}
				}
			}catch(NullPointerException ex){
				JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.estadorepetidoM",2),mensajero.devuelveMensaje("canvas.estadorepetidoT",2),JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
