package com.fusemachines.akka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fusemachines.fsm.MyFSMBase;
import com.fusemachines.fsm.MyFSMBase.State;
import com.fusemachines.kafka.MyKafkaProducer;

import akka.actor.UntypedActor;

@Component
@Scope("prototype")
public class FiniteStateMachineProducerActor extends UntypedActor{

	@Value("${topic}")
	private String topic;
	
	@Autowired
	MyKafkaProducer myKafkaProducer;
	
	@Autowired
	MyFSMBase myFSMBase;
	
	@Override
	public void onReceive(Object message) throws Exception {
			myKafkaProducer.send(message.toString());
			System.out.println(myFSMBase.getState());
			System.out.println("Message Send to Topic");
			myFSMBase.setState(State.THIRD_STATE);
	}

}
