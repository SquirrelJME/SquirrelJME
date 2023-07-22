// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.key;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface InputDevice
{
	@Api
	void setKeyListener(KeyListener __l);

	@Api
	String getId();

	@Api
	boolean isHardwareAssigned();

	@Api
	void setHardwareAssigned(boolean __h);
}

