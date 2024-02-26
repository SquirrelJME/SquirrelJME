// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Manages the verbosity level.
 *
 * @since 2020/07/11
 */
public final class VMTraceFlagTracker
{
	/**
	 * {@squirreljme.property cc.squirreljme.vm.trace=flags
	 * Enable tracing within the virtual machine?}
	 */
	public static final String TRACING_ENABLED =
		"cc.squirreljme.vm.trace";
	
	/** Tracing bits that are defined by system properties. */
	public static final int GLOBAL_TRACING_BITS;
	
	/** The levels used. */
	private final Map<Integer, __Level__> _levels =
		new LinkedHashMap<>();
	
	/** The next code. */
	private int _nextCode;
	
	static
	{
		// Decode the tracing flags to see if some bits are enabled
		String tracing = System.getProperty(
			VMTraceFlagTracker.TRACING_ENABLED);
		int enableBits = 0;
		if (tracing != null)
			enableBits = VMTraceFlagTracker.parseBits(tracing);
		
		// Set enabled bits
		GLOBAL_TRACING_BITS = enableBits;
	}
	
	/**
	 * Initializes a base blank flag tracker.
	 * 
	 * @since 2024/01/14
	 */
	public VMTraceFlagTracker()
	{
	}
	
	/**
	 * Creates a trace tracker using the specified tracker as a base.
	 *
	 * @param __from The source trace tracker.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/14
	 */
	public VMTraceFlagTracker(VMTraceFlagTracker __from)
		throws NullPointerException
	{
		if (__from == null)
			throw new NullPointerException("NARG");
		
		this._nextCode = __from._nextCode;
		
		// Copy all sub-levels
		Map<Integer, __Level__> src = __from._levels;
		Map<Integer, __Level__> dst = this._levels;
		for (Map.Entry<Integer, __Level__> e : src.entrySet())
			dst.put(e.getKey(), new __Level__(e.getValue()));
	}
	
	/**
	 * Returns all the active verbose flags.
	 * 
	 * @return The active verbose flags.
	 * @since 2022/06/12
	 */
	public int activeFlags()
	{
		int rv = 0;
		
		for (Integer i : this._levels.keySet())
			if (i != null)
				rv |= i;
			
		return rv;
	}
	
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
	 * Parses the given bits of input.
	 *
	 * @param __input The input to parse.
	 * @return The resultant bit set.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/14
	 */
	public static int parseBits(String __input)
		throws NullPointerException
	{
		if (__input == null)
			throw new NullPointerException("NARG");
		
		// Set all the bits accordingly
		int enableBits = 0;
		for (String item : __input.split(Pattern.quote(",")))
		{
			for (VMTraceFlag flag : VMTraceFlag.values())
				if (flag.names.contains(item))
					enableBits |= flag.bits;
		}
		
		return enableBits;
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
		
		/**
		 * Initializes the level from the given level.
		 *
		 * @param __from The source level.
		 * @throws NullPointerException On null arguments.
		 * @since 2024/01/14
		 */
		__Level__(__Level__ __from)
			throws NullPointerException
		{
			if (__from == null)
				throw new NullPointerException("NARG");
			
			this._flags = __from._flags;
			this._numFrames = __from._numFrames;
		}
	}
}
