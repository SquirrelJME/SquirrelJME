// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.meep.lui.terminal;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import net.multiphasicapps.squirreljme.meep.lui.DisplayDriver;
import net.multiphasicapps.squirreljme.meep.lui.DisplayProvider;
import net.multiphasicapps.squirreljme.terminal.DefaultTerminal;
import net.multiphasicapps.squirreljme.terminal.Terminal;
import net.multiphasicapps.util.empty.EmptyIterator;

/**
 * This implements the terminal display driver.
 *
 * @since 2016/09/09
 */
public class TerminalDisplayProvider
	implements DisplayProvider
{
	/** The LIU terminal used. */
	private volatile LUIOnTerminal _luiterm;
	
	/** No terminal available? */
	private volatile boolean _none;
	
	/**
	 * Initializes the terminal display provider.
	 *
	 * @since 2016/09/11
	 */
	public TerminalDisplayProvider()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public Iterator<DisplayDriver> iterator()
	{
		// No terminal used?
		if (this._none)
			return EmptyIterator.<DisplayDriver>empty();
		
		// Need to setup the terminal?
		LUIOnTerminal rv = this._luiterm;
		if (rv == null)
		{
			// Get
			Terminal t = DefaultTerminal.getDefault();
			
			// If no terminal to use, stop
			if (t == null)
			{
				this._none = true;
				return EmptyIterator.<DisplayDriver>empty();
			}
			
			// Create
			rv = new LUIOnTerminal(t);
			
			// Set
			this._luiterm = rv;
		}
		
		// Iterate over the single instance
		return new __Iterator__();
	}
	
	/**
	 * Iterates over the single LUI provider.
	 *
	 * @since 2016/09/11
	 */
	private final class __Iterator__
		implements Iterator<DisplayDriver>
	{
		/** Still has to be access */
		private volatile boolean _has =
			true;
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public boolean hasNext()
		{
			return this._has;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public DisplayDriver next()
		{
			// If no iterated yet, clear and return it
			if (this._has)
			{
				this._has = false;
				
				// Return it
				return TerminalDisplayProvider.this._luiterm;
			}
			
			// {@squirreljme.error AO02 Value already iterated.}
			throw new NoSuchElementException("AO02");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public void remove()
		{
			// {@squirreljme.error AO01 Cannot remove display drivers.}
			throw new UnsupportedOperationException("AO01");
		}
	}
}

