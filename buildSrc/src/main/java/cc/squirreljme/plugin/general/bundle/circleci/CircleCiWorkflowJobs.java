// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general.bundle.circleci;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.gradle.internal.impldep.com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CircleCI workflow jobs.
 *
 * @since 2024/03/04
 */
@Value
@Builder(toBuilder = true)
public class CircleCiWorkflowJobs
{
	/** Workflow jobs. */
	@JsonProperty("items")
	List<CircleCiWorkflowJob> items;
	
	/** Token to get the next page of results. */
	@JsonProperty("next_page_token")
	String nextPageToken;
}
