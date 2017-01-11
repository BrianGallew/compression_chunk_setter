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


public class JMXConnection {
    JMXServiceURL url;
    MBeanServerConnection mbsc;

    JMXConnection() {
        try {
            url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:7199/jmxrmi");
        } catch (java.net.MalformedURLException the_exception) {
            System.out.println("Improperly formed URL:  " + the_exception.toString());
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
            System.out.println("Unable to connect to Cassandra: " + the_exception.toString());
            System.exit(2);
        }
    }

    @SuppressWarnings("unchecked")
    public Map getMap(String key, String attribute) {
        // Extract/return a Map attribute.
        try {
            return (HashMap) mbsc.getAttribute(new ObjectName(key), attribute);
        } catch (Exception the_exception) {
            System.out.println("Error reading bean " + key + " : " + the_exception.toString());
            System.exit(2);
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
            System.out.println("Error writing bean " + key + " : " + the_exception.toString());
	    System.exit(3);
        }
    }

}

