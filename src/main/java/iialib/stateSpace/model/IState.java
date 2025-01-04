package iialib.stateSpace.model;

import java.util.Iterator;

public interface IState<Operator> {

	/**
	 * Returns an iterator on the applicable operators on the state
	 * @return the iterator 
	 */
    public Iterator<Operator> applicableOperators();

    @Override
    public boolean equals(Object obj);

    @Override
    public String toString();

    // NB Think also about overriding the hashCode() method if you plan to use classes using hashCodes
}
