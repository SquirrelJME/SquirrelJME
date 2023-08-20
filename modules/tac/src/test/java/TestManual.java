// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests manual test output.
 *
 * @since 2023/08/20
 */
public class TestManual
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public void test()
		throws IOException
	{
		// byte[]
		this.manual("array", new byte[]{'a', 'r', 'r', 'a', 'y'});
		
		// ByteArrayOutputStream
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			baos.write('b');
			baos.write('a');
			baos.write('o');
			baos.write('s');
			
			this.manual("baos", baos);
		}
		
		// InputStream
		try (InputStream in = new ByteArrayInputStream(new byte[]{'i', 'n'}))
		{
			this.manual("is", in);
		}
		
		// Get where output files go
		Path manualDir = Paths.get(
			System.getProperty("cc.squirreljme.test.manualDir"));
		
		// byte[]
		try (InputStream in = Files.newInputStream(
			manualDir.resolve("array"),
			StandardOpenOption.READ))
		{
			this.secondary("array", StreamUtils.readAll(in));
		}
		
		// ByteArrayOutputStream
		try (InputStream in = Files.newInputStream(
			manualDir.resolve("baos"),
			StandardOpenOption.READ))
		{
			this.secondary("baos", StreamUtils.readAll(in));
		}
		
		// InputStream
		try (InputStream in = Files.newInputStream(
			manualDir.resolve("is"),
			StandardOpenOption.READ))
		{
			this.secondary("is", StreamUtils.readAll(in));
		}
	}
}
