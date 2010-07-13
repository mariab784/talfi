package vista.vistaGrafica.events;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import vista.vistaGrafica.AristaGeneral;
import vista.vistaGrafica.AutomataCanvas;
import vista.vistaGrafica.Estado;

/**
 * Clase que implementa los m�todoa para mover estado y aristas del automata
 * sobre el panel de dibujo
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class OyenteEditar extends MouseAdapter {

	private final int editar=1;
	
	private AutomataCanvas canvas;
	private Estado estado;
	private Point punto;
	private AristaGeneral arista; //cambiado antes Arista
	private int count;
	
	/**
	 * Creaci�n del oyente que recibe el panel de dibujos
	 * @param c panel de dibujos de automatas
	 */
	public OyenteEditar(AutomataCanvas c){
		super();
		canvas=c;
	}
	
	/**
	 * Evento de pulsaci�n de tecla de rat�n, si es la tecla izquierda
	 * y est� sobre un estado o una arista esta se podr� mover en el dibujo
	 * siempre y cuando se haya seleccionado el boton de editar
	 * @param e evento de pulsaci�n de tecla de rat�n
	 */
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(canvas.getEstadoB()!=editar) return;
		if(e.getButton()==MouseEvent.BUTTON1){
			estado=canvas.estadoEn(e.getPoint());
			if(estado!=null){
				if(!estado.isSelected()){
					canvas.deseleccionaEstados();
					estado.setSelected(true);
					count=1;
					punto=new Point(estado.getX(),estado.getY());
					canvas.repaint();
				} else{
					punto=e.getPoint();
				}
			} else {
				arista=canvas.aristaEn(e.getPoint());

					if(arista!=null){
						punto=e.getPoint();
					} else {
						punto=e.getPoint();
						canvas.deseleccionaEstados();
						count=0;
					}
			}
		}
	}
	
	/**
	 * M�todo que detecta el evento de soltar la tecla del rat�n, si ten�a alg�n
	 * estado o arista seleccionado con el rat�n se deselecciona y ya no se puede 
	 * mover hasta pulsarlo de nuevo, se pinta la arista/estado en el punt�
	 * �ltimo en el que se dej�
	 * @param e evendo de dejar de pulsar tecla de rat�n
	 */
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(canvas.getEstadoB()!=editar) return;
		if(estado!=null)
			if(estado.isSelected()&&count==1){
				estado.setSelected(false);
				count=0;
			}
		estado=null;
		arista=null;
		canvas.repaint();
	}

	/**
	 * M�todo que detecta cuando se est� moviendo el rat�n sobre el panel de dibujo con
	 * en bot�n pulsado, si tiene un estadoo varios o una arista seleccionados se van 
	 * moviendo y pintando seg�n se mueve, sino no hace nada
	 * @param e evento de movimiento minetras pulsa tecla
	 */
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if(canvas.getEstadoB()!=editar) return;
		if(estado!=null){
			int finalE=canvas.getListaEstados().size();
			for(int i=0;i<finalE;i++){
				Estado es=canvas.getListaEstados().get(i);
				Point p=new Point(es.getX()+e.getX()-punto.x,es.getY()+e.getY()-punto.y);
				if(es.isSelected()){
					es.setPoint(p);
					modificaAristasEstado(es,p);
				}
			}
			punto=e.getPoint();
			canvas.repaint();
		} else {
			if(arista!=null){
				Estado origen=canvas.buscaEstado(arista.getOrigen());
				Estado destino=canvas.buscaEstado(arista.getDestino());
				origen.setPoint(new Point(origen.getX()+(e.getPoint().x-punto.x), origen.getY()+(e.getPoint().y-punto.y)));
				modificaAristasEstado(origen,new Point(origen.getX(),origen.getY()));
				if(!origen.equals(destino)){
					destino.setPoint(new Point(destino.getX()+(e.getPoint().x-punto.x), destino.getY()+(e.getPoint().y-punto.y)));
					modificaAristasEstado(destino,new Point(destino.getX(),destino.getY()));
				}
				punto=e.getPoint();
				canvas.repaint();
			} else{
				Rectangle bounds;
				int nowX = e.getPoint().x;
				int nowY = e.getPoint().y;
				int leftX = punto.x;
				int topY = punto.y;
				if(nowX < punto.x) leftX = nowX;
				if(nowY < punto.y) topY = nowY;
				bounds = new Rectangle(leftX, topY, Math.abs(nowX-punto.x), Math.abs(nowY-punto.y));
				count=canvas.seleccionaEstadosEnElrectangulo(bounds);
				canvas.repaint();
				canvas.drawBounds(bounds);
			}
		}
	}
	

	private void modificaAristasEstado(Estado origen2,Point newPoint) {
		// TODO Auto-generated method stub
		
		int tam = 0; int tipo = 0;
		if(!canvas.getListaAristas().isEmpty()) tam = canvas.getListaAristas().size();
		if (!canvas.getListaAristasPila().isEmpty()){ tam = canvas.getListaAristasPila().size(); tipo = 1;}
		
		if (!canvas.getListaAristasTuring().isEmpty()){ tam = canvas.getListaAristasTuring().size(); tipo = 2;}
		for(int i=0;i</*canvas.getListaAristas().size()*/tam;i++){
			AristaGeneral a = null;
			if (tipo == 0) a=canvas.getListaAristas().get(i);
			else if (tipo == 1) a=canvas.getListaAristasPila().get(i);
			else if (tipo == 2) a=canvas.getListaAristasTuring().get(i);
			if(a.getOrigen().equals(origen2.getEtiqueta())){
				a.setX(newPoint.x);
				a.setY(newPoint.y);
			} 
			if(a.getDestino().equals(origen2.getEtiqueta())){
				a.setFx(newPoint.x);
				a.setFy(newPoint.y);
			}
		}
	}
}
