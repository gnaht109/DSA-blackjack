import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Blackjack {
    Font font = new Font("Comic Sans MS", Font.PLAIN, 15);
    Font font1 = new Font("Comic Sans MS", Font.PLAIN, 30);
    Font budgetFont = new Font("Comic Sans MS",Font.PLAIN,25);
    Font tiefont = new Font("Comic Sans MS",Font.PLAIN,15);
    Font winfont = new Font("Comic Sans MS",Font.PLAIN,15);
    Font losefont = new Font("Comic Sans MS",Font.PLAIN,15);


    String gameMessage = "Your turn";
    final int frameW = 1000;
    final int frameH = 600;
    final int cardW = 80;
    final int cardH = 100;

    JFrame frame1 = new JFrame("MENU");
    JButton playButton = new JButton("Play");
    JButton helpButton = new JButton("Help");
    JButton quitButton = new JButton("Quit");
    JPanel menuPanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
        g.drawRect((frameW/2)-55,(frameH/2)-85,100,50);
        g.drawRect((frameW/2)-55,(frameH/2)-85+85,100,50);
        g.drawRect((frameW/2)-55,(frameH/2)-85+170,100,50);


        Font tilteFont = new Font("Comic Sans MS", Font.PLAIN, 70);
        setFont(tilteFont);
        g.setColor(Color.WHITE);
        g.drawString("BLACK JACK", (frameW/2) - 220, (frameH/2)-150);
        }
    };
    
    Random random = new Random();

    ArrayList<Card> deck;

    public boolean isMenu = true;

    //dealer
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;
    Card hiddenCard;

    //player
    ArrayList<Card> playerHand;
    int totalwin = 0;
    boolean endTurn = false;
    boolean firstHandTurn = true;
    boolean isSingle = true;
    boolean isSplit ;
    int budget = 50;
    int firstHandBet = 5;
    int playerSum;
    int playerAceCount;

    //player second hand
    ArrayList<Card> playerSecondHand;
    int secondHandBet = 5;
    int playerSecondSum = 0;
    int playerSecondAceCount = 0;

    //----------------------card logic---------------------------------//
    public void drawCard(){
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size()-1);
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce()?1:0;

        Card card = deck.remove(deck.size()-1);
        dealerSum +=card.getValue();
        dealerAceCount += card.isAce()?1:0;
        dealerHand.add(card);

        //System.out.println("Dealer hands");
        //System.out.println(hiddenCard);
        //System.out.println(dealerHand);
        //System.out.println(dealerSum);
        //System.out.println(dealerAceCount);

        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for(int i = 0;i<2;i++){
            card = deck.remove(deck.size()-1);
            playerSum += card.getValue();
            playerAceCount += card.isAce()?1:0;
            playerHand.add(card);
        }

        //System.out.println("PLAYER HAND");
        //System.out.println(playerHand);
        //System.out.println(playerSum);
        //System.out.println(playerAceCount);
    }

    public void buildDeck(){
        deck = new ArrayList<Card>();
        String[] values = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        String[] types = {"S","C","D","H"};
        for(int i = 0;i < types.length;i++){
            for(int j = 0;j<values.length;j++){
                Card card = new Card(values[j],types[i]);
                deck.add(card);
            }
        }
        //("BUILD DECK");
        //System.out.println(deck);
    }

    public void shuffle(){
        for(int i=0;i<deck.size();i++){
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i,randomCard);
            deck.set(j,currCard);
        }
        //System.out.println("SHUFFLE THE DECK");
        //System.out.println(deck);
    }
