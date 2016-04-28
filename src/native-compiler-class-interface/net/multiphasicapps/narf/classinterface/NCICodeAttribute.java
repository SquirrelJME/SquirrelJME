// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

/**
 * This represents a code attribute.
 *
 * @since 2016/04/27
 */
public final class NCICodeAttribute
{
	/** The attribute data buffer. */
	protected final NCIByteBuffer abuffer;
	
	/** The owning method. */
	protected final NCIMethod method;

	/**
	 * Initializes the code attribute with the given attribute data.
	 *
	 * @param __m The method which owns this code attribute.
	 * @param __d The code attribute data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public NCICodeAttribute(NCIMethod __m, byte... __d)
		throws NullPointerException
	{
		this(__m, __d, 0, __d.length);
	}
	
	/**
	 * Initializes the code attribute with the given attribute data.
	 *
	 * @param __m The method which owns this code attribute.
	 * @param __d The code attribute data.
	 * @param __o The starting offset to read bytes from.
	 * @param __l The length of the area to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public NCICodeAttribute(NCIMethod __m, byte[] __d, int __o, int __l)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		method = __m;
		abuffer = new NCIByteBuffer(__d, __o, __l);
	}
	
	/**
	 * Returns the maximum number of local variables used.
	 *
	 * @return The maximum number of local varaibles.
	 * @since 2016/04/27
	 */
	public int maxLocals()
	{
		return abuffer.readUnsignedShort(2);
	}
	
	/**
	 * Returns the maximum number of stack variables used.
	 *
	 * @return The maximum number of stack variables.
	 * @since 2016/04/27
	 */
	public int maxStack()
	{
		return abuffer.readUnsignedShort(0);
	}
}

