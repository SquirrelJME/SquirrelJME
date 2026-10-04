// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import java.util.Objects;

/**
 * Represents a location within the debugger.
 *
 * @since 2021/04/17
 */
public final class JDWPHostLocation
{
	/** Blank location. */
	public static final JDWPHostLocation BLANK = 
		new JDWPHostLocation(null, -1, -1,
			null, null);
	
	/** The location within the method. */
	protected final long codeDx;
	
	/** The method index from the type. */
	public final int methodDx;
	
	/** The type where this is located. */
	public final Object type;
	
	/** The name of the method. */
	protected final String name;
	
	/** The descriptor of the method. */
	protected final String descriptor;
	
	/**
	 * Initializes the location.
	 * 
	 * @param __type The type used.
	 * @param __methodDx The method index.
	 * @param __codeDx The code index.
	 * @param __name The name of the method.
	 * @param __descriptor The type of the method.
	 * @since 2021/04/17
	 */
	public JDWPHostLocation(Object __type, int __methodDx, long __codeDx,
		String __name, String __descriptor)
	{
		this.type = __type;
		this.methodDx = __methodDx;
		this.codeDx = __codeDx;
		
		// For debugging
		this.name = Objects.toString(__name, "#" + __methodDx);
		this.descriptor = Objects.toString(__descriptor, "<?>");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof JDWPHostLocation))
			return false;
		
		JDWPHostLocation o = (JDWPHostLocation)__o;
		return Objects.equals(this.type, o.type) &&
			this.methodDx == o.methodDx &&
			this.codeDx == o.codeDx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public int hashCode()
	{
		long codeDx = this.codeDx;
		return System.identityHashCode(this.type) ^
			~this.methodDx ^ ((int)codeDx | (int)(codeDx >>> 32));
	}
	
	/**
	 * Checks if this meets the given location.
	 * 
	 * @param __type The class this is at.
	 * @param __methodDx The method index.
	 * @param __codeDx The code index.
	 * @return If this meets the given location.
	 * @since 2021/04/25
	 */
	public boolean meets(Object __type, int __methodDx, int __codeDx)
	{
		return this.type == __type &&
			this.methodDx == __methodDx &&
			this.codeDx == (long)__codeDx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/17
	 */
	@Override
	public String toString()
	{
		return String.format("Location[%s.%s%s @%d",
			this.type, this.name, this.descriptor, this.codeDx);
	}
	
	/**
	 * Returns a new location with the given code index.
	 * 
	 * @param __newCodeDx The new code index.
	 * @return The new location.
	 * @since 2022/08/28
	 */
	public JDWPHostLocation withCodeIndex(int __newCodeDx)
	{
		return new JDWPHostLocation(this.type, this.methodDx, __newCodeDx,
			this.name, this.descriptor);
	}
}
