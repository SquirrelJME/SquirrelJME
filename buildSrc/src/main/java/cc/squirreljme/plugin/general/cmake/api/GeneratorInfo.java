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
 * Generator information.
 *
 * @since 2026/02/01
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneratorInfo
{
	/** Is this a multi-config CMake? */
	@JsonProperty("multiConfig")
	Boolean multiConfig;
	
	/** The name of the generator. */
	@JsonProperty("name")
	String name;
	
	/** The generator platform. */
	@JsonProperty("platform")
	String platform;
}