//--------------------------------------card logic----------------------------------------//
//----------------------------------------gamePanel--------------------------------------//
    JPanel gamePanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            try{
                //Budget
                String budgetString ="Budget $"+budget;
                g.setFont(budgetFont);
                g.setColor(Color.yellow);
                g.drawString(budgetString,frameW-200,frameH/2 - 250);

                if(isSingle){
                    String betString ="Bet: $"+firstHandBet;
                    g.setFont(budgetFont);
                    g.setColor(Color.yellow);
                    g.drawString(betString,frameW-200,frameH/2 - 220);
                }

                g.setFont(font1);
                g.setColor(Color.GREEN);
                g.drawString(gameMessage,20,frameH/2);
                



                //drawHiddenCard
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if(endTurn){
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                if(hitButton.isEnabled()){
                    hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                }
                
                g.drawImage(hiddenCardImg, 20, 20, cardW,cardH,null);

                //drawDealerHand
                for(int i = 0;i<dealerHand.size();i++){
                    Card card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg,20 + cardW + 5 + (cardW + 5)*i,20,cardW,cardH,null);
                }
                
                //drawPlayerHand
                for(int i = 0;i<playerHand.size();i++){
                    Card card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg,20 + (cardW + 5)*i,350,cardW,cardH,null);
                }

                //drawSecondPlayerHand
                if(isSplit){
                    String leftBetString ="Left Bet: $"+firstHandBet;
                    g.setFont(budgetFont);
                    g.setColor(Color.yellow);
                    g.drawString(leftBetString,frameW-200,frameH/2 - 220);

                    String rightBetString ="Right Bet: $"+secondHandBet;
                    g.setFont(budgetFont);
                    g.setColor(Color.yellow);
                    g.drawString(rightBetString,frameW-200,frameH/2 - 190);

                    for(int i = 0;i<playerSecondHand.size();i++){
                        Card card = playerSecondHand.get(i);
                        Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                        g.drawImage(cardImg,(frameW/2) + (cardW + 5)*i,350,cardW,cardH,null);
                    }
                }

                if(endTurn && reduceDealerAce()>16 ){
                    String message = "";
                    
                    if(totalwin>0){
                        g.setColor(Color.yellow);
                        message ="+ $" +totalwin;
                    }
                    if(totalwin<0){
                        g.setColor(Color.red);
                        message = "- $"+(totalwin* -1);
                    }
                    if(totalwin==0){
                        g.setColor(Color.white);
                        message = "+ $0";
                    }
                    if(budget <= 0){
                        g.setColor(Color.red);
                        message = "Stop playing you're broke";
                        resetButton.setEnabled(false);
                    }
                    
                    g.setFont(font1);
                    g.drawString(message,frameW-160,(frameH/2)-95 -25);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };
    
