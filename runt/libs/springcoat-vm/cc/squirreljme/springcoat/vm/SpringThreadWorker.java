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

import cc.squirreljme.runtime.cldc.asm.ConsoleOutput;
import cc.squirreljme.runtime.cldc.asm.DebugAccess;
import java.io.PrintStream;
import java.util.Formatter;
import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassFlags;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.ConstantValueClass;
import net.multiphasicapps.classfile.ConstantValueString;
import net.multiphasicapps.classfile.ExceptionHandler;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.IntMatchingJumpTable;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.MemberFlags;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.classfile.MethodReference;
import net.multiphasicapps.classfile.PrimitiveType;

/**
 * A worker which runs the actual thread code in single-step fashion.
 *
 * @since 2018/09/03
 */
public final class SpringThreadWorker
	implements Runnable
{
	/** Number of instructions which can be executed before warning. */
	private static final int _EXECUTION_THRESHOLD =
		200000;
	
	/** The owning machine. */
	protected final SpringMachine machine;
	
	/** The thread being run. */
	protected final SpringThread thread;
	
	/** The current step count. */
	private volatile int _stepcount;
	
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
	 * Allocates the memory needed to store an array of the given class and
	 * of the given length.
	 *
	 * @param __cl The component type.
	 * @param __l The length of the array.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNegativeArraySizeException If the array size is negative.
	 * @since 2018/09/15
	 */
	public final SpringArrayObject allocateArray(SpringClass __cl, int __l)
		throws NullPointerException, SpringNegativeArraySizeException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return new SpringArrayObject(
			this.resolveClass(__cl.name().addDimensions(1)), __cl, __l);
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
	 * Converts the specified virtual machine object to a native object.
	 *
	 * @param __in The input object.
	 * @return The resulting native object.
	 * @throw NullPointerException On null arguments.
	 * @since 2018/09/20
	 */
	public final Object asNativeObject(Object __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Is null refernece
		else if (__in == SpringNullObject.NULL)
			return null;
		
		// Array type
		else if (__in instanceof SpringArrayObject)
		{
			SpringArrayObject sao = (SpringArrayObject)__in;
			
			int len = sao.length();
			
			// Depends on the array type
			SpringClass sscl = sao.type();
			ClassName type = sscl.name();
			switch (type.toString())
			{
					// Char array
				case "[C":
					{
						char[] rv = new char[len];
						
						for (int i = 0; i < len; i++)
							rv[i] = (char)(sao.<Integer>get(Integer.class, i).
								intValue());
						
						return rv;
					}
				
					// {@squirreljme.error BK1u Do not know how to convert the
					// given virtual machine array to a native machine array.
					// (The input class)}
				default:
					throw new RuntimeException(
						String.format("BK1u %s", type));
			}
		}
		
		// Class type
		else if (__in instanceof SpringSimpleObject)
		{
			SpringObject sso = (SpringObject)__in;
			
			// Depends on the class type
			SpringClass sscl = sso.type();
			ClassName type = sscl.name();
			switch (type.toString())
			{
				case "java/lang/String":
					return new String(this.<char[]>asNativeObject(
						char[].class, this.invokeMethod(false,
							new ClassName("java/lang/String"),
							new MethodNameAndType("toCharArray", "()[C"),
							sso)));
				
					// {@squirreljme.error BK1s Do not know how to convert the
					// given virtual machine class to a native machine object.
					// (The input class)}
				default:
					throw new RuntimeException(
						String.format("BK1s %s", type));
			}
		}
		
		// {@squirreljme.error BK1r Do not know how to convert the given class
		// to a native machine object. (The input class)}
		else
			throw new RuntimeException(
				String.format("BK1r %s", __in.getClass()));
	}
	
	/**
	 * Converts the specified virtual machine object to a native object.
	 *
	 * @param <C> The class type.
	 * @param __cl The class type.
	 * @param __in The input object.
	 * @return The resulting native object.
	 * @throw NullPointerException On null arguments.
	 * @since 2018/09/20
	 */
	public final <C> C asNativeObject(Class<C> __cl, Object __in)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.cast(this.asNativeObject(__in));
	}
	
	/**
	 * Converts the specified native object to a virtual machine object.
	 *
	 * @param __in The input object.
	 * @return The resulting VM object.
	 * @since 2018/09/16
	 */
	public final Object asVMObject(Object __in)
	{
		// Null is converted to null
		if (__in == null)
			return SpringNullObject.NULL;
		
		// As-is
		else if (__in instanceof Integer || __in instanceof Long ||
			__in instanceof Float || __in instanceof Double ||
			__in instanceof SpringObject)
			return __in;
		
		// Boolean to integer
		else if (__in instanceof Boolean)
			return Integer.valueOf((Boolean.TRUE.equals(__in) ? 1 : 0));
		
		// Character to Integer
		else if (__in instanceof Character)
			return Integer.valueOf(((Character)__in).charValue());
		
		// Promoted to integer
		else if (__in instanceof Byte || __in instanceof Short)
			return Integer.valueOf(((Number)__in).intValue());
		
		// Character array
		else if (__in instanceof char[])
		{
			char[] in = (char[])__in;
			
			// Setup return array
			int n = in.length;
			SpringArrayObject rv = this.allocateArray(
				this.loadClass(new ClassName("char")), n);
			
			// Copy array values
			for (int i = 0; i < n; i++)
				rv.set(i, (int)in[i]);
			
			return rv;
		}
		
		// Integer array
		else if (__in instanceof int[])
		{
			int[] in = (int[])__in;
			
			// Setup return array
			int n = in.length;
			SpringArrayObject rv = this.allocateArray(
				this.loadClass(new ClassName("int")), n);
			
			// Copy array values
			for (int i = 0; i < n; i++)
				rv.set(i, (int)in[i]);
			
			return rv;
		}
		
		// String array
		else if (__in instanceof String[])
		{
			String[] in = (String[])__in;
			
			// Setup return array
			int n = in.length;
			SpringArrayObject rv = this.allocateArray(
				this.loadClass(new ClassName("java/lang/String")), n);
			
			// Copy array values
			for (int i = 0; i < n; i++)
				rv.set(i, this.asVMObject(in[i]));
			
			return rv;
		}
		
		// String object
		else if (__in instanceof String)
		{
			String s = (String)__in;
			
			// Locate the string class
			SpringClass strclass = this.loadClass(
				new ClassName("java/lang/String"));
				
			// Setup an array of characters to represent the string data,
			// this is the simplest thing to do right now
			SpringObject array = (SpringObject)this.asVMObject(
				s.toString().toCharArray());
				
			// Setup character array sequence which wraps our array
			SpringObject cas = this.newInstance(new ClassName(
				"cc/squirreljme/runtime/cldc/string/CharArraySequence"),
				new MethodDescriptor("([C)V"), array);
			
			// Setup string which uses this sequence
			SpringObject rv = this.newInstance(
				new ClassName("java/lang/String"),
				new MethodDescriptor(
				"(Lcc/squirreljme/runtime/cldc/string/BasicSequence;)V"),
				cas);
			
			return rv;
		}
		
		// Constant string from the constant pool, which shared a global pool
		// of string objects! This must be made so that "aaa" == "aaa" is true
		// even across different classes!
		else if (__in instanceof ConstantValueString)
		{
			ConstantValueString cvs = (ConstantValueString)__in;
			
			// Get the string map but lock on the class loader because a class
			// might want a string but then another thread might be
			// initializing some class, and it will just deadlock as they wait
			// on each other
			SpringMachine machine = this.machine;
			Map<ConstantValueString, SpringObject> stringmap =
				machine.__stringMap();
			synchronized (machine.classLoader().classLoadingLock())
			{
				// Pre-cached object already exists?
				SpringObject rv = stringmap.get(cvs);
				if (rv != null)
					return rv;
				
				// Setup an array of characters to represent the string data,
				// this is the simplest thing to do right now
				SpringObject array = (SpringObject)this.asVMObject(
					cvs.toString().toCharArray());
				
				// Setup character array sequence which wraps our array
				SpringObject cas = this.newInstance(new ClassName(
					"cc/squirreljme/runtime/cldc/string/CharArraySequence"),
					new MethodDescriptor("([C)V"), array);
				
				// Setup string which uses this sequence
				rv = this.newInstance(new ClassName("java/lang/String"),
					new MethodDescriptor(
					"(Lcc/squirreljme/runtime/cldc/string/BasicSequence;)V"),
					cas);
				
				// Cache
				stringmap.put(cvs, rv);
				
				// Use it
				return rv;
			}
		}
		
		// A class object, as needed
		else if (__in instanceof ClassName ||
			__in instanceof ConstantValueClass ||
			__in instanceof SpringClass)
		{
			ClassName name = ((__in instanceof SpringClass) ?
				((SpringClass)__in).name() : ((__in instanceof ClassName) ?
				(ClassName)__in : ((ConstantValueClass)__in).className()));
			
			// Get the class object map but lock on the class loader since we
			// might end up just initializing classes and such
			SpringMachine machine = this.machine;
			Map<ClassName, SpringObject> com = machine.__classObjectMap();
			Map<SpringObject, ClassName> ocm = machine.
				__classObjectToNameMap();
			synchronized (machine.classLoader().classLoadingLock())
			{
				// Pre-cached object already exists?
				SpringObject rv = com.get(name);
				if (rv != null)
					return rv;
				
				// Resolve the input class, so it is initialized
				SpringClass resclass = this.resolveClass(name);
				
				// Resolve the class object
				SpringClass classobj = this.resolveClass(
					new ClassName("java/lang/Class"));
				
				// Copy interfaces into a class array
				SpringClass[] interfaces = resclass.interfaceClasses();
				int ni = interfaces.length;
				SpringArrayObject ints = this.allocateArray(classobj, ni);
				for (int i = 0; i < ni; i++)
					ints.set(i, this.asVMObject(interfaces[i]));
				
				// Initialize class with special class index and some class
				// information
				rv = this.newInstance(classobj.name(),
					new MethodDescriptor("(ILjava/lang/String;" +
						"Ljava/lang/Class;[Ljava/lang/Class;" +
						"Ljava/lang/Class;Ljava/lang/String;)V"),
					resclass.specialIndex(),
					this.asVMObject(name.toString()),
					this.asVMObject(resclass.superClass()),
					ints,
					(!resclass.isArray() ? SpringNullObject.NULL :
						this.asVMObject(resclass.componentType())),
					this.asVMObject(resclass.inJar()));
				
				// Cache and use it
				com.put(name, rv);
				ocm.put(rv, name);
				return rv;
			}
		}
		
		// {@squirreljme.error BK1f Do not know how to convert the given class
		// to a virtual machine object. (The input class)}
		else
			throw new RuntimeException(
				String.format("BK1f %s", __in.getClass()));
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
		
		// No current class, treat as always valid
		if (self == null)
			return true;
		
		// Allow object and class unlimited access to anything
		ClassName cn = self.name();
		if (cn.toString().equals("java/lang/Object") ||
			cn.toString().equals("java/lang/Class"))
			return true;
		
		// This is ourself so access is always granted
		else if (__cl == self)
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
			target = this.loadClass(__m.inClass());
		
		// No current class, treat as always valid
		if (self == null)
			return true;
		
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
			
			// Otherwise it has to be in the same package
			if (self.name().inPackage().equals(target.name().inPackage()))
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
		SpringThread.Frame[] frames = thread.frames();
		
		// Go through frames
		for (int n = frames.length, i = n - 1; i >= 0; i--)
		{
			SpringThread.Frame frame = frames[i];
			
			// No method, could be a blank frame
			SpringMethod m = frame.method();
			if (m == null)
				continue;
			
			// If this is the assembly classes, treat it as if the caller were
			// doing things rather than the asm classes itself
			ClassName icl = frame.method().inClass();
			if (icl.toString().startsWith("cc/squirreljme/runtime/cldc/asm/"))
				continue;
			
			return this.machine.classLoader().loadClass(icl);
		}
		
		return null;
	}
	
	/**
	 * Invokes the given method.
	 *
	 * @param __static Is the method static?
	 * @param __cl The class name.
	 * @param __nat The name and type.
	 * @param __args The arguments.
	 * @return The return value, if any.
	 * @since 2018/09/20
	 */
	public final Object invokeMethod(boolean __static, ClassName __cl,
		MethodNameAndType __nat, Object... __args)
		throws NullPointerException
	{
		if (__cl == null || __nat == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Lookup class
		SpringClass cl = this.resolveClass(__cl);
		
		// Lookup method
		SpringMethod method = cl.lookupMethod(__static, __nat);
		
		// Add blank frame for protection, this is used to hold the return
		// value on the stack
		SpringThread thread = this.thread;
		SpringThread.Frame blank = thread.enterBlankFrame();
		
		// Enter the method we really want to execute
		int framelimit = thread.numFrames();
		SpringThread.Frame execframe = thread.enterFrame(method, __args);
		
		// Execute this method
		this.run(framelimit);
		
		// {@squirreljme.error BK1t Current frame is not our blank frame.}
		SpringThread.Frame currentframe = thread.currentFrame();
		if (currentframe != blank)
			throw new SpringVirtualMachineException("BK1t");
		
		// Read return value from the blank frame
		Object rv;
		if (!__nat.type().hasReturnValue())
			rv = null;
		else
			rv = blank.popFromStack();
		
		// Pop the blank frame, we do not need it anymore
		thread.popFrame();
		
		// Return the popped value
		return rv;
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
			/*todo.DEBUG.note("Need to initialize %s.", __cl.name());*/
			
			// Set the class as initialized early to prevent loops, because
			// a super class might call something from the base class which
			// might be seen as initialized when it should not be. So this is
			// to prevent bad things from happening.
			__cl.setInitialized();
			
			// Initialize the static field map
			Map<SpringField, SpringFieldStorage> sfm =
				machine.__staticFieldMap();
			for (SpringField f : __cl.fieldsOnlyThisClass())
				if (f.isStatic())
					sfm.put(f, new SpringFieldStorage(f));
			
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
				/*todo.DEBUG.note("Class %s has no static initializer.",
					__cl.name());*/
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
	 * Handles a native action within the VM.
	 *
	 * Note that the return value should be a native type, it is translated
	 * as needed.
	 *
	 * @param __func The function to call.
	 * @param __args The arguments to the function.
	 * @return The result from the call.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/16
	 */
	public final Object nativeMethod(String __func, Object... __args)
		throws NullPointerException
	{
		if (__func == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Debug
		/*todo.DEBUG.note("Call native %s", __func);*/
		
		// Depends on the function
		switch (__func)
		{
				// Write to the console
			case "cc/squirreljme/runtime/cldc/asm/ConsoleOutput::" +
				"write:(II)I":
				{
					int fd = (Integer)__args[0];
					PrintStream to = (fd == ConsoleOutput.OUTPUT ?
						System.out : (fd == ConsoleOutput.ERROR ?
						System.err : null));
					
					// Write if it exists
					if (to != null)
					{
						to.write((Integer)__args[1]);
						return 0;
					}
					else
						return ConsoleOutput.ERROR_INVALIDFD;
				}
			
				// Fatal report of a raw call trace in early TODO code
			case "cc/squirreljme/runtime/cldc/asm/DebugAccess::" +
				"fatalTodoReport:([I)V":
				{
					// Format text to the output
					StringBuilder sb = new StringBuilder("[");
					
					// Print hex codes
					SpringArrayObject hex = (SpringArrayObject)__args[0];
					for (int i = 0, n = hex.length(); i < n; i++)
					{
						if (i > 0)
							sb.append(", ");
						sb.append(String.format("%08x",
							hex.<Integer>get(Integer.class, i)));
					}
					
					// End
					sb.append("]");
					
					// {@squirreljme.error BK1k Virtual machine code executed
					// a fatal TODOs report indicating unimplemented code,
					// failing. (The hex codes for the TODOs trace)}
					throw new SpringVirtualMachineException(
						String.format("BK1k %s", sb));
				}
			
				// Return the call trace
			case "cc/squirreljme/runtime/cldc/asm/DebugAccess::" +
				"rawCallTrace:()[I":
				{
					// Need to get all the stack frames first
					SpringThread.Frame[] frames = this.thread.frames();
					int numframes = frames.length;
					
					// Setup return value which stores all the frame data
					SpringMachine machine = this.machine;
					int[] rv = new int[DebugAccess.TRACE_COUNT * numframes];
					for (int i = numframes - 1, o = 0; i >= 0; i--,
						o += DebugAccess.TRACE_COUNT)
					{
						SpringThread.Frame frame = frames[i];
						
						SpringMethod inmethod = frame.method();
						int pc = frame.lastExecutedPc();
						
						// Only if the method is valid
						if (inmethod != null)
						{
							// Class hilo
							SpringMachine.longToInt(o + 0, rv,
								machine.debugUnresolveString(
									inmethod.inClass().toString()));
							
							// Method hilo
							SpringMachine.longToInt(o + 2, rv,
								machine.debugUnresolveString(
									inmethod.nameAndType().name().toString()));
							
							// Descriptor hilo
							SpringMachine.longToInt(o + 4, rv,
								machine.debugUnresolveString(
									inmethod.nameAndType().type().toString()));
							
							// File string
							SpringMachine.longToInt(o + 8, rv,
								machine.debugUnresolveString(
									inmethod.inFile()));
						}
						
						// Not valid
						else
						{
							SpringMachine.longToInt(o + 0, rv, -1);
							SpringMachine.longToInt(o + 2, rv, -1);
							SpringMachine.longToInt(o + 4, rv, -1);
							SpringMachine.longToInt(o + 8, rv, -1);
						}
						
						// Program counter hilo
						rv[o + 6] = 0;
						rv[o + 7] = frame.lastExecutedPc();
						
						// Line of code being executed
						rv[o + 10] = frame.lastExecutedPcSourceLine();
					}
					
					return rv;
				}
				
				// Resolve string pointer
			case "cc/squirreljme/runtime/cldc/asm/DebugAccess::" +
				"resolveString:(J)Ljava/lang/String;":
				return this.asVMObject(
					this.machine.debugResolveString((Long)__args[0]));
					
				// Unresolve string pointer
			case "cc/squirreljme/runtime/cldc/asm/DebugAccess::" +
				"unresolveString:(Ljava/lang/String;)J":
				return this.machine.debugUnresolveString(
					this.<String>asNativeObject(String.class, __args[0]));
			
				// Return the length of the array
			case "cc/squirreljme/runtime/cldc/asm/ObjectAccess::" +
				"arrayLength:(Ljava/lang/Object;)I":
				return ((SpringArrayObject)__args[0]).length();
			
				// Allocate array of a given class
			case "cc/squirreljme/runtime/cldc/asm/ObjectAccess::" +
				"arrayNew:(Ljava/lang/Class;I)Ljava/lang/Object;":
				{
					// Cannot do a reverse lookup
					SpringMachine machine = this.machine;
					Map<SpringObject, ClassName> ocm = machine.
						__classObjectToNameMap();
					synchronized (machine.classLoader().classLoadingLock())
					{
						// {@squirreljme.error BK1y Could not reverse class
						// object to class name.}
						ClassName cn = ocm.get((SpringObject)__args[0]);
						if (cn == null)
							throw new SpringVirtualMachineException("BK1y");
						
						// Lookup class for the component type, we need it
						SpringClass cl = this.loadClass(cn.componentType());
						
						// Allocate array for the component type
						return this.allocateArray(cl,
							(Integer)__args[1]);
					}
				}
			
				// Get the class by the name of whatever is input
			case "cc/squirreljme/runtime/cldc/asm/ObjectAccess::" +
				"classByName:(Ljava/lang/String;)Ljava/lang/Class;":
				return new ClassName(this.<String>asNativeObject(String.class,
					(SpringObject)__args[0]));
				
				// Get the class object for an object
			case "cc/squirreljme/runtime/cldc/asm/ObjectAccess::" +
				"classOf:(Ljava/lang/Object;)Ljava/lang/Class;":
				{
					// Just use the input class of the type
					SpringObject so = (SpringObject)__args[0];
					return so.type();
				}
				
				// Create new primitive weak reference
			case "cc/squirreljme/runtime/cldc/asm/ObjectAccess::" +
				"newWeakReference:" +
				"()Lcc/squirreljme/runtime/cldc/ref/PrimitiveReference;":
				{
					return new SpringPrimitiveWeakReference();
				}
				
				// Identity hash code
			case "cc/squirreljme/runtime/cldc/asm/ObjectAccess::" +
				"identityHashCode:(Ljava/lang/Object;)I":
				return System.identityHashCode(((SpringObject)__args[0]));
				
				// Get reference
			case "cc/squirreljme/runtime/cldc/asm/ObjectAccess::" +
				"referenceGet:(Lcc/squirreljme/runtime/cldc/ref/" +
				"PrimitiveReference;)Ljava/lang/Object;":
				return ((SpringPrimitiveReference)__args[0]).get();
				
				// Set reference
			case "cc/squirreljme/runtime/cldc/asm/ObjectAccess::" +
				"referenceSet:(Lcc/squirreljme/runtime/cldc/ref/" +
				"PrimitiveReference;Ljava/lang/Object;)V":
				((SpringPrimitiveReference)__args[0]).set(
					(SpringObject)__args[1]);
				return null;
				
				// Gets the raw bits for the given double value
			case "cc/squirreljme/runtime/cldc/asm/PrimitiveAccess::" +
				"doubleToRawLongBits:(D)J":
				return Double.doubleToRawLongBits((Double)__args[0]);
				
				// Close resource in JAR
			case "cc/squirreljme/runtime/cldc/asm/ResourceAccess::" +
				"close:(I)I":
				return this.machine.resourceAccess().close(
					(Integer)__args[0]);
				
				// Open resource in JAR
			case "cc/squirreljme/runtime/cldc/asm/ResourceAccess::" +
				"open:(Ljava/lang/String;Ljava/lang/String;)I":
				return this.machine.resourceAccess().open(
					this.<String>asNativeObject(String.class, __args[0]),
					this.<String>asNativeObject(String.class, __args[1]));
				
				// Read resource in JAR
			case "cc/squirreljme/runtime/cldc/asm/ResourceAccess::" +
				"read:(I[BII)I":
				{
					// Handle the arguments
					int fd = (Integer)__args[0];
					SpringArrayObject vb = (SpringArrayObject)__args[1];
					int vo = (Integer)__args[2];
					int vl = (Integer)__args[3];
					
					// Just create a temporary byte array since we cannot
					// write into the VM object representation anyway
					byte[] temp = new byte[vl];
					int rv = this.machine.resourceAccess().read(
						fd, temp, 0, vl);
					
					// Read returned EOF or an error
					if (rv < 0)
						return rv;
					
					// Copy temporary back into the VM buffer
					for (int i = 0, o = vo; i < rv; i++, o++)
						vb.set(o, (int)temp[i]);
					
					return rv;
				}
			
				// List suites that are available
			case "cc/squirreljme/runtime/cldc/asm/SuiteAccess::" +
				"availableSuites:()[Ljava/lang/String;":
				return this.machine.suiteManager().listLibraryNames();
			
				// Exit the virtual machine
			case "cc/squirreljme/runtime/cldc/asm/SystemAccess::" +
				"exit:(I)V":
				this.machine.exit((Integer)__args[0]);
				return null;
				
				// VM e-mail
			case "cc/squirreljme/runtime/cldc/asm/SystemProperties::" +
				"javaVMEmail:()Ljava/lang/String;":
				return "xer@multiphasicapps.net";
				
				// VM name
			case "cc/squirreljme/runtime/cldc/asm/SystemProperties::" +
				"javaVMName:()Ljava/lang/String;":
				return "SquirrelJME SpringCoat";
				
				// VM URL
			case "cc/squirreljme/runtime/cldc/asm/SystemProperties::" +
				"javaVMURL:()Ljava/lang/String;":
				return "http://multiphasicapps.net/";
				
				// VM vendor
			case "cc/squirreljme/runtime/cldc/asm/SystemProperties::" +
				"javaVMVendor:()Ljava/lang/String;":
				return "Stephanie Gawroriski";
				
				// VM Version
			case "cc/squirreljme/runtime/cldc/asm/SystemProperties::" +
				"javaVMVersion:()Ljava/lang/String;":
				return "0.0.2";
				
				// Get system property
			case "cc/squirreljme/runtime/cldc/asm/SystemProperties::" +
				"systemProperty:(Ljava/lang/String;)Ljava/lang/String;":
				return System.getProperty(this.<String>asNativeObject(
					String.class, __args[0]));
			
				// {@squirreljme.error BK1g Unknown native function. (The
				// native function)}
			default:
				throw new SpringVirtualMachineException(
					String.format("BK1g %s", __func));
		}
	}
	
	/**
	 * Creates a new instance of the given class and initializes it using the
	 * given arguments. Access checks are ignored.
	 *
	 * @param __cl The class to initialize.
	 * @param __desc The descriptor of the constructor.
	 * @param __args The arguments to the constructor.
	 * @return The newly created and setup object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/16
	 */
	public final SpringObject newInstance(ClassName __cl,
		MethodDescriptor __desc, Object... __args)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return this.newInstance(this.loadClass(__cl), __desc, __args);
	}
	
	/**
	 * Creates a new instance of the given class and initializes it using the
	 * given arguments. Access checks are ignored.
	 *
	 * @param __cl The class to initialize.
	 * @param __desc The descriptor of the constructor.
	 * @param __args The arguments to the constructor.
	 * @return The newly created and setup object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/16
	 */
	public final SpringObject newInstance(SpringClass __cl,
		MethodDescriptor __desc, Object... __args)
		throws NullPointerException
	{
		if (__cl == null || __desc == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Make sure this class is loaded
		__cl = this.loadClass(__cl);
		
		// Lookup constructor to this method
		SpringMethod cons = __cl.lookupMethod(false, new MethodName("<init>"),
			__desc);
		
		// Allocate the object
		SpringObject rv = this.allocateObject(__cl);
			
		// Stop execution when the constructor exits
		SpringThread thread = this.thread;
		int framelimit = thread.numFrames();
		
		// Need to pass the allocated object as the first argument
		int nargs = __args.length;
		Object[] callargs = new Object[nargs + 1];
		callargs[0] = rv;
		for (int i = 0, o = 1; i < nargs; i++, o++)
			callargs[o] = __args[i];
		
		// Enter the constructor
		thread.enterFrame(cons, callargs);
		
		// Execute until it finishes
		this.run(framelimit);
		
		// Return the resulting object
		return rv;
	}
	
	/**
	 * Resolves the given class, checking access.
	 *
	 * @param __cl The class to resolve.
	 * @return The resolved class.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringIllegalAccessException If the class cannot be accessed.
	 * @since 2018/09/15
	 */
	public final SpringClass resolveClass(ClassName __cl)
		throws NullPointerException, SpringIllegalAccessException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BK15 Could not access the specified class.
		// (The class to access; The context class)}
		SpringClass rv = this.loadClass(__cl);
		if (!this.checkAccess(rv))
			throw new SpringIllegalAccessException(String.format("BK15 %s %s",
				__cl, this.contextClass()));
		
		return rv;
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
	 * Handles the exception, if it is in this frame to be handled then it
	 * will say that the instruction is to be moved elsewhere. Otherwise it
	 * will flag the frame above that an exception occurred and should be
	 * handled.
	 *
	 * @param __o The object being thrown.
	 * @return The next PC address of the handler or a negative value if it
	 * is to proprogate to the above frame.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/13
	 */
	int __handleException(SpringObject __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Need the current frame and its byte code
		SpringThread thread = this.thread;
		SpringThread.Frame frame = thread.currentFrame();
		ByteCode code = frame.byteCode();
		int pc = frame.pc();
		
		// Get the handler for the given exception at the
		// given address
		ExceptionHandler useeh = null;
		for (ExceptionHandler eh : code.exceptions().at(pc))
		{
			// Is this handler compatible for the thrown
			// exception?
			SpringClass ehcl = this.loadClass(eh.type());
			
			if (ehcl.isCompatible(__o))
			{
				useeh = eh;
				break;
			}
		}
		
		// No handler for this exception, so just go up the
		// stack and find a handler recursively up every frame
		if (useeh == null)
		{
			// Pop our current frame from the call stack
			thread.popFrame();
			
			// Did we run out of stack frames?
			SpringThread.Frame cf = thread.currentFrame();
			if (cf == null)
			{
				// Send our throwable to a special handler
				this.invokeMethod(true, new ClassName("cc/squirreljme/" +
					"runtime/cldc/lang/UncaughtExceptionHandler"),
					new MethodNameAndType("handle",
					"(Ljava/lang/Throwable;)V"), __o);
				
				// Just stop execution here
				return -1;
			}
			
			// Toss onto the new current frame
			cf.tossException(__o);
			
			// Stop executing here and let it continue on the
			// other top frame
			return -1;
		}
		
		// Otherwise jump to that address
		else
		{
			// Clear the stack frame and then push our
			// exception back onto the stack
			frame.clearStack();
			frame.pushToStack(__o);
			
			// Handle at this address
			return useeh.handlerAddress();
		}
	}
	
	/**
	 * Looks up the specified instance field specifier and returns the
	 * information for it.
	 *
	 * @param __f The field to lookup.
	 * @return The specified for the field.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringIncompatibleClassChangeException If the field is static.
	 * @throws SpringNoSuchFieldException If the field does not exist.
	 * @since 2018/09/16
	 */
	private final SpringField __lookupInstanceField(FieldReference __f)
		throws NullPointerException, SpringIncompatibleClassChangeException,
			SpringNoSuchFieldException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
			
		// {@squirreljme.error BK18 Could not access the target class for
		// instance field access. (The field reference)}
		SpringClass inclass = this.loadClass(__f.className());
		if (!this.checkAccess(inclass))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK18 %s", __f));
		
		// {@squirreljme.error BK19 Could not access the target field for
		// instance field access. (The field reference; The field flags)}
		SpringField field = inclass.lookupField(false,
			__f.memberNameAndType());
		if (!this.checkAccess(field))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK19 %s %s", __f, field.flags()));
		
		return field;
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
	private final SpringFieldStorage __lookupStaticField(FieldReference __f)
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
		// Check if the VM is exiting, to discontinue execution if it has been
		// requested by any thread
		SpringMachine machine = this.machine;
		machine.exitCheck();
		
		// Need the current frame and its byte code
		SpringThread thread = this.thread;
		
		// Increase the step count
		this._stepcount++;
		
		SpringThread.Frame frame = thread.currentFrame();
		ByteCode code = frame.byteCode();
		
		// Frame is execution
		int iec = frame.incrementExecCount();
		if (iec > 0 && (iec % _EXECUTION_THRESHOLD) == 0)
		{
			// {@squirreljme.error BK22 Execution seems to be stuck in this
			// method.}
			System.err.println("BK22");
			this.thread.printStackTrace(System.err);
		}
		
		// Are these certain kinds of initializers? Because final fields are
		// writable during initialization accordingly
		SpringClass currentclass = this.contextClass();
		SpringMethod method = frame.method();
		boolean isstaticinit = method.isStaticInitializer(),
			isinstanceinit = method.isInstanceInitializer();
		
		// Determine the current instruction of execution
		int pc = frame.pc();
		Instruction inst = code.getByAddress(pc);
		
		// If we are tossing an exception, we need to handle it
		SpringObject tossing = frame.tossedException();
		if (tossing != null)
		{
			// Handling the tossed exception, so do not try handling it again
			frame.tossException(null);
			
			// Handle it
			pc = this.__handleException(tossing);
			if (pc < 0)
				return;
			
			// Put it on an empty stack
			frame.clearStack();
			frame.pushToStack(tossing);
			
			// Execute at the handler address now
			frame.setPc(pc);
			return;
		}
		
		// This PC is about to be executed, so set it as executed since if an
		// exception is thrown this could change potentially
		frame.setLastExecutedPc(pc);
		
		// Debug
		/*todo.DEBUG.note("step(%s %s::%s) -> %s", thread.name(),
			method.inClass(), method.nameAndType(), inst);*/
		
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
					
					// Load object from array
				case InstructionIndex.AALOAD:
					{
						int dx = frame.<Integer>popFromStack(Integer.class);
						SpringArrayObject obj = frame.<SpringArrayObject>
							popFromStack(SpringArrayObject.class);
						
						frame.pushToStack(obj.<SpringObject>get(
							SpringObject.class, dx));
					}
					break;
					
					// Store object to array
				case InstructionIndex.AASTORE:
					{
						SpringObject value = frame.<SpringObject>popFromStack(
							SpringObject.class);
						int dx = frame.<Integer>popFromStack(Integer.class);
						SpringArrayObject obj = frame.<SpringArrayObject>
							popFromStack(SpringArrayObject.class);
						
						obj.set(dx, value);
					}
					break;
					
					// Push null reference
				case InstructionIndex.ACONST_NULL:
					frame.pushToStack(SpringNullObject.NULL);
					break;
					
					// Load reference from local
				case InstructionIndex.ALOAD:
					frame.loadToStack(SpringObject.class,
						inst.<Integer>argument(0, Integer.class));
					break;
					
					// Load reference from local (short)
				case InstructionIndex.ALOAD_0:
				case InstructionIndex.ALOAD_1:
				case InstructionIndex.ALOAD_2:
				case InstructionIndex.ALOAD_3:
					frame.loadToStack(SpringObject.class,
						opid - InstructionIndex.ALOAD_0);
					break;
				
					// Allocate new array
				case InstructionIndex.ANEWARRAY:
					frame.pushToStack(this.allocateArray(this.resolveClass(
						inst.<ClassName>argument(0, ClassName.class)),
						frame.<Integer>popFromStack(Integer.class)));
					break;
					
					// Return reference
				case InstructionIndex.ARETURN:
					this.__vmReturn(thread,
						frame.<SpringObject>popFromStack(SpringObject.class));
					nextpc = Integer.MIN_VALUE;
					break;
					
					// Length of array
				case InstructionIndex.ARRAYLENGTH:
					frame.pushToStack(frame.<SpringArrayObject>popFromStack(
						SpringArrayObject.class).length());
					break;
					
					// Store reference to local variable
				case InstructionIndex.ASTORE:
					frame.storeLocal(inst.<Integer>argument(0, Integer.class),
						frame.<SpringObject>popFromStack(SpringObject.class));
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
					
					// Throwing of an exception
				case InstructionIndex.ATHROW:
					nextpc = this.__handleException(
						frame.<SpringObject>popFromStack(SpringObject.class));
					if (nextpc < 0)
						return;
					break;
					
					// Push value
				case InstructionIndex.BIPUSH:
				case InstructionIndex.SIPUSH:
					frame.pushToStack(inst.<Integer>argument(
						0, Integer.class));
					break;
					
					// Checks casting from a type to another
				case InstructionIndex.CHECKCAST:
					{
						SpringClass as = this.resolveClass(inst.
							<ClassName>argument(0, ClassName.class));
						
						// This is just popped back on if it passes
						SpringObject pop = frame.<SpringObject>popFromStack(
							SpringObject.class);
						
						// {@squirreljme.error BK17 Cannot cast object to the
						// target type. (The type to cast to; The type of the
						// object)}
						if (pop != SpringNullObject.NULL &&
							!as.isAssignableFrom(pop.type()))
							throw new SpringClassCastException(String.format(
								"BK17 %s %s", as, pop.type()));
						
						// Return the popped value
						else
							frame.pushToStack(pop);
					}
					break;
					
					// Double constant
				case InstructionIndex.DCONST_0:
				case InstructionIndex.DCONST_1:
					frame.pushToStack(
						Double.valueOf(opid - InstructionIndex.DCONST_0));
					break;
					
					// Load double from local variable
				case InstructionIndex.DLOAD:
					frame.loadToStack(Double.class,
						inst.<Integer>argument(0, Integer.class));
					break;
					
					// Load double from local variable
				case InstructionIndex.DLOAD_0:
				case InstructionIndex.DLOAD_1:
				case InstructionIndex.DLOAD_2:
				case InstructionIndex.DLOAD_3:
					frame.loadToStack(Double.class,
						opid - InstructionIndex.DLOAD_0);
					break;
					
					// Return double
				case InstructionIndex.DRETURN:
					this.__vmReturn(thread,
						frame.<Double>popFromStack(Double.class));
					nextpc = Integer.MIN_VALUE;
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
					
					// Duplicate top and place two down
				case InstructionIndex.DUP_X1:
					{
						Object a = frame.<Object>popFromStack(Object.class),
							b = frame.<Object>popFromStack(Object.class);
						
						// {@squirreljme.error BK1z Cannot duplicate and place
						// down below with two type.}
						if (a instanceof Long || a instanceof Double ||
							b instanceof Long || b instanceof Double)
							throw new SpringVirtualMachineException("BK1z");
						
						frame.pushToStack(a);
						frame.pushToStack(b);
						frame.pushToStack(a);
					}
					break;
					
					// Float to integer
				case InstructionIndex.F2I:
					{
						float value = frame.<Float>popFromStack(Float.class);
						frame.pushToStack(Integer.valueOf((int)value));
					}
					break;
				
					// Compare float, NaN is positive
				case InstructionIndex.FCMPG:
					{
						float b = frame.<Float>popFromStack(Float.class),
							a = frame.<Float>popFromStack(Float.class);
						
						if (Float.isNaN(a) || Float.isNaN(b))
							frame.pushToStack(1);
						else
							frame.pushToStack((a < b ? -1 : (a > b ? 1 : 0)));
					}
					break;
				
					// Compare float, NaN is negative
				case InstructionIndex.FCMPL:
					{
						float b = frame.<Float>popFromStack(Float.class),
							a = frame.<Float>popFromStack(Float.class);
						
						if (Float.isNaN(a) || Float.isNaN(b))
							frame.pushToStack(-1);
						else
							frame.pushToStack((a < b ? -1 : (a > b ? 1 : 0)));
					}
					break;
					
					// Float constant
				case InstructionIndex.FCONST_0:
				case InstructionIndex.FCONST_1:
				case InstructionIndex.FCONST_2:
					frame.pushToStack(
						Float.valueOf(opid - InstructionIndex.FCONST_0));
					break;
					
					// Load float from local variable
				case InstructionIndex.FLOAD:
					frame.loadToStack(Float.class,
						inst.<Integer>argument(0, Integer.class));
					break;
					
					// Load float from local variable
				case InstructionIndex.FLOAD_0:
				case InstructionIndex.FLOAD_1:
				case InstructionIndex.FLOAD_2:
				case InstructionIndex.FLOAD_3:
					frame.loadToStack(Float.class,
						opid - InstructionIndex.FLOAD_0);
					break;
				
					// Multiply float
				case InstructionIndex.FMUL:
					{
						float b = frame.<Float>popFromStack(Float.class),
							a = frame.<Float>popFromStack(Float.class);
						frame.pushToStack(a * b);
					}
					break;
					
					// Return float
				case InstructionIndex.FRETURN:
					this.__vmReturn(thread,
						frame.<Float>popFromStack(Float.class));
					nextpc = Integer.MIN_VALUE;
					break;
					
					// Read from instance field
				case InstructionIndex.GETFIELD:
					{
						// Lookup field
						SpringField ssf = this.__lookupInstanceField(
							inst.<FieldReference>argument(0,
							FieldReference.class));
						
						// Pop the object to read from
						SpringObject ref = frame.<SpringObject>popFromStack(
							SpringObject.class);
						
						// {@squirreljme.error BK1l Cannot read value from
						// null reference.}
						if (ref == SpringNullObject.NULL)
							throw new SpringNullPointerException("BK1l");
						
						// {@squirreljme.error BK1m Cannot read value from
						// this instance because it not a simple object.}
						if (!(ref instanceof SpringSimpleObject))
							throw new SpringIncompatibleClassChangeException(
								"BK1m");
						SpringSimpleObject sso = (SpringSimpleObject)ref;
						
						// Read and push to the stack
						frame.pushToStack(sso.fieldByIndex(ssf.index()).get());
					}
					break;
					
					// Read static variable
				case InstructionIndex.GETSTATIC:
					{
						// Lookup field
						SpringFieldStorage ssf = this.__lookupStaticField(
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
					
					// Load integer from array
				case InstructionIndex.BALOAD:
				case InstructionIndex.CALOAD:
				case InstructionIndex.SALOAD:
				case InstructionIndex.IALOAD:
					{
						int dx = frame.<Integer>popFromStack(Integer.class);
						SpringArrayObject obj = frame.<SpringArrayObject>
							popFromStack(SpringArrayObject.class);
						
						frame.pushToStack(obj.<Integer>get(Integer.class, dx));
					}
					break;
					
					// Store integer to array (compatible)
				case InstructionIndex.BASTORE:
				case InstructionIndex.CASTORE:
				case InstructionIndex.SASTORE:
				case InstructionIndex.IASTORE:
					{
						int value = frame.<Integer>popFromStack(Integer.class);
						int dx = frame.<Integer>popFromStack(Integer.class);
						SpringArrayObject obj = frame.<SpringArrayObject>
							popFromStack(SpringArrayObject.class);
						
						obj.set(dx, value);
					}
					break;
					
					// Integer to byte
				case InstructionIndex.I2B:
					{
						int value = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(Byte.valueOf((byte)value).
							intValue());
					}
					break;
					
					// Integer to double
				case InstructionIndex.I2D:
					{
						int value = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(Double.valueOf(value));
					}
					break;
					
					// Integer to long
				case InstructionIndex.I2L:
					{
						int value = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(Long.valueOf(value));
					}
					break;
					
					// Integer to character
				case InstructionIndex.I2C:
					{
						int value = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(Integer.valueOf((char)value));
					}
					break;
					
					// Integer to float
				case InstructionIndex.I2F:
					{
						int value = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(Float.valueOf(value));
					}
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
					
					// Object a == b
				case InstructionIndex.IF_ACMPEQ:
					{
						SpringObject b = frame.<SpringObject>popFromStack(
								SpringObject.class),
							a = frame.<SpringObject>popFromStack(
								SpringObject.class);
						
						if (a == b)
							nextpc = inst.<InstructionJumpTarget>argument(0,
								InstructionJumpTarget.class).target();
					}
					break;
					
					// Object a != b
				case InstructionIndex.IF_ACMPNE:
					{
						SpringObject b = frame.<SpringObject>popFromStack(
								SpringObject.class),
							a = frame.<SpringObject>popFromStack(
								SpringObject.class);
						
						if (a != b)
							nextpc = inst.<InstructionJumpTarget>argument(0,
								InstructionJumpTarget.class).target();
					}
					break;
					
					// int a == b
				case InstructionIndex.IF_ICMPEQ:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						
						if (a == b)
							nextpc = inst.<InstructionJumpTarget>argument(0,
								InstructionJumpTarget.class).target();
					}
					break;
					
					// int a >= b
				case InstructionIndex.IF_ICMPGE:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						
						if (a >= b)
							nextpc = inst.<InstructionJumpTarget>argument(0,
								InstructionJumpTarget.class).target();
					}
					break;
					
					// int a > b
				case InstructionIndex.IF_ICMPGT:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						
						if (a > b)
							nextpc = inst.<InstructionJumpTarget>argument(0,
								InstructionJumpTarget.class).target();
					}
					break;
					
					// int a <= b
				case InstructionIndex.IF_ICMPLE:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						
						if (a <= b)
							nextpc = inst.<InstructionJumpTarget>argument(0,
								InstructionJumpTarget.class).target();
					}
					break;
					
					// int a < b
				case InstructionIndex.IF_ICMPLT:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						
						if (a < b)
							nextpc = inst.<InstructionJumpTarget>argument(0,
								InstructionJumpTarget.class).target();
					}
					break;
					
					// int a != b
				case InstructionIndex.IF_ICMPNE:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						
						if (a != b)
							nextpc = inst.<InstructionJumpTarget>argument(0,
								InstructionJumpTarget.class).target();
					}
					break;
					
					// int a == 0
				case InstructionIndex.IFEQ:
					if (frame.<Integer>popFromStack(Integer.class) == 0)
						nextpc = inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class).target();
					break;
					
					// int a >= 0
				case InstructionIndex.IFGE:
					if (frame.<Integer>popFromStack(Integer.class) >= 0)
						nextpc = inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class).target();
					break;
					
					// int a > 0
				case InstructionIndex.IFGT:
					if (frame.<Integer>popFromStack(Integer.class) > 0)
						nextpc = inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class).target();
					break;
					
					// int a <= 0
				case InstructionIndex.IFLE:
					if (frame.<Integer>popFromStack(Integer.class) <= 0)
						nextpc = inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class).target();
					break;
					
					// int a < 0
				case InstructionIndex.IFLT:
					if (frame.<Integer>popFromStack(Integer.class) < 0)
						nextpc = inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class).target();
					break;
					
					// int a != 0
				case InstructionIndex.IFNE:
					if (frame.<Integer>popFromStack(Integer.class) != 0)
						nextpc = inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class).target();
					break;
					
					// If reference is not null
				case InstructionIndex.IFNONNULL:
					if (frame.<SpringObject>popFromStack(
						SpringObject.class) != SpringNullObject.NULL)
						nextpc = inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class).target();
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
					
					// Increment local variable
				case InstructionIndex.IINC:
				case InstructionIndex.WIDE_IINC:
					{
						int dx = inst.<Integer>argument(0, Integer.class);
						frame.storeLocal(dx, frame.<Integer>loadLocal(
							Integer.class, dx) + inst.<Integer>argument(1,
							Integer.class));
					}
					break;
					
					// Load integer from local variable
				case InstructionIndex.ILOAD:
					frame.loadToStack(Integer.class,
						inst.<Integer>argument(0, Integer.class));
					break;
					
					// Load integer from local variable
				case InstructionIndex.ILOAD_0:
				case InstructionIndex.ILOAD_1:
				case InstructionIndex.ILOAD_2:
				case InstructionIndex.ILOAD_3:
					frame.loadToStack(Integer.class,
						opid - InstructionIndex.ILOAD_0);
					break;
				
					// Addly integer
				case InstructionIndex.IADD:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(a + b);
					}
					break;
				
					// AND integer
				case InstructionIndex.IAND:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(a & b);
					}
					break;
				
					// Divide integer
				case InstructionIndex.IDIV:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(a / b);
					}
					break;
				
					// Multiply integer
				case InstructionIndex.IMUL:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(a * b);
					}
					break;
				
					// Negate integer
				case InstructionIndex.INEG:
					{
						int a = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(-a);
					}
					break;
					
					// Is the given object an instance of the given class?
				case InstructionIndex.INSTANCEOF:
					{
						// Check against this
						SpringClass as = this.resolveClass(inst.
							<ClassName>argument(0, ClassName.class));
						
						SpringClass vtype = frame.<SpringObject>popFromStack(
							SpringObject.class).type();
						frame.pushToStack((vtype != null &&
							as.isAssignableFrom(vtype) ? 1 : 0));
					}
					break;
					
					// Invoke interface method
				case InstructionIndex.INVOKEINTERFACE:
					this.__vmInvokeInterface(inst, thread, frame);
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
					
					// Invoke virtual method
				case InstructionIndex.INVOKEVIRTUAL:
					this.__vmInvokeVirtual(inst, thread, frame);
					break;
				
					// OR integer
				case InstructionIndex.IOR:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(a | b);
					}
					break;
				
					// Remainder integer
				case InstructionIndex.IREM:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(a % b);
					}
					break;
					
					// Return integer
				case InstructionIndex.IRETURN:
					this.__vmReturn(thread,
						frame.<Integer>popFromStack(Integer.class));
					nextpc = Integer.MIN_VALUE;
					break;
				
					// Shift left integer
				case InstructionIndex.ISHL:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(a << (b & 0x1F));
					}
					break;
				
					// Shift right integer
				case InstructionIndex.ISHR:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(a >> (b & 0x1F));
					}
					break;
					
					// Store integer to local variable
				case InstructionIndex.ISTORE:
					frame.storeLocal(inst.<Integer>argument(0, Integer.class),
						frame.<Integer>popFromStack(Integer.class));
					break;
					
					// Store integer to local variable
				case InstructionIndex.ISTORE_0:
				case InstructionIndex.ISTORE_1:
				case InstructionIndex.ISTORE_2:
				case InstructionIndex.ISTORE_3:
					frame.storeLocal(opid - InstructionIndex.ISTORE_0,
						frame.<Integer>popFromStack(Integer.class));
					break;
				
					// Subtract integer
				case InstructionIndex.ISUB:
					{
						Integer b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(a - b);
					}
					break;
				
					// XOR integer
				case InstructionIndex.IXOR:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(a ^ b);
					}
					break;
					
					// Long to integer
				case InstructionIndex.L2I:
					{
						long value = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(Integer.valueOf((int)value));
					}
					break;
					
					// Add long
				case InstructionIndex.LADD:
					{
						long b = frame.<Long>popFromStack(Long.class),
							a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(a + b);
					}
					break;
				
					// And long
				case InstructionIndex.LAND:
					{
						long b = frame.<Long>popFromStack(Long.class),
							a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(a & b);
					}
					break;
				
					// Compare long
				case InstructionIndex.LCMP:
					{
						long b = frame.<Long>popFromStack(Long.class),
							a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack((a < b ? -1 : (a > b ? 1 : 0)));
					}
					break;
					
					// Long constant
				case InstructionIndex.LCONST_0:
				case InstructionIndex.LCONST_1:
					frame.pushToStack(Long.valueOf(
						(opid - InstructionIndex.LCONST_0)));
					break;
					
					// Load from constant pool, push to the stack
				case InstructionIndex.LDC:
				case InstructionIndex.LDC_W:
					{
						ConstantValue value = inst.<ConstantValue>argument(0,
							ConstantValue.class);
						
						// Pushing a string, which due to the rules of Java
						// there must always be an equality (==) between two
						// strings, so "foo" == "foo" must be true even if it
						// is in different parts of the code
						// Additionally internall class objects are adapted
						// too as needed
						if (value instanceof ConstantValueString ||
							value instanceof ConstantValueClass)
							frame.pushToStack(this.asVMObject(value));
						
						// This will be pre-boxed so push it to the stack
						else
							frame.pushToStack(value.boxedValue());
					}
					break;
					
					// Load long or double from constant pool, to the stack
				case InstructionIndex.LDC2_W:
					frame.pushToStack(inst.<ConstantValue>argument(0,
						ConstantValue.class).boxedValue());
					break;
				
					// Divide long
				case InstructionIndex.LDIV:
					{
						long b = frame.<Long>popFromStack(Long.class),
							a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(a / b);
					}
					break;
					
					// Load integer from local variable
				case InstructionIndex.LLOAD:
					frame.loadToStack(Long.class,
						inst.<Integer>argument(0, Integer.class));
					break;
					
					// Load integer from local variable
				case InstructionIndex.LLOAD_0:
				case InstructionIndex.LLOAD_1:
				case InstructionIndex.LLOAD_2:
				case InstructionIndex.LLOAD_3:
					frame.loadToStack(Long.class,
						opid - InstructionIndex.LLOAD_0);
					break;
					
					// Multiply long
				case InstructionIndex.LMUL:
					{
						long b = frame.<Long>popFromStack(Long.class),
							a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(a * b);
					}
					break;
				
					// Negate long
				case InstructionIndex.LNEG:
					{
						long a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(-a);
					}
					break;
					
					// OR long
				case InstructionIndex.LOR:
					{
						long b = frame.<Long>popFromStack(Long.class),
							a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(a | b);
					}
					break;
					
					// Lookup in a jump table
				case InstructionIndex.LOOKUPSWITCH:
				case InstructionIndex.TABLESWITCH:
					nextpc = inst.<IntMatchingJumpTable>argument(0,
						IntMatchingJumpTable.class).match(
						frame.<Integer>popFromStack(Integer.class)).target();
					break;
				
					// Remainder long
				case InstructionIndex.LREM:
					{
						long b = frame.<Long>popFromStack(Long.class),
							a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(a % b);
					}
					break;
					
					// Return long
				case InstructionIndex.LRETURN:
					this.__vmReturn(thread,
						frame.<Long>popFromStack(Long.class));
					nextpc = Integer.MIN_VALUE;
					break;
				
					// Shift left long
				case InstructionIndex.LSHL:
					{
						int b = frame.<Integer>popFromStack(Integer.class);
						long a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(a << (((long)b) & 0x3F));
					}
					break;
					
					// Store long to local variable
				case InstructionIndex.LSTORE:
					frame.storeLocal(inst.<Integer>argument(0, Integer.class),
						frame.<Long>popFromStack(Long.class));
					break;
					
					// Store long to local variable
				case InstructionIndex.LSTORE_0:
				case InstructionIndex.LSTORE_1:
				case InstructionIndex.LSTORE_2:
				case InstructionIndex.LSTORE_3:
					frame.storeLocal(opid - InstructionIndex.LSTORE_0,
						frame.<Long>popFromStack(Long.class));
					break;
				
					// Unsigned shift right long
				case InstructionIndex.LUSHR:
					{
						int b = frame.<Integer>popFromStack(Integer.class);
						long a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(a >>> (((long)b) & 0x3F));
					}
					break;
					
					// XOR long
				case InstructionIndex.LXOR:
					{
						long b = frame.<Long>popFromStack(Long.class),
							a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(a ^ b);
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
				
					// Allocate new primitive array
				case InstructionIndex.NEWARRAY:
					frame.pushToStack(this.allocateArray(this.resolveClass(
						ClassName.fromPrimitiveType(
						inst.<PrimitiveType>argument(0, PrimitiveType.class))),
						frame.<Integer>popFromStack(Integer.class)));
					break;
					
					// Return from method with no return value
				case InstructionIndex.RETURN:
					thread.popFrame();
					break;
					
					// Pop category 1 value
				case InstructionIndex.POP:
					{
						// {@squirreljme.error BK1e Cannot pop category two
						// value from stack.}
						Object val = frame.popFromStack();
						if (val instanceof Long || val instanceof Double)
							throw new SpringVirtualMachineException("BK1e");
					}
					break;
					
					// Put to instance field
				case InstructionIndex.PUTFIELD:
					{
						// Lookup field
						SpringField ssf = this.__lookupInstanceField(
							inst.<FieldReference>argument(0,
							FieldReference.class));
						
						// Pop the value and the object to mess with
						Object value = frame.popFromStack();
						SpringObject ref = frame.<SpringObject>popFromStack(
							SpringObject.class);
						
						// {@squirreljme.error BK1a Cannot store value into
						// null reference.}
						if (ref == SpringNullObject.NULL)
							throw new SpringNullPointerException("BK1a");
						
						// {@squirreljme.error BK1b Cannot store value into
						// this instance because it not a simple object.}
						if (!(ref instanceof SpringSimpleObject))
							throw new SpringIncompatibleClassChangeException(
								"BK1b");
						SpringSimpleObject sso = (SpringSimpleObject)ref;
						
						// {@squirreljme.error BK1c Cannot store value into
						// a field which belongs to another class.}
						if (!this.loadClass(ssf.inClass()).isAssignableFrom(
							sso.type()))
							throw new SpringClassCastException("BK1c");
						
						// Set
						sso.fieldByIndex(ssf.index()).set(value,
							isinstanceinit);
					}
					break;
				
					// Put to static field
				case InstructionIndex.PUTSTATIC:
					{
						// Lookup field
						SpringFieldStorage ssf = this.__lookupStaticField(
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
		catch (SpringException e)
		{
			// Do not add causes or do anything if this was already thrown
			if ((e instanceof SpringFatalException) ||
				(e instanceof SpringMachineExitException))
				throw e;
			
			// Print the stack trace
			thread.printStackTrace(System.err);
			
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
			
			// {@squirreljme.error BK1p Fatal VM exception.}
			throw new SpringFatalException("BK1p", e);
		}
		
		// Set implicit next PC address, if it has not been set or the next
		// address was actually changed
		if (nextpc != orignextpc || pc == frame.pc())
			frame.setPc(nextpc);
	}
	
	/**
	 * Invokes a method in an interface.
	 *
	 * @param __i The instruction.
	 * @param __t The current thread.
	 * @param __f The current frame.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/19
	 */
	private final void __vmInvokeInterface(Instruction __i, SpringThread __t,
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
		
		// {@squirreljme.error BK1n Could not access the target
		// method for interface invoke. (The target method)}
		if (!this.checkAccess(refmethod))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK1n %s", ref));
		
		// Load arguments, includes the instance it acts on
		int nargs = refmethod.nameAndType().type().argumentCount() + 1;
		Object[] args = new Object[nargs];
		for (int i = nargs - 1; i >= 0; i--)
			args[i] = __f.popFromStack();
			
		// {@squirreljme.error BK1o Cannot invoke the method in the object
		// because it is of the wrong type. (The reference class; The class
		// of the target object)}
		SpringClass objclass = ((SpringObject)args[0]).type();
		if (!refclass.isAssignableFrom(objclass))
			throw new SpringClassCastException(
				String.format("BK1o %s %s", refclass, objclass));
		
		// Relookup the method since we need to the right one! Then invoke it
		__t.enterFrame(objclass.lookupMethod(false, ref.memberNameAndType()),
			args);
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
		
		// Load arguments
		int nargs = refmethod.nameAndType().type().
			argumentCount() + 1;
		Object[] args = new Object[nargs];
		for (int i = nargs - 1; i >= 0; i--)
			args[i] = __f.popFromStack();
		
		// Get the class of the current method being executed, lookup depends
		// on it
		SpringClass currentclass = this.loadClass(
			this.thread.currentFrame().method().inClass());
		
		// {@squirreljme.error BK20 Instance object for special invoke is
		// null.}
		SpringObject onthis = (SpringObject)args[0];
		if (onthis == null || onthis == SpringNullObject.NULL)
			throw new SpringNullPointerException("BK20");
		
		// These modify the action to be performed
		boolean insame = (currentclass == refclass),
			insuper = currentclass.isSuperClass(refclass),
			isinit = refmethod.name().isInstanceInitializer(),
			isprivate = refmethod.flags().isPrivate();
		
		// {@squirreljme.error BK21 Cannot call private method that is not
		// in the same class. (The method reference)}
		if (isprivate && !insame)
			throw new SpringIncompatibleClassChangeException(
				String.format("BK21 %s", ref));
		
		// Call superclass method instead?
		else if (!isprivate && insuper && !isinit)
			refmethod = currentclass.superClass().lookupMethod(false,
				ref.memberNameAndType());
		
		// Invoke this method
		__t.enterFrame(refmethod, args);
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
		
		// Virtualized native call, depends on what it is
		if (refmethod.flags().isNative())
		{
			// Calculate result of method
			MethodDescriptor type = ref.memberType();
			Object rv = this.nativeMethod(ref.className() + "::" +
				ref.memberName() + ":" + type, args);
			
			// Push native object to the stack
			if (type.hasReturnValue())
				__f.pushToStack(this.asVMObject(rv));
		}
		
		// Real code that exists in class file format
		else
			__t.enterFrame(refmethod, args);
	}
	
	/**
	 * Performs a virtual invoke.
	 *
	 * @param __i The instruction.
	 * @param __t The current thread.
	 * @param __f The current frame.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/16
	 */
	private final void __vmInvokeVirtual(Instruction __i, SpringThread __t,
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
		
		// {@squirreljme.error BK1d Could not access the target
		// method for virtual invoke. (The target method)}
		if (!this.checkAccess(refmethod))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK1d %s", ref));
		
		// Load arguments, includes the instance it acts on
		int nargs = refmethod.nameAndType().type().argumentCount() + 1;
		Object[] args = new Object[nargs];
		for (int i = nargs - 1; i >= 0; i--)
			args[i] = __f.popFromStack();
		
		// {@squirreljme.error BK1v Instance object for virtual invoke is
		// null.}
		SpringObject onthis = (SpringObject)args[0];
		if (onthis == null || onthis == SpringNullObject.NULL)
			throw new SpringNullPointerException("BK1v");
		
		// Re-resolve method for this object's class
		refmethod = onthis.type().lookupMethod(false,
			ref.memberNameAndType());
		
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
	
	/**
	 * Returns from the top-most frame then pushes the return value to the
	 * parent frame's stack (if any).
	 *
	 * @param __thread The thread to return in.
	 * @param __value The value to push.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/16
	 */
	private final void __vmReturn(SpringThread __thread, Object __value)
		throws NullPointerException
	{
		if (__thread == null || __value == null)
			throw new NullPointerException("NARG");
		
		// Pop our current frame
		SpringThread.Frame old = __thread.popFrame();
		old.setPc(Integer.MIN_VALUE);
		
		// Push the value to the current frame
		SpringThread.Frame cur = __thread.currentFrame();
		if (cur != null)
			cur.pushToStack(__value);
		
		/*System.err.printf("+++RETURN: %s%n", __value);
		__thread.printStackTrace(System.err);*/
	}
}

