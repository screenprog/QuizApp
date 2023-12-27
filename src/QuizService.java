import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizService {
    ArrayList<Questions> questions ;
    private final QuestionService qs;
    private final Scanner sc ;
    Player ply;

    Connection con;
    PreparedStatement pst = null;


    private int lastId;

    public QuizService()
    {
        this.questions = new ArrayList<>();
        this.sc = new Scanner(System.in);
        qs = new QuestionService();
        ply = new Player();
        getLastId();
        setInfo();
    }



    private void setInfo()
    {
        ply.setPlayerId(lastId);
        System.out.print("Enter your name : ");
        ply.setName(sc.nextLine());
        System.out.print("Enter your age ");
        ply.setAge(sc.nextInt());
    }

    void startQuiz()
    {
        int attempt = 0;
        int skip = 0;
        int score = 0;
        int wrong = 0;
        qs.loadQuestions();
        questions = qs.ques();

        sc.nextLine();
        System.out.println("""

                           - Write answers only rather than options. -\s
                     - To skip any question write "skip" instead of answer - \
                """);
        for (Questions q : questions) {
            System.out.print(q.id() + ". ");
            System.out.println(q.que());
            System.out.print("a) " + q.opt1());
            System.out.println("        b) " + q.opt2());
            System.out.print("c) " + q.opt3());
            System.out.println("        d) " + q.opt4());
            System.out.print("your answer (only answer) : ");
            String str = sc.nextLine();
            if (str.equals("skip"))
                skip++;
            else
                attempt++;

            if (q.ans().equals(str)) {
                score++;
            } else {
                wrong++;
            }
            q.setAns(str);

        }


        ply.setQueAttempt(attempt);
        ply.setQueSkip(skip);
        ply.setWrongAns(wrong);
        ply.setTotalScore(score);
    }

    void getScore()
    {
        System.out.print("\nNumber of questions attempted : "+ ply.queAttempt());

        System.out.print("\nNumber of questions skipped : "+ ply.queSkip());

        System.out.print("\nNumber of wrong questions : "+ ply.wrongAns());

        System.out.print("\nTotal score : "+ ply.totalScore());

    }

    void saveAnswers()
    {

        try
        {
            con = DriverManager.getConnection(qs.url(),qs.user(),qs.pass());
            String saveQue = "insert into players values(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(saveQue);
            pst.setString(1,"random_questions");
            pst.setInt(2,ply.playerId());
            pst.setString(3,ply.name());
            pst.setInt(4,ply.age());
            pst.setInt(5,ply.queAttempt());
            pst.setInt(6,ply.queSkip());
            pst.setInt(7,ply.wrongAns());
            pst.setInt(8, ply.totalScore());

            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally
        {

            try {
                if(pst != null)
                    pst.close();
                if( con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("\nResources closing exception : "+ e);
            }

        }

    }

    void getLastId()
    {
        try
        {
             con = DriverManager.getConnection(qs.url(),qs.user(),qs.pass());
            String lastIdQue = "SELECT * FROM players ORDER BY player_id DESC LIMIT 1";
            pst = con.prepareStatement(lastIdQue);
             try
             {
                 ResultSet st = pst.executeQuery();
                 st.next();
                 lastId = st.getInt(2);
             }
             catch (Exception e)
             {
                 System.out.println("Congratulations you are first player");
                 lastId = 0;
             }

             if(lastId < 10)
                 lastId = 23000;
             else
                 lastId++;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try
            {
                if( pst != null)
                        pst.close();
                if(con != null)
                    con.close();
            }
            catch(Exception e)
            {
                System.out.println("\nResources closing exception : "+ e);
            }
        }


    }


}
