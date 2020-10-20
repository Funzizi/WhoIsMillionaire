package com.example.whoismillionaire.model;

public class History {
    private int numberQuestion;
    private String time;
    private String date;

    public History(int numberQuestion, String time, String date) {
        this.numberQuestion = numberQuestion;
        this.time = time;
        this.date = date;
    }

    public int getNumberQuestion() {
        return numberQuestion;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
