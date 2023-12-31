import java.sql.*;
import java.util.*;
public class QuestionService {

    private final Scanner sc;
    private final ArrayList<Questions> ques;
    private Connection con = null;
    private PreparedStatement pst = null;
    private final String url = System.getenv("DataBaseURL");
    private final String user = System.getenv("DataBaseUserName");
    private final String pass = System.getenv("DataBaseUserPass");
    private int id = 1;
    public String tableName ;
    private final QuizService quizService = new QuizService("getQuiz()");
    ArrayList<String> quizList;
    public QuestionService() {
        sc = new Scanner(System.in);
        ques = new ArrayList<>();
        quizList = new ArrayList<>();
    }


    void setQuestions()
    {
        System.out.println("\nExisting quiz list : ");
        quizService.getQuiz(quizList);
        quizList.forEach(System.out::println);
        do
        {
            System.out.println("Enter unique quiz name : ");
            tableName = sc.nextLine();
        }while(quizList.contains(tableName));

        this.createTable();
        System.out.print(" - You can only set 15 questions in the quiz - ");
        int size = 15;
        for(int i = 0; i < size; i++)
        {
            Questions que = new Questions();
            que.setId(id++);
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

        String str;
        do
        {
            this.getQuestionsAnswers();
            System.out.print("To update any question enter question id else 0 : ");
            int check = sc.nextInt();
            if(check != 0)
                this.updateQuestions(check-1);
            sc.nextLine();
            System.out.println("If you are sure that all the questions are correct than write \"correct\"  : ");
            str = sc.nextLine();
        }while(!str.equals("correct"));

        this.saveToDataBase();

    }

    void createTable()
    {
        String createTableQue = "CREATE TABLE IF NOT EXISTS " +
                tableName +
                " (qid SERIAL PRIMARY KEY, questions TEXT, opt1 TEXT, opt2 TEXT, opt3 TEXT, opt4 TEXT, ans TEXT)";
        try
        {
            con = DriverManager.getConnection(url,user,pass);
            pst = con.prepareStatement(createTableQue);
            pst.execute();
        }
        catch (SQLException e)
        {
            System.err.println("Connection error : "+ e);
        }
        finally {
            try
            {
                if(pst != null)
                    pst.close();
                if (con != null)
                    con.close();
            }
            catch (Exception e)
            {
                System.err.println("Resources closing : "+ e);
            }
        }
    }

    void saveToDataBase()
    {
        final String saveQue = "insert into "+ tableName +" values(?,?,?,?,?,?,?)";

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

    void deleteQuestionFromDataBase()
    {
        final String deleteQue = "delete from"+ tableName +" where qid = ?";
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

    void checkQuiz()
    {
        System.out.println("\nWhich quiz you want to check : ");
        quizService.getQuiz(quizList);
        quizList.forEach(System.out::println);
        do
        {
            System.out.print("\nSelect a quiz : ");
            tableName = sc.nextLine();
        }while(!quizList.contains(tableName));
        final String readQue = "select * from "+ tableName ;
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
        System.out.println("Questions : \n ");
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

    void getPerformance()
    {
        final String perfQue = "select * from players";
        try(Connection cn = DriverManager.getConnection(url,user,pass) )
        {
            pst = cn.prepareStatement(perfQue);
            ResultSet rs = pst.executeQuery();
            while(rs.next())
            {
                System.out.println("\nQuiz name : "+ rs.getString(1));
                System.out.println("player ID : "+ rs.getInt(2));
                System.out.println("Player name : "+ rs.getString(3));
                System.out.println("Player age : "+ rs.getInt(4));
                System.out.println("Questions attempted : "+rs.getInt(5) );
                System.out.println("Questions skipped : "+rs.getInt(6));
                System.out.println("Wrong answers : "+ rs.getInt(7) );
                System.out.println("Total Score : "+rs.getInt(8));
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception : "+ e);
        }
    }
}
