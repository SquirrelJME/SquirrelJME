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
 * This represents the flags which a class may be.
 *
 * @since 2016/04/23
 */
public class ClassFlags
	extends Flags<ClassFlag>
	implements AccessibleFlags
{
	/**
	 * Initializes the class flags decoding from the specified bit field.
	 *
	 * @param __i The bit field to decode flags from.
	 * @since 2017/06/13
	 */
	public ClassFlags(int __i)
	{
		this(Flags.<ClassFlag>__decode(__i, ClassFlag.values()));
	}
	
	/**
	 * Initializes the class flags.
	 *
	 * @param __fl The class flags.
	 * @since 2016/04/23
	 */
	public ClassFlags(ClassFlag... __fl)
	{
		super(ClassFlag.class, __fl);
		
		this.__checkFlags();
	}
	
	/**
	 * Initializes the class flags.
	 *
	 * @param __fl The class flags.
	 * @since 2016/04/23
	 */
	public ClassFlags(Iterable<ClassFlag> __fl)
	{
		super(ClassFlag.class, __fl);
		
		this.__checkFlags();
	}
	
	/**
	 * Is this class abstract?
	 *
	 * @return {@code true} if it is abstract.
	 * @since 2016/03/15
	 */
	public final boolean isAbstract()
	{
		return this.contains(ClassFlag.ABSTRACT);
	}
	
	/**
	 * Is this an annotation?
	 *
	 * @return {@code true} if it is an annotation.
	 * @since 2016/03/15
	 */
	public final boolean isAnnotation()
	{
		return this.contains(ClassFlag.ANNOTATION);
	}
	
	/**
	 * Is this an enumeration?
	 *
	 * @return {@code true} if it is an enumeration.
	 * @since 2016/03/15
	 */
	public final boolean isEnum()
	{
		return this.contains(ClassFlag.ENUM);
	}
	
	/**
	 * Is this class final?
	 *
	 * @return {@code true} if it is final.
	 * @since 2016/03/15
	 */
	public final boolean isFinal()
	{
		return this.contains(ClassFlag.FINAL);
	}
	
	/**
	 * Is this an interface?
	 *
	 * @return {@code true} if an interface.
	 * @since 2016/03/15
	 */
	public final boolean isInterface()
	{
		return this.contains(ClassFlag.INTERFACE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public final boolean isPackagePrivate()
	{
		return !this.isPublic();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public final boolean isPrivate()
	{
		// Classes are never private
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public final boolean isProtected()
	{
		// Classes are never protected
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public final boolean isPublic()
	{
		return this.contains(ClassFlag.PUBLIC);
	}
	
	/**
	 * Is there special handling for super-class method calls?
	 *
	 * @return {@code true} if the super-class invocation special flag is set.
	 * @since 2016/03/15
	 */
	public final boolean isSpecialInvokeSpecial()
	{
		return this.contains(ClassFlag.SUPER);
	}
	
	/**
	 * Checks that the given flags are valid.
	 *
	 * @throws InvalidClassFormatException If they are not valid.
	 * @since 2016/04/23
	 */
	private void __checkFlags()
		throws InvalidClassFormatException
	{
		// Interface?
		if (this.isInterface())
		{
			// {@squirreljme.error JC2f An interface must also be abstract.
			// (The class flags)}
			if (!this.isAbstract())
				throw new InvalidClassFormatException(
					String.format("JC2f %s", this));
			
			// {@squirreljme.error JC2g An interface cannot be {@code final} or
			// {@code enum} and it must not have the special flag set. (The
			// class flags)}
			if (this.isFinal() || this.isSpecialInvokeSpecial() || this.isEnum())
				throw new InvalidClassFormatException(
					String.format("JC2g %s", this));
		}
		
		// Normal class
		else
		{
			// {@squirreljme.error JC2h Annotations must be interfaces. (The
			// class flags)}
			if (this.isAnnotation())
				throw new InvalidClassFormatException(
					String.format("JC2h %s", this));
				
			// {@squirreljme.error JC2i A class cannot be both {@code abstract}
			// and {@code final}. (The class flags)}
			if (this.isAbstract() && this.isFinal())
				throw new InvalidClassFormatException(
					String.format("JC2i %s", this));
		}
	}
}

