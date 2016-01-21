package org.g6.bigdata.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkUtil {
    /**
     * Get Spark configuration
     *
     * @param appName
     * @param master  Master URL	        Meaning
     *                local 	            Run Spark locally with one worker thread (i.e. no parallelism at all).
     *                local[K] 	        Run Spark locally with K worker threads (ideally, set this to the number of cores on your machine).
     *                local[*] 	        Run Spark locally with as many worker threads as logical cores on your machine.
     *                spark://HOST:PORT 	Connect to the given Spark standalone cluster master. The port must be whichever one your master is configured to use,
     *                which is 7077 by default.
     *                mesos://HOST:PORT 	Connect to the given Mesos cluster. The port must be whichever one your is configured to use, which is 5050 by default.
     *                Or, for a Mesos cluster using ZooKeeper, use mesos://zk://.... To submit with --deploy-mode cluster,
     *                the HOST:PORT should be configured to connect to the MesosClusterDispatcher.
     *                yarn 	            Connect to a YARN cluster in client or cluster mode depending on the value of --deploy-mode.
     *                The cluster location will be found based on the HADOOP_CONF_DIR or YARN_CONF_DIR variable.
     *                yarn-client 	    Equivalent to yarn with --deploy-mode client, which is preferred to `yarn-client`
     *                yarn-cluster 	    Equivalent to yarn with --deploy-mode cluster, which is preferred to `yarn-cluster`
     * @return
     */
    public static JavaSparkContext getSparkContext(String appName, String master) {
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
        return new JavaSparkContext(conf);
    }

    public static JavaSparkContext getSparkContext(String master) {
        SparkConf conf = new SparkConf().setAppName("").setMaster(master);
        return new JavaSparkContext(conf);
    }
}
