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
 * This represent the set of flags for fields.
 *
 * @since 2016/04/23
 */
public final class CIFieldFlags
	extends CIMemberFlags<CIFieldFlag>
	implements CIAccessibleFlags
{
	/**
	 * Initializes the field flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The field flags.
	 * @since 2016/04/23
	 */
	public CIFieldFlags(CIClass __oc, CIFieldFlag... __fl)
	{
		super(CIFieldFlag.class, __fl);
		
		__checkFlags(__oc);
	}
	
	/**
	 * Initializes the field flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The field flags.
	 * @since 2016/04/23
	 */
	public CIFieldFlags(CIClass __oc, Iterable<CIFieldFlag> __fl)
	{
		super(CIFieldFlag.class, __fl);
		
		__checkFlags(__oc);
	}
	
	/**
	 * Returns {@code true} if this is an enumeration.
	 *
	 * @return {@code true} if an enumeration.
	 * @since 2016/03/20
	 */
	public boolean isEnum()
	{
		return contains(CIFieldFlag.ENUM);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isFinal()
	{
		return contains(CIFieldFlag.FINAL);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPrivate()
	{
		return contains(CIFieldFlag.PRIVATE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isProtected()
	{
		return contains(CIFieldFlag.PROTECTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPublic()
	{
		return contains(CIFieldFlag.PUBLIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isStatic()
	{
		return contains(CIFieldFlag.STATIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isSynthetic()
	{
		return contains(CIFieldFlag.SYNTHETIC);
	}
	
	/**
	 * Returns {@code true} if this is transient.
	 *
	 * @return {@code true} if transient.
	 * @since 2016/03/20
	 */
	public boolean isTransient()
	{
		return contains(CIFieldFlag.TRANSIENT);
	}
	
	/**
	 * Returns {@code true} if this is volatile.
	 *
	 * @return {@code true} if volatile.
	 * @since 2016/03/20
	 */
	public boolean isVolatile()
	{
		return contains(CIFieldFlag.VOLATILE);
	}
	
	/**
	 * Checks that the given flags are valid.
	 *
	 * @param __oc The outer class.
	 * @throws CIException If they are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	private final void __checkFlags(CIClass __oc)
		throws CIException, NullPointerException
	{
		// Check
		if (__oc == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AO0r A field cannot be both {@code final} and
		// {@code volatile}. (The field flags)}
		if (isFinal() && isVolatile())
			throw new CIException(String.format("AO0r %s", this));
		
		// If the class is an interface, some flags cannot be set
		if (__oc.flags().isInterface())
			for (CIFieldFlag f : CIFieldFlag.values())
			{
				// Must have these
				boolean must = (f == CIFieldFlag.PUBLIC ||
					f == CIFieldFlag.STATIC || f == CIFieldFlag.FINAL);
				
				// Could have these
				boolean maybe = (f == CIFieldFlag.SYNTHETIC);
				
				// Is it set?
				boolean has = contains(f);
				
				// {@squirreljme.error AO1t Flags for interface field has an
				// incorrect set of flags. (The field flags)}
				if (must != has && !maybe)
					throw new CIException(String.format("AO1t %s", this));
			}
	}
}

