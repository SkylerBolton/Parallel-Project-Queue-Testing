import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CountDownLatch;

public class FineBFS {

    public static boolean[] visited = new boolean[5001];
    public static FineGrainConcurrentQueue fg = new FineGrainConcurrentQueue();
    static int[][] graph;
    public static CountDownLatch cd = new CountDownLatch(2);

    public static int[][] setUpGraph(Scanner stdin, int n) {
        int[][] adj = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adj[i][j] = stdin.nextInt() == 1 ? 1 : 0;
            }
        }
        return adj;
    }

    public static void main(String[] args) throws FileNotFoundException{
        Scanner stdin = new Scanner(new File("graph3.txt"));
        int n = stdin.nextInt();
        graph = setUpGraph(stdin, n);


        // Testing done on a dual-core system.    
		Thread thread1 = new Thread(new Thready(fg, 0));
        Thread thread2 = new Thread(new Thready(fg, 500));

        long startTime = System.nanoTime();

        thread1.start();
        thread2.start();
        try {
            cd.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long stop = System.nanoTime();
        System.out.println((stop - startTime) + " nanoseconds");
    }
} 
class Thready implements Runnable
{
    
    int source;
    public Thready(FineGrainConcurrentQueue f, int source)
    {
        this.source = source;
    }
	@Override
	public void run()
	{
        bfs(source);
	}

    public static void bfs (int source) {

        FineBFS.fg.enq(source);
        FineBFS.visited[source] = true;

        while (FineBFS.fg.isEmpty() != true) { 
            int curr = FineBFS.fg.deq();
            for (int i = 0; i < FineBFS.graph.length; i++ ) {
                if (!FineBFS.visited[i] && FineBFS.graph[curr][i] == 1) {
                    FineBFS.visited[i] = true;
                    FineBFS.fg.enq(i);
                }
            }
        }
        FineBFS.cd.countDown();
    }
} 
class FineGrainConcurrentQueue{
 
    Node head, tail;
    static private ReentrantLock enqLock;
    static private ReentrantLock deqLock;
   
    FineGrainConcurrentQueue() {
        enqLock = new ReentrantLock();
        deqLock = new ReentrantLock();
        head = tail = new Node(null);
    }
   
    void enq(Integer item) {
        //System.out.println("enqueuing");
        enqLock.lock();
        try {
            tail.next = new Node(item);
            tail = tail.next;
        } finally {
            enqLock.unlock();
        }
    }
   
    Integer deq() {
        //System.out.println("dequeuing");
        deqLock.lock();
        Integer res = 0;
        try {
            if (head == null) {
                //throw new EmptyStackException();
                return res;
            }
            if (head.next == null) {
                //throw new EmptyStackException();
                head = head.next;
                return res;
            }
            else 
            {
                res = head.next.item;
                head = head.next;
            }
        } finally {
            deqLock.unlock();
        }
        return res;
    }

    boolean isEmpty() {
        if (head == null) {
            return true;
        }
        return false;
    }
}

class Node{
    Node next;
    Integer item;
   
    Node(Integer item) {
        this.item = item;
    }
}
