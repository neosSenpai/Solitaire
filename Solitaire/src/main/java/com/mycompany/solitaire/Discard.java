package com.mycompany.solitaire;

import com.mycompany.solitaire.Card;
import javax.swing.*;
import java.util.ArrayList;


public class Discard extends JLayeredPane {
    private int mostRecentRemovedLayer;
    private int i = 0;

    public Discard() {
        this.setBounds(150, 20, 120, 170);
    }

    public void addCard(Card c) {
        c.setBounds(0, 0, 100, 145);
        this.add(c, Integer.valueOf(i));
        this.revalidate();
        this.repaint();
        i++;
    }

    public void removeCard(Card c) {
        mostRecentRemovedLayer = this.getLayer(c);
        i--;
        this.remove(c);
    }

    public int getMostRecentRemovedLayer() {
        return mostRecentRemovedLayer;
    }

    public int topCardRank() {
        Card c = (Card) this.getComponent(i);
        return c.getCardValue();
    }




}
