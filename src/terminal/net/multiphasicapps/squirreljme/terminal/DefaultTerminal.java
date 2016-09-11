// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terminal;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * This caches and returns the default terminal device to use.
 *
 * @since 2016/09/11
 */
public final class DefaultTerminal
{
	/**
	 * {@squirreljme.property net.multiphasicapps.squirreljme.terminal=(class)
	 * This is an optional property which specifies the provider for the
	 * default terminal instance.}
	 */
	public static final String TERMINAL_PROPERTY =
		"net.multiphasicapps.squirreljme.terminal";
	
	/** The default terminal that was selected. */
	private static volatile Reference<Terminal> _DEFAULT;
	
	/**
	 * Not used.
	 *
	 * @since 2016/09/11
	 */
	private DefaultTerminal()
	{
	}
	
	/**
	 * Returns the default terminal or {@code null} if there is no default.
	 *
	 * @return The default terminal or {@code null} if there is none.
	 * @since 2016/09/11
	 */
	public static Terminal getDefault()
	{
		// Get
		Reference<Terminal> ref = DefaultTerminal._DEFAULT;
		Terminal rv = null;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// See if it is specified as a default
			String prop = System.getProperty(TERMINAL_PROPERTY);
			if (prop != null)
				try
				{
					// Get class
					Class<?> cl = Class.forName(prop);
				
					// Create instance
					TerminalProvider tp = (TerminalProvider)cl.newInstance();
				
					// Get it
					rv = tp.terminal();
				}
			
				// Ignore
				catch (ClassCastException|IllegalAccessException|
					ClassNotFoundException|InstantiationException e)
				{
				}
			
			// Look through all services
			if (rv == null)
			{
				// Fill provider list
				List<TerminalProvider> provs = new ArrayList<>();
				for (TerminalProvider pv : ServiceLoader.<TerminalProvider>
					load(TerminalProvider.class))
					if (pv.priority() >= 0)
						provs.add(pv);
				
				// Go through when none has been selected
				while (rv == null && !provs.isEmpty())
				{
					int best = Integer.MIN_VALUE, bestdx = -1;
					
					// Find the highest providing terminal provider
					int n = provs.size();
					for (int i = 0; i < n; i++)
					{
						TerminalProvider at = provs.get(i);
						
						// Is this better?
						int p = at.priority();
						if (p > best)
						{
							best = p;
							bestdx = i;
						}
					}
					
					// Found one?
					if (bestdx > 0)
						rv = provs.remove(bestdx).terminal();
				}
			}
			
			// Cache
			DefaultTerminal._DEFAULT = new WeakReference<>(rv);
		}
		
		// Return it
		return rv;
	}
}

