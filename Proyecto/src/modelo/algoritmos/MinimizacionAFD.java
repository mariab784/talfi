/**
 * 
 */
package modelo.algoritmos;


import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import accesoBD.Mensajero;
import accesoBD.ParserXML;
import controlador.Controlador;
import controlador.Controlador_imp;
import modelo.Algoritmo;
import modelo.AutomatasException;
import modelo.automatas.*;

/**
 * 
 * ALGORITMO DE MInIMIZACION DE UN AUTOMATA FINITO DETERMINISTA
 * si el automata que se pasa no es determinista se deberñ traducir a un afd
 * 
 * 
 *  @author Luis San Juan, Rocio Barrigñete, Mario Huete
 *
 */
public class MinimizacionAFD implements Algoritmo{
	
	private Automata automataEntrada;
	private Automata automataSalida;
	private HashMap<String,HashMap<String,Registro>> tabla;
	private Integer numPasos;
	private ArrayList<String> nuevosEstados;
	private ArrayList<String> nuevosEstadosFinales;
	private Controlador controlador;
	private Mensajero mensajero;
	private String xml;
	private boolean soloEquivalencia;
	
	
	public boolean isSoloEquivalencia() {
		return soloEquivalencia;
	}

	public void setSoloEquivalencia(boolean soloEquivalencia) {
		this.soloEquivalencia = soloEquivalencia;
	}
    
	/*En esta funciñn voy a añadir un estado trampa explicito al automata que recibamos*/ 
	private Automata trampea (Automata ent){
		Automata nuevo = new AutomataFD();
		try {
			nuevo = (Automata) ent.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!nuevo.getEstados().contains("tramposo")){
			nuevo.insertaEstadoNoFinal("tramposo");
			ArrayList<String> alfabeto = nuevo.getAlfabeto().dameListaLetras(); 
			Iterator<String> itAlf = alfabeto.iterator();
			while (itAlf.hasNext()){
				String letra = itAlf.next();
				ent.insertaArista("tramposo", "tramposo", letra); //trampa va siempre a trampa
				ArrayList<String> listaTrampa=nuevo.getEstados();
				Iterator<String> itTram = listaTrampa.iterator();
				while (itTram.hasNext()){
					String recorro = itTram.next();
					if (nuevo.funcion_delta(recorro, letra) == null){ //si no tiene transicion definida voy a trampa 
						nuevo.insertaArista(recorro, "tramposo", letra);
					}
				}
			}
		}
		System.out.println(nuevo.toString());
			return nuevo;
		
	}
	
