// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This is the base class for all class related information.
 *
 * @since 2016/03/16
 */
public final class JVMClass
{
	/** The interpreter engine which owns this class. */
	protected final JVMEngine engine;
	
	/** Fields which exist in this class, lock on this. */
	protected final Map<JVMMemberKey<FieldSymbol>, JVMField> fields =
		new HashMap<>();
	
	/** Methods which exist in this class, lock on this. */
	protected final Map<JVMMemberKey<MethodSymbol>, JVMMethod> methods =
		new HashMap<>();
	
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
			throw new NullPointerException("NARG");
		
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
	 * Locates a field in the class by the given identifier anme and field
	 * descriptor.
	 *
	 * @param __name The name of the field.
	 * @param __desc The field descriptor.
	 * @param __super Look in super classes for the field?
	 * @return The field matching the name and descriptor or {@code null} if
	 * none was found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/17
	 */
	public final JVMField getField(IdentifierSymbol __name, FieldSymbol __desc,
		boolean __super)
	{
		return this.<JVMField, FieldSymbol>__getMember(fields, __name, __desc,
			__super);
	}
	
	/**
	 * Locates a method in the class by the given identifier name and method
	 * descriptor.
	 *
	 * @param __name The name of the method.
	 * @param __desc The method descriptor.
	 * @param __super Look in super classes for the method?
	 * @return The method matching the name and descriptor or {@code null}
	 * if none was found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public final JVMMethod getMethod(IdentifierSymbol __name,
		MethodSymbol __desc, boolean __super)
		throws NullPointerException
	{
		return this.<JVMMethod, MethodSymbol>__getMember(methods, __name,
			__desc, __super);
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
			throw new NullPointerException("NARG");
		
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
	
	/**
	 * Registers a field or a method with this class.
	 *
	 * @param __m The member to register to this class.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the member is owned by another
	 * class, or it is of an unknown type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/17
	 */
	protected final JVMClass registerMember(JVMMember<?> __m)
		throws IllegalArgumentException, NullPointerException
	{	
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Wrong class?
		if (__m.outerClass() != this)
			throw new IllegalArgumentException("IN0t");
		
		// A field?
		if (__m instanceof JVMField)
			synchronized (fields)
			{
				fields.put(new JVMMemberKey<>(__m.name(),
					__m.type().asFieldSymbol()), (JVMField)__m);
			}
		
		// A method
		else if (__m instanceof JVMMethod)
			synchronized (methods)
			{
				methods.put(new JVMMemberKey<>(__m.name(),
					__m.type().asMethodSymbol()), (JVMMethod)__m);
			}
		
		// Unknown
		else
			throw new IllegalArgumentException("IN0u");
		
		// Self
		return this;
	}
	
	/**
	 * Locates a member by the given name and type.
	 *
	 * @param <V> The type of member to find.
	 * @param <S> The symbol type it uses as a descriptor.
	 * @param __lookin The map to look inside for members.
	 * @param __name The name of the member.
	 * @param __type The type of the member.
	 * @param __super If not found in this class, should superclasses be
	 * searched?
	 * @return The member with the given name and type, or {@code null} if
	 * not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016//03/17
	 */
	private <V extends JVMMember, S extends MemberTypeSymbol> V __getMember(
		Map<JVMMemberKey<S>, V> __lookin, IdentifierSymbol __name, S __type,
		boolean __super)
		throws NullPointerException
	{
		return this.<V, S>__getMember(__lookin, new JVMMemberKey<>(__name,
			__type), __super);
	}
	
	/**
	 * Locates a member by the given name and type.
	 *
	 * @param <V> The type of member to find.
	 * @param <S> The symbol type it uses as a descriptor.
	 * @param __lookin The map to look inside for members.
	 * @param __key The member key to use during the search
	 * @param __super If not found in this class, should superclasses be
	 * searched?
	 * @return The member with the given name and type, or {@code null} if
	 * not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016//03/17
	 */
	private <V extends JVMMember, S extends MemberTypeSymbol> V __getMember(
		Map<JVMMemberKey<S>, V> __lookin, JVMMemberKey<S> __key,
		boolean __super)
		throws NullPointerException
	{
		// Check
		if (__lookin == null || __key == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (__lookin)
		{
			// Go through the map, if it is found then use it
			V rv = __lookin.get(__key);
			if (rv != null)
				return rv;
			
			// Look in super class?
			if (__super)
				throw new Error("TODO");
			
			// Not found
			return null;
		}
	}
}

