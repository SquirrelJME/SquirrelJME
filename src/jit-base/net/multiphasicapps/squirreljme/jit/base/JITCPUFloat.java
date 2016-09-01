// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.base;

/**
 * This represents the kind of floating point support is used for a given
 * target.
 *
 * @since 2016/08/29
 */
public enum JITCPUFloat
{
	/** Pure software. */
	SOFT("soft", false, false),
	
	/** Hardware 32-bit, Software 64-bit. */
	HARD32SOFT64("hard32soft64", true, false),
	
	/** Software 32-bit, Hardware 64-bit. */
	SOFT32HARD64("soft32hard64", false, true),
	
	/** Pure hardware. */
	HARD("hard", true, true),
	
	/** End. */
	;
	
	/** The name of the floating point type. */
	protected final String name;
	
	/** Hardware 32-bit support? */
	protected final boolean hard32;
	
	/** Hardware 64-bit support? */
	protected final boolean hard64;
	
	/**
	 * Initializes the floating point name.
	 *
	 * @param __n The identifying name.
	 * @param __h32 Is 32-bit floating point in hardware?
	 * @param __h64 Is 64-bit floating point in hardware?
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/29
	 */
	private JITCPUFloat(String __n, boolean __h32, boolean __h64)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
		this.hard32 = __h32;
		this.hard64 = __h64;
	}
	
	/**
	 * Is any hardware floating point being used?
	 *
	 * @return {@code true} if any floating point hardware is used.
	 * @since 2016/09/01
	 */
	public boolean isAnyHardware()
	{
		return isHardware32() || isHardware64();
	}
	
	/**
	 * Returns {@code true} if 32-bit floating point is implement in hardware.
	 *
	 * @return {@code true} if 32-bit floating point is implement in hardware.
	 * @since 2016/08/29
	 */
	public boolean isHardware32()
	{
		return this.hard32;
	}
	
	/**
	 * Returns {@code true} if 64-bit floating point is implement in hardware.
	 *
	 * @return {@code true} if 64-bit floating point is implement in hardware.
	 * @since 2016/08/29
	 */
	public boolean isHardware64()
	{
		return this.hard64;
	}
	
	/**
	 * Returns {@code true} if 32-bit floating point is implement in software.
	 *
	 * @return {@code true} if 32-bit floating point is implement in software.
	 * @since 2016/08/29
	 */
	public boolean isSoftware32()
	{
		return !this.hard32;
	}
	
	/**
	 * Returns {@code true} if 64-bit floating point is implement in software.
	 *
	 * @return {@code true} if 64-bit floating point is implement in software.
	 * @since 2016/08/29
	 */
	public boolean isSoftware64()
	{
		return !this.hard64;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/29
	 */
	@Override
	public final String toString()
	{
		return this.name;
	}
	
	/**
	 * Returns the floating point type which is associated with the given
	 * string.
	 *
	 * @param __s The string to get the floating point type for.
	 * @return The floating point type associated with the given string.
	 * @throws IllegalArgumentException If the input string is not known.
	 * @since 2016/08/29
	 */
	public static JITCPUFloat of(String __s)
		throws IllegalArgumentException
	{
		switch (__s)
		{
				// Software
			case "soft":
				return JITCPUFloat.SOFT;
			
				// Hardware 32-bit, Software 64-bit
			case "hard32soft64":
				return JITCPUFloat.HARD32SOFT64;
				
				// Software 32-bit, Hardware 64-bit
			case "soft32hard64":
				return JITCPUFloat.SOFT32HARD64;
				
				// Hardware
			case "hard":
				return JITCPUFloat.HARD;
				
				// {@squirreljme.error BQ0h Unknown floating point type.
				// (The floating point type string)}
			default:
				throw new IllegalArgumentException(String.format("BQ0h %s",
					__s));
		}
	}
}

