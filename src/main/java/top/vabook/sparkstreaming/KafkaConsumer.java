package top.vabook.sparkstreaming;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author vabook@163.com
 * @version 2018/7/19 16:49
 * 流程
 * 1.consumer --> createJavaConsumerConnector
 * 2.topicMap(topic,threadnum)
 * 3.createConsumerMessageStreams
 * 4.KafkaStream<byte[],byte[]>
 * 5.MessageAndMetadata
 * 6.
 */
public class KafkaConsumer {
    private final static String topic = "vabook";
    private final static Integer threads = 2;

    public static void main(String args[]) {
        Properties prop = new Properties();
        prop.put("zookeeper.connect", "Centos-01:2181");
        prop.put("group.id", "gg");
        prop.put("auto.offset.reset", "smallest");

        ConsumerConfig config = new ConsumerConfig(prop);
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(config);

        //map
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();

        //将topic 和线程数传入map中
        topicCountMap.put(topic,threads);
        Map<String, List<KafkaStream<byte[],byte[]>>>  consumerMap = consumer.createMessageStreams(topicCountMap);

        List<KafkaStream<byte[],byte[]>> streams = consumerMap.get(topic);
        for (final KafkaStream<byte[],byte[]> kafkaStream : streams){
            new Thread(new Runnable() {
                public void run() {
                    for (MessageAndMetadata<byte[],byte[]> mm : kafkaStream){
                        String msg = new String(mm.message());
                        System.out.println(msg);
                    }
                }
            }).start();
        }
    }
}
