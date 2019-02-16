// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a locatoin which points to a register or two registers if
 * a wide register.
 *
 * @since 2019/02/16
 */
public final class RegisterLocation
	implements Location
{
	/** The referenced register. */
	protected final int register;
	
	/** Is this a wide register (64-bit)? */
	protected final boolean iswide;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the location.
	 *
	 * @param __r The register to refer to.
	 * @since 2019/02/16
	 */
	public RegisterLocation(int __r)
	{
		this.register = __r;
		this.iswide = false;
	}
	
	/**
	 * Initializes the location which may be wide.
	 *
	 * @param __r The register to refer to.
	 * @param __w Is this a wide register?
	 * @since 2019/02/16
	 */
	public RegisterLocation(int __r, boolean __w)
	{
		this.register = __r;
		this.iswide = __w;
	}
	
	/**
	 * Returns this register as a narrow one.
	 *
	 * @return The narrow register.
	 * @since 2019/02/16
	 */
	public final RegisterLocation asNarrow()
	{
		return (this.iswide ? new RegisterLocation(this.register, false) :
			this);
	}
	
	/**
	 * Returns this register as a wide one.
	 *
	 * @return The wide register.
	 * @since 2019/02/16
	 */
	public final RegisterLocation asWide()
	{
		return (this.iswide ? this :
			new RegisterLocation(this.register, true));
	}
	
	/**
	 * Returns the wide state of this register according to the given flag.
	 *
	 * @param __w Return a wide register?
	 * @return The narrow or wide register.
	 * @since 2019/02/16
	 */
	public final RegisterLocation asWide(boolean __w)
	{
		return (__w ? this.asWide() : this.asNarrow());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/16
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof RegisterLocation))
			return false;
		
		RegisterLocation o = (RegisterLocation)__o;
		return this.register == o.register &&
			this.iswide == o.iswide;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/16
	 */
	@Override
	public final int hashCode()
	{
		return this.register ^ (this.iswide ? -1 : 0);
	}
	
	/**
	 * Returns the base register.
	 *
	 * @return The base register.
	 * @since 2019/02/16
	 */
	public final int register()
	{
		return this.register;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/16
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = (this.iswide ?
				"wr#" : "r#") + this.register));
		
		return rv;
	}
}

