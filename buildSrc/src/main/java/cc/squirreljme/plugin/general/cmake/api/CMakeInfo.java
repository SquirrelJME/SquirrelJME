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
import java.util.Map;
import lombok.Data;

/**
 * CMake information.
 *
 * @since 2026/02/01
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CMakeInfo
{
	@JsonProperty("version")
	FileApiVersion version;
	
	@JsonProperty("paths")
	Map<String, String> paths;
	
	@JsonProperty("generator")
	GeneratorInfo generator;
}
