// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.widget;

import java.lang.ref.Reference;
import javax.microedition.lcdui.Displayable;
import net.multiphasicapps.squirreljme.lcdui.NativeResource;

/**
 * This class represents a natively displayable widget.
 *
 * @since 2017/10/25
 */
public abstract class DisplayableWidget
	implements NativeResource
{
	/** The displayable reference. */
	protected final Reference<Displayable> reference;
	
	/**
	 * Initializes the displayable widget.
	 *
	 * @param __ref The reference to the displayable.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/25
	 */
	public DisplayableWidget(Reference<Displayable> __ref)
		throws NullPointerException
	{
		if (__ref == null)
			throw new NullPointerException("NARG");
		
		this.reference = __ref;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/25
	 */
	@Override
	public final Reference<? extends Object> boundObject()
	{
		return this.reference;
	}
	
	/**
	 * Embeds the specified embedded into this displayable.
	 *
	 * @param __e The embedded to embed.
	 * @throws IllegalStateException If there is alreayd something embedded.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/25
	 */
	public final void embed(Embedded __e)
		throws IllegalStateException, NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/25
	 */
	@Override
	public final void freeResource()
	{
		throw new todo.TODO();
	}
}

