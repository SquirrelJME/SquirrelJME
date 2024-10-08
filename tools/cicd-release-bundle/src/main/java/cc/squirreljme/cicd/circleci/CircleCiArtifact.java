// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.cicd.circleci;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents an artifact.
 *
 * @since 2024/10/05
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CircleCiArtifact
{
	/** Node index? */
	@JsonProperty("node_index")
	Integer nodeIndex;
	
	/** The path to the artifact. */
	@JsonProperty("path")
	String path;
	
	/** The URL for downloading. */
	@JsonProperty("url")
	String url;
}
