package iialib.stateSpace.algs.implementation;

import iialib.stateSpace.model.Problem;
import iialib.stateSpace.model.IOperatorWithCost;
import iialib.stateSpace.model.IState;
import iialib.stateSpace.algs.ISearchAlgorithm;
import iialib.stateSpace.algs.Solution;
import iialib.stateSpace.algs.SolutionWithCost;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class UniformCostSearch<S extends IState<O>, O extends IOperatorWithCost<S>> implements ISearchAlgorithm<S, O> {

	private static final String  DESCRIPTION = "UniformCost Search";

	Problem<S> problem;

	public UniformCostSearch() {
		super();
	}

	@Override
	public Solution<S, O> solve(Problem<S> p) {
		this.problem = p;
		System.out.println("-----------------------------------------------------");
		System.out.println("Solving problem: " + problem);
		System.out.println("with algorithm: " + DESCRIPTION);
		System.out.println("-----------------------------------------------------");
		SolutionWithCost<S, O> sol = (SolutionWithCost<S,O>) search(problem.getInitialState());
		System.out.println((sol!=null) ? "Solution : " + sol + "\nCost : " + sol.cost(): "FAILURE !");
		System.out.println("-----------------------------------------------------");
		return sol;
	}

	private Solution<S,O> search(S s){
		
		Solution<S,O> result = null;
    	
    	Set<SSNodeG<S,O>> developedNodes = new HashSet<SSNodeG<S,O>>();
    	SortedSet<SSNodeG<S,O>> frontier = new TreeSet<SSNodeG<S,O>>();
    	frontier.add(new SSNodeG<S,O>(s,null,null,0));
    	int cpt=1;
    	while(!frontier.isEmpty()) {
    		 cpt++;
    		 SSNodeG<S,O> node = frontier.first();		// Element with lowest G since Frontier is sorted by G
    		 System.out.println("DEBUG : frontier = "+frontier);
      		 S state = node.getState();
    		 System.out.println("\n" + cpt + " DEBUG : selecting node corresponding to = "+ state);
    		 double gState = node.getG();
    		 if (problem.isTerminal(state)) {
    			 System.out.println("DEBUG : terminal state = "+ state);
    			 result = buildSolution(node);
    			 break;
    		 } else {
    	  		 frontier.remove(node);
    	  		 System.out.println("DEBUG : removing node = "+ node + " from frontier : \n" + frontier);
        		 developedNodes.add(node);
       	  		 System.out.println("DEBUG : adding node = "+ node + " to developed nodes :");
       	  	     System.out.println("DEBUG : developedNodes = "+developedNodes);
    			 Iterator<O> it = state.applicableOperators();
    			 while (it.hasNext()) {
    			   O operator = it.next();
    			   S successor = operator.successor(state);
     			   double ng = gState + operator.getCost();  // cost to successor via state
       			   System.out.println("DEBUG : using op = "+ operator + " leads to " + successor + " whith cost "+ng);
    			   // Look for a node with same state in developedNodes
    			   SSNodeG<S,O>  nodeWithSameState = findNodeWithSameState(developedNodes,successor);
    			   if (nodeWithSameState == null) {
    				   System.out.println("DEBUG : "+successor + " does not appear in developedNodes ");
     				   nodeWithSameState = findNodeWithSameState(frontier,successor);
    				   if (nodeWithSameState != null) {
    					    System.out.println("DEBUG : node with same state " + successor + " in frontier"  );
    					   	if (ng < nodeWithSameState.getG()) {					   		
    					   	    System.out.println("DEBUG : ... and with higher cost ("+nodeWithSameState.getG()+") => Updating node ");
    					   	    // Update Parent and cost in treeSet requires removal / add of a new SSnodeG
    	   				    	frontier.remove(nodeWithSameState);
    	   				    	frontier.add(new SSNodeG<S,O>(successor,operator,node,ng));
    					   	} // Otherwise we keep the current node 
    				   } else {
    					   System.out.println("DEBUG : "+successor + " does not appear in frontier ");
         				   System.out.println("DEBUG : adding new node corresponding to " + successor + " to frontier"  );
    					   frontier.add(new SSNodeG<S,O>(successor,operator,node,ng));
    				   }
     			   }   				   	
   				   // NB if developedNodes contains a node with the same state as successor 
				   // ng can't be strictly lower than nodeWithSameState.getG()
				   // so we just forget this new path do successor.
    			}

    	    }
    	}		 
	    return result;	
    }	
    	
    private static <S extends IState<O>, O extends IOperatorWithCost<S>> SSNodeG<S,O>  findNodeWithSameState(Collection<SSNodeG<S,O>> collection, S state) {
    	for(SSNodeG<S,O>  node : collection)
			if (node.getState().equals(state))
					return node;
		return null ;	
    }
    
	private static <S extends IState<O>, O extends IOperatorWithCost<S>> SolutionWithCost<S,O> buildSolution(SSNodeG<S,O> node) {
		S s = node.getState();
		O op = node.getOperator();
		SSNode<S,O> ancestor = node.getAncestor();
		SolutionWithCost<S,O> sol = new SolutionWithCost<S,O>(s);
		while (ancestor != null) {
			sol = new SolutionWithCost<S,O>(ancestor.getState(),op,sol);
			op = ancestor.getOperator();
			ancestor = ancestor.getAncestor();
		} 
		return sol;
	}
}
