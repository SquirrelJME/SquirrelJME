// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.IllegalSymbolException;

/**
 * This represents the name of a class.
 *
 * @since 2016/03/15
 */
public final class CFClassName
	extends CFConstantEntry
	implements CFLDCLoadable.Narrow
{
	/** The class name index. */
	protected final int index;
	
	/** The actual class symbol. */
	private volatile Reference<ClassNameSymbol> _cname;
	
	/**
	 * Initializes the class name.
	 *
	 * @param __icp The owning constant pool.
	 * @param __dis Input stream to read data from.
	 * @throws CFFormatException If the class name is not
	 * valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/15
	 */
	CFClassName(CFConstantPool __icp, DataInputStream __dis)
		throws CFFormatException, IOException,
			NullPointerException
	{
		super(__icp);
		
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Get id
		index = __rangeCheck(__dis.readUnsignedShort());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/19
	 */
	@Override
	public ClassNameSymbol getValue()
	{
		return symbol();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/19
	 */
	@Override
	public boolean isWide()
	{
		return false;
	}
	
	/**
	 * Returns the symbol associated with this class.
	 *
	 * @return The class name symbol.
	 * @throws CFFormatException If the class name symbol is
	 * invalid.
	 * @since 2016/03/15
	 */
	public ClassNameSymbol symbol()
		throws CFFormatException
	{
		// Get reference
		Reference<ClassNameSymbol> ref = _cname;
		ClassNameSymbol rv = null;
		
		// In reference?
		if (ref != null)
			rv = ref.get();
		
		// Needs initialization
		if (rv == null)
			try
			{
				_cname = new WeakReference<>((rv = new ClassNameSymbol(
					pool.<CFUTF8>getAs(index, CFUTF8.class).toString())));
			}
			
			// Bad symbol
			catch (IllegalSymbolException ise)
			{
				// @{squirreljme.error CF0e The descriptor which represents
				// the name of a class is not valid. (The class name
				// symbol)}
				throw new CFFormatException("CF0e", ise);
			}
		
		// Return it
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/15
	 */
	@Override
	public String toString()
	{
		return symbol().toString();
	}
}

