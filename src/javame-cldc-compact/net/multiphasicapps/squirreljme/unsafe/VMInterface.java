// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * This contains the interface to the host virtual machine which handles
 * operating system specific details.
 *
 * Operating systems in general will implement this interface which would then
 * be used to provide the required native functionality.
 *
 * @since 2016/06/15
 */
public abstract class VMInterface
{
	public static final VMInterface INSTANCE =
		__getInstance();
	
	/**
	 * Writes a byte to standard error.
	 *
	 * @param __b The byte to write.
	 * @since 2016/06/16
	 */
	public void stdErr(byte __b)
	{
		// The default implementation drops all characters output to the native
		// console since it might not be supported
	}
	
	/**
	 * Writes a byte to standard output.
	 *
	 * @param __b The byte to write.
	 * @since 2016/06/16
	 */
	public void stdOut(byte __b)
	{
		// The default implementation drops all characters output to the native
		// console since it might not be supported
	}
	
	/**
	 * The OS interface is magically pre-initialized to a given value, so to
	 * make it Java compilation friendly the value of the field is returned.
	 *
	 * @return {@link #INSTANCE}.
	 * @since 2016/06/15
	 */
	private static VMInterface __getInstance()
	{
		return INSTANCE;
	}
}

