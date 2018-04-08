// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.layout;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.ClassIdentifier;

/**
 * This represents an import that is being performed.
 *
 * @since 2018/04/08
 */
public final class ClassImport
{
	/** Is this a static import? */
	protected final boolean isstatic;
	
	/** The package. */
	protected final BinaryName inpackage;
	
	/** What is being imported. */
	protected final ClassIdentifier what;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the given import.
	 *
	 * @param __pkg The package to import from.
	 * @param __what What is being imported.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	public ClassImport(boolean __static, BinaryName __pkg,
		ClassIdentifier __what)
		throws NullPointerException
	{
		if (__pkg == null || __what == null)
			throw new NullPointerException("NARG");
		
		this.isstatic = __static;
		this.inpackage = __pkg;
		this.what = __what;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/08
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof ClassImport))
			return false;
		
		ClassImport o = (ClassImport)__o;
		return this.isstatic == o.isstatic &&
			this.inpackage.equals(o.inpackage) &&
			this.what.equals(o.what);
	}

	/**
	 * {@inheritDoc}
	 * @since 2018/04/08
	 */
	@Override
	public final int hashCode()
	{
		return this.inpackage.hashCode() ^
			this.what.hashCode() ^ (this.isstatic ? ~0 : 0);
	}
	
	/**
	 * Returns the package the import is being made from.
	 *
	 * @return The package to import from.
	 * @since 2018/04/08
	 */
	public final BinaryName inPackage()
	{
		return this.inpackage;
	}
	
	/**
	 * Is this a static import?
	 *
	 * @return If this is a static import.
	 * @since 2018/04/08
	 */
	public final boolean isStatic()
	{
		return this.isstatic;
	}
	
	/**
	 * Is this a wildcard import?
	 *
	 * @return If this is a wildcard import.
	 * @since 2018/04/08
	 */
	public final boolean isWildcard()
	{
		return this.what.toString().equals("*");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/08
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				String.format("import %s %s.%s",
					(this.isstatic ? "static" : "normal"),
					this.inpackage, this.what)));
		
		return rv;
	}
	
	/**
	 * Returns what is being imported.
	 *
	 * @return What is being imported.
	 * @since 2018/04/08
	 */
	public final ClassIdentifier what()
	{
		return this.what;
	}
}

