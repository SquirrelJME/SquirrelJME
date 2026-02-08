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
 * File API version.
 *
 * @since 2026/02/01
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileApiVersion
{
	/** The major version. */
	@JsonProperty("major")
	Integer major;
	
	/** The minor version. */
	@JsonProperty("minor")
	Integer minor;
	
	/** The patch version. */
	@JsonProperty("patch")
	Integer patch;
	
	/** The suffix. */
	@JsonProperty("suffix")
	String suffix;
	
	/** The full version string. */
	@JsonProperty("string")
	String string;
	
	/** Is this dirty? */
	@JsonProperty("isDirty")
	Boolean isDirty;
}
