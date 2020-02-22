package cc.squirreljme.plugin;

import java.io.*;

/**
 * This is used to read comments from a source code file.
 *
 * @since 2020/02/22
 */
public class CommentReader
	extends Reader
{
	/** The source reader. */
	protected final Reader source;
	
	/** The input character buffer. */
	private final StringBuilder _buffer =
		new StringBuilder();
	
	/** The type of comment being handled. */
	private CommentType _commentType =
		CommentType.NONE;
	
	/**
	 * Initializes the comment reader.
	 *
	 * @param __in The input source.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public CommentReader(InputStream __in)
		throws NullPointerException
	{
		try
		{
			this.source = new InputStreamReader(__in, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("UTF-8 is not supported?", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/22
	 */
	@Override
	public void close()
		throws IOException
	{
		this.source.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2002/02/22
	 */
	@Override
	public int read(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, IOException
	{
		if (__o < 0 || __l < 0 || (__o + __l) > __c.length)
			throw new IndexOutOfBoundsException();
			
		Reader source = this.source;
		CommentType commentType = this._commentType;
		StringBuilder buffer = this._buffer;
		boolean forceRead = false,
			forceWrite = false;
		
		// Stream processing loop
		try
		{
			int putCount = 0;
			
			// Still more room in the output array?
			while (putCount < __l)
			{
				// If the buffer is empty fill it or if we want more
				int blen = buffer.length();
				if (forceRead || blen <= 0)
				{
					int rc = source.read();
					
					// EOF reached
					if (rc < 0)
						return (putCount > 0 ? putCount : -1);
					
					buffer.append((char)rc);
					
					// Do not want to force a read again
					forceRead = false;
				}
				
				// Single character in the buffer
				else if (blen == 1)
				{
					char ch = buffer.charAt(0);
					
					// End of line reached?
					if (ch == '\r' || ch == '\n')
					{
						// Clear the buffer
						buffer.delete(0, 1);
						
						// End of single line comment?
						if (commentType == CommentType.SINGLE)
							commentType = CommentType.NONE;
					}
					
					// We need more context to determine what to do here
					// * Could be opening double-slash on line?
					// * Could be closing block comment?
					else if ((commentType == CommentType.NONE && ch == '/') ||
						(commentType == CommentType.BLOCK && ch == '*'))
					{
						forceRead = true;
					}
					
					// Either pump it out, or drop it
					else
					{
						// Clear the buffer
						buffer.delete(0, 1);
						
						// Write to our output
						if (commentType != CommentType.NONE)
							__c[__o + (putCount++)] = ch;
					}
				}
				
				// Other buffer sizes
				else
				{
					// Start of single-line comment
					if (commentType == CommentType.NONE &&
						buffer.indexOf("//") == 0)
					{
						// Delete two characters
						buffer.delete(0, 2);
						
						// Is now single line comment
						commentType = CommentType.SINGLE;
					}
					
					// Start of block comment
					else if (commentType == CommentType.NONE &&
						buffer.indexOf("/*") == 0)
					{
						// Delete two characters
						buffer.delete(0, 2);
						
						// Is now block comment
						commentType = CommentType.BLOCK;
					}
					
					// End of block comment
					else if (commentType == CommentType.BLOCK &&
						buffer.indexOf("*/") == 0)
					{
						// Delete two characters
						buffer.delete(0, 2);
						
						// Not in a comment
						commentType = CommentType.NONE;
					}
					
					// Other sequence, just consume the character
					else
					{
						// Consume one character
						char ch = buffer.charAt(0);
						buffer.delete(0, 1);
						
						// End of single line comment?
						if (ch == '\r' || ch == '\n')
						{
							if (commentType == CommentType.SINGLE)
								commentType = CommentType.NONE;
						}
						
						// Write to our output
						else if (commentType != CommentType.NONE)
							__c[__o + (putCount++)] = ch;
					}
				}
			}
			
			return putCount;
		}
		
		// Store any changed parameters potentially
		finally
		{
			this._commentType = commentType;
		}
	}
	
	/**
	 * Reads the entire file to a string.
	 *
	 * @param __sb The input buffer to write to.
	 * @return {@code __sb}.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public final StringBuilder readAll(StringBuilder __sb)
		throws IOException, NullPointerException
	{
		if (__sb == null)
			throw new NullPointerException();
		
		char[] buf = new char[512];
		for (;;)
		{
			int rc = this.read(buf, 0, 512);
			
			if (rc < 0)
				break;
			
			__sb.append(buf, 0, rc);
		}
		
		return __sb;
	}
	
	/**
	 * The type of comment being parsed.
	 *
	 * @since 2020/02/22
	 */
	private static enum CommentType
	{
		/** No comment. */
		NONE,
		
		/** Single line comment. */
		SINGLE,
		
		/** Block comment. */
		BLOCK,
		
		/** End. */
		;
	}
}
