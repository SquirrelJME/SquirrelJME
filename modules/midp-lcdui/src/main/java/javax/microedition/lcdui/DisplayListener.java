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
public interface DisplayListener
{
	@Api
	@SerializedEvent
	@Async.Execute
	void displayAdded(Display __d);
	
	@Api
	@SerializedEvent
	@Async.Execute
	void displayStateChanged(Display __d, int __ns);
	
	@Api
	@SerializedEvent
	@Async.Execute
	void hardwareStateChanged(Display __d, int __ns);
	
	@Api
	@SerializedEvent
	@Async.Execute
	void orientationChanged(Display __d, int __no);
	
	@Api
	@SerializedEvent
	@Async.Execute
	void sizeChanged(Display __d, int __w, int __h);
}

