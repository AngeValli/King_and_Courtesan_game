package iialib.stateSpace.algs.implementation;

import iialib.stateSpace.model.Problem;
import iialib.stateSpace.model.IOperator;
import iialib.stateSpace.model.IState;
import iialib.stateSpace.algs.Solution;
import iialib.stateSpace.algs.ISearchAlgorithm;

public class IterativeDeepeningSearch<S extends IState<O>, O extends IOperator<S>>
		implements ISearchAlgorithm<S, O> {

	private static final String  DESCRIPTION = "Iterative Deepening Search";
	
	Problem<S> problem;
	
	int maxDepth =Integer.MAX_VALUE;

	public IterativeDeepeningSearch() {
		super();
	}
	
	public IterativeDeepeningSearch(int maxDepth) {
		super();
		this.maxDepth = maxDepth;
	}

	@Override
	public Solution<S, O> solve(Problem<S> problem) {
		this.problem = problem;
		System.out.println("----------------------------------------------------");
		System.out.println("Solving problem: " + problem);
		System.out.println("with algorithm: " + DESCRIPTION  + " maxDepth = " + maxDepth);
		System.out.println("----------------------------------------------------");
		BoundedDepthFirstSearchCycleDetect<S,O> alg;
		int currentMaxDepth = 1;
		Solution<S, O> sol = null;
		while(currentMaxDepth < maxDepth && sol == null) {
			alg = new BoundedDepthFirstSearchCycleDetect<S,O>(currentMaxDepth);
			sol = alg.solve(problem);
			currentMaxDepth++;
		}
		System.out.println("----------------------------------------------------");
		System.out.println((sol!=null) ? "Solution : " + sol : "FAILURE !");
		System.out.println("----------------------------------------------------");
		return sol;
	}
}
