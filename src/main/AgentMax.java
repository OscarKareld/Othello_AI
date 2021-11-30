package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoard;
import com.eudycontreras.othello.models.GameBoardCell;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.utilities.GameTreeUtility;

import java.text.DecimalFormat;
import java.util.List;


public class AgentMax extends Agent {
    private int nbrOfExploredNodes;
    private int nbrOfPrunedBranches;
    private int searchDepth;

    public AgentMax(PlayerTurn playerTurn) {
        super(playerTurn);
    }

    public AgentMax(String agentName) {
        super(agentName);
    }

    public AgentMax(String agentName, PlayerTurn playerTurn) {
        super(agentName, playerTurn);
    }

    @Override
    public AgentMove getMove(GameBoardState state) {
        nbrOfExploredNodes=0;
        nbrOfPrunedBranches=0;
        searchDepth=4;

        double startTime=System.nanoTime();
        int maxScore=Integer.MIN_VALUE;
        MoveWrapper bestMove = new MoveWrapper(null);

        List<ObjectiveWrapper> possibleMoves= AgentController.getAvailableMoves(state, PlayerTurn.PLAYER_ONE);
        for (ObjectiveWrapper move:possibleMoves) {
            GameBoardState nextState=AgentController.getNewState(state, move);
            int score=alfaBetaDFS(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, nextState, PlayerTurn.PLAYER_ONE);
            if(score>maxScore) {
                maxScore=score;
                bestMove=new MoveWrapper(move);
            }
        }

        super.setSearchDepth(searchDepth);
        super.setNodesExamined(nbrOfExploredNodes);
        super.setPrunedCounter(nbrOfPrunedBranches);
        double stopTime=System.nanoTime();
        DecimalFormat df = new DecimalFormat("####0.000");
        double executionTime=(stopTime-startTime)*Math.pow(10, -9);
        System.out.println(df.format(executionTime));
        return bestMove ;
    }

    private int alfaBetaDFS(int alfa, int beta, int depth, GameBoardState state, PlayerTurn playerTurn) {
        nbrOfExploredNodes++;
        List<ObjectiveWrapper> possibleMoves = AgentController.getAvailableMoves(state, playerTurn);

        if (cutOff(state,depth))
            return evaluation(state);

        if (possibleMoves.isEmpty())
            return alfaBetaDFS(alfa, beta, depth + 1, state, GameTreeUtility.getCounterPlayer(playerTurn));

        if (playerTurn.equals(PlayerTurn.PLAYER_ONE)) {
            int maxValue = Integer.MIN_VALUE;

            for (ObjectiveWrapper move : possibleMoves) {
                GameBoardState nextState = AgentController.getNewState(state, move);
                int value = alfaBetaDFS(alfa, beta, depth + 1, nextState, GameTreeUtility.getCounterPlayer(playerTurn));
                maxValue = Math.max(maxValue, value);
                alfa = Math.max(alfa, value);
                if (beta <= alfa) {
                    nbrOfPrunedBranches++;
                    break;
                }
            }
            return maxValue;
        } else {
            int minValue = Integer.MAX_VALUE;
            for (ObjectiveWrapper move : possibleMoves) {
                GameBoardState nextState = AgentController.getNewState(state, move);
                int value = alfaBetaDFS(alfa, beta, depth + 1, nextState, GameTreeUtility.getCounterPlayer(playerTurn));
                minValue = Math.min(minValue, value);
                beta = Math.min(beta, minValue);
                if (beta <= alfa) {
                    nbrOfPrunedBranches++;
                    break;
                }
            }
            return minValue;
        }
    }

    private boolean cutOff(GameBoardState state, int depth) {
        if(state.isTerminal())
            return true;
        if(depth==searchDepth)
            return true;
        else
            return false;
    }

    private int evaluation(GameBoardState state) {
        GameBoard gameboard=state.getGameBoard();
        int nbrOfRowsAndColumns=gameboard.getBoardSize();
        int evaluation=0;
        for(int row=0; row<nbrOfRowsAndColumns; row++)
            for(int col=0; col<nbrOfRowsAndColumns; col++) {
                GameBoardCell cell=gameboard.getGameBoardCell(row, col);
                BoardCellState thisState=cell.getCellState();
                if(thisState.equals(BoardCellState.WHITE)) {
                    evaluation+=AgentController.WEIGHT_MATRIX[row][col];
                }
            }
        return evaluation;
    }
}

