package com.mycompany.solitaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GUI extends JFrame implements KeyListener{
   
    static GUI mainFrame;
    
    private final GameMenu gm = new GameMenu();
    public GUI(String name) {
        super(name);
        setResizable(false);
        addKeyListener(this);       
    }

    public void addComponentsToPane(Container pane) {
        // Create Discard Pile, Deck Pile and Trunk
        Deck deck = new Deck();
        Score s = new Score();
        //Create Foundations & Set their locations
        ArrayList<Foundation> f = new ArrayList<Foundation>();
        for (int i = 0; i < 4; i++) {
            if (i != 0) {
                f.add(new Foundation());
            } else {
                f.add(new Foundation(deck.draw()));
            }
        }
        Trunk t = new Trunk(deck,f);
        t.setBounds(580, 270, 100, 265);
        int foundation_x = 400;
        for (Foundation foundation : f) {
            foundation.setBounds(foundation_x, 20, 100, 145);
            foundation_x += 120;
        }
        // Create wings and Set their location
        ArrayList<Wing> wings = new ArrayList<Wing>();
        for (int i = 0; i < 8; i++) {
            wings.add(new Wing(deck,f));
        }
        int x_Loc = 20;
        int y_Loc = 250;
        for (Wing w : wings) {
            if (x_Loc < 380) {
                w.setBounds(x_Loc, y_Loc, 100, 400);
                x_Loc += 120;
            } else if (x_Loc == 380) {
                w.setBounds(x_Loc, y_Loc, 100, 400);
                x_Loc = 780;
            } else {
                w.setBounds(x_Loc, y_Loc, 100, 400);
                x_Loc += 120;
            }
        }
        Discard discard = new Discard();
        Table table = new Table(discard,t,f,wings,s);
        // Add wings to table
        for (Wing w : wings) {
            table.add(w);
        }
        // Add foundation to table
        for (Foundation foo : f) {
            table.add(foo);
        }
        deck.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (deck.isEmpty()){
                    super.mousePressed(e);
                    playSound("/Sound/cardFan1.wav");
                    deck.redeal(discard);
                }else {
                    super.mousePressed(e);
                    playSound("/Sound/cardSlide4.wav");
                    deck.drawCardToDiscard(discard,f);
                }
                deck.revalidate();
                deck.repaint();
            }
        });
        table.add(t);
        table.add(s);
        table.add(deck);
        table.add(discard);
        pane.add(table);
    }

    
    
    private static void createAndShowGUI() {
        // Create and setup main window        
        mainFrame = new GUI("Eagle Wing");
        mainFrame.addComponentsToPane(mainFrame.getContentPane());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display and Center Window
        mainFrame.pack();
        mainFrame.setSize(1280, 720);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }


    @Override
    public void keyTyped(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ESCAPE) {
            System.out.println("Hello1");
        }

    }
    public int pressed = 0;
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gm.setVisible(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    
    public static synchronized void playSound(String path) {
  new Thread(new Runnable() {
  // The wrapper thread is unnecessary, unless it blocks on the
  // Clip finishing; see comments.
    public void run() {
      try {
        Clip clip = AudioSystem.getClip();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
          Menu.class.getResourceAsStream(path));
        clip.open(inputStream);
        clip.start(); 
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
  }).start();
}
}
