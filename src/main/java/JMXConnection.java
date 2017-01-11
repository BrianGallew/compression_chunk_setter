package org.gallew;

/**
 * Created by begallew on 4/20/16.
 */

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.Attribute;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMXConnection {
    private static final Logger logger = LoggerFactory.getLogger(JMXConnection.class);
    int retries = 2;
    int trial = 0;
    JMXServiceURL url;
    MBeanServerConnection mbsc;

    JMXConnection() {
        try {
            url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:7199/jmxrmi");
        } catch (java.net.MalformedURLException the_exception) {
            logger.error("Improperly formed URL: {} ", the_exception);
            System.exit(1);
        }
        connect();
    }

    private void connect() {
        //Make a JMX connection to a host
        try {
            JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
            mbsc = jmxc.getMBeanServerConnection();
        } catch (java.io.IOException the_exception) {
            logger.error("Unable to connect to Cassandra: {} ", the_exception);
            System.exit(2);
        }
    }

    @SuppressWarnings("unchecked")
    public Map getMap(String key, String attribute) {
        // Extract/return a Map attribute.
        try {
            HashMap retval = (HashMap) mbsc.getAttribute(new ObjectName(key), attribute);
            trial = 0;
            return retval;
        } catch (Exception the_exception) {
            trial = trial + 1;
            if (trial < retries) {
                connect();
                return getMap(key, attribute);
            }
            logger.error("Error reading bean {}: {}", key, the_exception);
        }
        return new HashMap();
    }

    @SuppressWarnings("unchecked")
    public void setMap(String key, String attribute, Map value) {
        // Extract/return a List attribute.

        try {
	    mbsc.setAttribute(new ObjectName(key), new Attribute(attribute, value));
        } catch (Exception the_exception) {
	    System.out.println(the_exception);
            logger.error("Error writing bean {}: {}", key, the_exception);
	    System.exit(3);
        }
    }

}

