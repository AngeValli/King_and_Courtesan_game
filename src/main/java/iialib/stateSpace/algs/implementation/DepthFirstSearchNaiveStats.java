package iialib.stateSpace.algs.implementation;

import iialib.stateSpace.model.Problem;
import iialib.stateSpace.model.IOperator;
import iialib.stateSpace.model.IState;
import iialib.stateSpace.algs.Solution;
import iialib.stateSpace.algs.ASearchAlgorithmStats;
import java.util.Iterator;

public class DepthFirstSearchNaiveStats<S extends IState<O>, O extends IOperator<S>> 
			extends ASearchAlgorithmStats <S, O> {

	private static final String  DESCRIPTION = "Naive Depth-First search (with statistics)";
	
	private Problem<S> problem;
		

	public DepthFirstSearchNaiveStats() {
		super();
	}

	@Override
	public Solution<S, O> solve(Problem<S> problem) {
		this.problem = problem;
		resetStatistics();
		System.out.println("----------------------------------------------------");
		System.out.println("Solving problem: " + problem);
		System.out.println("with algorithm: " + DESCRIPTION);
		System.out.println("----------------------------------------------------");
		Solution<S, O> sol = search(problem.getInitialState());
		System.out.println(this.statistics());
		System.out.println("----------------------------------------------------");
		System.out.println((sol!=null) ? "Solution : " + sol : "FAILURE !");
		System.out.println("----------------------------------------------------");
		return sol;
	}

	private Solution<S, O> search(S state) {
		this.increaseVisited();
		if (problem.isTerminal(state)) {
			return new Solution<S, O>(state);
		} else {
			this.increaseDeveloped();
			Iterator<O> it = state.applicableOperators(); 
			while (it.hasNext()) { 
				O operator = it.next();
				S successor = operator.successor(state);
				Solution<S, O> solutionRest = search(successor);
				if (solutionRest != null)
					return new Solution<S, O>(state, operator, solutionRest);
			}
			// no solution has been found
			return null;
		}
	}
}
