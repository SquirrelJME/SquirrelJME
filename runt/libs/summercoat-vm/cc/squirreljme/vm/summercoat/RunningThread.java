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

import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.MinimizedPool;
import dev.shadowtail.classfile.mini.MinimizedPoolEntryType;
import dev.shadowtail.classfile.nncc.AccessedField;
import dev.shadowtail.classfile.nncc.FieldAccessTime;
import dev.shadowtail.classfile.nncc.FieldAccessType;
import dev.shadowtail.classfile.nncc.InvokedMethod;
import dev.shadowtail.classfile.xlate.InvokeType;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.ClassFlags;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldFlags;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.FieldNameAndType;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodFlags;
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
	public void execEnterMethod(MethodHandle __mh, Object... __args)
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
	 * @return The return value of the method, will just be the value which
	 * was grabbed from the return register.
	 * @throws IllegalStateException If this thread has been run and the
	 * thread calling this method is not itself.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public long runMethod(MethodHandle __mh, Object... __args)
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
	 * Sets the value in the specified array.
	 *
	 * @param __tp The typed pointer.
	 * @param __dx The index to set.
	 * @param __v The value to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/18
	 */
	public final TypedPointer vmArraySet(TypedPointer __tp, int __dx,
		Object __v)
		throws NullPointerException
	{
		if (__tp == null || __v == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
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
			
			// If static field space has not been claimed then claim it now
			if (!__cl._claimedsfspace)
				this.__claimStaticFieldSpace(__cl);
			
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
						
						// Access to a field, offset is used
					case ACCESSED_FIELD:
						v = this.__getAccessedField(__cl,
							(AccessedField)orig);
						break;
						
						// Classes just point to loaded classes
					case CLASS_NAME:
						v = classloader.loadClass((ClassName)orig);
						break;
						
						// List of loaded class names
					case CLASS_NAMES:
						{
							// Get class names
							ClassNames cns = (ClassNames)orig;
							int ncn = cns.size();
							
							// Build new values
							LoadedClass[] lcs = new LoadedClass[ncn];
							for (int x = 0; x < ncn; x++)
								lcs[x] = classloader.loadClass(cns.get(x));
							
							// Set
							v = lcs;
						}
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
			
			// Initialize static fields with a constant value
			for (MinimizedField ff : __cl.miniclass.fields(true))
				if (ff.flags().isStatic() && ff.value != null)
				{
					throw new todo.TODO();
				}
			
			// Execute static constructor
			MethodHandle staticinit = __cl.staticinit;
			if (staticinit != null)
			{
				throw new todo.TODO();
			}
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
	public final TypedPointer vmInternString(String __s)
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
	public final TypedPointer vmNew(String __cl)
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
	public final TypedPointer vmNew(LoadedClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Uses virtually the same logic as arrays
		return this.vmNewArray(__cl, 0);
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
	public final TypedPointer vmNewArray(String __cl, int __len)
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
	public final TypedPointer vmNewArray(LoadedClass __cl, int __len)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Must be the same thread
		this.__checkSameThread();
		
		// Potentially initialize this class
		this.vmInitializeClass(__cl);
		
		throw new todo.TODO();
		/*
		// Create instance of object
		boolean isarray;
		Instance rv = ((isarray = __cl.isArray()) ?
			new PlainArray(__cl, __len) : new PlainObject(__cl));
		
		// Register the instance into the object table
		int vptr = this.status.memory.registerInstance(rv);
		((PlainObject)rv)._vptr = vptr;
		
		// Debug
		todo.DEBUG.note("Registered new %s at %d", __cl, vptr);
		
		// Done with it
		return new AllocationPoint(rv, vptr);
		*/
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
	public final TypedPointer vmNewInstance(String __cl, String __desc,
		Object... __args)
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
	public final TypedPointer vmNewInstance(LoadedClass __cl,
		MethodDescriptor __desc, Object... __args)
		throws NullPointerException
	{
		if (__cl == null || __desc == null)
			throw new NullPointerException("NARG");
		
		// Must be the same thread
		this.__checkSameThread();
		
		// Defensive copy
		__args = (__args == null ? new Object[0] : __args.clone());
		
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
	public final TypedPointer vmStaticMethod(MethodHandle __mh)
		throws NullPointerException
	{
		if (__mh == null)
			throw new NullPointerException("NARG");
		
		// Must be the same thread
		this.__checkSameThread();
		
		// Just return a specialized instance
		throw new todo.TODO();
		/*
		return new StaticMethodInstance(__mh);
		*/
	}
	
	/**
	 * Translates the specified string to an in VM string. This does not
	 * intern the string, it just creates a new string.
	 *
	 * @param __in The input string.
	 * @return The instance of the string or the associated {@code null}.
	 * @since 2019/01/10
	 */
	public final TypedPointer vmTranslateString(String __in)
	{
		// Must be the same thread
		this.__checkSameThread();
		
		// If null just make a null pointer
		if (__in == null)
			return new TypedPointer(this.status.classloader.
				loadClass("java/lang/String"), 0);
		
		// Allocate new character array sequence
		TypedPointer casap = this.vmNew(
			"cc/squirreljme/runtime/cldc/string/CharArraySequence");
		
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
	 * Claims the static field space for this class.
	 *
	 * @param __cl The space to claim.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/18
	 */
	private final void __claimStaticFieldSpace(LoadedClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Do not claim twice!
		if (__cl._claimedsfspace)
			return;
		
		// If no space is needed by static then
		int spaceneeded = __cl.miniclass.header.sfbytes;
		if (spaceneeded > 0)
		{
			throw new todo.TODO();
			/*
			// Allocate
			int addr;
			__cl._startsfbytes = (addr = this.status.memory.
				allocateStaticSpace(spaceneeded));
			
			// Debug
			todo.DEBUG.note("Claimed static space for %s at %d", __cl, addr);
			*/
		}
		
		// Set as claimed!
		__cl._claimedsfspace = true;
	}
	
	/**
	 * Reads from a field.
	 *
	 * @param __src The class doing the loading.
	 * @param __f The field to access
	 * @return The field offset.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/18
	 */
	private final FieldOffset __getAccessedField(LoadedClass __src,
		AccessedField __f)
		throws NullPointerException
	{
		if (__src == null || __f == null)
			throw new NullPointerException("NARG");
		
		TaskStatus status = this.status;
		
		// Get the various parts
		FieldAccessTime ftime = __f.time();
		FieldAccessType ftype = __f.type();
		LoadedClass fclass = status.classloader.loadClass(
			__f.field().className());
		FieldNameAndType fnat = __f.field().memberNameAndType();
		
		// Locate the field first
		MinimizedField fmin = fclass.lookupField(
			ftype == FieldAccessType.STATIC, fnat);
		
		// Get field flags, determine if quickly volatile
		FieldFlags fflag = fmin.flags();
		boolean isvolatile = fflag.isVolatile();
		
		// {@squirreljme.error AE0e Cannot write to final field. (The source
		// class; The target class; The target field)}
		if (ftime == FieldAccessTime.NORMAL && fflag.isFinal())
			throw new VMIncompatibleClassChangeException(
				String.format("AE0e %s %s %s", __src, fclass, fnat));
		
		// Perform access checks
		if (__src != fclass)
		{
			// Are these in the same package?
			boolean samepkg = __src.miniclass.thisName().isInSamePackage(
				fclass.miniclass.thisName());
			
			// Get both flags
			ClassFlags acf = __src.miniclass.flags(),
				bcf = fclass.miniclass.flags();
			
			// {@squirreljme.error AE0f Cannot access other class because it
			// is package private and the source class is not in the same
			// package. (This class; The other class)}
			if (!samepkg && bcf.isPackagePrivate())
				throw new VMIncompatibleClassChangeException(
					String.format("AE0f %s %s", __src, fclass));
			
			// {@squirreljme.error AE0g Cannot access private field of
			// another class. (This class; The other class; The field)}
			else if (fflag.isPrivate())
				throw new VMIncompatibleClassChangeException(
					String.format("AE0g %s %s %s", __src, fclass, fnat));
			
			// {@squirreljme.error AE0h Cannot access package private field of
			// another class in another package. (This class; The other class;
			// The field)}
			else if (fflag.isPackagePrivate() && !samepkg)
				throw new VMIncompatibleClassChangeException(
					String.format("AE0h %s %s %s", __src, fclass, fnat));
			
			// {@squirreljme.error AE0i Cannot access protected field of
			// another class that is not a super class of this class.
			// (This class; The other class; The field)}
			else if (fflag.isProtected() &&
				(!samepkg && !__src.isSuperClass(fclass)))
				throw new VMIncompatibleClassChangeException(
					String.format("AE0i %s %s %s", __src, fclass, fnat));
		}
		
		// Return offset to field
		return new FieldOffset(isvolatile, fmin.offset);
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
			// Are these in the same package?
			boolean samepkg = __from.miniclass.thisName().isInSamePackage(
				mcl.miniclass.thisName());
			
			// Get both flags
			ClassFlags acf = __from.miniclass.flags(),
				bcf = mcl.miniclass.flags();
			
			// {@squirreljme.error AE06 Cannot access other class because it
			// is package private and the source class is not in the same
			// package. (This class; The other class)}
			if (!samepkg && bcf.isPackagePrivate())
				throw new VMIncompatibleClassChangeException(
					String.format("AE06 %s %s", __from, mcl));
			
			// Lookup static method handle
			StaticMethodHandle smh = (StaticMethodHandle)mcl.lookupMethod(
				MethodLookupType.STATIC, mty == InvokeType.STATIC, mnt);
			
			// Get the flags of the remote method
			MethodFlags omf = smh.minimethod.flags();
			
			// {@squirreljme.error AE08 Cannot access private method of
			// another class. (This class; The other class; The method)}
			if (omf.isPrivate())
				throw new VMIncompatibleClassChangeException(
					String.format("AE08 %s %s %s", __from, mcl, mnt));
			
			// {@squirreljme.error AE09 Cannot access package private method of
			// another class in another package. (This class; The other class;
			// The method)}
			else if (omf.isPackagePrivate() && !samepkg)
				throw new VMIncompatibleClassChangeException(
					String.format("AE09 %s %s %s", __from, mcl, mnt));
			
			// {@squirreljme.error AE0a Cannot access protected method of
			// another class that is not a super class of this class.
			// (This class; The other class; The method)}
			else if (omf.isProtected() &&
				(!samepkg && !__from.isSuperClass(mcl)))
				throw new VMIncompatibleClassChangeException(
					String.format("AE0a %s %s %s", __from, mcl, mnt));
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
			
				// Special calls are non-virtual and are used for private and
				// constructor calls. They also have some confusing logic as
				// to how they should be linked as well.
				// If the target class is a super class of this one and
				// the target method is not an instance initializer then we
				// will call the super method instead
			case SPECIAL:
				boolean superlu = (__from.isSuperClass(mcl) &&
					!mnt.name().isInstanceInitializer());
				return mcl.lookupMethod((superlu ? MethodLookupType.SUPER :
					MethodLookupType.STATIC), false, mnt);
				
			default:
				throw new todo.TODO(mty.name());
		}
	}
}

