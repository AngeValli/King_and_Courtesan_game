package iialib.stateSpace.algs.implementation;

import iialib.stateSpace.model.Problem;
import iialib.stateSpace.model.IOperator;
import iialib.stateSpace.model.IState;
import iialib.stateSpace.algs.Solution;
import iialib.stateSpace.algs.ISearchAlgorithm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DepthFirstSearchCycleDetect<S extends IState<O>, O extends IOperator<S>>
		implements ISearchAlgorithm<S, O> {

	private static final String  DESCRIPTION = "Depth-First Search (with cyle detection)";
	
	Problem<S> problem;

	public DepthFirstSearchCycleDetect() {
			super();
		}

	@Override
	public Solution<S, O> solve(Problem<S> problem) {
		this.problem = problem;
		System.out.println("----------------------------------------------------");
		System.out.println("Solving problem: " + problem);
		System.out.println("with algorithm: " + DESCRIPTION);
		System.out.println("----------------------------------------------------");
		Solution<S, O> sol = search(problem.getInitialState(),new HashSet<S>());
		System.out.println("----------------------------------------------------");
		System.out.println((sol!=null) ? "Solution : " + sol : "FAILURE !");
		System.out.println("----------------------------------------------------");
		return sol;
	}

	private Solution<S, O> search(S state,Set<S> visitedNodes) {
		if (problem.isTerminal(state)) {
			return new Solution<S, O>(state);
		} else {
			Iterator<O> it = state.applicableOperators();
			while (it.hasNext()) {
				O operator = it.next();
				S successor = operator.successor(state);
				
				if (! visitedNodes.contains(successor)) {
					visitedNodes.add(state);
					Solution<S, O> solutionRest = search(successor,visitedNodes);
					if (solutionRest != null)
						return new Solution<S, O>(state, operator, solutionRest);
					visitedNodes.remove(state);
				}
			}
			// no solution has been found
			return null;
		}
	}
}
