// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This contains the annotation table which stores all of the annotation
 * information.
 *
 * @since 2018/05/15
 */
public final class AnnotationTable
{
	/** The annotations which have been declared. */
	protected final Map<BinaryName, AnnotationElement> _annotations;
	
	/**
	 * Initializes the annotation table.
	 *
	 * @param __e The input annotations.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/15
	 */
	public AnnotationTable(AnnotationElement... __e)
		throws InvalidClassFormatException, NullPointerException
	{
		this(Arrays.<AnnotationElement>asList((__e != null ? __e :
			new AnnotationElement[0])));
	}
	
	/**
	 * Initializes the annotation table.
	 *
	 * @param __e The input annotations.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/15
	 */
	public AnnotationTable(Iterable<AnnotationElement> __e)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		Map<BinaryName, AnnotationElement> rv = new LinkedHashMap<>();
		for (AnnotationElement e : __e)
		{
			// {@squirreljme.error JC20 Duplicate annotation declared. (The
			// declared annotation)}
			BinaryName name = e.type();
			if (rv.containsKey(name))
				throw new InvalidClassFormatException(String.format("JC20 %s",
					name));
			
			rv.put(name, e);
		}
		
		this._annotations = rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Decodes the visible and invisible annotations that may exist for a
	 * class, field, or method.
	 *
	 * @param __pool The constant pool.
	 * @param __attrs The input attributes.
	 * @return The parsed annotation values.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/15
	 */
	public static final AnnotationTable parse(Pool __pool,
		AttributeTable __attrs)
		throws IOException, NullPointerException
	{
		if (__pool == null || __attrs == null)
			throw new NullPointerException("NARG");
		
		// Parse visible then invisible annotations
		List<AnnotationElement> rv = new ArrayList<>();
		for (int z = 0; z < 2; z++)
			try (DataInputStream di = __attrs.open((z == 0 ?
				"RuntimeVisibleAnnotations" : "RuntimeInvisibleAnnotations")))
			{
				if (di != null)
				{
					throw new todo.TODO();
				}
			}
		
		return new AnnotationTable(rv);
	}
}

