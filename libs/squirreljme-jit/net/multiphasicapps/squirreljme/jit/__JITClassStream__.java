// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.classformat.ClassDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.ClassVersion;
import net.multiphasicapps.squirreljme.classformat.ConstantPool;
import net.multiphasicapps.squirreljme.classformat.FieldDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.MethodDescriptionStream;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.linkage.ClassFlags;
import net.multiphasicapps.squirreljme.linkage.FieldFlags;
import net.multiphasicapps.squirreljme.linkage.MethodFlags;

/**
 * This is the class description stream that the JIT uses for each class that
 * exists.
 *
 * @since 2017/01/30
 */
class __JITClassStream__
	implements ClassDescriptionStream
{
	/** The owning JIT. */
	protected final JIT jit;
	
	/**
	 * Initializes the class description stream.
	 *
	 * @param __jit The owning jit.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/30
	 */
	__JITClassStream__(JIT __jit)
		throws NullPointerException
	{
		// Check
		if (__jit == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.jit = __jit;
	}
	
	/**
	 * This reports the number of fields that are within a class.
	 *
	 * @param __n The number of fields in a class.
	 * @since 2016/09/09
	 */
	public void classFlags(ClassFlags __f)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * This reports the number of fields that are within a class.
	 *
	 * @param __n The number of fields in a class.
	 * @since 2016/09/09
	 */
	public void className(ClassNameSymbol __n)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * This reports the number of fields that are within a class.
	 *
	 * @param __n The number of fields in a class.
	 * @since 2016/09/09
	 */
	public void constantPool(ConstantPool __pool)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * This reports the number of fields that are within a class.
	 *
	 * @param __n The number of fields in a class.
	 * @since 2016/09/09
	 */
	public void endClass()
	{
		throw new Error("TODO");
	}
	
	/**
	 * This reports the number of fields that are within a class.
	 *
	 * @param __n The number of fields in a class.
	 * @since 2016/09/09
	 */
	public FieldDescriptionStream field(FieldFlags __f,
		IdentifierSymbol __name, FieldSymbol __type)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void fieldCount(int __n)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void interfaceClasses(ClassNameSymbol[] __i)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public MethodDescriptionStream method(MethodFlags __f,
		IdentifierSymbol __name, MethodSymbol __type)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void methodCount(int __n)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void superClass(ClassNameSymbol __n)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void version(ClassVersion __cv)
		throws NullPointerException
	{
		// The class stream handles this so handling the version serves
		// no real purpose
	}
}

