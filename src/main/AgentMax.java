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

import java.util.List;

public class AgentMax extends Agent {
    private int nbrOfExploredNodes;
    private int nbrOfPrunedBranches;
    private int depth;
    public static boolean endOfSearch;

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
        // funktion som returnerar en action med ett värde
        nbrOfExploredNodes=0;
        nbrOfPrunedBranches=0;
        int bestDepth=0;

        int maxScore=Integer.MIN_VALUE;
        MoveWrapper bestMove = new MoveWrapper(null);
        List<ObjectiveWrapper> possibleMoves= AgentController.getAvailableMoves(state, PlayerTurn.PLAYER_ONE);  // skapa en lista med möjliga drag för max
        // det maiximala djupet som kan tillämpas = antal fria rutor på brädet
        //int maxDepth=getFreeCells(state);
        int maxDepth=5;

        for (ObjectiveWrapper move:possibleMoves) {
            GameBoardState nextState=AgentController.getNewState(state, move);
            int score=miniMax(nextState, maxDepth);
            if(score>maxScore) {
                maxScore=score;
                bestMove=new MoveWrapper(move);
                bestDepth=depth;
            }
        }
        super.setSearchDepth(bestDepth);
        super.setNodesExamined(nbrOfExploredNodes);
        super.setPrunedCounter(nbrOfPrunedBranches);
        return bestMove ;
    }

    private int miniMax(GameBoardState state, int maxDepth) {

        depth=0;
        int score=0;
        endOfSearch=false;

        while(true) {

            if(depth==maxDepth)
                break;
            int thisScore= alfaBetaDFS(Integer.MIN_VALUE, Integer.MAX_VALUE, depth, state, playerTurn);

            if(!endOfSearch) {
                score=thisScore;
                depth++;
                System.out.println("score="+score);
                System.out.println("depth="+depth);
            }
        }
        System.out.println("ute ur miniMax-while");
        return score;
    }

    private int alfaBetaDFS(int alfa, int beta, int depth, GameBoardState state, PlayerTurn playerTurn) {
        nbrOfExploredNodes++;
        List<ObjectiveWrapper> possibleMoves = AgentController.getAvailableMoves(state, playerTurn);
        int nbrOfPossibleWhiteMoves = AgentController.getAvailableMoves(state, PlayerTurn.PLAYER_ONE).size();
        int nbrOfPossibleBlackMoves = AgentController.getAvailableMoves(state, PlayerTurn.PLAYER_TWO).size();



        if (endOfSearch || depth == 0 ||state.isTerminal() ||nbrOfPossibleBlackMoves + nbrOfPossibleWhiteMoves == 0) {
            //return utility(state);
            return (int) AgentController.getGameEvaluation(state, playerTurn);
        }

        if (possibleMoves.isEmpty())
            return alfaBetaDFS(alfa, beta, depth - 1, state, GameTreeUtility.getCounterPlayer(playerTurn));

        if (playerTurn.equals(PlayerTurn.PLAYER_ONE)) {
            int maxValue = Integer.MIN_VALUE;

            for (ObjectiveWrapper move : possibleMoves) {
                GameBoardState nextState = AgentController.getNewState(state, move);
                int value = alfaBetaDFS(alfa, beta, depth - 1, nextState, GameTreeUtility.getCounterPlayer(playerTurn));
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
                int value = alfaBetaDFS(alfa, beta, depth - 1, nextState, GameTreeUtility.getCounterPlayer(playerTurn));
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

    private int getFreeCells(GameBoardState state){
        GameBoardCell[][] gameBoard=state.getGameBoard().getCells();
        int nbrOfFreeCells=0;
        for(int row=0; row< gameBoard.length; row++)
            for(int column=0; column<gameBoard.length; column++)
                if(gameBoard[row][column].getCellState().equals(BoardCellState.EMPTY))
                    nbrOfFreeCells++;
        return nbrOfFreeCells;
    }

    private int utility(GameBoardState state) {
        GameBoard gameboard=state.getGameBoard();
        int nbrOfRowsAndColumns=gameboard.getBoardSize();
        int utility=0;
        for(int row=0; row<nbrOfRowsAndColumns; row++)
            for(int col=0; col<nbrOfRowsAndColumns; col++) {
                GameBoardCell cell=gameboard.getGameBoardCell(row, col);
                BoardCellState thisState=cell.getCellState();
                if(thisState.equals(BoardCellState.WHITE)) {
                    utility+=AgentController.WEIGHT_MATRIX[row][col];
                }
            }
        return utility;
    }
}
