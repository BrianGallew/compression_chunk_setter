package org.gallew;
import java.util.HashMap;

public class Main {
    static Integer port = 7199;
    static String keyspace = "mykeyspace";
    static String tablename = "mytable";
    static Integer chunksize = 64;
    public static void main(String[] args) {
	if (args.length > 3) {
	    System.out.println("Usage: cc_setter [chunksize=64] [tablename=fx_sensor_data_test] [keyspace=lightning]");
	    System.exit(5);
        }
        if (args.length > 0) {
            chunksize = Integer.decode(args[0]);
        }
	if (args.length > 1) {
            tablename = args[1];
        }
	if (args.length > 2) {
	    keyspace = args[2];
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
        System.out.println("done");
        
        System.exit(0);
    }

}
