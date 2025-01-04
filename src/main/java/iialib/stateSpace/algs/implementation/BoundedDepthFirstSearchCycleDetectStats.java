package iialib.stateSpace.algs.implementation;

import iialib.stateSpace.model.Problem;
import iialib.stateSpace.model.IOperator;
import iialib.stateSpace.model.IState;
import iialib.stateSpace.algs.Solution;
import iialib.stateSpace.algs.ASearchAlgorithmStats;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BoundedDepthFirstSearchCycleDetectStats<S extends IState<O>, O extends IOperator<S>>
		extends ASearchAlgorithmStats<S, O> {

	private static final String  DESCRIPTION = "Bounded Depth-First Search (with cyle detection)  (with statistics)";
	
	Problem<S> problem;
	int maxDepth;

	public BoundedDepthFirstSearchCycleDetectStats(int maxDepth) {
		super();
		this.maxDepth = maxDepth;
	}

	@Override
	public Solution<S, O> solve(Problem<S> problem) {
		this.problem = problem;
		resetStatistics();
		System.out.println("----------------------------------------------------");
		System.out.println("Solving problem: " + problem);
		System.out.println("with algorithm: " + DESCRIPTION  + " maxDepth = " + maxDepth);
		System.out.println("----------------------------------------------------");
		Solution<S, O> sol = search(problem.getInitialState(),new HashSet<S>(),maxDepth);
		System.out.println("----------------------------------------------------");
		System.out.println(this.statistics());
		System.out.println((sol!=null) ? "Solution : " + sol : "FAILURE !");
		System.out.println("----------------------------------------------------");
		return sol;
	}

	private Solution<S, O> search(S state, Set<S> visitedNodes, int depth) {
		this.increaseVisited();
		if (problem.isTerminal(state)) {
			return new Solution<S, O>(state);
		} else if (depth > 0) {
				System.out.println("DEBUG search "+ state +" is developed");
				this.increaseDeveloped();
				Iterator<O> it = state.applicableOperators();
				while (it.hasNext()) {
					O operator = it.next();
					S successor = operator.successor(state);
					System.out.println("DEBUG search : op is " + operator + " successor is " + successor);

					if (!visitedNodes.contains(successor)) {
						visitedNodes.add(state);
						Solution<S, O> solutionRest = search(successor, visitedNodes,depth - 1);
						if (solutionRest != null)
							return new Solution<S, O>(state, operator, solutionRest);
						visitedNodes.remove(state);
					}
				}
		}
		// no solution has been found
		return null;
	}
}
