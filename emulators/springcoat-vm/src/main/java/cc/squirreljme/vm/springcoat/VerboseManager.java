// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manages the verbosity level.
 *
 * @since 2020/07/11
 */
public final class VerboseManager
{
	/** The levels used. */
	private final Map<Integer, __Level__> _levels =
		new LinkedHashMap<>();
	
	/** The next code. */
	private int _nextCode;
	
	/**
	 * Adds verbosity at the given frame point.
	 * 
	 * @param __numFrames The frame count to limit at.
	 * @param __flags The verbosity flags to set.
	 * @return The code for the verbosity level.
	 * @since 2020/07/11
	 */
	public final int add(int __numFrames, int __flags)
	{
		// Would have no effect
		if (__flags == 0)
			return 0;
		
		int code = ++this._nextCode;
		
		this._levels.put(code, new __Level__(__numFrames, __flags));
		
		return code;
	}
	
	/**
	 * Checks if verbosity is being used.
	 * 
	 * @param __numFrames The frame count.
	 * @param __flags The flags to check.
	 * @return {@code true} if the given flags are being used.
	 * @since 2020/07/11
	 */
	public final boolean check(int __numFrames, int __flags)
	{
		// No flags to check
		if (__flags == 0)
			return false;
		
		// Go through each one
		for (Iterator<__Level__> iterator = this._levels.values().iterator();
			iterator.hasNext();)
		{
			__Level__ level = iterator.next();
			
			// Dropped below frame
			if (__numFrames < level._numFrames)
				iterator.remove();
			
			// Has matching flag?
			else if ((level._flags & __flags) != 0)
				return true;
		}
		
		// None found
		return false;
	}
	
	/**
	 * Removes the verbose code.
	 * 
	 * @param __code The code to remove.
	 * @since 2020/07/11
	 */
	public final void remove(int __code)
	{
		this._levels.remove(__code);
	}
	
	/**
	 * Stores the verbosity level.
	 * 
	 * @since 2020/07/11
	 */
	private static final class __Level__
	{
		/** The frame count. */
		final int _numFrames;
		
		/** The flags. */
		final int _flags;
		
		/**
		 * Initializes the level.
		 * 
		 * @param __numFrames The frame level.
		 * @param __flags The flags.
		 * @since 2020/07/11
		 */
		__Level__(int __numFrames, int __flags)
		{
			this._numFrames = __numFrames;
			this._flags = __flags;
		}
	}
}
