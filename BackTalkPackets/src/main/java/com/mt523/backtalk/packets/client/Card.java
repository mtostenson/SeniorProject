package com.mt523.backtalk.packets.client;

import java.io.Serializable;

public class Card implements Serializable {

    private int id;
    private String question;
    private String answer;
    private String category;
    private String hint;

    public Card(int id, String question, String answer, String hint,
            String category) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.hint = hint;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format(
                "\nid: %d\nquestion: %s\nanswer: %s\ncategory: %s\n", id,
                question, answer, category);
    }

    public String getHint() {
        return hint;
    }
}