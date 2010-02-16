/**
 * 
 */
package modelo.algoritmos;

import java.util.ArrayList;
import java.util.Iterator;
import accesoBD.Mensajero;
import controlador.Controlador;
import controlador.Controlador_imp;
import modelo.Algoritmo;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFD;
import modelo.expresion_regular.ExpresionRegular;

/**
 * Clase que implementa el algoritmo de transformaciñn de automata finito
 * a expresiñn regular
 * @author Miguel Ballesteros, Josñ Antonio Blanes, Samer Nabhan
 */
public class AFD_to_ER implements Algoritmo {

	private String xml;
	private Automata automataEntrada;
	private ExpresionRegular er;
	private Controlador controlador;
	private Mensajero mensajero;
	private String expresionRegular;
	
	private AutomataFD resultadosParciales;
	
	/**
	 * Constructor del Algortimo de obtenciñn de expresiñn regular
	 * a partir de Automata
	 * @param a automata del que queremos obtener la expresiñn regular
	 */
	public AFD_to_ER(Automata a){
		automataEntrada=a;
		mensajero=Mensajero.getInstancia();
		xml=new String();
		controlador=Controlador_imp.getInstancia();
	}
	
	/**
	 * Nñtodo que devuelve el String con la expresiñn regular resultante
	 * @return String con la expresiñn regular
	 */
	public ExpresionRegular cogeSalida(){
		return er;
	}
	
	
	@SuppressWarnings("unchecked")
	
