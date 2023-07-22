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
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public final class NotificationType
{
	@Api
	public static final NotificationType CALL =
		new NotificationType(null, null);
	
	@Api
	public static final NotificationType EMAIL =
		new NotificationType(null, null);
	
	@Api
	public static final NotificationType IM =
		new NotificationType(null, null);
	
	@Api
	public static final NotificationType MMS =
		new NotificationType(null, null);
	
	@Api
	public static final NotificationType REMINDER =
		new NotificationType(null, null);
	
	@Api
	public static final NotificationType SMS =
		new NotificationType(null, null);
	
	@Api
	public NotificationType(String __dl, Image __di)
	{
		throw Debugging.todo();
	}
}

