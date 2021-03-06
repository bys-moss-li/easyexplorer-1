package org.sf.easyexplore;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class EasyExplorePlugin extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "org.sf.easyexplore"; //$NON-NLS-1$
	private static EasyExplorePlugin plugin;
	private ResourceBundle resourceBundle;

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public EasyExplorePlugin() {
		super();
		plugin = this;
		try {
			this.resourceBundle = ResourceBundle
					.getBundle("org.sf.easyexplore.EasyExplorePluginResources");
		} catch (MissingResourceException localMissingResourceException) {
		}
	}

	public static EasyExplorePlugin getDefault() {
		return plugin;
	}

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public static String getResourceString(String key) {
		ResourceBundle bundle = getDefault().getResourceBundle();
		String res = null;
		try {
			res = bundle.getString(key);
		} catch (MissingResourceException localMissingResourceException) {
			res = key;
		}
		return res;
	}

	public ResourceBundle getResourceBundle() {
		return this.resourceBundle;
	}

	public static void log(Object msg) {
		ILog log = getDefault().getLog();

		Status status = new Status(1, PLUGIN_ID, 4, msg + "\n", null);
		log.log(status);
	}

	public static void log(Throwable ex) {
		ILog log = getDefault().getLog();
		StringWriter stringWriter = new StringWriter();
		ex.printStackTrace(new PrintWriter(stringWriter));
		String msg = stringWriter.getBuffer().toString();
		Status status = new Status(4, PLUGIN_ID, 4, msg, null);
		log.log(status);
	}

	protected void initializeDefaultPreferences(IPreferenceStore store) {
		String defaultTarget = "shell_open_command {0}";
		String osName = System.getProperty("os.name");
		if (osName.indexOf("Windows") != -1) {
			defaultTarget = "explorer.exe {0}";
		} else if (osName.indexOf("Mac") != -1) {
			defaultTarget = "open {0}";
		}
		store.setDefault("org.sf.easyexplore.targetPreference", defaultTarget);
	}

	public String getTarget() {
		return getPreferenceStore().getString(
				"org.sf.easyexplore.targetPreference");
	}

	public boolean isSupported() {
		String osName = System.getProperty("os.name");

		return (osName.indexOf("Windows") != -1)
				|| (osName.indexOf("Mac") != -1);
	}
}
