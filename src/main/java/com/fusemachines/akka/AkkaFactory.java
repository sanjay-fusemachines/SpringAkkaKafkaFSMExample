package com.fusemachines.akka;

import akka.actor.ActorSystem;

public class AkkaFactory {
	
	private static ActorSystem FSMActor = ActorSystem.create("AkkaSystem");
	
	public static ActorSystem getActorSystem(){
		return FSMActor;
	}
	
}
