package vista.vistaGrafica.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

import vista.vistaGrafica.AristaTuring;
import vista.vistaGrafica.Estado;
import modelo.automatas.AlfabetoPila_imp;
import modelo.automatas.Alfabeto_imp;
import accesoBD.Mensajero;

/**
 * Clase que  modifica la información de la arista que el usuario haya
 * decidido
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */

public class OyenteModificaAristaTuringActionListener implements ActionListener{

	private OyenteArista mouse;
	private AristaTuring arista;
	private Mensajero mensajero;
	
	/**
	 * Constructor de la clase modificadora de la infromación de la arista
	 * @param a arista a midificar
	 * @param m oyente que muestra de la información de la arista de donde se consiguen
	 * los nuevos datos introducidos por el usuario
	 */
	public OyenteModificaAristaTuringActionListener(AristaTuring a,OyenteArista m){
		this.arista=a;
		this.mouse=m;
		this.mensajero=Mensajero.getInstancia();
	}
	
	/**
	 * Método que obtiene los datos nuevos de la arista e inicia la actualización
	 * del panel de dibujos con ellos
	 * @param e evento de pulsación del botón
	 */
	public void actionPerformed(ActionEvent e){
		this.mensajero=Mensajero.getInstancia();
		

			
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
					mouse.getCanvas().getListaAristasTuring().remove(arista); //int x,int y,int fx, int fy,String origen,String destino)

					//System.out.println("LISTARISTAS: " + mouse.getCanvas().getListaAristas());
					AristaTuring arist = new AristaTuring(o.getX(),o.getY(),d.getX(),d.getY(), o.getEtiqueta(),d.getEtiqueta());
					arist.setSimbolos(ass);
					ss = mouse.getNomCima().getText();
					arist.setSimboloCinta(ss);
					if(mouse.getCanvas().getAlfabetoPila()==null) mouse.getCanvas().setAlfabetoPila(new AlfabetoPila_imp());
					if(!mouse.getCanvas().getAlfabetoPila().estaLetra(ss)){
						mouse.getCanvas().getAlfabetoPila().aniadirLetra(ss);
					}
					ass = new ArrayList<String>();
			//		String transicion =  mouse.getNomTrans().getText();

					ss = mouse.getNomTrans().getText();
					arist.setDireccion(ss);
					mouse.getCanvas().getListaAristasTuring().add(arist);
				
				mouse.getCanvas().setAlfabeto(mouse.getMouse().getCanvas().minimizarAlfabeto());
	//			mouse.getCanvas().setAlfabetoPila(mouse.getMouse().getCanvas().minimizarAlfabetoPila());

				mouse.getDialog().setVisible(false);

			} catch(NullPointerException ex){
				JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
			}
			
			
			
		}
}

