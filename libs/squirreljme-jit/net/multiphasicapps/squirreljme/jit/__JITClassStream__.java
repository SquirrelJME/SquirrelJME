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
	
	/** The output executrable. */
	protected final JITExecutableBuilder output;
	
	/**
	 * Initializes the class description stream.
	 *
	 * @param __jit The owning jit.
	 * @param __out The output executable.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/30
	 */
	__JITClassStream__(JIT __jit, JITExecutableBuilder __out)
		throws NullPointerException
	{
		// Check
		if (__jit == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.jit = __jit;
		this.output = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void classFlags(ClassFlags __f)
		throws NullPointerException
	{
		this.output.setClassFlags(__f);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void className(ClassNameSymbol __n)
		throws NullPointerException
	{
		this.output.setClassName(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void constantPool(ConstantPool __pool)
		throws NullPointerException
	{
		// The constant pool is not used at all
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void endClass()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
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
		// Dynamically used
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void interfaceClasses(ClassNameSymbol[] __i)
		throws NullPointerException
	{
		this.output.setInterfaceClassNames(__i);
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
		// Dynamically used
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void superClass(ClassNameSymbol __n)
	{
		this.output.setSuperClassName(__n);
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

