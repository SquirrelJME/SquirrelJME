// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.interpreter;

/**
 * Represents an abstract class.
 *
 * @since 2022/09/18
 */
public abstract class AbstractClass
{
	/**
	 * An abstract method.
	 * 
	 * @param __a The first digit.
	 * @param __b The second digit.
	 * @return The result of these.
	 * @since 2022/09/18
	 */
	public abstract int abstractMethod(int __a, int __b);
	
	/**
	 * A final method which cannot be overridden.
	 * 
	 * @param __a The first digit.
	 * @param __b The second digit.
	 * @return The result of these.
	 * @since 2022/09/18
	 */
	public final int finalMethod(int __a, int __b)
	{
		return __a + __b;
	}
	
	/**
	 * An overridable method, should be used with {@code super.superMethod()}.
	 * 
	 * @param __a The first digit.
	 * @param __b The second digit.
	 * @return The result of these.
	 * @since 2022/09/18
	 */
	public int superMethod(int __a, int __b)
	{
		return __a + __b;
	}
}
