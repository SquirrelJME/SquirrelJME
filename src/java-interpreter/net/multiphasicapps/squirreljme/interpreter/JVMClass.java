// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This is the base class for all class related information.
 *
 * @since 2016/03/16
 */
public abstract class JVMClass
{
	/** Is invokedynamic supported anyway even thougn it is illegal? */
	static final boolean SUPPORT_INVOKEDYNAMIC_ANYWAY =
		Boolean.valueOf(System.getProperty(
			JVMClass.class.getName() + ".invokedynamic"));
	
	/** The interpreter engine which owns this class. */
	protected final JVMEngine engine;
	
	/**
	 * Initializes the base class information.
	 *
	 * @param __eng The owning engine.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/16
	 */
	protected JVMClass(JVMEngine __eng)
		throws NullPointerException
	{
		// Check
		if (__eng == null)
			throw new NullPointerException();
		
		// Set
		engine = __eng;
	}
	
	/**
	 * Returns the constant pool of the class.
	 *
	 * @return The class constant pool.
	 * @since 2016/03/16
	 */
	public abstract JVMConstantPool constantPool();
	
	/**
	 * Returns the flags which are specified by this class.
	 *
	 * @return The used class flags.
	 * @since 2016/03/16
	 */
	public abstract Set<JVMClassFlag> flags();
	
	/**
	 * Returns the name of interfaces this implements.
	 *
	 * @return The set of interface names.
	 * @since 2016/03/16
	 */
	public abstract Set<ClassNameSymbol> interfaceNames();
	
	/**
	 * Returns the name of the super class.
	 *
	 * @return The super class name.
	 * @since 2016/03/16
	 */
	public abstract ClassNameSymbol superName();
	
	/**
	 * Returns the name of this class.
	 *
	 * @return The name of this class.
	 * @since 2016/03/16
	 */
	public abstract ClassNameSymbol thisName();
	
	/**
	 * Returns the version of the class file which determines if specific
	 * features are supported or not.
	 *
	 * @return The class version number.
	 * @since 2016/03/13
	 */
	public abstract JVMClassVersion version();
	
	/**
	 * Returns the engine which initialized this class.
	 *
	 * @return The owning engine
	 * @since 2016/03/16
	 */
	public final JVMEngine engine()
	{
		return engine;
	}
	
	/**
	 * Returns the name of this class as returned by {@link Class#getName()}.
	 *
	 * @return The name of this class.
	 * @since 2016/03/02
	 */
	public final String getClassLoaderName()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Locates a method in the class by the given identifier name and method
	 * descriptor.
	 *
	 * @param __name The name of the method.
	 * @param __desc The method descriptor.
	 * @return The method matching the name and descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public final JVMMethod getMethod(String __name, String __desc)
		throws NullPointerException
	{
		// Check
		if (__name == null || __desc == null)
			throw new NullPointerException();
		
		throw new Error("TODO");
	}
	
	/**
	 * Is a specific flag set?
	 *
	 * @param __jcf The flag to check if it is set.
	 * @return {@code true} if the flag is set or {@code false} if it is not.
	 * @throws NullPointerException On null arguments.
	 */
	public final boolean hasFlag(JVMClassFlag __jcf)
	{
		// Check
		if (__jcf == null)
			throw new NullPointerException();
		
		return flags().contains(__jcf);
	}
	
	/**
	 * Is this class abstract?
	 *
	 * @return {@code true} if it is abstract.
	 * @since 2016/03/15
	 */
	public final boolean isAbstract()
	{
		return flags().contains(JVMClassFlag.ABSTRACT);
	}
	
	/**
	 * Is this an annotation?
	 *
	 * @return {@code true} if it is an annotation.
	 * @since 2016/03/15
	 */
	public final boolean isAnnotation()
	{
		return flags().contains(JVMClassFlag.ANNOTATION);
	}
	
	/**
	 * Is this an enumeration?
	 *
	 * @return {@code true} if it is an enumeration.
	 * @since 2016/03/15
	 */
	public final boolean isEnum()
	{
		return flags().contains(JVMClassFlag.ENUM);
	}
	
	/**
	 * Is this class final?
	 *
	 * @return {@code true} if it is final.
	 * @since 2016/03/15
	 */
	public final boolean isFinal()
	{
		return flags().contains(JVMClassFlag.FINAL);
	}
	
	/**
	 * Is this an interface?
	 *
	 * @return {@code true} if an interface.
	 * @since 2016/03/15
	 */
	public final boolean isInterface()
	{
		return flags().contains(JVMClassFlag.INTERFACE);
	}
	
	/**
	 * Is this class package private?
	 *
	 * @return {@code true} if it is package private.
	 * @since 2016/03/15
	 */
	public final boolean isPackagePrivate()
	{
		return !isPublic();
	}
	
	/**
	 * Is this class public?
	 *
	 * @return {@code true} if it is public.
	 * @since 2016/03/15
	 */
	public final boolean isPublic()
	{
		return flags().contains(JVMClassFlag.PUBLIC);
	}
	
	/**
	 * Is there special handling for super-class method calls?
	 *
	 * @return {@code true} if the super-class invocation special flag is set.
	 * @since 2016/03/15
	 */
	public final boolean isSpecialInvokeSpecial()
	{
		return flags().contains(JVMClassFlag.SUPER);
	}
}

