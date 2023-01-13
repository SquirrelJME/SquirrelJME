// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIWidgetBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.lcdui.Displayable;

/**
 * Manages the current linked display for a {@link Displayable} and any
 * associated {@link UIFormBracket} and {@link UIWidgetBracket} on a given
 * {@link LinkedDisplay}, this makes it easier to manage and switch between
 * the different display states.
 *
 * @since 2023/01/12
 */
public final class CurrentLinkedDisplay
{
	/** Creator for the linked display state. */
	private final LinkedDisplayStateCreator _creator;
	
	/** The states that are currently available. */
	private final Map<LinkedDisplay, LinkedDisplayState> _states =
		new HashMap<>();
	
	/** The actual current state. */
	private volatile LinkedDisplay _current;
	
	/**
	 * Initializes the linked display state.
	 * 
	 * @param __creator The creator used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/01/12
	 */
	public CurrentLinkedDisplay(LinkedDisplayStateCreator __creator)
		throws NullPointerException
	{
		if (__creator == null)
			throw new NullPointerException("NARG");
		
		this._creator = __creator;
	}
	
	/**
	 * Returns the current linked display state.
	 * 
	 * @param <S> The type to use as the display state.
	 * @param __type The type to use as the display state.
	 * @return The current linked display state, if there is none then
	 * {@code null} will be returned.
	 * @since 2023/01/12
	 */
	public <S extends LinkedDisplayState> S getCurrent(Class<S> __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			LinkedDisplay current = this._current;
			
			if (current != null)
				return __type.cast(this._states.get(current));
			
			return null;
		}
	}
	
	/**
	 * Sets the current linked display, will potentially initialize any fields
	 * and otherwise if the information has not yet been set.
	 * 
	 * @param __display The display to set.
	 * @return The newly set linked display state.
	 * @since 2023/01/12
	 */
	public LinkedDisplayState setCurrent(LinkedDisplay __display)
	{
		throw Debugging.todo();
	}
}
