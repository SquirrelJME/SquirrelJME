// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.Window;

/**
 * Not Described.
 *
 * @since 2024/01/24
 */
public class InspectMethod
	extends Inspect<InfoMethod>
{
	/**
	 * Initializes the method inspector.
	 *
	 * @param __owner The owning window.
	 * @param __state The debugger state.
	 * @param __info The method information.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/24
	 */
	public InspectMethod(Window __owner, DebuggerState __state,
		InfoMethod __info)
		throws NullPointerException
	{
		super(__owner, __state, __info);
		
		this.addTrack("Name", __info.name);
		this.addTrack("Type", __info.type);
		this.addTrack("Flags", __info.flags);
		this.addTrack("Bytecode", __info.byteCode);
		
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/24
	 */
	@Override
	protected void updateInternal()
	{
	}
}
