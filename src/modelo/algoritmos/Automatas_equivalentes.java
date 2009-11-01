/**
 * 
 */
package modelo.algoritmos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import controlador.Controlador;
import controlador.Controlador_imp;
import modelo.Algoritmo;
import modelo.automatas.Alfabeto;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFD;

/**
 * Algoritmo que se encarga de calcular la distinguibilidad entre dos automatas finitos deterministas.
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class Automatas_equivalentes implements Algoritmo{
	
	private Automata automata1;//se comprueba la equivalencia entre automata1 y automata2
	private Automata automata2;//se comprueba la equivalencia entre automata1 y automata2
	private Controlador controlador;
	private boolean resultado;
	private String xml;

	
	/**
	 * Ejecuta la operacion de distinguibilidad mediante la tabla de equivalencia
	 */
	public Automata ejecutar(boolean muestraPasos) {
		// TODO Auto-generated method stub
		//se genera un "automata" no conexo con los dos autómatas.
		System.out.println("AUTO1:"+automata1);
		System.out.println("AUTO2:"+automata2);
		if (!automata1.getAlfabeto().getListaLetras().containsAll(automata2.getAlfabeto().getListaLetras())) {
			resultado=false;
			xml=new String();
			xml+="<exit><steps><step>";
			xml+="<table></table>";
			xml+="</step></steps>";
			if (resultado)
				xml+="<result>yes</result>";
			else 
				xml+="<result>no</result>";
			xml+="</exit>";
			
		}
		else {
		Automata aux=generaAutomataConjunto();
		MinimizacionAFD algMinimizacion=new MinimizacionAFD(aux);
		//
		controlador=Controlador_imp.getInstancia();
		algMinimizacion.registraControlador(controlador);
		//
		algMinimizacion.setSoloEquivalencia(true);
		algMinimizacion.ejecutar(false);//de momento sin pasos
		HashMap<String,HashMap<String,Boolean>> tablaMinimizacion=algMinimizacion.getTablaMinimizacion();
		tablaMinimizacion=quitarSubIndices(tablaMinimizacion);
		//ok!
		if (!automata1.getAlfabeto().equals(automata2.getAlfabeto())) resultado=false;
		else {
			if (existenLasParejas(tablaMinimizacion)) resultado=true;
				else resultado=false;
		}
		xml=new String();
		xml+="<exit><steps><step>";
		xml+=traducirTablaMinimizacion(tablaMinimizacion);
		xml+="</step></steps>";
		if (resultado)
			xml+="<result>yes</result>";
		else 
			xml+="<result>no</result>";
		xml+="</exit>";
		}
		return null;
		
	}
	
	private Automata generaAutomataConjunto() {
		//une los automatas 1 y 2. en un solo "automata", obviando que no es conexo y
		//que en el fondo es la unión de dos tablas de transiciones delta.
		Automata aux=new AutomataFD();
		
		ArrayList<String> estados=automata1.getEstados();
		ArrayList<String> estados2=automata2.getEstados();
		ArrayList<String> estadosNAutomata=new ArrayList<String>();
		estadosNAutomata.addAll(estados);
		Iterator<String> itEst=estados2.iterator();
		while(itEst.hasNext()) {
			String e=itEst.next()+"_$";
			estadosNAutomata.add(e);
		}
		aux.setEstados(estadosNAutomata);
		
		ArrayList<String> eFin1=automata1.getEstadosFinales();
		ArrayList<String> eFinales=automata2.getEstadosFinales();
		Iterator<String> itF=eFinales.iterator();
		while(itF.hasNext()) {
			String eF=itF.next()+"_$";
			eFin1.add(eF);
		}
		aux.setEstadosFinales(eFin1);
		
		//cojo el estado inicial del automata1 como inicial del "automata" auxiliar
		String estadoInicial=automata1.getEstadoInicial();
		aux.setEstadoInicial(estadoInicial);
		
		Alfabeto alfabeto=automata1.getAlfabeto();
		Iterator<String> it1=automata2.getAlfabeto().dameListaLetras().iterator();
		while(it1.hasNext()) alfabeto.aniadirLetra(it1.next());
		
		//ahora hay que cargar las aristas correspondientes.
		Iterator<String> it2=automata1.getEstados().iterator();
		while(it2.hasNext()) {
			String estado=it2.next();
			ArrayList<String> lav=automata1.getAristasVertice(estado);
			Iterator<String> it3=lav.iterator();
			while(it3.hasNext()) {
				String letra=it3.next();
				aux.insertaArista(estado, automata1.funcion_delta(estado, letra), letra);
			}
		}
		Iterator<String> it4=automata2.getEstados().iterator();
		while(it4.hasNext()) {
			String estado=it4.next();
			ArrayList<String> lav=automata2.getAristasVertice(estado);
			Iterator<String> it5=lav.iterator();
			while(it5.hasNext()) {
				String letra=it5.next();
				aux.insertaArista(estado+"_$", automata2.funcion_delta(estado, letra)+"_$", letra);
			}
		}
		return aux;
		//ok
	}
	
	/**
	 * Método que inicializa el primero de los dos automatas de los que
	 * se quiere hacer la equivalencia
	 * @param a automata nuevo que será el primero para ejecutar el algoritmo
	 */
	public void registraAutomata1(Automata a) {
		automata1=a;
	}
	
	/**
	 * Método que inicializa el segundo de los dos automatas de los que
	 * se quiere hacer la equivalencia
	 * @param a automata nuevo que será el segundo para ejecutar el algoritmo
	 */
	public void registraAutomata2(Automata a) {
		automata2=a;
	}
	
	private boolean existenLasParejas(HashMap<String,HashMap<String,Boolean>> tabla) {
		//se comprueba si existen las parejas necesarias para que ambos automatas sean 
		//equivalentes , se crean parejas de estados con los q vienen de los automatas 1 y 2.
		//hay que usar los objetos automat1 y automata2
		//IDEA: Todos los estados de cada automata deben quedar emparejados 
		//con al menos 1 estado del otro automata.
		//Usar un arraylist por automata y si ambos son iguales al final
		//que las listas de estados de cada automata entonces se devuelve cierto, caso contrario falso
		Iterator<String> it=tabla.keySet().iterator();
		ArrayList<String> estados1=new ArrayList<String>();
		ArrayList<String> estados2=new ArrayList<String>();
		while(it.hasNext()){
			String estado=it.next();
			HashMap<String,Boolean> fc=tabla.get(estado);
			Iterator<String> itCl=fc.keySet().iterator();
			while(itCl.hasNext()) {
				String est2=itCl.next();
				if (fc.get(est2)) {
					String s1=estado;
					String s2=est2;
					ArrayList<String> estadosRenombrados=renombrarTodos(automata2.getEstados());
					if ((automata1.getEstados().contains(s1))&&(estadosRenombrados.contains(s2))) {
							estados1.add(s1);
							estados2.add(s2);
					}
					if ((estadosRenombrados.contains(s1))&&(automata1.getEstados().contains(s2))) {
							estados2.add(s1);
							estados1.add(s2);
					}
				}
			}
		}
		estados2=desrenombrar(estados2);
		if ((estados1.containsAll(automata1.getEstados()))&&(estados2.containsAll(automata2.getEstados()))){
			return true;
		}
		return false;
	}

	
	public void registraControlador(Controlador controlador) {
		// TODO Auto-generated method stub
		this.controlador=controlador;
		
	}
	
	/**
	 * Método accesor del resultado del algoritmo
	 * @return true si son equivalentes y false en otro caso
	 */
	public boolean getResultado(){
		return resultado;
	}
		
	
	public String getXML() {
		// TODO Auto-generated method stub
		return xml;
	}
	
	private String traducirTablaMinimizacion(HashMap<String,HashMap<String,Boolean>> tabla) {
		String salida="<table>";
		Iterator<String> itc=tabla.keySet().iterator();
		while(itc.hasNext()) {
			String clave=itc.next();
			salida+="<column><key>"+clave+"</key>";
			HashMap<String,Boolean> hs=tabla.get(clave);
			Iterator<String> itf=tabla.keySet().iterator();
			while(itf.hasNext()) {
				String fila=itf.next();
				salida+="<fila><state>"+fila+"</state>"+"<compatible>"+hs.get(fila)+"</compatible></fila>";
			}
			salida+="</column>";
		}
		return salida+"</table>";
	}
	
	private HashMap<String,HashMap<String,Boolean>> quitarSubIndices(HashMap<String,HashMap<String,Boolean>> tm) {
		HashMap<String,HashMap<String,Boolean>> nuevaTabla=new HashMap<String,HashMap<String,Boolean>>();
		Iterator<String> itKey=tm.keySet().iterator();
		while(itKey.hasNext()) {
			String estado=itKey.next();
			String nestado=estado.substring(0, estado.length()-1);
			ArrayList<String> estadosRenombrados=renombrarTodos(automata2.getEstados());
			if (!automata1.getEstados().contains(nestado)&&(!estadosRenombrados.contains(nestado))){
				nestado=nestado.substring(0,nestado.length()-1);
			}
			HashMap<String,Boolean> hs=tm.get(estado);
			HashMap<String,Boolean> nhs=new HashMap<String,Boolean>();
			Iterator<String> itKey2=hs.keySet().iterator();
			while(itKey2.hasNext()) {
				String clave=itKey2.next();
				String nclave=clave.substring(0, clave.length()-1);
				nhs.put(nclave,hs.get(clave));
			}
			nuevaTabla.put(nestado, nhs);
		}
		return nuevaTabla;
		
	}
	
	private ArrayList<String> renombrarTodos(ArrayList<String> lista) {
		
		ArrayList<String> listaNueva=new ArrayList<String>();
		Iterator<String> it=lista.iterator();
		while(it.hasNext()) {
			String estado=it.next();
			listaNueva.add(estado+"_$");
		}
		return listaNueva;
	}
	
	private ArrayList<String> desrenombrar(ArrayList<String> lista) {
		ArrayList<String> listaNueva=new ArrayList<String>();
		Iterator<String> it=lista.iterator();
		while(it.hasNext()) {
			String estado=it.next();
			String s=estado.replace("_$","");
			listaNueva.add(s);
		}
		return listaNueva;
	}

}