//--------------------------------------------gamePanel-------------------------------------//
//--------------------------------------DETERMINE BET----------------------------------------//
    private void determineWinner() {
        playerSum = reducePlayerAce();
        playerSecondSum = reduceSecondPlayerAce();
        dealerSum = reduceDealerAce();
        
        // Check for dealer bust
        
        // Check for dealer bust
        if (dealerSum > 21) {
            if (isSplit) {
                if (playerSum <= 21) {
                    budget += firstHandBet;
                    totalwin += firstHandBet;
                    System.out.println("Win first hand");
                } else {
                    System.out.println("First hand bust");
                }

                if (playerSecondSum <= 21) {
                    budget += secondHandBet;
                    totalwin += secondHandBet;
                    System.out.println("Win second hand");
                } else {
                    System.out.println("Second hand bust");
                }
            } else {
                if (playerSum <= 21) {
                    budget += firstHandBet;
                    totalwin += firstHandBet;
                    System.out.println("Player wins");
                } else {
                    System.out.println("Player bust, tie");
                }
            }
            return;
        }
        
        if(dealerSum <= 21){
            // Check player hand
            if (playerSum > 21) {
                budget -= firstHandBet;
                totalwin -= firstHandBet;
                System.out.println("case lose");

            } else if (playerSum == dealerSum) {
                budget = budget;
                System.out.println("case tie");

            } else if (playerSum > dealerSum) {
                budget += firstHandBet;
                totalwin += firstHandBet;
                System.out.println("case win");
            } else {
                budget -= firstHandBet;
                totalwin -= firstHandBet;
                System.out.println("case lose");
            }
        
            // Check split hand 
            if (isSplit) {
                System.out.println("Split check");
                if (playerSecondSum > 21) {
                    budget -= secondHandBet;
                    totalwin -= secondHandBet;
                    System.out.println("split case lose");
                } else if (playerSecondSum == dealerSum) {
                    budget = budget;
                    System.out.println("split case tie");
                } else if (playerSecondSum > dealerSum) {
                    budget += secondHandBet;
                    totalwin += secondHandBet;
                    System.out.println("split case win");
                } else {
                    budget -= secondHandBet;
                    totalwin -= secondHandBet;
                    System.out.println("split case lose");
                }
            }
        }
    }
    //-------------------------------------------------------------------------------------------//


    JPanel buttonPanel = new JPanel(new FlowLayout());
    JButton hitButton = new JButton("HIT");
    JButton stayButton = new JButton("STAY");
    JButton splitButton = new JButton("SPLIT");
    JButton doubleButton = new JButton("DOUBLE");
    JButton resetButton = new JButton("Play Again");

    

    
    JFrame frame = new JFrame("Black Jack");
    JButton exitButton = new JButton("EXIT");

    public void setFrame(){
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameW,frameH);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        hitButton.setFocusable(false);
        stayButton.setFocusable(false);
        splitButton.setFocusable(false);
        doubleButton.setFocusable(false);
        resetButton.setFocusable(false);
        exitButton.setFocusable(false);
        hitButton.setFont(font);
        stayButton.setFont(font);
        splitButton.setFont(font);
        doubleButton.setFont(font);
        exitButton.setFont(font);
        resetButton.setFont(font);

        hitButton.setBackground(Color.WHITE);
        stayButton.setBackground(Color.WHITE);
        splitButton.setBackground(Color.WHITE);
        doubleButton.setBackground(Color.WHITE);
        exitButton.setBackground(Color.WHITE);
        resetButton.setBackground(Color.WHITE);


         //-------------------------------NOTHING HERE JUST BUTTON HOVER--------------------------------//

        hitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                hitButton.setBackground(Color.green);
            }
            @Override
            public void mouseExited(MouseEvent e){
                hitButton.setBackground(Color.WHITE);
            }
        });

        stayButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                stayButton.setBackground(Color.green);
            }
            @Override
            public void mouseExited(MouseEvent e){
                stayButton.setBackground(Color.WHITE);
            }
        });

        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                resetButton.setBackground(Color.green);
            }
            @Override
            public void mouseExited(MouseEvent e){
                resetButton.setBackground(Color.WHITE);
            }
        });
        
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                exitButton.setBackground(Color.RED);
            }
            @Override
            public void mouseExited(MouseEvent e){
                exitButton.setBackground(Color.WHITE);
            }
        });

        splitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                splitButton.setBackground(Color.CYAN);
            }
            @Override
            public void mouseExited(MouseEvent e){
                splitButton.setBackground(Color.WHITE);
            }
        });

        doubleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                doubleButton.setBackground(Color.BLUE);
            }
            @Override
            public void mouseExited(MouseEvent e){
                doubleButton.setBackground(Color.WHITE);
            }
        });

    //-------------------------------NOTHING HERE JUST BUTTON HOVER--------------------------------//

        buttonPanel.add(hitButton);
        buttonPanel.add(stayButton);
        buttonPanel.add(splitButton);
        buttonPanel.add(doubleButton);

        gamePanel.setBackground(new Color(53,101,77));
        buttonPanel.setBackground(new Color(53,101,77));

        frame.add(buttonPanel,BorderLayout.SOUTH);
        frame.add(gamePanel);

        resetButton.setBounds( frameW-200,(frameH/2)-95,150,30);
        exitButton.setBounds(frameW-200,(frameH/2)-95 + 35 ,150,30);

        gamePanel.setLayout(null);
        gamePanel.add(resetButton);
        gamePanel.add(exitButton);
        resetButton.setEnabled(false);


        exitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.dispose();
                frame1.dispose();
            }
        });

    }
    
