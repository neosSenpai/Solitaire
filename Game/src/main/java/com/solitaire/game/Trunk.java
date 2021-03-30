package com.solitaire.game;
import javax.swing.*;
import java.awt.*;

public class Trunk extends JLayeredPane {
    private int x_Location;
    private int y_Location;
    private int position;
    public Trunk(Deck d){
        this.setVisible(true);
        this.setOpaque(false);
        populateTrunk(d);

    }

    /**
     * Populates the trunk with 13 Card Objects
     * @param d Deck d used to draw 13 Card Objects
     */
    private void populateTrunk(Deck d){
        x_Location = 0; // x location of the
        y_Location = 0;
        position = 0;
        for(int i = 0; i < 13; i++){
            Card trunkCard = (Card) d.draw();
            if (i == 12){
                trunkCard.setBounds(x_Location,y_Location,100,145);
                this.add(trunkCard, Integer.valueOf(position));
                return;
            }
            trunkCard.turnFaceDown();
            trunkCard.setBounds(x_Location,y_Location,100,145);
            this.add(trunkCard, Integer.valueOf(position));
            y_Location += 10;
            position++;
        }
    }

    /**
     * Removes the top card from the trunk and turns the card below it face up
     * @param
     */
    public void removeCardFromTrunk(){
        Component compon = this.getComponent(lowestLayer());
        Card c = (Card) compon;
        this.remove(c);
        Component comp = this.getComponent(lowestLayer());
        if (comp instanceof Card){
            Card card = (Card) this.getComponent(lowestLayer());
            card.turnFaceUp();
        }
    }

    public void returnCardToTrunk(Card c){
        Card card = (Card) this.getComponent(lowestLayer());
        card.turnFaceDown();
        c.setBounds(0,y_Location,100,145);
        this.add(c);
        c.turnFaceUp();
        this.revalidate();
        this.repaint();
    }

    public Card getTrunkToWing(Wing wing){
        Card c = (Card) this.getComponent(lowestLayer());
        this.remove(c);
        Component comp = this.getComponent(lowestLayer());
        if (comp instanceof Card){
            Card card = (Card) this.getComponent(lowestLayer());
            card.turnFaceUp();
        }
        return c;
    }
}
