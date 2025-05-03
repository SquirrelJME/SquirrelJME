// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.keitaiwiki.music;

import java.util.ArrayList;

/**
 * Event list
 */
class MLDTrack
	extends ArrayList<MLDEvent>
	implements BasicTrack
{
	/**
	 * Initial event offset on reset
	 */
	int cue;
	
	/**
	 * Channel index base
	 */
	int index;
}
