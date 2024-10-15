// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.gh.classic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

/**
 * GitHub Project.
 *
 * @since 2024/10/03
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubProject
{
	/** The ID of this. */
	@JsonProperty("id")
	Integer id;
	
	/** The URL for columns. */
	@JsonProperty("columns_url")
	String columnsUrl;
}
