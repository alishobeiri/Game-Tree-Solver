import java.util.*;

public class tester {

            public static void main(String[] args) {
            boolean[] B=new boolean[]{false,true,true,
                true,true,false,
    false,true,false,true,false,true,false,
    false,false,true,false,false,false,true,
    true,false,false,true,false,true,false,
                true,false,true,
                false,true,false};





            HiRiQ X=new HiRiQ((byte) 2); 
            X.store(B);
            X.print();
            solve(B);
        }
            
    public static void solve(boolean[] B){
        HiRiQ W=new HiRiQ((byte) 4);
        W.store(B);
        ArrayList<String> solution = answer(W);
        System.out.println(solution.toString());
    }
    
    public static ArrayList<String> answer(HiRiQ W){
        boolean[] B=new boolean[33]; W.load(B);//Saves board to array
        ArrayDeque<HiRiQ> open=new ArrayDeque<>();//Generates moves sorted by config
        ArrayList<HiRiQ> visited=new ArrayList<>();//Meant to saved all the visited nodes
        open.addAll(generate(B,W));//Adds all the generated boards to queue
        HiRiQ X=W.clone(W); //Saves board
        try{
            if(X.IsSolved()){
                return X.moves;
            } 
        while(!(X.IsSolved())){
            X=open.poll(); //Takes first entry in the list
            if(deadEnd(X.config,X.weight)){ //See if it's a dead end
                System.out.println("Dead end... no solution");
                X.moves.clear();
                X.addMove("@");
                return X.moves;
            }
            if(X.IsSolved()){ //Checks if it is solved
                return X.moves; //Returns moves
            }else{
                if(!(visited.isEmpty())){ //Makes sure the visited is not empty
                    while(visited.contains(X)){ //Checks to see if there are duplicates
                        if(!(open.isEmpty())){ 
                            X=open.poll(); //Removes first one until there is a unique object
                        }else{ //In the case that all the white subs are repeats
                            X.load(B); //Saves config to array
                            open.clear(); //Clears previous queue
                            System.gc(); 
                            open.addAll(generate(B,X)); //Generates list
                        }   
                    }
                }
                visited.add(X.clone(X));//Adds the object to the visited list
                X.load(B);
                open.clear(); //Clears the list
                System.gc(); 
                open.addAll(generate(B,X)); //Generates new options to explore
            }
        }
        return W.moves;
        }catch(Exception E){
            ArrayList<String> exception=new ArrayList<>();
            exception.add("Unfortuntately ran out of memory and could not generate a solution");
            return exception;
        }
    }
    
    public static class HiRiQ{
                //int is used to reduce storage to a minimum...
                  public int config;
                  public byte weight;
                  public ArrayList<String> moves;

                //initialize to one of 5 reachable START config n=0,1,2,3,4
                HiRiQ(byte n)
                  {
                  this.moves=new ArrayList<>();
                  if (n==0)
                   {config=65536/2;weight=1;}
                  else
                    if (n==1)
                     {config=1626;weight=6;}
                    else
                      if (n==2)
                       {config=-1140868948; weight=10;}
                      else
                        if (n==3)
                         {config=-411153748; weight=13;}
                        else
                            if(n==4)
                         {config=-2147450879; weight=32;}
                            else
                                if(n==5){
                                    config=264; weight=2;
                                }
                  }

                  boolean IsSolved()
                  {
                          return( (config==65536/2) && (weight==1) );
                  }
                  
                  public HiRiQ clone(HiRiQ X){
                      HiRiQ clone=new HiRiQ((byte) 0);
                      boolean[] B=new boolean[33];
                      clone.store(this.load(B));
                      clone.moves.addAll(this.moves);
                      return clone;
                  }

