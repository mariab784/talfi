/**
 * 
 */
package modelo.automatas;

/**
 * Clase que tiene la funcinalidad de almacenar las coordenadas de los estados
 * según se hayan pintado en la interfaz gráica
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class Coordenadas {
	
	private int x,y;
	
	/**
	 * Constructor que crea unas coordenadas según se le psaen en los parámetros
	 * @param x nueva coordenada horizontal 
	 * @param y nueva coordenada vertical
	 */
	public Coordenadas(int x,int y) {
		this.x=x;
		this.y=y;
	}
	
	/**
	 * Método accesor de la coordenada horizontal
	 * @return la coordenada x, horizontal
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Método modificador de la coordenada horizontal
	 * @param x la nueva coordenada x, horizontal
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Método accesor de la coordenada vertical
	 * @return la coordenada y, vertical
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Método modificador de la coordenada vertical
	 * @param y la nueva coordenada y, vertical
	 */
	public void setY(int y) {
		this.y = y;
	}

	
}
