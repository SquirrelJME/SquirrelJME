// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nokia.mid.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import javax.microedition.lcdui.Image;

/**
 * This interface provides direct framebuffer pixel access.
 *
 * @since 2019/10/07
 */
@Api
public interface DirectGraphics
{
	@Api
	int FLIP_HORIZONTAL = 8192;
	
	@Api
	int FLIP_VERTICAL = 16384;
	
	@Api
	int ROTATE_180 = 180;
	
	@Api
	int ROTATE_270 = 270;
	
	@Api
	int ROTATE_90 = 90;
	
	@Api
	int TYPE_BYTE_1_GRAY = 1;
	
	@Api
	int TYPE_BYTE_1_GRAY_VERTICAL = -1;
	
	@Api
	int TYPE_BYTE_2_GRAY = 2;
	
	@Api
	int TYPE_BYTE_332_RGB = 332;
	
	@Api
	int TYPE_BYTE_4_GRAY = 4;
	
	@Api
	int TYPE_BYTE_8_GRAY = 8;
	
	@Api
	int TYPE_INT_888_RGB = 888;
	
	@Api
	int TYPE_INT_8888_ARGB = 8888;
	
	@Api
	int TYPE_USHORT_1555_ARGB = 1555;
	
	@Api
	int TYPE_USHORT_444_RGB = 444;
	
	@Api
	int TYPE_USHORT_4444_ARGB = 4444;
	
	@Api
	int TYPE_USHORT_555_RGB = 555;
	
	@Api
	int TYPE_USHORT_565_RGB = 565;
	
	@ApiDefinedDeprecated
	void drawImage(Image __var1, int __var2, int __var3, int __var4,
		int __var5);
	
	@Api
	void drawPixels(byte[] __var1, byte[] __var2, int __var3, int __var4,
		int __var5, int __var6, int __var7, int __var8, int __var9,
		int __var10);
	
	@Api
	void drawPixels(short[] __var1, boolean __var2, int __var3, int __var4,
		int __var5, int __var6, int __var7, int __var8, int __var9,
		int __var10);
	
	@Api
	void drawPixels(int[] __var1, boolean __var2, int __var3, int __var4,
		int __var5, int __var6, int __var7, int __var8, int __var9,
		int __var10);
	
	@Api
	void drawPolygon(int[] __var1, int __var2, int[] __var3, int __var4,
		int __var5, int __var6);
	
	@Api
	void drawTriangle(int __var1, int __var2, int __var3, int __var4,
		int __var5, int __var6, int __var7);
	
	@Api
	void fillPolygon(int[] __var1, int __var2, int[] __var3, int __var4,
		int __var5, int __var6);
	
	@Api
	void fillTriangle(int __var1, int __var2, int __var3, int __var4,
		int __var5, int __var6, int __var7);
	
	@Api
	void getPixels(byte[] __var1, byte[] __var2, int __var3, int __var4,
		int __var5, int __var6, int __var7, int __var8, int __var9);
	
	@Api
	void getPixels(int[] __var1, int __var2, int __var3, int __var4,
		int __var5, int __var6, int __var7, int __var8);
	
	@Api
	void getPixels(short[] __var1, int __var2, int __var3, int __var4,
		int __var5, int __var6, int __var7, int __var8);
	
	@Api
	void setARGBColor(int __var1);
}

