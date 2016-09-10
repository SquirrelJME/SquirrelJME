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

import net.multiphasicapps.squirreljme.classformat.ClassDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.ClassFlags;
import net.multiphasicapps.squirreljme.classformat.ClassVersion;
import net.multiphasicapps.squirreljme.classformat.ConstantPool;
import net.multiphasicapps.squirreljme.classformat.FieldDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.FieldFlags;
import net.multiphasicapps.squirreljme.classformat.MethodDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.MethodFlags;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This bridges the class description stream to the JIT namespace and class
 * writers.
 *
 * @since 2016/09/09
 */
class __ClassWriter__
	implements AutoCloseable, ClassDescriptionStream
{
	/** The owning JIT. */
	protected final JIT jit;
	
	/** The namespace writer to use. */
	protected final JITNamespaceWriter namespace;
	
	/** The class writer to forward to. */
	private volatile JITClassWriter _writer;
	
	/** The class version. */
	private volatile ClassVersion _version;
	
	/**
	 * Initializes the class writer bridge.
	 *
	 * @param __jit The owning JIT.
	 * @param __nsw The namespace writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	__ClassWriter__(JIT __jit, JITNamespaceWriter __nsw)
		throws NullPointerException
	{
		// Check
		if (__jit == null || __nsw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.jit = __jit;
		this.namespace = __nsw;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public void classFlags(ClassFlags __f)
		throws NullPointerException
	{
		// Get writer
		JITClassWriter writer = __writer();
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public void className(ClassNameSymbol __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Start writing into the class
		JITClassWriter writer = this.namespace.beginClass(__n);
		this._writer = writer;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public void close()
	{
		// Close the created writer if one is used
		JITClassWriter writer = this._writer;
		if (writer != null)
			writer.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public void constantPool(ConstantPool __pool)
		throws NullPointerException
	{
		// Get writer
		JITClassWriter writer = __writer();
		
		// Pass through
		writer.constantPool(__pool);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public void endClass()
	{
		// Get writer
		JITClassWriter writer = __writer();
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public FieldDescriptionStream field(FieldFlags __f,
		IdentifierSymbol __name, FieldSymbol __type)
		throws NullPointerException
	{
		// Get writer
		JITClassWriter writer = __writer();
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public void fieldCount(int __n)
	{
		// Get writer
		JITClassWriter writer = __writer();
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public void interfaceClasses(ClassNameSymbol[] __i)
		throws NullPointerException
	{
		// Get writer
		JITClassWriter writer = __writer();
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public MethodDescriptionStream method(MethodFlags __f,
		IdentifierSymbol __name, MethodSymbol __type)
		throws NullPointerException
	{
		// Get writer
		JITClassWriter writer = __writer();
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public void methodCount(int __n)
	{
		// Get writer
		JITClassWriter writer = __writer();
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public void superClass(ClassNameSymbol __n)
	{
		// Get writer
		JITClassWriter writer = __writer();
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public void version(ClassVersion __cv)
		throws NullPointerException
	{
		// Check
		if (__cv == null)
			throw new NullPointerException("NARG");
		
		// The class version really does not make much of a difference once
		// the writer methods are called for JIT purposes
		this._version = __cv;
	}
	
	/**
	 * Returns the class writer.
	 *
	 * @return The class writer.
	 * @throws JITException If a class writer was not created.
	 * @since 2016/09/09
	 */
	private JITClassWriter __writer()
		throws JITException
	{
		// {@squirreljme.error ED01 Output class writer was not created.}
		JITClassWriter writer = this._writer;
		if (writer == null)
			throw new JITException("ED01");
		return writer;
	}
}

