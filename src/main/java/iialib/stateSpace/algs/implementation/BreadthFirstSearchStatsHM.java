package iialib.stateSpace.algs.implementation;

import iialib.stateSpace.model.Problem;
import iialib.stateSpace.model.IOperator;
import iialib.stateSpace.model.IState;
import iialib.stateSpace.algs.ASearchAlgorithmStats;
import iialib.stateSpace.algs.Solution;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class BreadthFirstSearchStatsHM<S extends IState<O>, O extends IOperator<S>> 
	   extends ASearchAlgorithmStats<S, O> {

	private static final String  DESCRIPTION = "Breadth-First Search (HM) (with statistics)";

	Problem<S> problem;

	public BreadthFirstSearchStatsHM() {
		super();
	}

	@Override
	public Solution<S, O> solve(Problem<S> p) {
		this.problem = p;

		System.out.println("----------------------------------------------------");
		System.out.println("Solving problem: " + problem);
		System.out.println("with algorithm: " + DESCRIPTION);
		System.out.println("----------------------------------------------------");
		
		resetStatistics();
		Solution<S, O> sol = search(problem.getInitialState());
		System.out.println(this.statistics());
		
		System.out.println("----------------------------------------------------");
		System.out.println((sol!=null) ? "Solution : " + sol : "FAILURE !");
		System.out.println("----------------------------------------------------");
		
		return sol;
	}

	

	private Solution<S,O> search(S s){
		
		Solution<S,O> result = null;
    	// Data Structure initialization
    	HashMap<S,SSNode<S,O>> developedNodes = new HashMap<S,SSNode<S,O>>();
    	LinkedList<SSNode<S,O>> frontier = new LinkedList<SSNode<S,O>>();
    	frontier.addLast(new SSNode<S,O>(s,null,null));
     	this.increaseVisited();	// First created node
     	
    	while(!frontier.isEmpty()) {
    		 SSNode<S,O> node = frontier.remove();
     		 S state = node.getState();
     		 developedNodes.put(state,node);
   		 this.increaseDeveloped();
     		 //
    		 if (problem.isTerminal(state)) {
    			 result = buildSolution(node);
    			 break;
    		 }
    		 else {
    			 Iterator<O> it = state.applicableOperators();
    			 while (it.hasNext()) {
    			   O operator = it.next();
    			   S successor = operator.successor(state);
    			   if (! developedNodes.containsKey(successor) && ! containsNodeWithSameState(frontier,successor)) {
    				   frontier.addLast(new SSNode<S,O>(successor,operator,node));   				  
    			   	   this.increaseVisited();   // New created node
    			   }
    			 }
       		 }
     	}
        return result;
    }
   	
    private static <S extends IState<O>, O extends IOperator<S>> boolean  containsNodeWithSameState(Collection<SSNode<S,O>> collection, S state) {
    	for(SSNode<S,O>  node : collection)
			if (node.getState().equals(state))
					return true;
		return false;	
    }
    
	private static <S extends IState<O>, O extends IOperator<S>> Solution<S,O> buildSolution(SSNode<S,O> node) {
		S s = node.getState();
		O op = node.getOperator();
		SSNode<S,O> ancestor = node.getAncestor();
		Solution<S,O> sol = new Solution<S,O>(s);
		while (ancestor != null) {
			sol = new Solution<S,O>(ancestor.getState(),op,sol);
			op = ancestor.getOperator();
			ancestor = ancestor.getAncestor();
		} 
		return sol;
	}
}
