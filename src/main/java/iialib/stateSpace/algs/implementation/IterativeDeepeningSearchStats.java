
package iialib.stateSpace.algs.implementation;

import iialib.stateSpace.model.Problem;
import iialib.stateSpace.model.IOperator;
import iialib.stateSpace.model.IState;
import iialib.stateSpace.algs.Solution;
import iialib.stateSpace.algs.ASearchAlgorithmStats;

public class IterativeDeepeningSearchStats<S extends IState<O>, O extends IOperator<S>>
		extends ASearchAlgorithmStats<S, O> {

	private static final String  DESCRIPTION = "Iterative Deepening Search (with statistics)";
	
	// --------------- Attributes  ---------------  
	/**
	 * Maximal depth reached during iteration
	 */
	int maxDepth =Integer.MAX_VALUE;

	
	/**
	 * The problem beeing solved (f
	 */
	Problem<S> problem;

		
	public IterativeDeepeningSearchStats() {
		super();
	}
	
	public IterativeDeepeningSearchStats(int maxDepth) {
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
		BoundedDepthFirstSearchCycleDetectStats<S,O> alg;
		int currentMaxDepth = 1;
		Solution<S, O> sol = null;
		while(currentMaxDepth < maxDepth && sol == null) {
			alg = new BoundedDepthFirstSearchCycleDetectStats<S,O>(currentMaxDepth);
			sol = alg.solve(problem);
			this.increaseDeveloped(alg.getNumberOfDevelopedStates());
			this.increaseVisited(alg.getNumberOfVisitedStates());
			currentMaxDepth++;
		}
		System.out.println("----------------------------------------------------");
		System.out.println(this.statistics());
		System.out.println((sol!=null) ? "Solution : " + sol : "FAILURE !");
		System.out.println("----------------------------------------------------");
		return sol;
	}

}
