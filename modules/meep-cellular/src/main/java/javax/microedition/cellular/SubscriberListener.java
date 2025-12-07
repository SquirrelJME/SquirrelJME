// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.cellular;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface SubscriberListener
{
	@Api
	int SUBSCRIBER_ADDED = 0;
	
	@Api
	int SUBSCRIBER_REMOVED = 1;
	
	@Api
	void notifySubscriberEvent(Subscriber __sub, int __event);
}
