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

import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This contains and stores the definition of a single method.
 *
 * @since 2018/07/22
 */
public final class SpringMethod
	implements SpringMember
{
	/** The class this technically belongs to. */
	protected final ClassName inclass;
	
	/** The backing method and its information. */
	protected final Method method;
	
	/**
	 * Initializes the method representation.
	 *
	 * @param __ic The class this belongs to.
	 * @param __m The method to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	SpringMethod(ClassName __ic, Method __m)
		throws NullPointerException
	{
		if (__ic == null || __m == null)
			throw new NullPointerException("NARG");
		
		this.inclass = __ic;
		this.method = __m;
	}
	
	/**
	 * Returns the byte code of the method.
	 *
	 * @return The method byte code.
	 * @since 2018/09/03
	 */
	public final ByteCode byteCode()
	{
		return this.method.byteCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/09
	 */
	@Override
	public final MethodFlags flags()
	{
		return this.method.flags();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/03
	 */
	@Override
	public final ClassName inClass()
	{
		return this.inclass;
	}
	
	/**
	 * Returns whether this method is abstract.
	 *
	 * @return Whether this method is abstract.
	 * @since 2018/09/03
	 */
	public final boolean isAbstract()
	{
		return this.method.flags().isAbstract();
	}
	
	/**
	 * Returns whether this is a constructor or not.
	 *
	 * @return Whether this is a constructor or not.
	 * @since 2018/09/03
	 */
	public final boolean isInstanceInitializer()
	{
		return this.method.isInstanceInitializer();
	}
	
	/**
	 * Returns if this method is static.
	 *
	 * @return {@code true} if the method is static.
	 * @since 2018/09/03
	 */
	public final boolean isStatic()
	{
		return this.method.flags().isStatic();
	}
	
	/**
	 * Returns whether this is a static initializer or not.
	 *
	 * @return Whether this is a static initializer or not.
	 * @since 2018/09/03
	 */
	public final boolean isStaticInitializer()
	{
		return this.method.isStaticInitializer();
	}
	
	/**
	 * Returns the name and type of the method.
	 *
	 * @return The method name and type.
	 * @since 2018/09/03
	 */
	public final MethodNameAndType nameAndType()
	{
		return this.method.nameAndType();
	}
}

