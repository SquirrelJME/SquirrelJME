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
	/** Maximum code length. */
	public static final int MAXIMUM_CODE_LENGTH =
		65535;

	/** The attribute data buffer. */
	protected final NCIByteBuffer abuffer;
	
	/** The code buffer. */
	protected final NCIByteBuffer cbuffer;
	
	/** The owning method. */
	protected final NCIMethod method;
	
	/** Maximum locals. */
	protected final int maxlocals;
	
	/** Maximum stack. */
	protected final int maxstack;
	
	/** The length of code. */
	protected final int codelen;
	
	/** The number of exception handlers. */
	protected final int numhandlers;
	
	/** Exception handler cache. */
	protected final NCICodeExceptions exceptions;

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
		
		// Read in values
		maxlocals = abuffer.readUnsignedShort(2);
		maxstack = abuffer.readUnsignedShort(0);
		
		// {@squirreljme.error AO0y The methode code length exceeds the maximum
		// length limit. (The containing method; The code length)}
		codelen = abuffer.readInt(4);
		if (codelen < 0 || codelen > MAXIMUM_CODE_LENGTH)
			throw new NCIException(NCIException.Issue.LARGE_CODE,
				String.format("AO0y %s %d", method.nameAndType(), codelen));
		
		// Setup code buffer
		cbuffer = abuffer.window(8, codelen);
		
		// Exception handlers
		numhandlers = abuffer.readUnsignedShort(8 + codelen);
		
		// Setup the excpetion list
		exceptions = new NCICodeExceptions(method.outerClass().constantPool(),
			numhandlers, abuffer.window(10 + codelen, numhandlers * 8));
	}
	
	/**
	 * Returns the buffer which provides access to the raw byte code of a
	 * method.
	 *
	 * @return The view of the method byte code.
	 * @since 2016/04/28
	 */
	public NCIByteBuffer code()
	{
		return cbuffer;
	}
	
	/**
	 * Returns the list of exception handlers in this code block
	 *
	 * @return The exception handler list.
	 * @since 2016/05/08
	 */
	public NCICodeExceptions exceptionHandlers()
	{
		return exceptions;
	}
	
	/**
	 * Returns the maximum number of local variables used.
	 *
	 * @return The maximum number of local varaibles.
	 * @since 2016/04/27
	 */
	public int maxLocals()
	{
		return maxlocals;
	}
	
	/**
	 * Returns the maximum number of stack variables used.
	 *
	 * @return The maximum number of stack variables.
	 * @since 2016/04/27
	 */
	public int maxStack()
	{
		return maxstack;
	}
	
	/**
	 * Returns the method which contains this attribute.
	 *
	 * @return The containing method.
	 * @since 2016/04/08
	 */
	public NCIMethod method()
	{
		return method;
	}
}

