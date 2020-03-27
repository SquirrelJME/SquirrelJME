// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.objects;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.ReferenceChainer;
import cc.squirreljme.vm.springcoat.ReferenceCounter;
import cc.squirreljme.vm.springcoat.SpringClass;
import cc.squirreljme.vm.springcoat.SpringMonitor;
import cc.squirreljme.vm.springcoat.SpringObject;
import cc.squirreljme.vm.springcoat.SpringPointerArea;

/**
 * This wraps .
 *
 * @since 2020/03/26
 */
@Deprecated
public final class SpringObjectViewer
	implements SpringObject
{
	/** The object to view. */
	protected final ObjectViewer viewer;
	
	/**
	 * Initializes the viewer from the new method to the old method.
	 *
	 * @param __viewer The viewer to represent.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/26
	 */
	public SpringObjectViewer(ObjectViewer __viewer)
		throws NullPointerException
	{
		if (__viewer == null)
			throw new NullPointerException("NARG");
		
		this.viewer = __viewer;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/26
	 */
	@Deprecated
	@Override
	public SpringMonitor monitor()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/26
	 */
	@Deprecated
	@Override
	public SpringPointerArea pointerArea()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/26
	 */
	@Deprecated
	@Override
	public ReferenceChainer refChainer()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the object viewer this wraps.
	 *
	 * @return The viewer this wraps.
	 * @since 2020/03/26
	 */
	public ObjectViewer objectViewer()
	{
		return this.viewer;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/26
	 */
	@Deprecated
	@Override
	public ReferenceCounter refCounter()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/26
	 */
	@Deprecated
	@Override
	public SpringClass type()
	{
		throw Debugging.todo();
	}
}
