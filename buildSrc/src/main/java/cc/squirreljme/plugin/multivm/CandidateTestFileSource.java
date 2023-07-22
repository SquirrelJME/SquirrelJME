// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.util.FileLocation;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Candidate test file sources.
 *
 * @since 2022/09/05
 */
@AllArgsConstructor
@Value
@Builder
public class CandidateTestFileSource
{
	/** Primary set of sources? */
	public boolean primary;
	
	/** The files making up the sources. */
	public Collection<FileLocation> collection;
}
