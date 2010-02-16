/**
 * 
 */
package modelo.algoritmos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import controlador.Controlador;
import controlador.Controlador_imp;

import accesoBD.Mensajero;

import modelo.Algoritmo;
import modelo.automatas.Alfabeto;
import modelo.automatas.Alfabeto_imp;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFD;
import modelo.automatas.AutomataFND;

/**
 * Clase que ejecuta los procedimeintos para llevar a cabo la transformaciñn desde
 * un automata no determinista a uno determinista
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class AFN_to_AFD implements Algoritmo{
	private Automata automataEntrada;
	private Automata automataSalida;
	private Mensajero m;
	private Controlador controlador;
	private String xml;
	private HashMap<String,ArrayList<String>> tablaCorrespondencia;

	/**
	 * Constructor del algortimo
	 * @param autEntrada el automata a transformar
	 */
	public AFN_to_AFD(Automata autEntrada) {
		if(autEntrada instanceof AutomataFND) automataEntrada=autEntrada;
		else automataEntrada=null;
		automataSalida=new AutomataFD();
		m=new Mensajero();
		tablaCorrespondencia=new HashMap<String,ArrayList<String>>();
	}
	
	
	public void registraControlador(Controlador controlador) {
		this.controlador=controlador;
	}
	
	
	public Automata ejecutar(boolean pasos) {
		Alfabeto alfNuevo=new Alfabeto_imp();
		
		if (automataEntrada==null) {
			controlador.trataMensaje(m.devuelveMensaje("AFNtoAFD.error1", 2));
		}
		else {
			AutomataFND autEntrada=(AutomataFND)automataEntrada;
			String estadoInicial=autEntrada.getEstadoInicial();
			//en esta tabla se almacenan el nombre del nuevo estado, y los estados a los que corresponde(del viejo)
			HashMap<String,ArrayList<String>> tablaNuevosEstados=new HashMap<String,ArrayList<String>>();
			String estadoInicialFinal="t1";
			automataSalida.insertaVertice(estadoInicialFinal);
			automataSalida.setEstadoInicial(estadoInicialFinal);
			if (automataEntrada.getEstadosFinales().contains(estadoInicial))
				automataSalida.setEstadoFinal(estadoInicialFinal);
			ArrayList<String> lista1=new ArrayList<String>();
			lista1.add(estadoInicialFinal);
			ArrayList<String> lista2=new ArrayList<String>();
			lista2.add(estadoInicial);
			tablaNuevosEstados.put(estadoInicialFinal, lista1);
			tablaCorrespondencia.put(estadoInicialFinal,lista2);
			//estados a los que llega el estado inicial
			ArrayList<String> listaAristas1=automataEntrada.getAristasVertice(estadoInicial);
			Iterator<String> it1=listaAristas1.iterator();
			ArrayList<String> listaEstadosParcial=new ArrayList<String>();
			int cont=2;
			while(it1.hasNext()) {
				//ojo al string, si viene completo o con info redundante, creo que es indifirente
				String arista=it1.next();
				String estadoSig="t"+cont;
				automataSalida.insertaVertice(estadoSig);
				automataSalida.insertaArista(estadoInicialFinal, estadoSig, arista);
				//estan aseguradas las repeticiones???, creo que si.
				listaEstadosParcial.add(estadoSig);
				ArrayList<String> estadosDestino=autEntrada.deltaExtendida(estadoInicial, arista);
				tablaNuevosEstados.put(estadoSig,estadosDestino);
				tablaCorrespondencia.put(estadoSig, estadosDestino);
				if (algunoEsFinal(tablaNuevosEstados.get(estadoSig))) {
					automataSalida.insertaEstadoFinal(estadoSig);
				}
				cont++;
			}
			//se finalizo el tratamiento del primer estado
			//ahora se tratan los estados siguientes(deben tratarse en conjunto)
			boolean sigue=true;
			ArrayList<String> nuevaListaEstadosParcial=new ArrayList<String>();
			while(sigue) {
				if (nuevaListaEstadosParcial.size()>0) listaEstadosParcial=nuevaListaEstadosParcial;
				nuevaListaEstadosParcial=new ArrayList<String>();
				Iterator<String> it3=listaEstadosParcial.iterator();
				while(it3.hasNext()) {
					String estado=it3.next();
					ArrayList<String> lEstados=tablaNuevosEstados.get(estado);//lestados antiguos que corresponde
					Iterator<String> it4=automataEntrada.getAlfabeto().dameListaLetras().iterator();
					while(it4.hasNext()) {
						String letra=it4.next();
						ArrayList<String> listaEstadosSiguientes=new ArrayList<String>();
						Iterator<String> it5=lEstados.iterator();
						while(it5.hasNext()) {
							String estAntiguo=it5.next();							
							if (autEntrada.deltaExtendida(estAntiguo, letra)!=null) {
								if (!alfNuevo.getListaLetras().contains(letra)) alfNuevo.aniadirLetra(letra);
								Iterator<String> it6=autEntrada.deltaExtendida(estAntiguo, letra).iterator();
								while(it6.hasNext()) {
									String est=it6.next();
									if (!listaEstadosSiguientes.contains(est))
										listaEstadosSiguientes.add(est);
								}
							}
						}
						String estadoNuevo=existe(listaEstadosSiguientes,tablaNuevosEstados);
						if (estadoNuevo==null){
							estadoNuevo="t"+cont;
							cont++;
							tablaNuevosEstados.put(estadoNuevo, listaEstadosSiguientes);
							tablaCorrespondencia.put(estadoNuevo, listaEstadosSiguientes);
							nuevaListaEstadosParcial.add(estadoNuevo);
							automataSalida.insertaVertice(estadoNuevo);
							if (alfNuevo.getListaLetras().contains(letra))
								automataSalida.insertaArista(estado, estadoNuevo, letra);
							if (algunoEsFinal(tablaNuevosEstados.get(estadoNuevo))) {
								automataSalida.insertaEstadoFinal(estadoNuevo);
							}
						}
						else {
							if (alfNuevo.getListaLetras().contains(letra)) {
								//FALTA COMPROBAR QUE EXiSTE LA ARISTA
								if (existeArista(estado,estadoNuevo,letra))
									automataSalida.insertaArista(estado, estadoNuevo, letra);
							}
						}
					}
				}
				if (nuevaListaEstadosParcial.size()==0) sigue=false;
			}
		}
					
			
		controlador=Controlador_imp.getInstancia();		
		controlador.trataMensaje(automataSalida.toString());
		controlador.trataMensaje(m.devuelveMensaje("AFNtoAFD.finalizado",2));
		xml=new String();
		automataSalida.setAlfabeto(alfNuevo);
		xml+="<exit><steps>"+traducirTablaTraduccion()+"</steps>";
		xml+="<result>"+traducirXML(automataSalida)+"</result></exit>";
		//HAY QUE TRADUCIR A XML LA Tbla de correspondencia junto con el automata, para que salga como en apuntes en la salida
		//XML...... intentar que sea lo mñs parseable posible
		//VIGILAR QUE LA TABLA DE CORRESPONDENCIA ESTE CORRECTA PARA XML, ver ejemplo 10, 
		//resultado minimizacion ok, pero la tabla no esta tan claro que este bien
		return automataSalida;
	}
	
	private boolean existeArista(String estado, String estadoNuevo, String letra) {
		
		ArrayList<String> origenCorresponde=tablaCorrespondencia.get(estado);
		ArrayList<String> destinoCorresponde=tablaCorrespondencia.get(estadoNuevo);
		
		Iterator<String> itOri=origenCorresponde.iterator();
		while(itOri.hasNext()) {
			String estViejo=itOri.next();
			ArrayList<String> delta=automataEntrada.deltaExtendida(estViejo, letra);
			if (delta!=null) {
				Iterator<String> itViejo=delta.iterator();
				while(itViejo.hasNext()) {
					String viejo=itViejo.next();
					if (destinoCorresponde.contains(viejo))  return true;
				}
			}
		}
		return false;
	}
	
	private boolean algunoEsFinal(ArrayList<String> lEst) {
		Iterator<String> it=lEst.iterator();
		ArrayList<String> estadosFinales=automataEntrada.getEstadosFinales();
		while(it.hasNext()) {
			if (estadosFinales.contains(it.next())) return true;		
		}
		return false;
	}
	
	private String existe(ArrayList<String> listaEstados,HashMap<String,ArrayList<String>> tablaNuevosEstados) {
		//null si no existe
		//el estado que corresponde si existe
		
		Iterator<String> it2=tablaNuevosEstados.keySet().iterator();
		while(it2.hasNext()) {
			String key=it2.next();
			if (tablaNuevosEstados.get(key).containsAll(listaEstados)) {
				return key;
				}
			}
		return null;
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
	
	
	private String traducirTablaTraduccion() {
		String fichero="<table>";
		fichero+="";
		Iterator<String> itEst=automataSalida.getEstados().iterator();
		while(itEst.hasNext()) {
			String estado=itEst.next();
			String correspondencia=tablaCorrespondencia.get(estado).toString();
			fichero+="<flecha>";
			fichero+="<estado>"+estado+"="+correspondencia+"</estado>";
			Iterator<String> itLetras=automataSalida.getAlfabeto().dameListaLetras().iterator();
			while(itLetras.hasNext()) {
				String letra=itLetras.next();
				fichero+="<letra>"+letra+"</letra>";
				//if (!destino.equals("0"))
					fichero+="<destino>"+tablaCorrespondencia.get(automataSalida.funcion_delta(estado, letra))+"</destino>";
				//else fichero+="<destino>"+destino+"</destino>";
			}
			fichero+="</flecha>";
		}

		fichero+="</table>";
		return fichero;
	}


}
