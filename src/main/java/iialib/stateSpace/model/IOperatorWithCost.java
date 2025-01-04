package iialib.stateSpace.model;

public interface IOperatorWithCost<State> extends IOperator<State>{

	/**
	 * returns the cost of the operator
	 */
    public  double getCost();

}
