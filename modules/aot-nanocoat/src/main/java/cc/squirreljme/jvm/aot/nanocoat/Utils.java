// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.UnsupportedEncodingException;
import net.multiphasicapps.io.Base64Encoder;

/**
 * General utilities.
 *
 * @since 2023/05/28
 */
public final class Utils
{
	/**
	 * Not used.
	 * 
	 * @since 2023/05/28
	 */
	private Utils()
	{
	}
	
	/**
	 * Determines a DOS compatible file name.
	 * 
	 * @param __in The input name.
	 * @return The DOS compatible file name.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public static final String dosFileName(String __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Extract extension if there is one
		int lastDot = __in.lastIndexOf('.');
		String ext = (lastDot >= 0 ?
			"." + __in.substring(lastDot + 1) : "");
		
		// Determine actual name
		StringBuilder base = new StringBuilder();
		for (int i = 0, n = __in.length(); i < n; i++)
		{
			char c = __in.charAt(i);
			
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||
				(c >= '0' && c <= '9') || c == '_' || c == '-')
				base.append(c);
			else
				base.append('_');
		}
		
		// Long file name?
		if (base.length() > 8)
		{
			// Determine the hash of the name
			String temp = base.toString().toLowerCase();
			int hash = temp.hashCode();
			if (hash < 0)
				hash = -hash;
			
			// Convert to a max radix number which will have part of the name
			String code = Long.toString(hash & 0xFFFFFFFFL,
				Character.MAX_RADIX);
			
			// This will be [1, 7] characters, so clear out to fit the code
			// with 8 characters
			int codeLen = code.length();
			int baseLen = 8 - codeLen;
			base.delete(baseLen, base.length());
			
			// Append the code, which is the compactified hashcode
			base.append(code);
		}
		
		// Build name
		return (base + ext).toLowerCase();
	}
}
