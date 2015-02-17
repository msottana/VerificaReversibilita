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
        for (int i = 0; bool && i < adj.length; i++) {
            int v = adj[i];
            if (p[v][u] == 0.0) {
                bool = false;
            } else {
                if (parent[u] != v && color[v] == GREY
                        && p[v][u] * invprob[u] * prob[v] != p[u][v] * prob[u] * invprob[v]) {
                    bool = false;
                }
                if (color[v] == WHITE) {
                    color[v] = GREY;
                    parent[v] = u;
                    prob[v] = prob[u] * p[u][v];
                    invprob[v] = invprob[u] * p[u][v];
                    bool = bool && dfsReversible(p, v, parent, color, prob, invprob);
                }
            }
        }
        color[u] = BLACK;
        return bool;
    }

    public static boolean naiveReversible(double[][] p) {
        int v = p.length;
        int color[] = new int[v];
        double x[] = new double[v];
        for (int u = 0; u < v; u++) {
            color[u] = WHITE;
            x[u] = Double.MAX_VALUE;
        }
        x[0] = 1;
        return dfsNaiveReversible(p, 0, x, color);
    }

    private static boolean dfsNaiveReversible(double[][] p, int u, double[] x, int[] color) {
        boolean bool = true;
        int[] adj = adiacenti(u, p);
        for (int i = 0; bool && i < adj.length; i++) {
            int v = adj[i];
            if (p[v][u] == 0.0) {
                bool = false;
            } else {
                if (color[v] != WHITE && (int)(x[u]*p[u][v]*1000000000) != (int)(x[v]*p[v][u]*1000000000)) {
                    bool = false;
                }
                if (color[v] == WHITE) {
                    color[v] = GREY;
                    x[v] = x[u]*(p[u][v]/p[v][u]);
                    bool = bool && dfsNaiveReversible(p, v, x, color);
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
        for (int i = 0; i < p.length; i++) {
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
        double p[][] = {{0.0000000000000000, 0.2186152825203730, 0.2666448082270247, 0.0000000000000000, 0.6817357746198648, 0.1538750025740410, 0.2835591194496269, 0.3491790422110761, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000},
        {0.2303984503664523, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000},
        {0.7928912403782181, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000},
        {0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.8112626292523940, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.1364787243627471, 0.0000000000000000, 0.7701323490141594},
        {0.7806691829112921, 0.0000000000000000, 0.0000000000000000, 0.5864014909540195, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 1.0349857714361324, 0.0000000000000000},
        {0.1772317134606284, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000},
        {0.6153599329594659, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000},
        {0.9517477219345882, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000},
        {0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.1416365570022561, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000},
        {0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.8815958846491947, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000},
        {0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.7916210173041193, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000}};
        /**/
        System.out.println(reversible(p));
        System.out.println(naiveReversible(p));
    }

}