	private Automata quitaInaccesibles(Automata ent){
		AutomataFD copia = new AutomataFD();
		try {
			copia = (AutomataFD)ent.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean cambios = true;
		while (cambios){
			ArrayList<String> listaEstados=copia.getEstados();
			Iterator<String> itEstados = listaEstados.iterator();
			ArrayList<String> destinos = new ArrayList<String>();
			while (itEstados.hasNext()){
				String estado = itEstados.next();
				destinos.addAll(ent.getDestinos2(estado));
			}
			ArrayList<String> estados2 = new ArrayList<String>();
			estados2=ent.getEstados();
			Iterator<String> itEstados2 = estados2.iterator();
			cambios = false;
			while (itEstados2.hasNext()){
				String estadito = itEstados2.next();
				if (!destinos.contains(estadito)&& !copia.getEstadoInicial().equals(estadito) && copia.getEstados().contains(estadito)){
					copia.getEstados().remove(estadito);
					copia.eliminaVertice(estadito);
					cambios = true;
				}
			}
		}
		return copia;
		
	}
	/**
	 * Constructor del algoritmo
	 * @param ent automata que se va a minimizar
	 * @throws CloneNotSupportedException 
	 */
	public MinimizacionAFD(Automata ent) throws CloneNotSupportedException {
		mensajero=new Mensajero();
		Automata automata = trampea(ent);
		this.automataEntrada = quitaInaccesibles(automata);
		tabla=new HashMap<String,HashMap<String,Registro>>();
		numPasos=1;
		soloEquivalencia=false;
	}

	/**
	 * Compara dos estados, es obligados que tengan el siguiente formato q1, s1, etc...
	 * @param s1 estado 1
	 * @param s2 estado 2
	 * @return true si es mayor s1, false en caso contrario
	 */	
	public static boolean mayor(String s1,String s2) {
		if ((s1.length()<2)||(s2.length()<2)) return true;
		if (s1.charAt(s1.length()-1)>s2.charAt(s2.length()-1)) return true;
		return false;
	}

	
	/**
	 * Este metodo inicializa la tabla y marca todas las casillas
	 * como distinguibles si corresponden a una entrada de estado final 
	 * y estado no final
	 * 
	 * distinguible=false
	 * nodistinguible=true
	 */
	private void inicializarTabla() {
		
		ArrayList<String> listaVertices=automataEntrada.getEstados();
		ArrayList<String> listaEstFinales=automataEntrada.getEstadosFinales();
		
		Iterator<String> iter=listaVertices.iterator();
		//se inicializa la tabla
		while (iter.hasNext()){
			String estado=iter.next();
			HashMap<String,Registro> fila=new HashMap<String,Registro>();
			Iterator<String> iter2=listaVertices.iterator();
			while(iter2.hasNext()) {
				String estDestino=iter2.next();
				if (mayor(estDestino,estado)) {
					if (listaEstFinales.contains(estado)&&(!(listaEstFinales.contains(estDestino)))) {
						Registro marca = new Registro ("1",false,"Final-NoFinal");
						fila.put(estDestino, marca);
					}
					else {
						if (listaEstFinales.contains(estDestino)&&(!(listaEstFinales.contains(estado)))) {
							Registro marca = new Registro ("1",false,"Final-NoFinal"); // marcamos los pares compuestos por un estado final 
							fila.put(estDestino, marca);  //y otro no final, ya que sabemos que son distinguibles
						}
						else {
							Registro marca = new Registro ("0",true,"no marcado");
							fila.put(estDestino, marca); //El resto permanecen sin marcar.
						}
					}
				}
			}
			tabla.put(estado, fila);		
		}
		//tengo la tabla inicializada
	
	}
	
	/**
	 *Se encarga de ir recorriendo la tabla y comprobar si hay alguna casilla que modificar
	 *Adem�s marca los estados por los que vamos a realizar la minimizaci�n 
	 * 
	*/
	private void paso(){   //El metodo paso muestra la tabla por consola y luego marca
		
		System.out.println(tabla.toString());
		ArrayList<String> listaVertices=automataEntrada.getEstados();
		Iterator<String> it=listaVertices.iterator();
		System.out.println("Lista de vertices: "+listaVertices);
		numPasos = 2;
		while (it.hasNext()){
			String v1=it.next();
			HashMap<String,Registro> fila=tabla.get(v1);
			Iterator<String> itClaves=fila.keySet().iterator();
			System.out.println("vamos a mirar el puto estado " +v1);
            while (itClaves.hasNext()){	
				String v2=itClaves.next();
				System.out.println("Comparandolo con " +v2);
				System.out.println(tabla.get(v1).get(v2).getMarcado());
				System.out.println(tabla.get(v1).get(v2).getPaso());
				System.out.println(tabla.get(v1).get(v2).getEstados());
            }
		}
		
		//A partir de aquñ vamos a marcar los pares no marcados al inicializar
		//HashMap<String,HashMap<String,Registro>> tablaCopia = (HashMap<String,HashMap<String,Registro>>) tabla.clone();
		Iterator<String> pares=listaVertices.iterator();
		System.out.println(pares);
		boolean cambios = false;
		do{//Si no son iguales al final de la iteracion, no he acabado
			//tablaCopia = (HashMap<String,HashMap<String,Registro>>) tabla.clone(); //en caso contrario he minimizado
			pares=listaVertices.iterator();
			cambios = false;
			while (pares.hasNext()){
				System.out.println("paso por esta mierda de sitio");
				String primera = pares.next();
				System.out.println(primera);
				HashMap<String,Registro> fila = tabla.get(primera);
				Iterator<String> segundo = fila.keySet().iterator();
				while (segundo.hasNext()){
					System.out.println("y tambiñn paso por esta mierda de sitio");
					String segunda = segundo.next();
					System.out.println(segunda);
					if (tabla.get(primera).get(segunda).getMarcado()){
						ArrayList<String> aristas=automataEntrada.getAristasVertice(primera);
						System.out.println("aki no tamos marcados");
						ArrayList<String> aristas2 =automataEntrada.getAristasVertice(segunda);
						System.out.println("aquñ vamos a ver que son las putas aristas");
						System.out.println(primera);
						System.out.println(segunda);
						/* tengo que mirar si donde va la primera componente de cada una de las aristas
						 * y despuñs de mirar eso mirar si esos estados estñn marcados, si estñn marcados
						 * marco los nuevos estados, si no estan marcados paso de ellos y lo dejo para el siguiente paso
						 */
						Iterator<String> itArista1=aristas.iterator();
						Iterator<String> itArista2=aristas2.iterator();
						while (itArista1.hasNext()&& itArista2.hasNext()){
							String letra=itArista1.next();
							//String letra2=itArista2.next();
							String v1_letra=automataEntrada.funcion_delta(primera, letra);
							String v2_letra=automataEntrada.funcion_delta(segunda,letra);
							if (v1_letra!=null && v2_letra!=null){
								//if(v1_letra.compareTo(v2_letra)<0){
								if (tabla.get(v1_letra).get(v2_letra)!=null){ 
								//deberia mirar si los estados a los que van estan marcados.
									if (!tabla.get(v1_letra).get(v2_letra).getMarcado()){
										//Registro marca = new Registro(pasos,false,v1_letra+v2_letra);
										tabla.get(primera).get(segunda).setMarcado(false);
										tabla.get(primera).get(segunda).setPaso(numPasos.toString());
										tabla.get(primera).get(segunda).setEstados(v1_letra.substring(0, v1_letra.length()/2+1)+ "-" +v2_letra.substring(0, v2_letra.length()/2+1));
										cambios = true;
									}
								}
								if (tabla.get(v2_letra).get(v1_letra)!=null){
									if (!tabla.get(v2_letra).get(v1_letra).getMarcado()){
										tabla.get(primera).get(segunda).setMarcado(false);
										tabla.get(primera).get(segunda).setPaso(numPasos.toString());
										tabla.get(primera).get(segunda).setEstados(v1_letra.substring(0, v1_letra.length()/2+1)+ "-" + v2_letra.substring(0, v2_letra.length()/2+1));
										cambios = true;
									}
								}
							}
						}
							
					}
			
						
				}
			}
			numPasos++;
		} while(cambios);
 }
		
		
	/**
	 * Una vez terminada la tabla se genera el automata de salida con los estados asociados
	 */
	private void generaAutomata() {
		//se parte de la tabla de minimizacion completa y finalizada
		//buscar casillas con true.
		//para ellas:reunir la fila y la columna en un solo estado
		//generar estados con el mismo nombre para aquellos que no se vean afectados por lo anterior.
		//guardar en automata_salida el automata generado manteniendo las transiciones
		ArrayList<Object> listaEstadosNuevos=new ArrayList<Object>();
		ArrayList<String> listaEstadosAntiguos=automataEntrada.getEstados();
		ArrayList<Pareja> listaEstadosReducibles=new ArrayList<Pareja>();
		Iterator<String> it=tabla.keySet().iterator();
		while(it.hasNext()) {
			String e1=it.next();
			HashMap<String,Registro> fila=tabla.get(e1);
			Iterator<String> it2=fila.keySet().iterator();
			while(it2.hasNext()) {
					String e2=it2.next();
					if (fila.get(e2).getMarcado()) {
						//Los estados e1 y e2 se pueden juntar
						listaEstadosReducibles.add(new Pareja(e1,e2));
						
					}
					
			}
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		//se chequean los estados que no tienen parejas y se añaden a la lista de estados final
		//con el mismo nombre
		Iterator<String> itAntiguos=listaEstadosAntiguos.iterator();
		while(itAntiguos.hasNext()) {
			String s=itAntiguos.next();
			Iterator<Pareja> itParejas=listaEstadosReducibles.iterator();
			boolean marcado=false;
			while(itParejas.hasNext()) {
				Pareja p=itParejas.next();
				if (p.uno_u_otro(s)) marcado=true;
			}
			if (!marcado) listaEstadosNuevos.add(s);
		}
		/////////////////////////////////////////////////////////////////////////////////////////
		//Se genera la lista de estados final
		Iterator<Pareja> itReduc=listaEstadosReducibles.iterator();
		while(itReduc.hasNext()) {
			Pareja p=itReduc.next();
			//String estadoN="{"+p.e1+","+p.e2+"}";
			listaEstadosNuevos.add(p);
		}
		/////////////////////////////////////////////////////////////////////////////////////////
		//Generar el automata
		//Se generan en primer lugar los estados
		Iterator<Object> itEst=listaEstadosNuevos.iterator();
		while(itEst.hasNext()) {
			Object estado=itEst.next();
			automataSalida.insertaVertice(estado.toString());
		}
		controlador.trataMensaje(mensajero.devuelveMensaje("minimizacion.estadosNuevos", 2));
		controlador.trataMensaje(listaEstadosNuevos+"\n");
		//////////////////////////////////////////////////////////////////////////////////////////
		//ahora se generan las aristas
		/////////////////////////////////////////////////////////////////////////////////////////
		itEst=listaEstadosNuevos.iterator();
		while(itEst.hasNext()) {
			Object estado=itEst.next();
			String est=estado.toString();
			String aux=est.substring(0,1);
			ArrayList<String> listaAristas=new ArrayList<String>();
			if (aux.equals("[")) {
				//es un estado complejo
				if (estado instanceof Pareja) {
					Pareja p=(Pareja)estado;
					ArrayList<String> aristas1=automataEntrada.getAristasVertice(p.e1);
					ArrayList<String> aristas2=automataEntrada.getAristasVertice(p.e2);
					Iterator<String> itAris=aristas1.iterator();
					while(itAris.hasNext()) {
						listaAristas.add(itAris.next());
					}
					itAris=aristas2.iterator();
					while(itAris.hasNext()) {
						String a=itAris.next();
						if (!listaAristas.contains(a)) listaAristas.add(a);
					}
					//lista aristas completada
					Iterator<String> itAristas=listaAristas.iterator();
					while(itAristas.hasNext()) {
						String ar1=itAristas.next();
						String destino=automataEntrada.funcion_delta(p.e1, ar1);
						if (destino==null) destino=automataEntrada.funcion_delta(p.e2, ar1);
						//2 casos posibles esta en estado simple o complejo
						if (listaEstadosNuevos.contains(destino)) {
						//estado destino simple
						automataSalida.insertaArista(estado.toString(),destino,ar1);
					}
					else {
						//estado destino complejo
						Iterator<Pareja> enesimoIt=listaEstadosReducibles.iterator();
						while(enesimoIt.hasNext()) {
							Pareja e=enesimoIt.next();
							if(e.uno_u_otro(destino) && destino != null) {
								automataSalida.insertaArista(estado.toString(), e.toString(), ar1);
								}
							
							}
						}
					}
				}
			}
			else {
				//es un estado simple
				listaAristas=automataEntrada.getAristasVertice(est.toString());
				//lista aristas completada
				Iterator<String> itAristas=listaAristas.iterator();
				while(itAristas.hasNext()) {
					String ar1=itAristas.next();
					String destino=automataEntrada.funcion_delta(estado.toString(), ar1);
					//2 casos posibles esta en estado simple o complejo
					if (listaEstadosNuevos.contains(destino)) {
						//estado destino simple
						automataSalida.insertaArista(estado.toString(),destino,ar1);
					}
					else {
						//estado destino complejo
						Iterator<Pareja> enesimoIt=listaEstadosReducibles.iterator();
						while(enesimoIt.hasNext()) {
							Pareja e=enesimoIt.next();
							if(e.uno_u_otro(destino)) {
								automataSalida.insertaArista(estado.toString(), e.toString(), ar1);
							}
							
						}
					}
				}
				
			}
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////
		
		// por ultimo generacion de lista de estados finales e iniciales
		/////////////////
		//en primer lugar generacion del estadoInicialAntiguo
		String estadoInicialAntiguo=automataEntrada.getEstadoInicial();
		if (listaEstadosNuevos.contains(estadoInicialAntiguo)) {
			//el estado inicial antiguo no se redujo
			automataSalida.setEstadoInicial(estadoInicialAntiguo);
		}
		else {
			//el estado inicial antiguo se redujo
			Iterator<Pareja> reducidos=listaEstadosReducibles.iterator();
			while(reducidos.hasNext()) {
				Pareja p=reducidos.next();
				if (p.uno_u_otro(estadoInicialAntiguo)) {
					automataSalida.setEstadoInicial(p.toString());
				}
			}
		}
		controlador.trataMensaje(mensajero.devuelveMensaje("minimizacion.estInicialNuevo", 2));
		controlador.trataMensaje(automataSalida.getEstadoInicial()+"\n");
		/////////////////////////////////////////////////////////////////////////////////////
		//por ultimo la lista de estados finales.
		ArrayList<String> estadosFinalesAntiguos=automataEntrada.getEstadosFinales();
		Iterator<String> itEstFinales=estadosFinalesAntiguos.iterator();
		ArrayList<String> estadosFinalesNuevos=new ArrayList<String>();
		while(itEstFinales.hasNext()) {
			String estadoFinalAntiguo=itEstFinales.next();
			if (listaEstadosNuevos.contains(estadoFinalAntiguo)&&(!estadosFinalesNuevos.contains(estadoFinalAntiguo))) {
				//el estado inicial antiguo no se redujo
				estadosFinalesNuevos.add(estadoFinalAntiguo);
			}
			else {
				//el estado inicial antiguo se redujo
				Iterator<Pareja> reducidos=listaEstadosReducibles.iterator();
				while(reducidos.hasNext()) {
					Pareja p=reducidos.next();
					if (p.uno_u_otro(estadoFinalAntiguo)&&(!estadosFinalesNuevos.contains(p.toString()))) {
						estadosFinalesNuevos.add(p.toString());
					}
				}
			}
		}
		automataSalida.setEstadosFinales(estadosFinalesNuevos);
		controlador.trataMensaje(mensajero.devuelveMensaje("minimizacion.listaFinNuevos",2));
		controlador.trataMensaje(automataSalida.getEstadosFinales().toString()+"\n");
		//////////////////////////////////////////////////////////////////////////////////////
		//AUTOMATA REDUCIDO
		controlador.trataMensaje("______________________________________________________________________________");
		controlador.trataMensaje(mensajero.devuelveMensaje("minimizacion.minimizado",2)+automataSalida.toString());
	}
	
	//clase privada para grupos de estados
	private class Pareja {
		private String e1;
		private String e2;
		Pareja(String s1,String s2) {
			this.e1=s1;
			this.e2=s2;
		}
		public String toString() {
			return "["+e1+","+e2+"]";
		}
		boolean uno_u_otro(String s) {
			return ((e1.equals(s))||(e2.equals(s)));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void agrupaParejas() {
		ArrayList<String> listaEstados=automataSalida.getEstados();
		ArrayList<String> listaEstadosFinales=automataSalida.getEstadosFinales();
		String estadoInicial=automataSalida.getEstadoInicial();
		/******/
		//automataSalida=new AutomataFD();
		//automataSalida.setEstados(listaEstados);
		/**********/
		ArrayList<String> listaEstadosAsociados=new ArrayList<String>();
		nuevosEstados=new ArrayList<String>();
		nuevosEstadosFinales=new ArrayList<String>();
		
		ArrayList<String> estadosReagrupables=new ArrayList<String>();
		Iterator<String> it=listaEstados.iterator();
		while(it.hasNext()) {
			String estado=it.next();
			if (!estado.contains("[")) {
				//si no se ha reducido se añade a los estados (finales) y normales
				if (listaEstadosFinales.contains(estado)) nuevosEstadosFinales.add(estado);
				nuevosEstados.add(estado);
			}
			else {
				//se ha reducido. vamos a comprobar si tiene parejas asociables(1 o mas!!!!!)
				Iterator<String> it2=listaEstados.iterator();
				while(it2.hasNext()) {
					String estado2=it2.next();
					String coincidencia=coincidenEnUna(estado,estado2);
					if (coincidencia!=null) {
						String estadoNuevo=existePareja(estado,estado2,coincidencia,listaEstadosAsociados);
						if (!estadosReagrupables.contains(estado2))
							estadosReagrupables.add(estado2);
						if (estadoNuevo!=null) {
							//coinciden hay que agrupar(viene ya agrupado, se añade a la lista)
							//y las transiciones
							if (!listaEstadosAsociados.contains(estado)&&(!listaEstadosAsociados.contains(estado2))) {
								listaEstadosAsociados.add(estado);
								listaEstadosAsociados.add(estado2);
								if (this.noRepite(estadoNuevo, nuevosEstados)){
									nuevosEstados.add(estadoNuevo);
									if (listaEstadosFinales.contains(estado)||listaEstadosFinales.contains(estado2)) {
										nuevosEstadosFinales.add(estadoNuevo);
									}
									if (estadoInicial.equals(estado)||estadoInicial.equals(estado2)) {
										nuevosEstadosFinales.add(estadoNuevo);
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println("EST-Reagrupables:"+estadosReagrupables.toString());
		ArrayList<String> nuevosEstadosAgrupados=seagrupaMas(estadosReagrupables);
		if (nuevosEstadosAgrupados!=null) {
			//generadas las parejas de 4 o mas!
			//habria que limpiar la lista de estadosnuevos con los q sobran
			//y posteriormetente generar las transiciones nuevas con los estados grandes
			//(esto ultimo fuera de aqui)
			//usar un booleano para indicar que habra transiciones de 4 o mas!!!!!!!!!!!
			//para el metodo externo
			System.out.println("ANTES:"+nuevosEstadosAgrupados);
			Iterator<String> itNuevosAgrupados=nuevosEstadosAgrupados.iterator();
			while(itNuevosAgrupados.hasNext()) {
				String nuevoEstadoAgrupado=itNuevosAgrupados.next();
				//limpiar hijos de los asociados de este(lo guardo en una lista)
				ArrayList<String> nestadoLista=new ArrayList<String>();
				String nuevo=nuevoEstadoAgrupado.replace("[","");
				nuevo=nuevo.replace("]","");
				StringTokenizer st1=new StringTokenizer(nuevo,",");
				while(st1.hasMoreTokens()) {
					nestadoLista.add(st1.nextToken());
				}
				Iterator<String> itAsociadosViejos=((AbstractList<String>) nuevosEstados.clone()).iterator();
				while(itAsociadosViejos.hasNext()) {
					String vestado=itAsociadosViejos.next();
					ArrayList<String> vestadoLista=new ArrayList<String>();
					String viejo=vestado.replace("[","");
					viejo=viejo.replace("]","");
					StringTokenizer st2=new StringTokenizer(viejo,",");
					while(st2.hasMoreTokens()) {
						vestadoLista.add(st2.nextToken());
					}
					if (nestadoLista.containsAll(vestadoLista)) {
						nuevosEstados.remove(vestado);
					}
				}
				nuevosEstados.add(nuevoEstadoAgrupado);
				System.out.println("LISTA NUEVA:"+listaEstadosAsociados);
				
				
			}
			
			
			
		}
		
		
		Iterator<String> it3=listaEstados.iterator();
		while(it3.hasNext()){
			String est=it3.next();
			if (!listaEstadosAsociados.contains(est)&&(est.contains("["))) {
				nuevosEstados.add(est);
			}
		}
		System.out.println("ESTA SI: "+nuevosEstados);
		controlador.trataMensaje(nuevosEstados.toString());
		controlador.trataMensaje(nuevosEstadosFinales.toString());
	}
	/**
	 * Comprueba que hay parejas de 4 o mas, si las hay las agrupa y las inserta en una lista grande
	 * @param estadosReagrupables
	 * @return
	 */
	private ArrayList<String> seagrupaMas(ArrayList<String> estadosReagrupables) {
		// TODO Auto-generated method stub
		//ArrayList<ArrayList<String>> listaDeReagrupados=new ArrayList<ArrayList<String>>();
		ArrayList<String> nestados=new ArrayList<String>();
		Iterator<String> itreagrup=estadosReagrupables.iterator();
		while(itreagrup.hasNext()) {
			String st=itreagrup.next();
			ArrayList<String> coincidenciasSt=new ArrayList<String>();
			ArrayList<String> coincidenciasSt2=new ArrayList<String>();
			Iterator<String> itreagrup2=estadosReagrupables.iterator();
			while(itreagrup2.hasNext()) {
				String st2=itreagrup2.next();
				String coincidencia=coincidenEnUna(st,st2);
				if (coincidencia!=null&&(!coincidenciasSt.contains(st2))) {
					coincidenciasSt.add(st2);
				}
				Iterator<String> itcoincidencias=coincidenciasSt.iterator();
				while(itcoincidencias.hasNext()) {
					String st3=itcoincidencias.next();
					Iterator<String> itotro=estadosReagrupables.iterator();
					while(itotro.hasNext()) {
						String st4=itotro.next();
						String coincidencia1=coincidenEnUna(st3,st4);
						if (coincidencia1!=null&&(!coincidenciasSt2.contains(st4))) {
							coincidenciasSt2.add(st4);
						}
					}
					
				}
			}
			//coincidenciasSt.addAll(coincidenciasSt2);
			Iterator<String> itcst2=coincidenciasSt2.iterator();
			while(itcst2.hasNext()) {
				String cst=itcst2.next();
				if (!coincidenciasSt.contains(cst)) {
					coincidenciasSt.add(cst);
				}
			}
			System.out.println("COINCIDENCIAS DE "+st+" "+coincidenciasSt.toString());
			if (coincidenciasSt.size()==6) {
				//he encontrado un conjunto de estados que se agrupan en 4 o mas
				System.out.println("Hay reagrupamiento de 4 o mas estados");
				//GENERACION DE LA LISTA NUEVA
				String macroEstadoReagrupado="[";
				Iterator<String> itmer=coincidenciasSt.iterator();
				while(itmer.hasNext()) {
					String estado=itmer.next();
					estado=estado.replace("[", "");
					estado=estado.replace("]", "");
					StringTokenizer stoken=new StringTokenizer(estado,",");
					while(stoken.hasMoreTokens()) {
						String vestado=stoken.nextToken();
						System.out.println(vestado);
						if (!macroEstadoReagrupado.contains(vestado)) {
							if (macroEstadoReagrupado.length()>1) {
								macroEstadoReagrupado+=","+vestado;
							}
							else {
								macroEstadoReagrupado+=vestado;
							}
							
						}
					}
				}
				macroEstadoReagrupado+="]";
				System.out.println("MACRO ESTADO:"+macroEstadoReagrupado);
				if (noRepite(macroEstadoReagrupado,nestados))
					nestados.add(macroEstadoReagrupado);
			}
			
		}
		System.out.println("NUEVOS ESTADOS:::"+nestados);
		if (nestados.size()>=1) return nestados;
		return null;
	}
/////////////////////////////////////////////////////////////////////////////////////
	private boolean noRepite(String macroEstadoReagrupado,ArrayList<String> nestados) {
		// TODO Auto-generated method stubñ
		ArrayList<String> mer=new ArrayList<String>();
		String nuevo=macroEstadoReagrupado.replace("[","");
		nuevo=nuevo.replace("]","");
		StringTokenizer st1=new StringTokenizer(nuevo,",");
		while(st1.hasMoreTokens()) {
			mer.add(st1.nextToken());
		}
		
		Iterator<String> itnest=nestados.iterator();
		while(itnest.hasNext()) {
			String nest=itnest.next();
			ArrayList<String> nestt=new ArrayList<String>();
			String nuevonest=nest.replace("[","");
			nuevonest=nuevonest.replace("]","");
			StringTokenizer st2=new StringTokenizer(nuevonest,",");
			while(st2.hasMoreTokens()) {
				nestt.add(st2.nextToken());
			}
			if (mer.containsAll(nestt)) return false;
		}
		return true;
	}
////////////////////////////////////////////////////////////////////////////////////
	
	private String coincidenEnUna(String estado1, String estado2) {
		
		StringTokenizer t=new StringTokenizer(estado1,",");
		String s1=t.nextToken();
		s1=s1.replace("[", "");
		String s2=null;
		if (t.hasMoreElements()) {
			s2=(String)t.nextElement();
			s2=s2.replace("]","");
		}
		t=new StringTokenizer(estado2,",");
		String s3=t.nextToken();
		s1=s1.replace("[", "");
		String s4=null;
		if (t.hasMoreElements()) {
			s4=(String)t.nextElement();
			s4=s4.replace("]","");
		}
		if (s1.equals(s3)) return s1;
		if (s1.equals(s4)) return s1;
		if (s2.equals(s3)) return s2;
		if (s2.equals(s4)) return s2;
		return null;
	}
	
	private String existePareja(String estado1,String estado2, String coincidencia,ArrayList<String> asociados) {
		
		ArrayList<String> listaEstados=automataSalida.getEstados();
		Iterator<String> it=listaEstados.iterator();
		//....
		StringTokenizer t=new StringTokenizer(estado1,",");
		String s1=t.nextToken();
		s1=s1.replace("[", "");
		String s2=null;
		s2=(String)t.nextElement();
		s2=s2.replace("]","");
		//....
		t=new StringTokenizer(estado2
				,",");
		String s3=t.nextToken();
		s3=s3.replace("[", "");
		String s4=null;
		s4=(String)t.nextElement();
		s4=s4.replace("]","");
		//......
		String aBuscar=null;
		String aBuscar2=null;
		if ((coincidencia.equals(s1)) &&(coincidencia.equals(s3))) {
			aBuscar="["+s2+","+s4+"]";
			aBuscar2="["+s4+","+s2+"]";
		}
		if ((coincidencia.equals(s2)) &&(coincidencia.equals(s4))) {
			aBuscar="["+s1+","+s3+"]";
			aBuscar2="["+s3+","+s1+"]";
		}
		if ((coincidencia.equals(s1)) &&(coincidencia.equals(s4))) {
			aBuscar="["+s2+","+s3+"]";	
			aBuscar2="["+s3+","+s2+"]";
		}
		if ((coincidencia.equals(s2)) &&(coincidencia.equals(s4))) {
			aBuscar="["+s1+","+s3+"]";	
			aBuscar2="["+s3+","+s1+"]";
		}
		
		while(it.hasNext()) {
			String s=it.next();
			if (s.equals(aBuscar)) {
				String estadoNuevo=null;
				//hay que generar [aBuscar,coincidencia] bien generado: [aBuscar.s1,aBuscar.s2,coincidencia]
				String aux=aBuscar.replace("]","");
				aux=aux.replace("[", "");
				estadoNuevo="["+aux+","+coincidencia+"]";
				asociados.add(aBuscar);
				return estadoNuevo;
			}
			if (s.equals(aBuscar2)) {
				String estadoNuevo=null;
				String aux=aBuscar.replace("]","");
				aux=aux.replace("[", "");
				estadoNuevo="["+aux+","+coincidencia+"]";
				asociados.add(aBuscar2);
				return estadoNuevo;
			}
		}
		return null;
	}
	/**
	 * 
	 */
	private void generaNuevoAutomata() {
		System.out.println(automataSalida.toString());
		Automata nuevaSalida=new AutomataFD();
		nuevaSalida.setEstados(nuevosEstados);
		//nuevaSalida.setEstadosFinales(nuevosEstadosFinales);
		//OJO AQUI CON EL ESTADO INICIAL HAY QUE VER SI ES REDUCIDO
		/*if (nuevosEstados.contains(automataSalida.getEstadoInicial())) {
			nuevaSalida.setEstadoInicial(automataSalida.getEstadoInicial());
		}
		else {
			//hay que buscarle entre los reducidos!!!!!
			//y cargarlo!! hacer!!
		}*/
		//-------------------------------------------------------------------
		Iterator<String> it=nuevosEstados.iterator();
		System.out.println(nuevosEstados);
		String vestado=null;
		while(it.hasNext()){
			String estado=it.next();
			ArrayList<String> delta=null;
			if (!estado.contains("[")) {
				//estado no reducido
				delta=automataSalida.getAristasVertice(estado);
				vestado=null;
			}
			else {
				if (automataSalida.getEstados().contains(estado)) {
					//ya estaba a pesar de ser reducido
					delta=automataSalida.getAristasVertice(estado);
					System.out.println("MIRAR AQUI TMB:"+estado+"  "+"["+delta+"]");
					vestado=null;
				}
				else {
					System.out.println("MIRAR ESTO:"+estado);
					//caso de nuevo estado reducido
					//debemos obtener la delta del estado compuesto
					String aux=estado.replace("[","");
					aux=aux.replace("]", "");
					StringTokenizer st=new StringTokenizer(aux,",");
					String s1=st.nextToken();
					String s2=st.nextToken();
					vestado="["+s1+","+s2+"]";
					delta=automataSalida.getAristasVertice(vestado);//suficiente no???
					System.out.println(delta);
					if (delta.size()==0) {
						vestado="["+s2+","+s1+"]";
						System.out.println(vestado);
						delta=automataSalida.getAristasVertice(vestado);//suficiente no???
						System.out.println(delta);
					}
				
				}
			}
			Iterator<String> it2=delta.iterator();
			while(it2.hasNext()) {
				System.out.println("ESTADO:"+estado);
				String letra=it2.next();
				String destino=null;
				if (vestado==null) {
					System.out.println("Destino:"+destino+"  estado:"+estado);
					destino=automataSalida.funcion_delta(estado, letra);
				}
				else {
					System.out.println("VESTADO:"+vestado);
					destino=automataSalida.funcion_delta(vestado, letra);
				}
				if (nuevaSalida.getEstados().contains(destino)) {
					nuevaSalida.insertaArista(estado,destino, letra);
				}
				else {
					//a quien corresponde de los nuevos el estado destino
					String destinoNuevo=buscarDestinoNuevo(destino);
					System.out.println(destinoNuevo);
					nuevaSalida.insertaArista(estado, destinoNuevo, letra);
				}
			}
			//if (automataSalida.getEstadoInicial().equals(estado)) nuevaSalida.setEstadoInicial(estado);
			if (automataSalida.getEstadosFinales().contains(estado)) {
				if (vestado==null) nuevaSalida.insertaEstadoFinal(estado);
				else nuevaSalida.insertaEstadoFinal(vestado);
			}
		}
		if (nuevosEstados.contains(automataSalida.getEstadoInicial())) {
			nuevaSalida.setEstadoInicial(automataSalida.getEstadoInicial());
		}
		else {
			String estadoInicial=buscarDestinoNuevo(automataSalida.getEstadoInicial());
			nuevaSalida.setEstadoInicial(estadoInicial);
		}
		Iterator<String> itFinales=automataSalida.getEstadosFinales().iterator();
		while(itFinales.hasNext()) {
			String efi=itFinales.next();
			efi=buscarDestinoNuevo(efi);
			nuevaSalida.setEstadoFinal(efi);
		}
		System.out.println(nuevaSalida.toString());
		nuevaSalida.setAlfabeto(automataSalida.getAlfabeto());
		automataSalida=nuevaSalida;
		
	}
	
	/**
	 * 
	 */
	
	private String buscarDestinoNuevo(String estado) {
		//estado es el estado viejo, destino nuevo el nuevo
		Iterator<String> it=nuevosEstados.iterator();
		while(it.hasNext()) {
			String d=it.next();
			if (d.equals(estado)) return estado;
			if (d!=null&&estado!=null) {
			if (d.contains("[")) {
				if (d.length()>=estado.length()) {
					String aux=estado.replace("[", "");
					aux=aux.replace("]","");
					StringTokenizer st=new StringTokenizer(aux,",");
					boolean estan=true;
					while(st.hasMoreTokens()) {
						String est=st.nextToken();
						if (!d.contains(est)) estan=false;
					}
					if (estan) return d;
				}
			}
			
		}}
		return estado;
	}
		
	
	/**
	 * 
	 */
	public Automata ejecutar(boolean pasos) {
		automataEntrada=a�adirIndices(automataEntrada);
		automataSalida=new AutomataFD();
		controlador=Controlador_imp.getInstancia();
		
		controlador.trataMensaje("______________________________________________________________________________");
		inicializarTabla();
		controlador.trataMensaje("Tabla inicial:");
		controlador.trataMensaje(tabla.toString());
		if (pasos) {
			xml="<exit>";
			xml+="<steps>";
		}
		for(int i=0;i<numPasos;i++) {
			//a continuacion hay que considerar las parejas de estados (p,q) no marcados
			paso();
			controlador.trataMensaje("___________________________________________________________________________");
			controlador.trataMensaje(mensajero.devuelveMensaje("minimizacion.tablaPasos",2));
			controlador.trataMensaje(tabla.toString());
			if (pasos) {
				xml+="<step>";
				xml+=traducirTablaMinimizacion(tabla);
				xml+="</step>";
			}
		}
		if (pasos) {
			xml+="</steps>";
		}
		//ahora hay que generar el nuevo automata
		controlador.trataMensaje("______________________________________________________________________________");
		if (soloEquivalencia) return null;
		generaAutomata();
		agrupaParejas();
		
		//si hay cambios generar el nuevo automata
		//si no hay cambios no se hace nda
		//comprobe estado inicial????
		if (automataSalida.getEstados().containsAll(nuevosEstados)) {
			//no hay cmabios: finalizacion del computo
			controlador.trataMensaje(mensajero.devuelveMensaje("minimizacion.noAgrupa",2));
		}
		else {
			System.out.println("Nuevos estados:"+nuevosEstados.toString());
			//hay cambios se debe generar el nuevo automata!!
			controlador.trataMensaje(mensajero.devuelveMensaje("minimizacion.agrupa",2));
			//Automata aux=renombrar(automataSalida);
			//System.out.println(aux);
			//MinimizacionAFD nuevoAlgoritmo=new MinimizacionAFD(aux);
			//automataSalida=nuevoAlgoritmo.ejecutar(false);
			
			generaNuevoAutomata();
		}
		controlador.trataMensaje(automataSalida.toString());
		//generacion del xml de salida
		automataSalida.setAlfabeto(automataEntrada.getAlfabeto());
		automataSalida=renombrarSufijos(automataSalida);
		xml+="<result>"+traducirXML(automataSalida)+"</result></exit>";
		//fin generacion xml salida
		//automataSalida=renombrar(automataSalida);
		return automataSalida;
	}

private Automata renombrarSufijos(Automata a) {
	Automata automata=new AutomataFD();
	this.quitaInaccesibles(a);
	Iterator<String> itEst=a.getEstados().iterator();
	ArrayList<String> nEstados=new ArrayList<String>();
	ArrayList<String> nEstadosFin=new ArrayList<String>();
	HashMap<String,String> thash=new HashMap<String,String>();
	int estate = 0;
	while(itEst.hasNext()) {
		String estado=itEst.next();
		if (!estado.contains("[")) {
			thash.put(estado,estado.substring(0,estado.length()-1));
			if (a.getEstadoInicial().equals(estado)) automata.setEstadoInicial(estado.substring(0,estado.length()-1));
			if (a.getEstadosFinales().contains(estado)) nEstadosFin.add(estado.substring(0,estado.length()-1));
			nEstados.add(estado.substring(0,estado.length()-1));
		}
		else {
			String estadoST=estado.replace("[","");
			estadoST=estadoST.replace("]","");
			StringTokenizer st=new StringTokenizer(estadoST);
			String estadoN="[";
			while (st.hasMoreTokens()) {
				String s=st.nextToken(",");
				System.out.println(s);
				if (estadoN.equals("[")) estadoN+=s.substring(0,s.length()-1);
				else estadoN+=","+s.substring(0,s.length()-1);
			}
			estadoN+="]";
				thash.put(estado,"Q"+estate);
				if (a.getEstadoInicial().equals(estado)) automata.setEstadoInicial("Q"+estate);
				if (a.getEstadosFinales().contains(estado)) nEstadosFin.add("Q"+estate);
				nEstados.add("Q"+estate);
				estate++;
		}
	}
	automata.setEstados(nEstados);
	automata.setEstadosFinales(nEstadosFin);
	automata.setAlfabeto(a.getAlfabeto());
	Iterator<String> itEst2=a.getEstados().iterator();
	while(itEst2.hasNext()) {
		String estado=itEst2.next();
		Iterator<String> itAr=a.getAristasVertice(estado).iterator();
		while(itAr.hasNext()) {
			String letra=itAr.next();
			ArrayList<String> destino=a.deltaExtendida(estado, letra);
			Iterator<String> itDest=destino.iterator();
			while(itDest.hasNext()) {
				String dest=itDest.next();
				String ndest=thash.get(dest);
				automata.insertaArista(thash.get(estado), ndest, letra);
				//insertar pero no olvidar el subindice!!
				
			}
		}
	}
	AutomataFD au2 = null;
	try {
		 au2 = (AutomataFD) automata.clone();
	} catch (CloneNotSupportedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	automata = this.quitaInaccesibles(au2);
	if (!au2.getEstados().equals(automata.getEstados())){
		MinimizacionAFD min = null;
		try {
			min = new MinimizacionAFD(au2);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		automata = min.ejecutar(false);
	}
	//RENOMBRAR ESTADOS
	return automata;
	}



public void registraControlador(Controlador controlador) {
	// TODO Auto-generated method stub
	this.controlador=controlador;
	
}

/**
 * Mñtodo accesor de la tabla de minimizaciñn del algoritmo
 * @return la tabla de minimizaciñn de tipo HashMap<String,HashMap<String,Boolean>>
 */
public HashMap<String,HashMap<String,Registro>> getTablaMinimizacion() {
	return tabla;
}


public String getXML() {
	// TODO Auto-generated method stub
	return xml;
}
private String traducirXML(Automata automata) {
	String fichero="<authomata>\n\t<type>\n\t\t<item>";
	String tipoAutomatas="AutomataFD";
	fichero+=tipoAutomatas+"</item>\n\t</type>\n";
	Iterator<String> itAlfabeto=automata.getAlfabeto().dameListaLetras().iterator();
	fichero+="\t<alphabet>\n\t";
	while(itAlfabeto.hasNext()) {
		fichero+="\t<item>"+itAlfabeto.next()+"</item>\n\t";
	}
	fichero+="</alphabet>\n\t";
	//se genera la lista de estados y se guarda en el xml
	Iterator<String> itEst=automata.getEstados().iterator();
	fichero+="<states>\n\t";
	while(itEst.hasNext()) {
		fichero+="\t<state>"+itEst.next()+"</state>\n\t";
	}
	fichero+="</states>\n\t";
	//GENERACION DE LOS EsTADOS FINALES E INICIALES
	fichero+="<init>\n\t\t<state>"+automata.getEstadoInicial()+"</state>\n\t</init>\n\t";
	Iterator<String> itFin=automata.getEstadosFinales().iterator();
	fichero+="<finals>\n\t";
	while(itFin.hasNext()) {
		fichero+="\t<state>"+itFin.next()+"</state>\n\t";
	}
	fichero+="</finals>\n\t";
	//GENERACION DE LAS ARISTAS!
	fichero+="<arrows>\n\t";
	Iterator<String> itEstados=automata.getEstados().iterator();
	while(itEstados.hasNext()) {
		String e1=itEstados.next();
		Iterator<String> itAr=automata.getAristasVertice(e1).iterator();
		while(itAr.hasNext()){
			String letra=itAr.next();
			Iterator<String> itE=automata.deltaExtendida(e1, letra).iterator();
			while(itE.hasNext()){
				String e2=itE.next();
				fichero+="\t<arrow>\n\t";
				fichero+="\t<state>"+e1+"</state>\n\t";
				fichero+="\t<state>"+e2+"</state>\n\t";
				fichero+="\t<item>"+letra+"</item>\n\t</arrow>\n\t";
				}
			}	
		}
		fichero+="</arrows>\n";
		fichero+="</authomata>";
		return fichero;
	}

	private String traducirTablaMinimizacion(HashMap<String,HashMap<String,Registro>> tabla) {
		String salida="<table>";
		Iterator<String> itc=tabla.keySet().iterator();
		while(itc.hasNext()) {
			String clave=itc.next();
			salida+="<column><key>"+clave+"</key>";
			HashMap<String,Registro> hs=tabla.get(clave);
			Iterator<String> itf=tabla.keySet().iterator();
			while(itf.hasNext()) {
				String fila=itf.next();
				if (hs.get(fila)==null)
				salida+="<fila><state>"+fila+"</state>"+"<compatible>"+hs.get(fila)+"</compatible></fila>";
				else{
					salida+="<fila><state>"+fila+"</state><compatible>"+hs.get(fila).getMarcado()+"</compatible>";
					salida+="<paso>"+hs.get(fila).getPaso()+"</paso>";
					salida+="<estado>"+hs.get(fila).getEstados()+"</estado></fila>";
				}
			}
			salida+="</column>";
		}
		return salida+"</table>";
	}
	
	private Automata a�adirIndices(Automata a) {
		Automata automata=new AutomataFD();
		Iterator<String> itEst=a.getEstados().iterator();
		ArrayList<String> nEstados=new ArrayList<String>();
		ArrayList<String> nEstadosFin=new ArrayList<String>();
		int cont=0;
		HashMap<String,String> thash=new HashMap<String,String>();
		while(itEst.hasNext()) {
			String estado=itEst.next();
			thash.put(estado,estado+cont);
			if (a.getEstadoInicial().equals(estado)) automata.setEstadoInicial(estado+cont);
			if (a.getEstadosFinales().contains(estado)) nEstadosFin.add(estado+cont);
			nEstados.add(estado+cont);
			cont++;
		}
		automata.setEstados(nEstados);
		automata.setEstadosFinales(nEstadosFin);
		automata.setAlfabeto(a.getAlfabeto());
		Iterator<String> itEst2=a.getEstados().iterator();
		while(itEst2.hasNext()) {
			String estado=itEst2.next();
			Iterator<String> itAr=a.getAristasVertice(estado).iterator();
			while(itAr.hasNext()) {
				String letra=itAr.next();
				ArrayList<String> destino=a.deltaExtendida(estado, letra);
				Iterator<String> itDest=destino.iterator();
				while(itDest.hasNext()) {
					String dest=itDest.next();
					String ndest=thash.get(dest);
					if (ndest != null){
						automata.insertaArista(thash.get(estado), ndest, letra);
					//insertar pero no olvidar el subindice!!
					}
				}
			}
		}
		//RENOMBRAR ESTADOS
		return automata;
	}	
	
	
	
	
	public static void main(String[] args) {
		AutomataFD a=new AutomataFD();
		ParserXML p=ParserXML.getInstancia();
		try {
			a=(AutomataFD) p.extraerAutomata("XML/ejemplos/AFD/ejemplo10.xml");
		} catch (AutomatasException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(a.toString());
		
		MinimizacionAFD min = null;
		try {
			min = new MinimizacionAFD(a);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		min.ejecutar(true);
	}
	
	
	
}
