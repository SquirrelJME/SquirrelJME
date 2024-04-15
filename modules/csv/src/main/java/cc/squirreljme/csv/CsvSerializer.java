// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.csv;

/**
 * Serializes input and output code.
 *
 * @param <T> The type to serialize to.
 * @since 2023/09/12
 */
public interface CsvSerializer<T>
{
	/**
	 * Serializes the given values.
	 *
	 * @param __input The input value.
	 * @param __result The resultant value.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	void serialize(T __input, CsvSerializerResult __result)
		throws NullPointerException;
	
	/**
	 * Writes the resultant headers for output
	 *
	 * @param __result The serializer result output.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	void serializeHeaders(CsvSerializerResult __result)
		throws NullPointerException;
}
