// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This represents a thread which is running in the virtual machine.
 *
 * This class implements thread itself so it may be interrupted as needed and
 * such.
 *
 * @since 2019/01/05
 */
public final class RunningThread
	extends Thread
{
	/** The ID of this thread. */
	protected final int id;
	
	/** The task status this reports on. */
	protected final TaskStatus status;
	
	/**
	 * Initializes the thread.
	 *
	 * @param __id The thread ID.
	 * @param __s The task status.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public RunningThread(int __id, TaskStatus __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.id = __id;
		this.status = __s;
	}
	
	/**
	 * Sets up the thread so that the given method is enterred from this
	 * thread.
	 *
	 * @param __static Is this method static?
	 * @param __cl The class to enter.
	 * @param __name The method name.
	 * @param __desc The method type.
	 * @param __args The method arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public void execEnterMethod(boolean __static, String __cl, String __name,
		String __desc, Value... __args)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/05
	 */
	@Override
	public void run()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Creates a new array instance.
	 *
	 * @param __cl The class to create an array of, this is not the component
	 * type.
	 * @param __len The length of the array.
	 * @return The newly created array.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final ArrayInstance vmNewArray(String __cl, int __len)
		throws NullPointerException
	{
		return this.vmNewArray(this.status.classloader.loadClass(__cl), __len);
	}
	
	/**
	 * Creates a new array instance.
	 *
	 * @param __cl The class to create an array of, this is not the component
	 * type.
	 * @param __len The length of the array.
	 * @return The newly created array.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final ArrayInstance vmNewArray(LoadedClass __cl, int __len)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Creates a new instance of the given class.
	 *
	 * @param __cl The class to create an instance of.
	 * @param __desc The constructor method used.
	 * @param __args The arguments to the constructor.
	 * @return The newly constructed instance of the given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final Instance vmNewInstance(String __cl, String __desc,
		Value... __args)
		throws NullPointerException
	{
		return this.vmNewInstance(this.status.classloader.loadClass(__cl),
			new MethodDescriptor(__desc), __args);
	}
	
	/**
	 * Creates a new instance of the given class.
	 *
	 * @param __cl The class to create an instance of.
	 * @param __desc The constructor method used.
	 * @param __args The arguments to the constructor.
	 * @return The newly constructed instance of the given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final Instance vmNewInstance(LoadedClass __cl,
		MethodDescriptor __desc, Value... __args)
		throws NullPointerException
	{
		if (__cl == null || __desc == null)
			throw new NullPointerException("NARG");
		
		// Defensive copy
		__args = (__args == null ? new Value[0] : __args.clone());
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns a {@code StaticMethod} to execute the given method.
	 *
	 * @param __static Should a static method be resolved?
	 * @param __cl The class to lookup.
	 * @param __name The method name.
	 * @param __desc The method type.
	 * @return The virtual static method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final Instance vmStaticMethod(boolean __static, String __cl,
		String __name, String __desc)
		throws NullPointerException
	{
		return this.vmStaticMethod(__static,
			this.status.classloader.loadClass(__cl),
			new MethodNameAndType(__name, __desc));
	}
	
	/**
	 * Returns a {@code StaticMethod} to execute the given method.
	 *
	 * @param __static Should a static method be resolved?
	 * @param __cl The class to lookup.
	 * @param __name The method name.
	 * @param __desc The method type.
	 * @return The virtual static method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final Instance vmStaticMethod(boolean __static, LoadedClass __cl,
		String __name, String __desc)
		throws NullPointerException
	{
		return this.vmStaticMethod(__static, __cl,
			new MethodNameAndType(__name, __desc));
	}
	
	/**
	 * Returns a {@code StaticMethod} to execute the given method.
	 *
	 * @param __static Should a static method be resolved?
	 * @param __cl The class to lookup.
	 * @param __nat The method name and type.
	 * @return The virtual static method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final Instance vmStaticMethod(boolean __static, LoadedClass __cl,
		MethodNameAndType __nat)
		throws NullPointerException
	{
		if (__cl == null || __nat == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Translates the specified string to an in VM string.
	 *
	 * @param __in The input string.
	 * @return The instance of the string or the associated {@code null}.
	 * @since 2019/01/10
	 */
	public final Instance vmTranslateString(String __in)
	{
		throw new todo.TODO();
	}
}

