package net.byteflux.libby;

import net.byteflux.libby.classloader.URLClassLoaderHelper;
import net.byteflux.libby.logging.adapters.JDKLogAdapter;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A runtime dependency manager for Bukkit plugins.
 */
public class BukkitLibraryManager extends LibraryManager {
    /**
     * Plugin classpath helper
     */
    private final URLClassLoaderHelper classLoader;

    /**
     * Creates a new Bukkit library manager.
     *
     * @param plugin the plugin to manage
     */
    public BukkitLibraryManager(Plugin plugin) {
        this(plugin, "libraries");
    }

    /**
     * Creates a new Bukkit library manager.
     *
     * @param plugin the plugin to manage
     * @param directoryName download directory name
     */
    public BukkitLibraryManager(Plugin plugin, String directoryName) {
        super(new JDKLogAdapter(requireNonNull(plugin, "plugin").getLogger()),getLibrariesFolder(plugin), directoryName);
        classLoader = new URLClassLoaderHelper((URLClassLoader) plugin.getClass().getClassLoader(), this);
    }

    public static Path getLibrariesFolder(Plugin plugin) {
        String[] sections = plugin.getDataFolder().getAbsolutePath().split("\\\\");
        String path = Arrays.stream(sections).limit(sections.length - 2).collect(Collectors.joining(File.separator));
        return Paths.get(path);
    }

    /**
     * Adds a file to the Bukkit plugin's classpath.
     *
     * @param file the file to add
     */
    @Override
    protected void addToClasspath(Path file) {
        classLoader.addToClasspath(file);
    }
}
