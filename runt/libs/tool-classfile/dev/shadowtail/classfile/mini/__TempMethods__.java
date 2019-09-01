// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import dev.shadowtail.classfile.pool.DualClassRuntimePoolBuilder;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.io.TableSectionOutputStream;

/**
 * Contains temporary method information.
 *
 * @since 2019/03/13
 */
final class __TempMethods__
{
	/** The name of this class. */
	protected final ClassName classname;
	
	/** The methods in this table. */
	final List<MinimizedMethod> _methods =
		new ArrayList<>();
	
	/** The number of methods that are available. */
	int _count;
	
	/**
	 * Initializes.
	 *
	 * @param __cn The class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/22
	 */
	__TempMethods__(ClassName __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		this.classname = __cn;
	}
	
	/**
	 * Finds the method by the given name.
	 *
	 * @param __name The name to locate.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/08/11
	 */
	public final int findMethodIndex(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		List<MinimizedMethod> methods = this._methods;
		for (int i = 0, n = methods.size(); i < n; i++)
			if (__name.equals(methods.get(i).nameAndType().name().toString()))
				return i;
		
		return -1;
	}
	
	/**
	 * Returns the byte representation of the data here.
	 *
	 * @param __pool The constant pool.
	 * @return The byte data representation.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	public final byte[] getBytes(DualClassRuntimePoolBuilder __pool)
		throws NullPointerException
	{
		if (__pool == null)
			throw new NullPointerException("NARG");
		
		// Build into sections
		TableSectionOutputStream output = new TableSectionOutputStream();
		
		// Write
		MinimizedMethod current = null;
		try
		{
			// Methods to process
			List<MinimizedMethod> methods = this._methods;
			
			// Table of contents which represents each method
			TableSectionOutputStream.Section toc = output.addSection();
			
			// Format every method
			for (int i = 0, n = methods.size(); i < n; i++)
			{
				// Get the method to process
				current = methods.get(i);
				
				// Write all of the method code into its own section
				TableSectionOutputStream.Section codesection = null;
				byte[] rawcode = current._code;
				if (rawcode != null)
				{
					// Add aligned section for this code
					codesection = output.addSection(
						TableSectionOutputStream.VARIABLE_SIZE, 4);
					
					// Write all of the code here
					codesection.write(rawcode);
				}
				
				// Flags, name, and type
				toc.writeInt(current.flags);
				toc.writeUnsignedShortChecked(current.index);
				toc.writeUnsignedShortChecked(
					__pool.add(false, current.name.toString()).index);
				toc.writeUnsignedShortChecked(
					__pool.add(false, current.type).index);
				
				// Code section if one exists
				if (codesection != null)
				{
					toc.writeSectionAddressInt(codesection);
					toc.writeSectionSizeInt(codesection);
				}
				
				// There is no code
				else
				{
					toc.writeInt(0);
					toc.writeInt(0);
				}
			}
			
			// Write end of table
			toc.writeInt(0xFFFFFFFF);
			
			// Output as a byte array
			return output.toByteArray();
		}
		
		// {@squirreljme.error JC0p Could not process the method. (The method
		// this stopped at)}
		catch (InvalidClassFormatException|IOException e)
		{
			throw new InvalidClassFormatException("JC0p " + current, e);
		}
	}
}

