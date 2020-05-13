// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.exceptions.SpringArithmeticException;
import cc.squirreljme.vm.springcoat.exceptions.SpringClassCastException;
import cc.squirreljme.vm.springcoat.exceptions.SpringException;
import cc.squirreljme.vm.springcoat.exceptions.SpringFatalException;
import cc.squirreljme.vm.springcoat.exceptions.SpringIllegalAccessException;
import cc.squirreljme.vm.springcoat.exceptions.SpringIncompatibleClassChangeException;
import cc.squirreljme.vm.springcoat.exceptions.SpringMachineExitException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNegativeArraySizeException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNoSuchFieldException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNoSuchMethodException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNullPointerException;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.io.PrintStream;
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
	extends Thread
{
	/** Number of instructions which can be executed before warning. */
	private static final int _EXECUTION_THRESHOLD =
		200000;
	
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
		
		// Set the thread's worker to this
		if (__t._worker == null)
			__t._worker = this;
		
		// {@squirreljme.error BK1x Thread already has a worker associated
		// with it.}
		else
			throw new SpringVirtualMachineException("BK1x");
	}
	
	/**
	 * Allocates the memory needed to store an array of the given class and
	 * of the given length.
	 *
	 * @param __cl The component type.
	 * @param __l The length of the array.
	 * @return The allocated array.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNegativeArraySizeException If the array size is negative.
	 * @since 2018/09/15
	 */
	@Deprecated
	public final SpringArrayObject allocateArray(SpringClass __cl, int __l)
		throws NullPointerException, SpringNegativeArraySizeException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		SpringClass dim = this.resolveClass(__cl.name().addDimensions(1));
		switch (__cl.name().toString())
		{
				// Boolean
			case "boolean":
				return new SpringArrayObjectBoolean(dim, __cl, __l);
			
				// Byte
			case "byte":
				return new SpringArrayObjectByte(dim, __cl, __l);
				
				// Short
			case "short":
				return new SpringArrayObjectShort(dim, __cl, __l);
				
				// Char
			case "char":
				return new SpringArrayObjectChar(dim, __cl, __l);
				
				// Int
			case "int":
				return new SpringArrayObjectInteger(dim, __cl, __l);
				
				// Long
			case "long":
				return new SpringArrayObjectLong(dim, __cl, __l);
				
				// Float
			case "float":
				return new SpringArrayObjectFloat(dim, __cl, __l);
				
				// Float
			case "double":
				return new SpringArrayObjectDouble(dim, __cl, __l);
			
				// Generic array
			default:
				return new SpringArrayObjectGeneric(dim, __cl, __l);
		}
		
	}
	
	/**
	 * Allocates the memory needed to store an object of the given class.
	 *
	 * @param __cl The object to allocate.
	 * @return The allocated instance of the object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/08
	 */
	@Deprecated
	public final SpringObject allocateObject(SpringClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// The called constructor will allocate the space needed to store
		// this object
		return new SpringSimpleObject(__cl, this.machine.pointers);
	}
	
	/**
	 * Converts the specified virtual machine object to a native object.
	 *
	 * @param __in The input object.
	 * @return The resulting native object.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringFatalException If the object cannot be translated.
	 * @since 2018/09/20
	 */
	@Deprecated
	public final Object asNativeObject(Object __in)
		throws NullPointerException, SpringFatalException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Is null refernece
		else if (__in == SpringNullObject.NULL)
			return null;
		
		// Boxed types remain the same
		else if (__in instanceof Integer || __in instanceof Long ||
			__in instanceof Float || __in instanceof Double)
			return __in;
		
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
					
					// String
				case "[Ljava/lang/String;":
					{
						String[] rv = new String[len];
						
						for (int i = 0; i < len; i++)
							rv[i] = this.<String>asNativeObject(String.class,
								sao.<SpringObject>get(SpringObject.class, i));
						
						return rv;
					}
				
					// {@squirreljme.error BK1y Do not know how to convert the
					// given virtual machine array to a native machine array.
					// (The input class)}
				default:
					throw new RuntimeException(
						String.format("BK1y %s", type));
			}
		}
		
		// Class type
		else if (__in instanceof SpringSimpleObject)
		{
			SpringSimpleObject sso = (SpringSimpleObject)__in;
			
			// Depends on the class type
			SpringClass sscl = sso.type();
			ClassName type = sscl.name();
			switch (type.toString())
			{
				case "java/lang/Integer":
					return sso.fieldByField(sscl.lookupField(false,
						"_value", "I")).get();
				
				case "java/lang/String":
					return new String(this.<char[]>asNativeObject(
						char[].class, this.invokeMethod(false,
							new ClassName("java/lang/String"),
							new MethodNameAndType("toCharArray", "()[C"),
							sso)));
				
					// {@squirreljme.error BK1z Do not know how to convert the
					// given virtual machine class to a native machine object.
					// (The input class)}
				default:
					throw new RuntimeException(
						String.format("BK1z %s", type));
			}
		}
		
		// {@squirreljme.error BK20 Do not know how to convert the given class
		// to a native machine object. (The input class)}
		else
			throw new SpringFatalException(
				String.format("BK20 %s", __in.getClass()));
	}
	
	/**
	 * Converts the specified object to a native object or unwraps an array
	 * for direct access.
	 *
	 * @param <C> The resulting class type.
	 * @param __cl The class to cast to.
	 * @param __in The input object.
	 * @return The resulting object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/19
	 */
	@Deprecated
	public final <C> C asNativeObjectUnwrapArray(Class<C> __cl, Object __in)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException();
		
		if (__in instanceof SpringArrayObject)
			return __cl.cast(((SpringArrayObject)__in).array());
		return this.<C>asNativeObject(__cl, __in);
	}
	
	/**
	 * Converts the specified virtual machine object to a native object.
	 *
	 * @param <C> The class type.
	 * @param __cl The class type.
	 * @param __in The input object.
	 * @return The resulting native object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/20
	 */
	@Deprecated
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
	@Deprecated
	public final Object asVMObject(Object __in)
	{
		return this.asVMObject(__in, false);
	}
	
	/**
	 * Converts the specified native object to a virtual machine object.
	 *
	 * @param __in The input object.
	 * @param __noclassres Do not use class resolution? Just load the class?
	 * @return The resulting VM object.
	 * @since 2018/09/16
	 */
	@Deprecated
	public final Object asVMObject(Object __in, boolean __noclassres)
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
		
		// Convertable exception
		else if (__in instanceof SpringConvertableThrowable)
		{
			SpringConvertableThrowable e = (SpringConvertableThrowable)__in;
			
			// Initialize new instance with this type, use the input message
			return this.newInstance(new ClassName(e.targetClass()),
				new MethodDescriptor("(Ljava/lang/String;)V"),
				this.asVMObject(e.getMessage()));
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
			
			// Setup string which uses this sequence
			SpringObject rv = this.newInstance(
				new ClassName("java/lang/String"),
				new MethodDescriptor("([CS)V"),
				array, 0);
			
			return rv;
		}
		
		// Constant string from the constant pool, which shared a global pool
		// of string objects! This must be made so that "aaa" == "aaa" is true
		// even across different classes!
		
		// Load a constant value to string
		else if (__in instanceof ConstantValueString)
			return ObjectLoader.loadConstantValueString(
				this, (ConstantValueString)__in);
		
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
				SpringClass resclass = (__noclassres ? this.loadClass(name) :
					this.resolveClass(name));
				
				// Resolve the class object
				SpringClass classobj = this.resolveClass(
					new ClassName("java/lang/Class"));
				
				// Copy interfaces into a class array
				SpringClass[] interfaces = resclass.interfaceClasses();
				int ni = interfaces.length;
				SpringArrayObject ints = this.allocateArray(classobj, ni);
				for (int i = 0; i < ni; i++)
					ints.set(i, this.asVMObject(interfaces[i]));
				
				// See if there is a default constructor
				SpringMethod defconmeth = resclass.lookupDefaultConstructor();
				
				// Get static method for this constructor
				int defconflags;
				SpringObject defconsm;
				if (defconmeth != null)
				{
					defconflags = defconmeth.flags().toJavaBits();
					defconsm = new SpringVMStaticMethod(defconmeth);
				}
				
				// There is none
				else
				{
					defconflags = 0;
					defconsm = SpringNullObject.NULL;
				}
				
				// Get the static method for enumeration values
				ClassFlags classflags = resclass.flags();
				SpringObject enumsm;
				if (classflags.isEnum())
					try
					{
						enumsm = new SpringVMStaticMethod(
							resclass.lookupMethod(true,
							new MethodName("values"),
							new MethodDescriptor(name.addDimensions(1).
							field())));
					}
					catch (SpringIncompatibleClassChangeException e)
					{
						enumsm = SpringNullObject.NULL;
					}
				else
					enumsm = SpringNullObject.NULL;
				
				// Initialize V1 class data which is initialized with class
				// data
				SpringObject cdata = this.newInstance(new ClassName(
					"cc/squirreljme/runtime/cldc/lang/ClassDataV1"),
					new MethodDescriptor("(ILjava/lang/String;" +
						"Ljava/lang/Class;[Ljava/lang/Class;" +
						"Ljava/lang/Class;Ljava/lang/String;II" +
						"Lcc/squirreljme/runtime/cldc/asm/StaticMethod;" +
						"Lcc/squirreljme/runtime/cldc/asm/StaticMethod;)V"),
					resclass.specialIndex(),
					this.asVMObject(name.toString(), true),
					this.asVMObject(resclass.superClass(), true),
					ints,
					(!resclass.isArray() ? SpringNullObject.NULL :
						this.asVMObject(resclass.componentType(), true)),
					this.asVMObject(resclass.inJar()),
					classflags.toJavaBits(),
					defconflags, defconsm, enumsm);
				
				// Initialize class with special class index and some class
				// information
				rv = this.newInstance(classobj.name(),
					new MethodDescriptor(
					"(Lcc/squirreljme/runtime/cldc/lang/ClassData;)V"), cdata);
				
				// Cache and use it
				com.put(name, rv);
				ocm.put(rv, name);
				return rv;
			}
		}
		
		// {@squirreljme.error BK21 Do not know how to convert the given class
		// to a virtual machine object. (The input class)}
		else
			throw new RuntimeException(
				String.format("BK21 %s", __in.getClass()));
	}
	
	/**
	 * As VM object, but boxed if a primitive.
	 *
	 * @param __in The object to convert.
	 * @return The converted object.
	 * @since 2018/11/19
	 */
	@Deprecated
	public final Object asVMObjectBoxed(Object __in)
	{
		// Null is converted to null
		if (__in == null)
			return SpringNullObject.NULL;
		
		// Box these
		else if (__in instanceof Integer)
			return this.newInstance(new ClassName("java/lang/Integer"),
				new MethodDescriptor("(I)V"), __in);
		else if (__in instanceof Long)
			return this.newInstance(new ClassName("java/lang/Long"),
				new MethodDescriptor("(J)V"), __in);
		else if (__in instanceof Float)
			return this.newInstance(new ClassName("java/lang/Float"),
				new MethodDescriptor("(F)V"), __in);
		else if (__in instanceof Double)
			return this.newInstance(new ClassName("java/lang/Double"),
				new MethodDescriptor("(D)V"), __in);
		
		// As-is
		else if (__in instanceof SpringObject)
			return __in;
		
		else
			return this.asVMObject(__in);
	}
	
	/**
	 * As VM object, if it is an array it is wrapped otherwise if the object is
	 * a primitive type it becomes boxed.
	 *
	 * @param __in The object to convert.
	 * @return The converted object.
	 * @since 2018/12/03
	 */
	@Deprecated
	public final Object asVMObjectBoxedOrWrappedArray(Object __in)
	{
		if (__in == null)
			return SpringNullObject.NULL;
		
		// Array types
		else if (__in instanceof boolean[] ||
			__in instanceof byte[] ||
			__in instanceof short[] ||
			__in instanceof char[] ||
			__in instanceof int[] ||
			__in instanceof long[] ||
			__in instanceof float[] ||
			__in instanceof double[])
			return this.asWrappedArray(__in);
		
		// As boxed type instead
		else
			return this.asVMObjectBoxed(__in);
	}
	
	/**
	 * Wraps the native array so that it is directly read and written in
	 * the VM code.
	 *
	 * @param __a The object to convert.
	 * @return The object representing the array.
	 * @throws RuntimeException If the type is not an array.
	 * @since 2018/11/18
	 */
	@Deprecated
	public final SpringObject asWrappedArray(Object __a)
		throws RuntimeException
	{
		if (__a == null)
			return SpringNullObject.NULL;
		
		// Boolean
		else if (__a instanceof boolean[])
			return new SpringArrayObjectBoolean(
				this.loadClass(new ClassName("[Z")),
				this.loadClass(new ClassName("boolean")),
				(boolean[])__a);
		
		// Byte
		else if (__a instanceof byte[])
			return new SpringArrayObjectByte(
				this.loadClass(new ClassName("[B")),
				this.loadClass(new ClassName("byte")),
				(byte[])__a);
		
		// Short
		else if (__a instanceof short[])
			return new SpringArrayObjectShort(
				this.loadClass(new ClassName("[S")),
				this.loadClass(new ClassName("short")),
				(short[])__a);
		
		// Character
		else if (__a instanceof char[])
			return new SpringArrayObjectChar(
				this.loadClass(new ClassName("[C")),
				this.loadClass(new ClassName("char")),
				(char[])__a);
		
		// Integer
		else if (__a instanceof int[])
			return new SpringArrayObjectInteger(
				this.loadClass(new ClassName("[I")),
				this.loadClass(new ClassName("int")),
				(int[])__a);
		
		// Long
		else if (__a instanceof long[])
			return new SpringArrayObjectLong(
				this.loadClass(new ClassName("[J")),
				this.loadClass(new ClassName("long")),
				(long[])__a);
		
		// Float
		else if (__a instanceof float[])
			return new SpringArrayObjectFloat(
				this.loadClass(new ClassName("[F")),
				this.loadClass(new ClassName("float")),
				(float[])__a);
		
		// Double
		else if (__a instanceof double[])
			return new SpringArrayObjectDouble(
				this.loadClass(new ClassName("[D")),
				this.loadClass(new ClassName("double")),
				(double[])__a);
		
		// {@squirreljme.error BK22 Cannot wrap this as a native array.
		// (The input class type)}
		else
			throw new RuntimeException("BK22 " + __a.getClass());
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
			throw new todo.OOPS();
		
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
	 * Invokes the given method, returning any value it returns.
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
		
		// Lookup class and method for the static method
		SpringClass cl;
		SpringMethod method;
		if (__static)
		{
			cl = this.resolveClass(__cl);
			method = cl.lookupMethod(__static, __nat);
		}
		
		// Call it based on the object instead
		else
		{
			cl = ((SpringObject)__args[0]).type();
			method = cl.lookupMethod(false, __nat);
		} 
		
		// Overflow or exceptions might occur
		int framelimit;
		SpringThread.Frame blank, execframe;
		SpringThread thread = this.thread;
		try
		{
			// Add blank frame for protection, this is used to hold the return
			// value on the stack
			blank = thread.enterBlankFrame();
			
			// Enter the method we really want to execute
			framelimit = thread.numFrames();
			execframe = thread.enterFrame(method, __args);
			
			// Execute this method
			this.run(framelimit);
		}
		
		// Exception when running which was not caught
		catch (SpringVirtualMachineException e)
		{
			// Print the thread trace
			thread.printStackTrace(System.err);
			
			// Propogate up
			throw e;
		}
		
		// This is an error unless the thread signaled exit
		SpringThread.Frame currentframe = thread.currentFrame();
		if (currentframe != blank)
		{
			// If our thread just happened to signal an exit of the VM, then
			// the current frame will be invalid anyway, so since the
			// exception or otherwise might be signaled we must make an
			// exception for exit here so it continues going down.
			if (thread._signaledexit)
				throw new SpringMachineExitException(this.machine._exitcode);
			
			// {@squirreljme.error BK23 Current frame is not our blank frame.}
			throw new SpringVirtualMachineException("BK23");
		}
		
		// Read return value from the blank frame
		Object rv;
		if (__nat.type().hasReturnValue())
			rv = blank.popFromStack();
		else
			rv = null;
		
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
	 * @param __class The class name.
	 * @param __name The method name.
	 * @param __type The method type.
	 * @param __args The arguments to the function.
	 * @return The result from the call.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/16
	 */
	public final Object nativeMethod(ClassName __class, MethodName __name,
		MethodDescriptor __type, Object... __args)
		throws NullPointerException
	{
		if (__class == null || __name == null || __type == null ||
			__args == null)
			throw new NullPointerException("NARG");
		
		// There is only a single class with native methods
		if (!__class.toString().equals("cc/squirreljme/jvm/Assembly"))
			throw new SpringVirtualMachineException(String.format(
				"Not an assembly call: %s::%s:%s", __class, __name, __type));
		
		// This may be used by native methods
		SpringMachine machine = this.machine;
		
		// Depends on the function
		switch (__name.toString())
		{
				// Atomic ticker value
			case "atomicTicker":
				return AtomicTicker.next();
				
				// Long's classInfo
			case "classInfoOfLong":
				return this.thread.context().loadClassInfo(this,
					ClassName.fromPrimitiveType(PrimitiveType.LONG)).pointer();
				
				// Lock the garbage collector
			case "gcLock":
				return machine.tasks.gcLock.lock((Integer)__args[0]);
			
				// Unlock the garbage collector
			case "gcUnlock":
				machine.tasks.gcLock.unlock((Integer)__args[0]);
				return null;
				
				// Lock the memory allocator
			case "memAllocLock":
				return machine.tasks.memory.lock((Integer)__args[0]);
			
				// Unlocks the memory allocator
			case "memAllocUnlock":
				machine.tasks.memory.unlock((Integer)__args[0]);
				return null;
				
				// Read byte value
			case "memReadByte":
				return machine.tasks.memory.read(
					(long)__args[0] + (int)__args[1]);
				
				// Read int value
			case "memReadJavaInt":
			case "memReadInt":
				return machine.tasks.memory.readInt(
					(long)__args[0] + (int)__args[1]);
				
				// Read long value
			case "memReadJavaLong":
			case "memReadLong":
				return machine.tasks.memory.readLong(
					(long)__args[0] + (int)__args[1]);
				
				// Read short value
			case "memReadJavaShort":
			case "memReadShort":
				return machine.tasks.memory.readShort(
					(long)__args[0] + (int)__args[1]);
				
				// Write byte value
			case "memWriteByte":
				machine.tasks.memory.write(
					(long)__args[0] + (int)__args[1], (byte)__args[2]);
				return null;
				
				// Write int value
			case "memWriteJavaInt":
			case "memWriteInt":
				machine.tasks.memory.writeInt(
					(long)__args[0] + (int)__args[1], (int)__args[2]);
				return null;
				
				// Write long value
			case "memWriteJavaLong":
			case "memWriteLong":
				machine.tasks.memory.writeLong(
					(long)__args[0] + (int)__args[1], (long)__args[2]);
				return null;
				
				// Write short value
			case "memWriteJavaShort":
			case "memWriteShort":
				machine.tasks.memory.writeShort(
					(long)__args[0] + (int)__args[1], (short)__args[2]);
				return null;
				
				// Conversion of object to pointer
			case "objectToPointer":
				{
					SpringObject object = (SpringObject)__args[0];
					if (object == null)
						return 0L;
					return object.pointerArea().base;
				}
				
				// Conversion of pointer to object
			case "pointerToObject":
				return this.mapValueToObject(__args[0]);
				
				// Return the reference chain of this object
			case "refChainGet":
				return this.mapValueToObject(this.mapValueToObject(__args[0])
					.refChainer().get());
				
				// Set the reference chain of an object
			case "refChainSet":
				this.mapValueToObject(__args[0]).refChainer().set(
					this.mapValueToObject(__args[1]).pointerArea()
					.base());
				return null;
				
				// Reference count up
			case "refCountUp":
				this.mapValueToObject(__args[0]).refCounter().up();
				return null;
				
				// Spin-lock burn
			case "spinLockBurn":
				return null;
				
				// Pure and Un-pure system call
			case "sysCall":
			case "sysCallV":
			case "sysCallVL":
			case "sysCallP":
			case "sysCallPV":
			case "sysCallPVL":
				try
				{
					// We can use this to debug and measure the time needed
					// to execute system calls
					this.thread.profiler.enterFrame(
						String.format("<SystemCall#%d>", (__args.length > 0 ?
							((Number)__args[0]).shortValue() : -1)),
						__name.toString(), __type.toString());
					
					return SystemCallHandler.dispatch(this,
						__name, __type, __args);
				}
				finally
				{
					// Always clear this frame as we left the system call
					this.thread.profiler.exitFrame();
				}
			
				// Not-implemented
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown assembly call: %s:%s", __name, __type));
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
		SpringMethod cons = __cl.lookupMethod(false,
			new MethodName("<init>"), __desc);
		
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
	 * Maps an object to a long value.
	 *
	 * @param __v The value to convert.
	 * @return The converted value.
	 * @since 2020/03/13
	 */
	final long mapObjectToLong(SpringObject __v)
	{
		if (__v == SpringNullObject.NULL)
			return 0;
		return ((long)this.uniqueObjectToPointer(__v)) & 0xFFFFFFFFL;
	}
	
	/**
	 * Maps a value to an object.
	 *
	 * @param __v The value to map.
	 * @return The mapped object.
	 * @since 2020/03/13
	 */
	final SpringObject mapValueToObject(Object __v)
	{
		if (__v == null)
			return SpringNullObject.NULL;
		else if (__v instanceof Long || __v instanceof Integer)
			return this.uniquePointerToObject(((Number)__v).intValue());
		else if (__v instanceof SpringPointer)
			return this.uniquePointerToObject(((SpringPointer)__v).pointerInt);
		else if (__v instanceof SpringObject)
			return (SpringObject)__v;
		
		// Cannot map this
		else
			throw new SpringVirtualMachineException("Unknown value type: " +
				__v.getClass().getName());
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
		
		// {@squirreljme.error BK26 Could not access the specified class.
		// (The class to access; The context class)}
		SpringClass rv = this.loadClass(__cl);
		if (!this.checkAccess(rv))
			throw new SpringIllegalAccessException(String.format("BK26 %s %s",
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
		SpringThread thread = this.thread;
		try
		{
			// {@squirreljme.error BK27 Cannot have a negative frame limit.
			// (The frame limit)}
			if (__framelimit < 0)
				throw new IllegalArgumentException(String.format("BK27 %d",
					__framelimit));
			
			// The thread is alive as long as there are still frames of
			// execution
			while (thread.numFrames() > __framelimit)
			{
				// Single step executing the top frame
				this.__singleStep();
			}
		}
		
		// If the VM is exiting then clear the execution stack before we go
		// away
		catch (SpringMachineExitException e)
		{
			// Thread is okay to exit!
			thread._terminate = true;
			thread._signaledexit = true;
			
			// Exit all frames
			thread.exitAllFrames();
			
			// Exit profiler stack
			thread.profiler.exitAll(System.nanoTime());
			
			// Do not rethrow though
			return;
		}
		
		// Caught exception
		catch (RuntimeException e)
		{
			// Frame limit is zero, so kill the thread
			if (__framelimit == 0)
			{
				PrintStream err = System.err;
				
				err.println("****************************");
				
				// Print the real stack trace
				err.println("*** EXTERNAL STACK TRACE ***");
				e.printStackTrace(err);
				err.println();
				
				// Print the VM seen stack trace
				err.println("*** INTERNAL STACK TRACE ***");
				thread.printStackTrace(err);
				err.println();
				
				err.println("****************************");
				
				// Terminate the thread
				thread._terminate = true;
				
				// Exit all frames
				thread.exitAllFrames();
				
				// Exit from all profiler threads
				thread.profiler.exitAll(System.nanoTime());
			}
			
			// Re-toss
			throw e;
		}
		
		// Terminate if the last frame
		finally
		{
			if (__framelimit == 0)
				thread._terminate = true;
		}
	}
	
	/**
	 * Checks if an exception is being thrown and sets up the state from it.
	 *
	 * @return True if an exception was detected.
	 * @since 2018/12/06
	 */
	boolean __checkException()
	{
		// Are we exiting in the middle of an exception throwing?
		this.machine.exitCheck();
		
		// Check if this frame handles the exception
		SpringThread.Frame frame = this.thread.currentFrame();
		SpringObject tossing = frame.tossedException();
		if (tossing != null)
		{
			// Handling the tossed exception, so do not try handling it again
			frame.tossException(null);
			
			// Handle it
			int pc = this.__handleException(tossing);
			if (pc < 0)
				return true;
			
			// Put it on an empty stack
			frame.clearStack();
			frame.pushToStack(tossing);
			
			// Execute at the handler address now
			frame.setPc(pc);
			return true;
		}
		
		// No exception thrown
		return false;
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
			
		// Are we exiting in the middle of an exception throwing?
		this.machine.exitCheck();
		
		// Get all frames to check for unchecked ones
		SpringThread thread = this.thread;
		SpringThread.Frame[] frames = thread.frames();
		
		// Dive down and see
		int dive = frames.length - 1;
		while (dive >= 0)
		{
			// Get frame details
			SpringThread.Frame frame = frames[dive];
			ByteCode code = frame.byteCode();
			int pc = frame.lastExecutedPc();
			
			// Check if there is an exception handler here
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
			
			// We found one, stop
			if (useeh != null)
				break;
			
			// Otherwise continue the dive
			dive--;
		}
		
		// Exception is unhandled, so we should just fail here
		if (dive < 0)
			throw new SpringFatalException(String.format(
				"Uncaught exception: %s%n", __o.type().name));
		
		// Need the current frame and its byte code
		SpringThread.Frame frame = thread.currentFrame();
		ByteCode code = frame.byteCode();
		int pc = frame.lastExecutedPc();
		
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
				// Emit warning
				System.err.printf("Uncaught exception: %s%n",
					__o.type().name);
				
				// Exit
				throw new SpringMachineExitException(127);
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
	private SpringField __lookupInstanceField(FieldReference __f)
		throws NullPointerException, SpringIncompatibleClassChangeException,
			SpringNoSuchFieldException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
			
		// {@squirreljme.error BK28 Could not access the target class for
		// instance field access. (The field reference)}
		SpringClass inclass = this.loadClass(__f.className());
		if (!this.checkAccess(inclass))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK28 %s", __f));
		
		// {@squirreljme.error BK29 Could not access the target field for
		// instance field access. (The field reference; The field flags)}
		SpringField field = inclass.lookupField(false,
			__f.memberNameAndType());
		if (!this.checkAccess(field))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK29 %s %s", __f, field.flags()));
		
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
	private SpringFieldStorage __lookupStaticField(FieldReference __f)
		throws NullPointerException, SpringIncompatibleClassChangeException,
			SpringNoSuchFieldException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BK2a Could not access the target class for
		// static field access. (The field reference)}
		SpringClass inclass = this.loadClass(__f.className());
		if (!this.checkAccess(inclass))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK2a %s", __f));
		
		// {@squirreljme.error BK2b Could not access the target field for
		// static field access. (The field reference)}
		SpringField field = inclass.lookupField(true, __f.memberNameAndType());
		if (!this.checkAccess(field))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK2b %s", __f));
		
		// Lookup the global static field
		return this.machine.lookupStaticField(field);
	}
	
	/**
	 * Single step through handling a single instruction.
	 *
	 * This method uses strict floating point to make operations consistent.
	 *
	 * @since 2018/09/03
	 */
	private strictfp void __singleStep()
	{
		// Need the current frame and its byte code
		SpringThread thread = this.thread;
		
		// Check if the VM is exiting, to discontinue execution if it has been
		// requested by any thread
		SpringMachine machine = this.machine;
		try
		{
			machine.exitCheck();
		}
		
		// If the VM is exiting then clear the execution stack before we go
		// away
		catch (SpringMachineExitException e)
		{
			// Thread is okay to exit!
			thread._terminate = true;
			
			// Exit profiler stack
			thread.profiler.exitAll(System.nanoTime());
			
			throw e;
		}
		
		SpringThread.Frame frame = thread.currentFrame();
		ByteCode code = frame.byteCode();
		
		// Frame is execution
		int iec = frame.incrementExecCount();
		if (iec > 0 && (iec % SpringThreadWorker._EXECUTION_THRESHOLD) == 0)
		{
			// {@squirreljme.error BK2c Execution seems to be stuck in this
			// method.}
			System.err.println("BK2c");
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
		if (this.__checkException())
			return;
		
		// This PC is about to be executed, so set it as executed since if an
		// exception is thrown this could change potentially
		frame.setLastExecutedPc(pc);
		
		// Debug
		/*System.err.printf("step(%s %s::%s) -> %s%n", thread.name(),
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
							popFromStackNotNull(SpringArrayObject.class);
						
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
							popFromStackNotNull(SpringArrayObject.class);
						
						obj.set(dx, value);
					}
					break;
					
					// Push null reference
				case InstructionIndex.ACONST_NULL:
					frame.pushToStack(SpringNullObject.NULL);
					break;
					
					// Load reference from local
				case InstructionIndex.ALOAD:
				case InstructionIndex.WIDE_ALOAD:
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
					frame.pushToStack(
						frame.<SpringArrayObject>popFromStackNotNull(
						SpringArrayObject.class).length());
					break;
					
					// Store reference to local variable
				case InstructionIndex.ASTORE:
				case InstructionIndex.WIDE_ASTORE:
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
						
						// {@squirreljme.error BK2d Cannot cast object to the
						// target type. (The type to cast to; The type of the
						// object)}
						if (pop != SpringNullObject.NULL &&
							!as.isAssignableFrom(pop.type()))
							throw new SpringClassCastException(String.format(
								"BK2d %s %s", as, pop.type()));
						
						// Return the popped value
						else
							frame.pushToStack(pop);
					}
					break;
					
					// Double to float
				case InstructionIndex.D2F:
					{
						double value = frame.<Double>popFromStack(Double.class);
						frame.pushToStack(Float.valueOf((float)value));
					}
					break;
					
					// Double to int
				case InstructionIndex.D2I:
					{
						double value = frame.<Double>popFromStack(Double.class);
						frame.pushToStack(Integer.valueOf((int)value));
					}
					break;
					
					// Double to long
				case InstructionIndex.D2L:
					{
						double value = frame.<Double>popFromStack(Double.class);
						frame.pushToStack(Long.valueOf((long)value));
					}
					break;
				
					// Addiply double
				case InstructionIndex.DADD:
					{
						double b = frame.<Double>popFromStack(Double.class),
							a = frame.<Double>popFromStack(Double.class);
						frame.pushToStack(a + b);
					}
					break;
					
					// Compare double, NaN is positive
				case InstructionIndex.DCMPG:
					{
						double b = frame.<Float>popFromStack(Float.class),
							a = frame.<Float>popFromStack(Float.class);
						
						if (Double.isNaN(a) || Double.isNaN(b))
							frame.pushToStack(1);
						else
							frame.pushToStack((a < b ? -1 : (a > b ? 1 : 0)));
					}
					break;
				
					// Compare double, NaN is negative
				case InstructionIndex.DCMPL:
					{
						double b = frame.<Double>popFromStack(Double.class),
							a = frame.<Double>popFromStack(Double.class);
						
						if (Double.isNaN(a) || Double.isNaN(b))
							frame.pushToStack(-1);
						else
							frame.pushToStack((a < b ? -1 : (a > b ? 1 : 0)));
					}
					break;
					
					// Double constant
				case InstructionIndex.DCONST_0:
				case InstructionIndex.DCONST_1:
					frame.pushToStack(
						Double.valueOf(opid - InstructionIndex.DCONST_0));
					break;
				
					// Divide double
				case InstructionIndex.DDIV:
					{
						double b = frame.<Double>popFromStack(Double.class),
							a = frame.<Double>popFromStack(Double.class);
						frame.pushToStack(a / b);
					}
					break;
					
					// Load double from local variable
				case InstructionIndex.DLOAD:
				case InstructionIndex.WIDE_DLOAD:
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
				
					// Multiply double
				case InstructionIndex.DMUL:
					{
						double b = frame.<Double>popFromStack(Double.class),
							a = frame.<Double>popFromStack(Double.class);
						frame.pushToStack(a * b);
					}
					break;
				
					// Negate double
				case InstructionIndex.DNEG:
					{
						double a = frame.<Double>popFromStack(Double.class);
						frame.pushToStack(-a);
					}
					break;
				
					// Remainder double
				case InstructionIndex.DREM:
					{
						double b = frame.<Double>popFromStack(Double.class),
							a = frame.<Double>popFromStack(Double.class);
						frame.pushToStack(a % b);
					}
					break;
					
					// Return double
				case InstructionIndex.DRETURN:
					this.__vmReturn(thread,
						frame.<Double>popFromStack(Double.class));
					nextpc = Integer.MIN_VALUE;
					break;
				
					// Subtract double
				case InstructionIndex.DSUB:
					{
						double b = frame.<Double>popFromStack(Double.class),
							a = frame.<Double>popFromStack(Double.class);
						frame.pushToStack(a - b);
					}
					break;
					
					// Store double to local variable
				case InstructionIndex.DSTORE:
				case InstructionIndex.WIDE_DSTORE:
					frame.storeLocal(inst.<Integer>argument(0, Integer.class),
						frame.<Double>popFromStack(Double.class));
					break;
					
					// Store long to double variable
				case InstructionIndex.DSTORE_0:
				case InstructionIndex.DSTORE_1:
				case InstructionIndex.DSTORE_2:
				case InstructionIndex.DSTORE_3:
					frame.storeLocal(opid - InstructionIndex.DSTORE_0,
						frame.<Double>popFromStack(Double.class));
					break;
					
					// Duplicate top-most stack entry
				case InstructionIndex.DUP:
					{
						Object copy = frame.popFromStack();
						
						// {@squirreljme.error BK2e Cannot duplicate category
						// two type.}
						if (copy instanceof Long || copy instanceof Double)
							throw new SpringVirtualMachineException("BK2e");
						
						// Push twice!
						frame.pushToStack(copy);
						frame.pushToStack(copy);
					}
					break;
					
					// Duplicate top and place two down
				case InstructionIndex.DUP_X1:
					{
						Object a = frame.popFromStack(),
							b = frame.popFromStack();
						
						// {@squirreljme.error BK2f Cannot duplicate and place
						// down below with two type.}
						if (a instanceof Long || a instanceof Double ||
							b instanceof Long || b instanceof Double)
							throw new SpringVirtualMachineException("BK2f");
						
						frame.pushToStack(a);
						frame.pushToStack(b);
						frame.pushToStack(a);
					}
					break;
					
					// Dup[licate top entry and place two or three down
				case InstructionIndex.DUP_X2:
					{
						Object a = frame.popFromStack(),
							b = frame.popFromStack();
						
						// {@squirreljme.error BK2g Cannot duplicate cat2
						// type.}
						if (a instanceof Long || a instanceof Double)
							throw new SpringVirtualMachineException("BK2g");
						
						// Insert A below C
						if (b instanceof Long || b instanceof Double)
						{
							frame.pushToStack(a);
							frame.pushToStack(b);
							frame.pushToStack(a);
						}
						
						// Grab C as well and push below that
						else
						{
							Object c = frame.popFromStack();
							
							// {@squirreljme.error BK2h Cannot duplicate top
							// most entry and place two down because a cat2
							// type is in the way.}
							if (c instanceof Long || c instanceof Double)
								throw new SpringVirtualMachineException(
									"BK2h");
									
							frame.pushToStack(a);
							frame.pushToStack(c);
							frame.pushToStack(b);
							frame.pushToStack(a);
						}
					}
					break;
					
					// Duplicate top two cat1s or single cat2
				case InstructionIndex.DUP2:
					{
						Object a = frame.popFromStack();
						
						// Just cat two
						if (a instanceof Long || a instanceof Double)
						{
							frame.pushToStack(a);
							frame.pushToStack(a);
						}
						
						// Double values
						else
						{
							Object b = frame.popFromStack();
							
							// {@squirreljme.error BK2i Cannot duplicate top
							// two values.}
							if (b instanceof Long || b instanceof Double)
								throw new SpringVirtualMachineException(
									"BK2i");
							
							frame.pushToStack(b);
							frame.pushToStack(a);
							frame.pushToStack(b);
							frame.pushToStack(a);
						}
					}
					break;
					
					// Duplicate top one or two operand values and insert two
					// or three values down
				case InstructionIndex.DUP2_X1:
					{
						Object a = frame.popFromStack(),
							b = frame.popFromStack();
						
						// {@squirreljme.error BK2j Expected category one
						// type.}
						if (b instanceof Long || b instanceof Double)
							throw new SpringVirtualMachineException(
								"BK2j");
						
						// Insert this below b
						if (a instanceof Long || a instanceof Double)
						{
							frame.pushToStack(a);
							frame.pushToStack(b);
							frame.pushToStack(a);
						}
						
						// Three cat1 values
						else
						{
							Object c = frame.popFromStack();
							
							// {@squirreljme.error BK2k Cannot duplicate value
							// below category two type.}
							if (c instanceof Long || c instanceof Double)
								throw new SpringVirtualMachineException(
									"BK2k");
							
							frame.pushToStack(b);
							frame.pushToStack(a);
							frame.pushToStack(c);
							frame.pushToStack(b);
							frame.pushToStack(a);
						}
					}
					break;
				
					// Duplicate top one or two stack entries and insert
					// two, three, or four down
				case InstructionIndex.DUP2_X2:
					{
						Object a = frame.popFromStack(),
							b = frame.popFromStack();
						
						// Category two is on top
						if (a instanceof Long || a instanceof Double)
						{
							// Category two is on bottom (form 4)
							if (b instanceof Long || b instanceof Double)
							{
								frame.pushToStack(a);
								frame.pushToStack(b);
								frame.pushToStack(a);
							}
							
							// Category ones on bottom (form 2)
							else
							{
								Object c = frame.popFromStack();
								
								// {@squirreljme.error BK2l Cannot pop cat2
								// type for dup.}
								if (c instanceof Long || c instanceof Double)
									throw new SpringVirtualMachineException(
										"BK2l");
								
								frame.pushToStack(a);
								frame.pushToStack(c);
								frame.pushToStack(b);
								frame.pushToStack(a);
							}
						}
						
						// Category one is on top
						else
						{
							// {@squirreljme.error BK2m Category two type
							// cannot be on the bottom.}
							if (b instanceof Long || b instanceof Double)
								throw new SpringVirtualMachineException(
									"BK2m");
							
							Object c = frame.popFromStack();
							
							// C is category two (Form 3)
							if (c instanceof Long || c instanceof Double)
							{
								frame.pushToStack(b);
								frame.pushToStack(a);
								frame.pushToStack(c);
								frame.pushToStack(b);
								frame.pushToStack(a);
							}
							
							// Category one on bottom (Form 1)
							else
							{
								Object d = frame.popFromStack();
								
								// {@squirreljme.error BK2n Bottommost entry
								// cannot be cat2 type.}
								if (d instanceof Long || d instanceof Double)
									throw new SpringVirtualMachineException(
										"BK2n");
								
								frame.pushToStack(b);
								frame.pushToStack(a);
								frame.pushToStack(d);
								frame.pushToStack(c);
								frame.pushToStack(b);
								frame.pushToStack(a);
							}
						}
					}
					break;
					
					// Float to double
				case InstructionIndex.F2D:
					{
						float value = frame.<Float>popFromStack(Float.class);
						frame.pushToStack(Double.valueOf((double)value));
					}
					break;
					
					// Float to integer
				case InstructionIndex.F2I:
					{
						float value = frame.<Float>popFromStack(Float.class);
						frame.pushToStack(Integer.valueOf((int)value));
					}
					break;
					
					// Float to long
				case InstructionIndex.F2L:
					{
						float value = frame.<Float>popFromStack(Float.class);
						frame.pushToStack(Long.valueOf((long)value));
					}
					break;
				
					// Add float
				case InstructionIndex.FADD:
					{
						float b = frame.<Float>popFromStack(Float.class),
							a = frame.<Float>popFromStack(Float.class);
						frame.pushToStack(a + b);
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
				
					// Divide float
				case InstructionIndex.FDIV:
					{
						float b = frame.<Float>popFromStack(Float.class),
							a = frame.<Float>popFromStack(Float.class);
						frame.pushToStack(a / b);
					}
					break;
					
					// Load float from local variable
				case InstructionIndex.FLOAD:
				case InstructionIndex.WIDE_FLOAD:
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
				
					// Negate float
				case InstructionIndex.FNEG:
					{
						float a = frame.<Float>popFromStack(Float.class);
						frame.pushToStack(-a);
					}
					break;
				
					// Remainder float
				case InstructionIndex.FREM:
					{
						float b = frame.<Float>popFromStack(Float.class),
							a = frame.<Float>popFromStack(Float.class);
						frame.pushToStack(a % b);
					}
					break;
					
					// Return float
				case InstructionIndex.FRETURN:
					this.__vmReturn(thread,
						frame.<Float>popFromStack(Float.class));
					nextpc = Integer.MIN_VALUE;
					break;
				
					// Subtract float
				case InstructionIndex.FSUB:
					{
						float b = frame.<Float>popFromStack(Float.class),
							a = frame.<Float>popFromStack(Float.class);
						frame.pushToStack(a - b);
					}
					break;
					
					// Store float to local variable
				case InstructionIndex.FSTORE:
				case InstructionIndex.WIDE_FSTORE:
					frame.storeLocal(inst.<Integer>argument(0, Integer.class),
						frame.<Float>popFromStack(Float.class));
					break;
					
					// Store float to local variable
				case InstructionIndex.FSTORE_0:
				case InstructionIndex.FSTORE_1:
				case InstructionIndex.FSTORE_2:
				case InstructionIndex.FSTORE_3:
					frame.storeLocal(opid - InstructionIndex.FSTORE_0,
						frame.<Float>popFromStack(Float.class));
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
						
						// {@squirreljme.error BK2o Cannot read value from
						// null reference.}
						if (ref == SpringNullObject.NULL)
							throw new SpringNullPointerException("BK2o");
						
						// {@squirreljme.error BK2p Cannot read value from
						// this instance because it not a simple object.}
						if (!(ref instanceof SpringSimpleObject))
							throw new SpringIncompatibleClassChangeException(
								"BK2p");
						SpringSimpleObject sso = (SpringSimpleObject)ref;
						
						// Read and push to the stack
						frame.pushToStack(this.asVMObject(
							sso.fieldByIndex(ssf.index()).get()));
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
						frame.pushToStack(this.asVMObject(ssf.get()));
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
							popFromStackNotNull(SpringArrayObject.class);
						
						frame.pushToStack(obj.<Integer>get(Integer.class, dx));
					}
					break;
					
					// Load double from array
				case InstructionIndex.DALOAD:
					{
						int dx = frame.<Integer>popFromStack(Integer.class);
						SpringArrayObject obj = frame.<SpringArrayObject>
							popFromStackNotNull(SpringArrayObject.class);
						
						frame.pushToStack(obj.<Double>get(Double.class, dx));
					}
					break;
					
					// Load float from array
				case InstructionIndex.FALOAD:
					{
						int dx = frame.<Integer>popFromStack(Integer.class);
						SpringArrayObject obj = frame.<SpringArrayObject>
							popFromStackNotNull(SpringArrayObject.class);
						
						frame.pushToStack(obj.<Float>get(Float.class, dx));
					}
					break;
					
					
					// Load long from array
				case InstructionIndex.LALOAD:
					{
						int dx = frame.<Integer>popFromStack(Integer.class);
						SpringArrayObject obj = frame.<SpringArrayObject>
							popFromStackNotNull(SpringArrayObject.class);
						
						frame.pushToStack(obj.<Long>get(Long.class, dx));
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
							popFromStackNotNull(SpringArrayObject.class);
						
						obj.set(dx, value);
					}
					break;
					
					// Store double to array
				case InstructionIndex.DASTORE:
					{
						double value = frame.<Double>popFromStack(
							Double.class);
						int dx = frame.<Integer>popFromStack(Integer.class);
						SpringArrayObject obj = frame.<SpringArrayObject>
							popFromStackNotNull(SpringArrayObject.class);
						
						obj.set(dx, value);
					}
					break;
					
					// Store float to array
				case InstructionIndex.FASTORE:
					{
						float value = frame.<Float>popFromStack(Float.class);
						int dx = frame.<Integer>popFromStack(Integer.class);
						SpringArrayObject obj = frame.<SpringArrayObject>
							popFromStackNotNull(SpringArrayObject.class);
						
						obj.set(dx, value);
					}
					break;
					
					// Store long to array
				case InstructionIndex.LASTORE:
					{
						long value = frame.<Long>popFromStack(Long.class);
						int dx = frame.<Integer>popFromStack(Integer.class);
						SpringArrayObject obj = frame.<SpringArrayObject>
							popFromStackNotNull(SpringArrayObject.class);
						
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
					
					// Integer to short
				case InstructionIndex.I2S:
					{
						int value = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(Integer.valueOf((short)value));
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
				case InstructionIndex.WIDE_ILOAD:
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
					
					// Exception to be handled?
					if (this.__checkException())
						return;
					break;
					
					// Invoke special method (constructor, superclass,
					// or private)
				case InstructionIndex.INVOKESPECIAL:
					this.__vmInvokeSpecial(inst, thread, frame);
					
					// Exception to be handled?
					if (this.__checkException())
						return;
					break;
					
					// Invoke static method
				case InstructionIndex.INVOKESTATIC:
					this.__vmInvokeStatic(inst, thread, frame);
					
					// Exception to be handled?
					if (this.__checkException())
						return;
					break;
					
					// Invoke virtual method
				case InstructionIndex.INVOKEVIRTUAL:
					this.__vmInvokeVirtual(inst, thread, frame);
					
					// Exception to be handled?
					if (this.__checkException())
						return;
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
				case InstructionIndex.WIDE_ISTORE:
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
				
					// Unsigned shift right integer
				case InstructionIndex.IUSHR:
					{
						int b = frame.<Integer>popFromStack(Integer.class),
							a = frame.<Integer>popFromStack(Integer.class);
						frame.pushToStack(a >>> (b & 0x1F));
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
					
					// Long to double
				case InstructionIndex.L2D:
					{
						long value = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(Double.valueOf((double)value));
					}
					break;
					
					// Long to float
				case InstructionIndex.L2F:
					{
						long value = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(Float.valueOf((float)value));
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
				case InstructionIndex.WIDE_LLOAD:
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
					
					// Subtract long
				case InstructionIndex.LSUB:
					{
						long b = frame.<Long>popFromStack(Long.class),
							a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(a - b);
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
				
					// Shift right long
				case InstructionIndex.LSHR:
					{
						int b = frame.<Integer>popFromStack(Integer.class);
						long a = frame.<Long>popFromStack(Long.class);
						frame.pushToStack(a >> (((long)b) & 0x3F));
					}
					break;
					
					// Store long to local variable
				case InstructionIndex.LSTORE:
				case InstructionIndex.WIDE_LSTORE:
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
						monitor().exit(thread, true);
					break;
					
					// Allocate multi-dimensional array
				case InstructionIndex.MULTIANEWARRAY:
					{
						// Determine component type and dimension count
						SpringClass ccl = this.resolveClass(
							inst.<ClassName>argument(0, ClassName.class));
						int n = inst.<Integer>argument(1, Integer.class);
						
						// Pop values into array
						int[] pops = new int[n];
						for (int i = n - 1; i >= 0; i--)
							pops[i] = frame.<Integer>popFromStack(
								Integer.class);
						
						// Call method within the class library since it is
						// easier, becuse this is one super complex
						// instruction
						frame.pushToStack(
							this.invokeMethod(true, new ClassName(
							"cc/squirreljme/runtime/cldc/lang/ArrayUtils"),
							new MethodNameAndType("multiANewArray",
								"(Ljava/lang/Class;I[I)Ljava/lang/Object;"),
							this.asVMObject(ccl), 0, this.asVMObject(pops)));
						
						// Exception to be handled?
						if (this.__checkException())
							return;
					}
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
						// {@squirreljme.error BK2q Cannot pop category two
						// value from stack.}
						Object val = frame.popFromStack();
						if (val instanceof Long || val instanceof Double)
							throw new SpringVirtualMachineException("BK2q");
					}
					break;
					
					// Pop two cat1s or one cat2
				case InstructionIndex.POP2:
					{
						// Pop one value, if it is a long or double then only
						// pop one
						Object val = frame.popFromStack();
						if (!(val instanceof Long || val instanceof Double))
						{
							// {@squirreljme.error BK2r Cannot pop a category
							// one then category two type.}
							val = frame.popFromStack();
							if (val instanceof Long || val instanceof Double)
								throw new SpringVirtualMachineException(
									"BK2r");
						}
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
						
						// {@squirreljme.error BK2s Cannot store value into
						// null reference.}
						if (ref == SpringNullObject.NULL)
							throw new SpringNullPointerException("BK2s");
						
						// {@squirreljme.error BK2t Cannot store value into
						// this instance because it not a simple object.}
						if (!(ref instanceof SpringSimpleObject))
							throw new SpringIncompatibleClassChangeException(
								"BK2t");
						SpringSimpleObject sso = (SpringSimpleObject)ref;
						
						// {@squirreljme.error BK2u Cannot store value into
						// a field which belongs to another class.}
						if (!this.loadClass(ssf.inClass()).isAssignableFrom(
							sso.type()))
							throw new SpringClassCastException("BK2u");
						
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
					
					// Swap top two cat1 stack entries
				case InstructionIndex.SWAP:
					{
						Object v1 = frame.popFromStack(),
							v2 = frame.popFromStack();
						
						// {@squirreljme.error BK2v Cannot swap category
						// two types.}
						if (v1 instanceof Long || v1 instanceof Double ||
							v2 instanceof Long || v2 instanceof Double)
							throw new SpringClassCastException("BK2v");
						
						frame.pushToStack(v1);
						frame.pushToStack(v2);
					}
					break;
				
					// {@squirreljme.error BK2w Reserved instruction. (The
					// instruction)}
				case InstructionIndex.BREAKPOINT:
				case InstructionIndex.IMPDEP1:
				case InstructionIndex.IMPDEP2:
					throw new SpringVirtualMachineException(String.format(
						"BK2w %s", inst));
					
					// {@squirreljme.error BK2x Unimplemented operation.
					// (The instruction)}
				default:
					throw new SpringVirtualMachineException(String.format(
						"BK2x %s", inst));
			}
		}
		
		// Arithmetic exception, a divide by zero happened somewhere
		catch (ArithmeticException e)
		{
			// PC converts?
			nextpc = this.__handleException(
				(SpringObject)this.asVMObject(new SpringArithmeticException(
				e.getMessage())));
			
			// Do not set PC address?
			if (nextpc < 0)
				return;
		}
		
		// Use the original exception, just add a suppression note on it since
		// that is the simplest action
		catch (SpringException e)
		{
			// Do not add causes or do anything if this was already thrown
			if ((e instanceof SpringFatalException) ||
				(e instanceof SpringMachineExitException))
				throw e;
			
			// Now the exception is either converted or tossed for failure
			// Is this a convertable exception on the VM?
			if (e instanceof SpringConvertableThrowable)
			{
				// PC converts?
				nextpc = this.__handleException(
					(SpringObject)this.asVMObject(e));
				
				// Do not set PC address?
				if (nextpc < 0)
					return;
			}
			
			// Not a wrapped exception, kill the VM
			else
			{
				// Kill the VM
				this.machine.exitNoException(127);
				
				// Print the stack trace
				thread.printStackTrace(System.err);
				
				// Where is this located?
				SpringMethod inmethod = frame.method();
				ClassName inclassname = inmethod.inClass();
				SpringClass inclass = machine.classLoader().loadClass(
					inclassname);
				
				// Location information if debugging is used, this makes it
				// easier to see exactly where failed code happened
				String onfile = inclass.file().sourceFile();
				int online = code.lineOfAddress(pc);
				
				// {@squirreljme.error BK2y An exception was thrown in the
				// virtual machine while executing the specified location.
				// (The class; The method; The program counter; The file in
				// source code, null means it is unknown; The line in source
				// code, negative values are unknown; The instruction)}
				e.addSuppressed(new SpringVirtualMachineException(
					String.format("BK2y %s %s %d %s %d %s", inclassname,
					inmethod.nameAndType(), pc, onfile, online, inst)));
				
				// {@squirreljme.error BK2z Fatal VM exception.}
				throw new SpringFatalException("BK2z", e);
			}
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
	private void __vmInvokeInterface(Instruction __i, SpringThread __t,
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
		
		// {@squirreljme.error BK30 Could not access the target
		// method for interface invoke. (The target method)}
		if (!this.checkAccess(refmethod))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK30 %s", ref));
		
		// Load arguments, includes the instance it acts on
		int nargs = refmethod.nameAndType().type().argumentCount() + 1;
		Object[] args = new Object[nargs];
		for (int i = nargs - 1; i >= 0; i--)
			args[i] = __f.popFromStack();
			
		// {@squirreljme.error BK31 Instance object for interface invoke is
		// null.}
		SpringObject onthis = (SpringObject)args[0];
		if (onthis == null || onthis == SpringNullObject.NULL)
			throw new SpringNullPointerException("BK31");
			
		// {@squirreljme.error BK32 Cannot invoke the method in the object
		// because it is of the wrong type. (The reference class; The class
		// of the target object; The first argument)}
		SpringClass objclass = onthis.type();
		if (objclass == null || !refclass.isAssignableFrom(objclass))
			throw new SpringClassCastException(
				String.format("BK32 %s %s %s", refclass, objclass, args[0]));
		
		// Relookup the method since we need to the right one! Then invoke it
		__t.enterFrame(objclass.lookupMethod(false, ref.memberNameAndType()),
			args);
	}
	
	/**
	 * Converts an object to a unique pointer.
	 *
	 * @param __p The object to convert.
	 * @return The resulting pointer.
	 * @since 2019/06/16
	 */
	@Deprecated
	public final int uniqueObjectToPointer(SpringObject __p)
	{
		// Null reference?
		if (__p == SpringNullObject.NULL)
			return 0;
		
		// Return the base of the pointer area
		return __p.pointerArea().base;
	}
	
	/**
	 * Converts an object to a unique pointer.
	 *
	 * @param __p The object to convert.
	 * @return The resulting pointer.
	 * @since 2019/06/16
	 */
	@Deprecated
	public final SpringObject uniquePointerToObject(int __p)
	{
		// Null reference?
		if (__p == 0)
			return SpringNullObject.NULL;
		
		return this.machine.pointers.findObject(__p);
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
	private void __vmInvokeSpecial(Instruction __i, SpringThread __t,
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
		
		// {@squirreljme.error BK34 Could not access the target
		// method for special invoke. (The target method)}
		if (!this.checkAccess(refmethod))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK34 %s", ref));
		
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
		
		// {@squirreljme.error BK35 Instance object for special invoke is
		// null.}
		SpringObject onthis = (SpringObject)args[0];
		if (onthis == null || onthis == SpringNullObject.NULL)
			throw new SpringNullPointerException("BK35");
		
		// These modify the action to be performed
		boolean insame = (currentclass == refclass),
			insuper = currentclass.isSuperClass(refclass),
			isinit = refmethod.name().isInstanceInitializer(),
			isprivate = refmethod.flags().isPrivate();
		
		// {@squirreljme.error BK36 Cannot call private method that is not
		// in the same class. (The method reference)}
		if (isprivate && !insame)
			throw new SpringIncompatibleClassChangeException(
				String.format("BK36 %s", ref));
		
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
	private void __vmInvokeStatic(Instruction __i, SpringThread __t,
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
		
		// {@squirreljme.error BK37 Could not access the target
		// method for static invoke. (The target method)}
		if (!this.checkAccess(refmethod))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK37 %s", ref));
		
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
			Object rv;
			
			// Measure the performance of native calls
			try
			{
				this.thread.profiler.enterFrame(ref.className().toString(),
					ref.memberName().toString(), ref.memberType().toString());
				
				rv = this.nativeMethod(ref.className(),
					ref.memberName(), type, args);
			}
			finally
			{
				this.thread.profiler.exitFrame();
			}
			
			// Push native object to the stack
			if (type.hasReturnValue())
				__f.pushToStack(this.asVMObject(rv, true));
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
	private void __vmInvokeVirtual(Instruction __i, SpringThread __t,
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
		
		// {@squirreljme.error BK38 Could not access the target
		// method for virtual invoke. (The target method)}
		if (!this.checkAccess(refmethod))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK38 %s", ref));
		
		// Load arguments, includes the instance it acts on
		int nargs = refmethod.nameAndType().type().argumentCount() + 1;
		Object[] args = new Object[nargs];
		for (int i = nargs - 1; i >= 0; i--)
			args[i] = __f.popFromStack();
		
		// {@squirreljme.error BK39 Instance object for virtual invoke is
		// null.}
		SpringObject onthis = (SpringObject)args[0];
		if (onthis == null || onthis == SpringNullObject.NULL)
			throw new SpringNullPointerException("BK39");
		
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
	private void __vmNew(Instruction __i, SpringThread.Frame __f)
		throws NullPointerException
	{
		if (__i == null || __f == null)
			throw new NullPointerException("NARG");
		
		// Lookup class we want to allocate
		ClassName allocname;
		SpringClass toalloc = this.loadClass((allocname =
			__i.<ClassName>argument(0, ClassName.class)));
		
		// {@squirreljme.error BK3a Cannot allocate an instance of the given
		// class because it cannot be accessed. (The class to allocate)}
		if (!this.checkAccess(toalloc))
			throw new SpringIncompatibleClassChangeException(
				String.format("BK3a %s", allocname));
		
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
	private void __vmReturn(SpringThread __thread, Object __value)
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

