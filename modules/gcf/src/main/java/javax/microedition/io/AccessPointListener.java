// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.util.EventListener;

public interface AccessPointListener
	extends EventListener
{
	int EVENT_TYPE_CONNECTED =
		0;
	
	int EVENT_TYPE_DISCONNECTED =
		1;
	
	int EVENT_TYPE_REMOVED =
		2;
	
	void notifyEvent(AccessPoint __a, int __b);
}


