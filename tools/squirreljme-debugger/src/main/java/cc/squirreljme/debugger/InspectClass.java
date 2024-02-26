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
import javax.swing.JDialog;

/**
 * Inspects classes.
 *
 * @since 2024/01/24
 */
public class InspectClass
	extends Inspect<InfoClass>
{
	/**
	 * Initializes the class inspector.
	 *
	 * @param __owner The owning frame.
	 * @param __state The debugger state.
	 * @param __info The class information.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/24
	 */
	public InspectClass(Window __owner, DebuggerState __state,
		InfoClass __info)
		throws NullPointerException
	{
		super(__owner, __state, __info);
		
		// Track these
		this.addTrack("Name", __info.thisName);
		this.addTrack("Constant Pool", __info.constantPool);
		this.addTrack("Methods", __info.methods);
		
		// Update inspection
		this.update();
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
