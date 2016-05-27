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
 * This represent the set of flags for fields.
 *
 * @since 2016/04/23
 */
public final class NCIFieldFlags
	extends NCIMemberFlags<NCIFieldFlag>
	implements NCIAccessibleFlags
{
	/**
	 * Initializes the field flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The field flags.
	 * @since 2016/04/23
	 */
	public NCIFieldFlags(NCIClass __oc, NCIFieldFlag... __fl)
	{
		super(NCIFieldFlag.class, __fl);
		
		__checkFlags(__oc);
	}
	
	/**
	 * Initializes the field flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The field flags.
	 * @since 2016/04/23
	 */
	public NCIFieldFlags(NCIClass __oc, Iterable<NCIFieldFlag> __fl)
	{
		super(NCIFieldFlag.class, __fl);
		
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
		return contains(NCIFieldFlag.ENUM);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isFinal()
	{
		return contains(NCIFieldFlag.FINAL);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPrivate()
	{
		return contains(NCIFieldFlag.PRIVATE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isProtected()
	{
		return contains(NCIFieldFlag.PROTECTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPublic()
	{
		return contains(NCIFieldFlag.PUBLIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isStatic()
	{
		return contains(NCIFieldFlag.STATIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isSynthetic()
	{
		return contains(NCIFieldFlag.SYNTHETIC);
	}
	
	/**
	 * Returns {@code true} if this is transient.
	 *
	 * @return {@code true} if transient.
	 * @since 2016/03/20
	 */
	public boolean isTransient()
	{
		return contains(NCIFieldFlag.TRANSIENT);
	}
	
	/**
	 * Returns {@code true} if this is volatile.
	 *
	 * @return {@code true} if volatile.
	 * @since 2016/03/20
	 */
	public boolean isVolatile()
	{
		return contains(NCIFieldFlag.VOLATILE);
	}
	
	/**
	 * Checks that the given flags are valid.
	 *
	 * @param __oc The outer class.
	 * @throws NCIException If they are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	private final void __checkFlags(NCIClass __oc)
		throws NCIException, NullPointerException
	{
		// Check
		if (__oc == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AO0r A field cannot be both {@code final} and
		// {@code volatile}. (The field flags)}
		if (isFinal() && isVolatile())
			throw new NCIException(NCIException.Issue.ILLEGAL_FLAGS,
				String.format("AO0r %s", this));
		
		// If the class is an interface, some flags cannot be set
		if (__oc.flags().isInterface())
			for (NCIFieldFlag f : NCIFieldFlag.values())
			{
				// Must have these
				boolean must = (f == NCIFieldFlag.PUBLIC ||
					f == NCIFieldFlag.STATIC || f == NCIFieldFlag.FINAL);
				
				// Could have these
				boolean maybe = (f == NCIFieldFlag.SYNTHETIC);
				
				// Is it set?
				boolean has = contains(f);
				
				// {@squirreljme.error AO1t Flags for interface field has an
				// incorrect set of flags. (The field flags)}
				if (must != has && !maybe)
					throw new NCIException(NCIException.Issue.ILLEGAL_FLAGS,
						String.format("AO1t %s", this));
			}
	}
}

