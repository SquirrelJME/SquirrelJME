// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.java;

import net.multiphasicapps.classfile.ClassFlags;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Field;
import net.multiphasicapps.classfile.Method;

/**
 * Handler which can process classes and otherwise.
 *
 * @since 2022/08/04
 */
public interface ClassHandler
{
	/**
	 * Finishes handling the class.
	 * 
	 * @since 2022/09/07
	 */
	void finishClass();
	
	/**
	 * Processes the given field.
	 * 
	 * @param __field The field to process.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/07
	 */
	void processField(Field __field)
		throws NullPointerException;
		
	/**
	 * Indicates the start of processing members.
	 * 
	 * @param __isStatic Are these static members?
	 * @since 2022/09/07
	 */
	void processMembers(boolean __isStatic);
	
	/**
	 * Processes the given method.
	 * 
	 * @param __method The method to process.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/07
	 */
	void processMethod(Method __method)
		throws NullPointerException;
	
	/**
	 * Starts processing of the given class.
	 * 
	 * @param __flags The class flags.
	 * @param __thisName The name of this class.
	 * @param __superName The super class.
	 * @param __interfaces The interfaces this class implements.
	 * @throws NullPointerException If no flags or this name are specified.
	 * @since 2022/09/07
	 */
	void startClass(ClassFlags __flags, ClassName __thisName,
		ClassName __superName, ClassName... __interfaces)
		throws NullPointerException;
}
