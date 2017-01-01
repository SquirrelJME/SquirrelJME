// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.util.EventListener;

public interface AccessPointListener
	extends EventListener
{
	public static final int EVENT_TYPE_CONNECTED =
		0;
	
	public static final int EVENT_TYPE_DISCONNECTED =
		1;
	
	public static final int EVENT_TYPE_REMOVED =
		2;
	
	public abstract void notifyEvent(AccessPoint __a, int __b);
}


