package com.mycompany.solitaire;

import javax.swing.*;
import java.awt.*;

public class Score extends JLabel {

    private int playerScore;

    public Score(){
        playerScore = 0;
        this.setBounds(900,15,200,50);
        this.setText("Score: " + String.valueOf(playerScore));
        this.setFont (this.getFont ().deriveFont (25f));
        this.setForeground(Color.WHITE);
        this.setOpaque(false);
    }

    public int getPlayerScore() {
        return playerScore;
    }

    protected void addToPlayerScore(){
        playerScore += 50;
        this.setText("Score: " + String.valueOf(playerScore));
        repaint();
        revalidate();
    }
}
