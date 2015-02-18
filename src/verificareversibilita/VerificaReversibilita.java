/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verificareversibilita;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        double[][] p;
        BufferedReader in = new BufferedReader(new FileReader("cateneReversibili.txt"));
        BufferedWriter out = new BufferedWriter(new FileWriter("outputReversibili.txt"));
        int numeroCatene = Integer.valueOf(in.readLine());//leggo il primo valore che rappresenta il numero di catene
        int n = Integer.valueOf(in.readLine());//leggo il secondo valore che rappresenta il numero di nodi (tutte le cstene hanno lo stesso numero di nodi)
        boolean risultato;
        for (int i = 0; i < numeroCatene; i++) {
            p = leggiP(in, n);
            risultato = reversible(p);
            System.out.println(risultato);
            out.write(risultato + "");
            out.newLine();
        }
        out.close();
        in.close();
    }
    
    private static double[][] leggiP(BufferedReader in, int n) throws IOException {
        double[][] ret = new double[n][n];
        String[] riga;
        for (int i = 0; i < n; i++) {
            riga = in.readLine().split(",");
            for (int j = 0; j < n; j++) {
                ret[i][j] = Double.valueOf(riga[j]);
            }
        }
        return ret;
    }

}
