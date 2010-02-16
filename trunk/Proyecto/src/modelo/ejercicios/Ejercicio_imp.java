/**
 * 
 */
package modelo.ejercicios;
import modelo.*;
import modelo.algoritmos.AFNDLambda_to_AFND;
import modelo.algoritmos.AFN_to_AFD;
import modelo.algoritmos.Automatas_equivalentes;
import modelo.algoritmos.ERToAFNDLambda;
import modelo.automatas.Alfabeto;
import modelo.automatas.Automata;
import modelo.automatas.AutomataFD;
import modelo.automatas.AutomataFND;
import modelo.automatas.AutomataFNDLambda;
import modelo.expresion_regular.ArbolER;
import modelo.expresion_regular.ExpresionRegular;
import modelo.expresion_regular.ExpresionRegularImpl;

/**
 * Clase que implementa las funciones asociadas a los ejercicios
 * de la aplicaciñn asñ como a su correciñn.
 *  @author Miguel Ballesteros, Jose Antonio Blanes, Samer Nabhan
 *
 */
public class Ejercicio_imp implements Ejercicio{
	
	private String enunciado;
	private Object entrada;
	private Object resultado;
	private Alfabeto alf;
	private String tipo;
	private String ruta;

	/**
	 * Constructor que crea un ejercicio con los parñmetros que se le pasan
	 * @param enunciado enunciado del ejrcicio
	 * @param entrada entrada del usuario
	 * @param resultado soluciñn del ejercicio
	 * @param alf alfabeto del ejercicio
	 * @param tipo identificador de cadena de texto con el tipo del ejercicio:
	 * "Lenguaje" o "Automatas"
	 */
	public Ejercicio_imp(String enunciado, Object entrada, Object resultado, Alfabeto alf,String tipo,String ruta) {
		this.enunciado=enunciado;
		this.entrada=entrada;
		this.resultado=resultado;
		this.alf=alf;
		this.tipo=tipo;
		this.ruta=ruta;
	}
	
	
	
