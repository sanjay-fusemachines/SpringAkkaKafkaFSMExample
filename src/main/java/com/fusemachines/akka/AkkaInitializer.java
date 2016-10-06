package com.fusemachines.akka;

import com.fusemachines.ContextContainer;
import com.fusemachines.config.SpringExtension;

import akka.actor.ActorRef;

public class AkkaInitializer {
	
	private ActorRef producerActorRef;
	private ActorRef consumerActorRef;
	public AkkaInitializer() {
		SpringExtension ext = ContextContainer.getContext().getBean(SpringExtension.class);
		ext.initialize(ContextContainer.getContext());	
		
		this.producerActorRef = AkkaFactory.getActorSystem().actorOf(ext.props("finiteStateMachineProducerActor"),"fsmProducer");
		this.consumerActorRef = AkkaFactory.getActorSystem().actorOf(ext.props("finiteStateMachineConsumerActor"),"fsmConsumer");
		
	}
	

}
