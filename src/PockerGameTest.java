import java.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class PokerGameTest {
    private record TestCase(String caseName, List<String> cards, String expected) {
    }

    @Nested
    class ConstructorTest {
        @Test
        void major() {
            // given
            final List<String> cards = Arrays.asList("H1", "H2", "H3", "H4", "H13");
            // when
            final PokerGame pokerGame = new PokerGame(cards);
            // then
            assertEquals(cards, pokerGame.cards());
        }

        @Test
        void invalidPatterns() {
            // given
            Arrays.asList(
                    new TestCase("InvalidTooManyCards", Arrays.asList("H1", "H2", "H3", "H4", "H5", "H6"), "Invalid number of cards"),
                    new TestCase("NotEnoughCards", Arrays.asList("H1", "H2", "H3", "H4"), "Invalid number of cards"),
                    new TestCase("InvalidNumber", Arrays.asList("H1", "H2", "H14", "H4", "H5"), "Invalid card number"),
                    new TestCase("InvalidNumber", Arrays.asList("H1", "H2", "H0", "H4", "H5"), "Invalid card number"),
                    new TestCase("InvalidSuit", Arrays.asList("H1", "H2", "A3", "H4", "H5"), "Invalid card suit"),
                    new TestCase("InvalidNumber", Arrays.asList("H1", "S2", "HH3", "H4", "H5"), "Invalid card number")
            ).forEach(testCase -> {
                //when
                try {
                    new PokerGame(testCase.cards);
                    fail();
                } catch (IllegalArgumentException e) {
                    // then
                    assertEquals(testCase.expected, e.getMessage());
                }
            });
        }
    }

    @Nested
    class NoPairTest {
        @Test
        void patterns() {
            //given
            Arrays.asList(
                    new TestCase("major", Arrays.asList("H1", "H2", "H3", "H4", "C6"), "No Pair"),
                    new TestCase("NotStraight", Arrays.asList("C11", "H12", "S13", "D1", "C2"), "No Pair")
            ).forEach(testCase -> {
                //when
                String actual = new PokerGame(testCase.cards).judge();
                //then
                assertEquals(testCase.expected, actual, testCase.caseName);
            });
        }
    }

    @Nested
    class OnePairTest {
        @Test
        void patterns() {
            //given
            Arrays.asList(
                    new TestCase("major", Arrays.asList("H1", "C1", "H3", "H4", "C6"), "One Pair"),
                    new TestCase("lastTwoCard", Arrays.asList("C2", "H1", "S3", "D4", "H4"), "One Pair"),
                    new TestCase("centerTwoCard", Arrays.asList("C5", "H7", "S11", "D11", "C8"), "One Pair")
            ).forEach(testCase -> {
                //when
                String actual = new PokerGame(testCase.cards).judge();
                //then
                assertEquals(testCase.expected, actual, testCase.caseName);
            });
        }
    }

    @Nested
    class TwoPairTest {
        @Test
        void patterns() {
            //given
            Arrays.asList(
                    new TestCase("major", Arrays.asList("H1", "C1", "H3", "D3", "C6"), "Two Pair"),
                    new TestCase("RandomOrder", Arrays.asList("C2", "H13", "S3", "D13", "D2"), "Two Pair")
            ).forEach(testCase -> {
                //when
                String actual = new PokerGame(testCase.cards).judge();
                //then
                assertEquals(testCase.expected, actual, testCase.caseName);
            });
        }
    }

    @Nested
    class ThreeOfAKindTest {
        @Test
        void patterns() {
            //given
            Arrays.asList(
                    new TestCase("major", Arrays.asList("H1", "C1", "S1", "D3", "C6"), "Three of a Kind"),
                    new TestCase("RandomOrder", Arrays.asList("C12", "H12", "S3", "D12", "C6"), "Three of a Kind")
            ).forEach(testCase -> {
                //when
                String actual = new PokerGame(testCase.cards).judge();
                //then
                assertEquals(testCase.expected, actual, testCase.caseName);
            });
        }
    }

    @Nested
    class StraightTest {
        @Test
        void patterns() {
            //given
            Arrays.asList(
                    new TestCase("major", Arrays.asList("H1", "C2", "S3", "D4", "C5"), "Straight"),
                    new TestCase("RandomOrder", Arrays.asList("C2", "H1", "S3", "D4", "C5"), "Straight"),
                    new TestCase("RandomOrderStart5", Arrays.asList("C5", "H7", "S6", "D9", "C8"), "Straight"),
                    new TestCase("RandomOrderFinish1", Arrays.asList("C11", "H13", "S10", "D12", "C1"), "Straight")
            ).forEach(testCase -> {
                //when
                String actual = new PokerGame(testCase.cards).judge();
                //then
                assertEquals(testCase.expected, actual, testCase.caseName);
            });
        }
    }

    @Nested
    class FlushTest {
        @Test
        void patterns() {
            //given
            Arrays.asList(
                    new TestCase("major", Arrays.asList("H1", "H2", "H3", "H4", "H6"), "Flush"),
                    new TestCase("RandomOrder", Arrays.asList("D11", "D2", "D9", "D4", "D6"), "Flush")
            ).forEach(testCase -> {
                //when
                String actual = new PokerGame(testCase.cards).judge();
                //then
                assertEquals(testCase.expected, actual, testCase.caseName);
            });
        }
    }

    @Nested
    class FullHouseTest {
        @Test
        void patterns() {
            //given
            Arrays.asList(
                    new TestCase("beforePair", Arrays.asList("D1", "H1", "D3", "H3", "S3"), "Full House"),
                    new TestCase("afterPair", Arrays.asList("D1", "H1", "S1", "H3", "S3"), "Full House"),
                    new TestCase("randomOrder", Arrays.asList("D11", "H13", "S11", "H13", "C11"), "Full House")
            ).forEach(testCase -> {
                //when
                String actual = new PokerGame(testCase.cards).judge();
                //then
                assertEquals(testCase.expected, actual, testCase.caseName);
            });
        }
    }

    @Test
    void fourOfAKindTest() {
        List<String> cards = Arrays.asList("H1", "C1", "S1", "D1", "C6");
        String actual = new PokerGame(cards).judge();
        assertEquals("Four of a Kind", actual);
    }

    @Nested
    class StraightFlushTest {
        @Test
        void patterns() {
            //given
            Arrays.asList(
                    new TestCase("major", Arrays.asList("H1", "H2", "H3", "H4", "H5"), "Straight Flush"),
                    new TestCase("RandomOrder", Arrays.asList("C9", "C12", "C11", "C10", "C13"), "Straight Flush")
            ).forEach(testCase -> {
                //when
                String actual = new PokerGame(testCase.cards).judge();
                //then
                assertEquals(testCase.expected, actual, testCase.caseName);
            });
        }
    }

    @Nested
    class RoyalStraightFlushTest {
        @Test
        void patterns() {
            //given
            Arrays.asList(
                    new TestCase("major", Arrays.asList("H1", "H10", "H11", "H12", "H13"), "Royal Straight Flush"),
                    new TestCase("RandomOrder", Arrays.asList("C13", "C10", "C11", "C12", "C1"), "Royal Straight Flush")
            ).forEach(testCase -> {
                //when
                String actual = new PokerGame(testCase.cards).judge();
                //then
                assertEquals(testCase.expected, actual, testCase.caseName);
            });
        }
    }

}