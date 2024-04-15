// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.WeakReference;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests a fake backend.
 *
 * @since 2023/10/15
 */
public class TestFakeBackend
	extends BaseRunRoute
{
	@Override
	protected Backend backend(String __compiler)
	{
		return new __FakeBackend__(__compiler, new WeakReference<>(this));
	}
}
