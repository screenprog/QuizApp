import java.util.Scanner;

public class User
{
    private  boolean flag;
    private final Scanner sc;
    private final QuizService quizService;
    public User() {
        this.sc = new Scanner(System.in);
        quizService = new QuizService();
        flag = true;
    }
    void userFunction()
    {
        while(flag)
        {
            System.out.println("\n - Select an option -");
            System.out.println("   Play Quiz | 1 ");
            System.out.println(" Check Score | 2 ");
            System.out.println("   Main Menu | 0 ");
            System.out.print("You option : ");
            switch (sc.nextInt())
            {
                case 0:
                    flag = false;
                    break;
                case 1:
                    quizService.startQuiz();
                    break;
                case 2:
                    quizService.getScore();
                    break;
                default:
                    System.out.println(".....!");
            }

        }
    }

}