//-----------------------------------------BUTTON EVENT------------------------------------------------//

    public void resetMet(){
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                resetButton.setEnabled(false);
                gameMessage = "Player's turn";
                dealerHand.clear();
                playerHand.clear();
                playerSum = 0;
                playerSecondSum = 0;
                playerAceCount = 0;
                playerSecondAceCount = 0;
                firstHandBet = 5;
                secondHandBet = 0;
                endTurn = false;
                System.out.println("clear hand");
                if(isSplit){
                    playerSecondHand.clear();
                    isSplit = false;
                    System.out.println("clear split hand");
                }
                System.out.println("first bet:"+firstHandBet);
                System.out.println("second bet:"+secondHandBet);
                totalwin = 0;
                buildDeck();
                shuffle();
                drawCard();
                isSingle = true;
                firstHandTurn = true;
                splitButton.setEnabled(true);
                doubleButton.setEnabled(true);
                hitButton.setEnabled(true);
                if(reducePlayerAce()< 16){
                    stayButton.setEnabled(false);
                }
                else{
                    stayButton.setEnabled(true);
                }
                gamePanel.repaint();
                System.out.println("PLAY AGAIN");
                System.out.println(reducePlayerAce());
                System.out.println(reduceSecondPlayerAce());

                }
        });
    }

    public void hitMet(){
        hitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(isSingle){
                    gameMessage="hit";
                    splitButton.setEnabled(false);
                    doubleButton.setEnabled(false);
                    Card card = deck.remove(deck.size()-1);
                    playerSum += card.getValue();
                    playerAceCount += card.isAce()?1:0;
                    playerHand.add(card);
                    gamePanel.repaint();
                    //System.out.println(playerSum);
                    if(reducePlayerAce()>15){
                        stayButton.setEnabled(true);
                    }
                    if(reducePlayerAce()>21){
                        hitButton.setEnabled(false);
                        //stayButton.doClick();
                        //System.out.println("Stop");
                    }
                }
                else if(firstHandTurn){
                    gameMessage="hit left hand";
                    doubleButton.setEnabled(false);
                    Card card = deck.remove(deck.size()-1);
                    playerSum += card.getValue();
                    playerAceCount += card.isAce()?1:0;
                    playerHand.add(card);
                    gamePanel.repaint();
                    //System.out.println(playerSum);
                    if(reducePlayerAce()>15){
                        stayButton.setEnabled(true);
                    }
                    if(reducePlayerAce()>21){
                        hitButton.setEnabled(false);
                        //stayButton.doClick();
                        //System.out.println("Stop");
                    }
                }
                else{
                    gameMessage="hit right hand";
                    doubleButton.setEnabled(false);
                    Card card = deck.remove(deck.size()-1);
                    playerSecondSum += card.getValue();
                    playerSecondAceCount += card.isAce()?1:0;
                    playerSecondHand.add(card);
                    gamePanel.repaint();
                    //System.out.println(playerSum);
                    if(reduceSecondPlayerAce()>15){
                        stayButton.setEnabled(true);
                    }
                    if(reduceSecondPlayerAce()>21){
                        hitButton.setEnabled(false);
                        //stayButton.doClick();
                        //System.out.println("Stop");
                    }
                }
            }
        });
        gamePanel.repaint();
    }

    public void splitMet(){
        splitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("split");
                gameMessage = "Splitted, left hand turn";
                splitButton.setEnabled(false);
                doubleButton.setEnabled(true);
                isSingle = false;
                isSplit = true;
                playerSecondHand = new ArrayList<Card>();
                secondHandBet = firstHandBet;
                System.out.println("first hand bet after split: "+firstHandBet);
                System.out.println("split hand bet after split: "+secondHandBet);
                //get second card from first hand to the second hand
                Card card = playerHand.remove(playerHand.size()-1);
                playerSum -= card.getValue();
                playerSecondSum += card.getValue();
                playerAceCount += card.isAce()?1:0;
                playerSecondHand.add(card);

                //draw 1 more first hand
                card = deck.remove(deck.size()-1);
                playerSum += card.getValue();
                playerAceCount += card.isAce()?1:0;
                playerHand.add(card);

                //draw 1 more second hand
                card = deck.remove(deck.size()-1);
                playerSecondSum += card.getValue();
                playerSecondAceCount += card.isAce()?1:0;
                playerSecondHand.add(card);

                gamePanel.repaint();
                System.out.println("After Split: ");
                System.out.println("first hand: " +reducePlayerAce());
                System.out.println("second hand: " +reduceSecondPlayerAce());

                if(reducePlayerAce()>15){
                    stayButton.setEnabled(true);
                }else {
                    stayButton.setEnabled(false);
                }
                
            }
        });
        gamePanel.repaint();
    }

    public void doubleMet(){
        doubleButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("double");
                if(isSingle){
                    firstHandBet *= 2;
                    gameMessage = "doubled";
                    doubleButton.setEnabled(false);
                    
                }
                else if(firstHandTurn){
                    firstHandBet *= 2;
                    gameMessage = "doubled left hand";
                    doubleButton.setEnabled(false);
                }
                else{
                    secondHandBet *= 2;
                    gameMessage = "doubled right hand";
                    doubleButton.setEnabled(false);
                }
                gamePanel.repaint();
                
            }
        });
        gamePanel.repaint();
    }
    
    public void stayMet(){
        stayButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(isSingle){
                    gameMessage="End turn";
                    hitButton.setEnabled(false);
                    stayButton.setEnabled(false);
                    endTurn = true;
                    while(reduceDealerAce() < 17){
                        Card card = deck.remove(deck.size()-1);
                        dealerSum += card.getValue();
                        dealerAceCount += card.isAce()?1:0;
                        dealerHand.add(card);
                        //System.out.println("dealerSum:"+dealerSum);
                        //System.out.println("reduceDealerSum:"+reduceDealerAce());
                    }
                    determineWinner();
                    resetButton.setEnabled(true);
                    gamePanel.repaint();
                    //System.out.println("Game over");
                }

                else if(firstHandTurn && !isSingle){
                    gameMessage="Second hand turn";
                    hitButton.setEnabled(true);
                    doubleButton.setEnabled(true);
                    firstHandTurn = false;
                    System.out.println(reduceSecondPlayerAce());

                    if(reduceSecondPlayerAce()<16){
                        stayButton.setEnabled(false);
                        hitButton.setEnabled(true);
                    }
                    gamePanel.repaint();

                    
                }

                else{
                    gameMessage ="End turn";
                    hitButton.setEnabled(false);
                    stayButton.setEnabled(false);
                    doubleButton.setEnabled(false);
                    endTurn = true;
                    while(reduceDealerAce() < 17){
                        Card card = deck.remove(deck.size()-1);
                        dealerSum += card.getValue();
                        dealerAceCount += card.isAce()?1:0;
                        dealerHand.add(card);
                        //System.out.println("dealerSum:"+dealerSum);
                        //System.out.println("reduceDealerSum:"+reduceDealerAce());
                    }
                    determineWinner();
                    resetButton.setEnabled(true);
                    gamePanel.repaint();
                    //System.out.println("Game over");
                }
            }
        });
        gamePanel.repaint();
    }

