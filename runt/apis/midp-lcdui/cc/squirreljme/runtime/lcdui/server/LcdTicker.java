// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.lcdui.CollectableType;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This represents a ticker which represents a sequence of scrolling text for
 * information purposes.
 *
 * @since 2018/03/26
 */
public abstract class LcdTicker
	extends LcdCollectable
{
	/** Things which are listening to this ticker for change events. */
	private final Set<Reference<LcdTickerListener>> _listeners =
		new LinkedHashSet<>();
	
	/** The ticker text. */
	private volatile String _text;
	
	/**
	 * Initializes the ticker with the given handle.
	 *
	 * @param __handle The handle used.
	 * @since 2018/03/26
	 */
	public LcdTicker(int __handle)
	{
		super(__handle, CollectableType.TICKER);
	}
	
	/**
	 * Adds a listener for this ticker.
	 *
	 * The listener which is added will have the
	 * {@link LcdTickerListener#textChange(LcdTicker, String)} method called
	 * so that it can be known which text to show.
	 *
	 * @param __l The ticker to send changes to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	public final void addListener(LcdTickerListener __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		// Store listener and also call it right after it has been added
		// so it could be updated with the correct text without needing the
		// ticker text
		this._listeners.add(new WeakReference<>(__l));
		__l.textChanged(this, this._text);
	}
	
	/**
	 * Sets the text for the ticker.
	 *
	 * @param __t The text for the ticker.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	public final void setText(String __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this._text = __t;
		
		Set<Reference<LcdTickerListener>> listeners = this._listeners;
		for (Iterator<Reference<LcdTickerListener>> it = listeners.iterator();
			it.hasNext();)
		{
			Reference<LcdTickerListener> ref = it.next();
			LcdTickerListener ltl;
			
			// Inform that the text has changed
			if (ref != null && null != (ltl = ref.get()))
				ltl.textChanged(this, __t);
			
			// Remove old listeners
			else
				it.remove();
		}
	}
	
	/**
	 * Returns the ticker's text.
	 *
	 * @return The ticker text.
	 * @since 2018/03/26
	 */
	public final String text()
	{
		return this._text;
	}
}

