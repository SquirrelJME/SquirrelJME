// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host.views;

import cc.squirreljme.jdwp.host.JDWPHostValue;
import cc.squirreljme.jdwp.JDWPLocalVariable;
import cc.squirreljme.jdwp.host.trips.JDWPTripBreakpoint;

/**
 * Represents a type.
 *
 * @since 2021/04/10
 */
public interface JDWPViewType
	extends JDWPViewHasInstance, JDWPViewValidObject
{
	/**
	 * Checks if this type can cast to the other type.
	 *
	 * @param __fromWhich The type to cast from.
	 * @param __toWhich The type to cast to.
	 * @return If the cast is possible.
	 * @since 2022/08/28
	 */
	boolean canCastTo(Object __fromWhich, Object __toWhich);
	
	/**
	 * Returns the class loader used for the given type, note that there
	 * should always only be a single class loader in Java ME per process.
	 * 
	 * @param __which Which type to get the classloader of?
	 * @return The class loader for the given type.
	 * @since 2021/04/20
	 */
	Object classLoader(Object __which);
	
	/**
	 * Returns the number of items that are in the type's constant pool. 
	 *
	 * @param __which Get the constant pool count of what?
	 * @return The count in the constant pool or {@code -1} if not valid.
	 * @since 2024/01/20
	 */
	int constantPoolCount(Object __which);
	
	/**
	 * Returns the raw data to the class constant pool. 
	 *
	 * @param __which Get the constant pool count of what?
	 * @return The raw bytes for the pool or {@code null} if not valid.
	 * @since 2024/01/20
	 */
	byte[] constantPoolRaw(Object __which);
	
	/**
	 * Returns the component type.
	 * 
	 * @param __which Get the component type of what?
	 * @return The component type or {@code null} if this is not an array.
	 * @since 2021/04/11
	 */
	Object componentType(Object __which);
	
	/**
	 * Returns the flags of the given class in standard Java class flag
	 * format.
	 * 
	 * @param __which Get the flags of which class?
	 * @return The class flags.
	 * @since 2021/04/11
	 */
	int flags(Object __which);
	
	/**
	 * Returns the interface types of the given type.
	 * 
	 * @param __which Get the interfaces of which type?
	 * @return The interfaces for this type.
	 * @since 2021/04/14
	 */
	Object[] interfaceTypes(Object __which);
	
	/**
	 * Checks if this is a valid field index.
	 * 
	 * @param __which Which class?
	 * @param __fieldDx The field index.
	 * @return If this is a valid field index in this class.
	 * @since 2021/04/14
	 */
	boolean isValidField(Object __which, int __fieldDx);
	
	/**
	 * Checks if this is a valid method index.
	 * 
	 * @param __which Which class?
	 * @param __methodDx The method index.
	 * @return If this is a valid method index in this class.
	 * @since 2021/04/13
	 */
	boolean isValidMethod(Object __which, int __methodDx);
	
	/**
	 * Returns the flags of a field, compatible with Java bitflags.
	 * 
	 * @param __which Which class to get the field from.
	 * @param __fieldDx The field ID.
	 * @return The field flags.
	 * @since 2021/04/14
	 */
	int fieldFlags(Object __which, int __fieldDx);
	
	/**
	 * Returns the name of a field.
	 * 
	 * @param __which Which class to get the field from.
	 * @param __fieldDx The field ID.
	 * @return The field name.
	 * @since 2021/04/14
	 */
	String fieldName(Object __which, int __fieldDx);
	
	/**
	 * Returns all of the fields within the class.
	 * 
	 * @param __which Which class to get the fields of.
	 * @return All of the field indexes within the class.
	 * @since 2021/04/14
	 */
	int[] fields(Object __which);
	
	/**
	 * Returns the field signature.
	 * 
	 * @param __which Which class to get from?
	 * @param __fieldDx The field index.
	 * @return The field signature.
	 * @since 2021/04/14
	 */
	String fieldSignature(Object __which, int __fieldDx);
	
	/**
	 * Enables watching on the given field. Note that once enabled these cannot
	 * be disabled using this method, so if access/modification are watched
	 * then this will be called twice with {@code false} and {@code true}.
	 * 
	 * @param __which Which type to watch?
	 * @param __fieldDx The field index to watch.
	 * @param __write If {@code true} then field write is enabled, otherwise
	 * field access is enabled.
	 * @since 2021/04/30
	 */
	void fieldWatch(Object __which, int __fieldDx, boolean __write);
	
	/**
	 * Returns the {@link Class} object instance for the given type.
	 * 
	 * @param __which Get the instance of which class?
	 * @return The {@link Class} object instance.
	 * @since 2021/04/19
	 */
	@Override
	Object instance(Object __which);
	
	/**
	 * Sets a method breakpoint.
	 * 
	 * @param __which Which class is the breakpoint in?
	 * @param __methodDx The method index.
	 * @param __codeDx The code index.
	 * @param __trip The trip to call when this code is executed.
	 * @since 2021/04/25
	 */
	void methodBreakpoint(Object __which, int __methodDx, int __codeDx,
		JDWPTripBreakpoint __trip);
	
	/**
	 * Returns the method byte code.
	 * 
	 * @param __which Which class to get from?
	 * @param __methodDx The method index.
	 * @return The byte code that exists within the method or {@code null} if
	 * there is no byte code.
	 * @since 2021/04/14
	 */
	byte[] methodByteCode(Object __which, int __methodDx);
	
	/**
	 * Returns the flags of a method, compatible with Java bitflags.
	 * 
	 * @param __which Which class to get the field from.
	 * @param __methodDx The method ID.
	 * @return The method flags.
	 * @since 2021/04/14
	 */
	int methodFlags(Object __which, int __methodDx);
	
	/**
	 * Returns the method line table, which maps locations to source code
	 * lines.
	 * 
	 * @param __which Which class to get from?
	 * @param __methodDx The method index.
	 * @return The line number table which matches lines to locations within
	 * the method, will be {@code null} if not available.
	 * @since 2021/04/14
	 */
	int[] methodLineTable(Object __which, int __methodDx);
	
	/**
	 * Returns the method location count.
	 * 
	 * @param __which Which class to get from?
	 * @param __methodDx The method index.
	 * @return The number of locations within the class or a negative value
	 * if it is unknown ({@code native}).
	 * @since 2021/04/14
	 */
	int methodLocationCount(Object __which, int __methodDx);
	
	/**
	 * Returns the name of a method.
	 * 
	 * @param __which Which class to get the field from.
	 * @param __methodDx The method ID.
	 * @return The method name.
	 * @since 2021/04/14
	 */
	String methodName(Object __which, int __methodDx);
	
	/**
	 * Returns the method indexes of the class.
	 * 
	 * @param __which Get the method indexes of which class?
	 * @return The method indexes.
	 * @since 2021/04/14
	 */
	int[] methods(Object __which);
	
	/**
	 * Returns the method signature.
	 * 
	 * @param __which Which class to get from?
	 * @param __methodDx The method index.
	 * @return The method signature.
	 * @since 2021/04/14
	 */
	String methodSignature(Object __which, int __methodDx);
	
	/**
	 * Returns the local variable table for the method.
	 * 
	 * @param __which Which class to get from?
	 * @param __methodDx The method index.
	 * @return The local variable table, if {@code null} or empty then it
	 * is not considered valid.
	 * @since 2022/09/21
	 */
	JDWPLocalVariable[] methodVariableTable(Object __which, int __methodDx);
	
	/**
	 * Reads the value of an static field within the class.
	 *
	 * @param __which Which class to read from?
	 * @param __index The index of the field to read from the object.
	 * @param __out Where the value is to be stored.
	 * @return {@code true} if this is a valid value.
	 * @since 2021/04/10
	 */
	boolean readValue(Object __which, int __index, JDWPHostValue __out);
	
	/**
	 * Returns the signature of the given type, this signature is in the
	 * same format as field signatures. As such for non-array types and
	 * primitives, this should be the same one used to describe fields.
	 * 
	 * @param __which Get the signature of which type?
	 * @return The signature of the given type.
	 * @since 2021/04/11
	 */
	String signature(Object __which);
	
	/**
	 * Returns the source file that the type comes from.
	 * 
	 * @param __which Which type to get the source file of?
	 * @return The source file the type comes from, {@code null} if it is not
	 * known.
	 * @since 2021/04/14
	 */
	String sourceFile(Object __which);
	
	/**
	 * Returns the super type of the given type.
	 * 
	 * @param __which Get the super class of which type?
	 * @return The super class or {@code null} if there is none.
	 * @since 2021/04/12
	 */
	Object superType(Object __which);
	
	/**
	 * Returns the type that the {@link Class} instance represents.
	 * 
	 * @param __object An instance of {@link Class}.
	 * @return The type of the given {@link Class} or {@code null} if it is not
	 * valid.
	 * @since 2022/09/01
	 */
	Object typeOfClassInstance(Object __object);
}
