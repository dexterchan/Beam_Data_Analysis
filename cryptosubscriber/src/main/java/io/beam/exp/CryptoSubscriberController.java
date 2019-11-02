package io.beam.exp;


import io.beam.exp.service.CryptoSubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crypto")
public class CryptoSubscriberController {

    private final CryptoSubscriberService svc;

    @RequestMapping("/")
    public String helloworld() {
        return "Greetings from Crypto Subscriber!";
    }

    @GetMapping("/startservice")
    public String startService(@RequestParam String baseCcy, @RequestParam String counterCcy){
        svc.startSubscription("",baseCcy,counterCcy);
        return "subscription started";
    }

    @GetMapping("/stopservice")
    public String stopService(@RequestParam String baseCcy, @RequestParam String counterCcy){
        svc.stopSubscription("",baseCcy,counterCcy);
        return "subscription stopped";
    }
}
