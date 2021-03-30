package com.solitaire.game;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Foundation extends JPanel {
    private BufferedImage image; //background image for the foundation panels
    private Card starterCard; //card that starts the foundation
    private Card topCard;
    private CardLayout cLayout = new CardLayout();
    private boolean isEmpty;
    private boolean full;
    /**
     * A foundation that is the starter foundation and it must draw a card from the deck
     * @param starterCard card that starts the foundation
     */
    public Foundation(Card starterCard){
        this.setLayout(cLayout);
        try {
            image = ImageIO.read(new File("src/main/resources/foundBack.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setOpaque(false);
        this.starterCard = starterCard;
        this.topCard = starterCard;
        this.addCardToFoundation(starterCard);
        this.isEmpty = false;
    }

    /**
     * An empty foundation with no card/cards to start the game
     */
    public Foundation(){
        this.isEmpty = true;
        this.setLayout(cLayout);
        try {
            image = ImageIO.read(new File("src/main/resources/foundBack.png"));
        } catch (IOException ex) {
            // handle exception...
        }
        this.setOpaque(false);
    }

    /**
     * Get the int of the starter card for the foundation to build up by
     * @return
     */
    public int getFoundationStarterRank(){
        return starterCard.getCardValue();
    }

    /**
     * Adds a cards to the foundation
     * @param card
     */
    public void addCardToFoundation(Card card){
        this.add(card);
        this.topCard = card;
        cLayout.next(this);
        this.revalidate();
        this.repaint();
    }

    public void addStarterCard(Card c){
        this.add(c);
        this.topCard = c;
        this.starterCard = c;
        this.isEmpty = false;
        cLayout.next(this);
        this.revalidate();
        this.repaint();
    }

    public Boolean checkValidMove(Card card){
        Card top = this.getTopCard();
        try{
            int vaild = top.getCardValue()+1;

            // If top card is Ace, set the vaild card to add on be a two
            if (vaild == 15){
                vaild = 2;
            }
            int value = card.getCardValue();
            String suit = card.getSuit();
            if (vaild == value && topCard.getSuit() == suit){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }



    }
    public Card getTopCard(){
        return topCard;
    }

    public boolean full(){
        return full;
    }

    /**
     * Returns if the foundation is empty
     * @return
     */
    public boolean isEmpty(){
        return isEmpty;
    }
    /**
     * Paints the background of the JPanel
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
    }
}
