/*
    # PSEUDOCODIGO #

    Algoritmo PageRank

    teleportacion = 0.85;
    iteraciones = 4;
    matrizP(nodos,nodos) = matriz binaria leida de fichero;
    nodos = longitud matrizP;
    matrizA(nodos,nodos) = matriz de ceros;
    numeroAristas(nodos) = arreglo de suma de las columnas;

    // Link rank y Random Walker en una sola iteracion. complejidad O(NxN)
    For i = 0 to nodos
            For j = 0 to nodos
                    matrizA(i,j) = teleportacion * ( matrizP(i,j) / numeroAristas(i)) + ((1-teleportacion)/nodos);
            End For
    End For

    arregloR(nodos) = 1/nodos  en cada posicion;
    arregloRanking(nodos) = ceros en cada posicion;

    //Iterando Power method A*R = yR. Complejidad O(NxN)
    For i = 0 to iteraciones
    print "Iteracion " + i
            For j = 0 to nodos
                    For k = 0 to nodos
                            arregloRanking(j) = arregloRanking(j) + (matrizA(k,j) * arregloR(j));
                    End For
            End For
    End For

    Print arregloRanking;
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
        Map<Integer, List<Integer>> mapAristasNodo = new HashMap<>();
        Set<Integer> setNodos = new HashSet<>();

        FileReader in = new FileReader(NOMBRE_ARCHIVO);
        BufferedReader br = new BufferedReader(in);

        String line = null;
        while ((line = br.readLine()) != null) {
            String[] splits = line.split("\t");

            int x = Integer.parseInt(splits[0]);
            int y = Integer.parseInt(splits[1]);

            if (mapAristasNodo.containsKey(x)) {
                mapAristasNodo.get(x).add(y);
            } else {
                List<Integer> l = new ArrayList<>();
                l.add(y);
                mapAristasNodo.put(x, l);
            }

            setNodos.add(x);
            setNodos.add(y);
        }
        br.close();

        NUMERO_DE_NODOS = setNodos.size();
        setNodos = null;
        System.out.println("Nodos: " + NUMERO_DE_NODOS);

        Map<Integer, Map<Integer, Double>> mapMatrizA = new HashMap<>();

        double[][] matrizQ = new double[NUMERO_DE_NODOS][NUMERO_DE_NODOS];
        double[][] matrizA = new double[NUMERO_DE_NODOS][NUMERO_DE_NODOS];
        for (int i = 0; i < NUMERO_DE_NODOS; i++) {
            for (int j = 0; j < NUMERO_DE_NODOS; j++) {
                matrizQ[i][j] = 0;
                matrizA[i][j] = 0;
            }
        }

        for (Integer i : mapAristasNodo.keySet()) {
            List<Integer> list = mapAristasNodo.get(i);
            int numeroAristas = list.size();

            for (Integer j : list) {
                matrizQ[j - 1][i - 1] = 1;
                if (mapMatrizA.containsKey(j)) {
//					 if(mapMatrizA.get(i).containsKey(key)){
//						 mapMatrizA.get(i).put(key, beetaTimesM + oneMinBetaOverN);
//					 }else{
//						 Map<Integer, Double> m = new HashMap<>();
//						 m.put(key, beetaTimesM + oneMinBetaOverN);
//						 mapMatrizA.put(i, m);
//					 }
                    // A = BM + (1-B)/N
                    mapMatrizA.get(j).put(i, (PROBABILIDAD_TELEPORTACION / numeroAristas) + ((1 - PROBABILIDAD_TELEPORTACION) / NUMERO_DE_NODOS));
                } else {
                    Map<Integer, Double> m = new HashMap<>();
                    // A = BM + (1-B)/N
                    m.put(i, (PROBABILIDAD_TELEPORTACION / numeroAristas) + (1 - PROBABILIDAD_TELEPORTACION) / NUMERO_DE_NODOS);
                    mapMatrizA.put(j, m);
                }
            }
        }

        System.out.println("Matrix Q: ");

        for (int i = 0; i < matrizQ.length; i++) {
            for (int j = 0; j < matrizQ[i].length; j++) {
                System.out.print(matrizQ[i][j] + "  ");
            }
            System.out.println();
        }

        System.out.println("Mapeo Matrix A: " + mapMatrizA);
        mapAristasNodo = null;

        double[] arrayR = new double[NUMERO_DE_NODOS];
        Arrays.fill(arrayR, (1.0 / NUMERO_DE_NODOS));
        //NORMALIZANDO EL VECTOR R

        double[] vectorRanking = new double[NUMERO_DE_NODOS];

        for (int iteration = 0; iteration < ITERACIONES_METODO_POTENCIA; iteration++) {
            // MULTIPLICANDO A x R (METODO POTENCIA)
            System.out.println("_________________________________");
            System.out.println("Iteracion Metodo Potencia: " + (iteration + 1));
            for (int k = 0; k < arrayR.length; k++) {
                Map<Integer, Double> m = new HashMap<>();
                if (mapMatrizA.containsKey(k)) {
                    m = mapMatrizA.get(k);

                }

                double rank = 0;
                for (int j = 0; j < arrayR.length; j++) {
                    double elementoMatriz = 0;
                    if (!m.containsKey(j)) {
                        elementoMatriz = (1.0 / NUMERO_DE_NODOS) * (1 - PROBABILIDAD_TELEPORTACION);
                    } else {
                        elementoMatriz = m.get(j);
                        matrizA[k - 1][j - 1] = m.get(j);
                    }
                    rank += elementoMatriz * arrayR[j];
                }
                vectorRanking[k] = rank;
                rank = 0;
                System.out.println(vectorRanking[k]);
            }

            arrayR = vectorRanking;
        }

        System.out.println("Matrix A: ");

        for (int i = 0; i < matrizQ.length; i++) {
            for (int j = 0; j < matrizA[i].length; j++) {
             //   System.out.print(String.format( "%.2f", matrizA[i][j] ) + "  ");
            }
           // System.out.println();
        }

        System.out.println("Vector Ranking: ");

        for (int i = 0; i < NUMERO_DE_NODOS; i++) {
            System.out.println("Pagina " + (i + 1) + " :" + vectorRanking[i]);
        }
    }

}
