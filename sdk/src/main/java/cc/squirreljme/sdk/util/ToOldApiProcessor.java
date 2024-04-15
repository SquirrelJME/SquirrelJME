// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.sdk.util;

/**
 * Automatically converts usages of new APIs to older APIs to ease writing
 * code without needing to have clunky classes being used. The main purpose
 * of this is to allow one to write more modern Java ME 8 code and not have
 * to worry about back-porting manually.
 *
 * @since 2024/04/15
 */
public class ToOldApiProcessor
	implements ClassProcessor
{
}
