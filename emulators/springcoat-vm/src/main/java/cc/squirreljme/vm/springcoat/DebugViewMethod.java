// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jdwp.JDWPState;
import cc.squirreljme.jdwp.JDWPViewMethod;
import java.lang.ref.Reference;

/**
 * Debugging view for methods.
 *
 * @since 2021/04/11
 */
public class DebugViewMethod
	implements JDWPViewMethod
{
	/** The state of the debugger. */
	protected final Reference<JDWPState> state;
	
	/**
	 * Initializes the thread viewer.
	 * 
	 * @param __state The state.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	public DebugViewMethod(Reference<JDWPState> __state)
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
	}
}
