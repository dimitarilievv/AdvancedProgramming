package task29_FootballTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class Team{
    String name;
    int goals;
    int goalsTaken;
    int wins;
    int losses;
    int draws;

    public Team(String name, int wins, int losses, int draws) {
        this.name = name;
        this.goals = 0;
        this.goalsTaken = 0;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }
    void winGame(){
        wins++;
    }
    void loseGame(){
        losses++;
    }
    void drawGame(){
        draws++;
    }
    int numAll(){
        return wins+losses+draws;
    }
    int points(){
        return wins*3+draws;
    }

    public int goalDifference() {
        return goals-goalsTaken;
    }

    public String getName() {
        return name;
    }

    public void scoreGoals(int homeGoals) {
        goals+=homeGoals;
    }

    public void takeGoals(int awayGoals) {
        goalsTaken+=awayGoals;
    }
}
class FootballTable{
    Map<String,Team> teams;

    public FootballTable() {
        teams=new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        teams.putIfAbsent(homeTeam,new Team(homeTeam,0,0,0));
        teams.putIfAbsent(awayTeam,new Team(awayTeam,0,0,0));
        if(homeGoals==awayGoals){
            teams.get(homeTeam).drawGame();
            teams.get(awayTeam).drawGame();
        }else if(homeGoals>awayGoals){
            teams.get(homeTeam).winGame();
            teams.get(awayTeam).loseGame();
        }else{
            teams.get(homeTeam).loseGame();
            teams.get(awayTeam).winGame();
        }
        teams.get(homeTeam).scoreGoals(homeGoals);
        teams.get(homeTeam).takeGoals(awayGoals);
        teams.get(awayTeam).scoreGoals(awayGoals);
        teams.get(awayTeam).takeGoals(homeGoals);
    }

    public void printTable() {
        AtomicInteger index= new AtomicInteger(1);
        StringBuilder sb=new StringBuilder();
        Comparator<Team> comparator=Comparator.comparing(Team::points).thenComparing(Team::goalDifference).reversed().thenComparing(Team::getName);
        teams.values().stream()
                .sorted(comparator)
                .forEach(i->{
                    sb.append(String.format("%2d. %-15s%5d%5d%5d%5d%5d\n", index.getAndIncrement(), i.name,i.numAll(),i.wins,i.draws,i.losses,i.points()));
                });
        System.out.println(sb.toString());
    }
}
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}



