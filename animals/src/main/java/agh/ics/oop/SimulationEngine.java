package agh.ics.oop;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final ArrayList<Simulation> simulations;
    private final ArrayList<Thread> threads = new ArrayList<>();
    private ExecutorService service = Executors.newFixedThreadPool(4);

    public SimulationEngine(ArrayList<Simulation> simulations) {
        this.simulations = simulations;
    }


    public void runSync() {
        for (Simulation simulation : this.simulations) {
            simulation.run();
        }
    }

    public void runAsync() {
        for (Simulation simulation : this.simulations) {
            Thread thread = new Thread(simulation);
            threads.add(thread);
            thread.start();
        }
    }

    public void awaitSimulationsEnd() throws InterruptedException {

        for (Thread thread : this.threads) {
            thread.join();
        }

        service.shutdown();
        if (!service.awaitTermination(10, TimeUnit.SECONDS)) {
            service.shutdownNow();
        }
        this.service = Executors.newFixedThreadPool(4);
    }


    public void runAsyncInThreadPool() {
        for (Simulation simulation : this.simulations) {
            service.submit(simulation);
        }
    }
}


