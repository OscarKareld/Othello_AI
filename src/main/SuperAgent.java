package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SuperAgent extends Agent {

    protected int searchDepth = 0;
    protected int reachedLeafNodes = 0;
    protected int nodesExamined = 0;
    protected int prunedCounter = 0;
    protected PlayerTurn playerTurn;
    protected String agentName;
    private SuperMove nextMove;
    private GameBoardState state;

//    public SuperAgent(PlayerTurn playerTurn) {
//        super(playerTurn);
//        nextMove = new SuperMove();
//    }

    public SuperAgent(String agentName) {
        super(agentName);
    }

    public SuperAgent(String agentName, PlayerTurn playerTurn) {
        super(agentName, playerTurn);
    }

    public AgentMove getNextMove(GameBoardState gameBoardState) {
        SuperMove newMove=new SuperMove(gameBoardState);
        return miniMax(gameBoardState, 2, PlayerTurn.PLAYER_ONE, newMove);

        //Retrieves and stores all moves for specified player given the current state of the game
        //List<ObjectiveWrapper> agentMoves = getAvailableMoves(currentState, turn);
    }

    private SuperMove miniMax(GameBoardState state, int depth, PlayerTurn player, SuperMove newMove) {
        List<ObjectiveWrapper> possibleMoves= AgentController.getAvailableMoves(state, player);
        LinkedList<SuperMove> evaluatedMoves=new LinkedList<>();

        if(state.isTerminal() || depth==0) { // cut-off test
            double evaluation=AgentController.getDynamicHeuristic(state);
            newMove.setScore((int) evaluation);
            return newMove;
        }
        if(player.equals(PlayerTurn.PLAYER_ONE)) { // maxdrag
            for (ObjectiveWrapper move:possibleMoves) {
                //miniMax(AgentController.getNewState(gameBoardState,move));
                evaluatedMoves.add(miniMax(AgentController.getNewState(state,move), depth-1, player, newMove));
                Collections.sort(evaluatedMoves, Collections.reverseOrder());
                return evaluatedMoves.getFirst();
            }
        }

        if(player.equals(PlayerTurn.PLAYER_TWO)) { // mindrag
            for (ObjectiveWrapper move:possibleMoves) {
                //miniMax(AgentController.getNewState(gameBoardState,move));
                evaluatedMoves.add(miniMax(AgentController.getNewState(state,move), depth-1, player, newMove));
                Collections.sort(evaluatedMoves);
                return evaluatedMoves.getFirst();
            }
        }
        return newMove;
    }


    @Override
    public AgentMove getMove(GameBoardState gameBoardState) {
        nextMove=new SuperMove(gameBoardState);
        return getNextMove(gameBoardState);
    }
    // Return your move
    @Override
    public int getSearchDepth() {
        return searchDepth;
    }

    @Override
    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    @Override
    public int getReachedLeafNodes() {
        return reachedLeafNodes;
    }

    @Override
    public void setReachedLeafNodes(int reachedLeafNodes) {
        this.reachedLeafNodes = reachedLeafNodes;
    }

    @Override
    public int getNodesExamined() {
        return nodesExamined;
    }

    @Override
    public void setNodesExamined(int nodesExamined) {
        this.nodesExamined = nodesExamined;
    }

    @Override
    public int getPrunedCounter() {
        return prunedCounter;
    }

    @Override
    public void setPrunedCounter(int prunedCounter) {
        this.prunedCounter = prunedCounter;
    }
}
