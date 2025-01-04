package iialib.stateSpace.model;

public interface IHeuristic<S extends IState<?>> {
	
	public double apply(S state);

	@Override
	public String toString();
}
