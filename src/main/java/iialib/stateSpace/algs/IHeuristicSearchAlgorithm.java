package iialib.stateSpace.algs;

import iialib.stateSpace.model.IOperator;
import iialib.stateSpace.model.IState;
import iialib.stateSpace.model.Problem;
import iialib.stateSpace.model.IHeuristic;

public interface IHeuristicSearchAlgorithm<S extends IState<O>,O extends IOperator<S>>{
	
	   public Solution<S,O> solve(Problem<S> p,IHeuristic<S> h);

}
