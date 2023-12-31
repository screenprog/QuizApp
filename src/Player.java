import java.util.ArrayList;

public class Player {

    private String name;
    private int age;

    private int playerId;

    static class Score
    {
        String quizType = null;
        int attempt = 0;
        int skip = 0;
        int score = 0;
        int wrong = 0;
        public String quizType() {
            return quizType;
        }
        public void setQuizType(String quizType) {
            this.quizType = quizType;
        }

        public int attempt() {
            return attempt;
        }

        public int skip() {
            return skip;
        }

        public int score() {
            return score;
        }

        public int wrong() {
            return wrong;
        }
    }
    ArrayList<Score> scores = new ArrayList<>();
    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int age() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int playerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

}
