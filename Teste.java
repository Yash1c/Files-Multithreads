import java.util.ArrayList;


public class Teste {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<Integer> fila = new ArrayList<>();
        Produtor produtor = new Produtor(fila);
        Consumidor consumidor = new Consumidor(fila);

        produtor.start();
        consumidor.start();

        produtor.join();
        consumidor.join();

        System.out.println("Fila final: " + fila);
    }
}

class Consumidor extends Thread {
    private ArrayList<Integer> fila;
    public Consumidor(ArrayList<Integer> fila) {
        this.fila = fila;
    }

    public void run(){
        synchronized (fila) {
            System.out.println("Consumidor iniciado.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);

            }
            System.out.println("Consumidor terminou.");
            fila.remove(0);
        }
    }
}

class Produtor extends Thread {
    private ArrayList<Integer> fila;
    public Produtor(ArrayList<Integer> fila) {
        this.fila = fila;
    }

    public void run(){
        synchronized (fila) {
            System.out.println("Produtor iniciado.");
            this.fila.notify();
            for (int i = 0; i < 5; i++) {
                fila.add(i);
                System.out.println("Produtor produziu: " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Produtor terminou.");
        }
    }
}