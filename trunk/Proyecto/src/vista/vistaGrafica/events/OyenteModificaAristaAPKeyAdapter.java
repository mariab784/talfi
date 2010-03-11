package vista.vistaGrafica.events;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import modelo.automatas.AlfabetoPila_imp;
import modelo.automatas.Alfabeto_imp;
//import vista.vistaGrafica.Arista;
import vista.vistaGrafica.AristaAP;
import vista.vistaGrafica.Estado;
import accesoBD.Mensajero;

public class OyenteModificaAristaAPKeyAdapter extends KeyAdapter {

	private OyenteArista mouse;
	private AristaAP arista;
	private Mensajero mensajero;
	
	/**
	 * Constructor de la clase modificadora de la infromaci�n de la arista
	 * @param a arista a midificar
	 * @param m oyente que muestra de la informaci�n de la arista de donde se consiguen
	 * los nuevos datos introducidos por el usuario
	 */
	public OyenteModificaAristaAPKeyAdapter(AristaAP a,OyenteArista m){
		this.arista=a;
		this.mouse=m;
		this.mensajero=Mensajero.getInstancia();
	}
	
	/**
	 * M�todo que obtiene los datos nuevos de la arista e inicia la actualizaci�n
	 * del panel de dibujos con ellos, s�lo si se ha pulsado al tecla intro
	 * @param e evento de pulsaci�n de tecla
	 */
	public void keyPressed(KeyEvent e){
		this.mensajero=Mensajero.getInstancia();
		if(e.getKeyCode()==KeyEvent.VK_ENTER){

			
			this.mensajero=Mensajero.getInstancia();
			try{
				//mouse.setNombreArista(mouse.getNomArs().getText());
				if (mouse.getNomArs().getText().isEmpty() || mouse.getNomCima().getText().isEmpty() || 
						mouse.getNomTrans().getText().isEmpty())
					throw new NullPointerException();
				
				StringTokenizer st=new StringTokenizer(mouse.getNomArs().getText(),",");
				ArrayList<String> ass = new ArrayList<String>();
				String ss = null;
				while(st.hasMoreTokens()){
					ss=st.nextToken();
					ass.add(ss);
					if(mouse.getCanvas().getAlfabeto()==null) mouse.getCanvas().setAlfabeto(new Alfabeto_imp());
					if(!mouse.getCanvas().getAlfabeto().estaLetra(ss)){
						mouse.getCanvas().getAlfabeto().aniadirLetra(ss);
					}
				}	
					mouse.setEstadoOrigen(mouse.getDesde().getSelectedItem().toString());
					mouse.setEstadoDestino(mouse.getHasta().getSelectedItem().toString());
					Estado o=mouse.getCanvas().buscaEstado(mouse.getEstadoOrigen());
					Estado d=mouse.getCanvas().buscaEstado(mouse.getEstadoDestino());
					mouse.getCanvas().getListaAristasPila().remove(arista); //int x,int y,int fx, int fy,String origen,String destino)

					//System.out.println("LISTARISTAS: " + mouse.getCanvas().getListaAristas());
					AristaAP arist = new AristaAP(o.getX(),o.getY(),d.getX(),d.getY(), o.getEtiqueta(),d.getEtiqueta());
					arist.setSimbolos(ass);
					ss = mouse.getNomCima().getText();
					arist.setCimaPila(ss);
					if(mouse.getCanvas().getAlfabetoPila()==null) mouse.getCanvas().setAlfabetoPila(new AlfabetoPila_imp());
					if(!mouse.getCanvas().getAlfabetoPila().estaLetra(ss)){
						mouse.getCanvas().getAlfabetoPila().aniadirLetra(ss);
					}
					ass = new ArrayList<String>();
					String transicion =  mouse.getNomTrans().getText();
					int i = 0;
					while(i < transicion.length()){
						ss= "" + transicion.charAt(i);
						ass.add(ss);
						if(mouse.getCanvas().getAlfabetoPila()==null) mouse.getCanvas().setAlfabetoPila(new AlfabetoPila_imp());
						if(!mouse.getCanvas().getAlfabetoPila().estaLetra(ss)){
							mouse.getCanvas().getAlfabetoPila().aniadirLetra(ss);
						}
					
					i++;
					}
					arist.setSalida(ass);
					mouse.getCanvas().getListaAristasPila().add(arist);
				
				mouse.getCanvas().setAlfabeto(mouse.getMouse().getCanvas().minimizarAlfabeto());
				mouse.getCanvas().setAlfabetoPila(mouse.getMouse().getCanvas().minimizarAlfabetoPila());

				mouse.getDialog().setVisible(false);

			} catch(NullPointerException ex){
				JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
			}
			
			
			
		}
	}
	
}

