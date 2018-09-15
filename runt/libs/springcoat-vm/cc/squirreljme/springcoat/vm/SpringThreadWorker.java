// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassFlags;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.ConstantValueString;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.MemberFlags;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.classfile.MethodReference;

/**
 * A worker which runs the actual thread code in single-step fashion.
 *
 * @since 2018/09/03
 */
public final class SpringThreadWorker
	implements Runnable
{
	/** The owning machine. */
	protected final SpringMachine machine;
	
	/** The thread being run. */
	protected final SpringThread thread;
	
	/**
	 * Initialize the worker.
	 *
	 * @param __m The executing machine.
	 * @param __t The running thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	public SpringThreadWorker(SpringMachine __m, SpringThread __t)
		throws NullPointerException
	{
		if (__m == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.machine = __m;
		this.thread = __t;
	}
	
	/**
	 * Allocates the memory needed to store an object of the given class.
	 *
	 * @param __cl The object to allocate.
	 * @return The allocated instance of the object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/08
	 */
	public final SpringObject allocateObject(SpringClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// The called constructor will allocate the space needed to store
		// this object
		return new SpringSimpleObject(__cl);
	}
	
	/**
	 * Checks if the given class may be accessed.
	 *
	 * @param __cl The class to access.
	 * @return {@code true} if it may be accessed.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/09
	 */
	public final boolean checkAccess(SpringClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// If the target class is public then we can access it
		ClassFlags targetflags = __cl.flags();
		if (targetflags.isPublic())
			return true;
		
		// Get our current class
		SpringClass self = this.contextClass();
		
		// This is ourself so access is always granted
		if (__cl == self)
			return true;
		
		// Protected class, we must be a super class
		else if (targetflags.isProtected())
		{
			for (SpringClass r = self; r != null; r = r.superClass())
				if (__cl == r)
					return true;
		}
		
		// Must be in the same package
		else if (targetflags.isPackagePrivate())
		{
			if (self.name().inPackage().equals(__cl.name().inPackage()))
				return true;
		}
		
		// This should not occur
		else
			throw new RuntimeException("OOPS");
		
		// No access permitted
		return false;
	}
	
	/**
	 * Checks if the given member can be accessed.
	 *
	 * @param __m The member to check.
	 * @return If the member can be accessed.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/09
	 */
	public final boolean checkAccess(SpringMember __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Need the current and the target class to check permissions
		SpringClass self = this.contextClass(),
			target = this.machine.classLoader().loadClass(__m.inClass());
		
		// If in the same class all access is permitted
		if (self == target)
			return true;
		
		// Public has full access
		MemberFlags flags = __m.flags();
		if (flags.isPublic())
			return true;
		
		// Protected class, we must be a super class
		else if (flags.isProtected())
		{
			for (SpringClass r = self; r != null; r = r.superClass())
				if (target == r)
					return true;
		}
		
		// Classes must be in the same package
		else if (flags.isPackagePrivate())
		{
			if (self.name().inPackage().equals(target.name().inPackage()))
				return true;
		}
		
		// Access not permitted
		return false;
	}
	
	/**
	 * Returns the current class context, if any.
	 *
	 * @return The current class context or {@code null} if there is none.
	 * @since 2018/09/09
	 */
	public final SpringClass contextClass()
	{
		SpringThread thread = this.thread;
		SpringThread.Frame frame = thread.currentFrame();
		
		// No frame exists
		if (frame == null)
			return null;
		
		// Otherwise lookup according to the method
		return this.machine.classLoader().loadClass(frame.method().inClass());
	}
	
	/**
	 * Loads the specified class, potentially performing initialization on it
	 * if it has not been initialized.
	 *
	 * @param __cn The class to load.
	 * @return The loaded class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/08
	 */
	public final SpringClass loadClass(ClassName __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Use the class loading lock to prevent other threads from loading or
		// initializing classes while this thread does such things
		SpringClassLoader classloader = this.machine.classLoader();
		synchronized (classloader.classLoadingLock())
		{
			// Load the class from the class loader
			return this.loadClass(classloader.loadClass(__cn));
		}
	}
	
	/**
	 * Loads the specified class.
	 *
	 * @param __cl The class to load.
	 * @return {@code __cl}.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/08
	 */
	public final SpringClass loadClass(SpringClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Use the class loading lock to prevent other threads from loading or
		// initializing classes while this thread does such things
		SpringMachine machine = this.machine;
		SpringClassLoader classloader = machine.classLoader();
		synchronized (classloader.classLoadingLock())
		{
			// If the class has already been initialized then the class is
			// ready to be used
			if (__cl.isInitialized())
				return __cl;
			
			// Debug
			todo.DEBUG.note("Need to initialize %s.", __cl.name());
			
			// Set the class as initialized early to prevent loops, because
			// a super class might call something from the base class which
			// might be seen as initialized when it should not be. So this is
			// to prevent bad things from happening.
			__cl.setInitialized();
			
			// Initialize the static field map
			Map<SpringField, SpringStaticField> sfm =
				machine.__staticFieldMap();
			for (SpringField f : __cl.fieldsOnlyThisClass())
				if (f.isStatic())
					sfm.put(f, new SpringStaticField(f));
			
			// Recursively call self to load the super class before this class
			// is handled
			SpringClass clsuper = __cl.superClass();
			if (clsuper != null)
				this.loadClass(clsuper);
			
			// Go through interfaces and do the same
			for (SpringClass iface : __cl.interfaceClasses())
				this.loadClass(iface);
			
			// Look for static constructor for this class to initialize it as
			// needed
			SpringMethod init;
			try
			{
				init = __cl.lookupMethod(true,
					new MethodNameAndType("<clinit>", "()V"));
			}
			
			// No static initializer exists
			catch (SpringNoSuchMethodException e)
			{
				init = null;
				
				// Debug
				todo.DEBUG.note("Class %s has no static initializer.",
					__cl.name());
			}
			
			// Static initializer exists, setup a frame and call it
			if (init != null)
			{
				// Stop execution when the initializer exits
				SpringThread thread = this.thread;
				int framelimit = thread.numFrames();
				
				// Enter the static initializer
				thread.enterFrame(init);
				
				// Execute until it finishes
				this.run(framelimit);
			}
		}
		
		// Return the input class
		return __cl;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/03
	 */
	@Override
	public final void run()
	{
		// Run until there are no frames left
		this.run(0);
	}
	
	/**
	 * Runs the worker with a limit on the lowest frame that may be reached
	 * when execution finishes. This is needed in some cases to invoke methods
	 * and static initializers in auxiliary code without needing complex state
	 * or otherwise to handle such things.
	 *
	 * @param __framelimit The current frame depth execution will stop at.
	 * @throws IllegalArgumentException If the frame limit is negative.
	 * @since 2018/09/08
	 */
	public final void run(int __framelimit)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BK0e Cannot have a negative frame limit. (The
		// frame limit)}
		if (__framelimit < 0)
			throw new IllegalArgumentException(String.format("BK0e %d",
				__framelimit));
		
		// The thread is alive as long as there are still frames of
		// execution
		SpringThread thread = this.thread;
		while (thread.numFrames() > __framelimit)
		{
			// Single step executing the top frame
			this.__singleStep();
		}
		
		// No more frames to run, so just stop execution
	}
	
	/**
	 * Looks up the specified static field and returns the storage for it.
	 *
	 * @param __f The field to lookup.
	 * @return The static field storage.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringIncompatibleClassChangeException If the target field is
	 * not static.
	 * @throws SpringNoSuchFieldException If the field does not exist.
	 * @since 2018/09/09
	 */
	private final SpringStaticField __lookupStaticField(FieldReference __f)
		throws NullPointerException, SpringIncompatibleClassChangeException,
			SpringNoSuchFieldException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BK0i Could not access the target class for
		// static field access. (The field reference)}
		SpringClass inclass = this.loadClass(__f.className());
		if (!this.checkAccess(inclass))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK0i %s", __f));
		
		// {@squirreljme.error BK0l Could not access the target field for
		// static field access. (The field reference)}
		SpringField field = inclass.lookupField(true, __f.memberNameAndType());
		if (!this.checkAccess(field))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK0l %s", __f));
		
		// Lookup the global static field
		return this.machine.lookupStaticField(field);
	}
	
	/**
	 * Single step through handling a single instruction.
	 *
	 * @since 2018/09/03
	 */
	private final void __singleStep()
	{
		// Need the current frame and its byte code
		SpringThread thread = this.thread;
		SpringThread.Frame frame = thread.currentFrame();
		ByteCode code = frame.byteCode();
		
		// Are these certain kinds of initializers? Because final fields are
		// writable during initialization accordingly
		SpringClass currentclass = this.contextClass();
		SpringMethod method = frame.method();
		boolean isstaticinit = method.isStaticInitializer(),
			isinstanceinit = method.isInstanceInitializer();
		
		// Determine the current instruction of execution
		int pc = frame.pc();
		Instruction inst = code.getByAddress(pc);
		
		// Debug
		todo.DEBUG.note("step(%s %s::%s) -> %s", thread.name(),
			method.inClass(), method.nameAndType(), inst);
		
		// Used to detect the next instruction of execution following this,
		// may be set accordingly in the frame manually
		int nextpc = code.addressFollowing(pc),
			orignextpc = nextpc;
		
		// Handle individual instructions
		int opid;
		try
		{
			// Handle it
			switch ((opid = inst.operation()))
			{
					// Do absolutely nothing!
				case InstructionIndex.NOP:
					break;
					
					// Push null reference
				case InstructionIndex.ACONST_NULL:
					frame.pushToStack(SpringNullObject.NULL);
					break;
					
					// Load reference from local variable
				case InstructionIndex.ALOAD_0:
				case InstructionIndex.ALOAD_1:
				case InstructionIndex.ALOAD_2:
				case InstructionIndex.ALOAD_3:
					{
						frame.loadToStack(SpringObject.class,
							opid - InstructionIndex.ALOAD_0);
					}
					break;
					
					// Store reference to local varibale
				case InstructionIndex.ASTORE_0:
				case InstructionIndex.ASTORE_1:
				case InstructionIndex.ASTORE_2:
				case InstructionIndex.ASTORE_3:
					{
						frame.storeLocal(opid - InstructionIndex.ASTORE_0,
							frame.<SpringObject>popFromStack(
								SpringObject.class));
					}
					break;
					
					// Duplicate top-most stack entry
				case InstructionIndex.DUP:
					{
						Object copy = frame.<Object>popFromStack(Object.class);
						
						// {@squirreljme.error BK0y Cannot duplicate category
						// two type.}
						if (copy instanceof Long || copy instanceof Double)
							throw new SpringVirtualMachineException("BK0y");
						
						// Push twice!
						frame.pushToStack(copy);
						frame.pushToStack(copy);
					}
					break;
					
					// Read static variable
				case InstructionIndex.GETSTATIC:
					{
						// Lookup field
						SpringStaticField ssf = this.__lookupStaticField(
							inst.<FieldReference>argument(0,
							FieldReference.class));
						
						// Push read value to stack
						frame.pushToStack(ssf.get());
					}
					break;
					
					// Go to address
				case InstructionIndex.GOTO:
				case InstructionIndex.GOTO_W:
					nextpc = inst.<InstructionJumpTarget>argument(0,
						InstructionJumpTarget.class).target();
					break;
					
					// Integer constant
				case InstructionIndex.ICONST_M1:
				case InstructionIndex.ICONST_0:
				case InstructionIndex.ICONST_1:
				case InstructionIndex.ICONST_2:
				case InstructionIndex.ICONST_3:
				case InstructionIndex.ICONST_4:
				case InstructionIndex.ICONST_5:
					frame.pushToStack(Integer.valueOf(
						-1 + (opid - InstructionIndex.ICONST_M1)));
					break;
					
					// If reference is not null
				case InstructionIndex.IFNONNULL:
					{
						SpringObject a = frame.<SpringObject>popFromStack(
							SpringObject.class);
						if (a != SpringNullObject.NULL)
							nextpc = inst.<InstructionJumpTarget>argument(0,
								InstructionJumpTarget.class).target();
					}
					break;
					
					// If reference is null
				case InstructionIndex.IFNULL:
					{
						SpringObject a = frame.<SpringObject>popFromStack(
							SpringObject.class);
						if (a == SpringNullObject.NULL)
							nextpc = inst.<InstructionJumpTarget>argument(0,
								InstructionJumpTarget.class).target();
					}
					break;
					
					// Load integer from local variable
				case InstructionIndex.ILOAD_0:
				case InstructionIndex.ILOAD_1:
				case InstructionIndex.ILOAD_2:
				case InstructionIndex.ILOAD_3:
					{
						frame.loadToStack(Integer.class,
							opid - InstructionIndex.ILOAD_0);
					}
					break;
					
					// Invoke special method (constructor, superclass,
					// or private)
				case InstructionIndex.INVOKESPECIAL:
					this.__vmInvokeSpecial(inst, thread, frame);
					break;
					
					// Invoke static method
				case InstructionIndex.INVOKESTATIC:
					this.__vmInvokeStatic(inst, thread, frame);
					break;
					
					// Load from constant pool, push to the stack
				case InstructionIndex.LDC:
					{
						ConstantValue value = inst.<ConstantValue>argument(0,
							ConstantValue.class);
						
						// Pushing a string, which due to the rules of Java
						// there must always be an equality (==) between two
						// strings, so "foo" == "foo" must be true even if it
						// is in different parts of the code
						if (value instanceof ConstantValueString)
							throw new todo.TODO();
						
						// This will be pre-boxed so push it to the stack
						else
							frame.pushToStack(value.boxedValue());
					}
					break;
					
					// Enter monitor
				case InstructionIndex.MONITORENTER:
					frame.<SpringObject>popFromStack(SpringObject.class).
						monitor().enter(thread);
					break;
					
					// Exit monitor
				case InstructionIndex.MONITOREXIT:
					frame.<SpringObject>popFromStack(SpringObject.class).
						monitor().exit(thread);
					break;
					
					// Allocate new object
				case InstructionIndex.NEW:
					this.__vmNew(inst, frame);
					break;
					
					// Return from method with no return value
				case InstructionIndex.RETURN:
					thread.popFrame();
					break;
				
					// Put to static field
				case InstructionIndex.PUTSTATIC:
					{
						// Lookup field
						SpringStaticField ssf = this.__lookupStaticField(
							inst.<FieldReference>argument(0,
							FieldReference.class));
						
						// Set value, note that static initializers can set
						// static field values even if they are final
						ssf.set(frame.popFromStack(), isstaticinit);
					}
					break;
				
					// {@squirreljme.error BK10 Reserved instruction. (The
					// instruction)}
				case InstructionIndex.BREAKPOINT:
				case InstructionIndex.IMPDEP1:
				case InstructionIndex.IMPDEP2:
					throw new SpringVirtualMachineException(String.format(
						"BK10 %s", inst));
					
					// {@squirreljme.error BK0a Unimplemented operation.
					// (The instruction)}
				default:
					throw new SpringVirtualMachineException(String.format(
						"BK0a %s", inst));
			}
		}
		
		// Use the original exception, just add a suppression note on it since
		// that is the simplest action
		catch (RuntimeException e)
		{
			// Where is this located?
			SpringMethod inmethod = frame.method();
			ClassName inclassname = inmethod.inClass();
			SpringClass inclass = machine.classLoader().loadClass(
				inclassname);
			
			// Location information if debugging is used, this makes it easier
			// to see exactly where failed code happened
			String onfile = inclass.file().sourceFile();
			int online = code.lineOfAddress(pc);
			
			// {@squirreljme.error BK0d An exception was thrown in the virtual
			// machine while executing the specified location. (The class;
			// The method; The program counter; The file in source code,
			// null means it is unknown; The line in source code, negative
			// values are unknown; The instruction)}
			e.addSuppressed(new SpringVirtualMachineException(
				String.format("BK0d %s %s %d %s %d %s", inclassname,
				inmethod.nameAndType(), pc, onfile, online, inst)));
			throw e;
		}
		
		// Set implicit next PC address, if it has not been set or the next
		// address was actually changed
		if (nextpc != orignextpc || pc == frame.pc())
		{
			frame.setPc(nextpc);
			
			// Debug
			todo.DEBUG.note("Next PC %d", nextpc);
		}
	}
	
	/**
	 * Performs a special invoke.
	 *
	 * @param __i The instruction.
	 * @param __t The current thread.
	 * @param __f The current frame.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	private final void __vmInvokeSpecial(Instruction __i, SpringThread __t,
		SpringThread.Frame __f)
		throws NullPointerException
	{
		if (__i == null || __t == null || __f == null)
			throw new NullPointerException("NARG");
		
		MethodReference ref = __i.<MethodReference>argument(
			0, MethodReference.class);
		
		// Resolve the method reference
		SpringClass refclass = this.loadClass(ref.className());
		SpringMethod refmethod = refclass.lookupMethod(false,
			ref.memberNameAndType());
		
		// {@squirreljme.error BK0t Could not access the target
		// method for special invoke. (The target method)}
		if (!this.checkAccess(refmethod))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK0t %s", ref));
		
		// The new class the method is in
		SpringClass newclass = this.loadClass(
			refmethod.inClass());
		
		// Load arguments
		int nargs = refmethod.nameAndType().type().
			argumentCount() + 1;
		Object[] args = new Object[nargs];
		for (int i = nargs - 1; i >= 0; i--)
			args[i] = __f.popFromStack();
		
		// Invoke this method specially
		if (newclass.isSuperClass(refclass) &&
			!refmethod.name().isInstanceInitializer())
		{
			throw new todo.TODO();
		}
		
		// Invoke virtual instead
		else
		{
			__t.enterFrame(refmethod, args);
		}
	}
	
	/**
	 * Performs a static invoke.
	 *
	 * @param __i The instruction.
	 * @param __t The current thread.
	 * @param __f The current frame.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	private final void __vmInvokeStatic(Instruction __i, SpringThread __t,
		SpringThread.Frame __f)
		throws NullPointerException
	{
		if (__i == null || __t == null || __f == null)
			throw new NullPointerException("NARG");
		
		MethodReference ref = __i.<MethodReference>argument(
			0, MethodReference.class);
		
		// Resolve the method reference
		SpringClass refclass = this.loadClass(ref.className());
		SpringMethod refmethod = refclass.lookupMethod(true,
			ref.memberNameAndType());
		
		// {@squirreljme.error BK0w Could not access the target
		// method for static invoke. (The target method)}
		if (!this.checkAccess(refmethod))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK0w %s", ref));
		
		// Load arguments
		int nargs = refmethod.nameAndType().type().
			argumentCount();
		Object[] args = new Object[nargs];
		for (int i = nargs - 1; i >= 0; i--)
			args[i] = __f.popFromStack();
		
		// Enter frame for static method
		__t.enterFrame(refmethod, args);
	}
	
	/**
	 * Allocates a new instance of the given object, it is not initialized just
	 * allocated.
	 *
	 * @param __i The instruction.
	 * @param __f The current frame.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	private final void __vmNew(Instruction __i, SpringThread.Frame __f)
		throws NullPointerException
	{
		if (__i == null || __f == null)
			throw new NullPointerException("NARG");
		
		// Lookup class we want to allocate
		ClassName allocname;
		SpringClass toalloc = this.loadClass((allocname =
			__i.<ClassName>argument(0, ClassName.class)));
		
		// {@squirreljme.error BK0x Cannot allocate an instance of the given
		// class because it cannot be accessed. (The class to allocate)}
		if (!this.checkAccess(toalloc))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK0x %s", allocname));
		
		// Push a new allocation to the stack
		__f.pushToStack(this.allocateObject(toalloc));
	}
}

