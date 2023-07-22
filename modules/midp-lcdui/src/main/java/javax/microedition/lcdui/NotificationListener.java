// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import org.jetbrains.annotations.Async;

@Api
public interface NotificationListener
{
	@Api
	@SerializedEvent
	@Async.Execute
	void notificationDismissed(Notification __n);
	
	@Api
	@SerializedEvent
	@Async.Execute
	void notificationSelected(Notification __n);
	
	@Api
	@SerializedEvent
	@Async.Execute
	void notificationTimeout(Notification __n);
}

