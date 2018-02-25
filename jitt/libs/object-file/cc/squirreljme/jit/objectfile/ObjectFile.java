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
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This contains the object file which manages the executable code, program
 * data, strings, and symbols for a single program.
 *
 * @since 2018/02/23
 */
public final class ObjectFile
{
	/** The symbol table for this class. */
	protected final SymbolTable symbols =
		new SymbolTable();
	
	/** Sections within the object file. */
	private final Map<String, Section> _sections =
		new SortedTreeMap<>();
	
	/** Virtual table of strings that exist within the object file. */
	private final Map<String, ImportedSymbol> _strings =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the object file.
	 *
	 * @since 2018/02/23
	 */
	public ObjectFile()
	{
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
	 * Adds a string to the virtual string table, note that strings are not
	 * actually added to the output binary they are just referred to as
	 * global imports.
	 *
	 * @param __v The string value to add.
	 * @return The symbol to the string data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/23
	 */
	public final ImportedSymbol addString(String __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the section named under the given name.
	 *
	 * @param __name The section name to obtain.
	 * @return The section for that name or {@code null} if it does not
	 * exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	public final Section getSection(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		Map<String, Section> sections = this._sections;
		synchronized (sections)
		{
			return sections.get(__name);
		}
	}
}

