// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel;

/**
 * This class must be implemented and is used to implement the primitive
 * part of the kernel which would exist mostly in system dependent and
 * native code accordingly.
 *
 * @since 2018/03/03
 */
public interface PrimitiveKernel
{
	/**
	 * Creates a primitive process.
	 *
	 * @return The newly created process.
	 * @since 2018/03/03
	 */
	public abstract PrimitiveProcess createProcess();
	
	/**
	 * Creates a primitive thread.
	 *
	 * @return The newly created thread.
	 * @since 2018/03/03
	 */
	public abstract PrimitiveThread createThread();
	
	/**
	 * Returns the mapping of services available in the kernel, the returned
	 * value are in key/value pairs.
	 *
	 * @return The service map for values.
	 * @since 2018/03/15
	 */
	public abstract String[] serviceMap();
}

