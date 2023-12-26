import java.util.Scanner;

public class User
{
    private  boolean flag;
    private final Scanner sc;
    private final QuizService qs;
    public User() {
        this.sc = new Scanner(System.in);
        qs = new QuizService();
        flag = true;
    }
    void userFunction()
    {
        while(flag)
        {
            System.out.println("\n - Select an option -");
            System.out.println("  Start Quiz | 1 ");
            System.out.println(" Check Score | 2 ");
            System.out.println("        Exit | 0 ");
            System.out.print("You option : ");
            switch (sc.nextInt())
            {
                case 0:
                    qs.saveAnswers();
                    flag = false;
                    break;
                case 1:
                    qs.startQuiz();
                    break;
                case 2:
                    qs.getScore();
                    break;
                default:
                    System.out.println(".....!");
            }

        }
    }
}