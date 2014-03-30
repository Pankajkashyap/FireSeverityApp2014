package com.fireseverityapp;

import java.io.Serializable;

public class Reg implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private variables
	int _id;
	String _name;
	String _email;
	String _org;
	String _desig;

	public String get_org() {
		return _org;
	}

	public void set_org(String _org) {
		this._org = _org;
	}

	// Empty constructor
	public Reg(String string, String string2, String string3, String string4) {
		this._name = string;
		this._org = string2;
		this._desig = string3;
		this._email = string4;

	}

	// Empty constructor
	public Reg() {

	}

	// constructor
	public Reg(int id, String name) {
		this._id = id;
		this._name = name;
	}

	// constructor
	public Reg(String name) {
		this._name = name;
	}

	// getting ID
	public int getID() {
		return this._id;
	}

	// setting id
	public void setID(int id) {
		this._id = id;
	}

	// getting name
	public String getName() {
		return _name;
	}

	// setting name
	public void setName(String name) {
		this._name = name;
	}

	public String get_email() {
		return _email;
	}

	public void set_email(String _email) {
		this._email = _email;
	}

	public String get_desig() {
		return _desig;
	}

	public void set_desig(String _desig) {
		this._desig = _desig;
	}

}