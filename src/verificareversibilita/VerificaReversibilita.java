/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verificareversibilita;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

    /*
     * the variable v represents the sequence of nodes, each node is tagged with
     * an integer value from the range [0, v)
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
        int[] adj = adjacent(u, p);
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
        int[] adj = adjacent(u, p);
        for (int i = 0; bool && i < adj.length; i++) {
            int v = adj[i];
            if (p[v][u] == 0.0) {
                bool = false;
            } else {
                //We use an aproximation to reduce the precision of the calculations
                if (color[v] != WHITE && (x[u]*p[u][v]) - (x[v]*p[v][u]) > 0.0000000001) {
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

    private static int[] adjacent(int u, double[][] p) {
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
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        double[][] p;
        BufferedReader in = new BufferedReader(new FileReader("inputReversible.txt"));
        BufferedWriter out = new BufferedWriter(new FileWriter("outputReversible.txt"));
        BufferedWriter outNaive = new BufferedWriter(new FileWriter("outputNaiveReversible.txt"));
        int numberOfChains = Integer.valueOf(in.readLine());//read the number of chains in the files
        int n = Integer.valueOf(in.readLine());//read the number of vertices in the chain
        //all chains have the same number of vertices
        boolean result;
        for (int i = 0; i < numberOfChains; i++) {
            p = readProbabilityMatrix(in, n);
            result = reversible(p);
            System.out.print("normal: " + result + " / naive: ");
            out.write(result + "");
            out.newLine();
            result = naiveReversible(p);
            System.out.println(result);
            outNaive.write(result + "");
        }
        out.close();
        outNaive.close();
        in.close();
    }
    
    private static double[][] readProbabilityMatrix(BufferedReader in, int n) throws IOException {
        double[][] ret = new double[n][n];
        String[] row;
        for (int i = 0; i < n; i++) {
            row = in.readLine().split(",");
            for (int j = 0; j < n; j++) {
                ret[i][j] = Double.valueOf(row[j]);
            }
        }
        return ret;
    }

}
