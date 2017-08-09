// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.java.ClassName;
import net.multiphasicapps.squirreljme.jit.java.MethodDescriptor;
import net.multiphasicapps.squirreljme.jit.java.MethodFlags;
import net.multiphasicapps.squirreljme.jit.java.MethodName;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This class represents the sections that would exist in the output executable
 * such as the text and data sections. The bulk of the executable would
 * primarily only be generated containing all of the sections which are
 * contained here.
 *
 * @since 2017/06/15
 */
public class Sections
	extends __SubState__
{
	/** The sections which exist. */
	private final Map<SectionType, Section> _sections =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the section handler.
	 *
	 * @param __ls The reference to the owning linker state.
	 * @since 2017/06/15
	 */
	Sections(Reference<LinkerState> __ls)
		throws NullPointerException
	{
		super(__ls);
	}
	
	/**
	 * Creates a new fragment builder which will append to the next section
	 * that is referenced.
	 *
	 * @param __c The name of the class.
	 * @param __n The name of the method.
	 * @param __t The type of the method.
	 * @param __f The flags for the method.
	 * @return The fragment builder.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/09
	 */
	public final FragmentBuilder createFragmentBuilder(ClassName __c,
		MethodName __n, MethodDescriptor __t, MethodFlags __f)
		throws NullPointerException
	{
		// Check
		if (__c == null || __n == null || __t == null || __f == null)
			throw new NullPointerException("NARG");
		
		// Create
		LinkerState ls = __linkerState();
		Reference<LinkerState> rls = ls.__reference();
		return new FragmentBuilder(rls, ls.__sectionCounter().nextSection(ls,
			__c, __n, __t, __f));
	}
	
	/**
	 * Obtains the specified section.
	 *
	 * @param __t The section to get.
	 * @return The section or {@code null} if it is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/28
	 */
	public final Section get(SectionType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		return this._sections.get(__t);
	}
	
	/**
	 * This obtains a pre-existing section or creates a new one if it has not
	 * yet been created.
	 *
	 * @param __t The section type to get the section for.
	 * @return The section which may be created as needed.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/02
	 */
	final Section __getOrCreate(SectionType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Create section if missing
		Map<SectionType, Section> sections = this._sections;
		Section rv = sections.get(__t);
		if (rv == null)
			sections.put(__t,
				(rv = new Section(__linkerState().__reference(), __t)));
		
		return rv;
	}
}

