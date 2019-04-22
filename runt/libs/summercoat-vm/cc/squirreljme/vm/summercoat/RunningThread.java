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
import dev.shadowtail.classfile.nncc.ArgumentFormat;
import dev.shadowtail.classfile.nncc.FieldAccessTime;
import dev.shadowtail.classfile.nncc.FieldAccessType;
import dev.shadowtail.classfile.nncc.InvokedMethod;
import dev.shadowtail.classfile.nncc.NativeCode;
import dev.shadowtail.classfile.nncc.NativeInstruction;
import dev.shadowtail.classfile.nncc.NativeInstructionType;
import dev.shadowtail.classfile.xlate.CompareType;
import dev.shadowtail.classfile.xlate.DataType;
import dev.shadowtail.classfile.xlate.InvokeType;
import dev.shadowtail.classfile.xlate.MathType;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.LinkedList;
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
import net.multiphasicapps.collections.IntegerList;
import net.multiphasicapps.profiler.ProfiledThread;
import net.multiphasicapps.profiler.ProfilerSnapshot;

/**
 * This represents a thread which is running in the virtual machine.
 *
 * This class implements thread itself so it may be interrupted as needed and
 * such.
 *
 * @since 2019/01/05
 */
@Deprecated
public final class RunningThread
	extends Thread
{
	/** The ID of this thread. */
	protected final int id;
	
	/** The task status this reports on. */
	protected final TaskStatus status;
	
	/** The profiler for this thread. */
	protected final ProfiledThread profiler;
	
	/** Thread frames of what is being run. */
	private final LinkedList<ThreadFrame> _frames =
		new LinkedList<>();
	
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
		
		// Create profiled thread for this
		ProfilerSnapshot psnap = __s.profiler;
		this.profiler = (psnap == null ? null :
			psnap.measureThread(String.format("Task:%d[%s],%d",
				__s.id, __s.name, __id)));
	}
	
	/**
	 * Sets up the thread so that the given method is entered from this
	 * thread, it is not started. This version is faster as the arguments
	 * are all integers.
	 *
	 * @param __mh The method handle.
	 * @param __args The method arguments.
	 * @return The index of the added frame.
	 * @throws IllegalStateException If the thread has been started and this
	 * is not the current thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public ThreadFrame execEnterMethod(MethodHandle __mh, int... __args)
		throws IllegalStateException, NullPointerException
	{
		if (__mh == null)
			throw new NullPointerException("NARG");
		
		// Must be the same thread
		this.__checkSameThread();
		
		// Defensive copy
		__args = (__args == null ? new int[0] : __args.clone());
		
		// Need to resolve the method handle for the given pointer's class
		// Note that if the method handle is already a method handle then that
		// resolver will do nothing
		int iptr = (__args.length >= 1 ? __args[0] : 0);
		TaskStatus status = this.status;
		StaticMethodHandle smh = __mh.resolve(
			status.memory.readClassOptional(iptr));
		
		// Debug
		todo.DEBUG.note("Enter %s::%s:%s", smh.inclass,
			smh.inmethodname, smh.inmethodtype);
		
		// If the profiler is available, enter the frame so we do get to
		// profile it
		ProfiledThread profiler = this.profiler;
		if (profiler != null)
			profiler.enterFrame(smh.inclass.toString(),
				smh.inmethodname.toString(), smh.inmethodtype.toString(),
				System.nanoTime());
		
		// Get this frame index, this will be used by code that is just
		// executing the current frame until the frame has exited
		LinkedList<ThreadFrame> frames = this._frames;
		int framedx = frames.size();
		
		// Setup basic frame
		ThreadFrame nf = new ThreadFrame(smh.runpool, smh._code, framedx);
		
		// Load arguments into the argument registers
		int[] ra = nf.registers;
		for (int i = 0, n = __args.length, o = NativeCode.LOCAL_REGISTER_BASE;
			i < n; i++, o++)
			ra[o] = __args[i];
		
		throw new todo.TODO();
		/*
		// Copy global registers from the previous frame
		ThreadFrame prev = frames.peekLast();
		if (prev != null)
		{
			int[] prevra = prev.registers;
			
			ra[NativeCode.STATIC_FIELD_REGISTER] =
				prevra[NativeCode.STATIC_FIELD_REGISTER];
			ra[NativeCode.UNUSED_REGISTER] =
				prevra[NativeCode.UNUSED_REGISTER];
		}
		
		// Otherwise do an initial seed of them
		else
		{
			ra[NativeCode.STATIC_FIELD_REGISTER] = status.memory.staticfptr;
			ra[NativeCode.UNUSED_REGISTER] = 0;
		}
		
		// Register this frame
		frames.addLast(nf);
		
		// And return the frame
		return nf;
		*/
	}
	
	/**
	 * Sets up the thread so that the given method is entered from this
	 * thread, it is not started.
	 *
	 * @param __mh The method handle.
	 * @param __args The method arguments.
	 * @return The index of the added frame.
	 * @throws IllegalArgumentException If the argument is not valid.
	 * @throws IllegalStateException If the thread has been started and this
	 * is not the current thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public ThreadFrame execEnterMethod(MethodHandle __mh, Object... __args)
		throws IllegalStateException, NullPointerException
	{
		if (__mh == null)
			throw new NullPointerException("NARG");
		
		// Need to convert arguments to integers since everything is based
		// on registers, this ends up being far faster for calls accordingly
		// within the VM since everything is purely register based
		int[] iargs = this.argumentConvertToInt(__args);
		
		// Use the fast version, trim array accordingly
		return this.execEnterMethod(__mh, iargs);
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
		boolean didstart = this.__checkSameThread();
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
			// Setup thread frame for method handle and execute the frames
			// until its termination point is reached
			ThreadFrame frame = this.execEnterMethod(__mh, __args);
			this.__doExecution(frame.index);
			
			// Build return value from the locals
			int[] lrs = frame.registers;
			return ((((long)lrs[NativeCode.RETURN_REGISTER]) << 32L) |
				(lrs[NativeCode.RETURN_REGISTER + 1] & 0xFFFFFFFFL));
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
	public final void vmArraySet(TypedPointer __tp, int __dx, Object __v)
		throws NullPointerException
	{
		if (__tp == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Must be the same thread
		this.__checkSameThread();
		
		// Data type used for the array and component area size
		DataType dt = __tp.type.componentclass.miniclass.header.datatype;
		
		// Determine the address to write to
		int ptr = __tp.pointer + 12 + (__dx * dt.size());
		
		// Get integer form of the data
		int[] nt = RunningThread.argumentConvertToInt(__v);
		
		// Write according to the data type
		MemorySpace memory = this.status.memory;
		switch (dt)
		{
			case BYTE:
				throw new todo.TODO();
			
			case SHORT:
			case CHARACTER:
				memory.memWriteShort(false, ptr, (short)nt[0]);
				break;
			
			case INTEGER:
			case FLOAT:
			case OBJECT:
				memory.memWriteInt(false, ptr, nt[0]);
				break;
			
			case LONG:
			case DOUBLE:
				throw new todo.TODO();
			
			default:
				throw new todo.OOPS();
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
			
			// The pool is now realized!
			rpool._realized = rzp;
			
			// Initialize static fields with a constant value
			int startsfbytes = __cl._startsfbytes;
			MemorySpace memory = status.memory;
			for (MinimizedField ff : __cl.miniclass.fields(true))
				if (ff.flags().isStatic() && ff.value != null)
				{
					// Make intern string from it?
					Object usev;
					if (ff.value instanceof String)
						throw new todo.TODO();
					
					// Otherwise use given value
					else
						usev = ff.value;
					
					// Convert
					int[] vs = RunningThread.argumentConvertToInt(usev);
					
					// Set
					switch (ff.datatype)
					{
						case BYTE:
							throw new todo.TODO();
						
						case SHORT:
						case CHARACTER:
							memory.memWriteShort(false,
								startsfbytes + ff.offset, (short)vs[0]);
							break;
						
						case INTEGER:
						case FLOAT:
						case OBJECT:
							memory.memWriteInt(false,
								startsfbytes + ff.offset, vs[0]);
							break;
						
						case LONG:
						case DOUBLE:
							throw new todo.TODO();
						
						default:
							throw new todo.OOPS(ff.datatype.name());
					}
				}
			
			// Execute static constructor
			MethodHandle staticinit = __cl.staticinit;
			if (staticinit != null)
				this.runMethod(staticinit);
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
		
		// Get class loader
		ClassLoader classloader = this.status.classloader;
		
		throw new todo.TODO();
		/*
		// Translate and build interned type
		return new TypedPointer(classloader.loadClass("java/lang/String"),
			this.runMethod(classloader.loadClass(
			"cc.squirreljme.runtime.cldc.lang.StringIntern")
		long rv = this.runMethod(this.status.classloader.loadClass(__cl),
			this.vmTranslateString(__s)
		
		this.vmTranslateString(__s))
		
		throw new todo.TODO();
		*/
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
		
		// Determine the number of bytes to allocate
		int allocsz = __cl.totalifbytes;
		boolean isarray;
		if ((isarray = __cl.isArray()))
			allocsz += (__len * __cl.miniclass.header.datatype.size());
		
		// Allocate object
		MemorySpace memory = this.status.memory;
		int ptr = memory.allocate(true, allocsz);
		
		// Write the class ID and the initial count of 1 (so the object is
		// not just garbage collected out of nowhere
		memory.memWriteInt(true, ptr, memory.registerClass(__cl));
		memory.memWriteInt(true, ptr + 4, 1);
		
		// Write the length of the array as well
		if (isarray)
			memory.memWriteInt(true, ptr + 8, __len);
		
		// Allocate and return the typed pointer
		return new TypedPointer(__cl, ptr);
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
		
		// Allocate new object before we call the constructor on it
		TypedPointer rv = this.vmNew(__cl);
		
		// The arguments to the call must get the instance put in there so that
		// the constructor actually works
		int nargs = __args.length;
		Object[] xargs = new Object[nargs + 1];
		for (int i = 0, o = 1; i < nargs; i++, o++)
			xargs[i] = __args[i];
		xargs[0] = rv;
		
		// Execute constructor
		this.runMethod(__cl.lookupMethod(MethodLookupType.STATIC, false,
			new MethodNameAndType("<init>", __desc)), xargs);
		
		// Return the initialized value
		return rv;
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
		return this.vmNewInstance(
			"cc/squirreljme/runtime/cldc/lang/BasicStaticMethod", "(I)V",
			this.status.memory.registerHandle(__mh));
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
		
		// Create character array
		int strlen = __in.length();
		TypedPointer cha = this.vmNewArray("[C", strlen);
		
		// Store into the array
		for (int i = 0; i < strlen; i++)
			this.vmArraySet(cha, i, __in.charAt(i));
		
		// Create string with this character array
		return this.vmNewInstance("java/lang/String", "([C)V", cha);
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
			// Allocate
			int addr;
			__cl._startsfbytes = (addr = this.status.memory.
				allocateStaticSpace(spaceneeded));
			
			// Debug
			todo.DEBUG.note("Claimed static space for %s at %d", __cl, addr);
		}
		
		// Set as claimed!
		__cl._claimedsfspace = true;
	}
	
	/**
	 * Performs actual execution of the engine.
	 *
	 * @param __fl The frame limit, once the frame count drops below this
	 * amount execution will terminate.
	 * @since 2019/04/19
	 */
	private final void __doExecution(int __fl)
	{
		// This is set when all the various parameters should be reloaded
		// and initialized
		boolean reload = true;
		
		// Used for memory access
		final MemorySpace memory = this.status.memory;
		
		// Instruction arguments
		final int[] args = new int[6];
		final long[] largs = new long[6];
		final ProfiledThread profiler = this.profiler;

		// Used per frame
		int[] lrs = null,
			refq = null;
		ThreadFrame nowframe = null;
		RuntimeConstantPool pool = null;
		byte[] code = null;
		int pc = 0,
			refp = 0;
		
		// Execution is effectively an infinite loop
		LinkedList<ThreadFrame> frames = this._frames;
		for (int frameat = frames.size(), lastframe = -1; frameat >= __fl;
			frameat = frames.size())
		{
			// Reload parameters?
			if ((reload |= (lastframe != frameat)))
			{
				// Before dumping this frame, store old info
				if (nowframe != null)
				{
					nowframe.pc = pc;
					nowframe.refp = refp;
				}
				
				// Get current frame, stop execution if there is nothing
				// left to execute
				nowframe = frames.peekLast();
				if (nowframe == null)
					return;
				
				// Load stuff needed for execution
				lrs = nowframe.registers;
				refq = nowframe.refq;
				pool = nowframe.pool;
				code = nowframe.code;
				pc = nowframe.pc;
				refp = nowframe.refp;
				
				// Used to auto-detect frame change
				lastframe = frameat;
				
				// No longer reload information
				reload = false;
			}
			
			// Instruction to execute
			int op = (code[pc] & 0xFF);
			
			// Reset the value of the zero register, it is always zero
			lrs[0] = 0;
			
			// Clear arguments, just in case
			for (int i = 0, n = args.length; i < n; i++)
			{
				args[i] = 0;
				largs[i] = 0;
			}
			
			// Register list, just one is used everywhere
			int[] reglist = null;
			
			// Load arguments for this instruction
			ArgumentFormat[] af = NativeInstruction.argumentFormat(op);
			int rargp = pc + 1;
			for (int i = 0, n = af.length; i < n; i++)
				switch (af[i])
				{
					// Variable sized entries, may be pool values
					case VUINT:
					case VPOOL:
					case VJUMP:
						{
							// Long value?
							int base = (code[rargp++] & 0xFF);
							if ((base & 0x80) != 0)
							{
								base = ((base & 0x7F) << 8);
								base |= (code[rargp++] & 0xFF);
							}
							
							// Set
							args[i] = base;
						}
						break;
					
					// Register list.
					case REGLIST:
						{
							// Wide
							int count = (code[rargp++] & 0xFF);
							if ((count & 0x80) != 0)
							{
								count = ((count & 0x7F) << 8) |
									(code[rargp++] & 0xFF);
								
								// Read values
								reglist = new int[count];
								for (int r = 0; r < count; r++)
									reglist[r] =
										((code[rargp++] & 0xFF) << 8) |
										(code[rargp++] & 0xFF);
							}
							// Narrow
							else
							{
								reglist = new int[count];
								
								// Read values
								for (int r = 0; r < count; r++)
									reglist[r] = (code[rargp++] & 0xFF);
							}
						}
						break;
					
					// 32-bit integer/float
					case INT32:
					case FLOAT32:
						args[i] = ((code[rargp++] & 0xFF) << 24) |
							((code[rargp++] & 0xFF) << 16) |
							((code[rargp++] & 0xFF) << 8) |
							((code[rargp++] & 0xFF));
						break;
					
					// 64-bit long/double
					case INT64:
					case FLOAT64:
						largs[i] = ((code[rargp++] & 0xFFL) << 56L) |
							((code[rargp++] & 0xFFL) << 48L) |
							((code[rargp++] & 0xFFL) << 40L) |
							((code[rargp++] & 0xFFL) << 32L) |
							((code[rargp++] & 0xFFL) << 24L) |
							((code[rargp++] & 0xFFL) << 16L) |
							((code[rargp++] & 0xFFL) << 8L) |
							((code[rargp++] & 0xFFL));
						break;
					
					default:
						throw new todo.OOPS(af[i].name());
				}
			
			// Debug
			todo.DEBUG.note("exec @%d %s %s", pc,
				NativeInstruction.mnemonic(op), new IntegerList(args));
			
			// By default the next instruction is the address after all
			// arguments have been read
			int nextpc = rargp;
			
			// Handle operations byte by byte, depending on the encoding
			switch (NativeInstruction.encoding(op))
			{
					// Entry argument
				case NativeInstructionType.ENTRY_MARKER:
					break;
					
					// Compare integers and possibly jump
				case NativeInstructionType.IF_ICMP:
					{
						// Parts
						int a = lrs[args[0]],
							b = lrs[args[1]];
						
						// Compare
						boolean branch;
						switch (CompareType.of(op & 0b111))
						{
							case EQUALS:
								branch = (a == b); break;
							case NOT_EQUALS:
								branch = (a != b); break;
							case LESS_THAN:
								branch = (a < b); break;
							case LESS_THAN_OR_EQUALS:
								branch = (a <= b); break;
							case GREATER_THAN:
								branch = (a > b); break;
							case GREATER_THAN_OR_EQUALS:
								branch = (a >= b); break;
							case TRUE:
								branch = true; break;
							case FALSE:
								branch = false; break;
							
							default:
								throw new todo.OOPS();
						}
						
						// Branching?
						if (branch)
						{
							// Refclear?
							if (refp != 0 && ((op & 0x08) != 0))
								throw new todo.TODO();
							
							// Go to the given address
							nextpc = args[2];
						}
					}
					break;
					
					// Check class type
				case NativeInstructionType.IFNOTCLASS:
				case NativeInstructionType.IFNOTCLASS_REF_CLEAR:
				case NativeInstructionType.IFCLASS:
				case NativeInstructionType.IFCLASS_REF_CLEAR:
					{
						// How are we comparing?
						boolean not = ((op & 0b01) != 0),
							refcl = ((op & 0b10) != 0);
						
						// Get the object pointer and try to read the class
						// from it
						LoadedClass ocl = memory.readClassOptional(
								lrs[args[1]]),
							want = (LoadedClass)pool.get(args[0]);
						
						// Is this assignable?
						boolean assignable = (ocl != null && want != null ?
							want.isAssignableFrom(ocl) : false);
						
						// Debug
						todo.DEBUG.note("Object %s instanceof %s = %b, " +
							"not=%b", ocl, want, assignable, not);
						
						// Do we branch?
						boolean branch = (not ? !assignable : assignable);
						
						// Branching?
						if (branch)
						{
							// Refclear?
							if (refp != 0 && refcl)
								throw new todo.TODO();
							
							// Go to the given address
							nextpc = args[2];
						}
					}
					break;
					
					// Invoke method
				case NativeInstructionType.INVOKE:
					{
						// Get method handle to execute
						MethodHandle mh = (MethodHandle)pool.get(args[0]);
						
						// Load values from registers
						int nr = reglist.length;
						int[] vals = new int[nr];
						for (int r = 0; r < nr; r++)
							vals[r] = lrs[reglist[r]];
						
						// Enter invoked method
						this.execEnterMethod(mh, vals);
						
						// We are invoking so, the frame will be reloaded and
						// execution continues in this method loop
						reload = true;
					}
					break;
					
					// Load value from pool
				case NativeInstructionType.LOAD_POOL:
					{
						// Get value to load
						Object pv = pool.get(args[0]);
						
						// Debug
						todo.DEBUG.note("LoadPool %s", pv);
						
						// Get integer value to use
						int use;
						if (pv instanceof FieldOffset)
							use = ((FieldOffset)pv).offset;
						
						// String, which needs to be interned
						else if (pv instanceof String)
							use = this.vmInternString((String)pv).pointer;
						
						// Should not occur
						else
							throw new todo.OOPS(pv.getClass().getName());
						
						// Set value
						lrs[args[1]] = use;
					}
					break;
				
					// Integer register math
				case NativeInstructionType.MATH_CONST_INT:
				case NativeInstructionType.MATH_REG_INT:
					{
						// Parts
						int a = lrs[args[0]],
							b = (((op & 0x80) != 0) ? args[1] : lrs[args[1]]),
							c;
						
						// Operation to execute
						switch (MathType.of(op & 0xF))
						{
							case ADD:		c = a + b; break;
							case SUB:		c = a - b; break;
							case MUL:		c = a * b; break;
							case DIV:		c = a / b; break;
							case REM:		c = a % b; break;
							case NEG:		c = -a; break;
							case SHL:		c = a << b; break;
							case SHR:		c = a >> b; break;
							case USHR:		c = a >>> b; break;
							case AND:		c = a & b; break;
							case OR:		c = a | b; break;
							case XOR:		c = a ^ b; break;
							case SIGN_X8:	c = (byte)a; break;
							case SIGN_HALF:	c = (short)a; break;
							
							case CMPL:
							case CMPG:
								c = (a < b ? -1 : (a == b ? 0 : 1));
								break;
							
							default:
								throw new todo.OOPS();
						}
						
						// Set result
						lrs[args[2]] = c;
					}
					break;
				
					// Memory offset via register (not wide)
				case NativeInstructionType.MEMORY_OFF_REG:
					{
						// Determine if access is volatile
						int off = lrs[args[2]];
						boolean vol = ((off >>> 31) != (off >>> 30));
						if (vol)
							off ^= NativeCode.MEMORY_OFF_VOLATILE_BIT;
						
						// The pointer to access
						int ptr = lrs[args[1]] + off,
							vop = args[0];
						
						// Load
						if ((op & 0b1000) != 0)
						{
							throw new todo.TODO();
						}
						
						// Store
						else
						{
							int v = lrs[vop];
							switch (DataType.of(op & 0x7))
							{
								case OBJECT:
								case INTEGER:
								case FLOAT:
									memory.memWriteInt(vol, ptr, v);
									break;
								
								default:
									throw new todo.OOPS();
							}
						}
					}
					break;
				
					// New instance of object
				case NativeInstructionType.NEW:
					lrs[args[1]] = this.vmNew((LoadedClass)pool.get(args[0])).
						pointer;
					break;
					
					// Return from call
				case NativeInstructionType.RETURN:
					{
						// Exit the current frame
						if (profiler != null)
							profiler.exitFrame(System.nanoTime());
						
						// Remove the last frame
						ThreadFrame pop = frames.removeLast();
						
						// If we are landing on another frame, copy our
						// globals into that frame
						ThreadFrame now = frames.peekLast();
						if (now != null)
						{
							int[] from = pop.registers,
								to = now.registers;
							
							// Copy up to the base, skip the zero register
							for (int i = 1; i < NativeCode.LOCAL_REGISTER_BASE;
								i++)
								to[i] = from[i];
						}
						
						// Old state will be getting destroyed
						reload = true;
					}
					break;
					
					// Unknown operation
				default:
					throw new todo.OOPS(NativeInstruction.mnemonic(op));
			}
			
			// Go to next address
			pc = nextpc;
		}
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
		boolean iss = (ftype == FieldAccessType.STATIC);
		MinimizedField fmin = fclass.lookupField(iss, fnat);
		
		// Make sure the static field space for the target class is claimed
		// so we can actually locate the correct field!
		if (iss && !fclass._claimedsfspace)
			this.__claimStaticFieldSpace(fclass);
		
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
		return new FieldOffset(isvolatile,
			(iss ? fclass._startsfbytes : fclass.startifbytes) + fmin.offset);
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
		
		// Debug
		/*todo.DEBUG.note("From %s : %s", __from, __m);*/
		
		// Get all the various parts
		InvokeType mty = __m.type();
		LoadedClass mcl = this.status.classloader.loadClass(
			__m.handle().outerClass());
		MethodNameAndType mnt = __m.handle().nameAndType();
		
		// Calling method in another class, need to check access
		// Ignore for interfaces
		if (__from != mcl && mty != InvokeType.INTERFACE)
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
				if (superlu)
					return __from.superclass.lookupMethod(
						MethodLookupType.SUPER, false, mnt);
				else
					return mcl.lookupMethod(
						MethodLookupType.STATIC, false, mnt);
				
			default:
				throw new todo.TODO(mty.name());
		}
	}
	
	/**
	 * Converts input arguments to an array of integer values.
	 *
	 * @param __args The arguments to convert.
	 * @return The resulting integer value.
	 * @throws IllegalArgumentException If it cannot be converted.
	 * @since 2019/04/20
	 */
	public static final int[] argumentConvertToInt(Object... __args)
		throws IllegalArgumentException
	{
		if (__args == null || __args.length == 0)
			return new int[0];
		
		// Convert
		int n = __args.length,
			o = 0;
		int[] iargs = new int[n * 2];
		for (int i = 0; i < n; i++)
		{
			Object a = __args[i];
			
			// Convert argument accordingly
			if (a == null)
				iargs[o++] = 0;
			else if (a instanceof TypedPointer)
				iargs[o++] = ((TypedPointer)a).pointer;
			else if (a instanceof Character)
				iargs[o++] = ((Character)a).charValue();
			else if (a instanceof Integer)
				iargs[o++] = ((Integer)a).intValue();
			else if (a instanceof Float)
				iargs[o++] = Float.floatToRawIntBits((Float)a);
			else if (a instanceof Long)
			{
				long l = ((Long)a).longValue();
				iargs[o++] = (int)(l >>> 32);
				iargs[o++] = (int)l;
			}
			else if (a instanceof Double)
			{
				long l = Double.doubleToRawLongBits((Double)a);
				iargs[o++] = (int)(l >>> 32);
				iargs[o++] = (int)l;
			}
			
			// {@squirreljme.error AE0o Do not know how to convert value of
			// the given type to an integer form. (The value; The class type)}
			else
				throw new IllegalArgumentException(
					String.format("AE0o %s %s", a, a.getClass()));
		}
		
		// Return snipped array
		return (o == iargs.length ? iargs : Arrays.copyOf(iargs, o));
	}
}

