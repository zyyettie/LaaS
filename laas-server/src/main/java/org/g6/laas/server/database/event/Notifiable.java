package org.g6.laas.server.database.event;

import java.util.Collection;

public interface Notifiable<U> {

	U receiveFrom();
	
	Collection<U> sendTo();
	
	String getSummary();
}
