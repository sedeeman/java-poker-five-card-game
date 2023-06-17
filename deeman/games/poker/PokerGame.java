package deeman.games.poker;

import java.util.*;
import java.util.function.Consumer;

public class PokerGame {

    private final List<Card> deck = Card.getStandardDeck();
    private int playerCount;
    private int cardsInHand;
    private List<PokerHand> pokerHands;
    private List<Card> remainingCards;

    public PokerGame(int playerCount, int cardsInHand) {
        this.playerCount = playerCount;
        this.cardsInHand = cardsInHand;
        pokerHands = new ArrayList<>(cardsInHand);
    }

    public void startGame() {

        Collections.shuffle(deck);
        deck.sort(Card.sortByReverseRank());
        int randomMiddle = new Random().nextInt(25, 35);
        Collections.rotate(deck, randomMiddle);

        deal();

        System.out.println("---------------------Play Game--------------------------------");

        // Consumer accept single argument
        Consumer<PokerHand> checkedHand = PokerHand::evalHand;
        ;
        pokerHands.forEach(checkedHand.andThen(System.out::println));

        int dealtCards = playerCount * cardsInHand;
        int leftCards = deck.size() - dealtCards;

        /*replaceAll supports if and only list is not empty -> remainingCards have null values*/
        remainingCards = new ArrayList<>(Collections.nCopies(leftCards, null));
        remainingCards.replaceAll(rCards -> deck.get(dealtCards + remainingCards.indexOf(rCards)));
        Card.printDeck(remainingCards, "Remaining Cards", 2);

        /*using sublist*/
//        remainingCards = new ArrayList<>(deck.subList(dealtCards, deck.size()));
//        Card.printDeck(remainingCards, "Remaining Cards", 2);

    }

    public void deal() {
        Card[][] hands = new Card[playerCount][cardsInHand];

        for (int i = 0, deckIndex = 0; i < playerCount; i++) {
            for (int j = 0; j < cardsInHand; j++) {
                hands[i][j] = deck.get(deckIndex++);
            }
        }

        int player = 1;
        for (Card[] hand : hands) {
            pokerHands.add(new PokerHand(player++, Arrays.asList(hand)));
        }
    }


}