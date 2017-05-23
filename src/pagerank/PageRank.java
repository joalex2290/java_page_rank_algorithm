/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PageRank {

	private static final String NOMBRE_ARCHIVO = "dataset.txt";
	private static final double PROBABILIDAD_TELEPORTACION = 0.85;
	private static final int ITERACIONES_METODO_POTENCIA = 4;
	private static int NUMERO_DE_NODOS = 875713;
	
	public static void main(String[] args) throws Exception {
		Map<Integer,List<Integer>> conexionNodos = new HashMap<>();
		Set<Integer> rastreoNodos = new HashSet<>();
		
		FileReader in = new FileReader(NOMBRE_ARCHIVO);
		BufferedReader br = new BufferedReader(in);
		
		String line = null;
		while((line = br.readLine()) != null){
			String[] splits = line.split("\t");
			
			int x = Integer.parseInt(splits[0]);
			int y = Integer.parseInt(splits[1]);
			
			if(conexionNodos.containsKey(x)){
				conexionNodos.get(x).add(y);
			}else{
				List<Integer> l = new ArrayList<>();
				l.add(y);
				conexionNodos.put(x, l);
			}
			
			rastreoNodos.add(x);
			rastreoNodos.add(y);
		}
		br.close();
		
		NUMERO_DE_NODOS = rastreoNodos.size();
		rastreoNodos = null;
		System.out.println("Nodos: " + NUMERO_DE_NODOS);
		
		Map<Integer, Map<Integer,Double>> matrizQ = new HashMap<>();
		
		for(Integer i : conexionNodos.keySet()){
			List<Integer> list = conexionNodos.get(i);
			int numeroEnlaces = list.size();
                        
			for(Integer j : list){
				 if(matrizQ.containsKey(j)){
//					 if(matrizQ.get(i).containsKey(key)){
//						 matrizQ.get(i).put(key, beetaTimesM + oneMinBetaOverN);
//					 }else{
//						 Map<Integer, Double> m = new HashMap<>();
//						 m.put(key, beetaTimesM + oneMinBetaOverN);
//						 matrizQ.put(i, m);
//					 }
					 // A = BM + (1-B)/N
					 matrizQ.get(j).put(i, (PROBABILIDAD_TELEPORTACION/numeroEnlaces) + ((1 - PROBABILIDAD_TELEPORTACION)/NUMERO_DE_NODOS));
					 
				 }else{
					 Map<Integer, Double> m = new HashMap<>();
                                         // A = BM + (1-B)/N
					 m.put(i, (PROBABILIDAD_TELEPORTACION/numeroEnlaces) + (1 - PROBABILIDAD_TELEPORTACION)/NUMERO_DE_NODOS);
					 matrizQ.put(j, m);
				 }
			}			
		}
		
		System.out.println("Matrix A: " + matrizQ);
		conexionNodos = null;
		
		double[] arrayR = new double[NUMERO_DE_NODOS];
		Arrays.fill(arrayR, (1.0/NUMERO_DE_NODOS)); 
		//NORMALIZANDO EL VECTOR R
                
		double[] vectorRanking = new double[NUMERO_DE_NODOS];
		
		for (int iteration = 0; iteration < ITERACIONES_METODO_POTENCIA; iteration++) {
			// MULTIPLICANDO A x R (METODO POTENCIA)
			System.out.println("Iteracion Metodo Potencia: " + (iteration + 1));
			for (int k = 0; k < arrayR.length; k++) {
				Map<Integer, Double> m = new HashMap<>();
				if (matrizQ.containsKey(k)) {
					m = matrizQ.get(k);
				}

				double rank = 0;
				for (int j = 0; j < arrayR.length; j++) {
					double element = 0;
					if (!m.containsKey(j)) {
						element = (1.0/NUMERO_DE_NODOS) * (1 - PROBABILIDAD_TELEPORTACION);
					} else {
						element = m.get(j);
					}
					rank += element * arrayR[j];
				}
				vectorRanking[k] = rank;
				rank = 0;
                                System.out.println(vectorRanking[k]);
			}
			
			arrayR = vectorRanking;
		}

		System.out.println("Vector Ranking: ");
                
                for(int i = 0;i<NUMERO_DE_NODOS;i++){
                    System.out.println("Pagina " + (i+1) + " :" + vectorRanking[i]);
                }	
	}

}
