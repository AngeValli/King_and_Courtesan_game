package iialib.stateSpace.algs.implementation;


import iialib.stateSpace.model.IOperator;
import iialib.stateSpace.model.IState;

public class SSNodeG<S extends IState<O>, O extends IOperator<S>>  extends SSNode<S,O> implements Comparable<SSNodeG<S,O>> {

	private double g;

	public SSNodeG() {
		super();
		g = 0;
	}

	public SSNodeG(S state, O operator, SSNodeG<S, O> ancestor, double g) {
		super(state ,operator,ancestor);
		this.g = g;
	}

	public double getG() {
		return this.g;
	}

	public void setG(double g) {
		this.g = g;
	}
	
	
	public String toString() {
		return "Node(" + getState() + 
					 "," + getOperator() + 
					 "," + ((getAncestor()==null)?"null":"par("+ this.getAncestor().getState())+")" + 
					 "," + g + ")";
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(g);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SSNodeG other = (SSNodeG) obj;
		if (Double.doubleToLongBits(g) != Double.doubleToLongBits(other.g))
			return false;
		return true;
	}

	public int compareTo(SSNodeG<S,O> node) {
		// Fist compare on g value, then state.toString(), then op.toString(), then ancestors.
		//System.out.println("Debug[compareTo] for "+this+" with "+node);
		if (this == node)
			return 0;
		int gcomp = java.lang.Double.compare(g,node.g);
		//System.out.println("Debug[compareTo] gcomp ="+gcomp);		
		if (gcomp !=  0) 
			return gcomp;
		else {
			int scomp = this.getState().toString().compareTo(node.getState().toString());
		//	System.out.println("Debug[compareTo] scomp ="+scomp);		
			if (scomp !=  0) 
				return scomp;
			else {
				O op1 = this.getOperator();
				O op2 = node.getOperator();
				if (op1 == null)
					if(op2 != null)
						return -1;
					else 
						return 0; //  op = null, ancestor is null as well.
				else {// op1 != null
					if (op2 == null)
						return 1;
					else
						return ((SSNodeG<S,O>) this.getAncestor()).compareTo((SSNodeG<S,O>) node.getAncestor());	
				}
			}
		}
	}
	
}



