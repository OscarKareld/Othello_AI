package main;
import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.Index;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.enumerations.GameState;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoard;
import com.eudycontreras.othello.models.GameBoardCell;
import com.eudycontreras.othello.models.GameBoardState;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Each implemented class must contain a moveIndex wrapper that encapsulates information about where to place the chip. A definition of
 * what a valid move is also needs to be implemented. Please see {@link com.eudycontreras.othello.capsules.MoveWrapper}
 * for an example implementation. Also take a look at {@link AgentMove} for
 * additional details.
 */

public class SuperMove extends AgentMove {

    private int score;
    private GameBoardState state;
    private GameBoardState gameBoardState;
    private PlayerTurn player;

    public SuperMove(GameBoardState newState) {
        this.gameBoardState=newState;
        // TODO Auto-generated constructor stub
    }


    // TODO generate move
//    public AgentMove getNextMove(GameBoardState gameBoardState) {
//        state=gameBoardState;
//        return miniMax(gameBoardState, 2, PlayerTurn.PLAYER_ONE);
//
//        //Retrieves and stores all moves for specified player given the current state of the game
//        //List<ObjectiveWrapper> agentMoves = getAvailableMoves(currentState, turn);
//    }
//
//    private SuperMove miniMax(GameBoardState state, int depth, PlayerTurn player) {
//        List<ObjectiveWrapper> possibleMoves=AgentController.getAvailableMoves(state, player);
//        LinkedList<SuperMove> evaluatedMoves=new LinkedList<>();
//
//        if(state.isTerminal() || depth==0) { // cut-off test
//            double evaluation=AgentController.getDynamicHeuristic(state);
//            this.setScore((int) evaluation);
//            return this;
//        }
//        if(player.equals(PlayerTurn.PLAYER_ONE)) { // maxdrag
//            for (ObjectiveWrapper move:possibleMoves) {
//                //miniMax(AgentController.getNewState(gameBoardState,move));
//                evaluatedMoves.add(miniMax(AgentController.getNewState(gameBoardState,move), depth-1, player));
//                Collections.sort(evaluatedMoves, Collections.reverseOrder());
//                return evaluatedMoves.getFirst();
//            }
//
//        }
//
//        if(player.equals(PlayerTurn.PLAYER_TWO)) { // mindrag
//            for (ObjectiveWrapper move:possibleMoves) {
//                //miniMax(AgentController.getNewState(gameBoardState,move));
//                evaluatedMoves.add(miniMax(AgentController.getNewState(gameBoardState,move), depth-1, player));
//                Collections.sort(evaluatedMoves);
//                return evaluatedMoves.getFirst();
//            }
//
//        }
//
//        // state is terminal
//        int utility=utility(state);
//
//        // TODO fortsätt här
//        return this;
//    }



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

    public void setScore(int score) {
        this.score=score;
    }

    public int getScore() {
        return score;
//        System.out.println("score: "+AgentController.getDynamicHeuristic(state));
//        return (int) AgentController.getDynamicHeuristic(state);
    }
    private int moveReward;

    private Index startIndex;

    private BoardCellState target;

    public void setObjectiveInformation(ObjectiveWrapper objective){
        if(objective.getObjectiveCell() == null){
            return;
        }
        this.target = objective.getObjectiveCell().getCellState();
        this.moveIndex = objective.getObjectiveCell().getIndex();
        this.startIndex = objective.getCurrentCell().getIndex();
        this.moveReward = objective.getPath().size();
    }

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub

        return moveIndex != null && startIndex != null;
    }

    @Override
    public int compareTo(AgentMove otherMove) {
        SuperMove compareMove=(SuperMove) otherMove;
        // 1 om getScore()>compareMove.getScore()
        // -1 om getScore()<compareMove.getScore()
        // 0 om getScore()=compareMove.getScore()
        return Integer.compare(getScore(), compareMove.getScore());
    }
}