	public Automata ejecutar(boolean muestraPasos) {
		// TODO Auto-generated method stub
		controlador.trataMensaje(mensajero.devuelveMensaje("AFD_TO_ER.inicio",2).toUpperCase());
		expresionRegular=new String();	
		ArrayList<String> noFinales=(ArrayList<String>) automataEntrada.getEstados().clone();
		noFinales.removeAll(automataEntrada.getEstadosFinales());
		AutomataFD afd=(AutomataFD) automataEntrada;
		try {
			resultadosParciales=(AutomataFD) afd.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		Iterator<String> itnf=noFinales.iterator();
		//antes xml=automata entrada
		xml+="<steps>";
		/*
		xml+="<step><comment>Automata de entrada</comment>"+traducirXML(automataEntrada)+"</step>";*/
		while(itnf.hasNext()) {
			String estado=itnf.next();
			if (!estado.equals(automataEntrada.getEstadoInicial())) {
				resultadosParciales=eliminaEstado(estado,muestraPasos,resultadosParciales);
				//GENERACION DEL XmL
				xml+="<step><comment>"+mensajero.devuelveMensaje("AFD_TO_ER.eliminar",2)+estado+"</comment>";
				xml+=traducirXML(resultadosParciales)+"</step>";
			}
		}	
		
		//ahora calculamos las expresiones regulares para los estados que nos quedan
		//y las concatenamos con +
		
		ArrayList<String> listaFinales=automataEntrada.getEstadosFinales();
		
		String inicial=automataEntrada.getEstadoInicial();
		Automata resulParc2=null;
		try {
			resulParc2=(AutomataFD) resultadosParciales.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<String> itFinales2=listaFinales.iterator();
		while(itFinales2.hasNext()) {
			String final2=itFinales2.next();
			if (!inicial.equals(final2)) {
				resulParc2=eliminaEstado(final2,muestraPasos,(AutomataFD) resulParc2);
			}
		}
		String erInicial=new String();
		ArrayList<String> ini=resulParc2.getLetraTransicion(inicial, inicial);
		Iterator<String> itIni=ini.iterator();
		int pasadas=0;
		while(itIni.hasNext()){
			String transIni=itIni.next();
			if (transIni.length()>1&&transIni.contains("+")
					) {
				transIni="("+transIni+")";
			}
			erInicial+=transIni;
			pasadas++;
		}
		if (((erInicial.length()>1&&erInicial.contains("+")&&pasadas>1))&&((erInicial.charAt(0)!='(')&&(erInicial.charAt(erInicial.length()-1)!=')'))) {
			erInicial="("+erInicial+")";
		}
		if(!erInicial.equals(""))
			erInicial+="*";
		if (automataEntrada.getEstadosFinales().contains(automataEntrada.getEstadoInicial()))
			expresionRegular+=erInicial;
	
		
		ArrayList<String> adyacentesInicial=resultadosParciales.getDestinos(inicial);
		EstadoString initial=new EstadoString(inicial,null);
		//ahora debemos calcular la er de cada uno de ellos.
		ArrayList<EstadoString> estadosER=new ArrayList<EstadoString>();
		Iterator<String> ad=adyacentesInicial.iterator();
		initial.setExpresion(erInicial);
		estadosER.add(initial);
		while(ad.hasNext()){
			String est=ad.next();
			if(!est.equals(inicial))
				estadosER.add(new EstadoString(est,initial));
		}
		int i=0;
		while(i<estadosER.size()){
			String s=estadosER.get(i).getNombreEstado();
			Iterator<String> trans=resultadosParciales.getDestinos(s).iterator();
			while(trans.hasNext()){
				String transE=trans.next();
				if(!transE.equals(s)&&noEsta(estadosER,transE))
					estadosER.add(new EstadoString(transE,estadosER.get(i)));
			}
			i++;
		}
		if (automataEntrada.getEstadosFinales().contains(automataEntrada.getEstadoInicial())&&automataEntrada.getEstadosFinales().size()>1)
			expresionRegular="("+expresionRegular+")*";
		Iterator<EstadoString> itEst=estadosER.iterator();
		while(itEst.hasNext()){
			EstadoString e=itEst.next();
			if(!e.getNombreEstado().equals(inicial)){
				e.calculaExpresionEstado();
				String er=e.getExpresion();
				if ((er.length()>1&&er.contains("+")&&automataEntrada.getEstadosFinales().size()>1)
					&&((er.charAt(0)!='(')&&(er.charAt(er.length()-1)!=')'))) {
					er="("+er+")";
				}
				if(!expresionRegular.equals("")) expresionRegular+="+";
				expresionRegular+=er;
			}
		}
		xml+="<RExpr><item>"+expresionRegular+"</item></RExpr></steps>";
		controlador.trataMensaje(mensajero.devuelveMensaje("AFD_TO_ER.expresion",2)+expresionRegular);
		return null;
	}

	private boolean noEsta(ArrayList<EstadoString> estadosER,
			String trans) {
		// TODO Auto-generated method stub
		Iterator<EstadoString> itE=estadosER.iterator();
		while(itE.hasNext()){
			EstadoString e=itE.next();
			if(e.getNombreEstado().equals(trans)) return false;
		}
		return true;
	}

	private AutomataFD eliminaEstado(String estado,boolean muestraPasos,AutomataFD resulParc2) {
		// TODO Auto-generated method stub
		ArrayList<String> adyacentes=new ArrayList<String>();
		Iterator<String> italf=resulParc2.getAlfabeto().dameListaLetras().iterator();
		while(italf.hasNext()) {
			String letra=italf.next();
			String delta=resulParc2.funcion_delta(estado, letra);
			if (delta!=null&&(!adyacentes.contains(delta)&&!estado.equals(delta)))
				adyacentes.add(delta);
		}		
		ArrayList<String> precedentes=new ArrayList<String>();
		Iterator<String> itEstados=resulParc2.getEstados().iterator();
		while(itEstados.hasNext()) {
			String s=itEstados.next();
			ArrayList<String> aristasVertice=resulParc2.getAristasVertice(s);
			Iterator<String> itaristas=aristasVertice.iterator();
			while(itaristas.hasNext()) {
				String l1=itaristas.next();
				if (resulParc2.funcion_delta(s, l1).equals(estado)&&(!precedentes.contains(s)&&!estado.equals(s))) {
					precedentes.add(s);
				}
			}
				
		}
		
		Iterator<String> itadyacentes=adyacentes.iterator();
		while(itadyacentes.hasNext()) {
			String adyacente=itadyacentes.next();
			Iterator<String> itPrecedentes=precedentes.iterator();
			String est1=null;
			while(itPrecedentes.hasNext()) {
				String precedente=itPrecedentes.next();
				ArrayList<String> letra1=new ArrayList<String>();
				String est2;
				ArrayList<String> letra2=new ArrayList<String>();
				ArrayList<String> letra3=new ArrayList<String>();
				Iterator<String> iAr=resulParc2.getDestinos(precedente).iterator();
				while(iAr.hasNext()){
					est1=iAr.next();
					if(est1.equals(estado)) {
						letra1=resulParc2.getLetraTransicion(precedente,estado);
					}
				}
				iAr=resulParc2.getDestinos(estado).iterator();
				while(iAr.hasNext()){
				est2=iAr.next();
					if(est2.equals(adyacente)) {
						letra2=resulParc2.getLetraTransicion(estado,adyacente);
					}
					if(est2.equals(estado)){
						letra3=resulParc2.getLetraTransicion(estado,estado);
					}
					
				}
				Iterator<String> l1=letra1.iterator();
				while(l1.hasNext()){
					String let1=l1.next();
					if (let1.length()>1&&let1.contains("+")) {
						let1="("+let1+")";
					}
					Iterator<String> l2=letra2.iterator();
					while(l2.hasNext()){
						String let2=l2.next();
						if (let2.length()>1&&let2.contains("+")) {
							let2="("+let2+")";
						}
						if(letra3.size()==0){
							if (precedente.equals(estado)) {
								resulParc2.insertaAristaSobreescribe(adyacente,adyacente,"("+let1+")*");
							}
							else {
								resulParc2.insertaAristaSobreescribe(precedente,adyacente,let1+let2);
							}
						} else {
							Iterator<String> l3=letra3.iterator();
							while(l3.hasNext()){
								String let3=l3.next();
								if (let3.length()>1&&let3.contains("+")) {
									let3="("+let3+")";
								}
								resulParc2.insertaAristaSobreescribe(precedente,adyacente,let1+let3+"*"+let2);
							}
						}
					}
				}
			}
		}
		resulParc2.eliminaVertice(estado);
		return resulParc2;
		
	}

	
	
	public String getXML() {
		// TODO Auto-generated method stub
		return xml;
	}

	
	public void registraControlador(Controlador controlador) {
		// TODO Auto-generated method stub
		this.controlador=controlador;
	}
	
	public String getSalidaER() {
		return expresionRegular;
	}
	
	/**
	 * Clase que calcula la expresion regular de un estado a aprtir de
	 * la de su padre y teniendo en cuenta las transiciones que llegan y salen 
	 * de ñl mismo.
	 * @author Miguel Ballesteros, Josñ Antonio Blanes, Samer Nabhan
	 *
	 */
	public class EstadoString {
		private String estado;
		private String expresion;
		private EstadoString padre;
		
		/**
		 * Constructor de la clase con el nombre del estado y el padre
		 * que tiene en el grafo
		 * @param nombre del estado del que queremos calcular la expresiñn
		 * @param padre padre que le precede en el grafo
		 */
		public EstadoString(String nombre, EstadoString padre){
			estado=nombre;
			this.padre=padre;
		}
		
		/**
		 * Mñtoido accesor de la expresiñn totla del estado del objeto
		 * @return String con la expresiñn del estado
		 */
		public String getExpresion(){
			return expresion;
		}
		
		/**
		 * Mñtodo accesor del nombre del estado del objeto
		 * @return String con el nombre del estado
		 */
		public String getNombreEstado(){
			return estado;
		}
		
		/**
		 * Mñtodo que calcula la expresiñn del estado teniendo en cueta la del padre
		 * concatenada con la que los une y si este estado tiene transiciones de ñl
		 * a ñl mismo tambiñn.
		 */
		public void calculaExpresionEstado(){
			String erPadre=padre.getExpresion();
			String hijoER=new String();
			Iterator<String> l=resultadosParciales.getLetraTransicion(padre.getNombreEstado(),estado).iterator();
			while(l.hasNext()){
				String letra=l.next();
				if(!hijoER.equals("")) hijoER+="+";
				hijoER+=letra;
			}
			if (hijoER.length()>1&&hijoER.contains("+")
					&&((hijoER.charAt(0)!='(')&&(hijoER.charAt(hijoER.length()-1)!=')'))) {
				hijoER="("+hijoER+")";
			}
			l=resultadosParciales.getLetraTransicion(estado,estado).iterator();
			String mismo=new String();
			while(l.hasNext()){
				String letra=l.next();
				if(!mismo.equals("")) mismo+="+";
				mismo+=letra;
				
			}
			if (mismo.length()>1&&mismo.contains("+")) {
				mismo="("+mismo+")";
			}
			if(!mismo.equals("")){
				mismo+="*";
			}
			if(!mismo.equals(""))
				expresion=erPadre+hijoER+mismo;
			else
				expresion=erPadre+hijoER;
		}
		/**
		 * Mñtodo establece la expresiñn total del estado
		 * @param expresion expresiñn del estado
		 */
		public void setExpresion(String expresion){
			this.expresion=expresion;
		}
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

}
