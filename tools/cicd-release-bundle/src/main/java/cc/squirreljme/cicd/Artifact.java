// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.cicd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * Artifact data.
 *
 * @since 2025/03/29
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Artifact
{
	/** The path of the artifact. */
	@NonNull
	String path;
	
	/** The artifact data. */
	@NonNull
	@ToString.Exclude
	byte[] data;
}
