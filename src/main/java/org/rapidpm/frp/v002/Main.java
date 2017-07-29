package org.rapidpm.frp.v002;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.rapidpm.frp.model.Pair;
import org.rapidpm.frp.model.Result;

/**
 *
 */
public class Main {

  public static interface Service {
    Result<String> doWork(String input);
  }

  public static void main(String[] args) {

//    ((Service) input -> (Objects.nonNull(input))
//                        ? Result.success(input.toUpperCase())
//                        : Result.failure("Value was null"))
//        .doWork("Hello World")
//        .thenCombine(System::nanoTime ,
//                     (BiFunction<String, Supplier<Long>, Result<Pair<String, Long>>>) (s , longSupplier) -> {
//                       return Result.success(new Pair<>(s , longSupplier.get()));
//                     })
//        .ifPresentOrElse(
//            value -> System.out.println(" value present = " + value) ,
//            errormessage -> System.out.println(" value not present error message is = " + errormessage)
//        );


    final Service service = input -> (Objects.nonNull(input))
                                     ? Result.success(input.toUpperCase())
                                     : Result.failure("Value was null");

    final Result<String> helloWorld = service.doWork("Hello World");

    helloWorld
        .thenCombine(System::nanoTime ,
                     (BiFunction<String, Supplier<Long>, Result<Pair<String, Long>>>)
                         (s , longSupplier) -> Result.success(new Pair<>(s , longSupplier.get())))
        .ifPresentOrElse(
            value -> System.out.println(" value present = " + value) ,
            errormessage -> System.out.println(" value not present error message is = " + errormessage)
        );


    helloWorld
        .map(s -> new Pair<>(s , System.nanoTime()))
        .ifPresentOrElse(
            value -> System.out.println(" value present = " + value) ,
            errormessage -> System.out.println(" value not present error message is = " + errormessage)
        );


    final Consumer<Result<Pair<String, Long>>> resultConsumer = (result) ->
        result
            .ifPresentOrElse(
                value -> System.out.println(" value present = " + value) ,
                errormessage -> System.out.println(" value not present error message is = " + errormessage));


    resultConsumer.accept(helloWorld.map(s -> new Pair<>(s , System.nanoTime())));

  }


}
