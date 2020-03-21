// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValueString;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This is the loader for various different object types.
 *
 * @since 2020/03/14
 */
public final class ObjectLoader
{
	/** The string class. */
	private static final ClassName _STRING_CLASS =
		new ClassName("java/lang/String");
	
	/** Load-UTF and intern function. */
	private static final MethodNameAndType _LOAD_NAME_AND_TYPE =
		new MethodNameAndType("__loadUtfAndIntern",
		"(J)Ljava/lang/String;");
	
	/**
	 * Not used.
	 *
	 * @since 2020/03/14
	 */
	private ObjectLoader()
	{
	}
	
	/**
	 * Loads the class info from the given class.
	 *
	 * @param __class The class ot map.
	 * @return The object for this class.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/21
	 */
	public static SpringObject loadClassInfo(SpringThreadWorker __thread,
		SpringClass __class)
		throws NullPointerException
	{
		if (__thread == null || __class == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Loads a constant pool based string value into the intern pool of the
	 * VM.
	 *
	 * @param __thread The creating thread.
	 * @param __in The input string.
	 * @return The resulting object.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/14
	 */
	public static Object loadConstantValueString(SpringThreadWorker __thread,
		ConstantValueString __in)
		throws NullPointerException
	{
		if (__thread == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Load the UTF string into memory
		SpringMachine machine = __thread.machine;
		SpringPointer utfPointer = machine.tasks.memory.loadUtf(
			__in.toString());
		
		// Call the string internal intern since it does the things needed
		return __thread.invokeMethod(true, ObjectLoader._STRING_CLASS,
			ObjectLoader._LOAD_NAME_AND_TYPE, utfPointer.pointer);
	}
}
