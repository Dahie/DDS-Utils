package org.dds_utils.dropps.models;

import java.awt.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.swing.*;
import java.util.logging.Logger;


// https://github.com/dteoh/treasuremap/blob/master/src/main/java/org/jdesktop/application/ResourceMap.java

public class ResourceMap {
	private static Logger logger = Logger.getLogger(ResourceMap.class.getName());
    private Properties properties = new Properties();

    public String getString(String key) {
        return properties.getProperty(key);
    }

    public ImageIcon getIcon(String key) {
        final String iconUrl = this.getClass().getResource(getString(key)).toExternalForm();
        final ImageIcon icon = new ImageIcon(iconUrl);
        return icon;
    }

    public Integer getInteger(String key) {
        return Integer.valueOf(getString(key));
    }

    public ResourceMap(String fileName) {
        final ClassLoader classLoader = getClass().getClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream(fileName);

        final InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        try (BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int separatorIndex = line.indexOf('=');
                if (separatorIndex != -1) {
                    String key = line.substring(0, separatorIndex).trim();
                    String value = line.substring(separatorIndex + 1).trim();
                    properties.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//    public void injectComponents(final Component root) {
//        injectComponent(root);
//        if (root instanceof JMenu) {
//            /* Warning: we're bypassing the popupMenu here because
//             * JMenu#getPopupMenu creates it; doesn't seem right
//             * to do so at injection time.  Unfortunately, this
//             * means that attempts to inject the popup menu's
//             * "label" property will fail.
//             */
//            JMenu menu = (JMenu) root;
//            for (Component child : menu.getMenuComponents()) {
//                injectComponents(child);
//            }
//        } else if (root instanceof Container) {
//            Container container = (Container) root;
//            for (Component child : container.getComponents()) {
//                injectComponents(child);
//            }
//        }
//    }
//
//    public void injectComponent(final Component target) {
//        if (target == null) {
//            throw new IllegalArgumentException("null target");
//        }
//        injectComponentProperties(target);
//    }
//
//
//    private void injectComponentProperties(final Component component) {
//        String componentName = component.getName();
//        if (componentName != null) {
//            /* Optimization: punt early if componentName doesn't
//             * appear in any componentName.propertyName resource keys
//             */
//            boolean matchingResourceFound = false;
//            for (String key : keySet()) {
//                int i = key.lastIndexOf(".");
//                if ((i != -1) && componentName.equals(key.substring(0, i))) {
//                    matchingResourceFound = true;
//                    break;
//                }
//            }
//            if (!matchingResourceFound) {
//                return;
//            }
//            BeanInfo beanInfo = null;
//            try {
//                beanInfo = Introspector.getBeanInfo(component.getClass());
//            } catch (IntrospectionException e) {
//                String msg = "introspection failed";
//                RuntimeException re = new org.jdesktop.application.ResourceMap.PropertyInjectionException(msg, null, component, null);
//                re.initCause(e);
//                throw re;
//            }
//            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
//            if ((pds != null) && (pds.length > 0)) {
//                for (String key : keySet()) {
//                    int i = key.lastIndexOf(".");
//                    String keyComponentName = (i == -1) ? null : key.substring(0, i);
//                    if (componentName.equals(keyComponentName)) {
//                        if ((i + 1) == key.length()) {
//                            /* key has no property name suffix, e.g. "myComponentName."
//                             * This is probably a mistake.
//                             */
//                            String msg = "component resource lacks property name suffix";
//                            logger.warning(msg);
//                            break;
//                        }
//                        String propertyName = key.substring(i + 1);
//                        boolean matchingPropertyFound = false;
//                        for (PropertyDescriptor pd : pds) {
//                            if (pd.getName().equals(propertyName)) {
//                                injectComponentProperty(component, pd, key);
//                                matchingPropertyFound = true;
//                                break;
//                            }
//                        }
//                        if (!matchingPropertyFound) {
//                            String msg = String.format(
//                                    "[resource %s] component named %s doesn't have a property named %s",
//                                    key, componentName, propertyName);
//                            logger.warning(msg);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//	private void injectComponentProperty(final Component component, final PropertyDescriptor pd, final String key) {
//		Method setter = pd.getWriteMethod();
//		Class type = pd.getPropertyType();
//		if ((setter != null) && (type != null) && containsKey(key)) {
//			Object value = getObject(key, type);
//			String propertyName = pd.getName();
//			try {
//				// Note: this could be generalized, we could delegate
//				// to a component property injector.
//				if ("text".equals(propertyName) && (component instanceof AbstractButton)) {
//					MnemonicText.configure(component, (String)value);
//				}
//				else if ("text".equals(propertyName) && (component instanceof JLabel)) {
//					MnemonicText.configure(component, (String)value);
//				}
//				else {
//					setter.invoke(component, value);
//				}
//			}
//			catch (Exception e) {
//				String pdn = pd.getName();
//				String msg = "property setter failed";
//				RuntimeException re = new org.jdesktop.application.ResourceMap.PropertyInjectionException(msg, key, component, pdn);
//				re.initCause(e);
//				throw re;
//			}
//		}
//		else if (type != null) {
//			String pdn = pd.getName();
//			String msg = "no value specified for resource";
//			throw new PropertyInjectionException(msg, key, component, pdn);
//		}
//		else if (setter == null) {
//			String pdn = pd.getName();
//			String msg = "can't set read-only property";
//			throw new PropertyInjectionException(msg, key, component, pdn);
//		}
//	}

}

