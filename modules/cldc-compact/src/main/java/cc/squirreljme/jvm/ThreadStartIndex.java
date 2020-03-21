// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This represents a field within the thread starting area which determines
 * parameters and such in a fixed representation.
 *
 * @since 2020/03/21
 */
public interface ThreadStartIndex
{
	/**
	 * Place holder for nothing.
	 *
	 * @since 2002/03/21
	 */
	byte NOTHING =
		0;
	
	/**
	 * The main class to use for the thread entry point.
	 *
	 * @squirreljme.tsiparam 0 The {@link ClassInfo} pointer containing a
	 * reference to the {@code main(String[])} method.
	 * @since 2020/03/21
	 */
	byte MAIN_CLASS_INFO =
		1;
	
	/**
	 * The main arguments to use for the method.
	 *
	 * @squirreljme.tsiparam 0 A {@link String[]} pointer containing the
	 * main arguments to the main method.
	 * @since 2020/03/21
	 */
	byte MAIN_ARGUMENTS =
		2;
	
	/** The number of indexes. */
	byte NUM_INDEXES =
		3;
}
