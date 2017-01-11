#compression_chunk_setter #
Basic tool for setting compression chunk size for a Cassandra table via JMX

If you alter the compression settings for a table in Cassandra, every node will convert its portion of the table to the new settings RIGHT NOW, generally resulting in poor performance.  With this tool, you can update the chunk size on as many or as few nodes as you desire in keeping with your performance considerations.  When all the nodes have been updated, you can issue a final ALTER TABLE which will make your change permanent.

**THIS IS A TEMPORARY CHANGE!**  This change is not saved as part of the schema definition, and thus will not persist past reboots.

#Build #

```gradle shadowJar```

#Run #

This will connect to localhost:7199 to issue the relevant change.

```java -jar build/libs/compression_chunk_setter-all.jar 128 mytable mykeyspace```
