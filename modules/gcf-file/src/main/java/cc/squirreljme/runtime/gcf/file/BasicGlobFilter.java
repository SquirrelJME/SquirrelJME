// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Basic filtering for globs.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public final class BasicGlobFilter
	implements Iterator<String>
{
	/** Filter string match order. */
	private final String[] _order;
	
	/** The iterator to process. */
	private final Iterator<String> _it;
	
	/** The next queued item. */
	private volatile String _queue;
	
	/**
	 * Initializes the glob filter.
	 *
	 * @param __filter The filter to use.
	 * @param __iterator The iterator to iterate over.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public BasicGlobFilter(String __filter, Iterator<String> __iterator)
		throws NullPointerException
	{
		if (__filter == null || __iterator == null)
			throw new NullPointerException("NARG");
		
		// Iterator is just a simple copy
		this._it = __iterator;
		
		// The user may decide to be sneaky and try to pass something
		// like **cute****squirrels** which is equivalent to *cute*squirrels*
		StringBuilder sb = new StringBuilder(__filter.length());
		for (int n = __filter.length(), i = 0; i < n; i++)
		{
			char c = __filter.charAt(i);
			
			// Deduplicate asterisks, add everything else otherwise
			int sbl = sb.length();
			if (c != '*' || (c == '*' && sbl > 0 && sb.charAt(sbl - 1) != '*') || (c == '*' && sbl == 0))
				sb.append(c);
		}
		
		// Now need to divide the filter into orders
		List<String> order = new ArrayList<>();
		StringBuilder fl = new StringBuilder(__filter.length());
		for (int n = sb.length(), i = 0; i < n; i++)
		{
			char c = sb.charAt(i);
			
			// Wildcard?
			if (c == '*')
			{
				// If anything was before this, add it
				if (fl.length() > 0)
					order.add(fl.toString());
				fl.delete(0, fl.length());
				
				// null indicates wildcard
				order.add(null);
			}
			
			// Otherwise add to the filter order
			else
				fl.append(c);
		}
		
		// If anything is left over, add it to the final order
		if (fl.length() > 0)
			order.add(fl.toString());
		fl.delete(0, fl.length());
		
		// Finalize the order
		this._order = order.toArray(new String[order.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2025/12/28
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("RORO");
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2025/12/30
	 */
	@Override
	public String next()
		throws NoSuchElementException
	{
		// Check next first
		if (!this.hasNext())
			throw new NoSuchElementException("NSEE");
		
		// Get the next queued item and clear it
		String queue = this._queue;
		this._queue = null;
		return queue;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2025/12/30
	 */
	@Override
	public boolean hasNext()
	{
		// If there is already a queued item, do nothing
		if (this._queue != null)
			return true;
		
		Iterator<String> it = this._it;
		
		// See if this item should be filtered
		String queue = null;
		while (queue == null)
			try
			{
				// The current and dot specifiers are never returned
				String maybe = it.next();
				if (".".equals(maybe) || "..".equals(maybe))
					continue;
				
				// Get the next file name
				if (this.__isMatch(maybe))
				{
					queue = maybe;
					break;
				}
			}
			catch (NoSuchElementException __e)
			{
				return false;
			}
		
		// Store into the queue, if there was anything
		this._queue = queue;
		return queue != null;
	}
	
	/**
	 * Does this match the filter?
	 *
	 * @param __maybe The filter to check.
	 * @return If this matches the filter.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/30
	 */
	private boolean __isMatch(String __maybe)
		throws NullPointerException
	{
		if (__maybe == null)
			throw new NullPointerException("NARG");
		
		// Debug
		if (Debugging.VERBOSE)
			Debugging.debugNote("%s ?~= %s", Arrays.asList(this._order),
				__maybe);
		
		// Go through the entire order to find sequences
		String[] order = this._order;
		int orderAt = 0;
		for (int si = 0, n = __maybe.length(); si < n; )
		{
			// Reached the end of the order list before we found the end
			// of the string? We failed to find items in it
			if (orderAt >= order.length)
				return false;
			
			// What is currently here?
			String want = order[orderAt++];
			
			// Wildcard is here?
			if (want == null)
			{
				// If there is a string to the right of this wildcard, we
				// need to find that string to jump all the characters
				String right = (orderAt < order.length ? order[orderAt] : null);
				if (right != null)
				{
					// Find the string on the right side, if it is missing
					// then this is not a match
					int where = __maybe.indexOf(right, si);
					if (where < 0)
						return false;
					
					// Jump up to that position
					si = where;
				}
				
				// Otherwise consume all characters, as this takes whatever
				// remains
				else
					si = n;
			}
			
			// String sequence is here
			else
			{
				// This must exactly be at our current index
				int where = __maybe.indexOf(want, si);
				if (where != si)
					return false;
				
				// Move up past the wanted string
				si += want.length();
			}
		}
		
		// Only when all orders have been processed is this considered to
		// actually be valid
		return (orderAt == order.length);
	}
}
