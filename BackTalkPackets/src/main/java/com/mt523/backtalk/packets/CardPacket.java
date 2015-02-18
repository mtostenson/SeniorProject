package com.mt523.backtalk.packets;

public class CardPacket implements IBackTalkPacket {

    private int id;
    private String question;
    private String answer;
    private String category;

    public CardPacket(int id, String question, String answer, String category) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category = category;
    }

    @Override
    public void handlePacket() {
        System.out.println(question);
        System.out.println(answer);
        System.out.println(category);
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
}
