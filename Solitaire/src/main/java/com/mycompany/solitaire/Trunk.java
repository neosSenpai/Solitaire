package com.mycompany.solitaire;

import com.mycompany.solitaire.Foundation;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Trunk extends JLayeredPane {
    private int x_Location;
    private int y_Location;
    private int position;
    private Boolean empty;
    private Card topCard;
    private int trunkCardsLeft = 13;

    public Trunk(Deck d,ArrayList<Foundation> f){
        this.setVisible(true);
        this.setOpaque(false);
        populateTrunk(d);
        getTrunkToFoundation(f);
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
            Card trunkCard = d.draw();
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



    public Card getTrunkToWing(Wing wing,ArrayList<Foundation> f){
        Card c = (Card) this.getComponent(lowestLayer());
        this.remove(c);
        trunkCardsLeft--;
        if (trunkCardsLeft == 0){
            return c;
        }
        Component comp = this.getComponent(lowestLayer());
        if (comp instanceof Card){
            topCard = (Card) this.getComponent(lowestLayer());
            topCard.turnFaceUp();
            getTrunkToFoundation(f);
        }
        return c;
    }

    public void getTrunkToFoundation(ArrayList<Foundation> found){
        Card card = (Card) this.getComponent(lowestLayer());
        if (card.getCardValue() == found.get(0).getFoundationStarterRank()){
            for (Foundation f: found){
                if (f.isEmpty()){
                    this.remove(card);
                    trunkCardsLeft --;
                    f.addStarterCard(card);
                    Card c = (Card) this.getComponent(lowestLayer());
                    c.turnFaceUp();
                    this.revalidate();
                    this.repaint();
                    return;
                }
            }
        }
    }


}
