package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.Index;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardCell;
import com.eudycontreras.othello.models.GameBoardState;

public class ExampleAgentOscar extends Agent {

    public ExampleAgentOscar(PlayerTurn playerTurn) {
        super(playerTurn);
    }

    public ExampleAgentOscar(String agentName) {
        super(agentName, PlayerTurn.PLAYER_ONE);
    }

    public ExampleAgentOscar(String agentName, PlayerTurn playerTurn) {
        super(agentName, playerTurn);
    }

    @Override
    public AgentMove getMove(GameBoardState gameState) {
        //Vi måste få den här metoden att veta vilken childNode den ska välja baserat på den integer som maxValue returnerar.
        //Kanske behöver vi lägga till en variabel i GameBoardState som sparar det statets utilityValue??
        //TODO: FORTSÄTT HÄR
        for (GameBoardState state: gameState.getChildStates()) {
        }
        ExampleMove move = new ExampleMove();
        int max = maxValue(gameState);
        return null;
    }

    private int maxValue(GameBoardState gameState) {
        int highestMinimaxValue = Integer.MIN_VALUE;

        if (gameState.isTerminal()) {
            return utility(gameState);
        }
        else {

            for (int i = 0; i < gameState.getChildStates().size(); i++) {
                int currentMinimaxValue = minValue(gameState.getChildStates().get(i)); //Hämtar värdet ELLER fortsätter ner i trädet if != terminal
                if (currentMinimaxValue > highestMinimaxValue) {
                    highestMinimaxValue = currentMinimaxValue;
                }
            }
        }
        return highestMinimaxValue;
    }

    private int minValue(GameBoardState gameState) {
        int lowestMinimaxValue = Integer.MAX_VALUE;

        if (gameState.isTerminal()) {
            return utility(gameState);
        }
        else {

            for (int i = 0; i < gameState.getChildStates().size(); i++) {
                int currentMinimaxValue = maxValue(gameState.getChildStates().get(i)); //Hämtar värdet ELLER fortsätter ner i trädet if != terminal
                if (currentMinimaxValue < lowestMinimaxValue) {
                    lowestMinimaxValue = currentMinimaxValue;
                }
            }
        }
        return lowestMinimaxValue;
    }

    private int utility(GameBoardState gameState) {
        int utilityValue = 0;
        GameBoardCell[][] gameBoardCells = gameState.getCells();

        for (int row = 0; row < gameBoardCells.length; row++) {
            for (int col = 0; col < gameBoardCells[row].length; col++) {
                if (gameBoardCells[row][col].getCellState().equals(BoardCellState.WHITE)) {
                    utilityValue += AgentController.WEIGHT_MATRIX[col][row];
                }
            }
        }
        return utilityValue;
    }
}
