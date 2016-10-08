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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * This is a base class that is used for managing displays that are
 * available for usage.
 *
 * @param <R> The raw object to use for the display.
 * @param <D> The driver for the given raw display.
 * @param <P> The common display provider to use
 * @since 2016/10/08
 */
public class CommonDisplayManager<R, D extends CommonDisplay<R>,
	P extends CommonDisplayProvider<R, D>>
	implements Iterable<D>
{
	/** The class to provide a service for. */
	protected final Class<D> driverclass;
	
	/** The raw class. */
	protected final Class<R> rawclass;
	
	/** The service loader for display providers. */
	protected final ServiceLoader<P> services;
	
	/**
	 * Initializes the common display manager.
	 *
	 * @param __rl The raw display class.
	 * @param __dl The driver class.
	 * @param __pl The provider class to use for service lookup.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	public CommonDisplayManager(Class<R> __rl, Class<D> __dl, Class<P> __pl)
		throws NullPointerException
	{
		// Check
		if (__rl == null || __dl == null || __pl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.rawclass = __rl;
		this.driverclass = __dl;
		this.services = ServiceLoader.<P>load(__pl);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public Iterator<D> iterator()
	{
		return new __DriverIterator__();
	}
	
	/**
	 * This iterators over internal drivers.
	 *
	 * @since 2016/10/08
	 */
	private final class __DriverIterator__
		implements Iterator<D>
	{
		/** Display list. */
		protected final Iterator<D> displays;
		
		/**
		 * Pre-fill displays from providers.
		 *
		 * @since 2016/10/08
		 */
		{
			// Setup for filling
			List<D> rv = new ArrayList<>();
			for (P p : CommonDisplayManager.this.services)
				for (D d : p.getDisplays())
					rv.add(d);
			
			// Iterator
			this.displays = rv.iterator();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/08
		 */
		@Override
		public boolean hasNext()
		{
			return this.displays.hasNext();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/08
		 */
		@Override
		public D next()
		{
			return this.displays.next();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/08
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

