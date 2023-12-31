import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizService {
    ArrayList<Questions> questions ;
    private final Scanner sc ;
    Player ply;
    private final String url = System.getenv("DataBaseURL");
    private final String user = System.getenv("DataBaseUserName");
    private final String pass = System.getenv("DataBaseUserPass");
    private String quizName;
    Connection con;
    PreparedStatement pst = null;
    private int lastId;
    QuizService(String str)
    {
        sc = new Scanner(System.in);
    }
    public QuizService()
    {
        this.questions = new ArrayList<>();
        this.sc = new Scanner(System.in);
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
        Player.Score s = new Player.Score();
        this.selectQuiz();
        s.setQuizType(quizName);
        final String readQue = "select * from "+ quizName ;
        this.loadQuestions(readQue);
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
                s.skip++;
            else
                s.attempt++;

            if (q.ans().equals(str)) {
                s.score++;
            } else {
                s.wrong++;
            }
            q.setAns(str);

        }
        ply.scores.add(s);
        this.saveAnswers();
    }

    void getScore()
    {
        for(Player.Score s : ply.scores)
        {
            System.out.println("\nQuiz type : "+ s.quizType());
            System.out.println("Number of questions attempt : "+ s.attempt());
            System.out.println("Number of questions skipped : "+ s.skip());
            System.out.println("Number of wrong questions : "+ s.wrong());
            System.out.println("Total score : "+ s.score());
        }
    }

    void saveAnswers()
    {
        try
        {
            con = DriverManager.getConnection(url,user,pass);
            String saveQue = "insert into players values(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(saveQue);
            pst.setString(1,quizName);
            pst.setInt(2,ply.playerId());
            pst.setString(3,ply.name());
            pst.setInt(4,ply.age());
            pst.setInt(5,ply.scores.getLast().attempt());
            pst.setInt(6,ply.scores.getLast().skip());
            pst.setInt(7,ply.scores.getLast().wrong());
            pst.setInt(8, ply.scores.getLast().score());

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

    void selectQuiz()
    {
        ArrayList<String> quizNames = new ArrayList<>();
        this.getQuiz(quizNames);
        System.out.println();
        quizNames.forEach(System.out::println);
        sc.nextLine();
        do
        {
            System.out.print("Select a quiz name (correct name) : ");
            quizName = sc.nextLine();
        }while(!quizNames.contains(quizName));
    }
    void getQuiz(ArrayList<String> quizNames)
    {
        try(Connection con = DriverManager.getConnection(url,user,pass))
        {
            DatabaseMetaData dbmt = con.getMetaData();
            ResultSet rs = dbmt.getTables(null,null,null,new String[]{"TABLE"});
            while(rs.next())
            {
                String temp = rs.getString("TABLE_NAME");
                if(!temp.equals("players"))
                    quizNames.add(temp);
            }
        }
        catch (SQLException e)
        {
            System.err.println("Connection Error : "+ e);
        }
    }

    void getLastId()
    {
        try
        {
             con = DriverManager.getConnection(url,user,pass);
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

    void loadQuestions(String readQuiz)
    {
        try
        {
            con = DriverManager.getConnection(url,user,pass);
            pst = con.prepareStatement(readQuiz);
            ResultSet rs = pst.executeQuery();
            while (rs.next())
            {
                Questions q = new Questions();
                q.setId(rs.getInt(1));
                q.setQue(rs.getString(2));
                q.setOpt1(rs.getString(3));
                q.setOpt2(rs.getString(4));
                q.setOpt3(rs.getString(5));
                q.setOpt4(rs.getString(6));
                q.setAns(rs.getString(7));
                questions.add(q);
            }

        }
        catch(SQLException e)
        {
            System.out.println("SQL Exception : "+e);
        }
        finally
        {
            try
            {
                if (pst != null)
                    pst.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("SQL Exception : "+ e);
            }
        }
    }
}
