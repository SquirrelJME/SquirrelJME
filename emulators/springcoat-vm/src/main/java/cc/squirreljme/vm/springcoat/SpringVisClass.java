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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
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
	implements SpringClass
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringObject classObject()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringClass componentType()
	{
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public int fieldLookupBase()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringField[] fieldsOnlyThisClass()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringField[] fieldTable()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public ClassFile file()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public int findMethodIndex(SpringMethod __method)
		throws NullPointerException
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public int instanceFieldCount()
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public boolean isCompatible(Object __v)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public boolean isEnum()
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringMethod lookupDefaultConstructor()
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringField lookupField(boolean __static, FieldNameAndType __nat)
		throws NullPointerException, SpringNoSuchFieldException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringField lookupField(int __fieldDx)
		throws SpringNoSuchFieldException
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
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
		throw Debugging.todo();
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
		// There are no methods
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringMethod[] methodLookup()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public int methodLookupBase()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public Map<MethodNameAndType, SpringMethod> methods()
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public void setInitialized()
		throws SpringVirtualMachineException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public int staticFieldBase()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringFieldStorage[] staticFields()
	{
		throw Debugging.todo();
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
