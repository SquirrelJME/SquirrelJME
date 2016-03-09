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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.multiphasicapps.collections.MissingCollections;

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
	
	/** Local file header magic number. */
	protected static final long LFO_MAGIC_NUMBER =
		0;
	
	/** Version needed to extract. */
	protected static final long LFO_EXTRACT_VERSION =
		LFO_MAGIC_NUMBER + 4;
	
	/** General purpose flags. */
	protected static final long LFO_GENERAL_PURPOSE_FLAGS =
		LFO_EXTRACT_VERSION + 2;
	
	/** Compression method. */
	protected static final long LFO_COMPRESSION_METHOD =
		LFO_GENERAL_PURPOSE_FLAGS + 2;
	
	/** Last modification time. */
	protected static final long LFO_LAST_MODIFIED_TIME =
		LFO_COMPRESSION_METHOD + 2;
	
	/** Last modification date. */
	protected static final long LFO_LAST_MODIFIED_DATE =
		LFO_LAST_MODIFIED_TIME + 2;
	
	/** CRC-32. */
	protected static final long LFO_CRC32 =
		LFO_LAST_MODIFIED_DATE + 2;
	
	/** Compressed size. */
	protected static final long LFO_COMPRESSED_SIZE =
		LFO_CRC32 + 4;
	
	/** Uncompressed size. */
	protected static final long LFO_UNCOMPRESSED_SIZE =
		LFO_COMPRESSED_SIZE + 4;
	
	/** File name length. */
	protected static final long LFO_FILE_NAME_LENGTH =
		LFO_UNCOMPRESSED_SIZE + 4;
	
	/** Extra field length. */
	protected static final long LFO_EXTRA_FIELD_LENGTH =
		LFO_FILE_NAME_LENGTH + 2;
	
	/** The size of the local file header. */
	protected static final long BASE_FILE_HEADER_SIZE =
		LFO_EXTRA_FIELD_LENGTH + 2;
	
	/** File header magic number. */
	protected static final long FILE_HEADER_MAGIC =
		0x04034B50;
	
	/** Descriptor CRC32. */
	protected static final long DEO_CRC32 =
		0;
	
	/** Descriptor compressed size. */
	protected static final long DEO_COMPRESSED_SIZE =
		DEO_CRC32 + 4;
	
	/** Descriptor uncompressed size. */
	protected static final long DEO_UNCOMPRESSED_SIZE =
		DEO_COMPRESSED_SIZE + 4;
	
	/** Descriptor size. */
	protected static final long BASE_DESCRIPTOR_SIZE =
		DEO_UNCOMPRESSED_SIZE + 4;
	
	/** General purpose flag: File size in the data descriptor. */
	protected static final int GPF_SIZE_IN_DATA_DESCRIPTOR =
		(1 << 3);
	
	/** General purpose flag: Is UTF-8 encoded filename/comment? */
	protected static final int GPF_ENCODING_UTF8 =
		(1 << 11);
	
	/** The byte offset of the central directory. */
	protected final long cdirbase;
	
	/** The size of the central directory. */
	protected final long cdirsize;
	
	/** End of central direction position. */
	protected final long enddirpos;
	
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
			throw new ZIPFormatException.NoCentralDirectory();
		
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
		/** The size of the ZIP file. */
		protected final long zipsize;
		
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
			
			System.err.println("REQUEST " + StandardZIP32File.this.toString());
			
			// Read the directory
			long end = cdirbase + cdirsize;
			int readcount = 0;
			long totalsz = 0;
			for (long p = cdirbase; p < end; readcount++)
			{
				// Read magic number
				int cdmag = readInt(p);
				
				// Bad magic?
				if (cdmag != CENTRAL_DIRECTORY_MAGIC)
					throw new ZIPFormatException.IllegalMagic(cdmag,
						CENTRAL_DIRECTORY_MAGIC);
				
				// Set offset
				offsets[readcount] = p;
				
				// Check version
				int ver = readUnsignedShort(p + CDO_EXTRACT_VERSION);
				if (ver > MAX_CENTRAL_DIR_VERSION)
					throw new ZIPFormatException.TooNew(ver,
						MAX_CENTRAL_DIR_VERSION);
				
				// Flags
				int flags = readUnsignedShort(p + CDO_GENERAL_PURPOSE_FLAGS);
				
				// Read variable length attributes
				long varfn = readUnsignedShort(p + CDO_FILE_NAME_LENGTH);
				long varef = readUnsignedShort(p + CDO_EXTRA_FIELD_LENGTH);
				long varcm = readUnsignedShort(p + CDO_COMMENT_LENGTH);
				
				// Is a directory?
				boolean isdir = (readUnsignedByte(p +
					BASE_CENTRAL_DIRECTORY_SIZE + (varfn - 1)) == '/');
				
				// Is the size in the data descriptor?
				boolean hasddd = ((flags & GPF_SIZE_IN_DATA_DESCRIPTOR) != 0);
				
				// Get the compressed size
				long compsz = readUnsignedInt(p + CDO_COMPRESSED_SIZE);
				
				System.err.printf("At %d: %08x (%08x) %s gpf=%s%n", readcount,
					totalsz,
					offsets[readcount], isdir, hasddd);
				/*
				System.err.printf("CDO_MAGIC_NUMBER: %08x%n",
					readUnsignedInt(p + CDO_MAGIC_NUMBER));
				System.err.printf("CDO_BY_VERSION: %08x%n",
					readUnsignedShort(CDO_BY_VERSION));
				System.err.printf("CDO_EXTRACT_VERSION: %08x%n",
					readUnsignedShort(CDO_EXTRACT_VERSION));
				System.err.printf("CDO_GENERAL_PURPOSE_FLAGS: %08x%n",
					readUnsignedShort(CDO_GENERAL_PURPOSE_FLAGS));
				System.err.printf("CDO_COMPRESSION_METHOD: %08x%n",
					readUnsignedShort(CDO_COMPRESSION_METHOD));
				System.err.printf("CDO_LAST_MODIFIED_TIME: %08x%n",
					readUnsignedShort(CDO_LAST_MODIFIED_TIME));
				System.err.printf("CDO_LAST_MODIFIED_DATE: %08x%n",
					readUnsignedShort(CDO_LAST_MODIFIED_DATE));
				System.err.printf("CDO_CRC: %08x%n",
					readUnsignedShort(CDO_CRC));
				System.err.printf("CDO_COMPRESSED_SIZE: %08x%n",
					readUnsignedInt(p + CDO_COMPRESSED_SIZE));
				System.err.printf("CDO_UNCOMPRESSED_SIZE: %08x%n",
					readUnsignedInt(p + CDO_UNCOMPRESSED_SIZE));
				System.err.printf("CDO_FILE_NAME_LENGTH: %08x%n",
					readUnsignedShort(CDO_FILE_NAME_LENGTH));
				System.err.printf("CDO_EXTRA_FIELD_LENGTH: %08x%n",
					readUnsignedShort(CDO_EXTRA_FIELD_LENGTH));
				System.err.printf("CDO_COMMENT_LENGTH: %08x%n",
					readUnsignedShort(CDO_COMMENT_LENGTH));
				System.err.printf("CDO_DISK_NUMBER_START: %08x%n",
					readUnsignedShort(CDO_DISK_NUMBER_START));
				System.err.printf("CDO_INTERNAL_ATTRIBUTES: %08x%n",
					readUnsignedShort(CDO_INTERNAL_ATTRIBUTES));
				System.err.printf("CDO_EXTERNAL_ATTRIBUTES: %08x%n",
					readUnsignedInt(p + CDO_EXTERNAL_ATTRIBUTES));
				System.err.printf("CDO_LOCAL_HEADER_OFFSET: %08x%n",
					readUnsignedInt(p + CDO_LOCAL_HEADER_OFFSET));*/
				
				// Calculate the base ZIP size
				totalsz += BASE_FILE_HEADER_SIZE + varfn + varef;
				
				// Also include
				// the descriptor magic number 0x08074B50 in the calculation
				/*if (!isdir)*/
				
				// If the version number is greater than
				if (hasddd)
					totalsz += BASE_DESCRIPTOR_SIZE + 4;
				
				// Add compressed size
				if (!isdir)
					totalsz += compsz;
				
				// Data descriptor?
				/*if ((flags & GPF_SIZE_IN_DATA_DESCRIPTOR) != 0)
					totalsz += BASE_DESCRIPTOR_SIZE;*/
				
				// Skip ahead
				p += BASE_CENTRAL_DIRECTORY_SIZE + varfn + varef + varcm;
			}
			
			// Add the central directory size and the comment length (if one
			// is even used).
			totalsz += cdirsize/* + readUnsignedShort(
				enddirpos + EDO_COMMENT_LENGTH)*/;
			
			// Set
			zipsize = totalsz;
			System.err.println(zipsize + " <zip - chan> " + channel.size());
			
			// Short read?
			if (readcount != numentries)
				throw new ZIPFormatException.EntryMiscount(readcount,
					numentries);
			
			System.err.println("Entries: " + numentries);
			for (int i = 0; i < numentries; i++)
				super.get(i);
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
				throw new NullPointerException();
			
			// Set
			directory = __dir;
			index = __dx;
			centraldirpos = __off;
			
			long locfoff = (channel.size() - directory.zipsize) +
				readUnsignedInt(centraldirpos + CDO_LOCAL_HEADER_OFFSET);
			System.err.println(">> locfoff=" + locfoff + " maghere=" +
				Long.toString(readUnsignedInt(locfoff), 16) + " want=" +
				Long.toString(FILE_HEADER_MAGIC, 16) + " hoff=" +
				readUnsignedInt(centraldirpos + CDO_LOCAL_HEADER_OFFSET));
			localheaderpos = locfoff;
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
			// Get the offset of the local file header and calculate its
			// position.
			long locfoff = localheaderpos;
			
			throw new Error("TODO");
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
				int flen = readUnsignedShort(centraldirpos +
					CDO_FILE_NAME_LENGTH);
				
				// Read the input byte array
				byte barr[] = new byte[flen];
				readByteArray(centraldirpos + BASE_CENTRAL_DIRECTORY_SIZE,
					barr, 0, flen);
				
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
		 * Is the language that is used for the file name (and comment) UTF-8
		 * encoded?
		 *
		 * @throws IOException On read errors.
		 * @return {@code true} if the file name and comment are encoded using
		 * UTF-8.
		 * @since 2016/03/07
		 */
		protected boolean isLanguageUTF8()
			throws IOException
		{
			// Read data
			int bits = readUnsignedShort(centraldirpos +
				CDO_GENERAL_PURPOSE_FLAGS);
			
			// Is the bit set?
			return 0 != (bits & GPF_ENCODING_UTF8);
		}
	}
}

