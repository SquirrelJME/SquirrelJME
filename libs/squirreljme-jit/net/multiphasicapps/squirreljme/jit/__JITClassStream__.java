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

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirreljme.classformat.ClassDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.ClassVersion;
import net.multiphasicapps.squirreljme.classformat.ConstantPool;
import net.multiphasicapps.squirreljme.classformat.FieldDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.MethodDescriptionStream;
import net.multiphasicapps.squirreljme.executable.ExecutableClass;
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
	
	/** Fields in the class. */
	final List<__JITFieldStream__> _fields =
		new ArrayList<>();
	
	/** Methods in the class. */
	final List<__JITMethodStream__> _methods =
		new ArrayList<>();
	
	/** The name of this class. */
	volatile ClassNameSymbol _classname;
	
	/** The name of the super class. */
	volatile ClassNameSymbol _supername;
	
	/** Implemented interfaces. */
	volatile ClassNameSymbol[] _interfaces;
	
	/** The class flags. */
	volatile ClassFlags _flags;
	
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
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void classFlags(ClassFlags __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
			
		// Set
		this._flags = __f;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/30
	 */
	@Override
	public void className(ClassNameSymbol __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._classname = __n;
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
		// Check
		if (__f == null || __name == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Create new field
		__JITFieldStream__ rv = new __JITFieldStream__(this, __f, __name,
			__type);
		this._fields.add(rv);
		return rv;
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
		// Check
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._interfaces = __i.clone();
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
		// Check
		if (__f == null || __name == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Create new method
		__JITMethodStream__ rv = new __JITMethodStream__(this, __f, __name,
			__type);
		this._methods.add(rv);
		return rv;
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
		this._supername = __n;
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
	
	/**
	 * Builds the executable.
	 *
	 * @return The executable that may be ran.
	 * @since 2017/02/07
	 */
	final ExecutableClass __build()
	{
		throw new Error("TODO");
	}
}

