// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import com.oracle.json.JsonArrayBuilder;
import com.oracle.json.JsonObject;
import com.oracle.json.JsonReader;
import com.oracle.json.JsonStructure;
import com.oracle.json.JsonValue;
import com.oracle.json.JsonWriter;
import com.oracle.json.spi.JsonProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Manager for preferences.
 *
 * @since 2024/01/28
 */
public class PreferencesManager
{
	/** The path where the configuration is. */
	protected final Path configPath;
	
	/**
	 * Initializes the preference manager.
	 *
	 * @since 2024/01/28
	 */
	public PreferencesManager()
	{
		this.configPath = PreferencesManager.baseDirectory()
			.resolve("debugger.json");
	}
	
	/**
	 * Loads the given preferences.
	 *
	 * @param __prefs The preferences to load.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/28
	 */
	public void load(Preferences __prefs)
		throws NullPointerException
	{
		if (__prefs == null)
			throw new NullPointerException("NARG");
		
		// Load in the JSON data
		JsonProvider provider = JsonProvider.provider();
		try (InputStream in = Files.newInputStream(this.configPath,
			StandardOpenOption.READ);
			 JsonReader reader = provider.createReader(in))
		{
			// Read in JSON structure
			JsonObject struct = reader.readObject();
			
			__prefs.setResumeOnConnect(
				struct.getBoolean("resumeOnConnect"));
			__prefs.setLastAddress(
				struct.getString("lastAddress"));
			
			// Parse in all paths
			List<Path> paths = new ArrayList<>();
			for (JsonValue value : struct.getJsonArray("classSearchPath"))
				paths.add(Paths.get(value.toString()));
			__prefs.getClassSearchPath().clear();
			__prefs.getClassSearchPath().addAll(paths);
		}
		catch (IOException __e)
		{
			__e.printStackTrace();
		}
	}
	
	/**
	 * Stores the given preferences.
	 *
	 * @param __prefs The preferences to store.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/28
	 */
	public void store(Preferences __prefs)
		throws IOException, NullPointerException
	{
		if (__prefs == null)
			throw new NullPointerException("NARG");
		
		// Write to disk
		Path tempFile = null;
		JsonProvider provider = JsonProvider.provider();
		try
		{
			// Write everything into a temporary file first
			tempFile = Files.createTempFile("debugger", ".json");
			try (OutputStream out = Files.newOutputStream(tempFile,
				StandardOpenOption.WRITE, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
				JsonWriter writer = provider.createWriter(out))
			{
				// Search path where everything is
				JsonArrayBuilder searchPath = provider.createArrayBuilder();
				for (Path path : __prefs.getClassSearchPath())
					searchPath.add(path.toAbsolutePath().toString());
				
				// Build base object
				JsonObject object = provider.createObjectBuilder()
					.add("resumeOnConnect", __prefs.isResumeOnConnect())
					.add("lastAddress", __prefs.getLastAddress())
					.add("classSearchPath", searchPath)
					.build();
				
				// Write it out
				writer.writeObject(object);
			}
			
			// Make sure the configuration directory actually exists
			Path configPath = this.configPath;
			Files.createDirectories(configPath.getParent());
			
			// Then move over the configuration
			Files.move(tempFile, configPath);
		}
		
		// Cleanup temp file if it exists
		finally
		{
			if (tempFile != null)
				try
				{
					Files.delete(tempFile);
				}
				catch (IOException __ignored)
				{
					// Ignored
				}
		}
	}
	
	/**
	 * The base program directory for preference storage.
	 *
	 * @return The base program directory for preference storage.
	 * @since 2024/01/28
	 */
	public static Path baseDirectory()
	{
		return PreferencesManager.baseSystemDirectory()
			.resolve("squirreljme");
	}
	
	/**
	 * The base system directory for preference storage.
	 *
	 * @return The base system directory for preference storage.
	 * @since 2024/01/28
	 */
	public static Path baseSystemDirectory()
	{
		// Need to know which OS we are on
		String osName = System.getProperty("os.name")
			.toLowerCase(Locale.ROOT);
		String osVersion = System.getProperty("os.version")
			.toLowerCase(Locale.ROOT);
		
		// Windows
		if (osName.contains("windows") || osName.contains("reactos"))
		{
			// Where do we shove our data?
			String appDataEnv;
			if (osName.contains("95") || osName.contains("98") ||
				osName.contains("me"))
				appDataEnv = System.getenv("APPDATA");
			else
				appDataEnv = System.getenv("LOCALAPPDATA");
			
			// Resolve it
			if (appDataEnv != null)
				return Paths.get(appDataEnv);
			
			// Fallback here for Windows
			String programData = System.getenv("PROGRAMDATA");
			if (programData != null)
				return Paths.get(programData);
		}
		
		// Everything else
		else
		{
			// XDG Configuration
			String xdg = System.getenv("XDG_CONFIG_HOME");
			if (xdg != null)
				return Paths.get(xdg);
			
			// Home directory
			String homeDir = System.getenv("HOME");
			if (homeDir != null)
				return Paths.get(homeDir).resolve(".config");
		}
		
		// Unknown, assume home, then fallback to pwd otherwise
		String homeDir = System.getProperty("user.home");
		if (homeDir != null)
			return Paths.get(homeDir).resolve(".config");
		return Paths.get("user.dir");
	}
}
