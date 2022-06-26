// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.wireless.messaging;

import javax.microedition.io.Connection;

public interface MessageConnection
	extends Connection
{
	String BINARY_MESSAGE =
		"binary";
	
	String TEXT_MESSAGE =
		"text";
	
	Message newMessage(String __type);
	
	Message newMessage(String __type, String __address);
	
	int numberOfSegments(Message __msg);
	
	Message receive();
	
	void send(Message __msg);
	
	void setMessageListener(MessageListener __listener);
}
