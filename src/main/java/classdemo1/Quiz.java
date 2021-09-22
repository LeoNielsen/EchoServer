package classdemo1;

import java.util.HashMap;

public class Quiz {
    private HashMap<Integer, String> questions = new HashMap<>();
    private HashMap<Integer, String> answers = new HashMap<>();

    public void makeQuestions() {
        questions.put(100, "hvad er hovedstaden i Danmark?");
        answers.put(100, "Kobenhavn");
        questions.put(200, "hvad er hovedstaden i Tyskland?");
        answers.put(200, "Berlin");
        questions.put(300, "hvad er hovedstaden i Sverige?");
        answers.put(300, "Stockholm");
        questions.put(400, "hvad er hovedstaden i Norge?");
        answers.put(400, "Oslo");
        questions.put(500, "hvad er hovedstaden i Liechtenstein?");
        answers.put(500, "Vaduz");
    }

    public String getQuestion(int question) {
        return questions.get(question);
    }

    public String getAnswer(int answer) {
        return answers.get(answer);
    }

    public void removeQuestion(int question) {
        questions.replace(question,"taken");
        answers.replace(question,"");
    }
}
