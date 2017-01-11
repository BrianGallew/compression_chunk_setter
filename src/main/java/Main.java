package org.gallew;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    static Integer port = 7199;
    static String keyspace = "mykeyspace";
    static String tablename = "mytable";
    static Integer chunksize = 64;
    public static void main(String[] args) {
        if (args.length == 1) {
            chunksize = Integer.decode(args[0]);
        } else if (args.length == 2) {
            chunksize = Integer.decode(args[0]);
            tablename = args[1];
        } else if (args.length == 3) {
            chunksize = Integer.decode(args[0]);
            tablename = args[1];
	    keyspace = args[2];
        } else {
            logger.error("Usage: cc_setter [chunksize=64] [tablename=fx_sensor_data_test] [keyspace=lightning]");
        }

        JMXConnection conn = new JMXConnection();
        //org.apache.cassandra.db:type=StorageService attribute LiveNodes
	String key = "org.apache.cassandra.db:columnfamily="+tablename+",keyspace="+keyspace+",type=ColumnFamilies";
        HashMap compression_parameters = (HashMap)conn.getMap(key, "CompressionParameters");
        System.out.println(compression_parameters);
	compression_parameters.put("chunk_length_kb", Integer.toString(chunksize));
	conn.setMap(key, "CompressionParameters", compression_parameters);
	compression_parameters = (HashMap)conn.getMap(key, "CompressionParameters");
        System.out.println(compression_parameters);
        logger.error("done");
        
        System.exit(0);
    }

}