//--------------------------------------------BUTTON EVENT---------------------------------------------//

//---------------------------------------------ACE LOGIC----------------------------------------------//
    public int reducePlayerAce(){
        int sum, aceCount;
        sum = playerSum;
        aceCount = playerAceCount;

        while(sum > 21 && aceCount > 0){
            if(playerSum == 22){
                sum -= 1;

            }
            else{
                sum -= 10;
                aceCount -= 1;
            }
        }
        return sum;
    }

    public int reduceSecondPlayerAce(){
        int sum, aceCount;
        sum = playerSecondSum;
        aceCount = playerSecondAceCount;
        
        while(sum > 21 && aceCount > 0){
            if(playerSum == 22){
                sum -= 1;

            }
            else{
                sum -= 10;
                aceCount -= 1;
            }
        }
        return sum;
    }

    public int reduceDealerAce(){
        while(dealerSum > 21 && dealerAceCount > 0){
            if(dealerSum == 22){
                dealerSum -= 1;

            }
            else{
                dealerSum -= 10;
                dealerAceCount -= 1;
            }
        }
        return dealerSum;
    }

//-------------------------------------------------ACE LOGIC------------------------------------------------//

    public void start(){
    buildDeck();
    shuffle();
    drawCard();
    }


//-------------------------------------------MENU SETTING-----------------------------------------------//
    public void setmenu(){
        frame1.setBackground(new Color(53,101,77));
        frame1.setSize(frameW,frameH);
        frame1.setVisible(true);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        frame1.setResizable(false);
        menuPanel.setBackground(new Color(53,101,77));
        //g.drawRect((frameW/2)-50,(frameH/2)-85,100,50);
        //g.drawRect((frameW/2)-50,(frameH/2)-85+85,100,50);
        //g.drawRect((frameW/2)-50,(frameH/2)-85+170,100,50);
        menuPanel.setLayout(null);
        quitButton.setVisible(true);
        //menuPanel.add(playButton);
        menuPanel.add(helpButton);
        menuPanel.add(quitButton);
        playButton.setBounds((frameW/2)-55,(frameH/2)-85,100,50);
        helpButton.setBounds((frameW/2)-55,(frameH/2)-85+85,100,50);
        quitButton.setBounds((frameW/2)-55,(frameH/2)-85+170,100,50);
        playButton.setBackground(Color.WHITE);
        helpButton.setBackground(Color.WHITE);
        quitButton.setBackground(Color.WHITE);

        playButton.setFocusable(false);
        helpButton.setFocusable(false);
        quitButton.setFocusable(false);
        
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                playButton.setBackground(new Color(74,140,107));
            }
            @Override
            public void mouseExited(MouseEvent e){
                playButton.setBackground(Color.WHITE);
            }
        });

        helpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                helpButton.setBackground(new Color(74,140,107));
            }
            @Override
            public void mouseExited(MouseEvent e){
                helpButton.setBackground(Color.WHITE);
            }
        });

        quitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                quitButton.setBackground(Color.RED);
            }
            @Override
            public void mouseExited(MouseEvent e){
                quitButton.setBackground(Color.WHITE);
            }
        });

        Font buttonFont = new Font("Comic Sans MS", Font.PLAIN, 24);
        playButton.setFont(buttonFont);
        playButton.setBorder(BorderFactory.createEtchedBorder(1));
        helpButton.setFont(buttonFont);
        helpButton.setBorder(BorderFactory.createEtchedBorder(1));
        quitButton.setFont(buttonFont);
        quitButton.setBorder(BorderFactory.createEtchedBorder(1));
        menuPanel.add(playButton);
        frame1.add(menuPanel);
    }

    public void quitMet(){
        quitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame1.dispose();
                System.out.println("QUIT THE GAME");
            }
        });
    }

    public void playMet(){
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                budget = 50;
                running();
                frame1.dispose();
                System.out.println("PlAY THE GAME");
                System.out.println(reducePlayerAce());
                System.out.println(reduceSecondPlayerAce());

            }
        });
    }

    public void helpMet(){
        helpButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Help helpmenu = new Help();
                frame1.dispose();
                System.out.println("HELP MENU");
            }
        });
    }

//-------------------------------------------MENU SETTING-----------------------------------------------//

    public void running(){
        start();
        setFrame();
        hitButton.setEnabled(true);
        if(reducePlayerAce()< 16){
            stayButton.setEnabled(false);
        }
        hitMet();
        stayMet();
        resetMet();
        splitMet();
        doubleMet();
        System.out.println("RUN THE GAME");
    }


    Blackjack(){
            setmenu();
            playMet();
            quitMet();
            helpMet();
    }
}


