import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Help extends JFrame{
    Font font = new Font("Comic Sans MS", Font.PLAIN, 15);

    public Help(){
        setSize(600,600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        JButton returnButton = new JButton("Return");
        JPanel helpPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                try{
                    Image helpMenu = new ImageIcon(getClass().getResource("./cards/help.png")).getImage();
                    g.drawImage(helpMenu,0,0,null);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        helpPanel.setBackground(new Color(53,101,77));
        add(helpPanel);
        helpPanel.add(returnButton);


        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                dispose();
                Blackjack blackjack = new Blackjack();
            }
        });
        returnButton.setFocusable(false);
        returnButton.setFont(font);
        returnButton.setBackground(Color.WHITE);
        //returnButton.setBorder(BorderFactory.createEtchedBorder(1));

        returnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
            returnButton.setBackground(new Color(74,140,107));
            }
            @Override
            public void mouseExited(MouseEvent e){
            returnButton.setBackground(Color.WHITE);
            }
        });
    }
}