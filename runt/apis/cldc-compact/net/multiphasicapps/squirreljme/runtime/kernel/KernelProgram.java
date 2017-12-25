// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

/**
 * This represents a single program which exists within the kernel and maps
 * with suites within the Java ME environment. Each program is identified by
 * an identifier which represents the program index. The index remains constant
 * for the same program (unless that program has been changed). The index is
 * used to refer to the program slot. Since programs in the same slot may be
 * changed, there is a separate change mask which indicates the change number
 * of a program.
 *
 * @since 2017/12/11
 */
public abstract class KernelProgram
{
	/** This is the mask used to represent the slot position. */
	public static final int SLOT_MASK =
		0b0000_0000__0000_0000___0000_1111__1111_1111;
	
	/** This represents the number of times the program has changed. */
	public static final int CHANGE_MASK =
		0b1111_1111__1111_1111___1111_0000__0000_0000;
	
	/** This represents the shift used to read and write the change mask. */
	public static final int CHANGE_SHIFT =
		12;
}

