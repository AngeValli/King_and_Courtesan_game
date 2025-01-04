package iialib.stateSpace.algs;

import iialib.stateSpace.model.IOperator;
import iialib.stateSpace.model.IState;
import iialib.stateSpace.model.Problem;

public abstract class ASearchAlgorithmStats<S extends IState<O>, O extends IOperator<S>> extends SearchStats implements ISearchAlgorithm<S,O> {
			
		public abstract Solution<S,O> solve(Problem<S> problem);
}