                //transforms the array of 33 booleans to an (int) cinfig and a (byte) weight.
                  public void store(boolean[] B)
                  {
                  int a=1;
                  config=0;
                  weight=(byte) 0;
                  if (B[0]) {weight++;}
                  for (int i=1; i<32; i++)
                   {
                   if (B[i]) {config=config+a;weight++;}
                   a=2*a;
                   }
                  if (B[32]) {config=-config;weight++;}
                  }
                  @Override
                  public boolean equals(Object X){
                      HiRiQ W=(HiRiQ)X;
                      if(X instanceof HiRiQ){
                          return W.config==this.config&&W.weight==this.weight;
                      }
                      return false;
                  }
                  @Override
                  public int hashCode(){
                      boolean[] B=new boolean[33];
                      B=this.load(B);
                      long H=21;
                      for(int i=0;i<B.length;i++){
                          H = (H<<1)+(B[i]? 1:0);
                      }
                      return Long.valueOf(H).hashCode();
                  }
                  
                  public void addMove(String s){
                      moves.add(s);
                  }

                //transform the int representation to an array of booleans.
                //the weight (byte) is necessary because only 32 bits are memorized
                //and so the 33rd is decided based on the fact that the config has the
                //correct weight or not.
                  public boolean[] load(boolean[] B)
                  {
                  byte count=0;
                  int fig=config;
                  B[32]=fig<0;
                  if (B[32]) {fig=-fig;count++;}
                  int a=2;
                  for (int i=1; i<32; i++)
                   {
                   B[i]= fig%a>0;
                   if (B[i]) {fig=fig-a/2;count++;}
                   a=2*a;
                   }
                  B[0]= count<weight;
                  return(B);
                  }

                //prints the int representation to an array of booleans.
                //the weight (byte) is necessary because only 32 bits are memorized
                //and so the 33rd is decided based on the fact that the config has the
                //correct weight or not.
                  public void printB(boolean Z)
                  {if (Z) {System.out.print("[ ]");} else {System.out.print("[@]");}}

                  public void print()
                  {
                  byte count=0;
                  int fig=config;
                  boolean next,last=fig<0;
                  if (last) {fig=-fig;count++;}
                  int a=2;
                  for (int i=1; i<32; i++)
                   {
                   next= fig%a>0;
                   if (next) {fig=fig-a/2;count++;}
                   a=2*a;
                   }
                  next= count<weight;

                  count=0;
                  fig=config;
                  if (last) {fig=-fig;count++;}
                  a=2;

                  System.out.print("      ") ; printB(next);
                  for (int i=1; i<32; i++)
                   {
                   next= fig%a>0;
                   if (next) {fig=fig-a/2;count++;}
                   a=2*a;
                   printB(next);
                   if (i==2 || i==5 || i==12 || i==19 || i==26 || i==29) {System.out.println() ;}
                   if (i==2 || i==26 || i==29) {System.out.print("      ") ;};
                   }
                   printB(last); System.out.println() ;

                  }
                }
        
    
    public static ArrayList<HiRiQ> generate(boolean B[],HiRiQ W){
        //Generates a list of HiRiQ objects that will be checke
        //W.store(B);
        ArrayList<HiRiQ> moveList=new ArrayList<HiRiQ>(10);
        //MergeSort sort=new MergeSort(moveList);
        whiteHorizontalSub(B,moveList,W);
        whiteVerticalSub(B,moveList,W);
        if(moveList.size()==0){ //Does all white subs before black subs
            blackVerticalSub(B,moveList,W);
            blackHorizontalSub(B,moveList,W);
        }
        Collections.sort(moveList, new Comparator<HiRiQ>() {
        @Override public int compare(HiRiQ p1, HiRiQ p2) { 
        //Sorts based on distance from the desired config - speeds up algorithm
            if(Math.abs(Math.abs(p1.config)-(32768))>Math.abs(Math.abs(p2.config)-32768)){
                return 1;
            }else if(Math.abs(Math.abs(p1.config)-(32768))<Math.abs(Math.abs(p2.config)-32768)){
                return -1;
            }else if(Math.abs(Math.abs(p1.config)-(32768))==Math.abs(Math.abs(p2.config)-32768)){
                return (p1.weight-p2.weight);
            }
            return 0;
        }

    });

        //sort.sortGivenArray();
        //moveList.get(0).print();
        /*for(int i=0;i<moveList.size();i++){
            System.out.println(moveList.get(i).weight);
        }*/
        return moveList;
    }
    
