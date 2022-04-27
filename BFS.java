import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

public class BFS {

    public static int[][] setUpGraph(Scanner stdin, int n) {
        int[][] adj = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adj[i][j] = stdin.nextInt() == 1 ? 1 : 0;
            }
        }
        //System.out.println("graph made");
        return adj;

    }
    public static void main(String[] args) throws FileNotFoundException{
        Scanner stdin = new Scanner(new File("graph4.txt"));
        int n = stdin.nextInt();
        int[][] graph = setUpGraph(stdin, n);
        long startTime = System.nanoTime();
        bfs(graph, 0);
        long stop = System.nanoTime();
        System.out.println((stop - startTime) + " nanoseconds");
    }

    public static void bfs (int[][] graph, int source) {
        boolean[] visited = new boolean[5001];

        Queue<Integer> q = new LinkedList<>();

        q.add(source);
        visited[source] = true;

        while (q.peek() != null) {
            int curr = q.remove();
            
            for (int i = 0; i < graph.length; i++ ) {
                if (!visited[i] && graph[curr][i] == 1) {
                    visited[i] = true;
                    q.add(i);
                }
            }
        }
    }
} 

