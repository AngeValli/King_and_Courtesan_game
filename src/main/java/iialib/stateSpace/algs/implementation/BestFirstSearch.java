package iialib.stateSpace.algs.implementation;

import iialib.stateSpace.model.Problem;
import iialib.stateSpace.model.IHeuristic;
import iialib.stateSpace.model.IOperatorWithCost;
import iialib.stateSpace.model.IState;
import iialib.stateSpace.algs.IHeuristicSearchAlgorithm;
import iialib.stateSpace.algs.Solution;
import iialib.stateSpace.algs.SolutionWithCost;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class BestFirstSearch<S extends IState<O>, O extends IOperatorWithCost<S>> implements IHeuristicSearchAlgorithm<S, O> {

	private static final String  DESCRIPTION = "Best First Search";

	IHeuristic<S> h;
	
	Problem<S> problem;

	public BestFirstSearch() {
	}

	@Override
	public Solution<S, O> solve(Problem<S> p,IHeuristic<S> h) {
		this.problem = p;
		this.h = h;
		System.out.println("-----------------------------------------------------");
		System.out.println("Solving problem: " + problem);
		System.out.println("with algorithm: " + DESCRIPTION + " and heuristic " + h);
		System.out.println("-----------------------------------------------------");
		SolutionWithCost<S, O> sol = (SolutionWithCost<S,O>) search(problem.getInitialState());
		System.out.println("----------------------------------------------------");
		System.out.println((sol!=null) ? "Solution : " + sol + "\nCost : " + sol.cost(): "FAILURE !");
		System.out.println("-----------------------------------------------------");
		return sol;
	}

	private Solution<S,O> search(S s){
		int cpt = 1;
		Solution<S,O> result = null;
    	
    	Set<SSNodeGF<S,O>> developedNodes = new HashSet<SSNodeGF<S,O>>();
    	SortedSet<SSNodeGF<S,O>> frontier = new TreeSet<SSNodeGF<S,O>>(new FSmallestGHighestComparator<S,O>());
    	frontier.add(new SSNodeGF<S,O>(s,null,null,0,h.apply(s)));
    	
    	while(!frontier.isEmpty()) {
    		 cpt++;
    		 SSNodeGF<S,O> node = frontier.first();		// Element with lowest G since Frontier is sorted by G
    		 System.out.println("DEBUG : frontier = "+frontier);
      		 S state = node.getState();
    		 System.out.println(""+cpt+ " DEBUG : selecting node corresponding to = "+ state);
    		 double gState = node.getG();
    		 if (problem.isTerminal(state)) {
    			 System.out.println("DEBUG : terminal state = "+ state);
    			 result = buildSolution(node);
    			 break;
    		 } else {
    	  		 frontier.remove(node);
        		 developedNodes.add(node);
        		 System.out.println("DEBUG : developedNodes = "+developedNodes);
    			 Iterator<O> it = state.applicableOperators();
    			 while (it.hasNext()) {
    			   O operator = it.next();
    			   S successor = operator.successor(state);
     			   double ng = gState + operator.getCost();  // cost to successor via state
       			   //System.out.println("DEBUG : using op = "+ operator + " leads to " + successor + " whith cost "+ng);
    			   // Look for a node with same state in developedNodes
    			   SSNodeGF<S,O>  nodeWithSameState = findNodeWithSameState(developedNodes,successor);
       			   if (nodeWithSameState != null) {// Do nothing
       			   } else { 
       				   nodeWithSameState = findNodeWithSameState(frontier,successor);
    				   if (nodeWithSameState != null) {
    					   	if (ng < nodeWithSameState.getG()) {
    					   		//System.out.println("DEBUG : node with same state in frontier"  );
    					   	    //System.out.println("DEBUG : ... higher cost => Updating node ");
    					   	    // Update Parent and cost in treeSet requires removal / add of a new SSnodeG
    	   				    	frontier.remove(nodeWithSameState);
    	   				    	frontier.add(new SSNodeGF<S,O> (successor,operator,node,ng,h.apply(successor)));
    					   	} // /Otherwise, ignore this higher cost path to successor
    				   } else {
    					   		//System.out.println("DEBUG : adding new node corresponding to " + successor + " to frontier"  );
    				      		frontier.add(new SSNodeGF<S,O> (successor,operator,node,ng,h.apply(successor)));
    				   }
    			   }   				   	
    			 }
    		   }
    	}		 
    	return result;	
    }	
    	
    private static <S extends IState<O>, O extends IOperatorWithCost<S>> SSNodeGF<S,O> findNodeWithSameState(Collection<SSNodeGF<S,O>> collection, S state) {
    	for(SSNodeGF<S,O>  node : collection)
			if (node.getState().equals(state))
					return node;
		return null ;	
    }
    
	private static <S extends IState<O>, O extends IOperatorWithCost<S>> SolutionWithCost<S,O> buildSolution(SSNodeGF<S,O> node) {
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