	public String getEnunciado() {
		// TODO Auto-generated method stub
		return enunciado;
	}

	
	public Object getEntrada() {
		// TODO Auto-generated method stub
		return entrada;
	}

	
	public Object getResultado() {
		// TODO Auto-generated method stub
		return resultado;
	}

	
	public boolean corregir(Object respuesta) throws AutomatasException {
		// TODO Auto-generated method stub
		if (respuesta instanceof String) {//ES UNA EXPRESION REGULAR!!!!
			ExpresionRegular er=null;
			try {
				//EN PRIMER LUGAR PASAMOS HASTA AFD LA ER QUE NOS DAN!
				er = new ExpresionRegularImpl(alf,(String)respuesta);
				ArbolER arbEr=er.getArbolER();
				Algoritmo algEr=new ERToAFNDLambda(arbEr,alf);
				Automata a1=algEr.ejecutar(false);
				//
				Algoritmo algAFNDlambda=new AFNDLambda_to_AFND(a1);
				Automata a2=algAFNDlambda.ejecutar(false);
				//
				Algoritmo algAFN_AFD=new AFN_to_AFD(a2);
				Automata a3=algAFN_AFD.ejecutar(false);
				//(YA TENEMOS EL AUTOMATA QUE VAMOS A COMPARAR PARA LA RESPUESTA QUE NOS DAN)
				////(ahora vamos a calcular el automata para el resultado que tenemos almacenado
				//que deberña ser un string! para poder compararlos despuñs
				if(resultado instanceof String ){
					er = new ExpresionRegularImpl(alf,(String)resultado);
					ArbolER arbErRes=er.getArbolER();
					algEr=new ERToAFNDLambda(arbErRes,alf);
					Automata a1Res=algEr.ejecutar(false);
					//
					algAFNDlambda=new AFNDLambda_to_AFND(a1Res);
					Automata a2Res=algAFNDlambda.ejecutar(false);
					//
					algAFN_AFD=new AFN_to_AFD(a2Res);
					Automata a3Res=algAFN_AFD.ejecutar(false);
					/////////////////////////////////////////////////////////////////////
					Automatas_equivalentes algEquivalencia=new Automatas_equivalentes();
					algEquivalencia.registraAutomata1(a3);
					algEquivalencia.registraAutomata2(a3Res);
					algEquivalencia.ejecutar(false);
					return algEquivalencia.getResultado();//devuelve el resultado de la correcion
				}
				if(resultado instanceof AutomataFNDLambda){
					algAFNDlambda=new AFNDLambda_to_AFND((Automata)resultado);
					Automata a2Res=algAFNDlambda.ejecutar(false);
					//
					algAFN_AFD=new AFN_to_AFD(a2Res);
					Automata a3Res=algAFN_AFD.ejecutar(false);
					/////////////////////////////////////////////////////////////////////
					Automatas_equivalentes algEquivalencia=new Automatas_equivalentes();
					algEquivalencia.registraAutomata1(a3);
					algEquivalencia.registraAutomata2(a3Res);
					algEquivalencia.ejecutar(false);
					return algEquivalencia.getResultado();//devuelve el resultado de la correcion
				}
				if(resultado instanceof AutomataFND){
					algAFN_AFD=new AFN_to_AFD((Automata)resultado);
					Automata a3Res=algAFN_AFD.ejecutar(false);
					/////////////////////////////////////////////////////////////////////
					Automatas_equivalentes algEquivalencia=new Automatas_equivalentes();
					algEquivalencia.registraAutomata1(a3);
					algEquivalencia.registraAutomata2(a3Res);
					algEquivalencia.ejecutar(false);
					return algEquivalencia.getResultado();//devuelve el resultado de la correcion
				}
				if(resultado instanceof AutomataFD){
					Automatas_equivalentes algEquivalencia=new Automatas_equivalentes();
					algEquivalencia.registraAutomata1(a3);
					algEquivalencia.registraAutomata2((Automata)resultado);
					algEquivalencia.ejecutar(false);
					return algEquivalencia.getResultado();//devuelve el resultado de la correcion
				}
			} catch (AutomatasException e) {
				// TODO Auto-generated catch block
				throw e;
			}
		}
		else {
			//EN PRIMER LUGAR PASAMOS HASTA AFD LA ER QUE NOS DAN!
			Automata a=null;
			if (!(resultado instanceof AutomataFD)) {
				if (resultado instanceof AutomataFNDLambda) {
					Automata res=(Automata) resultado;
					Algoritmo algAFNDlambda=new AFNDLambda_to_AFND(res);
					Automata a2=algAFNDlambda.ejecutar(false);
					//
					Algoritmo algAFN_AFD=new AFN_to_AFD(a2);
					a=algAFN_AFD.ejecutar(false);
				}
				else {
					if(resultado instanceof String){
						try {
							ExpresionRegular er = new ExpresionRegularImpl(alf,(String)resultado);
							ArbolER arbErRes=er.getArbolER();
							Algoritmo algEr=new ERToAFNDLambda(arbErRes,alf);
							Automata a1Res=algEr.ejecutar(false);
							//
							Algoritmo algAFNDlambda=new AFNDLambda_to_AFND(a1Res);
							Automata a2Res=algAFNDlambda.ejecutar(false);
							//
							Algoritmo algAFN_AFD=new AFN_to_AFD(a2Res);
							a=algAFN_AFD.ejecutar(false);
						} catch(AutomatasException e){
							throw e;
						}
					}else{
						Algoritmo algAFN_AFD=new AFN_to_AFD((Automata)resultado);
						a=algAFN_AFD.ejecutar(false);
					}
				}
			}
			
			//
			//(YA TENEMOS EL AUTOMATA QUE VAMOS A COMPARAR PARA LA RESPUESTA QUE NOS DAN)
			////(ahora vamos a calcular el automata para la respuesta que nos han dado
			//para poder compararlos despuñs
				
				if (!(respuesta instanceof AutomataFD)) {
					if (respuesta instanceof AutomataFNDLambda) {
						Automata res=(Automata) respuesta;
						Algoritmo algAFNDlambda=new AFNDLambda_to_AFND(res);
						Automata a2=algAFNDlambda.ejecutar(false);
						//
						Algoritmo algAFN_AFD=new AFN_to_AFD(a2);
						respuesta=algAFN_AFD.ejecutar(false);
					}
					else {
						Algoritmo algAFN_AFD=new AFN_to_AFD((Automata)respuesta);
						respuesta=algAFN_AFD.ejecutar(false);
					}
				
				}
				
			Automatas_equivalentes algEquivalencia=new Automatas_equivalentes();
			algEquivalencia.registraAutomata1((Automata)respuesta);
			if (a==null) 
				algEquivalencia.registraAutomata2((Automata) resultado);
			else 
				algEquivalencia.registraAutomata2(a);
			algEquivalencia.ejecutar(false);
			return algEquivalencia.getResultado();//devuelve el resultado de la correcciñn
		}
		return false;
	}
	
	public boolean corregirEquivalencia(boolean resultado) {
		return false;
		
	}

	
	public Alfabeto getAlfabeto() {
		// TODO Auto-generated method stub
		return alf;
	}
	
	
	public String toString() {
		if (entrada!=null)
			return enunciado.toString()+"\n"+entrada.toString()+"\n"+resultado.toString()+"\n";
		else return enunciado.toString()+"\n"+resultado.toString()+"\n";
	}


	
	public String getTipo() {
		// TODO Auto-generated method stub
		return tipo;
	}

	
	public String getRuta() {
		return ruta;
	}

	
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

}
