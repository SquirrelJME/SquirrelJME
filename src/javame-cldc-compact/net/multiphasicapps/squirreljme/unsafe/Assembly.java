// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * This class contains special static methods which are translated by the
 * native compiler to support special actions. The purpose of this class is to
 * have inline assembly along with Java code.
 *
 * All methods in this class are unsafe and are used by the native compiler to
 * assist Java code in performing actions that it should not be performed.
 *
 * The native compiler is required to place checks before the generated
 * assembly code to see if the current code is executing in the kernel space,
 * if it is not then an exception is thrown.
 *
 * {@squirreljme.error ZZ04 Non-translated assembly generator
 * invocation.}
 *
 * @since 2016/05/27
 */
public final class Assembly
{
	/**
	 * This class is not initialized.
	 *
	 * @since 2016/05/27
	 */
	private Assembly()
	{
	}
	
	/**
	 * This allows reading of a specific register value and placing it into
	 * a variable which the normal Java code may use. This may be used if the
	 * result of an assembly statement needs to be placed into an actual
	 * variable before it can be used by Java code.
	 *
	 * @param __v The name of the register to load a value from and to be
	 * treated as an integer value. The specified value must be a compile time
	 * constant which exists in the constant pool of the class being compiled.
	 * @return The value of the given register.
	 * @throws Error If the assembly was not translated by the compiler.
	 * @since 2016/05/27
	 */
	public static int loadRegisterInt(String __v)
		throws Error
	{
		throw new Error("ZZ04");
	}
	
	/**
	 * This is used by the native compiler and any invocations of this method
	 * are used to translate the given invocation to the specified assembly
	 * statement.
	 *
	 * Only single assembly statements are supported.
	 *
	 * @param __v The assembly statement to parse and generate assembly code
	 * for. This statement must be static at compilation time and as such the
	 * value passed to this method must exist in the constant pool of the
	 * class.
	 * @throws Error If the assembly was not translated by the compiler.
	 * @since 2016/05/27
	 */
	public static void asm(String __v)
		throws Error
	{
		throw new Error("ZZ04");
	}
}

