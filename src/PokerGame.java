import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

class PokerGame {

    public List<String> cards() {
        return this.cards.items;
    }

    private final Cards cards;

    public PokerGame(List<String> cards) {
        if (cards.size() != 5) {
            throw new IllegalArgumentException("Invalid number of cards");
        }

        cards.forEach((card) -> {
            String number = card.substring(1);
            String suit = card.substring(0, 1);

            if (!List.of("H", "C", "D", "S").contains(suit)) {
                throw new IllegalArgumentException("Invalid card suit");
            }

            if(!number.matches("[0-9]+")) {
                throw new IllegalArgumentException("Invalid card number");
            }

            if (Integer.parseInt(number) < 1 || Integer.parseInt(number) > 13) {
                throw new IllegalArgumentException("Invalid card number");
            }
        });

        this.cards = new Cards(cards);
    }

    private record Cards(List<String> items) {
        private HashMap<String, Integer> generateCardNumberCount() {
            HashMap<String, Integer> cardNumberCount = new HashMap<>();
            this.items.forEach((card) -> {
                String number = card.substring(1);
                if (cardNumberCount.containsKey(number)) {
                    cardNumberCount.put(number, cardNumberCount.get(number) + 1);
                } else {
                    cardNumberCount.put(number, 1);
                }
            });
            return cardNumberCount;
        }

        private HashMap<String, Integer> generateCardSuitCount() {
            HashMap<String, Integer> cardSuitCount = new HashMap<>();
            this.items.forEach((card) -> {
                String suit = card.substring(0, 1);
                if (cardSuitCount.containsKey(suit)) {
                    cardSuitCount.put(suit, cardSuitCount.get(suit) + 1);
                } else {
                    cardSuitCount.put(suit, 1);
                }
            });
            return cardSuitCount;
        }

        private List<Integer> generateCardNumbers() {
            List<Integer> cardNumbers =
                    new java.util.ArrayList<>(this.items.stream().map(card -> Integer.valueOf(card.substring(1))).toList());
            cardNumbers.sort(Integer::compareTo);
            return cardNumbers;
        }

        private boolean hasSameCount(int count) {
            return this.generateCardNumberCount().containsValue(count);
        }

        private int pairCount() {
            return this.generateCardNumberCount().values().stream().filter(count -> count == 2).toList().size();
        }

        private boolean isStraight() {
            List<Integer> cardNumbers = this.generateCardNumbers();
            int compareNumber = cardNumbers.get(0);
            for (Integer cardNumber : cardNumbers) {
                if (compareNumber != cardNumber) {
                    return false;
                }
                compareNumber++;
            }
            return true;
        }

        private boolean isRoyalStraight() {
            List<Integer> cardNumbers = this.generateCardNumbers();
            return new HashSet<>(cardNumbers).containsAll(List.of(1, 10, 11, 12, 13));
        }

        private boolean isFlush() {
            return this.generateCardSuitCount().containsValue(5);
        }
    }

    public String judge() {

        boolean isRoyalStraight = this.cards.isRoyalStraight();
        boolean isFlush = this.cards.isFlush();
        if (isRoyalStraight && isFlush) {
            return "Royal Straight Flush";
        }

        boolean isStraight = this.cards.isStraight();
        boolean isStraightOrRoyalStraight = isStraight || isRoyalStraight;

        if (isStraight && isFlush) {
            return "Straight Flush";
        }

        if (isStraightOrRoyalStraight) {
            return "Straight";
        }

        if (this.cards.hasSameCount(3) && this.cards.hasSameCount(2)) {
            return "Full House";
        }

        if (isFlush) {
            return "Flush";
        }

        if (this.cards.hasSameCount(4)) {
            return "Four of a Kind";
        }

        if (this.cards.hasSameCount(3)) {
            return "Three of a Kind";
        }

        if (this.cards.pairCount() == 2) {
            return "Two Pair";
        }

        if (this.cards.pairCount() == 1) {
            return "One Pair";
        }

        return "No Pair";
    }
}