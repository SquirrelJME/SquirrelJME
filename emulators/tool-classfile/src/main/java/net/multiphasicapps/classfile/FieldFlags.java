// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
{
	/**
	 * Decodes field flags from the bit field and performs no checking.
	 *
	 * @param __i The bitfield to decode.
	 * @since 2019/04/18
	 */
	public FieldFlags(int __i)
	{
		super(FieldFlag.class,
			Flags.<FieldFlag>__decode(__i, FieldFlag.values()));
	}
	
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
		
		this.__checkFlags(__oc);
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
		
		this.__checkFlags(__oc);
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
		return this.contains(FieldFlag.ENUM);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isFinal()
	{
		return this.contains(FieldFlag.FINAL);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPrivate()
	{
		return this.contains(FieldFlag.PRIVATE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isProtected()
	{
		return this.contains(FieldFlag.PROTECTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPublic()
	{
		return this.contains(FieldFlag.PUBLIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isStatic()
	{
		return this.contains(FieldFlag.STATIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isSynthetic()
	{
		return this.contains(FieldFlag.SYNTHETIC);
	}
	
	/**
	 * Returns {@code true} if this is transient.
	 *
	 * @return {@code true} if transient.
	 * @since 2016/03/20
	 */
	public boolean isTransient()
	{
		return this.contains(FieldFlag.TRANSIENT);
	}
	
	/**
	 * Returns {@code true} if this is volatile.
	 *
	 * @return {@code true} if volatile.
	 * @since 2016/03/20
	 */
	public boolean isVolatile()
	{
		return this.contains(FieldFlag.VOLATILE);
	}
	
	/**
	 * Checks that the given flags are valid.
	 *
	 * @param __oc The outer class.
	 * @throws InvalidClassFormatException If they are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	private void __checkFlags(ClassFlags __oc)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__oc == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error JC2u A field cannot be both `final` and
		`volatile`. (The field flags)} */
		if (this.isFinal() && this.isVolatile())
			throw new InvalidClassFormatException(
				String.format("JC2u %s", this), this);
		
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
				boolean has = this.contains(f);
				
				/* {@squirreljme.error JC2v Flags for interface field has an
				incorrect set of flags. (The field flags)} */
				if (must != has && !maybe)
					throw new InvalidClassFormatException(
						String.format("JC2v %s", this), this);
			}
	}
}

