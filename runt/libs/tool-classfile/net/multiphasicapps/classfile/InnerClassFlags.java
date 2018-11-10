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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This represents a set of flags which are used as modifiers to inner classes.
 *
 * @since 2018/05/15
 */
public final class InnerClassFlags
	extends Flags<InnerClassFlag>
	implements AccessibleFlags
{
	/** Standard class flag representation. */
	private Reference<ClassFlags> _cflags;
	
	/**
	 * Initializes the inner class flags decoding from the specified bit field.
	 *
	 * @param __i The bit field to decode flags from.
	 * @since 2018/05/15
	 */
	public InnerClassFlags(int __i)
	{
		this(Flags.<InnerClassFlag>__decode(__i, InnerClassFlag.values()));
	}
	
	/**
	 * Initializes the inner class flags.
	 *
	 * @param __fl The inner class flags.
	 * @since 2018/05/15
	 */
	public InnerClassFlags(InnerClassFlag... __fl)
	{
		super(InnerClassFlag.class, __fl);
		
		this.__checkFlags();
	}
	
	/**
	 * Initializes the inner class flags.
	 *
	 * @param __fl The inner class flags.
	 * @since 2018/05/15
	 */
	public InnerClassFlags(Iterable<InnerClassFlag> __fl)
	{
		super(InnerClassFlag.class, __fl);
		
		this.__checkFlags();
	}
	
	/**
	 * Returns the outer class representation for these flags.
	 *
	 * @return The outer class flag representation.
	 * @since 2018/05/21
	 */
	public final ClassFlags asClassFlags()
	{
		Reference<ClassFlags> ref = this._cflags;
		ClassFlags rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			int mask = 0;
			for (InnerClassFlag i : this)
			{
				ClassFlag v;
				switch (i)
				{
					case PUBLIC:
						v = ClassFlag.PUBLIC;
						break;
						
					case FINAL:
						v = ClassFlag.FINAL;
						break;
						
					case INTERFACE:
						v = ClassFlag.INTERFACE;
						break;
						
					case ABSTRACT:
						v = ClassFlag.ABSTRACT;
						break;
						
					case SYNTHETIC:
						v = ClassFlag.SYNTHETIC;
						break;
						
					case ANNOTATION:
						v = ClassFlag.ANNOTATION;
						break;
						
					case ENUM:
						v = ClassFlag.ENUM;
						break;
					
					default:
						v = null;
						continue;
				}
				
				if (v != null)
					mask |= v.javaBitMask();
			}
			
			this._cflags = new WeakReference<>((rv = new ClassFlags(mask)));
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final boolean isPackagePrivate()
	{
		return !this.isPrivate() && !this.isProtected() && !this.isPublic();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final boolean isPrivate()
	{
		return this.contains(InnerClassFlag.PRIVATE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final boolean isProtected()
	{
		return this.contains(InnerClassFlag.PROTECTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final boolean isPublic()
	{
		return this.contains(InnerClassFlag.PUBLIC);
	}
	
	/**
	 * Checks that the inner class flags are valid.
	 *
	 * @throws InvalidClassFormatException If the inner class flags are not
	 * valid.
	 * @since 2018/05/15
	 */
	private final void __checkFlags()
		throws InvalidClassFormatException
	{
		// Construct class flags which checks if they are valid
		try
		{
			this.asClassFlags();
		}
		catch (InvalidClassFormatException e)
		{
			// {@squirreljme.error JC16 Inner class flags are not valid
			// because they would produce invalid standard outer class
			// flags. (The flags)}
			throw new InvalidClassFormatException(String.format("JC16 %s",
				this), e);
		}
		
		// {@squirreljme.error JC17 Multiple access modifiers, inner classes
		// can only be one or none of private, protected, or public.
		// (The flags)}
		int count = (this.isPublic() ? 1 : 0) +
			(this.isProtected() ? 1 : 0) +
			(this.isPrivate() ? 1 : 0);
		if (count > 1)
			throw new InvalidClassFormatException(String.format("JC17 %s",
				this));
	}
}

