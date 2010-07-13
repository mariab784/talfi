package modelo.algoritmos;

import java.util.ArrayList;

import modelo.automatas.MaquinaTuring;

public class TuringResultado {

	private ArrayList<String> listaPal;
	private ArrayList<String> listaCintaPal;
	private ArrayList<String> listaPalNo;
	private ArrayList<String> listaCintaPalNo;
	private ArrayList<String> listaBucle;
	private ArrayList<Boolean> resultPal;
	private ArrayList<Boolean> resultPalNo;
	private ArrayList<Boolean> resultBucle;
	private boolean result;
	private boolean tieneFinales;
	private AceptaTuring aceptaTuring;
	

	public TuringResultado(ArrayList<String> listaPal,ArrayList<String> listaCintaPal,
			ArrayList<String> listaPalNo,ArrayList<String> listaCintaPalNo,ArrayList<String> listaBucle,
			MaquinaTuring mt){
		resultPal = new ArrayList<Boolean>();
		resultPalNo = new ArrayList<Boolean>();
		resultBucle = new ArrayList<Boolean>();
		this.listaPal = listaPal;
		this.listaCintaPal = listaCintaPal;
		this.listaPalNo = listaPalNo;
		this.listaCintaPalNo = listaCintaPalNo;
		this.listaBucle = listaBucle;
		ArrayList<String> fin = mt.getEstadosFinales();

		tieneFinales = !(fin.isEmpty() || fin == null);
		aceptaTuring = new AceptaTuring(mt);
		construyeResultado();
	}
	
	public boolean getResult() {return result;}
	
	private void construyeResultado(){
	
		boolean r1 = true;
		boolean r2 = true;
		boolean r3 = true;
		int tam = 0;
		boolean b = true;
		
		if(listaPal != null){
		tam = listaPal.size();

		for(int i = 0; i < tam; i++){

			String s = listaPal.get(i);
			if(tieneFinales){
				b =  (aceptaTuring.simulaEjecucion(s,null) == 1);
			}
			else{
				b =  (aceptaTuring.simulaEjecucion(s,listaCintaPal.get(i)) == 1);
			}
			resultPal.add(b);			
			r1 = b&&r1;
			}
		}
		
		if(listaPalNo != null){
		tam = listaPalNo.size();
		
		for(int i = 0; i < tam; i++){

			String s = listaPal.get(i);
			if(tieneFinales){				
				b =  (aceptaTuring.simulaEjecucion(s,null) == 2);
			}
			else{
				b =  (aceptaTuring.simulaEjecucion(s,listaCintaPalNo.get(i)) == 2);
			}
			resultPalNo.add(b);
			
			r2 = b&&r2;
		}
		}
		
		if(listaBucle != null){
		tam = listaBucle.size();


		for(int i = 0; i < tam; i++){			
			String s = listaPal.get(i);	
			b =  (aceptaTuring.simulaEjecucion(s,null) == 2);
			resultBucle.add(b);
			r3 = b&&r3;
		}
		}
		result = r1&&r2&&r3;
	}
}
