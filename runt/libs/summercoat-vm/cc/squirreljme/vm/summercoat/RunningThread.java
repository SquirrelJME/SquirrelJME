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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
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
	
	/** Has this thread been started via the run method. */
	private volatile boolean _didstart;
	
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
	 * Sets up the thread so that the given method is entered from this
	 * thread, it is not started.
	 *
	 * @param __mh The method handle.
	 * @param __args The method arguments.
	 * @throws IllegalStateException If the thread has been started and this
	 * is not the current thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public void execEnterMethod(MethodHandle __mh, Value... __args)
		throws IllegalStateException, NullPointerException
	{
		// Must be the same thread
		__checkSameThread();
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/05
	 */
	@Override
	public void run()
	{
		// Thread is started by this, so method runs may only occur when the
		// current thread is self
		this._didstart = true;
		
		throw new todo.TODO();
	}
	
	/**
	 * Runs the specified method within the context of this thread and then
	 * returns the value of the execution. Note that if this thread has ever
	 * been started (its {@link run()} method called, then this must only
	 * ever be called by this self.
	 *
	 * @param __mh The method handle.
	 * @param __args The arguments to the call.
	 * @return The return value of the method, will be {@code null} on void
	 * types.
	 * @throws IllegalStateException If this thread has been run and the
	 * thread calling this method is not itself.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public Value runMethod(MethodHandle __mh, Value... __args)
		throws IllegalStateException, NullPointerException
	{
		if (__mh == null)
			throw new NullPointerException("NARG");
		
		// Must be the same thread
		__checkSameThread();
		
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
		
		// Must be the same thread
		__checkSameThread();
		
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
		
		// Must be the same thread
		__checkSameThread();
		
		// Defensive copy
		__args = (__args == null ? new Value[0] : __args.clone());
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns a {@code StaticMethod} to execute the given method.
	 *
	 * @param __mh The method handle.
	 * @return The virtual static method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final Instance vmStaticMethod(MethodHandle __mh)
		throws NullPointerException
	{
		// Must be the same thread
		__checkSameThread();
		
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
		// Must be the same thread
		__checkSameThread();
		
		throw new todo.TODO();
	}
	
	/**
	 * Checks that the call was done in the same thread, since when the thread
	 * is running it will completely break if another thread decides it wants
	 * to do things in this thread.
	 *
	 * @throws IllegalStateException If the thread was started and the check
	 * was performed in a different thread.
	 * @since 2019/01/10
	 */
	private final void __checkSameThread()
		throws IllegalStateException
	{
		// {@squirreljme.error AE01 This thread has already been started and
		// as such this method may only be called from within that thread.}
		if (this._didstart && this != Thread.currentThread())
			throw new IllegalStateException("AE01");
	}
}

