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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.BitSet;

class pair < X, Y > {
	X _first;
	Y _second;
	
	public pair(X f, Y s) { _first = f; _second = s; }
	
	X first() { return _first; }
	Y second() { return _second; }
	
	void setFirst(X f) { _first = f; }
	void setSecond(Y s) { _second = s; }
}

public class PageRank {

    private static final String NOMBRE_ARCHIVO = "dataset.txt";
    private static final double PROBABILIDAD_TELEPORTACION = 0.85;
    private static final int MAX_NODOS = 100000;
    private static final int ITERACIONES_METODO_POTENCIA = 4;
    private static int NUMERO_DE_NODOS = 875713;
    

    public static void main(String[] args) throws Exception {
        Vector< Vector<Integer> > vectorAristasNodo = new Vector< Vector<Integer> >(MAX_NODOS);
        Vector< Vector< pair< Integer , Double > > > vectorMatrizA = new Vector<Vector< pair< Integer,Double >>>(MAX_NODOS);
        BitSet registroNumeros = new BitSet(MAX_NODOS);
        registroNumeros.clear();
        
        for (int i = 0; i < MAX_NODOS; i++){
        	Vector< pair< Integer , Double > > NodoMatrizA = new Vector< pair< Integer , Double > >();
        	Vector<Integer> NodoMatrizQ = new Vector<Integer>();
        	vectorAristasNodo.add(NodoMatrizQ);
        	vectorMatrizA.add(NodoMatrizA);
        }

        FileReader in = new FileReader(NOMBRE_ARCHIVO);
        BufferedReader br = new BufferedReader(in);

        String line = null;
        while ((line = br.readLine()) != null) {
            String[] splits = line.split(" ");

            int x = Integer.parseInt(splits[0]);
            int y = Integer.parseInt(splits[1]);
            vectorAristasNodo.get(x).add(y);

            registroNumeros.set(x, true);
            registroNumeros.set(y, true);
        }
        br.close();

        NUMERO_DE_NODOS = 0;
        for (int i = 0; i < MAX_NODOS; i++)
        	if (registroNumeros.get(i) == true)
        		NUMERO_DE_NODOS++;
        registroNumeros = null;
        System.out.println("Nodos: " + NUMERO_DE_NODOS);

        double[][] matrizQ = new double[NUMERO_DE_NODOS][NUMERO_DE_NODOS];
        double[][] matrizA = new double[NUMERO_DE_NODOS][NUMERO_DE_NODOS];
        for (int i = 0; i < NUMERO_DE_NODOS; i++) {
            for (int j = 0; j < NUMERO_DE_NODOS; j++) {
                matrizQ[i][j] = 0;
                matrizA[i][j] = 0;
            }
        }
        
        for (int i = 0; i < MAX_NODOS; i++){
        	if(vectorAristasNodo.get(i).size() < 1) continue;
            Vector<Integer> lista = vectorAristasNodo.get(i);
            int numeroAristas = lista.size();
            Iterator<Integer> iterador = lista.iterator();
            while(iterador.hasNext()) {
            	int elemento = iterador.next();
                matrizQ[elemento - 1][i - 1] = 1;
                pair <Integer,Double> parejaAdiccionar = new pair< Integer , Double >(i,(PROBABILIDAD_TELEPORTACION / numeroAristas) + ((1 - PROBABILIDAD_TELEPORTACION) / NUMERO_DE_NODOS));
                vectorMatrizA.get(elemento).add(parejaAdiccionar);
                /*if (mapMatrizA.containsKey(elemento)) {
//					 if(mapMatrizA.get(i).containsKey(key)){
//						 mapMatrizA.get(i).put(key, beetaTimesM + oneMinBetaOverN);
//					 }else{
//						 Map<Integer, Double> m = new HashMap<>();
//						 m.put(key, beetaTimesM + oneMinBetaOverN);
//						 mapMatrizA.put(i, m);
//					 }
                    // A = BM + (1-B)/N
                    mapMatrizA.get(elemento).put(i, (PROBABILIDAD_TELEPORTACION / numeroAristas) + ((1 - PROBABILIDAD_TELEPORTACION) / NUMERO_DE_NODOS));
                } else {
                    Map<Integer, Double> m = new HashMap<>();
                    // A = BM + (1-B)/N
                    m.put(i, (PROBABILIDAD_TELEPORTACION / numeroAristas) + (1 - PROBABILIDAD_TELEPORTACION) / NUMERO_DE_NODOS);
                    mapMatrizA.put(elemento, m);
                }*/
            }
        }

        System.out.println("Matrix Q: ");

        for (int i = 0; i < matrizQ.length; i++) {
            for (int j = 0; j < matrizQ[i].length; j++) {
                System.out.print(matrizQ[i][j] + "  ");
            }
            System.out.println();
        }

        System.out.println("Mapeo Matrix A: " + vectorMatrizA);

        double[] arrayR = new double[NUMERO_DE_NODOS];
        Arrays.fill(arrayR, (1.0 / NUMERO_DE_NODOS));
        //NORMALIZANDO EL VECTOR R

        double[] vectorRanking = new double[NUMERO_DE_NODOS];

        for (int i = 0; i < ITERACIONES_METODO_POTENCIA; i++) {
            // MULTIPLICANDO A x R (METODO POTENCIA)
            System.out.println("_________________________________");
            System.out.println("Iteracion Metodo Potencia: " + (i + 1));
            //System.out.println("Iteracion Metodo Potencia: " + (i + 1));
            for (int k = 0; k < arrayR.length; k++) {
                Vector< pair<Integer,Double> > m = new Vector< pair<Integer,Double> >();
                if (vectorMatrizA.contains(k)) {
                    m = vectorMatrizA.get(k);
                }

                double rank = 0;
                for (int j = 0; j < arrayR.length; j++) {
                    double elementoMatriz = 0;
                    if (!m.contains(j)) {
                        elementoMatriz =  (1 - PROBABILIDAD_TELEPORTACION) / NUMERO_DE_NODOS; // 0 + (1-B)/N
                    System.out.println(elementoMatriz);
                    } else {
                        elementoMatriz = m.get(j).second();
                        matrizA[k - 1][j - 1] = m.get(j).second();
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
                System.out.print(String.format( "%.2f", matrizA[i][j] ) + "  ");
            }
            System.out.println();
        }

        System.out.println("Vector Ranking: ");

        for (int i = 0; i < NUMERO_DE_NODOS; i++) {
            System.out.println("Pagina " + (i + 1) + " :" + vectorRanking[i]);
        } 
    }
}