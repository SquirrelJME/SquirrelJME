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
 * This represents a single native program and is used to access program
 * information.
 *
 * @since 2017/12/25
 */
public interface NativeProgram
{
	/**
	 * The program type property.
	 * The values are the same as {@link KernelProgramType}.
	 */
	public static final int PROPERTY_PROGRAM_TYPE =
		1;
	
	/**
	 * Returns the value of the specified property.
	 *
	 * @param __prop The property to get.
	 * @return The property value.
	 * @since 2017/12/25
	 */
	public abstract int getProperty(int __prop);
	
	/**
	 * Returns the index of the program.
	 *
	 * @return The program index.
	 * @since 2017/12/25
	 */
	public abstract int index();
	
	/**
	 * Sets the value of the specified property.
	 *
	 * @param __prop The property to set.
	 * @param __v The new value to set.
	 * @return The old value.
	 * @throws IllegalStateException If the property could not be set, such
	 * as if the program is in read-only storage.
	 * @since 2017/12/25
	 */
	public abstract int setProperty(int __prop, int __v)
		throws IllegalStateException;
}

