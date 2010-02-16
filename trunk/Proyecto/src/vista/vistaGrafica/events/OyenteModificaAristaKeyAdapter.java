package vista.vistaGrafica.events;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import vista.vistaGrafica.Arista;
import vista.vistaGrafica.Estado;

import accesoBD.Mensajero;

import modelo.automatas.Alfabeto_imp;

/**
 * Clase que  modifica la informaciñn de la arista que el usuario haya
 * decidido, se lanza por pulsaciñn tecla sobre un campo de texto
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class OyenteModificaAristaKeyAdapter extends KeyAdapter {

	private OyenteArista mouse;
	private Arista arista;
	private Mensajero mensajero;
	
	/**
	 * Constructor de la clase modificadora de la infromaciñn de la arista
	 * @param a arista a midificar
	 * @param m oyente que muestra de la informaciñn de la arista de donde se consiguen
	 * los nuevos datos introducidos por el usuario
	 */
	public OyenteModificaAristaKeyAdapter(Arista a,OyenteArista m){
		this.arista=a;
		this.mouse=m;
		this.mensajero=Mensajero.getInstancia();
	}
	
	/**
	 * Mñtodo que obtiene los datos nuevos de la arista e inicia la actualizaciñn
	 * del panel de dibujos con ellos, sñlo si se ha pulsado al tecla intro
	 * @param e evento de pulsaciñn de tecla
	 */
	public void keyPressed(KeyEvent e){
		this.mensajero=Mensajero.getInstancia();
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			try{
				mouse.setNombreArista(mouse.getNomArs().getText());
				StringTokenizer st=new StringTokenizer(mouse.getNomArs().getText(),",");
				while(st.hasMoreTokens()){
					String ss=st.nextToken();
					if(mouse.getCanvas().getAlfabeto()==null) mouse.getCanvas().setAlfabeto(new Alfabeto_imp());
					if(!mouse.getCanvas().getAlfabeto().estaLetra(ss)){
						mouse.getCanvas().getAlfabeto().aniadirLetra(ss);
					}
					mouse.setEstadoOrigen(mouse.getDesde().getSelectedItem().toString());
					mouse.setEstadoDestino(mouse.getHasta().getSelectedItem().toString());
					Estado o=mouse.getCanvas().buscaEstado(mouse.getEstadoOrigen());
					Estado d=mouse.getCanvas().buscaEstado(mouse.getEstadoDestino());
					mouse.getCanvas().getListaAristas().remove(arista);
					mouse.getCanvas().getListaAristas().add(new Arista(o.getX(),o.getY(),d.getX(),d.getY(),ss,o.getEtiqueta(),d.getEtiqueta()));
				}
				mouse.getCanvas().setAlfabeto(mouse.getMouse().getCanvas().minimizarAlfabeto());
				mouse.getDialog().setVisible(false);
			} catch(NullPointerException ex){
				JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
}
