// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Shared CSV entry.
 *
 * @since 2023/09/03
 */
@Data
@NoArgsConstructor
public class SharedCsvEntry
{
	/** The prefix for the shared root. */
	@CsvBindByName(column = "prefix", required = true)
	String prefix;
	
	/** The identifier used. */
	@CsvBindByName(column = "identifier", required = true)
	String identifier;
	
	/** The header file path. */
	@CsvBindByName(column = "header", required = true)
	String header;
	
	/** The source file path. */
	@CsvBindByName(column = "source", required = true)
	String source;
}
