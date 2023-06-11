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

@Api
public interface DisplayListener
{
	@Api
	@SerializedEvent
	void displayAdded(Display __d);
	
	@Api
	@SerializedEvent
	void displayStateChanged(Display __d, int __ns);
	
	@Api
	@SerializedEvent
	void hardwareStateChanged(Display __d, int __ns);
	
	@Api
	@SerializedEvent
	void orientationChanged(Display __d, int __no);
	
	@Api
	@SerializedEvent
	void sizeChanged(Display __d, int __w, int __h);
}

