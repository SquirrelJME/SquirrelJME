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

/**
 * GitHub Project Column.
 *
 * @since 2024/10/03
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubColumn
{
	/** The ID of this. */
	@JsonProperty("id")
	Integer id;
	
	/** URL to individual cards. */
	@JsonProperty("cards_url")
	String cardsUrl;
}
