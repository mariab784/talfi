package vista.vistaGrafica.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

import vista.vistaGrafica.Arista;
import vista.vistaGrafica.AristaAP;
import vista.vistaGrafica.Estado;
import modelo.automatas.Alfabeto_imp;
import accesoBD.Mensajero;

/**
 * Clase que  modifica la informaci�n de la arista que el usuario haya
 * decidido
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class OyenteModificaAristaAPActionListener implements ActionListener{

	private OyenteArista mouse;
	private AristaAP arista;
	private Mensajero mensajero;
	
	/**
	 * Constructor de la clase modificadora de la infromaci�n de la arista
	 * @param a arista a midificar
	 * @param m oyente que muestra de la informaci�n de la arista de donde se consiguen
	 * los nuevos datos introducidos por el usuario
	 */
	public OyenteModificaAristaAPActionListener(AristaAP a,OyenteArista m){
		this.arista=a;
		this.mouse=m;
		this.mensajero=Mensajero.getInstancia();
	}
	
	/**
	 * M�todo que obtiene los datos nuevos de la arista e inicia la actualizaci�n
	 * del panel de dibujos con ellos
	 * @param e evento de pulsaci�n del bot�n
	 */
	public void actionPerformed(ActionEvent e){
		this.mensajero=Mensajero.getInstancia();
		try{
			//mouse.setNombreArista(mouse.getNomArs().getText());
			StringTokenizer st=new StringTokenizer(mouse.getNomArs().getText(),",");
			ArrayList<String> ass = new ArrayList<String>();
			while(st.hasMoreTokens()){
				String ss=st.nextToken();
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
				arist.setCimaPila(mouse.getNomCima().getText());
				ass = new ArrayList<String>();
				String transicion =  mouse.getNomTrans().getText();
				int i = 0;
				while(i < transicion.length()){
					String ss= "" + transicion.charAt(i);
					ass.add(ss);
					if(mouse.getCanvas().getAlfabeto()==null) mouse.getCanvas().setAlfabeto(new Alfabeto_imp());
					if(!mouse.getCanvas().getAlfabeto().estaLetra(ss)){
						mouse.getCanvas().getAlfabeto().aniadirLetra(ss);
					}
				
				i++;
				}
				arist.setSalida(ass);
				mouse.getCanvas().getListaAristasPila().add(arist);
			
			mouse.getCanvas().setAlfabeto(mouse.getMouse().getCanvas().minimizarAlfabeto());
			mouse.getCanvas().setAlfabetoPila(mouse.getMouse().getCanvas().minimizarAlfabetoPila());
				
				
				System.out.println("LALLALAALLALALALALAAAAAAAAAA");
				
			mouse.getDialog().setVisible(false);

		} catch(NullPointerException ex){
			JOptionPane.showMessageDialog(null,mensajero.devuelveMensaje("canvas.aristavaciaM",2),mensajero.devuelveMensaje("canvas.aristavaciaT",2),JOptionPane.ERROR_MESSAGE);
		}
	}
}
