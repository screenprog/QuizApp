import java.sql.*;
import java.util.*;
public class QuestionService {

    private final Scanner sc;
    private final ArrayList<Questions> ques;


    private Connection con = null;
    private PreparedStatement pst = null;
    private final String url = "jdbc:postgresql://localhost:5432/QuizeDataBase";
    private final String user = "postgres";
    private final String pass = "NoP4$$";
    private int id = 1;



    private static final String saveQue = "insert into questions_answers values(?,?,?,?,?,?,?)";
    private static final String deleteQue = "delete from questions_answers where qid = ?";
    private static final String readQue = "select * from questions_answers";
    private static final String perfQue = "select * from players";
    private static final String lastIdQue = "SELECT * FROM questions_answers " +
            "ORDER BY qid DESC " +
            "LIMIT 1";

    public QuestionService() {
        sc = new Scanner(System.in);
        ques = new ArrayList<>();
        getLastId();
    }

    public String url() {
        return url;
    }

    public String user() {
        return user;
    }

    public String pass() {
        return pass;
    }

    private void getLastId()
    {

            try(Connection con1 = DriverManager.getConnection(url,user,pass);
                PreparedStatement pst1 = con1.prepareStatement(lastIdQue))
            {

                ResultSet resultSet = pst1.executeQuery();
                resultSet.next();
                int temp = resultSet.getInt(1);
                if(temp > 0)
                    id = temp;
            }
            catch(SQLException e)
            {
                System.err.println("SQL Exception : "+ e);
            }


    }
    
    void setQuestions()
    {
        System.out.print("How many questions you want to set in the quiz : ");
        int size = sc.nextInt();
        for(int i = 0; i < size; i++)
        {
            Questions que = new Questions();
            que.setId(++id);
            sc.nextLine();
            System.out.print("Enter question : ");
            que.setQue(sc.nextLine());
            System.out.print("Enter option 1 : ");
            que.setOpt1(sc.nextLine());
            System.out.print("Enter option 2 : ");
            que.setOpt2(sc.nextLine());
            System.out.print("Enter option 3 : ");
            que.setOpt3(sc.nextLine());
            System.out.print("Enter option 4 : ");
            que.setOpt4(sc.nextLine());
            System.out.print("Enter answer : ");
            que.setAns(sc.nextLine());
            ques.add(que);
        }

    }

    void saveToDataBase()
    {

        try
        {
            con = DriverManager.getConnection(url, user, pass);
            pst = con.prepareStatement(saveQue);

            for (Questions que : ques)
            {
                pst.setInt(1, que.id());
                pst.setString(2, que.que());
                pst.setString(3, que.opt1());
                pst.setString(4, que.opt2());
                pst.setString(5, que.opt3());
                pst.setString(6, que.opt4());
                pst.setString(7, que.ans());

                pst.executeUpdate();
            }
            System.out.println("Saved to data base");
        } catch (SQLException e) {
            System.err.println("SQL Exception : "+ e);
        }
        finally
        {
            try
            {
                if(con != null && pst != null)
                {

                        pst.close();
                        con.close();
                }
            } catch (SQLException e) {
                System.out.println("\nResources closing exception : "+ e);
            }
        }

    }

    void deleteFromDataBase()
    {
        System.out.print("Enter qid to delete : ");
        int qid = sc.nextInt();
        try
        {
            con = DriverManager.getConnection(url,user,pass);
            pst = con.prepareStatement(deleteQue);
            pst.setInt(1, qid);
            pst.execute();
        }
        catch(SQLException e)
        {
            System.err.println("SQL Exception : "+ e);
        }
        finally
        {
            try
            {
                if(con != null)
                    con.close();
                if( pst != null)
                    pst.close();
            }
            catch (SQLException e)
            {
                System.out.println("\nResources closing exception : "+ e);
            }
        }
    }

    void readFromDataBase()
    {
        try
        {
            con = DriverManager.getConnection(url, user, pass);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(readQue);

            while(rs.next())
            {
                System.out.print(rs.getInt(1)+". ");
                System.out.println(rs.getString(2));
                System.out.print("a) "+ rs.getString(3));
                System.out.println("        b) "+ rs.getString(4));
                System.out.print("c) "+ rs.getString(5));
                System.out.println("        d) "+ rs.getString(6));
                System.out.println("Answer  : "+ rs.getString(7));

            }

            st.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                if(pst != null)
                    pst.close();
                if(con != null)
                    con.close();
            }
            catch(SQLException e)
            {
                System.err.println("Resources closing exception : "+ e );
            }

        }
    }
    void getQuestionsAnswers()
    {
        System.out.println("Data in memory : \n ");
        for (int i = 0; i< ques.size(); i++)
        {
            System.out.println("i : "+i +"-->"+ ques.get(i));
        }
    }

    void updateQuestions(int index)
    {
        Questions q = ques.get(index);
        System.out.println(q);
        sc.nextLine();
        System.out.print("Enter new question : ");
        q.setQue(sc.nextLine());
        System.out.print("Enter option 1 : ");
        q.setOpt1(sc.nextLine());
        System.out.print("Enter option 2 : ");
        q.setOpt2(sc.nextLine());
        System.out.print("Enter option 3 : ");
        q.setOpt3(sc.nextLine());
        System.out.print("Enter option 4 : ");
        q.setOpt4(sc.nextLine());
        System.out.print("Enter answer : ");
        q.setAns(sc.nextLine());
        ques.remove(index);
        ques.add(index, q);

    }

    void loadQuestions()
    {
        try
        {
            con = DriverManager.getConnection(url,user,pass);
            pst = con.prepareStatement(readQue);
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
                ques.add(q);
            }

            pst.close();
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

    void getPerformance()
    {
//        QuizService quizService = new QuizService();
        try(Connection cn = DriverManager.getConnection(url,user,pass) )
        {
            pst = cn.prepareStatement(perfQue);
            ResultSet rs = pst.executeQuery();
            while(rs.next())
            {
                System.out.println("\nQuiz name : "+ rs.getString(1));
                System.out.println("player ID : "+ rs.getInt(2));
                System.out.println("Player name : "+ rs.getString(3));
                System.out.println("Questions attempted : "+rs.getInt(4) );
                System.out.println("Questions skipped : "+rs.getInt(5));
                System.out.println("Wrong answers : "+ rs.getInt(6) );
                System.out.println("Total Score : "+rs.getInt(7));
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception : "+ e);
        }
    }

    public ArrayList<Questions> ques() {
        return ques;
    }
}
