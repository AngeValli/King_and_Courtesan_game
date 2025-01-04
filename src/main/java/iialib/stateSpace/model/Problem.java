package iialib.stateSpace.model;

import java.util.Set;

public abstract class Problem<State extends IState<?>> {

	private State initialState;
	private String description;
	
	// -------- Constructors -------- 

	public Problem(State s) {
		initialState = s;
	}

	public Problem(State s, String d) {
		initialState = s;
		description = d;
	}
	

	// -------- Getters / Setters -------- 

	public State getInitialState() {
		return initialState;
	};

	public String getDescription() {
		return description;
	}

	// ---------------- 

	public String toString() {
		return description;
	}

	
	public abstract boolean isTerminal(State s);
	
	
	
	
	// -------- Static functional Constructors -------- 
	// Functional Constructor for a goal reduced to a single state
	public static <State extends IState<?>> Problem<State> defineProblem(State initState,  State goalState) {
		String desc = "Problem: from "+ initState + " to : " + goalState;	
		//System.out.println("DEBUG PROBLEM" + desc);	
		return new Problem<State>(initState,desc) {
		 
			public boolean isTerminal(State aState) {
				return goalState.equals(aState);
			}
		};
	}
		
	// Constructor for a goal reduced to a set of States
	public static <State extends IState<?>> Problem<State> defineProblem(State initState, Set<State> terminalStates) {
		String desc = "Problem : from " + initState + "\n"
				    + "to goal :      " + terminalStates;
		
		return new Problem<State>(initState,desc) {
		
			public boolean isTerminal(State aState) {
				return terminalStates.contains(aState);
			}
		};
	}


}
