package io.pivotal.literx;

import io.pivotal.literx.domain.User;
import io.pivotal.literx.repository.ReactiveRepository;
import io.pivotal.literx.repository.ReactiveUserRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Learn how to control the demand.
 *
 * @author Sebastien Deleuze
 */
public class Part06Request {

	ReactiveRepository<User> repository = new ReactiveUserRepository();

	// Create a StepVerifier that initially requests all values and expect 4 values to be received
	StepVerifier requestAllExpectFour(Flux<User> flux) {
		return StepVerifier.create(flux)
			.expectNextCount(4)
			.expectComplete();
	}

	// Create a StepVerifier that initially requests 1 value and expects User.SKYLER then requests another value and expects User.JESSE.
	StepVerifier requestOneExpectSkylerThenRequestOneExpectJesse(Flux<User> flux) {
		return StepVerifier.create(flux)
			.thenRequest(1)
			.expectNext(User.SKYLER)
			.thenRequest(1)
			.expectNext(User.JESSE)
			.thenRequest(2)
			.expectNextMatches(u -> true)
			.expectNextMatches(u -> true)
			.expectComplete();
	}

	// Return a Flux with all users stored in the repository that prints automatically logs for all Reactive Streams signals
	Flux<User> fluxWithLog() {
		Flux<User> users = repository.findAll();
		return users.log();
	}

	// Return a Flux with all users stored in the repository that prints "Starring:" on subscribe, "firstname lastname" for all values and "The end!" on complete
	Flux<User> fluxWithDoOnPrintln() {
		Flux<User> users = repository.findAll();
		return users.doOnSubscribe(s -> System.out.println("Starring:"))
			.doOnNext(user -> System.out.println(user.getFirstname() + " " + user.getLastname()))
			.doOnComplete(() -> System.out.println("The end!"));
	}

}
