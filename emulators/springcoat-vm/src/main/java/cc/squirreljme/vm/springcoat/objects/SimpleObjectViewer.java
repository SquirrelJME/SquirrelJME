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
import cc.squirreljme.vm.springcoat.SpringObject;
import cc.squirreljme.vm.springcoat.SpringPointer;

/**
 * This is a viewer for simple objects.
 *
 * @since 2020/03/22
 */
public class SimpleObjectViewer
	implements ObjectViewer
{
	/** The pointer to this object. */
	protected final SpringPointer pointer;
	
	/**
	 * Initializes the simple object viewer.
	 *
	 * @param __pointer The pointer of this object.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/22
	 */
	public SimpleObjectViewer(SpringPointer __pointer)
		throws NullPointerException
	{
		if (__pointer == null)
			throw new NullPointerException("NARG");
		
		this.pointer = __pointer;
	}
	
	/**
	 * Returns an older {@link SpringObject} view of this object.
	 *
	 * @return The older object view.
	 * @since 2020/03/22
	 */
	@Deprecated
	public SpringObject asSpringObject()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/22
	 */
	@Override
	public SpringPointer pointer()
	{
		throw Debugging.todo();
	}
}
