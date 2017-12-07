package it.italiancoders.jwt.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    /*
        Esempio di endpoint per cui e'possibile accedere  anche senza token
     */
    @RequestMapping(value = "public/hello", method = RequestMethod.GET)
    public @ResponseBody String publicHelloWorld(){
        return "Ciao Mondo-Pubblico";
    }

    /*
        Esempio di endpoint per cui e'possibile accedere  solo con token valido
     */
    @RequestMapping(value = "protected/hello", method = RequestMethod.GET)
    public @ResponseBody String protectedHelloWorld(){
        return "Ciao Mondo-Protetto";
    }
}
