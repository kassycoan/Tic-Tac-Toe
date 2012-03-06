import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

public class View  extends JFrame implements ActionListener {

    // Start the game
    public static void main(String args[]) {
        new View(new Model());
    }
    private JButton[] playOptions;
    private JButton iconSwitch;
    private JButton differentIcon;
    private Board board;
    private int lineThickness=4;
    private Model model;
    private int imageType = 0;
    private String [][] images = {{"x.jpg", "o.jpg"},{"water.jpg", "fire.jpg"},{"dog.jpg", "cat.jpg"},{"fsuLogo.jpg", "ufLogo.jpg"},{"google.jpg", "facebook.jpg"}};
   
    //set up view layout
    private void createFooter(){
        JPanel bottomPanel = new JPanel();
        JPanel options = new JPanel();
        JPanel credits = new JPanel();
        JPanel switchIcons = new JPanel();
        playOptions = new JButton[model.gameTypeText.length];
        for(int i = 0; i<model.gameTypeText.length; i++){
            options.add(playOptions[i]=new JButton(model.gameTypeText[i]));
        }
        switchIcons.add(iconSwitch = new JButton("Switch Pieces with Other Player"));
        switchIcons.add(differentIcon = new JButton("Switch Icon Themes"));
        credits.add(new JLabel("Kassandra Coan"));
        for(int i = 0; i<playOptions.length; i++){
            playOptions[i].addActionListener(this);
        }        
        iconSwitch.addActionListener(this);
        differentIcon.addActionListener(this);
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(options, BorderLayout.NORTH);
        bottomPanel.add(switchIcons, BorderLayout.CENTER);
        bottomPanel.add(credits, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
        
    }
    private void createHeader(){
        JPanel topPanel=new JPanel();
        add(topPanel, BorderLayout.NORTH);
        topPanel.add(new JLabel("FSU TIC TAC TOE"));
    } 

      // Initialize
    public View(Model model) {
        super("Tic Tac Toe");
        this.model = model;
        createHeader();
        add(board=new Board(), BorderLayout.CENTER);
        createFooter();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }//end setup view layout

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == differentIcon){
            imageType = ++imageType % images.length;
            board.repaint();
            return;
        }
        if(e.getSource() == iconSwitch) {
            model.switchIcons();
            board.repaint();
            return;
        }
        for(int i = 0; i<playOptions.length; ++i){  
            if (e.getSource()==playOptions[i]) {
                board.newGame(i);
            }
        }
    }
// Board
private class Board extends JPanel implements MouseListener {
    public Board() {
      addMouseListener(this);
    }


    //drawing of the board comes from referenced code
    // draw board
      public void paintComponent(Graphics g) {
      super.paintComponent(g);
      setLayout(new BorderLayout());
      int w=getWidth();
      int h=getHeight();
      Graphics2D g2d = (Graphics2D) g;

      // Draw the grid
      g2d.setPaint(Color.WHITE);
      g2d.fill(new Rectangle2D.Double(0, 0, w, h));
      g2d.setPaint(Color.BLACK);
      g2d.setStroke(new BasicStroke(lineThickness));
      g2d.draw(new Line2D.Double(0, h/3, w, h/3));
      g2d.draw(new Line2D.Double(0, h*2/3, w, h*2/3));
      g2d.draw(new Line2D.Double(w/3, 0, w/3, h));
      g2d.draw(new Line2D.Double(w*2/3, 0, w*2/3, h));
    //end referenced drawing


      // Draw the icons
      for (int i=0; i<9; ++i) {
            double xpos=(i%3+0.5)*w/3.0;
            double ypos=(i/3+0.5)*h/3.0;
            Image img = null;
             if(model.getPos(i) == model.O){
                img = Toolkit.getDefaultToolkit().getImage(images[imageType][0]);
             } else if(model.getPos(i) == model.X) {
                img = Toolkit.getDefaultToolkit().getImage(images[imageType][1]);
             }
             if(img != null){
                g2d.drawImage(img, (int)(xpos-img.getWidth(this)/2), (int)(ypos-img.getHeight(this)/2), this);
             }
      }

      if(model.isGameOver()){
            int [] row = model.getWinningRow();
            //if it's a cats game
            if(row == null){
                return;
            }
            double xpos1=(row[0]%3+0.5)*w/3.0;
            double ypos1=(row[0]/3+0.5)*h/3.0;
            
            double xpos2=(row[1]%3+0.5)*w/3.0;
            double ypos2=(row[1]/3+0.5)*h/3.0;
            
            g2d.setPaint(Color.BLACK);
            g2d.setStroke(new BasicStroke(lineThickness*3));
            g2d.draw(new Line2D.Double(xpos1, ypos1, xpos2, ypos2));

      }
     
      g2d.finalize();

    }

    public void mouseClicked(MouseEvent e) {
      int xpos=e.getX()*3/getWidth();
      int ypos=e.getY()*3/getHeight();
      model.nextMove(xpos, ypos);
      repaint();
    }

    // Ignore other mouse events
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    // Start a new game
    void newGame(int type) {
        model.newGame(type);
        repaint();
    }
  } // end inner class Board

    
}

