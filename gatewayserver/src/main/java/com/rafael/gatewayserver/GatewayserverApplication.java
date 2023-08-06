package com.rafael.gatewayserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;

import java.util.Date;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

/*	@Autowired
	private TokenRelayGatewayFilterFactory filterFactory;*/


	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/rafaelbank/accounts/**")
						.filters(f -> f.rewritePath("/rafaelbank/accounts/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time",new Date().toString()))
						.uri("lb://ACCOUNTS")).
				route(p -> p
						.path("/rafaelbank/loans/**")
						.filters(f -> f.rewritePath("/rafaelbank/loans/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time",new Date().toString()))
						.uri("lb://LOANS")).
				route(p -> p
						.path("/rafaelbank/cards/**")
						.filters(f -> f.rewritePath("/rafaelbank/cards/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time",new Date().toString()))
						.uri("lb://CARDS")).build();
	}


/*	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/rafaelbank/accounts/**")
						.filters(f -> f.filters(filterFactory.apply())
								.rewritePath("/rafaelbank/accounts/(?<segment>.*)","/${segment}")
								.removeRequestHeader("Cookie"))
						.uri("lb://ACCOUNTS")).
				route(p -> p
						.path("/rafaelbank/loans/**")
						.filters(f -> f.filters(filterFactory.apply())
								.rewritePath("/rafaelbank/loans/(?<segment>.*)","/${segment}")
								.removeRequestHeader("Cookie"))
						.uri("lb://LOANS")).
				route(p -> p
						.path("/rafaelbank/cards/**")
						.filters(f -> f.filters(filterFactory.apply())
								.rewritePath("/rafaelbank/cards/(?<segment>.*)","/${segment}")
								.removeRequestHeader("Cookie"))
						.uri("lb://CARDS")).build();
	}*/

/*	@ConditionalOnClass({ContextSnapshot.class, Hooks.class})
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	@Bean
	public ApplicationListener<ContextRefreshedEvent> reactiveObservableHook() {
		return event -> Hooks.onEachOperator(
				ObservationContextSnapshotLifter.class.getSimpleName(),
				Operators.lift(ObservationContextSnapshotLifter.lifter()));
	}*/


}
