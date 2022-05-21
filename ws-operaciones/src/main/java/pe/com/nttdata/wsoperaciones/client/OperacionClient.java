package pe.com.nttdata.wsoperaciones.client;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import pe.com.nttdata.wsoperaciones.bean.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OperacionClient {
    private Builder accountWebClient = WebClient.builder();


    public Mono<Product> findByIdProduct(String id) {
        return accountWebClient
                .build()
                .get()
                .uri("http://localhost:8882/products/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }


    public Mono<Product> saveProduct(Product product) {
        return accountWebClient
                .build()
                .post()
                .uri("http://localhost:8882/products")
                .body(Mono.just(product), Product.class)
                .retrieve()
                .bodyToMono(Product.class);
    }

    public Mono<Historico> saveHistorico(Historico historico) {
        return accountWebClient
                .build()
                .post()
                .uri("http://localhost:8882/historicos")
                .body(Mono.just(historico), Historico.class)
                .retrieve()
                .bodyToMono(Historico.class);
    }

    public Mono<Customer> findByIdCustomer(String id) {
        return accountWebClient
                .build()
                .get()
                .uri("http://localhost:8882/customers/{id}", id)
                .retrieve()
                .bodyToMono(Customer.class);
    }

    public Flux<TypeProduct> findAllTypeProduct() {
        return accountWebClient
                .build()
                .get()
                .uri("http://localhost:8882/type-products")
                .retrieve()
                .bodyToFlux(TypeProduct.class);
    }

    public Flux<TypeCustomer> findByIdTypeCustomer(String idCustomer) {
        return accountWebClient
                .build()
                .get()
                .uri("http://localhost:8882/type-customers/buscar/{id}",idCustomer)
                .retrieve()
                .bodyToFlux(TypeCustomer.class);
    }

    public Flux<Product> findByIdCustomers(String id) {
        return accountWebClient
                .build()
                .get()
                .uri("http://localhost:8882/products/findByIdCustomers/{id}",id)
                .retrieve()
                .bodyToFlux(Product.class);
    }
}
