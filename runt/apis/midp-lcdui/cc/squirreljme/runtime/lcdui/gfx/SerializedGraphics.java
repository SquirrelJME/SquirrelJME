// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;

/**
 * This class takes any input graphics operations that were performed for it,
 * serializing anything that was sent to it. The serialized graphics can be
 * forwarded somewhere for example for later deserialization.
 *
 * @since 2018/11/19
 */
public abstract class SerializedGraphics
	extends Graphics
{
	/**
	 * This method is called for any operation which serializes to graphics.
	 *
	 * @param __func The graphics operation to perform.
	 * @param __args The input arguments to the function.
	 * @return Any result from the operation.
	 * @since 2018/11/19
	 */
	public abstract Object serialize(GraphicsFunction __func,
		Object... __args);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void clipRect(int __x, int __y, int __w, int __h)
	{
		this.serialize(GraphicsFunction.CLIP_RECT,
			__x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void copyArea(int __sx, int __sy, int __w, int __h,
		int __dx, int __dy, int __anchor)
		throws IllegalArgumentException, IllegalStateException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawChar(char __s, int __x, int __y, int __anchor)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawChars(char[] __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawImage(Image __i, int __x, int __y, int __anchor)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		this.serialize(GraphicsFunction.DRAW_LINE,
			__x1, __y1, __x2, __y2);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawRGB(int[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h, boolean __alpha)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawRect(int __x, int __y, int __w, int __h)
	{
		this.serialize(GraphicsFunction.DRAW_RECT,
			__x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch, int __wdest, int __hdest)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawString(String __s, int __x, int __y,
		int __anchor)
		throws NullPointerException
	{
		// Just pass the chars of the string since we cannot represent
		// string at all
		this.serialize(GraphicsFunction.DRAW_SUB_CHARS,
			__s.toCharArray(), 0, __s.length(), __x, __y, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawSubstring(String __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException, StringIndexOutOfBoundsException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawText(Text __t, int __x, int __y)
	{
		this.serialize(GraphicsFunction.DRAW_TEXT,
			SerializedGraphics.textSerialize(__t), __x, __y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void fillArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void fillRect(int __x, int __y, int __w, int __h)
	{
		this.serialize(GraphicsFunction.FILL_RECT,
			__x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void fillRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getAlpha()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getAlphaColor()
	{
		return (Integer)this.serialize(GraphicsFunction.GET_ALPHA_COLOR);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getBlendingMode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getBlueComponent()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getClipHeight()
	{
		return (Integer)this.serialize(GraphicsFunction.GET_CLIP_HEIGHT);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getClipWidth()
	{
		return (Integer)this.serialize(GraphicsFunction.GET_CLIP_WIDTH);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getClipX()
	{
		return (Integer)this.serialize(GraphicsFunction.GET_CLIP_X);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getClipY()
	{
		return (Integer)this.serialize(GraphicsFunction.GET_CLIP_Y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getColor()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getDisplayColor(int __rgb)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public Font getFont()
	{
		return SerializedGraphics.fontDeserialize(
			(byte[])this.serialize(GraphicsFunction.GET_FONT));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getGrayScale()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getGreenComponent()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getRedComponent()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getStrokeStyle()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getTranslateX()
	{
		return (Integer)this.serialize(GraphicsFunction.GET_TRANSLATE_X);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getTranslateY()
	{
		return (Integer)this.serialize(GraphicsFunction.GET_TRANSLATE_Y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setAlpha(int __a)
		throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setAlphaColor(int __argb)
	{
		this.serialize(GraphicsFunction.SET_ALPHA_COLOR, __argb);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setAlphaColor(int __a, int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setBlendingMode(int __m)
		throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setClip(int __x, int __y, int __w, int __h)
	{
		this.serialize(GraphicsFunction.SET_CLIP,
			__x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setColor(int __rgb)
	{
		this.serialize(GraphicsFunction.SET_COLOR, __rgb);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setColor(int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setFont(Font __f)
	{
		// Default?
		if (__f == null)
			__f = Font.getDefaultFont();
		
		// Serialize it
		this.serialize(GraphicsFunction.SET_FONT,
			SerializedGraphics.fontSerialize(__f));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setGrayScale(int __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setStrokeStyle(int __a)
		throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void translate(int __x, int __y)
	{
		this.serialize(GraphicsFunction.TRANSLATE,
			__x, __y);
	}
	
	/**
	 * Deserializes the input operation arguments and performs the call on
	 * the destination graphics.
	 *
	 * @param __g The destination graphics object.
	 * @param __func The graphics function to call.
	 * @param __args Arguments to the function.
	 * @return The result of the call.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/19
	 */
	public static Object deserialize(Graphics __g, GraphicsFunction __func,
		Object... __args)
		throws IllegalArgumentException, NullPointerException
	{
		if (__g == null || __func == null)
			throw new NullPointerException("NARG");
		
		// Depends on the function
		switch (__func)
		{
			case CLIP_RECT:
				__g.clipRect(
					(Integer)__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3]);
				return null;
			
			case SET_COLOR:
				__g.setColor(
					(Integer)__args[0]);
				return null;
			
			case DRAW_LINE:
				__g.drawLine(
					(Integer)__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3]);
				return null;
			
			case GET_CLIP_X:
				return __g.getClipX();
			
			case GET_CLIP_Y:
				return __g.getClipY();
			
			case GET_CLIP_WIDTH:
				return __g.getClipWidth();
			
			case GET_CLIP_HEIGHT:
				return __g.getClipHeight();
			
			case GET_TRANSLATE_X:
				return __g.getTranslateX();
			
			case GET_TRANSLATE_Y:
				return __g.getTranslateY();
			
			case TRANSLATE:
				__g.translate(
					(Integer)__args[0],
					(Integer)__args[1]);
				return null;
			
			case SET_CLIP:
				__g.setClip(
					(Integer)__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3]);
				return null;
				
				// Draw rectangle
			case DRAW_RECT:
				__g.drawRect(
					(Integer)__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3]);
				return null;
				
				// Get alpha color
			case GET_ALPHA_COLOR:
				return __g.getAlphaColor();
				
				// Set alpha color
			case SET_ALPHA_COLOR:
				__g.setAlphaColor((Integer)__args[0]);
				return null;
				
				// Fill rectangle
			case FILL_RECT:
				__g.fillRect(
					(Integer)__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3]);
				return null;
				
				// Set font
			case SET_FONT:
				__g.setFont(SerializedGraphics.fontDeserialize(
					(byte[])__args[0]));
				return null;
				
				// Get font
			case GET_FONT:
				return SerializedGraphics.fontSerialize(__g.getFont());
				
				// Draw sub-characters
			case DRAW_SUB_CHARS:
				__g.drawChars(
					(char[])__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5]);
				return null;
				
				// Draw text
			case DRAW_TEXT:
				__g.drawText(
					SerializedGraphics.textDeserialize((byte[])__args[0]),
					(Integer)__args[1],
					(Integer)__args[2]);
				return null;
			
			default:
				throw new todo.OOPS("" + __func);
		}
	}
	
	/**
	 * Deserializes the font.
	 *
	 * @param __b The input byte data.
	 * @return The resulting font.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/02
	 */
	public static Font fontDeserialize(byte[] __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Deserialize all of the data
		try (ByteArrayInputStream bais = new ByteArrayInputStream(__b))
		{
			int style, pixelsize;
			String name;
			
			try (DataInputStream dis = new DataInputStream(bais))
			{
				style = dis.readInt();
				pixelsize = dis.readInt();
				name = dis.readUTF();
			}
			
			return Font.getFont(name, style, pixelsize);
		}
		
		// {@squirreljme.error EB2s Could not serialize the text object.}
		catch (IOException e)
		{
			throw new RuntimeException("EB2s", e);
		}
	}
	
	/**
	 * Deserializes the font.
	 *
	 * @param __dis The stream to read from.
	 * @return The deserialized font.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/02
	 */
	public static Font fontDeserialize(DataInputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Read byte data
		int len = __dis.readUnsignedShort();
		byte[] ser = new byte[len];
		__dis.readFully(ser);
		return SerializedGraphics.fontDeserialize(ser);
	}
	
	/**
	 * Serializes the font.
	 *
	 * @param __f The font to serialize.
	 * @return The resulting byte data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/02
	 */
	public static byte[] fontSerialize(Font __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Serialize all of the data
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(64))
		{
			try (DataOutputStream dos = new DataOutputStream(baos))
			{
				dos.writeInt(__f.getStyle());
				dos.writeInt(__f.getPixelSize());
				dos.writeUTF(__f.getFontName());
			}
			
			// Return it
			return baos.toByteArray();
		}
		
		// {@squirreljme.error EB2s Could not serialize the text object.}
		catch (IOException e)
		{
			throw new RuntimeException("EB2s", e);
		}
	}
	
	/**
	 * Serializes the font.
	 *
	 * @param __dos The stream to write to.
	 * @param __f The font to serialize.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/02
	 */
	public static void fontSerialize(DataOutputStream __dos, Font __f)
		throws IOException, NullPointerException
	{
		if (__dos == null || __f == null)
			throw new NullPointerException("NARG");
		
		// Record data
		byte[] ser = SerializedGraphics.fontSerialize(__f);
		__dos.writeShort(ser.length);
		__dos.write(ser);
	}
	
	/**
	 * Deserializes the byte array to a {@link Text} object.
	 *
	 * @param __b The byte array to deserialize.
	 * @return The deserialized text.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/02
	 */
	public static Text textDeserialize(byte[] __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Deserialize all of the data
		try (ByteArrayInputStream bais = new ByteArrayInputStream(__b))
		{
			Text rv = new Text();
			try (DataInputStream dis = new DataInputStream(bais))
			{
				rv.setWidth(dis.readInt());
				rv.setHeight(dis.readInt());
				rv.setAlignment(dis.readInt());
				rv.setBackgroundColor(dis.readInt());
				rv.setFont(SerializedGraphics.fontDeserialize(dis));
				rv.setForegroundColor(dis.readInt());
				rv.setIndent(dis.readInt());
				rv.setInitialDirection(dis.readInt());
				rv.setScrollOffset(dis.readInt());
				rv.setSpaceAbove(dis.readInt());
				rv.setSpaceBelow(dis.readInt());
				
				// Read length
				int n = dis.readInt();
				
				// Read in text string
				rv.insert(0, dis.readUTF());
				
				// Read all character properties
				for (int i = 0; i < n; i++)
				{
					rv.setForegroundColor(dis.readInt(), i, 1);
					
					// Was a font used?
					if (dis.readBoolean())
						rv.setFont(
							SerializedGraphics.fontDeserialize(dis), i, 1);
				}
				
				// And now that there is proper length
				rv.setCaret(dis.readInt());
				rv.setHighlight(dis.readInt(), dis.readInt());
			}
			
			// Return it
			return rv;
		}
		
		// {@squirreljme.error EB2s Could not serialize the text object.}
		catch (IOException e)
		{
			throw new RuntimeException("EB2s", e);
		}
	}
	
	/**
	 * Serializes the text object to a byte array.
	 *
	 * @param __t The text to serialize.
	 * @return The serialized byte array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/02
	 */
	public static byte[] textSerialize(Text __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Serialize all of the data
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(4096))
		{
			try (DataOutputStream dos = new DataOutputStream(baos))
			{
				// Properties which do not need characters
				dos.writeInt(__t.getWidth());
				dos.writeInt(__t.getHeight());
				dos.writeInt(__t.getAlignment());
				dos.writeInt(__t.getBackgroundColor());
				SerializedGraphics.fontSerialize(dos, __t.getFont());
				dos.writeInt(__t.getForegroundColor());
				dos.writeInt(__t.getIndent());
				dos.writeInt(__t.getInitialDirection());
				dos.writeInt(__t.getScrollOffset());
				dos.writeInt(__t.getSpaceAbove());
				dos.writeInt(__t.getSpaceBelow());
				
				// Record length
				int n = __t.getTextLength();
				dos.writeInt(n);
				
				// Record the string
				dos.writeUTF(__t.getText(0, n));
				
				// Record all the character properties
				for (int i = 0; i < n; i++)
				{
					dos.writeInt(__t.getForegroundColor(i));
					
					// Font needs serialization
					Font f = __t.getFont(i);
					if (f == null)
						dos.writeBoolean(false);
					else
					{
						dos.writeBoolean(true);
						SerializedGraphics.fontSerialize(dos, f);
					}
				}
				
				// Depends on character stuff
				dos.writeInt(__t.getCaret());
				dos.writeInt(__t.getHighlightIndex());
				dos.writeInt(__t.getHighlightLength());
			}
			
			// Return it
			return baos.toByteArray();
		}
		
		// {@squirreljme.error EB2s Could not serialize the text object.}
		catch (IOException e)
		{
			throw new RuntimeException("EB2s", e);
		}
	}
}

