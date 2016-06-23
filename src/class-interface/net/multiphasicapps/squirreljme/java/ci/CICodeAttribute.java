// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

/**
 * This represents a code attribute.
 *
 * @since 2016/04/27
 */
public final class CICodeAttribute
	implements CIExecutable
{
	/** Maximum code length. */
	public static final int MAXIMUM_CODE_LENGTH =
		65535;

	/** The attribute data buffer. */
	protected final CIByteBuffer abuffer;
	
	/** The code buffer. */
	protected final CIByteBuffer cbuffer;
	
	/** The owning method. */
	protected final CIMethod method;
	
	/** Maximum locals. */
	protected final int maxlocals;
	
	/** Maximum stack. */
	protected final int maxstack;
	
	/** The length of code. */
	protected final int codelen;
	
	/** The number of exception handlers. */
	protected final int numhandlers;
	
	/** Exception handler cache. */
	protected final CICodeExceptions exceptions;
	
	/** The old stack map table. */
	protected final CIByteBuffer oldstackmap;
	
	/** The new stack map table. */
	protected final CIByteBuffer newstackmap;

	/**
	 * Initializes the code attribute with the given attribute data.
	 *
	 * @param __m The method which owns this code attribute.
	 * @param __d The code attribute data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public CICodeAttribute(CIMethod __m, byte... __d)
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
	public CICodeAttribute(CIMethod __m, byte[] __d, int __o, int __l)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.method = __m;
		CIByteBuffer abuffer = new CIByteBuffer(__d, __o, __l);
		this.abuffer = abuffer;
		
		// Read in values
		this.maxstack = abuffer.readUnsignedShort(0);
		this.maxlocals = abuffer.readUnsignedShort(2);
		
		// {@squirreljme.error AO0y The methode code length exceeds the maximum
		// length limit. (The containing method; The code length)}
		int codelen = abuffer.readInt(4);
		this.codelen = codelen;
		if (codelen < 0 || codelen > MAXIMUM_CODE_LENGTH)
			throw new CIException(String.format("AO0y %s %d",
				method.nameAndType(), codelen));
		
		// Setup code buffer
		cbuffer = abuffer.window(8, codelen);
		
		// Exception handlers
		numhandlers = abuffer.readUnsignedShort(8 + codelen);
		
		// Setup the excpetion list
		int xbase = 10 + codelen;
		int xsize = numhandlers * 8;
		exceptions = new CICodeExceptions(__m.outerClass().constantPool(),
			numhandlers, abuffer.window(xbase, xsize));
		
		// Go through the attributes to find the stack mappings
		int bp = xbase + xsize;
		int na = abuffer.readUnsignedShort(bp);
		CIPool pool = __m.outerClass().constantPool();
		CIByteBuffer sold = null, snew = null;
		for (int i = 0, pp = bp + 2; i < na; i++)
		{
			// Read associated string and length
			String attr = pool.<CIUTF>requiredAs(abuffer.
				readUnsignedShort(pp), CIUTF.class).toString();
			int len = abuffer.readInt(pp + 2);
			
			// {@squirreljme.error AO02 An attribute in the code attribute
			// has a size larger than 2GiB. (The length)}
			if (len < 0)
				throw new CIException(String.format("AO02 %d", len));
			
			// New stack map?
			if (attr.equals("StackMapTable"))
			{
				// {@squirreljme.error AO07 The StackMapTable attribute
				// appears multiple times, there must only be a single
				// attribute. (The attribute name)}
				if (snew != null)
					throw new CIException(String.format("AO07 %s", attr));
				
				// Create window
				snew = abuffer.window(pp + 6, len);
			}
			
			// Old stack map
			else if (attr.equals("StackMap"))
			{
				// {@squirreljme.error AO08 The StackMap attribute
				// appears multiple times, there must only be a single
				// attribute. (The attribute name)}
				if (sold != null)
					throw new CIException(String.format("AO08 %s", attr));
				
				// Create window
				sold = abuffer.window(pp + 6, len);
			}
			
			// Skip ahead to the next attribute
			pp += 6 + len;
		}
		
		// Store
		newstackmap = snew;
		oldstackmap = sold;
	}
	
	/**
	 * Returns the buffer which provides access to the raw byte code of a
	 * method.
	 *
	 * @return The view of the method byte code.
	 * @since 2016/04/28
	 */
	public CIByteBuffer code()
	{
		return this.cbuffer;
	}
	
	/**
	 * Returns the list of exception handlers in this code block
	 *
	 * @return The exception handler list.
	 * @since 2016/05/08
	 */
	public CICodeExceptions exceptionHandlers()
	{
		return this.exceptions;
	}
	
	/**
	 * Returns the maximum number of local variables used.
	 *
	 * @return The maximum number of local varaibles.
	 * @since 2016/04/27
	 */
	public int maxLocals()
	{
		return this.maxlocals;
	}
	
	/**
	 * Returns the maximum number of stack variables used.
	 *
	 * @return The maximum number of stack variables.
	 * @since 2016/04/27
	 */
	public int maxStack()
	{
		return this.maxstack;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/08
	 */
	@Override
	public CIMethod method()
	{
		return this.method;
	}
	
	/**
	 * Returns the old {@code StackMap} attribute that this code may
	 * optionally have.
	 *
	 * @return The old stack map data or {@code null} if it does not exist.
	 * @since 2016/05/12
	 */
	public CIByteBuffer stackMapOld()
	{
		if (!this.method.outerClass().version().useStackMapTable())
			return this.oldstackmap;
		return null;
	}
	
	/**
	 * Returns the new {@code StackMapTable} attribute that this code may
	 * optionally have.
	 *
	 * @return The new stack map data or {@code null} if it does not exist.
	 * @since 2016/05/12
	 */
	public CIByteBuffer stackMapNew()
	{
		if (this.method.outerClass().version().useStackMapTable())
			return this.newstackmap;
		return null;
	}
}

