package com.fusemachines.akka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fusemachines.fsm.MyFSMBase;
import com.fusemachines.fsm.MyFSMBase.State;
import com.fusemachines.kafka.MyKafkaConsumer;

import akka.actor.UntypedActor;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;

@Component
@Scope("prototype")
public class FiniteStateMachineConsumerActor extends UntypedActor{
	
	@Value("${consumertopic}")
	private String topic;
	
	@Autowired
	private MyFSMBase myFSMBase;
	
	ZkClient zkClient = null;
    ZkUtils zkUtils = null;
    MyKafkaConsumer myKafkaConsumer = new MyKafkaConsumer("localhost:2181", "myGroup");
    

	@Override
	public void onReceive(Object message) throws Exception {
		System.out.println(myFSMBase.getState());
		if((String)message == "Print Message"){
		
			zkClient = new ZkClient("localhost:2181", 10000, 10000, ZKStringSerializer$.MODULE$);
			zkUtils = new ZkUtils(zkClient, new ZkConnection("localhost:2181"), false);
		        ConsumerConnector consumerConnector = myKafkaConsumer.getConsumerConnector();
		        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		        topicCountMap.put(topic, new Integer(1));
		        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector
		                .createMessageStreams(topicCountMap);
		        KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);//remove args if error
		        ConsumerIterator<byte[], byte[]> it = stream.iterator();
		        while (it.hasNext()) {
		            String newString = new String(it.next().message());
		            processMessage(newString);	
		            break;
			}
		}else{
			unhandled(message);
		}
	}
	
	public void processMessage(String newString){
		System.out.println("Message From Topic");
		System.out.println(newString);
		myFSMBase.setState(State.FIRST_STATE);
	}

}
