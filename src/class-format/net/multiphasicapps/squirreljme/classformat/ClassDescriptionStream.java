// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This interface is implemented by anything that wishes to be told what a
 * class is like while it is decoded.
 *
 * @since 2016/09/09
 */
public interface ClassDescriptionStream
{
	/**
	 * This is called when the class flags are known.
	 *
	 * @param __cf The class flags to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	public abstract void classFlags(ClassFlags __f)
		throws NullPointerException;
	
	/**
	 * This is called when the name of the class becomes known.
	 *
	 * @param __n The name of the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	public abstract void className(ClassNameSymbol __n)
		throws NullPointerException;
	
	/**
	 * This is called when the constant pool of a class is known.
	 *
	 * @param __pool The class constant pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	public abstract void constantPool(ConstantPool __pool)
		throws NullPointerException;
	
	/**
	 * This is called when the class has been fully decoded.
	 *
	 * @since 2016/09/09
	 */
	public abstract void endClass();
	
	/**
	 * Specifies that the following field exists in the class where the field
	 * information is provided to the description stream.
	 *
	 * @param __f The flags.
	 * @param __name The name.
	 * @param __type The type.
	 * @return A description stream for describing the member.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	public abstract FieldDescriptionStream field(FieldFlags __f,
		IdentifierSymbol __name, FieldSymbol __type)
		throws NullPointerException;
	
	/**
	 * This reports the number of fields that are within a class.
	 *
	 * @param __n The number of fields in a class.
	 * @since 2016/09/09
	 */
	public abstract void fieldCount(int __n);
	
	/**
	 * This is called when the interfaces that the class implements has been
	 * decoded.
	 *
	 * @param __i The implemented interfaces.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	public abstract void interfaceClasses(ClassNameSymbol[] __i)
		throws NullPointerException;
	
	/**
	 * Specifies that the following method exists in the class where the method
	 * information is provided to the description stream.
	 *
	 * @param __f The flags.
	 * @param __name The name.
	 * @param __type The type.
	 * @return A description stream for describing the member.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	public abstract MethodDescriptionStream method(MethodFlags __f,
		IdentifierSymbol __name, MethodSymbol __type)
		throws NullPointerException;
	
	/**
	 * This reports the number of methods that are within a class.
	 *
	 * @param __n The number of methods in a class.
	 * @since 2016/09/09
	 */
	public abstract void methodCount(int __n);
	
	/**
	 * This is called when the name of the super class is known, since the
	 * super-class may be optional this may be {@code null}.
	 *
	 * @param __n The name of the super-class or {@code null} if there is
	 * no super class.
	 * @since 2016/09/09
	 */
	public abstract void superClass(ClassNameSymbol __n);
	
	/**
	 * This reports the version of the class.
	 *
	 * @param __cv The version of the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	public abstract void version(ClassVersion __cv)
		throws NullPointerException;
}

