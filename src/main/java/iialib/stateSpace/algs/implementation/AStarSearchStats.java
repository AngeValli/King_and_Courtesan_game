package iialib.stateSpace.algs.implementation;

import iialib.stateSpace.model.Problem;
import iialib.stateSpace.model.IHeuristic;
import iialib.stateSpace.model.IOperatorWithCost;
import iialib.stateSpace.model.IState;
import iialib.stateSpace.algs.AHeuristicSearchAlgorithmStats;
import iialib.stateSpace.algs.Solution;
import iialib.stateSpace.algs.SolutionWithCost;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class AStarSearchStats<S extends IState<O>, O extends IOperatorWithCost<S>> 
		extends AHeuristicSearchAlgorithmStats<S,O> {

	private static final String  DESCRIPTION = "A*";

	IHeuristic<S> h;
	
	Problem<S> problem;

	public AStarSearchStats() {
		super();
	}

	public AStarSearchStats(IHeuristic<S> h) {
		super();
		this.h = h;
	}

	@Override
	public Solution<S, O> solve(Problem<S> p,IHeuristic<S> h) {
		this.h = h;
		return solve(p);
	}

	public Solution<S, O> solve(Problem<S> p) {
		this.problem = p;
		System.out.println("-----------------------------------------------------");
		System.out.println("Solving problem: " + problem);
		System.out.println("with algorithm: " + DESCRIPTION);
		System.out.println("-----------------------------------------------------");
		resetStatistics();	
		SolutionWithCost<S, O> sol = (SolutionWithCost<S,O>) search(problem.getInitialState());
		System.out.println(this.statistics());
		System.out.println("----------------------------------------------------");
		System.out.println((sol!=null) ? "Solution : " + sol + "\nCost : " + sol.cost(): "FAILURE !");
		System.out.println("-----------------------------------------------------");
		return sol;
	}

	private Solution<S,O> search(S s){
		//int cpt = 1;
		Solution<S,O> result = null;
		// Data Structure initialization
    	HashMap<S,SSNodeGF<S,O>> developedNodes = new HashMap<S,SSNodeGF<S,O>>();
    	SortedSet<SSNodeGF<S,O>> frontier = new TreeSet<SSNodeGF<S,O>>(new FSmallestGHighestComparator<S,O>());
    	frontier.add(new SSNodeGF<S,O>(s,null,null,0,h.apply(s)));
    	increaseVisited();
    	
    	while(!frontier.isEmpty()) {
    		 //cpt++;
    		 // Select Node to expand
    		 SSNodeGF<S,O> node = frontier.first();		// Element with lowest G since Frontier is sorted by G
    		// System.out.println("DEBUG : frontier = "+frontier);
      		 S state = node.getState();
    		 //System.out.println(""+cpt+ " DEBUG : selecting node corresponding to = "+ state);
    		 increaseDeveloped();
    		 //
    		 if (problem.isTerminal(state)) {
    			 //System.out.println("DEBUG : terminal state = "+ state);
    			 result = buildSolution(node);
    			 break;
    		 } else {
    			 double gState = node.getG();
    	  		 frontier.remove(node);
        		 developedNodes.put(state,node);
        		 //System.out.println("DEBUG : developedNodes = "+developedNodes);
    			 Iterator<O> it = state.applicableOperators();
    			 while (it.hasNext()) {
    			   O operator = it.next();
    			   S successor = operator.successor(state);
     			   double ng = gState + operator.getCost();  // cost to successor via state
       			   //System.out.println("DEBUG : using op = "+ operator + " leads to " + successor + " whith cost "+ng);
    			   // Look for a node with same state in developedNodes
    			   SSNodeGF<S,O>  nodeWithSameState = developedNodes.get(successor);
    			   //System.out.println("DEBUG : developedNodes.get("+successor+") "+ nodeWithSameState);
       			   if (nodeWithSameState != null) {
				   		//System.out.println("DEBUG : node with same state in developedNodes"  );
      				  if (ng < nodeWithSameState.getG()) {
  				   	    //System.out.println("DEBUG : ... higher cost => Updating node and put it into frontier ");
  				   	    developedNodes.remove(successor);
       					frontier.add(new SSNodeGF<S,O> (successor,operator,node,ng,ng+h.apply(successor)));
       					increaseVisited();
       				  } // Otherwise, ignore this higher cost path to successor
      				//else {
				   	//	System.out.println("DEBUG : ... lower cost => Ignored ");
				   	//}
       			   } else { 
       				   nodeWithSameState = findNodeWithSameState(frontier,successor);
    				   if (nodeWithSameState != null) {
    					   	if (ng < nodeWithSameState.getG()) {
    					   		//System.out.println("DEBUG : node with same state in frontier"  );
    					   	    //System.out.println("DEBUG : ... higher cost => Updating node ");
    					   	    // Update Parent and cost in treeSet requires removal / add of a new SSnodeG
    	   				    	frontier.remove(nodeWithSameState);
    	   				    	frontier.add(new SSNodeGF<S,O> (successor,operator,node,ng,ng+h.apply(successor)));
    	   				    	increaseVisited();
    					   	} // /Otherwise, ignore this higher cost path to successor
    					   	//else {
    					   	//	System.out.println("DEBUG : ... lower cost => Ignored ");
    					   	//}
    				   } else {
    					   		//System.out.println("DEBUG : adding new node corresponding to " + successor + " to frontier"  );
    				      		frontier.add(new SSNodeGF<S,O> (successor,operator,node,ng,ng+h.apply(successor)));
    				      		increaseVisited();
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
