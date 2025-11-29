import java.util.*;
public class numbergame{
    public static void main(String z[]){
        Random ran=new Random();
        Scanner s=new Scanner(System.in);
        boolean playagain=true;
        int score=0;
        while(playagain){
        int num=ran.nextInt(101);
        int max_chance=10;
        boolean guessed=false;
        while(max_chance>0){
            System.out.println("you have "+max_chance+" left to guess the number ! ");
            System.out.println("Enter the guess of the  number i have choosen: ");
            int guess;
            if(s.hasNextInt()){
            guess=s.nextInt();
            }
            else{
                System.out.println("Enter valid number");
                s.next();
                continue;
            }
            if(guess==num){
                guessed=true;
                System.out.println("You have successfully guessed the number");
                score+=max_chance;
                break;
            }
            else if(guess>num){
                System.out.println("your guess is greater than the number");
            }
            else{
                System.out.println("your guess is lesser than the number");
            }
            max_chance--;
        }
        if(guessed==false){
            System.out.println("You are unable to guess the number please try again");
        }
        System.out.println("If you want to play again enter 1: ");
        int pa=s.nextInt();
        playagain=true?(pa==1):false;
        }
        System.out.println("Game over! your score is "+score);
        System.out.println("Thanks for playing ");
    }
}