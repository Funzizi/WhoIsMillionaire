package com.example.whoismillionaire.model;

import java.util.List;

public class Question {
    private String question;
    private List<String> answerList;
    private int answer;
    private int level;

    public Question(String question, List<String> answerList, int answer, int level) {
        this.question = question;
        this.answerList = answerList;
        setAnswer(answer);
        this.level = level;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public int getAnswer() {
        return answer;
    }

    public int getLevel() {
        return level;
    }
}
