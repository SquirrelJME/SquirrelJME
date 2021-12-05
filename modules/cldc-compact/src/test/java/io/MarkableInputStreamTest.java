// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package io;

import cc.squirreljme.runtime.cldc.io.MarkableInputStream;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that {@link MarkableInputStream} works properly.
 *
 * @since 2021/12/05
 */
public class MarkableInputStreamTest
	extends TestRunnable
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
			
			// Should be just fine
			this.secondary("init1", (char)in.read());
			this.secondary("init2", (char)in.read());
			
			// Mark a few bytes and read twice, they should be the same
			in.mark(MarkableInputStreamTest._COUNT);
			
			byte[] markA = new byte[MarkableInputStreamTest._COUNT];
			
			this.secondary("mark1", StreamUtils.readMostly(in, markA));
			
			in.reset();
			
			byte[] markB = new byte[MarkableInputStreamTest._COUNT];
			this.secondary("mark2", StreamUtils.readMostly(in, markB));
			this.secondary("marksame", Arrays.equals(markA, markB));
			
			// Read one more byte
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
			in.mark(MarkableInputStreamTest._COUNT);
			
			this.secondary("skip1", (char)in.read());
			
			in.mark(MarkableInputStreamTest._COUNT);
			
			byte[] skipA = new byte[MarkableInputStreamTest._COUNT];
			this.secondary("skip1", StreamUtils.readMostly(in, skipA));
			
			in.reset();
			
			byte[] skipB = new byte[MarkableInputStreamTest._COUNT];
			this.secondary("skip2", StreamUtils.readMostly(in, skipB));
			this.secondary("skipsame", Arrays.equals(skipA, skipB));
			
			// Should be this character after
			this.secondary("after", (char)in.read());
			
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
			in.mark(MarkableInputStreamTest._BYTES.length);
			
			byte[] restA = new byte[MarkableInputStreamTest._BYTES.length];
			this.secondary("rest1", StreamUtils.readMostly(in, restA));
			
			in.reset();
			
			byte[] restB = new byte[MarkableInputStreamTest._BYTES.length];
			this.secondary("rest2", StreamUtils.readMostly(in, restB));
			this.secondary("restsame", Arrays.equals(restA, restB));
			
			// This should be EOF
			this.secondary("eof", in.read() < 0);
		}
	}
}
