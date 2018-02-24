// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.objectfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This contains the object file which manages the executable code, program
 * data, strings, and symbols for a single program.
 *
 * @since 2018/02/23
 */
public final class ObjectFile
{
	/** Sections within the object file. */
	private final Map<String, Symbol> _symbols =
		new SortedTreeMap<>();
	
	/** Strings which have been added to the object file. */
	private final Map<String, Symbol> _strings =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the object file.
	 *
	 * @since 2018/02/23
	 */
	public ObjectFile()
	{
		// Create an initial strings section always since that is where
		// string data will be stored
		this.addSection(".strings", SectionFlag.READ);
	}
	
	/**
	 * Creates a new section within the object file.
	 *
	 * @param __name The name of the section.
	 * @param __flags The flags for the section.
	 * @return The newly created section.
	 * @throws IllegalStateException If a section with the given name already
	 * exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/23
	 */
	public final Section addSection(String __name, SectionFlag... __flags)
		throws IllegalStateException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Adds a string to the object file and returns the symbol which points
	 * to that given string.
	 *
	 * @param __v The string value to add.
	 * @return The symbol to the string data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/23
	 */
	public final Symbol addString(String __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

