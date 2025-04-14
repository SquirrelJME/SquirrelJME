// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package io;

import cc.squirreljme.runtime.cldc.io.MarkableInputStream;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.RandomAccess;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that {@link MarkableInputStream} works properly.
 *
 * @since 2021/12/05
 */
public class MarkableInputStreamTest
	extends TestRunnable
	implements RandomAccess
{
	/** Byte message to read. */
	private static final byte[] _BYTES =
		new byte[]{'S', 'q', 'u', 'i', 'r', 'r', 'e', 'l', 's', ' ', 'a', 'r',
		'e', ' ', 'c', 'u', 't', 'e', '!'};
	
	/** The mark count to use. */
	private static final int _COUNT =
		4;
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/05
	 */
	@Override
	public void test()
		throws Throwable
	{
		try (InputStream in = new MarkableInputStream(new ByteArrayInputStream(
			MarkableInputStreamTest._BYTES)))
		{
			this.secondary("markable", in.markSupported());
			
			// Squirrels are cute!
			
			// Should be just fine
				// [S]|quirrels are cute!
			this.secondary("init1", (char)in.read());
				// S[q]|uirrels are cute!
			this.secondary("init2", (char)in.read());
			
			// Mark a few bytes and read twice, they should be the same
				// Sq^|uirr<els are cute!
			in.mark(MarkableInputStreamTest._COUNT);
			
			byte[] markA = new byte[MarkableInputStreamTest._COUNT];
			
				// Sq^[uirr]|els are cute!
			this.secondary("mark1", StreamUtils.readMostly(in, markA));
			
				// Sq^|uirr<els are cute!
			in.reset();
			
			byte[] markB = new byte[MarkableInputStreamTest._COUNT];
				// Sq^[uirr]|els are cute!
			this.secondary("mark2", StreamUtils.readMostly(in, markB));
			this.secondary("marksame", Arrays.equals(markA, markB));
			
			// Read one more byte
				// Squirr[e]|ls are cute!
			this.secondary("after1", (char)in.read());
			
			// Resetting should not work here since we read past the amount
			boolean didAReset;
			try
			{
				in.reset();
				didAReset = true;
			}
			catch (IOException e)
			{
				didAReset = false;
			}
			this.secondary("didareset", didAReset);
			
			// Mark some bytes then skip one, mark again to reset the counter
			// then do a full read again and skip
				// Squirre^|ls a<re cute!
			in.mark(MarkableInputStreamTest._COUNT);
			
			byte[] undoA = new byte[MarkableInputStreamTest._COUNT];
				// Squirre^|[ls a]<re cute!
			this.secondary("undo", StreamUtils.readMostly(in, undoA));
			
				// Squirre^|ls a<re cute!
			in.reset();
			
				// Squirre^[l]|s a<re cute!
			this.secondary("skip1", (char)in.read());
			
				// Squirrel^|s ar<e cute!
			in.mark(MarkableInputStreamTest._COUNT);
			
			byte[] skipA = new byte[MarkableInputStreamTest._COUNT];
				// Squirrel^[s ar]|e cute!
			this.secondary("skip2", StreamUtils.readMostly(in, skipA));
			this.secondary("skip2c", (char)skipA[0]);
			
				// Squirrel^|s are cute!
			in.reset();
			
			byte[] skipB = new byte[MarkableInputStreamTest._COUNT];
				// Squirrel^[s ar]|e cute!
			this.secondary("skip3", StreamUtils.readMostly(in, skipB));
			this.secondary("skip3c", (char)skipB[0]);
			this.secondary("skipsame", Arrays.equals(skipA, skipB));
			
			// Should be this character after
				// Squirrels ar[e]| cute!
			this.secondary("after2", (char)in.read());
			
			// Resetting should not work here since we read past the amount
			try
			{
				in.reset();
				didAReset = true;
			}
			catch (IOException e)
			{
				didAReset = false;
			}
			this.secondary("didaresetafter", didAReset);
			
			// Mark for a large buffer, we will read up to EOF
				// Squirrels are^| cute!<
			in.mark(MarkableInputStreamTest._BYTES.length);
			
			byte[] restA = new byte[MarkableInputStreamTest._BYTES.length];
				// Squirrels are^[ cute!]|< <-- 6
			this.secondary("rest1", StreamUtils.readMostly(in, restA));
			
				// Squirrels are^| cute!<
			in.reset();
			
			byte[] restB = new byte[MarkableInputStreamTest._BYTES.length];
				// Squirrels are^[ cute!]|< <-- 6
			this.secondary("rest2", StreamUtils.readMostly(in, restB));
			this.secondary("restsame", Arrays.equals(restA, restB));
			
			// This should be EOF
				// Squirrels are cute!| (-1)
			this.secondary("eof", in.read() < 0);
		}
	}
}
