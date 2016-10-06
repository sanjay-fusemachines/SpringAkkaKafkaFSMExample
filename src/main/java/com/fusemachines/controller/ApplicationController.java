package com.fusemachines.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fusemachines.akka.AkkaFactory;
import com.fusemachines.fsm.MyFSMBase;
import com.fusemachines.fsm.MyFSMBase.State;

import akka.actor.ActorSelection;

@RestController
public class ApplicationController {
	
	@Autowired
	MyFSMBase myFSMBase;
	
	@RequestMapping(value="/start", method = RequestMethod.POST)
	public void startApplication(@RequestBody String jsonData){
		
		//Checking if the posted data is JSON or not
		if(isJSONValid(jsonData)){
			System.out.println(myFSMBase.getState());
			
			//Firstly JSON data comes to the system and the system changes state from FIRST to SECOND
			myFSMBase.setState(State.SECOND_STATE);
			
			//Producer Actor is Created Here
			ActorSelection fsmProducerActor = AkkaFactory.getActorSystem().actorSelection("akka://AkkaSystem/user/fsmProducer");
			
			//Producer Actor is given instruction by system
			fsmProducerActor.tell(jsonData, null);
			
			//Consumer Actor is Created Here
			ActorSelection fsmConsumerActor = AkkaFactory.getActorSystem().actorSelection("akka://AkkaSystem/user/fsmConsumer");
			
			//Consumer Actor is given instruction by system
			fsmConsumerActor.tell("Print Message", null);
			
		}else
		{
			System.out.println("Error");
		}
		
	}
	
	public boolean isJSONValid(String test) {
	    try {
	        new JSONObject(test);
	    } catch (JSONException ex) {
	        try {
	            new JSONArray(test);
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}

}
