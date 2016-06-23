// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bytecode;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This interface is used for micro-operation execution. Each operation in the
 * byte code can cause these to be executed which provides a basic interpreter
 * interface for running byte code.
 *
 * There are 4 billion registers that may be used to store 32-bit integer or
 * 32-bit float values. 64-bit values take up two register positions similar to
 * the Java virtual machine. Registers which are negative are considered to
 * be the temporary stack registers.
 *
 * There is a single activation register which is given a class, field, or
 * method which may be operated on with specific operations. When said types
 * are stored into the active register they should be checked that they are
 * accessible, exist, and are valid for execution.
 *
 * @since 2016/06/22
 */
public interface BCMicroExecution
{
	/**
	 * This sets the given class to be in the active register.
	 *
	 * @param __c The class to become active.
	 * @since 2016/06/22
	 */
	public abstract void activateClass(ClassNameSymbol __c);
	
	/**
	 * This sets the given static or instance field so that it is in the active
	 * register.
	 *
	 * @param __static If {@code true} then a static field is activated,
	 * otherwise an instance field is activated.
	 * @param __c The containing class.
	 * @param __n The name of the field.
	 * @param __t The type of the field.
	 * @since 2016/06/22
	 */
	public abstract void activateField(boolean __static, ClassNameSymbol __c,
		IdentifierSymbol __n, FieldSymbol __t);
	
	/**
	 * This sets the given static or instance method so that it is in the
	 * active register.
	 *
	 * @param __static if {@code true} then a static method is activated,
	 * otherwise an instance method is activated.
	 * @param __c The containing class.
	 * @param __n The name of the method.
	 * @param __t The type of the field.
	 * @since 2016/06/22
	 */
	public abstract void activateMethod(boolean __static, ClassNameSymbol __c,
		IdentifierSymbol __n, FieldSymbol __t);
	
	/**
	 * Allocates the class which is currently activated and stores it in a
	 * given register.
	 *
	 * @param __reg The target register where the allocated active class
	 * is placed.
	 * @since 2016/06/22
	 */
	public abstract void allocateClass(int __reg);
	
	/**
	 * Adjusts the top of the stack.
	 *
	 * @param __dif The amount to adjust the top of the stack with, positive
	 * values increase the stack size while negative values reduce it.
	 * @since 2016/06/22
	 */
	public abstract void adjustStackTop(int __dif);
	
	/**
	 * Unconditionally jumps to the specified logical address.
	 *
	 * @param __la The logical address to jump to.
	 * @since 2016/06/22
	 */
	public abstract void unconditionalJump(int __la);
}

