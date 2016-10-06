package com.fusemachines.fsm;

import org.springframework.stereotype.Component;


@Component
public  class MyFSMBase {

	public enum State{
		FIRST_STATE, SECOND_STATE, THIRD_STATE;
	}
	
	private State state = State.FIRST_STATE;
	
	public void setState(State s){
		this.state = s;
	}
	
	public State getState(){
		return state;
	}
	
}
