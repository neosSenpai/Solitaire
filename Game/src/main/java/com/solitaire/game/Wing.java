package com.solitaire.game;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Wing extends JLayeredPane {
    private BufferedImage image; //background image for the wing panel
    private int y_Loc = 20;
    private int i = 1;
    private boolean empty;

    public Wing(Deck d, ArrayList<Foundation> f){
        try {
            image = ImageIO.read(new File("src/main/resources/foundBack.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setStarterCard(d,f);
    }

    /**
     * Sets the card that the wings start with
     * @param d deck is needed to draw the beginning card
     */
    private void setStarterCard(Deck d, ArrayList<Foundation> f){
        Card starterCard = (Card) d.draw();
        Foundation foundation = f.get(0);
        if (starterCard.getCardValue() == foundation.getFoundationStarterRank()){
            d.addCardBackToDeck(starterCard);
            setStarterCard(d,f);
        }
        starterCard.setBounds(0,0,100,145);
        this.add(starterCard,Integer.valueOf(0));
    }

    /**
     * Removes a card from the Wing pile
     * @param c Card to remove from the Wing
     */
    public void removeCard(Card c){
        this.remove(c);
        y_Loc -= 20;
        i--;
        if (i == 0){
            empty = true;
            i = 0;
        }
        repaint();
    }

    public void addCard(Card c){
        c.setBounds(0,y_Loc,100,145);
        this.add(c,Integer.valueOf(i));
        y_Loc += 20;
        i++;
    }

    public Boolean checkValidMove(Card c){
        try{
            Component topComp = this.getComponent(lowestLayer());
            Card topCard = (Card) topComp;
            int valid = topCard.getCardValue()-1;
            // If top card is Ace, set the valid card to add on be a two
            if (valid == 15){
                valid = 2;
            }
            int value = c.getCardValue();
            String suit = c.getSuit();
            if (valid == value && topCard.getSuit() == suit){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }

    }

    public boolean isEmpty(){
        return empty;
    }

    /**
     * Paints the background of the Wing Panel
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
    }


}
