package deeman;

import deeman.games.poker.PokerGame;

public class GameController {
    public static void main(String[] args) {

        PokerGame fiveCardGrow = new PokerGame(8, 4);
        fiveCardGrow.startGame();
    }
}
