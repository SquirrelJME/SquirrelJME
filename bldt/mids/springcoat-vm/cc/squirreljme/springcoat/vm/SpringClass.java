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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Field;
import net.multiphasicapps.classfile.FieldNameAndType;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This is a representation of a class file as it is seen by the virtual
 * machine, it is intended to remain simple and only refer to what is needed
 * for the machine to run.
 *
 * @since 2018/07/21
 */
public final class SpringClass
{
	/** The name of this class. */
	protected final ClassName name;
	
	/** The class file data. */
	protected final ClassFile file;
	
	/** The super class. */
	protected final SpringClass superclass;
	
	/** The number of instance fields that exist. */
	protected final int instancefieldcount;
	
	/** Interface classes. */
	private final SpringClass[] _interfaceclasses;
	
	/** Methods which exist in this class, includes statics for this only. */
	private final Map<MethodNameAndType, SpringMethod> _methods =
		new HashMap<>();
	
	/** Fields which exist in this class, only includes this class fields */
	private final Map<FieldNameAndType, SpringField> _fields =
		new HashMap<>();
	
	/** The table of fields defined in this class, includes super classes. */
	private final SpringField[] _fieldtable;
	
	/** Has this class been initialized? */
	private volatile boolean _initialized;
	
	/**
	 * Initializes the spring class.
	 *
	 * @param __super The super class of this class.
	 * @param __interfaces The the interfaces this class implements.
	 * @param __cf The class file for this class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/07/21
	 */
	SpringClass(SpringClass __super, SpringClass[] __interfaces,
		ClassFile __cf)
		throws NullPointerException
	{
		if (__interfaces == null || __cf == null)
			throw new NullPointerException("NARG");
		
		ClassName name = __cf.thisName();
		this.name = name;
		
		this.file = __cf;
		this.superclass = __super;
		
		// Check
		this._interfaceclasses = (__interfaces = __interfaces.clone());
		for (SpringClass x : __interfaces)
			if (x == null)
				throw new NullPointerException("NARG");
		
		// Go through and initialize methods declared in this class
		Map<MethodNameAndType, SpringMethod> methods = this._methods;
		for (Method m : __cf.methods())
			if (null != methods.put(m.nameAndType(),
				new SpringMethod(name, m)))
			{
				// {@squirreljme.error BK06 Duplicated method in class. (The
				// method)}
				throw new SpringClassFormatException(name, String.format(
					"BK06 %s", m.nameAndType()));
			}
		
		// Fields that are defined in super classes must be allocated, stored,
		// and indexed appropriately so that way casting between types and
		// accessing other fields is actually valid
		int superfieldcount = (__super == null ? 0 :
			__super.instancefieldcount);
		int instancefieldcount = superfieldcount;
		
		// Initialize all of the fields as needed
		Map<FieldNameAndType, SpringField> fields = this._fields;
		List<SpringField> instfields = new ArrayList<>(fields.size());
		for (Field f : __cf.fields())
		{
			boolean isinstance = f.isInstance();
			
			// {@squirreljme.error BK0g Duplicated field in class. (The field)}
			SpringField sf;
			if (null != fields.put(f.nameAndType(),
				(sf = new SpringField(name, f,
					(isinstance ? instancefieldcount++ : -1)))))
				throw new SpringClassFormatException(name, String.format(
					"BK0g %s", f.nameAndType()));
			
			// Used to build our part of the field table
			if (isinstance)
				instfields.add(sf);
		}
		
		// Each field is referenced by an index rather than a map, this is
		// more efficient for instances and additionally still allows for
		// sub-classes to declare fields as needed.
		SpringField[] fieldtable = new SpringField[instancefieldcount];
		this._fieldtable = fieldtable;
		
		// Copy the super class field table, since technically all of the
		// fields in the super class are a part of this class.
		if (__super != null)
		{
			SpringField[] supertable = __super._fieldtable;
			for (int i = 0; i < superfieldcount; i++)
				fieldtable[i] = supertable[i];
		}
		
		// Store all of the instance fields
		for (int i = superfieldcount, p = 0, pn = instfields.size();
			p < pn; i++, p++)
			fieldtable[i] = instfields.get(p);
		
		// Used to quickly determine how big to set storage for a class
		this.instancefieldcount = instancefieldcount;
		
		// Go through super and interfaces and add non-static methods which
		for (int i = 0, n = __interfaces.length; i <= n; i++)
		{
			// The class to look within
			SpringClass lookin = (i == 0 ? __super : __interfaces[i - 1]);
			if (lookin == null)
				continue;
			
			// Go through class methods
			for (Map.Entry<MethodNameAndType, SpringMethod> e :
				lookin._methods.entrySet())
			{
				MethodNameAndType k = e.getKey();
				SpringMethod v = e.getValue();
				
				// Ignore static and initializer methods
				if (v.isStatic() || v.isInstanceInitializer() ||
					v.isStaticInitializer())
					continue;
				
				// If the method does not exist in the table then it gets added
				// otherwise it is effectively replaced
				if (!methods.containsKey(k))
					methods.put(k, v);
			}
		}
		
		// Debug
		todo.DEBUG.note("Class %s (size=%d, fields=%d, methods=%d).", name,
			instancefieldcount,
			fields.size(),
			methods.size());
	}
	
