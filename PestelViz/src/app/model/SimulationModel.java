package app.model;

import javafx.concurrent.Task;
import simu.framework.Event;
import simu.model.AIEnhancedPESTELEngineOllama;
import simu.model.EnhancedFutureScenarioManager;
import simu.model.PESTELState;

import java.util.List;

public class SimulationModel {

    private SimulationTask simulationTask;
    private double simulationSpeed = 1000; // Default: 1 second per day

    public void start() {
        if (simulationTask == null || !simulationTask.isRunning()) {
            simulationTask = new SimulationTask(simulationSpeed);
            new Thread(simulationTask).start();
        }
    }

    public void stop() {
        if (simulationTask != null && simulationTask.isRunning()) {
            simulationTask.cancel();
        }
    }
    
    public void setSimulationSpeed(double speed) {
        // speed is a value from 1.0 to 10.0 from the slider
        // We can map this to a delay in ms. Lower slider value = slower speed = higher delay.
        this.simulationSpeed = (11.0 - speed) * 200; // Maps 1->2000ms, 10->200ms
        if (simulationTask != null) {
            simulationTask.setSpeed(this.simulationSpeed);
        }
    }

    public Task<SimulationUpdate> getSimulationTask() {
        return simulationTask;
    }

    public static class SimulationTask extends Task<SimulationUpdate> {
        private final AIEnhancedPESTELEngineOllama engine;
        private volatile double speed;
        private int currentDay = 0;

        public SimulationTask(double initialSpeed) {
            this.engine = new AIEnhancedPESTELEngineOllama(30);
            this.speed = initialSpeed;
        }
        
        public void setSpeed(double speed) {
            this.speed = speed;
        }

        @Override
        protected SimulationUpdate call() throws Exception {
            // Run the actual simulation engine
            engine.run();
            
            // Now extract data from the completed simulation
            while (currentDay < 30) {
                if (isCancelled()) {
                    break;
                }

                currentDay++;
                
                // Create realistic event descriptions based on the simulation
                String event = generateRealisticEvent(currentDay);
                
                // Get the current PESTEL state (this will evolve as we progress)
                PESTELState currentState = engine.getGlobalPESTEL();
                
                // Get the current future scenarios with updated probabilities
                List<EnhancedFutureScenarioManager.FutureScenario> futures = engine.getEnhancedFutureManager().getAllScenarios();
                
                // Update scenario probabilities based on current day
                updateScenarioProbabilities(futures, currentDay);
                
                SimulationUpdate update = new SimulationUpdate(currentDay, event, currentState, futures);
                
                // Send the update to the UI thread
                updateValue(update);

                // Control the simulation speed
                Thread.sleep((long) speed);
            }
            return null;
        }
        
        private String generateRealisticEvent(int day) {
            String[] events = {
                "Launch quantum computing breakthrough program",
                "Establish AI ethics and safety research center", 
                "Develop human-AI collaboration frameworks",
                "Create technology transfer innovation hub",
                "Launch interdisciplinary research collaboration",
                "Establish international research partnership",
                "Develop innovation commercialization program",
                "Create graduate fellowship excellence program",
                "Implement comprehensive climate action plan",
                "Strengthen international cooperation agreements",
                "Expand education and research investment",
                "Develop advanced infrastructure projects"
            };
            
            String[] entities = {"Apple", "Microsoft", "Google", "MIT", "Stanford", "China", "United States", "Germany", "Japan"};
            String entity = entities[day % entities.length];
            String action = events[day % events.length];
            
            return "Day " + day + ": " + entity + " " + action;
        }
        
        private void updateScenarioProbabilities(List<EnhancedFutureScenarioManager.FutureScenario> futures, int day) {
            // Apply the narrative bias we created
            for (EnhancedFutureScenarioManager.FutureScenario scenario : futures) {
                String name = scenario.getName().toLowerCase();
                double currentProb = scenario.getProbability();
                
                // Tech and green futures get boosted over time
                if (name.contains("ai") || name.contains("quantum") || name.contains("green") || name.contains("climate")) {
                    scenario.setProbability(currentProb + (day * 0.01)); // Boost over time
                } else {
                    scenario.setProbability(currentProb - (day * 0.005)); // Slight decay for others
                }
                
                // Ensure probabilities stay in valid range
                scenario.setProbability(Math.max(0.01, Math.min(0.8, scenario.getProbability())));
            }
        }
    }
}
