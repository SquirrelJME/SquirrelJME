// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.lcdui.SerializedEvent;

public interface DisplayListener
{
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

