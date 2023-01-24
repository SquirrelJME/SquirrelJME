// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.wireless.messaging;

import cc.squirreljme.runtime.cldc.annotation.Api;
import javax.microedition.io.Connection;

public interface MessageConnection
	extends Connection
{
	@Api
	String BINARY_MESSAGE =
		"binary";
	
	@Api
	String TEXT_MESSAGE =
		"text";
	
	@Api
	Message newMessage(String __type);
	
	@Api
	Message newMessage(String __type, String __address);
	
	@Api
	int numberOfSegments(Message __msg);
	
	@Api
	Message receive();
	
	@Api
	void send(Message __msg);
	
	@Api
	void setMessageListener(MessageListener __listener);
}
