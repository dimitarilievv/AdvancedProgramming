package task23_Quiz;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.*;
class InvalidOperationException extends Exception{
    public InvalidOperationException(String message) {
        super(message);
    }
}


abstract class Question implements Comparable<Question>{
    String question;
    int points;

    public Question(String question, int points) {
        this.question = question;
        this.points = points;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public int compareTo(Question o) {
        return Integer.compare(this.points,o.points);
    }
    abstract float answerQuestion(String studentAnswer);
}
class QuestionFactory{
    static List<String> ALLOWED_ANSWERS=Arrays.asList("A","B","C","D","E");
    static Question create(String line) throws InvalidOperationException {
        String []parts=line.split(";");
        String type=parts[0];
        String text=parts[1];
        int points=Integer.parseInt(parts[2]);
        String answer=parts[3];
        if(type.equals("MC")){
            if(!ALLOWED_ANSWERS.contains(answer)){
                throw new InvalidOperationException(String.format("%s is not allowed option for this question",answer));
            }
            return new MC(text,points,answer);
        }else{
            return new TF(text,points,Boolean.parseBoolean(answer));
        }
    }
}
class TF extends Question{
    boolean answer;

    public TF(String question, int points, boolean answer) {
        super(question, points);
        this.answer = answer;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("True/False Question: ").append(question).append( " Points: ").append(points).append(" Answer: ").append(answer);
        return sb.toString();
    }

    @Override
    float answerQuestion(String studentAnswer) {
        return (answer==Boolean.parseBoolean(studentAnswer)) ? points: 0.0f;
    }
}
class MC extends Question{
    private String answer;

    public MC(String question, int points, String answer) {
        super(question, points);
        this.answer = answer;
    }

    //    @Override
//    String getAnswer() {
//        return answer+"";
//    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Multiple Choice Question: ").append(question).append( " Points ").append(points).append(" Answer: ").append(answer);
        return sb.toString();
    }

    @Override
    float answerQuestion(String studentAnswer) {
        return answer.equals(studentAnswer)?points:(points*-0.2f);
    }
}
class Quiz{
    List<Question> questions;

    public Quiz() {
        this.questions = new ArrayList<>();
    }
    void addQuestion(String questionData) {
        //MC;Question1;3;E
        //TF;Question3;2;false
        try {
            questions.add(QuestionFactory.create(questionData));
        } catch (InvalidOperationException e) {
            System.out.println(e.getMessage());
        }
    }
    void printQuiz(OutputStream os){
        PrintWriter pw=new PrintWriter(os);
        questions.stream().sorted(Comparator.reverseOrder()).forEach(q->pw.println(q));
        pw.flush();
    }
    void answerQuiz(List<String> answers, OutputStream os) throws InvalidOperationException {
        if(questions.size()!=answers.size()){
            throw new InvalidOperationException("Answers and questions must be of same length!");
        }
        PrintWriter pw=new PrintWriter(os);
        float sum=0;
        for(int i=0;i< answers.size();i++){
            float points=questions.get(i).answerQuestion(answers.get(i));
            pw.println(String.format("%d. %.2f",i+1,points));
            sum+=points;
        }
        pw.println(String.format("Total points: %.2f",sum));
        pw.flush();

    }

}
public class QuizTest {
    public static void main(String[] args) throws InvalidOperationException {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i=0;i<questions;i++) {
            quiz.addQuestion(sc.nextLine());
        }

        List<String> answers = new ArrayList<>();

        int answersCount =  Integer.parseInt(sc.nextLine());

        for (int i=0;i<answersCount;i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase==1) {
            quiz.printQuiz(System.out);
        } else if (testCase==2) {
            try{
                quiz.answerQuiz(answers, System.out);
            }catch (InvalidOperationException ie){
                System.out.println(ie.getMessage());
            }

        } else {
            System.out.println("Invalid test case");
        }
    }
}
