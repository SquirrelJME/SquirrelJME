// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.debug;

import java.util.Objects;

/**
 * This represents a single entry within the call stack. This is used for
 * debugging purporses to determine where code has thrown an exception.
 *
 * @since 2018/02/21
 */
public final class CallTraceElement
{
	/** The class name. */
	protected final String classname;
	
	/** The method name. */
	protected final String methodname;
	
	/** The method descriptor. */
	protected final String methoddescriptor;
	
	/** The execution pointer of the address. */
	protected final long address;
	
	/**
	 * Initializes an empty call trace element.
	 *
	 * @since 2018/02/21
	 */
	public CallTraceElement()
	{
		this(null, null, null, -1);
	}
	
	/**
	 * Initializes a call trace element.
	 *
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @since 2018/02/21
	 */
	public CallTraceElement(String __cl, String __mn)
	{
		this(__cl, __mn, null, -1);
	}
	
	/**
	 * Initializes a call trace element.
	 *
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @param __md The method descriptor.
	 * @since 2018/02/21
	 */
	public CallTraceElement(String __cl, String __mn, String __md)
	{
		this(__cl, __mn, __md, -1);
	}
	
	/**
	 * Initializes a call trace element.
	 *
	 * @param __cl The class name.
	 * @param __mn The method name.
	 * @param __md The method descriptor.
	 * @param __addr The address the method executes at.
	 * @since 2018/02/21
	 */
	public CallTraceElement(String __cl, String __mn, String __md, long __addr)
	{
		this.classname = __cl;
		this.methodname = __mn;
		this.methoddescriptor = __md;
		this.address = __addr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/21
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof CallTraceElement))
			return false;
		
		CallTraceElement o = (CallTraceElement)__o;
		return Objects.equals(this.classname, o.classname) &&
			Objects.equals(this.methodname, o.methodname) &&
			Objects.equals(this.methoddescriptor, o.methoddescriptor) &&
			this.address == o.address;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/21
	 */
	@Override
	public int hashCode()
	{
		long address = this.address;
		return Objects.hashCode(this.classname) ^
			Objects.hashCode(this.methodname) ^
			Objects.hashCode(this.methoddescriptor) ^
			(int)((address >>> 32) | address);
	}
}

