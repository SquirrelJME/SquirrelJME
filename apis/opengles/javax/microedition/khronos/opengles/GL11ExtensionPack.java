// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.opengles;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface GL11ExtensionPack
	extends GL
{
	public static final int GL_BLEND_DST_ALPHA =
		32970;
	
	public static final int GL_BLEND_DST_RGB =
		32968;
	
	public static final int GL_BLEND_EQUATION =
		32777;
	
	public static final int GL_BLEND_EQUATION_ALPHA =
		34877;
	
	public static final int GL_BLEND_EQUATION_RGB =
		32777;
	
	public static final int GL_BLEND_SRC_ALPHA =
		32971;
	
	public static final int GL_BLEND_SRC_RGB =
		32969;
	
	public static final int GL_COLOR_ATTACHMENT0_OES =
		36064;
	
	public static final int GL_COLOR_ATTACHMENT10_OES =
		36074;
	
	public static final int GL_COLOR_ATTACHMENT11_OES =
		36075;
	
	public static final int GL_COLOR_ATTACHMENT12_OES =
		36076;
	
	public static final int GL_COLOR_ATTACHMENT13_OES =
		36077;
	
	public static final int GL_COLOR_ATTACHMENT14_OES =
		36078;
	
	public static final int GL_COLOR_ATTACHMENT15_OES =
		36079;
	
	public static final int GL_COLOR_ATTACHMENT1_OES =
		36065;
	
	public static final int GL_COLOR_ATTACHMENT2_OES =
		36066;
	
	public static final int GL_COLOR_ATTACHMENT3_OES =
		36067;
	
	public static final int GL_COLOR_ATTACHMENT4_OES =
		36068;
	
	public static final int GL_COLOR_ATTACHMENT5_OES =
		36069;
	
	public static final int GL_COLOR_ATTACHMENT6_OES =
		36070;
	
	public static final int GL_COLOR_ATTACHMENT7_OES =
		36071;
	
	public static final int GL_COLOR_ATTACHMENT8_OES =
		36072;
	
	public static final int GL_COLOR_ATTACHMENT9_OES =
		36073;
	
	public static final int GL_DECR_WRAP =
		34056;
	
	public static final int GL_DEPTH_ATTACHMENT_OES =
		36096;
	
	public static final int GL_DEPTH_COMPONENT =
		6402;
	
	public static final int GL_DEPTH_COMPONENT16 =
		33189;
	
	public static final int GL_DEPTH_COMPONENT24 =
		33190;
	
	public static final int GL_DEPTH_COMPONENT32 =
		33191;
	
	public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_OES =
		36049;
	
	public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_OES =
		36048;
	
	public static final int 
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_OES =
		36051;
	
	public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_OES =
		36050;
	
	public static final int GL_FRAMEBUFFER_BINDING_OES =
		36006;
	
	public static final int GL_FRAMEBUFFER_COMPLETE_OES =
		36053;
	
	public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_OES =
		36054;
	
	public static final int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_OES =
		36057;
	
	public static final int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_OES =
		36059;
	
	public static final int GL_FRAMEBUFFER_INCOMPLETE_FORMATS_OES =
		36058;
	
	public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_OES 
		=
		36055;
	
	public static final int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_OES =
		36060;
	
	public static final int GL_FRAMEBUFFER_OES =
		36160;
	
	public static final int GL_FRAMEBUFFER_UNSUPPORTED_OES =
		36061;
	
	public static final int GL_FUNC_ADD =
		32774;
	
	public static final int GL_FUNC_REVERSE_SUBTRACT =
		32779;
	
	public static final int GL_FUNC_SUBTRACT =
		32778;
	
	public static final int GL_INCR_WRAP =
		34055;
	
	public static final int GL_INVALID_FRAMEBUFFER_OPERATION_OES =
		1286;
	
	public static final int GL_MAX_COLOR_ATTACHMENTS_OES =
		36063;
	
	public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE =
		34076;
	
	public static final int GL_MAX_RENDERBUFFER_SIZE_OES =
		34024;
	
	public static final int GL_MIRRORED_REPEAT =
		33648;
	
	public static final int GL_NORMAL_MAP =
		34065;
	
	public static final int GL_REFLECTION_MAP =
		34066;
	
	public static final int GL_RENDERBUFFER_ALPHA_SIZE_OES =
		36179;
	
	public static final int GL_RENDERBUFFER_BINDING_OES =
		36007;
	
	public static final int GL_RENDERBUFFER_BLUE_SIZE_OES =
		36178;
	
	public static final int GL_RENDERBUFFER_DEPTH_SIZE_OES =
		36180;
	
	public static final int GL_RENDERBUFFER_GREEN_SIZE_OES =
		36177;
	
	public static final int GL_RENDERBUFFER_HEIGHT_OES =
		36163;
	
	public static final int GL_RENDERBUFFER_INTERNAL_FORMAT_OES =
		36164;
	
	public static final int GL_RENDERBUFFER_OES =
		36161;
	
	public static final int GL_RENDERBUFFER_RED_SIZE_OES =
		36176;
	
	public static final int GL_RENDERBUFFER_STENCIL_SIZE_OES =
		36181;
	
	public static final int GL_RENDERBUFFER_WIDTH_OES =
		36162;
	
	public static final int GL_RGB565_OES =
		36194;
	
	public static final int GL_RGB5_A1 =
		32855;
	
	public static final int GL_RGB8 =
		32849;
	
	public static final int GL_RGBA4 =
		32854;
	
	public static final int GL_RGBA8 =
		32856;
	
	public static final int GL_STENCIL_ATTACHMENT_OES =
		36128;
	
	public static final int GL_STENCIL_INDEX =
		6401;
	
	public static final int GL_STENCIL_INDEX1_OES =
		36166;
	
	public static final int GL_STENCIL_INDEX4_OES =
		36167;
	
	public static final int GL_STENCIL_INDEX8_OES =
		36168;
	
	public static final int GL_STR =
		-1;
	
	public static final int GL_TEXTURE_BINDING_CUBE_MAP =
		34068;
	
	public static final int GL_TEXTURE_CUBE_MAP =
		34067;
	
	public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X =
		8318;
	
	public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y =
		8320;
	
	public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z =
		8322;
	
	public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X =
		8317;
	
	public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y =
		8319;
	
	public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z =
		8321;
	
	public static final int GL_TEXTURE_GEN_MODE =
		9472;
	
	public static final int GL_TEXTURE_GEN_STR =
		36192;
	
	public abstract void glBindFramebufferOES(int __a, int __b);
	
	public abstract void glBindRenderbufferOES(int __a, int __b);
	
	public abstract void glBindTexture(int __a, int __b);
	
	public abstract void glBlendEquation(int __a);
	
	public abstract void glBlendEquationSeparate(int __a, int __b);
	
	public abstract void glBlendFuncSeparate(int __a, int __b, int __c, int 
		__d);
	
	public abstract int glCheckFramebufferStatusOES(int __a);
	
	public abstract void glCompressedTexImage2D(int __a, int __b, int __c, 
		int __d, int __e, int __f, int __g, Buffer __h);
	
	public abstract void glCopyTexImage2D(int __a, int __b, int __c, int __d,
		int __e, int __f, int __g, int __h);
	
	public abstract void glDeleteFramebuffersOES(int __a, int[] __b, int __c
		);
	
	public abstract void glDeleteFramebuffersOES(int __a, IntBuffer __b);
	
	public abstract void glDeleteRenderbuffersOES(int __a, int[] __b, int __c
		);
	
	public abstract void glDeleteRenderbuffersOES(int __a, IntBuffer __b);
	
	public abstract void glEnable(int __a);
	
	public abstract void glFramebufferRenderbufferOES(int __a, int __b, int 
		__c, int __d);
	
	public abstract void glFramebufferTexture2DOES(int __a, int __b, int __c,
		int __d, int __e);
	
	public abstract void glGenFramebuffersOES(int __a, int[] __b, int __c);
	
	public abstract void glGenFramebuffersOES(int __a, IntBuffer __b);
	
	public abstract void glGenRenderbuffersOES(int __a, int[] __b, int __c);
	
	public abstract void glGenRenderbuffersOES(int __a, IntBuffer __b);
	
	public abstract void glGenerateMipmapOES(int __a);
	
	public abstract void glGetFramebufferAttachmentParameterivOES(int __a, 
		int __b, int __c, int[] __d, int __e);
	
	public abstract void glGetFramebufferAttachmentParameterivOES(int __a, 
		int __b, int __c, IntBuffer __d);
	
	public abstract void glGetIntegerv(int __a, int[] __b, int __c);
	
	public abstract void glGetIntegerv(int __a, IntBuffer __b);
	
	public abstract void glGetRenderbufferParameterivOES(int __a, int __b, 
		int[] __c, int __d);
	
	public abstract void glGetRenderbufferParameterivOES(int __a, int __b, 
		IntBuffer __c);
	
	public abstract void glGetTexGenfv(int __a, int __b, float[] __c, int __d
		);
	
	public abstract void glGetTexGenfv(int __a, int __b, FloatBuffer __c);
	
	public abstract void glGetTexGeniv(int __a, int __b, int[] __c, int __d);
	
	public abstract void glGetTexGeniv(int __a, int __b, IntBuffer __c);
	
	public abstract void glGetTexGenxv(int __a, int __b, int[] __c, int __d);
	
	public abstract void glGetTexGenxv(int __a, int __b, IntBuffer __c);
	
	public abstract boolean glIsFramebufferOES(int __a);
	
	public abstract boolean glIsRenderbufferOES(int __a);
	
	public abstract void glRenderbufferStorageOES(int __a, int __b, int __c, 
		int __d);
	
	public abstract void glStencilOp(int __a, int __b, int __c);
	
	public abstract void glTexEnvf(int __a, int __b, float __c);
	
	public abstract void glTexEnvfv(int __a, int __b, float[] __c, int __d);
	
	public abstract void glTexEnvfv(int __a, int __b, FloatBuffer __c);
	
	public abstract void glTexEnvx(int __a, int __b, int __c);
	
	public abstract void glTexEnvxv(int __a, int __b, int[] __c, int __d);
	
	public abstract void glTexEnvxv(int __a, int __b, IntBuffer __c);
	
	public abstract void glTexGenf(int __a, int __b, float __c);
	
	public abstract void glTexGenfv(int __a, int __b, float[] __c, int __d);
	
	public abstract void glTexGenfv(int __a, int __b, FloatBuffer __c);
	
	public abstract void glTexGeni(int __a, int __b, int __c);
	
	public abstract void glTexGeniv(int __a, int __b, int[] __c, int __d);
	
	public abstract void glTexGeniv(int __a, int __b, IntBuffer __c);
	
	public abstract void glTexGenx(int __a, int __b, int __c);
	
	public abstract void glTexGenxv(int __a, int __b, int[] __c, int __d);
	
	public abstract void glTexGenxv(int __a, int __b, IntBuffer __c);
	
	public abstract void glTexParameterf(int __a, int __b, float __c);
}


