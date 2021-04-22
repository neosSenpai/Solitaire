package com.mycompany.solitaire;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Deck extends JLayeredPane {
    private ArrayList<Card> cards;
    private Boolean isEmpty;
    private BufferedImage emptyDeckImage;
    private JLabel emptyDeck;
    private int position = 0;
    private int x_offset;
    private int y_offset;


    /**
     * Creates a new deck of 52 cards and shuffles them
     */
    public Deck() {
        this.setBounds(20,20,120,155);
        isEmpty = false;
        populateDeck();
        shuffle();
        drawDeck();
    }

    /**
     * Populates the deck with 52 card objects
     */
    private void populateDeck(){
        cards = new ArrayList<Card>();
        for (Card.Suit suit: Card.Suit.values()){
            for (Card.Rank rank : Card.Rank.values()){
                cards.add(new Card(rank,suit));
            }
        }
    }

    /**
     * Shuffles the deck of card objects
     */
    private void shuffle(){
        Collections.shuffle(cards);
    }

    /**
     * Returns the size of the deck depending on how many card objects are in the deck
     * @return int cards.size
     */
    public int getDeckSize(){
        return cards.size();
    }

    /**
     * Draws the deck to the table JPanel
     */
    private void drawDeck(){
        this.setPreferredSize(new Dimension(150,185)); //dimension of the deck's JLayeredPane
        x_offset = 15; // the initial offset for x
        y_offset = 0; // the initial offset for y

        for (Card c : cards){
            c.setBounds(x_offset,y_offset,100,145); //set the b
            this.add(c, Integer.valueOf(position));
            if (position % 10 == 0){
                x_offset -= 1;
                y_offset += 2;
            }
            position++;
        }
    }

    /**
     * Draws a card to the Discard pile
     * @param discard  the Discard class which extends JLayeredPane
     */
    public void drawCardToDiscard(Discard discard,ArrayList<Foundation> f){
        // If a JLayered pane root pane set deck to empty
        if (highestLayer() == 0){
            isEmpty = true;
        }
        // get the Card Object at the top of the deck
        Card topCard = cards.get(cards.size()-1);
        Foundation foundation = f.get(0);
        if (topCard.getCardValue() == foundation.getFoundationStarterRank()){
            topCard.turnFaceUp();
            discard.addCard(topCard);
            this.remove(topCard);
            cards.remove(topCard);
            position--;
            for (Foundation found: f){
                if (found.isEmpty()){
                    discard.removeCard(topCard);
                    discard.revalidate();
                    discard.repaint();
                    found.addStarterCard(topCard);
                    return;
                }
            }
        }else {
            topCard.turnFaceUp();
            discard.addCard(topCard);
            this.remove(topCard);
            cards.remove(topCard);
            position--;
        }
        if(isEmpty){
            drawEmpty();
            return;
        }
    }



    /**
     * Draws a card from the top of deck and returns the Card object drawn
     * @return Card
     */
    public Card draw(){
        Card topCard = cards.get(cards.size()-1);
        topCard.turnFaceUp();
        this.remove(topCard);
        cards.remove(topCard);
        position--;
        return topCard;
    }

    public void addCardBackToDeck(Card c){
        c.turnFaceDown();
        this.add(c,Integer.valueOf(this.lowestLayer()));
        position++;
    }

    public boolean isEmpty(){
        return isEmpty;
    }

    /**
     * Draws an image when the deck is empty
     */
    public void drawEmpty(){
        if (isEmpty){
            // set the image of the empty deck
            try {
                emptyDeckImage = ImageIO.read(new File("src/main/resources/images/placeholder.png"));
            }catch (IOException e){
                e.printStackTrace();
            }
            // create the image JLabel and set the icon and bounds
            emptyDeck = new JLabel();
            emptyDeck.setIcon(new ImageIcon(emptyDeckImage));
            emptyDeck.setVisible(true);
            emptyDeck.setBounds(x_offset+15,y_offset-5,100,140);
            // add the image to the Deck JLayeredPane
            this.add(emptyDeck);
            this.revalidate();
            this.repaint();
        }else {
            return;
        }
    }
    public void redeal(Discard d){
        this.remove(emptyDeck);
        this.revalidate();
        this.repaint();
        x_offset = 15; // the initial offset for x
        y_offset = 0; // the initial offset for y
        position = 0;
        for (Component comp : d.getComponents()){
            if (comp instanceof Card){
                Card c = (Card) comp;
                c.turnFaceDown();
                cards.add(c);
                d.remove(c);
                d.revalidate();
                d.repaint();
            }
        }
        for (Card c : cards){
            c.setBounds(x_offset,y_offset,100,145); //set the b
            this.add(c, Integer.valueOf(position));
            if (position % 10 == 0){
                x_offset -= 1;
                y_offset += 2;
            }
            position++;
        }

        isEmpty = false;
        this.revalidate();
        this.repaint();
    }



}
