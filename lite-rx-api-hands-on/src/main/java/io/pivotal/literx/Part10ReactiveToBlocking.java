package io.pivotal.literx;

import io.pivotal.literx.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * Learn how to turn Reactive API to blocking one.
 *
 * @author Haibo Yan(Tristan)
 */
public class Part10ReactiveToBlocking {

	// Return the user contained in that Mono
	User monoToValue(Mono<User> mono) {
		return mono.block();
	}

	// Return the users contained in that Flux
	Iterable<User> fluxToValues(Flux<User> flux) {
		return flux.toStream().collect(Collectors.toList());
	}

}
