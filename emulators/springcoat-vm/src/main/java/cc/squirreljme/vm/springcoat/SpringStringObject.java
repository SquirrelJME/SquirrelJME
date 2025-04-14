// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.util.Objects;

/**
 * Represents a wrapped string.
 *
 * @since 2025/01/22
 */
public class SpringStringObject
	extends SpringSimpleObject
{
	/** The string used. */
	private volatile String _string;
	
	/**
	 * Initializes the string.
	 *
	 * @param __cl The class of the string.
	 * @param __s The initial String value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/01/22
	 */
	public SpringStringObject(SpringClass __cl, String __s)
		throws NullPointerException
	{
		super(__cl);
		
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Must be the string class
		if (!"java/lang/String".equals(__cl.name().toString()))
			throw new SpringMLECallError("Invalid String Init");
		
		// Set string
		this._string = __s;
	}
	
	/**
	 * Optionally sets the string value.
	 *
	 * @param __value The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/01/23
	 */
	public void optional(String __value)
		throws NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			if (this._string == null)
				this._string = __value;
		}
	}
	
	/**
	 * Sets the string, fails if already set.
	 *
	 * @param __value The value to set.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringMLECallError If a string was already set.
	 * @since 2025/01/23
	 */
	public void set(String __value)
		throws NullPointerException, SpringMLECallError
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			if (this._string != null)
				throw new SpringMLECallError("String already set.");
			
			this._string = __value;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/01/22
	 */
	@Override
	public String toString()
	{
		synchronized (this)
		{
			String result = this._string;
			return (result == null ? "" : result);
		}
	}
}
