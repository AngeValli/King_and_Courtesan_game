package iialib.stateSpace.algs.implementation;

import java.util.Comparator;

import iialib.stateSpace.model.IOperator;
import iialib.stateSpace.model.IState;

public class SSNodeGF<S extends IState<O>, O extends IOperator<S>>  extends SSNodeG<S,O>{

	private double f;

	public SSNodeGF(float f) { 
		super();
		this.f = f;
	}

	public SSNodeGF(S state, O operator, SSNodeGF<S, O> ancestor, double g, double f) {
		super(state ,operator,ancestor,g);
		this.f = f;
	}

	public double getF() {
		return this.f;
	}

	public void setF(float f) {
		this.f = f;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SSNodeGF<S,O> other = (SSNodeGF<S,O>) obj;
		if (f != other.f)
			return false;
		return super.equals(other);
	}

	public String toString() {
		return "Node(" + getState() + 
					 "," + getOperator() + 
					 "," + ((getAncestor()==null)?"null":"par("+ this.getAncestor().getState())+")" + 
					 "," + getG() +
		 			 "," + f + ")";
	}
	
	
	
/*  // Can't be used since SSNodeGF are also SSNodeG for which compareTo is already defined
	public int compareTo(SSNodeGF<S,O> node) {
		System.out.print("DEBUG compareTo: "+this+".compareTo(" +node+") :" );		
		if (this == node)
			return 0;		
		int fcomp = java.lang.Float.compare(this.f,node.f);
		System.out.println("DEBUG : fcomp = " + fcomp );
		if (fcomp !=  0) {
			System.out.println("DEBUG compareTo: fcomp = " + fcomp );
			return fcomp;	// Order by f increasing
		}
		else {
			fcomp = -super.compareTo(node); // order by G decreasing
			System.out.println("DEBUG compareTo: fcomp = " + fcomp );
			return fcomp ;
		}
		
	}
	
/*	// Does not work since static variables can't be parametrized by generic types 

	public static Comparator<SSNodeGF<S,O>> FGComparator = new Comparator<SSNodeGF<S,O>>() {
		// compare nodes according to G value, then according to state order.

		public int compare(SSNodeGF<S,O> n1, SSNodeGF<S,O> n2) {
			if (n1 == n2)
				return 0;		
			int fcomp = java.lang.Float.compare(n1.f,n2.f);
			if (fcomp !=  0) 
				return fcomp;
			else 
				return -n1.compareTo(n2);
		}
		
	};
*/

	
	@SuppressWarnings("rawtypes")
	public static Comparator<SSNodeGF> FGComparator = new Comparator<SSNodeGF>() {
		// compare nodes according to G value, then according to state order.

		@SuppressWarnings("unchecked")
		public int compare(SSNodeGF n1, SSNodeGF n2) {
			if (n1 == n2)
				return 0;		
			int fcomp = java.lang.Double.compare(n1.f,n2.f);
			if (fcomp !=  0) 
				return fcomp;
			else 
				return -n1.compareTo(n2);
		}
		
	};
	
	public class FSmallestGHighestComparator implements Comparator<SSNodeGF<S,O>> {
	
		public int compare(SSNodeGF<S,O> n1, SSNodeGF<S,O> n2) {
			if (n1 == n2)
				return 0;		
			int fcomp = java.lang.Double.compare(n1.f,n2.f);
			if (fcomp !=  0) 
				return fcomp;
			else 
				return -n1.compareTo(n2);
		}
		
	};

	

}
	

