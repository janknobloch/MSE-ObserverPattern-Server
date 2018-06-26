package de.tum.jk.behavioural.observer;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat")
public class ChatEndPoint {

	static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println("incoming msg:" + message);
		for (Session s : peers) {
			// if (!s.equals(session)) {
			if (message.contains("\n"))
				s.getBasicRemote().sendText(session.getId() + ": " + message);
			else
				s.getBasicRemote().sendText(session.getId() + ": " + message + "\n");
			// }
		}

	}

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) throws IOException {
		peers.add(session);
		for (Session s : peers) {
			// if (!s.equals(session))
			s.getBasicRemote().sendText("Connected Students:" + peers.size() + "\n");
		}
		// properties = config.getUserProperties();
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) throws IOException {

		peers.remove(session);
		for (Session s : peers) {
			// if (!s.equals(session))
			s.getBasicRemote().sendText("Connected Students:" + peers.size() + "\n");
		}
	}

	@OnError
	public void onError(Session session, Throwable t) {
		t.printStackTrace();
	}
}