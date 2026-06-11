package com.ontoweb.newworkflow.handler.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
@IntegrationComponentScan
@ConditionalOnProperty(value = "spring.mqtt.enable", havingValue = "true")
public class MqttConfig {

    private MqttPahoMessageDrivenChannelAdapter adapter;
    private MqttPahoMessageDrivenChannelAdapter adapter3;
    private ConcurrentHashMap<String,Integer> defaultConsumerTopic = new ConcurrentHashMap<>();
    private List<String> consumerThreeTopic = new ArrayList<>();

    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String hostUrl;

    //生产者和消费者是单独连接服务器会使用到一个clientid（客户端id），如果是同一个clientid的话会出现Lost connection: 已断开连接; retrying...
    @Value("${spring.mqtt.clientId}")
    private String consumerClientId;

    @Value("${spring.mqtt.clientId1}")
    private String producerClientId;

    @Value("${spring.mqtt.subTopic}")
    private String consumerTopic;

    @Value("${spring.mqtt.timeout}")
    private int timeout;   //连接超时

    @Value("${spring.mqtt.keepalive}")
    private int keepalive;  //连接超时

    @Value("${spring.mqtt.producer.defaultQos}")
    private int defaultProducerQos;
    @Value("${spring.mqtt.producer.defaultRetained}")
    private boolean defaultRetained;

    //入站通道名（消费者）订阅的bean名称
    public static final String CHANNEL_NAME_IN = "mqttInboundChannel";
    //出站通道名（生产者）发布的bean名称
    public static final String CHANNEL_NAME_OUT = "mqttOutboundChannel";

