package deeman.games.poker;

import java.util.*;

public class PokerHand {

    private List<Card> hand;
    private List<Card> keepers;
    private List<Card> discards;
    private int playerNo;
    private Rank score = Rank.NONE;

    public PokerHand(int playerNo, List<Card> hand) {
        hand.sort(Card.sortByReverseRank());
        this.playerNo = playerNo;
        this.hand = hand;
        this.keepers = new ArrayList<>(hand.size());
        this.discards = new ArrayList<>(hand.size());
    }

    private void setRank(int faceCount) {
        switch (faceCount) {
            case 4 -> score = Rank.FOUR_OF_A_KIND;
            case 3 -> {
                if (score == Rank.NONE) score = Rank.THREE_OF_A_KIND;
                else score = Rank.FULL_HOUSE;
            }
            case 2 -> {
                if (score == Rank.NONE) score = Rank.ONE_PAIR;
                else if (score == Rank.THREE_OF_A_KIND) score = Rank.FULL_HOUSE;
                else score = Rank.TWO_PAIR;
            }
        }
    }

    public void evalHand() {
        List<String> faceList = new ArrayList<>(hand.size());
        hand.forEach(c -> faceList.add(c.face()));
        List<String> duplicateFaceCards = new ArrayList<>();
        faceList.forEach(face -> {
            if (!duplicateFaceCards.contains(face) && Collections.frequency(faceList, face) > 1) {
                duplicateFaceCards.add(face);
            }
        });

        for (String duplicate : duplicateFaceCards) {
            int first = faceList.indexOf(duplicate);
            int last = faceList.lastIndexOf(duplicate);
            setRank(last - first + 1);// set rank to duplicate face cards we have
            List<Card> sub = hand.subList(first, last + 1);
            keepers.addAll(sub);
        }

        pickDiscards();

    }

    public void pickDiscards() {
        List<Card> temp = new ArrayList<>(hand);
        temp.removeAll(keepers);
        int rankedCards = keepers.size();
        Collections.reverse(temp);
        int index = 0;
        for (Card c : temp) {
            if (index++ < 3 && (rankedCards > 2 || c.rank() < 9))
                discards.add(c);
            else
                keepers.add(c);

        }
    }


    @Override
    public String toString() {
        return "%d. %-16s Rank:%d %-40s Best:%-6s Worst:%-6s %s".formatted(
                playerNo,
                score,
                score.ordinal(),
                hand,
                Collections.max(hand, Comparator.comparing(Card::rank)),
                Collections.min(hand, Comparator.comparing(Card::rank)),
                (discards.size() > 0) ? "Discards " + discards : "");
    }
}
