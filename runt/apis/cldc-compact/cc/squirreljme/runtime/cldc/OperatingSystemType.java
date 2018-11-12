// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

import cc.squirreljme.runtime.cldc.asm.SystemAccess;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents the type of operating system SquirrelJME is running on.
 *
 * This enumeration is intended to make OS detection standard.
 *
 * @since 2018/01/13
 */
public enum OperatingSystemType
{
	/** Unknown. */
	UNKNOWN,
	
	/** Amiga. */
	AMIGA,
	
	/** Linux. */
	LINUX,
	
	/** Class Mac OS. */
	MAC_OS_CLASSIC,
	
	/** Mac OS X. */
	MAC_OS_X,
	
	/** MS-DOS and compatibles. */
	MS_DOS,
	
	/** Palm OS. */
	PALM_OS,
	
	/** Solaris. */
	SOLARIS,
	
	/** Windows 16-bit. */
	WINDOWS_WIN16,
	
	/** Windows CE. */
	WINDOWS_CE,
	
	/** Windows 9x. */
	WINDOWS_9X,
	
	/** Windows NT. */
	WINDOWS_NT,
	
	/** End. */
	;
	
	/** Lowercase form of the type . */
	private Reference<String> _lower;
	
	/**
	 * Is this a DOS system?
	 *
	 * @return {@code true} if it is a DOS system.
	 * @since 2018/01/13
	 */
	public final boolean isDOS()
	{
		switch (this)
		{
			case MS_DOS:
				return true;
			
			default:
				return false;
		}
	}
	
	/**
	 * Is this a modern Windows system?
	 *
	 * @return {@code true} if it is a modern Windows system.
	 * @since 2018/01/13
	 */
	public final boolean isModernWindows()
	{
		switch (this)
		{
			case WINDOWS_9X:
			case WINDOWS_NT:
				return true;
			
			default:
				return false;
		}
	}
	
	/**
	 * Is this a UNIX system?
	 *
	 * @return {@code true} if it is a UNIX system.
	 * @since 2018/01/13
	 */
	public final boolean isUnix()
	{
		switch (this)
		{
			case LINUX:
			case MAC_OS_X:
			case SOLARIS:
				return true;
			
			default:
				return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/13
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._lower;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._lower = new WeakReference<>(
				rv = this.name().toLowerCase().replace('_', '-'));
		
		return rv;
	}
	
	/**
	 * Returns the type of operating system that this is.
	 *
	 * @return The type of operating system this is.
	 * @since 2018/10/14
	 */
	public static OperatingSystemType systemType()
	{
		return OperatingSystemType.values()[
			SystemAccess.operatingSystemType()];
	}
}

