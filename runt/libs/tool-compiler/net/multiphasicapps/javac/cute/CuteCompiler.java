// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.cute;

import net.multiphasicapps.javac.Compiler;
import net.multiphasicapps.javac.CompilerException;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerInputLocation;
import net.multiphasicapps.javac.CompilerOptions;
import net.multiphasicapps.javac.CompilerOutput;
import net.multiphasicapps.javac.CompilerPathSet;

/**
 * This is used to create instances of the compiler.
 *
 * @since 2017/11/28
 */
public class CuteCompiler
	implements Compiler
{
	/** Internal lock. */
	private final Object _lock =
		new Object();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public boolean addInput(CompilerInput __i)
		throws CompilerException, NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public Runnable compile(CompilerOutput __o)
		throws CompilerException, NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public CompilerInput[] input()
		throws CompilerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public CompilerPathSet location(CompilerInputLocation __l)
		throws CompilerException, NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public CompilerOptions options()
		throws CompilerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public boolean removeInput(CompilerInput __i)
		throws CompilerException, NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public CompilerPathSet setLocation(CompilerInputLocation __l,
		CompilerPathSet __s)
		throws CompilerException, NullPointerException
	{
		if (__l == null || __s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public CompilerOptions setOptions(CompilerOptions __o)
		throws CompilerException, NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

