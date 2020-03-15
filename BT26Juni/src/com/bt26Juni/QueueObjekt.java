package com.bt26Juni;

public class QueueObjekt {
	private String a; 
	private String b;
	public QueueObjekt(String a_producer, String b_producer){
		this.a = a_producer;
		this.b = b_producer;
	}
	public String get_a(){
		
		return a;
	}
    public String get_b(){
    	return b;
    }
    public void set_a(String a_new){
    	a = a_new;
    }
    public void set_b(String b_new){
    	b = b_new;
    }
}
