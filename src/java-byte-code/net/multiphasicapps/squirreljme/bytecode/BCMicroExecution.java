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

import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentiferSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This interface is used for micro-operation execution. Each operation in the
 * byte code can cause these to be executed which provides a basic interpreter
 * interface for running byte code.
 *
 * There are 2 billion registers that may be used to store 32-bit integer or
 * 32-bit float values. 64-bit values take up two register positions similar to
 * the Java virtual machine.
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
}

