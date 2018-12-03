// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * This is a choice entry which is drawable.
 *
 * @since 2018/12/02
 */
final class __DrawableChoiceEntry__
	extends __Drawable__
{
	/** The choice to draw. */
	final __ChoiceEntry__ _choice;
	
	/** Text to draw for this choice. */
	private final Text _text =
		new Text();
	
	/**
	 * Initializes the choice entry.
	 *
	 * @param __c The choice to draw.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/02
	 */
	__DrawableChoiceEntry__(__ChoiceEntry__ __c)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		this._choice = __c;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	final void __drawChain(Graphics __g)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	final void __updateDrawChain(__DrawSlice__ __sl)
	{
		throw new todo.TODO();
	}
}

