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
import cc.squirreljme.vm.springcoat.objects.ArrayViewer;
import cc.squirreljme.vm.springcoat.objects.ObjectViewer;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValueString;
import net.multiphasicapps.classfile.MethodDescriptor;
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
	
	/** The class info class. */
	private static final ClassName _CLASSINFO_CLASS =
		new ClassName("cc/squirreljme/jvm/ClassInfo");
	
	/** Load-UTF and intern function. */
	private static final MethodNameAndType _LOAD_NAME_AND_TYPE =
		new MethodNameAndType("__loadUtfAndIntern",
		"(J)Ljava/lang/String;");
	
	/** Classinfo constructor. */
	private static final MethodDescriptor _CLASSINFO_CONSTRUCTOR =
		new MethodDescriptor("(IJJIIIIILcc/squirreljme/jvm/ClassInfo;" +
			"[Lcc/squirreljme/jvm/ClassInfo;Lcc/squirreljme/jvm/ClassInfo;" +
			"Ljava/lang/Class;[J[JJIIIIJJ)V");
	
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
		
		// Prevent other threads from loading class infos
		Map<ClassName, SpringObject> classInfos = __thread.machine._classInfos;
		synchronized (classInfos)
		{
			// Pre-cached?
			SpringObject rv = classInfos.get(__class.name);
			if (rv != null)
				return rv;
			
			// Use for the allocation of strings
			MemoryManager mmu = __thread.machine.tasks.memory;
			
			// Initialize instance
			rv = __thread.newInstance(_CLASSINFO_CLASS, _CLASSINFO_CONSTRUCTOR,
				0, // int __fl
				(long)-1, // long __minip
				mmu.loadUtf(__class.name.toString()).pointer, // long __namep
				-1, // int __sz
				-1, // int __bz
				-1, // int __no
				-1, // int __dim
				-1, // int __csz
				SpringNullObject.NULL, // ClassInfo __scl
				SpringNullObject.NULL, // ClassInfo[] __icl
				SpringNullObject.NULL, // ClassInfo __ccl
				SpringNullObject.NULL, // Class<?> __cop
				SpringNullObject.NULL, // long[] __vtv
				SpringNullObject.NULL, // long[] __vtp
				(long)-1, // long __pool
				-1, // int __jardx
				-1, // int __nm
				-1, // int __cd
				-1, // int __sfp
				(long)-1, // long __dn
				(long)-1); // long __sm
			
			// Cache and use it
			classInfos.put(__class.name, rv);
			return rv;
		}
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
	
	/**
	 * Allocates new array.
	 *
	 * @param <T> The type of array viewer to create.
	 * @param __thread The creating thread.
	 * @param __componentType The component type.
	 * @param __len The length of the array.
	 * @return The array viewer.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/25
	 */
	public static <T> ArrayViewer<T> newArray(SpringThreadWorker __thread,
		SpringClass __componentType, int __len)
		throws NullPointerException
	{
		if (__thread == null || __componentType == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Creates a new instance of the given class.
	 *
	 * @param __thread The thread to allocate under.
	 * @param __class The class to allocate.
	 * @return The allocated object.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/25
	 */
	public static ObjectViewer newInstance(SpringThreadWorker __thread,
		SpringClass __class)
		throws NullPointerException
	{
		if (__thread == null || __class == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Allocates a new string.
	 *
	 * @param __thread The allocating thread.
	 * @param __s The string to allocate.
	 * @return The allocated string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/25
	 */
	public static ObjectViewer newString(SpringThreadWorker __thread,
		String __s)
		throws NullPointerException
	{
		if (__thread == null || __s == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Initializes a new string array.
	 *
	 * @param __thread The thread to create under.
	 * @param __vs Array values.
	 * @return The resultant array.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/22
	 */
	public static ArrayViewer<ObjectViewer> newStringArray(
		SpringThreadWorker __thread, String... __vs)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		if (__vs == null)
			__vs = new String[0];
		
		// Allocate array
		ArrayViewer<ObjectViewer> rv = ObjectLoader.<ObjectViewer>newArray(
			__thread,
			__thread.machine.classloader.loadClass(ObjectLoader._STRING_CLASS),
			__vs.length);
		
		// Store strings into the array
		for (int i = 0, n = __vs.length; i < n; i++)
		{
			String v = __vs[i];
			
			if (v != null)
				rv.set(i, ObjectLoader.newString(__thread, v));
		}
		
		return rv;
	}
}
