import java.util.Random;

public class Model {
    

    static final char BLANK=' ', O='O', X='X';
    static final int HvH = 0, HvC = 1, CvH = 2, CvC = 3;
    private char position[]={  // Board position (BLANK, O, or X)
        BLANK, BLANK, BLANK,
        BLANK, BLANK, BLANK,
        BLANK, BLANK, BLANK};
    
    static final int rows[][]={{0,2},{3,5},{6,8},{0,6},{1,7},{2,8},{0,8},{2,6}};
      // Endpoints of the 8 rows in position[] (across, down, diagonally)
    private Random random=new Random();
    private char current = O;
    private int gameType = 0; 
    public String gameTypeText[] = {"Human vs. Human", "Human vs. Computer", "Computer vs. Human", "Computer vs Computer"};


    void setGameType(int type){
        gameType = type;
    }
    
    boolean isGameOver(){
        return (won(O) || won(X) || isDraw()); 
    }

    void setPos(int pos, char type){
        position[pos] = type;
    }

    void setPos(int x, int y, char type){
        position[convertPos(x, y)] = type;
    }
    char getPos(int pos){
        return position[pos];
    }
    char getPos(int x, int y){
        return position[convertPos(x, y)];
    }
    void newGame(){
        clearBoard();
        if(gameType == CvH){
            compMove();
        }
        if(gameType == CvC){
            nextMove(1); //position doesn't matter in this case
        }
    }
    void newGame(int type){
        setGameType(type);
        newGame();
    }

    void clearBoard(){
      for (int j=0; j<9; ++j){
        position[j]=BLANK;
      }
    }
    
    public int[] getWinningRow(){
        char [] players = {O, X};
        for(int player=0; player<players.length; player++){
            for (int i=0; i<8; ++i){
                if (testRow(players[player], rows[i][0], rows[i][1])){
                    return rows[i];
                }
            }
        }
        return null;
    }
    
    private void switchPlayers(){
        current = (current == X ? O : X);
    }

    public void switchIcons(){
        for(int i = 0; i<position.length; i++){
            switch(position[i]){
                case X:
                    position[i] = O;
                    break;
                case O:
                    position[i] = X;
                    break;
            }
        }
        switchPlayers();
    }
    
    private boolean playerMove(int pos){
        if(!isGameOver() && getPos(pos) == BLANK){
            setPos(pos, current);
            switchPlayers();
            return true;
        }
        return false;
    }

    int convertPos(int xpos, int ypos){
        return xpos+3*ypos;
    }
    void nextMove(int xpos, int ypos){
        nextMove(convertPos(xpos, ypos));
    }

    void nextMove(int pos){
        if(pos < 0 || pos >= 9){
            return;
        }
        if(gameType != CvC){
           playerMove(pos);
           if(gameType == HvC || gameType == CvH){
                compMove();
            }
       }else{
            while(compMove()); //play until a computer wins
       }
    }

    private boolean compMove() {
      if(isGameOver()){
        return false;
      }

      
      //referenced code begins here
      //many parts quoted from "mmahoney"
      //please see documentation file for further detail
      
      int r=findRow(X);  // complete a row of X and win if possible
      if (r<0)
        r=findRow(O);  // or try to block O from winning
      if (r<0) {  // otherwise move randomly
        do
          r=random.nextInt(9);
        while (position[r]!=BLANK);
      }
      setPos(r, current);
      switchPlayers();
      return true;
    }


    boolean won(char player) {
      for (int i=0; i<8; ++i) {
        if (testRow(player, rows[i][0], rows[i][1])) {
          return true;
        }
      }
      return false;
    }

    boolean testRow(char player, int a, int b) {
      return position[a]==player && position[b]==player 
          && position[(a+b)/2]==player;
    }


    // Return 0-8 for the position of a blank spot in a row if the
    // other 2 spots are occupied by player, or -1 if no spot exists
    int findRow(char player) {
      for (int i=0; i<8; ++i) {
        int result=find1Row(player, rows[i][0], rows[i][1]);
        if (result>=0)
          return result;
      }
      return -1;
    }

    // If 2 of 3 spots in the row from position[a] to position[b]
    // are occupied by player and the third is blank, then return the
    // index of the blank spot, else return -1.
    int find1Row(char player, int a, int b) {
      int c=(a+b)/2;  // middle spot
      if (position[a]==player && position[b]==player && position[c]==BLANK)
        return c;
      if (position[a]==player && position[c]==player && position[b]==BLANK)
        return b;
      if (position[b]==player && position[c]==player && position[a]==BLANK)
        return a;
      return -1;
    }

    // Are all 9 spots filled?
    boolean isDraw() {
      for (int i=0; i<9; ++i){
        if (position[i]==BLANK){
          return false;
        }
      }
      return true;
    }

}
