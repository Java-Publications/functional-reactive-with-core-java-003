package org.rapidpm.frp.v001;

import java.util.concurrent.CompletableFuture;

import org.rapidpm.frp.model.Result;

/**
 *
 */
public class Main {

  public static void main(String[] args) {

    final Result<String> stringResult = Result.success("value");

    stringResult
        .ifPresentOrElse(
            value -> System.out.println(" value present = " + value) ,
            errormessage -> System.out.println(" value not present error message is = " + errormessage)
        );

//    final Result<String> thenCombine = stringResult
//        .thenCombine(100 , new BiFunction<String, Integer, Result<String>>() {
//          @Override
//          public Result<String> apply(String s , Integer integer) {
//            return Result.ofNullable(s + integer);
//          }
//        });

    stringResult
        .thenCombine(100 , (s , integer) -> Result.ofNullable(s + integer))
        .ifPresent(System.out::println);

    final CompletableFuture<Result<String>> async = stringResult
        .thenCombineAsync(100 , (s , integer) -> Result.ofNullable(s + integer));

//    async.thenAccept(new Consumer<Result<String>>() {
//      @Override
//      public void accept(Result<String> stringResult) {
//        stringResult.ifPresent(System.out::println);
//      }
//    });
    async.thenAccept((Result<String> result) -> result.ifPresent(System.out::println));
  }

}
