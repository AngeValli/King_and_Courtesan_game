package iialib.stateSpace.algs.implementation;

import java.util.Comparator;

import iialib.stateSpace.model.IOperator;
import iialib.stateSpace.model.IState;

public class FSmallestGHighestComparator<S extends IState<O>, O extends IOperator<S>> implements Comparator<SSNodeGF<S,O>> {
	
	public int compare(SSNodeGF<S,O> n1, SSNodeGF<S,O> n2) {
		if (n1 == n2)
			return 0;		
		int fcomp = java.lang.Double.compare(n1.getF(),n2.getF());
		if (fcomp !=  0) 
			return fcomp;
		else 
			return -n1.compareTo(n2);
	}
	
}
	

