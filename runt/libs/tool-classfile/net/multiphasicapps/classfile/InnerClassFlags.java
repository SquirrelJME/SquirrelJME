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
 * This represents a set of flags which are used as modifiers to inner classes.
 *
 * @since 2018/05/15
 */
public final class InnerClassFlags
	extends Flags<InnerClassFlag>
	implements AccessibleFlags
{
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
		throw new todo.TODO();
	}
}

