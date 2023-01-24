// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.key;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * DESCRIBE THIS.
 *
 * @since 2016/08/30
 */
public interface InputDeviceListener
{
	@Api
	void hardwareStateChanged(InputDevice __dev, boolean __hw);
	
	@Api
	void inputDeviceAdded(InputDevice __dev);
	
	@Api
	void inputDeviceRemoved(InputDevice __dev);
}

