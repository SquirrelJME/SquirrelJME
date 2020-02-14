// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

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
public interface OperatingSystemType
{
	/** Unknown. */
	public static final int UNKNOWN =
		0;
	
	/** Amiga. */
	public static final int AMIGA =
		1;
	
	/** Linux. */
	public static final int LINUX =
		2;
	
	/** Class Mac OS. */
	public static final int MAC_OS_CLASSIC =
		3;
	
	/** Mac OS X. */
	public static final int MAC_OS_X =
		4;
	
	/** MS-DOS and compatibles. */
	public static final int MS_DOS =
		5;
	
	/** Palm OS. */
	public static final int PALM_OS =
		6;
		
	/** Solaris. */
	public static final int SOLARIS =
		7;
	
	/** Windows 16-bit. */
	public static final int WINDOWS_WIN16 =
		8;
	
	/** Windows CE. */
	public static final int WINDOWS_CE =
		9;
	
	/** Windows 9x. */
	public static final int WINDOWS_9X =
		10;
	
	/** Windows NT. */
	public static final int WINDOWS_NT =
		11;
}

