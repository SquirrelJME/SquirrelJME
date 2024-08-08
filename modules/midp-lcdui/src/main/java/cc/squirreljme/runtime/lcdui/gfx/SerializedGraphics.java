// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import cc.squirreljme.runtime.cldc.debug.Debugging;
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
 * Translation is performed locally to the graphics and wherever the graphics
 * target is, it will not have translation forwarded and serialized to simplify
 * the target.
 *
 * @since 2018/11/19
 */
@SuppressWarnings({"ManualMinMaxCalculation", "MagicNumber"})
@Deprecated
public abstract class SerializedGraphics
	extends Graphics
{
	/** X translation. */
	protected int transx;
	
	/** Y translation. */
	protected int transy;

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
		int transx = this.transx,
			transy = this.transy;
		
		// Calculate the actual desired clip location
		int nx = __x + transx,
			ny = __y + transy,
			na = nx + __w,
			nb = ny + __h;
		
		// Get the current clip location
		int cx = this.__getClipX(),
			cy = this.__getClipY(),
			ca = cx + this.getClipWidth(),
			cb = cy + this.getClipHeight();
		
		// Use direct set clip but with the correct coordinates
		int bx = (nx > cx ? nx : cx),
			by = (ny > cy ? ny : cy);
		this.serialize(GraphicsFunction.SET_CLIP,
			bx,
			by,
			(na < ca ? na : ca) - bx,
			(nb < cb ? nb : cb) - by);
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
		int transx = this.transx,
			transy = this.transy;
		
		this.serialize(GraphicsFunction.COPY_AREA,
			__sx + transx, __sy + transy, __w, __h,
			__dx + transx, __dy + transy, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawArc(int __x, int __y, int __w, int __h, int __startAngle,
		int __arcAngle)
	{
		int transx = this.transx,
			transy = this.transy;
		
		this.serialize(GraphicsFunction.DRAW_ARC,
			__x + transx, __y + transy, __w, __h, __startAngle, __arcAngle);
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
		int transx = this.transx,
			transy = this.transy;
		
		this.serialize(GraphicsFunction.DRAW_ARGB16,
			__data, __off, __scanlen, __x + transx, __y + transy, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawChar(char __s, int __x, int __y, int __anchor)
	{
		int transx = this.transx,
			transy = this.transy;
		
		this.serialize(GraphicsFunction.DRAW_CHAR,
			(int)__s, __x + transx, __y + transy, __anchor);
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
		int transx = this.transx,
			transy = this.transy;
		
		this.serialize(GraphicsFunction.DRAW_CHARS,
			__s, __o, __l, __x + transx, __y + transy, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawImage(Image __i, int __x, int __y, int __anchor)
		throws IllegalArgumentException, NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.drawRegion(__i, 0, 0, __i.getWidth(), __i.getHeight(), 0,
			__x, __y, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		int transx = this.transx,
			transy = this.transy;
		
		this.serialize(GraphicsFunction.DRAW_LINE,
			__x1 + transx, __y1 + transy, __x2 + transx, __y2 + transy);
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
		int transx = this.transx,
			transy = this.transy;
		
		this.serialize(GraphicsFunction.DRAW_RGB,
			__data, __off, __scanlen, __x + transx, __y + transy,
			__w, __h, (__alpha ? 1 : 0));
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
		int transx = this.transx,
			transy = this.transy;
		
		this.serialize(GraphicsFunction.DRAW_RGB16,
			__data, __off, __scanlen, __x + transx, __y + transy, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawRect(int __x, int __y, int __w, int __h)
	{
		int transx = this.transx,
			transy = this.transy;
		
		this.serialize(GraphicsFunction.DRAW_RECT,
			__x + transx, __y + transy, __w, __h);
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
		if (__src == null)
			throw new NullPointerException("NARG");
		
		this.drawRegion(__src, __xsrc, __ysrc, __wsrc, __hsrc, __trans,
			__xdest, __ydest, __anch, __wsrc, __hsrc);
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
		if (__src == null)
			throw new NullPointerException("NARG");
			
		int transx = this.transx,
			transy = this.transy;
		
		// Extract image pixel data before sending over
		int numpixels = __wsrc * __hsrc;
		int[] data = new int[numpixels];
		__src.getRGB(data, 0, __wsrc, __xsrc, __ysrc, __wsrc, __hsrc);
		
		/* {@squirreljme.error EB0l Illegal region draw.} */
		int rv = (Integer)this.serialize(GraphicsFunction.DRAW_REGION,
			data, (__wsrc << 16) | __hsrc, __trans,
			__xdest + transx, __ydest + transy,
			__anch, (__wdest << 16) | __hdest);
		if (rv < 0)
			throw new IllegalArgumentException("EB0l");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void drawRoundRect(int __x, int __y, int __w, int __h,
		int __arcWidth, int __arcHeight)
	{
		int transx = this.transx,
			transy = this.transy;
		
		this.serialize(GraphicsFunction.DRAW_ROUND_RECT,
			__x + transx, __y + transy, __w, __h, __arcWidth, __arcHeight);
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
		// Just pass the chars of the string since we cannot represent
		// string at all
		this.serialize(GraphicsFunction.DRAW_SUB_CHARS,
			__s.toCharArray(), __o, __l, __x, __y, __anchor);
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
	public void fillArc(int __x, int __y, int __w, int __h, int __startAngle,
		int __arcAngle)
	{
		this.serialize(GraphicsFunction.FILL_ARC,
			__x, __y, __w, __h, __startAngle, __arcAngle);
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
		int __arcWidth, int __arcHeight)
	{
		this.serialize(GraphicsFunction.FILL_ROUND_RECT,
			__x, __y, __w, __h, __arcWidth, __arcHeight);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		this.serialize(GraphicsFunction.FILL_TRIANGLE,
			__x1, __y1, __x2, __y2, __x3, __y3);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getAlpha()
	{
		return (this.getAlphaColor() >> 24) & 0xFF;
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
		return (Integer)this.serialize(GraphicsFunction.GET_BLENDING_MODE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getBlueComponent()
	{
		return (this.getAlphaColor()) & 0xFF;
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
		return this.__getClipX() - this.transx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getClipY()
	{
		return this.__getClipY() - this.transy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getColor()
	{
		return this.getAlphaColor() & 0xFFFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getDisplayColor(int __rgb)
	{
		return (Integer)this.serialize(GraphicsFunction.GET_DISPLAY_COLOR,
			__rgb);
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
		return (this.getRedComponent() + this.getGreenComponent() +
			this.getBlueComponent()) / 3;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getGreenComponent()
	{
		return (this.getAlphaColor() >> 8) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getRedComponent()
	{
		return (this.getAlphaColor() >> 16) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getStrokeStyle()
	{
		return (Integer)this.serialize(GraphicsFunction.GET_STROKE_STYLE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getTranslateX()
	{
		return this.transx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public int getTranslateY()
	{
		return this.transy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setAlpha(int __a)
		throws IllegalArgumentException
	{
		this.setAlphaColor(__a, this.getRedComponent(),
			this.getGreenComponent(), this.getBlueComponent());
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
		this.serialize(GraphicsFunction.SET_ALPHA_COLOR,
			((__a & 0xFF) << 24) |
			((__r & 0xFF) << 16) |
			((__g & 0xFF) << 8) |
			(__b & 0xFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setBlendingMode(int __m)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB0m Failed to set blending mode.} */
		int okay = (Integer)this.serialize(GraphicsFunction.SET_BLENDING_MODE,
			__m);
		if (okay < 0)
			throw new IllegalArgumentException("EB0m");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setClip(int __x, int __y, int __w, int __h)
	{
		int transx = this.transx,
			transy = this.transy;
		
		this.serialize(GraphicsFunction.SET_CLIP,
			__x + transx, __y + transy, __w, __h);
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
		this.serialize(GraphicsFunction.SET_COLOR,
			((__r & 0xFF) << 16) |
			((__g & 0xFF) << 8) |
			(__b & 0xFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setFont(Font __font)
	{
		// Default?
		if (__font == null)
			__font = Font.getDefaultFont();
		
		// Serialize it
		this.serialize(GraphicsFunction.SET_FONT,
			SerializedGraphics.fontSerialize(__font));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void setGrayScale(int __v)
	{
		this.setAlphaColor(this.getAlpha(), __v, __v, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 * @param __s
	 */
	@Override
	public void setStrokeStyle(int __s)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB0n Failed to set stroke style.} */
		int okay = (Integer)this.serialize(GraphicsFunction.SET_STROKE_STYLE,
			__s);
		if (okay < 0)
			throw new IllegalArgumentException("EB0n");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public void translate(int __x, int __y)
	{
		this.transx += __x;
		this.transy += __y;
	}
	
	/**
	 * Returns the raw clipping X of the target.
	 *
	 * @return The target clipping.
	 * @since 2020/01/10
	 */
	private int __getClipX()
	{
		return (Integer)this.serialize(GraphicsFunction.GET_CLIP_X);
	}
	
	/**
	 * Returns the raw clipping Y of the target.
	 *
	 * @return The target clipping.
	 * @since 2020/01/10
	 */
	private int __getClipY()
	{
		return (Integer)this.serialize(GraphicsFunction.GET_CLIP_Y);
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
				
				// Get stroke style
			case GET_STROKE_STYLE:
				return __g.getStrokeStyle();
				
				// Set stroke style
			case SET_STROKE_STYLE:
				try
				{
					__g.setStrokeStyle((Integer)__args[0]);
				}
				catch (IllegalArgumentException e)
				{
					return -1;
				}
				return 0;
				
				// Copy area.
			case COPY_AREA:
				__g.copyArea(
					(Integer)__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5],
					(Integer)__args[6]);
				return null;
			
				// Draw arc.
			case DRAW_ARC:
				__g.drawArc(
					(Integer)__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5]);
				return null;
			
				// Draw ARGB16.
			case DRAW_ARGB16:
				__g.drawARGB16(
					(short[])__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5],
					(Integer)__args[6]);
				return null;
			
				// Draw character.
			case DRAW_CHAR:
				__g.drawChar(
					(char)(((Integer)__args[0]).intValue()),
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3]);
				return null;
			
				// Draw characters.
			case DRAW_CHARS:
				__g.drawChars(
					(char[])__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5]);
				return null;
			
				// Draw RGB.
			case DRAW_RGB:
				__g.drawRGB(
					(int[])__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5],
					(Integer)__args[6],
					(((Integer)__args[7]) != 0 ? true : false));
				return null;
			
				// Draw RGB16.
			case DRAW_RGB16:
				__g.drawRGB16(
					(short[])__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5],
					(Integer)__args[6]);
				return null;
			
				// Draw round rectangle.
			case DRAW_ROUND_RECT:
				__g.drawRoundRect(
					(Integer)__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5]);
				return null;
			
				// Fill arc.
			case FILL_ARC:
				__g.fillArc(
					(Integer)__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5]);
				return null;
			
				// Fill round rectangle.
			case FILL_ROUND_RECT:
				__g.fillRoundRect(
					(Integer)__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5]);
				return null;
			
				// Fill triangle.
			case FILL_TRIANGLE:
				__g.fillTriangle(
					(Integer)__args[0],
					(Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5]);
				return null;
			
				// Get blending mode.
			case GET_BLENDING_MODE:
				return __g.getBlendingMode();
			
				// Get display color.
			case GET_DISPLAY_COLOR:
				return __g.getDisplayColor((Integer)__args[0]);
			
				// Set blending mode.
			case SET_BLENDING_MODE:
				try
				{
					__g.setBlendingMode((Integer)__args[0]);
				}
				catch (IllegalArgumentException e)
				{
					return -1;
				}
				return 0;
			
				// Draw region
			case DRAW_REGION:
				try
				{
					// Extract width/heights since they are combined here
					int sw = ((Integer)__args[1]) >>> 16,
						sh = ((Integer)__args[1]) & 0xFFFF,
						dw = ((Integer)__args[6]) >>> 16,
						dh = ((Integer)__args[6]) & 0xFFFF;
					
					// Note that the passed buffer only contains image data
					// from the source region, as such the source coordinates
					// will always be zero
					__g.drawRegion(Image.createRGBImage(
							(int[])__args[0], sw, sh, true),
						0, 0,
						sw, sh,
						(Integer)__args[2],
						(Integer)__args[3],
						(Integer)__args[4],
						(Integer)__args[5],
						dw, dh);
				}
				catch (IllegalArgumentException e)
				{
					return -1;
				}
				return 0;
			
			default:
				throw Debugging.oops(__func);
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
		
		/* {@squirreljme.error EB0o Could not serialize the text object.} */
		catch (IOException e)
		{
			throw new RuntimeException("EB0o", e);
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
		
		/* {@squirreljme.error EB0p Could not serialize the text object.} */
		catch (IOException e)
		{
			throw new RuntimeException("EB0p", e);
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
		
		/* {@squirreljme.error EB0q Could not serialize the text object.} */
		catch (IOException e)
		{
			throw new RuntimeException("EB0q", e);
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
		
		/* {@squirreljme.error EB0r Could not serialize the text object.} */
		catch (IOException e)
		{
			throw new RuntimeException("EB0r", e);
		}
	}
}