    /**
     * MQTT连接器选项
     *
     * @return {@link MqttConnectOptions}
     */
    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        mqttConnectOptions.setKeepAliveInterval(keepalive);
        mqttConnectOptions.setMaxInflight(100);
        return mqttConnectOptions;
    }

    /**
     * MQTT客户端
     *
     * @return {@link org.springframework.integration.mqtt.core.MqttPahoClientFactory}
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }


    /*******************************生产者*******************************************/
    /**
     * MQTT信息通道（生产者）
     *
     * @return {@link MessageChannel}
     */
    @Bean(name = CHANNEL_NAME_OUT)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器（生产者）
     * mqtt消息出站通道默认配置，用于向外发出mqtt消息
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(producerClientId+"_producer", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(consumerTopic);
        messageHandler.setDefaultRetained(defaultRetained);
        messageHandler.setDefaultQos(defaultProducerQos);
        return messageHandler;
    }
    /********************************************************************************/


    /*******************************消费者********************************************/
    /**
     * MQTT信息通道（消费者）
     *
     * @return {@link MessageChannel}
     */
    @Bean(name = CHANNEL_NAME_IN)
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息订阅绑定（消费者）
     *
     * @return {@link org.springframework.integration.core.MessageProducer}
     */
    @Bean
    public MessageProducer inbound() {
        // 可以同时消费（订阅）多个Topic
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(consumerClientId, mqttClientFactory(), consumerTopic);
        adapter.setCompletionTimeout(timeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(0);
        // 设置订阅通道
        adapter.setOutputChannel(mqttInboundChannel());
        //恢复间隔–控制在故障之后适配器会尝试重新连接的时间间隔，默认为10000ms（10s）
        adapter.setRecoveryInterval(10000);
        return adapter;
    }

    /**
     * MQTT消息处理器（消费者）
     *
     * @return {@link MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_IN)
    public MessageHandler handler() {
        return null;
//        return new MqttConsumer();
    }
    /********************************************************************************/


//    /*******************************消费者1*******************************************/
//    /**
//     * MQTT信息通道（消费者1）
//     *
//     * @return {@link MessageChannel}
//     */
//    @Bean(name = "mqttInputChannelOne")
//    public MessageChannel mqttInboundChannelOne() {
//        return new DirectChannel();
//    }
//
//    /**
//     * MQTT消息订阅绑定（消费者1）
//     *
//     * @return {@link org.springframework.integration.core.MessageProducer}
//     */
//    @Bean
//    public MessageProducer inboundOne() {
//        // 可以同时消费（订阅）多个Topic
//        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(consumerClientId+Math.random(), mqttClientFactory(), "/ele/11");
//        adapter.setCompletionTimeout(timeout);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(0);
//        // 设置订阅通道
//        adapter.setOutputChannel(mqttInboundChannelOne());
//        //恢复间隔–控制在故障之后适配器会尝试重新连接的时间间隔，默认为10000ms（10s）
//        adapter.setRecoveryInterval(10000);
//        return adapter;
//    }
//
//    /**
//     * MQTT消息处理器（消费者）
//     *
//     * @return {@link MessageHandler}
//     */
//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannelOne")
//    public MessageHandler handlerXX() {return new MqttConsumerOne();}
//    /********************************************************************************/
//
//
//    /*******************************消费者2*******************************************/
//    /**
//     * MQTT信息通道2（消费者）
//     *
//     * @return {@link MessageChannel}
//     */
//    @Bean(name = "mqttInputChannelTwo")
//    public MessageChannel mqttInputChannelTwo() {
//        return new DirectChannel();
//    }
//    //配置client2，监听的topic:
//    @Bean
//    public MessageProducer inbound1() {
//        adapter = new MqttPahoMessageDrivenChannelAdapter(consumerClientId+Math.random(), mqttClientFactory(), "1");
//        adapter.setCompletionTimeout(timeout);
//        // 设置转换器，接收bytes
//        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
//        converter.setPayloadAsBytes(true);
//        adapter.setConverter(converter);
//        adapter.setQos(0);
//        adapter.setOutputChannel(mqttInputChannelTwo());
//        return adapter;
//    }
//
//    //通过通道2获取数据
//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannelTwo")
//    public MessageHandler handlerTwo() {
//        return new MqttConsumerTwo();
//    }
//    /********************************************************************************/
//
//
//    /*******************************消费者3*******************************************/
//    /**
//     * MQTT信息通道（消费者3）
//     *
//     * @return {@link MessageChannel}
//     */
//    @Bean(name = "mqttInputChannelThree")
//    public MessageChannel mqttInboundChannelThree() {
//        return new DirectChannel();
//    }
//
//    /**
//     * MQTT消息订阅绑定（消费者3）
//     *
//     * @return {@link org.springframework.integration.core.MessageProducer}
//     */
//    @Bean
//    public MessageProducer inbound2() {
//        // 可以同时消费（订阅）多个Topic
//        adapter3 = new MqttPahoMessageDrivenChannelAdapter(consumerClientId+ UUID.randomUUID().toString().substring(0,6), mqttClientFactory(), "1");
//        adapter3.setCompletionTimeout(timeout);
//        // 设置转换器，接收bytes
//        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
//        converter.setPayloadAsBytes(true);
//        adapter3.setConverter(converter);
//        adapter3.setQos(0);
//        // 设置订阅通道
//        adapter3.setOutputChannel(mqttInboundChannelThree());
//        //恢复间隔–控制在故障之后适配器会尝试重新连接的时间间隔，默认为10000ms（10s）
//        adapter3.setRecoveryInterval(10000);
//        return adapter3;
//    }
//
//    /**
//     * MQTT消息处理器（消费者3）
//     *
//     * @return {@link MessageHandler}
//     */
//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannelThree")
//    public MessageHandler handlerThree() {return new MqttConsumerThree();}
//
//    /**
//     * 删除主题方法
//     */
//    public void removeTopic3(String consumerTopic){
//        if(adapter3==null){
//            adapter3 = new MqttPahoMessageDrivenChannelAdapter(consumerClientId+Math.random(), mqttClientFactory(), "1");
//        }
//        if(!consumerThreeTopic.contains(consumerTopic)){
//            return;
//        }
//        adapter3.removeTopic(consumerTopic);
//
//        consumerThreeTopic.remove(consumerTopic);
//
//
//    }
//
//    /**
//     * 添加主题方法
//     */
//    public void addListenTopic3(String consumerTopic){
//        log.info("消费者3的主题为：{}",consumerTopic);
//        if(adapter3==null){
//            adapter3 = new MqttPahoMessageDrivenChannelAdapter(consumerClientId+Math.random(), mqttClientFactory(), "1");
//        }
//        if(consumerThreeTopic.contains(consumerTopic)){
//            return;
//        }
//        adapter3.addTopic(consumerTopic,1);
//        consumerThreeTopic.add(consumerTopic);
//    }
//    /********************************************************************************/
//
//
//    /*******************************消费者AI*******************************************/
//    /**
//     * MQTT信息通道（消费者AI）
//     *
//     * @return {@link MessageChannel}
//     */
//    @Bean(name = "mqttInputChannelAi")
//    public MessageChannel mqttInboundChannelAi() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public MessageProducer inboundAi() {
//        // 可以同时消费（订阅）多个Topic
//        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(consumerClientId+Math.random(), mqttClientFactory(), "/AI");
//        adapter.setCompletionTimeout(timeout);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(0);
//        // 设置订阅通道
//        adapter.setOutputChannel(mqttInboundChannelAi());
//        //恢复间隔–控制在故障之后适配器会尝试重新连接的时间间隔，默认为10000ms（10s）
//        adapter.setRecoveryInterval(10000);
//        return adapter;
//    }
//
//    /**
//     * MQTT消息处理器（消费者）
//     *
//     * @return {@link MessageHandler}
//     */
//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannelAi")
//    public MessageHandler handlerAi() {return new MqttConsumerAi();}
//    /********************************************************************************/
//
//
//
//    /**
//     * 提供添加主题方法
//     */
//    public void addListenTopic(String consumerTopic1){
////        //移除最开始创建的空主题
////        adapter.removeTopic();
//        if(adapter==null){
//            adapter = new MqttPahoMessageDrivenChannelAdapter(consumerClientId+Math.random(), mqttClientFactory(), "1");
//        }
//
//        if(this.defaultConsumerTopic.containsKey(consumerTopic1)){
//            Integer num = defaultConsumerTopic.get(consumerTopic1)+1;
//            this.defaultConsumerTopic.put(consumerTopic1,num);
//        }else{
//            this.defaultConsumerTopic.put(consumerTopic1,1);
//        }
//        int size =this.defaultConsumerTopic.keySet().size();
//        for (String s : this.defaultConsumerTopic.keySet().toArray(new String[size])) {
//            adapter.addTopic(s,1);
//        }
//
//        log.info("addListenTopic看一下.....{}",defaultConsumerTopic);
//        log.info("addListenTopic adpter是{}",adapter.getTopic());
//
//    }
//    /**
//     * 删除主题方法
//     */
//    public void removeListenTopic(){
//        if(adapter==null){
//            adapter = new MqttPahoMessageDrivenChannelAdapter(consumerClientId+Math.random(), mqttClientFactory(), "1");
//        }
//        for (String s : this.defaultConsumerTopic.keySet()) {
//            adapter.removeTopic(s);
//
//        }
//        log.info("removeListenTopic删除全部=={}",defaultConsumerTopic);
//
//    }
//    /**
//     * 删除主题方法
//     */
//    public void removeTopic(String consumerTopic1){
//        if(adapter==null){
//            adapter = new MqttPahoMessageDrivenChannelAdapter(consumerClientId+Math.random(), mqttClientFactory(), "1");
//        }
//        if(this.defaultConsumerTopic.containsKey(consumerTopic1)){
//            if(defaultConsumerTopic.get(consumerTopic1)==1){
//                adapter.removeTopic(consumerTopic1);
//                defaultConsumerTopic.remove(consumerTopic1);
//                return;
//            }
//            Integer num = defaultConsumerTopic.get(consumerTopic1)-1;
//            this.defaultConsumerTopic.put(consumerTopic1,num);
//        }
//    }

}


