package com.horacio.mutant;

import com.horacio.mutant.service.DetectionResult;
import com.horacio.mutant.service.DnaService;
import com.horacio.mutant.web.MutantRequest;
import com.horacio.mutant.web.MutantResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionRegistration;
import org.springframework.cloud.function.context.FunctionType;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

//import org.springframework.cloud.function.adapter.aws.FunctionInvoker
@SpringBootApplication
public class MutantDetectorApplication {
//public class MutantDetectorApplication implements ApplicationContextInitializer<GenericApplicationContext> {

    @Autowired
    private DnaService dnaService;

    //@Bean
    /*public Supplier<List<Order>> orders() {
        return () -> orderDao.buildOrders();
    }*/

    // POST
    /*@Bean
    public Function<String, String> reverse() {
        return (input) -> new StringBuilder(input).reverse().toString();
    }*/

    // GET
    /*@Bean
    public Supplier<Book> getBook() {
        return () -> new Book(101, "Core Java");
    }*/

    // POST
    /*@Bean
    public Consumer<String> printMessage() {
        return (input) -> System.out.println(input);
    }*/

    @Bean
    public Function<MutantRequest,String> mutant(){
        return request -> {
                DetectionResult result = dnaService.detectMutantAndSave(request.getDna());
                if (!result.isMutant()){
                    throw new RuntimeException("Human detected");
                }
                return "Mutante";
            };
    }

    /*@Bean
    public Supplier<Stats> stats(){
        return () -> new Stats();
    }*/

    @Data
    public static class Stats{
        int count_mutant_dna = 40;
        int count_human_dna = 100;
        double ration = 0.4;
    }

    public static void main(String[] args) {
        SpringApplication.run(MutantDetectorApplication.class, args);
    }

    /*
    @Override
    public void initialize(GenericApplicationContext context) {
        context.registerBean("stats", FunctionRegistration.class,
                () -> new FunctionRegistration<Supplier<Stats>>(stats())
                        .type(FunctionType.supplier(Stats.class).getType()));

        context.registerBean("mutants", FunctionRegistration.class,
                () -> new FunctionRegistration<Consumer<MutantRequest>>(mutant())
                        .type(FunctionType.consumer(MutantRequest.class).getType()));
    }*/
}
