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
 * This represents the name of a method. This has the same constraints as the
 * identifier except it cannot contain the {@code <} or {@code >} symbols
 * unless it is {@code <init>} or {@code <clinit>}.
 *
 * @since 2017/07/07
 */
public final class MethodName
	extends Identifier
	implements Comparable<MethodName>, MemberName
{
	/**
	 * Initializes the method name.
	 *
	 * @param __s The method name.
	 * @throws InvalidClassFormatException If the method name is method valid.
	 * @since 2017/07/07
	 */
	public MethodName(String __s)
		throws InvalidClassFormatException
	{
		super(__s);
		
		// Cannot contain < or >
		if (!__s.equals("<init>") && !__s.equals("<clinit>"))
			for (int i = 0, n = __s.length(); i < n; i++)
			{
				char c = __s.charAt(i);
				
				// {@squirreljme.error JC1b Method names cannot contain less
				// than or greater than signs. (The method name)}
				if (c == '<' || c == '>')
					throw new InvalidClassFormatException(
						String.format("JC1b %s", __s));
			}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/10
	 */
	@Override
	public int compareTo(MethodName __o)
	{
		return this.string.compareTo(__o.string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public boolean equals(Object __o)
	{
		return (__o instanceof MethodName) && super.equals(__o);
	}
	
	/**
	 * Is this any initializer for a method.
	 *
	 * @return If this is any initializer method.
	 * @since 2017/10/12
	 */
	public boolean isAnyInitializer()
	{
		return isInstanceInitializer() || isStaticInitializer();
	}
	
	/**
	 * Returns {@code true} if this represents the instance initializer.
	 *
	 * @return {@code true} if this is the instance initializer.
	 * @since 2017/07/28
	 */
	public boolean isInstanceInitializer()
	{
		return this.string.equals("<init>");
	}
	
	/**
	 * Returns {@code true} if this represents the static initializer.
	 *
	 * @return {@code true} if this is the static initializer.
	 * @since 2017/07/28
	 */
	public boolean isStaticInitializer()
	{
		return this.string.equals("<clinit>");
	}
}

