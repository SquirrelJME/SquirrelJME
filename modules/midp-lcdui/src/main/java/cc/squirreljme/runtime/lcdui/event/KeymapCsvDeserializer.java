// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.event;

import cc.squirreljme.csv.CsvDeserializer;
import java.util.Map;

/**
 * Handles deserialization of vendor keycode CSVs.
 *
 * @since 2026/05/14
 */
final class KeymapCsvDeserializer
	implements CsvDeserializer<String[]>
{
	/**
	 * {@inheritDoc}
	 * @since 2026/05/14
	 */
	@Override
	public String[] deserialize(Map<String, String> __values)
		throws NullPointerException
	{
		return new String[] {__values.get("keycode").toLowerCase(),
			__values.get("value")};
	}
}
