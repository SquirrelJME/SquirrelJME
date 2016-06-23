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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.java.symbols.BinaryNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This describes a single exception that is handled by the byte code.
 *
 * @since 2016/05/08
 */
public final class CICodeException
{
	/** The starting PC address. */
	protected final int startpc;
	
	/** The ending PC address. */
	protected final int endpc;
	
	/** The handler PC address. */
	protected final int handlerpc;
	
	/** The class type to handle. */
	protected final ClassNameSymbol handletype;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the code exception.
	 *
	 * @param __spc The inclusive starting address.
	 * @param __epc The exclusive ending address.
	 * @param __hpc The handler address.
	 * @param __t The type of exception to handle, if {@code null} then this
	 * means {@link Throwable}.
	 * @throws CIException If the addresses are negative or the end is at or
	 * before the start.
	 * @since 2016/05/08
	 */
	CICodeException(int __spc, int __epc, int __hpc, BinaryNameSymbol __t)
		throws CIException
	{
		// {@squirreljme.error AO01 The input addresses are either negative or
		// the end is at or before the start. (The start address; The end
		// address; The handler address)}
		if (__spc < 0 || __epc < 0 || __hpc < 0 || __epc <= __spc)
			throw new CIException(String.format("AO01 %d %d %d",
				__spc, __epc, __hpc));
		
		// Set
		startpc = __spc;
		endpc = __epc;
		handlerpc = __hpc;
		handletype = (__t == null ? BinaryNameSymbol.THROWABLE : __t).
			asClassName();
	}
	
	/**
	 * Returns the exclusive end address.
	 *
	 * @return The exclusive end address.
	 * @since 2016/05/08
	 */
	public int endAddress()
	{
		return endpc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/08
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must be self
		if (!(__o instanceof CICodeException))
			return false;
		
		// Cast and check
		CICodeException o = (CICodeException)__o;
		return startpc == o.startpc &&
			endpc == o.endpc &&
			handlerpc == o.handlerpc &&
			handletype.equals(o.handletype);
	}
	
	/**
	 * Returns the address of the exception handler.
	 *
	 * @return The exception handler address.
	 * @since 2016/05/08
	 */
	public int handlerAddress()
	{
		return handlerpc;
	}
	
	/**
	 * Returns the type of exception this handles.
	 *
	 * @return The type of exception to handle.
	 * @since 2016/05/08
	 */
	public ClassNameSymbol handlesType()
	{
		return handletype;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/08
	 */
	@Override
	public int hashCode()
	{
		return startpc ^ endpc ^ handlerpc ^ handletype.hashCode();
	}
	
	/**
	 * Returns the inclusive start address.
	 *
	 * @return The inclusive start address.
	 * @since 2016/05/08
	 */
	public int startAddress()
	{
		return startpc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/08
	 */
	@Override
	public String toString()
	{
		// Get reference
		Reference<String> ref = _string;
		String rv;
		
		// In reference?
		if (ref == null || null == (rv = ref.get()))
			_string = new WeakReference<>((rv = "[" + startpc + ", " +
				endpc + ")->" + handlerpc + "?" + handletype));
		
		// Return it
		return rv;
	}
}

