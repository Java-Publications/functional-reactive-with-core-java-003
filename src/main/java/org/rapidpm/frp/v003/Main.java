package org.rapidpm.frp.v003;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import org.rapidpm.frp.model.Result;

/**
 *
 */
public class Main {

  public static interface Service {
    Result<String> doWork(String input);
  }

  //Demo for some Services to call
  public static Supplier<Step001> serviceA() { return () -> new Step001(now());}

  public static Function<Step001, Step002> serviceB() { return (step001) -> new Step002(step001.timestamp01 , now());}

  public static Function<Step002, Step003> serviceC() { return (step002) -> new Step003(step002.timestamp01 , step002.timestamp02 , now());}

  //Demo for some classes
  public static class Step001 {
    private final LocalDateTime timestamp01;

    public Step001(LocalDateTime timestamp01) {this.timestamp01 = timestamp01;}
  }

  public static class Step002 {
    private final LocalDateTime timestamp01;
    private final LocalDateTime timestamp02;

    public Step002(LocalDateTime timestamp01 , LocalDateTime timestamp02) {
      this.timestamp01 = timestamp01;
      this.timestamp02 = timestamp02;
    }
  }

  public static class Step003 {
    private final LocalDateTime timestamp01;
    private final LocalDateTime timestamp02;
    private final LocalDateTime timestamp03;

    public Step003(LocalDateTime timestamp01 , LocalDateTime timestamp02 , LocalDateTime timestamp03) {
      this.timestamp01 = timestamp01;
      this.timestamp02 = timestamp02;
      this.timestamp03 = timestamp03;
    }

    @Override
    public String toString() {
      return "Step003{" +
             "timestamp01=" + timestamp01 +
             ", timestamp02=" + timestamp02 +
             ", timestamp03=" + timestamp03 +
             '}';
    }
  }


  public static Function<Result<String>, Result<Step003>> workflow = (input) ->
      input
          .or(() -> Result.success("nooop")) // default per demo definition here -> convert failure´s
          .thenCombine(
              serviceA() ,
              (value , supplier) -> Result.success(supplier.get()) // not working with value, to make it simple
          )
          .thenCombine(
              serviceB() ,
              (step001 , fkt) -> Result.success(fkt.apply(step001))
          )
          .thenCombine(
              serviceC() ,
              (step002 , fkt) -> Result.success(fkt.apply(step002))
          );


  public static void main(String[] args) {


    // some Service....
    final Service service = input -> (Objects.nonNull(input))
                                     ? Result.success(input.toUpperCase())
                                     : Result.failure("Value was null");




    final Function<Result<String>, Result<Step003>> modifiedWorkflow = workflow
        .andThen(result -> {
          result.ifPresentOrElse(
              value -> System.out.println(" value present = " + value) ,
              errormessage -> System.out.println(" value not present error message is = " + errormessage)
          );
          return result;
        });

    // service will be invoked
    modifiedWorkflow.apply(service.doWork("Hello World"));

  }

}
