import java.util.*;
import java.util.concurrent.*;

class Node extends Thread {
    private String id;
    private List<Node> dependents;
    private CountDownLatch latch;

    public Node(String id) {
        this.id = id;
        this.dependents = new ArrayList<>();
        this.latch = new CountDownLatch(1);
    }

    public void addDependent(Node node) {
        dependents.add(node);
    }

    public void run() {
        try {
            System.out.println("Thread " + id + " started");
            perform();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread " + id + " finished");
        latch.countDown();
        for (Node dependent : dependents) {
            dependent.latch.countDown();
        }
    }

    private void perform() throws InterruptedException {
        Random random = new Random();
        int sleepTime = random.nextInt(2000);
        Thread.sleep(sleepTime);
    }
}

public class Threads {
    public static void main(String[] args) {
        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");
        Node E = new Node("E");
        Node F = new Node("F");
        Node G = new Node("G");

        A.addDependent(E);
        B.addDependent(E);
        C.addDependent(E);
        D.addDependent(E);
        A.addDependent(B);
        D.addDependent(C);

        List<Node> nodes = Arrays.asList(A, B, C, D, E, F, G);

        for (Node node : nodes) {
            node.start();
        }

        for (Node node : nodes) {
            try {
                node.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
