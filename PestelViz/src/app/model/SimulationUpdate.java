package app.model;

import simu.model.EnhancedFutureScenarioManager.FutureScenario;
import simu.model.PESTELState;
import java.util.List;
import java.util.Map;

/**
 * A simple data class to hold a snapshot of the simulation's state at a given time.
 * This is used to pass data from the background simulation thread to the UI thread.
 */
public class SimulationUpdate {
    private final int currentDay;
    private final String latestEvent;
    private final PESTELState pestelState;
    private final List<FutureScenario> futureScenarios;

    public SimulationUpdate(int currentDay, String latestEvent, PESTELState pestelState, List<FutureScenario> futureScenarios) {
        this.currentDay = currentDay;
        this.latestEvent = latestEvent;
        this.pestelState = pestelState;
        this.futureScenarios = futureScenarios;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public String getLatestEvent() {
        return latestEvent;
    }

    public PESTELState getPestelState() {
        return pestelState;
    }

    public List<FutureScenario> getFutureScenarios() {
        return futureScenarios;
    }
}


