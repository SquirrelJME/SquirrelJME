// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.singularexport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
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

/**
 * This reads the input class and virtualizes it by prefixing all class names
 * fields and methods with a special prefix.
 *
 * @since 2016/09/30
 */
class __VirtualClass__
	implements ClassDescriptionStream
{
	/** The virtual package. */
	protected final ClassNameSymbol virtualpackage;
	
	/** Methods. */
	final List<__Method__> _methods =
		new ArrayList<>();
	
	/** The class flags. */
	volatile ClassFlags _flags;
	
	/** The name of this class. */
	volatile ClassNameSymbol _thisname;
	
	/** The name of the super class. */
	volatile ClassNameSymbol _supername;
	
	/** The implemented interfaces. */
	volatile ClassNameSymbol[] _interfacenames;
	
	/**
	 * Initializes the virtual class.
	 *
	 * @param __vpack The virtual package name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	__VirtualClass__(ClassNameSymbol __vpack)
		throws NullPointerException
	{
		// Check
		if (__vpack == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.virtualpackage = __vpack;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void classFlags(ClassFlags __f)
		throws NullPointerException
	{
		// Set
		this._flags = __f;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void className(ClassNameSymbol __n)
		throws NullPointerException
	{
		this._thisname = __SymbolUtil__.__virtualClass(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void constantPool(ConstantPool __pool)
		throws NullPointerException
	{
		// Ignore the constant pool
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void endClass()
	{
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
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void fieldCount(int __n)
	{
		// Ignore
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void interfaceClasses(ClassNameSymbol[] __i)
		throws NullPointerException
	{
		// Virtualize interfaces
		int n = __i.length;
		ClassNameSymbol[] to = new ClassNameSymbol[n];
		for (int i = 0; i < n; i++)
			to[i] = __SymbolUtil__.__virtualClass(__i[i]);
		
		// Set
		this._interfacenames = to;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public MethodDescriptionStream method(MethodFlags __f,
		IdentifierSymbol __name, MethodSymbol __type)
		throws NullPointerException
	{
		// Remember it
		__Method__ m = new __Method__(__f, __name, __type);
		this._methods.add(m);
		
		// Set for decode
		return new __VirtualMethod__(m);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void methodCount(int __n)
	{
		// Ignore
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void superClass(ClassNameSymbol __n)
	{
		// Wrap super class normally
		ClassNameSymbol vn;
		if (__n != null)
			vn = __SymbolUtil__.__virtualClass(__n);
		
		// Otherwise Object must extend a class, and as such make all classes
		// extend the virtual engine's fake-Object
		else
			vn = ClassNameSymbol.of(virtualpackage + "/VirtualObject");
		
		// Set
		this._supername = vn;;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void version(ClassVersion __cv)
		throws NullPointerException
	{
		// ignore
	}
	
	/**
	 * Returns the virtualized class name.
	 *
	 * @return The virtualized class name.
	 * @since 2016/09/30
	 */
	final String __name()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Writes the virtualized class to the specified output.
	 *
	 * @param __dos The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	final void __output(DataOutputStream __dos)
		throws IOException, NullPointerException
	{
		// Check
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

