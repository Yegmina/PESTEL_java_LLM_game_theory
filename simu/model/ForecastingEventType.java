package simu.model;

import simu.framework.IEventType;

/**
 * Event types for the AI agent-based forecasting simulation system.
 * These events represent different types of interactions and state changes
 * in the forecasting model.
 */
public enum ForecastingEventType implements IEventType {
    // Agent interaction events
    AGENT_DECISION,           // Agent makes a decision based on current state
    AGENT_INFLUENCE,          // Agent influences global state
    AGENT_COLLABORATION,      // Agents collaborate with each other
    
    // Global state events
    GLOBAL_STATE_UPDATE,      // Global state parameters are updated
    EXTERNAL_INFLUENCE,       // External factors affect the system
    
    // Scenario generation events
    SCENARIO_GENERATION,      // Generate new future scenarios
    STATE_TRANSITION,         // System transitions between states
    CLUSTER_FORMATION,        // Scenarios are clustered into groups
    
    // AI model events
    AI_ANALYSIS,              // AI model analyzes current situation
    AI_PREDICTION,            // AI model generates predictions
    AI_RECOMMENDATION,        // AI model provides recommendations
    
    // Time progression events
    TIME_STEP_ADVANCE,        // Simulation advances to next time step
    MONTHLY_UPDATE,           // Monthly state updates and assessments
    
    // System events
    SIMULATION_INITIALIZATION, // Initialize the simulation
    SIMULATION_TERMINATION,   // End the simulation
    RESULTS_ANALYSIS          // Analyze and report results
}
