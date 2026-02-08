// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general.cmake.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * A reply file reference
 *
 * @since 2026/02/01
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplyFile
{
	/** The kind. */
	@JsonProperty("kind")
	ObjectKind kind;
	
	/** The version. */
	@JsonProperty("version")
	FileApiVersion version;
	
	/** The reference file data. */
	@JsonProperty("jsonFile")
	String jsonFile;
}
