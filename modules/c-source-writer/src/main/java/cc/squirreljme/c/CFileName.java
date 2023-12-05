// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Represents a file name in C.
 *
 * @since 2023/06/04
 */
public final class CFileName
	implements Comparable<CFileName>
{
	/** The file name. */
	protected final String fileName;
	
	/** Basename cache. */
	private volatile Reference<CFileName> _baseName;
	
	/**
	 * Initializes the C file name.
	 * 
	 * @param __fileName The C file name.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	CFileName(String __fileName)
		throws NullPointerException
	{
		if (__fileName == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error CW0a Identifier cannot be blank.} */
		if (__fileName.isEmpty())
			throw new IllegalArgumentException("CW0a");
		
		// Check identifier
		boolean hasDot = false;
		for (int i = 0, n = __fileName.length(); i < n; i++)
		{
			char c = __fileName.charAt(i);
			
			if (c == '.')
			{
				/* {@squirreljme.error CW31 Filename has multiple extensions.} */
				if (hasDot)
					throw new IllegalArgumentException("CW31");
					
				hasDot = true;
			}
			
			/* {@squirreljme.error CW01 Identifier cannot start with a number.
			(The identifier)} */
			else if (i == 0 && c >= '0' && c <= '9')
				throw new IllegalArgumentException("CW01 " + __fileName);
			
			/* {@squirreljme.error CW09 Identifier contains an invalid
			character. (The identifier)} */
			else if (!((c >= 'a' && c <= 'z') ||
				(c >= 'A' && c <= 'Z') ||
				(c >= '0' && c <= '9') ||
				c == '_' || c == '/'))
				throw new IllegalArgumentException("CW09 " + __fileName);
		}
		
		// Is fine
		this.fileName = __fileName;
	}
	
	/**
	 * Returns the base name of this file.
	 *
	 * @return The base name.
	 * @since 2023/10/15
	 */
	public CFileName baseName()
	{
		Reference<CFileName> ref = this._baseName;
		CFileName rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			String fileName = this.fileName;
			int lastSlash = fileName.lastIndexOf('/');
			
			// Only if there is a slash component may the basename be different
			if (lastSlash >= 0)
				rv = new CFileName(fileName.substring(lastSlash + 1));
			else
				rv = this;
			
			this._baseName = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public int compareTo(CFileName __b)
	{
		return this.fileName.compareTo(__b.fileName);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		if (!(__o instanceof CFileName))
			return false;
		
		return this.fileName.equals(((CFileName)__o).fileName);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public int hashCode()
	{
		return this.fileName.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public String toString()
	{
		return this.fileName;
	}
	
	/**
	 * Initializes the C file name.
	 * 
	 * @param __fileName The C file name.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	public static CFileName of(String __fileName)
		throws NullPointerException
	{
		return new CFileName(__fileName);
	}
}
