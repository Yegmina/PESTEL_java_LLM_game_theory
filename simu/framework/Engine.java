package simu.framework;

/**
 * 
 * The base class for all simulation models
 *
 */
public abstract class Engine {
	
	private double simulationTime = 0;
	protected EventList eventList;
	
	protected Clock clock;
	
	/**
	 * The constructor of the engine
	 */
	public Engine(){
		clock = Clock.getInstance();
		eventList = new EventList();
	}

	/**
	 * Set the simulation time
	 * @param t Simulation time
	 */
	public void setSimulationTime(double t) {
		simulationTime = t;
	}
	
	/**
	 * Run the simulation
	 */
	public void run(){
		initialize(); // creating, e.g., the first event

		while (simulate()) {
			Trace.out(Trace.Level.INFO, "\nA-phase: time is " + currentTime());
			clock.setClock(currentTime());
			
			Trace.out(Trace.Level.INFO, "\nB-phase:" );
			runBEvents();
			
			Trace.out(Trace.Level.INFO, "\nC-phase:" );
			tryCEvents();

		}

		results();
	}

	private double currentTime(){
		return eventList.getNextEventTime();
	}
	
	private void runBEvents(){
		while (eventList.getNextEventTime() == clock.getClock()){
			runEvent(eventList.remove());
		}
	}

	private boolean simulate(){
		return clock.getClock() < simulationTime;
	}

	/**
	 * Execute an event
	 * @param t The event to be executed
	 */
	protected abstract void runEvent(Event t);

	/**
	 * Execute all possible C-events (conditional events)
	 * Defined in simu.model-package's class who is inheriting the Engine class
	 */
	protected abstract void tryCEvents();

	/**
	 * Initialize the simulation.
	 * Create all the static components of the system.
	 * Create the first event and add it to the event list.
	 * This method is defined in simu.model-package's class who is inheriting the Engine class
	 */
	protected abstract void initialize();

	/**
	 * Show the results of the simulation
	 * Defined in simu.model-package's class who is inheriting the Engine class
	 */
	protected abstract void results();


	// --- Methods for UI Interaction --- //

	public EventList getEventList() {
		return eventList;
	}

	public double getTime() {
		return clock.getClock();
	}

	public void runSingleEvent(Event e) {
	    runEvent(e);
    }

    public void runCEvents() {
	    tryCEvents();
    }
}