    public static ArrayList whiteHorizontalSub(boolean[] H,ArrayList<HiRiQ> list,HiRiQ W){
            int i=0;int j=1;int k=2;int count=0;
            //Forbidden values=1,2,4,5,11,12,18,19,25,26,28,29,31,32
            HiRiQ board=W.clone(W);
            boolean[] B=Arrays.copyOf(H, 33);
            while(k<=32){
                if(list.size()>10){ //Makes surethat there is not more than 10 entries in the list - memory management
                    break;
                }
                //Forbidden values that will not result in a horizontal substitution
                if(i==1||i==2||i==4||i==5||i==11||i==12||i==18||i==19||i==25||i==26||i==28||i==29||i==31||i==32){
                    i++;j++;k++;
                }else{
                //Checks for WWB substituition
                //Makes switch to BBW
                if(B[i] && B[j] && !B[k]){
                    B[i]=!B[i];
                    B[j]=!B[j];
                    B[k]=!B[k];
                    board=W.clone(W);
                    board.addMove(i+"W"+k);
                    board.store(B);
                    list.add(board);
                    i++;j++;k++;
                    B=Arrays.copyOf(H, 33);
                //Checks for a BWW situation
                //Makes switch to WBB
                }
                if(!B[i] && B[j] && B[k]){
                    B[i]=!B[i];
                    B[j]=!B[j];
                    B[k]=!B[k];
                    board=W.clone(W);
                    board.addMove(k+"W"+i);
                    board.store(B);
                    list.add(board);
                    i++;j++;k++;
                    B=Arrays.copyOf(H, 33);
                }else{
                    i++;j++;k++;
                }
                }
            }
            return list;
    }

    public static ArrayList whiteVerticalSub(boolean[] H,ArrayList list,HiRiQ W){
        int i=0;int j=3;int k=8;
        HiRiQ board=W.clone(W);
        boolean[] B=Arrays.copyOf(H, 33);
        while(k<=32){
            if(list.size()>10){ //Makes surethat there is not more than 10 entries in the list - memory management
                    break;
                }
            if(B[i] && B[j] && !B[k]){
                    B[i]=!B[i];
                    B[j]=!B[j];
                    B[k]=!B[k];
                    board=W.clone(W);
                    board.addMove(i+"W"+k);
                    board.store(B);//Stores array config before reset
                    list.add(board);//Adds board to list to be sorted
                    B=Arrays.copyOf(H, 33); //Resets array
                //Checks for a BWW situation
                //Makes switch to WBB
            }
            if(!B[i] && B[j] && B[k]){
                    B[i]=!B[i];
                    B[k]=!B[k];
                    B[j]=!B[j];
                    board=W.clone(W);
                    board.addMove(k+"W"+i);
                    board.store(B);//Stores array config before reset
                    list.add(board);//Adds board to list to be sorted
                    B=Arrays.copyOf(H, 33); //Resets array
            }
            i++;j++;k++;
            //The following set of if statements will be aligning counters
            if(k==10){
                i=3;j=8;k=15;
            }
            if(k==17){
                i=6;j=13;k=20;
            }
            if(k==26){
                i=15;j=22;k=27;
            }
            if(k==29){
                i=22;j=27;k=30;
            }
        }
        return list;
    }
    
