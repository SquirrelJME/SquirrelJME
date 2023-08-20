// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.profiler;

import cc.squirreljme.runtime.cldc.io.HexDumpOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import net.multiphasicapps.tac.TestConsumer;

/**
 * Tests generation of the snapshots.
 *
 * @since 2023/08/19
 */
public class TestGenerate
	extends TestConsumer<String>
{
	/**
	 * {@inheritDoc}
	 * @since 2023/08/19
	 */
	@Override
	public void test(String __type)
		throws IOException
	{
		// Determine output format type
		SnapshotFormat format = SnapshotFormat.of(__type);
		
		// Initialize base snapshot
		ProfilerSnapshot snapshot = new ProfilerSnapshot();
		
		// Enter a few threads
		for (String threadName : Arrays.asList("threadA", "threadB",
			"threadC"))
		{
			// Start thread
			ProfiledThread thread =
				snapshot.measureThread(threadName);
			
			// Enter base frame
			ProfiledFrame baseFrame = thread.enterFrame(
				"baseClass", "baseMethod", "(Lbase;)V",
				1000);
			
			// Enter then exit sleep
			baseFrame.sleep(true, 2000);
			baseFrame.sleep(false, 3000);
			
			// Enter leaf-frame
			ProfiledFrame leafFrame = thread.enterFrame(
				"leafClass", "leafMethod", "(Lleaf;)V",
				4000);
			
			// Exit sub-frame
			thread.exitFrame(5000);
			
			// Enter next-frame
			ProfiledFrame nextFrame = thread.enterFrame(
				"nextClass", "nextMethod", "(Lnext;)V",
				6000);
			
			// Enter then exit sleep
			nextFrame.sleep(true, 6100);
			nextFrame.sleep(false, 6200);
			
			// Enter then exit below
			thread.enterFrame(
				"nextSubClass", "nextSubMethod", "(LnextSub;)V",
				6000);
			thread.exitFrame();
			
			// Exit next-frame
			thread.exitFrame(7000);
			
			// Enter then exit sleep
			baseFrame.sleep(true, 8000);
			baseFrame.sleep(false, 8500);
			
			// Exit base frame
			thread.exitFrame(9000);
		}
		
		// Write output to a byte array
		try (ByteArrayOutputStream out = new ByteArrayOutputStream())
		{
			// Write snapshot
			format.writer(snapshot, out).write();
			
			// Bytes for testing and otherwise
			byte[] bytes = out.toByteArray();
			
			// For manual testing, write hexdump
			HexDumpOutputStream.dump(System.err, bytes);
				
			// Are there bytes?
			this.secondary("has-bytes", bytes.length > 0);
		}
	}
}
