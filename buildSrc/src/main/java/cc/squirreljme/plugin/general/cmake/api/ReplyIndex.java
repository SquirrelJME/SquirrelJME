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
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * The reply index.
 *
 * @since 2026/02/01
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplyIndex
{
	/** CMake Information. */
	@JsonProperty("cmake")
	CMakeInfo cmake;
	
	/** Object kinds. */
	@JsonProperty("objects")
	List<ObjectKind> objects;
	
	/** Opaque reply for {@code query/}. */
	@JsonProperty("reply")
	Map<String, Object> reply;
}
