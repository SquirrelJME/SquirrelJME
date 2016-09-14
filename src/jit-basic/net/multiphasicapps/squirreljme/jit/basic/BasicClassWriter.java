// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import java.io.IOException;
import net.multiphasicapps.squirreljme.classformat.ClassDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.ClassFlags;
import net.multiphasicapps.squirreljme.classformat.ClassVersion;
import net.multiphasicapps.squirreljme.classformat.ConstantPool;
import net.multiphasicapps.squirreljme.classformat.FieldFlags;
import net.multiphasicapps.squirreljme.classformat.MethodFlags;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITFieldWriter;
import net.multiphasicapps.squirreljme.jit.JITMethodWriter;

/**
 * This writes generic classes to the output namespace.
 *
 * @since 2016/09/14
 */
public class BasicClassWriter
	extends __BaseWriter__
	implements JITClassWriter
{
	/** The constant pool. */
	protected final BasicConstantPool pool;
	
	/** The class storage area. */
	private final __Class__ _classindex;
	
	/**
	 * Initializes the basic class writer for output.
	 *
	 * @param __nsw The namespace writer.
	 * @param __cl The class index.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	BasicClassWriter(BasicNamespaceWriter __nsw, __Class__ __cl)
		throws NullPointerException
	{
		super(__nsw, __cl);
		
		// Set
		this._classindex = __cl;
		this.pool = __nsw._pool;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void classFlags(ClassFlags __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._classindex._flags = __f;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void className(ClassNameSymbol __n)
		throws NullPointerException
	{
		// This information is already known
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void close()
		throws JITException
	{
		// Close the output
		try
		{
			this.output.close();
		}
		
		// {@squirreljme.error BV0a Could not close the class output.}
		catch (IOException e)
		{
			throw new JITException("BV0a", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void constantPool(ConstantPool __pool)
		throws NullPointerException
	{
		// This information is not used
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void endClass()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public JITFieldWriter field(FieldFlags __f, IdentifierSymbol __n,
		FieldSymbol __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__f == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void fieldCount(int __n)
	{
		this._classindex._numfields = __n;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void interfaceClasses(ClassNameSymbol[] __i)
		throws NullPointerException
	{
		// Store interfaces
		__Class__ classindex = this._classindex;
		classindex._numinterfaces = __i.length;
		classindex._interfacedx = this.namespace.__interfaces(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public JITMethodWriter method(MethodFlags __f,
		IdentifierSymbol __n, MethodSymbol __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__f == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Create new method
		BasicConstantPool pool = this.pool;
		__Method__ m = new __Method__(__f, pool.addIdentifier(__n),
			pool.addMethodSymbol(__t));
		
		// Store class in the method table
		BasicNamespaceWriter namespace = this.namespace;
		int dx = namespace.__addMethod(m);
		__Class__ classindex = this._classindex;
		if (classindex._methoddx < 0)
			classindex._methoddx = dx;
		
		// Return a new writer
		return new BasicMethodWriter(namespace, this.output, m);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void methodCount(int __n)
	{
		this._classindex._nummethods = __n;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void superClass(ClassNameSymbol __n)
	{
		if (__n != null)
			this._classindex._super = this.pool.addClassName(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void version(ClassVersion __cv)
		throws NullPointerException
	{
		// The version is not important at this point in time
	}
}

