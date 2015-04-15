package com.gome.clover.akka.serverclient.pubsub;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.zeromq.*;

public class WorkerTaskA extends UntypedActor {
	ActorRef subSocket = ZeroMQExtension.get(getContext().system())
			.newSubSocket(new Connect("tcp://127.0.0.1:1237"),
					new Listener(getSelf()), new Subscribe("someTopic"));

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof ZMQMessage) {
			ZMQMessage m = (ZMQMessage) message;
			String mesg = new String(m.payload(1));
			log.info("Received Message @ A -> {}",mesg);
		}
	}
}
