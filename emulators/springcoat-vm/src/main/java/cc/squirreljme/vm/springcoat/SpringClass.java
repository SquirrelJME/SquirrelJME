// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.springcoat.exceptions.SpringClassFormatException;
import cc.squirreljme.vm.springcoat.exceptions.SpringIncompatibleClassChangeException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNoSuchFieldException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNoSuchMethodException;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassFlags;
import net.multiphasicapps.classfile.ClassName;
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
public interface SpringClass
	extends HasAccessibleFlags
{
	/**
	 * Returns the class loader which loaded this class.
	 * 
	 * @return The class loader which loaded this.
	 * @throws IllegalStateException If it was not set or GCed.
	 * @since 2021/03/15
	 */
	SpringClassLoader classLoader()
		throws IllegalStateException;
	
	/**
	 * Returns the {@link Class} object.
	 * 
	 * @return The {@link Class} object.
	 * @since 2021/04/19
	 */
	SpringObject classObject();
	
	/**
	 * Returns the component type of this class.
	 *
	 * @return The component type.
	 * @since 2018/09/27
	 */
	SpringClass componentType();
	
	/**
	 * Returns the number of array dimensions.
	 *
	 * @return The number of dimensions.
	 * @since 2018/09/28
	 */
	int dimensions();
	
	/**
	 * Returns the field lookup.
	 * 
	 * @return The field lookup.
	 * @since 2021/04/16
	 */
	SpringField[] fieldLookup();
	
	/**
	 * The base index for fields.
	 *
	 * @return The field base index.
	 * @since 2024/08/02
	 */
	int fieldLookupBase();
	
	/**
	 * Returns the fields which are only declared in this class.
	 *
	 * @return The fields only declared in this class.
	 * @since 2018/09/09
	 */
	SpringField[] fieldsOnlyThisClass();
	
	/**
	 * Returns the table of fields used for this class which includes the
	 * super classes.
	 *
	 * @return The field table used for this class.
	 * @since 2018/09/16
	 */
	SpringField[] fieldTable();
	
	/**
	 * Returns the associated class file.
	 *
	 * @return The class file.
	 * @since 2018/09/08
	 */
	ClassFile file();
	
	/**
	 * Returns the method index of the given method.
	 * 
	 * @param __method The method to look for.
	 * @return The method index of the given method.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	int findMethodIndex(SpringMethod __method)
		throws NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/09
	 */
	@Override
	ClassFlags flags();
	
	/**
	 * Returns the JAR this class is in.
	 *
	 * @return The JAR this class is in.
	 * @since 2018/10/07
	 */
	VMClassLibrary inJar();
	
	/**
	 * Returns the number of instance fields this class stores. This is for
	 * the most part the size of the given class.
	 *
	 * @return The number of instance fields this class stores.
	 * @since 2018/09/08
	 */
	int instanceFieldCount();
	
	/**
	 * Returns the interfaces that this class implements.
	 *
	 * @return The implemented interfaces.
	 * @since 2018/09/08
	 */
	SpringClass[] interfaceClasses();
	
	/**
	 * Is this an array?
	 *
	 * @return If this is an array.
	 * @since 2018/09/27
	 */
	boolean isArray();
	
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
	boolean isAssignableFrom(SpringClass __o)
		throws NullPointerException;
	
	/**
	 * Checks if the given value is compatible with this class.
	 *
	 * @param __v The value to check.
	 * @return If it is compatible or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/16
	 */
	boolean isCompatible(Object __v)
		throws NullPointerException;
	
	/**
	 * Checks if this is an enumeration.
	 * 
	 * @return If this is an enumeration.
	 * @since 2020/06/28
	 */
	boolean isEnum();
	
	/**
	 * Has this class been initialized?
	 *
	 * @return If the class has been initialized.
	 * @since 2018/09/08
	 */
	boolean isInitialized();
	
	/**
	 * Is this the object class?
	 *
	 * @return If this is the object class.
	 * @since 2018/11/04
	 */
	boolean isObjectClass();
	
	/**
	 * Is this a primitive type?
	 *
	 * @return If this is a primitive type.
	 * @since 2024/01/20
	 */
	boolean isPrimitive();
	
	/**
	 * Checks if the given class is a super class of this class.
	 *
	 * @param __cl The class to check.
	 * @return {@code true} if it is a superclass.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/09
	 */
	boolean isSuperClass(SpringClass __cl)
		throws NullPointerException;
	
	/**
	 * Looks up the method which acts as the default constructor for instance
	 * objects.
	 *
	 * @return The default constructor for the object or {@code null} if there
	 * is none.
	 * @since 2018/09/08
	 */
	SpringMethod lookupDefaultConstructor();
	
	/**
	 * Locates the given field in this class.
	 *
	 * @param __static Is the field static?
	 * @param __name The name of the field.
	 * @param __desc The type of the field.
	 * @return The field.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNoSuchFieldException If the field does not exist.
	 * @since 2018/11/19
	 */
	SpringField lookupField(boolean __static, String __name, String __desc)
		throws NullPointerException, SpringNoSuchFieldException;
	
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
	SpringField lookupField(boolean __static, FieldName __name,
		FieldDescriptor __desc)
		throws NullPointerException, SpringNoSuchFieldException;
	
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
	SpringField lookupField(boolean __static, FieldNameAndType __nat)
		throws NullPointerException, SpringNoSuchFieldException;
	
	/**
	 * Returns a field for the given index.
	 * 
	 * @param __fieldDx The field index.
	 * @return The field for the given index.
	 * @throws SpringNoSuchFieldException If the field does not exist.
	 * @since 2021/04/16
	 */
	SpringField lookupField(int __fieldDx)
		throws SpringNoSuchFieldException;
	
	/**
	 * Locates the given method in the class.
	 *
	 * @param __static Is the method static?
	 * @param __name The name of the method.
	 * @param __desc The descriptor of the method.
	 * @return The method which was found.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringIncompatibleClassChangeException If the target method
	 * does not match static-ness.
	 * @throws SpringNoSuchMethodException If the specified method does not
	 * exist.
	 * @since 2018/09/03
	 */
	SpringMethod lookupMethod(boolean __static, MethodName __name,
		MethodDescriptor __desc)
		throws NullPointerException, SpringIncompatibleClassChangeException,
			SpringNoSuchMethodException;
	
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
	SpringMethod lookupMethod(boolean __static, MethodNameAndType __nat)
		throws NullPointerException, SpringIncompatibleClassChangeException,
			SpringNoSuchMethodException;
	
	/**
	 * Looks up a method with the given index.
	 * 
	 * @param __methodDx The method index.
	 * @return The given method.
	 * @throws SpringNoSuchMethodException If no method by the given index
	 * exists.
	 * @since 2021/04/15
	 */
	SpringMethod lookupMethod(int __methodDx)
		throws SpringNoSuchMethodException;
	
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
	SpringMethod lookupMethodNonVirtual(MethodNameAndType __nat)
		throws NullPointerException, SpringIncompatibleClassChangeException,
			SpringNoSuchMethodException;
	
	/**
	 * Returns the method lookup table.
	 * 
	 * @return The method lookup table.
	 * @since 2021/04/15
	 */
	SpringMethod[] methodLookup();
	
	/**
	 * Returns the base index for method lookup within this class.
	 *
	 * @return The method lookup base.
	 * @since 2024/08/02
	 */
	int methodLookupBase();
	
	/**
	 * Returns the methods to use.
	 *
	 * @return The methods to use.
	 * @since 2024/08/03
	 */
	Map<MethodNameAndType, SpringMethod> methods();
	
	/**
	 * Returns the name of this class.
	 *
	 * @return The name of this class.
	 * @since 2018/09/08
	 */
	ClassName name();
	
	/**
	 * Sets the class object.
	 *
	 * @param __rv The object to use.
	 * @since 2024/08/02
	 */
	void setClassObject(SpringObject __rv);
	
	/**
	 * Sets the class as initialized.
	 *
	 * @throws SpringVirtualMachineException If the class has already been
	 * initialized.
	 * @since 2018/09/08
	 */
	void setInitialized()
		throws SpringVirtualMachineException;
	
	/**
	 * Returns the base for static fields.
	 *
	 * @return The static field base.
	 * @since 2024/08/02
	 */
	int staticFieldBase();
	
	/**
	 * Returns the static field storage.
	 *
	 * @return The static field storage.
	 * @since 2024/08/02
	 */
	SpringFieldStorage[] staticFields();
	
	/**
	 * Returns the super class of this class.
	 *
	 * @return The super class of this class.
	 * @since 2018/09/08
	 */
	SpringClass superClass();
}

