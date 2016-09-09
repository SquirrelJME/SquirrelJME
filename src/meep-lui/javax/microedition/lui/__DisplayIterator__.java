// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lui;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.multiphasicapps.squirreljme.meep.lui.DisplayDriver;
import net.multiphasicapps.squirreljme.meep.lui.DisplayProvider;

/**
 * This is an interator which goes through all display providers and returns
 * any display services that they expose.
 *
 * @since 2016/09/09
 */
final class __DisplayIterator__
	implements Iterator<Display>
{
	/** Only use input? */
	protected final boolean input;
	
	/** The providers to use. */
	private final DisplayProvider[] _providers;
	
	/** The number of providers available. */
	private final int _n;
	
	/** The current array position. */
	private volatile int _at = -1;
	
	/** The next display to return. */
	private volatile Display _next;
	
	/** The current sub-iterator. */
	private volatile Iterator<DisplayDriver> _sub;
	
	/**
	 * Initializes the iterator.
	 *
	 * @param __dp The display providers.
	 * @param __input Only consider providers which provide input
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	__DisplayIterator__(DisplayProvider[] __dp, boolean __input)
		throws NullPointerException
	{
		// Check
		if (__dp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._providers = __dp;
		this.input = __input;
		this._n = __dp.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public boolean hasNext()
	{
		return (__enqueue() != null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public Display next()
	{
		// Get next
		Display rv = __enqueue();
		
		// {@squirreljme.error DA03 No more displays are available.}
		if (rv == null)
			throw new NoSuchElementException("DA03");
		
		// Return it
		this._next = null;
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public void remove()
	{
		// {@squirreljme.error DA02 Displays cannot be removed from this
		// iterator.}
		throw new UnsupportedOperationException("DA02");
	}
	
	/**
	 * Finds and returns the next available display.
	 *
	 * @return The next available display. 
	 * @since 2016/09/09
	 */
	private final Display __enqueue()
	{
		// If there is a next display, return it
		Display rv = this._next;
		if (rv != null)
			return rv;
		
		// Loop to find providers
		DisplayProvider[] providers = this._providers;
		int n = this._n;
		int at = this._at;
		while (at < n)
		{
			// Load in new iterator?
			Iterator<DisplayDriver> sub = this._sub;
			if (sub == null || !sub.hasNext())
			{
				// Increase and store
				at++;
				this._at = at;
				
				// If at the end, stop
				if (at >= n)
					return null;
				
				// Otherwise get iterator from a provider
				sub = providers[at].iterator();
				this._sub = sub;
			}
			
			// Get the next
			DisplayDriver drv = sub.next();
			
			// This should not normally occur
			if (drv == null)
				continue;
			
			// Lock
			synchronized (drv)
			{
				// Get
				rv = drv.getDisplay();
				
				// If no display is associated with a given driver then
				// create and associate it
				if (rv == null)
				{
					rv = new Display(drv);
					drv.setDisplay(rv);
				}
			}
			
			// Cache the display
			this._next = rv;
			break;
		}
		
		// Return
		return rv;
	}
}