    public static ArrayList blackVerticalSub(boolean[] H,ArrayList list,HiRiQ W){
        int i=0;int j=3;int k=8;
        HiRiQ board=W.clone(W);
        boolean[] B=Arrays.copyOf(H, 33);
        while(k<=32){
            if(list.size()>10){
                    break;
                }
            if(B[i] && !B[j] && !B[k]){
                    B[i]=!B[i];
                    B[j]=!B[j];
                    B[k]=!B[k];
                    board=W.clone(W);
                    board.addMove(k+"B"+i);
                    board.store(B);//Stores array config before reset
                    list.add(board);//Adds board to list to be sorted
                    B=Arrays.copyOf(H, 33); //Resets array
                //Checks for a BWW situation
                //Makes switch to WBB
            }
            if(!B[i] && !B[j] && B[k]){
                    B[i]=!B[i];
                    B[k]=!B[k];
                    B[j]=!B[j];
                    board=W.clone(W);
                    board.addMove(i+"B"+k);
                    board.store(B);//Stores array config before reset
                    list.add(board);//Adds board to list to be sorted
                    B=Arrays.copyOf(H, 33); //Resets array
            }
            i++;j++;k++;
            //The following set of if statements will be aligning counters
            if(k==10){
                i=3;j=8;k=15;
            }
            if(k==17){
                i=6;j=13;k=20;
            }
            if(k==26){
                i=15;j=22;k=27;
            }
            if(k==29){
                i=22;j=27;k=30;
            }
        }
        return list;
    }
    
    public static ArrayList blackHorizontalSub(boolean[] H,ArrayList<HiRiQ> list,HiRiQ W){
            int i=0;int j=1;int k=2;
            //Forbidden values=1,2,4,5,11,12,18,19,25,26,28,29,31,32
            HiRiQ board=W.clone(W);
            boolean[] B=Arrays.copyOf(H, 33);
            while(k<33){
                if(list.size()>10){ //Makes surethat there is not more than 10 entries in the list - memory management
                    break;
                }
                //Forbidden values that will not result in a horizontal substitution
                if(i==1||i==2||i==4||i==5||i==11||i==12||i==18||i==19||i==25||i==26||i==28||i==29||i==31||i==32){
                    i++;j++;k++;
                }else{
                //Checks for WWB substituition
                //Makes switch to BBW
                if(B[i] && !B[j] && !B[k]){
                    B[i]=!B[i];
                    B[j]=!B[j];
                    B[k]=!B[k];
                    board=W.clone(W);
                    board.addMove(k+"B"+i);
                    board.store(B);
                    list.add(board);
                    i++;j++;k++;
                    B=Arrays.copyOf(H, 33);
                //Checks for a BWW situation
                //Makes switch to WBB
                }
                if(!B[i] && !B[j] && B[k]){
                    B[i]=!B[i];
                    B[j]=!B[j];
                    B[k]=!B[k];
                    board=W.clone(W);
                    board.addMove(i+"B"+k);
                    board.store(B);
                    list.add(board);
                    i++;j++;k++;
                    B=Arrays.copyOf(H, 33);
                }else{
                    i++;j++;k++;
                }
                }
            }
            return list;
    }
    
    public static boolean deadEnd(int config, int weight){
            if(config==-2147483647&&weight==33){
                return true;
            }
            if(config==-2013093879&&weight==28){
                return true;
            }
            if(config==-1811591147&&weight==25){
                return true;
            }
            if(config==-1677201379&&weight==20){
                return true;
            }
            if(config==-1051720382&&weight==25){
                return true;
            }
            if(config==-917330614&&weight==20){
                return true;
            }
            if(config==-715827882&&weight==17){
                return true;
            }
            if(config==-581438114&&weight==12){
                return true;
            }
            if(config==1566045533&&weight==21){
                return true;
            }
            if(config==1431655765&&weight==16){
                return true;
            }
            if(config==1230153033&&weight==13){
                return true;
            }
            if(config==1095763265&&weight==8){
                return true;
            }
            if(config==470282268&&weight==13){
                return true;
            }
            if(config==335892500&&weight==8){
                return true;
            }
            if(config==134389768&&weight==5){
                return true;
            }
            if(config==0&&weight==0){
                return true;
            }
        return false;
    }
    
    
    
}



