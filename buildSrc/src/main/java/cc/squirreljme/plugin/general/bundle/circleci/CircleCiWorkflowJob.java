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
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import org.gradle.internal.impldep.com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A single job within a workflow.
 *
 * @since 2024/03/04
 */
@Value
@Builder(toBuilder = true)
public class CircleCiWorkflowJob
{
	/** Canceled by. */
	@JsonProperty("canceled_by")
	UUID canceledBy;
	
	/** Dependencies. */
	@JsonProperty("dependencies")
	List<UUID> dependencies;
	
	/** Job number. */
	@JsonProperty("job_number")
	Long jobNumber;
	
	/** Job ID. */
	@JsonProperty("id")
	UUID id;
	
	/** Started at. */
	@JsonProperty("started_at")
	String startedAt;
	
	/** The job name. */
	@JsonProperty("name")
	String name;
	
	/** Approved by. */
	@JsonProperty("approved_by")
	String approvedBy;
	
	/** Project slug. */
	@JsonProperty("project_slug")
	String projectSlug;
	
	/** Status. */
	@JsonProperty("status")
	String status;
	
	/** Type. */
	@JsonProperty("type")
	String type;
	
	/** Stopped at. */
	@JsonProperty("stopped_at")
	String stoppedAt;
	
	/** Approval request ID. */
	@JsonProperty("approval_request_id")
	String approvalRequestId;
}
