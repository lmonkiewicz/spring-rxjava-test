package com.lmonkiewicz.rxjava;

import io.reactivex.Flowable;
import io.reactivex.Single;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@RestController
@RequestMapping("/some")
public class SomeController {

    @GetMapping("/string/{param}")
    public String string(@PathVariable String param) {
        return "Hello "+param;
    }


    @GetMapping("/single/{param}")
    public Single<String> single(@PathVariable String param) {
        return Single.just(param)
                .map(p -> "Hello "+p);
    }

    @GetMapping("/combine/{param}")
    public Single<String> combine(@PathVariable String param) {
//        final Publisher<String>[] publishers = asList(
//                Single.just("H").delay(100, MILLISECONDS).toFlowable(),
//                Single.just("e").delay(200, MILLISECONDS).toFlowable(),
//                Single.just("l").delay(500, MILLISECONDS).toFlowable(),
//                Single.just("l").delay(50, MILLISECONDS).toFlowable(),
//                Single.just("o").delay(1000, MILLISECONDS).toFlowable()
//        ).toArray(new Publisher[0]);

        final Iterable<Publisher<String>> list = asList(
                Single.just("H").delay(100, MILLISECONDS).toFlowable(),
                Single.just("e").delay(200, MILLISECONDS).toFlowable(),
                Single.just("l").delay(500, MILLISECONDS).toFlowable(),
                Single.just("l").delay(50, MILLISECONDS).toFlowable(),
                Single.just("o").delay(1000, MILLISECONDS).toFlowable()
        );
        final Function<Object[], String> combiner = (Object[] objects) -> Arrays.stream(objects)
                .map(Object::toString)
                .collect(Collectors.joining());

        return Flowable.combineLatest(list, combiner)
                .map(x -> x+" "+param)
                .single("NOPE");
    }
}
