// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.pvmjvm;

import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;

/**
 * This is used to wrap the class loader which paravirtualizes all classes in
 * a process by prefixing and creating virtual classes so that all class
 * access done by a process is handled by this loader.
 *
 * @since 2016/06/19
 */
public class PVMClassLoader
	extends ClassLoader
{
	/** The process which owns this class loader. */
	protected final PVMProcess process;
	
	/** The prefix for mangled field types. */
	protected final String mangledprefix;
	
	/**
	 * Initializes the class loader.
	 *
	 * @param __proc The owning process.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/19
	 */
	public PVMClassLoader(PVMProcess __proc)
		throws NullPointerException
	{
		// Check
		if (__proc == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.process = __proc;
		this.mangledprefix = "__squirreljme#" + __proc.pid();
	}
	
	/**
	 * Demangles the given mangled field symbol so that the actual class (to
	 * the process itself) is obtained.
	 *
	 * @param __f The field symbol to demangle.
	 * @return The original unmangle field.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/19
	 */
	public FieldSymbol fieldDemangle(FieldSymbol __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Primitives go through unchanged
		if (__f.primitiveType() != null)
			return __f;
		
		throw new Error("TODO");
	}
	
	/**
	 * Mangles the given field symbol to the field symbol that should be used
	 * in this process.
	 *
	 * @param __f The field symbol to mangle.
	 * @return The class which represents the mangled field.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/19
	 */
	public FieldSymbol fieldMangle(FieldSymbol __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Primitives go through unchanged
		if (__f.primitiveType() != null)
			return __f;
		
		// The target string
		StringBuilder sb = new StringBuilder(this.mangledprefix);
		sb.append('/');
		
		// Transform field characters
		String s = __f.toString();
		int n = s.length();
		for (int i = 0; i < n; i++)
		{
			char c = s.charAt(i);
			switch (c)
			{
					// Must be escaped
				case '.':
				case ';':
				case '[':
				case '/':
				case 0x0080:
					sb.append(0x0080);
					sb.append((char)(0x0080 + c));
					break;
				
					// Normal
				default:
					sb.append(c);
					break;
			}
		}
		
		// As a class
		return ClassNameSymbol.of(sb.toString()).asField();
	}
}

