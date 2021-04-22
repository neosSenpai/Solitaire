package com.mycompany.solitaire;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Card extends JLabel{
    private Suit suit;
    private Rank rank;
    private BufferedImage image;
    private BufferedImage backOfCard;
    private Boolean faceDown;
    /**
     * Suit of the Card Object
     * EX: CLUBS,HEARTS,...
     */
    public enum Suit{
        CLUBS("Clubs"),HEARTS("Hearts"),SPADES("Spades"),DIAMONDS("Diamonds");

        // String of the suit, used to get the cards image location
        private final String suitString;

        /**
         * Suit of the card
         * @param s string of the suit used for the suit's image location
         */
        Suit(String s){
            suitString = s;
        }

        /**
         * Returns the suit as a string
         * EX: If the card is CLUBS the getSuitString() will return "Clubs"
         * @return suitString, the name of the suit as a string
         */
        public String getSuitString(){
            return suitString;
        }
    }

    /**
     * Rank of the card
     * EX: TWO,THREE,FOUR,FIVE,....,QUEEN,KING,ACE
     */
    public enum Rank{
        TWO(2,"Two"), THREE(3,"Three"), FOUR(4,"Four"), FIVE(5,"Five"), SIX(6,"Six"), SEVEN(7,"Seven"), EIGHT(8,"Eight"), NINE(9,"Nine"),TEN(10,"Ten"),JACK(11,"Jack"),QUEEN(12,"Queen"),KING(13,"King"),ACE(14,"Ace");

        // the integer value of the Rank
        private final int cardValue;
        // the string value of the Rank, used to get the cards image location
        private final String rankString;

        /**
         * Rank of the card object
         * @param val integer value assigned to the Rank
         * @param s string value assigned to the Rank, used to get Image Location
         */
        Rank(int val,String s) {
            cardValue = val;
            rankString = s;
        }



        /**
         * Returns the Rank's string value
         * EX: If the card is an ACE the getRankString() will return "Ace"
         * @return string rankString
         */
        public String getRankString() {
            return rankString;
        }
    }

    /**
     * Card object constructor
     * @param rank
     * @param suit
     */
    public Card(Rank rank,Suit suit){
        this.rank = rank;
        this.suit = suit;
        faceDown = true;
        // Set the Image of the Card
        if (faceDown){
            turnFaceDown();
        }else {
            turnFaceUp();
        }
    }


    /**
     * Turns the card object face up
     */
    public void turnFaceUp(){
        faceDown = false;
        try{
            image = ImageIO.read(new File("src/main/resources/images/"+ rank.getRankString() + "_" + suit.getSuitString() + ".png"));
        } catch (IOException e){
            e.printStackTrace();
        }
        this.setIcon(new ImageIcon(image));
    }

    /**
     * Turns the card object face down
     */
    public void turnFaceDown(){
        faceDown = true;
        try{
            backOfCard = ImageIO.read(new File("src/main/resources/images/back.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }

        this.setIcon(new ImageIcon(backOfCard));
    }

    /**
     * Returns the name of the Card as a String
     * @return String name
     */
    public String getCardName(){
        return  this.rank.getRankString() + "_" + this.suit.getSuitString();
    }


    public int getCardValue(){
        return this.rank.cardValue;
    }

    public String getSuit(){
        return this.suit.getSuitString();
    }




}
