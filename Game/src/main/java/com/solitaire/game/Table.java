package com.solitaire.game;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Table extends JLayeredPane {

    private BufferedImage tableBackground;
    private String imageLocation;


    public Table(Discard discard, Trunk trunk, ArrayList<Foundation> f, ArrayList<Wing> w) {
        setLayout(null);
        imageLocation = "src/main/resources/background.jpg";
        try {
            tableBackground = ImageIO.read(new File(imageLocation));
            Graphics g2d = tableBackground.createGraphics();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final MyMouseAdapter myMouseAdapter = new MyMouseAdapter(this, discard, trunk, f, w);
        addMouseListener(myMouseAdapter);
        addMouseMotionListener(myMouseAdapter);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1600, 900);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(tableBackground, 0, 0, tableBackground.getWidth(), tableBackground.getHeight(), null);
    }


    class MyMouseAdapter extends MouseAdapter {
        private Card selectedCard = null;
        private JLayeredPane cardGameTable = null;
        private Discard discardPile = null;
        private ArrayList<Foundation> foundation = null;
        private ArrayList<Wing> wing = null;
        private Trunk trunk = null;
        private Component compOfOrigin;
        private int dragLabelWidthDiv2;
        private int dragLabelHeightDiv2;
        private int Origin_x;
        private int Origin_y;

        public MyMouseAdapter(JLayeredPane gameTable, Discard discardPile, Trunk trunk, ArrayList<Foundation> foundation, ArrayList<Wing> wing) {
            this.cardGameTable = gameTable;
            this.discardPile = discardPile;
            this.foundation = foundation;
            this.wing = wing;
            this.trunk = trunk;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Component comp = cardGameTable.getComponentAt(e.getPoint());
            compOfOrigin = comp;
            Origin_x = comp.getX();
            Origin_y = comp.getY();
            if (comp instanceof Discard) {
                try {
                    Card card = (Card) discardPile.getComponent(lowestLayer());
                    selectedCard = card;
                    if (selectedCard != null) {
                        discardPile.removeCard(card);
                        discardPile.revalidate();
                        discardPile.repaint();
                        // Center Card
                        dragLabelWidthDiv2 = selectedCard.getWidth() / 2;
                        dragLabelHeightDiv2 = selectedCard.getHeight() / 2;
                        int x = e.getPoint().x - dragLabelWidthDiv2;
                        int y = e.getPoint().y - dragLabelHeightDiv2;
                        selectedCard.setLocation(x, y);

                        cardGameTable.add(selectedCard, JLayeredPane.DRAG_LAYER);
                        cardGameTable.revalidate();
                        cardGameTable.repaint();
                    }
                } catch (Exception err) {
                    return;
                }
            } else if (comp instanceof Wing) {
                try {
                    Card card = (Card) ((Wing) comp).getComponent(lowestLayer());
                    selectedCard = card;
                    ((Wing) comp).revalidate();
                    ((Wing) comp).repaint();
                    ((Wing) comp).removeCard(selectedCard);
                    dragLabelWidthDiv2 = selectedCard.getWidth() / 2;
                    dragLabelHeightDiv2 = selectedCard.getHeight() / 2;
                    int x = e.getPoint().x - dragLabelWidthDiv2;
                    int y = e.getPoint().y - dragLabelHeightDiv2;
                    selectedCard.setLocation(x, y);
                    cardGameTable.add(selectedCard, JLayeredPane.DRAG_LAYER);
                    cardGameTable.revalidate();
                    cardGameTable.repaint();
                } catch (Exception error) {
                    return;
                }
            } else if (comp instanceof Trunk) {
                try {
                    Card card = (Card) ((Trunk) comp).getComponent(lowestLayer());
                    selectedCard = card;
                    ((Trunk) comp).revalidate();
                    ((Trunk) comp).repaint();
                    ((Trunk) comp).removeCardFromTrunk();
                    dragLabelWidthDiv2 = selectedCard.getWidth() / 2;
                    dragLabelHeightDiv2 = selectedCard.getHeight() / 2;
                    int x = e.getPoint().x - dragLabelWidthDiv2;
                    int y = e.getPoint().y - dragLabelHeightDiv2;
                    selectedCard.setLocation(x, y);
                    cardGameTable.add(selectedCard, JLayeredPane.DRAG_LAYER);
                    cardGameTable.revalidate();
                    cardGameTable.repaint();
                } catch (Exception er) {
                    return;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            cardGameTable.remove(selectedCard);
            Component comp = cardGameTable.getComponentAt(e.getPoint());
            cardGameTable.add(selectedCard, JLayeredPane.DRAG_LAYER);
            cardGameTable.revalidate();
            cardGameTable.repaint();
            // If on release the card is over the foundation. Check for valid move, if no valid move return to origin.
            if (comp instanceof Foundation) {
                Foundation f = (Foundation) comp;
                if (f.checkValidMove(selectedCard)) {
                    f.addCardToFoundation(selectedCard);
                    if (compOfOrigin instanceof Wing){
                        Wing wingOrigin = (Wing) compOfOrigin;
                        if (wingOrigin.isEmpty()){
                            wingOrigin.addCard(trunk.getTrunkToWing(wingOrigin));
                        }
                    }
                    selectedCard = null;
                } else {
                    returnToOrigin(compOfOrigin, selectedCard);
                }
                // If on release the component the Card Object is over is a wing. Check for valid move, if no valid move return to origin.
            } else if (comp instanceof Wing) {
                Wing w = (Wing) comp;
                if (w.checkValidMove(selectedCard)) {
                    w.addCard(selectedCard);
                    if (compOfOrigin instanceof Wing){
                        Wing wingOrigin = (Wing) compOfOrigin;
                        if (wingOrigin.isEmpty()){
                            wingOrigin.addCard(trunk.getTrunkToWing(wingOrigin));
                        }
                    }
                    selectedCard = null;
                } else {
                    returnToOrigin(compOfOrigin,selectedCard);
                }
                // Check to see if component the
            }else if (comp instanceof Table){
                returnToOrigin(compOfOrigin,selectedCard);
            }else if(comp instanceof Discard){
                returnToOrigin(compOfOrigin, selectedCard);
            }else if (comp instanceof Deck){
                returnToOrigin(compOfOrigin, selectedCard);
            }else if (comp instanceof Trunk){
                trunk.returnCardToTrunk(selectedCard);
                returnToOrigin(compOfOrigin, selectedCard);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (selectedCard != null) {
                int x = e.getPoint().x - dragLabelWidthDiv2;
                int y = e.getPoint().y - dragLabelHeightDiv2;
                selectedCard.setLocation(x, y);
                cardGameTable.revalidate();
                cardGameTable.repaint();
            }
        }

        private void returnToOrigin(Component origin, Card selectedCard) {
            if (origin instanceof Discard) {
                cardGameTable.remove(selectedCard);
                cardGameTable.revalidate();
                cardGameTable.repaint();
                discardPile.addCard(selectedCard);
            } else if (origin instanceof Wing) {
                cardGameTable.remove(selectedCard);
                cardGameTable.revalidate();
                cardGameTable.repaint();
                Wing w = (Wing) origin;
                w.addCard(selectedCard);
            } else if (origin instanceof Trunk) {
                cardGameTable.remove(selectedCard);
                cardGameTable.revalidate();
                cardGameTable.repaint();
                trunk.returnCardToTrunk(selectedCard);
            }
        }
    }
}