	/**
	 * Returns the fields which are only declared in this class.
	 *
	 * @return The fields only declared in this class.
	 * @since 2018/09/09
	 */
	public final SpringField[] fieldsOnlyThisClass()
	{
		Map<FieldNameAndType, SpringField> fields = this._fields;
		return fields.values().<SpringField>toArray(
			new SpringField[fields.size()]);
	}
	
	/**
	 * Returns the associated class file.
	 *
	 * @return The class file.
	 * @since 2018/09/08
	 */
	public final ClassFile file()
	{
		return this.file;
	}
	
	/**
	 * Returns the number of instance fields this class stores. This is for
	 * the most part the size of the given class.
	 *
	 * @return The number of instance fields this class stores.
	 * @since 2018/09/08
	 */
	public final int instanceFieldCount()
	{
		return this.instancefieldcount;
	}
	
	/**
	 * Returns the interfaces that this class implements.
	 *
	 * @return The implemented interfaces.
	 * @since 2018/09/08
	 */
	public final SpringClass[] interfaceClasses()
	{
		return this._interfaceclasses.clone();
	}
	
	/**
	 * Has this class been initialized?
	 *
	 * @return If the class has been initialized.
	 * @since 2018/09/08
	 */
	public final boolean isInitialized()
	{
		return this._initialized;
	}
	
	/**
	 * Looks up the method which acts as the default constructor for instance
	 * objects.
	 *
	 * If an object has no constructor then a constructor in a base class is
	 * used.
	 *
	 * @return The default constructor for the object.
	 * @since 2018/09/08
	 */
	public final SpringMethod lookupDefaultConstructor()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Locates the given method in the class.
	 *
	 * @param __static Is the method static?
	 * @param __name The name of the method.
	 * @param __desc The descriptor of the method.
	 * @return The method which was found.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNoSuchMethodException If the specified method does not
	 * exist.
	 * @since 2018/09/03
	 */
	public final SpringMethod lookupMethod(boolean __static, MethodName __name,
		MethodDescriptor __desc)
		throws NullPointerException, SpringNoSuchMethodException
	{
		if (__name == null || __desc == null)
			throw new NullPointerException("NARG");
		
		return this.lookupMethod(__static, new MethodNameAndType(__name,
			__desc));
	}
	
	/**
	 * Locates the given method in the class.
	 *
	 * @param __static Is the method static?
	 * @param __nat The name and type of the method.
	 * @return The method which was found.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNoSuchMethodException If the specified method does not
	 * exist.
	 * @since 2018/09/03
	 */
	public final SpringMethod lookupMethod(boolean __static,
		MethodNameAndType __nat)
		throws NullPointerException, SpringNoSuchMethodException
	{
		if (__nat == null)
			throw new NullPointerException("NARG");
		
		// Debug
		todo.DEBUG.note("Looking up method %s::%s (static=%b)", this.name,
			__nat, __static);
		
		// {@squirreljme.error BK07 The specified method does not exist.
		// (The class which was looked in; The name and type of the method)} 
		SpringMethod rv = this._methods.get(__nat);
		if (rv == null)
			throw new SpringNoSuchMethodException(String.format("BK07 %s %s",
				this.name, __nat));
		
		// {@squirreljme.error BK08 The specified method exists in the class
		// however it does not match being static. (The class the method is in;
		// The name and type of the method; If a static method was requested)}
		if (rv.isStatic() != __static)
			throw new SpringNoSuchMethodException(String.format("BK08 %s %s",
				this.name, __nat, __static));
		
		return rv;
	}
	
	/**
	 * Returns the name of this class.
	 *
	 * @return The name of this class.
	 * @since 2018/09/08
	 */
	public final ClassName name()
	{
		return this.name;
	}
	
	/**
	 * Sets the class as initialized.
	 *
	 * @throws SpringVirtualMachineException If the class has already been
	 * initialized.
	 * @since 2018/09/08
	 */
	public final void setInitialized()
		throws SpringVirtualMachineException
	{
		// {@squirreljme.error BK0f Class attempted to be initialized twice.
		// (This class)}
		if (this._initialized)
			throw new SpringVirtualMachineException(String.format(
				"BK0f %s", this.name));
		
		this._initialized = true;
	}
	
	/**
	 * Returns the super class of this class.
	 *
	 * @return The super class of this class.
	 * @since 2018/09/08
	 */
	public final SpringClass superClass()
	{
		return this.superclass;
	}
}

