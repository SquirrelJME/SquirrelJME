// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents a single exception handler.
 *
 * @since 2017/02/09
 */
public final class ExceptionHandler
{
	/** The start address. */
	protected final int startpc;
	
	/** The end address. */
	protected final int endpc;
	
	/** The handler address. */
	protected final JumpTarget handlerpc;
	
	/** The class type to handle. */
	protected final ClassName type;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the exception handler.
	 *
	 * @param __spc The start address.
	 * @param __epc The end address.
	 * @param __hpc The handler address.
	 * @param __cn The class to be handled.
	 * @throws JITException If the addresses are not valid.
	 * @since 2017/02/09
	 */
	ExceptionHandler(int __spc, int __epc, int __hpc, ClassName __cn)
		throws JITException
	{
		// {@squirreljme.error JI1f An address is negative. (The start address;
		// The end address; The handler address)}
		if (__spc < 0 || __epc < 0 || __hpc < 0)
			throw new JITException(String.format("JI1f %d %d %d",
				__spc, __epc, __hpc));
		
		// {@squirreljme.error JI1g The end address is at or before the start
		// address. (The start address; The end address)}
		if (__epc <= __spc)
			throw new JITException(String.format("JI1g %d %d",
				__spc, __epc));
		
		// Set
		this.startpc = __spc;
		this.endpc = __epc;
		this.handlerpc = new JumpTarget(__hpc);
		this.type = (__cn == null ? new ClassName("java/lang/Throwable") :
			__cn);
	}
	
	/**
	 * Returns the end address.
	 *
	 * @return The end address.
	 * @since 2017/02/09
	 */
	public int endAddress()
	{
		return this.endpc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/09
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ExceptionHandler))
			return false;
		
		// Compare
		ExceptionHandler o = (ExceptionHandler)__o;
		return this.startpc == o.startpc &&
			this.endpc == o.endpc &&
			this.handlerpc.equals(o.handlerpc) &&
			Objects.equals(this.type, o.type);
	}
	
	/**
	 * Returns the handler address.
	 *
	 * @return The handler address.
	 * @since 2017/02/09
	 */
	public JumpTarget handlerAddress()
	{
		return this.handlerpc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/09
	 */
	@Override
	public int hashCode()
	{
		return this.startpc ^ this.endpc ^ this.handlerpc.hashCode() ^
			Objects.hashCode(this.type);
	}
	
	/**
	 * Checks whether the address is in range of this exception handler, that
	 * it there is a handler for this instruction.
	 *
	 * @param __i The address to check.
	 * @return {@code true} if the address is in range.
	 * @since 2017/04/01
	 */
	public boolean inRange(int __i)
	{
		return __i >= this.startpc && __i < this.endpc;
	}
	
	/**
	 * Returns the start address.
	 *
	 * @return The start address.
	 * @since 2017/02/09
	 */
	public int startAddress()
	{
		return this.startpc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/09
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"{%d-%d > %s (%s)}", this.startpc, this.endpc, this.handlerpc,
				this.type)));
		
		return rv;
	}
	
	/**
	 * Returns the type of exception to handle.
	 *
	 * @return The type of exception to handle.
	 * @since 2017/02/09
	 */
	public ClassName type()
	{
		return this.type;
	}
}

