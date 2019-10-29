package io.beam.exp;


import io.beam.exp.service.CryptoSubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CryptoSubscriberController {

    CryptoSubscriberService svc=null;

    @RequestMapping("/")
    public String helloworld() {
        return "Greetings from Crypto Subscriber!";
    }
}
