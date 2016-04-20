// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.personality;

/**
 * This interface provides details on how a CPU is so that the SSA compiler
 * can generate code for the given CPU using optimal means.
 *
 * @since 2016/04/20
 */
public interface NPPersonality
{
	/**
	 * Returns the native integer register size of the given CPU in bits.
	 *
	 * @return The CPU's native register size.
	 * @since 2016/04/20
	 */
	public abstract int cpuBits();
	
	/**
	 * Returns the native floating point register size if it is compatible
	 * with the Java virtual machine floating point format.
	 *
	 * If the return value is {@code 0} then software floating point will be
	 * used. If the value is less than {@code 64} then software floating point
	 * will be used for double floating point values.
	 *
	 * @return The supported number of floating point bits.
	 * @since 2016/04/20
	 */
	public abstract int floatBits();
}

