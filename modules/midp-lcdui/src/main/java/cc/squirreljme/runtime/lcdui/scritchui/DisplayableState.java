// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 * State for {@link Displayable}.
 *
 * @since 2024/03/08
 */
public final class DisplayableState
{
	/** The displayable this is linked to. */
	protected final Reference<Displayable> displayable;
	
	/** The title of this displayable. */
	protected final StringNotifier title =
		new StringNotifier();
	
	/** The display this is showing on. */
	private volatile Reference<DisplayState> _current;
	
	/**
	 * Initializes the displayable state.
	 *
	 * @param __displayable The displayable this is linked to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/08
	 */
	public DisplayableState(Displayable __displayable)
		throws NullPointerException
	{
		if (__displayable == null)
			throw new NullPointerException("NARG");
		
		this.displayable = new WeakReference<>(__displayable);
	}
	
	/**
	 * Returns the associated displayable.
	 *
	 * @return The associated displayable.
	 * @since 2024/03/08
	 */
	public final Displayable displayable()
	{
		Displayable result = this.displayable.get();
		
		if (result == null)
			throw new IllegalStateException("GCGC");
		
		return result;
	}
}
