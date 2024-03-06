// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.NativeArchiveShelf;
import cc.squirreljme.jvm.mle.brackets.NativeArchiveBracket;
import cc.squirreljme.jvm.mle.brackets.NativeArchiveEntryBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.vm.springcoat.brackets.NativeArchiveEntryObject;
import cc.squirreljme.vm.springcoat.brackets.NativeArchiveObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.io.IOException;

/**
 * SpringCoat layer for {@link NativeArchiveShelf}.
 *
 * @since 2024/03/05
 */
public enum MLENativeArchive
	implements MLEFunction
{
	/** {@link NativeArchiveShelf#archiveClose(NativeArchiveBracket)}. */
	ARCHIVE_CLOSE("archiveClose:" +
		"(Lcc/squirreljme/jvm/mle/brackets/NativeArchiveBracket;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/03/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			Object archive = __args[0];
			
			try
			{
				NativeArchiveShelf.archiveClose(
					MLENativeArchive.__archiveObject(archive).wrapped);
			}
			catch (MLECallError __e)
			{
				throw new SpringMLECallError(__e);
			}
			
			return null;
		}
	},
	
	/**
	 * {@link NativeArchiveShelf#archiveEntry(NativeArchiveBracket,
	 * String)}.
	 */
	ARCHIVE_ENTRY("archiveEntry:" +
		"(Lcc/squirreljme/jvm/mle/brackets/NativeArchiveBracket;" +
		"Ljava/lang/String;)" +
		"Lcc/squirreljme/jvm/mle/brackets/NativeArchiveEntryBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/03/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			Object archive = __args[0];
			String name = __thread.asNativeObject(String.class, __args[1]);
			
			try
			{
				NativeArchiveEntryBracket entry =
					NativeArchiveShelf.archiveEntry(
						MLENativeArchive.__archiveObject(archive).wrapped,
						name);
				if (entry == null)
					return SpringNullObject.NULL;
				
				return new NativeArchiveEntryObject(__thread.machine,
					entry);
			}
			catch (MLECallError __e)
			{
				throw new SpringMLECallError(__e);
			}
		}
	},
	
	/** {@link NativeArchiveShelf#archiveOpenZip(byte[], int, int)}. */
	ARCHIVE_OPEN_ZIP("archiveOpenZip:" +
		"([BII)Lcc/squirreljme/jvm/mle/brackets/NativeArchiveBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/03/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringArrayObjectByte buf = (SpringArrayObjectByte)__args[0];
			int off = (Integer)__args[1];
			int len = (Integer)__args[2];
			
			try
			{
				return new NativeArchiveObject(__thread.machine,
					NativeArchiveShelf.archiveOpenZip(buf.array(),
						off, len));
			}
			catch (MLECallError __e)
			{
				throw new SpringMLECallError(__e);
			}
		}
	},
	
	/**
	 * {@link NativeArchiveShelf#entryIsDirectory(NativeArchiveEntryBracket)}.
	 */
	ENTRY_IS_DIRECTORY("entryIsDirectory:" +
		"(Lcc/squirreljme/jvm/mle/brackets/NativeArchiveEntryBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/03/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			NativeArchiveEntryObject entry =
				MLENativeArchive.__entryObject(__args[0]);
			
			try
			{
				return NativeArchiveShelf.entryIsDirectory(entry.wrapped);
			}
			catch (MLECallError __e)
			{
				throw new SpringMLECallError(__e);
			}
		}
	},
	
	/** {@link NativeArchiveShelf#entryOpen(NativeArchiveEntryBracket)}. */
	ENTRY_OPEN("entryOpen:" +
		"(Lcc/squirreljme/jvm/mle/brackets/NativeArchiveEntryBracket;)" +
		"Ljava/io/InputStream;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/03/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			NativeArchiveEntryObject entry =
				MLENativeArchive.__entryObject(__args[0]);
			
			try
			{
				return __thread.proxyInputStream(
					NativeArchiveShelf.entryOpen(entry.wrapped));
			}
			catch (MLECallError|IOException __e)
			{
				throw new SpringMLECallError(__e);
			}
		}
	},
	
	/**
	 * {@link NativeArchiveShelf#entryUncompressedSize(
	 * NativeArchiveEntryBracket)}.
	 */
	ENTRY_UNCOMPRESSED_SIZE("entryUncompressedSize:" +
		"(Lcc/squirreljme/jvm/mle/brackets/NativeArchiveEntryBracket;)J")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/03/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			NativeArchiveEntryObject entry =
				MLENativeArchive.__entryObject(__args[0]);
			
			try
			{
				return NativeArchiveShelf.entryUncompressedSize(entry.wrapped);
			}
			catch (MLECallError __e)
			{
				throw new SpringMLECallError(__e);
			}
		}
	},
	
	/** End. */
	;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/05
	 */
	MLENativeArchive(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/05
	 */
	@Override
	public String key()
	{
		return this.key;
	}
	
	/**
	 * Checks if this is a {@link NativeArchiveObject}.
	 * 
	 * @param __object The object to check.
	 * @return As one if this is one.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2024/03/05
	 */
	static NativeArchiveObject __archiveObject(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof NativeArchiveObject))
			throw new SpringMLECallError("Not a NativeArchiveObject.");
		
		return (NativeArchiveObject)__object; 
	}
	
	/**
	 * Checks if this is a {@link NativeArchiveEntryObject}.
	 * 
	 * @param __object The object to check.
	 * @return As one if this is one.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2024/03/05
	 */
	static NativeArchiveEntryObject __entryObject(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof NativeArchiveEntryObject))
			throw new SpringMLECallError(
				"Not a NativeArchiveEntryObject.");
		
		return (NativeArchiveEntryObject)__object; 
	}
}
