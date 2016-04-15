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
import java.util.Map;
import java.util.Objects;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.IllegalSymbolException;
import net.multiphasicapps.descriptors.MemberTypeSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents name and type information.
 *
 * @since 2016/03/15
 */
public final class CFNameAndType
	extends CFConstantEntry
	implements Map.Entry<IdentifierSymbol, MemberTypeSymbol>
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Name index. */
	protected final int namedx;
	
	/** Type index. */
	protected final int typedx;
	
	/** The name as a symbol. */
	private volatile Reference<IdentifierSymbol> _name;
	
	/** The descriptor as a symbol. */
	private volatile Reference<MemberTypeSymbol> _type;
	
	/**
	 * Initializes name and type information.
	 *
	 * @param __icp The owning constant pool.
	 * @param __dis Data source.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/15
	 */
	CFNameAndType(CFConstantPool __icp, DataInputStream __dis)
		throws IOException, NullPointerException
	{
		super(__icp);
		
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Read values
		namedx = __rangeCheck(__dis.readUnsignedShort());
		typedx = __rangeCheck(__dis.readUnsignedShort());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public boolean equals(Object __o)
	{
		// If not a NAT, do not bother
		if (!(__o instanceof CFNameAndType))
			return false;
		
		// Cast
		CFNameAndType o = (CFNameAndType)__o;
		
		// The symbols in either one of them could be illegal;
		try
		{
			return Objects.equals(getKey(), o.getKey()) &&
				Objects.equals(getValue(), o.getValue());
		}
		
		// One has an illegal set of symbols.
		catch (CFFormatException jvmcfe)
		{
			// Do not match
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public IdentifierSymbol getKey()
	{
		// Lock
		synchronized (lock)
		{
			// Get reference
			Reference<IdentifierSymbol> ref = _name;
			IdentifierSymbol rv = null;
			
			// In reference?
			if (ref != null)
				rv = ref.get();
			
			// Needs caching
			if (rv == null)
				try
				{
					_name = new WeakReference<>((rv = new IdentifierSymbol(
						pool.<CFUTF8>getAs(namedx, CFUTF8.class).toString())));
				}
				
				// Bad identifier
				catch (IllegalSymbolException ise)
				{
					// {@squirreljme.error CF0f The specified symbol is
					// not a valid identifier. (The identifier symbol)}
					throw new CFFormatException("CF0f", ise);
				}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public MemberTypeSymbol getValue()
	{
		// Lock
		synchronized (lock)
		{
			// Get reference
			Reference<MemberTypeSymbol> ref = _type;
			MemberTypeSymbol rv = null;
			
			// In reference?
			if (ref != null)
				rv = ref.get();
			
			// Needs caching
			if (rv == null)
				try
				{
					_type = new WeakReference<>((rv = MemberTypeSymbol.
						create(pool.<CFUTF8>getAs(typedx, CFUTF8.class).
							toString())));
				}
				
				// Not a valid symbol
				catch (IllegalSymbolException ise)
				{
					// {@squirreljme.error CF0g The given descriptor is
					// neither a valid field or method descriptor. (The
					// member type symbol)}
					throw new CFFormatException(String.format("CF0g"),
						ise);
				}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public int hashCode()
	{
		// Could fail
		try
		{
			return Objects.hashCode(getKey()) ^
				Objects.hashCode(getValue());
		}
		
		// Badly formed symbol
		catch (CFFormatException jvmcfe)
		{
			return 0xDEADBEEF;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public MemberTypeSymbol setValue(MemberTypeSymbol __v)
	{
		throw new UnsupportedOperationException("RORO");
	}
	
	/**
	 * Returns the represented symbol as a field symbol.
	 *
	 * @return The type as a field symbol.
	 * @throws CFFormatException If the type is not a field symbol.
	 * @since 2016/03/17
	 */
	public FieldSymbol typeAsField()
		throws CFFormatException
	{
		try
		{
			return getValue().asField();
		}
		
		// Is not one
		catch (ClassCastException cce)
		{
			// {@squirreljme.error CF0h The specified symbol cannot be
			// represented as a field because it is not a field symbol.
			// (The current symbol)}
			throw new CFFormatException("CF0h", cce);
		}
	}
	
	/**
	 * Returns the represented symbol as a method symbol.
	 *
	 * @return The type as a method symbol.
	 * @throws CFFormatException If the type is not a method symbol.
	 * @since 2016/03/17
	 */
	public MethodSymbol typeAsMethod()
		throws CFFormatException
	{
		try
		{
			return getValue().asMethod();
		}
		
		// Is not one
		catch (ClassCastException cce)
		{
			// {@squirreljme.error CF0i The specified symbol cannot be
			// represented as a method because it is not a valid method
			// symbol. (The current symbol)}
			throw new CFFormatException("CF0i", cce);
		}
	}
}

