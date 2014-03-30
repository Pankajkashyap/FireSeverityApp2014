package com.fireseverityapp.mail;

import android.app.Application;

public class MailAccount extends Application{

	/**
	 * @author Ling An 17-10-2013
	 * 
	 * where to set the Mail Account (ID, Password)
	 */
	private String accountID = "fireseveritylevel@gmail.com";
	private String password = "hello12345";
	private String receiverEmail = "kanwar.r007@gmail.com";
	private String subject = "Fire Severity Level App Notice Email";

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public String getAccountID(){
		return accountID;
	}
	
	public void setAccountID(String accountID){
		this.accountID = accountID;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
