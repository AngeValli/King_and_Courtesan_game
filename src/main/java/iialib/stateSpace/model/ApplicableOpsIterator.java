package iialib.stateSpace.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ApplicableOpsIterator<State extends IState<Operator>, Operator extends IOperator<State>> implements Iterator<Operator> {
	
	State state;
	Iterator<Operator> opsIt;
	boolean hasNext;
	Operator nextApplicableOperator;		
	
	public ApplicableOpsIterator(Collection<Operator> operators, State s) {
		opsIt = operators.iterator();
		state = s;
		findNextApplicable();
	}
	
	private Operator findNextApplicable() {
		Operator prevApplicableOperator = nextApplicableOperator;
		hasNext = false;
		while (opsIt.hasNext()) {
			Operator op = opsIt.next();
			if(op.isApplicable(state)) {
				hasNext = true;
				nextApplicableOperator = op;
				break;
			}
		}
		return prevApplicableOperator;			
	}

		@Override
	public Operator next() {
        if (!hasNext) {
            throw new NoSuchElementException();
        }
        return findNextApplicable();
    }

	@Override	
	public boolean hasNext() {
      return hasNext;
    }
	
}
