// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.zips;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.io.CRC32InputStream;
import net.multiphasicapps.io.DataProcessorInputStream;
import net.multiphasicapps.io.InflateDataProcessor;
import net.multiphasicapps.io.SizeLimitedInputStream;

/**
 * This represents a standard ZIP file.
 *
 * 32-bit ZIPs may only have 65k entries max and are limited to 4GiB in size,
 * individual files with their header information cannot exceed 4GiB also.
 *
 * Multi-part ZIPs are treated as single part ZIP files, attempting to read
 * entries which are split between archives will result in
 * {@link IOExceptions}.
 *
 * @since 2016/03/02
 */
public class StandardZIP32File
	extends StandardZIPFile
{
	/** Maximum version. */
	protected static final int MAX_CENTRAL_DIR_VERSION =
		20;
	
	/** General purpose flag: CRC and sizes in the data descriptor. */
	protected static final int GPF_USE_DATA_DESCRIPTOR =
		(1 << 3);
	
	/** General purpose flag: Is UTF-8 encoded filename/comment? */
	protected static final int GPF_ENCODING_UTF8 =
		(1 << 11);
	
	/** Stored compression method. */
	protected static final int METHOD_STORED =
		0;
	
	/** Deflated compression method. */
	protected static final int METHOD_DEFLATED =
		8;
	
	/** The CRC magic number. */
	protected static final int CRC_MAGIC_NUMBER =
		0xDEBB20E3;
	
	/** The CRC precondition. */
	protected static final int CRC_PRECONDITION =
		0xFFFFFFFF;
	
	/** The byte offset of the central directory. */
	protected final long cdirbase;
	
	/** The size of the central directory. */
	protected final long cdirsize;
	
	/** End of central direction position. */
	protected final long enddirpos;
	
	/** The address where the ZIP starts. */
	protected final long zipstart;
	
	/** The number of entries in this ZIP. */
	protected final int numentries;
	
	/**
	 * Initializes a 32-bit ZIP file.
	 *
	 * @param __sbc The source channel to read from.
	 * @throws IOException On read errors.
	 * @throws ZIPFormatException If this is not a valid ZIP file.
	 * @since 2016/03/02
	 */
	public StandardZIP32File(SeekableByteChannel __sbc)
		throws IOException, ZIPFormatException
	{
		super(__sbc);
		
		// Try to locate the end of the central index
		long idi = Long.MIN_VALUE;
		long csz = channel.size();
		long maxsearch = Math.max(0L, csz - ZIP32EndDirectory.MAX_SIZE);
		for (long i = csz - ZIP32EndDirectory.BASE_SIZE; i >= 0; i--)
		{
			// Read magic number here
			int maybe = (int)readStruct(i,
				ZIP32EndDirectory.MAGIC_NUMBER);
			
			// If this is the magic, then check the comment length
			if (maybe == ZIP32EndDirectory.MAGIC_NUMBER_VALUE)
			{
				// Read length
				int comlen = (int)readStruct(i,
					ZIP32EndDirectory.COMMENT_LENGTH);
				
				// The comment must end at the the end of the ZIP
				if ((i + ZIP32EndDirectory.BASE_SIZE + comlen) == csz)
				{
					idi = i;
					break;
				}
			}
		}
		
		// Not a 32-bit ZIP?
		if (idi < 0L)
			throw new ZIPFormatException("ZP06");
		
		// Position is here
		enddirpos = idi;
		
		// The number of entries in this ZIP
		numentries = (int)readStruct(enddirpos,
			ZIP32EndDirectory.DISK_ENTRY_COUNT);
		
		// The size of the central directory
		cdirsize = readStruct(enddirpos,
			ZIP32EndDirectory.CENTRAL_DIR_SIZE);
		
		// The central directory starts the size count of bytes before the
		// start of end directory
		cdirbase = enddirpos - cdirsize;
		
		// The offset to the central directory will be used along with its
		// size to determine the size of the ZIP file
		long cdiroff = readStruct(enddirpos,
			ZIP32EndDirectory.CENTRAL_DIR_OFFSET);
		
		// The ZIP starts
		zipstart = csz - (cdiroff + cdirsize + (csz - enddirpos));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/05
	 */
	@Override
	protected Directory readDirectory()
		throws IOException
	{
		return new Directory32();
	}
	
	/**
	 * This represents the 32-bit ZIP directory.
	 *
	 * @since 2016/03/05
	 */
	protected class Directory32
		extends Directory
	{
		/**
		 * Initializes the 32-bit directory.
		 *
		 * @throws IOException On read/write errors.
		 * @since 2016/03/05
		 */
		protected Directory32()
			throws IOException
		{
			super(numentries);
			
			// Read the directory
			long end = cdirbase + cdirsize;
			int readcount = 0;
			long totalsz = 0;
			for (long p = cdirbase; p < end; readcount++)
			{
				// Read magic number
				int cdmag = (int)readStruct(p,
					ZIP32CentralDirectory.MAGIC_NUMBER);
				
				// Bad magic?
				if (cdmag != ZIP32CentralDirectory.MAGIC_NUMBER_VALUE)
					throw new ZIPFormatException(
						String.format("ZP07 %08x %08x",
						cdmag, ZIP32CentralDirectory.MAGIC_NUMBER_VALUE));
				
				// Set offset
				offsets[readcount] = p;
				
				// Check version
				int ver = (int)readStruct(p,
					ZIP32CentralDirectory.EXTRACT_VERSION);
				if (ver > MAX_CENTRAL_DIR_VERSION)
					throw new ZIPFormatException(String.format("ZP08 %d %d",
						ver, MAX_CENTRAL_DIR_VERSION));
				
				// Read variable length attributes
				long varfn = readStruct(p,
					ZIP32CentralDirectory.FILE_NAME_LENGTH);
				long varef = readStruct(p,
					ZIP32CentralDirectory.EXTRA_DATA_LENGTH);
				long varcm = readStruct(p,
					ZIP32CentralDirectory.COMMENT_LENGTH);
				
				// Skip ahead
				p += ZIP32CentralDirectory.BASE_SIZE + varfn + varef + varcm;
			}
			
			// Short read?
			if (readcount != numentries)
				throw new ZIPFormatException(String.format("ZP09 %d %d",
					readcount, numentries));
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/06
		 */
		@Override
		protected FileEntry readEntry(int __dx, long __off)
			throws IOException
		{
			return new FileEntry32(this, __dx, __off);
		}
	}
	
	/**
	 * This represents a 32-bit ZIP file entry.
	 *
	 * @since 2016/03/06
	 */
	protected class FileEntry32
		extends FileEntry
	{
		/** The owning central directory. */
		protected final Directory32 directory;
		
		/** Index. */
		protected final int index;
		
		/** Central directory position. */
		protected final long centraldirpos;
		
		/** Local file header position. */
		protected final long localheaderpos;
		
		/** Name cache. */
		private volatile Reference<String> _name;
		
		/**
		 * Initializes the file entry.
		 *
		 * @param __dir The central directory.
		 * @param __dx Index of this entry.
		 * @param __off The central directory offset of this entry.
		 * @throws IOException On read errors.
		 * @since 2016/03/06
		 */
		protected FileEntry32(Directory32 __dir, int __dx, long __off)
			throws IOException, NullPointerException
		{
			// Check
			if (__dir == null)
				throw new NullPointerException("NARG");
			
			// Set
			directory = __dir;
			index = __dx;
			centraldirpos = __off;
			
			// Determine the local header position
			localheaderpos = zipstart + readStruct(centraldirpos,
				ZIP32CentralDirectory.LOCAL_HEADER_OFFSET);
			
			// Make sure the magic is valid
			int lhm = (int)readStruct(localheaderpos,
				ZIP32LocalFile.MAGIC_NUMBER);
			if (lhm != ZIP32LocalFile.MAGIC_NUMBER_VALUE)
				throw new ZIPFormatException(String.format("ZP07 %08x %08x",
					lhm, ZIP32LocalFile.MAGIC_NUMBER_VALUE));
		}
		
		/**
		 * Is the language that is used for the file name (and comment) UTF-8
		 * encoded?
		 *
		 * @return {@code true} if the file name and comment are encoded using
		 * UTF-8.
		 * @throws IOException On read errors.
		 * @since 2016/03/07
		 */
		protected boolean isLanguageUTF8()
			throws IOException
		{
			// Read data
			int bits = (int)readStruct(localheaderpos,
				ZIP32LocalFile.GENERAL_PURPOSE_FLAGS);
			
			// Is the bit set?
			return 0 != (bits & GPF_ENCODING_UTF8);
		}
		
		/**
		 * Is the data descriptor to be used to determine the checksum and
		 * size of the input data?
		 *
		 * @return {@code true} if the data descriptor is to be used.
		 * @throws IOException On read errors.
		 * @since 2016/03/09
		 */
		protected boolean isUseDataDescriptor()
			throws IOException
		{
			// Get flags
			int bits = (int)readStruct(localheaderpos,
				ZIP32LocalFile.GENERAL_PURPOSE_FLAGS);
			
			// Is it set?
			return 0 != (bits & GPF_USE_DATA_DESCRIPTOR);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/06
		 */
		@Override
		public String name()
			throws IOException
		{
			// Get reference
			Reference<String> ref = _name;
			String rv = null;
			
			// In reference?
			if (ref != null)
				rv = ref.get();
			
			// Needs decoding
			if (rv == null)
			{
				// Get length of file name
				int flen = (int)readStruct(localheaderpos,
					ZIP32LocalFile.FILE_NAME_LENGTH);
				
				// Read the input byte array
				byte barr[] = new byte[flen];
				readByteArray(localheaderpos +
					ZIP32LocalFile.FILE_NAME.offset(), barr, 0, flen);
				
				// If UTF-8 then use internal handling
				if (isLanguageUTF8())
					rv = new String(barr, 0, flen, "utf-8");
				
				// Otherwise use codepage handling, Java ME only has two
				// character sets available
				else
					rv = IBM437CodePage.toString(barr, 0, flen);
				
				// Cache it
				_name = new WeakReference<>(rv);
			}
			
			// Return it
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/06
		 */
		@Override
		public int index()
		{
			return index;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/06
		 */
		@Override
		public InputStream open()
			throws IOException
		{
			// Cannot be a directory
			String n = name();
			if (n.endsWith("/"))
				throw new ZIPFormatException(String.format("ZP0a %s", n));
			
			// Obtain the compression method
			int method = (int)readStruct(localheaderpos,
				ZIP32LocalFile.COMPRESSION_METHOD);
			
			// Unknown compression method?
			if (method != METHOD_STORED && method != METHOD_DEFLATED)
				throw new ZIPFormatException(String.format("ZP0b %s %d", n,
					method));
			
			// CRC and data sizes
			int crc;
			long usz;
			long csz;
			
			// Input data position is after all structure data
			long startpos = localheaderpos + ZIP32LocalFile.BASE_SIZE +
				readStruct(localheaderpos, ZIP32LocalFile.FILE_NAME_LENGTH) +
				readStruct(localheaderpos, ZIP32LocalFile.EXTRA_DATA_LENGTH);
			
			// Use the data descriptor? If this is set then it is plausable
			// that the ZIP was streamed without knowing the actual compressed
			// and uncompressed data sizes. So in this case, just use the
			// local directory data.
			if (isUseDataDescriptor())
			{
				// Read fields
				crc = (int)readStruct(centraldirpos,
					ZIP32CentralDirectory.CRC);
				usz = readStruct(centraldirpos,
					ZIP32CentralDirectory.UNCOMPRESSED_SIZE);
				csz = readStruct(centraldirpos,
					ZIP32CentralDirectory.COMPRESSED_SIZE);
			}
			
			// Otherwise this data is in the header (it was at least known
			// before generation time).
			else
			{
				// In the file header
				crc = (int)readStruct(localheaderpos, ZIP32LocalFile.CRC);
				usz = readStruct(localheaderpos,
					ZIP32LocalFile.UNCOMPRESSED_SIZE);
				csz = readStruct(localheaderpos,
					ZIP32LocalFile.COMPRESSED_SIZE);
			}
			
			// Open raw data stream
			InputStream rawsource = new DataStream(startpos, startpos + csz);
			
			// Calculate the CRC also
			/*rawsource = new CRC32InputStream(rawsource, CRC_MAGIC_NUMBER,
				CRC_PRECONDITION, crc);*/
			
			// Stored
			if (method == METHOD_STORED)
				return rawsource;
			
			// Deflated
			else if (method == METHOD_DEFLATED)
				return new SizeLimitedInputStream(
					new DataProcessorInputStream(rawsource,
						new InflateDataProcessor()), usz, true);
			
			// Unknown
			else
				throw new ZIPFormatException(String.format("ZP0b %s %d", n,
					method));
		}
	}
}

