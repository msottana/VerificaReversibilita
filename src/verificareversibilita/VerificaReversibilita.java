/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verificareversibilita;

/**
 *
 * @author Matteo & Giulia
 */
public class VerificaReversibilita {
    
    public static int WHITE = 0;
    public static int BLACK = 1;
    public static int GREY = 2;
    public static int NIL = -1;
    
    
    
    /* v rappresenta la sequenza di nodi, i nodi sono etichettati con numeri compresi in [0,v[
     * p è la matrice di archi
     */
    public static boolean reversible(double[][] p) {
        int v = p.length;
        int color[] = new int[v];
        int parent[] = new int[v];
        double prob[] = new double[v];
        double invprob[] = new double[v];
        for (int u = 0; u < v; u++) {
            color[u] = WHITE;
            parent[u] = NIL;
            prob[u] = 1;
            invprob[u] = 1;
        }
        return dfsReversible(p, 0, parent, color, prob, invprob);
    }

    private static boolean dfsReversible(double[][] p, int u, int[] parent, int[] color, double[] prob, double[] invprob) {
        boolean bool = true;
        int[] adj = adiacenti(u, p);
        for(int v = 0; bool && v < adj.length; v++) {
            if (p[v][u] == 0.0) {
                bool = false;
            } else {
                if (parent[u] != v && color[v] == GREY && 
                        p[v][u]*invprob[u]*prob[v] != p[u][v]*prob[u]*invprob[v]) {
                    bool = false;
                }
                if (color[v] == WHITE) {
                    color[v] = GREY;
                    parent[v] = u;
                    prob[v] = prob[u]*p[u][v];
                    invprob[v] = invprob[u]*p[u][v];
                    bool = bool && dfsReversible(p, v, parent, color, prob, invprob);
                }
            }
        }
        color[u] = BLACK;
        return bool;
    }
    
    private static int[] adiacenti(int u, double[][] p) {
        int n = 0;
        int[] vet = new int[p.length];
        int[] ret;
        for(int i = 0; i < p.length; i++) {
            if (p[u][i] != 0) {
                vet[n] = i;
                n++;
            }
        }
        ret = new int[n];
        for (int i = 0; i < n; i++) {
            ret[i] = vet[i];
        }
        return ret;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
   
}
