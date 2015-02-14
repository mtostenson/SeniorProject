package com.mt523.backtalk.packets;

public class BasePacket implements IBackTalkPacket {

	private String question;
	private String answer;
	private String category;

	public BasePacket(String q, String a, String c) {
		question = q;
		answer = a;
		category = c;
	}

	@Override
	public void handlePacket() {
		System.out.println(question);
		System.out.println(answer);
		System.out.println(category);
	}

	public CharSequence getQ() {
		return question;
	}

}
