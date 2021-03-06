package fr.wildcodeschool.poker;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PokerHand {

    private List<Card> cards = new ArrayList<Card>();

    public PokerHand (List<Card> cards){
        this.cards = cards;
    }

    public PokerHand (String hand){
        //on split la string sur les espaces
        String [] handSplitted = hand.split(" ");

        //reconnnaitre dans chaque substring la valeur et la couleur
        Card currentCard;
        // Suit.parse('S') => une valeur Spade
        for (String handSplit : handSplitted) {
            CardValue cardValue = CardValue.parse(handSplit.charAt(0));
            CardSuit cardSuit = CardSuit.parse(handSplit.charAt(1));
            currentCard = new Card(cardValue, cardSuit);
            cards.add(currentCard);
        }
        //on cree l'objet Card pour le mettre dans la liste

    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    // une main possède une combinaison et éventuellement un reste
    // trier les cartes de la main de la plus haute valeur à la plus basse
    // comparer les couleurs des cartes
    // si toutes ont la même couleur
    // comparer les valeurs
    // si valeur carte n+1 = valeur carte n, bouleén 2
    // donner une valeur à chaque combinaison

    public CombinationType getBestCombination() {
        return CombinationType.fromPokerHand(this);
    }

    public List<Card> getKicker() {
        Combination combination = new Combination(this);
        List<Card> combinationCards = combination.getCards();
        return getCards().stream()
                .filter(c -> !combinationCards.contains(c))
                .collect(Collectors.toList());
    }

    public Result compareWith(PokerHand candidate) {
        Result res = null;
        Combination currentCombination = new Combination(this);
        Combination candidateCombination = new Combination(candidate);

        int resComp = currentCombination.compareTo(candidateCombination);
        if ( resComp == 0) {
            resComp = compareKickers(candidate.getKicker());
        }
        if (resComp == 0) {
            res = Result.TIE;
        } else if (resComp > 0) {
            res = Result.WIN;
        } else {
            res = Result.LOSS;
        }
        return res;
    }

    /**
     * Compare le kicker de la main courante avec un autre kicker
     * @param candidateKicker
     * @return
     */
    private int compareKickers(List<Card> candidateKicker) {
        return Card.compareCards(getKicker(), candidateKicker);
    }

}
