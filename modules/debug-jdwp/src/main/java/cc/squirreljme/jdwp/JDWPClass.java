// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Represents a class type.
 *
 * @deprecated Use viewers instead.
 * @since 2021/03/13
 */
@Deprecated
public interface JDWPClass
	extends JDWPReferenceType
{
	/**
	 * Returns the class binary name.
	 * 
	 * @return The binary name of the class.
	 * @since 2021/03/14
	 */
	String debuggerBinaryName();
	
	/**
	 * Returns the class type of this class.
	 * 
	 * @return The class type.
	 * @since 2021/03/13
	 */
	JDWPClassType debuggerClassType();
	
	/**
	 * Returns the class name as a field descriptor.
	 * 
	 * @return The class name as a field descriptor.
	 * @since 2021/03/13
	 */
	String debuggerFieldDescriptor();
	
	/**
	 * Reads the field value from the class.
	 * 
	 * @param __obj The object to get the value of, if the field is static
	 * then this is ignored.
	 * @param __field The field to read from.
	 * @param __value The read value of the field.
	 * @return {@code true} if a value was read, otherwise {@code false}
	 * if the field has a static mismatch or does not exist.
	 * @since 2021/03/15
	 */
	boolean debuggerFieldValue(JDWPObjectLike __obj, JDWPField __field,
		JDWPValue __value);
	
	/**
	 * Returns the class fields.
	 * 
	 * @return The class fields.
	 * @since 2021/03/14
	 */
	JDWPField[] debuggerFields();
	
	/**
	 * Returns interfaces the class implements.
	 * 
	 * @return The interface classes.
	 * @since 2021/03/14
	 */
	JDWPClass[] debuggerInterfaceClasses();
	
	/**
	 * Returns all of the methods within the class.
	 * 
	 * @return The methods in the class.
	 * @since 2021/03/13
	 */
	JDWPMethod[] debuggerMethods();
	
	/**
	 * The source file for this class.
	 * 
	 * @return The class source file, if {@code null} it is not available.
	 * @since 2021/03/14
	 */
	String debuggerSourceFile();
	
	/**
	 * Returns the super class of this class.
	 * 
	 * @return The super class or {@code null} if there is none.
	 * @since 2021/03/14
	 */
	JDWPClass debuggerSuperClass();
}
