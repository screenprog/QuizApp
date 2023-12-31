import java.util.*;

public class Admin {
    private final Scanner sc;
    private final QuestionService questionService;
    Admin()
    {
        sc = new Scanner(System.in);
        questionService = new QuestionService();
    }
    void adminFunction()
    {

        boolean flag = true;
        while (flag)
        {
            System.out.println("\n          - Select an option - ");
            System.out.println("           Add quiz | 1");
            System.out.println("         check quiz | 2");
            System.out.println("  Check performance | 4");
            System.out.println("          Main Menu | 0");
            System.out.print("Enter : ");
            int opt = sc.nextInt();
            switch (opt)
            {
                case 0:
                    flag = false;
                    break;
                case 1:
                    questionService.setQuestions();
                    break;
                case 2:
                    questionService.checkQuiz();
                    break;
                case 4:
                    questionService.getPerformance();
                    break;
                default:
                    System.out.println("......!");
            }
        }
    }
}
