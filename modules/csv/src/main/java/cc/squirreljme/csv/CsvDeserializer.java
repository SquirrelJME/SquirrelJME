// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.csv;

import java.util.Map;

/**
 * Handles deserialization of CSVs.
 *
 * @param <T> The type this deserializes to.
 * @since 2023/09/12
 */
public interface CsvDeserializer<T>
{
	/**
	 * Deserializes the given values.
	 *
	 * @param __values The values to deserialize.
	 * @return The deserialized values.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	T deserialize(Map<String, String> __values)
		throws NullPointerException;
}
