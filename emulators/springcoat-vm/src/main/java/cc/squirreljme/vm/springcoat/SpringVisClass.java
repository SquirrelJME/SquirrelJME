// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.springcoat.exceptions.SpringClassNotFoundException;
import cc.squirreljme.vm.springcoat.exceptions.SpringIncompatibleClassChangeException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNoSuchFieldException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNoSuchMethodException;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassFlag;
import net.multiphasicapps.classfile.ClassFlags;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.FieldNameAndType;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Virtualized class wrapping a class on the host.
 *
 * @since 2024/08/04
 */
public class SpringVisClass
	extends SpringBaseClass
{
	/** The class flags for a vis class. */
	public static final ClassFlags CLASS_FLAGS =
		new ClassFlags(ClassFlag.PUBLIC, ClassFlag.FINAL,
			ClassFlag.SYNTHETIC);
	
	/** The machine to use. */
	protected final SpringMachine machine;
	
	/** The VisName of this class. */
	protected final ClassName visName;
	
	/** The real class to wrap. */
	protected final Class<?> real;
	
	/** The interfaces this class implements. */
	private volatile SpringClass[] _interfaceClasses;
	
	/** The super class, which is always {@link Object}. */
	private volatile SpringClass _superClass;
	
	/**
	 * Initializes the virtual class. 
	 *
	 * @param __machine The machine this is under.
	 * @param __real The real class being used.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/04
	 */
	public SpringVisClass(SpringMachine __machine, Class<?> __real)
		throws NullPointerException
	{
		if (__machine == null || __real == null)
			throw new NullPointerException("NARG");
		
		this.visName = new ClassName("$$VIS$$/" +
			__real.getName().replace(".", "/")
				.replace('[', '_')
				.replace(';', '_'));
		this.machine = __machine;
		this.real = __real;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringClassLoader classLoader()
		throws IllegalStateException
	{
		return this.machine.classLoader();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringObject classObject()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringClass componentType()
	{
		// Not an array? Then there is no component
		Class<?> real = this.real;
		if (!real.isArray())
			return null;
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public int dimensions()
	{
		// Need to count the number of brackets at the start
		Class<?> real = this.real;
		if (real.isArray())
		{
			String name = real.getName();
			for (int i = 0, n = name.length(); i < n; i++)
				if (name.charAt(i) != '[')
					return i;
			
			// Is all just array????
			return name.length() - 1;
		}
		
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringField[] fieldLookup()
	{
		// Use object class instead
		return this.superClass().fieldLookup();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public int fieldLookupBase()
	{
		// Always based at zero, since this virtual class has no fields
		// or methods, everything is inherited from object
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringField[] fieldsOnlyThisClass()
	{
		// VisClasses have no fields
		return new SpringField[0];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringField[] fieldTable()
	{
		// VisClasses have no fields
		return new SpringField[0];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public ClassFile file()
	{
		// VisClasses have no class file
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public int findMethodIndex(SpringMethod __method)
		throws NullPointerException
	{
		// Use object class instead
		return this.superClass().findMethodIndex(__method);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public ClassFlags flags()
	{
		return SpringVisClass.CLASS_FLAGS;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public VMClassLibrary inJar()
	{
		// VisClasses are not in any Jars
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public int instanceFieldCount()
	{
		// VisClasses have no instances
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringClass[] interfaceClasses()
	{
		SpringMachine machine = this.machine;
		Class<?> real = this.real;
		synchronized (this)
		{
			// Already determined?
			SpringClass[] result = this._interfaceClasses;
			if (result != null)
				return result.clone();
			
			// Queue in classes to recursively get interfaces from
			Deque<Class<?>> realQueue = new ArrayDeque<>();
			realQueue.addLast(real);
			
			// Determine which VM oriented interfaces are available
			Set<SpringClass> build = new LinkedHashSet<>();
			while (!realQueue.isEmpty())
			{
				Class<?> at = realQueue.removeFirst();
				for (Class<?> atInterface : at.getInterfaces())
				{
					// Add interface because we need to go into it
					realQueue.addLast(atInterface);
					
					// Set as an implemented class
					try
					{
						build.add(machine.classLoader()
							.loadClass(ClassName.fromRuntimeName(
								atInterface.getName())));
					}
					catch (SpringClassNotFoundException __ignore)
					{
						// If the class is not found, do nothing
					}
				}
			}
			
			// Debug
			/*Debugging.debugNote("VIS.interfaceClasses(%s) = %s",
				real, build);*/
			
			// Cache and use it
			result = build.toArray(new SpringClass[build.size()]);
			this._interfaceClasses = result;
			return result.clone();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public boolean isArray()
	{
		return this.real.isArray();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public boolean isAssignableFrom(SpringClass __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// As this is shared in multiple places, do use split logic
		return SpringVMClass.isAssignableFrom(this, __o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public boolean isCompatible(Object __v)
		throws NullPointerException
	{
		if (__v instanceof SpringObject)
			return this.isAssignableFrom(((SpringObject)__v).type());
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public boolean isEnum()
	{
		return this.real.isEnum();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public boolean isInitialized()
	{
		// VisClasses are always initialized
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public boolean isObjectClass()
	{
		// VisClasses is always not the Object class
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public boolean isPrimitive()
	{
		// VisClasses are never primitive
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public boolean isSuperClass(SpringClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// This is shared logic
		return SpringVMClass.isSuperClass(this, __cl);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringMethod lookupDefaultConstructor()
	{
		// There is no default constructor, these are constructed effectively
		// with magic
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringField lookupField(boolean __static, String __name,
		String __desc)
		throws NullPointerException, SpringNoSuchFieldException
	{
		// Use object class instead
		return this.superClass().lookupField(__static, __name, __desc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringField lookupField(boolean __static, FieldName __name,
		FieldDescriptor __desc)
		throws NullPointerException, SpringNoSuchFieldException
	{
		// Use object class instead
		return this.superClass().lookupField(__static, __name, __desc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringField lookupField(boolean __static, FieldNameAndType __nat)
		throws NullPointerException, SpringNoSuchFieldException
	{
		// Use object class instead
		return this.superClass().lookupField(__static, __nat);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringField lookupField(int __fieldDx)
		throws SpringNoSuchFieldException
	{
		// Use object class instead
		return this.superClass().lookupField(__fieldDx);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringMethod lookupMethod(boolean __static, MethodName __name,
		MethodDescriptor __desc)
		throws NullPointerException, SpringIncompatibleClassChangeException,
			SpringNoSuchMethodException
	{
		// Use object class instead
		return this.superClass().lookupMethod(__static, __name, __desc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringMethod lookupMethod(boolean __static, MethodNameAndType __nat)
		throws NullPointerException, SpringIncompatibleClassChangeException,
		SpringNoSuchMethodException
	{
		// Use object class instead
		return this.superClass().lookupMethod(__static, __nat);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringMethod lookupMethod(int __methodDx)
		throws SpringNoSuchMethodException
	{
		// Use object class instead
		return this.superClass().lookupMethod(__methodDx);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringMethod lookupMethodNonVirtual(MethodNameAndType __nat)
		throws NullPointerException, SpringIncompatibleClassChangeException,
		SpringNoSuchMethodException
	{
		// Use object class instead
		return this.superClass().lookupMethodNonVirtual(__nat);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringMethod[] methodLookup()
	{
		// Use object class instead
		return this.superClass().methodLookup();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public int methodLookupBase()
	{
		// Always based at zero, since this virtual class has no fields
		// or methods, everything is inherited from object
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public Map<MethodNameAndType, SpringMethod> methods()
	{
		// Use object class instead
		return this.superClass().methods();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public ClassName name()
	{
		return this.visName;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public void setClassObject(SpringObject __rv)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public void setInitialized()
		throws SpringVirtualMachineException
	{
		// Does nothing, this is always initialized
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public int staticFieldBase()
	{
		// Always based at zero, since this virtual class has no fields
		// or methods, everything is inherited from object
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringFieldStorage[] staticFields()
	{
		// No static fields can be stored for VisClasses
		return new SpringFieldStorage[0];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringClass superClass()
	{
		// VisClasses always are based directly on object
		SpringClass result = this._superClass;
		if (result == null)
		{
			result = this.machine.classLoader().loadClass(
				new ClassName("java/lang/Object"));
			this._superClass = result;
		}
		
		return result;
	}
}
