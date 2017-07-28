// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents the name of a method. This has the same constraints as the
 * identifier except it cannot contain the {@code <} or {@code >} symbols
 * unless it is {@code <init>} or {@code <clinit>}.
 *
 * @since 2017/07/07
 */
public final class MethodName
	extends Identifier
{
	/**
	 * Initializes the method name.
	 *
	 * @param __s The method name.
	 * @throws JITException If the method name is method valid.
	 * @since 2017/07/07
	 */
	public MethodName(String __s)
		throws JITException
	{
		super(__s);
		
		// Cannot contain < or >
		if (!__s.equals("<init>") && __s.equals("<clinit>"))
			for (int i = 0, n = __s.length(); i < n; i++)
			{
				char c = __s.charAt(i);
				
				// {@squirreljme.error JI15 Method names cannot contain less
				// than or greater than signs. (The method name)}
				if (c == '<' || c == '>')
					new JITException(String.format("JI15 %s", __s));
			}
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

