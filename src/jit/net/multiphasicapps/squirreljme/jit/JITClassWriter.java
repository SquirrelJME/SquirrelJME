// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.classformat.ClassFlags;
import net.multiphasicapps.squirreljme.classformat.ConstantPool;
import net.multiphasicapps.squirreljme.classformat.FieldFlags;
import net.multiphasicapps.squirreljme.classformat.MethodFlags;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This is used to write class details on output.
 *
 * @since 2016/07/06
 */
public interface JITClassWriter
	extends AutoCloseable
{
	/**
	 * Records the class flags.
	 *
	 * @param __cf The class flags.
	 * @throws JITException If the flags could not be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/18
	 */
	public abstract void classFlags(ClassFlags __cf)
		throws JITException, NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/06
	 */
	@Override
	public abstract void close()
		throws JITException;
	
	/**
	 * This creates a writer which is able to convert the logic of a method
	 * into native machine code or similar.
	 *
	 * @return The code writer.
	 * @throws JITException If the logic writer could not be written.
	 * @since 2016/08/19
	 */
	public abstract JITMethodWriter code()
		throws JITException;
	
	/**
	 * Sets the constant pool that the class uses.
	 *
	 * @param __pool The constant pool to use.
	 * @throws JITException If it could not be set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/12
	 */
	public abstract void constantPool(ConstantPool __pool)
		throws JITException, NullPointerException;
	
	/**
	 * Ends the class.
	 *
	 * @throws JITException If it could not be ended.
	 * @since 2016/08/17
	 */
	public abstract void endClass()
		throws JITException;
	
	/**
	 * Ends a field.
	 *
	 * @throws JITException If the field could not be ended.
	 * @since 2016/08/18
	 */
	public abstract void endField()
		throws JITException;
	
	/**
	 * Ends a method.
	 *
	 * @throws JITException If the method could not be ended.
	 * @since 2016/08/19
	 */
	public abstract void endMethod()
		throws JITException;
	
	/**
	 * Specifies that a field is to be added to the class.
	 *
	 * @param __f The field flags.
	 * @param __n The name of the field.
	 * @param __t The type of the field.
	 * @param __cv The constant value of the field, may be {@code null} if
	 * there is no value set.
	 * @throws JITException If the field could not be recorded.
	 * @throws NullPointerException On null arguments, except for {@code __cv}.
	 * @since 2016/08/18
	 */
	public abstract void field(FieldFlags __f, IdentifierSymbol __n,
		FieldSymbol __t, Object __cv)
		throws JITException, NullPointerException;
	
	/**
	 * Sets the number of fields to use within a class.
	 *
	 * @param __n The number of fields to use.
	 * @throws JITException If it could not be set.
	 * @since 2016/08/17
	 */
	public abstract void fieldCount(int __n)
		throws JITException;
	
	/**
	 * Records class interfaces.
	 *
	 * @param __ins The class interfaces.
	 * @throws JITException If the interfaces could not be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public abstract void interfaceClasses(ClassNameSymbol[] __ins)
		throws JITException, NullPointerException;
	
	/**
	 * Specifies that a method is to be added to the class.
	 *
	 * @param __f The method flags.
	 * @param __n The name of the method.
	 * @param __t The type of the method.
	 * @throws JITException If the method could not be recorded.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/19
	 */
	public abstract void method(MethodFlags __f, IdentifierSymbol __n,
		MethodSymbol __t)
		throws JITException, NullPointerException;
	
	/**
	 * Sets the number of methods to use within the class.
	 *
	 * @param __n The method count.
	 * @throws JITException If it could not be set.
	 * @since 2016/08/17
	 */
	public abstract void methodCount(int __n)
		throws JITException;
	
	/**
	 * This is called when a method does not contain any byte code (it is
	 * abstract).
	 *
	 * @throws JITException If no code could be indicated.
	 * @since 2016/08/19
	 */
	public abstract void noCode()
		throws JITException;
	
	/**
	 * Records the name of the super-class of the class being decoded.
	 *
	 * @param __cn The name of the super class, may be {@code null} if there
	 * is none.
	 * @throws JITException If the super-class could not be written.
	 * @since 2016/07/22
	 */
	public abstract void superClass(ClassNameSymbol __cn)
		throws JITException;
}

