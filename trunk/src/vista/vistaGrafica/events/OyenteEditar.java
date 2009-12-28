package vista.vistaGrafica.events;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import vista.vistaGrafica.Arista;
import vista.vistaGrafica.AutomataCanvas;
import vista.vistaGrafica.Estado;

/**
 * Clase que implementa los métodoa para mover estado y aristas del automata
 * sobre el panel de dibujo
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class OyenteEditar extends MouseAdapter {

	private final int editar=1;
	
	private AutomataCanvas canvas;
	private Estado estado;
	private Point punto;
	private Arista arista;
	private int count;
	
	/**
	 * Creación del oyente que recibe el panel de dibujos
	 * @param c panel de dibujos de automatas
	 */
	public OyenteEditar(AutomataCanvas c){
		super();
		canvas=c;
	}
	
	/**
	 * Evento de pulsación de tecla de ratón, si es la tecla izquierda
	 * y está sobre un estado o una arista esta se podrá mover en el dibujo
	 * siempre y cuando se haya seleccionado el boton de editar
	 * @param e evento de pulsación de tecla de ratón
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
	 * Método que detecta el evento de soltar la tecla del ratón, si tenía algún
	 * estado o arista seleccionado con el ratón se deselecciona y ya no se puede 
	 * mover hasta pulsarlo de nuevo, se pinta la arista/estado en el puntó
	 * último en el que se dejó
	 * @param e evendo de dejar de pulsar tecla de ratón
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
	 * Método que detecta cuando se está moviendo el ratón sobre el panel de dibujo con
	 * en botón pulsado, si tiene un estadoo varios o una arista seleccionados se van 
	 * moviendo y pintando según se mueve, sino no hace nada
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
		for(int i=0;i<canvas.getListaAristas().size();i++){
			Arista a=canvas.getListaAristas().get(i);
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
