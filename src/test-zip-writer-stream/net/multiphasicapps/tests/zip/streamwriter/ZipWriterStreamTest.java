// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.zip.streamwriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import java.util.Random;
import net.multiphasicapps.tests.IndividualTest;
import net.multiphasicapps.tests.InvalidTestException;
import net.multiphasicapps.tests.TestGroupName;
import net.multiphasicapps.tests.TestFamily;
import net.multiphasicapps.tests.TestInvoker;
import net.multiphasicapps.util.seekablearray.SeekableByteArrayChannel;
import net.multiphasicapps.zip.blockreader.ZipEntry;
import net.multiphasicapps.zip.blockreader.ZipFile;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;
import net.multiphasicapps.zip.ZipCompressionType;

/**
 * This tests that ZIP files are correctly streamed and that any output ZIP
 * file can be read by the already existing ZIP code.
 *
 * This test also forms the basis for the stream based ZIP reader support.
 *
 * @since 2016/07/10
 */
public class ZipWriterStreamTest
	implements TestInvoker
{
	/** The number of files to write uncompressed and compressed. */
	public static final int NUM_FILES =
		2;
	
	/** The size of the files to write, does not have to be large. */
	public static final int FILE_SIZE =
		384;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void runTest(IndividualTest __t)
		throws NullPointerException, Throwable
	{
		// Check
		if (__t == null)
			throw new NullPointerException();
		
		// Get random seed to generate some files with
		Random rand = new Random(Long.decode(__t.subName().toString()));
		
		// Create a ZIP with a bunch of random files
		byte[] zipdata = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// The stream must be closed before the byte array becomes a valid
			// ZIP file
			try (ZipStreamWriter zsw = new ZipStreamWriter(baos))
			{
				// Write a number of files, one compressed and the other not
				// compressed
				int nf = NUM_FILES, fs = FILE_SIZE;
				for (int f = 0; f < nf; f++)
				{
					// Random data used to write
					long frseed = rand.nextLong();
				
					// Setup next ZIP entry
					for (int q = 0; q < 2; q++)
						try (OutputStream os = zsw.nextEntry((q == 0 ? "n" :
							"c") + frseed, (q == 0 ?
							ZipCompressionType.NO_COMPRESSION :
							ZipCompressionType.DEFLATE)))
						{
							// Setup random
							Random fr = new Random(frseed);
							
							// Write bytes to the entry
							for (int s = 0; s < fs; s++)
								os.write(fr.nextInt(255));
						}
				}
			}
			
			// Get ZIP data
			zipdata = baos.toByteArray();
		}
		
		// Read the input ZIP file that was created in memory and try to
		// see if entries were written correctly. If the ZIP cannot be opened
		// here then it is malformed.
		try (ZipFile zip = ZipFile.open(new SeekableByteArrayChannel(zipdata)))
		{
			// Go through all entries and check if they contain valid data
			for (ZipEntry ze : zip)
				throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public TestFamily testFamily()
	{
		// Generate some random seeds
		Random rand = new Random(0x1989_07_06);
		
		return new TestFamily(
			"net.multiphasicapps.zip.streamwriter.ZipStreamWriter",
			Long.toString(rand.nextLong()),
			Long.toString(rand.nextLong()),
			Long.toString(rand.nextLong()));
	}
}

