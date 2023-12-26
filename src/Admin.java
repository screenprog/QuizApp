import java.util.*;

public class Admin {
    private final Scanner sc;
    private final QuestionService qs;
    Admin()
    {
        sc = new Scanner(System.in);
        qs = new QuestionService();
    }
    void adminFunction()
    {
        boolean flag = true;
        while (flag)
        {
            System.out.println("\n          - Select an option - ");
            System.out.println("     Write question | 1");
            System.out.println("      Read question | 2");
            System.out.println("    Delete question | 3");
            System.out.println("      Save question | 4");
            System.out.println("  Check performance | 5");
            System.out.println("               Exit | 0");
            System.out.print("Enter : ");
            int opt = sc.nextInt();
            switch (opt)
            {
                case 0:
                    flag = false;
                    break;
                case 1:
                    writeQuestions();
                    break;
                case 2:
                    qs.readFromDataBase();
                    break;
                case 3:
                    qs.deleteFromDataBase();
                    break;
                case 4:
                    qs.saveToDataBase();
                    break;
                case 5:
                    qs.getPerformance();
                    break;
                default:
                    System.out.println("......!");
            }
        }
    }
    void writeQuestions()
    {
        String str;
        qs.setQuestions();
        do
        {
            System.out.println("Questions : \n");
            qs.getQuestionsAnswers();
            System.out.print("To update any question enter question index else -1 : ");
            int check = sc.nextInt();
            if(check != -1)
                qs.updateQuestions(check);
            sc.nextLine();
            System.out.println("If you are sure that all the questions are correct than write \"yes\"  : ");
            str = sc.nextLine();

        }while(!str.equals("yes"));

    }

}
