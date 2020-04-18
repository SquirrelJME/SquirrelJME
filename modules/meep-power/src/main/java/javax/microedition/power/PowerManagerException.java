// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.power;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public class PowerManagerException
	extends RuntimeException
{
	public static final byte ILLEGAL_STATE_TRANSITION_REQUEST = 0;
	
	public static final byte KEEP_CURRENT_STATE = 1;
	
	public static final byte STATE_TRANSITION_FAILURE = 2;
	
	public PowerManagerException(byte var1)
	{
		throw Debugging.todo();
	}
	
	public PowerManagerException(byte var1, String var2)
	{
		throw Debugging.todo();
	}
	
	public byte getReason()
	{
		throw Debugging.todo();
	}
}
