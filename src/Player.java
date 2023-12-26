public class Player {

    private String name;
    private int age;

    private int playerId;
    private int totalScore;
    private int queAttempt;

    private int queSkip;
    private int wrongAns;

    public Player()
    {

    }

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

    public int totalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int queAttempt() {
        return queAttempt;
    }

    public void setQueAttempt(int queAttempt) {
        this.queAttempt = queAttempt;
    }

    public int wrongAns() {
        return wrongAns;
    }

    public void setWrongAns(int wrongAns) {
        this.wrongAns = wrongAns;
    }

    public int queSkip() {
        return queSkip;
    }

    public void setQueSkip(int queSkip) {
        this.queSkip = queSkip;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "name='" + name + '\'' +
                ", playerId='" + playerId + '\'' +
                ", totalScore=" + totalScore +
                ", queAttempt=" + queAttempt +
                ", wrongAns=" + wrongAns +
                '}';
    }
}
