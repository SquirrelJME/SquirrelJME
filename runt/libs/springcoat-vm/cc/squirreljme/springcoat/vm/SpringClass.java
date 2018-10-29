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
import net.multiphasicapps.classfile.ClassFlags;
import net.multiphasicapps.classfile.Field;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.FieldNameAndType;
import net.multiphasicapps.classfile.HasAccessibleFlags;
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
	implements HasAccessibleFlags
{
	/** The name of this class. */
	protected final ClassName name;
	
	/** The class file data. */
	protected final ClassFile file;
	
	/** The super class. */
	protected final SpringClass superclass;
	
	/** The number of instance fields that exist. */
	protected final int instancefieldcount;
	
	/** The special class index. */
	protected final int specialindex;
	
	/** The dimentions of this class. */
	protected final int dimensions;
	
	/** The component type. */
	protected final SpringClass component;
	
	/** The JAR this class is in. */
	protected final String injar;
	
	/** Interface classes. */
	private final SpringClass[] _interfaceclasses;
	
	/** Methods which exist in this class, includes statics for this only. */
	private final Map<MethodNameAndType, SpringMethod> _methods =
		new HashMap<>();
	
	/** Non-virtual instance methods. */
	private final Map<MethodNameAndType, SpringMethod> _nonvirtmethods =
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
	 * @param __si The special class index.
	 * @param __ct The component type.
	 * @param __injar The JAR this class is in.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/07/21
	 */
	SpringClass(SpringClass __super, SpringClass[] __interfaces,
		ClassFile __cf, int __si, SpringClass __ct, String __injar)
		throws NullPointerException
	{
		if (__interfaces == null || __cf == null)
			throw new NullPointerException("NARG");
		
		ClassName name = __cf.thisName();
		this.name = name;
		this.specialindex = __si;
		this.injar = __injar;
		this.file = __cf;
		this.superclass = __super;
		this.component = __ct;
		this.dimensions = name.dimensions();
		
		// Check
		this._interfaceclasses = (__interfaces = __interfaces.clone());
		for (SpringClass x : __interfaces)
			if (x == null)
				throw new NullPointerException("NARG");
		
		// Used for method location
		String filename = __cf.sourceFile();
		
		// Go through and initialize methods declared in this class
		Map<MethodNameAndType, SpringMethod> nvmeths = this._nonvirtmethods;
		Map<MethodNameAndType, SpringMethod> methods = this._methods;
		for (Method m : __cf.methods())
		{
			SpringMethod sm;
			if (null != methods.put(m.nameAndType(),
				(sm = new SpringMethod(name, m, filename))))
			{
				// {@squirreljme.error BK06 Duplicated method in class. (The
				// method)}
				throw new SpringClassFormatException(name, String.format(
					"BK06 %s", m.nameAndType()));
			}
			
			// Store only instance methods which are not static
			if (!m.flags().isStatic())
				nvmeths.put(m.nameAndType(), sm);
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
			boolean isinstance = f.flags().isInstance();
			
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
		// exist in sub-classes
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
				
				// Ignore static, initializer methods, and private methods
				if (v.flags().isStatic() || v.isInstanceInitializer() ||
					v.isStaticInitializer() || v.flags().isPrivate())
					continue;
				
				// If the method does not exist in the table then it gets added
				// otherwise it is effectively replaced
				if (!methods.containsKey(k))
					methods.put(k, v);
			}
		}
		
		// Debug
		/*todo.DEBUG.note("Class %s (size=%d, fields=%d, methods=%d).", name,
			instancefieldcount,
			fields.size(),
			methods.size());*/
	}
	
	/**
	 * Returns the component type of this class.
	 *
	 * @return The component type.
	 * @since 2018/09/27
	 */
	public final SpringClass componentType()
	{
		return this.component;
	}
	
	/**
	 * Returns the number of array dimensions.
	 *
	 * @return The number of dimensions.
	 * @since 2018/09/28
	 */
	public final int dimensions()
	{
		return this.name.dimensions();
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
	 * Returns the table of fields used for this class.
	 *
	 * @return The field table used for this class.
	 * @since 2018/09/16
	 */
	public final SpringField[] fieldTable()
	{
		return this._fieldtable.clone();
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
	 * {@inheritDoc}
	 * @since 2018/09/09
	 */
	@Override
	public final ClassFlags flags()
	{
		return this.file.flags();
	}
	
	/**
	 * Returns the JAR this class is in.
	 *
	 * @return The JAR this class is in.
	 * @since 2018/10/07
	 */
	public final String inJar()
	{
		return this.injar;
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
	 * Is this an array?
	 *
	 * @return If this is an array.
	 * @since 2018/09/27
	 */
	public final boolean isArray()
	{
		return this.name.isArray();
	}
	
	/**
	 * Checks if this class can be assigned from the target class, that is
	 * {@code this = (ThisClass)__o}.
	 *
	 * This is the same as {@link Class#isInstance(Object)} except it works
	 * only on class representations.
	 *
	 * @param __o The other class to check.
	 * @return If the other class can be assigned as this class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	public final boolean isAssignableFrom(SpringClass __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Go through target superclasses to find this class
		for (SpringClass r = __o; r != null; r = r.superclass)
		{
			if (r == this)
				return true;
		
			// Go through interfaces for the class to find this class
			for (SpringClass i : r._interfaceclasses)
				if (this.isAssignableFrom(i))
					return true;
		}
		
		// If this is an array and the other type is an array with the same
		// number of dimensions, then compare the base type so that say
		// Number[] is assignable from Integer[].
		int thisdims = this.dimensions(),
			otherdims = __o.dimensions();
		if (thisdims > 0 && thisdims == otherdims)
			if (this.__rootType().isAssignableFrom(__o.__rootType()))
				return true;
		
		return false;
	}
	
	/**
	 * Checks if the given value is compatible with this class.
	 *
	 * @param __v The value to check.
	 * @return If it is compatible or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/16
	 */
	public final boolean isCompatible(Object __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Primitive must match standard promoted type
		ClassName name = this.name;
		if (name.isPrimitive())
			switch (name.toString())
			{
				case "boolean":
				case "byte":
				case "short":
				case "char":
				case "int":
					return (__v instanceof Integer);
				
				case "long":
					return (__v instanceof Long);
				
				case "float":
					return (__v instanceof Float);
					
				case "double":
					return (__v instanceof Double);
				
				default:
					throw new RuntimeException("OOPS");
			}
			
		// Would be an assignable reference
		else if (__v instanceof SpringNullObject)
			return true;
		
		// Not primitive type, must be assignable
		else if (__v instanceof SpringObject)
			return this.isAssignableFrom(((SpringObject)__v).type());
		
		// Unknown
		else
			throw new RuntimeException("OOPS");
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
	 * Checks if the given class is a super class of the this class.
	 *
	 * @param __cl The class to check.
	 * @return {@code true} if it is a superclass.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/09
	 */
	public final boolean isSuperClass(SpringClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		for (SpringClass r = this.superclass; r != null; r = r.superClass())
			if (r == __cl)
				return true;
		
		return true;
	}
	
	/**
	 * Looks up the method which acts as the default constructor for instance
	 * objects.
	 *
	 * @return The default constructor for the object or {@code null} if there
	 * is none.
	 * @since 2018/09/08
	 */
	public final SpringMethod lookupDefaultConstructor()
	{
		return this.lookupMethodNonVirtual(new MethodNameAndType("<init>",
			"()V"));
	}
	
	/**
	 * Locates the given field in this class.
	 *
	 * @param __static Is the field static?
	 * @param __name The name of the field.
	 * @param __desc The type of the field.
	 * @return The field.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNoSuchFieldException If the field does not exist.
	 * @since 2018/09/09
	 */
	public final SpringField lookupField(boolean __static, FieldName __name,
		FieldDescriptor __desc)
		throws NullPointerException, SpringNoSuchFieldException
	{
		if (__name == null || __desc == null)
			throw new NullPointerException("NARG");
		
		return this.lookupField(__static,
			new FieldNameAndType(__name, __desc));
	}
	
	/**
	 * Locates the given field in this class.
	 *
	 * @param __static Is the field static?
	 * @param __nat The name and type of the field.
	 * @return The field.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNoSuchFieldException If the field does not exist.
	 * @since 2018/09/09
	 */
	public final SpringField lookupField(boolean __static,
		FieldNameAndType __nat)
		throws NullPointerException, SpringNoSuchFieldException
	{
		if (__nat == null)
			throw new NullPointerException("NARG");
		
		// Debug
		/*todo.DEBUG.note("Looking up field %s::%s (static=%b)", this.name,
			__nat, __static);*/
		
		// {@squirreljme.error BK0j The specified field does not exist.
		// (The class which was looked in; The name and type of the field)} 
		SpringField rv = this._fields.get(__nat);
		if (rv == null)
			throw new SpringNoSuchFieldException(String.format("BK0j %s %s",
				this.name, __nat));
		
		// {@squirreljme.error BK0k The specified field exists in the class
		// however it does not match being static. (The class the field is in;
		// The name and type of the method; If a static field was requested)}
		if (rv.isStatic() != __static)
			throw new SpringIncompatibleClassChangeException(String.format(
				"BK0k %s %s", this.name, __nat, __static));
		
		return rv;
	}
	
	/**
	 * Locates the given method in the class.
	 *
	 * @param __static Is the method static?
	 * @param __name The name of the method.
	 * @param __desc The descriptor of the method.
	 * @return The method which was found.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringIncompatibleClassChangeException If the target method
	 * does not match staticness.
	 * @throws SpringNoSuchMethodException If the specified method does not
	 * exist.
	 * @since 2018/09/03
	 */
	public final SpringMethod lookupMethod(boolean __static, MethodName __name,
		MethodDescriptor __desc)
		throws NullPointerException, SpringIncompatibleClassChangeException,
			SpringNoSuchMethodException
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
	 * @throws SpringIncompatibleClassChangeException If the target method
	 * does not match staticness.
	 * @throws SpringNoSuchMethodException If the specified method does not
	 * exist.
	 * @since 2018/09/03
	 */
	public final SpringMethod lookupMethod(boolean __static,
		MethodNameAndType __nat)
		throws NullPointerException, SpringIncompatibleClassChangeException,
			SpringNoSuchMethodException
	{
		if (__nat == null)
			throw new NullPointerException("NARG");
		
		// Debug
		/*todo.DEBUG.note("Looking up method %s::%s (static=%b)", this.name,
			__nat, __static);*/
		
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
			throw new SpringIncompatibleClassChangeException(String.format(
				"BK08 %s %s %b", this.name, __nat, __static));
		
		return rv;
	}
	
	/**
	 * Looks up the specified method non-virtually.
	 *
	 * @param __nat The name and type.
	 * @return The target method.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringIncompatibleClassChangeException If the target method
	 * is static.
	 * @throws SpringNoSuchMethodException If no method exists.
	 * @since 2018/09/09
	 */
	public final SpringMethod lookupMethodNonVirtual(MethodNameAndType __nat)
		throws NullPointerException, SpringIncompatibleClassChangeException,
			SpringNoSuchMethodException
	{
		if (__nat == null)
			throw new NullPointerException("NARG");
		
		// Debug
		/*todo.DEBUG.note("Looking up non-virtual method %s::%s.", this.name,
			__nat);*/
		
		// {@squirreljme.error BK0r The specified method does not exist, when
		// non-virtual lookup is used. (The class which was looked in; The
		// name and type of the method)} 
		SpringMethod rv = this._nonvirtmethods.get(__nat);
		if (rv == null)
			throw new SpringNoSuchMethodException(String.format("BK0r %s %s",
				this.name, __nat));
		
		// {@squirreljme.error BK0s Non-virtual method lookup found a static
		// method. (The class being looked in; The name and type requested)}
		if (rv.flags().isStatic())
			throw new SpringIncompatibleClassChangeException(String.format(
				"BK0s %s %s", this.name, __nat));
		
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
	 * Returns the special class index.
	 *
	 * @return The special class index.
	 * @since 2018/09/20
	 */
	public final int specialIndex()
	{
		return this.specialindex;
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final String toString()
	{
		return this.name.toString();
	}
	
	/**
	 * Returns the root type, the base of the component.
	 *
	 * @return The root type of this type.
	 * @since 2018/09/27
	 */
	private final SpringClass __rootType()
	{
		SpringClass rv = this;
		for (SpringClass r = this; r != null; r = r.component)
			rv = r;
		return rv;
	}
}

