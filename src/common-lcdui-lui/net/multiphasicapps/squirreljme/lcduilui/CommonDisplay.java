// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcduilui;

/**
 * This is the base class for common display handlers.
 *
 * @param <R> The raw display type to use.
 * @since 2016/10/08
 */
public abstract class CommonDisplay<R>
{
	/** Lock. */
	protected final Object lock =
		new Object();	
	
	/** The bound raw display. */
	private volatile R _raw;
	
	/** The instance of the display. */
	private volatile CommonDisplayInstance _instance;
	
	/**
	 * Creates a new instance for this display.
	 *
	 * @return The newly created instance.
	 * @since 2016/10/08
	 */
	protected abstract CommonDisplayInstance createInstance();
	
	/**
	 * Returns the instance of this display.
	 *
	 * @return The display instance.
	 * @since 2016/10/08
	 */
	public final CommonDisplayInstance getInstance()
	{
		// Lock
		synchronized (this.lock)
		{
			// Needs to be created?
			CommonDisplayInstance rv = this._instance;
			if (rv == null)
				this._instance = (rv = createInstance());
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Binds a raw display to this common display.
	 *
	 * @param __r The raw display to bind.
	 * @throws IllegalStateException If a display is already bound.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	public final void bindRawDisplay(R __r)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error EH01 Could not bind the raw display because
			// it is already bound.}
			if (this._raw != null)
				throw new IllegalStateException("EH01");
			
			// Set
			this._raw = __r;
		}
	}
	
	/**
	 * Returns the bound raw display, if one has been set.
	 *
	 * @return The bound raw display, or {@code null} if it was not bound.
	 * @since 2016/10/08
	 */
	public final R getRawDisplay()
	{
		// Lock
		synchronized (this.lock)
		{
			return this._raw;
		}
	}
}

