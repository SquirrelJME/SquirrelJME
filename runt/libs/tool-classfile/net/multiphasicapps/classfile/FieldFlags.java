// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represent the set of flags for fields.
 *
 * @since 2016/04/23
 */
public final class FieldFlags
	extends MemberFlags<FieldFlag>
	implements AccessibleFlags
{
	/**
	 * Initializes the field flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The field flags.
	 * @since 2016/04/23
	 */
	public FieldFlags(ClassFlags __oc, FieldFlag... __fl)
	{
		super(FieldFlag.class, __fl);
		
		__checkFlags(__oc);
	}
	
	/**
	 * Initializes the field flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The field flags.
	 * @since 2016/04/23
	 */
	public FieldFlags(ClassFlags __oc, Iterable<FieldFlag> __fl)
	{
		super(FieldFlag.class, __fl);
		
		__checkFlags(__oc);
	}
	
	/**
	 * Decodes field flags from the bit field.
	 *
	 * @param __oc The outer class flags.
	 * @param __i The bitfield to decode.
	 * @since 2017/07/07
	 */
	public FieldFlags(ClassFlags __oc, int __i)
	{
		this(__oc, Flags.<FieldFlag>__decode(__i, FieldFlag.values()));
	}
	
	/**
	 * Returns {@code true} if this is an enumeration.
	 *
	 * @return {@code true} if an enumeration.
	 * @since 2016/03/20
	 */
	public boolean isEnum()
	{
		return contains(FieldFlag.ENUM);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isFinal()
	{
		return contains(FieldFlag.FINAL);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPrivate()
	{
		return contains(FieldFlag.PRIVATE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isProtected()
	{
		return contains(FieldFlag.PROTECTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPublic()
	{
		return contains(FieldFlag.PUBLIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isStatic()
	{
		return contains(FieldFlag.STATIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isSynthetic()
	{
		return contains(FieldFlag.SYNTHETIC);
	}
	
	/**
	 * Returns {@code true} if this is transient.
	 *
	 * @return {@code true} if transient.
	 * @since 2016/03/20
	 */
	public boolean isTransient()
	{
		return contains(FieldFlag.TRANSIENT);
	}
	
	/**
	 * Returns {@code true} if this is volatile.
	 *
	 * @return {@code true} if volatile.
	 * @since 2016/03/20
	 */
	public boolean isVolatile()
	{
		return contains(FieldFlag.VOLATILE);
	}
	
	/**
	 * Checks that the given flags are valid.
	 *
	 * @param __oc The outer class.
	 * @throws InvalidClassFormatException If they are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	private final void __checkFlags(ClassFlags __oc)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__oc == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC12 A field cannot be both {@code final} and
		// {@code volatile}. (The field flags)}
		if (isFinal() && isVolatile())
			throw new InvalidClassFormatException(
				String.format("JC12 %s", this));
		
		// If the class is an interface, some flags cannot be set
		if (__oc.isInterface())
			for (FieldFlag f : FieldFlag.values())
			{
				// Must have these
				boolean must = (f == FieldFlag.PUBLIC ||
					f == FieldFlag.STATIC || f == FieldFlag.FINAL);
				
				// Could have these
				boolean maybe = (f == FieldFlag.SYNTHETIC);
				
				// Is it set?
				boolean has = contains(f);
				
				// {@squirreljme.error JC13 Flags for interface field has an
				// incorrect set of flags. (The field flags)}
				if (must != has && !maybe)
					throw new InvalidClassFormatException(
						String.format("JC13 %s", this));
			}
	}
}

