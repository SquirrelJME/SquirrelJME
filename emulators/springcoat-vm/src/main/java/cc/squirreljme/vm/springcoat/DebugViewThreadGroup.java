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
import cc.squirreljme.jdwp.views.JDWPViewThreadGroup;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import net.multiphasicapps.classfile.ClassName;

/**
 * A view over a group of threads, in SpringCoat this is an individual machine.
 *
 * @since 2021/04/10
 */
public class DebugViewThreadGroup
	implements JDWPViewThreadGroup
{
	/** The state of the debugger. */
	protected final Reference<JDWPState> state;
	
	/**
	 * Initializes the thread group viewer.
	 * 
	 * @param __state The state.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	public DebugViewThreadGroup(Reference<JDWPState> __state)
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/25
	 */
	@Override
	public Object[] allTypes(Object __which)
	{
		return ((SpringMachine)__which).classLoader().loadedClasses();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public boolean isValid(Object __which)
	{
		return (__which instanceof SpringMachine);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/18
	 */
	@Override
	public Object findType(Object __which, String __name)
	{
		return ((SpringMachine)__which).classLoader()
			.loadClass(new ClassName(__name));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public String name(Object __which)
	{
		return ((SpringMachine)__which).toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public Object[] threads(Object __which)
	{
		// Return all of the threads for this group
		return ((SpringMachine)__which).getThreads();
	}
}
