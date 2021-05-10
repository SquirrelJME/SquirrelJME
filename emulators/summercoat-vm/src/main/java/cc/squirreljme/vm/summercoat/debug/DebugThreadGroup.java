// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat.debug;

import cc.squirreljme.jdwp.views.JDWPViewThreadGroup;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Viewer for thread groups.
 *
 * @since 2021/05/10
 */
public class DebugThreadGroup
	implements JDWPViewThreadGroup
{
	/**
	 * {@inheritDoc}
	 * @since 2021/05/10
	 */
	@Override
	public Object[] allTypes(Object __which)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/05/10
	 */
	@Override
	public void exit(Object __which, int __code)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/05/10
	 */
	@Override
	public Object findType(Object __which, String __name)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/05/10
	 */
	@Override
	public boolean isValid(Object __which)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/05/10
	 */
	@Override
	public String name(Object __which)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/05/10
	 */
	@Override
	public Object[] threads(Object __which)
	{
		throw Debugging.todo();
	}
}
