package iialib.stateSpace.algs;

import iialib.stateSpace.model.IOperator;
import iialib.stateSpace.model.IState;
import iialib.stateSpace.model.Problem;

public abstract class AHeuristicSearchAlgorithmStats<S extends IState<O>, O extends IOperator<S>> extends SearchStats implements IHeuristicSearchAlgorithm<S,O> {
			
		public abstract Solution<S,O> solve(Problem<S> problem);
}
