// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.csv;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Map;

/**
 * Example serialization handler.
 *
 * @since 2023/09/14
 */
final class __ExampleSerialization__
	implements CsvDeserializerSerializer<String[]>
{
	/**
	 * {@inheritDoc}
	 * @since 2023/09/14
	 */
	@Override
	public String[] deserialize(Map<String, String> __values)
		throws NullPointerException
	{
		return new String[]{
			__values.get("first"),
			__values.get("second"),
			__values.get("third"),
			__values.get("fourth")
		};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/14
	 */
	@Override
	public void serialize(String[] __input, CsvSerializerResult __result)
		throws NullPointerException
	{
		int n = __input.length;
		
		// Store values
		if (n > 0)
			__result.value("first", __input[0]);
		if (n > 1)
			__result.value("second", __input[1]);
		if (n > 2)
			__result.value("third", __input[2]);
		if (n > 3)
			__result.value("fourth", __input[3]);
		
		// Finish
		__result.endRow();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/14
	 */
	@Override
	public void serializeHeaders(CsvSerializerResult __result)
		throws NullPointerException
	{
		__result.headers("first", "second", "third", "fourth");
	}
}
