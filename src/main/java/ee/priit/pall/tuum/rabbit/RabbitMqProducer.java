package ee.priit.pall.tuum.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqProducer {
    private final RabbitTemplate template;
    private final Binding binding;

    public RabbitMqProducer(RabbitTemplate template, Binding binding) {
        this.template = template;
        this.binding = binding;
    }

    public void produce(Object message) {
        System.out.println("Producing message...");
        template.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);
        System.out.println("Message sending finished");
    }
}
