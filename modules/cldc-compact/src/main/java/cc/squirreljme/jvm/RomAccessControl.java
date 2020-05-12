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
 * Controls that are used to access the ROM and Soft ROM (if available).
 *
 * This is used by {@link SystemCallIndex#ROM_ACCESS}.
 *
 * It is not defined what a ROM reference identifier will be, except that
 * {@code 0} indicates no ROM value. The values must conform to a 64-bit
 * integer, since it is possible that for simplicity a memory address could
 * be used.
 *
 * @since 2020/05/10
 */
public interface RomAccessControl
{
	/**
	 * Search for a JAR ROM with a modified UTF string.
	 *
	 * @squirreljme.syscallparam 1 Pointer (high bytes) to a modified UTF
	 * string.
	 * @squirreljme.syscallparam 2 Pointer (low bytes).
	 * @squirreljme.syscallreturn A 64-bit ROM Reference, or {@code 0} if not
	 * found.
	 * @since 2020/05/10
	 */
	byte CONTROL_SEARCH_BY_JAR_MUTF =
		1;
	
	/**
	 * Search for a JAR ROM with a UTF-8 encoded byte array.
	 *
	 * @squirreljme.syscallparam 1 Pointer (high bytes) to a UTF-8 encoded
	 * byte array.
	 * @squirreljme.syscallparam 2 Pointer (low bytes).
	 * @squirreljme.syscallreturn A 64-bit ROM Reference, or {@code 0} if not
	 * found.
	 * @since 2020/05/10
	 */
	byte CONTROL_SEARCH_BY_JAR_BYTES =
		2;
	
	/** The number of controls. */
	byte NUM_CONTROLS =
		4;
}
