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

import dev.shadowtail.classfile.mini.MinimizedPool;
import dev.shadowtail.classfile.mini.MinimizedPoolEntryType;
import dev.shadowtail.classfile.nncc.InvokedMethod;
import dev.shadowtail.classfile.xlate.InvokeType;
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
	
	/** Owner thread, for when it is not started and has been called. */
	private volatile Thread _rmthread;
	
	/** The counts for the current thread run. */
	private volatile int _rmcount;
	
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
		boolean didstart = __checkSameThread();
		if (!didstart)
			synchronized (this)
			{
				// Current thread is always used
				Thread current = Thread.currentThread();
				
				// Take over for this thread
				Thread rmthread = this._rmthread;
				if (rmthread == null)
				{
					this._rmthread = current;
					this._rmcount = 1;
				}
				
				// Count up the thread usage
				else
				{
					// {@squirreljme.error AE02 Cannot run a method which is
					// crossed from another thread which is running a method.}
					if (rmthread != Thread.currentThread())
						throw new IllegalStateException("AE02");
					
					this._rmcount++;
				}
			}
		
		// Needed to clear the current thread
		try
		{
			throw new todo.TODO();
		}
		
		// Make sure this is always run
		finally
		{
			// If the thread has not been started then reduce our count and
			// additionally make sure if it clears that the current context
			// thread is also cleared
			if (!didstart)
				synchronized (this)
				{
					if ((--this._rmcount) <= 0)
						this._rmthread = null;
				}
		}
	}
	
	/**
	 * Initializes the specified class within the virtual machine.
	 *
	 * @param __cl The class to initialize.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public final void vmInitializeClass(LoadedClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Must be the same thread
		this.__checkSameThread();
		
		// If this class has been initialized, do not do it again!
		if (__cl._beeninit)
			return;
		
		// Used by the initializer
		TaskStatus status = this.status;
		ClassLoader classloader = status.classloader;
		
		// Lock on the class initialization lock as only a single class can
		// initialize at once
		synchronized (status.classinitlock)
		{
			// Check again if this has been initialized, even though this
			// should never happen it just might
			if (__cl._beeninit)
				return;
			
			// Set class as initialized since there might be recursive
			// initialization at play
			__cl._beeninit = true;
			
			// Initialize the super class first
			LoadedClass slc = __cl.superclass;
			if (slc != null)
				this.vmInitializeClass(slc);
			
			// Then initialize any interfaces
			for (LoadedClass ilc : __cl._interfaces)
				this.vmInitializeClass(ilc);
			
			// Debug
			todo.DEBUG.note("Initializing %s...", __cl.miniclass.thisName());
			
			// Before any code can be run in the the class, the constant pool
			// need to be initialized with references and positions
			RuntimeConstantPool rpool = __cl.runpool;
			MinimizedPool mpool = rpool.minipool;
			
			// Allocate array of realized pool entries
			int nmp = mpool.size();
			Object[] rzp = new Object[nmp];
			
			// Initialize each pool entry
			for (int p = 0; p < nmp; p++)
			{
				// Type and original value
				MinimizedPoolEntryType type = mpool.type(p);
				Object orig = mpool.get(p);
				
				// Debug
				todo.DEBUG.note("Pool %s: %s", type, orig);
				
				// Obtain its true value
				Object v;
				switch (type)
				{
						// Ignore null
					case NULL:
						v = null;
						break;
						
						// These are used as is, strings are interned when
						// they are demanded
					case STRING:
					case INTEGER:
					case LONG:
					case FLOAT:
					case DOUBLE:
						v = orig;
						break;
						
						// These do not actually contain any information that
						// is useful for SummerCoat because it has classes to
						// represent these easily. These are just here to make
						// writing the C based VM easier.
					case METHOD_DESCRIPTOR:
						v = orig;
						break;
						
						// Classes just point to loaded classes
					case CLASS_NAME:
						v = classloader.loadClass((ClassName)orig);
						break;
						
						// Method to be invoked, is referenced by handle
					case INVOKED_METHOD:
						v = this.__getInvokeHandle(__cl, (InvokedMethod)orig);
						break;
					
						// Unhandled
					default:
						throw new todo.OOPS(type.name());
				}
				
				// Set
				rzp[p] = v;
			}
			
			throw new todo.TODO();
		}
	}
	
	/**
	 * Interns the string so that only a single instance is available.
	 *
	 * @param __s The string to intern.
	 * @return The single intern instance.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public final Instance vmInternString(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Must be the same thread
		this.__checkSameThread();
		
		throw new todo.TODO();
	}
	
	/**
	 * Creates a new instance of the given object.
	 *
	 * @param __cl The instance to create.
	 * @return The newly created instance.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public final Instance vmNew(String __cl)
		throws NullPointerException
	{
		return this.vmNew(this.status.classloader.loadClass(__cl));
	}
	
	/**
	 * Creates a new instance of the given object.
	 *
	 * @param __cl The instance to create.
	 * @return The newly created instance.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public final Instance vmNew(LoadedClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Must be the same thread
		this.__checkSameThread();
		
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
		this.__checkSameThread();
		
		// Potentially initialize this class
		this.vmInitializeClass(__cl);
		
		// Create instance of object
		ArrayInstance rv = new PlainArray(__cl, __len);
		
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
		this.__checkSameThread();
		
		// Defensive copy
		__args = (__args == null ? new Value[0] : __args.clone());
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns an in virtual machine {@code StaticMethod} which when is
	 * executed will execute the given method handle.
	 *
	 * @param __mh The method handle.
	 * @return The virtual static method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final Instance vmStaticMethod(MethodHandle __mh)
		throws NullPointerException
	{
		if (__mh == null)
			throw new NullPointerException("NARG");
		
		// Must be the same thread
		this.__checkSameThread();
		
		// Just return a specialized instance
		return new StaticMethodInstance(__mh);
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
		this.__checkSameThread();
		
		throw new todo.TODO();
	}
	
	/**
	 * Checks that the call was done in the same thread, since when the thread
	 * is running it will completely break if another thread decides it wants
	 * to do things in this thread.
	 *
	 * @return If the thread has been started, and is using {@link #run()}.
	 * @throws IllegalStateException If the thread was started and the check
	 * was performed in a different thread.
	 * @since 2019/01/10
	 */
	private final boolean __checkSameThread()
		throws IllegalStateException
	{
		// {@squirreljme.error AE01 This thread has already been started and
		// as such this method may only be called from within that thread.}
		boolean didstart = this._didstart;
		if (didstart && this != Thread.currentThread())
			throw new IllegalStateException("AE01");
		return didstart;
	}
	
	/**
	 * Obtains the invocation handle for the given class.
	 *
	 * @param __from The calling class.
	 * @param __m The invoked method.
	 * @return The handle to call this method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	private final MethodHandle __getInvokeHandle(LoadedClass __from,
		InvokedMethod __m)
		throws NullPointerException
	{
		if (__from == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Get all the various parts
		InvokeType mty = __m.type();
		LoadedClass mcl = this.status.classloader.loadClass(
			__m.handle().outerClass());
		MethodNameAndType mnt = __m.handle().nameAndType();
		
		// Calling method in another class, need to check access
		if (__from != mcl)
		{
			throw new todo.TODO();
		}
		
		// The returned handle depends on the invocation type
		switch (mty)
		{
				// Static methods just point directly to the target method
			case STATIC:
				return mcl.lookupMethod(MethodLookupType.STATIC, true, mnt);
			
				// Virtual methods, interface methods are treated as virtual
				// because no interface lookup optimization is available.
			case INTERFACE:
			case VIRTUAL:
				return mcl.lookupMethod(MethodLookupType.INSTANCE, false, mnt);
			
			default:
				throw new todo.TODO(mty.name());
		}
	}
}

